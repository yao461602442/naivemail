package org.gdufs.dao;

import org.gdufs.entity.*;
/**
 * �����˻���������ӿ�
 * @author Administrator
 *
 */

public interface IAccountDao  {

	/**
	 * ����һ�������˺ţ�����ǰ���뱣֤�˻������ڡ�
	 * @param account
	 * @return ������Ӱ������������0Ϊ����ʧ�ܣ�����1����ɹ�
	 */
	public int insertAccount(Account account);
	
	/**
	 * ���������˺�
	 * @param account
	 * @return ������Ӱ������������0Ϊ����ʧ�ܣ�����1���³ɹ�
	 */
	public int updateAccount(Account account);
	
	/**
	 * ɾ��ָ��id�������˺�
	 * @param a_id
	 * @return ������Ӱ������������0Ϊɾ��ʧ�ܣ�����1ɾ���ɹ�
	 */
	public int deleteAccount(Account account);
	
	/**
	 * ��ȡָ�������˺�
	 * @param account �˻���
	 * @param pwd ����
	 * @return ������������Ҳ����򷵻�Null
	 */
	public Account queryAccount(String account, String pwd);
	
	/**
	 * ���������˺��Ƿ���ڡ� �����������ԣ�
	 * @param account
	 * @param pwd
	 * @return ���ز�ѯ�����1��ʾ��ѯ�ɹ���0��ʾ��ѯʧ��
	 */
	public int checkAccount(String account, String pwd);

}
