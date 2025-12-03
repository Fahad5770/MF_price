<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%




boolean noFilters = false;

String HeaderType = Utilities.filterString(request.getParameter("HeaderType"),1,100);
if (HeaderType == null){
	HeaderType = "Standard";
}
/*
DistribtuorDashboard
OrderBookerDashboard
Standard
*/

String DistributorName = Utilities.filterString(request.getParameter("DistributorName"), 1, 100);
long DistributorCode = Utilities.parseLong(request.getParameter("DistributorCode"));
int PageIndex = Utilities.parseInt(request.getParameter("PageIndex"));

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

long ForcedUniqueVVID = Utilities.parseLong(request.getParameter("UUID"));

long UniqueVVID = Utilities.getUniqueVoucherID(SessionUserID);

if (ForcedUniqueVVID != 0){
	UniqueVVID = ForcedUniqueVVID;
}

String HeaderBackURL = "ReportCenterMain.jsp?"+Math.random()+"="+Math.random();

int FeatureID = 0;
String ReportURL="";
String ReportTitle="";
String DefaultFilter="";





 
session.setAttribute( UniqueVVID+"_SR1StartDate", new java.util.Date() );
session.setAttribute( UniqueVVID+"_SR1EndDate", new java.util.Date() );


int ReportID = Utilities.parseInt(request.getParameter("ReportID"));


if (ReportID == 23){
	 DefaultFilter = "LoadDate(\'LoadDateRangeHyperlink1\')";
}else if(ReportID==114 || ReportID==113 || ReportID==110 || ReportID==141){
	DefaultFilter = "LoadDateTimeRange(\'LoadDateTimeRangeHyperlink\')";
	
	session.setAttribute( UniqueVVID+"_SR1StartDateTime", new java.util.Date() );	
	session.setAttribute( UniqueVVID+"_SR1StartDateTimeHour", 6 );
	session.setAttribute( UniqueVVID+"_SR1StartDateTimeMinutes", 0 );	
	
	
	session.setAttribute( UniqueVVID+"_SR1EndDateTime",  Utilities.parseDate(Utilities.getDisplayDateNext(new java.util.Date())));	
	session.setAttribute( UniqueVVID+"_SR1EndDateTimeHour", 6 );
	session.setAttribute( UniqueVVID+"_SR1EndDateTimeMinutes", 0 );
		
}else{
	 DefaultFilter = "LoadDateRange(\'LoadDateRangeHyperlink\')";
}

