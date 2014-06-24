package com.obc.csrg.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import java.io.*;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.activation.FileDataSource;
import javax.activation.DataHandler;
import javax.mail.Multipart;
import javax.activation.DataSource;

import com.obc.csrg.util.FileOnTheFly;

public class EmailUtil implements Serializable {

	private String SMTP_HOST = "localhost";
	private int SMTP_PORT =  25;
    private String SMTP_AUTH =  "false";
    private String SMTP_AUTH_USER = "";
    private String SMTP_AUTH_PWD =  "";
    private String contentType = "text/html; charset=UTF-8";
    
    private List<String> attachs = new ArrayList<String>(); //full name of attachments to send
    private List<FileOnTheFly> flyAttachs = new ArrayList<FileOnTheFly>();
    
    public EmailUtil(){
    	super();
    }
	public EmailUtil(String smtp_host, int smtp_port, String smtp_auth,
			String smtp_auth_user, String smtp_auth_pwd) {
		super();
		SMTP_HOST = smtp_host;
		SMTP_PORT = smtp_port;
		SMTP_AUTH = smtp_auth;
		SMTP_AUTH_USER = smtp_auth_user;
		SMTP_AUTH_PWD = smtp_auth_pwd;
	}
	
	public void send(String fromName, String fromAddress, String toName, String toAddress, String subject, String text) throws Exception{
		
		String[] recipients = toAddress.split(",|;");
		
		//System.out.println("Recipients : " + recipients.length);
		
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", SMTP_HOST);
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.port", String.valueOf(SMTP_PORT));
		props.setProperty("mail.smtp.auth", SMTP_AUTH);
		//props.setProperty("mail.smtp.allow8bitmime", "true");
		
		javax.mail.Authenticator auth = new SMTPAuthenticator();
		Session mailSession = Session.getInstance(props,auth);
		// uncomment for debugging infos to stdout
		//mailSession.setDebug(true);
		
		Transport transport = mailSession.getTransport();
		MimeMessage message = new MimeMessage(mailSession);
		message.setFrom(new InternetAddress(fromAddress,fromName,"UTF-8"));
		message.setSubject(subject,"UTF-8");
		message.setContent(text, contentType);

		//transport.connect(SMTP_HOST, SMTP_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
        transport.connect();
        if(recipients==null || recipients.length==0)
        	message.setRecipient(Message.RecipientType.TO,new InternetAddress(toAddress,toName,"UTF-8"));
        else{
        	Address[] addrs = new Address[recipients.length];
        	for(int i=0;i<recipients.length;i++){
        		addrs[i]=new InternetAddress(recipients[i],toName,"UTF-8");
        	}
        	message.setRecipients(Message.RecipientType.TO, addrs);
        }
		transport.sendMessage(message,message.getRecipients(Message.RecipientType.TO));
		transport.close();
    }
	/*public void send(String fromName, String fromAddress, String toName, String toAddress, String assunto, String texto,List<String> attachs) throws Exception{
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", SMTP_HOST);
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.port", String.valueOf(SMTP_PORT));
		props.setProperty("mail.smtp.auth", SMTP_AUTH);
		//props.setProperty("mail.smtp.allow8bitmime", "true");
		
		javax.mail.Authenticator auth = new SMTPAuthenticator();
		Session mailSession = Session.getInstance(props,auth);
		// uncomment for debugging infos to stdout
		//mailSession.setDebug(true);
		
		Transport transport = mailSession.getTransport();
		MimeMessage message = new MimeMessage(mailSession);
		message.setFrom(new InternetAddress(fromAddress,fromName,"UTF-8"));
		message.setSubject(assunto,"UTF-8");
		//message.setContent(texto, "text/html; charset=UTF-8");
		
		MimeBodyPart messagePart = new MimeBodyPart();
        messagePart.setText(texto);
        
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messagePart);
        for(String filename:attachs){
        	MimeBodyPart attachmentPart = new MimeBodyPart();
    		FileDataSource fileDataSource = new FileDataSource(filename) {
                @Override
                public String getContentType() {
                    return "application/octet-stream";
                }
            };
            attachmentPart.setDataHandler(new DataHandler(fileDataSource));
            String[] nameParts = filename.split("/");
            if(nameParts.length>0)
            	attachmentPart.setFileName(nameParts[nameParts.length-1]);
            else
            	attachmentPart.setFileName(filename);
            multipart.addBodyPart(attachmentPart);
        }
        
        message.setContent(multipart);
		//transport.connect(SMTP_HOST, SMTP_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
        transport.connect();
		message.setRecipient(Message.RecipientType.TO,new InternetAddress(toAddress,toName,"UTF-8"));
		transport.sendMessage(message,message.getRecipients(Message.RecipientType.TO));
		transport.close();
    }*/
	public void send(String fromName, String fromAddress, String toName, String toAddress, String subject, String text, 
			List<FileOnTheFly> attachs) throws Exception{
		
		String[] recipients = toAddress.split(",|;");
		
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", SMTP_HOST);
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.port", String.valueOf(SMTP_PORT));
		props.setProperty("mail.smtp.auth", SMTP_AUTH);
		//props.setProperty("mail.smtp.allow8bitmime", "true");
		
		javax.mail.Authenticator auth = new SMTPAuthenticator();
		Session mailSession = Session.getInstance(props,auth);
		// uncomment for debugging infos to stdout
		//mailSession.setDebug(true);
		
		Transport transport = mailSession.getTransport();
		MimeMessage message = new MimeMessage(mailSession);
		message.setFrom(new InternetAddress(fromAddress,fromName,"UTF-8"));
		message.setSubject(subject,"UTF-8");
		//message.setContent(texto, "text/html; charset=UTF-8");
		
		MimeBodyPart messagePart = new MimeBodyPart();
		messagePart.setContent(text, this.contentType);
		
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messagePart);
        for(FileOnTheFly file:attachs){
        	String filename = file.getFilename();
        	
        	InputStream is = file.getStream();
        	//BufferedReader br = new BufferedReader(new InputStreamReader(is));
    		//InputStreamReader isr = new InputStreamReader(is);
    		//char[] tb =  new char[file.getSize()];
    		//isr.read(tb);
    		
    		File f1 = new File(file.getFilename());
    		
    		//Writer out1 = new BufferedWriter(new FileWriter(f1));
    		byte[] bytes = new byte[file.getSize()];
    		is.read(bytes);
    		FileOutputStream fos = new FileOutputStream(f1);
    		fos.write(bytes);
    		fos.close();
    		//out1.write(tb);
    		//out1.close();
        	
        	MimeBodyPart attachmentPart = new MimeBodyPart();
        	
        	FileDataSource fileDataSource = new FileDataSource(f1) {
                @Override
                public String getContentType() {
                    return "application/octet-stream";
                }
            };
            attachmentPart.setDataHandler(new DataHandler(fileDataSource));
    		//System.out.println(attachmentPart.getSize());
            String[] nameParts = filename.split("/");
            if(nameParts.length>0)
            	attachmentPart.setFileName(nameParts[nameParts.length-1]);
            else
            	attachmentPart.setFileName(filename);
            multipart.addBodyPart(attachmentPart);
        }
        
