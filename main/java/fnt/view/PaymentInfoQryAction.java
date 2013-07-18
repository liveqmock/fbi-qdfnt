package fnt.view;

import fnt.repository.model.FsQdfPaymentInfo;
import fnt.service.PaymentQryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-7-5
 * Time: 下午8:27
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class PaymentInfoQryAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(PaymentInfoQryAction.class);
    private String date8 = new SimpleDateFormat("yyyyMMdd").format(new Date());
    @ManagedProperty(value = "#{paymentQryService}")
    private PaymentQryService paymentQryService;
    private List<FsQdfPaymentInfo> paymentInfoList = new ArrayList<FsQdfPaymentInfo>();
    private BigDecimal totalAmt = new BigDecimal("0.00");
    private int infoCnt = 0;

    @PostConstruct
    public void init() {
        onQry();
    }

    public String onQry() {
        try {
            paymentInfoList.clear();
            totalAmt = new BigDecimal("0.00");
            paymentInfoList = paymentQryService.qryPaymentInfos(date8);
            if (paymentInfoList.size() == 0) {
                MessageUtil.addWarn("没有查询到数据。");
            } else {
                for (FsQdfPaymentInfo info : paymentInfoList) {
                    logger.info(totalAmt + "");
                    totalAmt = totalAmt.add(info.getZje());
                }
                infoCnt = paymentInfoList.size();
            }
        } catch (Exception e) {
            logger.error("查询失败." + e.getMessage() == null ? "" : e.getMessage(), e);
            MessageUtil.addError("查询失败." + e.getMessage() == null ? "" : e.getMessage());
        }
        return null;
    }

    public String getDate8() {
        return date8;
    }

    public void setDate8(String date8) {
        this.date8 = date8;
    }

    public PaymentQryService getPaymentQryService() {
        return paymentQryService;
    }

    public void setPaymentQryService(PaymentQryService paymentQryService) {
        this.paymentQryService = paymentQryService;
    }

    public List<FsQdfPaymentInfo> getPaymentInfoList() {
        return paymentInfoList;
    }

    public void setPaymentInfoList(List<FsQdfPaymentInfo> paymentInfoList) {
        this.paymentInfoList = paymentInfoList;
    }

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }

    public int getInfoCnt() {
        return infoCnt;
    }

    public void setInfoCnt(int infoCnt) {
        this.infoCnt = infoCnt;
    }
}
