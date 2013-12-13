
import org.gdufs.dao.IAccountDao;
import org.gdufs.dao.impl.AccountDao;
import org.gdufs.view.MAIL;
import org.gdufs.view.login;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *  程序入口
 * @author Administrator
 */
public class Main {
    public static void main(String[] args){
        //查询是否存在默认账号，若有则直接进入主程序，否则启动登录界面
        IAccountDao adao = new AccountDao();
        if(adao.defaultAccount() > 0){
            //存在默认账号，直接进入邮箱主界面
            startMAIL();
        } else {
            startLogin();
        }        
       
    }
    
    private static void startMAIL(){
         java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MAIL().setVisible(true);
            }
        });
    }
    
    private static void startLogin(){
         java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new login().setVisible(true);
            }
        });
    }
}
