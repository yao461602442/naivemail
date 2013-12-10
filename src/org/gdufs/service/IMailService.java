package org.gdufs.service;

import java.util.List;

import javax.mail.Folder;
import javax.mail.internet.MimeMessage;

import org.gdufs.entity.Account;
import org.gdufs.entity.Mail;

public interface IMailService {

	/**
	 * 账号正确性认证，采用登录Pop服务器的方式进行认证
	 * @param popAddress pop服务器地址，在MailServiceAddress里面找
	 * @param account 用户账户实体
	 * @return 返回反馈登录消息
	 */
	public String authentication(String popAddress, Account account);
	
	/**
	 * 从邮件服务器接收所有邮件
	 * @return 所有邮件的邮件列表
	 */
	public List<Mail> receiveAllMail(String popAddress, Account account);
	
	/**
	 * 获取新邮件
	 * @return 获取数据库中不存在的邮件，即新邮件
	 */
	public List<Mail> getRecentMail(String popAddress, Account account);
	
	/**
	 * 发送邮件
	 * @param mail 邮件对象
	 */
	public void sendMail(String smtpAddress, Account account, Mail mail);
	
	
}