//System.out.println(ReportID);
 if (ReportID == 15){
	
	FeatureID=100; 
	ReportURL="ReportCenterR101.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R101 - Sales Summary";
	
}else  if (ReportID == 16){
	
	FeatureID=101; 
	ReportURL="ReportCenterR102.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R102 - Discounted Sales Summary";
	
}else  if (ReportID == 17){
	
	FeatureID=102; 
	ReportURL="ReportCenterR103.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R103 - Sales / Distributor Centric PJP";
	DefaultFilter = "LoadAllDistributors(\'LoadAllDistributorsHyperlink\')";
	
}else  if (ReportID == 18){
	
	FeatureID=103; 
	ReportURL="ReportCenterR104.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R104 - Promotions / Package and Distributor";
	
}else  if (ReportID == 19){
	
	FeatureID=104; 
	ReportURL="ReportCenterR105.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R105 - Promotions / Package and Distributor";
	
}else  if (ReportID == 20){
	
	FeatureID=105; 
	ReportURL="ReportCenterR106.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R106 - Sales / Package and Outlet";
	DefaultFilter = "LoadAllDistributors(\'LoadAllDistributorsHyperlink\')";
	
}else  if (ReportID == 21){
	
	FeatureID=106; 
	ReportURL="ReportCenterR107.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R107 - Promotions / Package and Outlet";
	DefaultFilter = "LoadAllDistributors(\'LoadAllDistributorsHyperlink\')";
	
}else  if (ReportID == 22){
	
	FeatureID=107; 
	ReportURL="ReportCenterR108.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R108 - Sales / Package and Outlet";
	DefaultFilter = "LoadAllDistributors(\'LoadAllDistributorsHyperlink\')";
	
}else  if (ReportID == 23){
	
	FeatureID=108; 
	ReportURL="ReportCenterR109.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R109 - KPI / Orderbooker";	
	
	DefaultFilter = "LoadDate(\'LoadDateRangeHyperlink1\')";
	
	/*Date StartDate = new Date();
	Calendar cc = Calendar.getInstance();   // this takes current date
    cc.set(Calendar.DAY_OF_MONTH, 1);
    StartDate = cc.getTime();
	
	session.setAttribute( UniqueVVID+"_SR1StartDate", StartDate );
	*/
}else  if (ReportID == 24){
	
	FeatureID=109; 
	ReportURL="ReportCenterR110.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R110 - Distributor Performance";	
	
}else  if (ReportID == 25){
	
	FeatureID=111; 
	ReportURL="ReportCenterR111.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R111 - Lifting Report";	
	//DefaultFilter = "LoadAllDistributors(\'LoadAllDistributorsHyperlink\')"; 
	DefaultFilter = "LoadAllDistributorsLifting(\'LoadAllDistributorsLiftingHyperlink\')";
	
}else  if (ReportID == 26){
	
	FeatureID=112; 
	ReportURL="ReportCenterR112.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R112 - Order based Sales Summary";	
	
}else  if (ReportID == 27){
	
	FeatureID=113; 
	ReportURL="ReportCenterR113.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R113 - Dispatch based Sales Summary";	
	
}else  if (ReportID == 28){
	
	FeatureID=114; 
	ReportURL="ReportCenterR114.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R114 - Dispatch Summary";
}else  if (ReportID == 29){
	
	FeatureID=115; 
	ReportURL="ReportCenterR115.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R115 - Stock Summary";
}else  if (ReportID == 30){
	
	FeatureID=116; 
	ReportURL="ReportCenterR116.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R116 - Order Booker Activity";
	DefaultFilter = "LoadAllOrderBookers(\'LoadAllOrderBookersHyperlink\')"; 
}else  if (ReportID == 31){
	
	FeatureID=117; 
	ReportURL="ReportCenterR117.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R117 - Backorder Summary";	
	
}else  if (ReportID == 32){
	
	FeatureID=118; 
	ReportURL="ReportCenterR118.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R118 - Lifting Summary";	
	
}else  if (ReportID == 33){
	
	FeatureID=120; 
	ReportURL="ReportCenterR119.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R119 - Lifting / Package and Distributor";	
	
}else  if (ReportID == 34){
	
	FeatureID=121; 
	ReportURL="ReportCenterR120.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R120 - Orderbooker Tracking";
	//DefaultFilter = "LoadAllOrderBookers(\'LoadAllOrderBookersHyperlink\')";
	
}else  if (ReportID == 35){
	
	FeatureID=122; 
	ReportURL="ReportCenterR121.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R121 - Lifting Summary / Invoice Value";	
	
}else  if (ReportID == 36){
	
	FeatureID=123; 
	ReportURL="ReportCenterR122.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R122 - Lifting Bifurcation";	
	
}else  if (ReportID == 37){
	
	FeatureID=124; 
	ReportURL="ReportCenterR123.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R123 - Timestamp Analysis";
	
	
	
}else  if (ReportID == 38){
	
	FeatureID=125; 
	ReportURL="ReportCenterR124.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R124 - Order Booker Performance";
	DefaultFilter = "LoadDateRangeNew(\'LoadDateRangeHyperlinkNew\')";
	
	Date StartDate = new Date();
	Calendar cc = Calendar.getInstance();   // this takes current date
    cc.set(Calendar.DAY_OF_MONTH, 1);
    StartDate = cc.getTime();
	
	session.setAttribute( UniqueVVID+"_SR1StartDate", StartDate );
	
}else  if (ReportID == 39){
	
	FeatureID=126;
	ReportURL="ReportCenterR125.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R125 - Time & Distance Analysis";	
	
}else  if (ReportID == 40){
	
	FeatureID=128;
	ReportURL="ReportCenterR126.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R126 - Outlet Profile";	
	DefaultFilter = "LoadAllOutletsManual(\'LoadAllOutletsHyperlink1\')";
	
}else  if (ReportID == 41){
	
	FeatureID=129;
	ReportURL="ReportCenterR127.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R127 - Complaint List";	
	
}else  if (ReportID == 42){
	
	FeatureID=130;
	ReportURL="ReportCenterR128.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R128 - PJP List";	
	DefaultFilter = "LoadAllDistributors(\'LoadAllDistributorsHyperlink\')";
}else  if (ReportID == 43){
	
	FeatureID=131;
	ReportURL="ReportCenterR129.jsp?UniqueSessionID="+UniqueVVID;
	//ReportTitle = "R129 - Lifting Average Price";	
	ReportTitle = "R129 - Outlet Inactivity Analysis";	
	
	session.setAttribute( UniqueVVID+"_SR1SelectedDaysGreaterThan", 30 );	
	session.setAttribute( UniqueVVID+"_SR1SelectedDaysLessThan", 60 );
	
	DefaultFilter = "LoadDaysRange(\'LoadDaysRangeHyperlinkNew\')";	
}else  if (ReportID == 44){
	
	FeatureID=132;
	ReportURL="ReportCenterR130.jsp?UniqueSessionID="+UniqueVVID;
	//ReportTitle = "R129 - Lifting Average Price";	
	ReportTitle = "R130 - Outlet Inactivity Analysis";	
	
	session.setAttribute( UniqueVVID+"_SR1SelectedDaysGreaterThan", 90 );	
	session.setAttribute( UniqueVVID+"_SR1SelectedDaysLessThan", 9999 );
	
	DefaultFilter = "LoadDaysRange(\'LoadDaysRangeHyperlinkNew\')";	
}else  if (ReportID == 45){
	
	FeatureID=133;
	ReportURL="ReportCenterR131.jsp?UniqueSessionID="+UniqueVVID;
	//ReportTitle = "R129 - Lifting Average Price";	
	ReportTitle = "R131 - Lifting Average Price";	
		
}else  if (ReportID == 46){
	
	FeatureID=134;
	ReportURL="ReportCenterR132.jsp?UniqueSessionID="+UniqueVVID;	
	ReportTitle = "R132 - Sales versus Targets";
	DefaultFilter = "LoadDateRangeNew(\'LoadDateRangeHyperlinkNew\')";
	
	Date StartDate = new Date();
	Calendar cc = Calendar.getInstance();   // this takes current date
    cc.set(Calendar.DAY_OF_MONTH, 1);
    StartDate = cc.getTime();
	
	session.setAttribute( UniqueVVID+"_SR1StartDate", StartDate );
}else  if (ReportID == 47){
	
	FeatureID=136;
	ReportURL="ReportCenterR133.jsp?UniqueSessionID="+UniqueVVID;	
	ReportTitle = "R133 - Lifting versus Targets";
	DefaultFilter = "LoadDateRangeNew(\'LoadDateRangeHyperlinkNew\')";
	
	Date StartDate = new Date();
	Calendar cc = Calendar.getInstance();   // this takes current date
    cc.set(Calendar.DAY_OF_MONTH, 1);
    StartDate = cc.getTime();
	
	session.setAttribute( UniqueVVID+"_SR1StartDate", StartDate );
}else  if (ReportID == 48){
	
	FeatureID=135;
	ReportURL="CRMComplaintReport.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "Complaint Assignment";
	HeaderBackURL = "home.jsp";
}else  if (ReportID == 49){
	
	FeatureID=137;
	ReportURL="ReportCenterR134.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R134 - Complaint List / Sales";	
	session.setAttribute( UniqueVVID+"_SR1StartDate", null );
	session.setAttribute( UniqueVVID+"_SR1EndDate", null );
	
}else  if (ReportID == 50){
	
	FeatureID=139;
	ReportURL="ReportCenterR135.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R135 - Complaint List / TOT";	
	session.setAttribute( UniqueVVID+"_SR1StartDate", null );
	session.setAttribute( UniqueVVID+"_SR1EndDate", null );
	
}else  if (ReportID == 51){
	
	FeatureID=140;
	ReportURL="CRMComplaintVerificationReport.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "Complaint Verification";
	HeaderBackURL = "home.jsp";
	
}else if (ReportID == 52){
	
	FeatureID=141;
	ReportURL="ReportCenterR136.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R136 - Complaint Status";
	DefaultFilter = "LoadComplaintID(\'LoadComplaintIDHyperlinkNew\')";
	
}else  if (ReportID == 53){
	
	FeatureID=143;
	ReportURL="ReportCenterR137.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "Complaint Status";
	DefaultFilter = "LoadAllDistributors(\'LoadAllDistributorsHyperlink\')";
		
}else  if (ReportID == 54){
	
	FeatureID=144;
	ReportURL="CRMTaskReport.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "Task Report";
	
		
}else  if (ReportID == 55){
	
	FeatureID=145;
	ReportURL="ReportCenterR138.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "Hand to Hand Discount Summary";
	
		
}else  if (ReportID == 56){
	
	FeatureID=146;
	ReportURL="ReportCenterR139.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "Hand to Hand Discount Detail";
	
		
}
else  if (ReportID == 57){
	
	FeatureID=147;
	ReportURL="ReportCenterR140.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R140 - Complaint List / TOT";	
	session.setAttribute( UniqueVVID+"_SR1StartDate", null );
	session.setAttribute( UniqueVVID+"_SR1EndDate", null );
	
} else  if (ReportID == 58){
	
	FeatureID=149;
	ReportURL="ReportCenterR141.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R141 - Pending Orders";	
	
	
} else  if (ReportID == 59){
	
	FeatureID=151;
	ReportURL="ReportCenterR142.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R142 - Invoices Not Lifted";	
	
	
} else  if (ReportID == 60){
	
	FeatureID=152;
	ReportURL="ReportCenterR143.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R143 - Complaint List / Merchandising";	
	session.setAttribute( UniqueVVID+"_SR1StartDate", null );
	session.setAttribute( UniqueVVID+"_SR1EndDate", null );
	
} else  if (ReportID == 61){
	
	FeatureID=159;
	ReportURL="ReportCenterR144.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R144 - Invoice Summary";		
}else  if (ReportID == 62){
	
	FeatureID=161; 
	ReportURL="ReportCenterR145.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R145 - Cancelled Orders";	
	
} else  if (ReportID == 63){
	
	FeatureID=162; 
	ReportURL="ReportCenterR146.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R146 - Customer Ledger";	
	
}else  if (ReportID == 64){
	
	FeatureID=163; 
	ReportURL="ReportCenterR147.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R147 - Cash Instrument Ledger";	
	
}else  if (ReportID == 65){
	
	FeatureID=166; 
	ReportURL="ReportCenterR148.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R148 - Cash Instrument Summary";	
	
}else  if (ReportID == 66){
	
	FeatureID=168; 
	ReportURL="ReportCenterR149.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R149 - Payment Posting";		
	DefaultFilter = "LoadAllWarehouse(\'LoadAllWarehouseHyperlink\')";
	
}else  if (ReportID == 67){
	
	FeatureID=171; 
	ReportURL="ReportCenterR150.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R150 - KPI / Distributor";
	
	//DefaultFilter = "LoadAllWarehouse(\'LoadAllWarehouseHyperlink\')";
	
}
else  if (ReportID == 68){
	
	FeatureID=173; 
	ReportURL="ReportCenterR151.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R151 - Cash Receipt Summary";	
}
else  if (ReportID == 69){
	
	FeatureID=176; 
	ReportURL="ReportCenterR152.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R152 - Customer Credit Summary";
	DefaultFilter = "LoadAllHODs(\'LoadAllHODHyperlink\')";	
}
else  if (ReportID == 70){
	
	FeatureID=177; 
	ReportURL="ReportCenterR153.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R153 - Cash Instrument Ledger(Personal)";	
	
}
else  if (ReportID == 71){
	
	FeatureID=178; 
	ReportURL="ReportCenterR154.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R154 - Cash Receipt Summary(Personal)";	
}
else  if (ReportID == 72){
	
	FeatureID=179; 
	ReportURL="ReportCenterR155.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R155 - Posted Invoices";	
} 
else  if (ReportID == 73){
	
	FeatureID=180; 
	ReportURL="ReportCenterR156.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R156 - Customer Status";
	DefaultFilter = "LoadAllHODs(\'LoadAllHODHyperlink\')";
}
else  if (ReportID == 74){
	
	FeatureID=183; 
	ReportURL="ReportCenterR157.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R157 - Posted Invoices(Personal)";
	
} 
else  if (ReportID == 75){
	
	FeatureID=185; 
	ReportURL="ReportCenterR158.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R158 - Cash Receipt Summary";
	
} 
else  if (ReportID == 76){
	
	FeatureID=189; 
	ReportURL="ReportCenterR159.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R159 - Cash Receipt List";	
} 
else  if (ReportID == 77){
	
	FeatureID=190; 
	ReportURL="ReportCenterR160.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R160 - Customer Account Adjustments";	
} 
else  if (ReportID == 78){
	
	FeatureID=191; 
	ReportURL="ReportCenterR161.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R161 - Invoices Not Posted";	
} 
else  if (ReportID == 79){
	
	FeatureID=193; 
	ReportURL="ReportCenterR162.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R162 - Pending Payment Posting";	
	DefaultFilter = "LoadAllWarehouse(\'LoadAllWarehouseHyperlink\')";
}  
else  if (ReportID == 80){
	
	FeatureID=194; 
	ReportURL="ReportCenterR163.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R163 - Sales / Outlet Types";	
	
} 
else  if (ReportID == 81){
	
	FeatureID=195; 
	ReportURL="ReportCenterR164.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R164 - Discounted Outlets";
	DefaultFilter = "LoadAllDiscountType('LoadAllDiscountTypeHyperlink')";
	
}
else  if (ReportID == 82){
	
	FeatureID=196;
	ReportURL="ReportCenterR165.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R165 - Unproductive Outlets";	
	
}
else  if (ReportID == 83){
	
	FeatureID=197;
	ReportURL="ReportCenterR166.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R166 - Manual Sales Analysis";	
	
}
else  if (ReportID == 84){
	
	FeatureID=200;
	ReportURL="ReportCenterR167.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R167 - Active Promotions";
	DefaultFilter = "LoadAllDistributors(\'LoadAllDistributorsHyperlink\')";
}
else  if (ReportID == 85){
	
	FeatureID=201;
	ReportURL="ReportCenterR168.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R168 - Sampling Credit Slip";	
	
}
else  if (ReportID == 86){
	
	FeatureID=202;
	ReportURL="ReportCenterR169.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R169 - Order Booker Sales";
	//DefaultFilter = "LoadAllOrderBookers(\'LoadAllOrderBookersHyperlink\')";
}else  if (ReportID == 87){
	
	FeatureID=205;
	ReportURL="ReportCenterR170.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R170 - One Time Credit Issuance";
	//DefaultFilter = "LoadAllOrderBookers(\'LoadAllOrderBookersHyperlink\')";
}
else  if (ReportID == 88){
	
	FeatureID=204;
	ReportURL="ReportCenterR171.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R171 - Instrument Receipt Summary";
	//DefaultFilter = "LoadAllOrderBookers(\'LoadAllOrderBookersHyperlink\')";
}
else  if (ReportID == 89){
	
	FeatureID=208;
	ReportURL="ReportCenterR172.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R172 - Outlet Planned vs Actual Sales";
	DefaultFilter = "LoadAllRegion('LoadAllRegionHyperlink')";
}
else  if (ReportID == 90){
	
	FeatureID=210;
	ReportURL="ReportCenterR173.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R173 - Order Booker Incentive";	
}
else  if (ReportID == 91){
	
	FeatureID=211;
	ReportURL="ReportCenterR174.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R174 - Active Promotions";
	DefaultFilter = "LoadAllRegion('LoadAllRegionHyperlink')";
}
else  if (ReportID == 92){
	
	FeatureID=212;
	ReportURL="ReportCenterR175.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R175 - Pre-Selling Market Coverage";
	
}
else  if (ReportID == 93){
	
	FeatureID=213;
	ReportURL="ReportCenterR176.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R176 - Upfront Discount";
	
}else  if (ReportID == 94){
	
	FeatureID=216; 
	ReportURL="ReportCenterR177.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R177 - Distributor Ledger [Secondary User]";	
	
}else  if (ReportID == 95){
	
	FeatureID=217; 
	ReportURL="ReportCenterR178.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R178 - Active Promotions";	
	
}else  if (ReportID == 96){
	
	FeatureID=218;
	ReportURL="CRMMarketWatchReport.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "CRM Market Watch";
	
		
}else  if (ReportID == 97){
	
	FeatureID=219;
	ReportURL="ReportCenterR179.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R179 - New Empty Issued";	
	 
		
}else  if (ReportID == 98){
	
	FeatureID=220;
	ReportURL="ReportCenterR180.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R180 - Old Empty Purchase";	
	
		
}else  if (ReportID == 99){
	
	FeatureID=221;
	ReportURL="ReportCenterR181.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R181 - Empty Transactions";	
	
		
}else  if (ReportID == 100){
	
	FeatureID=222;
	ReportURL="ReportCenterR182.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R182 - Empty Reconciliation";	
		
}else  if (ReportID == 101){
	
	FeatureID=223;
	ReportURL="ReportCenterR183.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R183 - Primary Sales vs Targets";	
	DefaultFilter = "LoadDateRangeNew(\'LoadDateRangeHyperlinkNew\')";
		
}else  if (ReportID == 102){
	
	FeatureID=224;
	ReportURL="ResearchReviewMain.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "Research Review";
	
}else  if (ReportID == 103){
	
	FeatureID=225;
	ReportURL="ReportCenterR184.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R184 - Average Price";
	
}else  if (ReportID == 104){
	
	FeatureID=226;
	ReportURL="ReportCenterR185.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R185 - Revenue Per Case";
	
}else  if (ReportID == 105){
	
	FeatureID=227;
	ReportURL="ReportCenterR186.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R186 - Revenue Per Case";
	
}else  if (ReportID == 106){
	
	FeatureID=229; 
	ReportURL="ReportCenterR187.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R187 - Empty Ledger";	
	
}else  if (ReportID == 107){
	
	FeatureID=230; 
	ReportURL="ReportCenterR188.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R188 - Orders / Package and Outlet";
	DefaultFilter = "LoadAllDistributors(\'LoadAllDistributorsHyperlink\')";
	
}else  if (ReportID == 108){
	
	FeatureID=231; 
	ReportURL="ReportCenterR189.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R189 - Per Case Discounts";
	DefaultFilter = "LoadAllDistributors(\'LoadAllDistributorsHyperlink\')";	
}
 
