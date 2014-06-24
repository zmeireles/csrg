package com.obc.csrg.action;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import java.io.Serializable;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.obc.csrg.local.SendNotificationLocal;
import com.obc.csrg.local.StateBeanLocal;
import com.obc.csrg.util.NotificationUtil;
import com.obc.csrg.model.Notification;
import com.obc.csrg.model.Config;

@Stateless
@Name("sendNotificationAction")
public class SendNotificationAction implements SendNotificationLocal,Serializable{

	@Logger 
	private Log log;
	
	@In 
	private EntityManager entityManager;
	
	@In
	protected Map<String,String> messages;
	
	public boolean sendNotification(Long notificationId){
		log.info("[sendNotification] notificationId:#0", notificationId);
		List<Notification> notifications = entityManager.createQuery("select m from Notification m where m.id=?")
							.setParameter(1, notificationId)
							.getResultList();
		log.info("[sendNotification] notification count:#0", notifications.size());
		if(notifications.size()>0){
			Notification notification = notifications.get(0);
			NotificationUtil notificationUtil = new NotificationUtil(entityManager,log,messages);
			notificationUtil.notifyTargets(notification, entityManager, log, this.getConfig(),false);
			return true;
		}
		return false;
	}
	private Config getConfig() {
		Config config = null;
		try {
			List<Config> configs = entityManager.createQuery(
					"select c from Config c").setHint(
					"org.hibernate.cacheable", true).getResultList();
			if (configs.size() > 0) {
				config = configs.get(0);
			} else {
				config = null;
			}
		} catch (Exception ex) {
			config = null;
		}
		return config;
	}
}
