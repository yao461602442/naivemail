
import org.gdufs.controller.FrameFactory;
import org.gdufs.dao.IAccountDao;
import org.gdufs.dao.impl.AccountDao;
import org.gdufs.pub.AccountHandler;
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
        if(AccountHandler.getLoginAccount() != null){
            //����Ĭ���˺ţ�ֱ�ӽ�������������
            startMAIL();
        } else {
            startLogin();
        }        
       
    }
    
    private static void startMAIL(){
         java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrameFactory.getMainFrame().setFrame().setVisible(true);
            }
        });
    }
    
    private static void startLogin(){
         java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrameFactory.getLoginFrame().setVisible(true);
            }
        });
    }
}
