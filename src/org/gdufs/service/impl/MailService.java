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

import org.gdufs.dao.impl.MailDao;
import org.gdufs.entity.Account;
import org.gdufs.entity.Mail;
import org.gdufs.entity.MailBox;
import org.gdufs.entity.MailServiceAddress;
import org.gdufs.pub.MailServiceFactory;
import org.gdufs.service.IMailService;


public class MailService implements IMailService {

	private String dateformat = "yy-MM-dd HH:mm"; // Ĭ�ϵ���ǰ��ʾ��ʽ
	private static Mail[] mails = null; //�ʼ��б�
	
	
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
                        System.out.println(message);
			// ��֤�û���
			socketWriter.println("USER " + account.getA_account());
			message = socketReader.readLine();
                        System.out.println(message);
			if (!message.startsWith("+OK")) {
				message = "��¼ʧ�ܣ��û�������";
				return message;
			}
			// ��֤����
			socketWriter.println("PASS " + account.getA_passwd());
			message = socketReader.readLine();
                        System.out.println(message);
			if (!message.startsWith("+OK")) {
				message = "������󣡻���POP����δ��ͨ��";
				return message;
			}
			message = "��֤�ɹ���";
		} catch (Exception e) {
			e.printStackTrace();
		} // pop3�������˿�Ϊ110
			// ��һ����Ϣ��û�õ�
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
		//�����ʼ�a_id
		for(int i=0; i<mails.length; ++i){
			mails[i].setA_id(account.getA_id());
		}		
		//��mailsתΪList
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
		//����ʼ�ͷ�е�����.�ж��Ƿ������ݿ��д�������ʼ����еĻ������ظ���,�������ȡ
		List<Mail> mailList = new ArrayList<Mail>();
		MailService ms = (MailService)MailServiceFactory.getMailService();
		Folder folder = ms.getFolder(popAddress, account);		
		try {
			folder.open(Folder.READ_ONLY);
			ExecutorService exector = Executors.newFixedThreadPool(10);
			int n = folder.getMessageCount();
			Message[] message = folder.getMessages();
			for(int i=0; i<n; ++i){
				exector.execute(new RecentMailHandler((MimeMessage)message[i], mailList));
			}
			exector.shutdown();
			while(!exector.isTerminated());
		} catch (MessagingException e) {
			e.printStackTrace();
		}		
		//�����ʼ�a_id
		for(int i=0; i<mailList.size(); ++i){
			mailList.get(i).setA_id(account.getA_id());
		}
		
		return mailList;
	}

	@Override
	public void sendMail(String smtpAddress, Account account, Mail mail) {
		// TODO Auto-generated method stub

	}
	
	
	//===============�ʼ���ȡ�߳�==============================
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
	
	static class RecentMailHandler extends Thread{
		private MimeMessage mimeMessage = null;
		private List<Mail> mailList = null;
		
		public RecentMailHandler(MimeMessage mimeMessage, List<Mail> mailList){
			this.mimeMessage = mimeMessage;		
			this.mailList = mailList;
		}
		
		@Override
		public void run(){
			MailService ms = (MailService)MailServiceFactory.getMailService();
			String from = null;
			String time = null;
			String subject = null;
			try {
				 from = ms.getFrom(mimeMessage);
				 time = ms.getSentDate(mimeMessage);
				 subject = ms.getSubject(mimeMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//���
			MailDao mdao = new MailDao();
			if(mdao.checkUnique(subject, from, time)==0){
				Mail currentMail = ms.receiveOneMail(this.mimeMessage);		
				synchronized (mailList) {
					mailList.add(currentMail);
				}
			}
		}
	}

	// =================˽�з���=======================================

	/**
	 * ��װSocketд
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
	 * ��װsocket��
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
	 * ��ȡ�ʼ��������ж�Ӧ�˻��ռ����ļ��ж���
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
	 * ����һ���ʼ�������
	 * @param mimeMessage MimeMessage��Ķ��󣬴�Message��ת����������Message�������
	 * @return һ���ʼ�����
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
			// �����ʼ�����
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
	 * ��÷����˵ĵ�ַ������
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
	 * ����ʼ����ռ��ˣ����ͣ������͵ĵ�ַ�����������������ݵĲ����Ĳ�ͬ "to" ----�ռ��� "cc"---�����˵�ַ "bcc"---�����˵�ַ
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
	 * ����ʼ�����
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
	 * ����ʼ���������
	 */
	private String getSentDate(MimeMessage mimeMessage) throws Exception {
		Date sentdate = mimeMessage.getSentDate();
		SimpleDateFormat format = new SimpleDateFormat(dateformat);
		return format.format(sentdate);
	}

	/**
	 * �����ʼ����ѵõ����ʼ����ݱ��浽һ��StringBuffer�����У������ʼ� ��Ҫ�Ǹ���MimeType���͵Ĳ�ִͬ�в�ͬ�Ĳ�����һ��һ���Ľ���
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
