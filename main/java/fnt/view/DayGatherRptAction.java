package fnt.view;

import fnt.common.utils.JxlsManager;
import fnt.common.utils.ObjectFieldsCopier;
import fnt.repository.model.FsQdfSysCtl;
import fnt.repository.model.common.DayGatherData;
import fnt.service.CommonService;
import fnt.service.FsqdfSysCtlService;
import fnt.view.pojo.DayGatherRptPojo;
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
public class DayGatherRptAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(PaymentAction.class);

    private String date8;
    @ManagedProperty(value = "#{fsqdfSysCtlService}")
    private FsqdfSysCtlService sysCtlService;
    @ManagedProperty(value = "#{commonService}")
    private CommonService commonService;

    private FsQdfSysCtl sysCtl;
    private String totalIncome = "0.00";                   // 总计
    private String totalVchIncome = "0.00";                // 当日正常收入合计 [机打票和手工票缴款]
    private String totalPendingIncome = "0.00";            // 当日待补票转收入合计 [对账成功的待补票转补票收入]
    private String totalUnknownIncome = "0.00";            // 当日不明款转收入合计 [对账成功的不明款转票据(注意：不明款有两种处理)]
    private DecimalFormat df = new DecimalFormat("###,##0.00");
    private List<DayGatherData> dayGatherList = new ArrayList<DayGatherData>();
    private List<DayGatherRptPojo> dayGatherRptRecords = new ArrayList<DayGatherRptPojo>();

    @PostConstruct
    public void init() {
        sysCtl = sysCtlService.getFsQdfSysCtl("1");
        date8 = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public String onQry() {
        try {
            if (date8.compareTo(sysCtl.getTxnDate()) >= 0) {
                MessageUtil.addWarn(date8 + "该日期未对账，无法生成报表。");
                return null;
            }
            dayGatherList.clear();
            dayGatherList = commonService.qryDayGatherDataByDate(date8);
            dayGatherRptRecords.clear();
            if (dayGatherList.size() == 0) {
                MessageUtil.addWarn("没有查询到当日报表数据。");
                return null;
            }
            BigDecimal totalBDIncome = new BigDecimal("0.00");
            BigDecimal totalVchBDIncome = new BigDecimal("0.00");
            BigDecimal totalPendingBDIncome = new BigDecimal("0.00");
            BigDecimal totalUnknownBDIncome = new BigDecimal("0.00");
            for (DayGatherData data : dayGatherList) {
                DayGatherRptPojo record = new DayGatherRptPojo();
                ObjectFieldsCopier.copyFields(data, record);
                // 收入属性
                if ("1".equals(data.getPendingflag())) {   // 补票
                    record.setIncomeProperty("待补票转收入");
                    totalPendingBDIncome = totalPendingBDIncome.add(record.getZje());
                } else if ("3".equals(data.getPendingflag())) { // 不明款直接补票
                    record.setIncomeProperty("不明款转收入");
                    totalUnknownBDIncome = totalUnknownBDIncome.add(record.getZje());
                } else {
                    record.setIncomeProperty("正常收入");
                    totalVchBDIncome = totalVchBDIncome.add(record.getZje());
                }
                if ("01".equals(data.getZjxzbm())) {                                     // 01-预算内
                    record.setBudgetItemAmt(df.format(record.getZje()));
                    BigDecimal bjAmt = record.getZje();
                    if ("000000".equals(data.getHrxzqh())) {                             // 上缴中央
                        record.setBudgetCentreAmt(df.format(record.getFcje()));
                        bjAmt = bjAmt.subtract(record.getFcje());
                    }
                    if ("370000".equals(data.getHrxzqh())) {                      // 上缴省
                        record.setBudgetProvinceAmt(df.format(record.getFcje()));
                        bjAmt = bjAmt.subtract(record.getFcje());
                    }                                                              // 本级收入
                    record.setBudgetCityAmt(df.format(bjAmt));
                } else {                                                                  // 02-预算外
                    record.setFundItemAmt(df.format(record.getZje()));
                    BigDecimal bjAmt = record.getZje();
                    if ("000000".equals(data.getHrxzqh())) {                             // 上缴中央
                        record.setFundCentreAmt(df.format(record.getFcje()));
                        bjAmt = bjAmt.subtract(record.getFcje());
                    }
                    if ("370000".equals(data.getHrxzqh())) {                      // 上缴省
                        record.setFundProvinceAmt(df.format(record.getFcje()));
                        bjAmt = bjAmt.subtract(record.getFcje());
                    }                                                               // 本级收入
                    record.setFundCityAmt(df.format(bjAmt));
                }
                dayGatherRptRecords.add(record);
            }
            totalBDIncome = totalBDIncome.add(totalPendingBDIncome).add(totalUnknownBDIncome).add(totalVchBDIncome);
            totalIncome = df.format(totalBDIncome);
            totalPendingIncome = df.format(totalPendingBDIncome);
            totalUnknownIncome = df.format(totalUnknownBDIncome);
            totalVchIncome = df.format(totalVchBDIncome);

        } catch (Exception e) {
            logger.error("查询日报数据失败。", e);
            MessageUtil.addError("查询日报数据失败。" + (e.getMessage() == null ? "" : e.getMessage()));
        }
        return null;
    }


    public String onGenRpt() {
        try {
            if (date8.compareTo(sysCtl.getTxnDate()) >= 0) {
                MessageUtil.addWarn(date8 + "该日期未对账，无法生成报表。");
                return null;
            }
            if (dayGatherRptRecords.size() == 0) {
                MessageUtil.addWarn("请先查询报表数据。");
                return null;
            } else {
                String excelFilename = "青岛市财政非税收入日报表-" + date8 + ".xls";
                JxlsManager jxls = new JxlsManager();
                Map beansMap = new HashMap();
                logger.info(totalIncome + "   " + totalVchIncome + " " + totalPendingIncome + " " + totalUnknownIncome);
                beansMap.put("records", dayGatherRptRecords);
                beansMap.put("actno", sysCtl.getCbsActno());
                beansMap.put("actnam", sysCtl.getCbsActnam());
                beansMap.put("rptdate", date8);
                beansMap.put("bankName", sysCtl.getBankName());
                beansMap.put("totalIncome", totalIncome);
                beansMap.put("totalVchIncome", totalVchIncome);
                beansMap.put("totalPendingIncome", totalPendingIncome);
                beansMap.put("totalUnknownIncome", totalUnknownIncome);
                jxls.exportDataToXls(excelFilename, "/report/FntTotalIncome.xls", beansMap);
            }
        } catch (Exception e) {
            logger.error("生成收入日报表失败。", e);
            MessageUtil.addError("生成收入日报表失败。" + (e.getMessage() == null ? "" : e.getMessage()));
        }
        return null;
    }

    // -------------------------------------

    public List<DayGatherData> getDayGatherList() {
        return dayGatherList;
    }

    public void setDayGatherList(List<DayGatherData> dayGatherList) {
        this.dayGatherList = dayGatherList;
    }

    public List<DayGatherRptPojo> getDayGatherRptRecords() {
        return dayGatherRptRecords;
    }

    public void setDayGatherRptRecords(List<DayGatherRptPojo> dayGatherRptRecords) {
        this.dayGatherRptRecords = dayGatherRptRecords;
    }

    public CommonService getCommonService() {
        return commonService;
    }

    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    public FsQdfSysCtl getSysCtl() {
        return sysCtl;
    }

    public void setSysCtl(FsQdfSysCtl sysCtl) {
        this.sysCtl = sysCtl;
    }

    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }

    public String getTotalVchIncome() {
        return totalVchIncome;
    }

    public void setTotalVchIncome(String totalVchIncome) {
        this.totalVchIncome = totalVchIncome;
    }

    public String getTotalPendingIncome() {
        return totalPendingIncome;
    }

    public void setTotalPendingIncome(String totalPendingIncome) {
        this.totalPendingIncome = totalPendingIncome;
    }

    public String getTotalUnknownIncome() {
        return totalUnknownIncome;
    }

    public void setTotalUnknownIncome(String totalUnknownIncome) {
        this.totalUnknownIncome = totalUnknownIncome;
    }

    public DecimalFormat getDf() {
        return df;
    }

    public void setDf(DecimalFormat df) {
        this.df = df;
    }

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
}
