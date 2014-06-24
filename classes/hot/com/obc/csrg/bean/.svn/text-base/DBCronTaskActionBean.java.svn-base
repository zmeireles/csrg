package com.obc.csrg.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Calendar;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.async.Expiration;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;

import com.obc.csrg.local.DBCronTaskActionLocal;
import com.obc.csrg.model.DBCronTask;
import com.obc.csrg.model.TaskArg;
import com.obc.csrg.local.SendNotificationLocal;
import com.obc.csrg.local.CreateKeywordsActionLocal;

@Stateless
@Name("dbCronTaskAction")
public class DBCronTaskActionBean implements DBCronTaskActionLocal,Serializable {

	@Logger 
	private Log log;
	
	@In 
	private EntityManager entityManager;
	
	@In(create=true)
	private SendNotificationLocal sendNotificationAction;
	
	@In(create=true)
	private CreateKeywordsActionLocal createKeywordsAction;
	
	public void executeTask(@Expiration final Date when, final Long dbCronTaskId){
		log.info("[executeTask] when: #0, dbCronTaskId: #1", when, dbCronTaskId);
		boolean execSuccess=false;
		DBCronTask dbCronTask = this.getDBCronTaskFromEntityManager(dbCronTaskId);
		if(dbCronTask!=null){
			//carregar argumentos
			HashMap<Integer,String> args = new HashMap<Integer,String>();
			Iterator<TaskArg> argsIt = dbCronTask.getArgs().iterator();
			TaskArg aux;
			while(argsIt.hasNext()){
				aux=argsIt.next();
				args.put(aux.getIndexOrder(), aux.getArg());
			}
			//executar tarefa
			switch(dbCronTask.getTask()){
			case ADD_NOTIFICATION:
				execSuccess = sendNotificationAction.sendNotification(Long.valueOf(args.get(0)));
				break;
			case CREATE_KEYWORDS:
				execSuccess = createKeywordsAction.createKeywords4GeoAreas(Integer.valueOf(args.get(0)), Integer.valueOf(args.get(1)));
				break;
			//add new tasks here...
			
			default:
			}
			//re-activar a tarefa ou finalizar
			Events.instance().raiseAsynchronousEvent("managePostExecTask",dbCronTaskId,execSuccess);
		}else{
			log.error("[executeTask] ###############################################");
			log.error("[executeTask] dbCronTaskId: #0 (not found in the database)", dbCronTaskId);
			log.error("[executeTask] ###############################################");
		}
		//log.debug("[executeTask] when: #0, dbCronTaskId: #1, execSuccess:#2", when, dbCronTaskId, execSuccess);
	}
	
	
	public DBCronTask getDBCronTaskFromEntityManager(final Long dbCronTaskId){
		DBCronTask dbCronTask = null;
		log.info("[getDBCronTaskFromEntityManager] dbCronTaskId:#0",dbCronTaskId);
		if(dbCronTaskId!=null){
			List<DBCronTask> dbCronTaskList = null;
			int atemptsToGetTask = 5;
			long pauseBetweenAtempts = 3000L;
			//get dbCronTask from DB
			//this.sleepAtLeast(1000);//first wait 1 second
			dbCronTaskList = entityManager.createQuery("select c from DBCronTask c where c.id=?")
							.setParameter(1, dbCronTaskId)
							.getResultList();
			while(!(dbCronTaskList!=null && dbCronTaskList.size()>0) && atemptsToGetTask>0){
				log.info("[getDBCronTaskFromEntityManager] didn't get dbCronTask_#0 from DB. Retry again... in #1 ms", dbCronTaskId, pauseBetweenAtempts);
				this.sleepAtLeast(pauseBetweenAtempts);
				dbCronTaskList = entityManager.createQuery("select c from DBCronTask c where c.id=?")
								.setParameter(1, dbCronTaskId)
								.getResultList();
				atemptsToGetTask--;
			}
			if(dbCronTaskList!=null && dbCronTaskList.size()>0){
				//check to see if dates match
				Calendar now = Calendar.getInstance();
				
				dbCronTask = dbCronTaskList.get(0);
				if(now.before(dbCronTask.getBeginDate())){
					log.info("[getDBCronTaskFromEntityManager] task date changed, in Quartz:#0, in DB:#1",
							now,dbCronTask.getBeginDate());
					dbCronTask = null;
				}
			}
		}
		return dbCronTask;
	}
	
	public void sleepAtLeast(long millis) {
		long t0 = System.currentTimeMillis();
		long millisLeft = millis;
		while (millisLeft > 0) {
			try{
				Thread.sleep(millisLeft);
			}catch(InterruptedException ie){}
			long t1 = System.currentTimeMillis();
			millisLeft = millis - (t1 - t0);
		}
	}
	
	
}
