<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="java.sql.SQLException"%>

<script>

function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}

</script>

<style>
td{
font-size: 8pt;
}

</style>


<%!

Datasource ds = new Datasource();

%>

<%
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();
Statement s5 = c.createStatement();
Statement s6 = c.createStatement();
Statement s7 = c.createStatement();
Statement s8 = c.createStatement();
Statement s9 = c.createStatement();
Statement s10 = c.createStatement();
Statement s11= c.createStatement();
Statement s12 = c.createStatement();
Statement s13 = c.createStatement();
Statement s14 = c.createStatement();
Statement s15 = c.createStatement();
Statement s16 = c.createStatement();
Statement s17 = c.createStatement();
Statement s18 = c.createStatement();
Statement s19 = c.createStatement();
Statement s20 = c.createStatement();

long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 253;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}




//Date date = Utilities.parseDate(request.getParameter("Date"));


Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");




if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	//StartDate = new Date(); // add code of start of current month if first time report opens
	Calendar cc = Calendar.getInstance();   // this takes current date
    cc.set(Calendar.DAY_OF_MONTH, 1);
    StartDate = cc.getTime();
     
	
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}


int SelectedMonth = Utilities.getMonthNumberByDate(StartDate);
int SelectedYear = Utilities.getYearByDate(StartDate);



long SelectedPackagesArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPackages") != null){
   	SelectedPackagesArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPackages");           	
}

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
if (session.getAttribute(UniqueSessionID+"_SR1SelectedHOD") != null){
	SelectedHODArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedHOD");
	HODIDs = Utilities.serializeForSQL(SelectedHODArray);
}


String WhereHOD = "";
String WhereHOD1 = "";

if (HODIDs.length() > 0){
	WhereHOD = " and distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
	WhereHOD1 = " and cache_snd_id in ("+HODIDs+") ";

}


//RSM


String RSMIDs="";
long SelectedRSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRSM") != null){
	SelectedRSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRSM");
	RSMIDs = Utilities.serializeForSQL(SelectedRSMArray);
}

String WhereRSM = "";
String WhereRSM1 = "";

if (RSMIDs.length() > 0){
	WhereRSM = " and distributor_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
	WhereRSM1 = " and cache_rsm_id in("+RSMIDs+") ";
	
}

//SM


String SMIDs="";
long SelectedSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedSM") != null){
	SelectedSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedSM");
	SMIDs = Utilities.serializeForSQL(SelectedSMArray);
}

String WhereSM = "";
if (SMIDs.length() > 0){
	WhereSM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
}

//TDM


String TDMIDs="";
long SelectedTDMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedTDM") != null){
	SelectedTDMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedTDM");
	TDMIDs = Utilities.serializeForSQL(SelectedTDMArray);
}

String WhereTDM = "";
if (TDMIDs.length() > 0){
	WhereTDM = " and distributor_id in (SELECT distributor_id FROM common_distributors where tdm_id in ("+TDMIDs+")) ";	
}

//ASM


String ASMIDs="";
long SelectedASMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedASM") != null){
	SelectedASMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedASM");
	ASMIDs = Utilities.serializeForSQL(SelectedASMArray);
}

String WhereASM = "";
if (ASMIDs.length() > 0){
	WhereASM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
}

Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);


long SelectedBrandArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedBrands") != null){
	   SelectedBrandArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedBrands");           	
}


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




/*
long DistributorID = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
	DistributorID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
}

System.out.println("DistributorID = "+DistributorID);
String WhereCustomerID ="";
String WhereDistributorID ="";
if (DistributorID != 0){
	WhereCustomerID = " and customer_id in ("+DistributorID+") ";
	WhereDistributorID = " and cache_distributor_id in ("+DistributorIDs+") ";
}else{
	WhereCustomerID = " and customer_id in ("+DistributorIDs+") ";
	WhereDistributorID = " and cache_distributor_id in ("+DistributorIDs+") ";
}
*/

//System.out.println("Hello "+WhereCustomerID);

//Distributor

long SelectedDistributorsArray[] = null;
boolean IsDistributorSelected=false;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){

	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors"); 
	IsDistributorSelected = true;
}else{
}

String DistributorIDs2 = "";
String WhereCustomerID ="";
String WhereDistributorID ="";
if(SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0){
	for(int i = 0; i < SelectedDistributorsArray.length ; i++){
		
		if(i == 0){
			DistributorIDs2 += SelectedDistributorsArray[i];
		}else{
			DistributorIDs2 += ", "+SelectedDistributorsArray[i];
		}
	}
	WhereCustomerID = " and customer_id in ("+DistributorIDs2+") ";
	WhereDistributorID = " and distributor_id in ("+DistributorIDs2+") ";
	//out.print(WhereDistributors);
}

//GTM Category


String GTMCategoryIDs="";
long SelectedGTMCategoryArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedGTMCategory") != null){
	SelectedGTMCategoryArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedGTMCategory");
	GTMCategoryIDs = Utilities.serializeForSQL(SelectedGTMCategoryArray);
}

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