else  if (ReportID == 109){
	
	FeatureID=232; 
	ReportURL="ReportCenterR190.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R190 - Distributor Empty Status";
	DefaultFilter = "LoadDate(\'LoadDateRangeHyperlink1\')";
	
}else  if (ReportID == 110){
	
	FeatureID=233; 
	ReportURL="ReportCenterR191.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R191 - Empty Receipt List";
	DefaultFilter = "LoadDateTimeRange(\'LoadAllHODHyperlink\')";
	
}else  if (ReportID == 111){
	
	FeatureID=237; 
	ReportURL="ReportCenterR192.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R192 - Empty Activity";
	
	
}else  if (ReportID == 112){
	
	FeatureID=238; 
	ReportURL="ReportCenterR193.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R193 - Outlet Sale";
	
}else  if (ReportID == 113){
	
	FeatureID=240; 
	ReportURL="ReportCenterR194.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R194 - Empty Receipts";
	DefaultFilter = "LoadDateTimeRange(\'LoadAllHODHyperlink\')";
	
}else  if (ReportID == 114){
	
	FeatureID=241; 
	ReportURL="ReportCenterR195.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R195 - Empty Receipts Summary";	
	DefaultFilter = "LoadDateTimeRange(\'LoadAllHODHyperlink\')";
	
}else  if (ReportID == 115){
	
	FeatureID=242; 
	ReportURL="ReportCenterR196.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R196 - Outlet Productivity";
	
}else if (ReportID == 116){
	
	FeatureID=243;
	ReportURL="ReportCenterR197.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R197 - Customer Discounts";
	
}else if (ReportID == 117){
	
	FeatureID=244;
	ReportURL="ReportCenterR198.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R198 - Order Booker Sales";
	
}else if (ReportID == 118){
	
	FeatureID=246;
	ReportURL="ReportCenterR199.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R199 - Lifting Without Targets";
	DefaultFilter = "LoadDateRangeNew('LoadDateRangeHyperlinkNew')";
	
}else  if (ReportID == 119){
	
	FeatureID=248;
	ReportURL="CRMMarketWatchCaneReport.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "CRM Market Watch Cane";
	
		
}else  if (ReportID == 120){
	
	FeatureID=249;
	ReportURL="ReportCenterR200.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R200 - Distributor Sales Summary";
	
		
}else if (ReportID == 121){
	
	FeatureID=252;
	ReportURL="ReportCenterR201.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R201 - Distributor Target Analysis Lifting";
	DefaultFilter = "LoadDate('LoadDateRangeHyperlink1')";
	
}
else if (ReportID == 122){
	
	FeatureID=253;
	ReportURL="ReportCenterR202.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R202 - Distributor Score Card";
		
}
else if (ReportID == 123){
	
	FeatureID=255;
	ReportURL="ReportCenterR203.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R203 - Revenue based Lifting";
		
}
else if (ReportID == 124){
	
	FeatureID=256;
	ReportURL="ReportCenterR204.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R204 - Distributor Area Allocation";
	DefaultFilter = "LoadDateRangeNew('LoadDateRangeHyperlinkNew')";
		
}
else if (ReportID == 125){
	
	FeatureID=257;
	ReportURL="ReportCenterR205.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R205 - Sales Segregation";
	
		
}else if (ReportID == 126){
	
	FeatureID=258;
	ReportURL="ReportCenterR206.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R206 - Active Empty Credit Limit";
		
}else if (ReportID == 127){
	
	FeatureID=259;
	ReportURL="ReportCenterR207.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R207 - Empty Credit Status";
	DefaultFilter = "LoadDate(\'LoadDateRangeHyperlink1\')";
}else if (ReportID == 128){
	
	FeatureID=260;
	ReportURL="ReportCenterR208.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R208 - Lifting vs Secondary Sales";
	
}else  if (ReportID == 129){
	
	FeatureID=263; 
	ReportURL="ReportCenterR209.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R209 - Orderbooker Tracking";
	//DefaultFilter = "LoadAllOrderBookers(\'LoadAllOrderBookersHyperlink\')";
	
}else  if (ReportID == 130){
	
	FeatureID=264;
	ReportURL="ReportCenterR210.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R210 - PJP List";	
	DefaultFilter = "LoadAllOutletType(\'LoadAllOutletTypeHyperlink\')";
}else  if (ReportID == 131){
	
	FeatureID=265;
	ReportURL="ReportCenterR211.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R211 - KPI/Order Booker";
	DefaultFilter = "LoadDate(\'LoadDateRangeHyperlink1\')";
	
}else  if (ReportID == 132){
	
	FeatureID=266; 
	ReportURL="ReportCenterR212.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R212 - Lifting Report";	
	//DefaultFilter = "LoadAllDistributors(\'LoadAllDistributorsHyperlink\')"; 
	//DefaultFilter = "LoadAllDistributorsLifting(\'LoadAllDistributorsLiftingHyperlink\')";
	
}else  if (ReportID == 133){
	
	FeatureID=267; 
	ReportURL="ReportCenterR213.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R213 - Distributor Empty Status";
	DefaultFilter = "LoadDate(\'LoadDateRangeHyperlink1\')";
	
}else  if (ReportID == 134){
	
	FeatureID=268;
	ReportURL="ReportCenterR214.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R214 - Order Booker Sales";
	//DefaultFilter = "LoadAllOrderBookers(\'LoadAllOrderBookersHyperlink\')";
}else if (ReportID == 135){
	
	FeatureID=270;
	ReportURL="ReportCenterR215.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R215 - Order Booker Scorecard";
	
}else  if (ReportID == 136){
	
	FeatureID=271;
	ReportURL="ReportCenterR216.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R216 - Credit Limit Processing";		
}else  if (ReportID == 137){
	
	FeatureID=272; 
	ReportURL="ReportCenterR217.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R217 - Stock Summary";
}else  if (ReportID == 138){
	
	FeatureID=275; 
	ReportURL="ReportCenterR218.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R218 - Outsourcing Sales";	
	
}else  if (ReportID == 139){
	
	FeatureID=277; 
	ReportURL="ReportCenterR219.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R219 - Outsourcing Purchases";	
	
}else  if (ReportID == 140){
	
	FeatureID=278; 
	ReportURL="ReportCenterR220.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R220 - Customer Credit Limit";	
	
}else  if (ReportID == 141){
	
	FeatureID=280; 
	ReportURL="ReportCenterR221.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R221 - Empty Issuance";	
	DefaultFilter = "LoadDateTimeRange(\'LoadAllHODHyperlink\')";
	
}else  if (ReportID == 142){
	
	FeatureID=282; 
	ReportURL="ReportCenterR222.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R222 - Outsourcing Sales Printing";	
	
}else  if (ReportID == 143){
	
	FeatureID=284; 
	ReportURL="ReportCenterR223.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R223 - Distributor Per Case Discounts";	
	
}else if (ReportID == 144){
	
	FeatureID=285;
	ReportURL="ReportCenterR224.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R224 - Order Booker Timestamps";
	DefaultFilter = "LoadDate(\'LoadDateRangeHyperlink1\')";
	
}else  if (ReportID == 145){
	
	FeatureID=286; 
	ReportURL="ReportCenterR225.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R225 - Empty Receive";	
	DefaultFilter = "LoadDateTimeRange(\'LoadAllHODHyperlink\')";
	
}else if (ReportID == 146){
	
	FeatureID=287;
	ReportURL="ReportCenterR226.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R226 - Secondary Sales";
	DefaultFilter = "LoadDateRangeNew('LoadDateRangeHyperlinkNew')";
	
}else if (ReportID == 147){
	
	FeatureID=288;
	ReportURL="ReportCenterR227.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R227 - Sampling Detail";
		
}else  if (ReportID == 148){
	
	FeatureID=289; 
	ReportURL="ReportCenterR228.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R228 - Sales / Package and Outlet";
	DefaultFilter = "LoadAllDistributors(\'LoadAllDistributorsHyperlink\')";
	
}else if (ReportID == 149){
	
	FeatureID=290;
	ReportURL="ReportCenterR229.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R229 - PJP KPIs";
	
	//System.out.println("Hello -"+UniqueVVID);
		
}else if (ReportID == 150){
	
	FeatureID=291;
	ReportURL="ReportCenterR230.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R230 - Distributor KPIs";
	
	//System.out.println("Hello -"+UniqueVVID);
		
}else if (ReportID == 151){
	
	FeatureID=292;
	ReportURL="ReportCenterR231.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R231 - Distributor Stock Analysis";
	DefaultFilter = "LoadAllHODs(\'LoadAllHODHyperlink\')";	
	//System.out.println("Hello -"+UniqueVVID);
		
}else if (ReportID == 152){
	
	FeatureID=293;
	ReportURL="ReportCenterR232.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R232 - Primary and Secondary Sales vs. Last Year";
	
}else if (ReportID == 153){
	
	FeatureID=294;
	ReportURL="ReportCenterR233.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R233 - Primary and Secondary Sales";
	
}else if (ReportID == 154){
	
	FeatureID=295;
	ReportURL="ReportCenterR234.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R234 - Customer Credit Summary";
	DefaultFilter = "LoadDate(\'LoadDateRangeHyperlink1\')";
}else if (ReportID == 155){
	
	FeatureID=296;
	ReportURL="ReportCenterR235.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R235 - Volume Analysis";
	DefaultFilter = "LoadDate(\'LoadDateRangeHyperlink1\')";
	
}else if (ReportID == 156){
	
	FeatureID=297;
	ReportURL="ReportCenterR236.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R236 - SKU & Sales Analysis";
	
	
}else if (ReportID == 157){
	
	FeatureID=298;
	ReportURL="ReportCenterR237.jsp?UniqueSessionID="+UniqueVVID;
	ReportTitle = "R237 - Distributor Statement";
	
	
}







