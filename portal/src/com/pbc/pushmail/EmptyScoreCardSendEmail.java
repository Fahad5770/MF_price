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
import com.pbc.reports.EmptyReconciliation;

public class EmptyScoreCardSendEmail {
	
	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	String HTMLMessage;
    
	public static void main(String[] args) throws DocumentException, IOException{
		try {
			EmptyScoreCardSendEmail pe = new EmptyScoreCardSendEmail();
			pe.setHTMLMessage();
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			Utilities.sendPBCHTMLEmail( new String[]{"naveed.ahmad@pbc.com.pk","shafqat.riaz@pbc.com.pk","sohail.shaukat@pbc.com.pk","ather.nazir@pbc.com.pk"},new String[]{"omerfk@pbc.com.pk","anas.wahab@pbc.com.pk"}, new String[]{"shahrukh.salman@pbc.com.pk"}, "Glass Breakage Summary | "+Utilities.getDisplayDateFormat(Utilities.getDateByDays(-1)), pe.getHTMLMessage(), null);
			//Utilities.sendPBCHTMLEmail( new String[]{"dev@pbc.com.pk"}, null, null, "Glass Breakage Summary | "+Utilities.getDisplayDateFormat(Utilities.getDateByDays(-1)), pe.getHTMLMessage(), null);
			//pe.getHTMLMessage();
			
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
	
	public EmptyScoreCardSendEmail() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		ds.createConnection();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();
	}
	

	public void setHTMLMessage() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		
		
		
		
		
		//////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////
		
		
		
		
		
		//This Week Start Date
		Date ThisWeekEndDate=Utilities.getDateByDays(-1);// new Date();
		Date ThisWeekStartDate=Utilities.getDateByDays(ThisWeekEndDate,-6);
		
		
		//System.out.println(ThisWeekStartDate +" - "+ThisWeekEndDate );
		
		
		EmptyReconciliation ER = new EmptyReconciliation();
		
		long GrandTotalEmpty240=0;
		long GrandTotalEmpty250=0;
		long GrandTotalEmpty1000=0;
		
		long NewEmptyGTotalRawCases240=0;
		long NewEmptyGTotalUnits240=0;
		
		
		
		
		
		
	 	
	 	
		
		
		
		////////////////////////////// Month To Date /////////////////////////////
		
		
		//
		 		
		     
		
		//2015		
		
				Date MTDEndDate= Utilities.getDateByDays(-1);// new Date();
				Date MTDStartDate= Utilities.getStartDateByDate(MTDEndDate);
				
				//System.out.println(ThisWeekStarDate +" - "+ThisWeekEndDate );
				
				
				EmptyReconciliation ERMTD = new EmptyReconciliation();
				
				long MTDGrandTotalEmpty240=0;
				long MTDGrandTotalEmpty250=0;
				long MTDGrandTotalEmpty1000=0;
				
				long MTDNewEmptyGTotalRawCases240=0;
				long MTDNewEmptyGTotalUnits240=0;
				
				
				
				
				
				
			 	long MTDZabtAllowanceRawCase250 = ERMTD.getZabtAllowance(MTDStartDate,MTDEndDate,11);
			 	long MTDZabtAllowanceRawCase1000 = ERMTD.getZabtAllowance(MTDStartDate,MTDEndDate,1);
			 	
			 	long MTDBreakageAllowanceRawCase250 = ERMTD.getBreakageAllowance(MTDStartDate,MTDEndDate,11);
			 	long MTDBreakageAllowanceRawCase1000 = ERMTD.getBreakageAllowance(MTDStartDate,MTDEndDate,1);
				
			 	
				long MTDNewEmptyGTotalRawCases250= ERMTD.getNewEmptyOpen(MTDStartDate,MTDEndDate,11);
			 	long MTDNewEmptyGTotalRawCases1000=ERMTD.getNewEmptyOpen(MTDStartDate,MTDEndDate,1);
				
			 	
				long MTDOldEmptyGTotalRawCases250=ERMTD.getOldEmptyPurchase(MTDStartDate,MTDEndDate,11);
				long MTDOldEmptyGTotalRawCases1000=ERMTD.getOldEmptyPurchase(MTDStartDate,MTDEndDate,1);
				
				long MTDGTotalRawCases250=ER.getAgencyPurchase(MTDStartDate,MTDEndDate,11);
				long MTDGTotalRawCases1000=ER.getAgencyPurchase(MTDStartDate,MTDEndDate,1);
				
				MTDGrandTotalEmpty250 = MTDNewEmptyGTotalRawCases250+MTDOldEmptyGTotalRawCases250+MTDGTotalRawCases250;
				MTDGrandTotalEmpty1000 = MTDNewEmptyGTotalRawCases1000+MTDOldEmptyGTotalRawCases1000+MTDGTotalRawCases1000;
				
				
				//Losses
				
				double MTDLossTotal240=0;
				double MTDLossTotal250=0;
				double MTDLossTotal1000=0;
				
				long[] MTDRawCasesUnitsSLCBR250={0,0};
				long[] MTDRawCasesUnitsSpecialBreakage250={0,0};
				long[] MTDRawCasesUnitsSpecialBurst250={0,0};
				long[] MTDRawCasesUnitsSpecialZabt250={0,0};
				
				long[] MTDRawCasesUnitsSLCBR1000={0,0};
				long[] MTDRawCasesUnitsSpecialBreakage1000={0,0};
				long[] MTDRawCasesUnitsSpecialBurst1000={0,0};
				long[] MTDRawCasesUnitsSpecialZabt1000={0,0};
				
				MTDRawCasesUnitsSLCBR250 = ERMTD.getSLCBR(MTDStartDate,MTDEndDate,11);
				MTDRawCasesUnitsSpecialBreakage250 = ERMTD.getSpecialBreakage(MTDStartDate,MTDEndDate,11);
				MTDRawCasesUnitsSpecialBurst250 = ERMTD.getSpecialBurst(MTDStartDate,MTDEndDate,11);
				MTDRawCasesUnitsSpecialZabt250 = ERMTD.getSpecialZabt(MTDStartDate,MTDEndDate,11);
				
				MTDRawCasesUnitsSLCBR1000 = ERMTD.getSLCBR(MTDStartDate,MTDEndDate,1);
				MTDRawCasesUnitsSpecialBreakage1000 = ERMTD.getSpecialBreakage(MTDStartDate,MTDEndDate,1);
				MTDRawCasesUnitsSpecialBurst1000 = ERMTD.getSpecialBurst(MTDStartDate,MTDEndDate,1);
				MTDRawCasesUnitsSpecialZabt1000 = ERMTD.getSpecialZabt(MTDStartDate,MTDEndDate,1);
				
				long [] MTDRawCasesUnitsDepotBurst250={0,0};
				long [] MTDRawCasesUnitsDepotFallenBurst250={0,0};
				long [] MTDRawCasesUnitsLifterBreakage250={0,0};
				long [] MTDRawCasesUnitsLifterBurst250={0,0};
				long [] MTDRawCasesUnitsWashBreakage250={0,0};
				long [] MTDRawCasesUnitsFallenBreakage250={0,0};
				
				long [] MTDRawCasesUnitsDepotBurst1000={0,0};
				long [] MTDRawCasesUnitsDepotFallenBurst1000={0,0};
				long [] MTDRawCasesUnitsLifterBreakage1000={0,0};
				long [] MTDRawCasesUnitsLifterBurst1000={0,0};
				long [] MTDRawCasesUnitsWashBreakage1000={0,0};
				long [] MTDRawCasesUnitsFallenBreakage1000={0,0};
				
				
				MTDRawCasesUnitsDepotBurst250 = ERMTD.getDepotBurst(MTDStartDate,MTDEndDate,11);
				MTDRawCasesUnitsDepotFallenBurst250 = ERMTD.getDepotFallenBurst(MTDStartDate,MTDEndDate,11);
				MTDRawCasesUnitsLifterBreakage250 = ERMTD.getLifterBreakage(MTDStartDate,MTDEndDate,11);
				MTDRawCasesUnitsLifterBurst250 = ERMTD.getLifterBurst(MTDStartDate,MTDEndDate,11);
				MTDRawCasesUnitsWashBreakage250 = ERMTD.getWashBreakage(MTDStartDate,MTDEndDate,11);
				MTDRawCasesUnitsFallenBreakage250 = ERMTD.getFallenBreakage(MTDStartDate,MTDEndDate,11);
				
				MTDRawCasesUnitsDepotBurst1000 = ERMTD.getDepotBurst(MTDStartDate,MTDEndDate,1);
				MTDRawCasesUnitsDepotFallenBurst1000 = ERMTD.getDepotFallenBurst(MTDStartDate,MTDEndDate,1);
				MTDRawCasesUnitsLifterBreakage1000 = ERMTD.getLifterBreakage(MTDStartDate,MTDEndDate,1);
				MTDRawCasesUnitsLifterBurst1000 = ERMTD.getLifterBurst(MTDStartDate,MTDEndDate,1);
				MTDRawCasesUnitsWashBreakage1000 = ERMTD.getWashBreakage(MTDStartDate,MTDEndDate,1);
				MTDRawCasesUnitsFallenBreakage1000 = ERMTD.getFallenBreakage(MTDStartDate,MTDEndDate,1);
				
				long[] MTDRawCasesUnitsDuringPro250=ERMTD.getDuringProductionShippingBreakage(MTDStartDate,MTDEndDate,11);
				long[] MTDRawCasesUnitsDuringPro1000=ERMTD.getDuringProductionShippingBreakage(MTDStartDate,MTDEndDate,1);
				
				int MTDTotalUnitsLoss1RawCases250=ERMTD.getDuringProductionBreakage(MTDStartDate,MTDEndDate,11);
				int MTDTotalUnitsLoss1RawCases1000=ERMTD.getDuringProductionBreakage(MTDStartDate,MTDEndDate,1);
				
				
				long MTDRawCasesUnitsEmptyReturn250=0;
				long MTDRawCasesUnitsEmptyReturn1000=0;
				
				MTDRawCasesUnitsEmptyReturn250 = ERMTD.getEmptyReturnToStore(MTDStartDate,MTDEndDate,11);						
				MTDRawCasesUnitsEmptyReturn1000 = ERMTD.getEmptyReturnToStore(MTDStartDate,MTDEndDate,1);
				
				long[] MTDRawCasesUnitsEmptySale250=ERMTD.getEmptySales(MTDStartDate,MTDEndDate,11);
				long[] MTDRawCasesUnitsEmptySale1000=ERMTD.getEmptySales(MTDStartDate,MTDEndDate,1);
				
				
				
				
				
				long MTDTotalMarketLoss250= MTDZabtAllowanceRawCase250+MTDBreakageAllowanceRawCase250;
				
				long MTDTotalMarketLoss1000= MTDZabtAllowanceRawCase1000+MTDBreakageAllowanceRawCase1000;
				
				long MTDTotalSupplyChainLoss250= MTDRawCasesUnitsSLCBR250[0]+MTDRawCasesUnitsSpecialBreakage250[0]+MTDRawCasesUnitsSpecialBurst250[0]+
						MTDRawCasesUnitsDepotBurst250[0]+MTDRawCasesUnitsDepotFallenBurst250[0]+MTDRawCasesUnitsLifterBreakage250[0]+MTDRawCasesUnitsLifterBurst250[0]+MTDRawCasesUnitsFallenBreakage250[0]+
						   MTDRawCasesUnitsDuringPro250[0]+MTDRawCasesUnitsWashBreakage250[0]+MTDRawCasesUnitsSpecialZabt250[0];
				
				
				long MTDTotalSupplyChainLoss1000= MTDRawCasesUnitsSLCBR1000[0]+MTDRawCasesUnitsSpecialBreakage1000[0]+MTDRawCasesUnitsSpecialBurst1000[0]+
						MTDRawCasesUnitsDepotBurst1000[0]+MTDRawCasesUnitsDepotFallenBurst1000[0]+MTDRawCasesUnitsLifterBreakage1000[0]+MTDRawCasesUnitsLifterBurst1000[0]+MTDRawCasesUnitsFallenBreakage1000[0]+
						   MTDRawCasesUnitsDuringPro1000[0]+MTDRawCasesUnitsWashBreakage1000[0]+MTDRawCasesUnitsSpecialZabt1000[0];
				
				////////////////////////////////////////////////////////////////////////
				
				
				//2014		
				Calendar now = Calendar.getInstance();
				now.add(Calendar.YEAR, -1);
				now.add(Calendar.DATE, -1);
				
				Date MTDEndDate2014=now.getTime();
				Date MTDStartDate2014= Utilities.getStartDateByDate(MTDEndDate2014);
				
			   // System.out.println(MTDStartDate2014+" - "+MTDEndDate2014);
				
				System.out.println(MTDStartDate2014 +" - "+MTDEndDate2014 );
				
				
				EmptyReconciliation ERMTD2014 = new EmptyReconciliation();
				
				long MTDGrandTotalEmpty2402014=0;
				long MTDGrandTotalEmpty2502014=0;
				long MTDGrandTotalEmpty10002014=0;
				
				long MTDNewEmptyGTotalRawCases2402014=0;
				long MTDNewEmptyGTotalUnits2402014=0;
				
				
				
				
				
				long MTDZabtAllowanceRawCase2502014 = ERMTD.getZabtAllowance(MTDStartDate2014,MTDEndDate2014,11);
			 	long MTDZabtAllowanceRawCase10002014 = ERMTD.getZabtAllowance(MTDStartDate2014,MTDEndDate2014,1);
			 	
			 	long MTDBreakageAllowanceRawCase2502014 = ERMTD.getBreakageAllowance(MTDStartDate2014,MTDEndDate2014,11);
			 	long MTDBreakageAllowanceRawCase10002014 = ERMTD.getBreakageAllowance(MTDStartDate2014,MTDEndDate2014,1);
			 	
			 	
				long MTDNewEmptyGTotalRawCases2502014= ERMTD2014.getNewEmptyOpen(MTDStartDate2014,MTDEndDate2014,11);
			 	long MTDNewEmptyGTotalRawCases10002014=ERMTD2014.getNewEmptyOpen(MTDStartDate2014,MTDEndDate2014,1);
				
			 	
				long MTDOldEmptyGTotalRawCases2502014=ERMTD2014.getOldEmptyPurchase(MTDStartDate2014,MTDEndDate2014,11);
				long MTDOldEmptyGTotalRawCases10002014=ERMTD2014.getOldEmptyPurchase(MTDStartDate2014,MTDEndDate2014,1);
				
				long MTDGTotalRawCases2502014=ERMTD2014.getAgencyPurchase(MTDStartDate2014,MTDEndDate2014,11);
				long MTDGTotalRawCases10002014=ERMTD2014.getAgencyPurchase(MTDStartDate2014,MTDEndDate2014,1);
				
				MTDGrandTotalEmpty2502014 = MTDNewEmptyGTotalRawCases2502014+MTDOldEmptyGTotalRawCases2502014+MTDGTotalRawCases2502014;
				MTDGrandTotalEmpty10002014 = MTDNewEmptyGTotalRawCases10002014+MTDOldEmptyGTotalRawCases10002014+MTDGTotalRawCases10002014;
				
				
				//Losses
				
				double MTDLossTotal2402014=0;
				double MTDLossTotal2502014=0;
				double MTDLossTotal10002014=0;
				
				long[] MTDRawCasesUnitsSLCBR2502014={0,0};
				long[] MTDRawCasesUnitsSpecialBreakage2502014={0,0};
				long[] MTDRawCasesUnitsSpecialBurst2502014={0,0};
				long[] MTDRawCasesUnitsSpecialZabt2502014={0,0};
				
				long[] MTDRawCasesUnitsSLCBR10002014={0,0};
				long[] MTDRawCasesUnitsSpecialBreakage10002014={0,0};
				long[] MTDRawCasesUnitsSpecialBurst10002014={0,0};
				long[] MTDRawCasesUnitsSpecialZabt10002014={0,0};
				
				MTDRawCasesUnitsSLCBR2502014 = ERMTD2014.getSLCBR(MTDStartDate2014,MTDEndDate2014,11);
				MTDRawCasesUnitsSpecialBreakage2502014 = ERMTD2014.getSpecialBreakage(MTDStartDate2014,MTDEndDate2014,11);
				MTDRawCasesUnitsSpecialBurst2502014 = ERMTD2014.getSpecialBurst(MTDStartDate2014,MTDEndDate2014,11);
				MTDRawCasesUnitsSpecialZabt2502014 = ERMTD2014.getSpecialZabt(MTDStartDate2014,MTDEndDate2014,11);
				
				MTDRawCasesUnitsSLCBR10002014 = ERMTD2014.getSLCBR(MTDStartDate2014,MTDEndDate2014,1);
				MTDRawCasesUnitsSpecialBreakage10002014 = ERMTD.getSpecialBreakage(MTDStartDate2014,MTDEndDate2014,1);
				MTDRawCasesUnitsSpecialBurst10002014 = ERMTD2014.getSpecialBurst(MTDStartDate2014,MTDEndDate2014,1);
				MTDRawCasesUnitsSpecialZabt10002014 = ERMTD2014.getSpecialZabt(MTDStartDate2014,MTDEndDate2014,1);
				
				long [] MTDRawCasesUnitsDepotBurst2502014={0,0};
				long [] MTDRawCasesUnitsDepotFallenBurst2502014={0,0};
				long [] MTDRawCasesUnitsLifterBreakage2502014={0,0};
				long [] MTDRawCasesUnitsLifterBurst2502014={0,0};
				long [] MTDRawCasesUnitsWashBreakage2502014={0,0};
				long [] MTDRawCasesUnitsFallenBreakage2502014={0,0};
				
				long [] MTDRawCasesUnitsDepotBurst10002014={0,0};
				long [] MTDRawCasesUnitsDepotFallenBurst10002014={0,0};
				long [] MTDRawCasesUnitsLifterBreakage10002014={0,0};
				long [] MTDRawCasesUnitsLifterBurst10002014={0,0};
				long [] MTDRawCasesUnitsWashBreakage10002014={0,0};
				long [] MTDRawCasesUnitsFallenBreakage10002014={0,0};
				
				
				MTDRawCasesUnitsDepotBurst2502014 = ERMTD2014.getDepotBurst(MTDStartDate2014,MTDEndDate2014,11);
				MTDRawCasesUnitsDepotFallenBurst2502014 = ERMTD2014.getDepotFallenBurst(MTDStartDate2014,MTDEndDate2014,11);
				MTDRawCasesUnitsLifterBreakage2502014 = ERMTD2014.getLifterBreakage(MTDStartDate2014,MTDEndDate2014,11);
				MTDRawCasesUnitsLifterBurst2502014 = ERMTD2014.getLifterBurst(MTDStartDate2014,MTDEndDate2014,11);
				MTDRawCasesUnitsWashBreakage2502014 = ERMTD2014.getWashBreakage(MTDStartDate2014,MTDEndDate2014,11);
				MTDRawCasesUnitsFallenBreakage2502014 = ERMTD2014.getFallenBreakage(MTDStartDate2014,MTDEndDate2014,11);
				
				MTDRawCasesUnitsDepotBurst10002014 = ERMTD2014.getDepotBurst(MTDStartDate2014,MTDEndDate2014,1);
				MTDRawCasesUnitsDepotFallenBurst10002014 = ERMTD2014.getDepotFallenBurst(MTDStartDate2014,MTDEndDate2014,1);
				MTDRawCasesUnitsLifterBreakage10002014 = ERMTD2014.getLifterBreakage(MTDStartDate2014,MTDEndDate2014,1);
				MTDRawCasesUnitsLifterBurst10002014 = ERMTD2014.getLifterBurst(MTDStartDate2014,MTDEndDate2014,1);
				MTDRawCasesUnitsWashBreakage10002014 = ERMTD2014.getWashBreakage(MTDStartDate2014,MTDEndDate2014,1);
				MTDRawCasesUnitsFallenBreakage10002014 = ERMTD2014.getFallenBreakage(MTDStartDate2014,MTDEndDate2014,1);
				
				long[] MTDRawCasesUnitsDuringPro2502014=ERMTD2014.getDuringProductionShippingBreakage(MTDStartDate2014,MTDEndDate2014,11);
				long[] MTDRawCasesUnitsDuringPro10002014=ERMTD2014.getDuringProductionShippingBreakage(MTDStartDate2014,MTDEndDate2014,1);
				
				int MTDTotalUnitsLoss1RawCases2502014=ERMTD2014.getDuringProductionBreakage(MTDStartDate2014,MTDEndDate2014,11);
				int MTDTotalUnitsLoss1RawCases10002014=ERMTD2014.getDuringProductionBreakage(MTDStartDate2014,MTDEndDate2014,1);
				
				
				long MTDRawCasesUnitsEmptyReturn2502014=0;
				long MTDRawCasesUnitsEmptyReturn10002014=0;
				
				MTDRawCasesUnitsEmptyReturn2502014 = ERMTD2014.getEmptyReturnToStore(MTDStartDate2014,MTDEndDate2014,11);						
				MTDRawCasesUnitsEmptyReturn10002014 = ERMTD2014.getEmptyReturnToStore(MTDStartDate2014,MTDEndDate2014,1);
				
				long[] MTDRawCasesUnitsEmptySale2502014=ERMTD2014.getEmptySales(MTDStartDate2014,MTDEndDate2014,11);
				long[] MTDRawCasesUnitsEmptySale10002014=ERMTD2014.getEmptySales(MTDStartDate2014,MTDEndDate2014,1);
				
				
				
				
				
				long MTDTotalMarketLoss2502014= MTDZabtAllowanceRawCase2502014+MTDBreakageAllowanceRawCase250;
				long MTDTotalMarketLoss10002014= MTDZabtAllowanceRawCase10002014+MTDBreakageAllowanceRawCase250;
				
				
				
				long MTDTotalSupplyChainLoss2502014= MTDRawCasesUnitsSLCBR2502014[0]+MTDRawCasesUnitsSpecialBreakage2502014[0]+MTDRawCasesUnitsSpecialBurst2502014[0]+
						MTDRawCasesUnitsDepotBurst2502014[0]+MTDRawCasesUnitsDepotFallenBurst2502014[0]+MTDRawCasesUnitsLifterBreakage2502014[0]+MTDRawCasesUnitsLifterBurst2502014[0]+MTDRawCasesUnitsFallenBreakage2502014[0]+
						   MTDRawCasesUnitsDuringPro2502014[0]+MTDRawCasesUnitsWashBreakage2502014[0]+MTDRawCasesUnitsSpecialZabt2502014[0];
				
				long MTDTotalSupplyChainLoss10002014= MTDRawCasesUnitsSLCBR10002014[0]+MTDRawCasesUnitsSpecialBreakage10002014[0]+MTDRawCasesUnitsSpecialBurst10002014[0]+
						MTDRawCasesUnitsDepotBurst10002014[0]+MTDRawCasesUnitsDepotFallenBurst10002014[0]+MTDRawCasesUnitsLifterBreakage10002014[0]+MTDRawCasesUnitsLifterBurst10002014[0]+MTDRawCasesUnitsFallenBreakage10002014[0]+
						   MTDRawCasesUnitsDuringPro10002014[0]+MTDRawCasesUnitsWashBreakage10002014[0]+MTDRawCasesUnitsSpecialZabt10002014[0];
				
				
				
				////////////////////////////////////////////////////////////////////////
				///////////////////////////////////////////////////////////////////////
				///////////////////////////////////////////////////////////////////////
				
				
				
				// Year 2014		
				Calendar now1 = Calendar.getInstance();
				now1.add(Calendar.YEAR, -1);
				now1.add(Calendar.DATE, -1);
				
				Date YTDEndDate2014=now1.getTime();
				Date YTDStartDate2014= Utilities.parseDate("01/01/"+now1.get(Calendar.YEAR));
				
			   //System.out.println(YTDStartDate2014+" - "+YTDEndDate2014);
				
				//System.out.println(MTDStartDate2014 +" - "+MTDEndDate2014 );
				
			   
				
				EmptyReconciliation ERYTD2014 = new EmptyReconciliation();
				
				long YTDGrandTotalEmpty2402014=0;
				long YTDGrandTotalEmpty2502014=0;
				long YTDGrandTotalEmpty10002014=0;
				
				long YTDNewEmptyGTotalRawCases2402014=0;
				long YTDNewEmptyGTotalUnits2402014=0;
				
				
								
				long YTDZabtAllowanceRawCase2502014 = ERMTD.getZabtAllowance(YTDStartDate2014,YTDEndDate2014,11);
			 	long YTDZabtAllowanceRawCase10002014 = ERMTD.getZabtAllowance(YTDStartDate2014,YTDEndDate2014,1);
			 	
			 	long YTDBreakageAllowanceRawCase2502014 = ERMTD.getBreakageAllowance(YTDStartDate2014,YTDEndDate2014,11);
			 	long YTDBreakageAllowanceRawCase10002014 = ERMTD.getBreakageAllowance(YTDStartDate2014,YTDEndDate2014,1);
				
			 	
			 	
				long YTDNewEmptyGTotalRawCases2502014= ERYTD2014.getNewEmptyOpen(YTDStartDate2014,YTDEndDate2014,11);
			 	long YTDNewEmptyGTotalRawCases10002014=ERYTD2014.getNewEmptyOpen(YTDStartDate2014,YTDEndDate2014,1);
				
			 	
				long YTDOldEmptyGTotalRawCases2502014=ERYTD2014.getOldEmptyPurchase(YTDStartDate2014,YTDEndDate2014,11);
				long YTDOldEmptyGTotalRawCases10002014=ERYTD2014.getOldEmptyPurchase(YTDStartDate2014,YTDEndDate2014,1);
				
				long YTDGTotalRawCases2502014=ERYTD2014.getAgencyPurchase(YTDStartDate2014,YTDEndDate2014,11);
				long YTDGTotalRawCases10002014=ERYTD2014.getAgencyPurchase(YTDStartDate2014,YTDEndDate2014,1);
				
				YTDGrandTotalEmpty2502014 = YTDNewEmptyGTotalRawCases2502014+YTDOldEmptyGTotalRawCases2502014+YTDGTotalRawCases2502014;
				YTDGrandTotalEmpty10002014 = YTDNewEmptyGTotalRawCases10002014+YTDOldEmptyGTotalRawCases10002014+YTDGTotalRawCases10002014;
				
				
				//Losses
				
				double YTDLossTotal2402014=0;
				double YTDLossTotal2502014=0;
				double YTDLossTotal10002014=0;
				
				long[] YTDRawCasesUnitsSLCBR2502014={0,0};
				long[] YTDRawCasesUnitsSpecialBreakage2502014={0,0};
				long[] YTDRawCasesUnitsSpecialBurst2502014={0,0};
				long[] YTDRawCasesUnitsSpecialZabt2502014={0,0};
				
				long[] YTDRawCasesUnitsSLCBR10002014={0,0};
				long[] YTDRawCasesUnitsSpecialBreakage10002014={0,0};
				long[] YTDRawCasesUnitsSpecialBurst10002014={0,0};
				long[] YTDRawCasesUnitsSpecialZabt10002014={0,0};
				
				YTDRawCasesUnitsSLCBR2502014 = ERYTD2014.getSLCBR(YTDStartDate2014,YTDEndDate2014,11);
				YTDRawCasesUnitsSpecialBreakage2502014 = ERYTD2014.getSpecialBreakage(YTDStartDate2014,YTDEndDate2014,11);
				YTDRawCasesUnitsSpecialBurst2502014 = ERYTD2014.getSpecialBurst(YTDStartDate2014,YTDEndDate2014,11);
				YTDRawCasesUnitsSpecialZabt2502014 = ERYTD2014.getSpecialZabt(YTDStartDate2014,YTDEndDate2014,11);
				
				YTDRawCasesUnitsSLCBR10002014 = ERYTD2014.getSLCBR(YTDStartDate2014,YTDEndDate2014,1);
				YTDRawCasesUnitsSpecialBreakage10002014 = ERYTD2014.getSpecialBreakage(YTDStartDate2014,YTDEndDate2014,1);
				YTDRawCasesUnitsSpecialBurst10002014 = ERYTD2014.getSpecialBurst(YTDStartDate2014,YTDEndDate2014,1);
				YTDRawCasesUnitsSpecialZabt10002014 = ERYTD2014.getSpecialZabt(YTDStartDate2014,YTDEndDate2014,1);
				
				long [] YTDRawCasesUnitsDepotBurst2502014={0,0};
				long [] YTDRawCasesUnitsDepotFallenBurst2502014={0,0};
				long [] YTDRawCasesUnitsLifterBreakage2502014={0,0};
				long [] YTDRawCasesUnitsLifterBurst2502014={0,0};
				long [] YTDRawCasesUnitsWashBreakage2502014={0,0};
				long [] YTDRawCasesUnitsFallenBreakage2502014={0,0};
				
				long [] YTDRawCasesUnitsDepotBurst10002014={0,0};
				long [] YTDRawCasesUnitsDepotFallenBurst10002014={0,0};
				long [] YTDRawCasesUnitsLifterBreakage10002014={0,0};
				long [] YTDRawCasesUnitsLifterBurst10002014={0,0};
				long [] YTDRawCasesUnitsWashBreakage10002014={0,0};
				long [] YTDRawCasesUnitsFallenBreakage10002014={0,0};
				
				
				YTDRawCasesUnitsDepotBurst2502014 = ERYTD2014.getDepotBurst(YTDStartDate2014,YTDEndDate2014,11);
				YTDRawCasesUnitsDepotFallenBurst2502014 = ERYTD2014.getDepotFallenBurst(YTDStartDate2014,YTDEndDate2014,11);
				YTDRawCasesUnitsLifterBreakage2502014 = ERYTD2014.getLifterBreakage(YTDStartDate2014,YTDEndDate2014,11);
				YTDRawCasesUnitsLifterBurst2502014 = ERYTD2014.getLifterBurst(YTDStartDate2014,YTDEndDate2014,11);
				YTDRawCasesUnitsWashBreakage2502014 = ERYTD2014.getWashBreakage(YTDStartDate2014,YTDEndDate2014,11);
				YTDRawCasesUnitsFallenBreakage2502014 = ERYTD2014.getFallenBreakage(YTDStartDate2014,YTDEndDate2014,11);
				
				YTDRawCasesUnitsDepotBurst10002014 = ERYTD2014.getDepotBurst(YTDStartDate2014,YTDEndDate2014,1);
				YTDRawCasesUnitsDepotFallenBurst10002014 = ERYTD2014.getDepotFallenBurst(YTDStartDate2014,YTDEndDate2014,1);
				YTDRawCasesUnitsLifterBreakage10002014 = ERYTD2014.getLifterBreakage(YTDStartDate2014,YTDEndDate2014,1);
				YTDRawCasesUnitsLifterBurst10002014 = ERYTD2014.getLifterBurst(YTDStartDate2014,YTDEndDate2014,1);
				YTDRawCasesUnitsWashBreakage10002014 = ERYTD2014.getWashBreakage(YTDStartDate2014,YTDEndDate2014,1);
				YTDRawCasesUnitsFallenBreakage10002014 = ERYTD2014.getFallenBreakage(YTDStartDate2014,YTDEndDate2014,1);
				
				long[] YTDRawCasesUnitsDuringPro2502014=ERYTD2014.getDuringProductionShippingBreakage(YTDStartDate2014,YTDEndDate2014,11);
				long[] YTDRawCasesUnitsDuringPro10002014=ERYTD2014.getDuringProductionShippingBreakage(YTDStartDate2014,YTDEndDate2014,1);
				
				int YTDTotalUnitsLoss1RawCases2502014=ERYTD2014.getDuringProductionBreakage(YTDStartDate2014,YTDEndDate2014,11);
				int YTDTotalUnitsLoss1RawCases10002014=ERYTD2014.getDuringProductionBreakage(YTDStartDate2014,YTDEndDate2014,1);
				
				
				long YTDRawCasesUnitsEmptyReturn2502014=0;
				long YTDRawCasesUnitsEmptyReturn10002014=0;
				
				YTDRawCasesUnitsEmptyReturn2502014 = ERYTD2014.getEmptyReturnToStore(YTDStartDate2014,YTDEndDate2014,11);						
				YTDRawCasesUnitsEmptyReturn10002014 = ERYTD2014.getEmptyReturnToStore(YTDStartDate2014,YTDEndDate2014,1);
				
				long[] YTDRawCasesUnitsEmptySale2502014=ERYTD2014.getEmptySales(YTDStartDate2014,YTDEndDate2014,11);
				long[] YTDRawCasesUnitsEmptySale10002014=ERYTD2014.getEmptySales(YTDStartDate2014,YTDEndDate2014,1);
				
				
				
				
				
				long YTDTotalMarketLoss2502014= YTDZabtAllowanceRawCase2502014+YTDBreakageAllowanceRawCase2502014;
				long YTDTotalMarketLoss10002014= YTDZabtAllowanceRawCase10002014+YTDBreakageAllowanceRawCase10002014;
				
				long YTDTotalSupplyChainLoss2502014= YTDRawCasesUnitsSLCBR2502014[0]+YTDRawCasesUnitsSpecialBreakage2502014[0]+YTDRawCasesUnitsSpecialBurst2502014[0]+
						YTDRawCasesUnitsDepotBurst2502014[0]+YTDRawCasesUnitsDepotFallenBurst2502014[0]+YTDRawCasesUnitsLifterBreakage2502014[0]+YTDRawCasesUnitsLifterBurst2502014[0]+YTDRawCasesUnitsFallenBreakage2502014[0]+
						   YTDRawCasesUnitsDuringPro2502014[0]+YTDRawCasesUnitsWashBreakage2502014[0]+YTDRawCasesUnitsSpecialZabt2502014[0];
				
				long YTDTotalSupplyChainLoss10002014= YTDRawCasesUnitsSLCBR10002014[0]+YTDRawCasesUnitsSpecialBreakage10002014[0]+YTDRawCasesUnitsSpecialBurst10002014[0]+
						YTDRawCasesUnitsDepotBurst10002014[0]+YTDRawCasesUnitsDepotFallenBurst10002014[0]+YTDRawCasesUnitsLifterBreakage10002014[0]+YTDRawCasesUnitsLifterBurst10002014[0]+YTDRawCasesUnitsFallenBreakage10002014[0]+
						   YTDRawCasesUnitsDuringPro10002014[0]+YTDRawCasesUnitsWashBreakage10002014[0]+YTDRawCasesUnitsSpecialZabt10002014[0];
				
				
				
				////////////////////////////////////////////////////////////////////
				
				
				// Year 2015		
				Calendar now2 = Calendar.getInstance();
				now2.add(Calendar.DATE, -1);
				
				Date YTDEndDate2015=now2.getTime();
				Date YTDStartDate2015= Utilities.parseDate("01/01/"+now2.get(Calendar.YEAR));
				
			  // System.out.println(YTDStartDate2015+" - "+YTDEndDate2015);
				
				//System.out.println(MTDStartDate2014 +" - "+MTDEndDate2014 );
				
			   
				
				EmptyReconciliation ERYTD2015 = new EmptyReconciliation();
				
				long YTDGrandTotalEmpty2402015=0;
				long YTDGrandTotalEmpty2502015=0;
				long YTDGrandTotalEmpty10002015=0;
				
				long YTDNewEmptyGTotalRawCases2402015=0;
				long YTDNewEmptyGTotalUnits2402015=0;
				
				
				
				
				
				long YTDZabtAllowanceRawCase2502015 = ERMTD.getZabtAllowance(YTDStartDate2015,YTDEndDate2015,11);
			 	long YTDZabtAllowanceRawCase10002015 = ERMTD.getZabtAllowance(YTDStartDate2015,YTDEndDate2015,1);
			 	
			 	long YTDBreakageAllowanceRawCase2502015 = ERMTD.getBreakageAllowance(YTDStartDate2015,YTDEndDate2015,11);
			 	long YTDBreakageAllowanceRawCase10002015 = ERMTD.getBreakageAllowance(YTDStartDate2015,YTDEndDate2015,1);
				
			 	
			 	
				long YTDNewEmptyGTotalRawCases2502015= ERYTD2015.getNewEmptyOpen(YTDStartDate2015,YTDEndDate2015,11);
			 	long YTDNewEmptyGTotalRawCases10002015=ERYTD2015.getNewEmptyOpen(YTDStartDate2015,YTDEndDate2015,1);
				
			 	
				long YTDOldEmptyGTotalRawCases2502015=ERYTD2015.getOldEmptyPurchase(YTDStartDate2015,YTDEndDate2015,11);
				long YTDOldEmptyGTotalRawCases10002015=ERYTD2015.getOldEmptyPurchase(YTDStartDate2015,YTDEndDate2015,1);
				
				long YTDGTotalRawCases2502015=ERYTD2015.getAgencyPurchase(YTDStartDate2015,YTDEndDate2015,11);
				long YTDGTotalRawCases10002015=ERYTD2015.getAgencyPurchase(YTDStartDate2015,YTDEndDate2015,1);
				
				YTDGrandTotalEmpty2502015 = YTDNewEmptyGTotalRawCases2502015+YTDOldEmptyGTotalRawCases2502015+YTDGTotalRawCases2502015;
				YTDGrandTotalEmpty10002015 = YTDNewEmptyGTotalRawCases10002015+YTDOldEmptyGTotalRawCases10002015+YTDGTotalRawCases10002015;
				
				
				//Losses
				
				double YTDLossTotal2402015=0;
				double YTDLossTotal2502015=0;
				double YTDLossTotal10002015=0;
				
				long[] YTDRawCasesUnitsSLCBR2502015={0,0};
				long[] YTDRawCasesUnitsSpecialBreakage2502015={0,0};
				long[] YTDRawCasesUnitsSpecialBurst2502015={0,0};
				long[] YTDRawCasesUnitsSpecialZabt2502015={0,0};
				
				long[] YTDRawCasesUnitsSLCBR10002015={0,0};
				long[] YTDRawCasesUnitsSpecialBreakage10002015={0,0};
				long[] YTDRawCasesUnitsSpecialBurst10002015={0,0};
				long[] YTDRawCasesUnitsSpecialZabt10002015={0,0};
				
				YTDRawCasesUnitsSLCBR2502015 = ERYTD2015.getSLCBR(YTDStartDate2015,YTDEndDate2015,11);
				YTDRawCasesUnitsSpecialBreakage2502015 = ERYTD2015.getSpecialBreakage(YTDStartDate2015,YTDEndDate2015,11);
				YTDRawCasesUnitsSpecialBurst2502015 = ERYTD2015.getSpecialBurst(YTDStartDate2015,YTDEndDate2015,11);
				YTDRawCasesUnitsSpecialZabt2502015 = ERYTD2015.getSpecialZabt(YTDStartDate2015,YTDEndDate2015,11);
				
				YTDRawCasesUnitsSLCBR10002015 = ERYTD2015.getSLCBR(YTDStartDate2015,YTDEndDate2015,1);
				YTDRawCasesUnitsSpecialBreakage10002015 = ERYTD2015.getSpecialBreakage(YTDStartDate2015,YTDEndDate2015,1);
				YTDRawCasesUnitsSpecialBurst10002015 = ERYTD2015.getSpecialBurst(YTDStartDate2015,YTDEndDate2015,1);
				YTDRawCasesUnitsSpecialZabt10002015 = ERYTD2015.getSpecialZabt(YTDStartDate2015,YTDEndDate2015,1);
				
				long [] YTDRawCasesUnitsDepotBurst2502015={0,0};
				long [] YTDRawCasesUnitsDepotFallenBurst2502015={0,0};
				long [] YTDRawCasesUnitsLifterBreakage2502015={0,0};
				long [] YTDRawCasesUnitsLifterBurst2502015={0,0};
				long [] YTDRawCasesUnitsWashBreakage2502015={0,0};
				long [] YTDRawCasesUnitsFallenBreakage2502015={0,0};
				
				long [] YTDRawCasesUnitsDepotBurst10002015={0,0};
				long [] YTDRawCasesUnitsDepotFallenBurst10002015={0,0};
				long [] YTDRawCasesUnitsLifterBreakage10002015={0,0};
				long [] YTDRawCasesUnitsLifterBurst10002015={0,0};
				long [] YTDRawCasesUnitsWashBreakage10002015={0,0};
				long [] YTDRawCasesUnitsFallenBreakage10002015={0,0};
				
				
				YTDRawCasesUnitsDepotBurst2502015 = ERYTD2015.getDepotBurst(YTDStartDate2015,YTDEndDate2015,11);
				YTDRawCasesUnitsDepotFallenBurst2502015 = ERYTD2015.getDepotFallenBurst(YTDStartDate2015,YTDEndDate2015,11);
				YTDRawCasesUnitsLifterBreakage2502015 = ERYTD2015.getLifterBreakage(YTDStartDate2015,YTDEndDate2015,11);
				YTDRawCasesUnitsLifterBurst2502015 = ERYTD2015.getLifterBurst(YTDStartDate2015,YTDEndDate2015,11);
				YTDRawCasesUnitsWashBreakage2502015 = ERYTD2015.getWashBreakage(YTDStartDate2015,YTDEndDate2015,11);
				YTDRawCasesUnitsFallenBreakage2502015 = ERYTD2015.getFallenBreakage(YTDStartDate2015,YTDEndDate2015,11);
				
				YTDRawCasesUnitsDepotBurst10002015 = ERYTD2015.getDepotBurst(YTDStartDate2015,YTDEndDate2015,1);
				YTDRawCasesUnitsDepotFallenBurst10002015 = ERYTD2015.getDepotFallenBurst(YTDStartDate2015,YTDEndDate2015,1);
				YTDRawCasesUnitsLifterBreakage10002015 = ERYTD2015.getLifterBreakage(YTDStartDate2015,YTDEndDate2015,1);
				YTDRawCasesUnitsLifterBurst10002015 = ERYTD2015.getLifterBurst(YTDStartDate2015,YTDEndDate2015,1);
				YTDRawCasesUnitsWashBreakage10002015 = ERYTD2015.getWashBreakage(YTDStartDate2015,YTDEndDate2015,1);
				YTDRawCasesUnitsFallenBreakage10002015 = ERYTD2015.getFallenBreakage(YTDStartDate2015,YTDEndDate2015,1);
				
				long[] YTDRawCasesUnitsDuringPro2502015=ERYTD2015.getDuringProductionShippingBreakage(YTDStartDate2015,YTDEndDate2015,11);
				long[] YTDRawCasesUnitsDuringPro10002015=ERYTD2015.getDuringProductionShippingBreakage(YTDStartDate2015,YTDEndDate2015,1);
				
				int YTDTotalUnitsLoss1RawCases2502015=ERYTD2015.getDuringProductionBreakage(YTDStartDate2015,YTDEndDate2015,11);
				int YTDTotalUnitsLoss1RawCases10002015=ERYTD2015.getDuringProductionBreakage(YTDStartDate2015,YTDEndDate2015,1);
				
				
				long YTDRawCasesUnitsEmptyReturn2502015=0;
				long YTDRawCasesUnitsEmptyReturn10002015=0;
				
				YTDRawCasesUnitsEmptyReturn2502015 = ERYTD2015.getEmptyReturnToStore(YTDStartDate2015,YTDEndDate2015,11);						
				YTDRawCasesUnitsEmptyReturn10002015 = ERYTD2015.getEmptyReturnToStore(YTDStartDate2015,YTDEndDate2015,1);
				
				long[] YTDRawCasesUnitsEmptySale2502015=ERYTD2015.getEmptySales(YTDStartDate2015,YTDEndDate2015,11);
				long[] YTDRawCasesUnitsEmptySale10002015=ERYTD2015.getEmptySales(YTDStartDate2015,YTDEndDate2015,1);
				
				
				
				
				
				long YTDTotalMarketLoss2502015= YTDZabtAllowanceRawCase2502015+YTDBreakageAllowanceRawCase2502015;
				long YTDTotalMarketLoss10002015= YTDZabtAllowanceRawCase10002015+YTDBreakageAllowanceRawCase10002015;
				
				long YTDTotalSupplyChainLoss2502015= YTDRawCasesUnitsSLCBR2502015[0]+YTDRawCasesUnitsSpecialBreakage2502015[0]+YTDRawCasesUnitsSpecialBurst2502015[0]+
						YTDRawCasesUnitsDepotBurst2502015[0]+YTDRawCasesUnitsDepotFallenBurst2502015[0]+YTDRawCasesUnitsLifterBreakage2502015[0]+YTDRawCasesUnitsLifterBurst2502015[0]+YTDRawCasesUnitsFallenBreakage2502015[0]+
						   YTDRawCasesUnitsDuringPro2502015[0]+YTDRawCasesUnitsWashBreakage2502015[0]+YTDRawCasesUnitsSpecialZabt2502015[0];
				
				long YTDTotalSupplyChainLoss10002015= YTDRawCasesUnitsSLCBR10002015[0]+YTDRawCasesUnitsSpecialBreakage10002015[0]+YTDRawCasesUnitsSpecialBurst10002015[0]+
						YTDRawCasesUnitsDepotBurst10002015[0]+YTDRawCasesUnitsDepotFallenBurst10002015[0]+YTDRawCasesUnitsLifterBreakage10002015[0]+YTDRawCasesUnitsLifterBurst10002015[0]+YTDRawCasesUnitsFallenBreakage10002015[0]+
						   YTDRawCasesUnitsDuringPro10002015[0]+YTDRawCasesUnitsWashBreakage10002015[0]+YTDRawCasesUnitsSpecialZabt10002015[0];
				
				
				
				double ChPercentageMarketLoss250=0;
				if(MTDTotalMarketLoss2502014!=0){
					ChPercentageMarketLoss250 = ((MTDTotalMarketLoss250 - MTDTotalMarketLoss2502014)/Utilities.parseDouble(MTDTotalMarketLoss2502014+"")*100);
				}
				
				
				double YChPercentageMarketLoss250=0;
				if(YTDTotalMarketLoss2502014!=0){
					YChPercentageMarketLoss250 = ((YTDTotalMarketLoss2502015 - YTDTotalMarketLoss2502014)/Utilities.parseDouble(YTDTotalMarketLoss2502014+"")*100);
				}
				
				
				double ChPercentageSupplyChain250=0;
				if(MTDTotalSupplyChainLoss2502014!=0){
					ChPercentageSupplyChain250 = ((MTDTotalSupplyChainLoss250 - MTDTotalSupplyChainLoss2502014)/Utilities.parseDouble(MTDTotalSupplyChainLoss2502014+"")*100);
				}
				
				
				double YChPercentageSupplyChain250=0;
				if(YTDTotalSupplyChainLoss2502014!=0){
					YChPercentageSupplyChain250 = ((YTDTotalSupplyChainLoss2502015 - YTDTotalSupplyChainLoss2502014)/Utilities.parseDouble(YTDTotalSupplyChainLoss2502014+"")*100);
				}
				
				
				double ChPercentageProductionBreakage1250=0;
				if(MTDRawCasesUnitsDuringPro2502014[0]!=0){
					ChPercentageProductionBreakage1250 = ((MTDRawCasesUnitsDuringPro250[0] - MTDRawCasesUnitsDuringPro2502014[0])/Utilities.parseDouble(MTDRawCasesUnitsDuringPro2502014[0]+"")*100);
				}

				double YChPercentageProductionBreakage1250=0;
				if(YTDRawCasesUnitsDuringPro2502014[0]!=0){
					YChPercentageProductionBreakage1250 = ((YTDRawCasesUnitsDuringPro2502015[0] - YTDRawCasesUnitsDuringPro2502014[0])/Utilities.parseDouble(YTDRawCasesUnitsDuringPro2502014[0]+"")*100);
				}
				
				
				
				
				
				double ChPercentageMarketLoss1000=0;
				if(MTDTotalMarketLoss10002014!=0){
					ChPercentageMarketLoss1000 = ((MTDTotalMarketLoss1000 - MTDTotalMarketLoss10002014)/Utilities.parseDouble(MTDTotalMarketLoss10002014+"")*100);
				}
				
				
				double YChPercentageMarketLoss1000=0;
				if(YTDTotalMarketLoss10002014!=0){
					YChPercentageMarketLoss1000 = ((YTDTotalMarketLoss10002015 - YTDTotalMarketLoss10002014)/Utilities.parseDouble(YTDTotalMarketLoss10002014+"")*100);
				}
				
				
				double ChPercentageSupplyChain1000=0;
				if(MTDTotalSupplyChainLoss10002014!=0){
					ChPercentageSupplyChain1000 = ((MTDTotalSupplyChainLoss1000 - MTDTotalSupplyChainLoss10002014)/Utilities.parseDouble(MTDTotalSupplyChainLoss10002014+"")*100);
				}
				
				
				double YChPercentageSupplyChain1000=0;
				if(YTDTotalSupplyChainLoss10002014!=0){
					YChPercentageSupplyChain1000 = ((YTDTotalSupplyChainLoss10002015 - YTDTotalSupplyChainLoss10002014)/Utilities.parseDouble(YTDTotalSupplyChainLoss10002014+"")*100);
				}
				
				
				double ChPercentageProductionBreakage11000=0;
				if(MTDRawCasesUnitsDuringPro10002014[0]!=0){
					ChPercentageProductionBreakage11000 = ((MTDRawCasesUnitsDuringPro1000[0] - MTDRawCasesUnitsDuringPro10002014[0])/Utilities.parseDouble(MTDRawCasesUnitsDuringPro10002014[0]+"")*100);
				}

				double YChPercentageProductionBreakage11000=0;
				if(YTDRawCasesUnitsDuringPro10002014[0]!=0){
					YChPercentageProductionBreakage11000 = ((YTDRawCasesUnitsDuringPro10002015[0] - YTDRawCasesUnitsDuringPro10002014[0])/Utilities.parseDouble(YTDRawCasesUnitsDuringPro10002014[0]+"")*100);
				}
				
				
				
			   
			   ///////////////////////////////////////////////////////////////////////////////////////////////////
			   ///////////////////////////////////////////////////////////////////////////////////////////////////
			   //////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		
		String html = "<html>";
			html += "<body><br>";
		
			
			/*
			html +="<table style='width: 500px; font-size:14px; border-collapse: collapse;' >";
			html += "<tr>";
		    html += "<td colspan='2' style='text-align: center;'><img src='https://chart.googleapis.com/chart?cht=lc&chd=t:1,2,3,4,5|8,8,5,6,1&chds=a&chs=400x250&chl=Jan 2015|July 2015&chxt=y,r&chco=bda609,0963bd,f0516c&chdl=2014|2015&chdlp=b&chtt=(millions)'></td>";
		    html += "</tr>";
		    
		    html += "<tr>";
		    html += "<td>&nbsp;</td>";
		    html += "</tr>";
		    html += "<tr>";
		    html += "<td>&nbsp;</td>";
		    html += "</tr>";
		    html +="</table>";
		    */
		    
		    
		    
			html += "<table style='width: 500px; font-size:14px; border-collapse: collapse;' >";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; border-left:3px solid #EBEBEB;border-color:#BAC4E8;font-weight: bold;width:30%'>250ML in Cases</td>";
				//html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold;width:15%'>7 Days</td>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; border-left:3px solid #EBEBEB;border-color:#BAC4E8;font-weight: bold;width:15%' colspan=3>MTD</td>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; border-left:3px solid #EBEBEB;border-color:#BAC4E8;font-weight: bold;width:15%' colspan=3>YTD</td>";
				html += "</tr>";
			
				
				html += "<tr >";
					html += "<td style='b1ackground-color: #EDEFF2; height: 18px; font-size:12px;  border:1px solid #EBEBEB;border-color:white;text-align: center; font-weight: bold;'></td>";	
					html += "<td style='background-color: #C8CFE6; c1olor: white;height: 18px; font-size:12px;  border:1px solid #EBEBEB;border-left:3px solid #BAC4E8;b1order-color:#BAC4E8;text-align: center; font-weight: bold;'>2014</td>";
					html += "<td style='background-color: #C8CFE6; c1olor: white;height: 18px; font-size:12px;  border:1px solid #EBEBEB;border-color:white;text-align: center; font-weight: bold;'>2015</td>";
					html += "<td style='background-color: #C8CFE6; c1olor: white;height: 18px; font-size:12px; border:1px solid #EBEBEB;border-color:white; text-align: center; font-weight: bold;'>Ch(%)</td>";	
					html += "<td style='background-color: #C8CFE6; c1olor: white;height: 18px; font-size:12px;  border-left:3px solid #EBEBEB;border-color:#BAC4E8;text-align: center; font-weight: bold;'>2014</td>";
					html += "<td style='background-color: #C8CFE6; c1olor: white;height: 18px; font-size:12px; border:1px solid #EBEBEB;border-color:white; text-align: center; font-weight: bold;'>2015</td>";	
					html += "<td style='background-color: #C8CFE6; c1olor: white;height: 18px; font-size:12px;  border:1px solid #EBEBEB;border-color:white;text-align: center; font-weight: bold;'>Ch(%)</td>";	
				html += "</tr>";
				
			
				
				
				
				String MColorML ="#EDEFF2";
				String YColorML="#EDEFF2";
				
				String PlusMLMTD="";
				String PlusMLYTD="";
				if(ChPercentageMarketLoss250<0){ //Green
					MColorML="#87D692";
				}else if(ChPercentageMarketLoss250>0){ //Red
					MColorML="#DE87A6";
					PlusMLMTD="+";
				}
				
				if(YChPercentageMarketLoss250<0){ //Green
					YColorML="#87D692";
				}else if(YChPercentageMarketLoss250>0){ //Red
					YColorML="#DE87A6";
					PlusMLYTD="+";
				}
				
				html += "<tr >";
					html += "<td style='background-color: #EDEFF2; height: 30x; font-size:12px; text-align: left; font-weight: bold;'>Market Loss<br/></td>";
					//html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;' >"+(TotalMarketLoss250==0?"":Utilities.getDisplayCurrencyFormat(TotalMarketLoss250))+"</td>";
					html += "<td style='background-color: #EDEFF2; height: 30px; font-size:12px;text-align: center; font-weight: bold;border:1px solid #EBEBEB;border-left:3px solid #BAC4E8;b1order-color:#BAC4E8;' >"+(MTDTotalMarketLoss2502014==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDTotalMarketLoss2502014))+"<br/></td>";
					html += "<td style='background-color: #EDEFF2; height: 30px; font-size:12px;text-align: center; font-weight: bold;' >"+(MTDTotalMarketLoss250==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDTotalMarketLoss250))+"<br/></td>";
					html += "<td style='background-color: "+MColorML+"; height: 30px; font-size:12px;text-align: center; font-weight: bold;' >"+(ChPercentageMarketLoss250==0?"&nbsp;":PlusMLMTD+Math.round(ChPercentageMarketLoss250)+"%")+"<br/></td>";
					html += "<td style='background-color: #EDEFF2; height: 30px; font-size:12px;text-align: center; border-left:3px solid #EBEBEB;border-color:#BAC4E8;font-weight: bold;' >"+(YTDTotalMarketLoss2502014==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDTotalMarketLoss2502014))+"<br/></td>";
					html += "<td style='background-color: #EDEFF2; height: 30px; font-size:12px;text-align: center; font-weight: bold;' >"+(YTDTotalMarketLoss2502015==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDTotalMarketLoss2502015))+"<br/></td>";
					html += "<td style='background-color: "+YColorML+"; height: 30px; font-size:12px;text-align: center; font-weight: bold;' >"+(YChPercentageMarketLoss250==0?"&nbsp;":PlusMLYTD+Math.round(YChPercentageMarketLoss250)+"%")+"<br/></td>";
					
					html += "</tr>";
				
					
					
					String PlusMBMTD="";
					String PlusMBYTD="";
					double ChPercentageMarketBreakage250=0;
					if(MTDBreakageAllowanceRawCase2502014!=0){
						ChPercentageMarketBreakage250 = ((MTDBreakageAllowanceRawCase250 - MTDBreakageAllowanceRawCase2502014)/Utilities.parseDouble(MTDBreakageAllowanceRawCase2502014+"")*100);
					}
					if(ChPercentageMarketBreakage250>0){PlusMBMTD="+";}
					
					
					
					double YChPercentageMarketBreakage250=0;
					if(YTDBreakageAllowanceRawCase2502014!=0){
						YChPercentageMarketBreakage250 = ((YTDBreakageAllowanceRawCase2502015 - YTDBreakageAllowanceRawCase2502014)/Utilities.parseDouble(YTDBreakageAllowanceRawCase2502014+"")*100);
					}
					if(YChPercentageMarketBreakage250>0){PlusMBYTD="+";}
										
					//System.out.println(ChPercentageWashBreakage250);
					
					String MColorMB ="";
					String YColorMB="";
					if(ChPercentageMarketBreakage250<0){ //Green
						MColorMB="#D7F7DB";
					}else if(ChPercentageMarketBreakage250>0){ //Red
						MColorMB="#FAE3EC";
					}
					
					if(YChPercentageMarketBreakage250<0){ //Green
						YColorMB="#D7F7DB";
					}else if(YChPercentageMarketBreakage250>0){ //Red
						YColorMB="#FAE3EC";
					}
					
					
					html += "<tr >";
					html += "<td valign='top' style='text-align:left; font-size:12px; border:1px solid #EBEBEB'><b>Breakage Allowance</b><br/>given to distributors againts breakage</td>";					
					//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsWashBreakage250[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsWashBreakage250[0]))+"</td>";
					html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+ (MTDBreakageAllowanceRawCase2502014==0? "&nbsp;" : Utilities.getDisplayCurrencyFormat(MTDBreakageAllowanceRawCase2502014)) +"</td>";
					html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB'>"+(MTDBreakageAllowanceRawCase250==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDBreakageAllowanceRawCase250))+"</td>";
					
					html += "<td style='background-color: "+MColorMB+"; border:1px solid #EBEBEB; height: 18px; font-size:12px;text-align: center;;' >"+(ChPercentageMarketBreakage250==0?"&nbsp;":PlusMBMTD+Math.round(ChPercentageMarketBreakage250)+"%")+"</td>";
					html += "<td style='height: 18px; text-align: center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;b1order-color:#BAC4E8;'>"+(YTDBreakageAllowanceRawCase2502014==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDBreakageAllowanceRawCase2502014))+"</td>";
					html += "<td style='height: 18px; text-align: center;font-size:12px; border:1px solid #EBEBEB'>"+(YTDBreakageAllowanceRawCase2502015==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDBreakageAllowanceRawCase2502015))+"</td>";
					html += "<td style='background-color: "+YColorMB+"; height: 18px; font-size:12px;border:1px solid #EBEBEB; text-align: center;' >"+(YChPercentageMarketBreakage250==0?"&nbsp;":PlusMBYTD+Math.round(YChPercentageMarketBreakage250)+"%")+"</td>";
					html += "</tr>";
					
					
					String PlusMZMTD="";
					String PlusMZYTD="";
					double ChPercentageMarketZabt250=0;
					if(MTDZabtAllowanceRawCase2502014!=0){
						ChPercentageMarketZabt250 = ((MTDZabtAllowanceRawCase250 - MTDZabtAllowanceRawCase2502014)/Utilities.parseDouble(MTDZabtAllowanceRawCase2502014+"")*100);
					}
					if(ChPercentageMarketZabt250>0){PlusMZMTD="+";}
					
					
					
					double YChPercentageMarketZabt250=0;
					if(YTDZabtAllowanceRawCase2502014!=0){
						YChPercentageMarketZabt250 = ((YTDZabtAllowanceRawCase2502015 - YTDZabtAllowanceRawCase2502014)/Utilities.parseDouble(YTDZabtAllowanceRawCase2502014+"")*100);
					}
					if(YChPercentageMarketZabt250>0){PlusMZYTD="+";}
										
					//System.out.println(ChPercentageWashBreakage250);
					
					String MColorMZ ="";
					String YColorMZ="";
					if(ChPercentageMarketZabt250<0){ //Green
						MColorMZ="#D7F7DB";
					}else if(ChPercentageMarketZabt250>0){ //Red
						MColorMZ="#FAE3EC";
					}
					
					if(YChPercentageMarketZabt250<0){ //Green
						YColorMZ="#D7F7DB";
					}else if(YChPercentageMarketZabt250>0){ //Red
						YColorMZ="#FAE3EC";
					}
					
					
					html += "<tr >";
					html += "<td valign='top' style='text-align:left; font-size:12px; border:1px solid #EBEBEB'><b>Zabt Allowance</b><br/>given to distributors againts breakage</td>";					
					//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsWashBreakage250[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsWashBreakage250[0]))+"</td>";
					html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+ (MTDZabtAllowanceRawCase2502014==0? "&nbsp;" : Utilities.getDisplayCurrencyFormat(MTDZabtAllowanceRawCase2502014)) +"</td>";
					html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB'>"+(MTDZabtAllowanceRawCase250==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDZabtAllowanceRawCase250))+"</td>";
					
					html += "<td style='background-color: "+MColorMZ+"; border:1px solid #EBEBEB; height: 18px; font-size:12px;text-align: center;;' >"+(ChPercentageMarketZabt250==0?"&nbsp;":PlusMZMTD+Math.round(ChPercentageMarketZabt250)+"%")+"</td>";
					html += "<td style='height: 18px; text-align: center;font-size:12px; border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(YTDZabtAllowanceRawCase2502014==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDZabtAllowanceRawCase2502014))+"</td>";
					html += "<td style='height: 18px; text-align: center;font-size:12px; border:1px solid #EBEBEB'>"+(YTDZabtAllowanceRawCase2502015==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDZabtAllowanceRawCase2502015))+"</td>";
					html += "<td style='background-color: "+YColorMZ+"; height: 18px; font-size:12px;border:1px solid #EBEBEB; text-align: center;' >"+(YChPercentageMarketZabt250==0?"&nbsp;":PlusMZYTD+Math.round(YChPercentageMarketZabt250)+"%")+"</td>";
			    html += "</tr>";
					
			    
			    
				
				
			
			//////////////////////////////////////////////////////////////
				
				
					
					
					
				
				
				
				String MColorSC ="#EDEFF2";
				String YColorSC="#EDEFF2";
				
				String PlusSCMTD="";
				String PlusSCYTD="";
				if(ChPercentageSupplyChain250<0){ //Green
					MColorSC="#87D692";
				}else if(ChPercentageSupplyChain250>0){ //Red
					MColorSC="#DE87A6";
					PlusSCMTD="+";
				}
				
				if(YChPercentageSupplyChain250<0){ //Green
					YColorSC="#87D692";
				}else if(YChPercentageSupplyChain250>0){ //Red
					YColorSC="#DE87A6";
					PlusSCYTD="+";
				}
				
				
				
			    html += "<tr>";
					html += "<td style='background-color: #EDEFF2; height: 30px; font-size:12px;text-align: left; font-weight: bold;'>Supply Chain Loss</td>";
					//html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;' >"+(TotalSupplyChainLoss250==0?"":Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalSupplyChainLoss250))+"</td>";
					html += "<td style='background-color: #EDEFF2; height: 30px; font-size:12px;text-align: center; font-weight: bold;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;' >"+(MTDTotalSupplyChainLoss2502014==0?"&nbsp;":Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDTotalSupplyChainLoss2502014))+"</td>";
					html += "<td style='background-color: #EDEFF2; height: 30px; font-size:12px;text-align: center; font-weight: bold;' >"+(MTDTotalSupplyChainLoss250==0?"&nbsp;":Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDTotalSupplyChainLoss250))+"</td>";
					html += "<td style='background-color: "+MColorSC+"; height: 30px; font-size:12px;text-align: center;font-weight: bold;' >"+(ChPercentageSupplyChain250==0?"&nbsp;":PlusSCMTD+Math.round(ChPercentageSupplyChain250)+"%")+"</td>";
					html += "<td style='background-color: #EDEFF2; height: 30px; font-size:12px;text-align: center; font-weight: bold;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;' >"+(YTDTotalSupplyChainLoss2502014==0?"&nbsp;":Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDTotalSupplyChainLoss2502014))+"</td>";
					html += "<td style='background-color: #EDEFF2; height: 30px; font-size:12px;text-align: center; font-weight: bold;' >"+(YTDTotalSupplyChainLoss2502015==0?"&nbsp;":Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDTotalSupplyChainLoss2502015))+"</td>";
					html += "<td style='background-color: "+YColorSC+"; height: 30px; font-size:12px;text-align: center;font-weight: bold;' >"+(YChPercentageSupplyChain250==0?"&nbsp;":PlusSCYTD+Math.round(YChPercentageSupplyChain250)+"%")+"</td>";
					
					html += "</tr>";
				
					
					
					String PlusWBMTD="";
					String PlusWBYTD="";
					double ChPercentageWashBreakage250=0;
					if(MTDRawCasesUnitsWashBreakage2502014[0]!=0){
						 ChPercentageWashBreakage250 = ((MTDRawCasesUnitsWashBreakage250[0] - MTDRawCasesUnitsWashBreakage2502014[0])/Utilities.parseDouble(MTDRawCasesUnitsWashBreakage2502014[0]+"")*100);
					}
					if(ChPercentageWashBreakage250>0){PlusWBMTD="+";}
					
					
					
					double YChPercentageWashBreakage250=0;
					if(YTDRawCasesUnitsWashBreakage2502014[0]!=0){
						 YChPercentageWashBreakage250 = ((YTDRawCasesUnitsWashBreakage2502015[0] - YTDRawCasesUnitsWashBreakage2502014[0])/Utilities.parseDouble(YTDRawCasesUnitsWashBreakage2502014[0]+"")*100);
					}
					if(YChPercentageWashBreakage250>0){PlusWBYTD="+";}
										
					//System.out.println(ChPercentageWashBreakage250);
					
					String MColorWB ="";
					String YColorWB="";
					if(ChPercentageWashBreakage250<0){ //Green
						MColorWB="#D7F7DB";
					}else if(ChPercentageWashBreakage250>0){ //Red
						MColorWB="#FAE3EC";
					}
					
					if(YChPercentageWashBreakage250<0){ //Green
						YColorWB="#D7F7DB";
					}else if(YChPercentageWashBreakage250>0){ //Red
						YColorWB="#FAE3EC";
					}
					
					
				html += "<tr >";
					html += "<td valign='top' style='text-align:left; font-size:12px; border:1px solid #EBEBEB'><b>Wash Breakage</b><br/>during sorting and washing</td>";					
					//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsWashBreakage250[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsWashBreakage250[0]))+"</td>";
					html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+ (MTDRawCasesUnitsWashBreakage2502014[0]==0? "&nbsp;" : Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsWashBreakage2502014[0])) +"</td>";
					html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB'>"+(MTDRawCasesUnitsWashBreakage250[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsWashBreakage250[0]))+"</td>";
					
					html += "<td style='background-color: "+MColorWB+"; border:1px solid #EBEBEB; height: 18px; font-size:12px;text-align: center;;' >"+(ChPercentageWashBreakage250==0?"&nbsp;":PlusWBMTD+Math.round(ChPercentageWashBreakage250)+"%")+"</td>";
					html += "<td style='height: 18px; text-align: center;font-size:12px; border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(YTDRawCasesUnitsWashBreakage2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsWashBreakage2502014[0]))+"</td>";
					html += "<td style='height: 18px; text-align: center;font-size:12px; border:1px solid #EBEBEB'>"+(YTDRawCasesUnitsWashBreakage2502015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsWashBreakage2502015[0]))+"</td>";
					html += "<td style='background-color: "+YColorWB+"; height: 18px; font-size:12px;border:1px solid #EBEBEB; text-align: center;' >"+(YChPercentageWashBreakage250==0?"&nbsp;":PlusWBYTD+Math.round(YChPercentageWashBreakage250)+"%")+"</td>";
			    html += "</tr>";

			    double ChPercentageSpecailZabt250=0;
				if(MTDRawCasesUnitsSpecialZabt2502014[0]!=0){
					ChPercentageSpecailZabt250 = ((MTDRawCasesUnitsSpecialZabt250[0] - MTDRawCasesUnitsSpecialZabt2502014[0])/Utilities.parseDouble(MTDRawCasesUnitsSpecialZabt2502014[0]+"")*100);
				}
				
				double YChPercentageSpecailZabt250=0;
				if(YTDRawCasesUnitsSpecialZabt2502014[0]!=0){
					YChPercentageSpecailZabt250 = ((YTDRawCasesUnitsSpecialZabt2502015[0] - YTDRawCasesUnitsSpecialZabt2502014[0])/Utilities.parseDouble(YTDRawCasesUnitsSpecialZabt2502014[0]+"")*100);
				}
				
				String MColorSZ ="";
				String YColorSZ="";
				
				String PlusSZMTD="";
				String PlusSZYTD="";
				if(ChPercentageSpecailZabt250<0){ //Green
					MColorSZ="#D7F7DB";
				}else if(ChPercentageSpecailZabt250>0){ //Red
					MColorSZ="#FAE3EC";
					PlusSZMTD="+";
				}
				
				if(YChPercentageSpecailZabt250<0){ //Green
					YColorWB="#D7F7DB";
				}else if(YChPercentageSpecailZabt250>0){ //Red
					YColorWB="#FAE3EC";
					PlusSZYTD="+";
				}
				
				
				
				//System.out.println(ChPercentageSpecailZabt250);
			    
			    html += "<tr>";
					html += "<td valign='top' style='text-align:left;border:1px solid #EBEBEB; font-size:12px;'><b>Special Zabt</b><br/>breakage received from warehouses and direct route</td>";
					//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsSpecialZabt250[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsSpecialZabt250[0]))+"</td>";
					html += "<td style='text-align:center;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;font-size:12px;'>"+(MTDRawCasesUnitsSpecialZabt2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsSpecialZabt2502014[0]))+"</td>";
					html += "<td style='text-align:center;border:1px solid #EBEBEB;font-size:12px;'>"+(MTDRawCasesUnitsSpecialZabt250[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsSpecialZabt250[0]))+"</td>";
					html += "<td style='background-color: "+MColorSZ+"; border:1px solid #EBEBEB;height: 18px;font-size:12px; text-align: center;' >"+(ChPercentageSpecailZabt250==0?"&nbsp;":PlusSZMTD+Math.round(ChPercentageSpecailZabt250)+"%")+"</td>";
					html += "<td style='height: 18px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;text-align: center;font-size:12px; '>"+(YTDRawCasesUnitsSpecialZabt2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsSpecialZabt2502014[0]))+"</td>";
					html += "<td style='height: 18px; border:1px solid #EBEBEB;text-align: center;font-size:12px; '>"+(YTDRawCasesUnitsSpecialZabt2502015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsSpecialZabt2502015[0]))+"</td>";
					html += "<td style='background-color: "+YColorWB+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(YChPercentageSpecailZabt250==0?"&nbsp;":PlusSZYTD+Math.round(YChPercentageSpecailZabt250)+"%")+"</td>";
				html += "</tr>";
					
					
					
					
					 double ChPercentageSLC250=0;
						if(MTDRawCasesUnitsSLCBR2502014[0]!=0){
							ChPercentageSLC250 = ((MTDRawCasesUnitsSLCBR250[0] - MTDRawCasesUnitsSLCBR2502014[0])/Utilities.parseDouble(MTDRawCasesUnitsSLCBR2502014[0]+"")*100);
						}
						
						double YChPercentageSLC250=0;
						if(YTDRawCasesUnitsSLCBR2502014[0]!=0){
							YChPercentageSLC250 = ((YTDRawCasesUnitsSLCBR2502015[0] - YTDRawCasesUnitsSLCBR2502014[0])/Utilities.parseDouble(YTDRawCasesUnitsSLCBR2502014[0]+"")*100);
						}
					
						String PlusSLBMTD="";
						String PlusSLBYTD="";
						String MColorSLB ="";
						String YColorSLB="";
						if(ChPercentageSLC250<0){ //Green
							MColorSLB="#D7F7DB";
						}else if(ChPercentageSLC250>0){ //Red
							MColorSLB="#FAE3EC";
							PlusSLBMTD="+";
						}
						
						if(YChPercentageSLC250<0){ //Green
							YColorSLB="#D7F7DB";
						}else if(YChPercentageSLC250>0){ //Red
							YColorSLB="#FAE3EC";
							PlusSLBYTD="+";
						}
						
				html += "<tr>";
					html += "<td valign='top' style='text-align:left;border:1px solid #EBEBEB;font-size:12px;'><b>S.L.C BR</b> <br/>breakage during unloading</td>";
					//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsSLCBR250[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsSLCBR250[0]))+"</td>";
					html += "<td style='text-align:center;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;font-size:12px;'>"+(MTDRawCasesUnitsSLCBR2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsSLCBR2502014[0]))+"</td>";
					html += "<td style='text-align:center;border:1px solid #EBEBEB;font-size:12px;'>"+(MTDRawCasesUnitsSLCBR250[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsSLCBR250[0]))+"</td>";
					html += "<td style='background-color: "+MColorSLB+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(ChPercentageSLC250==0?"&nbsp;":PlusSLBMTD+Math.round(ChPercentageSLC250)+"%")+"</td>";
					html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(YTDRawCasesUnitsSLCBR2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsSLCBR2502014[0]))+"</td>";
					html += "<td style='text-align:center;border:1px solid #EBEBEB;font-size:12px;'>"+(YTDRawCasesUnitsSLCBR2502015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsSLCBR2502015[0]))+"</td>";
					html += "<td style='background-color: "+YColorSLB+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(YChPercentageSLC250==0?"&nbsp;":PlusSLBYTD+Math.round(YChPercentageSLC250)+"%")+"</td>";
				html += "</tr>";
				
				
				double ChPercentageSpBreakage250=0;
				if(MTDRawCasesUnitsSpecialBreakage2502014[0]!=0){
					ChPercentageSpBreakage250 = ((MTDRawCasesUnitsSpecialBreakage250[0] - MTDRawCasesUnitsSpecialBreakage2502014[0])/Utilities.parseDouble(MTDRawCasesUnitsSpecialBreakage2502014[0]+"")*100);
				}
				
				double YChPercentageSpBreakage250=0;
				if(YTDRawCasesUnitsSpecialBreakage2502014[0]!=0){
					YChPercentageSpBreakage250 = ((YTDRawCasesUnitsSpecialBreakage2502015[0] - YTDRawCasesUnitsSpecialBreakage2502014[0])/Utilities.parseDouble(YTDRawCasesUnitsSpecialBreakage2502014[0]+"")*100);
				}
				
				String PlusSpBMTD ="";
				String PlusSpBYTD="";
				
				String MColorSpB ="";
				String YColorSpB="";
				
				if(ChPercentageSpBreakage250<0){ //Green
					MColorSpB="#D7F7DB";
				}else if(ChPercentageSpBreakage250>0){ //Red
					MColorSpB="#FAE3EC";
					PlusSpBMTD="+";
				}
				
				if(YChPercentageSpBreakage250<0){ //Green
					YColorSpB="#D7F7DB";
				}else if(YChPercentageSpBreakage250>0){ //Red
					YColorSpB="#FAE3EC";
					PlusSpBYTD="+";
				}
				
				
			
				html += "<tr>";
					html += "<td valign='top' style='text-align:left;border:1px solid #EBEBEB;font-size:12px;'><b>Special Breakage</b><br/>in transit between factory and depot</td>";
					//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsSpecialBreakage250[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsSpecialBreakage250[0]))+"</td>";
					html += "<td style='text-align:center;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;font-size:12px;'>"+(MTDRawCasesUnitsSpecialBreakage2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsSpecialBreakage2502014[0]))+"</td>";
					html += "<td style='text-align:center;border:1px solid #EBEBEB;font-size:12px;'>"+(MTDRawCasesUnitsSpecialBreakage250[0]==0?"":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsSpecialBreakage250[0]))+"</td>";
					html += "<td style='background-color: "+MColorSpB+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(ChPercentageSpBreakage250==0?"&nbsp;":PlusSpBMTD+Math.round(ChPercentageSpBreakage250)+"%")+"</td>";
					html += "<td style='text-align:center;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;font-size:12px;'>"+(YTDRawCasesUnitsSpecialBreakage2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsSpecialBreakage2502014[0]))+"</td>";
					html += "<td style='text-align:center;border:1px solid #EBEBEB;font-size:12px;'>"+(YTDRawCasesUnitsSpecialBreakage2502015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsSpecialBreakage2502015[0]))+"</td>";
					html += "<td style='background-color: "+YColorSpB+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(YChPercentageSpBreakage250==0?"&nbsp;":PlusSpBYTD+Math.round(YChPercentageSpBreakage250)+"%")+"</td>";
				html += "</tr>";
				
				double ChPercentageSpBurst250=0;
				if(MTDRawCasesUnitsSpecialBurst2502014[0]!=0){
					ChPercentageSpBurst250 = ((MTDRawCasesUnitsSpecialBurst250[0] - MTDRawCasesUnitsSpecialBurst2502014[0])/Utilities.parseDouble(MTDRawCasesUnitsSpecialBurst2502014[0]+"")*100);
				}
				
				double YChPercentageSpBurst250=0;
				if(YTDRawCasesUnitsSpecialBurst2502014[0]!=0){
					YChPercentageSpBurst250 = ((YTDRawCasesUnitsSpecialBurst2502015[0] - YTDRawCasesUnitsSpecialBurst2502014[0])/Utilities.parseDouble(YTDRawCasesUnitsSpecialBurst2502014[0]+"")*100);
				}
				
				
				String PlusSpBuMTD="";
				String PlusSpBuYTD="";
				String MColorSpBu ="";
				String YColorSpBu="";
				if(ChPercentageSpBurst250<0){ //Green
					MColorSpBu="#D7F7DB";
				}else if(ChPercentageSpBurst250>0){ //Red
					MColorSpBu="#FAE3EC";
					PlusSpBuMTD="+";
				}
				
				if(YChPercentageSpBurst250<0){ //Green
					YColorSpBu="#D7F7DB";
				}else if(YChPercentageSpBurst250>0){ //Red
					YColorSpBu="#FAE3EC";
					PlusSpBuYTD="+";
				}
				
				html += "<tr>";
				html += "<td valign='top' style='text-align:left;border:1px solid #EBEBEB;font-size:12px;'><b>Special Burst</b><br/>in warehouses</td>";
				//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsSpecialBurst250[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsSpecialBurst250[0]))+"</td>";
				html += "<td style='text-align:center;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;font-size:12px;'>"+(MTDRawCasesUnitsSpecialBurst2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsSpecialBurst2502014[0]))+"</td>";
				
				html += "<td style='text-align:center;border:1px solid #EBEBEB;font-size:12px;'>"+(MTDRawCasesUnitsSpecialBurst250[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsSpecialBurst250[0]))+"</td>";
				html += "<td style='background-color: "+MColorSpBu+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(ChPercentageSpBurst250==0?"&nbsp;":PlusSpBuMTD+Math.round(ChPercentageSpBurst250)+"%")+"</td>";
				html += "<td style='text-align:center;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;font-size:12px;'>"+(YTDRawCasesUnitsSpecialBurst2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsSpecialBurst2502014[0]))+"</td>";
				html += "<td style='text-align:center;border:1px solid #EBEBEB;font-size:12px;'>"+(YTDRawCasesUnitsSpecialBurst2502015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsSpecialBurst2502015[0]))+"</td>";
				html += "<td style='background-color: "+YColorSpBu+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(YChPercentageSpBurst250==0?"&nbsp;":PlusSpBuYTD+Math.round(YChPercentageSpBurst250)+"%")+"</td>";
			html += "</tr>";
			
			
			double ChPercentageDepotBurst250=0;
			if(MTDRawCasesUnitsDepotBurst2502014[0]!=0){
				ChPercentageDepotBurst250 = ((MTDRawCasesUnitsDepotBurst250[0] - MTDRawCasesUnitsDepotBurst2502014[0])/Utilities.parseDouble(MTDRawCasesUnitsDepotBurst2502014[0]+"")*100);
			}
			

			double YChPercentageDepotBurst250=0;
			if(YTDRawCasesUnitsDepotBurst2502014[0]!=0){
				YChPercentageDepotBurst250 = ((YTDRawCasesUnitsDepotBurst2502015[0] - YTDRawCasesUnitsDepotBurst2502014[0])/Utilities.parseDouble(YTDRawCasesUnitsDepotBurst2502014[0]+"")*100);
			}
			
			String PlusDBuMTD="";
			String PlusDBuYTD="";
			String MColorDBu ="";
			String YColorDBu="";
			if(ChPercentageDepotBurst250<0){ //Green
				MColorDBu="#D7F7DB";
			}else if(ChPercentageDepotBurst250>0){ //Red
				MColorDBu="#FAE3EC";
				PlusDBuMTD="+";
			}
			
			if(YChPercentageDepotBurst250<0){ //Green
				YColorDBu="#D7F7DB";
			}else if(YChPercentageDepotBurst250>0){ //Red
				YColorDBu="#FAE3EC";
				PlusDBuYTD="+";
			}
			
		html += "<tr>";
		html += "<td valign='top' style='text-align:left;border:1px solid #EBEBEB;font-size:12px;'><b>Depot Burst</b><br/>during loading at DPG4</td>";
		//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsDepotBurst250[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsDepotBurst250[0]))+"</td>";
		html += "<td style='text-align:center;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;font-size:12px;'>"+(MTDRawCasesUnitsDepotBurst2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsDepotBurst2502014[0]))+"</td>";
		html += "<td style='text-align:center;border:1px solid #EBEBEB;font-size:12px;'>"+(MTDRawCasesUnitsDepotBurst250[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsDepotBurst250[0]))+"</td>";
		html += "<td style='background-color: "+MColorDBu+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(ChPercentageDepotBurst250==0?"&nbsp;":PlusDBuMTD+Math.round(ChPercentageDepotBurst250)+"%")+"</td>";
		html += "<td style='text-align:center;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;font-size:12px;'>"+(YTDRawCasesUnitsDepotBurst2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsDepotBurst2502014[0]))+"</td>";
		html += "<td style='text-align:center;border:1px solid #EBEBEB;font-size:12px;'>"+(YTDRawCasesUnitsDepotBurst2502015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsDepotBurst2502015[0]))+"</td>";
		html += "<td style='background-color: "+YColorDBu+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(YChPercentageDepotBurst250==0?"&nbsp;":PlusDBuYTD+Math.round(YChPercentageDepotBurst250)+"%")+"</td>";
	html += "</tr>";
	
	
	double ChPercentageDepotFallenBurst250=0;
	if(MTDRawCasesUnitsDepotFallenBurst2502014[0]!=0){
		ChPercentageDepotFallenBurst250 = ((MTDRawCasesUnitsDepotFallenBurst250[0] - MTDRawCasesUnitsDepotFallenBurst2502014[0])/Utilities.parseDouble(MTDRawCasesUnitsDepotFallenBurst2502014[0]+"")*100);
	}
	
	double YChPercentageDepotFallenBurst250=0;
	if(YTDRawCasesUnitsDepotFallenBurst2502014[0]!=0){
		YChPercentageDepotFallenBurst250 = ((YTDRawCasesUnitsDepotFallenBurst2502015[0] - YTDRawCasesUnitsDepotFallenBurst2502014[0])/Utilities.parseDouble(YTDRawCasesUnitsDepotFallenBurst2502014[0]+"")*100);
	}
	
	String PlusDFMTD ="";
	String PlusDFYTD="";
	
	String MColorDF ="";
	String YColorDF="";
	if(ChPercentageDepotFallenBurst250<0){ //Green
		MColorDF="#D7F7DB";
	}else if(ChPercentageDepotFallenBurst250>0){ //Red
		MColorDF="#FAE3EC";
		PlusDFMTD="+";
	}
	
	if(YChPercentageDepotFallenBurst250<0){ //Green
		YColorDF="#D7F7DB";
	}else if(YChPercentageDepotFallenBurst250>0){ //Red
		YColorDF="#FAE3EC";
		PlusDFYTD="+";
	}
	
		html += "<tr>";
		html += "<td valign='top' style='text-align:left;border:1px solid #EBEBEB;font-size:12px;'><b>Depot Fallen Burst</b><br/>at DPG4</td>";
		//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsDepotFallenBurst250[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsDepotFallenBurst250[0]))+"</td>";
		html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(MTDRawCasesUnitsDepotFallenBurst2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsDepotFallenBurst2502014[0]))+"</td>";
		html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(MTDRawCasesUnitsDepotFallenBurst250[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsDepotFallenBurst250[0]))+"</td>";
		html += "<td style='background-color: "+MColorDF+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(ChPercentageDepotFallenBurst250==0?"&nbsp;":PlusDFMTD+Math.round(ChPercentageDepotFallenBurst250)+"%")+"</td>";
		html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(YTDRawCasesUnitsDepotFallenBurst2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsDepotFallenBurst2502014[0]))+"</td>";
		html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(YTDRawCasesUnitsDepotFallenBurst2502015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsDepotFallenBurst2502015[0]))+"</td>";
		html += "<td style='bac1kground-color: "+MColorDF+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(YChPercentageDepotFallenBurst250==0?"&nbsp;":PlusDFYTD+Math.round(YChPercentageDepotFallenBurst250)+"%")+"</td>";
	html += "</tr>";
	
	double ChPercentageLifterBreakage250=0;
	if(MTDRawCasesUnitsLifterBreakage2502014[0]!=0){
		ChPercentageLifterBreakage250 = ((MTDRawCasesUnitsLifterBreakage250[0] - MTDRawCasesUnitsLifterBreakage2502014[0])/Utilities.parseDouble(MTDRawCasesUnitsLifterBreakage2502014[0]+"")*100);
	}
	
	double YChPercentageLifterBreakage250=0;
	if(YTDRawCasesUnitsLifterBreakage2502014[0]!=0){
		YChPercentageLifterBreakage250 = ((YTDRawCasesUnitsLifterBreakage2502015[0] - YTDRawCasesUnitsLifterBreakage2502014[0])/Utilities.parseDouble(YTDRawCasesUnitsLifterBreakage2502014[0]+"")*100);
	}
	
	String PlusLBMTD="";
	String PlusLBYTD="";
	String MColorLB ="";
	String YColorLB="";
	if(ChPercentageLifterBreakage250<0){ //Green
		MColorLB="#D7F7DB";
	}else if(ChPercentageLifterBreakage250>0){ //Red
		MColorLB="#FAE3EC";
		PlusLBMTD="+";
	}
	
	if(YChPercentageLifterBreakage250<0){ //Green
		YColorLB="#D7F7DB";
	}else if(YChPercentageLifterBreakage250>0){ //Red
		YColorLB="#FAE3EC";
		PlusLBYTD="+";
	}
	

		html += "<tr>";
		html += "<td valign='top' style='text-align:left;border:1px solid #EBEBEB;font-size:12px;'><b>Lifter Breakage</b><br/>at DPG4</td>";
		//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsLifterBreakage250[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsLifterBreakage250[0]))+"</td>";
		html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(MTDRawCasesUnitsLifterBreakage2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsLifterBreakage2502014[0]))+"</td>";
		html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(MTDRawCasesUnitsLifterBreakage250[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsLifterBreakage250[0]))+"</td>";
		html += "<td style='background-color: "+MColorLB+";border:1px solid #EBEBEB; height: 18px; font-size:12px;text-align: center;' >"+(ChPercentageLifterBreakage250==0?"&nbsp;":PlusLBMTD+Math.round(ChPercentageLifterBreakage250)+"%")+"</td>";
		html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(YTDRawCasesUnitsLifterBreakage2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsLifterBreakage2502014[0]))+"</td>";
		html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(YTDRawCasesUnitsLifterBreakage2502015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsLifterBreakage2502015[0]))+"</td>";
		html += "<td style='background-color: "+YColorLB+"; border:1px solid #EBEBEB;height: 18px;font-size:12px; text-align: center;' >"+(YChPercentageLifterBreakage250==0?"&nbsp;":PlusLBYTD+Math.round(YChPercentageLifterBreakage250)+"%")+"</td>";
	html += "</tr>";
	
	double ChPercentageLifterBurst250=0;
	if(MTDRawCasesUnitsLifterBurst2502014[0]!=0){
		ChPercentageLifterBurst250 = ((MTDRawCasesUnitsLifterBurst250[0] - MTDRawCasesUnitsLifterBurst2502014[0])/Utilities.parseDouble(MTDRawCasesUnitsLifterBurst2502014[0]+"")*100);
	}
	
	double YChPercentageLifterBurst250=0;
	if(YTDRawCasesUnitsLifterBurst2502014[0]!=0){
		YChPercentageLifterBurst250 = ((YTDRawCasesUnitsLifterBurst2502015[0] - YTDRawCasesUnitsLifterBurst2502014[0])/Utilities.parseDouble(YTDRawCasesUnitsLifterBurst2502014[0]+"")*100);
	}
	
	
	String PlusLBuMTD = "";
	String PlusLBuYTD="";
	
	String MColorLBu ="";
	String YColorLBu="";
	if(ChPercentageLifterBurst250<0){ //Green
		MColorLBu="#D7F7DB";
	}else if(ChPercentageLifterBurst250>0){ //Red
		MColorLBu="#FAE3EC";
		PlusLBuMTD="+";
	}
	
	if(YChPercentageLifterBurst250<0){ //Green
		YColorLBu="#D7F7DB";
	}else if(YChPercentageLifterBurst250>0){ //Red
		YColorLBu="#FAE3EC";
		PlusLBuYTD="+";
	}

	html += "<tr>";
		html += "<td valign='top' style='text-align:left;border:1px solid #EBEBEB;font-size:12px;'><b>Lifter Burst</b><br/>at DPG4</td>";
		//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsLifterBurst250[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsLifterBurst250[0]))+"</td>";
		html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(MTDRawCasesUnitsLifterBurst2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsLifterBurst2502014[0]))+"</td>";
		html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(MTDRawCasesUnitsLifterBurst250[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsLifterBurst250[0]))+"</td>";
		html += "<td style='background-color: "+MColorLBu+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(ChPercentageLifterBurst250==0?"&nbsp;":PlusLBuMTD+Math.round(ChPercentageLifterBurst250)+"%")+"</td>";
		html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(YTDRawCasesUnitsLifterBurst2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsLifterBurst2502014[0]))+"</td>";
		html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(YTDRawCasesUnitsLifterBurst2502015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsLifterBurst2502015[0]))+"</td>";
		html += "<td style='background-color: "+YColorLBu+";border:1px solid #EBEBEB; height: 18px;font-size:12px; text-align: center;' >"+(YChPercentageLifterBurst250==0?"&nbsp;":PlusLBuYTD+Math.round(YChPercentageLifterBurst250)+"%")+"</td>";
	html += "</tr>";

	double ChPercentageFallenBreakage250=0;
	if(MTDRawCasesUnitsFallenBreakage2502014[0]!=0){
		ChPercentageFallenBreakage250 = ((MTDRawCasesUnitsFallenBreakage250[0] - MTDRawCasesUnitsFallenBreakage2502014[0])/Utilities.parseDouble(MTDRawCasesUnitsFallenBreakage2502014[0]+"")*100);
	}
	
	double YChPercentageFallenBreakage250=0;
	if(YTDRawCasesUnitsFallenBreakage2502014[0]!=0){
		YChPercentageFallenBreakage250 = ((YTDRawCasesUnitsFallenBreakage2502015[0] - YTDRawCasesUnitsFallenBreakage2502014[0])/Utilities.parseDouble(YTDRawCasesUnitsFallenBreakage2502014[0]+"")*100);
	}
	
	String PlusLFBMTD="";
	String PlusLFBYTD="";
	
	
	String MColorLFB ="";
	String YColorLFB="";
	if(ChPercentageFallenBreakage250<0){ //Green
		MColorLFB="#D7F7DB";
	}else if(ChPercentageFallenBreakage250>0){ //Red
		MColorLFB="#FAE3EC";
		PlusLFBMTD="+";
	}
	
	if(YChPercentageFallenBreakage250<0){ //Green
		YColorLFB="#D7F7DB";
	}else if(YChPercentageFallenBreakage250>0){ //Red
		YColorLFB="#FAE3EC";
		PlusLFBYTD="+";
	}
	
	html += "<tr>";
		html += "<td valign='top' style='text-align:left;font-size:12px;border:1px solid #EBEBEB;'><b>Fallen Breakage</b><br/>at DPG4 or factory</td>";
		//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsFallenBreakage250[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsFallenBreakage250[0]))+"</td>";
		html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(MTDRawCasesUnitsFallenBreakage2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsFallenBreakage2502014[0]))+"</td>";
		html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(MTDRawCasesUnitsFallenBreakage250[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsFallenBreakage250[0]))+"</td>";
		html += "<td style='background-color: "+MColorLFB+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(ChPercentageFallenBreakage250==0?"&nbsp;":PlusLFBMTD+Math.round(ChPercentageFallenBreakage250)+"%")+"</td>";
		html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(YTDRawCasesUnitsFallenBreakage2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsFallenBreakage2502014[0]))+"</td>";
		html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(YTDRawCasesUnitsFallenBreakage2502015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsFallenBreakage2502015[0]))+"</td>";
		html += "<td style='background-color: "+YColorLFB+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(YChPercentageFallenBreakage250==0?"&nbsp;":PlusLFBYTD+Math.round(YChPercentageFallenBreakage250)+"%")+"</td>";
	html += "</tr>";

	double ChPercentageProductionBreakage250=0;
	if(MTDTotalUnitsLoss1RawCases2502014!=0){
		ChPercentageProductionBreakage250 = ((MTDTotalUnitsLoss1RawCases250 - MTDTotalUnitsLoss1RawCases2502014)/Utilities.parseDouble(MTDTotalUnitsLoss1RawCases2502014+"")*100);
	}
	
	double YChPercentageProductionBreakage250=0;
	if(YTDTotalUnitsLoss1RawCases2502014!=0){
		YChPercentageProductionBreakage250 = ((YTDTotalUnitsLoss1RawCases2502015 - YTDTotalUnitsLoss1RawCases2502014)/Utilities.parseDouble(YTDTotalUnitsLoss1RawCases2502014+"")*100);
	}
	
	String PlusLDPMTD ="";
	String PlusLDPYTD="";
	
	String MColorLDP ="#EDEFF2";
	String YColorLDP="#EDEFF2";
	if(ChPercentageProductionBreakage250<0){ //Green
		MColorLDP="#D7F7DB";
	}else if(ChPercentageProductionBreakage250>0){ //Red
		MColorLDP="#FAE3EC";
		PlusLDPMTD="+";
	}
	
	if(YChPercentageProductionBreakage250<0){ //Green
		YColorLDP="#D7F7DB";
	}else if(YChPercentageProductionBreakage250>0){ //Red
		YColorLDP="#FAE3EC";
		PlusLDPYTD="+";
	}
	
	
	double ChPercentageProductionLoss250=0;
	if(MTDRawCasesUnitsDuringPro2502014[0]!=0){
		ChPercentageProductionLoss250 = ((MTDRawCasesUnitsDuringPro250[0] - MTDRawCasesUnitsDuringPro2502014[0])/Utilities.parseDouble(MTDRawCasesUnitsDuringPro2502014[0]+"")*100);
	}
	
	
	double YChPercentageProductionLoss250=0;
	if(YTDRawCasesUnitsDuringPro2502014[0]!=0){
		YChPercentageProductionLoss250 = ((YTDRawCasesUnitsDuringPro2502015[0] - YTDRawCasesUnitsDuringPro2502014[0])/Utilities.parseDouble(YTDRawCasesUnitsDuringPro2502014[0]+"")*100);
	}
	
	
	String MColorPL ="";
	String YColorPL="";
	
	String PlusPLMTD="";
	String PlusPLYTD="";
	if(ChPercentageProductionLoss250<0){ //Green
		MColorPL="#87D692";
	}else if(ChPercentageProductionLoss250>0){ //Red
		MColorPL="#DE87A6";
		PlusPLMTD="+";
	}
	
	if(YChPercentageProductionLoss250<0){ //Green
		YColorPL="#87D692";
	}else if(YChPercentageProductionLoss250>0){ //Red
		YColorPL="#DE87A6";
		PlusPLYTD="+";
	}
	
	
	

	html += "<tr>";
		html += "<td valign='top' style='text-align:left;font-size:12px;border:1px solid #EBEBEB;'><b>Production Breakage</b><br/>at conveyor area</td>";
		//html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;' >"+(RawCasesUnitsDuringPro250[0]==0?"":Utilities.getDisplayCurrencyFormatRoundedAccounting(RawCasesUnitsDuringPro250[0]))+"</td>";
		html += "<td style=' height: 30px; text-align: center; font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;' >"+(MTDRawCasesUnitsDuringPro2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDRawCasesUnitsDuringPro2502014[0]))+"</td>";
		html += "<td style=' height: 30px; text-align: center; font-size:12px;f1ont-weight: bold;' >"+(MTDRawCasesUnitsDuringPro250[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDRawCasesUnitsDuringPro250[0]))+"</td>";
		html += "<td style='background-color: "+MColorLDP+"; height: 30px; text-align: center;font-size:12px;f1ont-weight: bold;' >"+(ChPercentageProductionLoss250==0?"&nbsp;":PlusPLMTD+Math.round(ChPercentageProductionLoss250)+"%")+"</td>";
		html += "<td style=' height: 30px; text-align: center; font-size:12px;font-we1ight: bold;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;' >"+(YTDRawCasesUnitsDuringPro2502014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDRawCasesUnitsDuringPro2502014[0]))+"</td>";
		html += "<td style=' height: 30px; text-align: center; font-size:12px;font-weig1ht: bold;' >"+(YTDRawCasesUnitsDuringPro2502015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDRawCasesUnitsDuringPro2502015[0]))+"</td>";
		html += "<td style='background-color: "+YColorLDP+"; height: 30px; text-align: center;font-size:12px;font-w1eight: bold;' >"+(YChPercentageProductionLoss250==0?"&nbsp;":PlusPLYTD+Math.round(YChPercentageProductionLoss250)+"%")+"</td>";
		
	html += "</tr>";
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	html += "<tr>";
	html += "<td style='background-color: #EDEFF2; height: 30px; text-align: left; font-weight: bold;font-size:12px;' >Production Loss</td>";
	//html += "<td style='text-align:left;font-size:12px;'>"+(TotalUnitsLoss1RawCases250==0?"":Utilities.getDisplayCurrencyFormat(TotalUnitsLoss1RawCases250))+"</td>";
			html += "<td style='text-align:center;background-color: #EDEFF2;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;font-weight: bold;'>"+(MTDTotalUnitsLoss1RawCases2502014==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDTotalUnitsLoss1RawCases2502014))+"</td>";
			html += "<td style='text-align:center;background-color: #EDEFF2;font-size:12px;border:1px solid #EDEFF2;font-weight: bold;'>"+(MTDTotalUnitsLoss1RawCases250==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDTotalUnitsLoss1RawCases250))+"</td>";
			html += "<td style='background-color: "+MColorPL+"; border:1px solid #EDEFF2;height: 18px;font-size:12px;font-weight: bold; text-align: center;' >"+(ChPercentageProductionBreakage250==0?"&nbsp;":PlusLDPMTD+Math.round(ChPercentageProductionBreakage250)+"%")+"</td>";
			html += "<td style='text-align:center;background-color: #EDEFF2;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;font-weight: bold;'>"+(YTDTotalUnitsLoss1RawCases2502014==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDTotalUnitsLoss1RawCases2502014))+"</td>";
			html += "<td style='text-align:center;background-color: #EDEFF2;font-size:12px;border:1px solid #EDEFF2;font-weight: bold;'>"+(YTDTotalUnitsLoss1RawCases2502015==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDTotalUnitsLoss1RawCases2502015))+"</td>";
			html += "<td style='background-color: "+YColorPL+"; border:1px solid #EDEFF2;height: 18px;font-size:12px;font-weight: bold; text-align: center;' >"+(YChPercentageProductionBreakage250==0?"&nbsp;":PlusLDPYTD+Math.round(YChPercentageProductionBreakage250)+"%")+"</td>";
	