//Region
String RegionIDs="";
long SelectedRegionArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRegion") != null){
	SelectedRegionArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRegion");
	RegionIDs = Utilities.serializeForSQL(SelectedRegionArray);
}


Region[] RegionObj = UserAccess.getUserFeatureRegion(SessionUserID, FeatureID);
String ScopeRegionIDs = UserAccess.getRegionQueryString(RegionObj);


String WhereRegion = "";
if (RegionIDs.length() > 0){
	WhereRegion = " and region_id in ("+RegionIDs+") ";	
}



s.executeUpdate("SET SESSION group_concat_max_len = 1000000");

s.executeUpdate("create temporary table temp_orderbooker_performance (name varchar(500), percent_before_10am double, percent_after_6pm double, avg_time_in varchar(20), avg_time_out varchar(20),outlets_in_pjp int(5), outlets_ordered int(5), converted_cases_sold int(10), gross_revenue double, eco double, bill_productivity double, sku_per_bill double, pack_size_per_bill double, drop_size double, orderbookers_worked double, unplanned_calls double, converted_cases_csd double, converted_cases_ncb double, sku_per_order_csd double, sku_per_order_ncb double, ssrb_250 double, ssrb_240 double, ssrb_total double, target_days_left double, target_cc double, target_achieved_cc double, target_days_past double, cr_assigned double, completed_calls double, adjusted_calls double, ssrb_slice double, ssrb_aquafina double, backorders_percent double, backorders_cases double, orders_cases double, sales_returns_cases double, sales_returns_percent double, week_planned_calls double, week_productive_calls double)");


//System.out.println(StartDate +" - "+EndDate);


%>





<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>

<%
String SND_ID=HODIDs;



//System.out.println(SND_ID);

%>



