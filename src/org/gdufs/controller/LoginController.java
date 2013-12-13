package org.gdufs.controller;

import org.gdufs.entity.Account;
import org.gdufs.entity.MailServiceAddress;
import org.gdufs.pub.MailServiceFactory;
import org.gdufs.service.IMailService;

/**
 * 对应登录界面的控制器
 * @author Administrator
 *
 */
public class LoginController {
	/**
	 * 
	 * @param mailAccount 必须满足邮件格式
	 * @param passwd
	 * @return
	 */
	public static String login(String mailAccount, String passwd){
		//判断邮件格式
		if(!emailValidate(mailAccount)){
			return "邮箱账号不合法！";
		}
		//获取服务器地址
		String serverName = MailServiceAddress.getPopAddress(mailAccount);
                if(serverName == null){
                    return "不支持该邮箱账号！";
                }
                System.out.println(serverName);
		//新建账号
		Account account = new Account();		
		account.setA_account(mailAccount);
		account.setA_passwd(passwd);		
		IMailService mailService = MailServiceFactory.getMailService();
		return mailService.authentication(serverName, account);
	}
        /**
         * 验证邮箱正确性
         * @param address 邮箱地址
         * @return 
         */
        private static boolean emailValidate(String address){
		return address.matches
					("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	}
	
	public static void main(String[] args) {
		
	}
	
}