html += "</tr>";
	


String PlusPBMTD="";
String PlusPBYTD="";

String MColorPB ="";
String YColorPB="";
if(ChPercentageProductionBreakage1250<0){ //Green
	MColorPB="#D7F7DB";
}else if(ChPercentageProductionBreakage1250>0){ //Red
	MColorPB="#FAE3EC";
	PlusPBMTD="+";
}

if(YChPercentageProductionBreakage1250<0){ //Green
	YColorLDP="#D7F7DB";
}else if(YChPercentageProductionBreakage1250>0){ //Red
	YColorLDP="#FAE3EC";
	PlusPBYTD="+";
}

	html += "<tr>";
		html += "<td valign='top' style='text-align:left;font-size:12px;border:1px solid #EBEBEB;'><b>Production Breakage</b><br/>at filling area</td>";
		//html += "<td style='text-align:left;font-size:12px;'>"+(TotalUnitsLoss1RawCases250==0?"":Utilities.getDisplayCurrencyFormat(TotalUnitsLoss1RawCases250))+"</td>";
				html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(MTDTotalUnitsLoss1RawCases2502014==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDTotalUnitsLoss1RawCases2502014))+"</td>";
				html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(MTDTotalUnitsLoss1RawCases250==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDTotalUnitsLoss1RawCases250))+"</td>";
				html += "<td style='background-color: "+MColorLDP+"; border:1px solid #EBEBEB;height: 18px;font-size:12px; text-align: center;' >"+(ChPercentageProductionBreakage250==0?"&nbsp;":PlusLDPMTD+Math.round(ChPercentageProductionBreakage250)+"%")+"</td>";
				html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(YTDTotalUnitsLoss1RawCases2502014==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDTotalUnitsLoss1RawCases2502014))+"</td>";
				html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(YTDTotalUnitsLoss1RawCases2502015==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDTotalUnitsLoss1RawCases2502015))+"</td>";
				html += "<td style='background-color: "+YColorLDP+"; border:1px solid #EBEBEB;height: 18px;font-size:12px; text-align: center;' >"+(YChPercentageProductionBreakage250==0?"&nbsp;":PlusLDPYTD+Math.round(YChPercentageProductionBreakage250)+"%")+"</td>";
			html += "</tr>";
	
	
