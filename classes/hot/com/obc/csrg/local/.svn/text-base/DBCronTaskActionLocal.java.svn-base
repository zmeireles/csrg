package com.obc.csrg.local;

import java.util.Date;

import javax.ejb.Local;

import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.async.Expiration;

@Local
public interface DBCronTaskActionLocal {

	@Asynchronous
	public void executeTask(@Expiration final Date when, final Long dbCronTaskId);
}
