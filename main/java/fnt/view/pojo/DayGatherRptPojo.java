package fnt.view.pojo;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-7-1
 * Time: ����7:55
 * To change this template use File | Settings | File Templates.
 */
public class DayGatherRptPojo {

    private String wtzsdwbm;          // ί�����յ�λ����
    private String wtzsdwmc;          // ί�����յ�λ����
    private String srxmbm;            // ������Ŀ����
    private String srxmmc;            // ������Ŀ����
    private String yskmbm;            // Ԥ���Ŀ����
    private String yskmmc;            // Ԥ���Ŀ����
    private String pendingflag;       // ��Ʊ���
    // ��������
    private String incomeProperty;

    private String zjxzbm;            // �ʽ����ʱ���
    private String zjxzmc;            // �ʽ���������
    private BigDecimal zje = new BigDecimal("0.00");            // ���

    private String totalAmt = "0.00";          // �ϼ�
    private String budgetItemAmt = "0";           // Ԥ����С��
    private String budgetCentreAmt = "0";         // Ԥ����Ӧ������
    private String budgetProvinceAmt = "0";       // Ԥ����Ӧ��ʡ
    private String budgetCityAmt = "0";           // Ԥ���ڱ�������

    private String fundItemAmt = "0";           // ����ר��С��
    private String fundCentreAmt = "0";         // ����ר��Ӧ������
    private String fundProvinceAmt = "0";       // ����ר��Ӧ��ʡ
    private String fundCityAmt = "0";           // ����ר����������

    private String hrxzqh;            // �������� [���롢ʡ]
    private BigDecimal fcje = new BigDecimal("0");          // �ֳɽ��

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
