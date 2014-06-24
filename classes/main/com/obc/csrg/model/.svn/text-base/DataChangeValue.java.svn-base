package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.util.Date;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.constants.Constants;
import com.obc.csrg.events.ModelEventQueue;

@Entity
@Name("dataChangeValue")
@Scope(SESSION)
@Table(name = "data_change_value")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class DataChangeValue extends Model implements Serializable{

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "data_change_value_id", nullable = false)
	private Long id;

	@Column(name = "date")
	private Date date;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "data_change_property_id")
	private DataChangeProperty dataChangeProperty;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
	private User user; //user that did the change.
	
	@Column(name = "text_value", columnDefinition = "text")
	private String textValue="";
	
	@Column(name = "byte_array_value")
	private byte[] byteArrayValue;
	
	@Column(name = "char_array_value")
	private char[] charArrayValue;
	
	@Column(name = "regular_value")
	private String regularValue;
	
	@Column(name = "model_id_value")  //this is for the case when the change is for a new model 
									//(for example changing a person's professional category)
	private Long modelIdValue;
	
	@Column(name = "model_id")
	private Long modelId;//id of the changed model
	
	
	private String locale=Constants.DEFAULT_LOCALE;
	
	//constructors
	public DataChangeValue() {
	}

	//business logic

	@Override
	public boolean remove(EntityManager entityManager) {
		ModelEventQueue.fireOnBeforeModelRemoveEvent(this);
		
		if(this.user!=null){
			this.user.getChangedValues().remove(this);
			this.user = null;
		}
		
		entityManager.remove(this);
		ModelEventQueue.fireOnAfterModelRemoveEvent(this);
		return true;
	}
	public int hashCode() {
		return (this.id == null) ? 0 : this.id.hashCode();
	}

	public boolean equals(Object object) {
		if (object instanceof DataChangeValue) {
			final DataChangeValue obj = (DataChangeValue) object;
			return (this.id != null) ? this.id.equals(obj.id)
					: (obj.id == null);
		}
		return false;
	}
	
	//getters and setters
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public DataChangeProperty getDataChangeProperty() {
		return dataChangeProperty;
	}

	public void setDataChangeProperty(DataChangeProperty dataChangeProperty) {
		this.dataChangeProperty = dataChangeProperty;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getTextValue() {
		return textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

	public byte[] getByteArrayValue() {
		return byteArrayValue;
	}

	public void setByteArrayValue(byte[] byteArrayValue) {
		this.byteArrayValue = byteArrayValue;
	}

	public char[] getCharArrayValue() {
		return charArrayValue;
	}

	public void setCharArrayValue(char[] charArrayValue) {
		this.charArrayValue = charArrayValue;
	}

	public String getRegularValue() {
		return regularValue;
	}

	public void setRegularValue(String regularValue) {
		this.regularValue = regularValue;
	}
	
	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	
	public Long getModelIdValue() {
		return modelIdValue;
	}

	public void setModelIdValue(Long modelIdValue) {
		this.modelIdValue = modelIdValue;
	}
}
