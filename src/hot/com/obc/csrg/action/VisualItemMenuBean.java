package com.obc.csrg.action;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.log.Log;

import javax.persistence.EntityManager;

import com.obc.csrg.local.StateBeanLocal;
import com.obc.csrg.local.VisualItemMenuBeanLocal;
import com.obc.csrg.model.Config;
import com.obc.csrg.model.VisualItem;

@Stateless
@Name("visualItemMenu")
public class VisualItemMenuBean  implements VisualItemMenuBeanLocal {

	@Logger
	private Log log;

	@In
	protected EntityManager entityManager;
    
	@In(required = false)
	private FacesContext facesContext;

	@In(required=false)
	protected StateBeanLocal stateBean;
	
	@RequestParameter
	private Long pageId;
	
	private int menuLabel = 0;
	private int menuMaxLabel = 0;
	private int [] maxWidth = {0, 0, 0, 0, 0, 0, 0, 0, 0};
	
	String menuTxt = "This will be the menu code";

	StringBuffer text= new StringBuffer();
	int option = 0;
	
	
	final String QUERY_COND = " m.publish=true ";// + //published
			//" and ((select count(u.id) from UserNotification u where u.visualItem.id=m.id)=0 ";// + 	//everyone
			//"or ? in (select u.user.id from UserNotification u where " +
			//"u.visualItem.id is not null and u.visualItem.id=m.id)) ";// or allowed users
 
	final String QUERY = " m.publish=true " + //published
			" and ((select count(u.id) from UserNotification u where u.visualItem.id=m.id)=0 " + 	//everyone
			"or ? in (select u.user.id from UserNotification u where " +
			"u.visualItem.id is not null and u.visualItem.id=m.id)) ";// or allowed users
	
	private String selectedQuery = QUERY;
	
    public VisualItemMenuBean(){
 
    	this.menuTxt = "some text";
    }
    
    private Config getConfig() {
		Config config = null;
		try {
			List<Config> configs = entityManager.createQuery(
					"select c from Config c").setHint(
					"org.hibernate.cacheable", true).getResultList();
			if (configs.size() > 0) {
				config = configs.get(0);
			} else {
				config = null;
			}
		} catch (Exception ex) {
			config = null;
		}
		return config;
	}
    
    public String buildMenu(){
    	this.menuTxt = "some text";
    	this.selectedQuery = QUERY;
    	//log.info("[buildMenu] menuTxt:#0", menuTxt);
    	//this.createMenu();
    	
    	//return this.menuTxt;
    	int i = this.text.length();
    	this.text.delete(0, i);
    	this.initMenu();
    	//log.info("[buildMenu] menuTxt:#0", menuTxt);
    	//log.info("[buildMenu] text:#0", text.toString());
    	return text.toString();
    }
    
    public String buildSimulatorMenu(){
    	this.menuTxt = "some text";
    	this.selectedQuery = QUERY_COND;
    	//log.info("[buildMenu] menuTxt:#0", menuTxt);
    	//this.createMenu();
    	
    	this.option = 1;
    	//return this.menuTxt;
    	int i = this.text.length();
    	this.text.delete(0, i);
    	this.initMenu();
    	//log.info("[buildMenu] menuTxt:#0", menuTxt);
    	//log.info("[buildMenu] text:#0", text.toString());
    	return text.toString();
    }
    
