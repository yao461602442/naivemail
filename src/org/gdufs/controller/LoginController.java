package org.gdufs.controller;

import org.gdufs.entity.Account;
import org.gdufs.entity.MailServiceAddress;
import org.gdufs.pub.MailServiceFactory;
import org.gdufs.service.IMailService;

/**
 * ��Ӧ��¼����Ŀ�����
 * @author Administrator
 *
 */
public class LoginController {
	/**
	 * 
	 * @param mailAccount ���������ʼ���ʽ
	 * @param passwd
	 * @return
	 */
	public static String login(String mailAccount, String passwd){
		//�ж��ʼ���ʽ
		if(!emailValidate(mailAccount)){
			return "�����˺Ų��Ϸ���";
		}
		//��ȡ��������ַ
		String serverName = MailServiceAddress.getPopAddress(mailAccount);
                if(serverName == null){
                    return "��֧�ָ������˺ţ�";
                }
                System.out.println(serverName);
		//�½��˺�
		Account account = new Account();		
		account.setA_account(mailAccount);
		account.setA_passwd(passwd);		
		IMailService mailService = MailServiceFactory.getMailService();
		return mailService.authentication(serverName, account);
	}
        /**
         * ��֤������ȷ��
         * @param address �����ַ
         * @return 
         */
        private static boolean emailValidate(String address){
		return address.matches
					("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	}
	
	public static void main(String[] args) {
		
	}
	
}