        message.setContent(multipart);
		//transport.connect(SMTP_HOST, SMTP_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
        transport.connect();
        if(recipients==null || recipients.length==0)
        	message.setRecipient(Message.RecipientType.TO,new InternetAddress(toAddress,toName,"UTF-8"));
        else{
        	Address[] addrs = new Address[recipients.length];
        	for(int i=0;i<recipients.length;i++){
        		addrs[i]=new InternetAddress(recipients[i],toName,"UTF-8");
        	}
        	message.setRecipients(Message.RecipientType.TO, addrs);
        }
		//message.setRecipient(Message.RecipientType.TO,new InternetAddress(toAddress,toName,"UTF-8"));
		transport.sendMessage(message,message.getRecipients(Message.RecipientType.TO));
		transport.close();
    }
	public void send(String fromName, String fromAddress, String toName, String toAddress, String assunto, String texto, 
			Object[] attachs) throws Exception{
		
		String[] recipients = toAddress.split(",|;");
		
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", SMTP_HOST);
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.port", String.valueOf(SMTP_PORT));
		props.setProperty("mail.smtp.auth", SMTP_AUTH);
		//props.setProperty("mail.smtp.allow8bitmime", "true");
		
		javax.mail.Authenticator auth = new SMTPAuthenticator();
		Session mailSession = Session.getInstance(props,auth);
		// uncomment for debugging infos to stdout
		//mailSession.setDebug(true);
		
		Transport transport = mailSession.getTransport();
		MimeMessage message = new MimeMessage(mailSession);
		
		message.setFrom(new InternetAddress(fromAddress,fromName,"UTF-8"));
		message.setSubject(assunto,"UTF-8");
		
		MimeBodyPart messagePart = new MimeBodyPart();
        //messagePart.setText(texto,"UTF-8");
        //System.out.println("[SEND EMAIL] contentType:" + messagePart.getContentType());
        messagePart.setContent(texto, this.contentType);
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messagePart);
        
        for(Object obj:attachs){
        	File file = (File) obj;
        	MimeBodyPart attachmentPart = new MimeBodyPart();
        	FileDataSource fileDataSource = new FileDataSource(file) {
                @Override
                public String getContentType() {
                    return "application/octet-stream";
                }
            };
            attachmentPart.setDataHandler(new DataHandler(fileDataSource));
    		//System.out.println(attachmentPart.getSize());
            String[] nameParts = file.getName().split("/");
            if(nameParts.length>0)
            	attachmentPart.setFileName(nameParts[nameParts.length-1]);
            else
            	attachmentPart.setFileName(file.getName());
            multipart.addBodyPart(attachmentPart);
        }
        message.setContent(multipart);
		//transport.connect(SMTP_HOST, SMTP_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
        transport.connect();
		//message.setRecipient(Message.RecipientType.TO,new InternetAddress(toAddress,toName,"UTF-8"));
        if(recipients==null || recipients.length==0)
        	message.setRecipient(Message.RecipientType.TO,new InternetAddress(toAddress,toName,"UTF-8"));
        else{
        	Address[] addrs = new Address[recipients.length];
        	for(int i=0;i<recipients.length;i++){
        		addrs[i]=new InternetAddress(recipients[i],toName,"UTF-8");
        	}
        	message.setRecipients(Message.RecipientType.TO, addrs);
        }
		
		transport.sendMessage(message,message.getRecipients(Message.RecipientType.TO));
		transport.close();
    }
    private class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
           return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
        }
    }
    public void addAttachment(String filename){
    	this.attachs.add(filename);
    }
    public void addFlyAttachment(FileOnTheFly attachment){
    	this.flyAttachs.add(attachment);
    }
    //getters and setters
	public List<String> getAttachs() {
		return attachs;
	}
	public void setAttachs(List<String> attachs) {
		this.attachs = attachs;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public List<FileOnTheFly> getFlyAttachs() {
		return flyAttachs;
	}
	public void setFlyAttachs(List<FileOnTheFly> flyAttachs) {
		this.flyAttachs = flyAttachs;
	}
	public String getSMTP_HOST() {
		return SMTP_HOST;
	}
	public void setSMTP_HOST(String smtp_host) {
		SMTP_HOST = smtp_host;
	}
	public int getSMTP_PORT() {
		return SMTP_PORT;
	}
	public void setSMTP_PORT(int smtp_port) {
		SMTP_PORT = smtp_port;
	}
	public String getSMTP_AUTH() {
		return SMTP_AUTH;
	}
	public void setSMTP_AUTH(String smtp_auth) {
		SMTP_AUTH = smtp_auth;
	}
	public String getSMTP_AUTH_USER() {
		return SMTP_AUTH_USER;
	}
	public void setSMTP_AUTH_USER(String smtp_auth_user) {
		SMTP_AUTH_USER = smtp_auth_user;
	}
	public String getSMTP_AUTH_PWD() {
		return SMTP_AUTH_PWD;
	}
	public void setSMTP_AUTH_PWD(String smtp_auth_pwd) {
		SMTP_AUTH_PWD = smtp_auth_pwd;
	}
}
