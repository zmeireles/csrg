package com.obc.csrg.util;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

public class PopFaxUtil extends EmailUtil implements Serializable{

	private int sendMethod=1;//1dest numbers sent in Body: field; 2 dest numbers sent in To: field
	private boolean ack = false;
	private boolean dithering = false;
	private boolean tab= false;
	private boolean attachment = false;
	private boolean noReport = false;
	private boolean sendId = false;
	private Long id = 0L;
	private String faxAccount = "";
	private String faxPwd = "";
	private String faxEmail = "";
	private List<String> destNumbers = new ArrayList<String>();
	private final String newLine = "\n";
	

	//constructors
	public PopFaxUtil(){
		super();
	}
	public PopFaxUtil(String faxAccount,String faxPwd,String faxEmail){
		super();
		this.faxAccount = faxAccount;
		this.faxPwd = faxPwd;
		this.faxEmail = faxEmail;
	}
	public PopFaxUtil(String smtpHost, int smtpPort, String smtpAuth,
			String smtpAuthUser, String smtpAuthPwd,
			String faxAccount,String faxPwd,String faxEmail){
		super(smtpHost,smtpPort,smtpAuth,smtpAuthUser,smtpAuthPwd);
		
		this.faxAccount = faxAccount;
		this.faxPwd = faxPwd;
		this.faxEmail = faxEmail;
	}
	
	//business methods
	public void sendFax() throws Exception{
		String from =  this.faxAccount;
		String to = this.faxEmail;//default for method 1
		String subject = this.faxPwd;
		String body = this.buildBody();
		
		//System.out.println("Body");
		//System.out.println(body);
		if(this.sendMethod==2){
			//code here
		}
		this.setContentType("text/plain; charset=UTF-8");
		//this.send("Client", from, "", to, subject, body,this.getAttachs());
		this.send("Client", from, "", to, subject, body,this.getFlyAttachs());
	}
	public void addDestNumber(String faxNumber){
		this.destNumbers.add(faxNumber);
	}
	private String buildBody(){
		String result = "";
		if(this.sendMethod==1){
			for(String destNumber:this.destNumbers){
				result += result.equals("") ? "+" + destNumber : newLine + "+" + destNumber;
			}
			if(this.sendId)
				result += result.equals("") ? "/ID_" + this.id : newLine + "/ID_" + this.id;
			if(this.ack)
				result += result.equals("") ? "/ACK" : newLine + "/ACK";
			if(this.dithering)
				result += result.equals("") ? "/DITHERING" : newLine + "/DITHERING";
			if(this.tab)
				result += result.equals("") ? "/TAB" : newLine + "/TAB";
			if(this.attachment)
				result += result.equals("") ? "/ATTACHMENT" : newLine + "/ATTACHMENT";
		}else if(this.sendMethod==2){
			if(this.sendId)
				result += result.equals("") ? "/ID_" + this.id : newLine + "/ID_" + this.id;
			if(this.ack)
				result += result.equals("") ? "/ACK" : newLine + "/ACK";
			if(this.dithering)
				result += result.equals("") ? "/DITHERING" : newLine + "/DITHERING";
			if(this.tab)
				result += result.equals("") ? "/NOREPORT" : newLine + "/NOREPORT";
		}
		return result;
	}
	//getters and setters
	public int getSendMethod() {
		return sendMethod;
	}
	public void setSendMethod(int sendMethod) {
		this.sendMethod = sendMethod;
	}
	public boolean isAck() {
		return ack;
	}
	public void setAck(boolean ack) {
		this.ack = ack;
	}
	public boolean isDithering() {
		return dithering;
	}
	public void setDithering(boolean dithering) {
		this.dithering = dithering;
	}
	public boolean isTab() {
		return tab;
	}
	public void setTab(boolean tab) {
		this.tab = tab;
	}
	public boolean isAttachment() {
		return attachment;
	}
	public void setAttachment(boolean attachment) {
		this.attachment = attachment;
	}
	public boolean isNoReport() {
		return noReport;
	}
	public void setNoReport(boolean noReport) {
		this.noReport = noReport;
	}
	public String getFaxAccount() {
		return faxAccount;
	}
	public void setFaxAccount(String faxAccount) {
		this.faxAccount = faxAccount;
	}
	public String getFaxPwd() {
		return faxPwd;
	}
	public void setFaxPwd(String faxPwd) {
		this.faxPwd = faxPwd;
	}
	public String getFaxEmail() {
		return faxEmail;
	}
	public void setFaxEmail(String faxEmail) {
		this.faxEmail = faxEmail;
	}
	public boolean isSendId() {
		return sendId;
	}
	public void setSendId(boolean sendId) {
		this.sendId = sendId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<String> getDestNumbers() {
		return destNumbers;
	}
	public void setDestNumbers(List<String> destNumbers) {
		this.destNumbers = destNumbers;
	}
}
