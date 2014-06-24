package com.obc.csrg.notification;

import java.util.List;
import java.io.File;
import javax.persistence.EntityManager;

import org.jboss.seam.log.Log;

import com.obc.csrg.model.Config;
import com.obc.csrg.model.Model;

public abstract class Notification {
	
	private Log log;
	private EntityManager entityManager;
	private Config config;
	
	public Notification(EntityManager entityManager){
		this.entityManager = entityManager;
	}
	public Notification(EntityManager entityManager,Log log){
		this.entityManager = entityManager;
		this.log = log;
	}
	public Notification(EntityManager entityManager,Log log,Config config){
		this.entityManager = entityManager;
		this.log = log;
		this.config = config;
	}
	//business logic
	public abstract <T extends Model> boolean sendNotification(String fomAddress,String fromName,T to, String titulo, String texto,List<File> attachs);
	//getters and setters
	public Log getLog() {
		return log;
	}
	public void setLog(Log log) {
		this.log = log;
	}
	public EntityManager getEntityManager() {
		return entityManager;
	}
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	public void initConfig(){
		try{
			List<Config> configs = entityManager.createQuery("select c from Config c")
					.setHint("org.hibernate.cacheable", true)
					.getResultList();
			if(configs.size()>0){
				config = configs.get(0);
			}else{
				config = null;
			}
		}catch(Exception ex){
			config = null;
		}
	}
	public Config getConfig() {
		return config;
	}
	public void setConfig(Config config) {
		this.config = config;
	}
}
