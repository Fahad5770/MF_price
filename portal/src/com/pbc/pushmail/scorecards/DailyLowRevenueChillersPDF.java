package com.pbc.pushmail.scorecards;

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

public class DailyLowRevenueChillersPDF {

	public static final String RESULT = Utilities.getEmailAttachmentsPath()+ "/CashInflowSummary.pdf";
	
	Font fonttableheader = FontFactory.getFont("Arial", 8, Font.BOLD);
	Font fontheading = FontFactory.getFont("Arial", 8, Font.NORMAL);
	Font fontpbc = FontFactory.getFont("Arial", 10, Font.BOLD);
	Font fonttitle = FontFactory.getFont("Arial", 10, Font.BOLD);
	Font fonttitleblack = FontFactory.getFont("Arial", 10, Font.BOLD);
	Font reportheading = FontFactory.getFont("Arial", 8, Font.BOLD);
	
	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	
	
	Date StartDate = new Date();
	Date EndDate = new Date();
	
	
	Date Yesterday = Utilities.getDateByDays(-1);
	
	
	long SND_ID = 0;
	
	
	public static void main(String[] args) throws DocumentException, IOException{
		
		try {
			//new DistributorScoreCardPDF().createPdf(RESULT, this.SND_ID);
			DailyLowRevenueChillersPDF DistributorSD1 = new com.pbc.pushmail.scorecards.DailyLowRevenueChillersPDF();
			DistributorSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/test"+Math.random()+".pdf", 2262);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public DailyLowRevenueChillersPDF() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		
		
		ds.createConnection();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6){
			Yesterday = Utilities.getDateByDays(-2);
		}
		
	}
	
	public void createPdf(String filename, long SND_ID) throws DocumentException, IOException, SQLException {
		
		
		this.SND_ID = SND_ID;
		
		Document document = new Document(PageSize.A3.rotate());
	    PdfWriter.getInstance(document, new FileOutputStream(filename));
	    
	    document.open();
	    
	    PdfPTable table = new PdfPTable(5);
	    
	    table.setWidthPercentage(100f);
	    table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
	    
	    reportheading.setColor(BaseColor.WHITE);
	    fonttitle.setColor(BaseColor.WHITE);
	    
	    
		long TotalZeroSaleOutlets = 0;
		long ZeroSaleOutletsToDisplay = 0;
	    
		s.executeUpdate("create temporary table temp_outlet_list (outlet_id int(11), outlet_name varchar(200), channel varchar(200), address varchar(200), pjp varchar(200), days_ago int(11), distributor_id bigint(11), distributor_name varchar(200), order_booker_id bigint(11), order_booker_name varchar(200), revenue_per_day double, chillers double)");
		
		ResultSet rs2 = s2.executeQuery("select outlet_id, cases, ((cases/26)/(select count(*) from common_assets where tot_status = 'INJECTED' and outlet_id_parsed = tab1.outlet_id)) revenue, (select count(*) from common_assets where tot_status = 'INJECTED' and outlet_id_parsed = tab1.outlet_id) tots from ("+
				"SELECT isa.outlet_id, sum(isa.net_amount) cases FROM pep.inventory_sales_adjusted isa where isa.created_on between from_days(to_days(curdate())-30) and from_days(to_days(curdate())+1) and isa.distributor_id in (select distributor_id from common_distributors where snd_id = "+SND_ID+" or rsm_id in ("+SND_ID+")) and net_amount != 0 group by isa.outlet_id order by cases limit 300"+
				") tab1 having tots != 0 order by revenue limit 100");
		while (rs2.next()){
			
			double RevenuePerDay = rs2.getDouble("revenue");
			double Chillers = rs2.getDouble("tots");
			
			long OutletID = rs2.getLong(1);
			long DaysAgo = 0;
			
			ResultSet rs3 = s3.executeQuery("select co.name, co.channel_id, (select label from common_outlets_channels where id = co.channel_id) channel_label, co.address, dbpv.label, dbpv.distributor_id, (select name from common_distributors cd where cd.distributor_id = dbpv.distributor_id) distributor_name, dbpv.assigned_to, (select display_name from users where id = dbpv.assigned_to) order_booker_name from common_outlets co join distributor_beat_plan_view dbpv on co.id = dbpv.outlet_id where co.id = "+OutletID);
			if (rs3.first()){
				
				String OutletName = rs3.getString(1);
				String channel = rs3.getString(3);
				String address = rs3.getString("address");
				String PJP = rs3.getString("label");
				String DistributorName = rs3.getString("distributor_name");
				long DistributorID = rs3.getLong("distributor_id");
				String OrderBookerName = rs3.getString("order_booker_name");
				long OrderBookerID = rs3.getLong("assigned_to");
				
				
				
				s.executeUpdate("insert into temp_outlet_list values ("+OutletID+",'"+OutletName+"','"+channel+"','"+address+"','"+PJP+"',"+DaysAgo+","+DistributorID+",'"+DistributorName+"',"+OrderBookerID+",'"+OrderBookerName+"',"+RevenuePerDay+","+Chillers+")");
				
			}
			
			
			
		}

	    
	    PdfPCell pcell = new PdfPCell(new Phrase("Outlets with Lowest Revenue / Chiller", fonttitle));
        pcell.setBackgroundColor(BaseColor.BLUE);
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcell.setBorderColor(BaseColor.BLUE);
        pcell.setColspan(5);
        pcell.setMinimumHeight(22);
        table.addCell(pcell);
        
        long TotalTOTInjected = 0;
        long TotalTOTInjected2015 = 0;
        long TotalTOTInjected2014 = 0;
        long TotalTOTInjected2013 = 0;
        
        ResultSet rs4 = s.executeQuery("select distinct distributor_id, distributor_name from temp_outlet_list order by revenue_per_day");
        while(rs4.next()){
        	
        	String DistributorLabel = rs4.getString(1)+" - "+rs4.getString(2);
        	long DistributorID = rs4.getLong(1);
        	
            table.addCell(getHeadingCellLevel2(DistributorLabel,5));

            table.addCell(getHeadingCell("Outlet"));
            table.addCell(getHeadingCell("Address"));
            table.addCell(getHeadingCell("PJP"));
            table.addCell(getHeadingCellCenter("# of Chillers"));
            table.addCell(getHeadingCellCenter("Revenue / Chiller / Day (Rs.)",4));
            
            ResultSet rs5 = s2.executeQuery("select distinct order_booker_id, order_booker_name from temp_outlet_list where distributor_id = "+DistributorID+" order by revenue_per_day");
            while(rs5.next()){
            	String OrderBookerLabel = rs5.getString(1)+" - "+rs5.getString(2);
            	long OrderBookerID = rs5.getLong(1);
            	
            	
                table.addCell(getHeadingCellLevel3(OrderBookerLabel,5));
                
                ResultSet rs6 = s3.executeQuery("select * from temp_outlet_list tol where tol.distributor_id = "+DistributorID+" and tol.order_booker_id = "+OrderBookerID+" order by tol.revenue_per_day");
                while(rs6.next()){
                	
                	String OutletLabel = rs6.getString("outlet_id")+" "+rs6.getString("outlet_name");
                	
                    table.addCell(getNormalCell(OutletLabel));
                    table.addCell(getNormalCell(rs6.getString("address")));
                    table.addCell(getNormalCell(rs6.getString("pjp")));
                    table.addCell(getNormalCellCenter(""+Math.round(rs6.getDouble("chillers"))));
                    table.addCell(getNormalCellCenter(Utilities.getDisplayCurrencyFormatRounded(rs6.getDouble("revenue_per_day"))));
                }
                
            }
            
        	
        }
        
        table.addCell(getNormalCell(""));
        table.addCell(getNormalCell(""));
        table.addCell(getNormalCell(""));
        table.addCell(getNormalCell(""));
        table.addCell(getNormalCell(""));
        
        
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
	    
	    
	    
	    
	    document.add(table);
	    document.add(Chunk.NEWLINE);
	    
	    document.add(new Paragraph("Revenue/Chiller/Day = Average revenue per chiller per day in last 30 days",fontheading));
	    
	    document.close();
	    
	    
	    
	}
	
