package fnt.repository.model.common;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-7-4
 * Time: 下午2:51
 * To change this template use File | Settings | File Templates.
 */
public class DayGatherData {
    private String wtzsdwbm;          // 委托征收单位编码
    private String wtzsdwmc;          // 委托征收单位名称
    private String srxmbm;            // 收入项目编码
    private String srxmmc;            // 收入项目名称
    private String yskmbm;            // 预算科目编码
    private String yskmmc;            // 预算科目名称
    private String pendingflag;       // 补票标记
    private String zjxzbm;            // 资金性质编码
    private String zjxzmc;            // 资金性质名称
    private BigDecimal zje;            // 金额
    private String hrxzqh;            // 划入区划 [中央、省] hrxzqh
    private BigDecimal fcje;          // 分成金额
    // ----------------------------------------

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

    public BigDecimal getZje() {
        return zje;
    }

    public void setJe(BigDecimal zje) {
        this.zje = zje;
    }

    public String getPendingflag() {
        return pendingflag;
    }

    public void setPendingflag(String pendingflag) {
        this.pendingflag = pendingflag;
    }

    public void setZje(BigDecimal zje) {
        this.zje = zje;
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