///////////////////////////////////// Grand Total
	
String MTDTotalColor="";
String YTDTotalColor="";
String MTDTotalPlus="";
String YTDTotalPlus="";



long TotalLossMTD2014=MTDTotalMarketLoss2502014+MTDTotalSupplyChainLoss2502014+MTDTotalUnitsLoss1RawCases2502014;
long TotalLossMTD2015=MTDTotalMarketLoss250+MTDTotalSupplyChainLoss250+MTDTotalUnitsLoss1RawCases250;
double TotalLossMTDChangePercent=((TotalLossMTD2015-TotalLossMTD2014)/Utilities.parseDouble(TotalLossMTD2014+""))*100;

//System.out.println(TotalLossMTDChangePercent);


if(TotalLossMTDChangePercent>0){
MTDTotalColor="#DE87A6";
MTDTotalPlus="+";
}else{
MTDTotalColor = "#87D692";
}


long TotalLossYTD2014=YTDTotalMarketLoss2502014+YTDTotalSupplyChainLoss2502014+YTDTotalUnitsLoss1RawCases2502014;
long TotalLossYTD2015=YTDTotalMarketLoss2502015+YTDTotalSupplyChainLoss2502015+YTDTotalUnitsLoss1RawCases2502015;
double TotalLossYTDChangePercent=((TotalLossYTD2015-TotalLossYTD2014)/Utilities.parseDouble(TotalLossYTD2014+""))*100;


