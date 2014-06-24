package com.obc.csrg.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.Component;

import com.obc.csrg.model.DBFile;

public class Download extends HttpServlet implements Serializable {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		System.out.println("SERVLET");
		String title = ".:: CSRG ::.";
		String noDownload = putHtmlBody(title, "<b>ERROR: </b>No File");
		EntityManager em = (EntityManager) Component.getInstance("entityManager");
		
		try{
			if (request.getParameter("f") != null && request.getParameter("k") != null){	//performs the download of the file
				Long ficheiroId = new Long(request.getParameter("f"));
				List <DBFile> lf = em.createQuery("select f from DBFile f where f.id=?")
							.setParameter(1, ficheiroId)
							.getResultList();
				if(lf != null && lf.size() > 0){
					DBFile f = lf.get(0);
					if(f.valorIsCert(request.getParameter("k"))){							//in case it's certified
						response.setContentType(f.getContentType());
						response.addHeader("Content-Disposition", "attachment; filename=\""+f.toString()+"\"");
						response.getOutputStream().write(f.getData());
					}else{
						response.getOutputStream().print(noDownload);
					}
				}else{
					response.getOutputStream().print(noDownload);
				}
			}else if(request.getParameter("f") != null && 
					request.getParameter("f").equals("imgLiveSupport") && 
					request.getParameter("wikiDomain") != null &&
					request.getParameter("myImg") != null){							//sends live support image
				String adress = "http://"+request.getParameter("wikiDomain")+"/tiki-live_support_server.php?operators_online";
				try{
					URL url = new URL(adress);
					InputStream in = url.openConnection().getInputStream();
					int i = in.read();
					while(i!=-1){
						response.getOutputStream().write(i);
						i = in.read();
					}
				}catch(Exception e){
					//System.err.print(adress);
					//e.printStackTrace();
					try{
						adress = "http://"+request.getParameter("myImg");
						URL url = new URL(adress);
						InputStream in = url.openConnection().getInputStream();
						int i = in.read();
						while(i!=-1){
							response.getOutputStream().write(new Integer(i).byteValue());
							i = in.read();
						}
					}catch(Exception ex){}
				}
			}else{
				response.getOutputStream().print(noDownload);
			}
		}catch(Exception e){
			response.getOutputStream().print(putHtmlBody(title, "<b>ERROR: </b>"+e));
		}
	}
	
	private String putHtmlBody(String title, String text){
		
		return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
		"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n" +
		"<head>\n" +
		"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />" +
		"<title>"+title+"</title>\n" +
		"</head>\n" +
		"<body>\n" +
		text +
		"</body>\n" +
		"</html>";
	}
	
}
