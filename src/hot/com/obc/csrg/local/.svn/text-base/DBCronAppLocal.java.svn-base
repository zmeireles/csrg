package com.obc.csrg.local;

import javax.ejb.Local;
import org.jboss.seam.annotations.async.Asynchronous;

import com.obc.csrg.model.DBCronTask;

@Local
public interface DBCronAppLocal {
	public void init();
	public void managePostExecTask(final Long dbCronTaskId, final boolean execResult);
	@Asynchronous
	public void createTask(final DBCronTask dbCronTask);
}