if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}




session.removeAttribute("SR1SelectedPackages");
session.removeAttribute("SR1SelectedBrands");
session.removeAttribute("SR1SelectedDistributors");
session.removeAttribute("SR1SelectedOrderBookers");
session.removeAttribute("SR1SelectedVehicles");
session.removeAttribute("SR1SelectedEmployees");
session.removeAttribute("SR1SelectedOutlets");
session.removeAttribute("SR1StartDate");
session.removeAttribute("SR1EndDate");
session.removeAttribute("SR1DateType");



session.setAttribute( "SR1FeatureID",FeatureID );
session.removeAttribute("SR1SelectedPJP");
session.removeAttribute("SR1SelectedHOD");




%>
<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<script src="js/lookups.js"></script>
<script src="js/ReportCenter.js?500011111111111199999999999111111=11999999999991111111"></script>
<%if(ReportID == 34 || ReportID == 39 || ReportID == 40 || ReportID == 30 || ReportID == 129){ %>
<script src="http://maps.google.com/maps/api/js?sensor=true"></script>
<%} %>

<% if (ReportID == 37 || ReportID == 39){ %>
<script src="lib/highcharts301/js/highcharts.js"></script>
<% } %>

<% if (ReportID == 48){ %>
<script src="js/DictionaryUrduAddWords.js"></script>
<% } %>

