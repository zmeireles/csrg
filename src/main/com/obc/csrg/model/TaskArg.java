package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.Length;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Entity
@Name("taskArg")
@Scope(SESSION)
@Table(name = "task_arg")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class TaskArg extends Model implements Serializable {

	private Long id=0L;
	private int indexOrder;
	private String arg;
	private DBCronTask dbCronTask;
	
	
	public TaskArg() {
		super();
	}
	public TaskArg(int indexOrder, String arg, DBCronTask dbCronTask) {
		super();
		this.indexOrder = indexOrder;
		this.arg = arg;
		this.dbCronTask = dbCronTask;
	}


	@Override
	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "task_arg_id", nullable = false)
	public Long getId() {
	     return id;
	}
	public void setId(Long id) {
	     this.id = id;
	}
	
	@Column(name = "index_order")
	public int getIndexOrder() {
		return indexOrder;
	}
	public void setIndexOrder(int indexOrder) {
		this.indexOrder = indexOrder;
	}
	
	@Length(max=2000)
	@Column(name = "arg", columnDefinition="text")//columnDefinition="varchar2(2000 char)")
	public String getArg() {
		return arg;
	}
	public void setArg(String arg) {
		this.arg = arg;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "db_cron_task_id")
	public DBCronTask getDbCronTask() {
		return dbCronTask;
	}
	public void setDbCronTask(DBCronTask dbCronTask) {
		this.dbCronTask = dbCronTask;
	}
	
}