<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					 	<tr>
					 	<td colspan="31">Weekly Distributor Score Card</td>	
					 	
					 	</tr>
					 	
					 	<tr>
					 	<td colspan="3" style="text-align:center; background-color:#3f4040; color:white;"></td>
					 	<td colspan="3" style="text-align:center; background-color:#3f4040; color:white;">Order Bookers</td>
					 	<td colspan="6" style="text-align:center; background-color:#3f4040; color:white;">Productivity</td>
					 	<td colspan="5" style="text-align:center; background-color:#3f4040; color:white;">Single Serve (CC)</td>
					 	<td colspan="3" style="text-align:center; background-color:#3f4040; color:white;">SKU / Bill</td>
					 	<td style="text-align:center; background-color:#3f4040; color:white;">Drop Size</td>
					 	<td colspan="4" style="text-align:center; background-color:#3f4040; color:white;">Monthly Target (CC)</td>
					 	<td colspan="3" style="text-align:center; background-color:#3f4040; color:white;">Sales (CC)</td>
					 	<td style="text-align:center; background-color:#3f4040; color:white;">Backorders</td>
					 	<td style="text-align:center; background-color:#3f4040; color:white;">Returns</td>
					 	<td style="text-align:center; background-color:#3f4040; color:white;">Net Sales</td>
					 	
								 	
						 </tr>
						 
					<tr>
					<td colspan="3" style="text-align:center; background-color:#dfdfdf; ">Distributor</td>
					<td style="text-align:center; background-color:#dfdfdf; ">Worked</td>
					<td style="text-align:center; background-color:#dfdfdf; ">Check-In</td>
					<td style="text-align:center; background-color:#dfdfdf; ">Check-Out</td>
					<td style="text-align:center; background-color:#dfdfdf; ">Productive Calls</td>
					<td style="text-align:center; background-color:#dfdfdf; ">Completed Calls</td>
					<td style="text-align:center; background-color:#dfdfdf; ">Adjusted Calls</td>
					<td style="text-align:center; background-color:#dfdfdf; ">Drop Calls</td>
					<td style="text-align:center; background-color:#dfdfdf; ">Order Productivity</td>
					<td style="text-align:center; background-color:#dfdfdf; ">Bill Productivity</td>
					<td style="text-align:center; background-color:#dfdfdf; ">Standard</td>
					<td style="text-align:center; background-color:#dfdfdf; ">Sting</td>
					<td style="text-align:center; background-color:#dfdfdf; ">Slice</td>
					<td style="text-align:center; background-color:#dfdfdf; ">Aquafina</td>
					<td style="text-align:center; background-color:#dfdfdf; ">Total</td>
					
					<td style="text-align:center; background-color:#dfdfdf; ">CSD</td>
					<td style="text-align:center; background-color:#dfdfdf; ">NCB</td>
					<td style="text-align:center; background-color:#dfdfdf; ">Total</td>
					<td style="text-align:center; background-color:#dfdfdf; "></td>
					<td style="text-align:center; background-color:#dfdfdf; ">Target</td>
					<td style="text-align:center; background-color:#dfdfdf; ">Achieved</td>
					<td style="text-align:center; background-color:#dfdfdf; ">Achieved (%)</td>
					<td style="text-align:center; background-color:#dfdfdf; ">Days Left</td>
					<td style="text-align:center; background-color:#dfdfdf; ">CSD</td>
					<td style="text-align:center; background-color:#dfdfdf; ">NCB</td>
					<td style="text-align:center; background-color:#dfdfdf; ">Total</td>
					<td style="text-align:center; background-color:#dfdfdf; ">(%) of Orders</td>
					<td style="text-align:center; background-color:#dfdfdf; ">(%) of Orders</td>
					<td style="text-align:center; background-color:#dfdfdf; ">Amount</td>
					
					
					
					  
					
					
					
					</tr>	 
						 
					 </thead>
					<tbody>
					<%
					this.populateData(WhereHOD,StartDate,EndDate,WhereDistributorID);
				    


					
					int TotalOutletsPJP = 0;
					int TotalOutletsOrdered = 0;
					int TotalConvertedCases = 0;
					long TotalConvertedCasesCSD = 0;
					long TotalConvertedCasesNCB = 0;
					double TotalOrderAmount = 0;
					
					
					long TotalConvertedCasesSSRB250 = 0;
					long TotalConvertedCasesSSRB240 = 0;
					long TotalConvertedCasesSSRBSlice = 0;
					long TotalConvertedCasesSSRBAF = 0;
					
					long TotalConvertedCasesSSRBTotal = 0;
					
					long TotalTargetCC = 0;
					long TotalTargetAchievedCC = 0;
					
					long TotalUnplannedCalls = 0;
					long TotalCompletedCalls = 0;
					long TotalAdjustedCalls = 0;
					long TotalDropCalls = 0;
					
					
					double TotalOrdersCC = 0;
					double TotalBackordersCC = 0;
					double TotalSalesReturnsCC = 0;
					
					double TotalWeekPlannedCalls = 0;
					double TotalWeekProductiveCalls = 0;
					
					ResultSet rs1 = s3.executeQuery("select * from temp_orderbooker_performance order by name");
					while(rs1.next()){	
						int OutletsPJP = rs1.getInt("outlets_in_pjp");
						int OutletsOrdered = rs1.getInt("outlets_ordered");
						double OrderAmount = rs1.getDouble("gross_revenue"); 
						double UnplannedCalls = rs1.getDouble("unplanned_calls");
						
						long CompletedCalls = Math.round(rs1.getDouble("completed_calls"));
						long AdjustedCalls = Math.round(rs1.getDouble("adjusted_calls"));
						
						
						double WeekPlannedCalls = Math.round(rs1.getDouble("week_planned_calls"));
						double WeekProductiveCalls = Math.round(rs1.getDouble("week_productive_calls"));
						
						TotalWeekPlannedCalls += WeekPlannedCalls;
						TotalWeekProductiveCalls += WeekProductiveCalls;
						
						TotalOrdersCC += rs1.getDouble("orders_cases");
						TotalBackordersCC += rs1.getDouble("backorders_cases");
						TotalSalesReturnsCC += rs1.getDouble("sales_returns_cases");
						
						TotalAdjustedCalls += AdjustedCalls;
						TotalCompletedCalls += CompletedCalls;
						
						long DropCalls = OutletsOrdered - CompletedCalls;
						TotalDropCalls += DropCalls;
						
						TotalOutletsPJP += OutletsPJP;
						TotalOutletsOrdered += OutletsOrdered;
						TotalOrderAmount += OrderAmount;
						
						long ConvertedCasesSSRB250 = rs1.getLong("ssrb_250");
						long ConvertedCasesSSRB240 = rs1.getLong("ssrb_240");
						long ConvertedCasesSSRBSlice = rs1.getLong("ssrb_slice");
						long ConvertedCasesSSRBAF = rs1.getLong("ssrb_aquafina");
						
						long ConvertedCasesSSRBTotal = rs1.getLong("ssrb_total");
						
						
						TotalConvertedCasesSSRB250 += ConvertedCasesSSRB250;
						TotalConvertedCasesSSRB240 += ConvertedCasesSSRB240;
						TotalConvertedCasesSSRBSlice += ConvertedCasesSSRBSlice;
						TotalConvertedCasesSSRBAF += ConvertedCasesSSRBAF;
						
						
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
						
						
						double BackordersPercent = rs1.getDouble("backorders_percent");
						double SalesReturnsPercent = rs1.getDouble("sales_returns_percent");
					%>
						<tr>
						<td colspan="3"><%=Utilities.truncateStringToMax(rs1.getString("name"),27) %></td>
				        
				        
				        
						<td><%=Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("orderbookers_worked"))+" / "+Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("cr_assigned"))%></td>
						<td><%=rs1.getString("avg_time_in")%></td>
						<td><%=rs1.getString("avg_time_out")%></td>
						<td><%=Utilities.getDisplayCurrencyFormatRounded(OutletsOrdered)+""%></td>
						<td><%=Utilities.getDisplayCurrencyFormatRounded(CompletedCalls)+""%></td>
						<td><%=Utilities.getDisplayCurrencyFormatRounded(AdjustedCalls)%></td>
						<%
						if (DropCalls != 0){%>
							<td><%=Utilities.getDisplayCurrencyFormatRounded(DropCalls)%></td>
						<%}else{%>
							<td></td>
						<%}
						
						double OrderProductivity = 0;
						if (WeekPlannedCalls != 0){
							OrderProductivity = (WeekProductiveCalls / WeekPlannedCalls) * 100; 
						}
						%>
						<td><%=Utilities.getDisplayCurrencyFormatRounded(OrderProductivity)%>%</td>
						<td><%=Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("bill_productivity"))%>%</td>
						<%
						double SSRBPercent = 0;
						if (ConvertedCases != 0){
							SSRBPercent = ((ConvertedCasesSSRBTotal * 1d) / (ConvertedCases * 1d)) * 100;
						}
						double SSRBPercent250 = 0;
						if (ConvertedCases != 0){
							SSRBPercent250 = ((ConvertedCasesSSRB250 * 1d) / (ConvertedCases * 1d)) * 100;
						}
						double SSRBPercent240 = 0;
						if (ConvertedCases != 0){
							SSRBPercent240 = ((ConvertedCasesSSRB240 * 1d) / (ConvertedCases * 1d)) * 100;
						}
						double SSRBPercentSlice = 0;
						if (ConvertedCases != 0){
							SSRBPercentSlice = ((ConvertedCasesSSRBSlice * 1d) / (ConvertedCases * 1d)) * 100;
						}
						double SSRBPercentAF = 0;
						if (ConvertedCases != 0){
							SSRBPercentAF = ((ConvertedCasesSSRBAF * 1d) / (ConvertedCases * 1d)) * 100;
						}
						
