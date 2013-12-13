package org.gdufs.dao;

import org.gdufs.entity.*;
/**
 * 定义账户对象操作接口
 * @author Administrator
 *
 */

public interface IAccountDao  {

	/**
	 * 插入一个邮箱账号，插入前必须保证账户不存在。
	 * @param account
	 * @return 返回受影响行数，返回0为插入失败，返回1插入成功
	 */
	public int insertAccount(Account account);
	
	/**
	 * 更新邮箱账号
	 * @param account
	 * @return 返回受影响行数，返回0为更新失败，返回1更新成功
	 */
	public int updateAccount(Account account);
	
	/**
	 * 删除指定id的邮箱账号
	 * @param a_id
	 * @return 返回受影响行数，返回0为删除失败，返回1删除成功
	 */
	public int deleteAccount(Account account);
	
	/**
	 * 获取指定邮箱账号
	 * @param account 账户名
	 * @param pwd 密码
	 * @return 邮箱对象，若查找不到则返回Null
	 */
	public Account queryAccount(String account, String pwd);
        
        /**
         * 获取指定邮箱账号
         * @param id 账号id
         * @return 邮箱对象，若找不到则返回null
         */
        public Account queryAccount(int id);
	
	/**
	 * 检验邮箱账号是否存在。 仅仅检查存在性！
	 * @param account
	 * @param pwd
	 * @return 返回查询结果，1表示查询成功，0表示查询失败
	 */
	public int checkAccount(String account, String pwd);
        
        /**
         * 查询是否存在默认账号
         * @return 若存在则返回账号id，不存在返回0
         */
        public int defaultAccount();
            
        

}
