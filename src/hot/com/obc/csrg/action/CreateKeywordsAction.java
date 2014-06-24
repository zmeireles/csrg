package com.obc.csrg.action;

import java.io.Serializable;
import java.util.Map;
import java.util.Locale;
import java.util.List;
import java.util.Calendar;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.obc.csrg.local.CreateKeywordsActionLocal;
import com.obc.csrg.local.DBCronAppLocal;
import com.obc.csrg.search.KeywordsFactory;
import com.obc.csrg.model.*;
import com.obc.csrg.constants.SearchObjectTypeEnum;
import com.obc.csrg.constants.ModelEnum;
import com.obc.csrg.constants.DBCronTaskEnum;

@Stateless
@Name("createKeywordsAction")
public class CreateKeywordsAction implements CreateKeywordsActionLocal,Serializable{

	@Logger 
	private Log log;
	
	@In 
	private EntityManager entityManager;
	
	@In
	private Locale locale;
	
	@In 
	private Map<String,String> messages;
	
	@In
	DBCronAppLocal dbCronApp;
	
	public void createModelKeywords(Model model){
		//log.info("[createModelKeywords] model:#0", model);
		KeywordsFactory kFactory = new KeywordsFactory(model,locale,entityManager,log);
		kFactory.createKeywords();
		//log.info("[createModelKeywords] keywords created");
	}
	
	public void updateModelKeywords(Model model){
		//log.info("[updateModelKeywords] model:#0", model);
		KeywordsFactory kFactory = new KeywordsFactory(model,locale,entityManager,log);
		kFactory.updateKeywords();
		//log.info("[updateModelKeywords] keywords updated");
	}
	
	public void removeModelKeywords(Model model){
		//log.info("[removeModelKeywords] model:#0", model);
		KeywordsFactory kFactory = new KeywordsFactory(model,locale,entityManager,log);
		kFactory.removeKeywords();
		//log.info("[removeModelKeywords] keywords removed");
	}
	
	/**
	 * iterates through all SearchObject MODEL instance and checks for missing and deleted properties
	 * create missing properties and delete non existing ones (including search values) 
	 */
	public void updateModelKeywords(){
		
	}
	
	/**
	 * Method to be called from dbCrontTaksActionBean that generate keywords for GeographicArea model
	 * After execution another task is placed in the DBCron, increasing iteration or re adjusting 
	 * records parameter 
	 * @param firstRecord - the iteration number, it determines the first record to be read
	 * @param records - the number of records read in each iteration
	 * @return
	 */
	public boolean createKeywords4GeoAreas(int firstRecord,int records){
		Long recordCount = (Long) entityManager.createQuery("select count(m) from GeographicArea m")
								.getSingleResult();
		log.info("[createKeywords4GeoAreas] recordCount:#0, firstRecord:#1, records:#2", recordCount,firstRecord,records);
		List<GeographicArea> geoAreas= entityManager.createQuery("select m from GeographicArea m")
						.setFirstResult(firstRecord)
						.setMaxResults(records)
						.getResultList();
		int x=0;
		int flushCount=0;
		int count = geoAreas.size();
		boolean conditionsChecked=false;
		SearchObject obj = null;
		boolean failed = false;
		Long beginTime = System.currentTimeMillis();
		for(GeographicArea m:geoAreas){
			KeywordsFactory kFactory = new KeywordsFactory(m,locale,entityManager,log);
			//log.info("[createKeywords4GeoAreas] #0, of:#1, #2:#3", x++,count,m.getClass().getSimpleName(), m);
			String modelName = m.getClass().getSimpleName().split("[$]")[0];
			if(!conditionsChecked){
				obj = kFactory.updateKeywords();
				if(modelName.equals(m.getClass().getSimpleName()))
					conditionsChecked = true;
				if(obj==null){
					failed=true;
					break;
				}
			}else
				kFactory.updateValueKeywords(obj);
			x++;
			log.info("[createKeywords4GeoAreas] #0, of:#1, #2:#3", x,count,m.getClass().getSimpleName(), m);
			Long secondsElapsed = this.secondsElapsed(beginTime, System.currentTimeMillis());
			if(secondsElapsed>240)
				break;
			if(this.calculateAverageSeconds(secondsElapsed, x)+secondsElapsed>240){
				log.info("[createKeywords4GeoAreas] next record will fail");
				break;
			}
			flushCount++;
			if(flushCount==100){
				entityManager.flush();
				flushCount=0;
			}
		}
		entityManager.flush();
		Long endTime = System.currentTimeMillis();
		log.info("[createKeywords4GeoAreas] seconds elapsed:#0", (endTime-beginTime)/(1000));
		if(!failed){
			if(records>x){
				log.info("[createKeywords4GeoAreas] forced break in record:#0", x);
				records=x;
			}
			Long secondsElapsed = (endTime-beginTime)/1000;
			float fSecs = secondsElapsed;
			float average = fSecs/records;
			log.info("[createKeywords4GeArea] average:#0", average);
			if(firstRecord+records<recordCount){//need more tasks
				//re adjust records if needed depending on time elapsed
				int newRecordsValue = this.calculateNextRecordsValue(beginTime, endTime, records, 240);
				log.info("[createKeywords4GeoAreas] calculated records, next read will be:#0 records]", newRecordsValue);
				if(newRecordsValue<=0)
					newRecordsValue = 1;
				this.sendToDBCron(firstRecord+records, newRecordsValue, 1, ModelEnum.GEOGRAPHIC_AREAS);
			}
		}else{//for some reason it failed, going to do iterations with less records
			records/=2;
			if(records>=10)//minimum number of records
				this.sendToDBCron(firstRecord, records, 1, ModelEnum.GEOGRAPHIC_AREAS);
		}
		return true;
	}
	private float calculateAverageSeconds(Long secondsElapsed, int records){
		float fSecs = secondsElapsed;
		float average = fSecs/records;
		return average;
	}
	
