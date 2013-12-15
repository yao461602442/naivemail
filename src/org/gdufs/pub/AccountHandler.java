/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gdufs.pub;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.text.html.HTMLDocument;
import org.gdufs.dao.IAccountDao;
import org.gdufs.dao.impl.AccountDao;
import org.gdufs.entity.Account;

/**
 *
 * @author Administrator
 */
public class AccountHandler {

    private static Account account = null;
    private static String POPAddress = null;
    private static String SMTPAddress = null;

    static {
        //查询数据库中最新的登录的账号
        IAccountDao adao = new AccountDao();
        int id = adao.defaultAccount();
        if (id > 0) {
            System.out.println("id=" + id);
            account = adao.queryAccount(id);
            System.out.println(account);
        }
    }

    private AccountHandler() {
        //
    }

    public static Account getLoginAccount() {
        return account;
    }

    public static void setLoginAccount(Account a) {
        account = a;
        //过程：把登录表清空，插入最新登录的账号！
        //偷懒直接写！
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstat = null;
        try {
            //清掉里面的内容
            String sql = "delete from login";
            pstat = conn.prepareStatement(sql);
            pstat.execute();

            //插入最新登录的账号id
            sql = "insert into login (a_id) values(?)";
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, account.getA_id());
            pstat.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(pstat);
            DBUtil.close(conn);
        }
    }
}
