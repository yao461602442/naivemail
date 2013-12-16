package org.gdufs.service.impl;

import java.util.List;

import org.gdufs.entity.Mail;
import org.gdufs.entity.MailBox;
import org.gdufs.pub.BayesFilter;
import org.gdufs.service.IClassifier;

public class Classifier implements IClassifier {

	@Override
	public int categoryOneMail(Mail mail) {
		if(!BayesFilter.testForSpam(mail.getM_content())){
			//À¬»øÓÊ¼þ
			mail.setM_spam(1);
			mail.setB_id(MailBox.SPAMBOX);
		}
		return 1;
	}

	@Override
	public int categoryBatchMail(List<Mail> list) {
		for(int i=0; i<list.size(); ++i){
			categoryOneMail(list.get(i));
		}
		return list.size();
	}
	public static void main(String[] args) {
		
	}

	

}
