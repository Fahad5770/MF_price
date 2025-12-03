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

public class DailyScoreCardSendEmail {
	
	public static final String filename_distributor_sd1 = "DistributorScoreCard_Daily_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_distributor_sd2 = "DistributorScoreCard_Daily_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_distributor_sd3 = "DistributorScoreCard_Daily_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	public static final String filename_orderbooker_sd1 = "OrderBookerScoreCard_Daily_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_orderbooker_sd2 = "OrderBookerScoreCard_Daily_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_orderbooker_sd3 = "OrderBookerScoreCard_Daily_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	public static final String filename_zerosales_sd1 = "ZeroSalesOutlets_Daily_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_zerosales_sd2 = "ZeroSalesOutlets_Daily_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_zerosales_sd3 = "ZeroSalesOutlets_Daily_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";

	public static final String filename_lowrevenue_sd1 = "LowestRevenueChillers_Daily_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_lowrevenue_sd2 = "LowestRevenueChillers_Daily_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_lowrevenue_sd3 = "LowestRevenueChillers_Daily_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	public static void main(String[] args) {
		
		try {
			
			
			DailyDistributorScoreCardPDF DistributorSD1 = new com.pbc.pushmail.scorecards.DailyDistributorScoreCardPDF();
			DistributorSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd1, EmployeeHierarchy.getSDHead(1).USER_ID);
			
			DailyDistributorScoreCardPDF DistributorSD2 = new com.pbc.pushmail.scorecards.DailyDistributorScoreCardPDF();
			DistributorSD2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd2, EmployeeHierarchy.getSDHead(2).USER_ID);
			
			DailyDistributorScoreCardPDF DistributorSD3 = new com.pbc.pushmail.scorecards.DailyDistributorScoreCardPDF();
			DistributorSD3.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd3, EmployeeHierarchy.getSDHead(4).USER_ID);
			
			
			DailyOrderBookerScoreCardPDF OrderBookerSD1 = new com.pbc.pushmail.scorecards.DailyOrderBookerScoreCardPDF();
			OrderBookerSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_orderbooker_sd1, EmployeeHierarchy.getSDHead(1).USER_ID);
			
			DailyOrderBookerScoreCardPDF OrderBookerSD2 = new com.pbc.pushmail.scorecards.DailyOrderBookerScoreCardPDF();
			OrderBookerSD2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_orderbooker_sd2, EmployeeHierarchy.getSDHead(2).USER_ID);
			
			DailyOrderBookerScoreCardPDF OrderBookerSD3 = new com.pbc.pushmail.scorecards.DailyOrderBookerScoreCardPDF();
			OrderBookerSD3.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_orderbooker_sd3, EmployeeHierarchy.getSDHead(4).USER_ID);
			
			
			DailyZeroSalesOutletsPDF ZeroSalesSD1 = new com.pbc.pushmail.scorecards.DailyZeroSalesOutletsPDF();
			ZeroSalesSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_zerosales_sd1, EmployeeHierarchy.getSDHead(1).USER_ID);
			
			DailyZeroSalesOutletsPDF ZeroSalesSD2 = new com.pbc.pushmail.scorecards.DailyZeroSalesOutletsPDF();
			ZeroSalesSD2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_zerosales_sd2, EmployeeHierarchy.getSDHead(2).USER_ID);
			
			DailyZeroSalesOutletsPDF ZeroSalesSD3 = new com.pbc.pushmail.scorecards.DailyZeroSalesOutletsPDF();
			ZeroSalesSD3.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_zerosales_sd3, EmployeeHierarchy.getSDHead(4).USER_ID);
			
			