if(TotalLossYTDChangePercent>0){
YTDTotalColor="#DE87A6";
YTDTotalPlus="+";
}else{
YTDTotalColor = "#87D692";
}

html += "<tr>";
html += "<td >&nbsp;</td>";
html += "<tr>";

html += "<tr>";
html += "<td style='background-color: #EDEFF2; height: 30px; text-align:left;font-size:12px; border:1px solid #BAC4E8;border-left:3px solid #EBEBEB;border-color:#BAC4E8;font-weight: bold;'><b>Total Loss</b></td>";
//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsDuringPro250[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsDuringPro250[0]))+"</td>";
html += "<td style='text-align:center;background-color: #EDEFF2; height: 30px;font-size:12px;border:1px solid #BAC4E8; border-left:3px solid #BAC4E8;b1order-color:white;font-weight: bold;'>"+(TotalLossMTD2014==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(TotalLossMTD2014))+"</td>";
html += "<td style='text-align:center;background-color: #EDEFF2; height: 30px;font-size:12px;border:1px solid #BAC4E8;b1order-color:white;font-weight: bold;'>"+(TotalLossMTD2015==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(TotalLossMTD2015))+"</td>";
html += "<td style='background-color: "+MTDTotalColor+";  height: 30px;border:1px solid #BAC4E8;height: 18px; font-size:12px;b1order-color:white;text-align: center;font-weight: bold;' >"+(TotalLossMTDChangePercent==0?"&nbsp;":MTDTotalPlus+Math.round(TotalLossMTDChangePercent)+"%")+"</td>";
html += "<td style='text-align:center;background-color: #EDEFF2; height: 30px;font-size:12px;border:1px solid #BAC4E8; border-left:3px solid #BAC4E8;font-weight: bold;'>"+(TotalLossYTD2014==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(TotalLossYTD2014))+"</td>";
html += "<td style='text-align:center;background-color: #EDEFF2; height: 30px;font-size:12px;border:1px solid #BAC4E8;b1order-color:white;font-weight: bold;'>"+(TotalLossYTD2015==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(TotalLossYTD2015))+"</td>";
html += "<td style='background-color: "+YTDTotalColor+";  height: 30x;border:1px solid #BAC4E8;height: 18px; b1order-color:white;font-size:12px;text-align: center;font-weight: bold;' >"+(TotalLossYTDChangePercent==0?"&nbsp;":YTDTotalPlus+Math.round(TotalLossYTDChangePercent)+"%")+"</td>";
html += "</tr>";

	
	///////////////////////////////////////////////////////////////////
