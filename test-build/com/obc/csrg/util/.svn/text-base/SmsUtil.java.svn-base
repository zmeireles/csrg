package com.obc.csrg.util;

import java.io.FilterInputStream;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import javax.persistence.EntityManager;

import org.jboss.seam.log.Log;

import com.obc.csrg.model.Config;
import com.obc.csrg.model.SmsLog;
import com.obc.csrg.model.Person;
import com.obc.csrg.model.User;

public class SmsUtil {

	private EntityManager entityManager;
	private User user;
	private Log log;
	private Map<String,String> messages;
	
	private String smsSource;
	private String smsMessage;
	private String destinationCellphone;
	private Long senderId; //user que envia a mensagem
	private Person toPerson;
	private String logSms;
	private boolean registration=false;//sms sent on user registration or not
	private final int SMS_LIMIT = 160;
	private Config config;
	//private String[] EncodeingTexts = {" ","!","@","#","%","&","*","(",")","+","-",".","/"};
	//private String[] EncodeingValues = {"%20","%21","%40","%23","%25","%26","%2A","%28","%29","%2B","%2D","%2E","%2F"};

	
	
	
	//contructors
	public SmsUtil(User user,EntityManager entityManager,Log log,Map<String,String> messages){
		this.user = user;
		this.entityManager = entityManager;
		this.log = log;
		this.messages = messages;
	}
	public SmsUtil(Config config,String smsMessage,String destinationCellphone){
		this.config = config;
		this.smsSource = config.getSmsSource();
		this.smsMessage = smsMessage;
		this.destinationCellphone = destinationCellphone;
	}
	public SmsUtil(Config config,String smsMessage,String destinationCellphone, 
			User user,EntityManager entityManager, Log log,Map<String,String> messages){
		this.config = config;
		this.smsSource = config.getSmsSource();
		this.smsMessage = smsMessage;
		this.destinationCellphone = destinationCellphone;
		this.user = user;
		this.log = log;
		this.messages = messages;
		this.entityManager = entityManager;
	}
	
