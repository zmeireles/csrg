package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.constants.DBCronTaskEnum;

@Entity
@Name("dbCronTask")
@Scope(SESSION)
@Table(name = "db_cron_task")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class DBCronTask extends Model implements Serializable {

	private Long id = 0L;
	private Date beginDate; // begining date of the task
	private Date endDate; // expiration date
	private int periodType; // calender.field
	private int period; // case there are repetitions, defines the number of
						// periods of the periodType
	private int repetitions; // number of repetitions to execute the task
	private DBCronTaskEnum task; // task to be executed
	private Set<TaskArg> args = new HashSet<TaskArg>(0);
	
	@Column(name = "entity_id")
	private Long entityId;

	public DBCronTask() {
		super();
	}

	public DBCronTask(Date beginDate, Date endDate, int periodType, int period,
			int repetitions, DBCronTaskEnum task, String... args) {
		super();
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.periodType = periodType;
		this.period = period;
		this.repetitions = repetitions;
		this.task = task;
		this.addArgs(args);
	}
	public DBCronTask(Date beginDate, Date endDate, int periodType, int period,
			int repetitions, DBCronTaskEnum task, Long entityId, String... args) {
		super();
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.periodType = periodType;
		this.period = period;
		this.repetitions = repetitions;
		this.task = task;
		this.entityId = entityId;
		this.addArgs(args);
	}

	/*
	 * Business functions
	 */
	
	public static DBCronTask getInstance(Date executionDate,
			DBCronTaskEnum task, String... args) {
		return new DBCronTask(executionDate, null, 0, 0, 0, task, args);
	}

	public static DBCronTask getInstance(DBCronTaskEnum task, String... args) {
		return new DBCronTask(new Date(), null, 0, 0, 0, task, args);
	}
	
	public static DBCronTask getInstance(Date executionDate,
			DBCronTaskEnum task,Long entityId, String... args) {
		return new DBCronTask(executionDate, null, 0, 0, 0, task, entityId, args);
	}

	public static DBCronTask getInstance(DBCronTaskEnum task, Long entityId, String... args) {
		return new DBCronTask(new Date(), null, 0, 0, 0, task, entityId, args);
	}
	
	private void addArgs(String... args) {
		for (int i = 0; i < args.length; i++) {
			this.getArgs().add(new TaskArg(i, args[i], this));
		}
	}

	@Override
	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "db_cron_task_id", nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "begin_date")
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	@Column(name = "endDate")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "period_type")
	public int getPeriodType() {
		return periodType;
	}

	public void setPeriodType(int periodType) {
		this.periodType = periodType;
	}

	@Column(name = "period")
	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	@Column(name = "repetitions")
	public int getRepetitions() {
		return repetitions;
	}

	public void setRepetitions(int repetitions) {
		this.repetitions = repetitions;
	}

	@Column(name = "task")
	public DBCronTaskEnum getTask() {
		return task;
	}

	public void setTask(DBCronTaskEnum task) {
		this.task = task;
	}

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH, CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "dbCronTask")
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	public Set<TaskArg> getArgs() {
		return args;
	}

	public void setArgs(Set<TaskArg> args) {
		this.args = args;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	
}
