package com.obc.csrg.servlet;

import java.io.File;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.Component;
import org.jboss.seam.servlet.ContextualHttpServletRequest;

import com.obc.csrg.model.Config;

public class MmsDownload extends HttpServlet implements Serializable {
	
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
		System.out.println("MMS download servlet");
		
		String[] arr = request.getRequestURL().toString().split("/");
		
		if(arr.length>2){
			String originalFilename = arr[arr.length-1].trim();
			String username = arr[arr.length-2].trim();
			String path = "c:/csrg/files";
			
			List<Config> configs = em.createQuery("select m from Config m")
									.getResultList();
			if(configs.size()>0){
				Config config = configs.get(0);
				if(config.getUploadDirectory()!=null && !config.getUploadDirectory().trim().equals(""))
					path=config.getUploadDirectory();
			}
			//System.out.println("path:" + path);
			String filename = path + "/" + username+"/" + originalFilename;
			File f = new File(filename);
			int length = 0;
			
			ServletOutputStream outputStream = response.getOutputStream();
			ServletContext context = getServletConfig().getServletContext();
			String mimetype = context.getMimeType(filename);
			
			//System.out.println(mimetype);
			//set response
			String headerName = "Cache-Control"; //"Content-Disposition"
			String headerValue = "no-store";//"attachment; filename=\"" + originalFilename + "\"" 
			response.setContentType( (mimetype != null) ? mimetype : "application/octet-stream" );
	        response.setContentLength( (int)f.length() );
	        response.setHeader(headerName, headerValue);
	        
	        
	        //stream to requester
	        /*
	         * all stuff
	        FileInputStream fis = new FileInputStream(f);
	        byte[] bytes = new byte[(int)f.length()];
        	fis.read(bytes);
	        response.getOutputStream().write(bytes);
	        */
	        
	        //or buffered
	        final int BUFSIZE = 8192;
	        byte[] bbuf = new byte[BUFSIZE];
	        DataInputStream in = new DataInputStream(new FileInputStream(f));
	        
	        while ((in != null) && ((length = in.read(bbuf)) != -1))
	        {
	        	outputStream.write(bbuf,0,length);
	        }

	        in.close();
	        outputStream.flush();
	        outputStream.close();
		}
	}
	
}