	//business logic
	public boolean sendDirectSms(){
		boolean success = false;
		if(!this.okToSendSms()){
			return false;
		}
		Config config = null;
		if(this.config==null)
			config = (Config)entityManager.createQuery("select obj from Config obj")
						.setHint("org.hibernate.cacheable", true).getSingleResult();
		else
			config=this.config;
		
		if(config.getUrlSms()==null || config.getUsernameSms()==null || config.getPasswordSms()==null)
			return false;
		boolean boolAllowCSms = config.isAllowCSms();
		String msgPrefix = config.getUrlSms() + "?username=" + config.getUsernameSms() + "&password=" +
								config.getPasswordSms() + "&message=";
		String msgSuffix = "&msisdn=" + this.destinationCellphone + "&sender=" + this.getSmsSource() + 
								"&allow_concat_text_sms=" + (boolAllowCSms ? "1" : "0");
		if(log!=null) log.info("[sendSms] prefix: [#0] suffix[#1]", msgPrefix, msgSuffix);
		try{
			String[] texts = this.getMessages(boolAllowCSms);
			String text = "";
			for(int i=0;i<texts.length;i++){
				//log.info("[sendSms] send message: #0", texts[i]);
				
				//for(int j =0; j<EncodeingTexts.length;j++)
				//texts[i] = texts[i].replace(EncodeingTexts[j], EncodeingValues[j]);
			
				texts[i] = texts[i].replace("%", "%25");
				texts[i] = texts[i].replace("&", "%26");
				texts[i] = texts[i].replace("'", "%27");
				texts[i] = texts[i].replace("(", "%28");
				texts[i] = texts[i].replace(")", "%29");
				texts[i] = texts[i].replace("*", "%2A");
				texts[i] = texts[i].replace("+", "%2B");
				texts[i] = texts[i].replace(",", "%2C");
				texts[i] = texts[i].replace("-", "%2D");
				texts[i] = texts[i].replace(".", "%2E");
				texts[i] = texts[i].replace("/", "%2F");
				texts[i] = texts[i].replace(":", "%3A");
				texts[i] = texts[i].replace(";", "%3B");
				texts[i] = texts[i].replace("<", "%3C");
				texts[i] = texts[i].replace("=", "%3D");
				texts[i] = texts[i].replace(">", "%3E");
				texts[i] = texts[i].replace("?", "%3F");
				texts[i] = texts[i].replace("@", "%40");
				texts[i] = texts[i].replace("_", "%5F");
				texts[i] = texts[i].replace("¡", "%A1");
				texts[i] = texts[i].replace("£", "%A3");
				texts[i] = texts[i].replace("¤", "%A4");
				texts[i] = texts[i].replace("¥", "%A5");
				texts[i] = texts[i].replace("§", "%A7");
				texts[i] = texts[i].replace("¿", "%BF");
				texts[i] = texts[i].replace("ß", "%DF");
				
				texts[i] = texts[i].replace("ƒ", "F");
				texts[i] = texts[i].replace("Š", "S");
				texts[i] = texts[i].replace("Œ", "O");
				texts[i] = texts[i].replace("Ž", "Z");
				texts[i] = texts[i].replace("š", "s");
				texts[i] = texts[i].replace("œ", "o");
				texts[i] = texts[i].replace("ž", "z");
	
				texts[i] = texts[i].replace("Ä", "A");
				texts[i] = texts[i].replace("Å", "A");
				texts[i] = texts[i].replace("Æ", "A");
				texts[i] = texts[i].replace("À", "A");
				texts[i] = texts[i].replace("Á", "A");
				texts[i] = texts[i].replace("Â", "A");
				texts[i] = texts[i].replace("Ã", "A");
				
				texts[i] = texts[i].replace("Ç", "C");
				
				texts[i] = texts[i].replace("É", "E");
				texts[i] = texts[i].replace("È", "E");
				texts[i] = texts[i].replace("Ê", "E");
				texts[i] = texts[i].replace("Ë", "E");
				
				texts[i] = texts[i].replace("Ì", "I");
				texts[i] = texts[i].replace("Í", "I");
				texts[i] = texts[i].replace("Î", "I");
				texts[i] = texts[i].replace("Ï", "I");
				
				texts[i] = texts[i].replace("Ñ", "N");
				
				texts[i] = texts[i].replace("Ö", "O");
				texts[i] = texts[i].replace("Ø", "O");
				texts[i] = texts[i].replace("Ò", "O");
				texts[i] = texts[i].replace("Ó", "O");
				texts[i] = texts[i].replace("Ô", "O");
				texts[i] = texts[i].replace("Õ", "O");
				
				texts[i] = texts[i].replace("Ü", "U");
				texts[i] = texts[i].replace("Ù", "U");
				texts[i] = texts[i].replace("Ú", "U");
				texts[i] = texts[i].replace("Û", "U");
				
				texts[i] = texts[i].replace("Ý", "Y");
				texts[i] = texts[i].replace("Ÿ", "Y");

				texts[i] = texts[i].replace("à", "a");
				texts[i] = texts[i].replace("ä", "a");
				texts[i] = texts[i].replace("å", "a");
				texts[i] = texts[i].replace("æ", "a");
				texts[i] = texts[i].replace("á", "a");
				texts[i] = texts[i].replace("â", "a");
				texts[i] = texts[i].replace("ã", "a");
				
				texts[i] = texts[i].replace("ç", "c");
				
				texts[i] = texts[i].replace("ê", "e");
				texts[i] = texts[i].replace("ë", "e");
				texts[i] = texts[i].replace("è", "e");
				texts[i] = texts[i].replace("é", "e");
				
				texts[i] = texts[i].replace("ì", "i");
				texts[i] = texts[i].replace("ï", "i");
				texts[i] = texts[i].replace("î", "i");
				texts[i] = texts[i].replace("ò", "o");
				
				texts[i] = texts[i].replace("ö", "o");
				texts[i] = texts[i].replace("ø", "o");
				texts[i] = texts[i].replace("ô", "o");
				texts[i] = texts[i].replace("ó", "o");
				texts[i] = texts[i].replace("õ", "o");
				
				texts[i] = texts[i].replace("ñ", "n");
				
				texts[i] = texts[i].replace("ù", "u");
				texts[i] = texts[i].replace("ü", "u");
				texts[i] = texts[i].replace("ú", "u");
				texts[i] = texts[i].replace("û", "u");

				texts[i] = texts[i].replace("!", "%21");
				texts[i] = texts[i].replace("\"", "%22");
				texts[i] = texts[i].replace("#", "%23");
				texts[i] = texts[i].replace(" ", "%20");


				
				
				
				URL url = new URL(msgPrefix + texts[i] + msgSuffix);
				//log.info("URL SMS:#0", msgPrefix + texts[i] + msgSuffix );
				//log.info("sendSms] Port:#0, Protocol#1, Path:#2", url.getPort(),url.getProtocol(),url.getPath());
				FilterInputStream result = (FilterInputStream)url.getContent();
				int valor=0;
				char chr;
				while(true){
					valor = result.read();
					if(valor==-1)
						break;
					chr = (char) valor;
					text += chr;
				}
			}
			if(messages!=null)
				this.setLogSms(messages.get("SmsSuccess"));
			else
				this.setLogSms("SmsSuccess");
			
			if(log!=null) log.info("Message sent: #0", text);
			String [] resultArray = text.split("\\|");
			
			if(resultArray.length>0){
				String subString = resultArray[0].trim();
				if(!subString.equals("")){
					int resultado = Integer.parseInt(resultArray[0]);
					if(resultado==0)
						success=true;
				}
			}
		}catch(Exception e){
			if(log!=null) log.info("Erro: #0", e.getMessage());
			if(messages!=null)
				this.setLogSms(messages.get("SmsFail") + ": " + e.getMessage());
			else
				this.setLogSms("SmsFail" + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return success;
	}
	//send sms
	public boolean sendSms(){
		boolean success = false;
		if(!this.okToSendSms()){
			return false;
		}
		Config config = null;
		if(this.config!=null)
			config = this.config;
		else
			config = (Config) entityManager.createQuery("select obj from Config obj")
						.setHint("org.hibernate.cacheable", true).getSingleResult();
		
		if(config.getUrlSms()==null || config.getUsernameSms()==null || config.getPasswordSms()==null)
			return false;
		boolean boolAllowCSms = config.isAllowCSms();
		String msgPrefix = config.getUrlSms() + "?username=" + config.getUsernameSms() + "&password=" +
								config.getPasswordSms() + "&message=";
		String msgSuffix = "&msisdn=" + this.destinationCellphone + "&sender=" + this.getSmsSource() + 
								"&allow_concat_text_sms=" + (boolAllowCSms ? "1" : "0");
		log.info("[sendSms] prefix: [#0] suffix[#1]", msgPrefix, msgSuffix);
		try{
			String[] texts = this.getMessages(boolAllowCSms);
						
			String text = "";
			for(int i=0;i<texts.length;i++){
				log.info("[sendSms] enviar mensagem: #0", texts[i]);
				
				//for(int j =0; j<EncodeingTexts.length;j++)
					//texts[i] = texts[i].replace(EncodeingTexts[j], EncodeingValues[j]);
				
				
				
				texts[i] = texts[i].replace(" ", "%20");

				
				URL url = new URL(msgPrefix + texts[i] + msgSuffix);
				log.info("URL SMS:#0", msgPrefix + texts[i] + msgSuffix );
				log.info("sendSms] Port:#0, Protocol#1, Path:#2", url.getPort(),url.getProtocol(),url.getPath());
				FilterInputStream result = (FilterInputStream)url.getContent();
				int valor=0;
				char chr;
				while(true){
					valor = result.read();
					if(valor==-1)
						break;
					chr = (char) valor;
					text += chr;
				}
			}
			this.setLogSms(messages.get("SmsSuccess"));
			updateSmsLog();
			
			log.info("Message sent: #0", text);
			String [] resultArray = text.split("\\|");
			
			if(resultArray.length>0){
				String subString = resultArray[0].trim();
				if(!subString.equals("")){
					int resultado = Integer.parseInt(resultArray[0]);
					if(resultado==0)
						success=true;
				}
			}
		}catch(Exception e){
			if(log!=null) log.info("Erro: #0", e.getMessage());
			this.setLogSms(messages.get("SmsFail") + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return success;
	}
	/**
	 * se fôr permitido enviar CSMS então activa o parametro de csms
	 * caso contrario e o texto fôr maior que 160 parte em mensagens pequenas
	 * @return
	 */
	private String[] getMessages(boolean allowCSms){
		
		String texto = this.smsMessage;
		
		if(texto.length()<this.SMS_LIMIT || allowCSms)
			return new String[]{texto};
		else{
			int count=texto.length()/this.SMS_LIMIT + 1;
			if(count>254)
				count = 254;
			String[] result = new String[count];
			//log.info("[getMessages] count: #0", count);
			/*byte[] header = new byte[5];
			header[0] = (byte)Integer.parseInt("00",16);
			header[1] = (byte)Integer.parseInt("03",16);
			header[2] = (byte)Integer.parseInt("66",16);
			header[3] = (byte)count;
			for(int i=0;i<4;i++){
				log.info("[getmessages] header[#0]:#1:", i,header[i]);
			}*/
			for(int i=0;i<count;i++){
				String msg = "";
				if(texto.length()>=this.SMS_LIMIT){
					msg = texto.substring(0,this.SMS_LIMIT);
					texto = texto.substring(this.SMS_LIMIT);
				}else{
					msg = texto;
					texto = "";
				}
				/*header[4] = (byte)i;
				log.info("[getmessages] header[4]:#0:",header[4]);
				log.info("[getmessages] header: #0", header);
				result[i] = header + "[" + msg + "]";
				log.info("[getmessages] result: #0", result[i]);*/
				result[i] = msg;
				//log.info("[getmessages] result: #0", result[i]);
			}
			return result;
		}
	}
	private void updateSmsLog(){
		String texto = this.smsMessage;
		String textoSms="";
		//log.info("[actualizaHistorico] entrei");
		while(texto.length()>0){
			SmsLog history = new SmsLog();
			history.setCreationDate(new Date());
			history.setSentDate(new Date());
			history.setSent(true);
			history.setSmsSource(this.getSmsSource());
			history.setDestinationCellphone(this.destinationCellphone);
			history.setRegistration(this.isRegistration());
			if(texto.length()>this.SMS_LIMIT){
				textoSms=texto.substring(0, this.SMS_LIMIT);
				texto=texto.substring(this.SMS_LIMIT);
			}else{
				textoSms=texto;
				texto="";
			}
			history.setText(textoSms);
			history.setSourceUser(user);
			if(this.getToPerson()!=null)
				history.setDestinationUser(this.getToPerson().getUser());
			entityManager.persist(history);
			//log.info("[actualizaHistorico] persisted texto enviado: #0, texto restante: #1", textoSms,texto);
		}
		
	}
	public boolean okToSendSms(){
		boolean result = true;
		
		if(log!=null) log.info("[okToSendSms] source:#0, message:#1, cellphone:#2", this.getSmsSource(),this.smsMessage,this.destinationCellphone);
		if(this.getSmsSource()==null || this.smsMessage==null || this.destinationCellphone==null)
			return false;
		if(this.smsMessage.equals("") || this.destinationCellphone.equals(""))
			result = false;
		if(log!=null) log.info("[okToSendSms] result: #0",result);
		return result;
	}
	
	public String textStatus(){
		final int textLength = this.smsMessage==null ? 0 : this.smsMessage.length();
		int count=textLength/this.SMS_LIMIT + 1;
		String status = messages.get("SmsMaxLengthMessage") + " " + messages.get("SmsCharUsedPrefix") + " " + textLength + 
							" " + messages.get("SmsMessageNumberPrefix") + " " + count;
		return status;
	}
	//getters and setters

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public Map<String, String> getMessages() {
		return messages;
	}

	public void setMessages(Map<String, String> messages) {
		this.messages = messages;
	}

	public String getSmsSource() {
		return smsSource;
	}

	public void setSmsSource(String smsSource) {
		this.smsSource = smsSource;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public String getLogSms() {
		return logSms;
	}

	public void setLogSms(String logSms) {
		this.logSms = logSms;
	}

	public Person getToPerson() {
		return toPerson;
	}

	public void setToPerson(Person toPerson) {
		this.toPerson = toPerson;
	}
	
	public boolean isRegistration() {
		return registration;
	}

	public void setRegistration(boolean registration) {
		this.registration = registration;
	}
	
	public String getDestinationCellphone() {
		return destinationCellphone;
	}

	public void setDestinationCellphone(String destinationCellphone) {
		this.destinationCellphone = destinationCellphone;
	}
	
	public String getSmsMessage() {
		return smsMessage;
	}

	public void setSmsMessage(String smsMessage) {
		this.smsMessage = smsMessage;
	}
}
