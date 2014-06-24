package com.obc.csrg.local;

import javax.ejb.Local;

@Local
public interface SendMessageLocal {

	public String getToEmail();
	public void setToEmail(String toEmail);
	public String getSubject();
	public void setSubject(String subject);
	public String getText();
	public void setText(String text);
	public String sendEmail();
	public String getFeedback();
	public void setFeedback(String feedback);
	public String sendSms();
	
	public String getMobilePrefix();
	public void setMobilePrefix(String mobilePrefix);
	public String getMobileNumber();
	public void setMobileNumber(String mobileNumber);
}
