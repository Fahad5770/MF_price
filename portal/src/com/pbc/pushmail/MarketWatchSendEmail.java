package com.pbc.pushmail;

import java.io.FileOutputStream;
import java.io.IOException;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.mail.EmailException;

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
import com.pbc.util.Utilities;

public class MarketWatchSendEmail {
	
	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	String HTMLMessage;
	public static final String filename = "MarketWatchSource"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	public static void main(String[] args) throws DocumentException, IOException{
		try {
			MarketWatchSendEmail pe = new MarketWatchSendEmail();
			pe.setHTMLMessage();
			
			
			new com.pbc.pushmail.MarketWatchSourcePDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename, 0);
			
			
			
			
			//updated by mohsin [01/04/2017]
			Utilities.sendPBCHTMLEmail(new String[]{"omerfk@pbc.com.pk","salman.baig@pbc.com.pk"}, new String[]{"anjum.a.ansari@gmail.com","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","jazeb@pbc.com.pk","khurram.jaffar@pbc.com.pk","muhammad.rizwan@pbc.com.pk","r&d@pbc.com.pk","kashif.rashid@pbc.com.pk"}, null, "Market Watch "+Utilities.getDisplayDateFormat(new Date()), pe.getHTMLMessage(), new String[]{filename});
			
			
			/////older - Utilities.sendPBCHTMLEmail(new String[]{"omerfk@pbc.com.pk","salman.baig@pbc.com.pk"}, new String[]{"anjum.a.ansari@gmail.com","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","obaid@pbc.com.pk","jazeb@pbc.com.pk","ahsan.rashid@optimus-uae.com","muhammadameer@hotmail.com","rafiquemeo@yahoo.com","shehzadaleem@hotmail.com","naeem.mumtaz@pbc.com.pk","mohsin.khan@pbc.com.pk"}, null, "Market Watch "+Utilities.getDisplayDateFormat(new Date()), pe.getHTMLMessage(), new String[]{filename});
			
			
			//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"}, new String[]{"shahrukh.salman@pbc.com.pk"}, null, "Market Watch "+Utilities.getDisplayDateFormat(new Date()), pe.getHTMLMessage(), new String[]{filename});
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getHTMLMessage(){
		return HTMLMessage;
	}
	
	public MarketWatchSendEmail() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		ds.createConnection();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();
	}
	

