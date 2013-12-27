/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gdufs.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
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
	private JPopupMenu popupInbox = new JPopupMenu();
	private JPopupMenu popupSpam = new JPopupMenu();
	private static int popupSelectedItemIndex = 0;
	private MouseAdapter inboxPopupMouseAction = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			maybeShowPopup(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			jListMailList.setSelectedIndex(jListMailList.locationToIndex(e
					.getPoint()));
			maybeShowPopup(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger() && jListMailList.getSelectedIndex() != -1) {
				Object selected = jListMailList.getModel().getElementAt(
						jListMailList.getSelectedIndex());
				popupSelectedItemIndex = jListMailList.getSelectedIndex();
				popupInbox.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	};
	
	private MouseAdapter spamBoxPopupMouseAction = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			maybeShowPopup(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			jListMailList.setSelectedIndex(jListMailList.locationToIndex(e
					.getPoint()));
			maybeShowPopup(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger() && jListMailList.getSelectedIndex() != -1) {
				Object selected = jListMailList.getModel().getElementAt(
						jListMailList.getSelectedIndex());
				popupSelectedItemIndex = jListMailList.getSelectedIndex();
				popupSpam.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}; 

	/**
	 * Creates new form MAIL
	 */
	public MAIL() {
		 initComponents();
		initPopupMenu();
		pack();
		// 调整主界面大小问题
//		double width = Toolkit.getDefaultToolkit().getScreenSize().width; //得到当前屏幕分辨率的高
//		  double height = Toolkit.getDefaultToolkit().getScreenSize().height;//得到当前屏幕分辨率的宽搜索
//		  this.setSize((int)width,(int)height);//设置大小
//		  this.setLocation(0,0); //设置窗体居中显示
//		  this.setResizable(false);//禁用最大化按钮
		
//		 double heightAdjustBefore = this.getHeight();
//		 Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
//		 double heightAdjustNow = screenSize.getHeight()*0.9;
//		 Dimension adjust = new Dimension();
//		 adjust.setSize(this.getWidth(), heightAdjustNow);
//		 this.setSize(adjust);
		// 调整其它界面大小
		// double ratio = heightAdjustNow/heightAdjustBefore;
		// double treeMailBoxHeight = jTreeMailBox.getHeight()*ratio;
		// double listMailHeight = jListMailList.getHeight()*ratio;
		// double textAreaContentHeight = jTextAreaContent.getHeight()*ratio;
		// Dimension treeD = new Dimension();
		// treeD.setSize(jTreeMailBox.getWidth(),treeMailBoxHeight);
		// jTreeMailBox.setSize(treeD);
		// Dimension listD = new Dimension();
		// listD.setSize(jListMailList.getWidth(),listMailHeight);
		// jListMailList.setSize(listD);

		// 设置属性
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
			super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			Summary m = (Summary) value;
			if (m.getRead() == 0) {
				setFont(font1);
			}
			return this;
		}
	}

	/**
	 * 列表的内容
	 */
	class Summary {
		private String subject;
		private String time;
		private int read;

		public Summary(String subject, String time, int read) {
			this.subject = subject;
			this.time = time;
			this.read = read;
		}

		public int getRead() {
			return read;
		}

		@Override
		public String toString() {
			if (subject.length() > 10) {
				subject = subject.substring(0, 10);
				subject += "...";
			}
			String message = "";
			if (subject == null || subject.isEmpty()) {
				subject = "<无主题>";
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
		jTreeMailBox.setSelectionRow(0);
		// jListMailList.setFont(new Font(null, Font.BOLD, 20));
		return this;
	}

	/**
	 * 初始化收件箱弹出菜单
	 */
	private void initPopupMenu() {
		JMenuItem setSpamItem = new JMenuItem("标为为垃圾邮件");
		setSpamItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 将被选中项标为垃圾邮件
				Mail m = mailList.get(popupSelectedItemIndex);
				m.setB_id(MailBox.SPAMBOX);
				IMailDao mdao = new MailDao();
				mdao.updateMail(m);
				// 设置界面变化
				DefaultListModel model = (DefaultListModel) jListMailList
						.getModel();
				model.removeElementAt(popupSelectedItemIndex);
				if (popupSelectedItemIndex == model.getSize()) {
					jListMailList.setSelectedIndex(popupSelectedItemIndex - 1);
				}
				jListMailList.setSelectedIndex(popupSelectedItemIndex);
			}
		});
		popupInbox.add(setSpamItem);
		
		JMenuItem setInboxItem = new JMenuItem("这不是垃圾邮件");
		setInboxItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// 将被选中项标记为正常邮件
				Mail m = mailList.get(popupSelectedItemIndex);
				m.setB_id(MailBox.INBOX);
				IMailDao mdao = new MailDao();
				mdao.updateMail(m);
				// 设置界面变化
				DefaultListModel model = (DefaultListModel) jListMailList
						.getModel();
				model.removeElementAt(popupSelectedItemIndex);
				if (popupSelectedItemIndex == model.getSize()) {
					jListMailList.setSelectedIndex(popupSelectedItemIndex - 1);
				}
				jListMailList.setSelectedIndex(popupSelectedItemIndex);
			}
		});
		popupSpam.add(setInboxItem);
	}
	
	private void clearALlPopupAction(){
		jListMailList.removeMouseListener(spamBoxPopupMouseAction);
		jListMailList.removeMouseListener(inboxPopupMouseAction);
	}
	
	private void setInboxPopupAction() {		
		jListMailList.addMouseListener(inboxPopupMouseAction);
	}
	
	private void setSpamBoxPopupAction(){		
		jListMailList.addMouseListener(spamBoxPopupMouseAction);
	}
	
	/**
	 * 刷新邮件列表JList
	 * 
	 * @return
	 */
	private void refreshJList() {
		// 1.填充JList的内容
		mailListModel.clear();
		for (int i = 0; i < mailList.size(); ++i) {
			String subject = mailList.get(i).getM_title();
			String time = mailList.get(i).getM_time();
			int read = mailList.get(i).getM_read();
			Summary summary = new Summary(subject, time, read);
			mailListModel.addElement(summary);
		}
		// System.out.println("mailListModel size : " +
		// mailListModel.getSize());
		// 设置邮件列表内容
		jListMailList.setModel(mailListModel);
		// 2.设置被选中状态，以及右边展示
		int selectedIndex = jListMailList.getSelectedIndex();
		if (selectedIndex == -1) {
			// 设置为第一封默认展示
			if (mailList.size() > 0) {
				this.setShowMail(mailList.get(0));
				jListMailList.setSelectedIndex(0);
			} else {
				// mailList没内容
				this.setShowMail(new Mail());
			}
		} else {
			this.setShowMail(mailList.get(selectedIndex));
		}
	}

	private void setShowMail(Mail m) {
		if (m == null) {
			return;
		}
		String longString = m.getM_title();
		StringBuilder builder = new StringBuilder("<html>");
	    char[] chars = longString.toCharArray();
	    FontMetrics fontMetrics = jLabelSubject.getFontMetrics(jLabelSubject.getFont());
	    for (int beginIndex = 0, limit = 1;; limit++) {
	        System.out.println(beginIndex + " " + limit + " " + (beginIndex + limit));
	        if (fontMetrics.charsWidth(chars, beginIndex, limit) < jLabelSubject.getWidth()) {
	            if (beginIndex + limit < chars.length) {
	                continue;
	            }
	            builder.append(chars, beginIndex, limit);
	            break;
	        }
	        builder.append(chars, beginIndex, limit - 1).append("<br/>");
	        beginIndex+=limit-1;
	        //beginIndex <span style="color: rgb(255, 0, 0);">+=</span> limit - 1;
	        limit = 0;
	    }
	    builder.append("</html>");
	    
		jLabelSubject.setText(builder.toString());
		jLabelFrom.setText(m.getM_sender());
		jLabelTo.setText(m.getM_receiver());
		jLabelTime.setText(m.getM_time());
		jTextAreaContent.setText(m.getM_content());
		if(m.getA_id()==0)
			return;
		// 选择了特定邮件后，设置删除、转发、回复按钮可用!草稿箱中回复按钮不可用
		if (m.getB_id() != MailBox.DRAFTS) {
			jButtonReply.setEnabled(true);
		}
		jButtonDelete.setEnabled(true);

	}

	/**
	 * 邮件获取线程
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
			// 1.获取邮件
			button.setEnabled(false);
			Account user = AccountHandler.getLoginAccount();
			System.out.println(user);
			IMailService mailService = MailServiceFactory.getMailService();
			String server = MailServiceAddress.getPopAddress(user
					.getA_account());
			frame.setTitle("NaiveMail   正在收取邮件中..");
			List<Mail> mailList = mailService.getRecentMail(server, user);

			// 2.垃圾邮件过滤
			frame.setTitle("NaiveMail   收取邮件完成，正在识别垃圾邮件..");
			int length = mailList.size();
			IClassifier classfier = new Classifier();
			classfier.categoryBatchMail(mailList);
			// 3.插入新邮件到数据库中
			frame.setTitle("NaiveMail   识别完成，正在保存操作结果..");
			IMailDao mdao = new MailDao();
			mdao.insertBatchMail(mailList);
			return null;
		}

		@Override
		public void done() {
			frame.setTitle("NaiveMail");
			button.setEnabled(true);
			// 刷新界面
			frame.setFrame();
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButtonReceiveMail = new javax.swing.JButton();
        jButtonWriteMail = new javax.swing.JButton();
        jButtonReply = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonSearch = new javax.swing.JButton();
        jButtonUserManager = new javax.swing.JButton();
        jTextFieldSearch = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTreeMailBox = new javax.swing.JTree();
        jPanel5 = new javax.swing.JPanel();
        jLabelFrom = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabelTo = new javax.swing.JLabel();
        jLabelTime = new javax.swing.JLabel();
        jLabelSubject = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextAreaContent = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListMailList = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("NaiveMail");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setPreferredSize(new java.awt.Dimension(1000, 750));

        jButtonReceiveMail.setFont(new java.awt.Font("微软雅黑", 0, 14)); // NOI18N
        jButtonReceiveMail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/getmail.png"))); // NOI18N
        jButtonReceiveMail.setText("收取");
        jButtonReceiveMail.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonReceiveMail.setMargin(new java.awt.Insets(4, 4, 4, 4));
        jButtonReceiveMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReceiveMailActionPerformed(evt);
            }
        });

        jButtonWriteMail.setFont(new java.awt.Font("微软雅黑", 0, 14)); // NOI18N
        jButtonWriteMail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/compose.png"))); // NOI18N
        jButtonWriteMail.setText("写邮件");
        jButtonWriteMail.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonWriteMail.setMargin(new java.awt.Insets(4, 4, 4, 4));
        jButtonWriteMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWriteMailActionPerformed(evt);
            }
        });

        jButtonReply.setFont(new java.awt.Font("微软雅黑", 0, 14)); // NOI18N
        jButtonReply.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/reply.png"))); // NOI18N
        jButtonReply.setText("回复");
        jButtonReply.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonReply.setMargin(new java.awt.Insets(4, 4, 4, 4));
        jButtonReply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReplyActionPerformed(evt);
            }
        });

        jButtonDelete.setFont(new java.awt.Font("微软雅黑", 0, 14)); // NOI18N
        jButtonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/tool_archive.png"))); // NOI18N
        jButtonDelete.setText("删除");
        jButtonDelete.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonDelete.setInheritsPopupMenu(true);
        jButtonDelete.setMargin(new java.awt.Insets(4, 4, 4, 4));
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jButtonSearch.setFont(new java.awt.Font("微软雅黑", 0, 14)); // NOI18N
        jButtonSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search_icon.png"))); // NOI18N
        jButtonSearch.setText("搜索");
        jButtonSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonSearch.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonSearch.setMargin(new java.awt.Insets(4, 4, 4, 4));
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });

        jButtonUserManager.setFont(new java.awt.Font("微软雅黑", 0, 14)); // NOI18N
        jButtonUserManager.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/menu.png"))); // NOI18N
        jButtonUserManager.setText("用户信息");
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jSeparator1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jButtonReceiveMail)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonWriteMail)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonReply)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDelete)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonSearch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 197, Short.MAX_VALUE)
                        .addComponent(jButtonUserManager)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonReceiveMail, jButtonReply, jButtonSearch, jButtonWriteMail, jTextFieldSearch});

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setName(""); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(1215, 700));

        jTreeMailBox.setBackground(new java.awt.Color(196, 235, 255));
        jTreeMailBox.setFont(new java.awt.Font("微软雅黑", 0, 18)); // NOI18N
        jTreeMailBox.setForeground(new java.awt.Color(255, 255, 255));
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("收件箱");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("草稿箱");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("已发送邮件");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("已删除邮件");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("垃圾邮件");
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

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabelFrom.setFont(new java.awt.Font("微软雅黑", 1, 14)); // NOI18N
        jLabelFrom.setText("这里填充发件人");

        jLabel2.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("发给");

        jLabelTo.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jLabelTo.setForeground(new java.awt.Color(102, 102, 102));
        jLabelTo.setText("这里填充收件人");

        jLabelTime.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        jLabelTime.setForeground(new java.awt.Color(102, 102, 102));
        jLabelTime.setText("2013-12-08 18:30");

        jLabelSubject.setFont(new java.awt.Font("微软雅黑", 1, 16)); // NOI18N
        jLabelSubject.setText("邮件主题");

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
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelTo, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                        .addComponent(jLabelTime, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabelFrom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelSubject, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane4)
                        .addContainerGap())))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabelSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelFrom)
                .addGap(2, 2, 2)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabelTo)
                    .addComponent(jLabelTime))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE))
        );

        jListMailList.setFont(new java.awt.Font("微软雅黑", 0, 14)); // NOI18N
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 906, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void jButtonReceiveMailActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonReceiveMailActionPerformed
		// 收取邮件按钮
		receiveWorker.execute();
		while (receiveWorker.isDone()) {
			receiveWorker = new ReceiveMailWorker(this, jButtonReceiveMail);
		}
	}// GEN-LAST:event_jButtonReceiveMailActionPerformed

	private void jTreeMailBoxFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTreeMailBoxFocusGained
		// TODO add your handling code here:
		// 没用！System.out.println(evt.getID());
	}// GEN-LAST:event_jTreeMailBoxFocusGained

	private void jTreeMailBoxValueChanged(
			javax.swing.event.TreeSelectionEvent evt) {// GEN-FIRST:event_jTreeMailBoxValueChanged
		// 邮箱列表点击事件
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jTreeMailBox
				.getLastSelectedPathComponent();// 返回最后选定的节点
		// System.out.println(selectedNode.toString());
		// 设定删除、转发、回复不可用
		jButtonDelete.setEnabled(false);
		jButtonReply.setEnabled(false);
		// 清空邮件显示列表
		jListMailList.setListData(new String[] {});
		// 获取当前登录账号
		IMailDao mdao = new MailDao();
		Account a = AccountHandler.getLoginAccount();
		clearALlPopupAction();// 清除列表右键菜单
		// 填充邮件列表内容
		if (selectedNode.toString().equals("收件箱")) {
			// 显示收件箱中的邮件到列表中
			mailList = mdao.queryBoxAll(a, MailBox.INBOX);
			setInboxPopupAction();
		} else if (selectedNode.toString().equals("草稿箱")) {
			mailList = mdao.queryBoxAll(a, MailBox.DRAFTS);
		} else if (selectedNode.toString().equals("已发送邮件")) {
			mailList = mdao.queryBoxAll(a, MailBox.SENTBOX);
		} else if (selectedNode.toString().equals("已删除邮件")) {
			mailList = mdao.queryBoxAll(a, MailBox.DELETEDBOX);
		} else {
			// 垃圾邮件
			mailList = mdao.queryBoxAll(a, MailBox.SPAMBOX);
			setSpamBoxPopupAction();
		}
		// 给mailList排序
		MailSorter.sortByTime(mailList);
		this.refreshJList();
	}// GEN-LAST:event_jTreeMailBoxValueChanged

	private void jListMailListValueChanged(
			javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_jListMailListValueChanged
		// 邮件列表选择事件
		int index = jListMailList.getSelectedIndex();
		if (evt.getValueIsAdjusting() || index < 0) {
			return;
		}
		Mail m = new Mail();
		if (mailList.size() > 0) {
			m = mailList.get(index);
			// 如果邮件没有被看过，则设置read字段，并更新
			if (m.getM_read() == 0) {
				// 更新数据库
				m.setM_read(1);
				IMailDao mdao = new MailDao();
				mdao.updateMail(m);
				// 更新列表
				this.refreshJList();
			}
			// 这里强制设置index！
			jListMailList.setSelectedIndex(index);
			this.setShowMail(m);
		} else {
			this.setShowMail(m);
		}
	}// GEN-LAST:event_jListMailListValueChanged

	private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonDeleteActionPerformed
		// 删除特定邮件
		// 获取被选定的邮件
		int index = jListMailList.getSelectedIndex();
		if (index < 0 || mailList == null || mailList.size() <= 0) {
			return;
		}
		IMailDao mdao = new MailDao();
		Mail mail = mailList.get(index);
		int length = mailListModel.getSize();
		// 1.邮件在已删除列表中
		if (mail.getB_id() == MailBox.DELETEDBOX) {
			// 询问是否确认删除！
			int type = JOptionPane.showConfirmDialog(this, "确认删除吗？", "删除",
					JOptionPane.YES_NO_OPTION);
			if (type == JOptionPane.YES_OPTION) {
				mdao.deleteMail(mail.getM_id());
				mailListModel.remove(index);
				// 设置显示被删除的下一封
				if (mailList.size() > 0) {
					if (index == length - 1) {
						index--;
					}
					this.setShowMail(mailList.get(index));
					jListMailList.setSelectedIndex(index);
				} else {
					this.setShowMail(null);
				}
			}
		} else {
			// 2.邮件在其它列表中,则把邮件移动到垃圾箱
			mail.setB_id(MailBox.DELETEDBOX);
			mdao.updateMail(mail);
			mailList.remove(index);
			mailListModel.remove(index);
			if (mailList.size() > 0) {
				if (index == length - 1) {
					index--;
				}
				this.setShowMail(mailList.get(index));
				jListMailList.setSelectedIndex(index);
			} else {
				this.setShowMail(null);
			}
		}

	}// GEN-LAST:event_jButtonDeleteActionPerformed

	private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSearchActionPerformed
		// 搜索功能
		String key = jTextFieldSearch.getText().trim();
		if (key.isEmpty()) {
			return;
		}
		IMailDao mdao = new MailDao();
		Account a = AccountHandler.getLoginAccount();
		List<Mail> searchResult = mdao.searchMail(a, key);
		mailList = searchResult;
		this.refreshJList();
	}// GEN-LAST:event_jButtonSearchActionPerformed

	private void jButtonUserManagerActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonUserManagerActionPerformed
		// 打开用户设置界面
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				FrameFactory.getUserInfoFrame().setFrame();
				FrameFactory.getUserInfoFrame().setVisible(true);
			}
		});
	}// GEN-LAST:event_jButtonUserManagerActionPerformed

	private void jButtonWriteMailActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonWriteMailActionPerformed
		// 写邮件按钮
		WriteMail frame = FrameFactory.getWriteMailFrame();
		frame.setVisible(true);
		frame.setFrame();
	}// GEN-LAST:event_jButtonWriteMailActionPerformed

	private void jButtonReplyActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonReplyActionPerformed
		// 回复邮件按钮
		// 1.获取当前被选中的邮件
		int selectedIndex = jListMailList.getSelectedIndex();
		Mail selectedMail = mailList.get(selectedIndex);
		// 2.构造一个被回复的邮件
		Mail mail = new Mail();
		String content = "\n\n\n\n---------原始邮件-----------\n";
		content += "发件人：" + selectedMail.getM_sender() + "\n";
		content += "发送时间：" + selectedMail.getM_time() + "\n";
		content += "收件人：" + selectedMail.getM_receiver() + "\n";
		content += "主题：" + selectedMail.getM_title() + "\n";
		content += selectedMail.getM_content();
		mail.setM_title("re:" + selectedMail.getM_title());
		mail.setM_receiver(selectedMail.getM_sender());
		mail.setM_sender(AccountHandler.getLoginAccount().getA_account());
		mail.setM_content(content);
		WriteMail frame = FrameFactory.getWriteMailFrame();
		frame.setVisible(true);
		frame.setMail(mail);
		frame.setFrame();
	}// GEN-LAST:event_jButtonReplyActionPerformed

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		/*
		 * Set the Nimbus look and feel
		 */
		// <editor-fold defaultstate="collapsed"
		// desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase
		 * /tutorial/uiswing/lookandfeel/plaf.html
		 */
		// try {
		// for (javax.swing.UIManager.LookAndFeelInfo info :
		// javax.swing.UIManager.getInstalledLookAndFeels()) {
		// if ("Nimbus".equals(info.getName())) {
		// javax.swing.UIManager.setLookAndFeel(info.getClassName());
		// break;
		// }
		// }
		// } catch (ClassNotFoundException ex) {
		// java.util.logging.Logger.getLogger(MAIL.class.getName()).log(java.util.logging.Level.SEVERE,
		// null, ex);
		// } catch (InstantiationException ex) {
		// java.util.logging.Logger.getLogger(MAIL.class.getName()).log(java.util.logging.Level.SEVERE,
		// null, ex);
		// } catch (IllegalAccessException ex) {
		// java.util.logging.Logger.getLogger(MAIL.class.getName()).log(java.util.logging.Level.SEVERE,
		// null, ex);
		// } catch (javax.swing.UnsupportedLookAndFeelException ex) {
		// java.util.logging.Logger.getLogger(MAIL.class.getName()).log(java.util.logging.Level.SEVERE,
		// null, ex);
		// }
		// </editor-fold>

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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelFrom;
    private javax.swing.JLabel jLabelSubject;
    private javax.swing.JLabel jLabelTime;
    private javax.swing.JLabel jLabelTo;
    private javax.swing.JList jListMailList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextAreaContent;
    private javax.swing.JTextField jTextFieldSearch;
    private javax.swing.JTree jTreeMailBox;
    // End of variables declaration//GEN-END:variables
}
