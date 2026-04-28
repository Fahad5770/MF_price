<%@page import="com.pbc.util.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.sampling.SamplingPosting"%>


<%!

class Package {
	
	int PACKAGE_ID;
	double COMPANY_SHARE;
	double DISTRIBUTOR_SHARE;
	double DEDUCTION_TERM;
	int BRAND_ID = 0;
}
%>
<% 

try{
	Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
}catch (Exception e){
	e.printStackTrace();
}
Connection oc = DriverManager.getConnection("jdbc:odbc:sampling","","");
Statement os = oc.createStatement();
Statement os2 = oc.createStatement();

StringBuffer af = new StringBuffer();

Datasource ds = new Datasource();
ds.createConnection();
Statement s = ds.createStatement();
Statement s1 = ds.createStatement();



ResultSet rs = os.executeQuery("select * from [datasheet$] order by 3, 1");
while(rs.next()){
	
	int sn = rs.getInt(1);
	Date date = rs.getDate(2);
	long OutletID = rs.getLong(3);
	
	int LastNo = 0;
	
	ResultSet rs5 = os2.executeQuery("select max(Sn) from [datasheet$] where `SD Outlet ID` = "+OutletID);
	while(rs5.next()){
		LastNo = rs5.getInt(1);
	}
	

	Date FromDate = rs.getDate(4);
	Date ToDate = rs.getDate(5);
	double AdvanceCompanyShare = rs.getDouble(6);
	double AdvanceDistributorShare = rs.getDouble(7);
	double FixedCompanyShare = rs.getDouble(9);
	double FixedDistributorShare = rs.getDouble(10);
	double FixedDeductionTerm = rs.getDouble(11);
	
	List<Package> items = new ArrayList<Package>();
	
	Package pkg1 = new Package();
	pkg1.PACKAGE_ID = 11;
	pkg1.COMPANY_SHARE = rs.getDouble(12);
	pkg1.DISTRIBUTOR_SHARE = rs.getDouble(13);
	pkg1.DEDUCTION_TERM = rs.getDouble(14);
	items.add(pkg1);
	
	Package pkg2 = new Package();
	pkg2.PACKAGE_ID = 1;
	pkg2.COMPANY_SHARE = rs.getDouble(15);
	pkg2.DISTRIBUTOR_SHARE = rs.getDouble(16);
	pkg2.DEDUCTION_TERM = rs.getDouble(17);
	items.add(pkg2);
	
	Package pkg3 = new Package();
	pkg3.PACKAGE_ID = 2;
	pkg3.COMPANY_SHARE = rs.getDouble(18);
	pkg3.DISTRIBUTOR_SHARE = rs.getDouble(19);
	pkg3.DEDUCTION_TERM = rs.getDouble(20);
	items.add(pkg3);
	
	Package pkg4 = new Package();
	pkg4.PACKAGE_ID = 3;
	pkg4.COMPANY_SHARE = rs.getDouble(21);
	pkg4.DISTRIBUTOR_SHARE = rs.getDouble(22);
	pkg4.DEDUCTION_TERM = rs.getDouble(23);
	items.add(pkg4);
	
	Package pkg5 = new Package();
	pkg5.PACKAGE_ID = 5;
	pkg5.COMPANY_SHARE = rs.getDouble(24);
	pkg5.DISTRIBUTOR_SHARE = rs.getDouble(25);
	pkg5.DEDUCTION_TERM = rs.getDouble(26);
	items.add(pkg5);
	
	Package pkg6 = new Package();
	pkg6.PACKAGE_ID = 6;
	pkg6.COMPANY_SHARE = rs.getDouble(27);
	pkg6.DISTRIBUTOR_SHARE = rs.getDouble(28);
	pkg6.DEDUCTION_TERM = rs.getDouble(29);
	items.add(pkg6);
	
	Package pkg7 = new Package();
	pkg7.PACKAGE_ID = 8;
	pkg7.COMPANY_SHARE = rs.getDouble(30);
	pkg7.DISTRIBUTOR_SHARE = rs.getDouble(31);
	pkg7.DEDUCTION_TERM = rs.getDouble(32);
	items.add(pkg7);
	
	Package pkg8 = new Package();
	pkg8.PACKAGE_ID = 9;
	pkg8.COMPANY_SHARE = rs.getDouble(33);
	pkg8.DISTRIBUTOR_SHARE = rs.getDouble(34);
	pkg8.DEDUCTION_TERM = rs.getDouble(35);
	items.add(pkg8);
	
	Package pkg14 = new Package();
	pkg14.PACKAGE_ID = 2;
	pkg14.BRAND_ID = 12;
	pkg14.COMPANY_SHARE = rs.getDouble(36);
	pkg14.DISTRIBUTOR_SHARE = rs.getDouble(37);
	pkg14.DEDUCTION_TERM = rs.getDouble(38);
	items.add(pkg14);
	
	Package pkg15 = new Package();
	pkg15.PACKAGE_ID = 6;
	pkg15.BRAND_ID = 12;
	pkg15.COMPANY_SHARE = rs.getDouble(39);
	pkg15.DISTRIBUTOR_SHARE = rs.getDouble(40);
	pkg15.DEDUCTION_TERM = rs.getDouble(41);
	items.add(pkg15);
	
	Package pkg16 = new Package();
	pkg16.PACKAGE_ID = 4;
	pkg16.COMPANY_SHARE = rs.getDouble(42);
	pkg16.DISTRIBUTOR_SHARE = rs.getDouble(43);
	pkg16.DEDUCTION_TERM = rs.getDouble(44);
	items.add(pkg16);
	
	
	String OutletName = "";
	String BusinessType = "";
	String address = "";
	String region = "";
	long asm = 0;
	long cr = 0;
	String market = "";
	String vehicle = "";
	
	ResultSet rs1 = s.executeQuery("SELECT Outlet_Name, Bsi_Name, Address, Region, ASM_ID, CR_ID, Market_Name, Vehicle, Latitude, Longitude FROM pbc.outletmaster where Outlet_ID = "+OutletID);
	if (rs1.first()){
		OutletName = rs1.getString(1);
		BusinessType = rs1.getString(2);
		address = rs1.getString(3);
		region = rs1.getString(4);
		asm = rs1.getLong(5);
		cr = rs1.getLong(6);
		market = rs1.getString(7);
		vehicle = rs1.getString(8);
	}
	
	long RequestID = 0;
	long SamplingID = 0;
	
	Workflow wf = new Workflow();
	
	RequestID = wf.createRequest(1, 1, 1, 6, "System Generated");
	wf.doStepAction(RequestID, 2, true, 1, 6, "System Generated");
	wf.close();
	
	
	
	String query = "INSERT INTO sampling (request_id, outlet_id, outlet_name, business_type, address, region, asm, cr, market, vehicle, advance_company_share, advance_agency_share, advance_valid_from, advance_valid_to, fixed_company_share, fixed_agency_share, fixed_deduction_term, fixed_valid_from, fixed_valid_to, active, deactivated_on)"+
			"VALUES"+
			"("+RequestID+", '"+OutletID+"', '"+OutletName+"', '"+BusinessType+"', '"+address+"', '"+region+"', '"+asm+"', '"+cr+"', '"+market+"', '"+vehicle+"', '"+AdvanceCompanyShare+"', '"+AdvanceDistributorShare+"', "+Utilities.getSQLDate(FromDate)+", "+Utilities.getSQLDate(ToDate)+", '"+FixedCompanyShare+"', '"+FixedDistributorShare+"', '"+FixedDeductionTerm+"', "+Utilities.getSQLDate(FromDate)+", "+Utilities.getSQLDate(ToDate)+", 0, now())";
	
	
	if (sn == LastNo){
		query = "INSERT INTO sampling (request_id, outlet_id, outlet_name, business_type, address, region, asm, cr, market, vehicle, advance_company_share, advance_agency_share, advance_valid_from, advance_valid_to, fixed_company_share, fixed_agency_share, fixed_deduction_term, fixed_valid_from, fixed_valid_to, active, deactivated_on)"+
				"VALUES"+
				"("+RequestID+", '"+OutletID+"', '"+OutletName+"', '"+BusinessType+"', '"+address+"', '"+region+"', '"+asm+"', '"+cr+"', '"+market+"', '"+vehicle+"', '"+AdvanceCompanyShare+"', '"+AdvanceDistributorShare+"', "+Utilities.getSQLDate(FromDate)+", "+Utilities.getSQLDate(ToDate)+", '"+FixedCompanyShare+"', '"+FixedDistributorShare+"', '"+FixedDeductionTerm+"', "+Utilities.getSQLDate(FromDate)+", "+Utilities.getSQLDate(ToDate)+", 1, null)";
	}
	
	int updated = s1.executeUpdate(query);
	
	ResultSet rs2 = s1.executeQuery("select LAST_INSERT_ID()");
	if (rs2.first()){
		SamplingID = rs2.getLong(1);
	}	
	
	for (int i = 0; i < items.size(); i++){
		
		Package row = items.get(i);
		
		if (row.COMPANY_SHARE != 0 || row.DISTRIBUTOR_SHARE != 0){
			s.executeUpdate("INSERT INTO sampling_percase (sampling_id,package,brand_id,agency_share,company_share,deduction_term,valid_from,valid_to) VALUES ("+SamplingID+", "+row.PACKAGE_ID+", "+row.BRAND_ID+","+row.DISTRIBUTOR_SHARE+", "+row.COMPANY_SHARE+", "+row.DEDUCTION_TERM+","+Utilities.getSQLDate(FromDate)+", "+Utilities.getSQLDate(ToDate)+")");
		}
		
	}
	
	if (AdvanceCompanyShare > 0){
		SamplingPosting sp = new SamplingPosting();
		sp.postNewAdvanceSampling(SamplingID, OutletID, 1, AdvanceCompanyShare, "Advance Sampling (Imported)");
		sp.close();
	}
	
	
}

out.print(af.toString());

os2.close();
os.close();
oc.close();

s1.close();
s.close();
ds.dropConnection();

/*
Datasource ds = new Datasource();

ds.createConnection();

Statement s = ds.createStatement();
Statement s2 = ds.createStatement();


out.print("done");

s2.close();
s.close();

ds.dropConnection();
*/
%>