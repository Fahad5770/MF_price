package com.pbc.pushmail;

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
import com.pbc.util.Utilities;
import org.apache.commons.lang3.time.DateUtils;

public class DistributorScoreCardMonthlyPDF {

	//public static final String RESULT = Utilities.getEmailAttachmentsPath()+ "/CashInflowSummary.pdf";
	
	Font fonttableheader = FontFactory.getFont("Arial", 6, Font.NORMAL);
	Font fontheading = FontFactory.getFont("Arial", 6, Font.NORMAL);
	Font fontpbc = FontFactory.getFont("Arial", 8, Font.BOLD);
	Font fonttitle = FontFactory.getFont("Arial", 8, Font.BOLD);
	Font reportheading = FontFactory.getFont("Arial", 6, Font.BOLD);
	
	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	
	
	
	
	
	Date StartDate = Utilities.parseDateYYYYMMDDWithoutSeparator("20150301");
	Date EndDate = Utilities.parseDateYYYYMMDDWithoutSeparator("20150331");
	
	
	long SND_ID = 0;
	
	public static void main(String[] args) throws DocumentException, IOException{
		/*
		try {
			//new DistributorScoreCardPDF().createPdf(RESULT, this.SND_ID);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/
	}
	
	public DistributorScoreCardMonthlyPDF() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		ds.createConnection();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
	}
	
	public void createPdf(String filename, long SND_ID) throws DocumentException, IOException, SQLException {
		
		this.SND_ID = SND_ID;
		
	    Document document = new Document(PageSize.A3.rotate());
	    PdfWriter.getInstance(document, new FileOutputStream(filename));
	    
	    document.open();
	    
	    PdfPTable table = new PdfPTable(31);
	    
	    table.setWidthPercentage(100f);
	    table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
	    
	    reportheading.setColor(BaseColor.WHITE);
	    fonttitle.setColor(BaseColor.WHITE);
	    
	    PdfPCell pcell = new PdfPCell(new Phrase("Monthly Distributor Score Card", fonttitle));
        pcell.setBackgroundColor(BaseColor.BLUE);
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcell.setBorderColor(BaseColor.BLUE);
        pcell.setColspan(31);
        pcell.setMinimumHeight(22);
        table.addCell(pcell);
        
        table.addCell(getHeadingCellLevel2("",3));
        table.addCell(getHeadingCellLevel2("Productivity",5));
        table.addCell(getHeadingCellLevel2("Single Serve (% of Sales)",5));
        table.addCell(getHeadingCellLevel2("1500ML",1));
        table.addCell(getHeadingCellLevel2("Lifting (CC)",3));
        table.addCell(getHeadingCellLevel2("Monthly Target (CC)",4));
        table.addCell(getHeadingCellLevel2("Sales Pattern (%) of Sales",3));
        table.addCell(getHeadingCellLevel2("Zero Sales Outlets",3));
        table.addCell(getHeadingCellLevel2("Sales (CC)",3));
        table.addCell(getHeadingCellLevel2("Sales",1));
        
	    pcell = new PdfPCell(getHeadingCell("Distributor"));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(222, 222, 222));
        pcell.setColspan(3);
        table.addCell(pcell);
        
        
        
        table.addCell(getHeadingCell("Productive Calls"));
        table.addCell(getHeadingCell("Completed Calls"));
        table.addCell(getHeadingCell("Adjusted Calls"));
        table.addCell(getHeadingCell("Drop Calls"));
        table.addCell(getHeadingCell("Bill Productivity"));
        table.addCell(getHeadingCell("Standard"));
        table.addCell(getHeadingCell("Sting"));
        table.addCell(getHeadingCell("Slice"));
        table.addCell(getHeadingCell("Aquafina"));
        table.addCell(getHeadingCell("Total"));
        table.addCell(getHeadingCell("(Cases)"));
        table.addCell(getHeadingCell("CSD"));
        table.addCell(getHeadingCell("NCB"));
        table.addCell(getHeadingCell("Total"));
        table.addCell(getHeadingCell("Target"));
        table.addCell(getHeadingCell("Achieved"));
        table.addCell(getHeadingCell("Achieved (%)"));
        table.addCell(getHeadingCell("Days Left"));
        table.addCell(getHeadingCell("1st 10 days"));
        table.addCell(getHeadingCell("2nd 10 days"));
        table.addCell(getHeadingCell("Last 10 days"));
        table.addCell(getHeadingCell(">15 days"));
        table.addCell(getHeadingCell(">25 days"));
        table.addCell(getHeadingCell(">45 days"));
        
        table.addCell(getHeadingCell("CSD"));
        table.addCell(getHeadingCell("NCB"));
        table.addCell(getHeadingCell("Total"));
        table.addCell(getHeadingCell("Amount"));
        
        Paragraph header = new Paragraph("Period: "+Utilities.getDisplayDateFormat(StartDate)+" - "+Utilities.getDisplayDateFormat(EndDate),fontpbc);
        header.setAlignment(Element.ALIGN_RIGHT);
        
        Image img = Image.getInstance(String.format(Utilities.getImageResoucesPath()+"/"+"%s", "pepsi28.png"));
        img.scaleToFit(20, 20);
        //img.setTransparency(new int[]{ 0xF0, 0xFF });
        
        img.setAbsolutePosition(35, 788);
        
        document.add(img);
        Paragraph pbc = new Paragraph("          Punjab Beverages",fontpbc);
        document.add(pbc);
        
        
	    document.add(header);
	    document.add(Chunk.NEWLINE);
	    
	    populateData();
	    
		int TotalOutletsPJP = 0;
		int TotalOutletsOrdered = 0;
		int TotalConvertedCases = 0;
		long TotalConvertedCasesCSD = 0;
		long TotalConvertedCasesNCB = 0;
		double TotalOrderAmount = 0;
		
		
		long TotalConvertedCasesSSRB250 = 0;
		long TotalConvertedCasesSSRB240 = 0;
		long TotalConvertedCasesSSRBSlice = 0;
		long TotalConvertedCasesSSRBAF = 0;
		
		long TotalConvertedCasesSSRBTotal = 0;
		
		
		double TotalConvertedCasesFirst10 = 0;
		double TotalConvertedCasesSecond10 = 0;
		double TotalConvertedCasesThird10 = 0;	
		
		
		long TotalLiftingCSD = 0;
		long TotalLiftingNCB = 0;
		long TotalLiftingTotal = 0;
		
		long TotalTargetCC = 0;
		long TotalTargetAchievedCC = 0;
		
		long TotalUnplannedCalls = 0;
		long TotalCompletedCalls = 0;
		long TotalAdjustedCalls = 0;
		long TotalDropCalls = 0;
		
		long TotalCasesSold1500ML = 0;
		
		
		
		long TotalZeroSale10 = 0;
		long TotalZeroSale20 = 0;
		long TotalZeroSale30 = 0;
		
		ResultSet rs1 = s3.executeQuery("select * from temp_orderbooker_performance order by name");
		while(rs1.next()){	
			int OutletsPJP = rs1.getInt("outlets_in_pjp");
			int OutletsOrdered = rs1.getInt("outlets_ordered");
			double OrderAmount = rs1.getDouble("gross_revenue"); 
			double UnplannedCalls = rs1.getDouble("unplanned_calls");
			
			long CompletedCalls = Math.round(rs1.getDouble("completed_calls"));
			long AdjustedCalls = Math.round(rs1.getDouble("adjusted_calls"));
			
			
			TotalAdjustedCalls += AdjustedCalls;
			TotalCompletedCalls += CompletedCalls;
			
			long DropCalls = OutletsOrdered - CompletedCalls;
			TotalDropCalls += DropCalls;
			
			TotalOutletsPJP += OutletsPJP;
			TotalOutletsOrdered += OutletsOrdered;
			TotalOrderAmount += OrderAmount;
			
			long ConvertedCasesSSRB250 = rs1.getLong("ssrb_250");
			long ConvertedCasesSSRB240 = rs1.getLong("ssrb_240");
			long ConvertedCasesSSRBSlice = rs1.getLong("ssrb_slice");
			long ConvertedCasesSSRBAF = rs1.getLong("ssrb_aquafina");
			
			long ConvertedCasesSSRBTotal = rs1.getLong("ssrb_total");
			
			long CaseSold1500ML = Math.round(rs1.getDouble("cases_sold_1500ml"));
			TotalCasesSold1500ML += CaseSold1500ML; 
			
			
			TotalConvertedCasesSSRB250 += ConvertedCasesSSRB250;
			TotalConvertedCasesSSRB240 += ConvertedCasesSSRB240;
			TotalConvertedCasesSSRBSlice += ConvertedCasesSSRBSlice;
			TotalConvertedCasesSSRBAF += ConvertedCasesSSRBAF;
			
			
			TotalConvertedCasesSSRBTotal += ConvertedCasesSSRBTotal;
			
			int ConvertedCases = rs1.getInt("converted_cases_sold");
			double ConvertedCasesCSD = rs1.getInt("converted_cases_csd");
			double ConvertedCasesNCB = rs1.getInt("converted_cases_ncb");

			TotalConvertedCasesCSD += ConvertedCasesCSD;
			TotalConvertedCasesNCB += ConvertedCasesNCB;
			TotalConvertedCases += ConvertedCases;
			
			
			double LiftingCSD = rs1.getInt("lifting_csd");
			double LiftingNCB = rs1.getInt("lifting_ncb");
			double LiftingTotal = rs1.getInt("lifting_total");
			
			TotalLiftingCSD += LiftingCSD;
			TotalLiftingNCB += LiftingNCB;
			TotalLiftingTotal += LiftingTotal;
			
			
			double First10Percenrage = rs1.getDouble("first_10");
			double Second10Percenrage = rs1.getDouble("second_10");
			double Third10Percenrage = rs1.getDouble("third_10");
			
			double ZeroSales10 = rs1.getDouble("zero_sales_10");
			double ZeroSales20 = rs1.getDouble("zero_sales_20");
			double ZeroSales30 = rs1.getDouble("zero_sales_30");

			TotalZeroSale10 += ZeroSales10;
			TotalZeroSale20 += ZeroSales20;
			TotalZeroSale30 += ZeroSales30;
			
			TotalConvertedCasesFirst10 += rs1.getDouble("first_10_cc");
			TotalConvertedCasesSecond10 += rs1.getDouble("second_10_cc");
			TotalConvertedCasesThird10 += rs1.getDouble("third_10_cc");
			
			long TargetDaysPast = Math.round(rs1.getDouble("target_days_past"));
			long TargetDaysLeft = Math.round(rs1.getDouble("target_days_left"));
			long TargetCC = Math.round(rs1.getDouble("target_cc"));
			long TargetAchievedCC = Math.round(rs1.getDouble("target_achieved_cc"));
			
			double TargetPercentage = 0;
			if (TargetCC != 0){
				TargetPercentage = (((TargetAchievedCC * 1d) / (TargetCC * 1d))) * 100;
			}
			
			String TargetPercentageString = "";
			if (TargetPercentage != 0){
				TargetPercentageString = Utilities.getDisplayCurrencyFormatRounded(TargetPercentage)+"%";
			}
			
			TotalTargetCC += TargetCC;
			TotalTargetAchievedCC += TargetAchievedCC;
			
			
			boolean isTargetHighlighted = false;
			long TargetAchievedDailyAverage = 0;
			if (TargetDaysPast != 0){
				TargetAchievedDailyAverage = TargetAchievedCC / TargetDaysPast;
			}
			long TargetRequiredAverage = 0;
			if (TargetDaysLeft != 0){
				TargetRequiredAverage = (TargetCC - TargetAchievedCC) / TargetDaysLeft;
			}
			if (TargetAchievedDailyAverage < TargetRequiredAverage){
				
				isTargetHighlighted = true;
			}
			
			if (TargetDaysLeft == 0 && (TargetCC - TargetAchievedCC) > 0 ){
				isTargetHighlighted = true;
			}
			
			
			TotalUnplannedCalls += UnplannedCalls;
			
		    pcell = new PdfPCell(new Phrase(Utilities.truncateStringToMax(rs1.getString("name"),27), fontheading));
	        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
	        pcell.setColspan(3);
	        table.addCell(pcell);
	        
	        
			table.addCell(getNormalCellRightAlignedProductivity(Utilities.getDisplayCurrencyFormatRounded(OutletsOrdered)+""));
			table.addCell(getNormalCellRightAlignedProductivity(Utilities.getDisplayCurrencyFormatRounded(CompletedCalls)+""));
			table.addCell(getNormalCellRightAlignedProductivity(Utilities.getDisplayCurrencyFormatRounded(AdjustedCalls)));
			
			if (DropCalls != 0){
				table.addCell(getNormalCellRightAlignedProductivity(Utilities.getDisplayCurrencyFormatRounded(DropCalls)));
			}else{
				table.addCell(getNormalCellRightAlignedProductivity(""));
			}
			table.addCell(getNormalCellStrikeRate(Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("bill_productivity"))));
			
			double SSRBPercent = 0;
			if (ConvertedCases != 0){
				SSRBPercent = ((ConvertedCasesSSRBTotal * 1d) / (ConvertedCases * 1d)) * 100;
			}
			double SSRBPercent250 = 0;
			if (ConvertedCases != 0){
				SSRBPercent250 = ((ConvertedCasesSSRB250 * 1d) / (ConvertedCases * 1d)) * 100;
			}
			double SSRBPercent240 = 0;
			if (ConvertedCases != 0){
				SSRBPercent240 = ((ConvertedCasesSSRB240 * 1d) / (ConvertedCases * 1d)) * 100;
			}
			double SSRBPercentSlice = 0;
			if (ConvertedCases != 0){
				SSRBPercentSlice = ((ConvertedCasesSSRBSlice * 1d) / (ConvertedCases * 1d)) * 100;
			}
			double SSRBPercentAF = 0;
			if (ConvertedCases != 0){
				SSRBPercentAF = ((ConvertedCasesSSRBAF * 1d) / (ConvertedCases * 1d)) * 100;
			}
			
			table.addCell(getNormalCellRightAlignedSSRB(Utilities.getDisplayCurrencyFormatRounded(SSRBPercent250)+"%"));
			
			if (SSRBPercent240 != 0){
				table.addCell(getNormalCellRightAlignedSSRB(Utilities.getDisplayCurrencyFormatRounded(SSRBPercent240)+"%"));
			}else{
				table.addCell(getNormalCellRightAlignedSSRB(""));
			}
			
			if (SSRBPercentSlice != 0){
				table.addCell(getNormalCellRightAlignedSSRB(Utilities.getDisplayCurrencyFormatRounded(SSRBPercentSlice)+"%"));
			}else{
				table.addCell(getNormalCellRightAlignedSSRB(""));
			}
			if (SSRBPercentAF != 0){
				table.addCell(getNormalCellRightAlignedSSRB(Utilities.getDisplayCurrencyFormatRounded(SSRBPercentAF)+"%"));
			}else{
				table.addCell(getNormalCellRightAlignedSSRB(""));
			}
			
			table.addCell(getNormalCellRightAlignedSSRB(Utilities.getDisplayCurrencyFormatRounded(SSRBPercent)+"%"));
			table.addCell(getNormalCellRightAligned(Utilities.getDisplayCurrencyFormatOneDecimal(CaseSold1500ML)));
			
			table.addCell(getNormalCellRightAlignedLifting(Utilities.getDisplayCurrencyFormatOneDecimal(LiftingCSD)));
			table.addCell(getNormalCellRightAlignedLifting(Utilities.getDisplayCurrencyFormatOneDecimal(LiftingNCB)));
			table.addCell(getNormalCellRightAlignedLifting(Utilities.getDisplayCurrencyFormatOneDecimal(LiftingTotal)));
			
			
			if (TargetCC != 0){
				table.addCell(getNormalCellRightAlignedTarget(Utilities.getDisplayCurrencyFormatRounded(TargetCC)));
				table.addCell(getNormalCellRightAlignedTarget(Utilities.getDisplayCurrencyFormatRounded(TargetAchievedCC)));
				table.addCell(getNormalCellTargetAchieved(TargetPercentageString, isTargetHighlighted));
				table.addCell(getNormalCellRightAlignedTarget(Utilities.getDisplayCurrencyFormatRounded(TargetDaysLeft)));
			}else{
				table.addCell(getNormalCellRightAlignedTarget(""));
				table.addCell(getNormalCellRightAlignedTarget(""));
				table.addCell(getNormalCellRightAlignedTarget(""));
				table.addCell(getNormalCellRightAlignedTarget(""));
			}

			table.addCell(getNormalCellRightAlignedPattern(Utilities.getDisplayCurrencyFormatRounded(First10Percenrage)+"%"));
			table.addCell(getNormalCellRightAlignedPattern(Utilities.getDisplayCurrencyFormatRounded(Second10Percenrage)+"%"));
			table.addCell(getNormalCellRightAlignedPattern(Utilities.getDisplayCurrencyFormatRounded(Third10Percenrage)+"%"));
			
			table.addCell(getNormalCellRightAlignedZeroSales(Utilities.getDisplayCurrencyFormatRounded(ZeroSales10)));
			table.addCell(getNormalCellRightAlignedZeroSales(Utilities.getDisplayCurrencyFormatRounded(ZeroSales20)));
			table.addCell(getNormalCellRightAlignedZeroSales(Utilities.getDisplayCurrencyFormatRounded(ZeroSales30)));
			
			table.addCell(getNormalCellRightAlignedCC(Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesCSD)));
			table.addCell(getNormalCellRightAlignedCC(Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesNCB)));
			table.addCell(getNormalCellRightAlignedCC(Utilities.getDisplayCurrencyFormatRounded(ConvertedCases)));
			
			table.addCell(getNormalCellRightAligned(Utilities.getDisplayCurrencyFormatRounded(OrderAmount)));
		}
		
		
		
		double TotalTargetPercentage = 0;
		if (TotalTargetCC != 0){
			TotalTargetPercentage = (((TotalTargetAchievedCC * 1d) / (TotalTargetCC * 1d))) * 100;
		}
		
		
	    pcell = new PdfPCell(new Phrase("", fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setColspan(3);
        table.addCell(pcell);
	    
        table.addCell(getNormalCellRightAlignedProductivity(Utilities.getDisplayCurrencyFormatRounded(TotalOutletsOrdered)+""));
		table.addCell(getNormalCellRightAlignedProductivity(Utilities.getDisplayCurrencyFormatRounded(TotalCompletedCalls)+""));
		table.addCell(getNormalCellRightAlignedProductivity(Utilities.getDisplayCurrencyFormatRounded(TotalAdjustedCalls)+""));
		table.addCell(getNormalCellRightAlignedProductivity(Utilities.getDisplayCurrencyFormatRounded(TotalDropCalls)+""));
		
		double TotalProductivity = 0;
		if (TotalOutletsOrdered != 0){
			TotalProductivity = ((TotalCompletedCalls * 1d) / (TotalOutletsOrdered * 1d)) * 100d;
		}
		
		table.addCell(getNormalCellRightAlignedProductivity(Utilities.getDisplayCurrencyFormatRounded(TotalProductivity)+"%"));
		
		double PercentTotalConvertedCasesSSRB250 = 0;
		if (TotalConvertedCases != 0){
			PercentTotalConvertedCasesSSRB250 = ((TotalConvertedCasesSSRB250 * 1d) / (TotalConvertedCases * 1d)) * 100;
		}
		double PercentTotalConvertedCasesSSRB240 = 0;
		if (TotalConvertedCases != 0){
			PercentTotalConvertedCasesSSRB240 = ((TotalConvertedCasesSSRB240 * 1d) / (TotalConvertedCases * 1d)) * 100;
		}
		double PercentTotalConvertedCasesSSRBTotal = 0;
		if (TotalConvertedCases != 0){
			PercentTotalConvertedCasesSSRBTotal = ((TotalConvertedCasesSSRBTotal * 1d) / (TotalConvertedCases * 1d)) * 100;
		}
		double PercentTotalConvertedCasesSSRBSlice = 0;
		if (TotalConvertedCases != 0){
			PercentTotalConvertedCasesSSRBSlice = ((TotalConvertedCasesSSRBSlice * 1d) / (TotalConvertedCases * 1d)) * 100;
		}
		double PercentTotalConvertedCasesSSRBAF = 0;
		if (TotalConvertedCases != 0){
			PercentTotalConvertedCasesSSRBAF = ((TotalConvertedCasesSSRBAF * 1d) / (TotalConvertedCases * 1d)) * 100;
		}
		
		
		
		table.addCell(getNormalCellRightAlignedSSRB(Utilities.getDisplayCurrencyFormatOneDecimal(PercentTotalConvertedCasesSSRB250)+"%"));
		table.addCell(getNormalCellRightAlignedSSRB(Utilities.getDisplayCurrencyFormatOneDecimal(PercentTotalConvertedCasesSSRB240)+"%"));
		table.addCell(getNormalCellRightAlignedSSRB(Utilities.getDisplayCurrencyFormatOneDecimal(PercentTotalConvertedCasesSSRBSlice)+"%"));
		table.addCell(getNormalCellRightAlignedSSRB(Utilities.getDisplayCurrencyFormatOneDecimal(PercentTotalConvertedCasesSSRBAF)+"%"));
		table.addCell(getNormalCellRightAlignedSSRB(Utilities.getDisplayCurrencyFormatOneDecimal(PercentTotalConvertedCasesSSRBTotal)+"%"));

		table.addCell(getNormalCellRightAligned(Utilities.getDisplayCurrencyFormatRounded(TotalCasesSold1500ML)));
		
		table.addCell(getNormalCellRightAlignedLifting(Utilities.getDisplayCurrencyFormatRounded(TotalLiftingCSD)));
		table.addCell(getNormalCellRightAlignedLifting(Utilities.getDisplayCurrencyFormatRounded(TotalLiftingNCB)));
		table.addCell(getNormalCellRightAlignedLifting(Utilities.getDisplayCurrencyFormatRounded(TotalLiftingTotal)));
		
		table.addCell(getNormalCellRightAlignedTarget(Utilities.getDisplayCurrencyFormatRounded(TotalTargetCC)));
		table.addCell(getNormalCellRightAlignedTarget(Utilities.getDisplayCurrencyFormatRounded(TotalTargetAchievedCC)));
		table.addCell(getNormalCellRightAlignedTarget(Utilities.getDisplayCurrencyFormatRounded(TotalTargetPercentage)+"%"));
		table.addCell(getNormalCellRightAlignedTarget(""));
		
		
		double TotalPercentageFirst10 = 0;
		double TotalPercentageSecond10 = 0;
		double TotalPercentageThird10 = 0;
		
		if (TotalConvertedCases != 0){
			TotalPercentageFirst10 = (TotalConvertedCasesFirst10 / (TotalConvertedCases * 1d)) * 100;
			TotalPercentageSecond10 = (TotalConvertedCasesSecond10 / (TotalConvertedCases * 1d)) * 100;
			TotalPercentageThird10 = (TotalConvertedCasesThird10 / (TotalConvertedCases * 1d)) * 100;
		}
		
		table.addCell(getNormalCellRightAlignedPattern(Utilities.getDisplayCurrencyFormatRounded(TotalPercentageFirst10)+"%"));
		table.addCell(getNormalCellRightAlignedPattern(Utilities.getDisplayCurrencyFormatRounded(TotalPercentageSecond10)+"%"));
		table.addCell(getNormalCellRightAlignedPattern(Utilities.getDisplayCurrencyFormatRounded(TotalPercentageThird10)+"%"));
		
		
		table.addCell(getNormalCellRightAlignedZeroSales(Utilities.getDisplayCurrencyFormatRounded(TotalZeroSale10)));
		table.addCell(getNormalCellRightAlignedZeroSales(Utilities.getDisplayCurrencyFormatRounded(TotalZeroSale20)));
		table.addCell(getNormalCellRightAlignedZeroSales(Utilities.getDisplayCurrencyFormatRounded(TotalZeroSale30)));
		
		table.addCell(getNormalCellRightAlignedCC(Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesCSD)));
		table.addCell(getNormalCellRightAlignedCC(Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesNCB)));
		table.addCell(getNormalCellRightAlignedCC(Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCases)));
		
		
		
		table.addCell(getNormalCellRightAligned(Utilities.getDisplayCurrencyFormatRounded(TotalOrderAmount)));
	    
	    document.add(table);
	    //document.add(Chunk.NEWLINE);
	    //document.add(new Paragraph("* This report is under review and may embody further changes to its layout.",fontheading));
	    
	    document.add(Chunk.NEWLINE);
	    document.add(new Paragraph("Definitions:",fontheading));
	    document.add(new Paragraph("Productive Calls: Number of orders placed during the period",fontheading));
	    document.add(new Paragraph("Completed Calls: Number of orders converted to sales",fontheading));
	    document.add(new Paragraph("Adjusted Calls: Number of orders with partial sales return",fontheading));
	    document.add(new Paragraph("Drop Calls: Number of orders didn\'t convert into sales",fontheading));
	    document.add(Chunk.NEWLINE);
	    document.add(new Paragraph("Formulas:",fontheading));
	    document.add(new Paragraph("Bill Productivity = Completed Calls / Productive Calls",fontheading));
	    
	    document.close();
	    
	    s.executeUpdate("drop temporary table temp_orderbooker_performance");
	    
	}
	
	private PdfPCell getHeadingCell(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(222, 222, 222));
        return pcell;
	}
	private PdfPCell getNormalCell(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        return pcell;
        
	}
	private PdfPCell getNormalCellOB(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(255,255,217));
        return pcell;
	}

	private PdfPCell getNormalCellStrikeRate(String title){
		
		int StrikeRate = Utilities.parseInt(title);
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title+"%", fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        //pcell.setBackgroundColor(new BaseColor(246, 237, 255));
        
        if (StrikeRate < 80){
        	pcell.setBackgroundColor(BaseColor.RED);
        	pcell.setBorderColor(BaseColor.RED);
        }
        
        return pcell;
	}
	private PdfPCell getNormalCellDropSize(String title){
		
		double DropSize = Utilities.parseDouble(title);
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        
        
        if (DropSize < 4){
        	pcell.setBackgroundColor(BaseColor.RED);
        	pcell.setBorderColor(BaseColor.RED);
        }
        
        return pcell;
	}
	private PdfPCell getNormalCellSKU(String title){
		
		double SKU = Utilities.parseDouble(title);
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        
        if (SKU < 2){
        	pcell.setBackgroundColor(BaseColor.RED);
        	pcell.setBorderColor(BaseColor.RED);
        }
        
        return pcell;
	}
	private PdfPCell getNormalCellPackPerOrder(String title){
		
		double SKU = Utilities.parseDouble(title);
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        
        if (SKU < 1.5){
        	pcell.setBackgroundColor(BaseColor.RED);
        	pcell.setBorderColor(BaseColor.RED);
        }
        
        return pcell;
	}
	
	private PdfPCell getNormalCellRightAligned(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        return pcell;
	}
	private PdfPCell getNormalCellRightAlignedTarget(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(255, 245, 254));
        return pcell;
	}
	
	private PdfPCell getNormalCellTargetAchieved(String title, boolean isHighlighted){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(255, 245, 254));
        
        if (isHighlighted){
        	pcell.setBackgroundColor(BaseColor.RED);
        	pcell.setBorderColor(BaseColor.RED);
        }
        
        return pcell;
	}

	private PdfPCell getNormalCellRightAlignedSSRB(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(233, 255, 217));
        return pcell;
	}
	
	private PdfPCell getNormalCellRightAlignedCC(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(227, 230, 255));
        return pcell;
	}

	private PdfPCell getNormalCellRightAlignedZeroSales(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(222, 255, 252));
        return pcell;
	}

	private PdfPCell getNormalCellRightAlignedPattern(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(248, 255, 222));
        return pcell;
	}
	
	private PdfPCell getNormalCellRightAlignedLifting(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(240, 246, 255));
        return pcell;
	}

	private PdfPCell getNormalCellRightAlignedProductivity(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        //pcell.setBackgroundColor(new BaseColor(246, 237, 255));
        return pcell;
	}
	
	private PdfPCell getHeadingCellLevel2(String title, int colspan){	
	    PdfPCell pcell2 = new PdfPCell(new Phrase(title, reportheading));
	    pcell2.setBackgroundColor(BaseColor.DARK_GRAY);
	    pcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	    pcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    pcell2.setBorderColor(BaseColor.LIGHT_GRAY);
	    pcell2.setColspan(colspan);
	    pcell2.setMinimumHeight(20);
	    return pcell2;
	}
	private void populateData() throws SQLException {
		s.executeUpdate("SET SESSION group_concat_max_len = 1000000");
		
		s.executeUpdate("create temporary table temp_orderbooker_performance (name varchar(500), percent_before_10am double, percent_after_6pm double, avg_time_in varchar(20), avg_time_out varchar(20),outlets_in_pjp int(5), outlets_ordered int(5), converted_cases_sold int(10), gross_revenue double, eco double, bill_productivity double, sku_per_bill double, pack_size_per_bill double, drop_size double, orderbookers_worked double, unplanned_calls double, converted_cases_csd double, converted_cases_ncb double, sku_per_order_csd double, sku_per_order_ncb double, ssrb_250 double, ssrb_240 double, ssrb_total double, target_days_left double, target_cc double, target_achieved_cc double, target_days_past double, cr_assigned double, completed_calls double, adjusted_calls double, ssrb_slice double, ssrb_aquafina double, cases_sold_1500ml double, lifting_csd double, lifting_ncb double, lifting_total double, first_10 double, second_10 double, third_10 double, first_10_cc double, second_10_cc double, third_10_cc double, zero_sales_10 double, zero_sales_20 double, zero_sales_30 double)");
								
		ResultSet rs = s.executeQuery("select mo.distributor_id, (select name from common_distributors where distributor_id = mo.distributor_id) distributor_name, group_concat(mo.id) order_ids, count(mo.id) order_count, count(distinct mo.outlet_id) unique_outlets_ordered, count(distinct created_by) orderbookers_worked, group_concat(distinct mo.outlet_id) outlet_ids_ordered from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+this.SND_ID+")) and distributor_id not in (100839,100762,100814,100558) group by mo.distributor_id");
		
		while(rs.next()){
			long DistributorID = rs.getLong(1);
			String DistributorName = rs.getString(2);
			
			double OrderBookersWorked = rs.getDouble("orderbookers_worked");
			String OrderIDs = rs.getString("order_ids");
			String OutletIDsOrdered = rs.getString("outlet_ids_ordered");
			//String DaysOfWeekIDs = "1,2,3,4,5,7";//rs.getString("days_of_week");
			double OrderCount = rs.getInt("order_count"); 
			double UniqueOutletsOrdered = rs.getInt("unique_outlets_ordered");
			
			double PlannedOutletsCount = 0;
			String PlannedOutletIDs = "";
			
			int OrderBookersAssigned = 0;
			
			//if (DistributorID == 100782){
				//System.out.print("select count(distinct outlet_id), group_concat(distinct outlet_id), count(distinct assigned_to) cr_assigned from distributor_beat_plan_log force index (distributor_beat_plan_log_date_distributor_id_day_number) where log_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and distributor_id = "+DistributorID+" and day_number in ("+DaysOfWeekIDs+") and assigned_to is not null");
			//}
			/*
			ResultSet rs2 = s3.executeQuery("select count(distinct assigned_to) cr_assigned from distributor_beat_plan_log force index (distributor_beat_plan_log_date_distributor_id_day_number) where log_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and distributor_id = "+DistributorID+" and day_number in ("+DaysOfWeekIDs+") and assigned_to is not null");
			if (rs2.first()){
				//PlannedOutletsCount = rs2.getDouble(1);
				//PlannedOutletIDs = rs2.getString(2);
				OrderBookersAssigned = rs2.getInt("cr_assigned");
			}
			*/
			/*
			
			double OrdersFromPlannedOutlets = 0;
			ResultSet rs10 = s3.executeQuery("select count(id) from common_outlets where id in ("+PlannedOutletIDs+") and id in ("+OutletIDsOrdered+")");
			if (rs10.first()){
				OrdersFromPlannedOutlets = rs10.getDouble(1);
			}
			*/
			/*
			
			ResultSet rs3 = s3.executeQuery("select sum(((mop.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mop.is_promotion = 0 and mo.id in ("+OrderIDs+") ");
			if(rs3.first()){
				ConvertedCasesSold = rs3.getDouble(1);
			}
			
			*/
			long ConvertedCasesLiftedCSD = 0;
			ResultSet rs22 = s3.executeQuery("select sum(((idnp.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and ipv.type_id = 1 and idn.created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+ " and idn.distributor_id = "+DistributorID);
			if(rs22.first()){
				ConvertedCasesLiftedCSD = Math.round(rs22.getDouble(1));
			}
			
			long ConvertedCasesLiftedNCB = 0;
			ResultSet rs23 = s3.executeQuery("select sum(((idnp.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and ipv.type_id = 2 and idn.created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+ " and idn.distributor_id = "+DistributorID);
			if(rs23.first()){
				ConvertedCasesLiftedNCB = Math.round(rs23.getDouble(1));
			}
			long ConvertedCasesLiftedTotal = ConvertedCasesLiftedCSD + ConvertedCasesLiftedNCB;
			
			
			long ConvertedCasesSoldCSD = 0;
			ResultSet rs12 = s3.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where ipv.type_id = 1 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") ");
			if(rs12.first()){
				ConvertedCasesSoldCSD = Math.round(rs12.getDouble(1));
			}
			
			long ConvertedCasesSoldNCB = 0;
			ResultSet rs11 = s3.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where ipv.type_id = 2 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") ");
			if(rs11.first()){
				ConvertedCasesSoldNCB = Math.round(rs11.getDouble(1));
			}
			long ConvertedCasesSold = ConvertedCasesSoldCSD + ConvertedCasesSoldNCB;
			
			
			long ConvertedCasesSoldFirst10 = 0;
			ResultSet rs24 = s3.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") and isa.created_on between '2015-03-01' and '2015-03-11'");
			if(rs24.first()){
				ConvertedCasesSoldFirst10 = Math.round(rs24.getDouble(1));
			}
			long ConvertedCasesSoldSecond10 = 0;
			ResultSet rs25 = s3.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") and isa.created_on between '2015-03-11' and '2015-03-21'");
			if(rs25.first()){
				ConvertedCasesSoldSecond10 = Math.round(rs25.getDouble(1));
			}
			long ConvertedCasesSoldThird10 = 0;
			ResultSet rs26 = s3.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") and isa.created_on between '2015-03-21' and '2015-04-02'");
			if(rs26.first()){
				ConvertedCasesSoldThird10 = Math.round(rs26.getDouble(1));
			}
			
			double First10Percentage = 0;
			if (ConvertedCasesSold != 0){
				First10Percentage = ((ConvertedCasesSoldFirst10 * 1d) / (ConvertedCasesSold * 1d) ) * 100;
			}
			double Second10Percentage = 0;
			if (ConvertedCasesSold != 0){
				Second10Percentage = ((ConvertedCasesSoldSecond10 * 1d) / (ConvertedCasesSold * 1d) ) * 100;
			}
			double Third10Percentage = 0;
			if (ConvertedCasesSold != 0){
				Third10Percentage = ((ConvertedCasesSoldThird10 * 1d) / (ConvertedCasesSold * 1d) ) * 100;
			}
			
			
			long ConvertedCasesSoldSSRB250 = 0;
			ResultSet rs16 = s3.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where ipv.ssrb_type_id = 1 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") ");
			if(rs16.first()){
				ConvertedCasesSoldSSRB250 = Math.round(rs16.getDouble(1));
			}
			
			long ConvertedCasesSoldSSRB240 = 0;
			ResultSet rs17 = s3.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where ipv.ssrb_type_id = 2 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") ");
			if(rs17.first()){
				ConvertedCasesSoldSSRB240 = Math.round(rs17.getDouble(1));
			}
			
			long ConvertedCasesSoldSSRBSlice = 0;
			ResultSet rs18 = s3.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where ipv.category_id = 1 and ipv.package_id = 16 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") ");
			if(rs18.first()){
				ConvertedCasesSoldSSRBSlice = Math.round(rs18.getDouble(1));
			}
			
			long ConvertedCasesSoldSSRBAF = 0;
			ResultSet rs19 = s3.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where ipv.product_id = 81 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") ");
			if(rs19.first()){
				ConvertedCasesSoldSSRBAF = Math.round(rs19.getDouble(1));
			}
			
			long ConvertedCasesSoldSSRBTotal = ConvertedCasesSoldSSRB250 + ConvertedCasesSoldSSRB240 + ConvertedCasesSoldSSRBSlice + ConvertedCasesSoldSSRBAF;
			
			long CasesSold1500ML = 0;
			ResultSet rs21 = s3.executeQuery("select sum(isap.total_units), ipv.unit_per_sku from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where ipv.category_id = 1 and ipv.package_id = 2 and ipv.type_id = 1 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") ");
			if(rs21.first()){
				CasesSold1500ML = Math.round(rs21.getDouble(1)/rs21.getDouble(2));
			}
			
			
			double GrossRevenue = 0;
			double UniqueOutletsInvoiced = 0;
			double InvoiceCount = 0;
			ResultSet rs4 = s3.executeQuery("select sum(net_amount), count(distinct outlet_id), count(id) from inventory_sales_adjusted where order_id in ("+OrderIDs+") and net_amount != 0");
			if(rs4.first()){
				GrossRevenue = rs4.getDouble(1);
				UniqueOutletsInvoiced = rs4.getDouble(2);
				InvoiceCount = rs4.getDouble(3);
			}
			double BillProductivity = 0;
			if (OrderCount != 0){
				BillProductivity = (InvoiceCount / OrderCount) * 100;
			}
			
			int AdjustedCalls = 0;
			ResultSet rs20 = s3.executeQuery("SELECT count(distinct invoice_id) FROM inventory_sales_dispatch_adjusted_products where invoice_id in ("+
				"select id from inventory_sales_adjusted where order_id in ("+OrderIDs+")"+
			")");
			if (rs20.first()){
				AdjustedCalls = rs20.getInt(1);
			}
			
			double UnplannedOutletCount = 0;//UniqueOutletsOrdered - OrdersFromPlannedOutlets;
			double ECO = 0;
			
			
			/*
			double TotalLinesSold = 0;
			
			ResultSet rs5 = s3.executeQuery("select count(*) total_lines_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+")");
			if(rs5.first()){
			 TotalLinesSold = rs5.getDouble("total_lines_sold");
			}
			

			double TotalLinesSoldCSD = 0;
			ResultSet rs13 = s3.executeQuery("select count(*) total_lines_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products ip on isap.product_id = ip.id where ip.type_id = 1 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+")");
			if(rs13.first()){
				TotalLinesSoldCSD = rs13.getDouble("total_lines_sold");
			}
			
			double TotalLinesSoldNCB = 0;
			ResultSet rs14 = s3.executeQuery("select count(*) total_lines_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products ip on isap.product_id = ip.id where ip.type_id = 2 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+")");
			if(rs14.first()){
				TotalLinesSoldNCB = rs14.getDouble("total_lines_sold");
			}
			
			double TotalQuantitySold = 0;
			ResultSet rs9 = s3.executeQuery("select sum(isap.raw_cases) total_qty_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+")");
			if(rs9.first()){
				TotalQuantitySold = rs9.getDouble("total_qty_sold");
			}
			*/
			double SKUPerBill = 0;
			if (InvoiceCount != 0){
			 //SKUPerBill = TotalLinesSold / InvoiceCount;
			}
			
			double SKUPerBillCSD = 0;
			if (InvoiceCount != 0){
			 //SKUPerBillCSD = TotalLinesSoldCSD / InvoiceCount;
			}
			
			double SKUPerBillNCB = 0;
			if (InvoiceCount != 0){
			 //SKUPerBillNCB = TotalLinesSoldNCB / InvoiceCount;
			}
			
			double DropSize = 0;
			if (InvoiceCount != 0){
				 //DropSize = TotalQuantitySold / InvoiceCount;
			}
			double PackagesSold = 0;
			/*
			ResultSet rs6 = s3.executeQuery("select count(package_count) package_total from (" +
					" select mop.id, count(*) package_count  from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mop.is_promotion = 0 and mo.id in ("+OrderIDs+") group by mop.id,ipv.package_id " +
			" ) tb1");
			if(rs6.first()){
				PackagesSold = rs6.getDouble("package_total");
			}
			
			double PackPerBill = 0;
			if (InvoiceCount != 0){
				PackPerBill = PackagesSold / InvoiceCount;
			}
			*/
			double PackPerBill = 0;
			Date AvgTimeIn= new Date();
			/*
			ResultSet rs7 = s3.executeQuery("SELECT sec_to_time(avg(time_to_sec(time_format(first_order_time,'%H:%i')))) avg_time_in from (" +
					" SELECT min(mobile_timestamp) first_order_time FROM mobile_order where mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and distributor_id="+DistributorID+" group by date(mobile_timestamp)" +
					" ) tbl1");
			
			if(rs7.first()){				
				AvgTimeIn = rs7.getTime("avg_time_in");
			}
			*/
			Date AvgTimeOut= new Date();
			/*
			ResultSet rs8 = s3.executeQuery("SELECT sec_to_time(avg(time_to_sec(time_format(last_order_time,'%H:%i')))) avg_time_out from (" +
					" SELECT max(mobile_timestamp) last_order_time FROM mobile_order where mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and distributor_id="+DistributorID+" group by date(mobile_timestamp)" +
					" ) tbl2");
			if(rs8.first()){				
				AvgTimeOut = rs8.getTime("avg_time_out");
			}
			*/
			
			
			int DaysLeft = 0;
			long TargetCC = 0;
			long TargetAchievedCC = 0;
			int DaysPast = 0;
			ResultSet rs15 = s3.executeQuery("SELECT id, dt.distributor_id, dt.start_date, dt.end_date, to_days(dt.end_date)-to_days(date("+Utilities.getSQLDate(EndDate)+")) days_left, to_days(date("+Utilities.getSQLDate(EndDate)+"))-to_days(dt.start_date) days_past, ( "+
					"select sum(((dtp.quantity * ip.unit_per_case) * ip.liquid_in_ml) / ip.conversion_rate_in_ml) converted_cases from distributor_targets_packages dtp join inventory_packages ip on dtp.package_id = ip.id where dtp.id = dt.id "+
				") converted_cases_target, ( "+
					"select sum((isap.total_units * ipv.liquid_in_ml / ipv.conversion_rate_in_ml)) converted_cases_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.distributor_id = "+DistributorID+" and isap.is_promotion = 0 and isa.created_on between dt.start_date and from_days(to_days(dt.end_date)+1) "+
				") converted_cases_sold FROM distributor_targets dt where dt.distributor_id = "+DistributorID+" and dt.type_id = 1 and date("+Utilities.getSQLDate(EndDate)+") between dt.start_date and dt.end_date");
			
			if (rs15.first()){
				DaysLeft = rs15.getInt("days_left");
				TargetCC = Math.round(rs15.getDouble("converted_cases_target"));
				TargetAchievedCC = Math.round(rs15.getDouble("converted_cases_sold"));
				DaysPast = rs15.getInt("days_past");
			}
			
			
			double CompletedCalls = InvoiceCount;
			
			long ZeroSale10 = 0;
			long ZeroSale20 = 0;
			long ZeroSale30 = 0;
			
			if (DistributorName.indexOf("NCSD") == -1){
				
				/*
				long ZeroSaleOutlets = 0;
				ResultSet rs2 = s.executeQuery("select count(*) from ("+
						"select outlet_id, to_days(curdate())-to_days(ifnull((select date(max(created_on)) from inventory_sales_adjusted where outlet_id = tab1.outlet_id),'2013-01-01')) days_ago from ("+
						"SELECT distinct outlet_id FROM distributor_beat_plan_view dbpv join common_distributors cd on dbpv.distributor_id = cd.distributor_id where (cd.snd_id = "+EmployeeID+" or cd.rsm_id = "+EmployeeID+") and cd.distributor_id in (select distinct distributor_id from inventory_sales_adjusted) and cd.distributor_id not in (100059, 100814)"+
						") tab1 having days_ago > 30"+
						") tab3");
				if (rs2.first()){
					ZeroSaleOutlets = rs2.getLong(1);
				}
				*/
				
				ResultSet rs27 = s3.executeQuery("select outlet_id, to_days(curdate())-to_days(ifnull((select date(max(created_on)) from inventory_sales_adjusted where outlet_id = tab1.outlet_id),'2013-01-01')) days_ago from ("+
				"SELECT distinct outlet_id FROM distributor_beat_plan_view dbpv where dbpv.distributor_id = "+DistributorID+
				") tab1 having days_ago > 15");
				//ResultSet rs27 = s3.executeQuery("select codv.outlet_id, to_days("+Utilities.getSQLDate(EndDate)+") - ifnull(to_days((select max(created_on) from inventory_sales_adjusted where outlet_id = codv.outlet_id)),0) days from common_outlets_distributors_view codv where codv.distributor_id = "+DistributorID+" having days > 10");
				while(rs27.next()){
					
					int days = rs27.getInt(2);
					if (days > 15 && days <= 25){
						ZeroSale10++;
					}else if (days > 25 && days <= 45){
						ZeroSale20++;
					}else if (days > 45){
						ZeroSale30++;
					}
				}
			}
			
			
			
			
			s2.executeUpdate("insert into temp_orderbooker_performance (name, avg_time_in, avg_time_out, outlets_in_pjp, outlets_ordered, converted_cases_sold, gross_revenue, eco, bill_productivity, sku_per_bill, pack_size_per_bill, drop_size, orderbookers_worked,unplanned_calls, converted_cases_csd, converted_cases_ncb, sku_per_order_csd, sku_per_order_ncb, ssrb_250, ssrb_240, ssrb_total, target_days_left, target_cc, target_achieved_cc, target_days_past, cr_assigned, completed_calls, adjusted_calls, ssrb_slice, ssrb_aquafina, cases_sold_1500ml, lifting_csd, lifting_ncb, lifting_total, first_10, second_10, third_10, first_10_cc, second_10_cc, third_10_cc, zero_sales_10, zero_sales_20, zero_sales_30)"+
									"values('"+DistributorID+" - "+DistributorName+"','"+Utilities.getDisplayTimeFormat(AvgTimeIn)+"','"+Utilities.getDisplayTimeFormat(AvgTimeOut)+"',"+PlannedOutletsCount+","+OrderCount+","+ConvertedCasesSold+","+GrossRevenue+","+ECO+","+BillProductivity+","+SKUPerBill+","+PackPerBill+","+DropSize+","+OrderBookersWorked+","+UnplannedOutletCount+","+ConvertedCasesSoldCSD+","+ConvertedCasesSoldNCB+","+SKUPerBillCSD+","+SKUPerBillNCB+","+ConvertedCasesSoldSSRB250+","+ConvertedCasesSoldSSRB240+","+ConvertedCasesSoldSSRBTotal+","+DaysLeft+","+TargetCC+","+TargetAchievedCC+","+DaysPast+", "+OrderBookersAssigned+","+CompletedCalls+", "+AdjustedCalls+","+ConvertedCasesSoldSSRBSlice+","+ConvertedCasesSoldSSRBAF+","+CasesSold1500ML+","+ConvertedCasesLiftedCSD+","+ConvertedCasesLiftedNCB+","+ConvertedCasesLiftedTotal+","+First10Percentage+","+Second10Percentage+","+Third10Percentage+","+ConvertedCasesSoldFirst10+","+ConvertedCasesSoldSecond10+","+ConvertedCasesSoldThird10+","+ZeroSale10+","+ZeroSale20+","+ZeroSale30+") ");
			
			
			
		}
		
	}
	
}
