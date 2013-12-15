package org.gdufs.entity;
/**
 * 邮箱文件夹表
 * @author Administrator
 *
 */
public class MailBox {
	
	public static final int INBOX = 1; //收件箱
	public static final int SPAMBOX = 2; //垃圾箱
	public static final int DRAFTS = 3; //草稿箱
	public static final int SENTBOX = 4; //已发送
        public static final int DELETEDBOX = 5; //已删除
	

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
