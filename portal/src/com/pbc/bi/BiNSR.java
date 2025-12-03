package com.pbc.bi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import com.pbc.common.Distributor;
import com.pbc.util.Datasource;
import com.pbc.util.UserAccess;
import com.pbc.util.Utilities;

public class BiNSR {
	
	public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
	
	public static void main(String args[]) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		
		Datasource ds = new Datasource();
		ds.createConnectionToReplica();
		Statement s = ds.createStatement();
		
		BiNSR bi = new BiNSR();
		
		Date dates[] = Utilities.getPastMonthsInDate(Utilities.getDateByDays(-1), 2);
		
		for (int i = 0; i < dates.length; i++){
			
			Date iStartDate = removeTime(dates[i]);
			
			
			Date iEndDate = removeTime(dates[i]);
						
			if (i == (dates.length-1)){
				iEndDate = removeTime(Utilities.getDateByDays(-1));
			}else{
				iEndDate = removeTime(Utilities.getEndDateByDate(dates[i]));
			}
			
			System.out.println(iStartDate + " " +iEndDate);
			
			bi.NSR(iStartDate,iEndDate, false, false);
			bi.NSR(iStartDate,iEndDate, true, false);
			bi.NSR(iStartDate,iEndDate, false, true);
			
			/*
			ResultSet rs5 = s.executeQuery("select distinct customer_id from "+ds.logDatabaseName()+".bi_percase_price_invoice where kurrf_dat between "+Utilities.getSQLDate(iStartDate)+" and "+Utilities.getSQLDate(iEndDate));
			while (rs5.next()){
				bi.NSRDistributors(iStartDate, iEndDate, rs5.getLong(1));
			}
			*/
			//break;
		}
		
		
		
		
		s.close();
		ds.dropConnection();
		
	}
	
	public static void NSR(Date StartDate, Date EndDate, boolean isFaisalabad, boolean isROF) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		
		
		int month = Utilities.getMonthNumberByDate(StartDate);
		int year = Utilities.getYearByDate(StartDate);
		
		
		int i_isFaisalabad = 0;
		int i_isROF = 0;
		int i_isFranchise = 1;
		
		
		long UniqueSessionID = 0;

		long SessionUserID = 0;
		int FeatureID = 225;

		Datasource dsi = new Datasource();
		dsi.createConnection();
		Statement s5 = dsi.createStatement();
		
		Datasource ds = new Datasource();
		ds.createConnectionToReplica();
		Connection c = ds.getConnection();
		Statement s = c.createStatement();
		Statement s2 = c.createStatement();
		Statement s3 = c.createStatement();
		Statement s4 = c.createStatement();
		

