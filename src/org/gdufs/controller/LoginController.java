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
		int atIndex = mailAccount.indexOf("@");
		int lastDocIndex = mailAccount.indexOf(".", atIndex);
		String serverName = mailAccount.substring(atIndex+1, lastDocIndex);
		//System.out.println(serverName);
		if(serverName.toLowerCase().equals("qq")){
			serverName=MailServiceAddress.QQPopAddress;
		}else if(serverName.toLowerCase().equals("163")){
			serverName=MailServiceAddress._163PopAddress;
		} else
		{
			return "不支持该类邮箱";
		}
		//新建账号
		Account account = new Account();		
		account.setA_account(mailAccount);
		account.setA_passwd(passwd);		
		IMailService mailService = MailServiceFactory.getMailService();
		return mailService.authentication(serverName, account);
	}
	
	public static void main(String[] args) {
		System.out.println(login("mitcn@163.com", "a245412401"));
		System.out.println(login("yao.mitcn@qq.com", "jy13535393974"));
		System.out.println(login("yao.mitcn@qqom", "jy13535393974"));
		System.out.println(login("yao.mitcn@gdufs.com", "jy13535393974"));
		System.out.println(login("yao.mitcn@qq.com", "jy535393974"));
		System.out.println(login("yaosdfasd@qq.com", "jy13535393974"));
	}
	public static boolean emailValidate(String address){
		return address.matches
					("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	}
}
