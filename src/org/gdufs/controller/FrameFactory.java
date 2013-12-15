/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gdufs.controller;

import javax.swing.JFrame;
import org.gdufs.view.MAIL;
import org.gdufs.view.UserInfo;
import org.gdufs.view.WriteMail;
import org.gdufs.view.login;
import org.omg.PortableInterceptor.USER_EXCEPTION;

/**
 *
 * @author Administrator
 */
public class FrameFactory {
    private static login loginFrame = new login();
    private static MAIL mainFrame = new MAIL();
    private static UserInfo userInfoFrame = new UserInfo();
    private static WriteMail writeMailFrame = new WriteMail();
    private FrameFactory(){
        //
    }
    
    public static login getLoginFrame(){
        return loginFrame;
    }
    
    public static MAIL getMainFrame(){
        return mainFrame;
    }
    
    public static UserInfo getUserInfoFrame(){
        return userInfoFrame;
    }
    
    public static WriteMail getWriteMailFrame(){
        return writeMailFrame;
    }
}
