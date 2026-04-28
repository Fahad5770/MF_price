<%@page import="java.util.Date"%>
<%@page import="com.pbc.outlet.OutletDashboard"%>
<%@page import="com.pbc.inventory.PromotionItem"%>
<%@page import="com.pbc.inventory.Product"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@page import="com.pbc.inventory.SalesPosting"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.common.Distributor"%>


<%
/*

sdfsdfsdfsdf
Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();

ResultSet rs = s.executeQuery("select distinct invoice_id from inventory_sales_dispatch_adjusted_products");
while(rs.next()){
	boolean posted = SalesPosting.post(rs.getLong(1), 2431);
}
ds.dropConnection();
*/


//out.println(request.getRemoteAddr());
/*
Datasource ds = new Datasource();
ds.createConnection();
ds.startTransaction();

try{ 
	
Statement s = ds.createStatement();
Statement s2 = ds.createStatement();

ResultSet rs = s2.executeQuery("select approval_id, outlet_id, month FROM pep.sampling_monthly_approval where approval_id not in (select distinct approval_id from sampling_monthly_approval_percase) and approval_id not in (select distinct approval_id from sampling_monthly_approval_fixed) and status_id = 1");
while(rs.next()){
	long ApprovalID = rs.getLong(1);
	long OutletID = rs.getLong(2);
	Date SamplingDate = rs.getDate(3);
	
	OutletDashboard op = new OutletDashboard();
	op.setOutletID(OutletID);
	
	double FixedSamplingAmount = op.getFixedDiscountAmount(SamplingDate);
	double FixedDeductionAgainstAdvance = op.getFixedDiscountDeductionAmount(SamplingDate);
	double FixedNetPayable = FixedSamplingAmount - FixedDeductionAgainstAdvance;
	
	if (FixedSamplingAmount != 0 || FixedDeductionAgainstAdvance != 0 || FixedNetPayable != 0){
		s.executeUpdate("INSERT INTO sampling_monthly_approval_fixed (approval_id, sampling_amount, deduction_against_advance, net_payable, is_batch) VALUES ("+ApprovalID+", "+FixedSamplingAmount+", "+FixedDeductionAgainstAdvance+", "+FixedNetPayable+",1)");
	}

	ResultSet rs6 = op.getPerCaseDiscountResultSet(SamplingDate);
	while(rs6.next()){
		
		int PackageID = rs6.getInt("package");
		int BrandID = rs6.getInt("brand_id");
		double CompanyShare = rs6.getDouble("company_share");
		double DeductionTerm = rs6.getDouble("deduction_term");
		int Sales = rs6.getInt("qty");
		
		double PerCaseSamplingAmount = Sales * CompanyShare;
		double PerCaseDeductionAgainstAdvance = Sales * DeductionTerm;
		double PerCaseNetPayable = PerCaseSamplingAmount - PerCaseDeductionAgainstAdvance;
		
		if (Sales != 0 || PerCaseNetPayable != 0 || PerCaseSamplingAmount != 0 || PerCaseDeductionAgainstAdvance != 0){
			s.executeUpdate("INSERT INTO sampling_monthly_approval_percase (approval_id,package_id, brand_id, company_share, deduction_term, sales, sampling_amount, deduction_against_advance, net_payable, is_batch) VALUES ("+ApprovalID+","+PackageID+", "+BrandID+", "+CompanyShare+", "+DeductionTerm+", "+Sales+", "+PerCaseSamplingAmount+", "+PerCaseDeductionAgainstAdvance+", "+PerCaseNetPayable+",1)");
		}
	}
	
	op.close();
	
	
}

ds.commit();
}catch(Exception e){
	ds.rollback();
}finally{
	ds.dropConnection();
}


*/


%>

	       							