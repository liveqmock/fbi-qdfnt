package fnt.view;

import fnt.repository.model.FsQdfChkTxn;
import fnt.repository.model.FsQdfSysCtl;
import fnt.service.FsqdfChkVchService;
import fnt.service.FsqdfSysCtlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * POS明细
 */
@ManagedBean
@ViewScoped
public class PosChkAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(PaymentAction.class);

    private String date8;
    @ManagedProperty(value = "#{fsqdfSysCtlService}")
    private FsqdfSysCtlService sysCtlService;
    @ManagedProperty(value = "#{fsqdfChkVchService}")
    private FsqdfChkVchService chkVchService;
    private FsQdfSysCtl sysCtl;
    private List<FsQdfChkTxn> posTxns;

    @PostConstruct
    public void init() {
        sysCtl = sysCtlService.getFsQdfSysCtl("1");
        date8 = getDateAfter(sysCtl.getTxnDate(), "yyyyMMdd", -1, "yyyyMMdd");
    }

    public void onQuery() {
        try {
            posTxns = chkVchService.qryPosChkTxns(date8);
            if (posTxns.size() == 0) {
                MessageUtil.addInfo("没有查询到数据。");
            }
        } catch (Exception e) {
            logger.error("查询POS明细结果失败。", e);
            MessageUtil.addError("查询pos明细结果失败。" + (e.getMessage() == null ? "" : e.getMessage()));
        }
    }

    // ----------------------------------

    public String getDateAfter(String date, String srcPattern, int days, String desPattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(srcPattern);
        try {
            Date objDate = sdf.parse(date);
            return getDateAfter(objDate, days, desPattern);
        } catch (ParseException e) {
            logger.error("日期格式错误。", e);
        }
        return null;
    }

    public String getDateAfter(Date date, int days, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + days);
        return sdf.format(calendar.getTime());
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

    public String getDate8() {
        return date8;
    }

    public void setDate8(String date8) {
        this.date8 = date8;
    }

    public FsqdfChkVchService getChkVchService() {
        return chkVchService;
    }

    public void setChkVchService(FsqdfChkVchService chkVchService) {
        this.chkVchService = chkVchService;
    }

    public List<FsQdfChkTxn> getPosTxns() {
        return posTxns;
    }

    public void setPosTxns(List<FsQdfChkTxn> posTxns) {
        this.posTxns = posTxns;
    }

}
