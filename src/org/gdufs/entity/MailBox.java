package org.gdufs.entity;
/**
 * �����ļ��б�
 * @author Administrator
 *
 */
public class MailBox {
	
	public static final int INBOX = 1;
	public static final int SPAMBOX = 2;
	public static final int DRAFTS = 3;
	public static final int SENTBOX = 4;
        public static final int DELETEDBOX = 5;
	

	private int    b_id;
	private String b_name;
	
	public MailBox()
	{
		//�չ��캯��
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
