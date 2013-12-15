package org.gdufs.service;

import java.util.List;

import javax.mail.Folder;
import javax.mail.internet.MimeMessage;

import org.gdufs.entity.Account;
import org.gdufs.entity.Mail;

public interface IMailService {

	/**
	 * �˺���ȷ����֤�����õ�¼Pop�������ķ�ʽ������֤
	 * @param popAddress pop��������ַ����MailServiceAddress������
	 * @param account �û��˻�ʵ��
	 * @return ���ط�����¼��Ϣ
	 */
	public String authentication(String popAddress, Account account);
	
	/**
	 * ���ʼ����������������ʼ�
	 * @return �����ʼ����ʼ��б�
	 */
	public List<Mail> receiveAllMail(String popAddress, Account account);
	
	/**
	 * ��ȡ���ʼ�
	 * @return ��ȡ���ݿ��в����ڵ��ʼ��������ʼ�
	 */
	public List<Mail> getRecentMail(String popAddress, Account account);
	
	/**
	 * �����ʼ�
	 * @param mail �ʼ�����
	 */
	public void sendMail(Account account, Mail mail);
	
	
}
