package org.gdufs.service;

import org.gdufs.entity.*;
import java.util.*;
/**
 * �������ӿ�
 * @author Administrator
 *
 */
public interface IClassifier {
	/**
	 * Ϊһ���ʼ�����
	 * @param mail
	 * @return ����1����ɹ�������0����ʧ��
	 */
	public int categoryOneMail(Mail mail);
	
	/**
	 * Ϊһ���ʼ�����
	 * @param list �ʼ��б�
	 * @return ����0����ʧ�ܣ����෵�ط����ʼ�������Ŀ
	 */
	public int categoryBatchMail(List<Mail> list);
}
