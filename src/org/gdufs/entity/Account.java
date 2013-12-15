package org.gdufs.entity;

/**
 * 邮箱账户表
 *
 * @author Administrator
 *
 */
public class Account {

    private int a_id;
    private String a_account;
    private String a_passwd;
    private int autoReceive; //自动检查更新
    private int checkTime; //检查更新的时间间隔

    public Account() {
        //
    }

    //getter and setter
    public int getA_id() {
        return a_id;
    }

    public void setA_id(int id) {
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

    public int getAutoReceive() {
        return autoReceive;
    }

    public void setAutoReceive(int autoReceive) {
        this.autoReceive = autoReceive;
    }

    public int getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(int checkTime) {
        this.checkTime = checkTime;
    }

    @Override
    public String toString() {
        return "Account{" + "a_id=" + a_id + ", a_account=" + a_account + ", a_passwd=" + a_passwd + ", autoReceive=" + autoReceive + ", checkTime=" + checkTime + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.a_id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Account other = (Account) obj;
        if (this.a_id != other.a_id) {
            return false;
        }
        return true;
    }


}
