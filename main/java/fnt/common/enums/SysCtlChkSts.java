package fnt.common.enums;

import java.util.Hashtable;

/**
 * ϵͳ���Ʊ�-���˽��
 */
public enum SysCtlChkSts implements EnumApp {
    SUCCESS("0", "�ɹ�"),
    FAIL("1", "ʧ��");

    private String code = null;
    private String title = null;
    private static Hashtable<String, SysCtlChkSts> aliasEnums;

    SysCtlChkSts(String code, String title) {
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

    public static SysCtlChkSts valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
