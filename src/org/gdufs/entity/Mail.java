package org.gdufs.entity;

import org.gdufs.dao.*;
import org.gdufs.dao.impl.*;

/**
 * 邮件实体
 */

public class Mail {

	private int    m_id; //标记一封的邮件唯一id。 从产生到删除id均不能修改
	private int    a_id; //邮件所属的邮箱账号id
	private int    b_id; //邮件所属的文件夹id, 收件箱，发件箱，等等
	private String m_title;
	private String m_time;
	private String m_sender;
	private String m_receiver;
	private String m_content;
	private int    m_spam; //1为垃圾邮件 ,0为正常邮件
	private int    m_read; //1为已读， 0为未读。  默认为0
	
	public Mail()
	{
		//空构造函数,必须调用getter和setter方法设置属性
	}
	
	
	//额外的操作方法
	public String getMailBox()
	{
		//返回邮件所在文件夹
		IMailDao mailDao = new MailDao();
		String mailBoxName = mailDao.getMailBox(this);
		return mailBoxName;
	}
	
	
	//==========getter 和 setter==========================
	//没有m_id的setter!
	public int getM_id() {
		return m_id;
	}

	public void setA_id(int a_id) {
		this.a_id = a_id;
	}

	public int getB_id() {
		return b_id;
	}

	public void setB_id(int b_id) {
		this.b_id = b_id;
	}

	public String getM_title() {
		return m_title;
	}

	public void setM_title(String m_title) {
		this.m_title = m_title;
	}

	public String getM_time() {
		return m_time;
	}

	public void setM_time(String m_time) {
		this.m_time = m_time;
	}

	public String getM_sender() {
		return m_sender;
	}

	public void setM_sender(String m_sender) {
		this.m_sender = m_sender;
	}

	public String getM_receiver() {
		return m_receiver;
	}

	public void setM_receiver(String m_receiver) {
		this.m_receiver = m_receiver;
	}

	public String getM_content() {
		return m_content;
	}

	public void setM_content(String m_content) {
		this.m_content = m_content;
	}

	public int getM_spam() {
		return m_spam;
	}

	public void setM_spam(int m_spam) {
		this.m_spam = m_spam;
	}

	public int getM_read() {
		return m_read;
	}

	public void setM_read(int m_read) {
		this.m_read = m_read;
	}

	public int getA_id() {
		return a_id;
	}
	//======================================================


	@Override
	public String toString() {
		return "Mail \n[m_id="+ m_id+ "\n m_title=" + m_title + "\n m_time=" + m_time
				+ "\n m_sender=" + m_sender + "\n m_receiver=" + m_receiver
				+ "\n m_content=" + m_content + "]";
	}
	
	
}

