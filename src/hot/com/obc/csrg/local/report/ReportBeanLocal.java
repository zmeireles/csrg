package com.obc.csrg.local.report;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.faces.model.SelectItem;

import com.obc.csrg.model.Model;

@Local
public interface ReportBeanLocal<E extends Model> {
	public void create();
	public void destroy();
	public String query();
	public String reset();
	public String reset(String link);
	public String getDescriptionSearchTxt();
	public List<SelectItem> getThreeStatesList();
	public String getSearchTxt();
	public void setSearchTxt(String searchTxt);
	public List<E> getResultList();
	public String getResultsMessage();
	public Date getStartDate();
	public void setStartDate(Date startDate);
	public Date getStopDate();
	public void setStopDate(Date stopDate);
	public boolean isShowStartDate();
	public void setShowStartDate(boolean showStartDate);
	public boolean isShowStopDate();
	public void setShowStopDate(boolean showStopDate);
	public int getPage();
	public void setPage(int page);
	public boolean isShowFirstPage();
	public boolean isShowLastPage();
	public int getFirstPage();
	public int getPreviousPage();
	public int getNextPage();
	public int getLastPage();
	public List<Integer> getScrollPageToRender();
	public boolean isShowScrollDataTable();
	public void changePage();
	public boolean isPageRendered(int currentPage);
}
