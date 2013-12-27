/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gdufs.view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.WindowConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.gdufs.controller.FrameFactory;
import org.gdufs.dao.IAccountDao;
import org.gdufs.dao.impl.AccountDao;
import org.gdufs.entity.Account;
import org.gdufs.pub.AccountHandler;

/**
 *
 * @author Administrator
 */
public class UserInfo extends javax.swing.JFrame {

    private List<Account> accountList = new ArrayList<Account>();

    /**
     * Creates new form UserInfo
     */
    public UserInfo() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //设置界面
        //setFrame();
        
    }
    
    public void setFrame(){
        //设置左界面
        Account user = AccountHandler.getLoginAccount();
        jTextFieldEmail.setText(user.getA_account());
        jPasswordFieldPwd.setText(user.getA_passwd());
        //设置时间间隔窗口只能输入5位以内数字
        this.jTextFieldReceiveSequence.setDocument(new NumberLenghtLimitedDmt(5));
        if (user.getAutoReceive() == 0) {
            jCheckBoxAutoReceive.setSelected(false);
            jTextFieldReceiveSequence.setEnabled(false);
        } else {
            jCheckBoxAutoReceive.setSelected(true);
            jTextFieldReceiveSequence.setText(user.getCheckTime() + "");
        }
        //设置右界面
        IAccountDao adao = new AccountDao();
        accountList = adao.getAllAccount();
        DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < accountList.size(); ++i) {
            model.addElement(accountList.get(i).getA_account());
        }
        jListAccount.setModel(model);
    }

    class NumberLenghtLimitedDmt extends PlainDocument {

        public NumberLenghtLimitedDmt(int limit) {
            super();
            this.limit = limit;
        }

        @Override
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null) {
                return;
            }
            if ((getLength() + str.length()) <= limit) {

                //以下的代码用于控制输入文本框的字符串类型是数字还是纯字母等
                char[] upper = str.toCharArray();
                String insert = "";
                int length = 0;
                for (int i = 0; i < upper.length; i++) {
                    if (upper[i] >= '0' && upper[i] <= '9') {
                        insert += upper[i];
                    }
                }
                super.insertString(offset, insert, attr);
            }
        }
        private int limit;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonNo = new javax.swing.JButton();
        jButtonYes = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel = new javax.swing.JPanel();
        jButtonSwitch = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonNew = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListAccount = new javax.swing.JList();
        jPanelDisplayInfo = new javax.swing.JPanel();
        jLabelEMail = new javax.swing.JLabel();
        jLabelPwd = new javax.swing.JLabel();
        jTextFieldEmail = new javax.swing.JTextField();
        jPasswordFieldPwd = new javax.swing.JPasswordField();
        jCheckBoxAutoReceive = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldReceiveSequence = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("用户信息");

        jButtonNo.setFont(new java.awt.Font("微软雅黑", 0, 14)); // NOI18N
        jButtonNo.setText("取消");
        jButtonNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNoActionPerformed(evt);
            }
        });

        jButtonYes.setFont(new java.awt.Font("微软雅黑", 0, 14)); // NOI18N
        jButtonYes.setText("确定");
        jButtonYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonYesActionPerformed(evt);
            }
        });

        jPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel.setName(""); // NOI18N

        jButtonSwitch.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jButtonSwitch.setText("切换");
        jButtonSwitch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSwitchActionPerformed(evt);
            }
        });

        jButtonDelete.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jButtonDelete.setText("删除");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jButtonNew.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jButtonNew.setText("新建");
        jButtonNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(jListAccount);

        javax.swing.GroupLayout jPanelLayout = new javax.swing.GroupLayout(jPanel);
        jPanel.setLayout(jPanelLayout);
        jPanelLayout.setHorizontalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanelLayout.createSequentialGroup()
                        .addComponent(jButtonNew)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSwitch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDelete)
                        .addGap(0, 5, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelLayout.setVerticalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSwitch)
                    .addComponent(jButtonDelete)
                    .addComponent(jButtonNew))
                .addContainerGap())
        );

        jSplitPane1.setRightComponent(jPanel);

        jPanelDisplayInfo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDisplayInfo.setForeground(new java.awt.Color(204, 204, 204));

        jLabelEMail.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jLabelEMail.setText("E-mail:");

        jLabelPwd.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jLabelPwd.setText("密码:");

        jTextFieldEmail.setText("这里会自动显示用户登录的邮箱。。。");

        jPasswordFieldPwd.setText("adfladj");
        jPasswordFieldPwd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldPwdActionPerformed(evt);
            }
        });

        jCheckBoxAutoReceive.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jCheckBoxAutoReceive.setText("自动收取邮件");
        jCheckBoxAutoReceive.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxAutoReceiveStateChanged(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jLabel1.setText("每隔");

        jLabel2.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jLabel2.setText("分钟");

        javax.swing.GroupLayout jPanelDisplayInfoLayout = new javax.swing.GroupLayout(jPanelDisplayInfo);
        jPanelDisplayInfo.setLayout(jPanelDisplayInfoLayout);
        jPanelDisplayInfoLayout.setHorizontalGroup(
            jPanelDisplayInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDisplayInfoLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanelDisplayInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelPwd)
                    .addComponent(jLabelEMail))
                .addGap(18, 18, 18)
                .addGroup(jPanelDisplayInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelDisplayInfoLayout.createSequentialGroup()
                        .addComponent(jCheckBoxAutoReceive)
                        .addGap(42, 42, 42)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldReceiveSequence, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addComponent(jTextFieldEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                    .addComponent(jPasswordFieldPwd))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanelDisplayInfoLayout.setVerticalGroup(
            jPanelDisplayInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDisplayInfoLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanelDisplayInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEMail)
                    .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelDisplayInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPwd)
                    .addComponent(jPasswordFieldPwd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelDisplayInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxAutoReceive)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldReceiveSequence, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(jPanelDisplayInfo);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(238, 238, 238)
                        .addComponent(jButtonYes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(251, 251, 251)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jSplitPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonYes)
                    .addComponent(jButtonNo))
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jPasswordFieldPwdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordFieldPwdActionPerformed
        // TODO add your handling code jButtonYes}//GEN-LAST:event_jPasswordFieldPwdActionPerformed
    }
        private void jButtonNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewActionPerformed
            // 创建新账号
            FrameFactory.getLoginFrame().setVisible(true);
            //this.dispose();
    }//GEN-LAST:event_jButtonNewActionPerformed

    private void jCheckBoxAutoReceiveStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxAutoReceiveStateChanged
        // TODO add your handling code here:
        if (jCheckBoxAutoReceive.isSelected()) {
            jTextFieldReceiveSequence.setEnabled(true);
        } else {
            jTextFieldReceiveSequence.setEnabled(false);
        }
    }//GEN-LAST:event_jCheckBoxAutoReceiveStateChanged

    private void jButtonYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonYesActionPerformed
        // TODO add your handling code here:
        if(jCheckBoxAutoReceive.isSelected()){
            int gap = Integer.parseInt(jTextFieldReceiveSequence.getText());
            IAccountDao adao = new AccountDao();
            Account a = AccountHandler.getLoginAccount();
            a.setAutoReceive(1);
            a.setCheckTime(gap);
            System.out.println(adao.updateAccount(a));
            AccountHandler.setLoginAccount(a);//更新后立马设置account
        }
        this.setVisible(false);
    }//GEN-LAST:event_jButtonYesActionPerformed

    private void jButtonSwitchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSwitchActionPerformed
        // 切换账号
        int index = jListAccount.getSelectedIndex();
        if(index<0){
            return ;
        }
        Account a = accountList.get(index);
        if(a.equals(AccountHandler.getLoginAccount())){
            return ;
        }
        AccountHandler.setLoginAccount(a);
        this.setFrame();
        FrameFactory.getMainFrame().setFrame();
    }//GEN-LAST:event_jButtonSwitchActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        // 删除所选账号
        int index = jListAccount.getSelectedIndex();
        if(index<0){
            return ;
        }
        Account a = accountList.get(index);
        if(a.equals(AccountHandler.getLoginAccount())){
            JOptionPane.showMessageDialog(this, "不可删除当前账户");
            return;
        }
        int type = JOptionPane.showConfirmDialog(this, "确认删除 "+a.getA_account()+" 吗？", "删除", JOptionPane.YES_NO_OPTION);
        if (type == JOptionPane.YES_OPTION) {
            IAccountDao adao = new AccountDao();
            adao.deleteAccount(a);
            this.setFrame();
        }
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNoActionPerformed
        // 取消按钮
        this.setVisible(false);
    }//GEN-LAST:event_jButtonNoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UserInfo().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonNew;
    private javax.swing.JButton jButtonNo;
    private javax.swing.JButton jButtonSwitch;
    private javax.swing.JButton jButtonYes;
    private javax.swing.JCheckBox jCheckBoxAutoReceive;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelEMail;
    private javax.swing.JLabel jLabelPwd;
    private javax.swing.JList jListAccount;
    private javax.swing.JPanel jPanel;
    private javax.swing.JPanel jPanelDisplayInfo;
    private javax.swing.JPasswordField jPasswordFieldPwd;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldReceiveSequence;
    // End of variables declaration//GEN-END:variables
}
