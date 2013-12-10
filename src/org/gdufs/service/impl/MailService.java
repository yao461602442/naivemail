package org.gdufs.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.gdufs.entity.Account;
import org.gdufs.entity.Mail;
import org.gdufs.entity.MailBox;
import org.gdufs.entity.MailServiceAddress;
import org.gdufs.pub.MailServiceFactory;
import org.gdufs.service.IMailService;


public class MailService implements IMailService {

	private String dateformat = "yy-MM-dd HH:mm"; // 默认的日前显示格式
	private static Mail[] mails = null; //邮件列表！
	
	
	@Override
	public String authentication(String popAddress, Account account) {
		String message = null;
		Socket socket = null;
		PrintWriter socketWriter = null;
		BufferedReader socketReader = null;
		try {
			socket = new Socket(popAddress, 110);
			socketReader = getReader(socket);
			socketWriter = getWriter(socket);
			message = socketReader.readLine();
			// 验证用户名
			socketWriter.println("USER " + account.getA_account());
			message = socketReader.readLine();
			if (!message.startsWith("+OK")) {
				message = "登录失败，用户不存在";
				return message;
			}
			// 验证密码
			socketWriter.println("PASS " + account.getA_passwd());
			message = socketReader.readLine();
			if (!message.startsWith("+OK")) {
				message = "密码错误！或者POP服务未开通！";
				return message;
			}
			message = "验证成功！";
		} catch (Exception e) {
			e.printStackTrace();
		} // pop3服务器端口为110
			// 第一行信息是没用的
		return message;
	}

	@Override
	public List<Mail> receiveAllMail(String popAddress, Account account){
		
		MailService ms = (MailService)MailServiceFactory.getMailService();
		Folder folder = ms.getFolder(popAddress, account);
		
		try {
			folder.open(Folder.READ_ONLY);
			ExecutorService exector = Executors.newFixedThreadPool(10);
			int n = folder.getMessageCount();
			this.mails = new Mail[n];
			Message[] message = folder.getMessages();
			for(int i=0; i<n; ++i){
				exector.execute(new MailHandler((MimeMessage)message[i], i));
			}
			exector.shutdown();
			while(!exector.isTerminated());
		} catch (MessagingException e) {
			e.printStackTrace();
		}		
		//设置邮件a_id
		for(int i=0; i<mails.length; ++i){
			mails[i].setA_id(account.getA_id());
		}		
		//把mails转为List
		List<Mail> mailList = new ArrayList<Mail>(mails.length);
		for(int i=0; i<mails.length; ++i)
		{
			if(mails[i]!=null)
				mailList.add(mails[i]);
		}
		mails=null;
		
		return mailList;
	}
	
	@Override
	public List<Mail> getRecentMail(String popAddress, Account account){
		
		return null;
	}

	@Override
	public void sendMail(String smtpAddress, Account account, Mail mail) {
		// TODO Auto-generated method stub

	}
	
	
	//===============邮件获取线程==============================
	static class MailHandler extends Thread{
		
		private MimeMessage mimeMessage = null;
		private int index;
		public MailHandler(MimeMessage mimeMessage, int k){
			this.mimeMessage = mimeMessage;		
			this.index = k;
		}
		
		@Override
		public void run(){
			MailService ms = (MailService)MailServiceFactory.getMailService();
			Mail currentMail = ms.receiveOneMail(this.mimeMessage);		
			mails[index] = currentMail;
		}
	}

	// =================私有方法=======================================

	/**
	 * 封装Socket写
	 * 
	 * @param socket
	 * @return
	 * @throws IOException
	 */
	private static PrintWriter getWriter(Socket socket) throws IOException {
		OutputStream socketOut = socket.getOutputStream();
		// return new PrintWriter(socketOut,true);
		return new PrintWriter(new OutputStreamWriter(socketOut, "GB2312"),
				true);
	}

	/**
	 * 封装socket读
	 * 
	 * @param socket
	 * @return
	 * @throws IOException
	 */
	private static BufferedReader getReader(Socket socket) throws IOException {
		InputStream socketIn = socket.getInputStream();
		// return new BufferedReader(new InputStreamReader(socketIn));
		return new BufferedReader(new InputStreamReader(socketIn, "GB2312"));
	}
	
