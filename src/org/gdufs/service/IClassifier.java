package org.gdufs.service;

import org.gdufs.entity.*;
import java.util.*;
/**
 * 分类器接口
 * @author Administrator
 *
 */
public interface IClassifier {
	/**
	 * 为一封邮件分类
	 * @param mail
	 * @return 返回1分类成功，返回0分类失败
	 */
	public int categoryOneMail(Mail mail);
	
	/**
	 * 为一批邮件分类
	 * @param list 邮件列表
	 * @return 返回0分类失败，其余返回分类邮件样本数目
	 */
	public int categoryBatchMail(List<Mail> list);
}
