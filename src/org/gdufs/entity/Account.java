package org.gdufs.entity;

/**
 * ” œ‰’Àªß±Ì
 * @author Administrator
 *
 */
public class Account {

	private int a_id;
	private String a_account;
	private String a_passwd;
	
	public Account()
	{
		//
	}
	
	//getter and setter
	public int getA_id() {
		return a_id;
	}
	public void setA_id(int id){
		this.a_id = id;
	}
	public String getA_account() {
		return a_account;
	}
	public void setA_account(String a_account) {
		this.a_account = a_account;
	}
	public String getA_passwd() {
		return a_passwd;
	}
	public void setA_passwd(String a_passwd) {
		this.a_passwd = a_passwd;
	}

	@Override
	public String toString() {
		return "Account [a_id=" + a_id + ", a_account=" + a_account
				+ ", a_passwd=" + a_passwd + "]";
	}
	
	
	

}