//		Date StartDate = Utilities.parseDate("02/09/2016"); //(Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
//		Date EndDate = Utilities.parseDate("02/09/2016");//new Date();//(Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");
		
		int SelectedMonth = Utilities.getMonthNumberByDate(StartDate);
		int SelectedYear = Utilities.getYearByDate(StartDate);
		
		long SelectedPackagesArray[] = null;
		
		String PackageIDs = "";
		String WherePackage = "";
		
		if(SelectedPackagesArray!= null && SelectedPackagesArray.length > 0){
			for(int i = 0; i < SelectedPackagesArray.length; i++){
				if(i == 0){
					PackageIDs += SelectedPackagesArray[i]+"";
				}else{
					PackageIDs += ", "+SelectedPackagesArray[i]+"";
				}
			}
			WherePackage = " and package_id in ("+PackageIDs+") ";
		}
		
		//HOD
		
		
		String HODIDs="";
		long SelectedHODArray[] = null;
		
		String WhereHOD = "";
		String WhereHOD1 = "";
		String WhereHOD2 = "";
		String WhereHOD3 = "";
		if (HODIDs.length() > 0){
			WhereHOD = " and customer_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
			WhereHOD1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";
			WhereHOD2 = " and distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";
			WhereHOD3 = " and ipprd.distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";
		}
		if (isFaisalabad){
			i_isFaisalabad = 1;
			i_isROF = 0;
			i_isFranchise = 0;
			
			WhereHOD = " and customer_id in(SELECT distributor_id FROM common_distributors where region_id in (5,11)) ";	
			WhereHOD1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where region_id in (5,11)) ";
			WhereHOD2 = " and distributor_id in(SELECT distributor_id FROM common_distributors where region_id in (5,11)) ";
			WhereHOD3 = " and ipprd.distributor_id in(SELECT distributor_id FROM common_distributors where region_id in (5,11)) ";
		}
		if (isROF){
			i_isFaisalabad = 0;
			i_isROF = 1;
			i_isFranchise = 0;
			
			WhereHOD = " and customer_id in(SELECT distributor_id FROM common_distributors where region_id not in (5,11)) ";	
			WhereHOD1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where region_id not in (5,11)) ";
			WhereHOD2 = " and distributor_id in(SELECT distributor_id FROM common_distributors where region_id not in (5,11)) ";
			WhereHOD3 = " and ipprd.distributor_id in(SELECT distributor_id FROM common_distributors where region_id not in (5,11)) ";
		}
		
		//RSM
		
		
		String RSMIDs="";
		long SelectedRSMArray[] = null;
		
		String WhereRSM = "";
		String WhereRSM1 = "";
		String WhereRSM2 = "";
		if (RSMIDs.length() > 0){
			WhereRSM = " and customer_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
			WhereRSM1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
			WhereRSM2 = " and distributor_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
		}
		
		//SM
		
		
		String SMIDs="";
		long SelectedSMArray[] = null;
		
		String WhereSM = "";
		if (SMIDs.length() > 0){
			WhereSM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
		}
		
		//TDM
		
		
		String TDMIDs="";
		long SelectedTDMArray[] = null;
		
		String WhereTDM = "";
		if (TDMIDs.length() > 0){
			WhereTDM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
		}
		
		//ASM
		
		
		String ASMIDs="";
		long SelectedASMArray[] = null;
		
		String WhereASM = "";
		if (ASMIDs.length() > 0){
			WhereASM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
		}
		
		Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
		String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);
		
		long SelectedBrandArray[] = null;
		
		String BrandIDs = "";
		String WhereBrand = "";
		
		if(SelectedBrandArray!= null && SelectedBrandArray.length > 0){
			for(int i = 0; i < SelectedBrandArray.length; i++){
				if(i == 0){
					BrandIDs += SelectedBrandArray[i]+"";
				}else{
					BrandIDs += ", "+SelectedBrandArray[i]+"";
				}
				
			}
		
			WhereBrand = " and ipv.brand_id in ("+BrandIDs+") ";
		}
		
		long DistributorID = 0;
		String WhereCustomerID ="";
		String WhereCustomerID1 ="";
		String WhereCustomerID2 ="";
		if (DistributorID != 0){
			WhereCustomerID = " and customer_id in ("+DistributorID+") ";
			WhereCustomerID1 = " and isa.distributor_id in ("+DistributorID+") ";
			WhereCustomerID2 = " and distributor_id in ("+DistributorID+") ";
		
		}else{
			
			WhereCustomerID = "";//" and customer_id in ("+DistributorIDs+") ";
			WhereCustomerID1 = "";//" and isa.distributor_id in ("+DistributorIDs+") ";
			WhereCustomerID2 = "";//" and distributor_id in ("+DistributorIDs+") ";
			
		}
		
		
		//GTM Category
		
		
		String GTMCategoryIDs="";
		long SelectedGTMCategoryArray[] = null;
		
		String WhereGTMCategory = "";
		String WhereGTMCategory1 = "";
		String WhereGTMCategory2 = "";
		String WhereGTMCategory3 = "";
		if (GTMCategoryIDs.length() > 0){
			WhereGTMCategory = " and customer_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";	
			WhereGTMCategory1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";
			WhereGTMCategory2 = " and tab1.distributor_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";
			WhereGTMCategory3 = " and distributor_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";
		}
		
		//System.out.println("Hello "+WhereCustomerID);
		
		
		int ArrayCount=0;
		
		ResultSet rs1 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
		while(rs1.next()){
			
			ResultSet rs2 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+rs1.getInt("product_type_id")+WherePackage);
			while(rs2.next()){
				ArrayCount++;
			}
		}
		
		
		//System.out.println(ArrayCount);
		
		double GrossRevenueArray[] = new double [ArrayCount];
		double SalesPromotionArray[] = new double [ArrayCount];
		double UpfrontDiscountArray[] = new double [ArrayCount];
		double PerCaseDiscount[] = new double [ArrayCount];
		double PrimaryPerCaseDiscount[] = new double [ArrayCount];
		double FixedDiscountArray[] = new double [ArrayCount];
		double FreightArray[] = new double [ArrayCount];
		double UnloadingArray[] = new double [ArrayCount];
		double CasesSoldArray[] = new double [ArrayCount];
		double NetRevenueArray[] = new double [ArrayCount];
		
		
		s5.executeUpdate("delete from  "+ds.logDatabaseName()+".bi_nsr where month = "+month+" and year ="+year+" and is_faisalabad = "+i_isFaisalabad+" and is_franchise = "+i_isFranchise+" and is_rest_of_franchise = "+i_isROF);
		
    	double ConvertedGrossRevenue=0;
    	int i=0;
		ResultSet rs3 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
		while(rs3.next()){
			int TypeID = rs3.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				ResultSet rs5 = s3.executeQuery("select sum(gross_value) from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+" and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
				}
				
				GrossRevenueArray[i]=GrossValue;
				ConvertedGrossRevenue += GrossValue;
				
				s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr (month, year, created_on, lrb_type_id, package_id, description, revenue, is_faisalabad, is_rest_of_franchise, is_franchise) values ("+month+","+year+",now(), "+TypeID+","+PackageID+",'Gross Revenue',"+GrossValue+","+i_isFaisalabad+","+i_isROF+","+i_isFranchise+")");
				//System.out.println("Gross Revenue "+GrossValue);
				
			i++;
			}
		}
		
		s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr (month, year, created_on, lrb_type_id, package_id, description, revenue, is_faisalabad, is_rest_of_franchise, is_franchise) values ("+month+","+year+",now(), "+0+","+0+",'Gross Revenue',"+ConvertedGrossRevenue+","+i_isFaisalabad+","+i_isROF+","+i_isFranchise+")");
		//System.out.println("Converted Gross Revenue "+ConvertedGrossRevenue);

		
		
    	double ConvertedSalesPromotion=0;
    	int j=0;
		ResultSet rs6 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
		while(rs6.next()){
			int TypeID = rs6.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				ResultSet rs5 = s3.executeQuery("select sum(free_stock) from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+"  and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
				}
				SalesPromotionArray[j]=GrossValue;
				ConvertedSalesPromotion+=GrossValue;
				
				
				s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr (month, year, created_on, lrb_type_id, package_id, description, amount, is_faisalabad, is_rest_of_franchise, is_franchise) values ("+month+","+year+",now(), "+TypeID+","+PackageID+",'Sales Promotion',"+GrossValue+","+i_isFaisalabad+","+i_isROF+","+i_isFranchise+")");
				//System.out.println("Sales Promotion "+GrossValue);
			j++;
			}
		}
		s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr (month, year, created_on, lrb_type_id, package_id, description, amount, is_faisalabad, is_rest_of_franchise, is_franchise) values ("+month+","+year+",now(), "+0+","+0+",'Sales Promotion',"+ConvertedSalesPromotion+","+i_isFaisalabad+","+i_isROF+","+i_isFranchise+")");
		//System.out.println("Converted Sales Promotion "+ConvertedSalesPromotion);
		
		
		
		
    	int jj=0;
    	double ConvertedUpfront=0;
		ResultSet rs7 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
		while(rs7.next()){
			int TypeID = rs7.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				ResultSet rs5 = s3.executeQuery("select sum(upfront_discount)*-1 from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+"  and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
				}
				UpfrontDiscountArray[jj]=GrossValue;
				ConvertedUpfront+=GrossValue;
				
				s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr (month, year, created_on, lrb_type_id, package_id, description, amount, is_faisalabad, is_rest_of_franchise, is_franchise) values ("+month+","+year+",now(), "+TypeID+","+PackageID+",'Upfront Discount',"+GrossValue+","+i_isFaisalabad+","+i_isROF+","+i_isFranchise+")");
				//System.out.println("Upfront Discount "+GrossValue);
			jj++;
			}
		}
		
		s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr (month, year, created_on, lrb_type_id, package_id, description, amount, is_faisalabad, is_rest_of_franchise, is_franchise) values ("+month+","+year+",now(), "+0+","+0+",'Upfront Discount',"+ConvertedUpfront+","+i_isFaisalabad+","+i_isROF+","+i_isFranchise+")");
		//System.out.println("Converted Upfront Discount "+ConvertedUpfront);
		
		
    	double TotalPrimaryPerCaseDiscount = 0;
    	int jji=0;
		ResultSet rs711 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
		while(rs711.next()){
			int TypeID = rs711.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				//double PrimaryPerCaseDiscount = 0;
				Date CurrentDate = StartDate;
				
				while(CurrentDate.before(Utilities.getDateByDays(EndDate,1))){
					
					ResultSet rs34 = s3.executeQuery("select sum(if(lifting_today>lifting_total,lifting_total,lifting_today) * percase_discount_rate) discount from ("+
								" select ipprd.distributor_id,ipprpl.package_id, ipprpl.lrb_type_id, ipprp.percase_discount_rate, ippr.valid_from, ippr.valid_to, ifnull(ipprp.quantity - ("+
									" select sum(idnp.total_units)/ipv.unit_per_sku from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id = ipprd.distributor_id and created_on between ippr.valid_from and ippr.valid_to and package_id=ipprpl.package_id and lrb_type_id=ipprpl.lrb_type_id"+
								" ),0) lifting_total, ifnull(("+
									" select sum(idnp.total_units)/ipv.unit_per_sku from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id = ipprd.distributor_id and created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and package_id=ipprpl.package_id and lrb_type_id=ipprpl.lrb_type_id"+
								" ),0) lifting_today from inventory_primary_percase_request ippr join inventory_primary_percase_request_distributors ipprd on ippr.id = ipprd.product_promotion_id join inventory_primary_percase_request_products ipprp on ippr.id=ipprp.id join inventory_primary_percase_request_products_lrb_types ipprpl on ippr.id=ipprpl.id and ipprp.package_id = ipprpl.package_id where date("+Utilities.getSQLDate(CurrentDate)+") between ippr.valid_from and ippr.valid_to and ippr.is_active = 1 "+WhereHOD3+" and ipprpl.package_id = "+PackageID+" and ipprpl.lrb_type_id = "+TypeID+" having lifting_total > 0"+
								" ) tab1");
					if (rs34.first()){
						PrimaryPerCaseDiscount[jji] += rs34.getDouble(1);
					}
					
					CurrentDate = Utilities.getDateByDays(CurrentDate,1);
				}
				
				TotalPrimaryPerCaseDiscount += PrimaryPerCaseDiscount[jji];
				
				s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr (month, year, created_on, lrb_type_id, package_id, description, amount, is_faisalabad, is_rest_of_franchise, is_franchise) values ("+month+","+year+",now(), "+TypeID+","+PackageID+",'Per Case Discount',"+PrimaryPerCaseDiscount[jji]+","+i_isFaisalabad+","+i_isROF+","+i_isFranchise+")");
				//System.out.println("Primary Per Case Discount "+PrimaryPerCaseDiscount[jji]);
				
				jji++;
			}
		}
		
		s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr (month, year, created_on, lrb_type_id, package_id, description, amount, is_faisalabad, is_rest_of_franchise, is_franchise) values ("+month+","+year+",now(), "+0+","+0+",'Per Case Discount',"+TotalPrimaryPerCaseDiscount+","+i_isFaisalabad+","+i_isROF+","+i_isFranchise+")");
		//System.out.println("Converted Primary Per Case Discount "+TotalPrimaryPerCaseDiscount);
		
		
		
		
    	int kkk=0;
    	double PerCaseDiscountConverted=0;
		ResultSet rs81 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
		while(rs81.next()){
			int TypeID = rs81.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				
				Date CurrentDate = StartDate;
				
				while(CurrentDate.before(Utilities.getDateByDays(EndDate,1))){
					//System.out.println("Hello "+Utilities.getDisplayDateFormat(CurrentDate));
					
					
					
					
					ResultSet rs16 = s3.executeQuery("select sum(discount_value) from ( "+
							 " select package_id, brand_id, sum(qty*discounted) discount_value, (select ip.lrb_type_id from inventory_products ip where ip.category_id =1 and ip.package_id = tab1.package_id and ip.brand_id = tab1.brand_id) product_type_id from ( "+
							 " select isa.outlet_id, ipv.package_id, ipv.brand_id, "+
							 
					 " case "+ 
						" when ((ipv.package_id=1 OR ipv.package_id=3) AND ipv.lrb_type_id=1) AND (isa.region_id=4 OR isa.region_id=5 OR isa.region_id=8 OR isa.region_id=9) AND (isa.created_on between '2016-03-22' and '2016-09-30') then 0 "+
		                " when (ipv.package_id=16) AND (isa.region_id=5 OR isa.region_id=8) AND (isa.created_on between '2016-04-01' and '2016-09-30') then 0 "+
		                " when ((ipv.package_id=2 OR ipv.package_id=24) AND ipv.lrb_type_id=1) AND (isa.region_id=5 OR isa.region_id=8) AND (isa.created_on between '2016-02-10' and '2016-07-18') then 0 "+
						" else SUM(isap.raw_cases) "+
		             " end as qty, "+ 
							" ( "+
							 " select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where date("+Utilities.getSQLDate(CurrentDate)+") between s.activated_on and s.deactivated_on and date("+Utilities.getSQLDate(CurrentDate)+") between sp.valid_from and sp.valid_to and sp.package = ipv.package_id  and sp.brand_id = ipv.brand_id and sp.brand_id != 0 and s.outlet_id = isa.outlet_id "+
							  " union all "+
							 " select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id join inventory_products ip on sp.package = ip.package_id and ip.category_id = 1 where date("+Utilities.getSQLDate(CurrentDate)+") between s.activated_on and s.deactivated_on and date("+Utilities.getSQLDate(CurrentDate)+") between sp.valid_from and sp.valid_to and sp.package = ipv.package_id and sp.brand_id = 0 and s.outlet_id = isa.outlet_id limit 1 "+
							  " ) discounted "+
							  " from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where 1=1 "+WhereHOD1+WhereRSM1+WhereCustomerID1+WhereGTMCategory1+" and isa.created_on between date("+Utilities.getSQLDate(CurrentDate)+") and date("+Utilities.getSQLDateNext(CurrentDate)+") and isap.is_promotion = 0 group by isa.outlet_id, ipv.package_id, ipv.brand_id having discounted is not null "+
							  " ) tab1 group by package_id, brand_id "+
							  " ) tab2 where package_id = "+PackageID+" and product_type_id = "+TypeID);
					
					if(rs16.first()){
						GrossValue +=rs16.getDouble(1);
					}
					
					
					
					CurrentDate = Utilities.getDateByDays(CurrentDate,1);
					//System.out.println(CurrentDate+" "+Utilities.getDateByDays(EndDate,1));
				}
				
				
				
				PerCaseDiscount[kkk]=GrossValue;
				
				PerCaseDiscountConverted+=GrossValue;
				
				
				s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr (month, year, created_on, lrb_type_id, package_id, description, amount, is_faisalabad, is_rest_of_franchise, is_franchise) values ("+month+","+year+",now(), "+TypeID+","+PackageID+",'Retailer - Variable',"+GrossValue+","+i_isFaisalabad+","+i_isROF+","+i_isFranchise+")");
				//System.out.println("Retailer Variable: "+GrossValue);
				
				kkk++;
			}
		}
		
		s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr (month, year, created_on, lrb_type_id, package_id, description, amount, is_faisalabad, is_rest_of_franchise, is_franchise) values ("+month+","+year+",now(), "+0+","+0+",'Retailer - Variable',"+PerCaseDiscountConverted+","+i_isFaisalabad+","+i_isROF+","+i_isFranchise+")");
		//System.out.println("Retailer Variable Converted "+PerCaseDiscountConverted);
		
		
		
    	int jjlm=0;
    	double ConvertedFixed=0;
    	double ConvertedFixed1=0;
		
		
		Date CurrentDate = StartDate;
		
		while(CurrentDate.before(Utilities.getDateByDays(EndDate,1))){
			//System.out.println("Hello "+Utilities.getDisplayDateFormat(CurrentDate));
			
			
			
			ResultSet rs16 = s3.executeQuery("select sum(fixed_company_share)/30 from ( "+
					" SELECT fixed_company_share, (select distributor_id from common_outlets_distributors_view where outlet_id = co.id limit 1) distributor_id FROM sampling s join common_outlets co on s.outlet_id = co.id where s.active = 1 and "+Utilities.getSQLDate(CurrentDate)+" between s.fixed_valid_from and s.fixed_valid_to and s.fixed_company_share != 0  "+
					" ) tab1 where 1=1 "+WhereHOD2+WhereRSM2+WhereCustomerID2+WhereGTMCategory2);
			
			if(rs16.first()){
				ConvertedFixed +=rs16.getDouble(1);
			}
			
			
			
			CurrentDate = Utilities.getDateByDays(CurrentDate,1);
		}
		
		//System.out.println("Helllllo - "+ConvertedFixed);
		
		
		
		s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr (month, year, created_on, lrb_type_id, package_id, description, amount, is_faisalabad, is_rest_of_franchise, is_franchise) values ("+month+","+year+",now(), "+0+","+0+",'Retailer - Fixed',"+ConvertedFixed+","+i_isFaisalabad+","+i_isROF+","+i_isFranchise+")");
		//System.out.println("Retailer Fixed Converted "+ConvertedFixed);
		
		
		
		
		
    	int kk=0;
    	double FreightConverted=0;
		ResultSet rs8 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
		while(rs8.next()){
			int TypeID = rs8.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				ResultSet rs5 = s3.executeQuery("select sum(freight)*-1 from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+"  and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
				}
				FreightArray[kk]=GrossValue;
				
				FreightConverted+=GrossValue;
				
				s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr (month, year, created_on, lrb_type_id, package_id, description, amount, is_faisalabad, is_rest_of_franchise, is_franchise) values ("+month+","+year+",now(), "+TypeID+","+PackageID+",'Freight',"+GrossValue+","+i_isFaisalabad+","+i_isROF+","+i_isFranchise+")");
				//System.out.println("Freight: "+GrossValue);
				
			kk++;
			}
		}
		s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr (month, year, created_on, lrb_type_id, package_id, description, amount, is_faisalabad, is_rest_of_franchise, is_franchise) values ("+month+","+year+",now(), "+0+","+0+",'Freight',"+FreightConverted+","+i_isFaisalabad+","+i_isROF+","+i_isFranchise+")");
		//System.out.println("Freight Converted:"+FreightConverted);

		
    	int ll=0;
    	double UnloadingConverted=0;
		ResultSet rs9 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
		while(rs9.next()){
			int TypeID = rs9.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				ResultSet rs5 = s3.executeQuery("select sum(unloading)*-1 from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+"  and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
				}
				UnloadingArray[ll]=GrossValue;
						
				UnloadingConverted+=GrossValue;
				
				
				s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr (month, year, created_on, lrb_type_id, package_id, description, amount, is_faisalabad, is_rest_of_franchise, is_franchise) values ("+month+","+year+",now(), "+TypeID+","+PackageID+",'Unloading',"+GrossValue+","+i_isFaisalabad+","+i_isROF+","+i_isFranchise+")");
				//System.out.println("Unloading: "+GrossValue);
			ll++;
			}
		}
		s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr (month, year, created_on, lrb_type_id, package_id, description, amount, is_faisalabad, is_rest_of_franchise, is_franchise) values ("+month+","+year+",now(), "+0+","+0+",'Unloading',"+UnloadingConverted+","+i_isFaisalabad+","+i_isROF+","+i_isFranchise+")");
		//System.out.println("Unloading Converted: "+UnloadingConverted);
		

    	int jjl=0;
    	double ConvertedHaulase=0;
		ResultSet rs71 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
		while(rs71.next()){
			int TypeID = rs71.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
			while(rs4.next()){
				
			jj++;
			}
		}
		
		ResultSet rs72 = s.executeQuery("SELECT sum(freight_amount) FROM inventory_delivery_note where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereHOD2+WhereRSM2+WhereCustomerID2+WhereGTMCategory3);
		if(rs72.first()){
			ConvertedHaulase = rs72.getDouble(1);
		}
		
		s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr (month, year, created_on, lrb_type_id, package_id, description, amount, is_faisalabad, is_rest_of_franchise, is_franchise) values ("+month+","+year+",now(), "+0+","+0+",'Haulage',"+ConvertedHaulase+","+i_isFaisalabad+","+i_isROF+","+i_isFranchise+")");
		//System.out.println("Haulage Converted "+ConvertedHaulase);

		
		
		
    	int lm=0;
    	double CasesSoldConverted=0;
		ResultSet rs10 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
		while(rs10.next()){
			int TypeID = rs10.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				double CCGross = 0;
				ResultSet rs5 = s3.executeQuery("select sum(bppi.quantity), sum(((bppi.quantity*ip.unit_per_case)*ip.liquid_in_ml)/ip.conversion_rate_in_ml) cc from peplogs.bi_percase_price_invoice bppi join inventory_packages ip on bppi.package_id = ip.id where 1=1 "+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+" and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and bppi.package_id="+PackageID+" and bppi.product_type_id="+TypeID);
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
					CCGross = rs5.getDouble(2);
				}
				CasesSoldArray[lm]=GrossValue;
				CasesSoldConverted+=CCGross;
				
				
				s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr (month, year, created_on, lrb_type_id, package_id, description, cases, is_faisalabad, is_rest_of_franchise, is_franchise) values ("+month+","+year+",now(), "+TypeID+","+PackageID+",'Cases Sold',"+GrossValue+","+i_isFaisalabad+","+i_isROF+","+i_isFranchise+")");
				//System.out.println("Cases Sold: "+GrossValue);
			lm++;
			}
		}
		
		s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr (month, year, created_on, lrb_type_id, package_id, description, cases, is_faisalabad, is_rest_of_franchise, is_franchise) values ("+month+","+year+",now(), "+0+","+0+",'Cases Sold',"+CasesSoldConverted+","+i_isFaisalabad+","+i_isROF+","+i_isFranchise+")");
		
		
		//System.out.println("Cases Sold Converted "+CasesSoldConverted);

		
		
		
		
		
		int km=0;
    	
    	double NetRevenueConvert=0;
    	for(int i1=0;i1<ArrayCount;i1++){
    		double NetRevenue=0;
    		NetRevenue = GrossRevenueArray[i1] - (SalesPromotionArray[i1]+UpfrontDiscountArray[i1]+FreightArray[i1]+UnloadingArray[i1]+PerCaseDiscount[i1]+PrimaryPerCaseDiscount[i1]);
    	
				
    		NetRevenueArray[km]=NetRevenue;	
    		NetRevenueConvert+=NetRevenue;
    		
    		System.out.println("Net Revenue"+NetRevenue);
		km++;
    	}	
		
    	NetRevenueConvert = NetRevenueConvert-(ConvertedFixed+ConvertedHaulase);
    	
    	
    	System.out.println("Net Revenue Converted "+NetRevenueConvert);
    	
    	for(int i1=0;i1<ArrayCount;i1++){
    	
    		
    		double Rate=0;
    		
    		if(CasesSoldArray[i1]!=0){
    			Rate = NetRevenueArray[i1]/CasesSoldArray[i1];
    		}
    		
    		System.out.println("Rate "+Rate);
		}	
		
		System.out.println("Rate Converted "+NetRevenueConvert/CasesSoldConverted);
		
		ds.dropConnection();
		dsi.dropConnection();
	}
	
	public static void NSRDistributors(Date StartDate, Date EndDate, long DistributorID) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		
		
		int month = Utilities.getMonthNumberByDate(StartDate);
		int year = Utilities.getYearByDate(StartDate);
		
		
		int i_isFaisalabad = 0;
		int i_isROF = 0;
		int i_isFranchise = 1;
		
		
		long UniqueSessionID = 0;

		long SessionUserID = 0;
		int FeatureID = 225;

		Datasource dsi = new Datasource();
		dsi.createConnection();
		Statement s5 = dsi.createStatement();
		
		Datasource ds = new Datasource();
		ds.createConnectionToReplica();
		Connection c = ds.getConnection();
		Statement s = c.createStatement();
		Statement s2 = c.createStatement();
		Statement s3 = c.createStatement();
		Statement s4 = c.createStatement();
		

