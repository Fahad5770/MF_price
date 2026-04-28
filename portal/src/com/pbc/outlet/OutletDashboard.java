	package com.pbc.outlet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.Util;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;


public class OutletDashboard {
	Connection c;
	Datasource ds;
	public Outlet OUTLET = new Outlet();
	
	public OutletDashboard() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		ds = new Datasource();
		ds.createConnection();
		c = ds.getConnection();
	}
	
	public void setOutletID(long OutletID) throws SQLException{
		OUTLET.ID = OutletID;
		setOutletInformation();
	}
	
	public void setOutletInformation() throws SQLException{
		Statement s = c.createStatement();
		
		ResultSet rs = s.executeQuery("select concat(Outlet_Name, ' ', Bsi_Name), customer_name, region, rsm_id, asm_id, cr_id, address, latitude, longitude,customer_id from outletmaster where Outlet_ID = "+OUTLET.ID);
		if (rs.first()){
			OUTLET.NAME = rs.getString(1);
			OUTLET.DISTRIBUTOR_NAME = rs.getString("customer_name");
			OUTLET.REGION_SHORT_NAME = rs.getString("region");
			OUTLET.RSM_ID = rs.getLong("rsm_id");
			OUTLET.ASM_ID = rs.getLong("asm_id");
			OUTLET.CR_ID = rs.getLong("cr_id");
			OUTLET.ADDRESS = rs.getString("address");
			OUTLET.LATITUDE = rs.getDouble("latitude");
			OUTLET.LONGITUDE = rs.getDouble("longitude");
			OUTLET.DISTRIBUTOR_ID=rs.getLong("customer_id");
		}
		
		s.close();
	}
	
	public void close() throws SQLException{
		ds.dropConnection();
	}
	
	
	public boolean isDiscountActive() throws SQLException{
		Statement s = c.createStatement();
		
		boolean ret = false;
		ResultSet rs = s.executeQuery("select fixed_company_share from sampling where outlet_id = "+OUTLET.ID+" and active = 1");
		if (rs.first()){
			ret = true;
		}
		
		s.close();
		
		return ret;
		
		
	}
	public boolean isFixedDiscountActive() throws SQLException{
		Statement s = c.createStatement();
		
		boolean ret = false;
		ResultSet rs = s.executeQuery("select fixed_company_share, fixed_company_share_offpeak from sampling where outlet_id = "+OUTLET.ID+" and active = 1");
		if (rs.first()){
			if (rs.getDouble(1) != 0 | rs.getDouble(2) != 0){
				ret = true;
			}
		}
		
		s.close();
		
		return ret;
		
		
	}
	public boolean isPerCaseDiscountActive() throws SQLException{
		Statement s = c.createStatement();
		
		boolean ret = false;
		ResultSet rs = s.executeQuery("select sum(sp.company_share) from sampling s, sampling_percase sp where s.sampling_id = sp.sampling_id and s.outlet_id = "+OUTLET.ID+" and s.active = 1");
		if (rs.first()){
			if (rs.getDouble(1) != 0){
				ret = true;
			}
		}
		
		s.close();
		
		return ret;
		
		
	}
	
	public MonthlyDiscount[] getSlips() throws SQLException{
		
		Statement s = c.createStatement();
		Statement s2 = c.createStatement();
		
		List <MonthlyDiscount>list = new ArrayList<MonthlyDiscount>();
		int num = 0;
		
		
		ResultSet rs = s.executeQuery("SELECT * FROM sampling_monthly_approval where outlet_id = "+OUTLET.ID+" order by month desc");
		while(rs.next()){
			
			
			
			MonthlyDiscount item = new MonthlyDiscount();
			
			item.DATE = rs.getDate("month");
			item.APPROVAL_ID = rs.getLong("approval_id");
			item.IS_RECEIVED = rs.getInt("is_received");
			item.IS_PRINTED = rs.getInt("is_printed");
			item.IS_APPROVED = rs.getInt("status_id");
			item.DEDUCTION_AGAINST_ADVANCE = rs.getDouble("deduction_against_advance");
			item.MANUAL_ADJUSTMENT = rs.getDouble("adjustment");
			item.PAYABLE = rs.getDouble("payable");
			item.NET_PAYABLE = rs.getDouble("net_payable");
			item.SAMPLING_AMOUNT = rs.getDouble("sampling_amount");

			
			ResultSet rs2 = s2.executeQuery("select method_id, dispatch_code, byhand_userid from sampling_monthly_approval_dispatch where approval_id="+item.APPROVAL_ID);
			if( rs2.first() ){
				item.METHOD_ID = rs2.getInt("method_id");
				item.DISPATCH_CODE = rs2.getString("dispatch_code");
				item.BY_HAND_USERID = rs2.getInt("byhand_userid");
			}else{
				item.METHOD_ID = 0;
				item.DISPATCH_CODE = "0";
				item.BY_HAND_USERID = 0;
			}
			
			if (item.IS_PRINTED == 0){
				item.SLIP_STATUS = "Not Printed";
			}
			if (item.IS_PRINTED == 1){
				item.SLIP_STATUS = "Printed";
			}
			if (item.IS_RECEIVED == 1){
				item.SLIP_STATUS = "Received Back";
			}
			
			if (item.IS_APPROVED == 1){
				item.APPROVAL_STATUS = "Approved";
			}else{
				item.APPROVAL_STATUS = "On Hold";
			}
			
			list.add(item);
			num++;
		}
		
		s.close();
		MonthlyDiscount ret[] = list.toArray(new MonthlyDiscount[num]);
		return ret;
	}
	
	public Advance[] getAdvancesIssued() throws SQLException{
		Statement s = c.createStatement();
		
		List <Advance>list = new ArrayList<Advance>();
		int num = 0;
		
		
		ResultSet rs = s.executeQuery("SELECT sp.posted_on, spd.posting_id, spd.debit, spd.remarks FROM sampling_posting_accounts spd, sampling_posting sp where sp.posting_id = spd.posting_id and spd.posting_type = 1 and spd.outlet_id = "+OUTLET.ID);
		while(rs.next()){
			Advance item = new Advance();
			
			item.DATE = rs.getDate(1);
			item.AMOUNT = rs.getDouble(3);
			item.APPROVAL_ID = rs.getLong(2);
			item.REMARKS = rs.getString(4);
			
			list.add(item);
			num++;
		}
		
		s.close();
		Advance ret[] = list.toArray(new Advance[num]);
		return ret;
		
	}
	public LedgerTransaction[] getLedger() throws SQLException{
		Statement s = c.createStatement();
		
		List <LedgerTransaction>list = new ArrayList<LedgerTransaction>();
		int num = 0;
		
		double balance = 0;
		
		ResultSet rs = s.executeQuery("SELECT sp.posting_id, sp.posted_on, sp.posted_by, (select display_name from users where id = sp.posted_by) posted_by_name, spd.debit, spd.credit, spd.remarks, spd.posting_type, (select label from sampling_posting_types where id = spd.posting_type) posting_type_label FROM sampling_posting sp, sampling_posting_accounts spd where sp.posting_id = spd.posting_id and spd.outlet_id = "+OUTLET.ID+" order by sp.posted_on");
		while(rs.next()){
			LedgerTransaction item = new LedgerTransaction();
			
			item.POSTING_ID = rs.getLong(1);
			item.DATE = rs.getTimestamp(2);
			item.POSTED_BY = rs.getLong("posted_by");
			item.POSTED_BY_NAME = rs.getString("posted_by_name");
			item.POSTING_TYPE = rs.getLong("posting_type");
			item.POSTING_TYPE_LABEL = rs.getString("posting_type_label");
			item.DEBIT = rs.getDouble("debit");
			item.CREDIT = rs.getDouble("credit");
			item.REMARKS = rs.getString("remarks");
			
			balance = balance + (item.DEBIT - item.CREDIT);
			item.BALANCE = balance;
			
			list.add(item);
			num++;
		}
		
		s.close();
		LedgerTransaction ret[] = list.toArray(new LedgerTransaction[num]);
		return ret;
		
	}
	
	public InventoryPackage[] getPackageList() throws SQLException{
		
		Statement s = c.createStatement();
		
		List<InventoryPackage> packages = new ArrayList<InventoryPackage>();
		
		int num = 0;
		ResultSet rs = s.executeQuery("select id, label from inventory_packages order by sort_order");
		while(rs.next()){
			InventoryPackage item = new InventoryPackage();
			
			item.ID = rs.getInt(1);
			item.LABEL = rs.getString(2);
			
			packages.add(item);
			
			num++;
		}
		
		s.close();
		
		return packages.toArray(new InventoryPackage[num]);
	}
	
	
	public InventoryPackage[] getDiscountPackageListWithBrands() throws SQLException{
		
		Statement s = c.createStatement();
		
		List<InventoryPackage> packages = new ArrayList<InventoryPackage>();
		
		int num = 0;
		ResultSet rs = s.executeQuery("select spc.package, spc.brand_id, ip.label, ifnull((select label from inventory_brands where id = spc.brand_id),'All') brand_label from sampling_percase spc, sampling s, inventory_packages ip where s.sampling_id = spc.sampling_id and spc.package = ip.id and s.outlet_id = "+OUTLET.ID+" group by spc.package, spc.brand_id order by ip.sort_order, spc.brand_id");
		while(rs.next()){
			InventoryPackage item = new InventoryPackage();
			
			item.ID = rs.getInt(1);
			item.LABEL = rs.getString(3);
			item.BRAND_ID = rs.getInt(2);
			item.BRAND_LABEL = rs.getString(4);

			packages.add(item);
			
			num++;
		}
		
		s.close();
		
		return packages.toArray(new InventoryPackage[num]);
	}
	
	public double[] getSalesByPackage(int PackageID, int month, int year) throws SQLException{
		
		Statement s = c.createStatement();
		
		double qty = 0;
		double converted = 0;
		
		ResultSet rs = s.executeQuery("select ip.id, ip.label, (SELECT sum(ss.quty_quant) FROM sap_sales ss where ss.outlet_id = "+OUTLET.ID+" and  ss.month_zmonth = "+month+" and ss.year_zyear="+year+" and material_matnr in (select sap_code from inventory_products where package_id = ip.id)) qty, converted_cases_per_package from inventory_packages ip where ip.id = "+PackageID);
		if (rs.first()){
			qty = rs.getDouble("qty");
			converted = qty * rs.getDouble("converted_cases_per_package");
		}
		
		s.close();
		
		double ret[] = new double[2];
		ret[0] = qty;
		ret[1] = converted;
		
		return ret;
	}
	
	public double getTotalDiscount() throws SQLException{
		Statement s = c.createStatement();
		
		double ret = 0;
		ResultSet rs = s.executeQuery("SELECT sum(sampling_amount) FROM sampling_monthly_approval where outlet_id = "+OUTLET.ID);
		if (rs.first()){
			ret = rs.getDouble(1);
		}
		
		s.close();
		
		return ret;
	}
	
	public double getAverageDiscount() throws SQLException{
		Statement s = c.createStatement();
		
		double ret = 0;
		ResultSet rs = s.executeQuery("SELECT avg(sampling_amount) FROM sampling_monthly_approval where outlet_id = "+OUTLET.ID);
		if (rs.first()){
			ret = rs.getDouble(1);
		}
		
		s.close();
		
		return ret;
	}
	
	public double getAverageSalesAmount() throws SQLException{
		Statement s = c.createStatement();
		
		double ret = 0;
		ResultSet rs = s.executeQuery("select avg(amt) from (SELECT month_zmonth, year_zyear, sum(amount_amtnt) amt FROM pep.sap_sales where outlet_id = " + OUTLET.ID + " group by month_zmonth, year_zyear) tab");
		if (rs.first()){
			ret = rs.getDouble(1);
		}
		
		s.close();
		
		return ret;
	}
	
	public double getTotalSalesAmount() throws SQLException{
		Statement s = c.createStatement();
		
		double ret = 0;
		ResultSet rs = s.executeQuery("select sum(amt) from (SELECT month_zmonth, year_zyear, sum(amount_amtnt) amt FROM pep.sap_sales where outlet_id = " + OUTLET.ID + " group by month_zmonth, year_zyear) tab");
		if (rs.first()){
			ret = rs.getDouble(1);
		}
		
		s.close();
		
		return ret;
	}
	
	public double getAverageSalesConvertedCases() throws SQLException{
		Statement s = c.createStatement();
		
		double ret = 0;
		ResultSet rs = s.executeQuery("select avg(converted_qty) from (select tab.month_zmonth, tab.year_zyear, sum((tab.qty * ips.converted_cases_per_package)) converted_qty from (SELECT month_zmonth, year_zyear, material_matnr, sum(quty_quant) qty FROM pep.sap_sales where outlet_id = "+OUTLET.ID+" group by month_zmonth, year_zyear, material_matnr) tab, inventory_products ip, inventory_packages ips where tab.material_matnr = ip.sap_code and ip.package_id = ips.id group by tab.month_zmonth, tab.year_zyear) tab2");
		if (rs.first()){
			ret = rs.getDouble(1);
		}
		
		s.close();
		
		return ret;
	}
	
	public double getTotalSalesConvertedCases() throws SQLException{
		Statement s = c.createStatement();
		
		double ret = 0;
		ResultSet rs = s.executeQuery("select sum(converted_qty) from (select tab.month_zmonth, tab.year_zyear, sum((tab.qty * ips.converted_cases_per_package)) converted_qty from (SELECT month_zmonth, year_zyear, material_matnr, sum(quty_quant) qty FROM pep.sap_sales where outlet_id = "+OUTLET.ID+" group by month_zmonth, year_zyear, material_matnr) tab, inventory_products ip, inventory_packages ips where tab.material_matnr = ip.sap_code and ip.package_id = ips.id group by tab.month_zmonth, tab.year_zyear) tab2");
		if (rs.first()){
			ret = rs.getDouble(1);
		}
		
		s.close();
		
		return ret;
	}
	
	public double getTotalSalesConvertedCases(int month, int year) throws SQLException{
		Statement s = c.createStatement();
		
		double ret = 0;
		ResultSet rs = s.executeQuery("select sum(converted_qty) from (select tab.month_zmonth, tab.year_zyear, sum((tab.qty * ips.converted_cases_per_package)) converted_qty from (SELECT month_zmonth, year_zyear, material_matnr, sum(quty_quant) qty FROM pep.sap_sales where outlet_id = "+OUTLET.ID+" and month_zmonth = "+month+" and year_zyear = "+year+" group by month_zmonth, year_zyear, material_matnr) tab, inventory_products ip, inventory_packages ips where tab.material_matnr = ip.sap_code and ip.package_id = ips.id group by tab.month_zmonth, tab.year_zyear) tab2");
		if (rs.first()){
			ret = rs.getDouble(1);
		}
		s.close();
		
		return ret;
	}
	
	public double getDiscountOfMonth(Date date) throws SQLException{
		Statement s = c.createStatement();
		
		double ret = 0;
		ResultSet rs = s.executeQuery("SELECT sampling_amount FROM sampling_monthly_approval where outlet_id = "+OUTLET.ID+" and month = "+Utilities.getSQLDate(date));
		if (rs.first()){
			ret = rs.getDouble(1);
		}
		
		s.close();
		
		return ret;
	}
	
	public Date getLastDateOfDiscount() throws SQLException{
		Statement s = c.createStatement();
		
		Date ret = null;
		ResultSet rs = s.executeQuery("SELECT month FROM sampling_monthly_approval where outlet_id = "+OUTLET.ID+" order by month desc limit 1");
		if (rs.first()){
			ret = rs.getDate(1);
		}
		
		s.close();
		
		return ret;
	}

	public double getFixedDiscountAmount(Date EndDate) throws SQLException{
		
		Statement s = c.createStatement();
		Statement s2 = c.createStatement();
		
		double ret = 0;
		boolean isFixedValid = false;
		
		ResultSet rs = s.executeQuery("SELECT s.fixed_company_share, s.fixed_valid_from, s.fixed_valid_to, s.fixed_company_share_offpeak, s.sampling_id from sampling s where s.outlet_id = "+OUTLET.ID+" and s.activated_on <= cast("+Utilities.getSQLDateNext(EndDate)+" as datetime) and from_days(to_days(s.deactivated_on)-1) >= cast("+Utilities.getSQLDate(EndDate)+" as datetime)");
		if (rs.first()){
			
			Date FixedValidFrom = rs.getDate("fixed_valid_from");
		  	Date FixedValidTo = rs.getDate("fixed_valid_to");
		  	
		  	if (FixedValidFrom != null && FixedValidTo != null){
			  	if (FixedValidFrom.before(EndDate) && FixedValidTo.after(EndDate)){
			  		ret = rs.getDouble(1);
			  		isFixedValid = true;
			  	}
		  	}
		  	
		  	
		  	
		  	// Off Peak
		  	
		  	boolean isFixedOffpeak = false;
		  	
		  	int month = Utilities.getMonthNumberByDate(EndDate);
		  	int year = Utilities.getYearByDate(EndDate);
		  	
		  	ResultSet rs10 = s2.executeQuery("select * from sampling_offpeak_months where month = "+month+" and year = "+year);
		  	if (rs10.first()){
				isFixedOffpeak = true;
		  	}
		  	
		  	if (isFixedValid == true){
		  		if (isFixedOffpeak == false){
				  	ret = rs.getDouble("fixed_company_share");
		  		}else{
				  	ret = rs.getDouble("fixed_company_share_offpeak");
		  		}
		  	}
		  	
		  	// Sales Threshold
		  	
		  	double SalesConvertedCases = getTotalSalesConvertedCases(month, year);
		  	
		  	boolean isThresholdActive = false;
		  	
		  	double ThresholdDiscount = 0;
		  	ResultSet rs2 = s2.executeQuery("SELECT discount FROM sampling_fixed_threshold where converted_sales >= "+SalesConvertedCases+" and sampling_id = "+rs.getString("sampling_id")+" order by percentage limit 1");
		  	if (rs2.first()){
		  		ThresholdDiscount = rs2.getDouble(1);
		  		isThresholdActive = true;
		  	}
		  	
		  	if (isThresholdActive == true){
		  		
		  		if (ThresholdDiscount < ret){
		  			ret = ThresholdDiscount;
		  		}
		  		
		  	}
		  	
		  	
		}
		
		
		s2.close();
		s.close();
		return ret;
	}

	public double getFixedDiscountAmountGenerated(Date EndDate) throws SQLException{
		
		Statement s = c.createStatement();
		Statement s2 = c.createStatement();
		
		double ret = 0;
		boolean isFixedValid = false;
		
		ResultSet rs = s.executeQuery("SELECT s.fixed_company_share, s.fixed_valid_from, s.fixed_valid_to, s.fixed_company_share_offpeak, s.sampling_id from sampling s, sampling_monthly_approval sma where s.outlet_id = sma.outlet_id and s.outlet_id = "+OUTLET.ID+" and sma.month = "+Utilities.getSQLDate(EndDate)+" and s.activated_on <= cast("+Utilities.getSQLDateNext(EndDate)+" as datetime) and from_days(to_days(s.deactivated_on)-1) >= cast("+Utilities.getSQLDate(EndDate)+" as datetime)");
		if (rs.first()){
			
			Date FixedValidFrom = rs.getDate("fixed_valid_from");
		  	Date FixedValidTo = rs.getDate("fixed_valid_to");
		  	
		  	if (FixedValidFrom != null && FixedValidTo != null){
			  	if (FixedValidFrom.before(EndDate) && FixedValidTo.after(EndDate)){
			  		ret = rs.getDouble(1);
			  		isFixedValid = true;
			  	}
		  	}
		  	
		  	boolean isFixedOffpeak = false;
		  	
		  	int month = Utilities.getMonthNumberByDate(EndDate);
		  	int year = Utilities.getYearByDate(EndDate);
		  	
		  	ResultSet rs10 = s2.executeQuery("select * from sampling_offpeak_months where month = "+month+" and year = "+year);
		  	if (rs10.first()){
				isFixedOffpeak = true;
		  	}
		  	
		  	if (isFixedValid == true){
		  		if (isFixedOffpeak == false){
				  	ret = rs.getDouble("fixed_company_share");
		  		}else{
				  	ret = rs.getDouble("fixed_company_share_offpeak");
		  		}
		  	}
		  	
		  	// Sales Threshold
		  	
		  	double SalesConvertedCases = getTotalSalesConvertedCases(month, year);
		  	
		  	boolean isThresholdActive = false;
		  	
		  	double ThresholdDiscount = 0;
		  	ResultSet rs2 = s2.executeQuery("SELECT discount FROM sampling_fixed_threshold where converted_sales >= "+SalesConvertedCases+" and sampling_id = "+rs.getString("sampling_id")+" order by percentage limit 1");
		  	if (rs2.first()){
		  		ThresholdDiscount = rs2.getDouble(1);
		  		isThresholdActive = true;
		  	}
		  	
		  	if (isThresholdActive == true){
		  		
		  		if (ThresholdDiscount < ret){
		  			ret = ThresholdDiscount;
		  		}
		  		
		  	}		  	
		}
		
		s2.close();
		s.close();
		return ret;
	}
	
	public double getFixedDiscountDeductionAmount(Date EndDate) throws SQLException{
		
		Statement s = c.createStatement();
		Statement s2 = c.createStatement();
		
		double ret = 0;
		boolean isFixedValid = false;
		
		ResultSet rs = s.executeQuery("SELECT s.fixed_deduction_term, s.fixed_valid_from, s.fixed_valid_to, s.fixed_deduction_term_offpeak, s.sampling_id from sampling s where s.outlet_id = "+OUTLET.ID+" and s.activated_on <= cast("+Utilities.getSQLDateNext(EndDate)+" as datetime) and from_days(to_days(s.deactivated_on)-1) >= cast("+Utilities.getSQLDate(EndDate)+" as datetime)");
		if (rs.first()){
			
			Date FixedValidFrom = rs.getDate("fixed_valid_from");
		  	Date FixedValidTo = rs.getDate("fixed_valid_to");
		  	
		  	if (FixedValidFrom != null && FixedValidTo != null){
			  	if (FixedValidFrom.before(EndDate) && FixedValidTo.after(EndDate)){
			  		isFixedValid = true;
			  	}
		  	}
		  	
		  	boolean isFixedOffpeak = false;
		  	
		  	int month = Utilities.getMonthNumberByDate(EndDate);
		  	int year = Utilities.getYearByDate(EndDate);
		  	
		  	ResultSet rs10 = s2.executeQuery("select * from sampling_offpeak_months where month = "+month+" and year = "+year);
		  	if (rs10.first()){
				isFixedOffpeak = true;
		  	}
		  	
		  	if (isFixedValid == true){
		  		if (isFixedOffpeak == false){
				  	ret = rs.getDouble("fixed_deduction_term");
		  		}else{
				  	ret = rs.getDouble("fixed_deduction_term_offpeak");
		  		}
		  	}
		  	
		  	// Sales Threshold
		  	
		  	double SalesConvertedCases = getTotalSalesConvertedCases(month, year);
		  	
		  	boolean isThresholdActive = false;
		  	
		  	double ThresholdDiscount = 0;
		  	ResultSet rs2 = s2.executeQuery("SELECT discount FROM sampling_fixed_threshold where converted_sales >= "+SalesConvertedCases+" and sampling_id = "+rs.getString("sampling_id")+" order by percentage limit 1");
		  	if (rs2.first()){
		  		ThresholdDiscount = rs2.getDouble(1);
		  		isThresholdActive = true;
		  	}
		  	
		  	if (isThresholdActive == true){
		  		
		  		if (ThresholdDiscount < ret){
		  			ret = ThresholdDiscount;
		  		}
		  		
		  	}
		  	
		}
		
		
		s2.close();
		s.close();
		return ret;
	}
	
	public double getPerCaseDiscountAmountGenerated(Date EndDate) throws SQLException{
		Statement s = c.createStatement();
		
		int month = Utilities.getMonthNumberByDate(EndDate);
		int year = Utilities.getYearByDate(EndDate);
				
		double discount = 0;
		
		
		ResultSet rs = s.executeQuery("SELECT sp.company_share, sp.valid_from, sp.valid_to, sp.package, sp.brand_id, "+
					"ifnull((case sp.brand_id when 0 then("+
					"(SELECT sum(ss.quty_quant) FROM sap_sales ss where ss.outlet_id = "+OUTLET.ID+" and  ss.month_zmonth = "+month+" and ss.year_zyear="+year+" and material_matnr in (select sap_code from inventory_products where package_id = sp.package))"+
					")else("+
					"(SELECT sum(ss.quty_quant) FROM sap_sales ss where ss.outlet_id = "+OUTLET.ID+" and  ss.month_zmonth = "+month+" and ss.year_zyear="+year+" and material_matnr in (select sap_code from inventory_products where package_id = sp.package and brand_id = sp.brand_id))"+
					") end),0) as qty"+
					" from sampling s, sampling_percase sp, sampling_monthly_approval sma where s.sampling_id = sp.sampling_id and s.outlet_id = sma.outlet_id and s.outlet_id = "+OUTLET.ID+" and sma.month = "+Utilities.getSQLDate(EndDate)+" and s.activated_on <= cast("+Utilities.getSQLDateNext(EndDate)+" as datetime) and from_days(to_days(s.deactivated_on)-1) >= cast("+Utilities.getSQLDate(EndDate)+" as datetime)");
		while (rs.next()){
			
			Date ValidFrom = rs.getDate("valid_from");
		  	Date ValidTo = rs.getDate("valid_to");
		  	
		  	if (ValidFrom != null && ValidTo != null){
			  	if (ValidFrom.before(EndDate) && ValidTo.after(EndDate)){
			  		double rate = rs.getDouble(1);
			  		discount += (rate * rs.getDouble("qty"));
			  	}
		  	}
		}
		
		s.close();
		
		return discount;
	}

	public double[] getPerCaseDiscountAmountAndDeductionAmount(Date EndDate) throws SQLException{
		Statement s = c.createStatement();
		
		int month = Utilities.getMonthNumberByDate(EndDate);
		int year = Utilities.getYearByDate(EndDate);
				
		double discount = 0;
		double deduction = 0;
		
		
		
		
		
		double ret[] = new double[2];
		
		
		ResultSet rs = s.executeQuery("SELECT sp.company_share, sp.valid_from, sp.valid_to, sp.package, sp.brand_id, sp.deduction_term, "+
				    "ifnull((case sp.brand_id when 0 then("+
				    "(SELECT sum(ss.quty_quant) FROM sap_sales ss where ss.outlet_id = "+OUTLET.ID+" and  ss.month_zmonth = "+month+" and ss.year_zyear="+year+" and material_matnr in (select sap_code from inventory_products where package_id = sp.package))"+
					")else("+
					"(SELECT sum(ss.quty_quant) FROM sap_sales ss where ss.outlet_id = "+OUTLET.ID+" and  ss.month_zmonth = "+month+" and ss.year_zyear="+year+" and material_matnr in (select sap_code from inventory_products where package_id = sp.package and brand_id = sp.brand_id))"+
					") end),0) as qty"+
					" from sampling s, sampling_percase sp where s.sampling_id = sp.sampling_id and s.outlet_id = "+OUTLET.ID+" and from_days(to_days(s.activated_on)-120) <= cast("+Utilities.getSQLDateNext(EndDate)+" as datetime) and from_days(to_days(s.deactivated_on)-121) >= cast("+Utilities.getSQLDate(EndDate)+" as datetime) /*and ((package in (2,24) and brand_id in (12)) or (package not in (2,24) and brand_id not in (-1)))*/ ");
		
		
		
		
		
		/*System.out.println("SELECT sp.company_share, sp.valid_from, sp.valid_to, sp.package, sp.brand_id, sp.deduction_term, "+
				"ifnull((case sp.brand_id when 0 then("+
				"(SELECT sum(ss.quty_quant) FROM sap_sales ss where ss.outlet_id = "+OUTLET.ID+" and  ss.month_zmonth = "+month+" and ss.year_zyear="+year+" and material_matnr in (select sap_code from inventory_products where package_id = sp.package))"+
				")else("+
				"(SELECT sum(ss.quty_quant) FROM sap_sales ss where ss.outlet_id = "+OUTLET.ID+" and  ss.month_zmonth = "+month+" and ss.year_zyear="+year+" and material_matnr in (select sap_code from inventory_products where package_id = sp.package and brand_id = sp.brand_id))"+
				") end),0) as qty"+
				" from sampling s, sampling_percase sp where s.sampling_id = sp.sampling_id and s.outlet_id = "+OUTLET.ID+" and s.activated_on <= cast("+Utilities.getSQLDateNext(EndDate)+" as datetime) and from_days(to_days(s.deactivated_on)-1) >= cast("+Utilities.getSQLDate(EndDate)+" as datetime)");
				*/
		while (rs.next()){
			
			Date ValidFrom = rs.getDate("valid_from");
		  	Date ValidTo = rs.getDate("valid_to");
		  	
		  	if (ValidFrom != null && ValidTo != null){
			  	if (ValidFrom.before(EndDate) && ValidTo.after(EndDate)){
			  		double rate = rs.getDouble(1);
			  		discount += (rate * rs.getDouble("qty"));
			  		deduction += (rs.getDouble("deduction_term") * rs.getDouble("qty"));
			  	}
		  	}
		}
		
		s.close();
		
		ret[0] = discount;
		ret[1] = deduction;
		
		return ret;
	}

	public ResultSet getPerCaseDiscountResultSet(Date EndDate) throws SQLException{
		Statement s = c.createStatement();
		
		int month = Utilities.getMonthNumberByDate(EndDate);
		int year = Utilities.getYearByDate(EndDate);
				
		ResultSet rs = s.executeQuery("SELECT sp.company_share, sp.valid_from, sp.valid_to, sp.package, (select label from inventory_packages where id = sp.package) package_name, sp.brand_id, ifnull((select label from inventory_brands where id = sp.brand_id),'All') brand_name, sp.deduction_term, "+
					"ifnull((case sp.brand_id when 0 then("+
					"(SELECT sum(ss.quty_quant) FROM sap_sales ss where ss.outlet_id = "+OUTLET.ID+" and  ss.month_zmonth = "+month+" and ss.year_zyear="+year+" and material_matnr in (select sap_code from inventory_products where package_id = sp.package))"+
					")else("+
					"(SELECT sum(ss.quty_quant) FROM sap_sales ss where ss.outlet_id = "+OUTLET.ID+" and  ss.month_zmonth = "+month+" and ss.year_zyear="+year+" and material_matnr in (select sap_code from inventory_products where package_id = sp.package and brand_id = sp.brand_id))"+
					") end),0) as qty"+
					" from sampling s, sampling_percase sp where s.sampling_id = sp.sampling_id and s.outlet_id = "+OUTLET.ID+" and s.activated_on <= cast("+Utilities.getSQLDateNext(EndDate)+" as datetime) and from_days(to_days(s.deactivated_on)-1) >= cast("+Utilities.getSQLDate(EndDate)+" as datetime)");
		return rs;
	}
	
	public double[] getPerCaseDiscountAmountAndRate(Date EndDate, int PackageID, int BrandID) throws SQLException{
		Statement s = c.createStatement();
		
		int month = Utilities.getMonthNumberByDate(EndDate);
		int year = Utilities.getYearByDate(EndDate);
				
		double rate = 0;
		double discount = 0;
		
		ResultSet rs = s.executeQuery("SELECT sp.company_share, sp.valid_from, sp.valid_to, sp.package, sp.brand_id, "+
					"ifnull((case sp.brand_id when 0 then("+
					"(SELECT sum(ss.quty_quant) FROM sap_sales ss where ss.outlet_id = "+OUTLET.ID+" and  ss.month_zmonth = "+month+" and ss.year_zyear="+year+" and material_matnr in (select sap_code from inventory_products where package_id = sp.package))"+
					")else("+
					"(SELECT sum(ss.quty_quant) FROM sap_sales ss where ss.outlet_id = "+OUTLET.ID+" and  ss.month_zmonth = "+month+" and ss.year_zyear="+year+" and material_matnr in (select sap_code from inventory_products where package_id = sp.package and brand_id = sp.brand_id))"+
					") end),0) as qty"+
					" from sampling s, sampling_percase sp where s.sampling_id = sp.sampling_id and s.outlet_id = "+OUTLET.ID+" and sp.package = "+PackageID+" and s.activated_on <= cast("+Utilities.getSQLDateNext(EndDate)+" as datetime) and from_days(to_days(s.deactivated_on)-1) >= cast("+Utilities.getSQLDate(EndDate)+" as datetime) and sp.package = "+PackageID+" and sp.brand_id = "+BrandID);
		while (rs.next()){
			
			Date ValidFrom = rs.getDate("valid_from");
		  	Date ValidTo = rs.getDate("valid_to");
		  	
		  	if (ValidFrom != null && ValidTo != null){
			  	if (ValidFrom.before(EndDate) && ValidTo.after(EndDate)){
			  		rate = rs.getDouble(1);
			  		discount += (rate * rs.getDouble("qty"));
			  	}
		  	}
		}
		
		s.close();
		
		double ret[] = new double[2];
		ret[0] = rate;
		ret[1] = discount;
		return ret;
	}
	
	
	
	//Added by Ferhan for new reports ////////////////////////////
	///////////////////////////////////////////////////////////////
	
	//With One parameter	at line 171
	public MonthlyDiscount[] getSlips1(String OutletID) throws SQLException{
			
			Statement s = c.createStatement();
			Statement s2 = c.createStatement();
			
			List <MonthlyDiscount>list = new ArrayList<MonthlyDiscount>();
			int num = 0;
			
			
			ResultSet rs = s.executeQuery("SELECT * FROM sampling_monthly_approval where outlet_id = "+OutletID+" order by month desc");
			while(rs.next()){
				
				
				
				MonthlyDiscount item = new MonthlyDiscount();
				
				item.DATE = rs.getDate("month");
				item.APPROVAL_ID = rs.getLong("approval_id");
				item.IS_RECEIVED = rs.getInt("is_received");
				item.IS_PRINTED = rs.getInt("is_printed");
				item.IS_APPROVED = rs.getInt("status_id");
				item.DEDUCTION_AGAINST_ADVANCE = rs.getDouble("deduction_against_advance");
				item.MANUAL_ADJUSTMENT = rs.getDouble("adjustment");
				item.PAYABLE = rs.getDouble("payable");
				item.NET_PAYABLE = rs.getDouble("net_payable");
				item.SAMPLING_AMOUNT = rs.getDouble("sampling_amount");

				
				ResultSet rs2 = s2.executeQuery("select method_id, dispatch_code, byhand_userid from sampling_monthly_approval_dispatch where approval_id="+item.APPROVAL_ID);
				if( rs2.first() ){
					item.METHOD_ID = rs2.getInt("method_id");
					item.DISPATCH_CODE = rs2.getString("dispatch_code");
					item.BY_HAND_USERID = rs2.getInt("byhand_userid");
				}else{
					item.METHOD_ID = 0;
					item.DISPATCH_CODE = "0";
					item.BY_HAND_USERID = 0;
				}
				
				if (item.IS_PRINTED == 0){
					item.SLIP_STATUS = "Not Printed";
				}
				if (item.IS_PRINTED == 1){
					item.SLIP_STATUS = "Printed";
				}
				if (item.IS_RECEIVED == 1){
					item.SLIP_STATUS = "Received Back";
				}
				
				if (item.IS_APPROVED == 1){
					item.APPROVAL_STATUS = "Approved";
				}else{
					item.APPROVAL_STATUS = "On Hold";
				}
				
				list.add(item);
				num++;
			}
			
			s.close();
			MonthlyDiscount ret[] = list.toArray(new MonthlyDiscount[num]);
			return ret;
		}
		
		
		
		
		//with one parameter line number 260
		public Advance[] getAdvancesIssued1(String OutletID) throws SQLException{
			Statement s = c.createStatement();
			
			List <Advance>list = new ArrayList<Advance>();
			int num = 0;
			
			
			ResultSet rs = s.executeQuery("SELECT sp.posted_on, spd.posting_id, spd.debit, spd.remarks FROM sampling_posting_accounts spd, sampling_posting sp where sp.posting_id = spd.posting_id and spd.posting_type = 1 and spd.outlet_id = "+OutletID);
			while(rs.next()){
				Advance item = new Advance();
				
				item.DATE = rs.getDate(1);
				item.AMOUNT = rs.getDouble(3);
				item.APPROVAL_ID = rs.getLong(2);
				item.REMARKS = rs.getString(4);
				
				list.add(item);
				num++;
			}
			
			s.close();
			Advance ret[] = list.toArray(new Advance[num]);
			return ret;
			
		}
		
		
		
		//with three parameters 323
		public LedgerTransaction[] getLedger1(String outletID,Date ToDate,Date FromDate) throws SQLException{
			Statement s = c.createStatement();
			System.out.println(ToDate+"-"+FromDate);
			List <LedgerTransaction>list = new ArrayList<LedgerTransaction>();
			int num = 0;
			
			double balance = 0;
			String query="SELECT sp.posting_id, sp.posted_on, sp.posted_by, (select display_name from users where id = sp.posted_by) posted_by_name, spd.debit, spd.credit, spd.remarks, spd.posting_type, (select label from sampling_posting_types where id = spd.posting_type) posting_type_label FROM sampling_posting sp, sampling_posting_accounts spd where sp.posting_id = spd.posting_id and spd.outlet_id = "+outletID+"   order by sp.posted_on";
			//ResultSet rs = s.executeQuery("SELECT sp.posting_id, sp.posted_on, sp.posted_by, (select display_name from users where id = sp.posted_by) posted_by_name, spd.debit, spd.credit, spd.remarks, spd.posting_type, (select label from sampling_posting_types where id = spd.posting_type) posting_type_label FROM sampling_posting sp, sampling_posting_accounts spd where sp.posting_id = spd.posting_id and spd.outlet_id = "+OUTLET.ID+" order by sp.posted_on");
			System.out.println(query);
			ResultSet rs = s.executeQuery(query);
			while(rs.next()){
				
				LedgerTransaction item = new LedgerTransaction();
				
				item.POSTING_ID = rs.getLong(1);
				item.DATE = rs.getTimestamp(2);
				item.POSTED_BY = rs.getLong("posted_by");
				item.POSTED_BY_NAME = rs.getString("posted_by_name");
				item.POSTING_TYPE = rs.getLong("posting_type");
				item.POSTING_TYPE_LABEL = rs.getString("posting_type_label");
				item.DEBIT = rs.getDouble("debit");
				item.CREDIT = rs.getDouble("credit");
				item.REMARKS = rs.getString("remarks");
				
				balance = balance + (item.DEBIT - item.CREDIT);
				item.BALANCE = balance;
				
				list.add(item);
				num++;
			}
			
			s.close();
			LedgerTransaction ret[] = list.toArray(new LedgerTransaction[num]);
			return ret;
			
		}
		
		//with one paramter 414
		public InventoryPackage[] getDiscountPackageListWithBrands1(String OutletID) throws SQLException{
			
			Statement s = c.createStatement();
			
			List<InventoryPackage> packages = new ArrayList<InventoryPackage>();
			
			int num = 0;
			ResultSet rs = s.executeQuery("select spc.package, spc.brand_id, ip.label, ifnull((select label from inventory_brands where id = spc.brand_id),'All') brand_label from sampling_percase spc, sampling s, inventory_packages ip where s.sampling_id = spc.sampling_id and spc.package = ip.id and s.outlet_id = "+OutletID+" group by spc.package, spc.brand_id order by ip.sort_order, spc.brand_id");
			while(rs.next()){
				InventoryPackage item = new InventoryPackage();
				
				item.ID = rs.getInt(1);
				item.LABEL = rs.getString(3);
				item.BRAND_ID = rs.getInt(2);
				item.BRAND_LABEL = rs.getString(4);

				packages.add(item);
				
				num++;
			}
			
			s.close();
			
			return packages.toArray(new InventoryPackage[num]);
		}
		
		
		//With four parameter at line number 462
		public double[] getSalesByPackage(int PackageID, int month, int year,String OutletID) throws SQLException{
				
				Statement s = c.createStatement();
				
				double qty = 0;
				double converted = 0;
				
				ResultSet rs = s.executeQuery("select ip.id, ip.label, (SELECT sum(ss.quty_quant) FROM sap_sales ss where ss.outlet_id = "+OutletID+" and  ss.month_zmonth = "+month+" and ss.year_zyear="+year+" and material_matnr in (select sap_code from inventory_products where package_id = ip.id)) qty, converted_cases_per_package from inventory_packages ip where ip.id = "+PackageID);
				if (rs.first()){
					qty = rs.getDouble("qty");
					converted = qty * rs.getDouble("converted_cases_per_package");
				}
				
				s.close();
				
				double ret[] = new double[2];
				ret[0] = qty;
				ret[1] = converted;
				
				return ret;
			}
			
			
			
			
			//with one parameter at 498
		public double getTotalDiscount1(String OutletID) throws SQLException{
			Statement s = c.createStatement();
			
			double ret = 0;
			ResultSet rs = s.executeQuery("SELECT sum(sampling_amount) FROM sampling_monthly_approval where outlet_id = "+OutletID);
			if (rs.first()){
				ret = rs.getDouble(1);
			}
			
			s.close();
			
			return ret;
		}
		
		//with paramter at 526
		public double getAverageDiscount1(String OutletID) throws SQLException{
			Statement s = c.createStatement();
			
			double ret = 0;
			ResultSet rs = s.executeQuery("SELECT avg(sampling_amount) FROM sampling_monthly_approval where outlet_id = "+OutletID);
			if (rs.first()){
				ret = rs.getDouble(1);
			}
			
			s.close();
			
			return ret;
		}
		
		//with one parameter at 622
		public double getDiscountOfMonth1(Date date,String OutletID) throws SQLException{
			Statement s = c.createStatement();
			
			double ret = 0;
			ResultSet rs = s.executeQuery("SELECT sampling_amount FROM sampling_monthly_approval where outlet_id = "+OutletID+" and month = "+Utilities.getSQLDate(date));
			if (rs.first()){
				ret = rs.getDouble(1);
			}
			
			s.close();
			
			return ret;
		}
		
		
		//Date of Discount with one parameter 651
		public Date getLastDateOfDiscount1(String OutletID) throws SQLException{
			Statement s = c.createStatement();
			
			Date ret = null;
			ResultSet rs = s.executeQuery("SELECT month FROM sampling_monthly_approval where outlet_id = "+OutletID+" order by month desc limit 1");
			if (rs.first()){
				ret = rs.getDate(1);
			}
			
			s.close();
			
			return ret;
		}
		
		
		//With OutletID at 737	
		public double getFixedDiscountAmount1(Date EndDate,String OutletID) throws SQLException{
			
			Statement s = c.createStatement();
			Statement s2 = c.createStatement();
			
			double ret = 0;
			boolean isFixedValid = false;
			
			ResultSet rs = s.executeQuery("SELECT s.fixed_company_share, s.fixed_valid_from, s.fixed_valid_to, s.fixed_company_share_offpeak, s.sampling_id from sampling s where s.outlet_id = "+OutletID+" and s.activated_on <= cast("+Utilities.getSQLDate(EndDate)+" as datetime) and s.deactivated_on >= cast("+Utilities.getSQLDate(EndDate)+" as datetime)");
			if (rs.first()){
				
				Date FixedValidFrom = rs.getDate("fixed_valid_from");
			  	Date FixedValidTo = rs.getDate("fixed_valid_to");
			  	
			  	if (FixedValidFrom != null && FixedValidTo != null){
				  	if (FixedValidFrom.before(EndDate) && FixedValidTo.after(EndDate)){
				  		ret = rs.getDouble(1);
				  		isFixedValid = true;
				  	}
			  	}
			  	
			  	
			  	// Off Peak
			  	
			  	boolean isFixedOffpeak = false;
			  	
			  	int month = Utilities.getMonthNumberByDate(EndDate);
			  	int year = Utilities.getYearByDate(EndDate);
			  	
			  	ResultSet rs10 = s2.executeQuery("select * from sampling_offpeak_months where month = "+month+" and year = "+year);
			  	if (rs10.first()){
					isFixedOffpeak = true;
			  	}
			  	
			  	if (isFixedValid == true){
			  		if (isFixedOffpeak == false){
					  	ret = rs.getDouble("fixed_company_share");
			  		}else{
					  	ret = rs.getDouble("fixed_company_share_offpeak");
			  		}
			  	}
			  	
			  	// Sales Threshold
			  	
			  	double SalesConvertedCases = getTotalSalesConvertedCases(month, year);
			  	
			  	boolean isThresholdActive = false;
			  	
			  	double ThresholdDiscount = 0;
			  	ResultSet rs2 = s2.executeQuery("SELECT discount FROM sampling_fixed_threshold where converted_sales >= "+SalesConvertedCases+" and sampling_id = "+rs.getString("sampling_id")+" order by percentage limit 1");
			  	if (rs2.first()){
			  		ThresholdDiscount = rs2.getDouble(1);
			  		isThresholdActive = true;
			  	}
			  	
			  	if (isThresholdActive == true){
			  		
			  		if (ThresholdDiscount < ret){
			  			ret = ThresholdDiscount;
			  		}
			  		
			  	}
			  	
			  	
			}
			
			
			s2.close();
			s.close();
			return ret;
		}

		
		//With one parameter at 1068
		public double[] getPerCaseDiscountAmountAndRate1(Date EndDate, int PackageID, int BrandID,String OutletID) throws SQLException{
			Statement s = c.createStatement();
			
			int month = Utilities.getMonthNumberByDate(EndDate);
			int year = Utilities.getYearByDate(EndDate);
					
			double rate = 0;
			double discount = 0;
			
			ResultSet rs = s.executeQuery("SELECT sp.company_share, sp.valid_from, sp.valid_to, sp.package, sp.brand_id, "+
						"ifnull((case sp.brand_id when 0 then("+
						"(SELECT sum(ss.quty_quant) FROM sap_sales ss where ss.outlet_id = "+OutletID+" and  ss.month_zmonth = "+month+" and ss.year_zyear="+year+" and material_matnr in (select sap_code from inventory_products where package_id = sp.package))"+
						")else("+
						"(SELECT sum(ss.quty_quant) FROM sap_sales ss where ss.outlet_id = "+OutletID+" and  ss.month_zmonth = "+month+" and ss.year_zyear="+year+" and material_matnr in (select sap_code from inventory_products where package_id = sp.package and brand_id = sp.brand_id))"+
						") end),0) as qty"+
						" from sampling s, sampling_percase sp where s.sampling_id = sp.sampling_id and s.outlet_id = "+OutletID+" and sp.package = "+PackageID+" and s.activated_on <= cast("+Utilities.getSQLDate(EndDate)+" as datetime) and s.deactivated_on >= cast("+Utilities.getSQLDate(EndDate)+" as datetime) and sp.package = "+PackageID+" and sp.brand_id = "+BrandID);
			while (rs.next()){
				
				Date ValidFrom = rs.getDate("valid_from");
			  	Date ValidTo = rs.getDate("valid_to");
			  	
			  	if (ValidFrom != null && ValidTo != null){
				  	if (ValidFrom.before(EndDate) && ValidTo.after(EndDate)){
				  		rate = rs.getDouble(1);
				  		discount += (rate * rs.getDouble("qty"));
				  	}
			  	}
			}
			
			s.close();
			
			double ret[] = new double[2];
			ret[0] = rate;
			ret[1] = discount;
			return ret;
		}
	
	
	
	
	
	//////////////////////////////////////////////////////////////
	
}
