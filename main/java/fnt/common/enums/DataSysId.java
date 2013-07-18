package fnt.common.enums;

import java.util.Hashtable;

/**
 * ������������
 */
public enum DataSysId implements EnumApp {
    WSYS("1", "�в���"),
    LOCAL("0", "����");

    private String code = null;
    private String title = null;
    private static Hashtable<String, DataSysId> aliasEnums;

    DataSysId(String code, String title) {
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

    public static DataSysId valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
