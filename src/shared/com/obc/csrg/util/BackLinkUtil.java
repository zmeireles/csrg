package com.obc.csrg.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.obc.csrg.util.ParamUtil;

public class BackLinkUtil implements Serializable {

	private String page = "";
	private List<ParamUtil> params = new ArrayList<ParamUtil>();
	// received parameters considered invalid for the back function
	private static String[] invalidParams = { "cid", "conversationPropagation" };

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public List<ParamUtil> getParams() {
		return params;
	}

	public void setParams(List<ParamUtil> params) {
		this.params = params;
	}

	public boolean hasParams() {
		boolean result = false;
		Iterator<ParamUtil> p = params.iterator();
		while (p.hasNext() && result == false) {
			if (this.isParamValid(p.next()))
				result = true;
		}
		return result;
	}

	public String toString() {
		StringBuffer result = new StringBuffer(this.getPage());
		if (this.hasParams())
			result.append("?").append(this.paramsToString());
		return result.toString();
	}

	public String paramsToString() {
		StringBuffer result = new StringBuffer("");
		Iterator<ParamUtil> paramsI = this.getParams().iterator();
		ParamUtil aux;
		while (paramsI.hasNext()) {
			aux = paramsI.next();
			if (this.isParamValid(aux)) {
				result.append(aux.toString());
				if (paramsI.hasNext())
					result.append("&");
			}
		}
		return result.toString();
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj.getClass().getName().equals(BackLinkUtil.class.getName())) {
			result = ((BackLinkUtil) obj).getPage().equals(this.getPage());
		}
		return result;
	}

	private boolean isParamValid(String param) {
		boolean result = true;
		for (String pInvalid : invalidParams) {
			if (param.toLowerCase().equals(pInvalid.toLowerCase()))
				result = false;
		}
		return result;
	}

	private boolean isParamValid(ParamUtil param) {
		return this.isParamValid(param.getName());
	}

	public boolean containParam(String param) {
		boolean result = false;
		Iterator<ParamUtil> p = params.iterator();
		while (p.hasNext() && result == false) {
			if (param.equalsIgnoreCase(p.next().getName()))
				result = true;
		}
		return result;
	}

	public String obterParam(String param) {
		Iterator<ParamUtil> i = this.getParams().iterator();
		ParamUtil aux = null;
		while (i.hasNext()) {
			aux = i.next();
			if (param.equalsIgnoreCase(aux.getName()))
				return aux.getValue();
		}
		return "";
	}

}
