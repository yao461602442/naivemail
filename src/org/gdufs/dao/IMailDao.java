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
	public int getMailBoxName(Mail mail);
	
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
	 * ��ȡָ���˻���ָ���ļ��е������ʼ��б����
	 * @param box �ļ������� ���е��У� �ռ��䣬�����䣬�ݸ��䣬������
	 * @return ��Ӧ�ļ����������е��ʼ��б�
	 */	
	public List<Mail> queryBoxAll(Account account, int box);
	
	/**
	 * ��ȡָ���ʼ�����
	 * @param mailId
	 * @return �ʼ�����
	 */
	public Mail queryMail(int mailId);
	
	/**
	 * ����Ƿ�Ϊ�ظ��ʼ�
	 * @param subject �ʼ�����
	 * @param from ������
	 * @param sentTime ����ʱ��
	 * @return
	 */
	public int checkUnique(Account account, String subject, String from, String sentTime);
        
        /**
         * ���ݹؼ��������ʼ�
         * @param account
         * @param key
         * @return 
         */
        public List<Mail> searchMail(Account account, String key);
	
}
