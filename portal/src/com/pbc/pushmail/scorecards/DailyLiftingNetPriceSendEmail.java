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

public class DailyLiftingNetPriceSendEmail {
	
	public static final String filename_net_trade_price = "NSR_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	
	
public static void main(String[] args) {
		
		try {
			
			//PJPKPIsExcel PJPKPIs = new com.pbc.pushmail.scorecards.PJPKPIsExcel();
			//NetSalesRevenueExcel obj = new NetSalesRevenueExcel();
			//obj.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_net_trade_price, 1804);
			
			
			
			
			
			
			
			Utilities.sendPBCHTMLEmail(new String[]{"fahad.khalid@pbc.com.pk"},new String[]{""}, null, "Daily Net Price | "+Utilities.getDisplayDateFormat(Utilities.getDateByDays(-1)), getHTMLMessage(), null);
			//Utilities.sendPBCHTMLEmail(new String[]{"jazeb@pbc.com.pk"},new String[]{"anas.wahab@pbc.com.pk"}, null, "Daily Net Price | "+Utilities.getDisplayDateFormat(Utilities.getDateByDays(-1)), getHTMLMessage(), null);
			//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "Daily Net Price | "+Utilities.getDisplayDateFormat(Utilities.getDateByDays(-1)), getHTMLMessage(), null);
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}

	public static String getHTMLMessage() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		
		Date YesterdayDate = Utilities.getDateByDays(-1);
		
		Date MonthStartDate = Utilities.getStartDateByDate(YesterdayDate);
		
		Datasource ds = new Datasource();
		ds.createConnectionToReplica();
		
		
		Statement s = ds.createStatement();
		Statement s2 = ds.createStatement();
		Statement s3 = ds.createStatement();
		
		
		String html = "<html>";
		html += "<body style='font-size:12px; font-family: Arial;'>";
		html += "<p><b>Daily Net Price</b><br>"+Utilities.getDisplayDateFormat(YesterdayDate)+"</p>";
		
		html += "<table style='font-size:12px; font-family: Arial; width: 600px; border-collapse: collapse;' border=1>";
		
		html += "<tr>";
		html += "<td style='width: 150px;'></td>";
		html += "<td style='width: 150px; text-align: center'>Min</td>";
		html += "<td style='width: 150px; text-align: center'>Max</td>";
		html += "<td style='width: 150px; text-align: center'>Avg Today</td>";
		html += "<td style='width: 150px; text-align: center'>Avg MTD</td>";
		html += "<tr>";
		
		s.executeUpdate("SET SESSION group_concat_max_len = 1000000");
        String FOCInvoiceIDs = "";
        ResultSet rs11 = s.executeQuery("select group_concat(invoice_no) from inventory_delivery_note idn where idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
        if(rs11.first()){
        	FOCInvoiceIDs = rs11.getString(1);
        }
		
        
        
		ResultSet rs = s.executeQuery("SELECT id, label FROM inventory_products_lrb_types");
		while(rs.next()){
			int TypeID = rs.getInt(1);
			String TypeLabel = rs.getString(2);
			html += "<tr><td colspan='5' style='background: #cecece'>"+TypeLabel+"</td></tr>";
			
			
			ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where is_visible = 1 and lrb_type_id="+rs.getString(1)+" and package_id not in (10,19,14,5) and product_id not in (182)");
			while(rs2.next()){
				int PackageID = rs2.getInt(1);
				String PackageLabel = rs2.getString(2);
				
				String MinCustomerID = "";
				double min = 0;
		        ResultSet rs22 = s3.executeQuery("select customer_id, (select name from common_distributors where distributor_id = customer_id) distributor_name, avg_rate from (SELECT customer_id, (sum(gross_value) - (sum(upfront_discount*-1) + sum(ifnull(free_stock,0))))/sum(quantity) avg_rate FROM peplogs.bi_percase_price_invoice where vbeln in ("+FOCInvoiceIDs+") and package_id = "+PackageID+" and product_type_id = "+TypeID+" and customer_id != 200769 group by customer_id) tab1 order by avg_rate");
		        if(rs22.first()){
		        	min = rs22.getDouble(3);
		        	MinCustomerID = rs22.getString(1);
		        	MinCustomerID = MinCustomerID + "-"+rs22.getString(2);
		        }
		        
				String MaxCustomerID = "";
				double max = 0;
		        ResultSet rs23 = s3.executeQuery("select customer_id, (select name from common_distributors where distributor_id = customer_id) distributor_name, avg_rate from (SELECT customer_id, (sum(gross_value) - (sum(upfront_discount*-1) + sum(ifnull(free_stock,0))))/sum(quantity) avg_rate FROM peplogs.bi_percase_price_invoice where vbeln in ("+FOCInvoiceIDs+") and package_id = "+PackageID+" and product_type_id = "+TypeID+" and customer_id != 200769 group by customer_id) tab1 order by avg_rate desc");
		        if(rs23.first()){
		        	max = rs23.getDouble(3);
		        	MaxCustomerID = rs23.getString(1);
		        	MaxCustomerID = MaxCustomerID + "-"+rs23.getString(2);
		        }
		        
		        //System.out.println("SELECT (sum(gross_value) - (sum(upfront_discount*-1) + sum(ifnull(free_stock,0))))/sum(quantity) avg_rate FROM peplogs.bi_percase_price_invoice where vbeln in ("+FOCInvoiceIDs+") and package_id = "+PackageID+" and product_type_id = "+TypeID+" and customer_id != 200769");
				double avg = 0;
		        ResultSet rs24 = s3.executeQuery("SELECT (sum(gross_value) - (sum(upfront_discount*-1) + sum(ifnull(free_stock,0))))/sum(quantity) avg_rate FROM peplogs.bi_percase_price_invoice where vbeln in ("+FOCInvoiceIDs+") and package_id = "+PackageID+" and product_type_id = "+TypeID+" and customer_id != 200769");
		        if(rs24.first()){
		        	avg = rs24.getDouble(1);
		        }        
				
		        double MTDavg = 0;
		        ResultSet MTDrs24 = s3.executeQuery("SELECT (sum(gross_value) - (sum(upfront_discount*-1) + sum(ifnull(free_stock,0))))/sum(quantity) avg_rate FROM peplogs.bi_percase_price_invoice where vbeln in (select invoice_no from inventory_delivery_note idn where idn.created_on between "+Utilities.getSQLDateLifting(MonthStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+") and package_id = "+PackageID+" and product_type_id = "+TypeID+" and customer_id != 200769");
		        if(MTDrs24.first()){
		        	MTDavg = MTDrs24.getDouble(1);
		        }        
		        
				html += "<tr>";
				html += "<td>"+PackageLabel+"</td>";
				html += "<td style='text-align: left;'><b>"+Utilities.getDisplayCurrencyFormatRounded(min)+"</b><br><i><font style='font-size: 10px'>"+Utilities.truncateStringToMax(MinCustomerID,20)+"</i></td>";
				html += "<td style='text-align: left;'><b>"+Utilities.getDisplayCurrencyFormatRounded(max)+"</b><br><i><font style='font-size: 10px'>"+Utilities.truncateStringToMax(MaxCustomerID,20)+"</i></td>";
				html += "<td style='text-align: left;'><b>"+Utilities.getDisplayCurrencyFormatRounded(avg)+"</b><br><font style='font-size: 10px'>&nbsp;</td>";
				html += "<td style='text-align: left;'><b>"+Utilities.getDisplayCurrencyFormatRounded(MTDavg)+"</b><br><font style='font-size: 10px'>&nbsp;</td>";
				html += "<tr>";
				
			}
			
		}
		
		html += "</table><br><br>";
		html += "<font style='font-size: 10px;'><i>* Net Price = Gross Invoice Value - Upfront Discount - Promotions</i></font>";
	
		s.close();
		ds.dropConnection();	
		
		return html;
		
	}

}