%>
						
						<td><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesSSRB250)%></td>
						<td><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesSSRB240)%></td>
						<td><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesSSRBSlice)%></td>
						<td><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesSSRBAF)%></td>
						<td><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesSSRBTotal)%></td>
						
						<td><%=Utilities.getDisplayCurrencyFormatOneDecimal(rs1.getDouble("sku_per_order_csd"))%></td>
						<td><%=Utilities.getDisplayCurrencyFormatOneDecimal(rs1.getDouble("sku_per_order_ncb"))%></td>
						<td><%=Utilities.getDisplayCurrencyFormatOneDecimal(rs1.getDouble("sku_per_bill"))%></td>
						
						<td><%=Utilities.getDisplayCurrencyFormatOneDecimal(rs1.getDouble("drop_size"))%></td>
	
	<%					
						if (TargetCC != 0){%>
							<td><%=Utilities.getDisplayCurrencyFormatRounded(TargetCC)%></td>
							<td><%=Utilities.getDisplayCurrencyFormatRounded(TargetAchievedCC)%></td>
							<td><%=TargetPercentageString%></td>
							<td><%=Utilities.getDisplayCurrencyFormatRounded(TargetDaysLeft)%></td>
						<%}else{%>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						<%}%>

						<td><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesCSD)%></td>
						<td><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesNCB)%></td>
						<td><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCases)%></td>
						
						<td><%=Utilities.getDisplayCurrencyFormatRounded(BackordersPercent)%>%</td>
						<td><%=Utilities.getDisplayCurrencyFormatRounded(SalesReturnsPercent)%>%</td>
						<td><%=Utilities.getDisplayCurrencyFormatRounded(OrderAmount)%></td>
					<%}
					
					
					%>
					</tr>
					<tr>
					<td colspan="6"></td>
					
					<%
					double TotalTargetPercentage = 0;
					if (TotalTargetCC != 0){
						TotalTargetPercentage = (((TotalTargetAchievedCC * 1d) / (TotalTargetCC * 1d))) * 100;
					}
					
					%>
				   
				    
					<td><%=Utilities.getDisplayCurrencyFormatRounded(TotalOutletsOrdered)+""%></td>
					<td><%=Utilities.getDisplayCurrencyFormatRounded(TotalCompletedCalls)+""%></td>
					<td><%=Utilities.getDisplayCurrencyFormatRounded(TotalAdjustedCalls)+""%></td>
					<td><%=Utilities.getDisplayCurrencyFormatRounded(TotalDropCalls)+""%></td>
					
					<%
					double TotalProductivity = 0;
					if (TotalOutletsOrdered != 0){
						TotalProductivity = ((TotalCompletedCalls * 1d) / (TotalOutletsOrdered * 1d)) * 100d;
					}
					
					double TotalOrderProductivity = 0;
					if (TotalWeekPlannedCalls != 0){
						TotalOrderProductivity = (TotalWeekProductiveCalls / TotalWeekPlannedCalls) * 100; 
					}
					%>
					
					<td><%=Utilities.getDisplayCurrencyFormatRounded(TotalOrderProductivity)+"%"%></td>
					
					<td><%=Utilities.getDisplayCurrencyFormatRounded(TotalProductivity)+"%"%></td>
					<%
					double PercentTotalConvertedCasesSSRB250 = 0;
					if (TotalConvertedCases != 0){
						PercentTotalConvertedCasesSSRB250 = ((TotalConvertedCasesSSRB250 * 1d) / (TotalConvertedCases * 1d)) * 100;
					}
					double PercentTotalConvertedCasesSSRB240 = 0;
					if (TotalConvertedCases != 0){
						PercentTotalConvertedCasesSSRB240 = ((TotalConvertedCasesSSRB240 * 1d) / (TotalConvertedCases * 1d)) * 100;
					}
					double PercentTotalConvertedCasesSSRBTotal = 0;
					if (TotalConvertedCases != 0){
						PercentTotalConvertedCasesSSRBTotal = ((TotalConvertedCasesSSRBTotal * 1d) / (TotalConvertedCases * 1d)) * 100;
					}
					double PercentTotalConvertedCasesSSRBSlice = 0;
					if (TotalConvertedCases != 0){
						PercentTotalConvertedCasesSSRBSlice = ((TotalConvertedCasesSSRBSlice * 1d) / (TotalConvertedCases * 1d)) * 100;
					}
					double PercentTotalConvertedCasesSSRBAF = 0;
					if (TotalConvertedCases != 0){
						PercentTotalConvertedCasesSSRBAF = ((TotalConvertedCasesSSRBAF * 1d) / (TotalConvertedCases * 1d)) * 100;
					}
					
				%>	
					

					<td><%=Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesSSRB250)%></td>
					<td><%=Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesSSRB240)%></td>
					<td><%=Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesSSRBSlice)%></td>
					<td><%=Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesSSRBAF)%></td>
					<td><%=Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesSSRBTotal)%></td>

					
				   <td colspan="4"></td>
			        
					<td><%=Utilities.getDisplayCurrencyFormatRounded(TotalTargetCC)%></td>
					<td><%=Utilities.getDisplayCurrencyFormatRounded(TotalTargetAchievedCC)%></td>
					<td><%=Utilities.getDisplayCurrencyFormatRounded(TotalTargetPercentage)+"%"%></td>
					<td></td>
			        
					<td><%=Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesCSD)%></td>
					<td><%=Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesNCB)%></td>
					<td><%=Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCases)%></td>
					
					<%
					double TotalBackordersPercent = 0;
					if (TotalOrdersCC != 0){
						TotalBackordersPercent = (TotalBackordersCC / TotalOrdersCC) * 100;
					}
					
					
					
					double TotalSalesReturnsPercent = 0;
					if (TotalOrdersCC != 0){
						TotalSalesReturnsPercent = (TotalSalesReturnsCC / TotalOrdersCC) * 100;
					}
					
					%>
					
					<td><%=Utilities.getDisplayCurrencyFormatRounded(TotalBackordersPercent)+"%" %></td>
					<td><%=Utilities.getDisplayCurrencyFormatRounded(TotalSalesReturnsPercent)+"%" %></td>
					<td><%=Utilities.getDisplayCurrencyFormatRounded(TotalOrderAmount)%></td>
					
					
					
									
					
					
					
					</tr>
					
					
					
					
					</tbody>
					
					 
					 
					 
					 
					 
					 
					 
			</table>
		</td>
	</tr>
