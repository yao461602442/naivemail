package org.gdufs.pub;

import org.gdufs.entity.*;
import java.util.*;
/**
 * 分词接口
 * @author Administrator
 *
 */
public interface IWordSplit {

	/**
	 * 把目标串分词，返回分词后的字符串
	 * @param content
	 * @return 分词后的字符串
	 */
	public String splitToString(String content);
	
	/**
	 * 把目标串分词，返回分词后的字符串列表
	 * @param content
	 * @return
	 */
	public List<String> splitToList(String content);
}
