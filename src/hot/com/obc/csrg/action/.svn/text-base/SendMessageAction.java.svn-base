package com.obc.csrg.action;

import javax.ejb.Stateless;


import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;


import java.io.Serializable;
import java.util.Map;

import com.obc.csrg.local.SendMessageLocal;
import com.obc.csrg.local.StateBeanLocal;
import com.obc.csrg.util.EmailUtil;
import com.obc.csrg.util.SmsUtil;
import com.obc.csrg.model.Config;

@Stateless
@Name("sendMessageAction")
public class SendMessageAction implements Serializable, SendMessageLocal {

	@Logger
	private Log log;

	@In(required=false)
	protected StateBeanLocal stateBean;
	
	@In
	private Map<String, String> messages;
	
	private String toEmail="";
	private String subject="";
	private String text="";
	
	private String feedback="";
	private String mobilePrefix="351";
	private String mobileNumber="";
	
	//business logic
	public String sendEmail(){
		Config config = stateBean.getConfig();
		EmailUtil emailUtil = new EmailUtil(config.getSmtpHost(),
				config.getSmtpPort(),
				((Boolean)config.getSmtpAuth()).toString(),
				config.getSmtpAuthUser(),
				config.getSmtpAuthPwd());
		
		try{
			emailUtil.send(config.getNotificationSourceName(), config.getNotificationSourceEmail(), "", toEmail, subject, text);
			feedback = messages.get("EmailSendSuccess");
		}catch(Exception e){
			feedback = messages.get("EmailSendSuccess") + " " + e.getMessage();
		}
		this.toEmail="";
		this.subject="";
		this.text="";
		return "";
	}
	public String sendSms(){
		
		//if(this.validateMessage())
		//{
			//feedback =messages.get("SmsContentInvalidCharacter");
		//	return "";
			
		//}
		
		Config config = stateBean.getConfig();
		SmsUtil smsUtil = new SmsUtil(config,this.text,this.mobilePrefix + this.mobileNumber);
		smsUtil.setLog(log);
		smsUtil.setMessages(messages);
		boolean response  = smsUtil.sendDirectSms();
		feedback = smsUtil.getLogSms();
		if(feedback==null || feedback.equals("")){
			if(response)
			{
				feedback = messages.get("SmsSendSuccess");
				//this.text = "";
				//this.mobileNumber = "";

			}
			else
				feedback =messages.get("SmsSendError");
		}
		
		if(response)
		{
			this.text = "";
			this.mobileNumber = "";
		}
		return "";
	}
	
	public boolean validateMessage()
	{
		int count = text.length();
		boolean status = true;
	//	String[] texts = (String[]) text;
		
		for(int i =0;i<count;i++)
		{
			//text[i] = text[i].replace(" ", "%20");
			
			char ch  = (char) text.indexOf(i);
				feedback =messages.get(ch);	
		}
		
		return status;
	}
	//getters and setters
	
	public String getToEmail() {
		return toEmail;
	}
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public String getMobilePrefix() {
		return mobilePrefix;
	}
	public void setMobilePrefix(String mobilePrefix) {
		this.mobilePrefix = mobilePrefix;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
}
