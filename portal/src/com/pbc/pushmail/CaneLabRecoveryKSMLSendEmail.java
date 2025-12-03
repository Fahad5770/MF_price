package com.pbc.pushmail;

import java.io.FileOutputStream;
import java.io.IOException;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
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

public class CaneLabRecoveryKSMLSendEmail {
	
	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	String HTMLMessage;
	
	static Date Yesterday = getDateByDays(0);
    
	public static void main(String[] args) throws DocumentException, IOException{
		try {
			CaneLabRecoveryKSMLSendEmail pe = new CaneLabRecoveryKSMLSendEmail();
			pe.setHTMLMessage();
			
			
			
			new com.pbc.pushmail.CaneLabRecoveryKSMLPDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/CaneLabRecoveryKSML.pdf", 2262,Yesterday);
			
			
			
			Utilities.sendPBCHTMLEmail(new String[]{"omerfk@pbc.com.pk","maqsood_anwar@hotmail.com"}, new String[]{"anas.wahab@pbc.com.pk"}, null, "Cane Lab Recovery | KSML | "+Utilities.getDisplayDateFormat(Yesterday), pe.getHTMLMessage(), new String[]{"CaneLabRecoveryKSML.pdf"});
			//Utilities.sendPBCHTMLEmail(new String[]{"anas.wahab@pbc.com.pk"}, null, null, "Cane Lab Recovery | KSML | "+Utilities.getDisplayDateFormat(Yesterday), pe.getHTMLMessage(), new String[]{"CaneLabRecoveryKSML.pdf"});
			
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
	
	public CaneLabRecoveryKSMLSendEmail() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		ds.createConnectionToKSMLLab();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();
	}
	
	public static String getSQLDateLifting(java.util.Date val) {
		
		// Converts date into sql format and ignores time

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			return "date_format('" + format.format(val) + " 7:00','%Y-%m-%d %H:%i')";

		} else {

			return null;

		}

	}
	public static String getSQLDateNextLifting(java.util.Date val) {

		
		// Adds another day to Date and converts date into SQL format, ignores time
		if (val != null) {

			java.util.Date next = DateUtils.addDays(val, 1);
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			return "date_format('" + format.format(next) + " 7:00','%Y-%m-%d %H:%i')";

		} else {

			return null;

		}

	}
	public void setHTMLMessage() throws SQLException{
		
		String html = "<html>";
			html += "<body><br>";
		
			
			
			////
			
			//Graph
			
			
			
			java.util.Date MonthStartDate = getStartDateByDate(Yesterday);
			
			
			//html +="<table style='width: 900px;' cellpadding='2' cellspacing='1'>";
			//html +="<tr>";
			//html +="<td width='100%' style='text-align: left'><img src='"+Graph+"'></td>";
			//html +="</tr>";
			//html +="</table>";
			
			//html +="<br/>";
			
			html +="<table border='0' style='border-collapse: collapse; border:1px solid #dee0e1;width:600px;'>";
			html +="<tr>";
			html +="<td style='min-width: 100px;background-color:#5875cf; color:white; border:1px solid #dee0e1;text-align:center;' rowspan='2'>Zone</th>";
			html +="<td style='min-width: 100px;background-color:#5875cf;color:white; border:1px solid #dee0e1;text-align:center;' colspan='2'>Recovery</th>";
			html +="<td style='min-width: 150px;background-color:#5875cf;color:white; border:1px solid #dee0e1;text-align:center;' rowspan='2'>Circle</th>";
			html +="</tr>";
			html +="<tr>";
			html +="<td style='min-width: 100px;background-color:#5875cf;color:white; border:1px solid #dee0e1;text-align:center;'>Today</th>";
			html +="<td style='min-width: 100px;background-color:#5875cf;color:white; border:1px solid #dee0e1; text-align:center;'>MTD</th>";
			html +="</tr>";
			
			
			ResultSet rs31 = s3.executeQuery("select distinct zone from cane_sample order by zone");
			while(rs31.next()){

			double pol = 0;
			double brix = 0;

			ResultSet rs = s.executeQuery("select zone, avg(pol) pol, avg(brix) brix from cane_sample where created_on between "+getSQLDateLifting(Yesterday)+" and "+getSQLDateNextLifting(Yesterday)+" and zone = '"+rs31.getString(1)+"'");
			if(rs.first()){
				pol = rs.getDouble("pol");
				brix = rs.getDouble("brix");
			}
			double recovery = 0;
			if (pol != 0 && brix != 0){
				recovery = getRecovery(pol,brix);
			}
			
			double MTDpol =  0;
			double MTDbrix = 0;
			ResultSet rs4 = s4.executeQuery("select zone, avg(pol) pol, avg(brix) brix from cane_sample where created_on between "+getSQLDateLifting(MonthStartDate)+" and "+getSQLDateNextLifting(Yesterday)+" and zone = '"+rs31.getString(1)+"'");
			if(rs4.first()){
				MTDpol = rs4.getDouble("pol");
				MTDbrix = rs4.getDouble("brix");
			}

			double MTDrecovery = 0;
			if (MTDpol != 0 && MTDbrix != 0){
				MTDrecovery = getRecovery(MTDpol,MTDbrix);
			}
			
			String color="";
			
		if(recovery<MTDrecovery){
			color="#e79ca8";
		}else if(recovery>=MTDrecovery){
			color="#cbe9aa";
		}else{
			color="";
		}
			
			
			html +="<tr>";
			html +="<td align='center' valign='center' style='border:1px solid #c2c3c3;'>"+rs31.getString("zone")+"</td>";
			html +="<td align='center' valign='center' style='border:1px solid #c2c3c3; background-color:"+color+"'>"+getDisplayCurrencyFormatThreeDecimalFixed(recovery)+"</td>";
			html +="<td align='center' valign='center' style='border:1px solid #c2c3c3;b1ackground-color:"+color+"'>"+getDisplayCurrencyFormatThreeDecimalFixed(MTDrecovery)+"</td>";
			html +="<td style='border:1px solid #dee0e1;'><table width='100%'>";
		
			ResultSet rs2 = s2.executeQuery("select circle, avg(pol) pol, avg(brix) brix from cane_sample where created_on between "+getSQLDateLifting(Yesterday)+" and "+getSQLDateNextLifting(Yesterday)+" and zone = '"+rs.getString("zone")+"' group by circle order by circle");
			while(rs2.next()){

		
				html +="<tr>";
				html +="<td>"+rs2.getString("circle")+"</td>";
				html +="<td align=right>"+getDisplayCurrencyFormatThreeDecimalFixed(getRecovery(rs2.getDouble("pol"),rs2.getDouble("brix")))+"</td>";
				html +="</tr>";
		
		}
		
			html +="</table></td>";
		
		}
		
		

		html +="</table>";
		html +="<br><table><tr><td style='width:20px; hight: 20px; background-color:#cbe9aa'></td><td>Above MTD average</td></tr><tr><td style='width:20px; hight: 20px; background-color:#e79ca8'></td><td>Below MTD average</td></tr></table>";
		html +="<br/><br/>";
		
		html +="<table style='border-collapse: collapse; border:1px solid #dee0e1; width:600px;'>";
		html +="<tr>";
		html +="<td style='width:120px;background-color:#5875cf; color:white; text-align:center;'>Date</td>";
		html +="<td style='width:120px;background-color:#5875cf; color:white; text-align:center;'>Zone 1</td>";
		html +="<td style='width:120px;background-color:#5875cf; color:white; text-align:center;'>Zone 2</td>";
		html +="<td style='width:120px;background-color:#5875cf; color:white; text-align:center;'>Zone 3</td>";
		html +="<td style='width:120px;background-color:#5875cf; color:white; text-align:center;'>Zone 4</td>";
		html +="</tr>";
			
			
		
		
			ResultSet rs5 = s4.executeQuery("SELECT distinct created_on_date FROM cane_sample order by created_on_date desc");
			while(rs5.next()){
				
				
			Date d = rs5.getDate("created_on_date");
			
			//System.out.println(Utilities.getDisplayDateFormat(d));
			html +="<tr>";
			html +="<td style='border:1px solid #dee0e1;text-align:center;'>";
			html +=Utilities.getDisplayDateFormat(d);
			html +="</td>";
			
			double z1oldre = 0;
			double z1curre =0;
			String z1color="";
			int z1flag=0;
			
			ResultSet rs425 = s.executeQuery("select zone, avg(pol) pol, avg(brix) brix from cane_sample where created_on between "+Utilities.getSQLDateLifting(getDateByDays1(-1,d))+" and "+Utilities.getSQLDateNextLifting(getDateByDays1(-1,d))+" and zone='ZONE-1' group by zone order by zone");
			if(rs425.next()){
				z1oldre = getRecovery(rs425.getDouble("pol"),rs425.getDouble("brix"));
				z1flag=1;
			}else{
				z1flag=0;
			}
			
			
			
			
			ResultSet rs4 = s.executeQuery("select zone, avg(pol) pol, avg(brix) brix from cane_sample where created_on between "+Utilities.getSQLDateLifting(d)+" and "+Utilities.getSQLDateNextLifting(d)+" and zone='ZONE-1' group by zone order by zone");
			
			if(rs4.next()){
				
				
				z1curre = getRecovery(rs4.getDouble("pol"),rs4.getDouble("brix"));
				
				if(z1curre>z1oldre){
					z1color="#cbe9aa";
				}else if(z1curre<z1oldre){
					z1color="#e79ca8";
				}
				
				if(z1flag==0){
					z1color="";
				}
				
				html +="<td style='border:1px solid #dee0e1;text-align:center; background-color:"+z1color+"'>";
				html +=getDisplayCurrencyFormatThreeDecimalFixed(z1curre);
				html +="</td>";
				
			}else{
				
				html +="<td style='border:1px solid #dee0e1;text-align:center;'>";
				html +="-";
				html +="</td>";
				
			}
			
			
			
			double z2oldre = 0;
			double z2curre =0;
			String z2color="";
			int z2flag=0;
			
			ResultSet rs415 = s.executeQuery("select zone, avg(pol) pol, avg(brix) brix from cane_sample where created_on between "+Utilities.getSQLDateLifting(getDateByDays1(-1,d))+" and "+Utilities.getSQLDateNextLifting(getDateByDays1(-1,d))+" and zone='ZONE-2' group by zone order by zone");
			if(rs415.next()){
				z2oldre = getRecovery(rs415.getDouble("pol"),rs415.getDouble("brix"));
				z2flag=1;
			}else{
				z2flag=0;
			}
			
			
			
			
			ResultSet rs41 = s.executeQuery("select zone, avg(pol) pol, avg(brix) brix from cane_sample where created_on between "+Utilities.getSQLDateLifting(d)+" and "+Utilities.getSQLDateNextLifting(d)+" and zone='ZONE-2' group by zone order by zone");
			
			if(rs41.next()){
				
				
				z2curre = getRecovery(rs41.getDouble("pol"),rs41.getDouble("brix"));
				
				if(z2curre>z2oldre){
					z2color="#cbe9aa";
				}else if(z2curre<z2oldre){
					z2color="#e79ca8";
				}
				
				if(z2flag==0){
					z2color="";
				}
				
				
				html +="<td style='border:1px solid #dee0e1;text-align:center;background-color:"+z2color+"'>";
				html +=getDisplayCurrencyFormatThreeDecimalFixed(z2curre);
				html +="</td>";
				
			}else{
				
				html +="<td style='border:1px solid #dee0e1;text-align:center;'>";
				html +="-";
				html +="</td>";
				
			}
			
			
			
			
			double z3oldre = 0;
			double z3curre =0;
			String z3color="";
			int z3flag=0;
			
			ResultSet rs435 = s.executeQuery("select zone, avg(pol) pol, avg(brix) brix from cane_sample where created_on between "+Utilities.getSQLDateLifting(getDateByDays1(-1,d))+" and "+Utilities.getSQLDateNextLifting(getDateByDays1(-1,d))+" and zone='ZONE-3' group by zone order by zone");
			if(rs435.next()){
				z3oldre = getRecovery(rs435.getDouble("pol"),rs435.getDouble("brix"));
				z3flag=1;
			}else{
				z3flag=0;
			}
			
			
			ResultSet rs42 = s.executeQuery("select zone, avg(pol) pol, avg(brix) brix from cane_sample where created_on between "+Utilities.getSQLDateLifting(d)+" and "+Utilities.getSQLDateNextLifting(d)+" and zone='ZONE-3' group by zone order by zone");
			
			if(rs42.next()){
				
				
				
				z3curre = getRecovery(rs42.getDouble("pol"),rs42.getDouble("brix"));
				
				if(z3curre>z3oldre){
					z3color="#cbe9aa";
				}else if(z3curre<z3oldre){
					z3color="#e79ca8";
				}
				
				if(z3flag==0){
					z3color="";
				}
				
				html +="<td style='border:1px solid #dee0e1;text-align:center; background-color:"+z3color+"'>";
				html +=getDisplayCurrencyFormatThreeDecimalFixed(z3curre);
				html +="</td>";
				
			}else{
				
				html +="<td style='border:1px solid #dee0e1;text-align:center;'>";
				html +="-";
				html +="</td>";
				
			}
			
			
			
			
			double z4oldre = 0;
			double z4curre =0;
			String z4color="";
			int z4flag=0;
			
			ResultSet rs445 = s.executeQuery("select zone, avg(pol) pol, avg(brix) brix from cane_sample where created_on between "+Utilities.getSQLDateLifting(getDateByDays1(-1,d))+" and "+Utilities.getSQLDateNextLifting(getDateByDays1(-1,d))+" and zone='ZONE-4' group by zone order by zone");
			if(rs445.next()){
				z4oldre = getRecovery(rs445.getDouble("pol"),rs445.getDouble("brix"));
				z4flag=1;
			}else{
				z4flag=0;
			}
			
			
			ResultSet rs43 = s.executeQuery("select zone, avg(pol) pol, avg(brix) brix from cane_sample where created_on between "+Utilities.getSQLDateLifting(d)+" and "+Utilities.getSQLDateNextLifting(d)+" and zone='ZONE-4' group by zone order by zone");
			
			if(rs43.next()){
				
				z4curre = getRecovery(rs43.getDouble("pol"),rs43.getDouble("brix"));
				
				//System.out.println("Current "+z4curre+" Old "+z4oldre+" flag "+z4flag);
				
				
				
				if(z4curre>z4oldre){
					z4color="#cbe9aa";
				}else if(z4curre<z4oldre){
					z4color="#e79ca8";
				}
				
				if(z4flag==0){
					z4color="";
				}
				
				html +="<td style='border:1px solid #dee0e1;text-align:center;background-color:"+z4color+"'>";
				html +=getDisplayCurrencyFormatThreeDecimalFixed(z4curre);
				html +="</td>";
				
			}else{
				
				html +="<td style='border:1px solid #dee0e1;text-align:center;'>";
				html +="-";
				html +="</td>";
				
			}
			
			
				
				
				
				html +="</tr>";
			
			
		}
			
			
			
		
		
		html +="</table>";
		html +="<br><table><tr><td style='width:20px; hight: 20px; background-color:#cbe9aa'></td><td>Increased w.r.t. last day</td></tr><tr><td style='width:20px; hight: 20px; background-color:#e79ca8'></td><td>Decreased w.r.t. last day</td></tr></table>";
		
		
		
		html +="<p>Please see attachment for individual samples</p>";
			
		
		html += "</body>";
		
		
		html += "</html>";
		
		this.HTMLMessage = html;
		
		s.close();
		s2.close();
		s3.close();
		s4.close();
	}

	
	public static int parseInt(String val) {

		if (val == null) {
			val = "0";
		}

		int ret = 0;

		try {
			ret = Integer.parseInt(val);
		} catch (NumberFormatException e) {
		}

		return ret;
	}
	public static int getMonthNumberByDate(java.util.Date val) {
		if (val != null) {
			SimpleDateFormat format = new SimpleDateFormat("MM");
			return parseInt(format.format(val));
		} else {

			return 0;

		}
	}
	public static int getYearByDate(java.util.Date val) {
		if (val != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy");
			return parseInt(format.format(val));
		} else {

			return 0;

		}
	}
	public static String getSQLDate(java.util.Date val) {
		
		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			return "'" + format.format(val) + "'";

		} else {

			return null;

		}

	}

	public static java.util.Date parseDateYYYYMMDD(String val) {

		java.util.Date d = null;

		if (val != null && !val.equals("0")) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				d = format.parse(val);
			} catch (ParseException e) {
			}
		}

		return d;

	}

	public static java.util.Date getStartDateByMonth(int month, int year) {

		Calendar c = Calendar.getInstance();
		c.set(year, month, 1);

		return c.getTime();
	}

	public static java.util.Date getStartDateByDate(java.util.Date idate) {
		int imonth = getMonthNumberByDate(idate);
		int iyear = getYearByDate(idate);
		return getStartDateByMonth(imonth - 1, iyear);
	}


double getRecovery(double pol, double brix){
	double recovery = 0;
	double sugar = 100;
	double mollasses_purity = 32;
	double milling = 0.60;
	double safety_factor = 0.98;
	double purity = (pol / brix) * 100;

	double sjm = (sugar*(purity-mollasses_purity)) / (purity*(sugar-mollasses_purity));	
	
	recovery = sjm * pol * milling * safety_factor;
	return recovery;
}


String getDisplayCurrencyFormatThreeDecimalFixed(double val) {

		java.text.NumberFormat format = new java.text.DecimalFormat("###,###.#");
		format.setMaximumFractionDigits(3);
		format.setMinimumFractionDigits(3);
		
		return format.format(val);
}

public static Date getDateByDays(int Days){
	return DateUtils.addDays(new Date(), Days);
}


public static Date getDateByDays1(int Days,Date d){
	return DateUtils.addDays(d, Days);
}
	
}