//			DailyLowRevenueChillersPDF LowRevenueSD1 = new com.pbc.pushmail.scorecards.DailyLowRevenueChillersPDF();
//			LowRevenueSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_lowrevenue_sd1, EmployeeHierarchy.getSDHead(1).USER_ID);
//			
//			DailyLowRevenueChillersPDF LowRevenueSD2 = new com.pbc.pushmail.scorecards.DailyLowRevenueChillersPDF();
//			LowRevenueSD2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_lowrevenue_sd2, EmployeeHierarchy.getSDHead(2).USER_ID);
//
//			DailyLowRevenueChillersPDF LowRevenueSD3 = new com.pbc.pushmail.scorecards.DailyLowRevenueChillersPDF();
//			LowRevenueSD3.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_lowrevenue_sd3, EmployeeHierarchy.getSDHead(4).USER_ID);
			
						
			Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(1).USER_EMAIL},new String[]{"omerfk@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","shahzad.ahmed@pbc.com.pk","shahrukh.salman@pbc.com.pk","nadeem@pbc.com.pk","anas.wahab@pbc.com.pk","imran.hashim@pbc.com.pk","anjum.a.ansari@gmail.com","ahsan.rashid@optimus-uae.com","muhammadameer@hotmail.com","rafiquemeo@yahoo.com","shehzadaleem@hotmail.com"}, null, "Daily Score Card | SD1 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(EmployeeHierarchy.getSDHead(1).USER_ID), new String[]{filename_distributor_sd1,filename_orderbooker_sd1,filename_zerosales_sd1});
			Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(2).USER_EMAIL},new String[]{"omerfk@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","shahzad.ahmed@pbc.com.pk","shahrukh.salman@pbc.com.pk","nadeem@pbc.com.pk","anas.wahab@pbc.com.pk","imran.hashim@pbc.com.pk","anjum.a.ansari@gmail.com","ahsan.rashid@optimus-uae.com","muhammadameer@hotmail.com","rafiquemeo@yahoo.com","shehzadaleem@hotmail.com"}, null, "Daily Score Card | SD2 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(EmployeeHierarchy.getSDHead(2).USER_ID), new String[]{filename_distributor_sd2,filename_orderbooker_sd2,filename_zerosales_sd2});
			Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(4).USER_EMAIL},new String[]{"omerfk@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","shahzad.ahmed@pbc.com.pk","shahrukh.salman@pbc.com.pk","nadeem@pbc.com.pk","anas.wahab@pbc.com.pk","imran.hashim@pbc.com.pk","anjum.a.ansari@gmail.com","ahsan.rashid@optimus-uae.com","muhammadameer@hotmail.com","rafiquemeo@yahoo.com","shehzadaleem@hotmail.com"}, null, "Daily Score Card | SD3 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(EmployeeHierarchy.getSDHead(4).USER_ID), new String[]{filename_distributor_sd3,filename_orderbooker_sd3,filename_zerosales_sd3});
			
			
			//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "Daily Score Card | SD1 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(EmployeeHierarchy.getSDHead(1).USER_ID), new String[]{filename_distributor_sd1,filename_orderbooker_sd1,filename_zerosales_sd1});
			//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "Daily Score Card | SD2 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(EmployeeHierarchy.getSDHead(2).USER_ID), new String[]{filename_distributor_sd2,filename_orderbooker_sd2,filename_zerosales_sd2});
			//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "Daily Score Card | SD3 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(EmployeeHierarchy.getSDHead(4).USER_ID), new String[]{filename_distributor_sd3,filename_orderbooker_sd3,filename_zerosales_sd3});
			
			
			
			
			User RSMs[] = EmployeeHierarchy.getRSMs();
			for (User user: RSMs){
				try{
					if (user.USER_EMAIL != null && user.USER_ID != EmployeeHierarchy.getSDHead(4).USER_ID){
						
						User SND = EmployeeHierarchy.getSND(user.USER_ID);
						
						String filename_distributor = "Distributor_ScoreCard_Daily_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
						String filename_orderbooker = "OrderBooker_ScoreCard_Daily_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
						String filename_zerosales = "ZeroSalesOutlets_Daily_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
						String filename_lowrevenue = "LowestRevenueChillers_Daily_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
						
						new com.pbc.pushmail.scorecards.DailyDistributorScoreCardPDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor, user.USER_ID);
						new com.pbc.pushmail.scorecards.DailyOrderBookerScoreCardPDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_orderbooker, user.USER_ID);
						new com.pbc.pushmail.scorecards.DailyZeroSalesOutletsPDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_zerosales, user.USER_ID);
						//new com.pbc.pushmail.scorecards.DailyLowRevenueChillersPDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_lowrevenue, user.USER_ID);
						
						
						Utilities.sendPBCHTMLEmail(new String[]{user.USER_EMAIL},new String[]{"omerfk@pbc.com.pk",SND.USER_EMAIL,"jazeb@pbc.com.pk","obaid@pbc.com.pk","shahrukh.salman@pbc.com.pk","nadeem@pbc.com.pk","anas.wahab@pbc.com.pk","imran.hashim@pbc.com.pk"}, null, "RSM Score Card | "+user.REGION_NAME+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(user.USER_ID), new String[]{filename_distributor,filename_orderbooker,filename_zerosales});
	//					Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "Daily Score Card | "+user.REGION_NAME+" | "+user.USER_DISPLAY_NAME+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(user.USER_ID), new String[]{filename_distributor,filename_orderbooker,filename_zerosales});
						
					}
				}catch(Exception e){e.printStackTrace();}
			}
			
			
			
			User TDMs[] = EmployeeHierarchy.getTDMs();
			for (User user: TDMs){
				try{
					if (user.USER_EMAIL != null){
						
						User RSM = EmployeeHierarchy.getRSMByTDM(user.USER_ID); 
						User SND = EmployeeHierarchy.getSND(RSM.USER_ID);
						
						if (RSM.USER_EMAIL == null){
							RSM.USER_EMAIL = SND.USER_EMAIL;
						}
						
						if (RSM.USER_EMAIL != null && SND.USER_EMAIL != null){
						
							String filename_distributor = "Distributor_ScoreCard_Daily_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
							String filename_orderbooker = "OrderBooker_ScoreCard_Daily_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
							String filename_zerosales = "ZeroSalesOutlets_Daily_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
							
							new com.pbc.pushmail.scorecards.DailyDistributorScoreCardPDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor, user.USER_ID);
							new com.pbc.pushmail.scorecards.DailyOrderBookerScoreCardPDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_orderbooker, user.USER_ID);
							new com.pbc.pushmail.scorecards.DailyZeroSalesOutletsPDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_zerosales, user.USER_ID);
							
							
							//System.out.println(SND.USER_EMAIL+" "+ RSM.USER_EMAIL);
							//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "TDM Score Card | "+user.REGION_NAME+" | "+user.USER_DISPLAY_NAME+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessageTDM(user.USER_ID), new String[]{filename_distributor,filename_orderbooker,filename_zerosales});
							
							
							Utilities.sendPBCHTMLEmail(new String[]{user.USER_EMAIL},new String[]{"omerfk@pbc.com.pk",SND.USER_EMAIL,RSM.USER_EMAIL,"jazeb@pbc.com.pk","obaid@pbc.com.pk","shahrukh.salman@pbc.com.pk","nadeem@pbc.com.pk","anas.wahab@pbc.com.pk","shahzad.ahmed@pbc.com.pk","imran.hashim@pbc.com.pk"}, null, "TDM Score Card | "+user.REGION_NAME+" | "+user.USER_DISPLAY_NAME+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessageTDM(user.USER_ID), new String[]{filename_distributor,filename_orderbooker,filename_zerosales});
						}
						
					}
				}catch(Exception e){e.printStackTrace();}
			}
			
			
			
			
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

	public static String getHTMLMessage(long EmployeeID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		Datasource ds = new Datasource();
		ds.createConnectionToReplica();
		
		
		Statement s = ds.createStatement();
		Statement s5 = ds.createStatement();
		Statement s2 = ds.createStatement();
		Statement s3 = ds.createStatement();
		
		
		String OrderSeries = "";
		String BillSeries = "";
		String LiftingSeries = "";
		String PrimaryOrdersSeries = "";
		String CashSeries = "";
		String SecondarySalesSeries = "";
		String BackordersSeries = "";
		String SalesReturnSeries = "";
		
		double LastBillProductivity = 0;
		
		for (int i =0; i < 15; i++){
			
			int difference = 0;
			difference = i - 14;
			
			Date CurrentDate = Utilities.getDateByDays(difference);
			
			if (Utilities.getDayOfWeekByDate(CurrentDate) != 6){ // Exclude Friday
				
				double TotalPlannedCalls = 0;
				double TotalOrders = 0;
				double TotalBills = 0;
				
				
				
				ResultSet rs80 = s5.executeQuery("select distinct dbpl.assigned_to, (select display_name from users where id = dbpl.assigned_to) orderbooker_name, (select is_active from users where id = dbpl.assigned_to) is_active from distributor_beat_plan_log dbpl force index (distributor_beat_plan_log_date_assigned_day_number) where dbpl.log_date between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDate(CurrentDate)+" and dbpl.distributor_id in (select distributor_id from common_distributors where is_active = 1 and (snd_id in ("+EmployeeID+") or rsm_id in ("+EmployeeID+"))) and dbpl.distributor_id != 100312 and dbpl.distributor_id in (select distinct distributor_id from mobile_order where created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+") and dbpl.day_number = "+Utilities.getDayOfWeekByDate(CurrentDate)+" and dbpl.assigned_to is not null");
				while(rs80.next()){
					
					long OrderbookerID = rs80.getLong(1);
					String OrderbookerName = rs80.getString(2);
				
					
					String OrderIDs = "0";
					String OutletIDsOrdered = "0";
					String DaysOfWeekIDs = Utilities.getDayOfWeekByDate(CurrentDate)+"";
					double OrderCount = 0;
					double UniqueOutletsOrdered = 0;
				
					ResultSet rs = s.executeQuery("select mo.created_by, group_concat(mo.id) order_ids, group_concat(distinct dayofweek(mo.created_on)) days_of_week, count(mo.id) order_count, count(distinct mo.outlet_id) unique_outlets_ordered, group_concat(distinct mo.outlet_id) outlet_ids_ordered from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and mo.created_by = "+rs80.getLong(1)+" group by mo.created_by");
					if(rs.first()){
						OrderIDs = rs.getString("order_ids");
						OutletIDsOrdered = rs.getString("outlet_ids_ordered");
						OrderCount = rs.getInt("order_count");
						UniqueOutletsOrdered = rs.getInt("unique_outlets_ordered");
					}
					
					double OrderBookersWorked = 0;
					double PlannedOutletsCount = 0;
					String PlannedOutletIDs = "";
					
					int OrderBookersAssigned = 0;
					ResultSet rs2 = s3.executeQuery("select count(distinct outlet_id), group_concat(distinct outlet_id) from distributor_beat_plan_log force index (distributor_beat_plan_log_date_assigned_day_number) where log_date between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDate(CurrentDate)+" and assigned_to = "+OrderbookerID+" and day_number in ("+DaysOfWeekIDs+")");
					if (rs2.first()){
						PlannedOutletsCount = rs2.getDouble(1);
						PlannedOutletIDs = rs2.getString(2);
					}
					
					double OrdersFromPlannedOutlets = 0;
					ResultSet rs10 = s3.executeQuery("select count(id) from common_outlets where id in ("+PlannedOutletIDs+") and id in ("+OutletIDsOrdered+")");
					if (rs10.first()){
						OrdersFromPlannedOutlets = rs10.getDouble(1);
					}
					
					
					double BillsFromOrders = 0;
					try{
					ResultSet rs11 = s3.executeQuery("select count(distinct outlet_id) from inventory_sales_adjusted where order_id in ("+OrderIDs+") and net_amount != 0");
					if (rs11.first()){
						BillsFromOrders = rs11.getDouble(1);
					}
					}catch(SQLException e12){System.out.println(e12);}
					
					TotalPlannedCalls += PlannedOutletsCount;
					TotalOrders += OrdersFromPlannedOutlets;
					TotalBills += BillsFromOrders;
					
				}
				
				double OrderProductivity = 0;
				if (TotalPlannedCalls != 0){
					OrderProductivity = (TotalOrders / TotalPlannedCalls) * 100;
				}
				
				double BillProductivity = 0;
				if (TotalOrders != 0){
					BillProductivity = (TotalBills / TotalOrders) * 100;
				}
				
				
				
				double SecondarySales = 0;
				ResultSet rs12 = s3.executeQuery("select sum(net_amount) from inventory_sales_adjusted where created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and distributor_id in (select distributor_id from common_distributors where is_active = 1 and (snd_id in ("+EmployeeID+") or rsm_id in ("+EmployeeID+")))");
				if (rs12.first()){
					SecondarySales = rs12.getDouble(1);
				}
				
				double BackordersCC = 0;
				ResultSet rs13 = s3.executeQuery("SELECT sum( ((mopb.total_units * ipv.liquid_in_ml) / ipv.conversion_rate_in_ml)) cc FROM mobile_order_products_backorder mopb join inventory_products_view ipv on mopb.product_id = ipv.product_id join mobile_order mo on mopb.id = mo.id where mo.backordered_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and mo.distributor_id in (select distributor_id from common_distributors where is_active = 1 and (snd_id in ("+EmployeeID+") or rsm_id in ("+EmployeeID+")))");
				if(rs13.first()){
					BackordersCC = rs13.getDouble(1);
				}
				
				double SalesReturnCC = 0;
				ResultSet rs14 = s3.executeQuery("SELECT sum( ((isdap.total_units * ipv.liquid_in_ml) / ipv.conversion_rate_in_ml)) cc FROM inventory_sales_dispatch_adjusted_products isdap join inventory_products_view ipv on isdap.product_id = ipv.product_id join inventory_sales_adjusted isa on isdap.invoice_id = isa.id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.distributor_id in (select distributor_id from common_distributors where is_active = 1 and (snd_id in ("+EmployeeID+") or rsm_id in ("+EmployeeID+"))) and isdap.is_promotion = 0");
				if(rs14.first()){
					SalesReturnCC = rs14.getDouble(1);
				}
				
				
				
				/*
			double BackordersCC = 0;
			//System.out.println("SELECT sum( ((mopb.total_units * ipv.liquid_in_ml) / ipv.conversion_rate_in_ml)) cc FROM mobile_order_products_backorder mopb join inventory_products_view ipv on mopb.product_id = ipv.product_id where mopb.id in ("+OrderIDs+")");
			ResultSet rs21 = s3.executeQuery("SELECT sum( ((mopb.total_units * ipv.liquid_in_ml) / ipv.conversion_rate_in_ml)) cc FROM mobile_order_products_backorder mopb join inventory_products_view ipv on mopb.product_id = ipv.product_id where mopb.id in ("+OrderIDs+")");
			if(rs21.first()){
				BackordersCC = rs21.getDouble(1);
			}
			
			double BackordersPercent = 0;
			if (ConvertedCasesOrdered != 0){
				BackordersPercent = (BackordersCC / (ConvertedCasesOrdered * 1d)) * 100;
			}
			
			double SalesReturnCC = 0;
			ResultSet rs22 = s3.executeQuery("SELECT sum( ((isdap.total_units * ipv.liquid_in_ml) / ipv.conversion_rate_in_ml)) cc FROM inventory_sales_dispatch_adjusted_products isdap join inventory_products_view ipv on isdap.product_id = ipv.product_id where isdap.invoice_id in (select distinct id from inventory_sales_invoices where order_id in ("+OrderIDs+")) and isdap.is_promotion = 0");
			if(rs22.first()){
				SalesReturnCC = rs22.getDouble(1);
			}
				
				*/
				
				//System.out.println(EmployeeID+" "+CurrentDate+ " "+OrderProductivity+" "+BillProductivity);
				
				
				if (i != 0){
					OrderSeries += ","; 
					BillSeries += ",";
					SecondarySalesSeries += ",";
					BackordersSeries += ",";
					SalesReturnSeries += ",";
				}
				
				
				
				OrderSeries += Math.round(OrderProductivity);
				SecondarySalesSeries += (SecondarySales/1000000d);
				BackordersSeries += Math.round(BackordersCC);
				SalesReturnSeries += Math.round(SalesReturnCC);
				
				
				
				if (difference == 0){
					BillSeries += Math.round(LastBillProductivity);
				}else{
					BillSeries += Math.round(BillProductivity);
				}
				
				
				
				
				LastBillProductivity = BillProductivity;
				
			}
			
			if (difference != 0){
				
				
				System.out.println(CurrentDate);
				
				// Lifting
				double LiftingAmount = 0;
				ResultSet rs = s.executeQuery("select sum(invoice_amount) invoice_amount from inventory_delivery_note idn where idn.distributor_id in (SELECT distributor_id FROM common_distributors where (snd_id = "+EmployeeID+" or rsm_id = "+EmployeeID+")) and idn.created_on between "+Utilities.getSQLDateLifting(CurrentDate)+" and "+Utilities.getSQLDateNextLifting(CurrentDate));
				if(rs.first()){
					LiftingAmount = rs.getDouble(1);
				}
				if (i != 0){
					LiftingSeries += ",";
					PrimaryOrdersSeries += ",";
					CashSeries += ",";
				}
				LiftingSeries += (LiftingAmount / 1000000);
				
				
				
				
				// Orders
				double PrimaryOrderAmount = 0;
				//Datasource dsSAP = new Datasource();
				//dsSAP.createConnectionToSAPDB();
				//Statement sSAP = dsSAP.createStatement();
				String WhereHOD = " and vbak.kunnr in ("+UserAccess.getDistributorQueryString(UserAccess.getSNDDistributors(EmployeeID))+") ";
				String Query = "SELECT vbak.vbeln order_number, vbak.kunnr distributor_id, vbak.erdat entry_date, vbak.audat order_date, (vbak.netwr + (select sum(mwsbp) from "+ds.getSAPDatabaseAlias()+".vbap where vbeln = vbak.vbeln)) /* Tax Amount from VBAP and Order Amount from VBAP-NETWR*/ order_amount, kna1.name1 distributor_name, vbuk.fksak FROM "+ds.getSAPDatabaseAlias()+".vbak vbak join "+ds.getSAPDatabaseAlias()+".vbuk vbuk on vbak.vbeln = vbuk.vbeln join "+ds.getSAPDatabaseAlias()+".kna1 kna1 on vbak.kunnr = kna1.kunnr where vbak.auart in ('ZDIS','ZMRS','ZDFR') and vbak.audat between "+Utilities.getSQLDateOracle(CurrentDate)+" and "+Utilities.getSQLDateOracle(CurrentDate)+" and vbuk.vbtyp = 'C' "+WhereHOD+" and kna1.CASSD != 'X' and kna1.FAKSD != 'X' order by vbak.audat";
				/*
				ResultSet rsSAP = sSAP.executeQuery(Query);
				while(rsSAP.next()){
					PrimaryOrderAmount += rsSAP.getDouble("order_amount");
				}
				
				PrimaryOrdersSeries += (PrimaryOrderAmount / 1000000);
				*/
				PrimaryOrdersSeries += (PrimaryOrderAmount);
				
				
				//sSAP.close();
				//dsSAP.dropConnection();
				
				
				
				// Cash
				double CashInflow = 0;
				ResultSet rs5 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts  glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glci.is_internal = 0 and created_on between "+Utilities.getSQLDateLifting(CurrentDate)+" and "+Utilities.getSQLDateNextLifting(CurrentDate)+" and customer_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeID+") or rsm_id in ("+EmployeeID+"))");
				if(rs5.first()){
					CashInflow = rs5.getDouble(1);
				}
				CashSeries += (CashInflow / 1000000);
			
			}
			
		}
		
		// Outlet Universe
		long OutletUniverse = 0;
		ResultSet rs = s.executeQuery("SELECT count(distinct outlet_id) FROM distributor_beat_plan_view dbpv join common_distributors cd on dbpv.distributor_id = cd.distributor_id where (cd.snd_id = "+EmployeeID+" or cd.rsm_id = "+EmployeeID+") and cd.distributor_id in (select distinct distributor_id from inventory_sales_adjusted where distributor_id != 100216)  ");
		if (rs.first()){
			OutletUniverse = rs.getLong(1);
		}
		
		long DiscountedUniverse = 0;
		ResultSet rs8 = s.executeQuery("SELECT count(distinct outlet_id) FROM distributor_beat_plan_view dbpv join common_distributors cd on dbpv.distributor_id = cd.distributor_id where (cd.snd_id = "+EmployeeID+" or cd.rsm_id = "+EmployeeID+") and cd.distributor_id in (select distinct distributor_id from inventory_sales_adjusted where distributor_id != 100216) and dbpv.outlet_id in (select distinct outlet_id from sampling s where s.active = 1 and s.sampling_id in (select sp.sampling_id from sampling_percase sp where curdate() between sp.valid_from and sp.valid_to UNION select si.sampling_id from sampling si where si.fixed_company_share !=0 and curdate() between si.fixed_valid_from and si.fixed_valid_to))");
		if (rs8.first()){
			DiscountedUniverse = rs8.getLong(1);
		}
		
		
		// Zero Sale Outlets
		long ZeroSaleOutlets10 = 0;
		long ZeroSaleOutlets20 = 0;
		long ZeroSaleOutlets30 = 0;
		long ZeroSaleOutlets90 = 0;
		long TotalZeroSaleOutlets = 0;
		long ZeroSaleOutletsToDisplay = 0;
		
		ResultSet rs2 = s.executeQuery(""+
				"select outlet_id, to_days(curdate())-to_days(ifnull((select date(max(created_on)) from inventory_sales_adjusted where outlet_id = tab1.outlet_id),'2013-01-01')) days_ago from ("+
				"SELECT distinct outlet_id FROM distributor_beat_plan_view dbpv join common_distributors cd on dbpv.distributor_id = cd.distributor_id where (cd.snd_id = "+EmployeeID+" or cd.rsm_id = "+EmployeeID+") and cd.distributor_id in (select distinct distributor_id from inventory_sales_adjusted where distributor_id != 100216) "+
				") tab1 having days_ago > 10"+
				"");
		while (rs2.next()){
			
			if (rs2.getInt(2) > 10 && rs2.getInt(2) <= 20){
				ZeroSaleOutlets10++;
			}
			if (rs2.getInt(2) > 20 && rs2.getInt(2) <= 30){
				ZeroSaleOutlets20++;
			}
			if (rs2.getInt(2) > 30 && rs2.getInt(2) <= 90){
				ZeroSaleOutlets30++;
				ZeroSaleOutletsToDisplay++;
			}
			if (rs2.getInt(2) > 90){
				ZeroSaleOutlets90++;
				ZeroSaleOutletsToDisplay++;
			}
			
			TotalZeroSaleOutlets++;
		}
		
		double ZeroPercentage = 0;
		if (OutletUniverse != 0){
			ZeroPercentage = ((ZeroSaleOutletsToDisplay * 1d) / (OutletUniverse * 1d)) * 100;	
		}
		
		double DiscountedPercentage = 0;
		if (OutletUniverse != 0){
			DiscountedPercentage = ((DiscountedUniverse * 1d) / (OutletUniverse * 1d)) * 100;	
		}
		
		
		
//786
		// Chillers
		int ActiveChillers = 0;
		ResultSet rs3 = s.executeQuery("select count(*) from common_assets where tot_status = 'INJECTED' and outlet_id_parsed in (select outlet_id from distributor_beat_plan_all_view where snd_id = "+EmployeeID+" or rsm_id = "+EmployeeID+")");
		if (rs3.first()){
			ActiveChillers = rs3.getInt(1);
		}
		
		double ChillerRevenue = 0;
		ResultSet rs6 = s.executeQuery("select sum(net_amount) from inventory_sales_adjusted where created_on between from_days(to_days(curdate())-30) and from_days(to_days(curdate())+1) and distributor_id in (select distributor_id from common_distributors where (snd_id in ("+EmployeeID+") or rsm_id in ("+EmployeeID+")))");
		if (rs6.first()){
			ChillerRevenue = rs6.getDouble(1);
		}
		
		double ChillerCC = 0;
		ResultSet rs5 = s.executeQuery("select sum(((isap.total_units * ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between from_days(to_days(curdate())-30) and from_days(to_days(curdate())+1) and isa.distributor_id in (select distributor_id from common_distributors where (snd_id in ("+EmployeeID+") or rsm_id in ("+EmployeeID+")))");
		if (rs5.first()){
			ChillerCC = rs5.getDouble(1);
		}

		double RevenuePerChiller = (ChillerRevenue / ActiveChillers)/26;
		double CCPerChiller = (ChillerCC / ActiveChillers)/26;
		
		
		
		
		String html = "<html>";
		html += "<body><br>";
			html += "<table style='width: 450px'>";

				html += "<tr>";
				html += "<td colspan='2' style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold;'>Primary Sales</td>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td colspan='2' style='text-align: center;'><img src='https://chart.googleapis.com/chart?cht=lc&chd=t:"+LiftingSeries+"|"+CashSeries+"&chds=a&chs=400x250&chl=15 Days Ago|Yesterday&chxt=y,r&chco=bda609,0963bd&chdl=Lifting|Cash&chdlp=b&chtt=(millions)'></td>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td colspan='2' style='text-align: center;'>&nbsp;</td>";
				html += "</tr>";

				html += "<tr>";
					html += "<td colspan='2' style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold;'>Secondary Sales</td>";
				html += "</tr>";

				html += "<tr>";
				html += "<td colspan='2' style='background-color: #E8E8E8; height: 18px; text-align: center; font-weight: normal;'>Productivity</td>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td colspan='2' style='text-align: center;'><img src='https://chart.googleapis.com/chart?cht=lc&chd=t:"+OrderSeries+"|"+BillSeries+"&chds=a&chs=400x250&chl=15 Days Ago|Today&chxt=y,r&chco=bda609,0963bd&chdl=Order%20Productivity|Bill%20Productivity&chdlp=bv&chtt=%age'></td>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td colspan='2' style='text-align: center;'>&nbsp;</td>";
				html += "</tr>";

				
				html += "<tr>";
				html += "<td colspan='2' style='background-color: #E8E8E8; height: 18px; text-align: center; font-weight: normal;'>Secondary Sales</td>";
				html += "</tr>";
				
				
				html += "<tr>";
				html += "<td colspan='2' style='text-align: center;'><img src='https://chart.googleapis.com/chart?cht=lc&chd=t:"+SecondarySalesSeries+"&chds=a&chs=400x250&chl=15 Days Ago|Today&chxt=y,r&chco=bda609,0963bd,f0516c&chdl=Secondary Sales&chdlp=b&chtt=(millions)'></td>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td colspan='2' style='text-align: left; font-size: 10px;'>* based on invoice date, inclusive of spot sales and desk sales</td>";
				html += "</tr>";

				html += "<tr>";
				html += "<td colspan='2' style='text-align: center;'>&nbsp;</td>";
				html += "</tr>";

				html += "<tr>";
				html += "<td colspan='2' style='background-color: #E8E8E8; height: 18px; text-align: center; font-weight: normal;'>Backorders & Returns</td>";
				html += "</tr>";
				
				
				html += "<tr>";
				html += "<td colspan='2' style='text-align: center;'><img src='https://chart.googleapis.com/chart?cht=lc&chd=t:"+BackordersSeries+"|"+SalesReturnSeries+"&chds=a&chs=400x250&chl=15 Days Ago|Today&chxt=y,r&chco=bda609,0963bd,f0516c&chdl=Backorders|Returns&chdlp=b&chtt=(Converted Cases)'></td>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td colspan='2' style='text-align: center;'>&nbsp;</td>";
				html += "</tr>";

				html += "<tr>";
				html += "<td colspan='2' style='text-align: center;'><table style='width: 100%'>";
				
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold; width: 33.3%'>Outlet Universe</td>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold; width: 33.3%'>Zero Sale</td>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold; width: 33.3%'>Discounted</td>";
			html += "</tr>";
			
			html += "<tr>";
				html += "<td style='text-align: center; background-color: #C8CFE6;'>"+Utilities.getDisplayCurrencyFormatRounded(OutletUniverse)+"</td>";
				html += "<td style='text-align: center; background-color: #C8CFE6;'>"+Utilities.getDisplayCurrencyFormatRounded(ZeroSaleOutletsToDisplay)+" ("+Utilities.getDisplayCurrencyFormatRounded(ZeroPercentage)+"%)</td>";
				html += "<td style='text-align: center; background-color: #C8CFE6;'>"+Utilities.getDisplayCurrencyFormatRounded(DiscountedUniverse)+" ("+Utilities.getDisplayCurrencyFormatRounded(DiscountedPercentage)+"%)</td>";
			html += "</tr>";
				
				html += "</table></td></tr>";
				
				
			
			
			
			html += "<tr>";
			html += "<td colspan='2' style='text-align: left; font-size: 10px;'>*Zero Sale outlets are based on 30 days of inactivity.<br>*Discounted outlets include Fixed and Per Case agreements</td>";
			html += "</tr>";
			html += "<tr>";
			html += "<td colspan='2' style='text-align: left; font-size: 10px;'>&nbsp;</td>";
			html += "</tr>";
			
			html += "<tr>";
			html += "<td colspan='2' style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold;'>Ageing of Zero Sale Outlets</td>";
			html += "</tr>";
			
			html += "<tr>";
			html += "<td colspan='2' style='background-color: #C8CFE6; text-align: left;'><table style='width: 100%'><tr><td style='text-align: center;'>11-20 Days</td><td style='text-align: center;'>21-30 Days</td><td style='text-align: center;'>31-90 Days</td><td style='text-align: center;'>> 90 Days</td></tr><tr><td style='background-color: white; text-align: center;'>"+ZeroSaleOutlets10+"</td><td style='background-color: white; text-align: center;'> "+ZeroSaleOutlets20+"</td><td style='background-color: white; text-align: center;'>"+ZeroSaleOutlets30+"</td><td style='background-color: white; text-align: center;'>"+ZeroSaleOutlets90+"</td></tr></table></td>";
			html += "</tr>";
			
			html += "<tr>";
			html += "<td colspan='2' style='text-align: left; font-size: 10px;'>&nbsp;</td>";
			html += "</tr>";
			
//			html += "<tr>";
//			html += "<td colspan='2' style='background-color: #835DA3; color: white; height: 18px; text-align: center; font-weight: bold;'>Active Chillers</td>";
//			html += "</tr>";
//			
//			html += "<tr>";
//			html += "<td colspan='2' style='background-color: #E8E8E8; text-align: left;'><table style='width: 100%'><tr><td style='text-align: center; width: 33%;'>Active</td><td style='text-align: center;width: 33.5%;'>Revenue/Day* (Rs.)</td><td style='text-align: center;width: 33.5%;'>CC/Day*</td></tr><tr><td style='background-color: white; text-align: center;'>"+Utilities.getDisplayCurrencyFormatRounded(ActiveChillers)+"</td><td style='background-color: white; text-align: center;'>"+Utilities.getDisplayCurrencyFormatRounded(RevenuePerChiller)+"</td><td style='background-color: white; text-align: center;'>"+Utilities.getDisplayCurrencyFormatOneDecimal(CCPerChiller)+"</td></tr></table></td>";
//			html += "</tr>";
//			
//			html += "<tr>";
//			html += "<td colspan='2' style='text-align: left; font-size: 10px;'>Revenue/Day = Average revenue per chiller per day in last 30 days<br>CC/Day = Average sales in converted cases per chiller per day in last 30 days</td>";
//			html += "</tr>";
			
			
			
			html += "<tr>";
			html += "<td colspan='2' style='background-color: #835DA3; color: white; height: 18px; text-align: center; font-weight: bold;'>Active Chillers</td>";
			html += "</tr>";
			
			html += "<tr>";
			html += "<td colspan='2' style='background-color: #E8E8E8; text-align: left;'><table style='width: 100%'><tr><td style='text-align: center; width: 50%;'>Active</td><td style='text-align: center;width: 50%;'>CC/Day*</td></tr><tr><td style='background-color: white; text-align: center;'>"+Utilities.getDisplayCurrencyFormatRounded(ActiveChillers)+"</td><td style='background-color: white; text-align: center;'>"+Utilities.getDisplayCurrencyFormatOneDecimal(CCPerChiller)+"</td></tr></table></td>";
			html += "</tr>";
			
			html += "<tr>";
			html += "<td colspan='2' style='text-align: left; font-size: 10px;'>CC/Day = Average sales in converted cases per chiller per day in last 30 days</td>";
			html += "</tr>";
			
			html += "<tr>";
			html += "<td colspan='2' style='text-align: left; font-size: 10px;'>&nbsp;</td>";
			html += "</tr>";
			
			
			if (EmployeeID == EmployeeHierarchy.getSDHead(1).USER_ID){
			
				
				html += "<tr>";
				html += "<td colspan='2' style='background-color: #835DA3; color: white; height: 18px; text-align: center; font-weight: bold;'>Chillers Injected out of Beat Plan*</td>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td colspan='2' style='background-color: #E8E8E8; text-align: left;'><table style='width: 100%'><tr><td style='text-align: left; width: 40%'>Region</td><td style='text-align: center;width: 20%'>2015</td><td style='text-align: center;width: 20%'>2014</td><td style='text-align: center;width: 20%'>Earlier</td></tr>";
					
				
				ResultSet rs7 = s.executeQuery("select region_id, (select region_name from common_regions where region_id = tab2.region_id) region_name, sum(if(yr<2014,chillers,0)) y2013, sum(if(yr=2014,chillers,0)) y2014, sum(if(yr>=2015,chillers,0)) y2015 from ( "+
							"SELECT region_id, year(movement_date) yr, count(*) chillers FROM pep.common_assets ca join common_outlets co on ca.outlet_id_parsed = co.id where tot_status = 'INJECTED' and co.region_id in (5,11) and ca.outlet_id_parsed not in (select distinct outlet_id from distributor_beat_plan_schedule) group by region_id, year(movement_date) "+
							") tab2 group by region_id");
				while (rs7.next()){
					html +="<tr><td style='background-color: white; text-align: left;'>"+rs7.getString(2)+"</td><td style='background-color: white; text-align: center;'>"+rs7.getString("y2015")+"</td><td style='background-color: white; text-align: center;'>"+rs7.getString("y2014")+"</td><td style='background-color: white; text-align: center;'>"+rs7.getString("y2013")+"</td></tr>";
				}
				
				html += "</table></td>";
				html += "</tr>";
	
				html += "<tr>";
				html += "<td colspan='2' style='text-align: left; font-size: 10px;'>* Analysis applicable only on Theia implemented regions.</td>";
				html += "</tr>";
			
			}
			
			html += "<tr>";
			html += "<td colspan='2' style='text-align: left; font-size: 10px;'>&nbsp;</td>";
			html += "</tr>";
			
			/*
			html += "<tr>";
			html += "<td colspan='2' style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold;'>Lowest Order SKUs Today</td>";
			html += "</tr>";
			
			
			
			html += "<tr>";
			html += "<td colspan='2' style='text-align: left;'><table style='width: 100%; border-collapse: collapse;'><tr style='background-color: #E9EBF2'><td style='text-align: left;' colspan='2'><b>SKU</b></td><td style='text-align: right;'><b>Cases</b></td></tr>";
			
			
			
			String background = "";
			boolean alternate = false;
			ResultSet rs4 = s.executeQuery("select ipv.product_id, ipv.package_label, ipv.brand_label, ifnull((select sum(raw_cases) orders from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mo.created_on between curdate() and from_days(to_days(curdate())+1) and distributor_id in (select distributor_id from common_distributors where snd_id = "+EmployeeID+" or rsm_id = "+EmployeeID+") and mop.is_promotion = 0 and mop.product_id = ipv.product_id),0) cases from inventory_products_view ipv where ipv.product_id in (3,5,6,108,140,11,13,14,16,17,31,33,34,36,37,42,44,45,47,48,115,67,70,80,81,83,85,86,88,111,139) order by cases limit 10");
			while(rs4.next()){
				html += "<tr "+background+"><td style='text-align: left;'>"+rs4.getString(2)+"</td> <td style='text-align: left;'>"+rs4.getString(3)+"</td><td style='text-align: right;'>"+Math.round(rs4.getDouble(4))+"</td></tr>";
				if (alternate == false){
					alternate = true;
					background = "style='background-color: #E9EBF2'";
				}else{
					alternate = false;
					background = "";
				}
			}
			
			html += "</table></td></tr>";
			*/
			
			html += "</table>";
				
			html += "<br><br>";
				
			//html += "Please see attachment for details.<br>This is a system generated email, please do not reply to it.";
			
			
		
			
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
