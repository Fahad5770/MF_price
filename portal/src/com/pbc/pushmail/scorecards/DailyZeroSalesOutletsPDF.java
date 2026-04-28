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

public class DailyZeroSalesOutletsPDF {

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
			DailyZeroSalesOutletsPDF DistributorSD1 = new com.pbc.pushmail.scorecards.DailyZeroSalesOutletsPDF();
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
	
	public DailyZeroSalesOutletsPDF() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		
		
		
		ds.createConnectionToReplica();
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
	    
	    PdfPTable table = new PdfPTable(8);
	    
	    table.setWidthPercentage(100f);
	    table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
	    
	    reportheading.setColor(BaseColor.WHITE);
	    fonttitle.setColor(BaseColor.WHITE);
	    
	    
		long TotalZeroSaleOutlets = 0;
		long ZeroSaleOutletsToDisplay = 0;
	    
		s.executeUpdate("create temporary table temp_outlet_list (outlet_id int(11), outlet_name varchar(200), channel varchar(200), address varchar(200), pjp varchar(200), days_ago int(11), distributor_id bigint(11), distributor_name varchar(200), order_booker_id bigint(11), order_booker_name varchar(200))");
		
		ResultSet rs2 = s2.executeQuery(""+
				"select outlet_id, to_days(curdate())-to_days(ifnull((select date(max(created_on)) from inventory_sales_adjusted where outlet_id = tab1.outlet_id),'2013-01-01')) days_ago from ("+
				"SELECT distinct outlet_id FROM distributor_beat_plan_view dbpv join common_distributors cd on dbpv.distributor_id = cd.distributor_id where (cd.snd_id = "+SND_ID+" or cd.rsm_id = "+SND_ID+" or cd.tdm_id = "+SND_ID+") and cd.distributor_id in (select distinct distributor_id from inventory_sales_adjusted) "+
				") tab1 having days_ago > 30"+
				"");
		while (rs2.next()){
			
			
			long OutletID = rs2.getLong(1);
			long DaysAgo = rs2.getLong(2);
			
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
				
				
				
				s.executeUpdate("insert into temp_outlet_list values ("+OutletID+",'"+OutletName+"','"+channel+"','"+address+"','"+PJP+"',"+DaysAgo+","+DistributorID+",'"+DistributorName+"',"+OrderBookerID+",'"+OrderBookerName+"')");
				
			}
			
			
			
		}

	    
	    
	    PdfPCell pcell = new PdfPCell(new Phrase("Zero Sales Outlets", fonttitle));
        pcell.setBackgroundColor(BaseColor.BLUE);
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcell.setBorderColor(BaseColor.BLUE);
        pcell.setColspan(8);
        pcell.setMinimumHeight(22);
        table.addCell(pcell);
        
        long TotalTOTInjected = 0;
        long TotalTOTInjected2015 = 0;
        long TotalTOTInjected2014 = 0;
        long TotalTOTInjected2013 = 0;
        
        ResultSet rs4 = s.executeQuery("select distinct distributor_id, distributor_name from temp_outlet_list order by distributor_id");
        while(rs4.next()){
        	
        	String DistributorLabel = rs4.getString(1)+" - "+rs4.getString(2);
        	long DistributorID = rs4.getLong(1);
        	
            table.addCell(getHeadingCellLevel2(DistributorLabel,8));

            table.addCell(getHeadingCell("Outlet"));
            table.addCell(getHeadingCell("Address"));
            table.addCell(getHeadingCell("PJP"));
            table.addCell(getHeadingCellCenter("Last Order (Days Ago)"));
            table.addCell(getHeadingCellCenter("TOT Injected",4));
            
            ResultSet rs5 = s2.executeQuery("select distinct order_booker_id, order_booker_name from temp_outlet_list where distributor_id = "+DistributorID+" order by order_booker_id");
            while(rs5.next()){
            	String OrderBookerLabel = rs5.getString(1)+" - "+rs5.getString(2);
            	long OrderBookerID = rs5.getLong(1);
            	
            	
                table.addCell(getHeadingCellLevel3(OrderBookerLabel,4));
                
                table.addCell(getHeadingCellLevel3Center("Total",1));
                table.addCell(getHeadingCellLevel3Center("2016",1));
                table.addCell(getHeadingCellLevel3Center("2015",1));
                table.addCell(getHeadingCellLevel3Center("2014 / Earlier",1));
                
                ResultSet rs6 = s3.executeQuery("select * from temp_outlet_list tol where tol.distributor_id = "+DistributorID+" and tol.order_booker_id = "+OrderBookerID+" and tol.outlet_name not like 'DESK S%' order by tol.days_ago desc");
                while(rs6.next()){
                	
                	String OutletLabel = rs6.getString("outlet_id")+" "+rs6.getString("outlet_name");
                	
                    table.addCell(getNormalCell(OutletLabel));
                    table.addCell(getNormalCell(rs6.getString("address")));
                    table.addCell(getNormalCell(rs6.getString("pjp")));
                    
                    long DaysAgo = rs6.getLong("days_ago");
                    String sDaysAgo = ""+DaysAgo;
                    if (DaysAgo > 800){
                    	sDaysAgo = "Never";
                    }
                    
                    int TOT2013 = 0;
                    int TOT2014 = 0;
                    int TOT2015 = 0;
                    
                    ResultSet rs7 = s4.executeQuery("SELECT year(movement_date_parsed) FROM common_assets where outlet_id = "+rs6.getString("outlet_id")+" and tot_status = 'INJECTED'");
                    while(rs7.next()){
                    	
                    	
                    	int year = rs7.getInt(1);
                    	
                    	if (year <= 2014){
                    		TOT2013++;
                    		TotalTOTInjected2013++;
                    	}
                    	if (year == 2015){
                    		TOT2014++;
                    		TotalTOTInjected2014++;
                    	}
                    	if (year >= 2016){
                    		TOT2015++;
                    		TotalTOTInjected2015++;
                    	}
                    	
                    }

                    table.addCell(getNormalCellCenter(sDaysAgo));
                    
                    int TOTInjected = (TOT2013+TOT2014+TOT2015);
                    
                    String sTOTInjected = "";
                    if (TOTInjected != 0){
                    	sTOTInjected = ""+(TOT2013+TOT2014+TOT2015);
                    }
                    
                    TotalTOTInjected += TOTInjected;
                    
                    table.addCell(getNormalCellCenter(sTOTInjected));
                    if (TOT2015 != 0){
                    	table.addCell(getNormalCellCenter(TOT2015+""));
                    }else{
                    	table.addCell(getNormalCellCenter(""));
                    }
                    if (TOT2014 != 0){
                    	table.addCell(getNormalCellCenter(TOT2014+""));
                    }else{
                    	table.addCell(getNormalCellCenter(""));
                    }
                    if (TOT2013 != 0){
                    	table.addCell(getNormalCellCenter(TOT2013+""));
                    }else{
                    	table.addCell(getNormalCellCenter(""));
                    }
                }
                
            }
            
        	
        }
        
        table.addCell(getNormalCell(""));
        table.addCell(getNormalCell(""));
        table.addCell(getNormalCell(""));
        table.addCell(getNormalCell(""));
        table.addCell(getNormalCellCenter(""+TotalTOTInjected));
        table.addCell(getNormalCellCenter(""+TotalTOTInjected2015));
        table.addCell(getNormalCellCenter(""+TotalTOTInjected2014));
        table.addCell(getNormalCellCenter(""+TotalTOTInjected2013));
        
        
        
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
	    pcell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
	    pcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
	    pcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    pcell2.setBorderColor(BaseColor.LIGHT_GRAY);
	    pcell2.setColspan(colspan);
	    pcell2.setMinimumHeight(20);
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
