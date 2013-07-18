package fnt.view;

import fnt.common.enums.PaymentTxnType;
import fnt.repository.model.FsQdfPendingTxn;
import fnt.repository.model.FsQdfPendingVchInfo;
import fnt.repository.model.FsQdfSysCtl;
import fnt.service.DepService;
import fnt.service.FsqdfPendingTxnService;
import fnt.service.FsqdfSysCtlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-7-8
 * Time: ����8:44
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class PendingVchAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(PendingVchAction.class);

    @ManagedProperty(value = "#{depService}")
    private DepService depService;
    @ManagedProperty(value = "#{fsqdfSysCtlService}")
    private FsqdfSysCtlService sysCtlService;
    @ManagedProperty(value = "#{fsqdfPendingTxnService}")
    private FsqdfPendingTxnService pendingTxnService;

    private FsQdfSysCtl sysCtl;
    private String date8;
    private FsQdfPendingTxn txn = new FsQdfPendingTxn();
    public List<FsQdfPendingTxn> txnList = new ArrayList<FsQdfPendingTxn>();
    private FsQdfPendingVchInfo vch = new FsQdfPendingVchInfo();
    public List<FsQdfPendingVchInfo> vchList = new ArrayList<FsQdfPendingVchInfo>();

    private List<SelectItem> payMethodOptions;
    private List<SelectItem> sendOptions;
    private PaymentTxnType txnType = PaymentTxnType.CASH;

    @PostConstruct
    public void init() {
        date8 = new SimpleDateFormat("yyyyMMdd").format(new Date());
        sysCtl = sysCtlService.getFsQdfSysCtl("1");
        payMethodOptions = new ArrayList<SelectItem>();
        payMethodOptions.add(new SelectItem("03", "���"));
        payMethodOptions.add(new SelectItem("04", "ת��֧Ʊ"));
        payMethodOptions.add(new SelectItem("05", "�ֽ�ͨ��"));
        sendOptions = new ArrayList<SelectItem>();
        sendOptions.add(new SelectItem("0", "��������"));
        sendOptions.add(new SelectItem("1", "��������"));

        txn.setXzqh(sysCtl.getAreaCode());
        txn.setCzzhzh(sysCtl.getCbsActno());
        txn.setJyrq(date8);
        txn.setBmkywxh("BMK" + new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date()));
        vch.setXzqh(sysCtl.getAreaCode());
        vch.setDbpywxh("DBP" + new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date()));
        vch.setCzzhzh(sysCtl.getCbsActno());
        vch.setJyrq(date8);
        txnList = pendingTxnService.qryPendingTxnsByDate(date8);
        vchList = pendingTxnService.qryPendingVchInfosByAddDate(date8);
    }

    // �������ϴ�
    public String onSendTxn() {
        try {
            String msg = depService.txn1533004(txn);
//            String ywxh = txn.getBmkywxh();
            if (msg.startsWith("0000")) {
                MessageUtil.addInfo("���ͳɹ�.������ҵ�����:" + msg.substring(4));
                txnList = pendingTxnService.qryPendingTxnsByDate(date8);
                txn = new FsQdfPendingTxn();
                txn.setXzqh(sysCtl.getAreaCode());
                txn.setCzzhzh(sysCtl.getCbsActno());
                txn.setBmkywxh("BMK" + new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date()));
                txn.setJyrq(date8);

            } else {
                MessageUtil.addError("�����쳣��" + msg);
            }
        } catch (Exception e) {
            String errmsg = "����ʧ�ܡ�" + e.getMessage() == null ? "" : e.getMessage();
            logger.error(errmsg, e);
            MessageUtil.addError(errmsg);
        }
        return null;
    }

    // ����Ʊ�ϴ�
    public String onSendVch() {
        try {
            String msg = depService.txn1533025(vch);
            if (msg.startsWith("0000")) {
                MessageUtil.addInfo("���ͳɹ�.����Ʊҵ�����:" + msg.substring(4));
                vchList = pendingTxnService.qryPendingVchInfosByAddDate(date8);
                vch = new FsQdfPendingVchInfo();
                vch.setXzqh(sysCtl.getAreaCode());
                vch.setDbpywxh("DBP" + new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date()));
                vch.setCzzhzh(sysCtl.getCbsActno());
                vch.setJyrq(date8);
            } else {
                MessageUtil.addError("�����쳣��" + msg);
            }
        } catch (Exception e) {
            String errmsg = "����ʧ�ܡ�" + e.getMessage() == null ? "" : e.getMessage();
            logger.error(errmsg, e);
            MessageUtil.addError(errmsg);
        }
        return null;
    }

    // --------------------------------
    public DepService getDepService() {
        return depService;
    }

    public void setDepService(DepService depService) {
        this.depService = depService;
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

    public String getDate8() {
        return date8;
    }

    public void setDate8(String date8) {
        this.date8 = date8;
    }

    public FsQdfPendingTxn getTxn() {
        return txn;
    }

    public void setTxn(FsQdfPendingTxn txn) {
        this.txn = txn;
    }

    public List<FsQdfPendingTxn> getTxnList() {
        return txnList;
    }

    public void setTxnList(List<FsQdfPendingTxn> txnList) {
        this.txnList = txnList;
    }

    public FsQdfPendingVchInfo getVch() {
        return vch;
    }

    public void setVch(FsQdfPendingVchInfo vch) {
        this.vch = vch;
    }

    public List<FsQdfPendingVchInfo> getVchList() {
        return vchList;
    }

    public void setVchList(List<FsQdfPendingVchInfo> vchList) {
        this.vchList = vchList;
    }

    public List<SelectItem> getPayMethodOptions() {
        return payMethodOptions;
    }

    public void setPayMethodOptions(List<SelectItem> payMethodOptions) {
        this.payMethodOptions = payMethodOptions;
    }

    public List<SelectItem> getSendOptions() {
        return sendOptions;
    }

    public void setSendOptions(List<SelectItem> sendOptions) {
        this.sendOptions = sendOptions;
    }

    public PaymentTxnType getTxnType() {
        return txnType;
    }

    public void setTxnType(PaymentTxnType txnType) {
        this.txnType = txnType;
    }
}
