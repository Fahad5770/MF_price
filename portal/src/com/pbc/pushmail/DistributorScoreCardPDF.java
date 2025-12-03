package com.pbc.pushmail;

import java.io.FileOutputStream;
import java.io.IOException;
 









import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
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

public class DistributorScoreCardPDF {

	public static final String RESULT = Utilities.getEmailAttachmentsPath()+ "/CashInflowSummary.pdf";
	
	Font fonttableheader = FontFactory.getFont("Arial", 6, Font.NORMAL);
	Font fontheading = FontFactory.getFont("Arial", 6, Font.NORMAL);
	Font fontpbc = FontFactory.getFont("Arial", 8, Font.BOLD);
	Font fonttitle = FontFactory.getFont("Arial", 8, Font.BOLD);
	Font fonttitleblack = FontFactory.getFont("Arial", 8, Font.BOLD);
	Font reportheading = FontFactory.getFont("Arial", 6, Font.BOLD);
	
	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	
	
	
	Date StartDate = new Date();
	Date EndDate = new Date();
	
	Date Yesterday = Utilities.getDateByDays(-1);
	
	
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
	
	public DistributorScoreCardPDF() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		
		
		ds.createConnection();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		
		
		
		if (Utilities.getDayOfWeekByDate(Yesterday) == 6){
			Yesterday = Utilities.getDateByDays(-2);
		}
		
	}
	
	public void createPdf(String filename, long SND_ID) throws DocumentException, IOException, SQLException {
		
		
		this.SND_ID = SND_ID;
		
		Document document = new Document(PageSize.A3.rotate());
	    PdfWriter.getInstance(document, new FileOutputStream(filename));
	    
	    document.open();
	    
	    PdfPTable table = new PdfPTable(26);
	    
	    table.setWidthPercentage(100f);
	    table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
	    
	    reportheading.setColor(BaseColor.WHITE);
	    fonttitle.setColor(BaseColor.WHITE);
	    
	    
	    PdfPCell pcell = new PdfPCell(new Phrase("Daily Distributor Score Card (Order Based)", fonttitle));
        pcell.setBackgroundColor(BaseColor.BLUE);
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcell.setBorderColor(BaseColor.BLUE);
        pcell.setColspan(26);
        pcell.setMinimumHeight(22);
        table.addCell(pcell);
        
        table.addCell(getHeadingCellLevel2("",3));
        table.addCell(getHeadingCellLevel2("Order Bookers",3));
        table.addCell(getHeadingCellLevel2("Productivity",6));
        table.addCell(getHeadingCellLevel2("SKU / Order",3));
        table.addCell(getHeadingCellLevel2("Orders in Converted Cases",3));
        table.addCell(getHeadingCellLevel2("SSRB in Converted Cases",3));
        table.addCell(getHeadingCellLevel2("Target in Converted Cases",4));
        table.addCell(getHeadingCellLevel2("",1));
        
	    pcell = new PdfPCell(getHeadingCell("Distributor"));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(222, 222, 222));
        pcell.setColspan(3);
        table.addCell(pcell);
        
        
        table.addCell(getHeadingCell("Worked"));
        table.addCell(getHeadingCell("Check In"));
        table.addCell(getHeadingCell("Check Out"));
        table.addCell(getHeadingCell("Planned Calls"));
        table.addCell(getHeadingCell("Productive Calls"));
        table.addCell(getHeadingCell("Unplanned Calls"));
        table.addCell(getHeadingCell("Productivity"));
        table.addCell(getHeadingCell("Last-day Productivity"));
        table.addCell(getHeadingCell("Drop Size"));
        table.addCell(getHeadingCell("CSD"));
        table.addCell(getHeadingCell("NCB"));
        table.addCell(getHeadingCell("Total"));
        table.addCell(getHeadingCell("CSD"));
        table.addCell(getHeadingCell("NCB"));
        table.addCell(getHeadingCell("Total"));
        table.addCell(getHeadingCell("Standard"));
        table.addCell(getHeadingCell("Sting"));
        table.addCell(getHeadingCell("Total"));
        table.addCell(getHeadingCell("Target"));
        table.addCell(getHeadingCell("Achieved"));
        table.addCell(getHeadingCell("Achieved (%)"));
        table.addCell(getHeadingCell("Days Left"));
        table.addCell(getHeadingCell("Order Amount"));
        
        Paragraph header = new Paragraph("Period: "+Utilities.getDisplayDateFormat(new Date())+" - "+Utilities.getDisplayDateFormat(new Date()),fontheading);
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
		long TotalConvertedCasesSSRBTotal = 0;
		
		long TotalTargetCC = 0;
		long TotalTargetAchievedCC = 0;
		
		long TotalUnplannedCalls = 0;
		
		boolean isNCBRowPresented = false;
		
		double TotalYesterdayPlannedCalls = 0;
		double TotalYesterdayProductiveCalls = 0;
		
		
		ResultSet rs1 = s3.executeQuery("select * from temp_orderbooker_performance order by is_ncsd, name");
		while(rs1.next()){
			
			if (rs1.getDouble("is_ncsd") == 1 && isNCBRowPresented == false){
				
				double TotalTargetPercentage = 0;
				if (TotalTargetCC != 0){
					TotalTargetPercentage = (((TotalTargetAchievedCC * 1d) / (TotalTargetCC * 1d))) * 100;
				}
				
				
			    pcell = new PdfPCell(new Phrase("", fontheading));
		        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
		        pcell.setColspan(6);
		        table.addCell(pcell);
			    
				table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(TotalOutletsPJP)+""));
				table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(TotalOutletsOrdered)+""));
				table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(TotalUnplannedCalls)+""));
				
				double TotalProductivity = 0;
				if (TotalOutletsPJP != 0){
					TotalProductivity = ((TotalOutletsOrdered * 1d) / (TotalOutletsPJP * 1d)) * 100d;
				}

				double TotalYesterdayProductivity = 0;
				if (TotalYesterdayPlannedCalls != 0){
					TotalYesterdayProductivity = (TotalYesterdayProductiveCalls / TotalYesterdayPlannedCalls) * 100;
				}
				
				table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(TotalProductivity)+"%"));
				table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(TotalYesterdayProductivity)+"%"));
			    pcell = new PdfPCell(new Phrase("", fontheading));
		        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
		        pcell.setColspan(4);
		        table.addCell(pcell);
				table.addCell(getNormalCellRightAlignedCC(Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesCSD)));
				table.addCell(getNormalCellRightAlignedCC(Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesNCB)));
				table.addCell(getNormalCellRightAlignedCC(Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCases)));
				
				table.addCell(getNormalCellRightAlignedSSRB(Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesSSRB250)));
				table.addCell(getNormalCellRightAlignedSSRB(Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesSSRB240)));
				table.addCell(getNormalCellRightAlignedSSRB(Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesSSRBTotal)));
				
				table.addCell(getNormalCellRightAlignedTarget(Utilities.getDisplayCurrencyFormatRounded(TotalTargetCC)));
				table.addCell(getNormalCellRightAlignedTarget(Utilities.getDisplayCurrencyFormatRounded(TotalTargetAchievedCC)));
				table.addCell(getNormalCellRightAlignedTarget(Utilities.getDisplayCurrencyFormatRounded(TotalTargetPercentage)+"%"));
				table.addCell(getNormalCellRightAlignedTarget(""));
				
				table.addCell(getNormalCellRightAligned(Utilities.getDisplayCurrencyFormatRounded(TotalOrderAmount)));
		        
			    pcell = new PdfPCell(new Phrase("NCB", fonttitleblack));
		        pcell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
		        pcell.setColspan(26);
		        table.addCell(pcell);

		        isNCBRowPresented = true;
				
		        /**/
				
				TotalOutletsPJP = 0;
				TotalOutletsOrdered = 0;
				TotalConvertedCases = 0;
				TotalConvertedCasesCSD = 0;
				TotalConvertedCasesNCB = 0;
				TotalOrderAmount = 0;
				
				
				TotalConvertedCasesSSRB250 = 0;
				TotalConvertedCasesSSRB240 = 0;
				TotalConvertedCasesSSRBTotal = 0;
				
				TotalTargetCC = 0;
				TotalTargetAchievedCC = 0;
				
				TotalUnplannedCalls = 0;
				
				TotalYesterdayPlannedCalls = 0;
				TotalYesterdayProductiveCalls = 0;
				
				/**/
		        
			}
			
			int OutletsPJP = rs1.getInt("outlets_in_pjp");
			int OutletsOrdered = rs1.getInt("outlets_ordered");
			double OrderAmount = rs1.getDouble("gross_revenue"); 
			double UnplannedCalls = rs1.getDouble("unplanned_calls");

			TotalOutletsPJP += OutletsPJP;
			TotalOutletsOrdered += OutletsOrdered;
			TotalOrderAmount += OrderAmount;
			
			
			double YesterdayPlannedCalls = rs1.getDouble("yesterday_planned_calls");
			double YesterdayProductiveCalls = rs1.getDouble("yesterday_productive_calls");
			
			TotalYesterdayPlannedCalls += YesterdayPlannedCalls;
			TotalYesterdayProductiveCalls += YesterdayProductiveCalls;
			
			double YesterdayProductivity = 0;
			if (YesterdayPlannedCalls != 0){
				YesterdayProductivity = (YesterdayProductiveCalls / YesterdayPlannedCalls) * 100;
			}
			
			long ConvertedCasesSSRB250 = rs1.getLong("ssrb_250");
			long ConvertedCasesSSRB240 = rs1.getLong("ssrb_240");
			long ConvertedCasesSSRBTotal = rs1.getLong("ssrb_total");
			
			TotalConvertedCasesSSRB250 += ConvertedCasesSSRB250;
			TotalConvertedCasesSSRB240 += ConvertedCasesSSRB240;
			TotalConvertedCasesSSRBTotal += ConvertedCasesSSRBTotal;
			
			int ConvertedCases = rs1.getInt("converted_cases_sold");
			double ConvertedCasesCSD = rs1.getInt("converted_cases_csd");
			double ConvertedCasesNCB = rs1.getInt("converted_cases_ncb");

			TotalConvertedCasesCSD += ConvertedCasesCSD;
			TotalConvertedCasesNCB += ConvertedCasesNCB;
			TotalConvertedCases += ConvertedCases;
			
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
			
			
			TotalUnplannedCalls += UnplannedCalls;
			
			
			
			
		    pcell = new PdfPCell(new Phrase(Utilities.truncateStringToMax(rs1.getString("name"),33), fontheading));
	        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
	        pcell.setColspan(3);
	        table.addCell(pcell);
	        
	        
			table.addCell(getNormalCellOB(Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("orderbookers_worked"))+" / "+Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("cr_assigned"))));
			table.addCell(getNormalCellOB(rs1.getString("avg_time_in")));
			table.addCell(getNormalCellOB(rs1.getString("avg_time_out")));
			table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(OutletsPJP)+""));
			table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(OutletsOrdered)+""));
			if (UnplannedCalls != 0){
				table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(UnplannedCalls)));
			}else{
				table.addCell(getNormalCell(""));
			}
			table.addCell(getNormalCellStrikeRate(Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("eco"))));
			table.addCell(getNormalCellStrikeRate(Utilities.getDisplayCurrencyFormatRounded(YesterdayProductivity)));
			table.addCell(getNormalCellDropSize(Utilities.getDisplayCurrencyFormatOneDecimal(rs1.getDouble("drop_size"))));
			table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatOneDecimal(rs1.getDouble("sku_per_order_csd"))));
			table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatOneDecimal(rs1.getDouble("sku_per_order_ncb"))));
			table.addCell(getNormalCellSKU(Utilities.getDisplayCurrencyFormatOneDecimal(rs1.getDouble("sku_per_bill"))));
			table.addCell(getNormalCellRightAlignedCC(Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesCSD)));
			table.addCell(getNormalCellRightAlignedCC(Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesNCB)));
			table.addCell(getNormalCellRightAlignedCC(Utilities.getDisplayCurrencyFormatRounded(ConvertedCases)));
			table.addCell(getNormalCellRightAlignedSSRB(Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesSSRB250)));
			table.addCell(getNormalCellRightAlignedSSRB(Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesSSRB240)));
			table.addCell(getNormalCellRightAlignedSSRB(Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesSSRBTotal)));
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
			
			table.addCell(getNormalCellRightAligned(Utilities.getDisplayCurrencyFormatRounded(OrderAmount)));
		}
		
		
		double TotalTargetPercentage = 0;
		if (TotalTargetCC != 0){
			TotalTargetPercentage = (((TotalTargetAchievedCC * 1d) / (TotalTargetCC * 1d))) * 100;
		}
		
		
	    pcell = new PdfPCell(new Phrase("", fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setColspan(6);
        table.addCell(pcell);
	    
		table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(TotalOutletsPJP)+""));
		table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(TotalOutletsOrdered)+""));
		table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(TotalUnplannedCalls)+""));
		
		double TotalProductivity = 0;
		if (TotalOutletsPJP != 0){
			TotalProductivity = ((TotalOutletsOrdered * 1d) / (TotalOutletsPJP * 1d)) * 100d;
		}

		double TotalYesterdayProductivity = 0;
		if (TotalYesterdayPlannedCalls != 0){
			TotalYesterdayProductivity = (TotalYesterdayProductiveCalls / TotalYesterdayPlannedCalls) * 100;
		}
		
		table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(TotalProductivity)+"%"));
		table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(TotalYesterdayProductivity)+"%"));
	    pcell = new PdfPCell(new Phrase("", fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setColspan(4);
        table.addCell(pcell);
		table.addCell(getNormalCellRightAlignedCC(Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesCSD)));
		table.addCell(getNormalCellRightAlignedCC(Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesNCB)));
		table.addCell(getNormalCellRightAlignedCC(Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCases)));
		
		table.addCell(getNormalCellRightAlignedSSRB(Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesSSRB250)));
		table.addCell(getNormalCellRightAlignedSSRB(Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesSSRB240)));
		table.addCell(getNormalCellRightAlignedSSRB(Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesSSRBTotal)));
		
		table.addCell(getNormalCellRightAlignedTarget(Utilities.getDisplayCurrencyFormatRounded(TotalTargetCC)));
		table.addCell(getNormalCellRightAlignedTarget(Utilities.getDisplayCurrencyFormatRounded(TotalTargetAchievedCC)));
		table.addCell(getNormalCellRightAlignedTarget(Utilities.getDisplayCurrencyFormatRounded(TotalTargetPercentage)+"%"));
		table.addCell(getNormalCellRightAlignedTarget(""));
		
		table.addCell(getNormalCellRightAligned(Utilities.getDisplayCurrencyFormatRounded(TotalOrderAmount)));
	    
	    document.add(table);
	    document.add(Chunk.NEWLINE);
	    document.add(new Paragraph("*KPIs are based on today's orders which are subject to change due to backorders, returns or cancellations.",fontheading));
	    
	    document.add(Chunk.NEWLINE);
	    document.add(new Paragraph("Definitions:",fontheading));
	    document.add(new Paragraph("Check In: Mobile Timestamp of First Order",fontheading));
	    document.add(new Paragraph("Check Out: Mobile Timestamp of Last Order",fontheading));
	    document.add(new Paragraph("Planned Calls: No. of Outlets in Beat Plan today",fontheading));
	    document.add(new Paragraph("Productive Calls: No. of Outlets placed Order",fontheading));
	    document.add(Chunk.NEWLINE);
	    document.add(new Paragraph("Formulas:",fontheading));
	    document.add(new Paragraph("Productivity = Productive Calls / Planned Calls",fontheading));
	    document.add(new Paragraph("SKU/Order = Total Lines Sold / Total Orders",fontheading));
	    document.add(new Paragraph("Drop Size = Total Quantity per Order / Total No. of Orders",fontheading));
	    
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
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        
        if (StrikeRate < 50){
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
		
		s.executeUpdate("create temporary table temp_orderbooker_performance (name varchar(500), percent_before_10am double, percent_after_6pm double, avg_time_in varchar(20), avg_time_out varchar(20),outlets_in_pjp int(5), outlets_ordered int(5), converted_cases_sold int(10), gross_revenue double, eco double, bill_productivity double, sku_per_bill double, pack_size_per_bill double, drop_size double, orderbookers_worked double, unplanned_calls double, converted_cases_csd double, converted_cases_ncb double, sku_per_order_csd double, sku_per_order_ncb double, ssrb_250 double, ssrb_240 double, ssrb_total double, target_days_left double, target_cc double, target_achieved_cc double, target_days_past double, cr_assigned double, is_ncsd double, yesterday_planned_calls double, yesterday_productive_calls double)");
								
		ResultSet rs = s.executeQuery("select mo.distributor_id, (select name from common_distributors where distributor_id = mo.distributor_id) distributor_name, group_concat(mo.id) order_ids, group_concat(distinct dayofweek(mo.created_on)) days_of_week, count(mo.id) order_count, count(distinct mo.outlet_id) unique_outlets_ordered, count(distinct created_by) orderbookers_worked, group_concat(distinct mo.outlet_id) outlet_ids_ordered from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and distributor_id in (SELECT distributor_id FROM common_distributors where (snd_id in ("+this.SND_ID+") or rsm_id in ("+this.SND_ID+"))) group by mo.distributor_id");
		
		while(rs.next()){
			
			
			long DistributorID = rs.getLong(1);
			String DistributorName = rs.getString(2);
			
			
			int isNCSD = 0;
			if (DistributorName.toLowerCase().indexOf("ncsd") != -1){
				isNCSD = 1;
			}
			
			
			double OrderBookersWorked = rs.getDouble("orderbookers_worked");
			String OrderIDs = rs.getString("order_ids");
			String OutletIDsOrdered = rs.getString("outlet_ids_ordered");
			String DaysOfWeekIDs = rs.getString("days_of_week");
			double OrderCount = rs.getInt("order_count"); 
			double UniqueOutletsOrdered = rs.getInt("unique_outlets_ordered");
			
			double PlannedOutletsCount = 0;
			String PlannedOutletIDs = "";
			
			int OrderBookersAssigned = 0;
			ResultSet rs2 = s3.executeQuery("select count(distinct outlet_id), group_concat(distinct outlet_id), count(distinct assigned_to) cr_assigned from distributor_beat_plan_log force index (distributor_beat_plan_log_date_distributor_id_day_number) where log_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and distributor_id = "+DistributorID+" and day_number in ("+DaysOfWeekIDs+") and assigned_to is not null");
			if (rs2.first()){
				PlannedOutletsCount = rs2.getDouble(1);
				PlannedOutletIDs = rs2.getString(2);
				OrderBookersAssigned = rs2.getInt("cr_assigned");
			}
			
			
			// Yesterday Productivity
			
			Calendar c = Calendar.getInstance();
			c.setTime(Yesterday);
			int YesterdayDayOfWeek = c.get(Calendar.DAY_OF_WEEK);			
			double YesterdayPlannedOutletsCount = 0;
			String YesterdayPlannedOutletIDs = "";
			
			
			
			
			
			
			
			ResultSet rs51 = s3.executeQuery("select count(distinct outlet_id), group_concat(distinct outlet_id) from distributor_beat_plan_log force index (distributor_beat_plan_log_date_distributor_id_day_number) where log_date between "+Utilities.getSQLDate(Yesterday)+" and "+Utilities.getSQLDate(Yesterday)+" and distributor_id = "+DistributorID+" and day_number in ("+YesterdayDayOfWeek+") and assigned_to is not null");
			if (rs51.first()){
				YesterdayPlannedOutletsCount = rs51.getDouble(1);
				YesterdayPlannedOutletIDs = rs51.getString(2);
			}
			
			double YesterdayOrdersFromPlannedOutlets = 0;
			ResultSet rs52 = s3.executeQuery("select count(distinct outlet_id) from mobile_order where created_on between "+Utilities.getSQLDate(Yesterday)+" and "+Utilities.getSQLDateNext(Yesterday)+" and outlet_id in ("+YesterdayPlannedOutletIDs+") and distributor_id = "+DistributorID);
			if (rs52.first()){
				YesterdayOrdersFromPlannedOutlets = rs52.getDouble(1);
			}
			
			// Yesterday Productivity
			
			
			if (DistributorID == 100666){
				//System.out.println("select count(distinct outlet_id), group_concat(distinct outlet_id), count(distinct assigned_to) cr_assigned from distributor_beat_plan_log force index (distributor_beat_plan_log_date_distributor_id_day_number) where log_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and distributor_id = "+DistributorID+" and day_number in ("+DaysOfWeekIDs+") and assigned_to is not null");
			}
			double OrdersFromPlannedOutlets = 0;
			ResultSet rs10 = s3.executeQuery("select count(id) from common_outlets where id in ("+PlannedOutletIDs+") and id in ("+OutletIDsOrdered+")");
			if (rs10.first()){
				OrdersFromPlannedOutlets = rs10.getDouble(1);
			}
			/*
			
			ResultSet rs3 = s3.executeQuery("select sum(((mop.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mop.is_promotion = 0 and mo.id in ("+OrderIDs+") ");
			if(rs3.first()){
				ConvertedCasesSold = rs3.getDouble(1);
			}
			*/
			long ConvertedCasesSoldCSD = 0;
			ResultSet rs12 = s3.executeQuery("select sum(((mop.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where ipv.type_id = 1 and mop.is_promotion = 0 and mo.id in ("+OrderIDs+") ");
			if(rs12.first()){
				ConvertedCasesSoldCSD = Math.round(rs12.getDouble(1));
			}
			
			long ConvertedCasesSoldNCB = 0;
			ResultSet rs11 = s3.executeQuery("select sum(((mop.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where ipv.type_id = 2 and mop.is_promotion = 0 and mo.id in ("+OrderIDs+") ");
			if(rs11.first()){
				ConvertedCasesSoldNCB = Math.round(rs11.getDouble(1));
			}
			long ConvertedCasesSold = ConvertedCasesSoldCSD + ConvertedCasesSoldNCB;
			
			long ConvertedCasesSoldSSRB250 = 0;
			ResultSet rs16 = s3.executeQuery("select sum(((mop.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where ipv.ssrb_type_id = 1 and mop.is_promotion = 0 and mo.id in ("+OrderIDs+") ");
			if(rs16.first()){
				ConvertedCasesSoldSSRB250 = Math.round(rs16.getDouble(1));
			}
			
			long ConvertedCasesSoldSSRB240 = 0;
			ResultSet rs17 = s3.executeQuery("select sum(((mop.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where ipv.ssrb_type_id = 2 and mop.is_promotion = 0 and mo.id in ("+OrderIDs+") ");
			if(rs17.first()){
				ConvertedCasesSoldSSRB240 = Math.round(rs17.getDouble(1));
			}
			long ConvertedCasesSoldSSRBTotal = ConvertedCasesSoldSSRB250 + ConvertedCasesSoldSSRB240;
			
			
			double GrossRevenue = 0;
			double UniqueOutletsInvoiced = 0;
			double InvoiceCount = 0;
			ResultSet rs4 = s3.executeQuery("select sum(net_amount), count(distinct outlet_id), count(id) from mobile_order where id in ("+OrderIDs+") and net_amount != 0");
			if(rs4.first()){
				GrossRevenue = rs4.getDouble(1);
				UniqueOutletsInvoiced = rs4.getDouble(2);
				InvoiceCount = rs4.getDouble(3);
			}
			double ECO = 0;
			if (PlannedOutletsCount != 0){
				ECO = (OrdersFromPlannedOutlets / PlannedOutletsCount) * 100;
			}
			if (ECO > 100){
				ECO = 100;
			}
			
			double UnplannedOutletCount = UniqueOutletsOrdered - OrdersFromPlannedOutlets;
			
			double BillProductivity = 0;
			if (PlannedOutletsCount != 0){
				BillProductivity = (UniqueOutletsInvoiced / PlannedOutletsCount) * 100;
			}
			if (BillProductivity > 100){
				BillProductivity = 100;
			}
			
			double TotalLinesSold = 0;
			ResultSet rs5 = s3.executeQuery("select count(*) total_lines_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mop.is_promotion = 0 and mo.id in ("+OrderIDs+")");
			if(rs5.first()){
			 TotalLinesSold = rs5.getDouble("total_lines_sold");
			}

			double TotalLinesSoldCSD = 0;
			ResultSet rs13 = s3.executeQuery("select count(*) total_lines_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products ip on mop.product_id = ip.id where ip.type_id = 1 and mop.is_promotion = 0 and mo.id in ("+OrderIDs+")");
			if(rs13.first()){
				TotalLinesSoldCSD = rs13.getDouble("total_lines_sold");
			}
			
			double TotalLinesSoldNCB = 0;
			ResultSet rs14 = s3.executeQuery("select count(*) total_lines_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products ip on mop.product_id = ip.id where ip.type_id = 2 and mop.is_promotion = 0 and mo.id in ("+OrderIDs+")");
			if(rs14.first()){
				TotalLinesSoldNCB = rs14.getDouble("total_lines_sold");
			}
			
			double TotalQuantitySold = 0;
			ResultSet rs9 = s3.executeQuery("select sum(mop.raw_cases) total_qty_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mop.is_promotion = 0 and mo.id in ("+OrderIDs+")");
			if(rs9.first()){
				TotalQuantitySold = rs9.getDouble("total_qty_sold");
			}
			
			double SKUPerBill = 0;
			if (InvoiceCount != 0){
			 SKUPerBill = TotalLinesSold / InvoiceCount;
			}
			
			double SKUPerBillCSD = 0;
			if (InvoiceCount != 0){
			 SKUPerBillCSD = TotalLinesSoldCSD / InvoiceCount;
			}
			
			double SKUPerBillNCB = 0;
			if (InvoiceCount != 0){
			 SKUPerBillNCB = TotalLinesSoldNCB / InvoiceCount;
			}
			
			double DropSize = 0;
			if (InvoiceCount != 0){
				 DropSize = TotalQuantitySold / InvoiceCount;
			}
			
			double PackagesSold = 0;
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
			
			Date AvgTimeIn= new Date();
			
			ResultSet rs7 = s3.executeQuery("SELECT sec_to_time(avg(time_to_sec(time_format(first_order_time,'%H:%i')))) avg_time_in from (" +
					" SELECT min(mobile_timestamp) first_order_time FROM mobile_order where mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and distributor_id="+DistributorID+" group by date(mobile_timestamp)" +
					" ) tbl1");
			
			if(rs7.first()){				
				AvgTimeIn = rs7.getTime("avg_time_in");
			}
			
			Date AvgTimeOut= new Date();
			
			ResultSet rs8 = s3.executeQuery("SELECT sec_to_time(avg(time_to_sec(time_format(last_order_time,'%H:%i')))) avg_time_out from (" +
					" SELECT max(mobile_timestamp) last_order_time FROM mobile_order where mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and distributor_id="+DistributorID+" group by date(mobile_timestamp)" +
					" ) tbl2");
			if(rs8.first()){				
				AvgTimeOut = rs8.getTime("avg_time_out");
			}
			
			
			int DaysLeft = 0;
			long TargetCC = 0;
			long TargetAchievedCC = 0;
			int DaysPast = 0;
			ResultSet rs15 = s3.executeQuery("SELECT id, dt.distributor_id, dt.start_date, dt.end_date, to_days(dt.end_date)-to_days(date("+Utilities.getSQLDate(StartDate)+")) days_left, to_days(date("+Utilities.getSQLDate(StartDate)+"))-to_days(dt.start_date) days_past, ( "+
					"select sum(((dtp.quantity * ip.unit_per_case) * ip.liquid_in_ml) / ip.conversion_rate_in_ml) converted_cases from distributor_targets_packages dtp join inventory_packages ip on dtp.package_id = ip.id where dtp.id = dt.id "+
				") converted_cases_target, ( "+
					"select sum((isap.total_units * ipv.liquid_in_ml / ipv.conversion_rate_in_ml)) converted_cases_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.distributor_id = "+DistributorID+" and isap.is_promotion = 0 and isa.created_on between dt.start_date and from_days(to_days(dt.end_date)+1) "+
				") converted_cases_sold FROM distributor_targets dt where dt.distributor_id = "+DistributorID+" and dt.type_id = 1 and date("+Utilities.getSQLDate(StartDate)+") between dt.start_date and dt.end_date");
			if (rs15.first()){
				DaysLeft = rs15.getInt("days_left");
				TargetCC = Math.round(rs15.getDouble("converted_cases_target"));
				TargetAchievedCC = Math.round(rs15.getDouble("converted_cases_sold"));
				DaysPast = rs15.getInt("days_past");
			}
			
			
			s2.executeUpdate("insert into temp_orderbooker_performance (name, avg_time_in, avg_time_out, outlets_in_pjp, outlets_ordered, converted_cases_sold, gross_revenue, eco, bill_productivity, sku_per_bill, pack_size_per_bill, drop_size, orderbookers_worked,unplanned_calls, converted_cases_csd, converted_cases_ncb, sku_per_order_csd, sku_per_order_ncb, ssrb_250, ssrb_240, ssrb_total, target_days_left, target_cc, target_achieved_cc, target_days_past, cr_assigned, is_ncsd, yesterday_planned_calls, yesterday_productive_calls)"+
									"values('"+DistributorID+" - "+DistributorName+"','"+Utilities.getDisplayTimeFormat(AvgTimeIn)+"','"+Utilities.getDisplayTimeFormat(AvgTimeOut)+"',"+PlannedOutletsCount+","+OrdersFromPlannedOutlets+","+ConvertedCasesSold+","+GrossRevenue+","+ECO+","+BillProductivity+","+SKUPerBill+","+PackPerBill+","+DropSize+","+OrderBookersWorked+","+UnplannedOutletCount+","+ConvertedCasesSoldCSD+","+ConvertedCasesSoldNCB+","+SKUPerBillCSD+","+SKUPerBillNCB+","+ConvertedCasesSoldSSRB250+","+ConvertedCasesSoldSSRB240+","+ConvertedCasesSoldSSRBTotal+","+DaysLeft+","+TargetCC+","+TargetAchievedCC+","+DaysPast+", "+OrderBookersAssigned+","+isNCSD+","+YesterdayPlannedOutletsCount+","+YesterdayOrdersFromPlannedOutlets+") ");
			
			
			
			
			
		}
		
	}
	
}
