package com.obc.csrg.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPageEvent;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.Image;
import com.lowagie.text.Anchor;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfAnnotation;

import java.awt.Color;


import java.text.SimpleDateFormat;

import com.obc.csrg.model.User;
import com.obc.csrg.model.Person;
import com.obc.csrg.model.Notification;
import com.obc.csrg.util.SearchModel;
import com.obc.csrg.util.HtmlBuilder;
import com.obc.csrg.util.PdfBuilder;
import com.obc.csrg.model.DBFile;
import com.obc.csrg.constants.Constants;

public class MessagesFactory {
	
	private static String tmpDir = Constants.CSRG_HOME + Constants.CSRG_DIR_TMP;
	public static Map<String,String> messages;
	
	public static String formatDate(Date d){
		return ((d==null)? "" : new SimpleDateFormat("dd-MM-yyyy").format(d));
	}
	
	/**
	 * converts a string to regular US-ASCII chars
	 * @param string
	 * @return
	 */
	public static String getAsciiString(String string){
		try{
			String result = new String(string.getBytes(),"US-ASCII");
			return result;
		}catch(Exception e){
			e.printStackTrace();
			return string;
		}
	}
	/**
	 * builds the pdf file to be sent by fax when an utente registers
	 * @param user
	 * @param messages
	 * @return the file handler if succeeds, null if fails
	 */
	public static File getUtenteRegistrationPdfFile(User user,Map<String,String> messages){
		String filename = "c:/temp.pdf";
		try{
			PdfBuilder pdf = new PdfBuilder(filename);
			pdf.addHeader(messages.get("RegisterFaxTitle"));
			pdf.addSubHeader(messages.get("RegisterFaxSubTitle"));
			pdf.addField("Username", user.getUsername());
			pdf.addField("Password", user.getPassword());
			pdf.addFooter(messages.get("RegisterFaxFooter"));
			return pdf.getPdfDocument();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * builds the pdf file to be sent by fax when an entity registers
	 * @param user
	 * @param messages
	 * @return the file handler if succeeds, null if fails
	 */
	public static File getEntidadeRegistrationPdfFile(User user,Map<String,String> messages){
		String filename = "c:/temp.pdf";
		try{
			PdfBuilder pdf = new PdfBuilder(filename);
			pdf.addHeader(messages.get("RegisterFaxTitle"));
			pdf.addSubHeader(messages.get("RegisterFaxSubTitle"));
			pdf.addField("Username", user.getUsername());
			pdf.addField("Password", user.getPassword());
			pdf.addFooter(messages.get("RegisterFaxFooter"));
			return pdf.getPdfDocument();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public static String getEmailText(Notification notification){
		return notification.getMessage();
	}
	public static String getSmsText(Notification notification){
		return notification.getSubject() + ": " + notification.getMessage();
	}
	/**
	 * builds a PDF file containing the notification subject and message
	 * @param notification
	 * @return PDF file
	 */
	public static File getFaxPdfForNotification(Notification notification){
		String filename = "c:/temp_" + notification.getId() + ".pdf";
		try{
			PdfBuilder pdf = new PdfBuilder(filename);
			pdf.addParagraph(notification.getSubject());
			pdf.addParagraph("");
			pdf.addParagraph(notification.getMessage());
			return pdf.getPdfDocument();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
