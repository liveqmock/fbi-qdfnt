package fnt.view;

import fnt.common.enums.PendingVchFlag;
import fnt.common.utils.JxlsManager;
import fnt.repository.model.FsQdfPaymentInfo;
import fnt.repository.model.FsQdfPendingTxn;
import fnt.repository.model.FsQdfPendingVchInfo;
import fnt.repository.model.FsQdfSysCtl;
import fnt.service.CommonService;
import fnt.service.FsqdfPendingTxnService;
import fnt.service.FsqdfSysCtlService;
import fnt.service.PaymentQryService;
import fnt.view.pojo.PendingTxnRptPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 不明款日报
 */
@ManagedBean
@ViewScoped
public class PendingTxnRptAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(PaymentAction.class);

    private String date8;
    @ManagedProperty(value = "#{fsqdfSysCtlService}")
    private FsqdfSysCtlService sysCtlService;
    @ManagedProperty(value = "#{fsqdfPendingTxnService}")
    private FsqdfPendingTxnService pendingTxnService;
    @ManagedProperty(value = "#{paymentQryService}")
    private PaymentQryService paymentQryService;
    @ManagedProperty(value = "#{commonService}")
    private CommonService commonService;
    private FsQdfSysCtl sysCtl;
    private String totalAddAmt = "0.00";
    private String totalCfmAmt = "0.00";
    private String totalChkAmt = "0.00";
    private DecimalFormat df = new DecimalFormat("###,##0.00");
    private List<PendingTxnRptPojo> txnList = new ArrayList<PendingTxnRptPojo>();

    @PostConstruct
    public void init() {
        sysCtl = sysCtlService.getFsQdfSysCtl("1");
        date8 = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public String onQryPendingTxnRpt() {
        try {
            if (date8.compareTo(sysCtl.getTxnDate()) >= 0) {
                MessageUtil.addWarn(date8 + "该日期未对账，请先对账。");
                return null;
            }
            txnList.clear();
            BigDecimal bdTotalAddAmt = new BigDecimal("0.00");
            BigDecimal bdTotalCfmAmt = new BigDecimal("0.00");
            BigDecimal bdTotalChkAmt = new BigDecimal("0.00");
            // 1- date8添加的不明款
            List<FsQdfPendingTxn> pendingTxnList = pendingTxnService.qryPendingTxnsByDate(date8);
            for (FsQdfPendingTxn record : pendingTxnList) {
                PendingTxnRptPojo pojo = new PendingTxnRptPojo();
                pojo.setBmkywxh(record.getBmkywxh());
                pojo.setAddAmt(df.format(record.getJe()));
                bdTotalAddAmt = bdTotalAddAmt.add(record.getJe());
                // 转待补票收入
                FsQdfPendingVchInfo vch = pendingTxnService.qryPendingVchInfoByBmkywxh(record.getBmkywxh());
                if (vch != null && (PendingVchFlag.CONFIRM_RIGHT.getCode().equals(vch.getQdfCfmFlag())
                        || PendingVchFlag.PENDED.getCode().equals(vch.getQdfCfmFlag()))) {
                    pojo.setAddConfirmAmt(df.format(record.getJe()));
                    bdTotalCfmAmt = bdTotalCfmAmt.add(record.getJe());
                }
                if (vch == null) {
                    FsQdfPaymentInfo info = paymentQryService.qryBmkPaymentInfo(record.getBmkywxh());
                    if (info != null) {
                        pojo.setSubAmt(df.format(record.getJe()));
                        bdTotalChkAmt = bdTotalChkAmt.add(record.getJe());
                    }
                }
                pojo.setRemark(record.getYt());
                txnList.add(pojo);
            }
            // 2-date8 转待补票收入
            List<FsQdfPendingVchInfo> vchList = pendingTxnService.qryCfmChkPendingVchInfosByDate(date8);
            for (FsQdfPendingVchInfo record : vchList) {
                PendingTxnRptPojo pojo = new PendingTxnRptPojo();
                pojo.setBmkywxh(record.getBmkywxh());
                if (date8.equals(record.getConfirmDate())) {
                    pojo.setAddConfirmAmt(df.format(record.getJe()));
                    bdTotalCfmAmt = bdTotalCfmAmt.add(record.getJe());
                }
                if (date8.equals(record.getChkDate())) {
                    pojo.setSubAmt(df.format(record.getJe()));
                    bdTotalChkAmt = bdTotalChkAmt.add(record.getJe());
                }
                txnList.add(pojo);
            }
            // 3-date8 不明款转收入
            List<FsQdfPendingTxn> chkedTxns = commonService.qryChkedFormerPendingTxns(date8);
            for (FsQdfPendingTxn record : chkedTxns) {
                PendingTxnRptPojo pojo = new PendingTxnRptPojo();
                pojo.setBmkywxh(record.getBmkywxh());
                pojo.setSubAmt(df.format(record.getJe()));
                bdTotalChkAmt = bdTotalChkAmt.add(record.getJe());
                txnList.add(pojo);
            }
            if(txnList.isEmpty()) {
                MessageUtil.addWarn(date8 + "没有不明款数据。");
            }
            totalAddAmt = df.format(bdTotalAddAmt);
            totalCfmAmt = df.format(bdTotalCfmAmt);
            totalChkAmt = df.format(bdTotalChkAmt);
        } catch (Exception e) {
            logger.error("查询不明款日报数据失败。", e);
            MessageUtil.addError("查询不明款日报数据失败。" + (e.getMessage() == null ? "" : e.getMessage()));
        }
        return null;
    }


    public String onGenPendingTxnRpt() {
        try {
            if (date8.compareTo(sysCtl.getTxnDate()) >= 0) {
                MessageUtil.addWarn(date8 + "该日期未对账，无法生成报表。");
                return null;
            }
            if (txnList.size() == 0) {
                MessageUtil.addWarn("请先查询不明款明细。");
            } else {
                String excelFilename = "青岛市财政非税不明款日报表-" + date8 + ".xls";
                JxlsManager jxls = new JxlsManager();
                Map beansMap = new HashMap();
                beansMap.put("records", txnList);
                beansMap.put("actno", sysCtl.getCbsActno());
                beansMap.put("actnam", sysCtl.getCbsActnam());
                beansMap.put("rptdate", date8);
                beansMap.put("bankName", sysCtl.getBankName());
                beansMap.put("totalAddAmt", totalAddAmt);
                beansMap.put("totalCfmAmt", totalCfmAmt);
                beansMap.put("totalChkAmt", totalChkAmt);
                jxls.exportDataToXls(excelFilename, "/report/PendingTxnRpt.xls", beansMap);
            }
        } catch (Exception e) {
            logger.error("生成不明款日报失败。", e);
            MessageUtil.addError("生成不明款日报失败。" + (e.getMessage() == null ? "" : e.getMessage()));
        }
        return null;
    }

    // -------------------------------------

    public String getDate8() {
        return date8;
    }

    public void setDate8(String date8) {
        this.date8 = date8;
    }

    public FsqdfSysCtlService getSysCtlService() {
        return sysCtlService;
    }

    public void setSysCtlService(FsqdfSysCtlService sysCtlService) {
        this.sysCtlService = sysCtlService;
    }

    public FsQdfSysCtl getSysCtl() {
        return sysCtl;
    }

    public void setSysCtl(FsQdfSysCtl sysCtl) {
        this.sysCtl = sysCtl;
    }

    public String getTotalAddAmt() {
        return totalAddAmt;
    }

    public void setTotalAddAmt(String totalAddAmt) {
        this.totalAddAmt = totalAddAmt;
    }

    public String getTotalCfmAmt() {
        return totalCfmAmt;
    }

    public void setTotalCfmAmt(String totalCfmAmt) {
        this.totalCfmAmt = totalCfmAmt;
    }

    public String getTotalChkAmt() {
        return totalChkAmt;
    }

    public void setTotalChkAmt(String totalChkAmt) {
        this.totalChkAmt = totalChkAmt;
    }

    public DecimalFormat getDf() {
        return df;
    }

    public void setDf(DecimalFormat df) {
        this.df = df;
    }

    public List<PendingTxnRptPojo> getTxnList() {
        return txnList;
    }

    public void setTxnList(List<PendingTxnRptPojo> txnList) {
        this.txnList = txnList;
    }

    public FsqdfPendingTxnService getPendingTxnService() {
        return pendingTxnService;
    }

    public void setPendingTxnService(FsqdfPendingTxnService pendingTxnService) {
        this.pendingTxnService = pendingTxnService;
    }

    public PaymentQryService getPaymentQryService() {
        return paymentQryService;
    }

    public void setPaymentQryService(PaymentQryService paymentQryService) {
        this.paymentQryService = paymentQryService;
    }

    public CommonService getCommonService() {
        return commonService;
    }

    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }
}
