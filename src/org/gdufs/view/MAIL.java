/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gdufs.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import org.gdufs.controller.FrameFactory;
import org.gdufs.dao.IMailDao;
import org.gdufs.dao.impl.MailDao;
import org.gdufs.entity.Account;
import org.gdufs.entity.Mail;
import org.gdufs.entity.MailBox;
import org.gdufs.entity.MailServiceAddress;
import org.gdufs.pub.AccountHandler;
import org.gdufs.pub.MailServiceFactory;
import org.gdufs.pub.MailSorter;
import org.gdufs.service.IClassifier;
import org.gdufs.service.IMailService;
import org.gdufs.service.impl.Classifier;

/**
 *
 * @author Administrator
 */
public class MAIL extends javax.swing.JFrame {

    private List<Mail> mailList = null;
    private DefaultListModel mailListModel = new DefaultListModel();
    private ReceiveMailWorker receiveWorker = null;

    /**
     * Creates new form MAIL
     */
    public MAIL() {
        initComponents();
        //�����������С����
//        double heightAdjustBefore = this.getHeight();
//        Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
//        double heightAdjustNow = screenSize.getHeight()*0.9;
//        Dimension adjust = new Dimension();
//        adjust.setSize(this.getWidth(), heightAdjustNow);
//        this.setSize(adjust);
        //�������������С
//        double ratio = heightAdjustNow/heightAdjustBefore;
//        double treeMailBoxHeight = jTreeMailBox.getHeight()*ratio;
//        double listMailHeight = jListMailList.getHeight()*ratio;
//        double textAreaContentHeight = jTextAreaContent.getHeight()*ratio;
//        Dimension treeD = new Dimension();
//        treeD.setSize(jTreeMailBox.getWidth(),treeMailBoxHeight);
//        jTreeMailBox.setSize(treeD);
//        Dimension listD = new Dimension();
//        listD.setSize(jListMailList.getWidth(),listMailHeight);
//        jListMailList.setSize(listD);
        
        //��������
        this.jTextAreaContent.setEditable(false);
        this.setResizable(false);
        jButtonDelete.setEnabled(false);
        jButtonReply.setEnabled(false);
        receiveWorker = new ReceiveMailWorker(this, jButtonReceiveMail);
        jListMailList.setCellRenderer(new MyRenderer());
        this.setLocationRelativeTo(null);
    }

    class MyRenderer extends DefaultListCellRenderer {

