package fnt.view;

import fnt.repository.model.FsQdfPaymentInfo;
import fnt.repository.model.FsQdfPaymentItem;
import fnt.repository.model.FsQdfSysCtl;
import fnt.service.FsqdfSysCtlService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pub.platform.form.config.SystemAttributeNames;
import pub.platform.security.OperatorManager;
import skyline.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fnt.service.DepService;

/**
 * 缴款
 */

@ManagedBean
@ViewScoped
public class PaymentAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(PaymentAction.class);

    private String vchType;
    private String vchSn;
    private boolean isPayed = false;
    // 当为STATUS为2时需要手工录入
    private String voucherStatus;
    private boolean checkPassed = false;
    private boolean checkCancel = false;

    private FsQdfPaymentInfo paymentInfo = new FsQdfPaymentInfo();
    private List<FsQdfPaymentItem> paymentItemList = new ArrayList<FsQdfPaymentItem>();
    private List<SelectItem> payMethodOptions;
    private List<SelectItem> payBillOptions;
    private List<SelectItem> levyTypeOptions;       // 征收方式
    private List<SelectItem> entrustFlagOptions;    // 委托征收标志

    private FsQdfSysCtl sysCtl;

    @ManagedProperty(value = "#{fsqdfSysCtlService}")
    private FsqdfSysCtlService sysCtlService;
    @ManagedProperty(value = "#{depService}")
    private DepService depService;

    private FsQdfPaymentItem item1 = new FsQdfPaymentItem();
    private FsQdfPaymentItem item2 = new FsQdfPaymentItem();
    private FsQdfPaymentItem item3 = new FsQdfPaymentItem();
    private FsQdfPaymentItem item4 = new FsQdfPaymentItem();
    private FsQdfPaymentItem item5 = new FsQdfPaymentItem();
    private FsQdfPaymentItem item6 = new FsQdfPaymentItem();

    @PostConstruct
    public void init() {
        vchType = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("vchtyp");
        if (!StringUtils.isEmpty(vchType)) paymentInfo.setPjzl(vchType);
        item1.setXmsx("1");
        item2.setXmsx("2");
        item3.setXmsx("3");
        item4.setXmsx("4");
        item5.setXmsx("5");
        item6.setXmsx("6");
        sysCtl = sysCtlService.getFsQdfSysCtl("1");
        paymentInfo.setXzqh(sysCtl.getAreaCode());
        paymentInfo.setYhskrq(new SimpleDateFormat("yyyyMMdd").format(new Date()));

        payMethodOptions = new ArrayList<SelectItem>();
        payMethodOptions.add(new SelectItem("01", "现金"));
        payMethodOptions.add(new SelectItem("02", "转账"));

        levyTypeOptions = new ArrayList<SelectItem>();
        levyTypeOptions.add(new SelectItem("1", "直接缴库"));
        levyTypeOptions.add(new SelectItem("2", "集中汇缴"));

        entrustFlagOptions = new ArrayList<SelectItem>();
        entrustFlagOptions.add(new SelectItem("1", "委托"));
        entrustFlagOptions.add(new SelectItem("0", "非委托"));

        payBillOptions = new ArrayList<SelectItem>();
        payBillOptions.add(new SelectItem("01", "121标准《山东省非税收入缴款书》"));
        payBillOptions.add(new SelectItem("02", "139类定额《山东省非税收入缴款书》"));
        payBillOptions.add(new SelectItem("03", "154《山东省非税收入缴款书（罚没专用）》"));
        payBillOptions.add(new SelectItem("04", "125《山东省非税收入缴款书（接受捐赠专用）》"));
        payBillOptions.add(new SelectItem("05", "103《山东省非税收入缴款书（法院专用）》"));
        payBillOptions.add(new SelectItem("06", "107《山东省非税收入缴款书（排污费专用）》"));
        payBillOptions.add(new SelectItem("07", "109《山东省非税收入缴款书（新型墙体材料基金专用）》"));
    }

    public String onQuery() {
        try {
            paymentInfo = new FsQdfPaymentInfo();
            paymentInfo.setXzqh(sysCtl.getAreaCode());
            paymentInfo.setYhskrq(new SimpleDateFormat("yyyyMMdd").format(new Date()));
            paymentItemList.clear();
            String msg = depService.txn1533001(vchType, vchSn);
            assembleBeansByMsg(msg);
            if ("2".equals(voucherStatus)) {
                MessageUtil.addInfo("此票号未开票。");
            } else {
                if (isPayed) {
                    MessageUtil.addInfo("该票据已缴款。");
                    checkCancel = true;
                } else {
                    MessageUtil.addInfo("该票据未缴款。");
                    checkPassed = true;
                }
            }
        } catch (Exception e) {
            MessageUtil.addError("查询失败。" + (e.getMessage() == null ? "" : e.getMessage()));
            logger.error("查询失败.", e);
        }
        return null;
    }

    // 机打票记账
    public String onBookkeeping() {
        try {
            paymentInfo.setLxskbz("0");// 离线收款标志
            boolean isOk = depService.txn1533002(assembleStrByBeans(paymentInfo, paymentItemList));
            if (isOk) {
                MessageUtil.addInfo("缴款成功！");
                checkPassed = false;
                paymentInfo = new FsQdfPaymentInfo();
                paymentItemList.clear();
            }
        } catch (Exception e) {
            MessageUtil.addError("缴款失败。" + (e.getMessage() == null ? "" : e.getMessage()));
            logger.error("缴款失败.", e);
        }
        return null;
    }

    // 手工票记账
    public String onWBookkeeping() {
        try {
            String msg = depService.txn1533001(paymentInfo.getPjzl(), paymentInfo.getJksbh());
            String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(msg, "|");
            voucherStatus = fieldArray[38];
            if (!"2".equals(voucherStatus)) {
                MessageUtil.addWarn("此票号信息已补录，请作为机打票缴款。");
                return null;
            }
            paymentInfo.setSgpbz("1");   // 手工票标志
            paymentInfo.setLxskbz("0");   // 离线收款标志   0，	不是1，	是
            paymentInfo.setSkrmc(sysCtl.getCbsActnam());
            paymentInfo.setSkrzh(sysCtl.getCbsActno());
            paymentInfo.setSkrkhh(sysCtl.getBankName());
            paymentItemList.clear();
            if (!StringUtils.isEmpty(item1.getSrxmbm())) paymentItemList.add(item1);
            if (!StringUtils.isEmpty(item2.getSrxmbm())) paymentItemList.add(item2);
            if (!StringUtils.isEmpty(item3.getSrxmbm())) paymentItemList.add(item3);
            if (!StringUtils.isEmpty(item4.getSrxmbm())) paymentItemList.add(item4);
            if (!StringUtils.isEmpty(item5.getSrxmbm())) paymentItemList.add(item5);
            if (!StringUtils.isEmpty(item6.getSrxmbm())) paymentItemList.add(item6);
            if (paymentItemList.size() == 0) {
                MessageUtil.addError("至少输入一个缴费项目明细!");
                return null;
            }
            BigDecimal totalAmt = new BigDecimal("0.00");
            for (FsQdfPaymentItem item : paymentItemList) {
                totalAmt = totalAmt.add(item.getJe());
            }
            if (totalAmt.compareTo(paymentInfo.getZje()) != 0) {
                MessageUtil.addError("总分金额不符。");
                return null;
            }
            boolean isOk = depService.txn1533002(assembleStrByBeans(paymentInfo, paymentItemList));
            if (isOk) {
                MessageUtil.addInfo("缴款成功！");
                isPayed = true;
                paymentInfo = new FsQdfPaymentInfo();
                paymentInfo.setPjzl(vchType);
                paymentItemList.clear();
            }
        } catch (Exception e) {
            MessageUtil.addError("缴款失败。" + (e.getMessage() == null ? "" : e.getMessage()));
            logger.error("缴款失败.", e);
        }
        return null;
    }

    public String onCancel() {
        try {
            boolean isOk = depService.txn1533013(vchType, vchSn);
            if (isOk) {
                MessageUtil.addInfo("取消缴款成功！");
                checkCancel = false;
                paymentInfo = new FsQdfPaymentInfo();
                paymentItemList.clear();
            }
        } catch (Exception e) {
            MessageUtil.addError("取消缴款失败。" + (e.getMessage() == null ? "" : e.getMessage()));
            logger.error("取消缴款失败.", e);
        }
        return null;
    }

    // 查询委托单位信息
    public void onQryWtdw() {
        try {
            String[] rtnmsg = depService.txn1533008(paymentInfo.getXzqh(), paymentInfo.getWtzsdwbm());
            paymentInfo.setWtzsdwmc(rtnmsg[1]);
            paymentInfo.setWtdwzgbmbm(rtnmsg[2]);
            paymentInfo.setWtdwzgbmmc(rtnmsg[3]);
            if ("0".equals(paymentInfo.getWtzsbz())) {
                paymentInfo.setStzsdwbm(paymentInfo.getWtzsdwbm());
                paymentInfo.setStzsdwmc(rtnmsg[1]);
                paymentInfo.setStdwzgbmbm(rtnmsg[2]);
                paymentInfo.setStdwzgbmmc(rtnmsg[3]);
            }
        } catch (Exception e) {
            logger.error("查询委托执收单位信息失败。", e);
            MessageUtil.addError(e.getMessage() == null ? "查询委托执收单位信息失败，请检查输入项。" : e.getMessage());
        }
    }

    // 查询受托单位信息
    public void onQryStdw() {
        try {
            String[] rtnmsg = depService.txn1533008(paymentInfo.getXzqh(), paymentInfo.getStzsdwbm());
            paymentInfo.setStzsdwmc(rtnmsg[1]);
            paymentInfo.setStdwzgbmbm(rtnmsg[2]);
            paymentInfo.setStdwzgbmmc(rtnmsg[3]);
        } catch (Exception e) {
            logger.error("查询受托执收单位信息失败。", e);
            MessageUtil.addError(e.getMessage() == null ? "查询受托执收单位信息失败，请检查输入项。" : e.getMessage());
        }
    }

    public void onQryXmxx1() {
        try {
            String[] rtnmsg = depService.txn1533007(paymentInfo.getXzqh(), paymentInfo.getWtzsdwbm(), item1.getSrxmbm());
            item1.setSrxmmc(rtnmsg[1]);
            item1.setZjxzbm(rtnmsg[2]);
            item1.setZjxzmc(rtnmsg[3]);
            item1.setYskmbm(rtnmsg[4]);
            item1.setYskmmc(rtnmsg[5]);
            item1.setJldw(rtnmsg[6]);
            item1.setSjbz(rtnmsg[7]);
        } catch (Exception e) {
            logger.error("查询缴款项目信息失败。", e);
            MessageUtil.addError(e.getMessage() == null ? "查询缴款项目信息失败，请检查输入项。" : e.getMessage());
        }
    }

    public void onQryXmxx2() {
        try {
            String[] rtnmsg = depService.txn1533007(paymentInfo.getXzqh(), paymentInfo.getWtzsdwbm(), item2.getSrxmbm());
            item2.setSrxmmc(rtnmsg[1]);
            item2.setZjxzbm(rtnmsg[2]);
            item2.setZjxzmc(rtnmsg[3]);
            item2.setYskmbm(rtnmsg[4]);
            item2.setYskmmc(rtnmsg[5]);
            item2.setJldw(rtnmsg[6]);
            item2.setSjbz(rtnmsg[7]);
        } catch (Exception e) {
            logger.error("查询缴款项目信息失败。", e);
            MessageUtil.addError(e.getMessage() == null ? "查询缴款项目信息失败，请检查输入项。" : e.getMessage());
        }
    }

    public void onQryXmxx3() {
        try {
            String[] rtnmsg = depService.txn1533007(paymentInfo.getXzqh(), paymentInfo.getWtzsdwbm(), item3.getSrxmbm());
            item3.setSrxmmc(rtnmsg[1]);
            item3.setZjxzbm(rtnmsg[2]);
            item3.setZjxzmc(rtnmsg[3]);
            item3.setYskmbm(rtnmsg[4]);
            item3.setYskmmc(rtnmsg[5]);
            item3.setJldw(rtnmsg[6]);
            item3.setSjbz(rtnmsg[7]);
        } catch (Exception e) {
            logger.error("查询缴款项目信息失败。", e);
            MessageUtil.addError(e.getMessage() == null ? "查询缴款项目信息失败，请检查输入项。" : e.getMessage());
        }
    }

    public void onQryXmxx4() {
        try {
            String[] rtnmsg = depService.txn1533007(paymentInfo.getXzqh(), paymentInfo.getWtzsdwbm(), item4.getSrxmbm());
            item4.setSrxmmc(rtnmsg[1]);
            item4.setZjxzbm(rtnmsg[2]);
            item4.setZjxzmc(rtnmsg[3]);
            item4.setYskmbm(rtnmsg[4]);
            item4.setYskmmc(rtnmsg[5]);
            item4.setJldw(rtnmsg[6]);
            item4.setSjbz(rtnmsg[7]);
        } catch (Exception e) {
            logger.error("查询缴款项目信息失败。", e);
            MessageUtil.addError(e.getMessage() == null ? "查询缴款项目信息失败，请检查输入项。" : e.getMessage());
        }
    }

    public void onQryXmxx5() {
        try {
            String[] rtnmsg = depService.txn1533007(paymentInfo.getXzqh(), paymentInfo.getWtzsdwbm(), item5.getSrxmbm());
            item5.setSrxmmc(rtnmsg[1]);
            item5.setZjxzbm(rtnmsg[2]);
            item5.setZjxzmc(rtnmsg[3]);
            item5.setYskmbm(rtnmsg[4]);
            item5.setYskmmc(rtnmsg[5]);
            item5.setJldw(rtnmsg[6]);
            item5.setSjbz(rtnmsg[7]);
        } catch (Exception e) {
            logger.error("查询缴款项目信息失败。", e);
            MessageUtil.addError(e.getMessage() == null ? "查询缴款项目信息失败，请检查输入项。" : e.getMessage());
        }
    }

    public void onQryXmxx6() {
        try {
            String[] rtnmsg = depService.txn1533007(paymentInfo.getXzqh(), paymentInfo.getWtzsdwbm(), item6.getSrxmbm());
            item6.setSrxmmc(rtnmsg[1]);
            item6.setZjxzbm(rtnmsg[2]);
            item6.setZjxzmc(rtnmsg[3]);
            item6.setYskmbm(rtnmsg[4]);
            item6.setYskmmc(rtnmsg[5]);
            item6.setJldw(rtnmsg[6]);
            item6.setSjbz(rtnmsg[7]);
        } catch (Exception e) {
            logger.error("查询缴款项目信息失败。", e);
            MessageUtil.addError(e.getMessage() == null ? "查询缴款项目信息失败，请检查输入项。" : e.getMessage());
        }
    }

    // ------------------------
    private void assembleBeansByMsg(String msg) {
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(msg, "|");
        paymentInfo.setPjzl(fieldArray[0]);                       // 票据种类
        paymentInfo.setJksbh(fieldArray[1]);                      // 缴款书编号
        paymentInfo.setXzqh(fieldArray[2]);                       // 行政区划
        paymentInfo.setZsfs(fieldArray[3]);                       // 征收方式
        paymentInfo.setJkfs(fieldArray[4]);                       // 缴款方式
        paymentInfo.setTzrq(fieldArray[5]);                       // 填制日期
        paymentInfo.setWtzsdwbm(fieldArray[6]);                   // 委托执收单位编码
        paymentInfo.setWtzsdwmc(fieldArray[7]);                   // 委托执收单位名称
        paymentInfo.setWtdwzgbmbm(fieldArray[8]);                 // 委托单位主管部门编码
        paymentInfo.setWtdwzgbmmc(fieldArray[9]);                 // 委托单位主管部门名称
        paymentInfo.setWtzsbz(fieldArray[10]);                    // 委托征收标志
        paymentInfo.setStzsdwbm(fieldArray[11]);                  // 受托执收单位编码
        paymentInfo.setStzsdwmc(fieldArray[12]);                  // 受托执收单位名称
        paymentInfo.setStdwzgbmbm(fieldArray[13]);                // 受托单位主管部门编码
        paymentInfo.setStdwzgbmmc(fieldArray[14]);                // 受托单位主管部门名称
        paymentInfo.setZsdwzzjg(fieldArray[15]);                  // 执收单位组织机构
        paymentInfo.setFkrmc(fieldArray[16]);                     // 付款人名称
        paymentInfo.setFkrkhh(fieldArray[17]);                     // 付款人开户行
        paymentInfo.setFkrmc(fieldArray[18]);                     // 付款人账号
        paymentInfo.setSkrmc(fieldArray[19]);                      // 收款人名称
        paymentInfo.setSkrkhh(fieldArray[20]);                     // 收款人开户行
        paymentInfo.setSkrzh(fieldArray[21]);                      // 收款人账号
        if (!StringUtils.isEmpty(fieldArray[22])) {
            paymentInfo.setZje(new BigDecimal(fieldArray[22]));     // 总金额
        }
        paymentInfo.setBz(fieldArray[23]);
        paymentInfo.setSgpbz(fieldArray[24]);                       // 手工票标志
        paymentInfo.setFmyy(fieldArray[25]);                        // 罚没原因
        paymentInfo.setFmly(fieldArray[26]);                        // 罚没理由
        paymentInfo.setDsfy(fieldArray[27]);                        // 代收法院
        paymentInfo.setBgrmc(fieldArray[28]);                       // 被告人
        paymentInfo.setAy(fieldArray[29]);                          // 案由
        paymentInfo.setAh(fieldArray[30]);                          // 案号
        paymentInfo.setZdjh(fieldArray[31]);                        // 字第几号
        paymentInfo.setBde(fieldArray[32]);                         // 标的额
        paymentInfo.setXzspdtwym(fieldArray[33]);                   // 行政审批大厅唯一编码
        paymentInfo.setByzd1(fieldArray[34]);                       // 备用字段1
        paymentInfo.setByzd2(fieldArray[35]);                       // 备用字段2
        paymentInfo.setByzd3(fieldArray[36]);                       // 备用字段3


        isPayed = ("10".equals(fieldArray[37]) ? true : false);          // 是否已缴款
        voucherStatus = fieldArray[38];                                 // 当为STATUS为2时需要手工录入
        int cnt = Integer.parseInt(fieldArray[39]);                      // 明细数
        for (int i = 1; i <= cnt; i++) {
            String strItem = fieldArray[39 + i];
            String[] itemFields = StringUtils.splitByWholeSeparatorPreserveAllTokens(strItem, ",");
            FsQdfPaymentItem item = new FsQdfPaymentItem();
            item.setPjzl(vchType);
            item.setJksbh(vchSn);
            item.setXmsx(itemFields[0]);
            item.setSrxmbm(itemFields[1]);
            item.setSrxmmc(itemFields[2]);
            item.setZjxzbm(itemFields[3]);
            item.setZjxzmc(itemFields[4]);
            item.setYskmbm(itemFields[5]);
            item.setYskmmc(itemFields[6]);
            item.setSjbz(itemFields[7]);
            item.setCfjdsbh(itemFields[8]);
            item.setFzxmmc(itemFields[9]);
            item.setJnshenggk(StringUtils.isEmpty(itemFields[10]) ? null : new BigDecimal(itemFields[10]));
            item.setJnshigk(StringUtils.isEmpty(itemFields[11]) ? null : new BigDecimal(itemFields[11]));
            item.setJzmj(StringUtils.isEmpty(itemFields[12]) ? null : new BigDecimal(itemFields[12]));
            item.setJldw(itemFields[13]);
            item.setSl(itemFields[14] == null ? null : new BigDecimal(itemFields[14]));
            item.setJe(itemFields[15] == null ? null : new BigDecimal(itemFields[15]));
            item.setByzd1(itemFields[16]);
            item.setByzd2(itemFields[17]);
            item.setByzd3(itemFields[18]);
            paymentItemList.add(item);
        }
    }

    private String assembleStrByBeans(FsQdfPaymentInfo info, List<FsQdfPaymentItem> items) {
        StringBuilder strBuilder = new StringBuilder();
        OperatorManager om = getOperatorManager();
        strBuilder.append(info.getPjzl()).append("|")               // 票据种类
                .append(info.getJksbh()).append("|")                // 缴款书编号
                .append(nullToEmpty(info.getXzqh())).append("|")                 // 行政区划
                .append(nullToEmpty(info.getZsfs())).append("|")                 // 征收方式
                .append(nullToEmpty(info.getJkfs())).append("|")                 // 缴款方式
                .append(castToDate8(info.getTzrq())).append("|")                 // 填制日期
                .append(nullToEmpty(info.getWtzsdwbm())).append("|")             // 委托执收单位编码
                .append(nullToEmpty(info.getWtzsdwmc())).append("|")           // 委托执收单位名称
                .append(nullToEmpty(info.getWtzsbz())).append("|")               // 委托征收标志
                .append(nullToEmpty(info.getStzsdwbm())).append("|")           // 10 受托执收单位编码
                .append(nullToEmpty(info.getStzsdwmc())).append("|")             // 受托执收单位名称
                .append(nullToEmpty(info.getZsdwzzjg())).append("|")
                .append(nullToEmpty(info.getFkrmc())).append("|")                // 付款人名称
                .append(nullToEmpty(info.getFkrkhh())).append("|")                // 付款人开户行
                .append(nullToEmpty(info.getFkrzh())).append("|")                // 15付款人账号
                .append(nullToEmpty(info.getSkrmc())).append("|")                // 收款人名称
                .append(nullToEmpty(info.getSkrkhh())).append("|")               // 收款人开户行
                .append(nullToEmpty(info.getSkrzh())).append("|")                // 收款人账号
                .append(om.getOperator().getDeptid()).append("|")                // 银行网点
                .append(info.getZje().toString()).append("|")                    // 20 总金额
                .append(nullToEmpty(info.getBz())).append("|")
                .append(nullToEmpty(info.getJym())).append("|")                  // 校验码
                .append("0|")                                                   // 离线收款标志
                .append(nullToEmpty(info.getSgpbz())).append("|")                // 手工票标志
                .append(nullToEmpty(info.getYhskrq())).append("|")
                .append(nullToEmpty(info.getFmyy())).append("|")
                .append(nullToEmpty(info.getFmly())).append("|")
                .append(nullToEmpty(info.getDsfy())).append("|")
                .append(nullToEmpty(info.getBgrmc())).append("|")
                .append(nullToEmpty(info.getAy())).append("|")
                .append(nullToEmpty(info.getAh())).append("|")
                .append(nullToEmpty(info.getZdjh())).append("|")
                .append(nullToEmpty(info.getBde())).append("|")
                .append(nullToEmpty(info.getXzspdtwym())).append("|")
                .append(nullToEmpty(info.getBmkywxh())).append("|").append(nullToEmpty(info.getByzd1())).append("|")
                .append(nullToEmpty(info.getByzd2())).append("|")
                .append(nullToEmpty(info.getByzd3())).append("|");
       /* if ("1".equals(info.getHostBookFlag()) || "1".equals(info.getQdfBookFlag())) {
            strBuilder.append("10").append("|");
        } else {
            strBuilder.append("00").append("|");                     // 缴款标志
        }
        // status
        if (checkPassed) {
            strBuilder.append("0");
        } else {
            strBuilder.append("2");
        }*/
//        strBuilder.append("|")
        strBuilder.append(String.valueOf(items.size())).append("|");   // 记录数
        // 明细
        /*
        项目顺序,收入项目编码,收入项目名称,处罚决定书编码,缴纳省国库,缴纳市国库,数量,金额
         */
        for (FsQdfPaymentItem item : items) {
            strBuilder.append(item.getXmsx()).append(",")
                    .append(item.getSrxmbm()).append(",")
                    .append(item.getSrxmmc()).append(",")
                    .append(item.getSjbz()).append(",")
                    .append(StringUtils.isEmpty(item.getCfjdsbh()) ? "" : item.getCfjdsbh()).append(",")
                    .append(StringUtils.isEmpty(item.getFzxmmc()) ? "" : item.getFzxmmc()).append(",")
                    .append(item.getJnshenggk() == null ? "" : String.valueOf(item.getJnshenggk())).append(",")
                    .append(item.getJnshigk() == null ? "" : String.valueOf(item.getJnshigk())).append(",")
                    .append(item.getJzmj() == null ? "" : String.valueOf(item.getJzmj())).append(",")
                    .append(item.getJldw()).append(",")
                    .append(item.getSl() == null ? "" : String.valueOf(item.getSl())).append(",")
                    .append(item.getJe() == null ? "" : String.valueOf(item.getJe())).append(",")
                    .append(nullToEmpty(item.getByzd1())).append(",")
                    .append(nullToEmpty(item.getByzd2())).append(",")
                    .append(nullToEmpty(item.getByzd3())).append("|");
        }
        return strBuilder.toString();
    }

    private OperatorManager getOperatorManager() {
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) extContext.getSession(true);
        OperatorManager om = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        if (om == null) {
            throw new RuntimeException("用户未登录！");
        }
        return om;
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    private String castToDate8(String srcDate) {
        if (srcDate == null) {
            return "        ";
        } else if (srcDate.length() > 8) {
            return srcDate.substring(0, 4) + srcDate.substring(5, 7) + srcDate.substring(8, 10);
        } else {
            return srcDate;
        }
    }

    // ==========================

    public String getVchType() {
        return vchType;
    }

    public void setVchType(String vchType) {
        this.vchType = vchType;
    }

    public String getVchSn() {
        return vchSn;
    }

    public void setVchSn(String vchSn) {
        this.vchSn = vchSn;
    }

    public boolean isCheckPassed() {
        return checkPassed;
    }

    public void setCheckPassed(boolean checkPassed) {
        this.checkPassed = checkPassed;
    }

    public FsQdfPaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(FsQdfPaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public List<FsQdfPaymentItem> getPaymentItemList() {
        return paymentItemList;
    }

    public void setPaymentItemList(List<FsQdfPaymentItem> paymentItemList) {
        this.paymentItemList = paymentItemList;
    }

    public DepService getDepService() {
        return depService;
    }

    public void setDepService(DepService depService) {
        this.depService = depService;
    }

    public boolean isPayed() {
        return isPayed;
    }

    public void setPayed(boolean payed) {
        isPayed = payed;
    }

    public String getVoucherStatus() {
        return voucherStatus;
    }

    public void setVoucherStatus(String voucherStatus) {
        this.voucherStatus = voucherStatus;
    }

    public boolean isCheckCancel() {
        return checkCancel;
    }

    public void setCheckCancel(boolean checkCancel) {
        this.checkCancel = checkCancel;
    }

    public List<SelectItem> getPayMethodOptions() {
        return payMethodOptions;
    }

    public void setPayMethodOptions(List<SelectItem> payMethodOptions) {
        this.payMethodOptions = payMethodOptions;
    }

    public List<SelectItem> getPayBillOptions() {
        return payBillOptions;
    }

    public void setPayBillOptions(List<SelectItem> payBillOptions) {
        this.payBillOptions = payBillOptions;
    }

    public FsQdfSysCtl getSysCtl() {
        return sysCtl;
    }

    public void setSysCtl(FsQdfSysCtl sysCtl) {
        this.sysCtl = sysCtl;
    }

    public FsqdfSysCtlService getSysCtlService() {
        return sysCtlService;
    }

    public void setSysCtlService(FsqdfSysCtlService sysCtlService) {
        this.sysCtlService = sysCtlService;
    }

    public List<SelectItem> getLevyTypeOptions() {
        return levyTypeOptions;
    }

    public void setLevyTypeOptions(List<SelectItem> levyTypeOptions) {
        this.levyTypeOptions = levyTypeOptions;
    }

    public List<SelectItem> getEntrustFlagOptions() {
        return entrustFlagOptions;
    }

    public void setEntrustFlagOptions(List<SelectItem> entrustFlagOptions) {
        this.entrustFlagOptions = entrustFlagOptions;
    }

    public FsQdfPaymentItem getItem1() {
        return item1;
    }

    public void setItem1(FsQdfPaymentItem item1) {
        this.item1 = item1;
    }

    public FsQdfPaymentItem getItem2() {
        return item2;
    }

    public void setItem2(FsQdfPaymentItem item2) {
        this.item2 = item2;
    }

    public FsQdfPaymentItem getItem3() {
        return item3;
    }

    public void setItem3(FsQdfPaymentItem item3) {
        this.item3 = item3;
    }

    public FsQdfPaymentItem getItem4() {
        return item4;
    }

    public void setItem4(FsQdfPaymentItem item4) {
        this.item4 = item4;
    }

    public FsQdfPaymentItem getItem5() {
        return item5;
    }

    public void setItem5(FsQdfPaymentItem item5) {
        this.item5 = item5;
    }

    public FsQdfPaymentItem getItem6() {
        return item6;
    }

    public void setItem6(FsQdfPaymentItem item6) {
        this.item6 = item6;
    }
}
