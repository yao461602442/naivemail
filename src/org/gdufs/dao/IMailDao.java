package org.gdufs.dao;
/**
 * 定义邮件对象操作接口 
 */
import org.gdufs.entity.*;
import java.util.*;

public interface IMailDao {
	
	/**
	 * 查询邮件对象所属文件夹
	 * @param mail
	 * @return 返回邮件对象所属文件夹名
	 */
	public int getMailBoxName(Mail mail);
	
	/**
	 * 更新邮件
	 * @param mail
	 * @return 返回受影响行数，返回0为更新失败，返回1更新成功
	 */
	public int updateMail(Mail mail);
	
	/**
	 * 插入一封邮件
	 * @param mail
	 * @return 返回受影响行数，返回0为插入失败，返回1插入成功
	 */	
	public int insertMail(Mail mail);
	
	/**
	 * 插入一批邮件，这个函数在插入操作上要做一定的优化
	 * @param list 邮件列表
	 * @return 返回受影响行数，返回0为插入失败，返回1表示插入成功
	 */
	public int insertBatchMail(List<Mail> list);
	
	/**
	 * 删除指定邮件对象
	 * @param mailId
	 * @return 返回受影响行数，返回0为删除失败，返回1删除成功
	 */
	public int deleteMail(int mailId); //这里的删除跟移动到垃圾箱不同，属于永久删除
	
	/**
	 * 获取指定账户中指定文件夹的所有邮件列表对象
	 * @param box 文件夹名。 现有的有： 收件箱，垃圾箱，草稿箱，发件箱
	 * @return 对应文件夹里面所有的邮件列表
	 */	
	public List<Mail> queryBoxAll(Account account, int box);
	
	/**
	 * 获取指定邮件对象
	 * @param mailId
	 * @return 邮件对象
	 */
	public Mail queryMail(int mailId);
	
	/**
	 * 检查是否为重复邮件
	 * @param subject 邮件主题
	 * @param from 发件人
	 * @param sentTime 发送时间
	 * @return
	 */
	public int checkUnique(String subject, String from, String sentTime);
        
        /**
         * 根据关键字搜索邮件
         * @param account
         * @param key
         * @return 
         */
        public List<Mail> searchMail(Account account, String key);
	
}
