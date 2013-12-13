package org.gdufs.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.gdufs.dao.IAccountDao;
import org.gdufs.entity.Account;
import org.gdufs.pub.DBUtil;

public class AccountDao implements IAccountDao {

	@Override
	public int insertAccount(Account account) {
		if(account==null)
			return 0;
		if(queryAccount(account.getA_account(), account.getA_passwd())!=null){
			return 0;
		}
		Connection conn = null;		
		PreparedStatement pstat = null;
		int ret = 0;
		Account a = null;
		String sql = "insert into account (a_account, a_passwd) values(?,?)";
		try {
			conn = DBUtil.getConnection();
			pstat = conn.prepareStatement(sql);
			pstat.setString(1,account.getA_account());
			pstat.setString(2, account.getA_passwd());
			ret = pstat.executeUpdate();
			pstat.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}
		return ret;
	}

	@Override
	public int updateAccount(Account account) {
		if(account==null)
			return 0;
		Connection conn = null;		
		PreparedStatement pstat = null;
		int ret = 0;
		Account a = null;
		String sql = "update account set a_passwd=? where a_id=?";
		try {
			conn = DBUtil.getConnection();
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, account.getA_passwd());
			pstat.setInt(2, account.getA_id());
			ret = pstat.executeUpdate();
			
			pstat.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public int deleteAccount(Account account) {
		if(account==null)
			return 0;
		Connection conn = null;		
		PreparedStatement pstat = null;
		int ret = 0;
		Account a = null;
		String sql = "delete from account where a_id=?";
		try {
			conn = DBUtil.getConnection();
			pstat = conn.prepareStatement(sql);
			pstat.setInt(1, account.getA_id());			
			ret = pstat.executeUpdate();
			pstat.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public Account queryAccount(String account, String pwd) {
		if(account==null || pwd==null)
			return null;
		Connection conn = null;		
		PreparedStatement pstat = null;
		ResultSet rs = null;
		Account a = null;
		String sql = "select * from account where a_account=? and a_passwd = ?";
		try {
			conn = DBUtil.getConnection();
			pstat = conn.prepareStatement(sql);
			pstat.setString(1,account);
			pstat.setString(2, pwd);
			rs = pstat.executeQuery();
			//ªÒ»°’Àªß
			if(rs.next()){
				a = new Account();
				a.setA_id(rs.getInt(1));
				a.setA_account(rs.getString(2));
				a.setA_passwd(rs.getString(3));
			}
			pstat.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}

	@Override
	public int checkAccount(String account, String pwd) {
		Account a = this.queryAccount(account, pwd);
		if(a!=null){
			return 1;
		}
		return 0;
	}
	

    @Override
    public int defaultAccount() {
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        int ret = 0;
        try{
            String sql = "select a_id from login";
            pstat = conn.prepareStatement(sql);
            rs = pstat.executeQuery();
            if(rs.next()){
                ret = rs.getInt("a_id");
            }            
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            DBUtil.close(pstat);
            DBUtil.close(conn);
        }
        return ret;
    }

    @Override
    public Account queryAccount(int id) {
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        Account account = null;
        try{
            String sql = "select a_account, a_passwd from account where a_id=?";            
            pstat = conn.prepareStatement(sql);
            rs = pstat.executeQuery();
            if(rs.next()){
                account.setA_account(rs.getString("a_account"));
                account.setA_id(id);
                account.setA_passwd("a_passwd");                
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            DBUtil.close(pstat);
            DBUtil.close(conn);
        }
        return account;
    }

}
