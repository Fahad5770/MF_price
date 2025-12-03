package com.pbc.pushmail.scorecards;

import java.sql.SQLException;

import com.pbc.common.User;
import com.pbc.employee.EmployeeHierarchy;
import com.pbc.util.Utilities;

import java.io.FileOutputStream;
import java.io.IOException;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pbc.util.Datasource;
import com.pbc.util.UserAccess;
import com.pbc.util.Utilities;

public class DistributorKPIsSendEmail2 {
	
	
	public static final String stock_filename_distributor_all_2 = "Stock_Analysis_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	
	public static void main(String[] args) {
		
		try {
			
			
	             
	        
	        Thread ConsolidatedThread = new Thread()
	        {
	            public void run() {
	                try{
			// Consolidated
			
	        
	        
			
			DistributorStockAnalysisExcel2 DistributorStockAnalysis2 = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel2();
			DistributorStockAnalysis2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + stock_filename_distributor_all_2, 0);
			
			
			
			//Utilities.sendPBCHTMLEmail(new String[]{"salman.baig@pbc.com.pk","zahoor@pbc.com.pk"},new String[]{"abdul.basit@pbc.com.pk","omerfk@pbc.com.pk","anjum.a.ansari@gmail.com","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","aamir.zafar@pbc.com.pk","asad.ali@pbc.com.pk","ahmad.raza@pbc.com.pk"}, null, "KPI | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_all,filename_distributor_all_mtd,filename_pjp_all,filename_pjp_all_mtd,stock_filename_distributor_all,filename_no_sale_all,filename_sku_availability_all,filename_volume_analysis_all});
			
			
			Utilities.sendPBCHTMLEmail(new String[]{"shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","zulqurnan.aslam@pbc.com.pk"},null, null, "KPI | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{stock_filename_distributor_all_2});
			
	                
	                
			
			
			
			
	        	                
	        	                
			
			
	                }catch(Exception e){e.printStackTrace();}
	            }
	        };
	        ConsolidatedThread.start();
	     
	       
			
			
			
	       
			

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public static String getHTMLMessage(long EmployeeID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		Datasource ds = new Datasource();
		ds.createConnection();
		
		
		Statement s = ds.createStatement();
		Statement s5 = ds.createStatement();
		Statement s2 = ds.createStatement();
		Statement s3 = ds.createStatement();
		
		
		
		
		String html = "<html>";
		html += "<body><br>";
				
			html += "<br><br>";
				
			html += "Please see attachments.";
			
			
		
		s.close();
		ds.dropConnection();	
		
		return html;
		
	}
	
	public static String getHTMLMessageTDM(long EmployeeID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		
		String html = "<html>";
		html += "<body><br>";
			html += "<table style='width: 450px'>";

			
			html += "<tr>";
			html += "<td colspan='2' style='text-align: left; font-size: 10px;'>&nbsp;</td>";
			html += "</tr>";
			
			html += "</table>";
				
			html += "<br><br>";
				
			html += "Please see attachment for details.<br>";
		
		return html;
		
	}

}