	private PdfPCell getHeadingCell(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(222, 222, 222));
        pcell.setMinimumHeight(20);
        return pcell;
	}
	
	private PdfPCell getHeadingCellCenter(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setVerticalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(222, 222, 222));
        pcell.setMinimumHeight(20);
        return pcell;
	}
	private PdfPCell getHeadingCellCenter(String title, int colspan){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setVerticalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(222, 222, 222));
        pcell.setMinimumHeight(20);
        pcell.setColspan(colspan);
        return pcell;
	}
	
	
	
	private PdfPCell getNormalCell(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        return pcell;
	}
	private PdfPCell getNormalCellCenter(String title){
		
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
	    pcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
	    pcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    pcell2.setBorderColor(BaseColor.LIGHT_GRAY);
	    pcell2.setColspan(colspan);
	    pcell2.setMinimumHeight(20);
	    return pcell2;
	}
	private PdfPCell getHeadingCellLevel3(String title, int colspan){	
	    PdfPCell pcell2 = new PdfPCell(new Phrase(title, fonttableheader));
	    //pcell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
	    pcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
	    pcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    pcell2.setBorderColor(BaseColor.LIGHT_GRAY);
	    pcell2.setColspan(colspan);
	    //pcell2.setMinimumHeight(20);
	    return pcell2;
	}
	private PdfPCell getHeadingCellLevel3Center(String title, int colspan){	
	    PdfPCell pcell2 = new PdfPCell(new Phrase(title, fonttableheader));
	    pcell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
	    pcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	    pcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    pcell2.setBorderColor(BaseColor.LIGHT_GRAY);
	    pcell2.setColspan(colspan);
	    pcell2.setMinimumHeight(20);
	    return pcell2;
	}
	
}
