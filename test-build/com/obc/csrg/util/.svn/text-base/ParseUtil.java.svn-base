package com.obc.csrg.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import javax.persistence.EntityManager;

import org.jboss.seam.log.Log;


import com.obc.csrg.constants.Constants;
import com.obc.csrg.constants.GeographicAreaTypeEnum;
import com.obc.csrg.model.GeographicArea;
import com.obc.csrg.model.SearchObject;
import com.obc.csrg.search.KeywordsFactory;

public class ParseUtil {

	/**
     * Parser for geographic areas
     * FILE <-> Constants.BQECOM_HOME+"import/Tabela_Area_Geografica.txt"
     * FILE <-> Constants.BQECOM_HOME+"import/Tabela_Area_Geografica_Pais.txt"
     * FILE <-> Constants.BQECOM_HOME+"import/Tabela_Area_Geografica_Regioes.txt"
     * FILE <-> Constants.BQECOM_HOME+"import/Tabela_Area_Geografica_Distritos.txt"
     * FILE <-> Constants.BQECOM_HOME+"import/Tabela_Area_Geografica_Concelhos.txt"
     */
    public static void initGeographicArea(final Log log, final EntityManager entityManager,Locale locale){
    	Long result = (Long)(entityManager.createQuery("select count(ag) from GeographicArea ag")
				.setHint("org.hibernate.cacheable", true)
				.getSingleResult());
    	if(result.equals(0L)){
    		log.debug("[initGeographicArea] init");
			File fileTodo = new File(Constants.CSRG_HOME+Constants.CSRG_DIR_IMPORT+"Tabela_Area_Geografica.txt");
			File filePais = new File(Constants.CSRG_HOME+Constants.CSRG_DIR_IMPORT+"Tabela_Area_Geografica_Pais.txt");
			File fileRegiao = new File(Constants.CSRG_HOME+Constants.CSRG_DIR_IMPORT+"Tabela_Area_Geografica_Regioes.txt");
    		File fileDistrito = new File(Constants.CSRG_HOME+Constants.CSRG_DIR_IMPORT+"Tabela_Area_Geografica_Distritos.txt");
    		File fileConcelho = new File(Constants.CSRG_HOME+Constants.CSRG_DIR_IMPORT+"Tabela_Area_Geografica_Concelhos.txt");
			if(!fileTodo.canRead()) log.info("[initGeographicArea] Can't Read File: #0", fileTodo.getAbsolutePath());
			else if(!filePais.canRead())log.info("[initGeographicArea] Can't Read File: #0", filePais.getAbsolutePath());
			else if(!fileRegiao.canRead())log.info("[initGeographicArea] Can't Read File: #0", fileRegiao.getAbsolutePath());
			else if(!fileDistrito.canRead())log.info("[initGeographicArea] Can't Read File: #0", fileDistrito.getAbsolutePath());
			else if(!fileConcelho.canRead())log.info("[initGeographicArea] Can't Read File: #0", fileConcelho.getAbsolutePath());
			else{
				GeographicArea aux;
				String line;
				String[] lineSplit;
				//init mundo
				//log.trace("[initGeographicArea] mundo init");
				GeographicArea mundo = new GeographicArea();
				int count=1;
				int flushCounter=0;
				mundo.setLevel(GeographicAreaTypeEnum.WORLD);
				mundo.setName("Terra");
				entityManager.persist(mundo);
				
				SearchObject obj = null;
				boolean conditionsChecked=false;
				log.info("[initGeographicArea] update search values #0, for:#1",count++, mundo);
				KeywordsFactory kFactory = new KeywordsFactory(mundo,locale,entityManager,log);
				String modelName = mundo.getClass().getSimpleName().split("[$]")[0];
				//obj = kFactory.updateKeywords();
				if(modelName.equals(mundo.getClass().getSimpleName()))
					conditionsChecked = true;
				//init pais
				GeographicArea pais = null;
				//log.trace("[initGeographicArea] pais init");
				try{
					BufferedReader input =  new BufferedReader(new FileReader(filePais));
					try{
						while (( line = input.readLine()) != null){
							line = line.trim();
							//log.trace("[initGeographicArea] line: #0", line);
							if(line.matches("^\\d+;.+;\\d+.*$")){
								lineSplit = line.split(";");
								aux = new GeographicArea();
								aux.setLevel(GeographicAreaTypeEnum.COUNTRY);
								aux.setName(lineSplit[1].trim());
								aux.setParent(mundo);
								aux.setCountry(true);
								if(aux.getName().equalsIgnoreCase("PORTUGAL")){
									aux.setShortName("PT");
									aux.setCountryPhonePrefix("351");
									pais = aux;
								}
								entityManager.persist(aux);
								log.info("[initGeographicArea] update search values #0, for:#1",count++, aux);
								kFactory.setInstance(aux);
								//kFactory = new KeywordsFactory(aux,locale,entityManager,log);
								/*
								if(!conditionsChecked){
									obj = kFactory.updateKeywords();
									modelName = aux.getClass().getSimpleName().split("[$]")[0];
									if(modelName.equals(aux.getClass().getSimpleName()))
										conditionsChecked = true;
								}else
									kFactory.createValueKeywords(obj);
								 */
							}
						}
					}finally {
						input.close();
					}
				}catch (IOException ex){
					ex.printStackTrace();
			    }
				//init regiao
				HashMap<String,GeographicArea> regiaoHash = new HashMap<String,GeographicArea>();
				//log.trace("[initGeographicArea] regiaoHash init");
				try{
					BufferedReader input =  new BufferedReader(new FileReader(fileRegiao));
					try{
						while (( line = input.readLine()) != null){
							line = line.trim();
							//log.trace("[initGeographicArea] line: #0", line);
							if(line.matches("^\\d+;.+;\\w+$")){
								lineSplit = line.split(";");
								aux = new GeographicArea();
								aux.setLevel(GeographicAreaTypeEnum.REGION);
								aux.setName(lineSplit[1].trim());
								aux.setParent(pais);
								aux.setArchipelago(lineSplit[2].trim().equalsIgnoreCase("true"));
								entityManager.persist(aux);
								log.info("[initGeographicArea] update search values #0, for:#1",count++, aux);
								//kFactory = new KeywordsFactory(aux,locale,entityManager,log);
								kFactory.setInstance(aux);
								//kFactory.createValueKeywords(obj);
								regiaoHash.put(lineSplit[0].trim(), aux);
							}
						}
					}finally {
						input.close();
					}
				}catch (IOException ex){
					ex.printStackTrace();
			    }
				//init distrito
				HashMap<String,GeographicArea> distritoHash = new HashMap<String,GeographicArea>();
				//log.trace("[initGeographicArea] distritoHash init");
				try{
					BufferedReader input =  new BufferedReader(new FileReader(fileDistrito));
					try{
						while (( line = input.readLine()) != null){
							line = line.trim();
							//log.trace("[initGeographicArea] line: #0", line);
							if(line.matches("^\\d+;\\d+;.+$")){
								lineSplit = line.split(";");
								aux = new GeographicArea();
								aux.setLevel(GeographicAreaTypeEnum.ISLAND_DISTRICT);
								aux.setName(lineSplit[2].trim());
								aux.setParent(regiaoHash.get(lineSplit[0].trim()));
								entityManager.persist(aux);
								log.info("[initGeographicArea] update search values #0, for:#1",count++, aux);
								//kFactory = new KeywordsFactory(aux,locale,entityManager,log);
								kFactory.setInstance(aux);
								//kFactory.createValueKeywords(obj);
								distritoHash.put(lineSplit[1].trim(), aux);
							}
						}
					}finally {
						input.close();
					}
				}catch (IOException ex){
					ex.printStackTrace();
			    }
				//init concelho
				HashMap<String,GeographicArea> concelhoHash = new HashMap<String,GeographicArea>();
				//log.trace("[initGeographicArea] concelhoHash init");
				try{
					BufferedReader input =  new BufferedReader(new FileReader(fileConcelho));
					try{
						while (( line = input.readLine()) != null){
							line = line.trim();
							//log.trace("[initGeographicArea] line: #0", line);
							if(line.matches("^\\d+;\\d+;.+$")){
								lineSplit = line.split(";");
								aux = new GeographicArea();
								aux.setLevel(GeographicAreaTypeEnum.COUNTY);
								aux.setName(lineSplit[2].trim());
								aux.setParent(distritoHash.get(lineSplit[0].trim()));
								entityManager.persist(aux);
								log.info("[initGeographicArea] update search values #0, for:#1",count++, aux);
								//kFactory = new KeywordsFactory(aux,locale,entityManager,log);
								kFactory.setInstance(aux);
								//kFactory.createValueKeywords(obj);
								concelhoHash.put(lineSplit[0].trim()+";"+lineSplit[1].trim(), aux);
							}
						}
					}finally {
						input.close();
					}
				}catch (IOException ex){
					ex.printStackTrace();
			    }
				entityManager.flush();
				//init freguesia
				HashMap<String,GeographicArea> freguesiaHash = new HashMap<String,GeographicArea>();
				//log.trace("[initGeographicArea] freguesiaHash init");
				try{
					BufferedReader input =  new BufferedReader(new FileReader(fileTodo));
					try{
						while (( line = input.readLine()) != null){
							line = line.trim();
							//log.trace("[initGeographicArea] line: #0", line);
							if(line.matches("^\\d+;\\d+;\\d+;.+;.+$")){
								lineSplit = line.split(";");
								if(freguesiaHash.get(lineSplit[0].trim()+";"+lineSplit[1].trim()+";"+lineSplit[2].trim())==null){
									aux = new GeographicArea();
									aux.setLevel(GeographicAreaTypeEnum.FREGUESIA);
									aux.setName(lineSplit[3].trim());
									aux.setParent(concelhoHash.get(lineSplit[0].trim()+";"+lineSplit[1].trim()));
									entityManager.persist(aux);
									log.info("[initGeographicArea] update search values #0, for:#1",count++, aux);
									//kFactory = new KeywordsFactory(aux,locale,entityManager,log);
									kFactory.setInstance(aux);
									//kFactory.createValueKeywords(obj);
									freguesiaHash.put(lineSplit[0].trim()+";"+lineSplit[1].trim()+";"+lineSplit[2].trim(), aux);
									/*
									if(flushCounter++>=1000){
										entityManager.flush();
										flushCounter=0;
									}*/
										
								}
							}
						}
					}finally {
						input.close();
					}
				}catch (IOException ex){
					ex.printStackTrace();
			    }
				entityManager.flush();
				//criar index de parents
				log.debug("[initGeographicArea] index init");
				boolean erroLoop = false;
				Long countIndexGeographicAreaParents = 0L;
				HashMap<Long,GeographicArea> testeLoopHash = new HashMap<Long,GeographicArea>();
		    	List<GeographicArea> areaGeograficaToIndex = entityManager.createQuery("select ag from GeographicArea ag")
								    					.setHint("org.hibernate.cacheable", true)
								    					.getResultList();
		    	for(GeographicArea agToIndex : areaGeograficaToIndex){
		    		//log.trace("[initGeographicArea] agToIndex: #0", agToIndex);
		    		testeLoopHash.clear();
		    		aux=agToIndex.getParent();
		    		while(aux!=null){
		    			if(testeLoopHash.get(aux.getId())==null){
		    				//log.trace("[initGeographicArea] agToIndex: #0, parent: #1", agToIndex, aux);
				    		entityManager.persist(agToIndex.newInstanceOfParentModel(aux));
			    			testeLoopHash.put(aux.getId(), aux);
			    			countIndexGeographicAreaParents++;
			    			aux=aux.getParent();
		    			}else{
		    				log.error("[initGeographicArea] agToIndex: #0, parent: #1, (database tree with loop)", agToIndex, aux);
		    				aux=null;
		    				erroLoop=true;
		    			}
		    		}
		    	}
		    	entityManager.flush();
				//fim
				log.debug("[initGeographicArea] distritoHash: #0", distritoHash.size());
				log.debug("[initGeographicArea] concelhoHash: #0", concelhoHash.size());
				log.debug("[initGeographicArea] freguesiaHash: #0", freguesiaHash.size());
				log.debug("[initGeographicArea] countIndexGeographicAreaParents: #0", countIndexGeographicAreaParents);
				if(!erroLoop) log.debug("[initGeographicArea] criado");
				else log.error("[initGeographicArea] arvore da base-dados com loop (index parents ficou imcompleto)");
			}
    	}else{
    		log.debug("[initGeographicArea] result: #0", result);
    	}
    }
}
