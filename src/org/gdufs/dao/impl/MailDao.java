package org.gdufs.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gdufs.dao.IMailDao;
import org.gdufs.entity.Account;
import org.gdufs.entity.Mail;
import org.gdufs.entity.MailBox;
import org.gdufs.pub.AccountHandler;
import org.gdufs.pub.DBUtil;

public class MailDao implements IMailDao {

    @Override
    public int updateMail(Mail mail) {
        int ret = 0;
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        String sql = "update mail set b_id=?,m_content=?, m_read=?, m_receiver=?,"
                + "m_sender=?,m_time=?,m_spam=?,m_title=? where m_id=?";
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, mail.getB_id());
            pstat.setString(2, mail.getM_content());
            pstat.setInt(3, mail.getM_read());
            pstat.setString(4, mail.getM_receiver());
            pstat.setString(5, mail.getM_sender());
            pstat.setString(6, mail.getM_time());
            pstat.setInt(7, mail.getM_spam());
            pstat.setString(8, mail.getM_title());
            pstat.setInt(9, mail.getM_id());
            ret = pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            DBUtil.close(pstat);
            DBUtil.close(conn);
        }
        return ret;
    }

    @Override
    public int insertMail(Mail mail) {
        int ret = 0;
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        String sql = "insert into mail (b_id,m_content,m_read,m_receiver,"
                + "m_sender,m_time,m_spam,m_title, a_id) values(?,?,?,?,?,?,?,?,?)";
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, mail.getB_id());
            pstat.setString(2, mail.getM_content());
            pstat.setInt(3, mail.getM_read());
            pstat.setString(4, mail.getM_receiver());
            pstat.setString(5, mail.getM_sender());
            pstat.setString(6, mail.getM_time());
            pstat.setInt(7, mail.getM_spam());
            pstat.setString(8, mail.getM_title());
            pstat.setInt(9, mail.getA_id());

            ret = pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            DBUtil.close(pstat);
            DBUtil.close(conn);
        }
        return ret;
    }

    @Override
    public int deleteMail(int mailId) {
        int ret = 0;
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        String sql = "delete from mail where m_id=?";
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, mailId);
            ret = pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            DBUtil.close(pstat);
            DBUtil.close(conn);
        }
        return ret;
    }

    @Override
    public List<Mail> queryBoxAll(Account account, int box) {
        List<Mail> mailList = new ArrayList<Mail>();
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        String sql = "select m_id,a_id,b_id,m_content,m_read,m_receiver,"
                + "m_sender,m_time,m_spam,m_title from mail where a_id=? and b_id=?";
        Mail mail = null;
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, account.getA_id());
            pstat.setInt(2, box);
            rs = pstat.executeQuery();
            while (rs.next()) {

                mail = new Mail();
                mail.setM_id(rs.getInt("m_id"));
                mail.setA_id(rs.getInt("a_id"));
                mail.setB_id(rs.getInt("b_id"));
                mail.setM_content(rs.getString("m_content"));
                mail.setM_read(rs.getInt("m_read"));
                mail.setM_receiver(rs.getString("m_receiver"));
                mail.setM_sender(rs.getString("m_sender"));
                mail.setM_time(rs.getString("m_time"));
                mail.setM_spam(rs.getInt("m_spam"));
                mail.setM_title(rs.getString("m_title"));
                mailList.add(mail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            DBUtil.close(pstat);
            DBUtil.close(conn);
        }
        return mailList;
    }

    @Override
    public Mail queryMail(int mailId) {
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        String sql = "select * from mail where m_id=?";
        Mail mail = null;
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, mailId);
            rs = pstat.executeQuery();
            if (rs.next()) {
                mail = new Mail();
                mail.setA_id(rs.getInt("a_id"));
                mail.setB_id(rs.getInt("b_id"));
                mail.setM_content(rs.getString("m_content"));
                mail.setM_read(rs.getInt("m_read"));
                mail.setM_receiver(rs.getString("m_receiver"));
                mail.setM_sender(rs.getString("m_sender"));
                mail.setM_time(rs.getString("m_time"));
                mail.setM_spam(rs.getInt("m_spam"));
                mail.setM_title(rs.getString("m_title"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  finally{
            DBUtil.close(pstat);
            DBUtil.close(conn);
        }
        return mail;
    }

    @Override
    public int insertBatchMail(List<Mail> list) {
        int ret = 0;
        if (list == null || list.size() == 0) {
            return ret;
        }
        Connection conn = DBUtil.getConnection();
        PreparedStatement prep = null;
        String sql = "insert into mail (a_id, b_id, m_title, m_time, m_sender, "
                + "m_receiver, m_content, m_spam, m_read) values(?,?,?,?,?,?,?,?,?)";
        try {
            prep = conn.prepareStatement(sql);
            for (Iterator it = list.iterator(); it.hasNext();) {
                Mail mail = (Mail) it.next();
                if (mail.getA_id() == 0 || mail.getB_id() == 0) {
                    continue;
                }
                prep.setInt(1, mail.getA_id());
                prep.setInt(2, mail.getB_id());
                prep.setString(3, mail.getM_title());
                prep.setString(4, mail.getM_time());
                prep.setString(5, mail.getM_sender());
                prep.setString(6, mail.getM_receiver());
                prep.setString(7, mail.getM_content());
                prep.setInt(8, mail.getM_spam());
                prep.setInt(9, mail.getM_read());
                prep.addBatch();
            }
            int[] tmp = prep.executeBatch();
            
            for (int i = 0; i < tmp.length; ++i) {
                ret += tmp[i];
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }  finally{
            DBUtil.close(prep);
            DBUtil.close(conn);
        }
        return ret;
    }

    @Override
    public int checkUnique(String subject, String from, String sentTime) {
        int ret = 0;
        Connection conn = null;
        PreparedStatement prep = null;
        String sql = "select * from mail where m_title=? and m_sender=? and m_time=?";
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            prep = conn.prepareStatement(sql);
            prep.setString(1, subject);
            prep.setString(2, from);
            prep.setString(3, sentTime);
            rs = prep.executeQuery();
            if (rs.next()) {
                ret = 1;
            }           
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            DBUtil.close(prep);
            DBUtil.close(conn);
        }
        return ret;
    }



    @Override
    public int getMailBoxName(Mail mail) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Mail> searchMail(Account account, String key) {
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        String sql = "select * from mail where a_id=? and (m_title like ? or m_content like ?)";
        List<Mail> list = new ArrayList<Mail>();
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, account.getA_id());
            pstat.setString(2, "%"+key+"%");
            pstat.setString(3, "%"+key+"%");
            rs = pstat.executeQuery();
            while (rs.next()) {
                Mail mail = new Mail();
                mail.setA_id(rs.getInt("a_id"));
                mail.setB_id(rs.getInt("b_id"));
                mail.setM_content(rs.getString("m_content"));
                mail.setM_read(rs.getInt("m_read"));
                mail.setM_receiver(rs.getString("m_receiver"));
                mail.setM_sender(rs.getString("m_sender"));
                mail.setM_time(rs.getString("m_time"));
                mail.setM_spam(rs.getInt("m_spam"));
                mail.setM_title(rs.getString("m_title"));
                list.add(mail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            DBUtil.close(pstat);
            DBUtil.close(conn);
        }
        return list;
    }
    
    public static void main(String[] args) {

        
    }
}