//		Date StartDate = Utilities.parseDate("02/09/2016"); //(Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
//		Date EndDate = Utilities.parseDate("02/09/2016");//new Date();//(Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");
		
		int SelectedMonth = Utilities.getMonthNumberByDate(StartDate);
		int SelectedYear = Utilities.getYearByDate(StartDate);
		
		long SelectedPackagesArray[] = null;
		
		String PackageIDs = "";
		String WherePackage = "";
		
		if(SelectedPackagesArray!= null && SelectedPackagesArray.length > 0){
			for(int i = 0; i < SelectedPackagesArray.length; i++){
				if(i == 0){
					PackageIDs += SelectedPackagesArray[i]+"";
				}else{
					PackageIDs += ", "+SelectedPackagesArray[i]+"";
				}
			}
			WherePackage = " and package_id in ("+PackageIDs+") ";
		}
		
		//HOD
		
		
		String HODIDs="";
		long SelectedHODArray[] = null;
		
		String WhereHOD = "";
		String WhereHOD1 = "";
		String WhereHOD2 = "";
		String WhereHOD3 = "";
		//if (HODIDs.length() > 0){
		WhereHOD = " and customer_id in("+DistributorID+") ";	
		WhereHOD1 = " and isa.distributor_id in("+DistributorID+") ";
		WhereHOD2 = " and distributor_id in("+DistributorID+") ";
		WhereHOD3 = " and ipprd.distributor_id in("+DistributorID+") ";
		//}
			/*
		if (isFaisalabad){
			i_isFaisalabad = 1;
			i_isROF = 0;
			i_isFranchise = 0;
			
			WhereHOD = " and customer_id in(SELECT distributor_id FROM common_distributors where region_id in (5,11)) ";	
			WhereHOD1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where region_id in (5,11)) ";
			WhereHOD2 = " and distributor_id in(SELECT distributor_id FROM common_distributors where region_id in (5,11)) ";
			WhereHOD3 = " and ipprd.distributor_id in(SELECT distributor_id FROM common_distributors where region_id in (5,11)) ";
		}
		if (isROF){
			i_isFaisalabad = 0;
			i_isROF = 1;
			i_isFranchise = 0;
			
			WhereHOD = " and customer_id in(SELECT distributor_id FROM common_distributors where region_id not in (5,11)) ";	
			WhereHOD1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where region_id not in (5,11)) ";
			WhereHOD2 = " and distributor_id in(SELECT distributor_id FROM common_distributors where region_id not in (5,11)) ";
			WhereHOD3 = " and ipprd.distributor_id in(SELECT distributor_id FROM common_distributors where region_id not in (5,11)) ";
		}
		*/
		//RSM
		
		
		String RSMIDs="";
		long SelectedRSMArray[] = null;
		
		String WhereRSM = "";
		String WhereRSM1 = "";
		String WhereRSM2 = "";
		if (RSMIDs.length() > 0){
			WhereRSM = " and customer_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
			WhereRSM1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
			WhereRSM2 = " and distributor_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
		}
		
		//SM
		
		
		String SMIDs="";
		long SelectedSMArray[] = null;
		
		String WhereSM = "";
		if (SMIDs.length() > 0){
			WhereSM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
		}
		
		//TDM
		
		
		String TDMIDs="";
		long SelectedTDMArray[] = null;
		
		String WhereTDM = "";
		if (TDMIDs.length() > 0){
			WhereTDM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
		}
		
		//ASM
		
		
		String ASMIDs="";
		long SelectedASMArray[] = null;
		
		String WhereASM = "";
		if (ASMIDs.length() > 0){
			WhereASM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
		}
		
		Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
		String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);
		
		long SelectedBrandArray[] = null;
		
		String BrandIDs = "";
		String WhereBrand = "";
		
		if(SelectedBrandArray!= null && SelectedBrandArray.length > 0){
			for(int i = 0; i < SelectedBrandArray.length; i++){
				if(i == 0){
					BrandIDs += SelectedBrandArray[i]+"";
				}else{
					BrandIDs += ", "+SelectedBrandArray[i]+"";
				}
				
			}
		
			WhereBrand = " and ipv.brand_id in ("+BrandIDs+") ";
		}
		
		//long DistributorID = 0;
		String WhereCustomerID ="";
		String WhereCustomerID1 ="";
		String WhereCustomerID2 ="";
		/*
		if (DistributorID != 0){
			WhereCustomerID = " and customer_id in ("+DistributorID+") ";
			WhereCustomerID1 = " and isa.distributor_id in ("+DistributorID+") ";
			WhereCustomerID2 = " and distributor_id in ("+DistributorID+") ";
		
		}else{
			
			WhereCustomerID = "";//" and customer_id in ("+DistributorIDs+") ";
			WhereCustomerID1 = "";//" and isa.distributor_id in ("+DistributorIDs+") ";
			WhereCustomerID2 = "";//" and distributor_id in ("+DistributorIDs+") ";
			
		}
		*/
		
		//GTM Category
		
		
		String GTMCategoryIDs="";
		long SelectedGTMCategoryArray[] = null;
		
		String WhereGTMCategory = "";
		String WhereGTMCategory1 = "";
		String WhereGTMCategory2 = "";
		String WhereGTMCategory3 = "";
		if (GTMCategoryIDs.length() > 0){
			WhereGTMCategory = " and customer_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";	
			WhereGTMCategory1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";
			WhereGTMCategory2 = " and tab1.distributor_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";
			WhereGTMCategory3 = " and distributor_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";
		}
		
		//System.out.println("Hello "+WhereCustomerID);
		
		
		int ArrayCount=0;
		
		ResultSet rs1 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
		while(rs1.next()){
			
			ResultSet rs2 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+rs1.getInt("product_type_id")+WherePackage);
			while(rs2.next()){
				ArrayCount++;
			}
		}
		
		
		//System.out.println(ArrayCount);
		
		double GrossRevenueArray[] = new double [ArrayCount];
		//double SalesPromotionArray[] = new double [ArrayCount];
		double UpfrontDiscountArray[] = new double [ArrayCount];
		double PerCaseDiscount[] = new double [ArrayCount];
		double PrimaryPerCaseDiscount[] = new double [ArrayCount];
		double FixedDiscountArray[] = new double [ArrayCount];
		double FreightArray[] = new double [ArrayCount];
		double UnloadingArray[] = new double [ArrayCount];
		double CasesSoldArray[] = new double [ArrayCount];
		double NetRevenueArray[] = new double [ArrayCount];
		
		
		s5.executeUpdate("delete from  "+ds.logDatabaseName()+".bi_nsr_distributors where month = "+month+" and year ="+year+" and distributor_id = "+DistributorID+"");
		
    	double ConvertedGrossRevenue=0;
    	int i=0;
		ResultSet rs3 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
		while(rs3.next()){
			int TypeID = rs3.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				ResultSet rs5 = s3.executeQuery("select sum(gross_value) from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+" and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
				}
				
				GrossRevenueArray[i]=GrossValue;
				ConvertedGrossRevenue += GrossValue;
				
				s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr_distributors (month, year, created_on, lrb_type_id, package_id, description, revenue, distributor_id) values ("+month+","+year+",now(), "+TypeID+","+PackageID+",'Gross Revenue',"+GrossValue+","+DistributorID+")");
				//System.out.println("Gross Revenue "+GrossValue);
				
			i++;
			}
		}
		
		//s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr_distributors (month, year, created_on, lrb_type_id, package_id, description, revenue, distributor_id) values ("+month+","+year+",now(), "+0+","+0+",'Gross Revenue',"+ConvertedGrossRevenue+","+DistributorID+")");
		//System.out.println("Converted Gross Revenue "+ConvertedGrossRevenue);

		
		
    	double ConvertedSalesPromotion=0;
    	int j=0;
		ResultSet rs6 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
		while(rs6.next()){
			int TypeID = rs6.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				ResultSet rs5 = s3.executeQuery("select sum(free_stock) from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+"  and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
				}
				//SalesPromotionArray[j]=GrossValue;
				ConvertedSalesPromotion+=GrossValue;
				
				
				s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr_distributors (month, year, created_on, lrb_type_id, package_id, description, amount, distributor_id) values ("+month+","+year+",now(), "+TypeID+","+PackageID+",'Sales Promotion',"+GrossValue+","+DistributorID+")");
				//System.out.println("Sales Promotion "+GrossValue);
			j++;
			}
		}
		//s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr_distributors (month, year, created_on, lrb_type_id, package_id, description, amount, distributor_id) values ("+month+","+year+",now(), "+0+","+0+",'Sales Promotion',"+ConvertedSalesPromotion+","+DistributorID+")");
		//System.out.println("Converted Sales Promotion "+ConvertedSalesPromotion);
		
		
		
		
    	int jj=0;
    	double ConvertedUpfront=0;
		ResultSet rs7 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
		while(rs7.next()){
			int TypeID = rs7.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				ResultSet rs5 = s3.executeQuery("select sum(upfront_discount)*-1 from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+"  and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
				}
				UpfrontDiscountArray[jj]=GrossValue;
				ConvertedUpfront+=GrossValue;
				
				s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr_distributors (month, year, created_on, lrb_type_id, package_id, description, amount, distributor_id) values ("+month+","+year+",now(), "+TypeID+","+PackageID+",'Upfront Discount',"+GrossValue+","+DistributorID+")");
				//System.out.println("Upfront Discount "+GrossValue);
			jj++;
			}
		}
		
		//s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr_distributors (month, year, created_on, lrb_type_id, package_id, description, amount, distributor_id) values ("+month+","+year+",now(), "+0+","+0+",'Upfront Discount',"+ConvertedUpfront+","+DistributorID+")");
		//System.out.println("Converted Upfront Discount "+ConvertedUpfront);
		
		
    	double TotalPrimaryPerCaseDiscount = 0;
    	int jji=0;
		ResultSet rs711 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
		while(rs711.next()){
			int TypeID = rs711.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				//double PrimaryPerCaseDiscount = 0;
				Date CurrentDate = StartDate;
				
				while(CurrentDate.before(Utilities.getDateByDays(EndDate,1))){
					
					ResultSet rs34 = s3.executeQuery("select sum(if(lifting_today>lifting_total,lifting_total,lifting_today) * percase_discount_rate) discount from ("+
								" select ipprd.distributor_id,ipprpl.package_id, ipprpl.lrb_type_id, ipprp.percase_discount_rate, ippr.valid_from, ippr.valid_to, ifnull(ipprp.quantity - ("+
									" select sum(idnp.total_units)/ipv.unit_per_sku from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id = ipprd.distributor_id and created_on between ippr.valid_from and ippr.valid_to and package_id=ipprpl.package_id and lrb_type_id=ipprpl.lrb_type_id"+
								" ),0) lifting_total, ifnull(("+
									" select sum(idnp.total_units)/ipv.unit_per_sku from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id = ipprd.distributor_id and created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and package_id=ipprpl.package_id and lrb_type_id=ipprpl.lrb_type_id"+
								" ),0) lifting_today from inventory_primary_percase_request ippr join inventory_primary_percase_request_distributors ipprd on ippr.id = ipprd.product_promotion_id join inventory_primary_percase_request_products ipprp on ippr.id=ipprp.id join inventory_primary_percase_request_products_lrb_types ipprpl on ippr.id=ipprpl.id and ipprp.package_id = ipprpl.package_id where date("+Utilities.getSQLDate(CurrentDate)+") between ippr.valid_from and ippr.valid_to and ippr.is_active = 1 "+WhereHOD3+" and ipprpl.package_id = "+PackageID+" and ipprpl.lrb_type_id = "+TypeID+" having lifting_total > 0"+
								" ) tab1");
					if (rs34.first()){
						PrimaryPerCaseDiscount[jji] += rs34.getDouble(1);
					}
					
					CurrentDate = Utilities.getDateByDays(CurrentDate,1);
				}
				
				TotalPrimaryPerCaseDiscount += PrimaryPerCaseDiscount[jji];
				
				s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr_distributors (month, year, created_on, lrb_type_id, package_id, description, amount, distributor_id) values ("+month+","+year+",now(), "+TypeID+","+PackageID+",'Per Case Discount',"+PrimaryPerCaseDiscount[jji]+","+DistributorID+")");
				//System.out.println("Primary Per Case Discount "+PrimaryPerCaseDiscount[jji]);
				
				jji++;
			}
		}
		
		//s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr_distributors (month, year, created_on, lrb_type_id, package_id, description, amount, distributor_id) values ("+month+","+year+",now(), "+0+","+0+",'Per Case Discount',"+TotalPrimaryPerCaseDiscount+","+DistributorID+")");
		//System.out.println("Converted Primary Per Case Discount "+TotalPrimaryPerCaseDiscount);
		
		
		
		
    	int kkk=0;
    	double PerCaseDiscountConverted=0;
		ResultSet rs81 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
		while(rs81.next()){
			int TypeID = rs81.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				
				Date CurrentDate = StartDate;
				
				while(CurrentDate.before(Utilities.getDateByDays(EndDate,1))){
					//System.out.println("Hello "+Utilities.getDisplayDateFormat(CurrentDate));
					
					
					
					
					ResultSet rs16 = s3.executeQuery("select sum(discount_value) from ( "+
							 " select package_id, brand_id, sum(qty*discounted) discount_value, (select ip.lrb_type_id from inventory_products ip where ip.category_id =1 and ip.package_id = tab1.package_id and ip.brand_id = tab1.brand_id) product_type_id from ( "+
							 " select isa.outlet_id, ipv.package_id, ipv.brand_id, "+
							 
					 " case "+ 
						" when ((ipv.package_id=1 OR ipv.package_id=3) AND ipv.lrb_type_id=1) AND (isa.region_id=4 OR isa.region_id=5 OR isa.region_id=8 OR isa.region_id=9) AND (isa.created_on between '2016-03-22' and '2016-09-30') then 0 "+
		                " when (ipv.package_id=16) AND (isa.region_id=5 OR isa.region_id=8) AND (isa.created_on between '2016-04-01' and '2016-09-30') then 0 "+
		                " when ((ipv.package_id=2 OR ipv.package_id=24) AND ipv.lrb_type_id=1) AND (isa.region_id=5 OR isa.region_id=8) AND (isa.created_on between '2016-02-10' and '2016-07-18') then 0 "+
						" else SUM(isap.raw_cases) "+
		             " end as qty, "+ 
							" ( "+
							 " select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where date("+Utilities.getSQLDate(CurrentDate)+") between s.activated_on and s.deactivated_on and date("+Utilities.getSQLDate(CurrentDate)+") between sp.valid_from and sp.valid_to and sp.package = ipv.package_id  and sp.brand_id = ipv.brand_id and sp.brand_id != 0 and s.outlet_id = isa.outlet_id "+
							  " union all "+
							 " select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id join inventory_products ip on sp.package = ip.package_id and ip.category_id = 1 where date("+Utilities.getSQLDate(CurrentDate)+") between s.activated_on and s.deactivated_on and date("+Utilities.getSQLDate(CurrentDate)+") between sp.valid_from and sp.valid_to and sp.package = ipv.package_id and sp.brand_id = 0 and s.outlet_id = isa.outlet_id limit 1 "+
							  " ) discounted "+
							  " from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where 1=1 "+WhereHOD1+WhereRSM1+WhereCustomerID1+WhereGTMCategory1+" and isa.created_on between date("+Utilities.getSQLDate(CurrentDate)+") and date("+Utilities.getSQLDateNext(CurrentDate)+") and isap.is_promotion = 0 group by isa.outlet_id, ipv.package_id, ipv.brand_id having discounted is not null "+
							  " ) tab1 group by package_id, brand_id "+
							  " ) tab2 where package_id = "+PackageID+" and product_type_id = "+TypeID);
					
					if(rs16.first()){
						GrossValue +=rs16.getDouble(1);
					}
					
					
					
					CurrentDate = Utilities.getDateByDays(CurrentDate,1);
					//System.out.println(CurrentDate+" "+Utilities.getDateByDays(EndDate,1));
				}
				
				
				
				PerCaseDiscount[kkk]=GrossValue;
				
				PerCaseDiscountConverted+=GrossValue;
				
				
				s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr_distributors (month, year, created_on, lrb_type_id, package_id, description, amount, distributor_id) values ("+month+","+year+",now(), "+TypeID+","+PackageID+",'Retailer - Variable',"+GrossValue+","+DistributorID+")");
				//System.out.println("Retailer Variable: "+GrossValue);
				
				kkk++;
			}
		}
		
		//s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr_distributors (month, year, created_on, lrb_type_id, package_id, description, amount, distributor_id) values ("+month+","+year+",now(), "+0+","+0+",'Retailer - Variable',"+PerCaseDiscountConverted+","+DistributorID+")");
		//System.out.println("Retailer Variable Converted "+PerCaseDiscountConverted);
		
		
		
    	int jjlm=0;
    	double ConvertedFixed=0;
    	double ConvertedFixed1=0;
		
		
		Date CurrentDate = StartDate;
		
		while(CurrentDate.before(Utilities.getDateByDays(EndDate,1))){
			//System.out.println("Hello "+Utilities.getDisplayDateFormat(CurrentDate));
			
			
			
			ResultSet rs16 = s3.executeQuery("select sum(fixed_company_share)/30 from ( "+
					" SELECT fixed_company_share, (select distributor_id from common_outlets_distributors_view where outlet_id = co.id limit 1) distributor_id FROM sampling s join common_outlets co on s.outlet_id = co.id where s.active = 1 and "+Utilities.getSQLDate(CurrentDate)+" between s.fixed_valid_from and s.fixed_valid_to and s.fixed_company_share != 0  "+
					" ) tab1 where 1=1 "+WhereHOD2+WhereRSM2+WhereCustomerID2+WhereGTMCategory2);
			
			if(rs16.first()){
				ConvertedFixed +=rs16.getDouble(1);
			}
			
			
			
			CurrentDate = Utilities.getDateByDays(CurrentDate,1);
		}
		
		//System.out.println("Helllllo - "+ConvertedFixed);
		
		
		
		s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr_distributors (month, year, created_on, lrb_type_id, package_id, description, amount, distributor_id) values ("+month+","+year+",now(), "+0+","+0+",'Retailer - Fixed',"+ConvertedFixed+","+DistributorID+")");
		//System.out.println("Retailer Fixed Converted "+ConvertedFixed);
		
		
		
		
		
    	int kk=0;
    	double FreightConverted=0;
		ResultSet rs8 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
		while(rs8.next()){
			int TypeID = rs8.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				ResultSet rs5 = s3.executeQuery("select sum(freight)*-1 from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+"  and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
				}
				FreightArray[kk]=GrossValue;
				
				FreightConverted+=GrossValue;
				
				s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr_distributors (month, year, created_on, lrb_type_id, package_id, description, amount, distributor_id) values ("+month+","+year+",now(), "+TypeID+","+PackageID+",'Freight',"+GrossValue+","+DistributorID+")");
				//System.out.println("Freight: "+GrossValue);
				
			kk++;
			}
		}
		//s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr_distributors (month, year, created_on, lrb_type_id, package_id, description, amount, distributor_id) values ("+month+","+year+",now(), "+0+","+0+",'Freight',"+FreightConverted+","+DistributorID+")");
		//System.out.println("Freight Converted:"+FreightConverted);

		
    	int ll=0;
    	double UnloadingConverted=0;
		ResultSet rs9 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
		while(rs9.next()){
			int TypeID = rs9.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				ResultSet rs5 = s3.executeQuery("select sum(unloading)*-1 from "+ds.logDatabaseName()+".bi_percase_price_invoice where package_id = "+PackageID+" and product_type_id = "+TypeID+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+"  and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
				}
				UnloadingArray[ll]=GrossValue;
						
				UnloadingConverted+=GrossValue;
				
				
				s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr_distributors (month, year, created_on, lrb_type_id, package_id, description, amount, distributor_id) values ("+month+","+year+",now(), "+TypeID+","+PackageID+",'Unloading',"+GrossValue+","+DistributorID+")");
				//System.out.println("Unloading: "+GrossValue);
			ll++;
			}
		}
		//s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr_distributors (month, year, created_on, lrb_type_id, package_id, description, amount, distributor_id) values ("+month+","+year+",now(), "+0+","+0+",'Unloading',"+UnloadingConverted+","+DistributorID+")");
		//System.out.println("Unloading Converted: "+UnloadingConverted);
		

    	int jjl=0;
    	double ConvertedHaulase=0;
		ResultSet rs71 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
		while(rs71.next()){
			int TypeID = rs71.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
			while(rs4.next()){
				
			jj++;
			}
		}
		
		ResultSet rs72 = s.executeQuery("SELECT sum(freight_amount) FROM inventory_delivery_note where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereHOD2+WhereRSM2+WhereCustomerID2+WhereGTMCategory3);
		if(rs72.first()){
			ConvertedHaulase = rs72.getDouble(1);
		}
		
		s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr_distributors (month, year, created_on, lrb_type_id, package_id, description, amount, distributor_id) values ("+month+","+year+",now(), "+0+","+0+",'Haulage',"+ConvertedHaulase+","+DistributorID+")");
		//System.out.println("Haulage Converted "+ConvertedHaulase);

		
		
		
    	int lm=0;
    	double CasesSoldConverted=0;
		ResultSet rs10 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
		while(rs10.next()){
			int TypeID = rs10.getInt(1);
			
			ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
			while(rs4.next()){
				int PackageID = rs4.getInt(1);
				
				double GrossValue = 0;
				double CCGross = 0;
				ResultSet rs5 = s3.executeQuery("select sum(bppi.quantity), sum(((bppi.quantity*ip.unit_per_case)*ip.liquid_in_ml)/ip.conversion_rate_in_ml) cc from peplogs.bi_percase_price_invoice bppi join inventory_packages ip on bppi.package_id = ip.id where 1=1 "+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+" and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and bppi.package_id="+PackageID+" and bppi.product_type_id="+TypeID);
				if (rs5.first()){
					GrossValue = rs5.getDouble(1);
					CCGross = rs5.getDouble(2);
				}
				CasesSoldArray[lm]=GrossValue;
				CasesSoldConverted+=CCGross;
				
				
				s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr_distributors (month, year, created_on, lrb_type_id, package_id, description, cases, distributor_id) values ("+month+","+year+",now(), "+TypeID+","+PackageID+",'Cases Sold',"+GrossValue+","+DistributorID+")");
				//System.out.println("Cases Sold: "+GrossValue);
			lm++;
			}
		}
		
		//s5.executeUpdate("insert into "+ds.logDatabaseName()+".bi_nsr_distributors (month, year, created_on, lrb_type_id, package_id, description, cases, distributor_id) values ("+month+","+year+",now(), "+0+","+0+",'Cases Sold',"+CasesSoldConverted+","+DistributorID+")");
		
		
		//System.out.println("Cases Sold Converted "+CasesSoldConverted);

		
		
		
		
		
		int km=0;
    	
    	double NetRevenueConvert=0;
    	for(int i1=0;i1<ArrayCount;i1++){
    		double NetRevenue=0;
    		//NetRevenue = GrossRevenueArray[i1] - (SalesPromotionArray[i1]+UpfrontDiscountArray[i1]+FreightArray[i1]+UnloadingArray[i1]+PerCaseDiscount[i1]+PrimaryPerCaseDiscount[i1]);
    	
				
    		NetRevenueArray[km]=NetRevenue;	
    		NetRevenueConvert+=NetRevenue;
    		
    		System.out.println("Net Revenue"+NetRevenue);
		km++;
    	}	
		
    	NetRevenueConvert = NetRevenueConvert-(ConvertedFixed+ConvertedHaulase);
    	
    	
    	System.out.println("Net Revenue Converted "+NetRevenueConvert);
    	
    	for(int i1=0;i1<ArrayCount;i1++){
    	
    		
    		double Rate=0;
    		
    		if(CasesSoldArray[i1]!=0){
    			Rate = NetRevenueArray[i1]/CasesSoldArray[i1];
    		}
    		
    		System.out.println("Rate "+Rate);
		}	
		
		System.out.println("Rate Converted "+NetRevenueConvert/CasesSoldConverted);
		
		ds.dropConnection();
		dsi.dropConnection();
	}	
}