    public String getIFramePageLink(){
    	return "/csrg/forms/iframeContent.seam?pageId=" + pageId;
    }
    public void pageFactory(){
    	//log.info("[pageFactory] pageId:#0", pageId);
    	if(pageId!=null && !pageId.equals(0L)){
    		//get the page
    		List<VisualItem> list = entityManager.createQuery("select m from VisualItem m where m.id=?")
    				.setParameter(1, pageId)
    				.getResultList();
    		//log.info("[pageFactory] list:#0", list);
    		if(list.size()>0){
    			VisualItem vi = list.get(0);
    			if(vi.getWebpage()!=null){
    				/*String html = "<iframe name='internalPage' " + 
    					"width='400' height='200'> " +
    					this.putHtmlBody(vi.getWebpage().getPageName(), vi.getWebpage().getHtml()) +
    					"</iframe>";*/
    				String html = this.putHtmlBody(vi.getWebpage().getPageName(), vi.getWebpage().getHtml());
    				
    				stateBean.echo(html);
    				//log.info("[pageFactory] html:#0", vi.getWebpage().getHtml());
    				//stateBean.echo(vi.getWebpage().getHtml());
    			}
    		}
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
    
/* ******************************************************************************************* */
    //Please check following 4 methods
    
    public void initMenu(){
		
    	String html = "<div id='sfMenu'><ul class='sf-menu sf-vertical' id='menuLabel0'>\n";
		
    	this.text.append(html);
    	
		List<VisualItem> menuItems = this.retrieveTopMenuItems();
		
		this.createMenuCode(menuItems);
		
		html = "</ul></div>\n";
		
    	this.text.append(html);

	}
    
    public List<VisualItem> retrieveTopMenuItems(){
		 List<VisualItem> menuItems;
		 if(this.selectedQuery.equals(QUERY))
			 menuItems = entityManager.createQuery(
					"select m from VisualItem m " +
					"where m.parent.id is null and " +
					this.QUERY +
					"order by m.itemOrder")
					.setParameter(1, stateBean==null || stateBean.getUser()==null ? 0L: stateBean.getUser().getId())
					.getResultList();
		 else
			 menuItems = entityManager.createQuery(
						"select m from VisualItem m " +
						"where m.parent.id is null and " +
						this.QUERY_COND +
						"order by m.itemOrder")
						//.setParameter(1, stateBean==null || stateBean.getUser()==null ? 0L: stateBean.getUser().getId())
						.getResultList();
		 //log.info("[retrieveTopMenuItems] menuItems:#0", menuItems);
		return menuItems;
    }
    
    
    
	public List<VisualItem> retrieveMenuItems(long parentId){
		
		 List<VisualItem> menuItems;
		 if(this.selectedQuery.equals(QUERY))
			menuItems = entityManager.createQuery(
					"select m from VisualItem m " +
					"where m.parent.id = ? and " +
					this.QUERY +
		 			"order by m.itemOrder")
		 			.setParameter(1, parentId)
		 			.setParameter(2, stateBean==null || stateBean.getUser()==null ? 0L: stateBean.getUser().getId())
		 			.getResultList();
		 else
			 menuItems = entityManager.createQuery(
						"select m from VisualItem m " +
						"where m.parent.id = ? and " +
						this.QUERY_COND +
			 			"order by m.itemOrder")
			 			.setParameter(1, parentId)
			 			//.setParameter(2, stateBean==null || stateBean.getUser()==null ? 0L: stateBean.getUser().getId())
			 			.getResultList();
		return menuItems;
	}
	
	
    public void createMenuCode(List<VisualItem> menuItems){
		 
    	String html = "";
    	
    	for (int i=0; i< menuItems.size(); i++){
    	
    		VisualItem item = menuItems.get(i);
    		String link = this.makeLink(item.getXhtmlFilename(), item.getId());
    	 
    		html = "<li><a href='" + link + "' class='viMenuAnchor' onclick='return popup(\"" + link + "\")'>" + item.getMenuText() + "</a>\n";

    		this.text.append(html);
    		
    		if(item.getMenuText().length() > this.maxWidth[this.menuLabel])
    		{
    			this.maxWidth[this.menuLabel] = item.getMenuText().length();
    		}
    				
    		List<VisualItem> subMenuItems = this.retrieveMenuItems(item.getId());
    		
    		if(subMenuItems.size() > 0){
    			
    			this.menuMaxLabel++;
    			this.menuLabel++;
    			
    			html = "<ul id='menuLabel" + Integer.toString(this.menuLabel) + "'>\n";
        		this.text.append(html);
				this.createMenuCode(subMenuItems); 
				html = "</ul>\n";
				this.text.append(html);	
				
    			this.menuLabel--;
    		}
    		
    		html = "</li>\n";
    		this.text.append(html);	
    	} 
	}
    
   
    public String makeLink(String xhtmlFile, long pageId){
    	String link = "";
    	
    	if(this.option == 1)
    		link = "/csrg/forms/dynamicContentViewPopup.seam?pageId=" + pageId;//"http://localhost/csrg/mypage.seam?id=" + pageId;
    	else
    		link = "/csrg/forms/dynamicContentView.seam?pageId=" + pageId;//"http://localhost/csrg/mypage.seam?id=" + pageId;


    	// this is wrong: if(xhtmlFile == "" || xhtmlFile == null ) the == compares objects or values IF objects are 
    	// of primitive types. That's not the case of String.
    	//in the if below the order of conditions is important
    	if(xhtmlFile==null || xhtmlFile.equals(""))
    		return link;
    	else    
    		return "/csrg/" + this.makeStaticLink(xhtmlFile); // "http://localhost/csrg/" + xhtmlFile;

    }
    
    
    public String makeStaticLink(String xhtmlFile)
    {
    	// String path ="";
    	String fname = "";
	    String ext = "";
	    
	    int mid = xhtmlFile.lastIndexOf(".");
	    fname = xhtmlFile.substring(0,mid);
	    ext = xhtmlFile.substring(mid+1,xhtmlFile.length());
	    
	   // path = "csrg/" + xhtmlFile;
	    //File file=new File(path);
	   // boolean exists = file.exists();
	    
	    //path ="NO";
	    //if(exists) path ="Yes";
	    
	    if(ext.equals("xhtml") || ext.equals("seam") )
	    	return fname + ".seam";
	    else 
	    	return "";
	    	
    }
    
  /* **************************************************************************************** */  
    
    
    
  
	public void createMenu()
	{
    	this.menuTxt = "This variable should contains the html code for the menu and execute in the page";
		
    	File f = new File("c:/menu.txt");
    	StringBuffer text= new StringBuffer();
    	try{
    		BufferedReader input = new BufferedReader(new FileReader(f));
    		try{
	    		String line;
	    		while((line = input.readLine())!=null){
	    			line = line.trim();
	    			text.append(line);
	    		}
    		}finally{
    			input.close();
    		}
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    	this.menuTxt = text.toString();
    	
    	//List<VisualItem>  items = em.createQuery("select v from VisualItem v").getResultList();
      	
    	/* 
    	 * 
    	//here are the code you need to convert 
    	
    	$vi = new VisualItem(); // model of visual item

    	$result = $vi->getItemByParentId('0'); 
	    	//result contains all rows of visual item table whose parent id is 0
	    	// that is this function  executes "select * from visual_item where parent_id = '$parent_id'"  
	    	// sql statement

    	$html = '<ul class="sf-menu sf-vertical">'; // contains html code for the menu; stats with <ul> tag
    	 
    	$html = this.createMyMenu($html, $result, $iv,  '0'); // this function is the recursive function
    	
    	$html = $html.'</ul>'; // ends with closing </ul> tag
    	
    	return $html;
    	
    	//thats it, now $html variable has the code 
    	
    	*
    	*/
    			
	}
	

	public String menuWidth(int index)
	{
		int width =   (this.maxWidth[index] * 12) + 15;	
		int maxWidth = this.getConfig().getViMenuMaxWidth();
		if(width >= maxWidth) 
			return  Integer.toString(maxWidth)+"px";
		else 
			return  Integer.toString(width)+"px";
	}
	
	
	
	
	//getters and setters
	public String getMenuTxt() {
		return menuTxt;
	}
	public void setMenuTxt(String menuTxt) {
		this.menuTxt = menuTxt;
	}

	public void setMenuMaxLabel(int menuMaxLabel) {
		this.menuMaxLabel = menuMaxLabel;
	}

	public int getMenuMaxLabel() {
		return menuMaxLabel;
	}
	
}
