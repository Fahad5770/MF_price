package com.pbc.cash;

import java.io.FileOutputStream;
import java.io.IOException;
 






import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class GLCashReceiptsPDF {

	public static final String RESULT = "c:\\Workspace\\hello.pdf";
	
	public static void main(String[] args) throws DocumentException, IOException{
		new GLCashReceiptsPDF().createPdf(RESULT);
	}
	public void createPdf(String filename) throws DocumentException, IOException {
	    Document document = new Document();
	    PdfWriter.getInstance(document, new FileOutputStream(filename));
	    document.open();
	    
	    PdfPTable table = new PdfPTable(3);
	    
	    table.setWidthPercentage(100f);
	    //table.getDefaultCell().setUseAscender(true);
	    //table.getDefaultCell().setUseDescender(true);
	    table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
	    
	    Font f = new Font();
        f.setColor(BaseColor.WHITE);
	    
        PdfPCell pcell = new PdfPCell(new Phrase("Testing Header", f));
        pcell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setColspan(3);
        
        table.addCell(pcell);
        
	    PdfPCell cell;
	    cell = new PdfPCell(new Phrase("Cell with colspan 3"));
	    cell.setColspan(2);
	    table.addCell(cell);
	    
	    
	    table.addCell("row 1; cell 1");
	    table.addCell("row 1; cell 2");
	    
	    table.addCell("row 2; cell 1");
	    table.addCell("row 2; cell 2");
	    
	    document.add(new Paragraph("Hello World!"));
	    document.add(table);
	    
	    document.close();
	}	
}
