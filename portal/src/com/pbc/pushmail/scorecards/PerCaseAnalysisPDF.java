package com.pbc.pushmail.scorecards;

import java.io.FileOutputStream;
import java.io.IOException;
 









import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class PerCaseAnalysisPDF {

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
	
	public String BOTTOM_LINE_DISTRIBUTORS[] = new String[5];
	public double BOTTOM_LINE_DISTRIBUTORS_REVENUE[] = new double[5];
	
	public static void main(String[] args) throws DocumentException, IOException, ParseException{
		
		try {
			//new DistributorScoreCardPDF().createPdf(RESULT, this.SND_ID);
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
			String dateInString = "01-08-2015";
			Date StartDate2 = sdf.parse(dateInString);
			
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-M-yyyy");
			String dateInString1 = "03-08-2015";
			Date EndDate2 = sdf1.parse(dateInString1);
			
			
			PerCaseAnalysisPDF DistributorSD1 = new PerCaseAnalysisPDF();
			DistributorSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/RPC"+Math.random()+".pdf", 2262,StartDate2,EndDate2);
			
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
	
	public PerCaseAnalysisPDF() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		
		
		
		ds.createConnectionToReplica();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6){
			Yesterday = Utilities.getDateByDays(-2);
		}
		
	}
	
	public void createPdf(String filename, long SND_ID, Date StartDate1, Date EndDate1) throws DocumentException, IOException, SQLException {
		
		
		this.SND_ID = SND_ID;
		this.StartDate = StartDate1;
		this.EndDate = EndDate1;
		
		
		//SELECT snd_id FROM pep.common_sd_groups where snd_id is not null
		String WhereHOD = "";
		String WhereHOD1 = "";
		String WhereHOD2 = "";
		String WhereCustomerID = "";
		String WhereRSM = "";
		WhereHOD = " and customer_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+SND_ID+")) ";	
		WhereHOD1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+SND_ID+")) ";
		WhereHOD2 = " and distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+SND_ID+")) ";

		if (SND_ID == -1){
			WhereHOD = " and customer_id in(SELECT distributor_id FROM common_distributors where snd_id in (SELECT snd_id FROM pep.common_sd_groups where snd_id is not null)) ";	
			WhereHOD1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in (SELECT snd_id FROM pep.common_sd_groups where snd_id is not null)) ";
			WhereHOD2 = " and distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in (SELECT snd_id FROM pep.common_sd_groups where snd_id is not null)) ";
		}
		
		
		Document document = new Document(PageSize.A3.rotate());
	    PdfWriter.getInstance(document, new FileOutputStream(filename));
	    
	    document.open();
	    
	    PdfPTable table = new PdfPTable(11);
	    
	    table.setWidthPercentage(100f);
	    table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
	    
	    reportheading.setColor(BaseColor.WHITE);
	    fonttitle.setColor(BaseColor.WHITE);
	    
	    
	    
	    PdfPCell pcell = new PdfPCell(new Phrase("Per Case Analysis", fonttitle));
        pcell.setBackgroundColor(BaseColor.BLACK);
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcell.setBorderColor(BaseColor.BLACK);
        pcell.setColspan(26);
        pcell.setMinimumHeight(22);
        table.addCell(pcell);
	    
	    
        
        	
            table.addCell(getHeadingCellLevel2("",3));
            table.addCell(getHeadingCellLevel2Center("Discounts",4));
            table.addCell(getHeadingCellLevel2Center("Other Costs",3));
            table.addCell(getHeadingCellLevel2Center("",1));

            
            //Query 3 : Outlet
            //
            
            
            
            table.addCell(getHeadingCellCol("",2));
            table.addCell(getHeadingCell("Gross Revenue"));
            table.addCell(getHeadingCellCenter("Sales Promotion"));
            table.addCell(getHeadingCellCenter("Upfront Discount"));
            table.addCell(getHeadingCellCenter("Retailer - Variable"));
            table.addCell(getHeadingCellCenter("Retailer - Fixed"));
            table.addCell(getHeadingCellCenter("Freight"));
            table.addCell(getHeadingCellCenter("Unloading"));
            table.addCell(getHeadingCellCenter("Haulage"));
            table.addCell(getHeadingCellCenter("Net Revenue"));
            
            double TotalGrossRevenue = 0;
			double TotalSalesPromotion = 0;
			double TotalUpFrontDiscount = 0;
			double TotalPerCaseDiscount = 0;
			double TotalFixedDiscount = 0;
			double TotalFreight = 0;
			double TotalUnloading = 0;
			double TotalHaulageDiscount = 0;
			double TotalCasesSold = 0;
			double TotalNetRevenue = 0;
			double TotalRate = 0;
			double TotalSecondaryCC = 0;
			double TotalSecondaryDiscount = 0;
			
			
			s.executeUpdate("CREATE temporary TABLE dist_net_revenue (distributor varchar(100) , net_revenue decimal(18,2), category_id smallint(2) )");
			
			
			
			
			String SQL = "SELECT customer_id, (SELECT name FROM common_distributors where distributor_id=customer_id) customer_name, sum(gross_value) gross_revenue, sum(upfront_discount) upfront_discount, sum(free_stock) sales_promotion, sum(freight) freight, sum(unloading) unloading, package_id, product_type_id FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate )+" "+WhereHOD+WhereRSM+WhereCustomerID+" and product_type_id in (1, 2, 3, 4) group by customer_id";
			//System.out.println(SQL);
			ResultSet rs = s.executeQuery(SQL);
			while(rs.next()){
				long CustomerID = rs.getLong("customer_id");
				
				
				
				double UpFrontDiscount = rs.getDouble("upfront_discount") * (-1);
				double Freight = rs.getDouble("freight") * (-1);
				double Unloading = rs.getDouble("unloading") * (-1);
				
				
				TotalGrossRevenue += rs.getDouble("gross_revenue");
				TotalSalesPromotion += rs.getDouble("sales_promotion");
				TotalUpFrontDiscount += UpFrontDiscount;
				
				
				//////////////////////////////////////////Cases Sold ////////////////////////////////////////////
								
				double CasesSold = 0;
				ResultSet rs6 = s3.executeQuery("select sum(bppi.quantity), sum(((bppi.quantity*ip.unit_per_case)*ip.liquid_in_ml)/ip.conversion_rate_in_ml) cc from peplogs.bi_percase_price_invoice bppi join inventory_packages ip on bppi.package_id = ip.id where customer_id = "+CustomerID+" and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
				if (rs6.first()){
					CasesSold = rs6.getDouble(2);
				}
				
				///////////////////////////////////////////////////////////////////////////////////////////////////////
				
				TotalCasesSold += CasesSold;
				
				
				
				
				
				table.addCell(getNormalCellCol(CustomerID+" - "+Utilities.truncateStringToMax(rs.getString("customer_name"), 22),2));
				if( rs.getDouble("gross_revenue") > 0 ) {
					table.addCell(getNormalCell1(Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("gross_revenue")/CasesSold)));
					
				}else{
					table.addCell(getNormalCell1(""));
				}
				if( rs.getDouble("sales_promotion") > 0 ){
					table.addCell(getNormalCell1(Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("sales_promotion")/CasesSold)));
				}else{
					table.addCell(getNormalCell1(""));
				}
				if( UpFrontDiscount > 0 ){
					table.addCell(getNormalCell1(Utilities.getDisplayCurrencyFormatRounded(UpFrontDiscount/CasesSold)));
				}else{
					table.addCell(getNormalCell1(""));
				}
				
				
