package org.gdufs.entity;
/**
 * 邮箱文件夹表
 * @author Administrator
 *
 */
public class MailBox {
	
	public static final int INBOX = 1;
	public static final int DeleteMessage = 2;
	public static final int DRAFTS = 3;
	public static final int SentMessage = 4;
	

	private int    b_id;
	private String b_name;
	
	public MailBox()
	{
		//空构造函数
	}
	
	//=========getter==================
	public int getB_id() {
		return b_id;
	}

	public String getB_name() {
		return b_name;
	}
	//================================================
	

}
