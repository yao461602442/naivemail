package tmp;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

import org.gdufs.dao.impl.AccountDao;
import org.gdufs.dao.impl.MailDao;
import org.gdufs.entity.Account;
import org.gdufs.entity.Mail;
import org.gdufs.entity.MailServiceAddress;
import org.gdufs.pub.MailServiceFactory;
import org.gdufs.service.IMailService;
import org.gdufs.service.impl.MailService;

public class MailTest {

	public static int count = 0;
	
	public static void main(String[] args) throws MessagingException, UnknownHostException, IOException{
		
		//测试邮件服务器模块
		IMailService ms = MailServiceFactory.getMailService();
		Account _163account = new Account();
		_163account.setA_account("mitcn@163.com");
		_163account.setA_passwd("");
		Account qqaccount = new Account();
		qqaccount.setA_account("yao.mitcn@qq.com");
		qqaccount.setA_passwd("");
		Account hjAccount = new Account();
		hjAccount.setA_account("1107402232@qq.com");
		hjAccount.setA_passwd("hjd1");
		//插入账号
		AccountDao adao = new AccountDao();
		adao.insertAccount(hjAccount);
		adao.insertAccount(_163account);
		adao.insertAccount(_163account);
		//获取账号
		hjAccount = adao.queryAccount(hjAccount.getA_account(), hjAccount.getA_passwd());
		_163account = adao.queryAccount(_163account.getA_account(), _163account.getA_passwd());
		System.out.println(_163account);
		qqaccount = adao.queryAccount(qqaccount.getA_account(), qqaccount.getA_passwd());
		System.out.println(qqaccount);
//		//1.邮箱验证测试
//		String msg = ms.authentication("pop.qq.com", qqaccount);
//		System.out.println(msg);
//		
//		//网易的邮箱认证比较慢！
////		Account _163account = new Account();
////		_163account.setA_account("mitcn@163.com");
////		_163account.setA_passwd("*****");
////		msg = ms.authentication("pop.163.com", _163account);
//		System.out.println(msg);
		
		
		//2.邮件收取测试
		//邮件中没有附件时效果好。解析邮件的速度快很多。
		
		List<Mail> mailList = ms.getRecentMail
					(MailServiceAddress._163PopAddress, _163account);
		
		for(int i=0; i<mailList.size(); ++i){
			System.out.println(mailList.get(i));
		}
//		MailDao mdao = new MailDao();
//		mdao.insertBatchMail(mailList);
		
		
	}
	
}


