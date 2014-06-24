package com.obc.csrg.action;

import javax.ejb.Stateless;
import java.util.List;

import java.lang.reflect.Method;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import java.io.Serializable;

import com.obc.csrg.local.ApplicationLocal;
import com.obc.csrg.local.OperationsLocal;
import com.obc.csrg.local.SessionChatLocal;
import com.obc.csrg.local.StateBeanLocal;
import com.obc.csrg.model.User;
import com.obc.csrg.model.Model;
import com.obc.csrg.local.report.UserReportBeanLocal;
import com.obc.csrg.local.report.ReportBeanLocal;
import com.obc.csrg.events.ChatEventQueue;
import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.chat.ChatRoom;
import com.obc.csrg.chat.ChatUser;
import com.obc.csrg.events.ChatEvent;

@Stateless
@Name("operationsBean")
public class OperationsBean implements Serializable, OperationsLocal {

	@Logger
	private Log log;

	@In
	private EntityManager entityManager;
	
	@In(required=false)
	private UserReportBeanLocal userReport;
	
	@In(required=false)
    private StateBeanLocal stateBean;
	
	@In(required=false)
	private SessionChatLocal sessionChatAction;
	
	@In(required = false)
	private ApplicationLocal applicationBean;

	private String param;
	private Long id;

	public void deleteUser(ActionEvent event) {

		Long userId = (Long)event.getComponent().getAttributes().get("userId");
		if(userId!=null){
			List<User> users = entityManager.createQuery("select u from User u where u.id=?")
							.setParameter(1, userId)
							.getResultList();
			if(users.size()>0){
				User user = users.get(0);
				//user.setUsername(user.getUsername() + "-A");
				
				if(user.remove(entityManager)){
					//entityManager.remove(user);
					log.info("[deleteUser] success");
					entityManager.flush();
					userReport.query();
				}
			}
		}
		//log.info("[deleteUser] userID:#0", userId);
	}
	public void deleteModel(ActionEvent event) {

		Long modelId = (Long)event.getComponent().getAttributes().get("modelId");
		String modelName = (String)event.getComponent().getAttributes().get("modelName");
		ReportBeanLocal reportBean = (ReportBeanLocal)event.getComponent().getAttributes().get("report");
		if(modelId!=null){
			List<Model> models = entityManager.createQuery("select m from " + modelName + " m where m.id=?")
							.setParameter(1, modelId)
							.getResultList();
			if(models.size()>0){
				Model model = models.get(0);
				//TODO: create a canRemove or mayRemove function in the model class to be able to know if the operation will succeed.
				//log.info("[deleteModel] delete model:#0", model);
				if(model.remove(entityManager)){
					//log.info("[deleteModel] success");
					entityManager.flush();
					if(reportBean!=null)
						reportBean.query();
				}
			}
		}
	}
	/**
	 * removes an association between to models in a many-to-one relation
	 * Uses method invocation and events to refresh viewers
	 * 
	 * @param event
	 */
	public void deleteManyToOneAssociation(ActionEvent event){
		try{
			Model model = (Model)event.getComponent().getAttributes().get("model");
			String manyToOneMethod = (String)event.getComponent().getAttributes().get("manyToOneMethod");
			String property = (String)event.getComponent().getAttributes().get("property");
			Object oldValue = event.getComponent().getAttributes().get("oldValue");
			Object paramObject = (Object)event.getComponent().getAttributes().get("paramObject");
			ReportBeanLocal reportBean = (ReportBeanLocal)event.getComponent().getAttributes().get("report");
			
			//log.info("[deleteManyToOneAssociation] modelClass:#0, method:#1, paramObjectClass:#2", 
					//model.getClass(),manyToOneMethod,paramObject.getClass());
			Method method = null;
			if(paramObject!=null)
				method = model.getClass().getMethod(manyToOneMethod,paramObject.getClass());
			else
				method = model.getClass().getMethod(manyToOneMethod);
			//execute the method on the object
			Object x=null;
			//log.info("[deleteManyToOneAssociation] before:#0", ((User)model).getServiceArea());
			method.invoke(model,x);
			//log.info("[deleteManyToOneAssociation] after:#0", ((User)model).getServiceArea());
			//fire the model updated/changed event
			entityManager.merge(model);
			ModelEventQueue.fireModelUpdatedEvent(model, property, oldValue,null);
			if(reportBean!=null)
				reportBean.query();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public String deleteUser() {
		
		log.info("[deleteUser] id:#0", this.id);
		return "success";
	}
	
	public void forceLogout(ActionEvent event) {
		//TODO:implement force logout
	}
	public void startChat(ActionEvent event) {
		User destinationUser = (User)event.getComponent().getAttributes().get("destinationUser");
		User sourceUser = sessionChatAction.getMyUser();
		//log.info("[startChat] source:#0, dest:#1", sourceUser,destinationUser);
		if(sourceUser!=null && destinationUser!=null){
			if(sourceUser.getId().equals(destinationUser.getId()))
				return;
			//first check if is there any chat between these 2 people
			//if so, just activate source user and refresh browser
			for(ChatRoom room:sessionChatAction.getChats()){
				if(room.isUserInChatRoom(destinationUser)){
					room.reactivateUser(sourceUser);
					log.info("[startChat] chat:#0 reactivated", room.getName());
					//no need to explicitly refresh browser window, return from action listener will take care of that
					return;
				}
			}
			//create new chat room, chat users and add them to the new chat room
			ChatRoom room = new ChatRoom(sourceUser.getUsername() + "/" + destinationUser.getUsername());
			ChatUser chatter = new ChatUser(sourceUser,sourceUser.getUsername(),room.generateColorCode(),true,false);
			room.getUsers().add(chatter);
			chatter = new ChatUser(destinationUser,destinationUser.getUsername(),room.generateColorCode(),false,false);
			chatter.setActive(false);//set to not active so the chat window will appear only after starter sends a message.
			room.getUsers().add(chatter);
			//fire new chat room created event in order to allow stateBean of users to be refreshed and chat room added
			//log.info("[startChat] fireChatEvent, room:#0, chatters:#1", room,room.getUsers());
			ChatEventQueue.fireNewChatEvent(room);
		}
		
	}
	public void leaveChat(ActionEvent event) {
		//endChatWithUser is the user we want to end chat conversation
		Long roomId = (Long)event.getComponent().getAttributes().get("roomId");
		User leavingUser = sessionChatAction.getMyUser();
		for(ChatRoom room:sessionChatAction.getChats()){
			if(room.getRoomId().equals(roomId)){
				ChatEventQueue.fireLeaveChatEvent(room,leavingUser);
				return;
			}
		}
	}
	public void endChat(ActionEvent event) {
		Long roomId = (Long)event.getComponent().getAttributes().get("roomId");
		if(roomId!=null){
			for(ChatRoom room:sessionChatAction.getChats()){
				if(room.getRoomId().equals(roomId)){
					ChatEventQueue.fireEndChatEvent(room);
					return;
				}
			}
		}
	}
	
	// getters and setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
