package com.obc.csrg.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
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

public class PdfBuilder {

	private String documentName;
	private Document document;
	private PdfWriter writer;
	
	public PdfBuilder(String filename) throws FileNotFoundException, DocumentException{
		documentName = filename;
		document = new Document(PageSize.A4);
		this.writer = PdfWriter.getInstance(document, new FileOutputStream(documentName));
		document.open();
	}
	public PdfBuilder(String filename,String header,boolean showInFirstPage,String fPage,String pPage,String nPage,String lPage) throws FileNotFoundException, DocumentException{
		documentName = filename;
		document = new Document(PageSize.A4);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(documentName));
		HeaderFooterPdf hf = new HeaderFooterPdf(header,showInFirstPage,fPage,pPage,nPage,lPage);
		writer.setPageEvent(hf);
		this.writer = writer;
		document.open();
	}
	
	public void addHeader(String name) throws DocumentException{
		addParagraph(name);
	}
	public void addSubHeader(String name) throws DocumentException{
		addParagraph(name);
	}
	public void addTab(String name) throws DocumentException{
		addParagraph(name);
	}
	public void addParagraph(String text) throws DocumentException{
		document.add(new Paragraph(text));
	}
	public void addParagraph(String text, String align) throws DocumentException{
		int element = Element.ALIGN_LEFT;
		if(align!=null){
			if(align.equalsIgnoreCase("center")) element = Element.ALIGN_CENTER;
			else if(align.equalsIgnoreCase("left")) element = Element.ALIGN_LEFT;
			else if(align.equalsIgnoreCase("right")) element = Element.ALIGN_RIGHT;
		}
		Paragraph p = new Paragraph(text);
		p.setAlignment(element);
		document.add(p);
	}
	public void addField(String name, String value) throws DocumentException{
		addParagraph(name+" : "+value);
	}
	public void addFooter(String name) throws DocumentException{
		addParagraph(name);
	}
	public void addSpace() throws DocumentException{
		addParagraph("");
	}
	public static String makeLink(String name, String link){
		link = link.trim();
		String l = (link.matches("^http://.*$"))? link.substring(7, link.length()) : link ;
		return name+" - http://"+l;
	}
	
	public File getPdfDocument(){
		document.close();
		return new File(documentName);
	}
	public Document getDocument(){
		return document;
	}
	
	
	private static class HeaderFooterPdf extends PdfPageEventImpl{
		protected Phrase header;
		protected PdfPTable footer;
		private String firstPage = "First Page";
		private String prevPage = "Prev Page";
		private String nextPage = "Next Page";
		private String lastPage = "Last Page";
		private boolean showInFirstPage = false;
		
		public HeaderFooterPdf(){
			header = new Phrase("cabeçalho");
			footer = new PdfPTable(4);
			footer.setTotalWidth(300);
			footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			footer.addCell(new Phrase(new Chunk(firstPage).setAction(new PdfAction(PdfAction.FIRSTPAGE))));
			footer.addCell(new Phrase(new Chunk(prevPage).setAction(new PdfAction(PdfAction.PREVPAGE))));
			footer.addCell(new Phrase(new Chunk(nextPage).setAction(new PdfAction(PdfAction.NEXTPAGE))));
			footer.addCell(new Phrase(new Chunk(lastPage).setAction(new PdfAction(PdfAction.LASTPAGE))));
		}
		public HeaderFooterPdf(String headerString,boolean showInFirstPage){
			this.showInFirstPage = showInFirstPage;
			this.buildStuff(headerString);
		}
		public HeaderFooterPdf(String header,boolean showInFirstPage,String fPage,String pPage,String nPage,String lPage){
			this.showInFirstPage = showInFirstPage;
			this.firstPage = fPage;
			this.prevPage = pPage;
			this.nextPage = nPage;
			this.lastPage = lPage;
			this.buildStuff(header);
		}
		private void buildStuff(String headerString){
			header = new Phrase(headerString);
			footer = new PdfPTable(4);
			footer.setTotalWidth(300);
			footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			footer.addCell(new Phrase(new Chunk(firstPage).setAction(new PdfAction(PdfAction.FIRSTPAGE))));
			footer.addCell(new Phrase(new Chunk(prevPage).setAction(new PdfAction(PdfAction.PREVPAGE))));
			footer.addCell(new Phrase(new Chunk(nextPage).setAction(new PdfAction(PdfAction.NEXTPAGE))));
			footer.addCell(new Phrase(new Chunk(lastPage).setAction(new PdfAction(PdfAction.LASTPAGE))));
		}
		
		@Override
		public void onEndPage(PdfWriter writer, Document document) {
			int page = 1;
			if(showInFirstPage)
				page=0;
			PdfContentByte cb = writer.getDirectContent();
			if (document.getPageNumber() > page) {
				ColumnText.showTextAligned(cb,Element.ALIGN_CENTER, header,(document.right() - document.left()) / 2
						+ document.leftMargin(), document.top() + 10, 0);
			}
			footer.writeSelectedRows(0, -1,(document.right() - document.left() - 300) /2
					+ document.leftMargin(), document.bottom() - 10, cb);
		}
		
	}


	public PdfWriter getWriter() {
		return writer;
	}
	public void setWriter(PdfWriter writer) {
		this.writer = writer;
	}
	
}
