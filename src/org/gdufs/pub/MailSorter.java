/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gdufs.pub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gdufs.entity.Mail;

/**
 * ���ʼ�����
 * @author Administrator
 */
public class MailSorter {
    /**
     * �Ѵ�������mailList����ʱ�䡰�ӽ���Զ������
     * @param list 
     */
    public static void sortByTime(List<Mail> list){
        Collections.sort(list, new Comparator<Mail>(){

            @Override
            public int compare(Mail o1, Mail o2) {
                int ret = 0;
                try {
                    SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm");
                    Date d1 = df.parse(o1.getM_time());
                    Date d2 = df.parse(o2.getM_time());
                    long diff = d1.getTime()-d2.getTime();
                    if(diff>0){
                        //ʱ�����ģ����Ǹ��µģ�����ǰ��
                        ret = -1;
                    } else if(diff<0){
                        ret = 1;
                    } else{
                        ret = 0;
                    }                    
                } catch (ParseException ex) {
                    Logger.getLogger(MailSorter.class.getName()).log(Level.SEVERE, null, ex);
                }
                return ret;
            }
            
        });
        
        
    }
}