</table>

	</li>	
</ul>



<%!
public void populateData(String WhereHOD, Date StartDate, Date EndDate, String WhereDistributorID) throws SQLException {
	
	Connection c = ds.getConnection();
	Statement s = c.createStatement();
	Statement s2 = c.createStatement();
	Statement s3 = c.createStatement();
	Statement s4 = c.createStatement();
	Statement s5 = c.createStatement();
	Statement s6 = c.createStatement();
	Statement s7 = c.createStatement();
	Statement s8 = c.createStatement();
	Statement s9 = c.createStatement();
	Statement s10 = c.createStatement();
	Statement s11= c.createStatement();
	Statement s12 = c.createStatement();
	Statement s13 = c.createStatement();
	Statement s14 = c.createStatement();
	Statement s15 = c.createStatement();
	Statement s16 = c.createStatement();
	Statement s17 = c.createStatement();
	Statement s18 = c.createStatement();
	Statement s19 = c.createStatement();
	Statement s20 = c.createStatement();
	
	
	//System.out.println("select mo.distributor_id, (select name from common_distributors where distributor_id = mo.distributor_id) distributor_name, group_concat(mo.id) order_ids, count(mo.id) order_count, count(distinct mo.outlet_id) unique_outlets_ordered, count(distinct created_by) orderbookers_worked, group_concat(distinct mo.outlet_id) outlet_ids_ordered from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+"  "+WhereHOD+" "+WhereDistributorID+" group by mo.distributor_id");
	
	//System.out.println(DistributorIDs);
	
	ResultSet rs = s.executeQuery("select mo.distributor_id, (select name from common_distributors where distributor_id = mo.distributor_id) distributor_name, group_concat(mo.id) order_ids, count(mo.id) order_count, count(distinct mo.outlet_id) unique_outlets_ordered, count(distinct created_by) orderbookers_worked, group_concat(distinct mo.outlet_id) outlet_ids_ordered from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereHOD+" "+WhereDistributorID+" group by mo.distributor_id");
	
	while(rs.next()){
		
		long DistributorID = rs.getLong(1);
		String DistributorName = rs.getString(2);
		
		double OrderBookersWorked = rs.getDouble("orderbookers_worked");
		String OrderIDs = rs.getString("order_ids");
		String OutletIDsOrdered = rs.getString("outlet_ids_ordered");
		String DaysOfWeekIDs = "1,2,3,4,5,7";//rs.getString("days_of_week");
		double OrderCount = rs.getInt("order_count"); 
		double UniqueOutletsOrdered = rs.getInt("unique_outlets_ordered");
		
		double PlannedOutletsCount = 0;
		String PlannedOutletIDs = "";
		
		int OrderBookersAssigned = 0;
		
		//if (DistributorID == 100782){
			//System.out.print("select count(distinct outlet_id), group_concat(distinct outlet_id), count(distinct assigned_to) cr_assigned from distributor_beat_plan_log force index (distributor_beat_plan_log_date_distributor_id_day_number) where log_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and distributor_id = "+DistributorID+" and day_number in ("+DaysOfWeekIDs+") and assigned_to is not null");
		//}
		
		ResultSet rs2 = s3.executeQuery("select count(distinct assigned_to) cr_assigned from distributor_beat_plan_log force index (distributor_beat_plan_log_date_distributor_id_day_number) where log_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and distributor_id = "+DistributorID+" and day_number in ("+DaysOfWeekIDs+") and assigned_to is not null");
		if (rs2.first()){
			//PlannedOutletsCount = rs2.getDouble(1);
			//PlannedOutletIDs = rs2.getString(2);
			OrderBookersAssigned = rs2.getInt("cr_assigned");
		}
		
		// aggregate planned outlets for the week
		
		double WeekPlannedCalls = 0;
		double WeekProductiveCalls = 0;
		
		if (true){
			
			ResultSet rs52 = s3.executeQuery("select distinct date(created_on), dayofweek(created_on) from mobile_order where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+"");
			while(rs52.next()){
				
				Date idate = rs52.getDate(1);
				int iDayOfWeek = rs52.getInt(2);
				
				if (iDayOfWeek != 6){
				
					double iPlannedOutletsCount = 0;
					String iPlannedOutletIDs = "";
					
					ResultSet irs2 = s4.executeQuery("select count(distinct outlet_id), group_concat(distinct outlet_id) from distributor_beat_plan_log force index (distributor_beat_plan_log_date_distributor_id_day_number) where log_date between "+Utilities.getSQLDate(idate)+" and "+Utilities.getSQLDate(idate)+" and distributor_id = "+DistributorID+" and day_number in ("+iDayOfWeek+") and assigned_to is not null");
					if (irs2.first()){
						iPlannedOutletsCount = irs2.getDouble(1);
						iPlannedOutletIDs = irs2.getString(2);
					}
					
					
					
					double iOrdersFromPlannedOutlets = 0;
					ResultSet irs10 = s4.executeQuery("select count(distinct outlet_id) from mobile_order where created_on between "+Utilities.getSQLDate(idate)+" and "+Utilities.getSQLDateNext(idate)+" and outlet_id in ("+iPlannedOutletIDs+") and distributor_id = "+DistributorID);
					if (irs10.first()){
						iOrdersFromPlannedOutlets = irs10.getDouble(1);
					}
					
					WeekPlannedCalls += iPlannedOutletsCount;
					WeekProductiveCalls += iOrdersFromPlannedOutlets;
					
					/*
					if (DistributorID == 100856){
						System.out.println(idate + " "+iPlannedOutletsCount+" "+iOrdersFromPlannedOutlets);
					}
					*/
					
				}
			}
		}
		
		/*
		
		double OrdersFromPlannedOutlets = 0;
		ResultSet rs10 = s3.executeQuery("select count(id) from common_outlets where id in ("+PlannedOutletIDs+") and id in ("+OutletIDsOrdered+")");
		if (rs10.first()){
			OrdersFromPlannedOutlets = rs10.getDouble(1);
		}
		*/
		
		double ConvertedCasesOrdered = 0;
		ResultSet rs3 = s3.executeQuery("select sum(((mop.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mop.is_promotion = 0 and mo.id in ("+OrderIDs+") ");
		if(rs3.first()){
			ConvertedCasesOrdered = rs3.getDouble(1);
		}
		
		long ConvertedCasesSoldCSD = 0;
		ResultSet rs12 = s3.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where ipv.type_id = 1 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") ");
		if(rs12.first()){
			ConvertedCasesSoldCSD = Math.round(rs12.getDouble(1));
		}
		
		long ConvertedCasesSoldNCB = 0;
		ResultSet rs11 = s3.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where ipv.type_id = 2 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") ");
		if(rs11.first()){
			ConvertedCasesSoldNCB = Math.round(rs11.getDouble(1));
		}
		long ConvertedCasesSold = ConvertedCasesSoldCSD + ConvertedCasesSoldNCB;
		
		long ConvertedCasesSoldSSRB250 = 0;
		ResultSet rs16 = s3.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where ipv.ssrb_type_id = 1 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") ");
		if(rs16.first()){
			ConvertedCasesSoldSSRB250 = Math.round(rs16.getDouble(1));
		}
		
		long ConvertedCasesSoldSSRB240 = 0;
		ResultSet rs17 = s3.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where ipv.ssrb_type_id = 2 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") ");
		if(rs17.first()){
			ConvertedCasesSoldSSRB240 = Math.round(rs17.getDouble(1));
		}
		
		long ConvertedCasesSoldSSRBSlice = 0;
		ResultSet rs18 = s3.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where ipv.category_id = 1 and ipv.package_id = 16 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") ");
		if(rs18.first()){
			ConvertedCasesSoldSSRBSlice = Math.round(rs18.getDouble(1));
		}
		
		long ConvertedCasesSoldSSRBAF = 0;
		ResultSet rs19 = s3.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where ipv.product_id = 81 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") ");
		if(rs19.first()){
			ConvertedCasesSoldSSRBAF = Math.round(rs19.getDouble(1));
		}
		
		long ConvertedCasesSoldSSRBTotal = ConvertedCasesSoldSSRB250 + ConvertedCasesSoldSSRB240 + ConvertedCasesSoldSSRBSlice + ConvertedCasesSoldSSRBAF;
		
		
		double GrossRevenue = 0;
		double UniqueOutletsInvoiced = 0;
		double InvoiceCount = 0;
		ResultSet rs4 = s3.executeQuery("select sum(net_amount), count(distinct outlet_id), count(id) from inventory_sales_adjusted where order_id in ("+OrderIDs+") and net_amount != 0");
		if(rs4.first()){
			GrossRevenue = rs4.getDouble(1);
			UniqueOutletsInvoiced = rs4.getDouble(2);
			InvoiceCount = rs4.getDouble(3);
		}
		double BillProductivity = 0;
		if (OrderCount != 0){
			BillProductivity = (InvoiceCount / OrderCount) * 100;
		}
		
		int AdjustedCalls = 0;
		ResultSet rs20 = s3.executeQuery("SELECT count(distinct invoice_id) FROM inventory_sales_dispatch_adjusted_products where invoice_id in ("+
			"select id from inventory_sales_adjusted where order_id in ("+OrderIDs+")"+
		")");
		if (rs20.first()){
			AdjustedCalls = rs20.getInt(1);
		}
		
		double UnplannedOutletCount = 0;//UniqueOutletsOrdered - OrdersFromPlannedOutlets;
		double ECO = 0;
		
		double TotalLinesSold = 0;
		ResultSet rs5 = s3.executeQuery("select count(*) total_lines_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+")");
		if(rs5.first()){
		 TotalLinesSold = rs5.getDouble("total_lines_sold");
		}

		double TotalLinesSoldCSD = 0;
		ResultSet rs13 = s3.executeQuery("select count(*) total_lines_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products ip on isap.product_id = ip.id where ip.type_id = 1 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+")");
		if(rs13.first()){
			TotalLinesSoldCSD = rs13.getDouble("total_lines_sold");
		}
		
		double TotalLinesSoldNCB = 0;
		ResultSet rs14 = s3.executeQuery("select count(*) total_lines_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products ip on isap.product_id = ip.id where ip.type_id = 2 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+")");
		if(rs14.first()){
			TotalLinesSoldNCB = rs14.getDouble("total_lines_sold");
		}
		
		double TotalQuantitySold = 0;
		ResultSet rs9 = s3.executeQuery("select sum(isap.raw_cases) total_qty_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+")");
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
		/*
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
		*/
		double PackPerBill = 0;
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
		ResultSet rs15 = s3.executeQuery("SELECT id, dt.distributor_id, dt.start_date, dt.end_date, to_days(dt.end_date)-to_days(date("+Utilities.getSQLDate(EndDate)+")) days_left, to_days(date("+Utilities.getSQLDate(EndDate)+"))-to_days(dt.start_date) days_past, ( "+
				"select sum(((dtp.quantity * ip.unit_per_case) * ip.liquid_in_ml) / ip.conversion_rate_in_ml) converted_cases from distributor_targets_packages dtp join inventory_packages ip on dtp.package_id = ip.id where dtp.id = dt.id "+
			") converted_cases_target, ( "+
				"select sum((isap.total_units * ipv.liquid_in_ml / ipv.conversion_rate_in_ml)) converted_cases_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.distributor_id = "+DistributorID+" and isap.is_promotion = 0 and isa.created_on between dt.start_date and from_days(to_days(dt.end_date)+1) "+
			") converted_cases_sold FROM distributor_targets dt where dt.distributor_id = "+DistributorID+" and dt.type_id = 1 and date("+Utilities.getSQLDate(EndDate)+") between dt.start_date and dt.end_date");
		
		if (rs15.first()){
			DaysLeft = rs15.getInt("days_left");
			TargetCC = Math.round(rs15.getDouble("converted_cases_target"));
			TargetAchievedCC = Math.round(rs15.getDouble("converted_cases_sold"));
			DaysPast = rs15.getInt("days_past");
		}
		
		
		double CompletedCalls = InvoiceCount;
		
		
		double BackordersCC = 0;
		//System.out.println("SELECT sum( ((mopb.total_units * ipv.liquid_in_ml) / ipv.conversion_rate_in_ml)) cc FROM mobile_order_products_backorder mopb join inventory_products_view ipv on mopb.product_id = ipv.product_id where mopb.id in ("+OrderIDs+")");
		ResultSet rs21 = s3.executeQuery("SELECT sum( ((mopb.total_units * ipv.liquid_in_ml) / ipv.conversion_rate_in_ml)) cc FROM mobile_order_products_backorder mopb join inventory_products_view ipv on mopb.product_id = ipv.product_id where mopb.id in ("+OrderIDs+")");
		if(rs21.first()){
			BackordersCC = rs21.getDouble(1);
		}
		
		double BackordersPercent = 0;
		if (ConvertedCasesOrdered != 0){
			BackordersPercent = (BackordersCC / (ConvertedCasesOrdered * 1d)) * 100;
		}
		
		double SalesReturnCC = 0;
		ResultSet rs22 = s3.executeQuery("SELECT sum( ((isdap.total_units * ipv.liquid_in_ml) / ipv.conversion_rate_in_ml)) cc FROM inventory_sales_dispatch_adjusted_products isdap join inventory_products_view ipv on isdap.product_id = ipv.product_id where isdap.invoice_id in (select distinct id from inventory_sales_invoices where order_id in ("+OrderIDs+")) and isdap.is_promotion = 0");
		if(rs22.first()){
			SalesReturnCC = rs22.getDouble(1);
		}
		
		double SalesReturnPercent = 0;
		if (ConvertedCasesOrdered != 0){
			SalesReturnPercent = (SalesReturnCC / (ConvertedCasesOrdered * 1d)) * 100;
		}
		
		
		
		s2.executeUpdate("insert into temp_orderbooker_performance (name, avg_time_in, avg_time_out, outlets_in_pjp, outlets_ordered, converted_cases_sold, gross_revenue, eco, bill_productivity, sku_per_bill, pack_size_per_bill, drop_size, orderbookers_worked,unplanned_calls, converted_cases_csd, converted_cases_ncb, sku_per_order_csd, sku_per_order_ncb, ssrb_250, ssrb_240, ssrb_total, target_days_left, target_cc, target_achieved_cc, target_days_past, cr_assigned, completed_calls, adjusted_calls, ssrb_slice, ssrb_aquafina, backorders_percent, backorders_cases, orders_cases, sales_returns_cases, sales_returns_percent, week_planned_calls, week_productive_calls)"+
								"values('"+DistributorID+" - "+DistributorName+"','"+Utilities.getDisplayTimeFormat(AvgTimeIn)+"','"+Utilities.getDisplayTimeFormat(AvgTimeOut)+"',"+PlannedOutletsCount+","+OrderCount+","+ConvertedCasesSold+","+GrossRevenue+","+ECO+","+BillProductivity+","+SKUPerBill+","+PackPerBill+","+DropSize+","+OrderBookersWorked+","+UnplannedOutletCount+","+ConvertedCasesSoldCSD+","+ConvertedCasesSoldNCB+","+SKUPerBillCSD+","+SKUPerBillNCB+","+ConvertedCasesSoldSSRB250+","+ConvertedCasesSoldSSRB240+","+ConvertedCasesSoldSSRBTotal+","+DaysLeft+","+TargetCC+","+TargetAchievedCC+","+DaysPast+", "+OrderBookersAssigned+","+CompletedCalls+", "+AdjustedCalls+","+ConvertedCasesSoldSSRBSlice+","+ConvertedCasesSoldSSRBAF+", "+BackordersPercent+","+BackordersCC+","+ConvertedCasesOrdered+","+SalesReturnCC+","+SalesReturnPercent+","+WeekPlannedCalls+","+WeekProductiveCalls+") ");
		
		
		
	}
	
}

%>



<%




ds.dropConnection();
%>



