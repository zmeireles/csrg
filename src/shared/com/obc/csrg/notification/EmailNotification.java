package com.obc.csrg.notification;

import javax.persistence.EntityManager;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import org.jboss.seam.log.Log;
import java.util.Date;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

import com.obc.csrg.model.EmailLog;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.Person;
import com.obc.csrg.util.EmailUtil;
import com.obc.csrg.model.Config;
import com.obc.csrg.model.User;

public class EmailNotification extends Notification{

	private boolean save=false;
	
	public EmailNotification(EntityManager entityManager){
		super(entityManager);
	}
	public EmailNotification(EntityManager entityManager,Log log){
		super(entityManager,log);
	}
	public EmailNotification(EntityManager entityManager,Log log,Config config){
		super(entityManager,log,config);
	}
	
	public <T extends Model> boolean sendNotification(String fromAddress,String fromName,T to, String subject, String text,List<File> attachs){
		boolean result = true;
		
		EmailLog emailLog = new EmailLog();
		if(to instanceof Person){
			emailLog.setToPerson((Person)to);
		}else if(to instanceof User){
			User u = (User)to;
			if(u.getPerson()!=null )
				emailLog.setToPerson(u.getPerson());
		}else{
			this.getLog().error("#0 [sendNotification] argument 'to' it's in a non predicted model -> #1", 
					this.toString().substring(this.toString().lastIndexOf(".")), to.getClass().getName());
			return false;
		}
		if(save) this.getEntityManager().persist(emailLog);
		if(save) this.getEntityManager().flush();
		emailLog.setSubject(subject);
		emailLog.setText(text);
		emailLog.setFromAddress(fromAddress);
		emailLog.setFromName(fromName);
		//this.getLog().info("[sendNotification] getToEntidade:#0, getToUtente:#1", emailLog.getToEntidade(),emailLog.getToUtente());
		String toAddress = emailLog.getToPerson().getEmail();
		String toName = emailLog.getToPerson().getName();
		emailLog.setCreationDate(new Date());
		emailLog.setSentDate(new Date());
		emailLog.setRetry(false);
		
		emailLog.setToAddress(toAddress);
		emailLog.setToName(toName);
		
		this.getLog().trace("[sendNotification] emailLog: #0", emailLog);
		
		if(this.getConfig()==null)
			this.initConfig();
		Config config = this.getConfig();
		EmailUtil email = new EmailUtil(config.getSmtpHost(),
				config.getSmtpPort(),
				((Boolean)config.getSmtpAuth()).toString(),
				config.getSmtpAuthUser(),
				config.getSmtpAuthPwd());
		try{
			//email.send(fromEntidade.getName(), fromEntidade.getEmailContacto(), toName, toAddress, assunto, texto);
			if(attachs==null)
				attachs = new ArrayList<File>();
			if(attachs.size()==0)
				email.send(fromName, fromAddress, toName, toAddress, subject, text);
			else{
				email.send(fromName, fromAddress, toName, toAddress, subject, text,attachs.toArray());
			}
			emailLog.setSent(true);
			this.getLog().info("[sendNotification] Email sent");
		}catch(MessagingException e){
			emailLog.setSent(false);
			emailLog.setResponse(e.getMessage());
			emailLog.setErrorClass(e.getClass().getName());
			emailLog.setRetry(true);
			this.getLog().info("[sendNotification] Error sending email:#0",e.getMessage());
			result = false;
		}catch(UnsupportedEncodingException e){
			emailLog.setSent(false);
			emailLog.setResponse(e.getMessage());
			emailLog.setErrorClass(e.getClass().getName());
			emailLog.setRetry(true);
			result = false;
			this.getLog().info("[sendNotification] Error sending email:#0",e.getMessage());
		}catch(Exception e){
			emailLog.setSent(false);
			emailLog.setResponse(e.getMessage());
			emailLog.setErrorClass(e.getClass().getName());
			emailLog.setRetry(true);
			result = false;
			this.getLog().info("[sendNotification] Error sending email:#0",e.getMessage());
		}finally{
			if(save){
				this.getEntityManager().merge(emailLog);
				this.getEntityManager().flush();
			}
		}
		return result;
	}
	public  boolean sendNotification(String fromAddress,String fromName,User fromUser, String toEmail, String subject, String text,List<File> attachs){
		boolean result = true;
		
		if(toEmail.equals(""))
			return false;
		EmailLog emailLog = new EmailLog();
		
		if(save) this.getEntityManager().persist(emailLog);
		if(save) this.getEntityManager().flush();
		emailLog.setSubject(subject);
		emailLog.setText(text);
		emailLog.setFromAddress(fromAddress);
		emailLog.setFromName(fromName);
		String toAddress = toEmail;
		String toName = "";
		emailLog.setCreationDate(new Date());
		emailLog.setSentDate(new Date());
		
		emailLog.setToAddress(toAddress);
		emailLog.setToName(toName);
		
		this.getLog().trace("[sendNotification] emailLog: #0", emailLog);
		if(this.getConfig()==null)
			this.initConfig();
		Config config = this.getConfig();
		EmailUtil email = new EmailUtil(config.getSmtpHost(),
				config.getSmtpPort(),
				((Boolean)config.getSmtpAuth()).toString(),
				config.getSmtpAuthUser(),
				config.getSmtpAuthPwd());
		try{
			//email.send(fromEntidade.getName(), fromEntidade.getEmailContacto(), toName, toAddress, assunto, texto);
			
			if(attachs==null)
				attachs = new ArrayList<File>();
			if(attachs.size()==0)
				email.send(fromName, fromAddress, toName, toAddress, subject, text);
			else{
				email.send(fromName, fromAddress, toName, toAddress, subject, text,attachs.toArray());
			}
			emailLog.setSent(true);
			this.getLog().trace("[sendNotificacao] Email sent");
			
		}catch(MessagingException e){
			emailLog.setSent(false);
			emailLog.setResponse(e.getMessage());
			emailLog.setErrorClass(e.getClass().getName());
			emailLog.setRetry(true);
			this.getLog().trace("[sendNotificacao] Error sending email:#0",e.getMessage());
			result = false;
		}catch(UnsupportedEncodingException e){
			emailLog.setSent(false);
			emailLog.setResponse(e.getMessage());
			emailLog.setErrorClass(e.getClass().getName());
			emailLog.setRetry(true);
			result = false;
			this.getLog().trace("[sendNotificacao] Error sending email:#0",e.getMessage());
		}catch(Exception e){
			emailLog.setSent(false);
			emailLog.setResponse(e.getMessage());
			emailLog.setErrorClass(e.getClass().getName());
			emailLog.setRetry(true);
			result = false;
			this.getLog().trace("[sendNotificacao] Error sending email:#0",e.getMessage());
		}finally{
			if(save){
				this.getEntityManager().merge(emailLog);
				this.getEntityManager().flush();
			}
		}
		return result;
	}
}
