package org.gdufs.entity;

public class MailServiceAddress {

    public final static String _163PopAddress = "pop.163.com";
    public final static String _163SMTPAddress = "smtp.163.com";
    public final static String QQPopAddress = "pop.qq.com";
    public final static String QQSMTPAddress = "smtp.qq.com";

    /**
     * ��ȡpop�ʼ���������ַ
     * @param mailAccount
     * @return 
     */
    public static String getPopAddress(String mailAccount) {
        //��ȡ��������ַ
        int atIndex = mailAccount.indexOf("@");
        int lastDocIndex = mailAccount.indexOf(".", atIndex);
        String serverName = mailAccount.substring(atIndex + 1, lastDocIndex);
        //System.out.println(serverName);
        if (serverName.toLowerCase().equals("qq")) {
            serverName = MailServiceAddress.QQPopAddress;
        } else if (serverName.toLowerCase().equals("163")) {
            serverName = MailServiceAddress._163PopAddress;
        } else {
            serverName = null;
        }
        return serverName;
    }
    
    /**
     * ��ȡsmtp�ʼ���������ַ
     * @param mailAccount
     * @return 
     */
    public static String getSmtpAddress(String mailAccount) {
        //��ȡ��������ַ
        int atIndex = mailAccount.indexOf("@");
        int lastDocIndex = mailAccount.indexOf(".", atIndex);
        String serverName = mailAccount.substring(atIndex + 1, lastDocIndex);
        //System.out.println(serverName);
        if (serverName.toLowerCase().equals("qq")) {
            serverName = MailServiceAddress.QQSMTPAddress;
        } else if (serverName.toLowerCase().equals("163")) {
            serverName = MailServiceAddress._163SMTPAddress;
        } else {
            serverName = null;
        }
        return serverName;
    }
}
