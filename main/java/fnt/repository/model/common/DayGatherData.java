package fnt.repository.model.common;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-7-4
 * Time: ����2:51
 * To change this template use File | Settings | File Templates.
 */
public class DayGatherData {
    private String wtzsdwbm;          // ί�����յ�λ����
    private String wtzsdwmc;          // ί�����յ�λ����
    private String srxmbm;            // ������Ŀ����
    private String srxmmc;            // ������Ŀ����
    private String yskmbm;            // Ԥ���Ŀ����
    private String yskmmc;            // Ԥ���Ŀ����
    private String pendingflag;       // ��Ʊ���
    private String zjxzbm;            // �ʽ����ʱ���
    private String zjxzmc;            // �ʽ���������
    private BigDecimal zje;            // ���
    private String hrxzqh;            // �������� [���롢ʡ] hrxzqh
    private BigDecimal fcje;          // �ֳɽ��
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
