package org.gdufs.entity;

import org.gdufs.dao.*;
import org.gdufs.dao.impl.*;

/**
 * �ʼ�ʵ��
 */

public class Mail {

	private int    m_id; //���һ����ʼ�Ψһid�� �Ӳ�����ɾ��id�������޸�
	private int    a_id; //�ʼ������������˺�id
	private int    b_id; //�ʼ��������ļ���id, �ռ��䣬�����䣬�ȵ�
	private String m_title;
	private String m_time;
	private String m_sender;
	private String m_receiver;
	private String m_content;
	private int    m_spam; //1Ϊ�����ʼ� ,0Ϊ�����ʼ�
	private int    m_read; //1Ϊ�Ѷ��� 0Ϊδ����  Ĭ��Ϊ0
	
	public Mail()
	{
		//�չ��캯��,�������getter��setter������������
	}
	
	
//	//����Ĳ�������
//	public String getMailBoxName()
//	{
//		//�����ʼ������ļ���
//		IMailDao mailDao = new MailDao();
//		String mailBoxName = mailDao.getMailBox(this);
//		return mailBoxName;
//	}
	
	
	//==========getter �� setter==========================
	public int getM_id() {
		return m_id;
	}
	public void setM_id(int m_id){
		this.m_id=m_id;
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

