
import org.gdufs.dao.IAccountDao;
import org.gdufs.dao.impl.AccountDao;
import org.gdufs.view.MAIL;
import org.gdufs.view.login;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *  �������
 * @author Administrator
 */
public class Main {
    public static void main(String[] args){
        //��ѯ�Ƿ����Ĭ���˺ţ�������ֱ�ӽ��������򣬷���������¼����
        IAccountDao adao = new AccountDao();
        if(adao.defaultAccount() > 0){
            //����Ĭ���˺ţ�ֱ�ӽ�������������
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
