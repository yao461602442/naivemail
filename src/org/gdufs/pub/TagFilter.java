package org.gdufs.pub;

/**
 * ȥ���ʼ������е�html��ǩ
 * @author Administrator
 *
 */
public class TagFilter {
	public static String removeHtmlTag(String content){
		return content.replaceAll("<.*?>", "");
	}
}
