package org.gdufs.dao;
/**
 * �����ʼ���������ӿ� 
 */
import org.gdufs.entity.*;
import java.util.*;

public interface IMailDao {
	
	/**
	 * ��ѯ�ʼ����������ļ���
	 * @param mail
	 * @return �����ʼ����������ļ�����
	 */
	public String getMailBox(Mail mail);
	
	/**
	 * �����ʼ�
	 * @param mail
	 * @return ������Ӱ������������0Ϊ����ʧ�ܣ�����1���³ɹ�
	 */
	public int updateMail(Mail mail);
	
	/**
	 * ����һ���ʼ�
	 * @param mail
	 * @return ������Ӱ������������0Ϊ����ʧ�ܣ�����1����ɹ�
	 */	
	public int insertMail(Mail mail);
	
	/**
	 * ����һ���ʼ�����������ڲ��������Ҫ��һ�����Ż�
	 * @param list �ʼ��б�
	 * @return ������Ӱ������������0Ϊ����ʧ�ܣ�����1��ʾ����ɹ�
	 */
	public int insertBatchMail(List<Mail> list);
	
	/**
	 * ɾ��ָ���ʼ�����
	 * @param mailId
	 * @return ������Ӱ������������0Ϊɾ��ʧ�ܣ�����1ɾ���ɹ�
	 */
	public int deleteMail(int mailId); //�����ɾ�����ƶ��������䲻ͬ����������ɾ��
	
	/**
	 * ��ȡָ���ļ��е������ʼ��б�����
	 * @param box �ļ������� ���е��У� �ռ��䣬�����䣬�ݸ��䣬������
	 * @return ��Ӧ�ļ����������е��ʼ��б�
	 */	
	public List<Mail> queryAll(String box);
	
	/**
	 * ��ȡָ���ʼ�����
	 * @param mailId
	 * @return �ʼ�����
	 */
	public Mail queryMail(int mailId);
	
}