package org.gdufs.pub;

/**
 * 去除邮件正文中的html标签
 * @author Administrator
 *
 */
public class TagFilter {
	public static String removeHtmlTag(String content){
		return content.replaceAll("<.*?>", "");
	}
}