        private Font font1;
        public MyRenderer() {
            this.font1 = getFont();
            font1 = font1.deriveFont(Font.BOLD);
        }
        
        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Summary m = (Summary)value;
            if(m.getRead()==0){
                setFont(font1);
            }            
            return this;
        }
    }
    
    /**
     * �б������
     */
    class Summary{
        private String subject;
        private String time;
        private int read;
        public Summary(String subject, String time, int read){
            this.subject = subject;
            this.time = time;
            this.read = read;
        }

        public int getRead() {
            return read;
        }
        
        @Override
        public String toString(){
            if (subject.length() > 10) {
                subject = subject.substring(0, 10);
                subject += "...";
            }
            String message = "";
            if (subject == null || subject.isEmpty()) {
                subject = "<������>";
            }
            if (time == null || time.isEmpty()) {
                time = "";
            }
            message = (subject + "  " + time);
            return message;
        }
    }

    public MAIL setFrame() {
        IMailDao mdao = new MailDao();
        Account a = AccountHandler.getLoginAccount();
        mailList = mdao.queryBoxAll(a, MailBox.INBOX);
        MailSorter.sortByTime(mailList);
        refreshJList();
        //jListMailList.setFont(new Font(null, Font.BOLD, 20));
        return this;
    }

    /**
     * ˢ���ʼ��б�JList
     *
     * @return
     */
    private void refreshJList() {
        //1.���JList������
        mailListModel.clear();
        for (int i = 0; i < mailList.size(); ++i) {
            String subject = mailList.get(i).getM_title();
            String time = mailList.get(i).getM_time();
            int read = mailList.get(i).getM_read();
            Summary summary = new Summary(subject, time, read);
            mailListModel.addElement(summary);
        }
        //System.out.println("mailListModel size : " + mailListModel.getSize());
        //�����ʼ��б�����       
        jListMailList.setModel(mailListModel);
        //2.���ñ�ѡ��״̬���Լ��ұ�չʾ
        int selectedIndex = jListMailList.getSelectedIndex();
        if(selectedIndex==-1){
            //����Ϊ��һ��Ĭ��չʾ
            if (mailList.size() > 0) {
                this.setShowMail(mailList.get(0));
                jListMailList.setSelectedIndex(0);
             }else{
            	 //mailListû����
            	 this.setShowMail(new Mail());
             }
        } else{
            this.setShowMail(mailList.get(selectedIndex));            
        }        
    }

    private void setShowMail(Mail m) {
        if (m == null) {
            return;
        }
        jLabelSubject.setText(m.getM_title());
        jLabelFrom.setText(m.getM_sender());
        jLabelTo.setText(m.getM_receiver());
        jLabelTime.setText(m.getM_time());
        jTextAreaContent.setText(m.getM_content());
        
        //ѡ�����ض��ʼ�������ɾ����ת�����ظ���ť����!�ݸ����лظ���ť������
        if(m.getB_id() != MailBox.DRAFTS){
            jButtonReply.setEnabled(true);
        }
        jButtonDelete.setEnabled(true);
        
    }

    /**
     * �ʼ���ȡ�߳�
     */
    class ReceiveMailWorker extends SwingWorker<String, Object> {

        private MAIL frame = null;
        private JButton button = null;

        public ReceiveMailWorker(MAIL frame, JButton button) {
            this.frame = frame;
            this.button = button;
        }

        @Override
        protected String doInBackground() throws Exception {
            //1.��ȡ�ʼ�
            button.setEnabled(false);
            Account user = AccountHandler.getLoginAccount();
            System.out.println(user);
            IMailService mailService = MailServiceFactory.getMailService();
            String server = MailServiceAddress.getPopAddress(user.getA_account());
            frame.setTitle("NaiveMail   ������ȡ�ʼ���..");
            List<Mail> mailList = mailService.getRecentMail(server, user);

            //2.�����ʼ�����
            frame.setTitle("NaiveMail   ��ȡ�ʼ���ɣ�����ʶ�������ʼ�..");
            int length = mailList.size();
            IClassifier classfier = new Classifier();
            classfier.categoryBatchMail(mailList);
            //3.�������ʼ������ݿ���
            frame.setTitle("NaiveMail   ʶ����ɣ����ڱ���������..");
            IMailDao mdao = new MailDao();
            mdao.insertBatchMail(mailList);
            return null;
        }

        @Override
        public void done() {
            frame.setTitle("NaiveMail");
            button.setEnabled(true);
            //ˢ�½���            
            frame.setFrame();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPanel1 = new javax.swing.JPanel();
        jButtonReceiveMail = new javax.swing.JButton();
        jButtonWriteMail = new javax.swing.JButton();
        jButtonReply = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonSearch = new javax.swing.JButton();
        jButtonUserManager = new javax.swing.JButton();
        jTextFieldSearch = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTreeMailBox = new javax.swing.JTree();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListMailList = new javax.swing.JList();
        jPanel5 = new javax.swing.JPanel();
        jLabelFrom = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabelTo = new javax.swing.JLabel();
        jLabelTime = new javax.swing.JLabel();
        jLabelSubject = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextAreaContent = new javax.swing.JTextArea();
        jSeparator1 = new javax.swing.JSeparator();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("NaiveMail");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jButtonReceiveMail.setFont(new java.awt.Font("΢���ź�", 0, 14)); // NOI18N
        jButtonReceiveMail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/getmail.png"))); // NOI18N
        jButtonReceiveMail.setText("��ȡ");
        jButtonReceiveMail.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonReceiveMail.setMargin(new java.awt.Insets(4, 4, 4, 4));
        jButtonReceiveMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReceiveMailActionPerformed(evt);
            }
        });

        jButtonWriteMail.setFont(new java.awt.Font("΢���ź�", 0, 14)); // NOI18N
        jButtonWriteMail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/compose.png"))); // NOI18N
        jButtonWriteMail.setText("д�ʼ�");
        jButtonWriteMail.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonWriteMail.setMargin(new java.awt.Insets(4, 4, 4, 4));
        jButtonWriteMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWriteMailActionPerformed(evt);
            }
        });

        jButtonReply.setFont(new java.awt.Font("΢���ź�", 0, 14)); // NOI18N
        jButtonReply.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/reply.png"))); // NOI18N
        jButtonReply.setText("�ظ�");
        jButtonReply.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonReply.setMargin(new java.awt.Insets(4, 4, 4, 4));
        jButtonReply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReplyActionPerformed(evt);
            }
        });

        jButtonDelete.setFont(new java.awt.Font("΢���ź�", 0, 14)); // NOI18N
        jButtonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/tool_archive.png"))); // NOI18N
        jButtonDelete.setText("ɾ��");
        jButtonDelete.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonDelete.setInheritsPopupMenu(true);
        jButtonDelete.setMargin(new java.awt.Insets(4, 4, 4, 4));
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jButtonSearch.setFont(new java.awt.Font("΢���ź�", 0, 14)); // NOI18N
        jButtonSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search_icon.png"))); // NOI18N
        jButtonSearch.setText("����");
        jButtonSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonSearch.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonSearch.setMargin(new java.awt.Insets(4, 4, 4, 4));
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });

        jButtonUserManager.setFont(new java.awt.Font("΢���ź�", 0, 14)); // NOI18N
        jButtonUserManager.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/menu.png"))); // NOI18N
        jButtonUserManager.setText("�û���Ϣ");
        jButtonUserManager.setMargin(new java.awt.Insets(4, 4, 4, 4));
        jButtonUserManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUserManagerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonReceiveMail)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonWriteMail)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonReply)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDelete)
                .addGap(144, 144, 144)
                .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonUserManager)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButtonUserManager, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonReply, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButtonWriteMail, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButtonReceiveMail, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonReceiveMail, jButtonReply, jButtonSearch, jButtonWriteMail, jTextFieldSearch});

        jTreeMailBox.setBackground(new java.awt.Color(196, 235, 255));
        jTreeMailBox.setFont(new java.awt.Font("΢���ź�", 0, 18)); // NOI18N
        jTreeMailBox.setForeground(new java.awt.Color(255, 255, 255));
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("�ռ���");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("�ݸ���");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("�ѷ����ʼ�");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("��ɾ���ʼ�");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("�����ʼ�");
        treeNode1.add(treeNode2);
        jTreeMailBox.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTreeMailBox.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTreeMailBox.setRootVisible(false);
        jTreeMailBox.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTreeMailBoxValueChanged(evt);
            }
        });
        jTreeMailBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTreeMailBoxFocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(jTreeMailBox);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setName(""); // NOI18N

        jListMailList.setFont(new java.awt.Font("΢���ź�", 0, 14)); // NOI18N
        jListMailList.setModel(this.mailListModel);
        jListMailList.setToolTipText("");
        jListMailList.setSelectionBackground(new java.awt.Color(102, 204, 255));
        jListMailList.setValueIsAdjusting(true);
        jListMailList.setVisibleRowCount(16);
        jListMailList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListMailListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jListMailList);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabelFrom.setFont(new java.awt.Font("΢���ź�", 1, 14)); // NOI18N
        jLabelFrom.setText("������䷢����");

        jLabel2.setFont(new java.awt.Font("΢���ź�", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("����");

        jLabelTo.setFont(new java.awt.Font("΢���ź�", 0, 12)); // NOI18N
        jLabelTo.setForeground(new java.awt.Color(102, 102, 102));
        jLabelTo.setText("��������ռ���");

        jLabelTime.setFont(new java.awt.Font("΢���ź�", 0, 12)); // NOI18N
        jLabelTime.setForeground(new java.awt.Color(102, 102, 102));
        jLabelTime.setText("2013-12-08 18:30");

        jLabelSubject.setFont(new java.awt.Font("΢���ź�", 1, 18)); // NOI18N
        jLabelSubject.setText("�ʼ�����");

        jTextAreaContent.setColumns(20);
        jTextAreaContent.setLineWrap(true);
        jTextAreaContent.setRows(5);
        jScrollPane4.setViewportView(jTextAreaContent);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSubject, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabelTo, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabelTime, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 591, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabelFrom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 591, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(6, 6, 6))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelFrom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabelTo)
                    .addComponent(jLabelTime))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 728, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 13, Short.MAX_VALUE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonReceiveMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReceiveMailActionPerformed
        // ��ȡ�ʼ���ť
        receiveWorker.execute();
        while (receiveWorker.isDone()) {
            receiveWorker = new ReceiveMailWorker(this, jButtonReceiveMail);
        }
    }//GEN-LAST:event_jButtonReceiveMailActionPerformed

    private void jTreeMailBoxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTreeMailBoxFocusGained
        // TODO add your handling code here:
        //û�ã�System.out.println(evt.getID());
    }//GEN-LAST:event_jTreeMailBoxFocusGained

    private void jTreeMailBoxValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTreeMailBoxValueChanged
        // �����б����¼�
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jTreeMailBox.getLastSelectedPathComponent();//�������ѡ���Ľڵ�
        //System.out.println(selectedNode.toString());
        //�趨ɾ����ת�����ظ�������
        jButtonDelete.setEnabled(false);
        jButtonReply.setEnabled(false);
        //����ʼ���ʾ�б�
        jListMailList.setListData(new String[]{});
        //��ȡ��ǰ��¼�˺�
        IMailDao mdao = new MailDao();
        Account a = AccountHandler.getLoginAccount();
        //����ʼ��б�����
        if (selectedNode.toString().equals("�ռ���")) {
            //��ʾ�ռ����е��ʼ����б���
            mailList = mdao.queryBoxAll(a, MailBox.INBOX);
        } else if (selectedNode.toString().equals("�ݸ���")) {
            mailList = mdao.queryBoxAll(a, MailBox.DRAFTS);
        } else if (selectedNode.toString().equals("�ѷ����ʼ�")) {
            mailList = mdao.queryBoxAll(a, MailBox.SENTBOX);
        } else if (selectedNode.toString().equals("��ɾ���ʼ�")) {
            mailList = mdao.queryBoxAll(a, MailBox.DELETEDBOX);
        } else {
            //�����ʼ� 
            mailList = mdao.queryBoxAll(a, MailBox.SPAMBOX);
        }
        //��mailList����
        MailSorter.sortByTime(mailList);
        this.refreshJList();
    }//GEN-LAST:event_jTreeMailBoxValueChanged

    private void jListMailListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListMailListValueChanged
        // �ʼ��б�ѡ���¼�
        int index = jListMailList.getSelectedIndex();
        if (evt.getValueIsAdjusting() || index < 0) {
            return;
        }
        Mail m = new Mail();
        if (mailList.size() > 0) {
            m = mailList.get(index);
            //����ʼ�û�б�������������read�ֶΣ�������
            if(m.getM_read()==0){
                //�������ݿ�
                m.setM_read(1);
                IMailDao mdao = new MailDao();
                mdao.updateMail(m);
                //�����б�
                this.refreshJList();
            }
            //����ǿ������index��
            jListMailList.setSelectedIndex(index);
            this.setShowMail(m);
        } else {
            this.setShowMail(m);
        }
    }//GEN-LAST:event_jListMailListValueChanged

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        // ɾ���ض��ʼ�
        //��ȡ��ѡ�����ʼ�        
        int index = jListMailList.getSelectedIndex();
        if (index < 0 || mailList == null || mailList.size()<=0) {
            return;
        }
        IMailDao mdao = new MailDao();
        Mail mail = mailList.get(index);
        int length = mailListModel.getSize();
        //1.�ʼ�����ɾ���б���
        if(mail.getB_id() == MailBox.DELETEDBOX){
        	//ѯ���Ƿ�ȷ��ɾ����
            int type = JOptionPane.showConfirmDialog(this, "ȷ��ɾ����", "ɾ��", JOptionPane.YES_NO_OPTION);
            if (type == JOptionPane.YES_OPTION) {
                mdao.deleteMail(mail.getM_id());
                mailListModel.remove(index);
                //������ʾ��ɾ������һ��
	            if (mailList.size() > 0) {
	            	if(index == length-1){
	            		index--;
	            	}
	                this.setShowMail(mailList.get(index));
	                jListMailList.setSelectedIndex(index);
	            } else {
	                this.setShowMail(null);
	            }
            }
        }else {
        //2.�ʼ��������б���,����ʼ��ƶ���������
        	mail.setB_id(MailBox.DELETEDBOX);
            mdao.updateMail(mail);
            mailList.remove(index);
            mailListModel.remove(index);
            if (mailList.size() > 0) {
            	if(index == length-1){
            		index--;
            	}
                this.setShowMail(mailList.get(index));
                jListMailList.setSelectedIndex(index);                
            } else {
                this.setShowMail(null);
            }
        }                
        
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed
        // ��������
        String key = jTextFieldSearch.getText().trim();
        if (key.isEmpty()) {
            return;
        }
        IMailDao mdao = new MailDao();
        Account a = AccountHandler.getLoginAccount();
        List<Mail> searchResult = mdao.searchMail(a, key);
        mailList = searchResult;
        this.refreshJList();
    }//GEN-LAST:event_jButtonSearchActionPerformed

    private void jButtonUserManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUserManagerActionPerformed
        // ���û����ý���
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrameFactory.getUserInfoFrame().setFrame();
                FrameFactory.getUserInfoFrame().setVisible(true);
            }
        });
    }//GEN-LAST:event_jButtonUserManagerActionPerformed

    private void jButtonWriteMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWriteMailActionPerformed
        // д�ʼ���ť
        WriteMail frame = FrameFactory.getWriteMailFrame();
        frame.setVisible(true);
        frame.setFrame();
    }//GEN-LAST:event_jButtonWriteMailActionPerformed

    private void jButtonReplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReplyActionPerformed
        // �ظ��ʼ���ť
        //1.��ȡ��ǰ��ѡ�е��ʼ�
        int selectedIndex = jListMailList.getSelectedIndex();
        Mail selectedMail = mailList.get(selectedIndex);
        //2.����һ�����ظ����ʼ�
        Mail mail = new Mail();
        String content = "\n\n\n\n---------ԭʼ�ʼ�-----------\n";
        content+="�����ˣ�"+selectedMail.getM_sender()+"\n";
        content+="����ʱ�䣺"+selectedMail.getM_time()+"\n";
        content+="�ռ��ˣ�"+selectedMail.getM_receiver()+"\n";
        content+="���⣺"+selectedMail.getM_title()+"\n";
        content+=selectedMail.getM_content();
        mail.setM_title("re:"+selectedMail.getM_title());
        mail.setM_receiver(selectedMail.getM_sender());
        mail.setM_sender(AccountHandler.getLoginAccount().getA_account());
        mail.setM_content(content);
        WriteMail frame = FrameFactory.getWriteMailFrame();
        frame.setVisible(true);
        frame.setMail(mail);
        frame.setFrame();        
    }//GEN-LAST:event_jButtonReplyActionPerformed

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
            java.util.logging.Logger.getLogger(MAIL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MAIL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MAIL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MAIL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MAIL().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonReceiveMail;
    private javax.swing.JButton jButtonReply;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JButton jButtonUserManager;
    private javax.swing.JButton jButtonWriteMail;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelFrom;
    private javax.swing.JLabel jLabelSubject;
    private javax.swing.JLabel jLabelTime;
    private javax.swing.JLabel jLabelTo;
    private javax.swing.JList jListMailList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextAreaContent;
    private javax.swing.JTextField jTextFieldSearch;
    private javax.swing.JTree jTreeMailBox;
    // End of variables declaration//GEN-END:variables
}
