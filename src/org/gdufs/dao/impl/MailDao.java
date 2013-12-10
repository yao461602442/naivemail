package org.gdufs.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.gdufs.dao.IMailDao;
import org.gdufs.entity.Mail;
import org.gdufs.pub.DBUtil;

public class MailDao implements IMailDao {

	@Override
	public String getMailBox(Mail mail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateMail(Mail mail) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertMail(Mail mail) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteMail(int mailId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Mail> queryAll(String box) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mail queryMail(int mailId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insertBatchMail(List<Mail> list) {
		int ret = 0;
		Connection conn = DBUtil.getConnection();
		PreparedStatement prep = null;
		String sql = "insert into mail (a_id, b_id, m_title, m_time, m_sender, " +
				"m_receiver, m_content, m_spam, m_read) values(?,?,?,?,?,?,?,?,?)";
		try {			
			prep = conn.prepareStatement(sql);
			for (Iterator it = list.iterator(); it.hasNext();) {
				Mail mail = (Mail)it.next();
				if(mail.getA_id()==0 || mail.getB_id()==0){
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
			int[] tmp =prep.executeBatch();
			// πÿ±’¡¨Ω”
			prep.close();
			conn.close();
			for(int i=0; i<tmp.length; ++i)
				ret+=tmp[i];
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return ret;	
	}

}
