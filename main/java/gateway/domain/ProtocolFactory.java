package gateway.domain;

import pub.platform.advance.utils.PropertyManager;
import pub.platform.form.config.SystemAttributeNames;
import pub.platform.security.OperatorManager;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TODO 协议工厂
 */
public class ProtocolFactory {

    public static LFixedLengthProtocol newFixedLengthProtocol() {
        LFixedLengthProtocol proto = new LFixedLengthProtocol();
        proto.appID = PropertyManager.getProperty("sys.id");
        String date14 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        proto.serialNo = date14;
        proto.txnTime = date14;
        proto.ueserID = PropertyManager.getProperty("linking.wsys.userid");
        OperatorManager om = getOperatorManager();
        proto.branchID = om.getOperator().getDeptid();
        proto.tellerID = om.getOperatorId();
        return proto;
    }

    public static OperatorManager getOperatorManager() {
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) extContext.getSession(true);
        OperatorManager om = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        if (om == null) {
            throw new RuntimeException("用户未登录！");
        }
        return om;
    }

}