<div data-role="page" id="SalesSummaryMain" data-url="SalesSummaryMain" data-theme="d">
<%
//if (isDashboard == false){

if (HeaderType.equals("Standard")){
%>
    <jsp:include page="Header5.jsp" >
    	<jsp:param value="<%=ReportTitle%>" name="title"/>
    	<jsp:param value="<%=HeaderBackURL%>" name="BackURL"/>
    </jsp:include>
    
       
    
<%
}else if (HeaderType.equals("DistributorDashboard")){
	//System.out.println("Hello "+DistributorCode);
	
	long DistributorID[] = {DistributorCode};
	session.setAttribute( "SR1SelectedDistributors", DistributorID);
	String ReportTitleToShow = Utilities.filterString(request.getParameter("ReportTitleQuery"), 1, 100);
	//System.out.println(DistributorName);
	%>
    <jsp:include page="DistributorDashboardHeader.jsp" >
    	<jsp:param value="<%=DistributorName%>" name="DistributorNameToShow"/>
    	<jsp:param value="<%=DistributorCode%>" name="DistributorCode"/>
    	<jsp:param value="<%=PageIndex%>" name="PageIndex"/>
    	<jsp:param value="<%=ReportTitleToShow%>" name="ReportTitleToShow"/>
    </jsp:include>
<%
}else if (HeaderType.equals("OrderBookerDashboard")){
	String EmployeeName = Utilities.filterString(request.getParameter("EmployeeName"),1,30);
	long EmployeeCode = Utilities.parseLong(request.getParameter("EmployeeCode"));
	long EmployeeCodeArray[] = {EmployeeCode};
	session.setAttribute( "SR1SelectedOrderBookers", EmployeeCodeArray);

%>
    <jsp:include page="OrderBookerDashboardHeader.jsp" >
    	<jsp:param value="<%=EmployeeName%>" name="title"/>
    	<jsp:param value="<%=PageIndex%>" name="tab"/>
    	<jsp:param value="<%=EmployeeCode%>" name="EmployeeCode"/>
    </jsp:include>
<%
}else if(HeaderType.equals("OutletDashboard")){
	//System.out.println(request.getParameter("OutletIDDD"));
	
	long OutletDashboardOultetID = Utilities.parseLong(request.getParameter("OutletDashboardOutletID"));
	//System.out.println(OutletDashboardOultetID);
	
	String OutletDashboardOutletName = Utilities.filterString(request.getParameter("OutletDashboardOutletName"),1,30);
	long OultetID1Array[] = {OutletDashboardOultetID};
	session.setAttribute( "SR1SelectedOutlets", OultetID1Array);	
%>
	<jsp:include page="OutletDashboardHeader.jsp" >
    	<jsp:param value="<%=OutletDashboardOutletName%>" name="title"/>
    	<jsp:param value="<%=PageIndex%>" name="tab"/>
    	<jsp:param value="<%=OutletDashboardOultetID%>" name="OutletID"/>
    </jsp:include>
<%} %>
    
    
        
    <div data-role="content" data-theme="d">
    
   
    
    <jsp:include page="LookupEmployeeSearchPopup.jsp" > 
    	<jsp:param value="EmployeeSearchCallBackAtOrderBookerDashboard" name="CallBack" />
    </jsp:include><!-- Include Employee Search -->
    
    
    
	<table border="0" style="width:100%;">
