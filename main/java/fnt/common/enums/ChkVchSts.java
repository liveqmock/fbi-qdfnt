package fnt.common.enums;

import java.util.Hashtable;

/**
 * 对账结果
 */
public enum ChkVchSts implements EnumApp {
    SUCCESS("1", "成功"),
    FAIL("0", "失败");

    private String code = null;
    private String title = null;
    private static Hashtable<String, ChkVchSts> aliasEnums;

    ChkVchSts(String code, String title) {
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

    public static ChkVchSts valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