/////////////////////////////// for 1000 ML ////////////////////////////////////////

html += "<tr><td>&nbsp;</td></tr>";
html += "<tr><td>&nbsp;</td></tr>";

html += "<tr>";
html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; border-left:3px solid #EBEBEB;border-color:#BAC4E8;font-weight: bold;width:30%'>1000ML in Cases</td>";
//html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold;width:15%'>7 Days</td>";
html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; border-left:3px solid #EBEBEB;border-color:#BAC4E8;font-weight: bold;width:15%' colspan=3>MTD</td>";
html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; border-left:3px solid #EBEBEB;border-color:#BAC4E8;font-weight: bold;width:15%' colspan=3>YTD</td>";
html += "</tr>";


html += "<tr >";
	html += "<td style='b1ackground-color: #EDEFF2; height: 18px; font-size:12px;  border:1px solid #EBEBEB;border-color:white;text-align: center; font-weight: bold;'></td>";	
	html += "<td style='background-color: #C8CFE6; c1olor: white;height: 18px; font-size:12px;  border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;text-align: center; font-weight: bold;'>2014</td>";
	html += "<td style='background-color: #C8CFE6; c1olor: white;height: 18px; font-size:12px;  border:1px solid #EBEBEB;border-color:white;text-align: center; font-weight: bold;'>2015</td>";
	html += "<td style='background-color: #C8CFE6; c1olor: white;height: 18px; font-size:12px; border:1px solid #EBEBEB;border-color:white; text-align: center; font-weight: bold;'>Ch(%)</td>";	
	html += "<td style='background-color: #C8CFE6; c1olor: white;height: 18px; font-size:12px;  border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;text-align: center; font-weight: bold;'>2014</td>";
	html += "<td style='background-color: #C8CFE6; c1olor: white;height: 18px; font-size:12px; border:1px solid #EBEBEB;border-color:white; text-align: center; font-weight: bold;'>2015</td>";	
	html += "<td style='background-color: #C8CFE6; c1olor: white;height: 18px; font-size:12px;  border:1px solid #EBEBEB;border-color:white;text-align: center; font-weight: bold;'>Ch(%)</td>";	
