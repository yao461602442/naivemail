package org.gdufs.pub;

import org.gdufs.service.IMailService;
import org.gdufs.service.impl.MailService;

/**
 * 单例模式饿汉式实现
 * @author Administrator
 *
 */
public class MailServiceFactory {
	private static IMailService mailService = new MailService();
	
	private MailServiceFactory(){
		//
	}
	
	public static IMailService getMailService(){
		return mailService;
	}
}
