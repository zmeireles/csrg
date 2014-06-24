package com.obc.csrg.bean;

import java.util.List;
import java.util.ArrayList;

import org.jboss.seam.annotations.Name;

import com.obc.csrg.constants.GeographicAreaTypeEnum;
import com.obc.csrg.model.Config;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.GeographicArea;

@Name("configHome")
public class ConfigHome<E extends Model> extends ObcEntityHome<Config> {


	private List<GeographicArea> countryList = new ArrayList<GeographicArea>();
  
	//business logic
	@Override
    public Object getId() {
		try{
			Config result = (Config)(entityManager.createQuery("select c from Config c")
					.setHint("org.hibernate.cacheable", true)
					.getSingleResult());
			return result.getId();
		}catch(Exception ex){
			stateBean.log4Debug(this.toString(), "[getId] #0", ex.toString());
			return super.getId();
		}
    }
		
	@Override
	protected void loadAfterCreate(){
		super.loadAfterCreate();
		this.initCountryList();
	}
	
	private void initCountryList(){
		countryList.clear();
		List<GeographicArea> countries = entityManager.createQuery(
				"select m from GeographicArea m where m.level=? order by m.name")
				.setParameter(1, GeographicAreaTypeEnum.COUNTRY)
				.getResultList();
		if(countries.size()>0){
			for(GeographicArea ga:countries){
				countryList.add(ga);
			}
		}
	}
	//getters and setters
	public List<GeographicArea> getCountryList() {
		return countryList;
	}

	public void setCountryList(List<GeographicArea> countryList) {
		this.countryList = countryList;
	}
}