html += "</tr>";


MColorML="#EDEFF2";
YColorML="#EDEFF2";
PlusMLMTD="";
PlusMLYTD="";


if(ChPercentageMarketLoss1000<0){ //Green
	MColorML="#87D692";
}else if(ChPercentageMarketLoss1000>0){ //Red
	MColorML="#DE87A6";
	PlusMLMTD="+";
}

if(YChPercentageMarketLoss1000<0){ //Green
	YColorML="#87D692";
}else if(YChPercentageMarketLoss1000>0){ //Red
	YColorML="#DE87A6";
	PlusMLYTD="+";
}

html += "<tr >";
	html += "<td style='background-color: #EDEFF2; height: 30x; font-size:12px; text-align: left; font-weight: bold;'>Market Loss<br/></td>";
	//html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;' >"+(TotalMarketLoss250==0?"":Utilities.getDisplayCurrencyFormat(TotalMarketLoss250))+"</td>";
	html += "<td style='background-color: #EDEFF2; height: 30px; font-size:12px;text-align: center; font-weight: bold;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;' >"+(MTDTotalMarketLoss10002014==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDTotalMarketLoss10002014))+"<br/></td>";
	html += "<td style='background-color: #EDEFF2; height: 30px; font-size:12px;text-align: center; font-weight: bold;' >"+(MTDTotalMarketLoss1000==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDTotalMarketLoss1000))+"<br/></td>";
	html += "<td style='background-color: "+MColorML+"; height: 30px; font-size:12px;text-align: center; font-weight: bold;' >"+(ChPercentageMarketLoss1000==0?"&nbsp;":PlusMLMTD+Math.round(ChPercentageMarketLoss1000)+"%")+"<br/></td>";
	html += "<td style='background-color: #EDEFF2; height: 30px; font-size:12px;text-align: center; font-weight: bold;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;' >"+(YTDTotalMarketLoss10002014==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDTotalMarketLoss10002014))+"<br/></td>";
	html += "<td style='background-color: #EDEFF2; height: 30px; font-size:12px;text-align: center; font-weight: bold;' >"+(YTDTotalMarketLoss10002015==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDTotalMarketLoss10002015))+"<br/></td>";
	html += "<td style='background-color: "+YColorML+"; height: 30px; font-size:12px;text-align: center; font-weight: bold;' >"+(YChPercentageMarketLoss1000==0?"&nbsp;":PlusMLYTD+Math.round(YChPercentageMarketLoss1000)+"%")+"<br/></td>";
	
	html += "</tr>";

	
	PlusMBMTD="";
	 PlusMBYTD="";
	double ChPercentageMarketBreakage1000=0;
	if(MTDBreakageAllowanceRawCase10002014!=0){
		ChPercentageMarketBreakage1000 = ((MTDBreakageAllowanceRawCase1000 - MTDBreakageAllowanceRawCase10002014)/Utilities.parseDouble(MTDBreakageAllowanceRawCase10002014+"")*100);
	}
	if(ChPercentageMarketBreakage1000>0){PlusMBMTD="+";}



	double YChPercentageMarketBreakage1000=0;
	if(YTDBreakageAllowanceRawCase10002014!=0){
		YChPercentageMarketBreakage1000 = ((YTDBreakageAllowanceRawCase10002015 - YTDBreakageAllowanceRawCase10002014)/Utilities.parseDouble(YTDBreakageAllowanceRawCase10002014+"")*100);
	}
	if(YChPercentageMarketBreakage1000>0){PlusMBYTD="+";}
						
	//System.out.println(ChPercentageWashBreakage250);

	 MColorMB ="";
	 YColorMB="";
	if(ChPercentageMarketBreakage1000<0){ //Green
		MColorMB="#D7F7DB";
	}else if(ChPercentageMarketBreakage1000>0){ //Red
		MColorMB="#FAE3EC";
	}

	if(YChPercentageMarketBreakage1000<0){ //Green
		YColorMZ="#D7F7DB";
	}else if(YChPercentageMarketBreakage1000>0){ //Red
		YColorMZ="#FAE3EC";
	}


	html += "<tr >";
	html += "<td valign='top' style='text-align:left; font-size:12px; border:1px solid #EBEBEB'><b>Breakage Allowance</b><br/>Empty given to distributor againts Breakage</td>";					
	//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsWashBreakage250[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsWashBreakage250[0]))+"</td>";
	html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+ (MTDBreakageAllowanceRawCase10002014==0? "&nbsp;" : Utilities.getDisplayCurrencyFormat(MTDBreakageAllowanceRawCase10002014)) +"</td>";
	html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB'>"+(MTDBreakageAllowanceRawCase1000==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDBreakageAllowanceRawCase1000))+"</td>";

	html += "<td style='background-color: "+MColorMB+"; border:1px solid #EBEBEB; height: 18px; font-size:12px;text-align: center;;' >"+(ChPercentageMarketBreakage1000==0?"&nbsp;":PlusMBMTD+Math.round(ChPercentageMarketBreakage1000)+"%")+"</td>";
	html += "<td style='height: 18px; text-align: center;font-size:12px; border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(YTDBreakageAllowanceRawCase10002014==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDBreakageAllowanceRawCase10002014))+"</td>";
	html += "<td style='height: 18px; text-align: center;font-size:12px; border:1px solid #EBEBEB'>"+(YTDBreakageAllowanceRawCase10002015==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDBreakageAllowanceRawCase10002015))+"</td>";
	html += "<td style='background-color: "+YColorMB+"; height: 18px; font-size:12px;border:1px solid #EBEBEB; text-align: center;' >"+(YChPercentageMarketBreakage1000==0?"&nbsp;":PlusMBYTD+Math.round(YChPercentageMarketBreakage1000)+"%")+"</td>";
	html += "</tr>";
	
	
	 PlusMZMTD="";
	 PlusMZYTD="";
	double ChPercentageMarketZabt1000=0;
	if(MTDZabtAllowanceRawCase10002014!=0){
		ChPercentageMarketZabt1000 = ((MTDZabtAllowanceRawCase1000 - MTDZabtAllowanceRawCase10002014)/Utilities.parseDouble(MTDZabtAllowanceRawCase10002014+"")*100);
	}
	if(ChPercentageMarketZabt1000>0){PlusMZMTD="+";}
	
	
	
	double YChPercentageMarketZabt1000=0;
	if(YTDZabtAllowanceRawCase10002014!=0){
		YChPercentageMarketZabt1000 = ((YTDZabtAllowanceRawCase10002015 - YTDZabtAllowanceRawCase10002014)/Utilities.parseDouble(YTDZabtAllowanceRawCase10002014+"")*100);
	}
	if(YChPercentageMarketZabt1000>0){PlusMZYTD="+";}
						
	//System.out.println(ChPercentageWashBreakage1000);
	
	 MColorMZ ="";
	 YColorMZ="";
	if(ChPercentageMarketZabt1000<0){ //Green
		MColorMZ="#D7F7DB";
	}else if(ChPercentageMarketZabt1000>0){ //Red
		MColorMZ="#FAE3EC";
	}
	
	if(YChPercentageMarketZabt1000<0){ //Green
		YColorMZ="#D7F7DB";
	}else if(YChPercentageMarketZabt1000>0){ //Red
		YColorMZ="#FAE3EC";
	}
	
	
	html += "<tr >";
	html += "<td valign='top' style='text-align:left; font-size:12px; border:1px solid #EBEBEB'><b>ZABT Allowance</b><br/>Empty given to distributor againts Breakage</td>";					
	//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsWashBreakage1000[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsWashBreakage1000[0]))+"</td>";
	html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+ (MTDZabtAllowanceRawCase10002014==0? "&nbsp;" : Utilities.getDisplayCurrencyFormat(MTDZabtAllowanceRawCase10002014)) +"</td>";
	html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB'>"+(MTDZabtAllowanceRawCase1000==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDZabtAllowanceRawCase1000))+"</td>";
	
	html += "<td style='background-color: "+MColorMZ+"; border:1px solid #EBEBEB; height: 18px; font-size:12px;text-align: center;;' >"+(ChPercentageMarketZabt1000==0?"&nbsp;":PlusMZMTD+Math.round(ChPercentageMarketZabt1000)+"%")+"</td>";
	html += "<td style='height: 18px; text-align: center;font-size:12px; border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(YTDZabtAllowanceRawCase10002014==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDZabtAllowanceRawCase10002014))+"</td>";
	html += "<td style='height: 18px; text-align: center;font-size:12px; border:1px solid #EBEBEB'>"+(YTDZabtAllowanceRawCase10002015==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDZabtAllowanceRawCase10002015))+"</td>";
	html += "<td style='background-color: "+YColorMZ+"; height: 18px; font-size:12px;border:1px solid #EBEBEB; text-align: center;' >"+(YChPercentageMarketZabt1000==0?"&nbsp;":PlusMZYTD+Math.round(YChPercentageMarketZabt1000)+"%")+"</td>";
html += "</tr>";
	
	
 


//////////////////////////////////////////////////////////////


MColorSC="#EDEFF2";
YColorSC="#EDEFF2";
PlusSCYTD="";
PlusSCMTD="";


if(ChPercentageSupplyChain1000<0){ //Green
	MColorSC="#87D692";
}else if(ChPercentageSupplyChain1000>0){ //Red
	MColorSC="#DE87A6";
	PlusSCMTD="+";
}

if(YChPercentageSupplyChain1000<0){ //Green
	YColorSC="#87D692";
}else if(YChPercentageSupplyChain1000>0){ //Red
	YColorSC="#DE87A6";
	PlusSCYTD="+";
}



html += "<tr>";
	html += "<td style='background-color: #EDEFF2; height: 30px; font-size:12px;text-align: left; font-weight: bold;'>Supply Chain Loss</td>";
	//html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;' >"+(TotalSupplyChainLoss1000==0?"":Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalSupplyChainLoss1000))+"</td>";
	html += "<td style='background-color: #EDEFF2; height: 30px; font-size:12px;text-align: center; font-weight: bold;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;' >"+(MTDTotalSupplyChainLoss10002014==0?"&nbsp;":Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDTotalSupplyChainLoss10002014))+"</td>";
	html += "<td style='background-color: #EDEFF2; height: 30px; font-size:12px;text-align: center; font-weight: bold;' >"+(MTDTotalSupplyChainLoss1000==0?"&nbsp;":Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDTotalSupplyChainLoss1000))+"</td>";
	html += "<td style='background-color: "+MColorSC+"; height: 30px; font-size:12px;text-align: center;font-weight: bold;' >"+(ChPercentageSupplyChain1000==0?"&nbsp;":PlusSCMTD+Math.round(ChPercentageSupplyChain1000)+"%")+"</td>";
	html += "<td style='background-color: #EDEFF2; height: 30px; font-size:12px;text-align: center; font-weight: bold;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;' >"+(YTDTotalSupplyChainLoss10002014==0?"&nbsp;":Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDTotalSupplyChainLoss10002014))+"</td>";
	html += "<td style='background-color: #EDEFF2; height: 30px; font-size:12px;text-align: center; font-weight: bold;' >"+(YTDTotalSupplyChainLoss10002015==0?"&nbsp;":Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDTotalSupplyChainLoss10002015))+"</td>";
	html += "<td style='background-color: "+YColorSC+"; height: 30px; font-size:12px;text-align: center;font-weight: bold;' >"+(YChPercentageSupplyChain1000==0?"&nbsp;":PlusSCYTD+Math.round(YChPercentageSupplyChain1000)+"%")+"</td>";
	
	html += "</tr>";

	
	
	PlusWBMTD="";
	PlusWBYTD="";
	
	double ChPercentageWashBreakage1000=0;
	if(MTDRawCasesUnitsWashBreakage10002014[0]!=0){
		 ChPercentageWashBreakage1000 = ((MTDRawCasesUnitsWashBreakage1000[0] - MTDRawCasesUnitsWashBreakage10002014[0])/Utilities.parseDouble(MTDRawCasesUnitsWashBreakage10002014[0]+"")*100);
	}
	if(ChPercentageWashBreakage1000>0){PlusWBMTD="+";}
	
	
	
	double YChPercentageWashBreakage1000=0;
	if(YTDRawCasesUnitsWashBreakage10002014[0]!=0){
		 YChPercentageWashBreakage1000 = ((YTDRawCasesUnitsWashBreakage10002015[0] - YTDRawCasesUnitsWashBreakage10002014[0])/Utilities.parseDouble(YTDRawCasesUnitsWashBreakage10002014[0]+"")*100);
	}
	if(YChPercentageWashBreakage1000>0){PlusWBYTD="+";}
						
	//System.out.println(ChPercentageWashBreakage250);
	
	
	MColorWB="";
	YColorWB="";
	
	if(ChPercentageWashBreakage1000<0){ //Green
		MColorWB="#D7F7DB";
	}else if(ChPercentageWashBreakage1000>0){ //Red
		MColorWB="#FAE3EC";
	}
	
	if(YChPercentageWashBreakage1000<0){ //Green
		YColorWB="#D7F7DB";
	}else if(YChPercentageWashBreakage1000>0){ //Red
		YColorWB="#FAE3EC";
	}
	
	
