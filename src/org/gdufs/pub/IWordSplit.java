package org.gdufs.pub;

import org.gdufs.entity.*;
import java.util.*;
/**
 * �ִʽӿ�
 * @author Administrator
 *
 */
public interface IWordSplit {

	/**
	 * ��Ŀ�괮�ִʣ����طִʺ���ַ���
	 * @param content
	 * @return �ִʺ���ַ���
	 */
	public String splitToString(String content);
	
	/**
	 * ��Ŀ�괮�ִʣ����طִʺ���ַ����б�
	 * @param content
	 * @return
	 */
	public List<String> splitToList(String content);
}
