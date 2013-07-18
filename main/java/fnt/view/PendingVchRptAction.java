package fnt.view;

import fnt.common.utils.JxlsManager;
import fnt.common.utils.ObjectFieldsCopier;
import fnt.repository.model.FsQdfPendingVchInfo;
import fnt.repository.model.FsQdfSysCtl;
import fnt.service.FsqdfPendingTxnService;
import fnt.service.FsqdfSysCtlService;
import fnt.view.pojo.PendingVchRptPojo;
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
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-6-27
 * Time: 下午4:40
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class PendingVchRptAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(PaymentAction.class);

    private String date8;
    @ManagedProperty(value = "#{fsqdfSysCtlService}")
    private FsqdfSysCtlService sysCtlService;
    @ManagedProperty(value = "#{fsqdfPendingTxnService}")
    private FsqdfPendingTxnService pendingTxnService;

    private FsQdfSysCtl sysCtl;
    private List<FsQdfPendingVchInfo> pendingVchInfoList;
    private List<PendingVchRptPojo> pendingVchRptPojoList = new ArrayList<PendingVchRptPojo>();
    private String totalAddAmt = "0.00";
    private String totalCfmAmt = "0.00";
    private String totalChkAmt = "0.00";
    private DecimalFormat df = new DecimalFormat("###,##0.00");

    @PostConstruct
    public void init() {
        sysCtl = sysCtlService.getFsQdfSysCtl("1");
        date8 = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public String onQryPendingVchRpt() {
        try {
            if (date8.compareTo(sysCtl.getTxnDate()) >= 0) {
                MessageUtil.addWarn(date8 + "该日期未对账，请先对账。");
                return null;
            }
            pendingVchInfoList = pendingTxnService.qryPendingVchInfosByDate(date8);
            if (pendingVchInfoList.size() == 0) {
                MessageUtil.addWarn("没有查询到数据。");
                return null;
            } else {
                BigDecimal bdTotalAddAmt = new BigDecimal("0.00");
                BigDecimal bdTotalCfmAmt = new BigDecimal("0.00");
                BigDecimal bdTotalChkAmt = new BigDecimal("0.00");
                pendingVchRptPojoList.clear();
                for (FsQdfPendingVchInfo vch : pendingVchInfoList) {
                    PendingVchRptPojo pojo = new PendingVchRptPojo();
                    ObjectFieldsCopier.copyFields(vch, pojo);
                    if (date8.equals(vch.getAddDate())) {
                        pojo.setAddAmt(df.format(vch.getJe()));
                        bdTotalAddAmt = bdTotalAddAmt.add(vch.getJe());
                    }
                    if (date8.equals(vch.getConfirmDate())) {
                        pojo.setAddConfirmAmt(df.format(vch.getJe()));
                        bdTotalCfmAmt = bdTotalCfmAmt.add(vch.getJe());
                    }
                    if (date8.equals(vch.getChkDate())) {
                        pojo.setSubAmt(df.format(vch.getJe()));
                        bdTotalChkAmt = bdTotalChkAmt.add(vch.getJe());
                    }
                    pendingVchRptPojoList.add(pojo);
                }
                totalAddAmt = df.format(bdTotalAddAmt);
                totalCfmAmt = df.format(bdTotalCfmAmt);
                totalChkAmt = df.format(bdTotalChkAmt);
            }
        } catch (Exception e) {
            logger.error("查询待补票日报数据失败。", e);
            MessageUtil.addError("查询待补票日报数据失败。" + (e.getMessage() == null ? "" : e.getMessage()));
        }
        return null;
    }


    public String onGenPendingVchRpt() {
        try {
            if (date8.compareTo(sysCtl.getTxnDate()) >= 0) {
                MessageUtil.addWarn(date8 + "该日期未对账，无法生成报表。");
                return null;
            }
            if (pendingVchRptPojoList == null || pendingVchRptPojoList.size() == 0) {
                MessageUtil.addWarn("请先查询日报数据。");
                return null;
            } else {
                String excelFilename = "青岛市财政非税待补票日报表-" + date8 + ".xls";
                JxlsManager jxls = new JxlsManager();
                Map beansMap = new HashMap();
                beansMap.put("records", pendingVchRptPojoList);
                beansMap.put("actno", sysCtl.getCbsActno());
                beansMap.put("actnam", sysCtl.getCbsActnam());
                beansMap.put("rptdate", date8);
                beansMap.put("bankName", sysCtl.getBankName());
                beansMap.put("totalAddAmt", totalAddAmt);
                beansMap.put("totalCfmAmt", totalCfmAmt);
                beansMap.put("totalChkAmt", totalChkAmt);
                jxls.exportDataToXls(excelFilename, "/report/PendingVchInfoList.xls", beansMap);
            }

        } catch (Exception e) {
            logger.error("生成待补票日报失败。", e);
            MessageUtil.addError("生成待补票日报失败。" + (e.getMessage() == null ? "" : e.getMessage()));
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

    public FsqdfPendingTxnService getPendingTxnService() {
        return pendingTxnService;
    }

    public void setPendingTxnService(FsqdfPendingTxnService pendingTxnService) {
        this.pendingTxnService = pendingTxnService;
    }

    public FsQdfSysCtl getSysCtl() {
        return sysCtl;
    }

    public void setSysCtl(FsQdfSysCtl sysCtl) {
        this.sysCtl = sysCtl;
    }

    public List<FsQdfPendingVchInfo> getPendingVchInfoList() {
        return pendingVchInfoList;
    }

    public void setPendingVchInfoList(List<FsQdfPendingVchInfo> pendingVchInfoList) {
        this.pendingVchInfoList = pendingVchInfoList;
    }

    public List<PendingVchRptPojo> getPendingVchRptPojoList() {
        return pendingVchRptPojoList;
    }

    public void setPendingVchRptPojoList(List<PendingVchRptPojo> pendingVchRptPojoList) {
        this.pendingVchRptPojoList = pendingVchRptPojoList;
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
}