html += "<tr >";
	html += "<td valign='top' style='text-align:left; font-size:12px; border:1px solid #EBEBEB'><b>Wash Breakage</b><br/>During sorting and washing</td>";					
	//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsWashBreakage250[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsWashBreakage250[0]))+"</td>";
	html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+ (MTDRawCasesUnitsWashBreakage10002014[0]==0? "&nbsp;" : Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsWashBreakage10002014[0])) +"</td>";
	html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB'>"+(MTDRawCasesUnitsWashBreakage1000[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsWashBreakage1000[0]))+"</td>";
	
	html += "<td style='background-color: "+MColorWB+"; border:1px solid #EBEBEB; height: 18px; font-size:12px;text-align: center;;' >"+(ChPercentageWashBreakage1000==0?"&nbsp;":PlusWBMTD+Math.round(ChPercentageWashBreakage1000)+"%")+"</td>";
	html += "<td style='height: 18px; text-align: center;font-size:12px; border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(YTDRawCasesUnitsWashBreakage10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsWashBreakage10002014[0]))+"</td>";
	html += "<td style='height: 18px; text-align: center;font-size:12px; border:1px solid #EBEBEB'>"+(YTDRawCasesUnitsWashBreakage10002015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsWashBreakage10002015[0]))+"</td>";
	html += "<td style='background-color: "+YColorWB+"; height: 18px; font-size:12px;border:1px solid #EBEBEB; text-align: center;' >"+(YChPercentageWashBreakage1000==0?"&nbsp;":PlusWBYTD+Math.round(YChPercentageWashBreakage1000)+"%")+"</td>";
html += "</tr>";

double ChPercentageSpecailZabt1000=0;
if(MTDRawCasesUnitsSpecialZabt10002014[0]!=0){
	ChPercentageSpecailZabt1000 = ((MTDRawCasesUnitsSpecialZabt1000[0] - MTDRawCasesUnitsSpecialZabt10002014[0])/Utilities.parseDouble(MTDRawCasesUnitsSpecialZabt10002014[0]+"")*100);
}

double YChPercentageSpecailZabt1000=0;
if(YTDRawCasesUnitsSpecialZabt10002014[0]!=0){
	YChPercentageSpecailZabt1000 = ((YTDRawCasesUnitsSpecialZabt10002015[0] - YTDRawCasesUnitsSpecialZabt10002014[0])/Utilities.parseDouble(YTDRawCasesUnitsSpecialZabt10002014[0]+"")*100);
}

MColorSZ="";
YColorWB="";
PlusSZYTD="";
PlusSZMTD="";

if(ChPercentageSpecailZabt1000<0){ //Green
	MColorSZ="#D7F7DB";
}else if(ChPercentageSpecailZabt1000>0){ //Red
	MColorSZ="#FAE3EC";
	PlusSZMTD="+";
}

if(YChPercentageSpecailZabt1000<0){ //Green
	YColorWB="#D7F7DB";
}else if(YChPercentageSpecailZabt1000>0){ //Red
	YColorWB="#FAE3EC";
	PlusSZYTD="+";
}



//System.out.println(ChPercentageSpecailZabt250);

html += "<tr>";
	html += "<td valign='top' style='text-align:left;border:1px solid #EBEBEB; font-size:12px;'><b>Special Zabt</b><br/>Breakage received at DPG4 from warehouses and direct route vehicle</td>";
	//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsSpecialZabt1000[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsSpecialZabt1000[0]))+"</td>";
	html += "<td style='text-align:center;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;font-size:12px;'>"+(MTDRawCasesUnitsSpecialZabt10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsSpecialZabt10002014[0]))+"</td>";
	html += "<td style='text-align:center;border:1px solid #EBEBEB;font-size:12px;'>"+(MTDRawCasesUnitsSpecialZabt1000[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsSpecialZabt1000[0]))+"</td>";
	html += "<td style='background-color: "+MColorSZ+"; border:1px solid #EBEBEB;height: 18px;font-size:12px; text-align: center;' >"+(ChPercentageSpecailZabt1000==0?"&nbsp;":PlusSZMTD+Math.round(ChPercentageSpecailZabt1000)+"%")+"</td>";
	html += "<td style='height: 18px; border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;text-align: center;font-size:12px; '>"+(YTDRawCasesUnitsSpecialZabt10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsSpecialZabt10002014[0]))+"</td>";
	html += "<td style='height: 18px; border:1px solid #EBEBEB;text-align: center;font-size:12px; '>"+(YTDRawCasesUnitsSpecialZabt10002015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsSpecialZabt10002015[0]))+"</td>";
	html += "<td style='background-color: "+YColorWB+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(YChPercentageSpecailZabt1000==0?"&nbsp;":PlusSZYTD+Math.round(YChPercentageSpecailZabt1000)+"%")+"</td>";
html += "</tr>";
	
	 double ChPercentageSLC1000=0;
		if(MTDRawCasesUnitsSLCBR10002014[0]!=0){
			ChPercentageSLC1000 = ((MTDRawCasesUnitsSLCBR1000[0] - MTDRawCasesUnitsSLCBR10002014[0])/Utilities.parseDouble(MTDRawCasesUnitsSLCBR10002014[0]+"")*100);
		}
		
		double YChPercentageSLC1000=0;
		if(YTDRawCasesUnitsSLCBR10002014[0]!=0){
			YChPercentageSLC1000 = ((YTDRawCasesUnitsSLCBR10002015[0] - YTDRawCasesUnitsSLCBR10002014[0])/Utilities.parseDouble(YTDRawCasesUnitsSLCBR10002014[0]+"")*100);
		}
	
		
		MColorSLB="";
		YColorSLB="";
		PlusSLBMTD="";
		PlusSLBYTD="";
		
		if(ChPercentageSLC1000<0){ //Green
			MColorSLB="#D7F7DB";
		}else if(ChPercentageSLC1000>0){ //Red
			MColorSLB="#FAE3EC";
			PlusSLBMTD="+";
		}
		
		if(YChPercentageSLC1000<0){ //Green
			YColorSLB="#D7F7DB";
		}else if(YChPercentageSLC1000>0){ //Red
			YColorSLB="#FAE3EC";
			PlusSLBYTD="+";
		}
		
html += "<tr>";
	html += "<td valign='top' style='text-align:left;border:1px solid #EBEBEB;font-size:12px;'><b>S.L.C BR</b> <br/>Breakage during unloading</td>";
	//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsSLCBR1000[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsSLCBR1000[0]))+"</td>";
	html += "<td style='text-align:center;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;font-size:12px;'>"+(MTDRawCasesUnitsSLCBR10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsSLCBR10002014[0]))+"</td>";
	html += "<td style='text-align:center;border:1px solid #EBEBEB;font-size:12px;'>"+(MTDRawCasesUnitsSLCBR1000[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsSLCBR1000[0]))+"</td>";
	html += "<td style='background-color: "+MColorSLB+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(ChPercentageSLC1000==0?"&nbsp;":PlusSLBMTD+Math.round(ChPercentageSLC1000)+"%")+"</td>";
	html += "<td style='text-align:center;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;font-size:12px;'>"+(YTDRawCasesUnitsSLCBR10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsSLCBR10002014[0]))+"</td>";
	html += "<td style='text-align:center;border:1px solid #EBEBEB;font-size:12px;'>"+(YTDRawCasesUnitsSLCBR10002015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsSLCBR10002015[0]))+"</td>";
	html += "<td style='background-color: "+YColorSLB+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(YChPercentageSLC1000==0?"&nbsp;":PlusSLBYTD+Math.round(YChPercentageSLC1000)+"%")+"</td>";
html += "</tr>";


double ChPercentageSpBreakage1000=0;
if(MTDRawCasesUnitsSpecialBreakage10002014[0]!=0){
	ChPercentageSpBreakage1000 = ((MTDRawCasesUnitsSpecialBreakage1000[0] - MTDRawCasesUnitsSpecialBreakage10002014[0])/Utilities.parseDouble(MTDRawCasesUnitsSpecialBreakage10002014[0]+"")*100);
}

double YChPercentageSpBreakage1000=0;
if(YTDRawCasesUnitsSpecialBreakage10002014[0]!=0){
	YChPercentageSpBreakage1000 = ((YTDRawCasesUnitsSpecialBreakage10002015[0] - YTDRawCasesUnitsSpecialBreakage10002014[0])/Utilities.parseDouble(YTDRawCasesUnitsSpecialBreakage10002014[0]+"")*100);
}


MColorSpB="";
YColorSpB="";
PlusSpBMTD="";
PlusSpBYTD="";

if(ChPercentageSpBreakage1000<0){ //Green
	MColorSpB="#D7F7DB";
}else if(ChPercentageSpBreakage1000>0){ //Red
	MColorSpB="#FAE3EC";
	PlusSpBMTD="+";
}

if(YChPercentageSpBreakage1000<0){ //Green
	YColorSpB="#D7F7DB";
}else if(YChPercentageSpBreakage1000>0){ //Red
	YColorSpB="#FAE3EC";
	PlusSpBYTD="+";
}



html += "<tr>";
	html += "<td valign='top' style='text-align:left;border:1px solid #EBEBEB;font-size:12px;'><b>Special Breakage</b><br/>Transit between factory and depot</td>";
	//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsSpecialBreakage1000[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsSpecialBreakage1000[0]))+"</td>";
	html += "<td style='text-align:center;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;font-size:12px;'>"+(MTDRawCasesUnitsSpecialBreakage10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsSpecialBreakage10002014[0]))+"</td>";
	html += "<td style='text-align:center;border:1px solid #EBEBEB;font-size:12px;'>"+(MTDRawCasesUnitsSpecialBreakage1000[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsSpecialBreakage1000[0]))+"</td>";
	html += "<td style='background-color: "+MColorSpB+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(ChPercentageSpBreakage1000==0?"&nbsp;":PlusSpBMTD+Math.round(ChPercentageSpBreakage1000)+"%")+"</td>";
	html += "<td style='text-align:center;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;font-size:12px;'>"+(YTDRawCasesUnitsSpecialBreakage10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsSpecialBreakage10002014[0]))+"</td>";
	html += "<td style='text-align:center;border:1px solid #EBEBEB;font-size:12px;'>"+(YTDRawCasesUnitsSpecialBreakage10002015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsSpecialBreakage10002015[0]))+"</td>";
	html += "<td style='background-color: "+YColorSpB+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(YChPercentageSpBreakage1000==0?"&nbsp;":PlusSpBYTD+Math.round(YChPercentageSpBreakage1000)+"%")+"</td>";
html += "</tr>";

double ChPercentageSpBurst1000=0;
if(MTDRawCasesUnitsSpecialBurst10002014[0]!=0){
	ChPercentageSpBurst1000 = ((MTDRawCasesUnitsSpecialBurst1000[0] - MTDRawCasesUnitsSpecialBurst10002014[0])/Utilities.parseDouble(MTDRawCasesUnitsSpecialBurst10002014[0]+"")*100);
}

double YChPercentageSpBurst1000=0;
if(YTDRawCasesUnitsSpecialBurst10002014[0]!=0){
	YChPercentageSpBurst1000 = ((YTDRawCasesUnitsSpecialBurst10002015[0] - YTDRawCasesUnitsSpecialBurst10002014[0])/Utilities.parseDouble(YTDRawCasesUnitsSpecialBurst10002014[0]+"")*100);
}


MColorSpBu="";
YColorSpBu="";
PlusSpBuMTD="";
PlusSpBuYTD="";

if(ChPercentageSpBurst1000<0){ //Green
	MColorSpBu="#D7F7DB";
}else if(ChPercentageSpBurst1000>0){ //Red
	MColorSpBu="#FAE3EC";
	PlusSpBuMTD="+";
}

if(YChPercentageSpBurst1000<0){ //Green
	YColorSpBu="#D7F7DB";
}else if(YChPercentageSpBurst1000>0){ //Red
	YColorSpBu="#FAE3EC";
	PlusSpBuYTD="+";
}

html += "<tr>";
html += "<td valign='top' style='text-align:left;border:1px solid #EBEBEB;font-size:12px;'><b>Special Burst</b><br/>Liquid burst in warehouses</td>";
//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsSpecialBurst1000[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsSpecialBurst1000[0]))+"</td>";
html += "<td style='text-align:center;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;font-size:12px;'>"+(MTDRawCasesUnitsSpecialBurst10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsSpecialBurst10002014[0]))+"</td>";

html += "<td style='text-align:center;border:1px solid #EBEBEB;font-size:12px;'>"+(MTDRawCasesUnitsSpecialBurst1000[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsSpecialBurst1000[0]))+"</td>";
html += "<td style='background-color: "+MColorSpBu+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(ChPercentageSpBurst1000==0?"&nbsp;":PlusSpBuMTD+Math.round(ChPercentageSpBurst1000)+"%")+"</td>";
html += "<td style='text-align:center;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;font-size:12px;'>"+(YTDRawCasesUnitsSpecialBurst10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsSpecialBurst10002014[0]))+"</td>";
html += "<td style='text-align:center;border:1px solid #EBEBEB;font-size:12px;'>"+(YTDRawCasesUnitsSpecialBurst10002015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsSpecialBurst10002015[0]))+"</td>";
html += "<td style='background-color: "+YColorSpBu+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(YChPercentageSpBurst1000==0?"&nbsp;":PlusSpBuYTD+Math.round(YChPercentageSpBurst1000)+"%")+"</td>";
html += "</tr>";


double ChPercentageDepotBurst1000=0;
if(MTDRawCasesUnitsDepotBurst10002014[0]!=0){
ChPercentageDepotBurst1000 = ((MTDRawCasesUnitsDepotBurst1000[0] - MTDRawCasesUnitsDepotBurst10002014[0])/Utilities.parseDouble(MTDRawCasesUnitsDepotBurst10002014[0]+"")*100);
}


double YChPercentageDepotBurst1000=0;
if(YTDRawCasesUnitsDepotBurst10002014[0]!=0){
YChPercentageDepotBurst1000 = ((YTDRawCasesUnitsDepotBurst10002015[0] - YTDRawCasesUnitsDepotBurst10002014[0])/Utilities.parseDouble(YTDRawCasesUnitsDepotBurst10002014[0]+"")*100);
}


MColorDBu="";
YColorDBu="";
PlusDBuMTD="";
PlusDBuYTD="";

if(ChPercentageDepotBurst1000<0){ //Green
MColorDBu="#D7F7DB";
}else if(ChPercentageDepotBurst1000>0){ //Red
MColorDBu="#FAE3EC";
PlusDBuMTD="+";
}

if(YChPercentageDepotBurst1000<0){ //Green
YColorDBu="#D7F7DB";
}else if(YChPercentageDepotBurst1000>0){ //Red
YColorDBu="#FAE3EC";
PlusDBuYTD="+";
}

html += "<tr>";
html += "<td valign='top' style='text-align:left;border:1px solid #EBEBEB;font-size:12px;'><b>Depot Burst</b><br/>Liquid burst during Loading at DPG 4</td>";
//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsDepotBurst1000[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsDepotBurst1000[0]))+"</td>";
html += "<td style='text-align:center;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;font-size:12px;'>"+(MTDRawCasesUnitsDepotBurst10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsDepotBurst10002014[0]))+"</td>";
html += "<td style='text-align:center;border:1px solid #EBEBEB;font-size:12px;'>"+(MTDRawCasesUnitsDepotBurst1000[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsDepotBurst1000[0]))+"</td>";
html += "<td style='background-color: "+MColorDBu+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(ChPercentageDepotBurst1000==0?"&nbsp;":PlusDBuMTD+Math.round(ChPercentageDepotBurst1000)+"%")+"</td>";
html += "<td style='text-align:center;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;font-size:12px;'>"+(YTDRawCasesUnitsDepotBurst10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsDepotBurst10002014[0]))+"</td>";
html += "<td style='text-align:center;border:1px solid #EBEBEB;font-size:12px;'>"+(YTDRawCasesUnitsDepotBurst10002015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsDepotBurst10002015[0]))+"</td>";
html += "<td style='background-color: "+YColorDBu+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(YChPercentageDepotBurst1000==0?"&nbsp;":PlusDBuYTD+Math.round(YChPercentageDepotBurst1000)+"%")+"</td>";
html += "</tr>";


double ChPercentageDepotFallenBurst1000=0;
if(MTDRawCasesUnitsDepotFallenBurst10002014[0]!=0){
ChPercentageDepotFallenBurst1000 = ((MTDRawCasesUnitsDepotFallenBurst1000[0] - MTDRawCasesUnitsDepotFallenBurst10002014[0])/Utilities.parseDouble(MTDRawCasesUnitsDepotFallenBurst10002014[0]+"")*100);
}

double YChPercentageDepotFallenBurst1000=0;
if(YTDRawCasesUnitsDepotFallenBurst10002014[0]!=0){
YChPercentageDepotFallenBurst1000 = ((YTDRawCasesUnitsDepotFallenBurst10002015[0] - YTDRawCasesUnitsDepotFallenBurst10002014[0])/Utilities.parseDouble(YTDRawCasesUnitsDepotFallenBurst10002014[0]+"")*100);
}

MColorDF="";
YColorDF="";
PlusDFMTD="";
PlusDFYTD="";


if(ChPercentageDepotFallenBurst1000<0){ //Green
MColorDF="#D7F7DB";
}else if(ChPercentageDepotFallenBurst1000>0){ //Red
MColorDF="#FAE3EC";
PlusDFMTD="+";
}

if(YChPercentageDepotFallenBurst1000<0){ //Green
YColorDF="#D7F7DB";
}else if(YChPercentageDepotFallenBurst1000>0){ //Red
YColorDF="#FAE3EC";
PlusDFYTD="+";
}

html += "<tr>";
html += "<td valign='top' style='text-align:left;border:1px solid #EBEBEB;font-size:12px;'><b>Depot Fallen Burst</b><br/>Liquid fallen and burst in DPG4</td>";
//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsDepotFallenBurst1000[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsDepotFallenBurst1000[0]))+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(MTDRawCasesUnitsDepotFallenBurst10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsDepotFallenBurst10002014[0]))+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(MTDRawCasesUnitsDepotFallenBurst1000[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsDepotFallenBurst1000[0]))+"</td>";
html += "<td style='background-color: "+MColorDF+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(ChPercentageDepotFallenBurst1000==0?"&nbsp;":PlusDFMTD+Math.round(ChPercentageDepotFallenBurst1000)+"%")+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(YTDRawCasesUnitsDepotFallenBurst10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsDepotFallenBurst10002014[0]))+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(YTDRawCasesUnitsDepotFallenBurst10002015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsDepotFallenBurst10002015[0]))+"</td>";
html += "<td style='bac1kground-color: "+MColorDF+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(YChPercentageDepotFallenBurst1000==0?"&nbsp;":PlusDFYTD+Math.round(YChPercentageDepotFallenBurst1000)+"%")+"</td>";
html += "</tr>";