	private int calculateNextRecordsValue(Long begin, Long end, int records,int limitInSeconds){
		int newRecordsValue = records;
		Long secondsElapsed = (end-begin)/1000;
		float fSecs = secondsElapsed;
		float average = fSecs/records;
		int calculatedRecords = Math.round(limitInSeconds/average);
		if(average*calculatedRecords>240)
			calculatedRecords--;
		newRecordsValue = calculatedRecords;
		
		if(newRecordsValue<=0)
			newRecordsValue = 1;
		
		return newRecordsValue;
	}
	private Long secondsElapsed(Long begin,Long end){
		return (end-begin)/1000;
	}
	private void sendToDBCron(int firstRecord,int records,int minutesOffset,ModelEnum model){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, minutesOffset);
		dbCronApp.createTask(DBCronTask.getInstance(cal.getTime(), DBCronTaskEnum.CREATE_KEYWORDS, (long)model.ordinal(), 
				String.valueOf(firstRecord),String.valueOf(records)));
	}
	private DBCronTask getExistingTask(){
		List<DBCronTask> tasks = entityManager.createQuery("select m from DBCronTask m where " +
				"m.task=? and m.entityId is not null and m.entityId=?")
				.setParameter(1, DBCronTaskEnum.CREATE_KEYWORDS)
				.setParameter(2, (long)ModelEnum.GEOGRAPHIC_AREAS.ordinal())
				.getResultList();
		return null;
	}
	
	/**
	 * Create keywords for all model objects
	 */
	public void createAllModelsKeywords(ModelEnum model){
		//users
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.USERS){
			List<User> users = entityManager.createQuery("select m from User m").getResultList();
			for(User m:users){
				KeywordsFactory kFactory = new KeywordsFactory(m,locale,entityManager,log);
				kFactory.updateKeywords();
			}
		}
		
		//departments
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.DEPARTMENTS){
			List<Department> departments = entityManager.createQuery("select m from Department m").getResultList();
			for(Department m:departments){
				KeywordsFactory kFactory = new KeywordsFactory(m,locale,entityManager,log);
				kFactory.updateKeywords();
			}
		}
		//geographic areas
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.GEOGRAPHIC_AREAS){
			this.sendToDBCron(0, 100, 0, ModelEnum.GEOGRAPHIC_AREAS);
		}
		//news
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.NEWS){
			List<News> news = entityManager.createQuery("select m from News m").getResultList();
			for(News m:news){
				KeywordsFactory kFactory = new KeywordsFactory(m,locale,entityManager,log);
				kFactory.updateKeywords();
			}
		}
		//notifications
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.NOTIFICATIONS){
			List<Notification> notifications = entityManager.createQuery("select m from Notification m").getResultList();
			for(Notification m:notifications){
				KeywordsFactory kFactory = new KeywordsFactory(m,locale,entityManager,log);
				kFactory.updateKeywords();
			}
		}
		//people
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.PERSONS){
			List<Person> people = entityManager.createQuery("select m from Person m").getResultList();
			for(Person m:people){
				KeywordsFactory kFactory = new KeywordsFactory(m,locale,entityManager,log);
				kFactory.updateKeywords();
			}
		}
		//professional categories
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.PROF_CATEGORIES){
			List<ProfCategory> profCategories = entityManager.createQuery("select m from ProfCategory m").getResultList();
			for(ProfCategory m:profCategories){
				KeywordsFactory kFactory = new KeywordsFactory(m,locale,entityManager,log);
				kFactory.updateKeywords();
			}
		}
		//service areas
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.SERVICE_AREAS){
			List<ServiceArea> serviceAreas = entityManager.createQuery("select m from ServiceArea m").getResultList();
			for(ServiceArea m:serviceAreas){
				KeywordsFactory kFactory = new KeywordsFactory(m,locale,entityManager,log);
				kFactory.updateKeywords();
			}
		}
		//service classifications
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.SERVICE_CLASSIFICATIONS){
			List<ServiceClassification> serviceClassifications = entityManager.createQuery("select m from ServiceClassification m").getResultList();
			for(ServiceClassification m:serviceClassifications){
				KeywordsFactory kFactory = new KeywordsFactory(m,locale,entityManager,log);
				kFactory.updateKeywords();
			}
		}
		//visual items
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.VISUAL_ITEMS){
			List<VisualItem> visualItems = entityManager.createQuery("select m from VisualItem m").getResultList();
			for(VisualItem m:visualItems){
				KeywordsFactory kFactory = new KeywordsFactory(m,locale,entityManager,log);
				kFactory.updateKeywords();
			}
		}
		//web pages
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.WEB_PAGES){
			List<Webpage> webpages = entityManager.createQuery("select m from Webpage m").getResultList();
			for(Webpage m:webpages){
				KeywordsFactory kFactory = new KeywordsFactory(m,locale,entityManager,log);
				kFactory.updateKeywords();
			}
		}
		//Data change model
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.DATA_CHANGE_MODELS){
			List<DataChangeModel> dataChangeModels = entityManager.createQuery("select m from DataChangeModel m").getResultList();
			for(DataChangeModel m:dataChangeModels){
				KeywordsFactory kFactory = new KeywordsFactory(m,locale,entityManager,log);
				kFactory.updateKeywords();
			}
		}
		
		//composed notifications
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.COMPOSED_NOTIFICATIONS){
			List<ComposedNotification> composedNotifications = entityManager.createQuery("select m from ComposedNotification m").getResultList();
			for(ComposedNotification m:composedNotifications){
				KeywordsFactory kFactory = new KeywordsFactory(m,locale,entityManager,log);
				kFactory.updateKeywords();
			}
		}
		/*
		//profiles
		List<Profile> profiles = entityManager.createQuery("select m from Profile m").getResultList();
		for(Profile m:profiles){
			KeywordsFactory kFactory = new KeywordsFactory(m,locale,entityManager,log);
			kFactory.updateKeywords();
		}*/
	}
	/**
	 * update properties for all models
	 */
	public void updateProperties(ModelEnum model){
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.USERS)
			(new KeywordsFactory<User>(new User(),locale,entityManager,log)).updateProperties();
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.DEPARTMENTS)
			(new KeywordsFactory<Department>(new Department(),locale,entityManager,log)).updateProperties();
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.GEOGRAPHIC_AREAS)
			(new KeywordsFactory<GeographicArea>(new GeographicArea(),locale,entityManager,log)).updateProperties();
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.NEWS)
			(new KeywordsFactory<News>(new News(),locale,entityManager,log)).updateProperties();
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.NOTIFICATIONS)
			(new KeywordsFactory<Notification>(new Notification(),locale,entityManager,log)).updateProperties();
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.PERSONS)
			(new KeywordsFactory<Person>(new Person(),locale,entityManager,log)).updateProperties();
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.PROF_CATEGORIES)
			(new KeywordsFactory<ProfCategory>(new ProfCategory(),locale,entityManager,log)).updateProperties();
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.SERVICE_AREAS)
			(new KeywordsFactory<ServiceArea>(new ServiceArea(),locale,entityManager,log)).updateProperties();
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.SERVICE_CLASSIFICATIONS)
			(new KeywordsFactory<ServiceClassification>(new ServiceClassification(),locale,entityManager,log)).updateProperties();
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.VISUAL_ITEMS)
			(new KeywordsFactory<VisualItem>(new VisualItem(),locale,entityManager,log)).updateProperties();
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.WEB_PAGES)
			(new KeywordsFactory<Webpage>(new Webpage(),locale,entityManager,log)).updateProperties();
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.DATA_CHANGE_MODELS)
			(new KeywordsFactory<DataChangeModel>(new DataChangeModel(),locale,entityManager,log)).updateProperties();
		if(model == ModelEnum.ALL_MODELS || model == ModelEnum.COMPOSED_NOTIFICATIONS)
			(new KeywordsFactory<ComposedNotification>(new ComposedNotification(),locale,entityManager,log)).updateProperties();
	}
	/**
	 * remove all keywords related to models
	 */
	public void clearModelKeywords(ModelEnum model){
		List<SearchObject> objects = entityManager.createQuery("select m from SearchObject m " +
				"where m.objectType=?")
				.setParameter(1, SearchObjectTypeEnum.MODEL)
				.getResultList();
		for(SearchObject m:objects){
			m.remove(entityManager);
		}
	}
}
