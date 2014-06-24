package com.obc.csrg.util;

import java.io.Serializable;

public class HtmlBuilder implements Serializable  {
	
	private StringBuffer bodyDoc = new StringBuffer("");
	
	private final static String headeDoc = "<head>\n<meta http-equiv='Content-Language' content='pt'>\n" +
							"<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>\n" +
							"<title>Bolsa de Qualificação e Emprego</title>\n</head>\n";
	
	public void addHeader(String name){
		bodyDoc.append("<p align='center'><font size='5'>"+name+"</font></p>\n" +
				"<p align='center'>&nbsp;</p>\n");
	}
	public void addSubHeader(String name){
		bodyDoc.append("<p align='center'><font size='5'>"+name+"</font></p>\n" +
				"<p align='center'>&nbsp;</p>\n");
	}
	public void addTab(String name){
		bodyDoc.append("<p align='center'>&nbsp;</p>\n" +
				"<p align='left'><u>"+name+" :</u></p>\n");
	}
	public void addField(String name, String value){
		bodyDoc.append("<p align='left'>"+name+" : "+value+"</p>\n");
	}
	public void addFooter(String name, String link){
		bodyDoc.append("<p align='center'><font size='1'>"+name+" - " +makeLink(link));
	}
	public void addSpace(){
		bodyDoc.append("<p align='center'>&nbsp;</p>\n");
	}
	public void initTable(int border, int cellspacing, int cellpadding){
		bodyDoc.append("<table border='"+border+"' cellspacing='"+cellspacing+"' cellpadding='"+cellpadding+"' >\n");
	}
	public void addTableLine(String ... cells){
		bodyDoc.append("<tr>\n");
		for(String c : cells){
			bodyDoc.append("<td>"+c+"</td>\n");
		}
		bodyDoc.append("</tr>\n");
	}
	public void endTable(){
		bodyDoc.append("</table>\n");
	}
	public void addParagraph(String text){
		bodyDoc.append("<p align='left'>"+text+"</p>\n");
	}
	public static String makeLink(String link){
		link = link.trim();
		String l = (link.matches("^http://.*$"))? link.substring(7, link.length()) : link ;
		return "<a href='http://"+l+"'>"+l+"</a></font></p>\n";
	}
	
	public String getHTMLDocument(){
		String result = "<html>\n"+headeDoc+"<body>\n"+bodyDoc.toString()+"</body>\n</html>\n";
		//System.out.println(result);
		return result;
	}
}
