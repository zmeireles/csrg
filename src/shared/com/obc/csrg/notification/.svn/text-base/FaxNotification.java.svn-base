package com.obc.csrg.notification;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.jboss.seam.log.Log;

import com.obc.csrg.model.Config;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.FaxLog;
import com.obc.csrg.model.User;
import com.obc.csrg.model.Person;
import com.obc.csrg.util.PopFaxUtil;
import com.obc.csrg.util.FileOnTheFly;
import com.obc.csrg.model.FaxAttachment;


public class FaxNotification extends EmailNotification{

	//constructors
	public FaxNotification(EntityManager entityManager){
		super(entityManager);
	}
	public FaxNotification(EntityManager entityManager,Log log){
		super(entityManager,log);
	}
	public FaxNotification(EntityManager entityManager,Log log,Config config){
		super(entityManager,log,config);
	}
	
	//business methods
	public <T extends Model> boolean sendNotification(String fromName, T to,String toFaxNumber,File file){
		boolean result = true;
		this.getLog().trace("[sendNotificacao] fromEntidade: #0, to: #1", fromName, to);
		if(to==null) 
			return false;
		FaxLog history = new FaxLog();
		if(to instanceof Person)
			history.setToPerson((Person)to);
		else if(to instanceof User){
			User u = (User)to;
			if(u.getPerson()!=null)
				history.setToPerson(u.getPerson());
		}else{
			this.getLog().error("#0 [sendNotificacao] o argumento 'to' esta num model nao previsto -> #1", 
					this.toString().substring(this.toString().lastIndexOf(".")), to.getClass().getName());
			return false;
		}
		this.getEntityManager().persist(history);
		this.getEntityManager().flush();
		history.setFromName(fromName);
		history.setToFaxNumber(toFaxNumber);
		String toName = history.getToPerson().getName();
		history.setToName(toName);
		history.setSentDate(new Date());
		FaxAttachment attachment = new FaxAttachment();
		if(this.getConfig()==null)
			this.initConfig();
		String filename = file.getAbsolutePath();
		filename = filename.replace("\\", "/");
		attachment.setFilename(file.getName());
		attachment.setFaxLog(history);
		history.getAttachments().add(attachment);
		try{
			InputStream is = new FileInputStream(filename);
			int fileLength = ((Long)file.length()).intValue();
			FileOnTheFly fileFly = new FileOnTheFly(filename,is,fileLength);
			Config config = this.getConfig();
			attachment.setData(new byte[fileLength]);
			new FileInputStream(filename).read(attachment.getData());
			PopFaxUtil fax = new PopFaxUtil(config.getSmtpHost(),
					config.getSmtpPort(),
					((Boolean)config.getSmtpAuth()).toString(),
					config.getSmtpAuthUser(),
					config.getSmtpAuthPwd(),
					config.getFaxAccount(),config.getFaxPwd(),config.getFaxEmail());
			fax.addDestNumber(toFaxNumber);
			fax.addFlyAttachment(fileFly);
			
			fax.setAck(true);
			//fax.setDithering(true);
			fax.setId(history.getId());
			fax.setSendId(true);
			fax.setTab(true);
			fax.setAttachment(true);
			//fax.setFaxEmail("zmeireles@gmail.com");//enviar para mim
			//fax.setFaxEmail("stickman.san@gmail.com");
			fax.sendFax();
			history.setSent(true);
			history.setSuccess(true);
			this.getEntityManager().merge(history);
			result = true;
		}catch (Exception e){
			e.printStackTrace();
			history.setSent(false);
			history.setSuccess(false);
			history.setResponse(e.getMessage());
			history.setRetry(true);
			result = false;
		}finally{
			this.getEntityManager().merge(history);
			this.getEntityManager().flush();
		}
		
		return result;
	}
	public <T extends Model> boolean sendNotification(String fromName, T to,String toFaxNumber,List<File> files){
		boolean result = true;
		this.getLog().trace("[sendNotificacao] fromEntidade: #0, to: #1", fromName, to);
		if(to==null) 
			return false;
		FaxLog history = new FaxLog();
		if(to instanceof Person)
			history.setToPerson((Person)to);
		else if(to instanceof User){
			User u = (User)to;
			if(u.getPerson()!=null)
				history.setToPerson(u.getPerson());
		}else{
			this.getLog().error("#0 [sendNotificacao] o argumento 'to' esta num model nao previsto -> #1", 
					this.toString().substring(this.toString().lastIndexOf(".")), to.getClass().getName());
			return false;
		}
		this.getEntityManager().persist(history);
		this.getEntityManager().flush();
		history.setFromName(fromName);
		history.setToFaxNumber(toFaxNumber);
		String toName = history.getToPerson().getName();
		history.setToName(toName);
		history.setSentDate(new Date());
		FaxAttachment attachment = new FaxAttachment();
		if(this.getConfig()==null)
			this.initConfig();
		
		try{
			System.out.println("XXXXX");
			Config config = this.getConfig();
			PopFaxUtil fax = new PopFaxUtil(config.getSmtpHost(),
					config.getSmtpPort(),
					((Boolean)config.getSmtpAuth()).toString(),
					config.getSmtpAuthUser(),
					config.getSmtpAuthPwd(),
					config.getFaxAccount(),config.getFaxPwd(),config.getFaxEmail());
			fax.addDestNumber(toFaxNumber);
			for(File file:files){
				String filename = file.getAbsolutePath();
				filename = filename.replace("\\", "/");
				attachment.setFilename(file.getName());
				attachment.setFaxLog(history);
				history.getAttachments().add(attachment);
				InputStream is = new FileInputStream(filename);
				int fileLength = ((Long)file.length()).intValue();
				FileOnTheFly fileFly = new FileOnTheFly(filename,is,fileLength);
				attachment.setData(new byte[fileLength]);
				new FileInputStream(filename).read(attachment.getData());
				fax.addFlyAttachment(fileFly);
			}
			fax.setAck(true);
			//fax.setDithering(true);
			fax.setId(history.getId());
			fax.setSendId(true);
			fax.setTab(true);
			fax.setAttachment(true);
			//fax.setFaxEmail("zmeireles@gmail.com");//enviar para mim
			fax.sendFax();
			history.setSent(true);
			history.setSuccess(true);
			this.getEntityManager().merge(history);
			result = true;
		}catch (Exception e){
			e.printStackTrace();
			history.setSent(false);
			history.setSuccess(false);
			history.setResponse(e.getMessage());
			history.setRetry(true);
			result = false;
		}finally{
			this.getEntityManager().merge(history);
			this.getEntityManager().flush();
		}
		
		return result;
	}
	public <T extends Model> boolean sendNotification(String fromName,User fromUser,String toFaxNumber,List<File> files){
		boolean result = true;
		FaxLog history = new FaxLog();
		
		this.getEntityManager().persist(history);
		this.getEntityManager().flush();
		history.setFromName(fromName);
		history.setToFaxNumber(toFaxNumber);
		String toName = "";
		history.setToName(toName);
		history.setSentDate(new Date());
		FaxAttachment attachment = new FaxAttachment();
		if(this.getConfig()==null)
			this.initConfig();
		
		try{
			System.out.println("XXXXX");
			Config config = this.getConfig();
			PopFaxUtil fax = new PopFaxUtil(config.getSmtpHost(),
					config.getSmtpPort(),
					((Boolean)config.getSmtpAuth()).toString(),
					config.getSmtpAuthUser(),
					config.getSmtpAuthPwd(),
					config.getFaxAccount(),config.getFaxPwd(),config.getFaxEmail());
			fax.addDestNumber(toFaxNumber);
			for(File file:files){
				String filename = file.getAbsolutePath();
				filename = filename.replace("\\", "/");
				attachment.setFilename(file.getName());
				attachment.setFaxLog(history);
				history.getAttachments().add(attachment);
				InputStream is = new FileInputStream(filename);
				int fileLength = ((Long)file.length()).intValue();
				FileOnTheFly fileFly = new FileOnTheFly(filename,is,fileLength);
				attachment.setData(new byte[fileLength]);
				new FileInputStream(filename).read(attachment.getData());
				fax.addFlyAttachment(fileFly);
			}
			fax.setAck(true);
			//fax.setDithering(true);
			fax.setId(history.getId());
			fax.setSendId(true);
			fax.setTab(true);
			fax.setAttachment(true);
			//fax.setFaxEmail("jmeireles@objectconnection.pt");//enviar para mim
			System.out.println("Vou enviar fax");
			fax.sendFax();
			history.setSent(true);
			history.setSuccess(true);
			this.getEntityManager().merge(history);
			result = true;
		}catch (Exception e){
			e.printStackTrace();
			history.setSent(false);
			history.setSuccess(false);
			history.setResponse(e.getMessage());
			history.setRetry(true);
			result = false;
		}finally{
			this.getEntityManager().merge(history);
			this.getEntityManager().flush();
		}
		
		return result;
	}
	//getters and setters
}
