package fnt.common.enums;

import java.util.Hashtable;

/**
 * 待补票确认标记
 */
public enum PendingVchFlag implements EnumApp {

    NOT_CONFIRM("0", "未确认"),
    CONFIRM_RIGHT("1", "已确认"),
    CONFIRM_WRONG("2", "确认错误"),
    PENDED("3", "已补票");

    private String code = null;
    private String title = null;
    private static Hashtable<String, PendingVchFlag> aliasEnums;

    PendingVchFlag(String code, String title) {
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

    public static PendingVchFlag valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }

    public String toRtnMsg() {
        return this.code + "|" + this.title;
    }
}
