package fnt.common.enums;

import java.util.Hashtable;

/**
 * 缴款方式
 * 01 现金 02 转账 06 POS 07 网银
 */
public enum PaymentTxnType implements EnumApp {
    CASH("01", "现金"),
    TRANSFER("02", "转账"),
    /*
    "03", "电汇"));
        payMethodOptions.add(new SelectItem("04", "转账支票"));
        payMethodOptions.add(new SelectItem("05", "现金通存"));
     */
    E_TRANS("03", "电汇"),
    P_TRANSFER("04", "转账支票"),
    P_CASH("05", "现金通存"),
    POS("06", "POS"),
    INTERNET("07", "网银");

    private String code = null;
    private String title = null;
    private static Hashtable<String, PaymentTxnType> aliasEnums;

    PaymentTxnType(String code, String title) {
        this.init(code, title);
    }

    @SuppressWarnings("unchecked")
    private void init(String code, String title) {
        this.code = code;
        this.title = title;
        synchronized (this.getClass()) {
            if (aliasEnums == null) {
                aliasEnums = new Hashtable();
            }
        }
        aliasEnums.put(code, this);
        aliasEnums.put(title, this);
    }

    public static PaymentTxnType valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