//////////////////////////////////////////per case discount //////////////////////////////////////////
int PackageID = rs.getInt("package_id");
int TypeID = rs.getInt("product_type_id");
double GrossValue = 0;

Date CurrentDate = StartDate;


double SecondaryCC = 0;
ResultSet rs17 = s3.executeQuery("SELECT sum(isap.total_units*ipv.liquid_in_ml)/6000 FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isap.is_promotion = 0 and distributor_id = "+CustomerID);
if (rs17.first()){
	SecondaryCC += rs17.getDouble(1);
}
TotalSecondaryCC += SecondaryCC;
while(CurrentDate.before(Utilities.getDateByDays(EndDate,1))){
	
	ResultSet rs16 = s3.executeQuery("select sum(discount_value) from ( "+
			 " select package_id, brand_id, sum(qty*discounted) discount_value, (select ip.lrb_type_id from inventory_products ip where ip.category_id =1 and ip.package_id = tab1.package_id and ip.brand_id = tab1.brand_id) product_type_id from ( "+
					 " select isa.outlet_id, ipv.package_id, ipv.brand_id, sum(isap.raw_cases) qty, ( "+
					 " select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where date("+Utilities.getSQLDate(CurrentDate)+") between s.activated_on and s.deactivated_on and date("+Utilities.getSQLDate(CurrentDate)+") between sp.valid_from and sp.valid_to and sp.package = ipv.package_id  and sp.brand_id = ipv.brand_id and sp.brand_id != 0 and s.outlet_id = isa.outlet_id "+
					  " union all "+
					 " select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id join inventory_products ip on sp.package = ip.package_id and ip.category_id = 1 where date("+Utilities.getSQLDate(CurrentDate)+") between s.activated_on and s.deactivated_on and date("+Utilities.getSQLDate(CurrentDate)+") between sp.valid_from and sp.valid_to and sp.package = ipv.package_id and sp.brand_id = 0 and s.outlet_id = isa.outlet_id limit 1 "+
					  " ) discounted "+
					  " from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where 1=1 "+/*WhereHOD1+WhereRSM1+*/" and isa.distributor_id = "+CustomerID+" "+/*WhereGTMCategory1+*/" and isa.created_on between date("+Utilities.getSQLDate(CurrentDate)+") and date("+Utilities.getSQLDateNext(CurrentDate)+") and isap.is_promotion = 0 group by isa.outlet_id, ipv.package_id, ipv.brand_id having discounted is not null "+
					  " ) tab1 group by package_id, brand_id "+
					  " ) tab2 "/*where package_id = "+PackageID+" and product_type_id = "+TypeID*/);
	
	if(rs16.first()){
		GrossValue +=rs16.getDouble(1);
	}
	
	CurrentDate = Utilities.getDateByDays(CurrentDate,1);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////

TotalSecondaryDiscount += GrossValue;

double PerCaseDiscount = 0;
if (SecondaryCC != 0){
	PerCaseDiscount = (GrossValue/SecondaryCC);
}


//TotalPerCaseDiscount += PerCaseDiscount;
				
if(  PerCaseDiscount != 0 ){
	table.addCell(getNormalCell1(Utilities.getDisplayCurrencyFormatRounded(PerCaseDiscount)));		
}else{
	table.addCell(getNormalCell1(""));
}


//////////////////////////////////////////fixed discount /////////////////////////////////////////////

double FixedDiscount = 0;
double GrossValue2 = 0;
CurrentDate = StartDate;

while(CurrentDate.before(Utilities.getDateByDays(EndDate,1))){
	
	ResultSet rs16 = s3.executeQuery("select sum(fixed_company_share)/30 from ( "+
			" SELECT fixed_company_share, (select distributor_id from common_outlets_distributors_view where outlet_id = co.id limit 1) distributor_id FROM sampling s join common_outlets co on s.outlet_id = co.id where s.active = 1 and "+Utilities.getSQLDate(CurrentDate)+" between s.fixed_valid_from and s.fixed_valid_to and s.fixed_company_share != 0  "+
			" ) tab1 where distributor_id =  "+CustomerID/* where 1=1 "+WhereHOD2+WhereRSM2+WhereCustomerID2+WhereGTMCategory2*/);
	
	if(rs16.first()){
		GrossValue2 +=rs16.getDouble(1);
	}
	
	
	
	CurrentDate = Utilities.getDateByDays(CurrentDate,1);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////
FixedDiscount = GrossValue2;
double FixedDiscountRate = 0;
if (SecondaryCC != 0){
	FixedDiscountRate = FixedDiscount/SecondaryCC;
}


TotalFixedDiscount += FixedDiscount;
TotalFreight += Freight;
TotalUnloading += Unloading;
				
if( FixedDiscountRate != 0 ){
	 table.addCell(getNormalCell1(Utilities.getDisplayCurrencyFormatRounded(FixedDiscountRate))); 
 }else{
	 table.addCell(getNormalCell1("")); 
 }
 
  if( Freight > 0 ){
	  table.addCell(getNormalCell1(Utilities.getDisplayCurrencyFormatRounded(Freight/CasesSold)));
  }else{
	  table.addCell(getNormalCell1(""));
  }
	
  if( Unloading > 0 ){
	  table.addCell(getNormalCell1(Utilities.getDisplayCurrencyFormatRounded(Unloading/CasesSold))); 
  }else{
	  table.addCell(getNormalCell1(""));
  }
  
//////////////////////////////////////////haulage discount ////////////////////////////////////////////
	
double HaulageDiscount = 0;
ResultSet rs72 = s3.executeQuery("SELECT sum(freight_amount) FROM inventory_delivery_note where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+/*WhereHOD2+WhereRSM2+*/" and distributor_id = "+CustomerID+" "/*+WhereGTMCategory3*/);
if(rs72.first()){
HaulageDiscount = rs72.getDouble(1);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////

TotalHaulageDiscount += HaulageDiscount;



if( HaulageDiscount > 0 ){
	table.addCell(getNormalCell1(Utilities.getDisplayCurrencyFormatRounded(HaulageDiscount/CasesSold)));
}else{
	table.addCell(getNormalCell1(""));
}


double NetRevenue = 0;
NetRevenue = rs.getDouble("gross_revenue") - (rs.getDouble("sales_promotion")+(rs.getDouble("upfront_discount")*-1)+(rs.getDouble("freight")*-1)+(rs.getDouble("unloading")*-1)+HaulageDiscount);
TotalNetRevenue += NetRevenue;

double Rate = (NetRevenue / CasesSold)-PerCaseDiscount-FixedDiscountRate;

if( Rate != 0 ){
	table.addCell(getNormalCell1(Utilities.getDisplayCurrencyFormatRounded(Rate)));
	s3.executeUpdate("insert into dist_net_revenue values('"+CustomerID+" - "+Utilities.truncateStringToMax(rs.getString("customer_name"), 22)+"',"+Rate+", (select category_id from common_distributors where distributor_id = "+CustomerID+" limit 1))");
}else{
	table.addCell(getNormalCell1(""));
}
		}
			
			int i=0;
			ResultSet rs3 = s3.executeQuery("select * from dist_net_revenue where category_id = 5 order by net_revenue asc limit 5");
			while(rs3.next()){
				BOTTOM_LINE_DISTRIBUTORS[i]=rs3.getString("distributor");
				BOTTOM_LINE_DISTRIBUTORS_REVENUE[i]=rs3.getDouble("net_revenue");
				i++;
			}
			
			
			
			
			double TotalSecondaryRate = 0;
			if (TotalSecondaryCC != 0){
				TotalSecondaryRate = TotalSecondaryDiscount / TotalSecondaryCC;
			}
			
			double TotalFixedRate = 0;
			if (TotalSecondaryCC != 0){
				TotalFixedRate = TotalFixedDiscount / TotalSecondaryCC;
			}
			
			double AllNet = 0;
			if (TotalCasesSold != 0){
				AllNet = (TotalNetRevenue/TotalCasesSold) - TotalSecondaryRate - TotalFixedRate;
			}
            
			
			table.addCell(getNormalCell2("Total",2));
			table.addCell(getNormalCell1(Utilities.getDisplayCurrencyFormatRounded(TotalGrossRevenue/TotalCasesSold)));
			table.addCell(getNormalCell1(Utilities.getDisplayCurrencyFormatRounded(TotalSalesPromotion/TotalCasesSold)));
			table.addCell(getNormalCell1(Utilities.getDisplayCurrencyFormatRounded(TotalUpFrontDiscount/TotalCasesSold)));
			table.addCell(getNormalCell1(Utilities.getDisplayCurrencyFormatRounded(TotalSecondaryRate)));
			table.addCell(getNormalCell1(Utilities.getDisplayCurrencyFormatRounded(TotalFixedRate)));
			table.addCell(getNormalCell1(Utilities.getDisplayCurrencyFormatRounded(TotalFreight/TotalCasesSold)));
			table.addCell(getNormalCell1(Utilities.getDisplayCurrencyFormatRounded(TotalUnloading/TotalCasesSold)));
			table.addCell(getNormalCell1(Utilities.getDisplayCurrencyFormatRounded(TotalHaulageDiscount/TotalCasesSold)));
			table.addCell(getNormalCell1(Utilities.getDisplayCurrencyFormatRounded(AllNet)));
			
            
            
           
		
		
		
		
        
        Paragraph header = new Paragraph("Period: "+Utilities.getDisplayDateFormat(StartDate1)+" - "+Utilities.getDisplayDateFormat(EndDate1),fontheading);
        header.setAlignment(Element.ALIGN_RIGHT);
        
        Image img = Image.getInstance(String.format(Utilities.getImageResoucesPath()+"/"+"%s", "pepsi28.png"));
        img.scaleToFit(20, 20);
        //img.setTransparency(new int[]{ 0xF0, 0xFF });
        img.setAbsolutePosition(35, 788);
        
        document.add(img);
        Paragraph pbc = new Paragraph("          Punjab Beverages",fontpbc);
        document.add(pbc);
        
        
       // Paragraph Heading = new Paragraph("Revenue Per Case",fontpbc);
       // Heading.setAlignment(Element.ALIGN_CENTER);
	     //document.add(Heading);
	    
        
	    document.add(header);
	    document.add(Chunk.NEWLINE);
	    
	    
	    
	    
	    document.add(table);
	    document.add(Chunk.NEWLINE);
	    
	    //document.add(new Paragraph("Revenue/Chiller/Day = Average revenue per chiller per day in last 30 days",fontheading));
	    
		
	    
	    
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
private PdfPCell getHeadingCellCol(String title, int colspan){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(222, 222, 222));
        pcell.setMinimumHeight(20);
        pcell.setColspan(colspan);
        return pcell;
	}
	
	private PdfPCell getHeadingCellCenter(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
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
        pcell.setMinimumHeight(20);
        return pcell;
	}
	private PdfPCell getNormalCell1(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setMinimumHeight(20);
        return pcell;
	}
	private PdfPCell getNormalCell2(String title,int colspan){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setMinimumHeight(20);
        pcell.setColspan(colspan);
        return pcell;
	}
	
private PdfPCell getNormalCellCol(String title, int colspan){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setMinimumHeight(20);
        pcell.setColspan(colspan);
        return pcell;
	}
	
	private PdfPCell getNormalCellCenter(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setMinimumHeight(15);
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
	
	private PdfPCell getHeadingCellLevel2Center(String title, int colspan){	
	    PdfPCell pcell2 = new PdfPCell(new Phrase(title, reportheading));
	    pcell2.setBackgroundColor(BaseColor.DARK_GRAY);
	    pcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
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
