package fnt.view;

import fnt.common.enums.ChkVchSts;
import fnt.common.enums.DataSysId;
import fnt.common.enums.PaymentTxnType;
import fnt.common.enums.SysCtlChkSts;
import fnt.repository.model.FsQdfChkVch;
import fnt.repository.model.FsQdfSysCtl;
import fnt.service.DepService;
import fnt.service.FsqdfChkVchService;
import fnt.service.FsqdfSysCtlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-6-27
 * Time: 下午4:40
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ChkActAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(PaymentAction.class);

    private String date8;
    @ManagedProperty(value = "#{fsqdfSysCtlService}")
    private FsqdfSysCtlService sysCtlService;
    @ManagedProperty(value = "#{fsqdfChkVchService}")
    private FsqdfChkVchService chkVchService;
    @ManagedProperty(value = "#{depService}")
    private DepService depService;

    private FsQdfSysCtl sysCtl;
    private List<FsQdfChkVch> chkVchList;
    private FsQdfChkVch[] selectedRecords;
    private String areaCode;
    private ChkVchSts chkVchSts = ChkVchSts.SUCCESS;
    private DataSysId dataSysId = DataSysId.LOCAL;
    private PaymentTxnType payType = PaymentTxnType.CASH;
    private SysCtlChkSts sysCtlChkSts = SysCtlChkSts.SUCCESS;

    private boolean isChecked = false;

    @PostConstruct
    public void init() {
        String isChk = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("isChk");
        if (isChk != null) {
            // 对账
            sysCtl = sysCtlService.getFsQdfSysCtl("1");
            date8 = sysCtl.getTxnDate();
            String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
            if(today.compareTo(date8) < 0) {
                isChecked = true;
            }
            areaCode = sysCtl.getAreaCode();
            sysCtl = null;
        } else {
            // 非对账
            sysCtl = sysCtlService.getFsQdfSysCtl("1");
            date8 = getDateAfter(sysCtl.getTxnDate(), "yyyyMMdd", -1, "yyyyMMdd");
            chkVchList = chkVchService.qryChkVchs(date8);
        }
    }

    public void onQuery() {
        try {
            chkVchList = chkVchService.qryChkVchs(date8);
            if (chkVchList.size() == 0) {
                MessageUtil.addInfo("没有查询到数据。");
            }
        } catch (Exception e) {
            logger.error("查询对账结果失败。", e);
            MessageUtil.addError("查询对账结果失败。" + (e.getMessage() == null ? "" : e.getMessage()));
        }
    }

    public void onChkAct() {
        try {
            boolean isOk = depService.txn1533003(areaCode, date8);
            if (isOk) {
                MessageUtil.addInfo("对账成功.");
            } else {
                MessageUtil.addError("对账失败，请查看明细.");
            }
            chkVchList = chkVchService.qryChkVchs(date8);
            if (chkVchList.size() == 0) {
                MessageUtil.addInfo("没有查询到数据。");
            }
            sysCtl = sysCtlService.getFsQdfSysCtl("1");
        } catch (Exception e) {
            logger.error("对账失败。", e);
            MessageUtil.addError("对账失败。" + (e.getMessage() == null ? "" : e.getMessage()));
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

    public FsqdfChkVchService getChkVchService() {
        return chkVchService;
    }

    public void setChkVchService(FsqdfChkVchService chkVchService) {
        this.chkVchService = chkVchService;
    }

    public FsQdfSysCtl getSysCtl() {
        return sysCtl;
    }

    public void setSysCtl(FsQdfSysCtl sysCtl) {
        this.sysCtl = sysCtl;
    }

    public List<FsQdfChkVch> getChkVchList() {
        return chkVchList;
    }

    public void setChkVchList(List<FsQdfChkVch> chkVchList) {
        this.chkVchList = chkVchList;
    }

    public String getDate8() {
        return date8;
    }

    public void setDate8(String date8) {
        this.date8 = date8;
    }

    public ChkVchSts getChkVchSts() {
        return chkVchSts;
    }

    public void setChkVchSts(ChkVchSts chkVchSts) {
        this.chkVchSts = chkVchSts;
    }

    public DataSysId getDataSysId() {
        return dataSysId;
    }

    public void setDataSysId(DataSysId dataSysId) {
        this.dataSysId = dataSysId;
    }

    public PaymentTxnType getPayType() {
        return payType;
    }

    public void setPayType(PaymentTxnType payType) {
        this.payType = payType;
    }

    public SysCtlChkSts getSysCtlChkSts() {
        return sysCtlChkSts;
    }

    public void setSysCtlChkSts(SysCtlChkSts sysCtlChkSts) {
        this.sysCtlChkSts = sysCtlChkSts;
    }

    public DepService getDepService() {
        return depService;
    }

    public void setDepService(DepService depService) {
        this.depService = depService;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public FsQdfChkVch[] getSelectedRecords() {
        return selectedRecords;
    }

    public void setSelectedRecords(FsQdfChkVch[] selectedRecords) {
        this.selectedRecords = selectedRecords;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
