package com.obc.csrg.local.report;

import javax.ejb.Local;

import com.obc.csrg.model.KeywordsLang;
import com.obc.csrg.search.SearchResultModel;
import com.obc.csrg.util.PagedArrayList;

@Local
public interface GlobalSearchReportBeanLocal extends ReportBeanLocal<KeywordsLang> {

	public PagedArrayList<SearchResultModel> getSearchResults();
}
