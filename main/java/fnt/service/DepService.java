package fnt.service;

import fnt.repository.model.FsQdfPendingTxn;
import fnt.repository.model.FsQdfPendingVchInfo;
import gateway.client.SyncSocketClient;
import gateway.domain.LFixedLengthProtocol;
import gateway.domain.ProtocolFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.security.OperatorManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ���ݽ���
 */
@Service
public class DepService {
    private static final Logger logger = LoggerFactory.getLogger(DepService.class);

    @Autowired
    private SyncSocketClient client;

    // ��ѯ�ɿ�����Ϣ
    public String txn1533001(String voucherType, String voucherSn) {
        LFixedLengthProtocol tia = newFixedLengthProtocol();
        tia.txnCode = "1533001";
        tia.msgBody = (voucherType + "|" + voucherSn + "|").getBytes();
        LFixedLengthProtocol toa = null;
        try {
            toa = client.onRequest(tia);
        } catch (Exception e) {
            logger.error("����ͨ���쳣.", e);
            throw new RuntimeException("����ͨ���쳣��");
        }
        if ("0000".equals(toa.rtnCode) || "0001".equals(toa.rtnCode)) {
            String msgbody = new String(toa.msgBody);
            logger.info("���ر����壺" + msgbody);
            return msgbody;
        } else {
            throw new RuntimeException("[" + toa.rtnCode + "]" + new String(toa.msgBody));
        }
    }

    // �ɿ�֪ͨ��ɿ�
    public boolean txn1533002(String msgbody) {
        LFixedLengthProtocol tia = newFixedLengthProtocol();
        tia.txnCode = "1533002";
        tia.msgBody = msgbody.getBytes();
        LFixedLengthProtocol toa = client.onRequest(tia);
        if ("0000".equals(toa.rtnCode)) {
            return true;
        } else {
            throw new RuntimeException("[" + toa.rtnCode + "]" + new String(toa.msgBody));
        }
    }

    // ȡ���ɿ�
    public boolean txn1533013(String voucherType, String voucherSn) {
        LFixedLengthProtocol tia = newFixedLengthProtocol();
        tia.txnCode = "1533013";
        tia.msgBody = (voucherType + "|" + voucherSn + "|").getBytes();
        LFixedLengthProtocol toa = client.onRequest(tia);
        if ("0000".equals(toa.rtnCode)) {
            return true;
        } else {
            throw new RuntimeException("[" + toa.rtnCode + "]" + new String(toa.msgBody));
        }
    }

    // ��ѯִ�յ�λ��Ϣ
    public String[] txn1533008(String areaCode, String unitCode) {
        LFixedLengthProtocol tia = newFixedLengthProtocol();
        tia.txnCode = "1533008";
        tia.msgBody = (areaCode + "|" + unitCode + "|").getBytes();
        LFixedLengthProtocol toa = client.onRequest(tia);
        if ("0000".equals(toa.rtnCode)) {
            String msgbody = new String(toa.msgBody);
            logger.info("���ر����壺" + msgbody);
            return StringUtils.splitByWholeSeparatorPreserveAllTokens(msgbody, "|");
        } else {
            throw new RuntimeException("[" + toa.rtnCode + "]" + new String(toa.msgBody));
        }
    }

    // ��ѯ������Ŀ��Ϣ
    public String[] txn1533007(String areaCode, String unitCode, String prjCode) {
        LFixedLengthProtocol tia = newFixedLengthProtocol();
        tia.txnCode = "1533007";
        tia.msgBody = (areaCode + "|" + unitCode + "|" + prjCode + "|").getBytes();
        LFixedLengthProtocol toa = client.onRequest(tia);
        if ("0000".equals(toa.rtnCode)) {
            String msgbody = new String(toa.msgBody);
            logger.info("���ر����壺" + msgbody);
            return StringUtils.splitByWholeSeparatorPreserveAllTokens(msgbody, "|");
        } else {
            throw new RuntimeException("[" + toa.rtnCode + "]" + new String(toa.msgBody));
        }
    }

    // ����
    public boolean txn1533003(String areaCode, String chkActDate) {
        LFixedLengthProtocol tia = newFixedLengthProtocol();
        tia.txnCode = "1533003";
        tia.msgBody = (areaCode + "|" + chkActDate + "|").getBytes();
        logger.info("��������������" + areaCode + " �������ڣ�" + chkActDate);
        LFixedLengthProtocol toa = client.onRequest(tia);
        return "0000".equals(toa.rtnCode);
    }

    // ��ѯ�ֳ���Ϣ
    public boolean txn1533028(String date) {
        LFixedLengthProtocol tia = newFixedLengthProtocol();
        tia.txnCode = "1533028";
        tia.msgBody = date.getBytes();
        LFixedLengthProtocol toa = client.onRequest(tia);
        if ("0000".equals(toa.rtnCode)) {
            return true;
        } else {
            throw new RuntimeException("[" + toa.rtnCode + "]" + new String(toa.msgBody));
        }
    }

    // ���Ͳ�������Ϣ
    public String txn1533004(FsQdfPendingTxn txn) {
        LFixedLengthProtocol tia = newFixedLengthProtocol();
        tia.txnCode = "1533004";
        StringBuffer txnBuffer = new StringBuffer();
        // ���Բ��������
        txnBuffer.append(txn.getXzqh()).append("|").append(txn.getBmkywxh()).append("|").append(txn.getCzzhzh()).append("|")
                .append(txn.getJkfs()).append("|").append(txn.getYt()).append("|")
                .append(txn.getJyrq()).append("|").append(txn.getJe()).append("|");
        tia.msgBody = txnBuffer.toString().getBytes();

        LFixedLengthProtocol toa = client.onRequest(tia);
        return toa.rtnCode + new String(toa.msgBody);
    }

    // ���ʹ���Ʊ��Ϣ
    public String txn1533025(FsQdfPendingVchInfo vch) {
        LFixedLengthProtocol tia = newFixedLengthProtocol();
        tia.txnCode = "1533025";
        StringBuffer vchBuffer = new StringBuffer();
        // ����ҵ����� ��linking����
        vchBuffer.append(vch.getXzqh()).append("|").append(vch.getDbpywxh()).append("|").append(vch.getJylx()).append("|")
                .append(vch.getCzzhzh()).append("|").append(vch.getZsdwbm()).append("|")
                .append(vch.getJkfs()).append("|").append(vch.getHkrqc()).append("|")
                .append(vch.getHkrzh()).append("|").append(vch.getHkrkhyh()).append("|")
                .append(vch.getYt()).append("|").append(vch.getFzqrxx()).append("|")
                .append(vch.getBmkywxh()).append("|").append(vch.getJyrq()).append("|")
                .append(vch.getJe()).append("|").append(vch.getZsdwmc()).append("|")
                .append(vch.getDwzgbmbm()).append("|").append(vch.getDwzgbmmc()).append("|");
        tia.msgBody = vchBuffer.toString().getBytes();
        LFixedLengthProtocol toa = client.onRequest(tia);
        return toa.rtnCode + new String(toa.msgBody);
    }

    private LFixedLengthProtocol newFixedLengthProtocol() {
        LFixedLengthProtocol proto = new LFixedLengthProtocol();
        proto.appID = PropertyManager.getProperty("sys.id");
        String date14 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        proto.serialNo = date14;
        proto.txnTime = date14;
        proto.ueserID = PropertyManager.getProperty("linking.wsys.userid");
        OperatorManager om = ProtocolFactory.getOperatorManager();
        proto.branchID = om.getOperator().getDeptid();
        proto.tellerID = om.getOperatorId();
        return proto;
    }

}
