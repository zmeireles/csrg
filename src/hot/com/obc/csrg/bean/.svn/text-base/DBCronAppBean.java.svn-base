package com.obc.csrg.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.log.Log;

import com.obc.csrg.local.DBCronAppLocal;
import com.obc.csrg.local.DBCronTaskActionLocal;
import com.obc.csrg.model.DBCronTask;

@AutoCreate
@Stateless
@Name("dbCronApp")
public class DBCronAppBean implements DBCronAppLocal,Serializable {

	@Logger 
	private Log log;
	
	@In 
    private EntityManager entityManager;
	
	@In(create=true)
	private DBCronTaskActionLocal dbCronTaskAction;
	
	public void init() {
		log.info("#0 [init]", this.toString().substring(this.toString().lastIndexOf(".")));
		List<DBCronTask> results = entityManager.createQuery("select c from DBCronTask c")
								.getResultList();
		for(DBCronTask c : results){
			activateTask(c);
		}
	}
	
	/*
	 * activates asynchronous event to execute task
	 */
	private void activateTask(final DBCronTask dbCronTask){
		log.info("[activateTask] when: #0, dbCronTaskId: #1, dbCronTaskValidUntil: #2", 
				dbCronTask.getBeginDate(), dbCronTask.getId(), dbCronTask.getEndDate());
		if(dbCronTask.getEndDate()==null || dbCronTask.getBeginDate().compareTo(dbCronTask.getEndDate())<=0){
			dbCronTaskAction.executeTask(dbCronTask.getBeginDate(), dbCronTask.getId());
			log.info("[activateTask] when: #0, dbCronTaskId: #1, task: #2 (activated)", 
					dbCronTask.getBeginDate(), dbCronTask.getId(), dbCronTask.getTask().name());
		}else{
			removeTask(dbCronTask);
		}
	}
	
	
	/*
	 * creates task in the database. Also activates the created task
	 */
	public void createTask(final DBCronTask dbCronTask){
		//see if there is already a task for the entity id
		if(dbCronTask.getId()==null || dbCronTask.getId().equals(0L)){
			log.info("[createTask] persist dbcronId:#0", dbCronTask.getId());
			entityManager.persist(dbCronTask);
			log.info("[createTask] persist dbcronId:#0", dbCronTask.getId());
		}else{
			log.info("[createTask] update dbcronId:#0", dbCronTask.getId());
			entityManager.merge(dbCronTask);
			log.info("[createTask] update dbcronId:#0", dbCronTask.getId());
		}
		entityManager.flush();
		
		log.info("[createTask] dbCronTaskId: #0 (created)", dbCronTask.getId());
		this.activateTask(dbCronTask);
	}
	
	
	/*
	 * remove task from DB
	 */
	private void removeTask(final DBCronTask dbCronTask){
		Long dbCronTaskId = dbCronTask.getId();
		entityManager.remove(dbCronTask);
		entityManager.flush();
		log.debug("[removeTarefa] dbCronTaskId: #0 (removido)", dbCronTaskId);
	}
	
	/*
	 * gere proxima execucao ou finaliza a tarefa
	 */
	@Observer("managePostExecTask")
    public void managePostExecTask(final Long dbCronTaskId, final boolean execResult){
		log.info("[managePostExecTask] dbCronTaskId: #0, execResult: #1", dbCronTaskId, execResult);
		if(dbCronTaskId!=null){
			//get dbCronTask from DB
			List<DBCronTask> taskList = entityManager.createQuery("select c from DBCronTask c where c.id=?")
									.setParameter(1, dbCronTaskId)
									.getResultList();
			DBCronTask dbCronTask;
			if(taskList.size()>0)
				dbCronTask = taskList.get(0);
			else
				return;
			if(dbCronTask.getRepetitions()>1 || dbCronTask.getRepetitions()==-1){
				Calendar c = Calendar.getInstance();
				c.setTime(dbCronTask.getBeginDate());
	       	 	c.add(dbCronTask.getPeriodType(), dbCronTask.getPeriod());
	       	 	dbCronTask.setBeginDate(c.getTime());
	       	 	if(dbCronTask.getRepetitions()!=-1) dbCronTask.setRepetitions(dbCronTask.getRepetitions()-1);
	       	 	createTask(dbCronTask);
			}else{
				removeTask(dbCronTask);
			}
		}
	}
	
}