<%
boolean IsPackageSelected=false;
int IsPackageSelectedOnLoad=0;
long SelectedPackagesArray[] = null;
if (session.getAttribute("SR1SelectedPackages") != null){
	SelectedPackagesArray = (long[])session.getAttribute("SR1SelectedPackages");
	if(SelectedPackagesArray.length>0)
	{
		IsPackageSelected = true;
		IsPackageSelectedOnLoad=1;
	}
}

%>      
	<tr>
		<td style="width:15%;<%if (noFilters == true){ %>display: none;<%} %>" valign="top" id="FilterTDID">
		
		
		
		<input type="hidden" name="IsPackageSelectedOnLoad" id="IsPackageSelectedOnLoad" value="<%=IsPackageSelectedOnLoad%>" />
		
		
		   <div id="LoadSalesSummaryFilterBy"  style="ma1rgin-top:40px; width:100%">
		
		</div>
		
			<form id="SalesSummaryMainForm" data-ajax="false">
				<input type="hidden" name="FilterType" id="FilterType" value=""/>
				<input type="hidden" name="ReportURL" id="ReportURL" value="<%=ReportURL%>"/>
				<input type="hidden" name="ReportID" id="ReportID" value="<%=ReportID%>"/>
				<input type="hidden" name="DefaultFilter" id="DefaultFilter" value="<%=DefaultFilter%>"/>
				<input type="hidden" name="UniqueSessionID" id="UniqueSessionID" value="<%=UniqueVVID %>"/>
				<input type="hidden" name="FilterShowHideFlag" id="FilterShowHideFlag" value="0"/>
				
				<div id="TESTID" style="float: right;"></div>
				
				
				<div id="LoadAllSearchResultsDIV" style="margin-left:-8px">
				
				</div>
			</form>
		</td>
		
		<td style="width:68%" valign="top" id="ReportSaleSummaryTD">
	<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
	<li data-role="list-divider" data-theme="a">Message</li>
	<li>
	<span style="font-weight: normal;">Please select any of data filters to continue.</span>
	</li>
	</ul>		
		</td>
	</tr>
		
	</table>


	<form id="DashboardDistributorFormID" name="DashboardDistributorFormID" action="DistributorDashboardOverview.jsp" method="POST" data-ajax="false">
    	<input type="hidden" name="DistributorCode" id="DashboardDistributorDistributorCode"/>
    </form>
    
    <form id="DashboardOrderBookerFormID" name="DashboardOrderBookerFormID" action="OrderBookerDashboardOverview.jsp" method="POST" data-ajax="false">
    	<input type="hidden" name="EmployeeCode" id="DashboardOrderBookerEmployeeCode"/>
    </form>

    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<!-- <button data-icon="check" data-theme="a" data-inline="true" id="RetursGenerateButton" onClick="GetAllSalesForReturn()">Generate Returns</button>
		 -->
	</div>    	
    </div>
   <%
   int FeatureIDSession =0;
   if (session.getAttribute("SR1FeatureID") != null)
   {
	  FeatureIDSession = (Integer)session.getAttribute("SR1FeatureID");
   	//System.out.println("heyyyy "+FeatureID);
   }
   
   %>
	<div data-role="popup" id="R125_ShowMapView" data-overlay-theme="a" data-theme="d" data-dismissible="true" class="ui-corner-all" style="width: 500px; height: 300px" > 
	     <div data-role="header" data-theme="a" class="ui-corner-top">
	         <h1>Map View</h1>
	     </div>
	     <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >
		<div id="map_view"></div>
	     </div>
	</div>
	
	<!--  Resolve Complain pop up -->
	
	
	<div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:300px; max-height: 300px" >
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Remarks</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >

			
            <table style="width:100%">
            	<tr>
                	<td>
                        <textarea cols="80" rows="40" name="ComplaintRemarks" id="ComplaintRemarks" style="min-height:100px;"></textarea>
                    
                    </td>
                </tr>
                <tr>
                    <td>
                    	<button data-role="button"  id="ResolveComplaintButton"  data-theme="c" data-inline="true" data-corners="false" onClick="ResolveComplaint();">Resolve</button>
                    </td>
                </tr>
            </table>
       

       
            
        </div>
    </div>
    
    <div data-role="popup" id="popupDialogReportCenterR167BrandsList" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="width: 500px;" class="ui-corner-all">
	    <div data-role="header" data-theme="a" class="ui-corner-top">
	        <h1>Brands</h1>
	    </div>
	    <div id="ReportCenterR167BrandsList" data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
	        <img src="images/snake-loader.gif" >
	    </div>
	</div>
	
	<div data-role="popup" id="popupDialogReportCenterR167ScopeList" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="width: 500px;" class="ui-corner-all">
	    <div data-role="header" data-theme="a" class="ui-corner-top">
	        <h1>Scope</h1>
	    </div>
	    <div id="ReportCenterR167ScopeList" data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
	        <img src="images/snake-loader.gif" >
	    </div>
	</div>
    
    
    <div data-role="popup" id="popupDialog1" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:300px; max-height: 300px" >
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Remarks</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >

			
            <table style="width:100%">
            	<tr>
                	<td>
                        <textarea cols="80" rows="40" name="ComplaintRemarks1" id="ComplaintRemarks1" style="min-height:100px;"></textarea>
                    
                    </td>
                </tr>
                <tr>
                    <td>
                    	<button data-role="button"  id="ResolveComplaintButton1"  data-theme="c" data-inline="true" data-corners="false" onClick="ResolveComplaintTOT();">Resolve</button>
                    </td>
                </tr>
            </table>
       

       
            
        </div>
    </div>
    
    
    
    <!--  For report 164 -->
    <div data-role="popup" id="popupDialog15" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; min-height:600px;" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>View</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >

			

        <div id="SearchContent34">
        
        </div>
            
        </div>
    </div>	
    
    
    <!--  /////////////////////  -->
    
    <!--  For report 172 -->
    <div data-role="popup" id="popupDialog16" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; min-height:600px;" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Plan</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >

			

        <div id="SearchContent35">
        
        </div>
            
        </div>
    </div>	
    
    
    <!--  /////////////////////  -->
    
	
	
    
<jsp:include page="LookupOutletSearchPopup.jsp" > 
    	<jsp:param value="OutletSearchCallBackReporCenter" name="CallBack" />
    	<jsp:param value="<%=FeatureIDSession %>" name="OutletSearchFeatureID" />
    </jsp:include><!-- Include Outlet Search -->	

<% if (ReportID == 63 || ReportID == 157){ %>
<jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="R146DistributorSearchCallBack" name="CallBack" />
    	<jsp:param value="<%=FeatureIDSession %>" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Distributor Search -->
<%} else if(ReportID ==78 || ReportID ==149){ %>

<jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="R164DistributorSearchCallBack" name="CallBack" />
    	<jsp:param value="<%=FeatureIDSession %>" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Distributor Search -->

<% }else{ %>

 <jsp:include page="LookupDistributorSearchPopup.jsp"> 
    	<jsp:param value="DistributorSearchCallBackAtDistributorReports" name="CallBack" />
    	<jsp:param value="44" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Distributor Search -->
    <% } %>
</div>

</body>
</html>

