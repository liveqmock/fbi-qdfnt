package fnt.common.enums;

import java.util.Hashtable;

/**
 * �ɿʽ
 * 01 �ֽ� 02 ת�� 06 POS 07 ����
 */
public enum PaymentTxnType implements EnumApp {
    CASH("01", "�ֽ�"),
    TRANSFER("02", "ת��"),
    /*
    "03", "���"));
        payMethodOptions.add(new SelectItem("04", "ת��֧Ʊ"));
        payMethodOptions.add(new SelectItem("05", "�ֽ�ͨ��"));
     */
    E_TRANS("03", "���"),
    P_TRANSFER("04", "ת��֧Ʊ"),
    P_CASH("05", "�ֽ�ͨ��"),
    POS("06", "POS"),
    INTERNET("07", "����");

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
