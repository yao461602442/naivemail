package org.gdufs.dao;

import java.util.List;
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
         * ��ȡָ�������˺�
         * @param id �˺�id
         * @return ����������Ҳ����򷵻�null
         */
        public Account queryAccount(int id);
	
	/**
	 * ���������˺��Ƿ���ڡ� �����������ԣ�
	 * @param account
	 * @param pwd
	 * @return ���ز�ѯ�����1��ʾ��ѯ�ɹ���0��ʾ��ѯʧ��
	 */
	public int checkAccount(String account, String pwd);
        
        /**
         * ��ѯ�Ƿ����Ĭ���˺�
         * @return �������򷵻��˺�id�������ڷ���0
         */
        public int defaultAccount();
        
        /**
         * ��ȡ���ݿ��������˺�
         * @return 
         */
        public List<Account> getAllAccount();
            
}
