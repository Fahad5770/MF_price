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

public class PerCaseAnalysisPackagePDF {

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
	
	
	public double SALES_PROMOTION = 0;
	public double UPFRONT_DISCOUNT = 0;
	public double RETAILER_VARIABLE = 0;
	public double RETAILER_FIXED = 0;
	
	
	public static void main(String[] args) throws DocumentException, IOException, ParseException{
		
		try {
			
			//Date StartDate2 = new Date();
			//Date EndDate2 = new Date();
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
			String dateInString = "01-07-2015";
			Date StartDate2 = sdf.parse(dateInString);
			
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-M-yyyy");
			String dateInString1 = "07-09-2015";
			Date EndDate2 = sdf1.parse(dateInString1);
			
			
			//new DistributorScoreCardPDF().createPdf(RESULT, this.SND_ID);
			PerCaseAnalysisPackagePDF DistributorSD1 = new PerCaseAnalysisPackagePDF();
			DistributorSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/test"+Math.random()+".pdf", 2262,StartDate2,EndDate2);
			
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
	
	public PerCaseAnalysisPackagePDF() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		
		
		
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
		
		
		String WhereHOD = "";
		String WhereHOD1 = "";
		String WhereHOD2 = "";
		String WhereHOD3 = "";

		WhereHOD = " and customer_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+SND_ID+")) ";	
		WhereHOD1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+SND_ID+")) ";	
		WhereHOD2 = " and tab1.distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+SND_ID+")) ";
		WhereHOD3 = " and distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+SND_ID+")) ";

		if (SND_ID == -1){
			WhereHOD = " and customer_id in(SELECT distributor_id FROM common_distributors where snd_id in (SELECT snd_id FROM pep.common_sd_groups where snd_id is not null)) ";	
			WhereHOD1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in (SELECT snd_id FROM pep.common_sd_groups where snd_id is not null)) ";	
			WhereHOD2 = " and tab1.distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in (SELECT snd_id FROM pep.common_sd_groups where snd_id is not null)) ";
			WhereHOD3 = " and distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in (SELECT snd_id FROM pep.common_sd_groups where snd_id is not null)) ";
		}
		
		
		Document document = new Document(PageSize.A3.rotate());
	    PdfWriter.getInstance(document, new FileOutputStream(filename));
	    
	    document.open();
	    int rowcount=0;
	    
	    ResultSet rs = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice  group by product_type_id order by product_type_id");
		while(rs.next()){
			rowcount+=rs.getInt("packge_count");
			
		}
	    
	   // System.out.println(rowcount+2);
	    PdfPTable table = new PdfPTable(rowcount+2); //+2 for 2 extra col
	    
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
	    
	    
	    
      
        
	    table.addCell(getHeadingCellLevel2("",1)); //For heading 
		ResultSet rs1 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice  group by product_type_id order by product_type_id");
		while(rs1.next()){
			table.addCell(getHeadingCellLevel2Center(rs1.getString("type_name"),rs1.getInt("packge_count")));
		}
		int ArrayCount=0;
		
		table.addCell(getHeadingCellLevel2("",1)); //For heading 
		
		 table.addCell(getHeadingCell(""));
		
		ResultSet rs2 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice  group by product_type_id order by product_type_id");
		while(rs2.next()){
			int TypeID=rs2.getInt("product_type_id");
			
			ResultSet rs3 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID);
			while(rs3.next()){
				
				 table.addCell(getHeadingCellCenter(rs3.getString("package_label")));
		
		ArrayCount++;
			}
		}
		table.addCell(getHeadingCellCenter("Total"));
		
		
		
		//System.out.println(ArrayCount);
		
		double GrossRevenueArray[] = new double [ArrayCount];
		double SalesPromotionArray[] = new double [ArrayCount];
		double UpfrontDiscountArray[] = new double [ArrayCount];
		double PerCaseDiscount[] = new double [ArrayCount];
		double FixedDiscountArray[] = new double [ArrayCount];
		double FreightArray[] = new double [ArrayCount];
		double UnloadingArray[] = new double [ArrayCount];
		double CasesSoldArray[] = new double [ArrayCount];
		double CasesSoldArray1[] = new double [ArrayCount];
		double NetRevenueArray[] = new double [ArrayCount];
            
            
		int lm1=0;
    	double CasesSoldConverted1=0;
		ResultSet rs101 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1  group by product_type_id order by product_type_id");
		while(rs101.next()){
			int TypeID = rs101.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				double CCGross = 0;
				ResultSet rs5 = s3.executeQuery("select sum(bppi.quantity), sum(((bppi.quantity*ip.unit_per_case)*ip.liquid_in_ml)/ip.conversion_rate_in_ml) from peplogs.bi_percase_price_invoice bppi join inventory_packages ip on bppi.package_id = ip.id where 1=1 "+WhereHOD+" and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and bppi.package_id="+PackageID+" and bppi.product_type_id="+TypeID);
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
					CCGross = rs5.getDouble(2);
				}
				CasesSoldArray1[lm1]=GrossValue;
				CasesSoldConverted1+=CCGross;
		
			lm1++;
			}
		}
		
		
		table.addCell(getNormalCell4("Gross Revenue"));
		
		double ConvertedGrossRevenue=0;
    	double ConvertedGrossRevenue1=0;
    	int i=0;
		ResultSet rs3 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1  group by product_type_id order by product_type_id");
		while(rs3.next()){
			int TypeID = rs3.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				double GrossValue1 = 0;
				ResultSet rs5 = s3.executeQuery("select sum(gross_value) from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+" "+WhereHOD+" and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
				}
				
				GrossRevenueArray[i]=GrossValue;
				ConvertedGrossRevenue += GrossValue;
				

				if(CasesSoldArray1[i]!=0){
					GrossValue1 = GrossValue/CasesSoldArray1[i];
				}
		
			if(GrossValue1!=0){
				table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(GrossValue1))); 
				}else{
					table.addCell(getNormalCell("")); 
				}
		
			i++;
			}
		}
		
		if(CasesSoldConverted1!=0){
			ConvertedGrossRevenue1 = ConvertedGrossRevenue/CasesSoldConverted1;
		}
		
		if(ConvertedGrossRevenue1!=0){
			table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatOneDecimal(ConvertedGrossRevenue1))); 
			}else{
				table.addCell(getNormalCell("")); 
			} 
		
		 
		table.addCell(getNormalCell2("Discounts",rowcount+2));
		
		
		table.addCell(getNormalCell4("Sales Promotion"));
		
		
		double ConvertedSalesPromotion=0;
    	double ConvertedSalesPromotion1=0;
    	int j=0;
		ResultSet rs6 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1  group by product_type_id order by product_type_id");
		while(rs6.next()){
			int TypeID = rs6.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				double GrossValue1 = 0;
				ResultSet rs5 = s3.executeQuery("select sum(free_stock) from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+" "+WhereHOD+" and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
				}
				SalesPromotionArray[j]=GrossValue;
				ConvertedSalesPromotion+=GrossValue;
				
				if(CasesSoldArray1[j]!=0){
					GrossValue1 = GrossValue/CasesSoldArray1[j];
				}
		
			if(GrossValue1!=0){
				table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(GrossValue1)));  
				}else{
					table.addCell(getNormalCell("")); 
				}
		
			j++;
			}
		}
		
		if(CasesSoldConverted1!=0){
			ConvertedSalesPromotion1 = ConvertedSalesPromotion/CasesSoldConverted1;
		}
		
		
    	if(ConvertedSalesPromotion1!=0){
    		table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatOneDecimal(ConvertedSalesPromotion1)));
    		this.SALES_PROMOTION = ConvertedSalesPromotion1;
    		}else{
    			table.addCell(getNormalCell(""));
    		}
		
		
    	table.addCell(getNormalCell4("Upfront Discount"));
    	
    	int jj=0;
    	double ConvertedUpfront=0;
    	double ConvertedUpfront1=0;
		ResultSet rs7 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1  group by product_type_id order by product_type_id");
		while(rs7.next()){
			int TypeID = rs7.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				double GrossValue1 = 0;
				ResultSet rs5 = s3.executeQuery("select sum(upfront_discount)*-1 from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+" "+WhereHOD+" and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
				}
				UpfrontDiscountArray[jj]=GrossValue;
				ConvertedUpfront+=GrossValue;
				
				if(CasesSoldArray1[jj]!=0){
					GrossValue1 = GrossValue/CasesSoldArray1[jj];
				}
		
			if(GrossValue1!=0){
				table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(GrossValue1))); 
				}else{
					table.addCell(getNormalCell("")); 
				}
		
			jj++;
			}
		}
		
		
		if(CasesSoldConverted1!=0){
			ConvertedUpfront1 = ConvertedUpfront/CasesSoldConverted1;
		}
		
		
    	if(ConvertedUpfront1!=0){
    		table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatOneDecimal(ConvertedUpfront1)));
    		this.UPFRONT_DISCOUNT = ConvertedUpfront1;
    		}else{
    			table.addCell(getNormalCell("")); 
    		}
    	
    	
    	table.addCell(getNormalCell4("Retailer - Variable"));
    	
    	
    	int kkk=0;
    	double PerCaseDiscountConverted=0;
    	double PerCaseDiscountConverted1=0;
    	double SSalesConverted = 0;
    	double PerCaseConvertedExpense = 0;
		ResultSet rs81 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1  group by product_type_id order by product_type_id");
		while(rs81.next()){
			int TypeID = rs81.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				
				
				
				double SSalesPack = 0;
				
				ResultSet rs17 = s3.executeQuery("SELECT sum(isap.total_units)/ipv.unit_per_sku, sum(isap.total_units*ipv.liquid_in_ml)/6000 FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and ipv.package_id = "+PackageID+" and ipv.lrb_type_id = "+TypeID+" and isap.is_promotion = 0 "+WhereHOD3);
				if (rs17.first()){
					SSalesPack = rs17.getDouble(1);
					SSalesConverted += rs17.getDouble(2);
				}
				
				Date CurrentDate = StartDate;
				double PerCaseSecondary = 0;
				double PerCasePrimary = 0;
				
				while(CurrentDate.before(Utilities.getDateByDays(EndDate,1))){
					//System.out.println("Hello "+Utilities.getDisplayDateFormat(CurrentDate));
					
					
					ResultSet rs16 = s3.executeQuery("select sum(discount_value) from ( "+
							 " select package_id, brand_id, sum(qty*discounted) discount_value, (select ip.lrb_type_id from inventory_products ip where ip.category_id =1 and ip.package_id = tab1.package_id and ip.brand_id = tab1.brand_id) product_type_id from ( "+
									 " select isa.outlet_id, ipv.package_id, ipv.brand_id, sum(isap.raw_cases) qty, ( "+
									 " select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where date("+Utilities.getSQLDate(CurrentDate)+") between s.activated_on and s.deactivated_on and date("+Utilities.getSQLDate(CurrentDate)+") between sp.valid_from and sp.valid_to and sp.package = ipv.package_id  and sp.brand_id = ipv.brand_id and sp.brand_id != 0 and s.outlet_id = isa.outlet_id "+
									  " union all "+
									 " select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id join inventory_products ip on sp.package = ip.package_id and ip.category_id = 1 where date("+Utilities.getSQLDate(CurrentDate)+") between s.activated_on and s.deactivated_on and date("+Utilities.getSQLDate(CurrentDate)+") between sp.valid_from and sp.valid_to and sp.package = ipv.package_id and sp.brand_id = 0 and s.outlet_id = isa.outlet_id limit 1 "+
									  " ) discounted "+
									  " from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where 1=1 "+WhereHOD1+" and isa.created_on between date("+Utilities.getSQLDate(CurrentDate)+") and date("+Utilities.getSQLDateNext(CurrentDate)+") and isap.is_promotion = 0 group by isa.outlet_id, ipv.package_id, ipv.brand_id having discounted is not null "+
									  " ) tab1 group by package_id, brand_id "+
									  " ) tab2 where package_id = "+PackageID+" and product_type_id = "+TypeID);
					
					if(rs16.first()){
						PerCaseSecondary +=rs16.getDouble(1);
					}
					
					
					
					CurrentDate = Utilities.getDateByDays(CurrentDate,1);
				}
				
				
				
				PerCasePrimary = PerCaseSecondary;
				
				if (CasesSoldArray1[kkk] != 0){
					if (SSalesPack != 0){
						PerCasePrimary = (PerCasePrimary/SSalesPack)*CasesSoldArray1[kkk];
					}else{
						PerCasePrimary = 0;
					}
				}else{
					PerCasePrimary = 0;
				}
				
				PerCaseDiscount[kkk]=PerCasePrimary;
				PerCaseDiscountConverted+=PerCaseSecondary;
				
				//if(CasesSoldArray1[kkk]!=0){
					//GrossValue1 = GrossValue/CasesSoldArray1[kkk];
					//GrossValue1 = GrossValue/SSalesPack;
				//}
				
				double PerCaseDiscountRate = 0;
				
				if (CasesSoldArray1[kkk] != 0){
					PerCaseDiscountRate = PerCaseSecondary/SSalesPack;
				}
    	
    	
				
				if(PerCaseDiscountRate!=0){
					table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(PerCaseDiscountRate)));
				}else{
					table.addCell(getNormalCell(""));
				}
				
				
		
			kkk++;
			}
		}
		
		if (CasesSoldConverted1 != 0){
			PerCaseDiscountConverted1 = (PerCaseDiscountConverted/SSalesConverted);
		}
		
		
    	
    	
    	if(PerCaseDiscountConverted1!=0){
    		table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatOneDecimal(PerCaseDiscountConverted1))); 
    		this.RETAILER_VARIABLE = PerCaseDiscountConverted1;
    		}else{
				table.addCell(getNormalCell("")); 
			}
    	
        
    	table.addCell(getNormalCell4("Retailer - Fixed"));
    	
    	int jjlm=0;
    	double ConvertedFixed=0;
    	double ConvertedFixed1=0;
		ResultSet rs712 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1  group by product_type_id order by product_type_id");
		while(rs712.next()){
			int TypeID = rs712.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				double GrossValue1 = 0;
				ResultSet rs5 = s3.executeQuery("select sum(upfront_discount)*-1 from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+WhereHOD+"  and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
				}
				
				
				
		
			jjlm++;
			}
		}
		
		
		Date CurrentDate = StartDate;
		
		while(CurrentDate.before(Utilities.getDateByDays(EndDate,1))){
			//System.out.println("Hello "+Utilities.getDisplayDateFormat(CurrentDate));
			
			ResultSet rs16 = s3.executeQuery("select sum(fixed_company_share)/30 from ( "+
					" SELECT fixed_company_share, (select distributor_id from common_outlets_distributors_view where outlet_id = co.id limit 1) distributor_id FROM sampling s join common_outlets co on s.outlet_id = co.id where s.active = 1 and "+Utilities.getSQLDate(CurrentDate)+" between s.fixed_valid_from and s.fixed_valid_to and s.fixed_company_share != 0 "+
					" ) tab1 where 1=1 "+WhereHOD2);
			
			if(rs16.first()){
				ConvertedFixed +=rs16.getDouble(1);
			}
			
			
			
			CurrentDate = Utilities.getDateByDays(CurrentDate,1);
		}
		
		//System.out.println("Helllllo - "+ConvertedFixed);
		
		
		
		
		
		//System.out.println("Helllllo - "+ConvertedFixed+" - "+CasesSoldConverted1);
		table.addCell(getNormalCell3("",rowcount)); //adding space
		
		if(SSalesConverted!=0){
			ConvertedFixed1 = ConvertedFixed/SSalesConverted;
		}
		
		//System.out.println("Hello "+ConvertedFixed1);
		
		
		
    	if(ConvertedFixed1!=0){
    		table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatOneDecimal(ConvertedFixed1)));
    		this.RETAILER_FIXED = ConvertedFixed1;
    		}else{
    			table.addCell(getNormalCell(""));
    		}
    	
    	
    	table.addCell(getNormalCell2("Other Costs",rowcount+2));
    	
    	table.addCell(getNormalCell4("Freight"));
    	
    	int kk=0;
    	double FreightConverted=0;
    	double FreightConverted1=0;
		ResultSet rs8 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1  group by product_type_id order by product_type_id");
		while(rs8.next()){
			int TypeID = rs8.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				double GrossValue1 = 0;
				ResultSet rs5 = s3.executeQuery("select sum(freight)*-1 from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+" "+WhereHOD+" and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
				}
				FreightArray[kk]=GrossValue;
				
				FreightConverted+=GrossValue;
				
				if(CasesSoldArray1[kk]!=0){
					GrossValue1 = GrossValue/CasesSoldArray1[kk];
				}
		
			if(GrossValue1!=0){
				table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(GrossValue1)));
				}else{
					table.addCell(getNormalCell(""));
				}
		
			kk++;
			}
		}
		
		if(CasesSoldConverted1!=0){
			FreightConverted1=FreightConverted/CasesSoldConverted1;
		}
		
    	
    	if(FreightConverted1!=0){
    		table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatOneDecimal(FreightConverted1)));
    		}else{
    			table.addCell(getNormalCell(""));
    		}
    	
    	
    	table.addCell(getNormalCell4("Unloading"));
    	
    	
    	int ll=0;
    	double UnloadingConverted=0;
    	double UnloadingConverted1=0;
		ResultSet rs9 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1  group by product_type_id order by product_type_id");
		while(rs9.next()){
			int TypeID = rs9.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				double GrossValue1 = 0;
				ResultSet rs5 = s3.executeQuery("select sum(unloading)*-1 from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+" "+WhereHOD+" and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
				}
				UnloadingArray[ll]=GrossValue;
						
				UnloadingConverted+=GrossValue;
				
				if(CasesSoldArray1[ll]!=0){
					GrossValue1 = GrossValue/CasesSoldArray1[ll];
				}
		if(GrossValue1!=0){
			table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(GrossValue1)));
			}else{
				table.addCell(getNormalCell(""));
			}
		
			ll++;
			}
		}
		
		if(CasesSoldConverted1!=0){
			UnloadingConverted1=UnloadingConverted/CasesSoldConverted1;
		}
		
    	
    	if(UnloadingConverted1!=0){
    		table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatOneDecimal(UnloadingConverted1)));
    		}else{
    			table.addCell(getNormalCell(("")));
    		}
    	
    	
    	
    	table.addCell(getNormalCell4("Haulage Discount"));
    	
    	int jjl=0;
    	double ConvertedHaulase=0;
    	double ConvertedHaulase1=0;
		ResultSet rs71 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1  group by product_type_id order by product_type_id");
		while(rs71.next()){
			int TypeID = rs71.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				double GrossValue1 = 0;
				ResultSet rs5 = s3.executeQuery("select sum(upfront_discount)*-1 from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+" "+WhereHOD+" and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
				}
				UpfrontDiscountArray[jjl]=GrossValue;
				ConvertedUpfront+=GrossValue;
			
		
			jjl++;
			}
		}
		
		
		
		
		ResultSet rs72 = s.executeQuery("SELECT sum(freight_amount) FROM pep.inventory_delivery_note where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereHOD3);
		if(rs72.first()){
			ConvertedHaulase = rs72.getDouble(1);
		}
		
		table.addCell(getNormalCell3("",rowcount)); //adding space
		if(CasesSoldConverted1!=0){
			ConvertedHaulase1 = ConvertedHaulase/CasesSoldConverted1;
		}
		
		//System.out.println("Hello "+ConvertedFixed1);
		
		
		if(ConvertedHaulase1!=0){
			table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatOneDecimal(ConvertedHaulase1)));
			}else{
				table.addCell(getNormalCell(""));
			}
    	
    	
		
		table.addCell(getNormalCell4("Cases Sold"));
		int lm=0;
    	double CasesSoldConverted=0;
		ResultSet rs10 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1  group by product_type_id order by product_type_id");
		while(rs10.next()){
			int TypeID = rs10.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				double CCGross = 0;
				ResultSet rs5 = s3.executeQuery("select sum(bppi.quantity), sum(((bppi.quantity*ip.unit_per_case)*ip.liquid_in_ml)/ip.conversion_rate_in_ml) from peplogs.bi_percase_price_invoice bppi join inventory_packages ip on bppi.package_id = ip.id where 1=1 "+WhereHOD+" and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and bppi.package_id="+PackageID+" and bppi.product_type_id="+TypeID);
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
					CCGross = rs5.getDouble(2);
				}
				CasesSoldArray[lm]=GrossValue;
				CasesSoldConverted+=CCGross;
		
			if(GrossValue!=0){
				table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(GrossValue)));
				}else{
					table.addCell(getNormalCell(""));
				}
			
		
			lm++;
			}
		}
		
		
    	
    	if(CasesSoldConverted!=0){
    		table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(CasesSoldConverted)));
    		}else{
    			table.addCell(getNormalCell(""));
    		}
    	
    	
    	table.addCell(getNormalCell4("Net Revenue"));
    	
    	int km=0;
    	
    	double NetRevenueConvert=0;
    	double NetRevenueConvert1=0;
    	for(int i1=0;i1<ArrayCount;i1++){
    		double NetRevenue=0;
    		double NetRevenue1=0;
    		//System.out.println("rev:"+PerCaseDiscount[i1]);
    		NetRevenue = GrossRevenueArray[i1] - (SalesPromotionArray[i1]+UpfrontDiscountArray[i1]+FreightArray[i1]+UnloadingArray[i1]+PerCaseDiscount[i1]);
    	
				
    		NetRevenueArray[km]=NetRevenue;	
    		NetRevenueConvert+=(NetRevenue+PerCaseDiscount[i1]);
    		
    		if(CasesSoldArray1[km]!=0){
    			NetRevenue1 = NetRevenue/CasesSoldArray1[km];
    			//NetRevenue1 = NetRevenue;
			}
		
			if(NetRevenue1!=0){
				table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(NetRevenue1)));
				}else{
					table.addCell(getNormalCell(""));
				}
		
		km++;
    	}	
    	NetRevenueConvert = NetRevenueConvert - (ConvertedHaulase);
    	
    	if(CasesSoldConverted1!=0){
			NetRevenueConvert1=NetRevenueConvert/CasesSoldConverted1;
		}
    	NetRevenueConvert1 -= (PerCaseDiscountConverted1 + ConvertedFixed1);
		
    	
		
    	
    	
    	
    	if(NetRevenueConvert1!=0){
    		table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatOneDecimal(NetRevenueConvert1)));
    		}else{
    			table.addCell(getNormalCell(""));
    		}
    	
    	
        Paragraph header = new Paragraph("Period: "+Utilities.getDisplayDateFormat(StartDate1)+" - "+Utilities.getDisplayDateFormat(EndDate1),fontheading);
        header.setAlignment(Element.ALIGN_RIGHT);
        
        Image img = Image.getInstance(String.format(Utilities.getImageResoucesPath()+"/"+"%s", "pepsi28.png"));
        img.scaleToFit(20, 20);
        //img.setTransparency(new int[]{ 0xF0, 0xFF });
        img.setAbsolutePosition(35, 788);
        
        document.add(img);
        Paragraph pbc = new Paragraph("          Punjab Beverages",fontpbc);
        document.add(pbc);
        
        
       // Paragraph Heading = new Paragraph("PerCase Cost Package",fontpbc);
        //Heading.setAlignment(Element.ALIGN_CENTER);
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
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setMinimumHeight(15);
        return pcell;
	}
	
	private PdfPCell getNormalCell2(String title, int colspan){	
		 PdfPCell pcell = new PdfPCell(new Phrase(title,fonttableheader));
	     pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
	     pcell.setBorderColor(BaseColor.LIGHT_GRAY);
	    
	     pcell.setColspan(colspan);
	     pcell.setMinimumHeight(15);
	    return pcell;
	}
	
	private PdfPCell getNormalCell3(String title, int colspan){	
		 PdfPCell pcell = new PdfPCell(new Phrase(title,fonttableheader));
	     pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	     pcell.setBorderColor(BaseColor.LIGHT_GRAY);	    
	     pcell.setColspan(colspan);
	     pcell.setMinimumHeight(15);
	    return pcell;
	}
	
	private PdfPCell getNormalCell4(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setMinimumHeight(15);
        return pcell;
	}
	
	
	private PdfPCell getNormalCellCenter(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        return pcell;
	}
	
	
	private PdfPCell getNormalCellCenter2(String title, int colspan){	
		 PdfPCell pcell = new PdfPCell(new Phrase(title,fonttableheader));
		 pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
	     pcell.setBorderColor(BaseColor.LIGHT_GRAY);
	     pcell.setBackgroundColor(new BaseColor(222, 222, 200));
	     pcell.setColspan(colspan);
	     pcell.setMinimumHeight(15);
	    return pcell;
	}
	
	private PdfPCell getNormalCellCenter3(String title, int colspan){	
		 PdfPCell pcell = new PdfPCell(new Phrase(title,fonttableheader));
		 pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
	     pcell.setBorderColor(BaseColor.LIGHT_GRAY);	    
	     pcell.setColspan(colspan);
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
