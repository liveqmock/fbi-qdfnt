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
 * �ɿ�
 */

@ManagedBean
@ViewScoped
public class PaymentAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(PaymentAction.class);

    private String vchType;
    private String vchSn;
    private boolean isPayed = false;
    // ��ΪSTATUSΪ2ʱ��Ҫ�ֹ�¼��
    private String voucherStatus;
    private boolean checkPassed = false;
    private boolean checkCancel = false;

    private FsQdfPaymentInfo paymentInfo = new FsQdfPaymentInfo();
    private List<FsQdfPaymentItem> paymentItemList = new ArrayList<FsQdfPaymentItem>();
    private List<SelectItem> payMethodOptions;
    private List<SelectItem> payBillOptions;
    private List<SelectItem> levyTypeOptions;       // ���շ�ʽ
    private List<SelectItem> entrustFlagOptions;    // ί�����ձ�־

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
        payMethodOptions.add(new SelectItem("01", "�ֽ�"));
        payMethodOptions.add(new SelectItem("02", "ת��"));

        levyTypeOptions = new ArrayList<SelectItem>();
        levyTypeOptions.add(new SelectItem("1", "ֱ�ӽɿ�"));
        levyTypeOptions.add(new SelectItem("2", "���л��"));

        entrustFlagOptions = new ArrayList<SelectItem>();
        entrustFlagOptions.add(new SelectItem("1", "ί��"));
        entrustFlagOptions.add(new SelectItem("0", "��ί��"));

        payBillOptions = new ArrayList<SelectItem>();
        payBillOptions.add(new SelectItem("01", "121��׼��ɽ��ʡ��˰����ɿ��顷"));
        payBillOptions.add(new SelectItem("02", "139�ඨ�ɽ��ʡ��˰����ɿ��顷"));
        payBillOptions.add(new SelectItem("03", "154��ɽ��ʡ��˰����ɿ��飨��ûר�ã���"));
        payBillOptions.add(new SelectItem("04", "125��ɽ��ʡ��˰����ɿ��飨���ܾ���ר�ã���"));
        payBillOptions.add(new SelectItem("05", "103��ɽ��ʡ��˰����ɿ��飨��Ժר�ã���"));
        payBillOptions.add(new SelectItem("06", "107��ɽ��ʡ��˰����ɿ��飨���۷�ר�ã���"));
        payBillOptions.add(new SelectItem("07", "109��ɽ��ʡ��˰����ɿ��飨����ǽ����ϻ���ר�ã���"));
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
                MessageUtil.addInfo("��Ʊ��δ��Ʊ��");
            } else {
                if (isPayed) {
                    MessageUtil.addInfo("��Ʊ���ѽɿ");
                    checkCancel = true;
                } else {
                    MessageUtil.addInfo("��Ʊ��δ�ɿ");
                    checkPassed = true;
                }
            }
        } catch (Exception e) {
            MessageUtil.addError("��ѯʧ�ܡ�" + (e.getMessage() == null ? "" : e.getMessage()));
            logger.error("��ѯʧ��.", e);
        }
        return null;
    }

    // ����Ʊ����
    public String onBookkeeping() {
        try {
            paymentInfo.setLxskbz("0");// �����տ��־
            boolean isOk = depService.txn1533002(assembleStrByBeans(paymentInfo, paymentItemList));
            if (isOk) {
                MessageUtil.addInfo("�ɿ�ɹ���");
                checkPassed = false;
                paymentInfo = new FsQdfPaymentInfo();
                paymentItemList.clear();
            }
        } catch (Exception e) {
            MessageUtil.addError("�ɿ�ʧ�ܡ�" + (e.getMessage() == null ? "" : e.getMessage()));
            logger.error("�ɿ�ʧ��.", e);
        }
        return null;
    }

    // �ֹ�Ʊ����
    public String onWBookkeeping() {
        try {
            String msg = depService.txn1533001(paymentInfo.getPjzl(), paymentInfo.getJksbh());
            String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(msg, "|");
            voucherStatus = fieldArray[38];
            if (!"2".equals(voucherStatus)) {
                MessageUtil.addWarn("��Ʊ����Ϣ�Ѳ�¼������Ϊ����Ʊ�ɿ");
                return null;
            }
            paymentInfo.setSgpbz("1");   // �ֹ�Ʊ��־
            paymentInfo.setLxskbz("0");   // �����տ��־   0��	����1��	��
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
                MessageUtil.addError("��������һ���ɷ���Ŀ��ϸ!");
                return null;
            }
            BigDecimal totalAmt = new BigDecimal("0.00");
            for (FsQdfPaymentItem item : paymentItemList) {
                totalAmt = totalAmt.add(item.getJe());
            }
            if (totalAmt.compareTo(paymentInfo.getZje()) != 0) {
                MessageUtil.addError("�ֽܷ�����");
                return null;
            }
            boolean isOk = depService.txn1533002(assembleStrByBeans(paymentInfo, paymentItemList));
            if (isOk) {
                MessageUtil.addInfo("�ɿ�ɹ���");
                isPayed = true;
                paymentInfo = new FsQdfPaymentInfo();
                paymentInfo.setPjzl(vchType);
                paymentItemList.clear();
            }
        } catch (Exception e) {
            MessageUtil.addError("�ɿ�ʧ�ܡ�" + (e.getMessage() == null ? "" : e.getMessage()));
            logger.error("�ɿ�ʧ��.", e);
        }
        return null;
    }

    public String onCancel() {
        try {
            boolean isOk = depService.txn1533013(vchType, vchSn);
            if (isOk) {
                MessageUtil.addInfo("ȡ���ɿ�ɹ���");
                checkCancel = false;
                paymentInfo = new FsQdfPaymentInfo();
                paymentItemList.clear();
            }
        } catch (Exception e) {
            MessageUtil.addError("ȡ���ɿ�ʧ�ܡ�" + (e.getMessage() == null ? "" : e.getMessage()));
            logger.error("ȡ���ɿ�ʧ��.", e);
        }
        return null;
    }

    // ��ѯί�е�λ��Ϣ
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
            logger.error("��ѯί��ִ�յ�λ��Ϣʧ�ܡ�", e);
            MessageUtil.addError(e.getMessage() == null ? "��ѯί��ִ�յ�λ��Ϣʧ�ܣ����������" : e.getMessage());
        }
    }

    // ��ѯ���е�λ��Ϣ
    public void onQryStdw() {
        try {
            String[] rtnmsg = depService.txn1533008(paymentInfo.getXzqh(), paymentInfo.getStzsdwbm());
            paymentInfo.setStzsdwmc(rtnmsg[1]);
            paymentInfo.setStdwzgbmbm(rtnmsg[2]);
            paymentInfo.setStdwzgbmmc(rtnmsg[3]);
        } catch (Exception e) {
            logger.error("��ѯ����ִ�յ�λ��Ϣʧ�ܡ�", e);
            MessageUtil.addError(e.getMessage() == null ? "��ѯ����ִ�յ�λ��Ϣʧ�ܣ����������" : e.getMessage());
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
            logger.error("��ѯ�ɿ���Ŀ��Ϣʧ�ܡ�", e);
            MessageUtil.addError(e.getMessage() == null ? "��ѯ�ɿ���Ŀ��Ϣʧ�ܣ����������" : e.getMessage());
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
            logger.error("��ѯ�ɿ���Ŀ��Ϣʧ�ܡ�", e);
            MessageUtil.addError(e.getMessage() == null ? "��ѯ�ɿ���Ŀ��Ϣʧ�ܣ����������" : e.getMessage());
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
            logger.error("��ѯ�ɿ���Ŀ��Ϣʧ�ܡ�", e);
            MessageUtil.addError(e.getMessage() == null ? "��ѯ�ɿ���Ŀ��Ϣʧ�ܣ����������" : e.getMessage());
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
            logger.error("��ѯ�ɿ���Ŀ��Ϣʧ�ܡ�", e);
            MessageUtil.addError(e.getMessage() == null ? "��ѯ�ɿ���Ŀ��Ϣʧ�ܣ����������" : e.getMessage());
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
            logger.error("��ѯ�ɿ���Ŀ��Ϣʧ�ܡ�", e);
            MessageUtil.addError(e.getMessage() == null ? "��ѯ�ɿ���Ŀ��Ϣʧ�ܣ����������" : e.getMessage());
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
            logger.error("��ѯ�ɿ���Ŀ��Ϣʧ�ܡ�", e);
            MessageUtil.addError(e.getMessage() == null ? "��ѯ�ɿ���Ŀ��Ϣʧ�ܣ����������" : e.getMessage());
        }
    }

    // ------------------------
    private void assembleBeansByMsg(String msg) {
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(msg, "|");
        paymentInfo.setPjzl(fieldArray[0]);                       // Ʊ������
        paymentInfo.setJksbh(fieldArray[1]);                      // �ɿ�����
        paymentInfo.setXzqh(fieldArray[2]);                       // ��������
        paymentInfo.setZsfs(fieldArray[3]);                       // ���շ�ʽ
        paymentInfo.setJkfs(fieldArray[4]);                       // �ɿʽ
        paymentInfo.setTzrq(fieldArray[5]);                       // ��������
        paymentInfo.setWtzsdwbm(fieldArray[6]);                   // ί��ִ�յ�λ����
        paymentInfo.setWtzsdwmc(fieldArray[7]);                   // ί��ִ�յ�λ����
        paymentInfo.setWtdwzgbmbm(fieldArray[8]);                 // ί�е�λ���ܲ��ű���
        paymentInfo.setWtdwzgbmmc(fieldArray[9]);                 // ί�е�λ���ܲ�������
        paymentInfo.setWtzsbz(fieldArray[10]);                    // ί�����ձ�־
        paymentInfo.setStzsdwbm(fieldArray[11]);                  // ����ִ�յ�λ����
        paymentInfo.setStzsdwmc(fieldArray[12]);                  // ����ִ�յ�λ����
        paymentInfo.setStdwzgbmbm(fieldArray[13]);                // ���е�λ���ܲ��ű���
        paymentInfo.setStdwzgbmmc(fieldArray[14]);                // ���е�λ���ܲ�������
        paymentInfo.setZsdwzzjg(fieldArray[15]);                  // ִ�յ�λ��֯����
        paymentInfo.setFkrmc(fieldArray[16]);                     // ����������
        paymentInfo.setFkrkhh(fieldArray[17]);                     // �����˿�����
        paymentInfo.setFkrmc(fieldArray[18]);                     // �������˺�
        paymentInfo.setSkrmc(fieldArray[19]);                      // �տ�������
        paymentInfo.setSkrkhh(fieldArray[20]);                     // �տ��˿�����
        paymentInfo.setSkrzh(fieldArray[21]);                      // �տ����˺�
        if (!StringUtils.isEmpty(fieldArray[22])) {
            paymentInfo.setZje(new BigDecimal(fieldArray[22]));     // �ܽ��
        }
        paymentInfo.setBz(fieldArray[23]);
        paymentInfo.setSgpbz(fieldArray[24]);                       // �ֹ�Ʊ��־
        paymentInfo.setFmyy(fieldArray[25]);                        // ��ûԭ��
        paymentInfo.setFmly(fieldArray[26]);                        // ��û����
        paymentInfo.setDsfy(fieldArray[27]);                        // ���շ�Ժ
        paymentInfo.setBgrmc(fieldArray[28]);                       // ������
        paymentInfo.setAy(fieldArray[29]);                          // ����
        paymentInfo.setAh(fieldArray[30]);                          // ����
        paymentInfo.setZdjh(fieldArray[31]);                        // �ֵڼ���
        paymentInfo.setBde(fieldArray[32]);                         // ��Ķ�
        paymentInfo.setXzspdtwym(fieldArray[33]);                   // ������������Ψһ����
        paymentInfo.setByzd1(fieldArray[34]);                       // �����ֶ�1
        paymentInfo.setByzd2(fieldArray[35]);                       // �����ֶ�2
        paymentInfo.setByzd3(fieldArray[36]);                       // �����ֶ�3


        isPayed = ("10".equals(fieldArray[37]) ? true : false);          // �Ƿ��ѽɿ�
        voucherStatus = fieldArray[38];                                 // ��ΪSTATUSΪ2ʱ��Ҫ�ֹ�¼��
        int cnt = Integer.parseInt(fieldArray[39]);                      // ��ϸ��
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
        strBuilder.append(info.getPjzl()).append("|")               // Ʊ������
                .append(info.getJksbh()).append("|")                // �ɿ�����
                .append(nullToEmpty(info.getXzqh())).append("|")                 // ��������
                .append(nullToEmpty(info.getZsfs())).append("|")                 // ���շ�ʽ
                .append(nullToEmpty(info.getJkfs())).append("|")                 // �ɿʽ
                .append(castToDate8(info.getTzrq())).append("|")                 // ��������
                .append(nullToEmpty(info.getWtzsdwbm())).append("|")             // ί��ִ�յ�λ����
                .append(nullToEmpty(info.getWtzsdwmc())).append("|")           // ί��ִ�յ�λ����
                .append(nullToEmpty(info.getWtzsbz())).append("|")               // ί�����ձ�־
                .append(nullToEmpty(info.getStzsdwbm())).append("|")           // 10 ����ִ�յ�λ����
                .append(nullToEmpty(info.getStzsdwmc())).append("|")             // ����ִ�յ�λ����
                .append(nullToEmpty(info.getZsdwzzjg())).append("|")
                .append(nullToEmpty(info.getFkrmc())).append("|")                // ����������
                .append(nullToEmpty(info.getFkrkhh())).append("|")                // �����˿�����
                .append(nullToEmpty(info.getFkrzh())).append("|")                // 15�������˺�
                .append(nullToEmpty(info.getSkrmc())).append("|")                // �տ�������
                .append(nullToEmpty(info.getSkrkhh())).append("|")               // �տ��˿�����
                .append(nullToEmpty(info.getSkrzh())).append("|")                // �տ����˺�
                .append(om.getOperator().getDeptid()).append("|")                // ��������
                .append(info.getZje().toString()).append("|")                    // 20 �ܽ��
                .append(nullToEmpty(info.getBz())).append("|")
                .append(nullToEmpty(info.getJym())).append("|")                  // У����
                .append("0|")                                                   // �����տ��־
                .append(nullToEmpty(info.getSgpbz())).append("|")                // �ֹ�Ʊ��־
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
            strBuilder.append("00").append("|");                     // �ɿ��־
        }
        // status
        if (checkPassed) {
            strBuilder.append("0");
        } else {
            strBuilder.append("2");
        }*/
//        strBuilder.append("|")
        strBuilder.append(String.valueOf(items.size())).append("|");   // ��¼��
        // ��ϸ
        /*
        ��Ŀ˳��,������Ŀ����,������Ŀ����,�������������,����ʡ����,�����й���,����,���
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
            throw new RuntimeException("�û�δ��¼��");
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