	/**
	 * 获取邮件服务器中对应账户收件箱文件夹对象！
	 * @param popAddress
	 * @param account
	 * @return
	 */
	private Folder getFolder(String popAddress, Account account) {
		Properties props = System.getProperties();		
		Session session = Session.getDefaultInstance(props, null);
		URLName urln = new URLName("pop3", popAddress, 110, null,
				account.getA_account(), account.getA_passwd());
		Store store = null;
		Folder folder = null;
		try {
			store = session.getStore(urln);
			store.connect();
			folder = store.getFolder("inbox");
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return folder;
	}

	/**
	 * 处理一封邮件的内容
	 * @param mimeMessage MimeMessage类的对象，从Message类转化而来，是Message类的子类
	 * @return 一封邮件对象
	 */
	private Mail receiveOneMail(MimeMessage mimeMessage) {
		Mail mail = null;
		String subject = null;
		String sentDate = null;
		String from = null;
		String to = null;
		String content = null;
		try {
			mail = new Mail();
			subject = this.getSubject(mimeMessage);
			sentDate = this.getSentDate(mimeMessage);
			from = this.getFrom(mimeMessage);
			to = this.getMailAddress(mimeMessage, "to");
			content = this.getMailContent((Part) mimeMessage);
			// 设置邮件属性
			mail.setM_title(subject);
			mail.setB_id(MailBox.INBOX);
			mail.setM_read(0);
			mail.setM_receiver(to);
			mail.setM_sender(from);
			mail.setM_content(content);
			mail.setM_time(sentDate);
			System.out.println("subject="+subject);

		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mail;
	}

	/**
	 * 获得发件人的地址和姓名
	 */
	private String getFrom(MimeMessage mimeMessage) throws Exception {
		InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
		String from = address[0].getAddress();
		if (from == null)
			from = "";
		String personal = address[0].getPersonal();
		if (personal == null)
			personal = "";
		String fromaddr = personal + "<" + from + ">";
		return fromaddr;
	}

	/**
	 * 获得邮件的收件人，抄送，和密送的地址和姓名，根据所传递的参数的不同 "to" ----收件人 "cc"---抄送人地址 "bcc"---密送人地址
	 */
	private String getMailAddress(MimeMessage mimeMessage, String type)
			throws Exception {
		String mailaddr = "";
		String addtype = type.toUpperCase();
		InternetAddress[] address = null;
		if (addtype.equals("TO") || addtype.equals("CC")
				|| addtype.equals("BCC")) {
			if (addtype.equals("TO")) {
				address = (InternetAddress[]) mimeMessage
						.getRecipients(Message.RecipientType.TO);
			} else if (addtype.equals("CC")) {
				address = (InternetAddress[]) mimeMessage
						.getRecipients(Message.RecipientType.CC);
			} else {
				address = (InternetAddress[]) mimeMessage
						.getRecipients(Message.RecipientType.BCC);
			}
			if (address != null) {
				for (int i = 0; i < address.length; i++) {
					String email = address[i].getAddress();
					if (email == null)
						email = "";
					else {
						email = MimeUtility.decodeText(email);
					}
					String personal = address[i].getPersonal();
					if (personal == null)
						personal = "";
					else {
						personal = MimeUtility.decodeText(personal);
					}
					String compositeto = personal + "<" + email + ">";
					mailaddr += "," + compositeto;
				}
				mailaddr = mailaddr.substring(1);
			}
		} else {
			throw new Exception("Error emailaddr type!");
		}
		return mailaddr;
	}

	/**
	 * 获得邮件主题
	 */
	private String getSubject(MimeMessage mimeMessage)
			throws MessagingException {
		String subject = "";
		try {
			subject = MimeUtility.decodeText(mimeMessage.getSubject());
			if (subject == null)
				subject = "";
		} catch (Exception exce) {
		}
		return subject;
	}

	/**
	 * 获得邮件发送日期
	 */
	private String getSentDate(MimeMessage mimeMessage) throws Exception {
		Date sentdate = mimeMessage.getSentDate();
		SimpleDateFormat format = new SimpleDateFormat(dateformat);
		return format.format(sentdate);
	}

	/**
	 * 解析邮件，把得到的邮件内容保存到一个StringBuffer对象中，解析邮件 主要是根据MimeType类型的不同执行不同的操作，一步一步的解析
	 */
	private String getMailContent(Part part) throws Exception {
		StringBuilder bodyText = new StringBuilder();
		String contenttype = part.getContentType();
		int nameindex = contenttype.indexOf("name");
		boolean conname = false;
		if (nameindex != -1)
			conname = true;
		//System.out.println("CONTENTTYPE: " + contenttype);
		if (part.isMimeType("text/plain") && !conname) {
			bodyText.append((String)part.getContent());
		} else if (part.isMimeType("text/html") && !conname) {
			bodyText.append((String) part.getContent());
		} else if (part.isMimeType("multipart/*")) {
			// System.out.println(part.getDescription());
			Multipart multipart = (Multipart) part.getContent();
			int counts = multipart.getCount();
			for (int i = 0; i < counts; i++) {
				bodyText.append(getMailContent(multipart.getBodyPart(i)));
			}
		} else if (part.isMimeType("message/rfc822")) {
			bodyText.append(getMailContent((Part) part.getContent()));
		} else {
		}
		return bodyText.toString();
	}

}