	public void setHTMLMessage() throws SQLException{
		
		String html = "<html>";
			html += "<body><br>";
		
			html += "<table style='width: 1010px;'>";
			
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='7'>1500ML PET</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;' colspan='2'>Difference</td>";
				html += "</tr>";
				html += "<tr>";
					html += "<td style='width: 100px;background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Region</td>";
					html += "<td style='width: 200px;background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Distributor</td>";
					html += "<td style='width: 80px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Outlets</td>";
					html += "<td style='width: 80px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Wholesale</td>";
					html += "<td style='width: 80px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Coke</td>";
					html += "<td style='width: 80px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Pepsi</td>";
					html += "<td style='width: 80px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Cross Franchise</td>";
					html += "<td style='width: 155px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Coke</td>";
					html += "<td style='width: 155px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Cross Franchise</td>";
				html += "</tr>";
				
				ResultSet rs = s.executeQuery("SELECT "+
					" cmw.region_id,"+ 
					" (select concat(region_short_name, ' ' ,region_name) from common_regions where region_id = cmw.region_id) region_name, cmw.distributor_id,"+
				    " (select concat(distributor_id, '-' ,name) from common_distributors where distributor_id = cmw.distributor_id) distributor_name,"+
				    " count(outlet_id) no_outlets,"+
				    " (select min(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 2 and company_id = 2 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) min_coke,"+
					" (select max(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 2 and company_id = 2 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) max_coke,"+
					" round((select avg(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 2 and company_id = 2 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)),0) avg_coke,"+
				    " (select min(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 2 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) min_pepsi,"+
					" (select max(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 2 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) max_pepsi,"+
					" round((select avg(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 2 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)),0) avg_pepsi,"+
				    " (select min(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 2 and company_id = 3 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) min_gourmet,"+
					" (select max(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 2 and company_id = 3 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) max_gourmet,"+
					" round((select avg(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 2 and company_id = 3 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)),0) avg_gourmet,"+
					" (select count(distinct cmwi.id) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 2 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1) and cmwr.source_id = 1) count_market, " +
					" (select count(distinct cmwi.id) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 2 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1) and cmwr.source_id = 2) count_distributor " +					
					" FROM crm_market_watch cmw"+ 
					" where cmw.created_on between curdate() and from_days(to_days(curdate())+1) and cmw.id in (select id from crm_market_watch_rates where package_id = 2 and rate != 0) group by distributor_id");
				while(rs.next()){
					
					double wholesale_percentage = 0;
					
					if ((rs.getInt("count_distributor")+rs.getDouble("count_market")) != 0){
						wholesale_percentage = (rs.getDouble("count_market") / (rs.getInt("count_distributor")+rs.getDouble("count_market")))*100;
					}
					
					double min_coke = rs.getDouble("min_pepsi")-rs.getDouble("min_coke");
					double max_coke = rs.getDouble("max_pepsi")-rs.getDouble("max_coke");
					double avg_coke = rs.getDouble("avg_pepsi")-rs.getDouble("avg_coke");
					
					double min_gourmet = rs.getDouble("min_pepsi")-rs.getDouble("min_gourmet");
					double max_gourmet = rs.getDouble("max_pepsi")-rs.getDouble("max_gourmet");
					double avg_gourmet = rs.getDouble("avg_pepsi")-rs.getDouble("avg_gourmet");
					
					String difference_min_coke = "";
					String difference_max_coke = "";
					String difference_avg_coke = "";
					
					if (rs.getDouble("min_coke") != 0 || rs.getDouble("max_coke") != 0){
					
						if (min_coke > 0){
							difference_min_coke = "Rs."+Math.round(min_coke) +" cheaper";
						}else if (min_coke < 0){
							min_coke = min_coke * -1;
							difference_min_coke = "Rs."+Math.round(min_coke) +" expensive";
						}else{
							difference_min_coke = "No Difference";
						}
						
						if (max_coke > 0){
							difference_max_coke = "Rs."+Math.round(max_coke) +" cheaper";
						}else if (max_coke < 0){
							max_coke = max_coke * -1;
							difference_max_coke = "Rs."+Math.round(max_coke) +" expensive";
						}else{
							difference_max_coke = "No Difference";
						}
						
						if (avg_coke > 0){
							difference_avg_coke = "Rs."+Math.round(avg_coke) +" cheaper";
						}else if (avg_coke < 0){
							avg_coke = avg_coke * -1;
							difference_avg_coke = "Rs."+Math.round(avg_coke) +" expensive";
						}else{
							difference_avg_coke = "No Difference";
						}
					
					}

					String difference_min_gourmet = "";
					String difference_max_gourmet = "";
					String difference_avg_gourmet = "";
					
					if (rs.getDouble("min_gourmet") != 0 || rs.getDouble("max_gourmet") != 0){
					
					if (min_gourmet > 0){
						difference_min_gourmet = "Rs."+Math.round(min_gourmet) +" cheaper";
					}else{
						min_gourmet = min_gourmet * -1;
						difference_min_gourmet = "Rs."+Math.round(min_gourmet) +" expensive";
					}
					
					if (max_gourmet > 0){
						difference_max_gourmet = "Rs."+Math.round(max_gourmet) +" cheaper";
					}else{
						max_gourmet = max_gourmet * -1;
						difference_max_gourmet = "Rs."+Math.round(max_gourmet) +" expensive";
					}
					
					if (avg_gourmet > 0){
						difference_avg_gourmet = "Rs."+Math.round(avg_gourmet) +" cheaper";
					}else{
						avg_gourmet = avg_gourmet * -1;
						difference_avg_gourmet = "Rs."+Math.round(avg_gourmet) +" expensive";
					}
					
					}
					
					
					html += "<tr>";
					html += "<td style='text-align: left;border-bottom: thin dotted #D5D6DE;'>"+Utilities.truncateStringToMax(rs.getString("region_name"),11)+"</td>";
					html += "<td style='text-align: left;border-bottom: thin dotted #D5D6DE;'>"+Utilities.truncateStringToMax(rs.getString("distributor_name"),18)+"</td>";
					html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'>"+rs.getInt("no_outlets")+"</td>";
					html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'>"+Math.round(wholesale_percentage)+"%</td>";						
					html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table style='width: 100%; text-align: center'><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+Math.round(rs.getDouble("min_coke"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+Math.round(rs.getDouble("max_coke"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Avg</td><td style='abackground-color: #F5CEDE;'>"+Math.round(rs.getDouble("avg_coke"))+"</td></tr></table></td>";
					html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table style='width: 100%; text-align: center'><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+Math.round(rs.getDouble("min_pepsi"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+Math.round(rs.getDouble("max_pepsi"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Avg</td><td style='abackground-color: #F5CEDE;'>"+Math.round(rs.getDouble("avg_pepsi"))+"</td></tr></table></td>";
					html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table style='width: 100%; text-align: center'><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+Math.round(rs.getDouble("min_gourmet"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+Math.round(rs.getDouble("max_gourmet"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Avg</td><td style='abackground-color: #F5CEDE;'>"+Math.round(rs.getDouble("avg_gourmet"))+"</td></tr></table></td>";
					html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table style='width: 100%; text-align: center'><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+difference_min_coke+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+difference_max_coke+"</td></tr><tr><td style='background-color: #F5CEDE;'>Avg</td><td style='background-color: #F5CEDE;'>"+difference_avg_coke+"</td></tr></table></td>";
					html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table style='width: 100%; text-align: center'><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+difference_min_gourmet+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+difference_max_gourmet+"</td></tr><tr><td style='background-color: #F5CEDE;'>Avg</td><td style='background-color: #F5CEDE;'>"+difference_avg_gourmet+"</td></tr></table></td>";
					html += "</tr>";
					
				}
				
				html += "<tr>";
				html += "<td>&nbsp;</td>";
				html += "</tr>";
				
				
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='7'>1000ML PET</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;' colspan='2'>Difference</td>";
				html += "</tr>";
				html += "<tr>";
					html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Region</td>";
					html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Distributor</td>";
					html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Outlets</td>";
					html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Wholesale</td>";
					html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Coke</td>";
					html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Pepsi</td>";
					html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Cross Franchise</td>";
					html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Coke</td>";
					html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Cross Franchise</td>";
				html += "</tr>";
				
				rs = s.executeQuery("SELECT "+
						" cmw.region_id,"+ 
						" (select concat(region_short_name, ' ' ,region_name) from common_regions where region_id = cmw.region_id) region_name, cmw.distributor_id,"+
					    " (select concat(distributor_id, '-' ,name) from common_distributors where distributor_id = cmw.distributor_id) distributor_name,"+
					    " count(outlet_id) no_outlets,"+
					    " (select min(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 3 and company_id = 2 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) min_coke,"+
						" (select max(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 3 and company_id = 2 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) max_coke,"+    
						" round((select avg(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 3 and company_id = 2 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)),0) avg_coke,"+
					    " (select min(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 3 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) min_pepsi,"+
						" (select max(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 3 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) max_pepsi,"+
						" round((select avg(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 3 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)),0) avg_pepsi,"+
					    " (select min(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 3 and company_id = 3 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) min_gourmet,"+
						" (select max(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 3 and company_id = 3 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) max_gourmet, "+
						" round((select avg(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 3 and company_id = 3 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)),0) avg_gourmet, "+
						" (select count(distinct cmwi.id) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 3 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1) and cmwr.source_id = 1) count_market, " +
						" (select count(distinct cmwi.id) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 3 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1) and cmwr.source_id = 2) count_distributor " +						
						" FROM crm_market_watch cmw"+ 
						" where cmw.created_on between curdate() and from_days(to_days(curdate())+1) and cmw.id in (select id from crm_market_watch_rates where package_id = 3 and rate != 0) group by distributor_id");
					while(rs.next()){
						
						
						double wholesale_percentage = 0;
						
						if ((rs.getInt("count_distributor")+rs.getDouble("count_market")) != 0){
							wholesale_percentage = (rs.getDouble("count_market") / (rs.getInt("count_distributor")+rs.getDouble("count_market")))*100;
						}
						
						double min_coke = rs.getDouble("min_pepsi")-rs.getDouble("min_coke");
						double max_coke = rs.getDouble("max_pepsi")-rs.getDouble("max_coke");
						double avg_coke = rs.getDouble("avg_pepsi")-rs.getDouble("avg_coke");
						
						double min_gourmet = rs.getDouble("min_pepsi")-rs.getDouble("min_gourmet");
						double max_gourmet = rs.getDouble("max_pepsi")-rs.getDouble("max_gourmet");
						double avg_gourmet = rs.getDouble("avg_pepsi")-rs.getDouble("avg_gourmet");
						
						String difference_min_coke = "";
						String difference_max_coke = "";
						String difference_avg_coke = "";
						
						if (rs.getDouble("min_coke") != 0 || rs.getDouble("max_coke") != 0){
						
							if (min_coke > 0){
								difference_min_coke = "Rs."+Math.round(min_coke) +" cheaper";
							}else if (min_coke < 0){
								min_coke = min_coke * -1;
								difference_min_coke = "Rs."+Math.round(min_coke) +" expensive";
							}else{
								difference_min_coke = "No Difference";
							}
							
							if (max_coke > 0){
								difference_max_coke = "Rs."+Math.round(max_coke) +" cheaper";
							}else if (max_coke < 0){
								max_coke = max_coke * -1;
								difference_max_coke = "Rs."+Math.round(max_coke) +" expensive";
							}else{
								difference_max_coke = "No Difference";
							}
							
							if (avg_coke > 0){
								difference_avg_coke = "Rs."+Math.round(avg_coke) +" cheaper";
							}else if (avg_coke < 0){
								avg_coke = avg_coke * -1;
								difference_avg_coke = "Rs."+Math.round(avg_coke) +" expensive";
							}else{
								difference_avg_coke = "No Difference";
							}
						
						}

						String difference_min_gourmet = "";
						String difference_max_gourmet = "";
						String difference_avg_gourmet = "";
						
						if (rs.getDouble("min_gourmet") != 0 || rs.getDouble("max_gourmet") != 0){
						
						if (min_gourmet > 0){
							difference_min_gourmet = "Rs."+Math.round(min_gourmet) +" cheaper";
						}else{
							min_gourmet = min_gourmet * -1;
							difference_min_gourmet = "Rs."+Math.round(min_gourmet) +" expensive";
						}
						
						if (max_gourmet > 0){
							difference_max_gourmet = "Rs."+Math.round(max_gourmet) +" cheaper";
						}else{
							max_gourmet = max_gourmet * -1;
							difference_max_gourmet = "Rs."+Math.round(max_gourmet) +" expensive";
						}
						
						if (avg_gourmet > 0){
							difference_avg_gourmet = "Rs."+Math.round(avg_gourmet) +" cheaper";
						}else{
							avg_gourmet = avg_gourmet * -1;
							difference_avg_gourmet = "Rs."+Math.round(avg_gourmet) +" expensive";
						}
						
						}
						
						
						html += "<tr>";
						html += "<td style='text-align: left;border-bottom: thin dotted #D5D6DE;'>"+Utilities.truncateStringToMax(rs.getString("region_name"),11)+"</td>";
						html += "<td style='text-align: left;border-bottom: thin dotted #D5D6DE;'>"+Utilities.truncateStringToMax(rs.getString("distributor_name"),18)+"</td>";
						html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'>"+rs.getInt("no_outlets")+"</td>";
						html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'>"+Math.round(wholesale_percentage)+"%</td>";						
						html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table style='width: 100%; text-align: center'><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+Math.round(rs.getDouble("min_coke"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+Math.round(rs.getDouble("max_coke"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Avg</td><td style='abackground-color: #F5CEDE;'>"+Math.round(rs.getDouble("avg_coke"))+"</td></tr></table></td>";
						html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table style='width: 100%; text-align: center'><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+Math.round(rs.getDouble("min_pepsi"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+Math.round(rs.getDouble("max_pepsi"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Avg</td><td style='abackground-color: #F5CEDE;'>"+Math.round(rs.getDouble("avg_pepsi"))+"</td></tr></table></td>";
						html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table style='width: 100%; text-align: center'><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+Math.round(rs.getDouble("min_gourmet"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+Math.round(rs.getDouble("max_gourmet"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Avg</td><td style='abackground-color: #F5CEDE;'>"+Math.round(rs.getDouble("avg_gourmet"))+"</td></tr></table></td>";
						html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table style='width: 100%; text-align: center'><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+difference_min_coke+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+difference_max_coke+"</td></tr><tr><td style='background-color: #F5CEDE;'>Avg</td><td style='background-color: #F5CEDE;'>"+difference_avg_coke+"</td></tr></table></td>";
						html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table style='width: 100%; text-align: center'><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+difference_min_gourmet+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+difference_max_gourmet+"</td></tr><tr><td style='background-color: #F5CEDE;'>Avg</td><td style='background-color: #F5CEDE;'>"+difference_avg_gourmet+"</td></tr></table></td>";
						html += "</tr>";
						
						
						
						
					}

					html += "<tr>";
					html += "<td>&nbsp;</td>";
					html += "</tr>";
					
					html += "<tr>";
					html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='7'>2250ML PET</td>";
					html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;' colspan='2'>Difference</td>";
					html += "</tr>";
					html += "<tr>";
					html += "<td style='width: 100px;background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Region</td>";
					html += "<td style='width: 200px;background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Distributor</td>";
					html += "<td style='width: 80px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Outlets</td>";
					html += "<td style='width: 80px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Wholesale</td>";
					html += "<td style='width: 80px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Coke</td>";
					html += "<td style='width: 80px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Pepsi</td>";
					html += "<td style='width: 80px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Cross Franchise</td>";
					html += "<td style='width: 155px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Coke</td>";
					html += "<td style='width: 155px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Cross Franchise</td>";
				html += "</tr>";
					
					rs = s.executeQuery("SELECT "+
							" cmw.region_id,"+ 
							" (select concat(region_short_name, ' ' ,region_name) from common_regions where region_id = cmw.region_id) region_name, cmw.distributor_id,"+
						    " (select concat(distributor_id, '-' ,name) from common_distributors where distributor_id = cmw.distributor_id) distributor_name,"+
						    " count(outlet_id) no_outlets,"+
						    " (select min(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 5 and company_id = 2 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) min_coke,"+
							" (select max(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 5 and company_id = 2 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) max_coke,"+    
							" round((select avg(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 5 and company_id = 2 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)),0) avg_coke,"+
						    " (select min(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 5 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) min_pepsi,"+
							" (select max(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 5 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) max_pepsi,"+
							" round((select avg(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 5 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)),0) avg_pepsi,"+
						    " (select min(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 5 and company_id = 3 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) min_gourmet,"+
							" (select max(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 5 and company_id = 3 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) max_gourmet, "+
							" round((select avg(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 5 and company_id = 3 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)),0) avg_gourmet, "+
							" (select count(distinct cmwi.id) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 5 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1) and cmwr.source_id = 1) count_market, " +
							" (select count(distinct cmwi.id) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 5 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1) and cmwr.source_id = 2) count_distributor " +							
							" FROM crm_market_watch cmw"+ 
							" where cmw.created_on between curdate() and from_days(to_days(curdate())+1) and cmw.id in (select id from crm_market_watch_rates where package_id = 5 and rate != 0) group by distributor_id");
						while(rs.next()){
							
							
							double wholesale_percentage = 0;
							
							if ((rs.getInt("count_distributor")+rs.getDouble("count_market")) != 0){
								wholesale_percentage = (rs.getDouble("count_market") / (rs.getInt("count_distributor")+rs.getDouble("count_market")))*100;
							}
							
							double min_coke = rs.getDouble("min_pepsi")-rs.getDouble("min_coke");
							double max_coke = rs.getDouble("max_pepsi")-rs.getDouble("max_coke");
							double avg_coke = rs.getDouble("avg_pepsi")-rs.getDouble("avg_coke");
							
							double min_gourmet = rs.getDouble("min_pepsi")-rs.getDouble("min_gourmet");
							double max_gourmet = rs.getDouble("max_pepsi")-rs.getDouble("max_gourmet");
							double avg_gourmet = rs.getDouble("avg_pepsi")-rs.getDouble("avg_gourmet");
							
							String difference_min_coke = "";
							String difference_max_coke = "";
							String difference_avg_coke = "";
							
							if (rs.getDouble("avg_pepsi") != 0){
								
								if (rs.getDouble("min_coke") != 0 || rs.getDouble("max_coke") != 0){
								
									if (min_coke > 0){
										difference_min_coke = "Rs."+Math.round(min_coke) +" cheaper";
									}else if (min_coke < 0){
										min_coke = min_coke * -1;
										difference_min_coke = "Rs."+Math.round(min_coke) +" expensive";
									}else{
										difference_min_coke = "No Difference";
									}
									
									if (max_coke > 0){
										difference_max_coke = "Rs."+Math.round(max_coke) +" cheaper";
									}else if (max_coke < 0){
										max_coke = max_coke * -1;
										difference_max_coke = "Rs."+Math.round(max_coke) +" expensive";
									}else{
										difference_max_coke = "No Difference";
									}
									
									if (avg_coke > 0){
										difference_avg_coke = "Rs."+Math.round(avg_coke) +" cheaper";
									}else if (avg_coke < 0){
										avg_coke = avg_coke * -1;
										difference_avg_coke = "Rs."+Math.round(avg_coke) +" expensive";
									}else{
										difference_avg_coke = "No Difference";
									}
								
								}
							
							}
							String difference_min_gourmet = "";
							String difference_max_gourmet = "";
							String difference_avg_gourmet = "";
							
							if (rs.getDouble("avg_pepsi") != 0){
							
								if (rs.getDouble("min_gourmet") != 0 || rs.getDouble("max_gourmet") != 0){
								
								if (min_gourmet > 0){
									difference_min_gourmet = "Rs."+Math.round(min_gourmet) +" cheaper";
								}else{
									min_gourmet = min_gourmet * -1;
									difference_min_gourmet = "Rs."+Math.round(min_gourmet) +" expensive";
								}
								
								if (max_gourmet > 0){
									difference_max_gourmet = "Rs."+Math.round(max_gourmet) +" cheaper";
								}else{
									max_gourmet = max_gourmet * -1;
									difference_max_gourmet = "Rs."+Math.round(max_gourmet) +" expensive";
								}
								
								if (avg_gourmet > 0){
									difference_avg_gourmet = "Rs."+Math.round(avg_gourmet) +" cheaper";
								}else{
									avg_gourmet = avg_gourmet * -1;
									difference_avg_gourmet = "Rs."+Math.round(avg_gourmet) +" expensive";
								}
								
								}
							
							}
							
							html += "<tr>";
							html += "<td style='text-align: left;border-bottom: thin dotted #D5D6DE;'>"+Utilities.truncateStringToMax(rs.getString("region_name"),11)+"</td>";
							html += "<td style='text-align: left;border-bottom: thin dotted #D5D6DE;'>"+Utilities.truncateStringToMax(rs.getString("distributor_name"),18)+"</td>";
							html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'>"+rs.getInt("no_outlets")+"</td>";
							html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'>"+Math.round(wholesale_percentage)+"%</td>";						
							html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table style='width: 100%; text-align: center'><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+Math.round(rs.getDouble("min_coke"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+Math.round(rs.getDouble("max_coke"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Avg</td><td style='abackground-color: #F5CEDE;'>"+Math.round(rs.getDouble("avg_coke"))+"</td></tr></table></td>";
							html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table style='width: 100%; text-align: center'><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+Math.round(rs.getDouble("min_pepsi"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+Math.round(rs.getDouble("max_pepsi"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Avg</td><td style='abackground-color: #F5CEDE;'>"+Math.round(rs.getDouble("avg_pepsi"))+"</td></tr></table></td>";
							html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table style='width: 100%; text-align: center'><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+Math.round(rs.getDouble("min_gourmet"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+Math.round(rs.getDouble("max_gourmet"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Avg</td><td style='abackground-color: #F5CEDE;'>"+Math.round(rs.getDouble("avg_gourmet"))+"</td></tr></table></td>";
							html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table style='width: 100%; text-align: center'><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+difference_min_coke+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+difference_max_coke+"</td></tr><tr><td style='background-color: #F5CEDE;'>Avg</td><td style='background-color: #F5CEDE;'>"+difference_avg_coke+"</td></tr></table></td>";
							if (difference_avg_gourmet.length() > 0){
								html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table style='width: 100%; text-align: center'><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+difference_min_gourmet+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+difference_max_gourmet+"</td></tr><tr><td style='background-color: #F5CEDE;'>Avg</td><td style='background-color: #F5CEDE;'>"+difference_avg_gourmet+"</td></tr></table></td>";
							}else{
								html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'></td>";
							}
							html += "</tr>";
							
							
							
							
						}

						html += "<tr>";
						html += "<td colspan='9'>&nbsp;</td>";
						html += "</tr>";
					
					/*
					html += "<tr>";
					html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='8'>2250ML PET*</td>";
					html += "</tr>";
					html += "<tr>";
						html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Region</td>";
						html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Distributor</td>";
						html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'># of Outlets</td>";
						html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Coke</td>";
						html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Pepsi</td>";
						html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Gourmet</td>";
						html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Difference Coke</td>";
						html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Difference Gourmet</td>";
					html += "</tr>";
					
					rs = s.executeQuery("SELECT "+
							" cmw.region_id,"+ 
							" (select concat(region_short_name, ' ' ,region_name) from common_regions where region_id = cmw.region_id) region_name, cmw.distributor_id,"+
						    " (select concat(distributor_id, '-' ,name) from common_distributors where distributor_id = cmw.distributor_id) distributor_name,"+
						    " count(outlet_id) no_outlets,"+
						    " (select min(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 5 and company_id = 2 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) min_coke,"+
							" (select max(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 5 and company_id = 2 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) max_coke,"+    
						    " (select min(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 5 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) min_pepsi,"+
							" (select max(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 5 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) max_pepsi,"+
						    " (select min(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 5 and company_id = 3 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) min_gourmet,"+
							" (select max(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 5 and company_id = 3 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) max_gourmet"+
							" FROM crm_market_watch cmw"+ 
							" where cmw.created_on between curdate() and from_days(to_days(curdate())+1) and cmw.id in (select id from crm_market_watch_rates where package_id = 5) group by distributor_id");
						while(rs.next()){
							
							double min_coke = rs.getDouble("min_pepsi")-rs.getDouble("min_coke");
							double max_coke = rs.getDouble("max_pepsi")-rs.getDouble("max_coke");
							
							double min_gourmet = rs.getDouble("min_pepsi")-rs.getDouble("min_gourmet");
							double max_gourmet = rs.getDouble("max_pepsi")-rs.getDouble("max_gourmet");
							
							String difference_min_coke = "";
							String difference_max_coke = "";
							
							
							if ((rs.getDouble("min_coke") != 0 || rs.getDouble("max_coke") != 0) && (rs.getDouble("min_pepsi") != 0 || rs.getDouble("max_pepsi") != 0)){
							
								if (min_coke > 0){
									difference_min_coke = "Rs."+Math.round(min_coke) +" cheaper";
								}else{
									min_coke = min_coke * -1;
									difference_min_coke = "Rs."+Math.round(min_coke) +" expensive";
								}
								
								if (max_coke > 0){
									difference_max_coke = "Rs."+Math.round(max_coke) +" cheaper";
								}else if (max_coke < 0){
									max_coke = max_coke * -1;
									difference_max_coke = "Rs."+Math.round(max_coke) +" expensive";
								}else{
									difference_max_coke = "No Difference";
								}
							
							}

							String difference_min_gourmet = "";
							String difference_max_gourmet = "";
							
							
							if (rs.getDouble("min_gourmet") != 0 || rs.getDouble("max_gourmet") != 0){
							
							if (min_gourmet > 0){
								difference_min_gourmet = "Rs."+Math.round(min_gourmet) +" cheaper";
							}else{
								min_gourmet = min_gourmet * -1;
								difference_min_gourmet = "Rs."+Math.round(min_gourmet) +" expensive";
							}
							
							if (max_gourmet > 0){
								difference_max_gourmet = "Rs."+Math.round(max_gourmet) +" cheaper";
							}else{
								max_gourmet = max_gourmet * -1;
								difference_max_gourmet = "Rs."+Math.round(max_gourmet) +" expensive";
							}
							
							}
							
							
							html += "<tr>";
							html += "<td style='text-align: left;border-bottom: thin dotted #D5D6DE;'>"+Utilities.truncateStringToMax(rs.getString("region_name"),11)+"</td>";
							html += "<td style='text-align: left;border-bottom: thin dotted #D5D6DE;'>"+Utilities.truncateStringToMax(rs.getString("distributor_name"),18)+"</td>";
							html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'>"+rs.getInt("no_outlets")+"</td>";
							html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'>"+Math.round(rs.getDouble("min_coke"))+"-"+Math.round(rs.getDouble("max_coke"))+"</td>";
							html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'>"+Math.round(rs.getDouble("min_pepsi"))+"-"+Math.round(rs.getDouble("max_pepsi"))+"</td>";
							html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'>"+Math.round(rs.getDouble("min_gourmet"))+"-"+Math.round(rs.getDouble("max_gourmet"))+"</td>";
							html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+difference_min_coke+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+difference_max_coke+"</td></tr></table></td>";
							html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+difference_min_gourmet+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+difference_max_gourmet+"</td></tr></table></td>";
							html += "</tr>";
							
							
							
							
						}
					
						
						html += "<tr>";
						html += "<td>&nbsp;</td>";
						html += "</tr>";
						
					html += "<tr>";
					html += "<td colspan=8>* 2250ML SKU of Coke contains 6 bottles. The rates has been adjusted for 4 bottles to compare with Pepsi.&nbsp;</td>";
					html += "</tr>";
					*/
						
						html += "<tr>";
						html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='7'>250ML SSRB</td>";
						html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;' colspan='2'>Difference</td>";
						html += "</tr>";
						html += "<tr>";
						html += "<td style='width: 100px;background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Region</td>";
						html += "<td style='width: 200px;background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Distributor</td>";
						html += "<td style='width: 80px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Outlets</td>";
						html += "<td style='width: 80px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Wholesale</td>";
						html += "<td style='width: 80px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Coke</td>";
						html += "<td style='width: 80px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Pepsi</td>";
						html += "<td style='width: 80px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Cross Franchise</td>";
						html += "<td style='width: 155px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Coke</td>";
						html += "<td style='width: 155px;background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Cross Franchise</td>";
					html += "</tr>";
						
						rs = s.executeQuery("SELECT "+
								" cmw.region_id,"+ 
								" (select concat(region_short_name, ' ' ,region_name) from common_regions where region_id = cmw.region_id) region_name, cmw.distributor_id,"+
							    " (select concat(distributor_id, '-' ,name) from common_distributors where distributor_id = cmw.distributor_id) distributor_name,"+
							    " count(outlet_id) no_outlets,"+
							    " (select min(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 11 and company_id = 2 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) min_coke,"+
								" (select max(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 11 and company_id = 2 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) max_coke,"+    
								" round((select avg(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 11 and company_id = 2 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)),0) avg_coke,"+
							    " (select min(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 11 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) min_pepsi,"+
								" (select max(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 11 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) max_pepsi,"+
								" round((select avg(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 11 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)),0) avg_pepsi,"+
							    " (select min(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 11 and company_id = 3 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) min_gourmet,"+
								" (select max(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 11 and company_id = 3 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)) max_gourmet, "+
								" round((select avg(rate) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 11 and company_id = 3 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1)),0) avg_gourmet, "+
								" (select count(distinct cmwi.id) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 11 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1) and cmwr.source_id = 1) count_market, " +
								" (select count(distinct cmwi.id) from crm_market_watch_rates cmwr join crm_market_watch cmwi on cmwr.id = cmwi.id where cmwi.distributor_id = cmw.distributor_id and package_id = 11 and company_id = 1 and rate != 0 and cmwi.created_on between curdate() and from_days(to_days(curdate())+1) and cmwr.source_id = 2) count_distributor " +							
								" FROM crm_market_watch cmw"+ 
								" where cmw.created_on between curdate() and from_days(to_days(curdate())+1) and cmw.id in (select id from crm_market_watch_rates where package_id = 11 and rate != 0) group by distributor_id");
							while(rs.next()){
								
								
								double wholesale_percentage = 0;
								
								if ((rs.getInt("count_distributor")+rs.getDouble("count_market")) != 0){
									wholesale_percentage = (rs.getDouble("count_market") / (rs.getInt("count_distributor")+rs.getDouble("count_market")))*100;
								}
								
								double min_coke = rs.getDouble("min_pepsi")-rs.getDouble("min_coke");
								double max_coke = rs.getDouble("max_pepsi")-rs.getDouble("max_coke");
								double avg_coke = rs.getDouble("avg_pepsi")-rs.getDouble("avg_coke");
								
								double min_gourmet = rs.getDouble("min_pepsi")-rs.getDouble("min_gourmet");
								double max_gourmet = rs.getDouble("max_pepsi")-rs.getDouble("max_gourmet");
								double avg_gourmet = rs.getDouble("avg_pepsi")-rs.getDouble("avg_gourmet");
								
								String difference_min_coke = "";
								String difference_max_coke = "";
								String difference_avg_coke = "";
								
								
								if (rs.getDouble("avg_pepsi") != 0){
								
								if (rs.getDouble("min_coke") != 0 || rs.getDouble("max_coke") != 0){
								
									if (min_coke > 0){
										difference_min_coke = "Rs."+Math.round(min_coke) +" cheaper";
									}else if (min_coke < 0){
										min_coke = min_coke * -1;
										difference_min_coke = "Rs."+Math.round(min_coke) +" expensive";
									}else{
										difference_min_coke = "No Difference";
									}
									
									if (max_coke > 0){
										difference_max_coke = "Rs."+Math.round(max_coke) +" cheaper";
									}else if (max_coke < 0){
										max_coke = max_coke * -1;
										difference_max_coke = "Rs."+Math.round(max_coke) +" expensive";
									}else{
										difference_max_coke = "No Difference";
									}
									
									if (avg_coke > 0){
										difference_avg_coke = "Rs."+Math.round(avg_coke) +" cheaper";
									}else if (avg_coke < 0){
										avg_coke = avg_coke * -1;
										difference_avg_coke = "Rs."+Math.round(avg_coke) +" expensive";
									}else{
										difference_avg_coke = "No Difference";
									}
								
								}
								
								}
								String difference_min_gourmet = "";
								String difference_max_gourmet = "";
								String difference_avg_gourmet = "";
								
								if (rs.getDouble("avg_pepsi") != 0){
								if (rs.getDouble("min_gourmet") != 0 || rs.getDouble("max_gourmet") != 0){
								
								if (min_gourmet > 0){
									difference_min_gourmet = "Rs."+Math.round(min_gourmet) +" cheaper";
								}else{
									min_gourmet = min_gourmet * -1;
									difference_min_gourmet = "Rs."+Math.round(min_gourmet) +" expensive";
								}
								
								if (max_gourmet > 0){
									difference_max_gourmet = "Rs."+Math.round(max_gourmet) +" cheaper";
								}else{
									max_gourmet = max_gourmet * -1;
									difference_max_gourmet = "Rs."+Math.round(max_gourmet) +" expensive";
								}
								
								if (avg_gourmet > 0){
									difference_avg_gourmet = "Rs."+Math.round(avg_gourmet) +" cheaper";
								}else{
									avg_gourmet = avg_gourmet * -1;
									difference_avg_gourmet = "Rs."+Math.round(avg_gourmet) +" expensive";
								}
								
								}
								}
								
								html += "<tr>";
								html += "<td style='text-align: left;border-bottom: thin dotted #D5D6DE;'>"+Utilities.truncateStringToMax(rs.getString("region_name"),11)+"</td>";
								html += "<td style='text-align: left;border-bottom: thin dotted #D5D6DE;'>"+Utilities.truncateStringToMax(rs.getString("distributor_name"),18)+"</td>";
								html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'>"+rs.getInt("no_outlets")+"</td>";
								html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'>"+Math.round(wholesale_percentage)+"%</td>";						
								html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table style='width: 100%; text-align: center'><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+Math.round(rs.getDouble("min_coke"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+Math.round(rs.getDouble("max_coke"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Avg</td><td style='abackground-color: #F5CEDE;'>"+Math.round(rs.getDouble("avg_coke"))+"</td></tr></table></td>";
								html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table style='width: 100%; text-align: center'><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+Math.round(rs.getDouble("min_pepsi"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+Math.round(rs.getDouble("max_pepsi"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Avg</td><td style='abackground-color: #F5CEDE;'>"+Math.round(rs.getDouble("avg_pepsi"))+"</td></tr></table></td>";
								html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table style='width: 100%; text-align: center'><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+Math.round(rs.getDouble("min_gourmet"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+Math.round(rs.getDouble("max_gourmet"))+"</td></tr><tr><td style='background-color: #EDEFF2;'>Avg</td><td style='abackground-color: #F5CEDE;'>"+Math.round(rs.getDouble("avg_gourmet"))+"</td></tr></table></td>";
								html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table style='width: 100%; text-align: center'><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+difference_min_coke+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+difference_max_coke+"</td></tr><tr><td style='background-color: #F5CEDE;'>Avg</td><td style='background-color: #F5CEDE;'>"+difference_avg_coke+"</td></tr></table></td>";
								if (difference_avg_gourmet.length() > 0){
									html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'><table style='width: 100%; text-align: center'><tr><td style='background-color: #EDEFF2;'>Min</td><td>"+difference_min_gourmet+"</td></tr><tr><td style='background-color: #EDEFF2;'>Max</td><td>"+difference_max_gourmet+"</td></tr><tr><td style='background-color: #F5CEDE;'>Avg</td><td style='background-color: #F5CEDE;'>"+difference_avg_gourmet+"</td></tr></table></td>";
								}else{
									html += "<td style='text-align: center;border-bottom: thin dotted #D5D6DE;'></td>";
								}
								html += "</tr>";
								
								
								
								
							}

							html += "<tr>";
							html += "<td colspan='9'>&nbsp;</td>";
							html += "</tr>";
						
					/*html += "<tr>";
					html += "<td colspan=9>* 2250ML SKU of Coke contains 6 bottles. The rates has been adjusted for 4 bottles to compare with Pepsi.&nbsp;</td>";
					html += "</tr>";*/

					html += "<tr>";
					html += "<td colspan=9>* All figures are based on net rates after deducting relevant promotions.</td>";
					html += "</tr>";
					
				
		html += "</table>";
		
		html += "</body>";
		
		
		html += "</html>";
		
		this.HTMLMessage = html;
	}

	
}
