package skyline.service;

import fnt.view.PaymentAction;
import gateway.domain.LFixedLengthProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.form.config.SystemAttributeNames;
import pub.platform.security.OperatorManager;
import skyline.repository.model.Ptenudetail;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 11-4-22
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ToolsService {
    private static final Logger logger = LoggerFactory.getLogger(ToolsService.class);

    @Autowired
    PlatformService platformService;


    /**
     * 根据枚举表的内容组下拉菜单
     *
     * @param enuName     枚举名称
     * @param isSelectAll 是否添加全部项选择
     * @param isExpandID  true:正常列表（不包含ID） false：列表中包含ID
     * @return 下拉菜单
     */
    public List<SelectItem> getEnuSelectItemList(String enuName, boolean isSelectAll, boolean isExpandID) {
        List<Ptenudetail> records = platformService.selectEnuDetail(enuName);
        List<SelectItem> items = new ArrayList<SelectItem>();
        SelectItem item;
        if (isSelectAll) {
            item = new SelectItem("", "全部");
            items.add(item);
        }
        for (Ptenudetail record : records) {
            if (isExpandID) {
                item = new SelectItem(record.getEnuitemvalue(), record.getEnuitemvalue() + " " + record.getEnuitemlabel());
            } else {
                item = new SelectItem(record.getEnuitemvalue(), record.getEnuitemlabel());
            }
            items.add(item);
        }
        return items;
    }

    public static long daysBetween(String strDate1, String strDate2, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date1 = sdf.parse(strDate1);
        Date date2 = sdf.parse(strDate2);
        long days = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000);
        return days > 0 ? days : (days * -1);
    }

    public static String getDateAfter(Date date, int days, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + days);
        return sdf.format(calendar.getTime());
    }

    public static String getDateAfter(String date, String srcPattern, int days, String desPattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(srcPattern);
        try {
            Date objDate = sdf.parse(date);
            return getDateAfter(objDate, days, desPattern);
        } catch (ParseException e) {
            logger.error("日期格式错误。", e);
        }
        return null;
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
