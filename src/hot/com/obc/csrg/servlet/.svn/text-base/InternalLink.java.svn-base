package com.obc.csrg.servlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.servlet.ContextualHttpServletRequest;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;

import com.obc.csrg.model.Webpage;

public class InternalLink extends HttpServlet implements Serializable {
	
	private EntityManager em;
	
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException{
		
		
		new ContextualHttpServletRequest(request){
			@Override
			public void process() throws Exception{
				em = (EntityManager) Component.getInstance("entityManager");
				doStuff(request,response);
			}
		}.run();
		
	}
	private void doStuff(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String[] arr = request.getRequestURL().toString().split("/");
		String url = request.getRequestURL().toString();
		String popup = request.getParameter("popup");
		
		
		String title = ".:: CSRG ::.";
		if(arr.length>0){
			int i = arr.length-1;
			String pagename = arr[i];
			
			if(em!=null){
				List<Webpage> list = em.createQuery("select m from Webpage m where " +
						"lower(m.pageName)=lower(?)")
						.setParameter(1, pagename)
						.getResultList();
				if(list.size()>0){
					PrintWriter out = response.getWriter();
					//out.println(putHtmlBody(list.get(0).getPageName(), list.get(0).getHtml(), list.get(0).getId()));
					
					String pageUrl = "";
					
					if(popup != null)
						pageUrl = "/csrg/forms/dynamicContentViewPopup.seam?pageId=" + list.get(0).getVisualItem().getId();
					else
						pageUrl = "/csrg/forms/dynamicContentView.seam?pageId=" + list.get(0).getVisualItem().getId();

					response.setStatus(response.SC_MOVED_TEMPORARILY);
					response.setHeader("Location", pageUrl);

					
					//out.close();
				}else{
					PrintWriter out = response.getWriter();
					out.println(putHtmlBody(title, url + "<br/>" + pagename + " not found", 1));
					out.close();
				}
			}else{
				PrintWriter out = response.getWriter();
				out.println(putHtmlBody(title, "entity manager null<br/>" + url + "<br/>" + pagename + " not found", 1));
				out.close();
			}
		}
	}
	private String putHtmlBody(String title, String text, long pageId){
		
	//	String s = (String) pageId;
		
		return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
		"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n" +
		"<head>\n" +
		"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />" +
		"<title>"+title+"</title>\n" +
		"</head>\n" +
		"<body>\n" +
		text +" Page: " +pageId +
		"</body>\n" +
		"</html>";
	}
	
}
