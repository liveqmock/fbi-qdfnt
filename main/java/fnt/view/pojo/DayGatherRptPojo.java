package fnt.view.pojo;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-7-1
 * Time: 下午7:55
 * To change this template use File | Settings | File Templates.
 */
public class DayGatherRptPojo {

    private String wtzsdwbm;          // 委托征收单位编码
    private String wtzsdwmc;          // 委托征收单位名称
    private String srxmbm;            // 收入项目编码
    private String srxmmc;            // 收入项目名称
    private String yskmbm;            // 预算科目编码
    private String yskmmc;            // 预算科目名称
    private String pendingflag;       // 补票标记
    // 收入属性
    private String incomeProperty;

    private String zjxzbm;            // 资金性质编码
    private String zjxzmc;            // 资金性质名称
    private BigDecimal zje = new BigDecimal("0.00");            // 金额

    private String totalAmt = "0.00";          // 合计
    private String budgetItemAmt = "0";           // 预算内小计
    private String budgetCentreAmt = "0";         // 预算内应缴中央
    private String budgetProvinceAmt = "0";       // 预算内应缴省
    private String budgetCityAmt = "0";           // 预算内本级收入

    private String fundItemAmt = "0";           // 财政专户小计
    private String fundCentreAmt = "0";         // 财政专户应缴中央
    private String fundProvinceAmt = "0";       // 财政专户应缴省
    private String fundCityAmt = "0";           // 财政专户本级收入

    private String hrxzqh;            // 划入区划 [中央、省]
    private BigDecimal fcje = new BigDecimal("0");          // 分成金额

    public String getWtzsdwbm() {
        return wtzsdwbm;
    }

    public void setWtzsdwbm(String wtzsdwbm) {
        this.wtzsdwbm = wtzsdwbm;
    }

    public String getWtzsdwmc() {
        return wtzsdwmc;
    }

    public void setWtzsdwmc(String wtzsdwmc) {
        this.wtzsdwmc = wtzsdwmc;
    }

    public String getSrxmbm() {
        return srxmbm;
    }

    public void setSrxmbm(String srxmbm) {
        this.srxmbm = srxmbm;
    }

    public String getSrxmmc() {
        return srxmmc;
    }

    public void setSrxmmc(String srxmmc) {
        this.srxmmc = srxmmc;
    }

    public String getYskmbm() {
        return yskmbm;
    }

    public void setYskmbm(String yskmbm) {
        this.yskmbm = yskmbm;
    }

    public String getYskmmc() {
        return yskmmc;
    }

    public void setYskmmc(String yskmmc) {
        this.yskmmc = yskmmc;
    }

    public String getPendingflag() {
        return pendingflag;
    }

    public void setPendingflag(String pendingflag) {
        this.pendingflag = pendingflag;
    }

    public String getIncomeProperty() {
        return incomeProperty;
    }

    public void setIncomeProperty(String incomeProperty) {
        this.incomeProperty = incomeProperty;
    }

    public String getZjxzbm() {
        return zjxzbm;
    }

    public void setZjxzbm(String zjxzbm) {
        this.zjxzbm = zjxzbm;
    }

    public String getZjxzmc() {
        return zjxzmc;
    }

    public void setZjxzmc(String zjxzmc) {
        this.zjxzmc = zjxzmc;
    }

    public BigDecimal getZje() {
        return zje;
    }

    public void setZje(BigDecimal zje) {
        this.zje = zje;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getBudgetItemAmt() {
        return budgetItemAmt;
    }

    public void setBudgetItemAmt(String budgetItemAmt) {
        this.budgetItemAmt = budgetItemAmt;
    }

    public String getBudgetCentreAmt() {
        return budgetCentreAmt;
    }

    public void setBudgetCentreAmt(String budgetCentreAmt) {
        this.budgetCentreAmt = budgetCentreAmt;
    }

    public String getBudgetProvinceAmt() {
        return budgetProvinceAmt;
    }

    public void setBudgetProvinceAmt(String budgetProvinceAmt) {
        this.budgetProvinceAmt = budgetProvinceAmt;
    }

    public String getBudgetCityAmt() {
        return budgetCityAmt;
    }

    public void setBudgetCityAmt(String budgetCityAmt) {
        this.budgetCityAmt = budgetCityAmt;
    }

    public String getFundItemAmt() {
        return fundItemAmt;
    }

    public void setFundItemAmt(String fundItemAmt) {
        this.fundItemAmt = fundItemAmt;
    }

    public String getFundCentreAmt() {
        return fundCentreAmt;
    }

    public void setFundCentreAmt(String fundCentreAmt) {
        this.fundCentreAmt = fundCentreAmt;
    }

    public String getFundProvinceAmt() {
        return fundProvinceAmt;
    }

    public void setFundProvinceAmt(String fundProvinceAmt) {
        this.fundProvinceAmt = fundProvinceAmt;
    }

    public String getFundCityAmt() {
        return fundCityAmt;
    }

    public void setFundCityAmt(String fundCityAmt) {
        this.fundCityAmt = fundCityAmt;
    }

    public String getHrxzqh() {
        return hrxzqh;
    }

    public void setHrxzqh(String hrxzqh) {
        this.hrxzqh = hrxzqh;
    }

    public BigDecimal getFcje() {
        return fcje;
    }

    public void setFcje(BigDecimal fcje) {
        this.fcje = fcje;
    }
}