double ChPercentageLifterBreakage1000=0;
if(MTDRawCasesUnitsLifterBreakage10002014[0]!=0){
ChPercentageLifterBreakage1000 = ((MTDRawCasesUnitsLifterBreakage1000[0] - MTDRawCasesUnitsLifterBreakage10002014[0])/Utilities.parseDouble(MTDRawCasesUnitsLifterBreakage10002014[0]+"")*100);
}

double YChPercentageLifterBreakage1000=0;
if(YTDRawCasesUnitsLifterBreakage10002014[0]!=0){
YChPercentageLifterBreakage1000 = ((YTDRawCasesUnitsLifterBreakage10002015[0] - YTDRawCasesUnitsLifterBreakage10002014[0])/Utilities.parseDouble(YTDRawCasesUnitsLifterBreakage10002014[0]+"")*100);
}

MColorLB="";
YColorLB="";
PlusLBMTD="";
PlusLBYTD="";

if(ChPercentageLifterBreakage1000<0){ //Green
MColorLB="#D7F7DB";
}else if(ChPercentageLifterBreakage1000>0){ //Red
MColorLB="#FAE3EC";
PlusLBMTD="+";
}

if(YChPercentageLifterBreakage1000<0){ //Green
YColorLB="#D7F7DB";
}else if(YChPercentageLifterBreakage1000>0){ //Red
YColorLB="#FAE3EC";
PlusLBYTD="+";
}


html += "<tr>";
html += "<td valign='top' style='text-align:left;border:1px solid #EBEBEB;font-size:12px;'><b>Lifter Breakage</b><br/>During lifter operations at DPG4</td>";
//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsLifterBreakage1000[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsLifterBreakage1000[0]))+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(MTDRawCasesUnitsLifterBreakage10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsLifterBreakage10002014[0]))+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(MTDRawCasesUnitsLifterBreakage1000[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsLifterBreakage1000[0]))+"</td>";
html += "<td style='background-color: "+MColorLB+";border:1px solid #EBEBEB; height: 18px; font-size:12px;text-align: center;' >"+(ChPercentageLifterBreakage1000==0?"&nbsp;":PlusLBMTD+Math.round(ChPercentageLifterBreakage1000)+"%")+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(YTDRawCasesUnitsLifterBreakage10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsLifterBreakage10002014[0]))+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(YTDRawCasesUnitsLifterBreakage10002015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsLifterBreakage10002015[0]))+"</td>";
html += "<td style='background-color: "+YColorLB+"; border:1px solid #EBEBEB;height: 18px;font-size:12px; text-align: center;' >"+(YChPercentageLifterBreakage1000==0?"&nbsp;":PlusLBYTD+Math.round(YChPercentageLifterBreakage1000)+"%")+"</td>";
html += "</tr>";

double ChPercentageLifterBurst1000=0;
if(MTDRawCasesUnitsLifterBurst10002014[0]!=0){
ChPercentageLifterBurst1000 = ((MTDRawCasesUnitsLifterBurst1000[0] - MTDRawCasesUnitsLifterBurst10002014[0])/Utilities.parseDouble(MTDRawCasesUnitsLifterBurst10002014[0]+"")*100);
}

double YChPercentageLifterBurst1000=0;
if(YTDRawCasesUnitsLifterBurst10002014[0]!=0){
YChPercentageLifterBurst1000 = ((YTDRawCasesUnitsLifterBurst10002015[0] - YTDRawCasesUnitsLifterBurst10002014[0])/Utilities.parseDouble(YTDRawCasesUnitsLifterBurst10002014[0]+"")*100);
}

MColorLBu="";
YColorLBu="";
PlusLBuMTD="";
PlusLBuYTD="";

if(ChPercentageLifterBurst1000<0){ //Green
MColorLBu="#D7F7DB";
}else if(ChPercentageLifterBurst1000>0){ //Red
MColorLBu="#FAE3EC";
PlusLBuMTD="+";
}

if(YChPercentageLifterBurst1000<0){ //Green
YColorLBu="#D7F7DB";
}else if(YChPercentageLifterBurst1000>0){ //Red
YColorLBu="#FAE3EC";
PlusLBuYTD="+";
}

html += "<tr>";
html += "<td valign='top' style='text-align:left;border:1px solid #EBEBEB;font-size:12px;'><b>Lifter Burst</b><br/>Liquid burst during lifter operations</td>";
//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsLifterBurst1000[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsLifterBurst1000[0]))+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(MTDRawCasesUnitsLifterBurst10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsLifterBurst10002014[0]))+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(MTDRawCasesUnitsLifterBurst1000[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsLifterBurst1000[0]))+"</td>";
html += "<td style='background-color: "+MColorLBu+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(ChPercentageLifterBurst1000==0?"&nbsp;":PlusLBuMTD+Math.round(ChPercentageLifterBurst1000)+"%")+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(YTDRawCasesUnitsLifterBurst10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsLifterBurst10002014[0]))+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(YTDRawCasesUnitsLifterBurst10002015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsLifterBurst10002015[0]))+"</td>";
html += "<td style='background-color: "+YColorLBu+";border:1px solid #EBEBEB; height: 18px;font-size:12px; text-align: center;' >"+(YChPercentageLifterBurst1000==0?"&nbsp;":PlusLBuYTD+Math.round(YChPercentageLifterBurst1000)+"%")+"</td>";
html += "</tr>";

double ChPercentageFallenBreakage1000=0;
if(MTDRawCasesUnitsFallenBreakage10002014[0]!=0){
ChPercentageFallenBreakage1000 = ((MTDRawCasesUnitsFallenBreakage1000[0] - MTDRawCasesUnitsFallenBreakage10002014[0])/Utilities.parseDouble(MTDRawCasesUnitsFallenBreakage10002014[0]+"")*100);
}

double YChPercentageFallenBreakage1000=0;
if(YTDRawCasesUnitsFallenBreakage10002014[0]!=0){
YChPercentageFallenBreakage1000 = ((YTDRawCasesUnitsFallenBreakage10002015[0] - YTDRawCasesUnitsFallenBreakage10002014[0])/Utilities.parseDouble(YTDRawCasesUnitsFallenBreakage10002014[0]+"")*100);
}

MColorLFB="";
YColorLFB="";
PlusLFBMTD="";
PlusLFBYTD="";

if(ChPercentageFallenBreakage1000<0){ //Green
MColorLFB="#D7F7DB";
}else if(ChPercentageFallenBreakage1000>0){ //Red
MColorLFB="#FAE3EC";
PlusLFBMTD="+";
}

if(YChPercentageFallenBreakage1000<0){ //Green
YColorLFB="#D7F7DB";
}else if(YChPercentageFallenBreakage1000>0){ //Red
YColorLFB="#FAE3EC";
PlusLFBYTD="+";
}

html += "<tr>";
html += "<td valign='top' style='text-align:left;font-size:12px;border:1px solid #EBEBEB;'><b>Fallen Breakage</b><br/> Empty fallen at DPG4 or factory</td>";
//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsFallenBreakage1000[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsFallenBreakage1000[0]))+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(MTDRawCasesUnitsFallenBreakage10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsFallenBreakage10002014[0]))+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(MTDRawCasesUnitsFallenBreakage1000[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsFallenBreakage1000[0]))+"</td>";
html += "<td style='background-color: "+MColorLFB+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(ChPercentageFallenBreakage1000==0?"&nbsp;":PlusLFBMTD+Math.round(ChPercentageFallenBreakage1000)+"%")+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(YTDRawCasesUnitsFallenBreakage10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsFallenBreakage10002014[0]))+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(YTDRawCasesUnitsFallenBreakage10002015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsFallenBreakage10002015[0]))+"</td>";
html += "<td style='background-color: "+YColorLFB+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(YChPercentageFallenBreakage1000==0?"&nbsp;":PlusLFBYTD+Math.round(YChPercentageFallenBreakage1000)+"%")+"</td>";
html += "</tr>";

double ChPercentageProductionBreakage1000=0;
if(MTDTotalUnitsLoss1RawCases10002014!=0){
ChPercentageProductionBreakage1000 = ((MTDTotalUnitsLoss1RawCases1000 - MTDTotalUnitsLoss1RawCases10002014)/Utilities.parseDouble(MTDTotalUnitsLoss1RawCases10002014+"")*100);
}

double YChPercentageProductionBreakage1000=0;
if(YTDTotalUnitsLoss1RawCases10002014!=0){
YChPercentageProductionBreakage1000 = ((YTDTotalUnitsLoss1RawCases10002015 - YTDTotalUnitsLoss1RawCases10002014)/Utilities.parseDouble(YTDTotalUnitsLoss1RawCases10002014+"")*100);
}

MColorLDP="";
PlusLDPYTD="";
PlusLDPMTD="";
PlusLDPYTD="";

if(ChPercentageProductionBreakage1000<0){ //Green
MColorLDP="#D7F7DB";
}else if(ChPercentageProductionBreakage1000>0){ //Red
MColorLDP="#FAE3EC";
PlusLDPMTD="+";
}

if(YChPercentageProductionBreakage1000<0){ //Green
YColorLDP="#D7F7DB";
}else if(YChPercentageProductionBreakage1000>0){ //Red
YColorLDP="#FAE3EC";
PlusLDPYTD="+";
}

MColorPB="";
YColorLDP="";
PlusPBMTD="";
PlusPBYTD="";

if(ChPercentageProductionBreakage11000<0){ //Green
MColorPB="#D7F7DB";
}else if(ChPercentageProductionBreakage11000>0){ //Red
MColorPB="#FAE3EC";
PlusPBMTD="+";
}

if(YChPercentageProductionBreakage11000<0){ //Green
YColorLDP="#D7F7DB";
}else if(YChPercentageProductionBreakage11000>0){ //Red
YColorLDP="#FAE3EC";
PlusPBYTD="+";
}


html += "<tr>";
html += "<td valign='top' style='text-align:left;font-size:12px;border:1px solid #EBEBEB;'><b>Production Breakage</b><br/>During production at conveyor area</td>";

//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsDuringPro1000[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsDuringPro1000[0]))+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(MTDRawCasesUnitsDuringPro10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsDuringPro10002014[0]))+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(MTDRawCasesUnitsDuringPro1000[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDRawCasesUnitsDuringPro1000[0]))+"</td>";
html += "<td style='background-color: "+MColorPB+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(ChPercentageProductionBreakage11000==0?"&nbsp;":PlusPBMTD+Math.round(ChPercentageProductionBreakage11000)+"%")+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(YTDRawCasesUnitsDuringPro10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsDuringPro10002014[0]))+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(YTDRawCasesUnitsDuringPro10002015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDRawCasesUnitsDuringPro10002015[0]))+"</td>";
html += "<td style='background-color: "+MColorPB+"; border:1px solid #EBEBEB;height: 18px; font-size:12px;text-align: center;' >"+(YChPercentageProductionBreakage11000==0?"&nbsp;":PlusPBYTD+Math.round(YChPercentageProductionBreakage11000)+"%")+"</td>";



html += "</tr>";



////////////////////////////////////////////////////////////////////////////////////////////////////////


double ChPercentageProductionLoss1000=0;
if(MTDRawCasesUnitsDuringPro10002014[0]!=0){
ChPercentageProductionLoss1000 = ((MTDRawCasesUnitsDuringPro1000[0] - MTDRawCasesUnitsDuringPro10002014[0])/Utilities.parseDouble(MTDRawCasesUnitsDuringPro10002014[0]+"")*100);
}


double YChPercentageProductionLoss1000=0;
if(YTDRawCasesUnitsDuringPro10002014[0]!=0){
YChPercentageProductionLoss1000 = ((YTDRawCasesUnitsDuringPro10002015[0] - YTDRawCasesUnitsDuringPro10002014[0])/Utilities.parseDouble(YTDRawCasesUnitsDuringPro10002014[0]+"")*100);
}

MColorPL="EDEFF2";
YColorPL="EDEFF2";
PlusPLMTD="";
PlusPLYTD="";

if(ChPercentageProductionLoss1000<0){ //Green
MColorPL="#87D692";
}else if(ChPercentageProductionLoss1000>0){ //Red
MColorPL="#DE87A6";
PlusPLMTD="+";
}

if(YChPercentageProductionLoss1000<0){ //Green
YColorPL="#87D692";
}else if(YChPercentageProductionLoss1000>0){ //Red
YColorPL="#DE87A6";
PlusPLYTD="+";
}



html += "<tr>";
html += "<td style='background-color: #EDEFF2; height: 30px; text-align: left; font-weight: bold;font-size:12px;' >Production Loss</td>";
//html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;' >"+(RawCasesUnitsDuringPro1000[0]==0?"":Utilities.getDisplayCurrencyFormatRoundedAccounting(RawCasesUnitsDuringPro1000[0]))+"</td>";
html += "<td style='background-color: #EDEFF2; height: 30px; text-align: center; font-size:12px;font-weight: bold;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;' >"+(MTDRawCasesUnitsDuringPro10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDTotalUnitsLoss1RawCases10002014))+"</td>";
html += "<td style='background-color: #EDEFF2; height: 30px; text-align: center; font-size:12px;font-weight: bold;' >"+(MTDRawCasesUnitsDuringPro1000[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDTotalUnitsLoss1RawCases1000))+"</td>";
html += "<td style='background-color: "+MColorPL+"; height: 30px; text-align: center;font-size:12px;font-weight: bold;' >"+(ChPercentageProductionLoss1000==0?"&nbsp;":PlusPLMTD+Math.round(ChPercentageProductionLoss1000)+"%")+"</td>";
html += "<td style='background-color: #EDEFF2; height: 30px; text-align: center; font-size:12px;font-weight: bold;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;' >"+(YTDRawCasesUnitsDuringPro10002014[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDTotalUnitsLoss1RawCases10002014))+"</td>";
html += "<td style='background-color: #EDEFF2; height: 30px; text-align: center; font-size:12px;font-weight: bold;' >"+(YTDRawCasesUnitsDuringPro10002015[0]==0?"&nbsp;":Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDTotalUnitsLoss1RawCases10002015))+"</td>";
html += "<td style='background-color: "+YColorPL+"; height: 30px; text-align: center;font-size:12px;font-weight: bold;' >"+(YChPercentageProductionLoss1000==0?"&nbsp;":PlusPLYTD+Math.round(YChPercentageProductionLoss1000)+"%")+"</td>";

html += "</tr>";




html += "<tr>";
html += "<td valign='top' style='text-align:left;font-size:12px;border:1px solid #EBEBEB;'><b>Production Breakage</b><br/>During production at filling area</td>";

//html += "<td style='text-align:left;font-size:12px;'>"+(TotalUnitsLoss1RawCases1000==0?"":Utilities.getDisplayCurrencyFormat(TotalUnitsLoss1RawCases1000))+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(MTDTotalUnitsLoss1RawCases10002014==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDTotalUnitsLoss1RawCases10002014))+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(MTDTotalUnitsLoss1RawCases1000==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(MTDTotalUnitsLoss1RawCases1000))+"</td>";
html += "<td style='background-color: "+MColorLDP+"; border:1px solid #EBEBEB;height: 18px;font-size:12px; text-align: center;' >"+(ChPercentageProductionBreakage1000==0?"&nbsp;":PlusLDPMTD+Math.round(ChPercentageProductionBreakage1000)+"%")+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB; border-left:3px solid #BAC4E8;'>"+(YTDTotalUnitsLoss1RawCases10002014==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDTotalUnitsLoss1RawCases10002014))+"</td>";
html += "<td style='text-align:center;font-size:12px;border:1px solid #EBEBEB;'>"+(YTDTotalUnitsLoss1RawCases10002015==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(YTDTotalUnitsLoss1RawCases10002015))+"</td>";
html += "<td style='background-color: "+YColorLDP+"; border:1px solid #EBEBEB;height: 18px;font-size:12px; text-align: center;' >"+(YChPercentageProductionBreakage1000==0?"&nbsp;":PlusLDPYTD+Math.round(YChPercentageProductionBreakage1000)+"%")+"</td>";


html += "</tr>";


///////////////////////////////////// Grand Total





long TotalLossMTD20141000=MTDTotalMarketLoss10002014+MTDTotalSupplyChainLoss10002014+MTDRawCasesUnitsDuringPro10002014[0];
long TotalLossMTD20151000=MTDTotalMarketLoss1000+MTDTotalSupplyChainLoss1000+MTDRawCasesUnitsDuringPro1000[0];
double TotalLossMTDChangePercent1000=((TotalLossMTD20151000-TotalLossMTD20141000)/Utilities.parseDouble(TotalLossMTD20141000+""))*100;

//System.out.println(TotalLossMTDChangePercent);

MTDTotalColor="#EDEFF2";
YTDTotalColor="#EDEFF2";
MTDTotalPlus="";
YTDTotalPlus="";

if(TotalLossMTDChangePercent>0){
MTDTotalColor="#DE87A6";
MTDTotalPlus="+";
}else{
MTDTotalColor = "#87D692";
}




long TotalLossYTD20141000=YTDTotalMarketLoss10002014+YTDTotalSupplyChainLoss10002014+YTDTotalUnitsLoss1RawCases10002014;
long TotalLossYTD20151000=YTDTotalMarketLoss10002015+YTDTotalSupplyChainLoss10002015+YTDTotalUnitsLoss1RawCases10002015;
double TotalLossYTDChangePercent1000=((TotalLossYTD20151000-TotalLossYTD20141000)/Utilities.parseDouble(TotalLossYTD20141000+""))*100;


if(TotalLossYTDChangePercent>0){
YTDTotalColor="#DE87A6";
YTDTotalPlus="+";
}else{
YTDTotalColor = "#87D692";
}


html += "<tr>";
html += "<td>&nbsp;</td>";
html += "<tr>";

html += "<tr>";
html += "<td style='background-color: #EDEFF2; height: 30px; text-align:left;font-size:12px; border:1px solid #BAC4E8;border-left:3px solid #BAC4E8;border-color:#BAC4E8;font-weight: bold;'><b>Total Loss</b></td>";
//html += "<td style='text-align:left;font-size:12px;'>"+(RawCasesUnitsDuringPro1000[0]==0?"":Utilities.getDisplayCurrencyFormat(RawCasesUnitsDuringPro1000[0]))+"</td>";
html += "<td style='text-align:center;background-color: #EDEFF2; height: 30px;font-size:12px;border:1px solid #BAC4E8; border-left:3px solid #BAC4E8;font-weight: bold;'>"+(TotalLossMTD20141000==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(TotalLossMTD20141000))+"</td>";
html += "<td style='text-align:center;background-color: #EDEFF2; height: 30px;font-size:12px;border:1px solid #BAC4E8;font-weight: bold;'>"+(TotalLossMTD20151000==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(TotalLossMTD20151000))+"</td>";
html += "<td style='background-color: "+MTDTotalColor+";  height: 30px;border:1px solid #BAC4E8;height: 18px; font-size:12px;text-align: center;font-weight: bold;' >"+(TotalLossMTDChangePercent==0?"&nbsp;":MTDTotalPlus+Math.round(TotalLossMTDChangePercent1000)+"%")+"</td>";
html += "<td style='text-align:center;background-color: #EDEFF2; height: 30px;font-size:12px;border:1px solid #BAC4E8; border-left:3px solid #BAC4E8;font-weight: bold;'>"+(TotalLossYTD20141000==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(TotalLossYTD20141000))+"</td>";
html += "<td style='text-align:center;background-color: #EDEFF2; height: 30px;font-size:12px;border:1px solid #BAC4E8;font-weight: bold;'>"+(TotalLossYTD20151000==0?"&nbsp;":Utilities.getDisplayCurrencyFormat(TotalLossYTD20151000))+"</td>";
html += "<td style='background-color: "+YTDTotalColor+";  height: 30x;border:1px solid #BAC4E8;height: 18px; font-size:12px;text-align: center;font-weight: bold;' >"+(TotalLossYTDChangePercent1000==0?"&nbsp;":YTDTotalPlus+Math.round(TotalLossYTDChangePercent1000)+"%")+"</td>";
html += "</tr>";



////////////////////////////////////////////////////////////////////////////////////
				
		
		html += "</body>";
		
		
		html += "</html>";
		
		this.HTMLMessage = html;
	}

	
}
