package com.pbc.pushmail;

import java.sql.SQLException;

import com.pbc.employee.EmployeeHierarchy;
import com.pbc.pushmail.scorecards.DailyLowRevenueChillersPDF;
import com.pbc.pushmail.scorecards.DailyOrderBookerScoreCardPDF;
import com.pbc.pushmail.scorecards.DailyZeroSalesOutletsPDF;
import com.pbc.pushmail.scorecards.WeeklyOrderBookerScoreCardPDF;
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

public class DistributorScoreCardWeeklySendEmail {
	
	public static final String filename_distributor_sd1 = "Distributor_ScoreCard_Weekly_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_distributor_sd2 = "Distributor_ScoreCard_Weekly_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_distributor_sd3 = "Distributor_ScoreCard_Weekly_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	public static final String filename_orderbooker_sd1 = "OrderBooker_ScoreCard_Weekly_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_orderbooker_sd2 = "OrderBooker_ScoreCard_Weekly_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_orderbooker_sd3 = "OrderBooker_ScoreCard_Weekly_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	public static final String filename_zerosales_sd1 = "ZeroSalesOutlets_Weekly_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_zerosales_sd2 = "ZeroSalesOutlets_Weekly_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_zerosales_sd3 = "ZeroSalesOutlets_Weekly_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	public static final String filename_lowrevenue_sd1 = "LowestRevenueChillers_Weekly_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_lowrevenue_sd2 = "LowestRevenueChillers_Weekly_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_lowrevenue_sd3 = "LowestRevenueChillers_Weekly_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	public static void main(String[] args) {
		try {
			
			
			new com.pbc.pushmail.DistributorScoreCardWeeklyPDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd1, EmployeeHierarchy.getSDHead(1).USER_ID);
			
			WeeklyOrderBookerScoreCardPDF OrderBookerSD1 = new com.pbc.pushmail.scorecards.WeeklyOrderBookerScoreCardPDF();
			OrderBookerSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_orderbooker_sd1, EmployeeHierarchy.getSDHead(1).USER_ID);

			DailyZeroSalesOutletsPDF ZeroSalesSD1 = new com.pbc.pushmail.scorecards.DailyZeroSalesOutletsPDF();
			ZeroSalesSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_zerosales_sd1, EmployeeHierarchy.getSDHead(1).USER_ID);
			

			new com.pbc.pushmail.DistributorScoreCardWeeklyPDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd2, EmployeeHierarchy.getSDHead(2).USER_ID);
			
			WeeklyOrderBookerScoreCardPDF OrderBookerSD2 = new com.pbc.pushmail.scorecards.WeeklyOrderBookerScoreCardPDF();
			OrderBookerSD2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_orderbooker_sd2, EmployeeHierarchy.getSDHead(2).USER_ID);
			
			DailyZeroSalesOutletsPDF ZeroSalesSD2 = new com.pbc.pushmail.scorecards.DailyZeroSalesOutletsPDF();
			ZeroSalesSD2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_zerosales_sd2, EmployeeHierarchy.getSDHead(2).USER_ID);
			
			
			new com.pbc.pushmail.DistributorScoreCardWeeklyPDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd3, EmployeeHierarchy.getSDHead(4).USER_ID);
			
			WeeklyOrderBookerScoreCardPDF OrderBookerSD3 = new com.pbc.pushmail.scorecards.WeeklyOrderBookerScoreCardPDF();
			OrderBookerSD3.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_orderbooker_sd3, EmployeeHierarchy.getSDHead(4).USER_ID);
			
			DailyZeroSalesOutletsPDF ZeroSalesSD3 = new com.pbc.pushmail.scorecards.DailyZeroSalesOutletsPDF();
			ZeroSalesSD3.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_zerosales_sd3, EmployeeHierarchy.getSDHead(4).USER_ID);

			//786 
			Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(1).USER_EMAIL},new String[]{"omerfk@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","shahzad.ahmed@pbc.com.pk","nadeem@pbc.com.pk","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","imran.hashim@pbc.com.pk","ahsan.rashid@optimus-uae.com","muhammadameer@hotmail.com","rafiquemeo@yahoo.com","shehzadaleem@hotmail.com"},null, "Weekly Score Card | SD1 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(EmployeeHierarchy.getSDHead(1).USER_ID), new String[]{filename_distributor_sd1,filename_zerosales_sd1,filename_orderbooker_sd1});
			Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(2).USER_EMAIL},new String[]{"omerfk@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","shahzad.ahmed@pbc.com.pk","nadeem@pbc.com.pk","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","imran.hashim@pbc.com.pk","ahsan.rashid@optimus-uae.com","muhammadameer@hotmail.com","rafiquemeo@yahoo.com","shehzadaleem@hotmail.com"},null, "Weekly Score Card | SD2 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(EmployeeHierarchy.getSDHead(2).USER_ID), new String[]{filename_distributor_sd2,filename_zerosales_sd2,filename_orderbooker_sd2});
			Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(4).USER_EMAIL},new String[]{"omerfk@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","shahzad.ahmed@pbc.com.pk","nadeem@pbc.com.pk","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","imran.hashim@pbc.com.pk","ahsan.rashid@optimus-uae.com","muhammadameer@hotmail.com","rafiquemeo@yahoo.com","shehzadaleem@hotmail.com"},null, "Weekly Score Card | SD3 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(EmployeeHierarchy.getSDHead(4).USER_ID), new String[]{filename_distributor_sd3,filename_zerosales_sd3,filename_orderbooker_sd3});
			
			

			
			
//			Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null,null, "Weekly Score Card | SD1 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(EmployeeHierarchy.getSDHead(1).USER_ID), new String[]{filename_distributor_sd1,filename_zerosales_sd1,filename_orderbooker_sd1});			
//			Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null,null, "Weekly Score Card | SD2 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(EmployeeHierarchy.getSDHead(2).USER_ID), new String[]{filename_distributor_sd2,filename_zerosales_sd2,filename_orderbooker_sd2});
//			Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null,null, "Weekly Score Card | SD3 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(EmployeeHierarchy.getSDHead(4).USER_ID), new String[]{filename_distributor_sd3,filename_zerosales_sd3,filename_orderbooker_sd3});
			
			
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
		ds.createConnection();
		
		
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
			
			Date CurrentDate = Utilities.getDateByDays(difference-1);
			
			if (Utilities.getDayOfWeekByDate(CurrentDate) != 6){ // Exclude Friday
				
				double TotalPlannedCalls = 0;
				double TotalOrders = 0;
				double TotalBills = 0;
				
				
				
				ResultSet rs80 = s5.executeQuery("select distinct dbpl.assigned_to, (select display_name from users where id = dbpl.assigned_to) orderbooker_name, (select is_active from users where id = dbpl.assigned_to) is_active from distributor_beat_plan_log dbpl force index (distributor_beat_plan_log_date_assigned_day_number) where dbpl.log_date between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDate(CurrentDate)+" and dbpl.distributor_id in (select distributor_id from common_distributors where is_active = 1 and (snd_id in ("+EmployeeID+") or rsm_id in ("+EmployeeID+"))) and dbpl.distributor_id in (select distinct distributor_id from mobile_order where created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+") and dbpl.day_number = "+Utilities.getDayOfWeekByDate(CurrentDate)+" and dbpl.assigned_to is not null");
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
					}catch(SQLException se1){System.out.println(se1);}
					
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
				Datasource dsSAP = new Datasource();
				dsSAP.createConnectionToSAPDB();
				Statement sSAP = dsSAP.createStatement();
				String WhereHOD = " and vbak.kunnr in ("+UserAccess.getDistributorQueryString(UserAccess.getSNDDistributors(EmployeeID))+") ";
				String Query = "SELECT vbak.vbeln order_number, vbak.kunnr distributor_id, vbak.erdat entry_date, vbak.audat order_date, (vbak.netwr + (select sum(mwsbp) from "+ds.getSAPDatabaseAlias()+".vbap where vbeln = vbak.vbeln)) /* Tax Amount from VBAP and Order Amount from VBAP-NETWR*/ order_amount, kna1.name1 distributor_name, vbuk.fksak FROM "+ds.getSAPDatabaseAlias()+".vbak vbak join "+ds.getSAPDatabaseAlias()+".vbuk vbuk on vbak.vbeln = vbuk.vbeln join "+ds.getSAPDatabaseAlias()+".kna1 kna1 on vbak.kunnr = kna1.kunnr where vbak.auart in ('ZDIS','ZMRS','ZDFR') and vbak.audat between "+Utilities.getSQLDateOracle(CurrentDate)+" and "+Utilities.getSQLDateOracle(CurrentDate)+" and vbuk.vbtyp = 'C' "+WhereHOD+" and kna1.CASSD != 'X' and kna1.FAKSD != 'X' order by vbak.audat";
				ResultSet rsSAP = sSAP.executeQuery(Query);
				while(rsSAP.next()){
					PrimaryOrderAmount += rsSAP.getDouble("order_amount");
				}
				PrimaryOrdersSeries += (PrimaryOrderAmount / 1000000);
				sSAP.close();
				dsSAP.dropConnection();
				
				
				
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
		ResultSet rs = s.executeQuery("SELECT count(distinct outlet_id) FROM distributor_beat_plan_view dbpv join common_distributors cd on dbpv.distributor_id = cd.distributor_id where (cd.snd_id = "+EmployeeID+" or cd.rsm_id = "+EmployeeID+") and cd.distributor_id in (select distinct distributor_id from inventory_sales_adjusted)  ");
		if (rs.first()){
			OutletUniverse = rs.getLong(1);
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
				"SELECT distinct outlet_id FROM distributor_beat_plan_view dbpv join common_distributors cd on dbpv.distributor_id = cd.distributor_id where (cd.snd_id = "+EmployeeID+" or cd.rsm_id = "+EmployeeID+") and cd.distributor_id in (select distinct distributor_id from inventory_sales_adjusted) "+
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
		
		
		// Chillers
		int ActiveChillers = 0;
		ResultSet rs3 = s.executeQuery("select count(*) from common_assets where tot_status = 'INJECTED' and outlet_id_parsed in (select outlet_id from distributor_beat_plan_all_view where snd_id = "+EmployeeID+" or rsm_id = "+EmployeeID+")");
		if (rs3.first()){
			ActiveChillers = rs3.getInt(1);
		}
		
		double ChillerRevenue = 0;
//		ResultSet rs6 = s.executeQuery("select sum(net_amount) from inventory_sales_adjusted where created_on between from_days(to_days(curdate())-30) and from_days(to_days(curdate())+1) and distributor_id in (select distributor_id from common_distributors where (snd_id in ("+EmployeeID+") or rsm_id in ("+EmployeeID+")))");
//		if (rs6.first()){
//			ChillerRevenue = rs6.getDouble(1);
//		}
		
		
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
				html += "<td colspan='2' style='text-align: center;'><img src='https://chart.googleapis.com/chart?cht=lc&chd=t:"+PrimaryOrdersSeries+"|"+LiftingSeries+"|"+CashSeries+"&chds=a&chs=400x250&chl=15 Days Ago|Yesterday&chxt=y,r&chco=bda609,0963bd,f0516c&chdl=Orders|Lifting|Cash&chdlp=b&chtt=(millions)'></td>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td colspan='2' style='text-align: center;'>&nbsp;</td>";
				html += "</tr>";
				
				
				Date WeekDates[] = Utilities.getCurrentWeekInDates();
				Date wStartDate = WeekDates[0];
				Date wEndDate = WeekDates[1];
				
				
				/*
				html += "<tr>";
				html += "<td colspan='2' style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold;'>Average Price</td>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td colspan='2' style='background-color: #E8E8E8; text-align: left;'><table style='width: 100%'><tr><td style='text-align: left; width: 20%'>Package</td><td style='text-align: center;width: 20%'>Distributor</td><td style='text-align: center;width: 20%'>Wholesale</td><td style='text-align: center;width: 20%'>DSD</td><td style='text-align: center;width: 20%'>All</td></tr>";
				
				
				
				int Packages[] = new int[5];
				Packages[0] = 24;
				Packages[1] = 2;
				Packages[2] = 3;
				Packages[3] = 5;
				Packages[4] = 6;
				
				String sPackages[] = new String[5];
				sPackages[0] = "1750 ML";
				sPackages[1] = "1500 ML";
				sPackages[2] = "1000 ML";
				sPackages[3] = "2250 ML";
				sPackages[4] = "500 ML";
				
				for (int i = 0; i < 5; i++){
				
					double AvgPriceDistributor = 0;
					double AvgPriceWholeSale = 0;
					double AvgPriceLKA = 0;
					double AvgPriceNKA = 0;
					double AvgPriceAll = 0;

					String sAvgPriceDistributor = "";
					String sAvgPriceWholeSale = "";
					String sAvgPriceLKA = "";
					String sAvgPriceNKA = "";
					String sAvgPriceAll = "";
					
					
					
					
					System.out.println("SELECT cd.category_id, (select label from common_distributors_categories where id = cd.category_id), ((sum(gross_value)+sum(unloading)+sum(freight)+sum(upfront_discount))-sum(ifnull(free_stock,0)))/sum(quantity) avg_price FROM peplogs.bi_average_price_invoice biapi join pep.common_distributors cd on biapi.customer_id = cd.distributor_id where kurrf_dat between "+Utilities.getSQLDate(wStartDate)+" and "+Utilities.getSQLDate(wEndDate)+" and package_id = "+Packages[i]+" and cd.snd_id = "+EmployeeID+" and cd.category_id in (5,4,3) and biapi.product_type_id = 1  and cd.distributor_id != 200769 and vbeln != 9000390667 group by cd.category_id");
					ResultSet rs8 = s.executeQuery("SELECT cd.category_id, (select label from common_distributors_categories where id = cd.category_id), ((sum(gross_value)+sum(unloading)+sum(freight)+sum(upfront_discount))-sum(ifnull(free_stock,0)))/sum(quantity) avg_price FROM peplogs.bi_average_price_invoice biapi join pep.common_distributors cd on biapi.customer_id = cd.distributor_id where kurrf_dat between "+Utilities.getSQLDate(wStartDate)+" and "+Utilities.getSQLDate(wEndDate)+" and package_id = "+Packages[i]+" and cd.snd_id = "+EmployeeID+" and cd.category_id in (5,4,3) and biapi.product_type_id = 1  and cd.distributor_id != 200769 and vbeln != 9000390667 group by cd.category_id");
					while(rs8.next()){
						int CategoryID = rs8.getInt(1);
						

						
						if (CategoryID == 5){
							AvgPriceDistributor = rs8.getDouble(3);
						}
						if (CategoryID == 4){
							AvgPriceWholeSale = rs8.getDouble(3);
						}
						if (CategoryID == 7){
							AvgPriceLKA = rs8.getDouble(3);
						}
						if (CategoryID == 3){
							AvgPriceNKA = rs8.getDouble(3);
						}
						
						
					}
					
					System.out.println("SELECT ((sum(gross_value)+sum(unloading)+sum(freight)+sum(upfront_discount))-sum(ifnull(free_stock,0)))/sum(quantity) avg_price FROM peplogs.bi_average_price_invoice biapi join pep.common_distributors cd on biapi.customer_id = cd.distributor_id where kurrf_dat between "+Utilities.getSQLDate(wStartDate)+" and "+Utilities.getSQLDate(wEndDate)+" and package_id = "+Packages[i]+" and cd.snd_id = "+EmployeeID+" and cd.distributor_id != 200769 and vbeln != 9000390667 and  biapi.product_type_id = 1");
					ResultSet rs9 = s.executeQuery("SELECT ((sum(gross_value)+sum(unloading)+sum(freight)+sum(upfront_discount))-sum(ifnull(free_stock,0)))/sum(quantity) avg_price FROM peplogs.bi_average_price_invoice biapi join pep.common_distributors cd on biapi.customer_id = cd.distributor_id where kurrf_dat between "+Utilities.getSQLDate(wStartDate)+" and "+Utilities.getSQLDate(wEndDate)+" and package_id = "+Packages[i]+" and cd.snd_id = "+EmployeeID+" and cd.distributor_id != 200769 and vbeln != 9000390667 and biapi.product_type_id = 1");
					while(rs9.next()){
						AvgPriceAll = rs9.getDouble(1);
					}
					
					if (AvgPriceDistributor != 0){
						sAvgPriceDistributor = ""+Utilities.getDisplayCurrencyFormatRounded(AvgPriceDistributor);
					}
					if (AvgPriceWholeSale != 0){
						sAvgPriceWholeSale = ""+Utilities.getDisplayCurrencyFormatRounded(AvgPriceWholeSale);
					}
					if (AvgPriceLKA != 0){
						sAvgPriceLKA = ""+Utilities.getDisplayCurrencyFormatRounded(AvgPriceLKA);
					}
					if (AvgPriceNKA != 0){
						sAvgPriceNKA = ""+Utilities.getDisplayCurrencyFormatRounded(AvgPriceNKA);
					}
					if (AvgPriceAll != 0){
						sAvgPriceAll = ""+Utilities.getDisplayCurrencyFormatRounded(AvgPriceAll);
					}
					
					html +="<tr><td style='background-color: white; text-align: left;'>"+sPackages[i]+"</td><td style='background-color: white; text-align: center;'>"+sAvgPriceDistributor+"</td><td style='background-color: white; text-align: center;'>"+sAvgPriceWholeSale+"</td><td style='background-color: white; text-align: center;'>"+sAvgPriceNKA+"</td><td style='background-color: #F8F8F8; text-align: center; font-weight: normal;'>"+sAvgPriceAll+"</td></tr>";
				
				}
				
				
				html += "</table></td>";
				html += "</tr>";

				html += "<tr>";
				html += "<td colspan='2' style='text-align: left; font-size: 10px;'>* Includes promotions, upfront discount, freight and unloading for this week.<br>* Exclusive of Sting and Aquafina<br>* DSD = Direct Store Delivery<br></td>";
				html += "</tr>";

				*/
				html += "<tr>";
				html += "<td colspan='2' style='text-align: center;'>&nbsp;</td>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td colspan='2' style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold;'>Chillers</td>";
				html += "</tr>";
				
				/*
				html += "<tr>";
				html += "<td colspan='2' style='background-color: #E8E8E8; text-align: left;'><table style='width: 100%'><tr><td style='text-align: center; width: 33%;'>Active</td><td style='text-align: center;width: 33.5%;'>Revenue/Day* (Rs.)</td><td style='text-align: center;width: 33.5%;'>CC/Day*</td></tr><tr><td style='background-color: white; text-align: center;'>"+Utilities.getDisplayCurrencyFormatRounded(ActiveChillers)+"</td><td style='background-color: white; text-align: center;'>"+Utilities.getDisplayCurrencyFormatRounded(RevenuePerChiller)+"</td><td style='background-color: white; text-align: center;'>"+Utilities.getDisplayCurrencyFormatOneDecimal(CCPerChiller)+"</td></tr></table></td>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td colspan='2' style='text-align: left; font-size: 10px;'>Revenue/Day = Average Revenue per chiller per day in last 30 days<br>CC/Day = Average Sales in Converted Cases per chiller per day in last 30 days</td>";
				html += "</tr>";
				
				*/
				
				
				html += "<tr>";
				html += "<td colspan='2' style='background-color: #E8E8E8; text-align: left;'><table style='width: 100%'><tr><td style='text-align: center; width: 50%;'>Active</td><td style='text-align: center;width: 50%;'>CC/Day*</td></tr><tr><td style='background-color: white; text-align: center;'>"+Utilities.getDisplayCurrencyFormatRounded(ActiveChillers)+"</td><td style='background-color: white; text-align: center;'>"+Utilities.getDisplayCurrencyFormatOneDecimal(CCPerChiller)+"</td></tr></table></td>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td colspan='2' style='text-align: left; font-size: 10px;'>CC/Day = Average Sales in Converted Cases per chiller per day in last 30 days</td>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td colspan='2' style='text-align: left; font-size: 10px;'>&nbsp;</td>";
				html += "</tr>";
				/*
				html += "<tr>";
					html += "<td colspan='2' style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold;'>Secondary Sales</td>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td colspan='2' style='background-color: #E8E8E8; height: 18px; text-align: center; font-weight: normal;'>Productivity</td>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td colspan='2' style='text-align: center;'><img src='https://chart.googleapis.com/chart?cht=lc&chd=t:"+OrderSeries+"|"+BillSeries+"&chds=a&chs=400x250&chl=15 Days Ago|Yesterday&chxt=y,r&chco=bda609,0963bd&chdl=Order%20Productivity|Bill%20Productivity&chdlp=bv&chtt=%age'></td>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td colspan='2' style='text-align: center;'>&nbsp;</td>";
				html += "</tr>";

				
				html += "<tr>";
				html += "<td colspan='2' style='background-color: #E8E8E8; height: 18px; text-align: center; font-weight: normal;'>Secondary Sales</td>";
				html += "</tr>";
				
				
				html += "<tr>";
				html += "<td colspan='2' style='text-align: center;'><img src='https://chart.googleapis.com/chart?cht=lc&chd=t:"+SecondarySalesSeries+"&chds=a&chs=400x250&chl=15 Days Ago|Yesterday&chxt=y,r&chco=bda609,0963bd,f0516c&chdl=Secondary Sales&chdlp=b&chtt=(millions)'></td>";
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
				html += "<td colspan='2' style='text-align: center;'><img src='https://chart.googleapis.com/chart?cht=lc&chd=t:"+BackordersSeries+"|"+SalesReturnSeries+"&chds=a&chs=400x250&chl=15 Days Ago|Yesterday&chxt=y,r&chco=bda609,0963bd,f0516c&chdl=Backorders|Returns&chdlp=b&chtt=(Converted Cases)'></td>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td colspan='2' style='text-align: center;'>&nbsp;</td>";
				html += "</tr>";
				*/
			html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold; width: 50%'>Outlet Universe</td>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold; width: 50%'>Zero Sale Outlets*</td>";
			html += "</tr>";
			
			html += "<tr>";
				html += "<td style='text-align: center; background-color: #C8CFE6;'>"+Utilities.getDisplayCurrencyFormatRounded(OutletUniverse)+"</td>";
				html += "<td style='text-align: center; background-color: #C8CFE6;'>"+Utilities.getDisplayCurrencyFormatRounded(ZeroSaleOutletsToDisplay)+" ("+Utilities.getDisplayCurrencyFormatRounded(ZeroPercentage)+"%)</td>";
			html += "</tr>";
			html += "<tr>";
			html += "<td colspan='2' style='text-align: left; font-size: 10px;'>* based on 30 days of inactivity</td>";
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
			/*
			html += "<tr>";
			html += "<td colspan='2' style='background-color: #E8E8E8; height: 18px; text-align: left; font-weight: bold;'>Outlets with Lowest Revenue / Chiller</td>";
			html += "</tr>";
			
			
			html += "<tr>";
			html += "<td colspan='2' style='background-color: #E8E8E8; text-align: left;'><table style='width: 100%'><tr><td style='text-align: left; width: 50%'>Outlet</td><td style='text-align: center;width: 25%'>Chillers</td><td style='text-align: center;width: 25%'>Revenue/Day* (Rs.)</td></tr>";
				
			
			ResultSet rs7 = s.executeQuery("select outlet_id, (select name from common_outlets where id = tab1.outlet_id) outlet_name, (select concat(distributor_id,'-',distributor_name) from distributor_beat_plan_all_view where outlet_id = tab1.outlet_id limit 1) distributor, cases, ((cases/26)/(select count(*) from common_assets where tot_status = 'INJECTED' and outlet_id_parsed = tab1.outlet_id)) revenue, (select count(*) from common_assets where tot_status = 'INJECTED' and outlet_id_parsed = tab1.outlet_id) tots from ("+
					"SELECT isa.outlet_id, sum(isa.net_amount) cases FROM pep.inventory_sales_adjusted isa where isa.created_on between from_days(to_days(curdate())-30) and from_days(to_days(curdate())+1) and isa.distributor_id in (select distributor_id from common_distributors where snd_id = "+EmployeeID+" or rsm_id in ("+EmployeeID+")) and net_amount != 0 group by isa.outlet_id order by cases limit 100"+
					") tab1 having tots != 0 order by revenue limit 15");
			while (rs7.next()){
				html +="<tr><td style='background-color: white; text-align: left;'>"+Utilities.truncateStringToMax(rs7.getString(1)+"-"+rs7.getString(2),26)+"</td><td style='background-color: white; text-align: center;'>"+rs7.getString("tots")+"</td><td style='background-color: white; text-align: center;'>"+Utilities.getDisplayCurrencyFormatRounded(rs7.getDouble("revenue"))+"</td></tr>";
			}
			
			html += "</table></td>";
			html += "</tr>";
			
			html += "<tr>";
			html += "<td colspan='2' style='text-align: left; font-size: 10px;'>Revenue/Day = Average Revenue per chiller per day in last 30 days</td>";
			html += "</tr>";
			
			html += "<tr>";
			html += "<td colspan='2' style='text-align: left; font-size: 10px;'>&nbsp;</td>";
			html += "</tr>";
			*/
			html += "<tr>";
			html += "<td colspan='2' style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold;'>Lowest Order SKUs This Week</td>";
			html += "</tr>";
			
			
			
			html += "<tr>";
			html += "<td colspan='2' style='text-align: left;'><table style='width: 100%; border-collapse: collapse;'><tr style='background-color: #E9EBF2'><td style='text-align: left;' colspan='2'><b>SKU</b></td><td style='text-align: right;'><b>Cases</b></td></tr>";
			
			
			
			String background = "";
			boolean alternate = false;
			ResultSet rs4 = s.executeQuery("select ipv.product_id, ipv.package_label, ipv.brand_label, ifnull((select sum(raw_cases) orders from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mo.created_on between from_days(to_days(curdate())-7) and from_days(to_days(curdate())+1) and distributor_id in (select distributor_id from common_distributors where snd_id = "+EmployeeID+" or rsm_id = "+EmployeeID+") and mop.is_promotion = 0 and mop.product_id = ipv.product_id),0) cases from inventory_products_view ipv where ipv.product_id in (3,5,6,108,140,11,13,14,16,17,31,33,34,36,37,42,44,45,47,48,115,67,70,80,81,83,85,86,88,111,139) order by cases limit 10");
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
			
			
			html += "</table>";
				
			html += "<br><br>";
				
			html += "Please see attachment for details.<br>This is a system generated email, please do not reply to it.";
			
			
		
		s.close();
		ds.dropConnection();	
		
		return html;
		
	}

	
}
