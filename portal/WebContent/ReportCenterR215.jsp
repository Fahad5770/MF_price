<%@page import="com.mysql.jdbc.Util"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.employee.EmployeeHierarchy"%>
<%@page import="com.pbc.common.User"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.pbc.employee.OrderBookerDashboard"%>



 

<style>
td{
font-size: 8pt;
}
 #map {
        width: 100%;
        height: 800px;
        margin-top: 10px;
      }
      
      
 .SelectedBold{
 	font-weight:bold;
 }     
</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 270;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

//Date date = Utilities.parseDate(request.getParameter("Date"));


Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");

if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	StartDate = new Date();
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}

//HOD


String HODIDs="";
long SelectedHODArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedHOD") != null){
	SelectedHODArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedHOD");
	HODIDs = Utilities.serializeForSQL(SelectedHODArray);
}

String WhereHOD = "";
if (HODIDs.length() > 0){
	WhereHOD = " and mo.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
}


%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Orderbooker Scorecard</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<input type="hidden" id="TempStoringIDOfLastClick" />
	<tr>
		
		<td style="width:100%" valign="top" >
			<table border=1 style="font-size:9px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					<thead>
						<tr>
							<th colspan="2" style="text-align:center">Order Booker</th>
							<th colspan="2" style="text-align:center">Avg Order Time</th>
							<th colspan="6" style="text-align:center">Productivity</th>
							<th colspan="3" style="text-align:center">SKU / Order</th>
							<th colspan="3" style="text-align:center">Sales in Converted Cases</th>
							<th colspan="3" style="text-align:center">SSRB in Converted Cases</th>
							<th colspan="3" style="text-align:center">Target in Converted Cases</th>							
							<th>&nbsp;</th>							
						</tr>
					</thead>
					<thead>
						<tr>
							<th>Name</th>
							<th>Distributor</th>
							<th>Check In</th>
							<th>Check Out</th>
							<th>Planned Calls</th>
							<th>Productive Calls</th>
							<th>Unplanned Calls</th>
							<th>Order Productivity</th>
							<th>Bill Productivity</th>
							<th>Drop Size</th>
							<th>CSD</th>
							<th>NCB</th>
							<th>Total</th>
							<th>CSD</th>
							<th>NCB</th>
							<th>Total</th>
							<th>Standard</th>
							<th>Sting</th>
							<th>Total</th>
							<th>Target</th>
							<th>Achieved</th>
							<th>Achieved (%)</th>
							<th>Sales Amount</th>							
						</tr>
					</thead>
						
			<%
			populateData(c, StartDate, EndDate, HODIDs);
			
			int TotalOutletsPJP = 0;
			int TotalOutletsOrdered = 0;
			int TotalConvertedCases = 0;
			
			double TotalConvertedCasesCSD = 0;
			double TotalConvertedCasesNCB = 0;
			double TotalOrderAmount = 0;
			
			double GrandTotalConvertedCasesCSD = 0;
			double GrandTotalConvertedCasesNCB = 0;
			double GrandTotalOrderAmount = 0;
			
			double TotalConvertedCasesSSRB250 = 0;
			double TotalConvertedCasesSSRB240 = 0;
			double TotalConvertedCasesSSRBTotal = 0;
			
			double GrandTotalConvertedCasesSSRB250 = 0;
			double GrandTotalConvertedCasesSSRB240 = 0;
			double GrandTotalConvertedCasesSSRBTotal = 0;
			
			
			long TotalTargetCC = 0;
			long TotalTargetAchievedCC = 0;
			
			long TotalUnplannedCalls = 0;
			
			boolean isNCBRowPresented = false;
			
			double TotalYesterdayPlannedCalls = 0;
			double TotalYesterdayProductiveCalls = 0;
			
			ResultSet rs1 = s3.executeQuery("select * from temp_orderbooker_performance order by distributor_names");
			while(rs1.next()){
				
				
				String DistributorNames = rs1.getString("distributor_names");
				
				int OutletsPJP = rs1.getInt("outlets_in_pjp");
				int OutletsOrdered = rs1.getInt("outlets_ordered");
				double OrderAmount = rs1.getDouble("gross_revenue"); 
				double UnplannedCalls = rs1.getDouble("unplanned_calls");
				
				TotalOutletsPJP += OutletsPJP;
				TotalOutletsOrdered += OutletsOrdered;
				TotalOrderAmount += OrderAmount;
				
				
				double YesterdayPlannedCalls = rs1.getDouble("yesterday_planned_calls");
				double YesterdayProductiveCalls = rs1.getDouble("yesterday_productive_calls");
				double BillProductivity = rs1.getDouble("bill_productivity");
				
				
				TotalYesterdayPlannedCalls += YesterdayPlannedCalls;
				TotalYesterdayProductiveCalls += YesterdayProductiveCalls;
				
				double YesterdayProductivity = 0;
				if (YesterdayPlannedCalls != 0){
					YesterdayProductivity = (YesterdayProductiveCalls / YesterdayPlannedCalls) * 100;
				}
				
				double ConvertedCasesSSRB250 = rs1.getDouble("ssrb_250");
				double ConvertedCasesSSRB240 = rs1.getDouble("ssrb_240");
				double ConvertedCasesSSRBTotal = rs1.getDouble("ssrb_total");
				
				TotalConvertedCasesSSRB250 += ConvertedCasesSSRB250;
				TotalConvertedCasesSSRB240 += ConvertedCasesSSRB240;
				TotalConvertedCasesSSRBTotal += ConvertedCasesSSRBTotal;
				
				double ConvertedCases = rs1.getDouble("converted_cases_sold");
				double ConvertedCasesCSD = rs1.getDouble("converted_cases_csd");
				double ConvertedCasesNCB = rs1.getDouble("converted_cases_ncb");

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
				
				
				%>				
				<tr>
					<td nowrap="nowrap" style="font-size:9px"><%=Utilities.truncateStringToMax(rs1.getString("name"),20)%></td>
					<td nowrap="nowrap" style="font-size:9px"><%=Utilities.truncateStringToMax(DistributorNames,233)%></td>
				<%		       
		        String TimeIn =  rs1.getString("avg_time_in");
		        if (TimeIn == null || TimeIn.equals("null")){
		        	TimeIn = "";
		        }
		        String TimeOut =  rs1.getString("avg_time_out");
		        if (TimeOut == null || TimeOut.equals("null")){
		        	TimeOut = "";
		        }
		        %>
		        	<td anowrap="nowrap" style="font-size:9px"><%=TimeIn%></td>
		        	<td anowrap="nowrap" style="font-size:9px"><%=TimeOut%></td>
		        	<td style="text-align:center; font-size:9px"><%=Utilities.getDisplayCurrencyFormatRounded(OutletsPJP)%></td>
		        	<td style="text-align:center; font-size:9px"><%=Utilities.getDisplayCurrencyFormatRounded(OutletsOrdered)%></td>
		        <%				
				if (UnplannedCalls != 0){
					%>
					<td style="text-align:center"><%=Utilities.getDisplayCurrencyFormatRounded(UnplannedCalls)%></td>
					<%
				}else{
					%>
					<td>&nbsp;</td>
					<%
				}
		        
		        
		        
				%>
					<td style="text-align:center; font-size:9px"><%=Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("eco"))%>%</td>
					<td style="text-align:center; font-size:9px"><%=Utilities.getDisplayCurrencyFormatRounded(BillProductivity)%>%</td>
					<td style="text-align:center; font-size:9px"><%=Utilities.getDisplayCurrencyFormatOneDecimal(rs1.getDouble("drop_size"))%></td>
					<td style="text-align:center; font-size:9px"><%=Utilities.getDisplayCurrencyFormatOneDecimal(rs1.getDouble("sku_per_order_csd"))%></td>
					<td style="text-align:center; font-size:9px"><%=Utilities.getDisplayCurrencyFormatOneDecimal(rs1.getDouble("sku_per_order_ncb"))%></td>
					<td style="text-align:center; font-size:9px"><%=Utilities.getDisplayCurrencyFormatOneDecimal(rs1.getDouble("sku_per_bill"))%></td>			
					<td style="background-color: #E4E7F4; text-align:center; font-size:9px"><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesCSD)%></td>
					<td style="background-color: #E4E7F4; text-align:center; font-size:9px"><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesNCB)%></td>
					<td style="background-color: #E4E7F4; text-align:center; font-size:9px"><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCases)%></td>
					<td style="background-color: #EAF3D6; text-align:center; font-size:9px"><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesSSRB250)%></td>
					<td style="background-color: #EAF3D6; text-align:center; font-size:9px"><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesSSRB240)%></td>
					<td style="background-color: #EAF3D6; text-align:center; font-size:9px"><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesSSRBTotal)%></td>
				<%				
				if (TargetCC != 0){					
					%>					
					<td style="text-align:center; font-size:9px"><%=Utilities.getDisplayCurrencyFormatRounded(TargetCC)%></td>
					<td style="text-align:center; font-size:9px"><%=Utilities.getDisplayCurrencyFormatRounded(TargetAchievedCC)%></td>
					<td style="text-align:center; font-size:9px"><%=TargetPercentageString%></td>
					<%
					
				}else{					
					%>					
					<td style="font-size:9px">&nbsp;</td>
					<td style="font-size:9px">&nbsp;</td>
					<td style="font-size:9px">&nbsp;</td>
					<%				
				}				 
				%>				
					<td style="text-align:right; font-size:9px"><%=Utilities.getDisplayCurrencyFormatRounded(OrderAmount)%></td>					
				</tr>
				<%
				
			}
			
			%>
					 <!--  load map here -->
					 
					 <tr>
					 	<td colspan="13" style="text-align:right; font-weight: bold; font-size:9px">Total </td>
					 	
					 	<td style="text-align:right; font-weight: bold; font-size:9px"><%=Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesCSD)%></td>
					 	<td style="text-align:right; font-weight: bold; font-size:9px"><%=Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesNCB)%></td>
					 	<td style="text-align:right; font-weight: bold; font-size:9px"><%=Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCases)%></td>
					 	
					 	<td style="text-align:right; font-weight: bold; font-size:9px"><%=Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesSSRB250)%></td>
					 	<td style="text-align:right; font-weight: bold; font-size:9px"><%=Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesSSRB240)%></td>
					 	<td style="text-align:right; font-weight: bold; font-size:9px"><%=Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCasesSSRBTotal)%></td>
					 	
					 	<td colspan="3" style="font-size:9px">&nbsp;</td>
					 	
					 	<td style="text-align:right; font-weight: bold; font-size:9px"><%=Utilities.getDisplayCurrencyFormatRounded(TotalOrderAmount)%></td>
					 	
					 </tr>
					 
				</table>	
				
				<%
				s.executeUpdate("drop temporary table temp_orderbooker_performance");
				%>
				
		</td>
	</tr>
</table>

	</li>	
</ul>

<%!

private void populateData(Connection c, Date StartDate, Date EndDate, String SND_ID) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
	
	Statement s = c.createStatement();
	Statement s2 = c.createStatement();
	Statement s3 = c.createStatement();
	Statement s5 = c.createStatement();
	
	//s.executeUpdate("SET SESSION group_concat_max_len = 1000000");
	
	s.executeUpdate("create temporary table temp_orderbooker_performance (name varchar(500), percent_before_10am double, percent_after_6pm double, avg_time_in varchar(20), avg_time_out varchar(20),outlets_in_pjp int(5), outlets_ordered int(5), converted_cases_sold int(10), gross_revenue double, eco double, bill_productivity double, sku_per_bill double, pack_size_per_bill double, drop_size double, orderbookers_worked double, unplanned_calls double, converted_cases_csd double, converted_cases_ncb double, sku_per_order_csd double, sku_per_order_ncb double, ssrb_250 double, ssrb_240 double, ssrb_total double, target_days_left double, target_cc double, target_achieved_cc double, target_days_past double, cr_assigned double, is_ncsd double, yesterday_planned_calls double, yesterday_productive_calls double, distributor_names varchar(500), region_name varchar(200))");
	
	//System.out.println("select distinct dbpl.assigned_to, (select display_name from users where id = dbpl.assigned_to) orderbooker_name, (select is_active from users where id = dbpl.assigned_to) is_active, concat(dbpl.distributor_id,'-',right((select name from common_distributors where distributor_id = dbpl.distributor_id),23)) from distributor_beat_plan_log dbpl force index (distributor_beat_plan_log_date_assigned_day_number) where dbpl.log_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and dbpl.distributor_id in (select distributor_id from common_distributors where is_active = 1 and (snd_id in ("+SND_ID+") or rsm_id in ("+SND_ID+"))) and dbpl.distributor_id in (select distinct distributor_id from mobile_order where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+") and dbpl.day_number in ("+Utilities.getDayOfWeekByDate(EndDate)+") and dbpl.assigned_to is not null");
	ResultSet rs80 = s5.executeQuery("select distinct dbpl.assigned_to, (select display_name from users where id = dbpl.assigned_to) orderbooker_name, (select is_active from users where id = dbpl.assigned_to) is_active, concat(dbpl.distributor_id,'-',right((select name from common_distributors where distributor_id = dbpl.distributor_id),23)) from distributor_beat_plan_log dbpl force index (distributor_beat_plan_log_date_assigned_day_number) where dbpl.log_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and dbpl.distributor_id in (select distributor_id from common_distributors where is_active = 1 and (snd_id in ("+SND_ID+") or rsm_id in ("+SND_ID+"))) and dbpl.distributor_id in (select distinct distributor_id from mobile_order where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+") and dbpl.day_number in ("+Utilities.getDayOfWeekByDate(EndDate)+") and dbpl.assigned_to is not null");
	while(rs80.next()){
		
		long OrderbookerID = rs80.getLong(1);
		String OrderbookerName = rs80.getString(2);
		String DistributorNames = rs80.getString(4);
		
		double TotalOrdersFromPlannedOutlets = 0;
		double TotalPlannedOutletsCount = 0;
		double TotalUniqueOutletsInvoiced = 0;
		double TotalOrderCount = 0;
		double TotalTotalQuantitySold = 0;
		double TotalUnplannedCalls = 0;
		double TotalTotalLinesSold = 0;
		double TotalInvoiceCount = 0;
		double TotalTotalLinesSoldCSD = 0;
		double TotalTotalLinesSoldNCB = 0;
		double TotalConvertedCasesSoldCSD = 0;
		double TotalConvertedCasesSoldNCB = 0;
		double TotalConvertedCasesSoldSSRB250 = 0;
		double TotalConvertedCasesSoldSSRB240 = 0;
		double TotalGross = 0;
		
		Date CurrentDate = StartDate;
		int i = 0;
		while(true){
			try{
			String OrderIDs = "0";
			String OutletIDsOrdered = "0";
			String DaysOfWeekIDs = Utilities.getDayOfWeekByDate(CurrentDate)+"";
			double OrderCount = 0;
			double UniqueOutletsOrdered = 0;
			
			//System.out.println("select mo.created_by, group_concat(mo.id) order_ids, group_concat(distinct dayofweek(mo.created_on)) days_of_week, count(mo.id) order_count, count(distinct mo.outlet_id) unique_outlets_ordered, group_concat(distinct mo.outlet_id) outlet_ids_ordered from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and mo.created_by = "+OrderbookerID+" group by mo.created_by");
			ResultSet rs45 = s3.executeQuery("select mo.created_by, group_concat(mo.id) order_ids, group_concat(distinct dayofweek(mo.created_on)) days_of_week, count(mo.id) order_count, count(distinct mo.outlet_id) unique_outlets_ordered, group_concat(distinct mo.outlet_id) outlet_ids_ordered from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and mo.created_by = "+OrderbookerID+" group by mo.created_by");
			if(rs45.first()){
				OrderIDs = rs45.getString("order_ids");
				OutletIDsOrdered = rs45.getString("outlet_ids_ordered");
				OrderCount = rs45.getInt("order_count");
				UniqueOutletsOrdered = rs45.getInt("unique_outlets_ordered");
			}
			
			int isNCSD = 0;
			double OrderBookersWorked = 0;
			double PlannedOutletsCount = 0;
			String PlannedOutletIDs = "";
			
			int OrderBookersAssigned = 0;
			ResultSet rs2 = s3.executeQuery("select count(distinct outlet_id), group_concat(distinct outlet_id) from distributor_beat_plan_log force index (distributor_beat_plan_log_date_assigned_day_number) where log_date between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDate(CurrentDate)+" and assigned_to = "+OrderbookerID+" and day_number in ("+DaysOfWeekIDs+")");
			if (rs2.first()){
				PlannedOutletsCount = rs2.getDouble(1);
				PlannedOutletIDs = rs2.getString(2);
			}



			double OrdersFromPlannedOutlets = 0;
			String OrdersFromPlannedOutletIDs = "0";
			ResultSet rs10 = s3.executeQuery("select count(id) from common_outlets where id in ("+PlannedOutletIDs+") and id in ("+OutletIDsOrdered+")");
			if (rs10.first()){
				OrdersFromPlannedOutlets = rs10.getDouble(1);
			}
			
			double GrossRevenue = 0;
			double UniqueOutletsInvoiced = 0;
			double InvoiceCount = 0;
			
			
			
			ResultSet rs4 = s3.executeQuery("select sum(net_amount), count(distinct outlet_id), count(id) from inventory_sales_adjusted where order_id in ("+OrderIDs+") and outlet_id in ("+PlannedOutletIDs+") and net_amount != 0");
			if(rs4.first()){
				GrossRevenue = rs4.getDouble(1);
				UniqueOutletsInvoiced = rs4.getDouble(2);
				InvoiceCount = rs4.getDouble(3);
			}
			TotalInvoiceCount += InvoiceCount;
			
			double OrderProductivity = 0;
			if (PlannedOutletsCount != 0){
				OrderProductivity = (OrdersFromPlannedOutlets / PlannedOutletsCount) * 100;
			}
			
			TotalOrdersFromPlannedOutlets += OrdersFromPlannedOutlets;
			TotalPlannedOutletsCount += PlannedOutletsCount;
			TotalUniqueOutletsInvoiced += UniqueOutletsInvoiced;
			TotalOrderCount += OrderCount;
			
			
			double BillProductivity = 0;
			if (OrdersFromPlannedOutlets != 0){
				BillProductivity = (UniqueOutletsInvoiced / OrdersFromPlannedOutlets) * 100;
			}

			double TotalQuantitySold = 0;
			ResultSet rs9 = s3.executeQuery("select sum(mop.raw_cases) total_qty_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mop.is_promotion = 0 and mo.id in ("+OrderIDs+")");
			if(rs9.first()){
				TotalQuantitySold = rs9.getDouble("total_qty_sold");
			}
			
			TotalTotalQuantitySold += TotalQuantitySold;
			
			
			double TotalLinesSold = 0;
			ResultSet rs5 = s3.executeQuery("select count(*) total_lines_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mop.is_promotion = 0 and mo.id in ("+OrderIDs+")");
			if(rs5.first()){
				TotalLinesSold = rs5.getDouble("total_lines_sold");
			}
			TotalTotalLinesSold += TotalLinesSold;
			
			double TotalLinesSoldCSD = 0;
			ResultSet rs13 = s3.executeQuery("select count(*) total_lines_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products ip on mop.product_id = ip.id where ip.type_id = 1 and mop.is_promotion = 0 and mo.id in ("+OrderIDs+")");
			if(rs13.first()){
				TotalLinesSoldCSD = rs13.getDouble("total_lines_sold");
			}
			TotalTotalLinesSoldCSD += TotalLinesSoldCSD;
			
			double TotalLinesSoldNCB = 0;
			ResultSet rs14 = s3.executeQuery("select count(*) total_lines_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products ip on mop.product_id = ip.id where ip.type_id = 2 and mop.is_promotion = 0 and mo.id in ("+OrderIDs+")");
			if(rs14.first()){
				TotalLinesSoldNCB = rs14.getDouble("total_lines_sold");
			}
			TotalTotalLinesSoldNCB += TotalLinesSoldNCB;
			
			
			double ConvertedCasesSoldCSD = 0;
			ResultSet rs12 = s3.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where ipv.type_id = 1 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") ");
			if(rs12.first()){
				ConvertedCasesSoldCSD = rs12.getDouble(1);
			}
			TotalConvertedCasesSoldCSD += ConvertedCasesSoldCSD;
			
			double ConvertedCasesSoldNCB = 0;
			ResultSet rs11 = s3.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where ipv.type_id = 2 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") ");
			if(rs11.first()){
				ConvertedCasesSoldNCB = rs11.getDouble(1);
			}
			TotalConvertedCasesSoldNCB += ConvertedCasesSoldNCB;
			
			double ConvertedCasesSoldSSRB250 = 0;
			ResultSet rs16 = s3.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where ipv.ssrb_type_id = 1 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") ");
			if(rs16.first()){
				ConvertedCasesSoldSSRB250 = rs16.getDouble(1);
			}
			TotalConvertedCasesSoldSSRB250 += ConvertedCasesSoldSSRB250;
			
			double ConvertedCasesSoldSSRB240 = 0;
			ResultSet rs17 = s3.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where ipv.ssrb_type_id = 2 and isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") ");
			if(rs17.first()){
				ConvertedCasesSoldSSRB240 = rs17.getDouble(1);
			}
			TotalConvertedCasesSoldSSRB240 += ConvertedCasesSoldSSRB240;
			
			ResultSet rs18 = s3.executeQuery("select sum(net_amount) from inventory_sales_adjusted where order_id in ("+OrderIDs+") and outlet_id in ("+PlannedOutletIDs+") and net_amount != 0");
			if(rs18.first()){
				GrossRevenue = rs18.getDouble(1);
			}
			TotalGross += GrossRevenue;
			
			double DropSize = 0;
			if (InvoiceCount != 0){
				 DropSize = TotalQuantitySold / OrderCount;
			}
			
			double UnplannedOutletCount = UniqueOutletsOrdered - OrdersFromPlannedOutlets; 
			TotalUnplannedCalls += UnplannedOutletCount;
			
			//System.out.println(CurrentDate + " " +EndDate);
			if(DateUtils.isSameDay(CurrentDate, EndDate)){
				break;
			}
			}catch(Exception e){}
			CurrentDate = Utilities.getDateByDays(CurrentDate, 1);
			i++;
			
		}			
		
		
		double YesterdayPlannedOutletsCount = 0;
		double YesterdayOrdersFromPlannedOutlets = 0;
		
		User RSM = EmployeeHierarchy.getRSM(OrderbookerID);
		
		double ConvertedCasesSoldCSD = TotalConvertedCasesSoldCSD;
		double ConvertedCasesSoldNCB = TotalConvertedCasesSoldNCB;
		double ConvertedCasesSold = ConvertedCasesSoldCSD + ConvertedCasesSoldNCB;
		double ConvertedCasesSoldSSRB250 = TotalConvertedCasesSoldSSRB250;
		double ConvertedCasesSoldSSRB240 = TotalConvertedCasesSoldSSRB240;
		double ConvertedCasesSoldSSRBTotal = ConvertedCasesSoldSSRB250 + ConvertedCasesSoldSSRB240;
		
		double GrossRevenue = TotalGross;
		double ECO = 0;
		
		double InvoiceCount = TotalOrderCount;
		double OrdersFromPlannedOutlets = TotalOrdersFromPlannedOutlets;
		double PlannedOutletsCount = TotalPlannedOutletsCount;
		double UniqueOutletsInvoiced = TotalUniqueOutletsInvoiced;
		
		if (PlannedOutletsCount != 0){
			ECO = (OrdersFromPlannedOutlets / PlannedOutletsCount) * 100;
		}
		if (ECO > 100){
			ECO = 100;
		}
		double UniqueOutletsOrdered = 0;
		double UnplannedOutletCount = TotalUnplannedCalls;//UniqueOutletsOrdered - OrdersFromPlannedOutlets;
		
		double BillProductivity = 0;
		if (OrdersFromPlannedOutlets != 0){
			BillProductivity = (UniqueOutletsInvoiced / OrdersFromPlannedOutlets) * 100;
		}
		if (BillProductivity > 100){
			BillProductivity = 100;
		}
		
		double TotalLinesSold = TotalTotalLinesSold;
		double TotalLinesSoldCSD = TotalTotalLinesSoldCSD;
		double TotalLinesSoldNCB = TotalTotalLinesSoldNCB;
		double TotalQuantitySold = TotalTotalQuantitySold;
		
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
//		ResultSet rs6 = s3.executeQuery("select count(package_count) package_total from (" +
//		" select mop.id, count(*) package_count  from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mop.is_promotion = 0 and mo.id in ("+OrderIDs+") group by mop.id,ipv.package_id " +
//		" ) tb1");
//		if(rs6.first()){
//			PackagesSold = rs6.getDouble("package_total");
//		}
		
		double PackPerBill = 0;
		if (InvoiceCount != 0){
			PackPerBill = PackagesSold / InvoiceCount;
		}
		
		Date AvgTimeIn= new Date();
		
		ResultSet rs7 = s3.executeQuery("SELECT sec_to_time(avg(time_to_sec(time_format(first_order_time,'%H:%i')))) avg_time_in from (" +
				" SELECT min(mobile_timestamp) first_order_time FROM mobile_order where mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and created_by="+OrderbookerID+" group by date(mobile_timestamp)" +
				" ) tbl1");
		
		if(rs7.first()){				
			AvgTimeIn = rs7.getTime("avg_time_in");
		}
		
		Date AvgTimeOut= new Date();
		
		ResultSet rs8 = s3.executeQuery("SELECT sec_to_time(avg(time_to_sec(time_format(last_order_time,'%H:%i')))) avg_time_out from (" +
				" SELECT max(mobile_timestamp) last_order_time FROM mobile_order where mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and created_by="+OrderbookerID+" group by date(mobile_timestamp)" +
				" ) tbl2");
		if(rs8.first()){				
			AvgTimeOut = rs8.getTime("avg_time_out");
		}
		
		
		
		int DaysLeft = 0;
		long TargetCC = 0;
		long TargetAchievedCC = 0;
		int DaysPast = 0;
//		ResultSet rs15 = s3.executeQuery("SELECT id, et.employee_id, et.month, et.year, ( "+
//				"select sum(((etp.quantity * ip.unit_per_case) * ip.liquid_in_ml) / ip.conversion_rate_in_ml) converted_cases from employee_targets_packages etp join inventory_packages ip on etp.package_id = ip.id where etp.id = et.id "+
//			") converted_cases_target, ( "+
//				"select sum((isap.total_units * ipv.liquid_in_ml / ipv.conversion_rate_in_ml)) converted_cases_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.booked_by = "+OrderbookerID+" and isap.is_promotion = 0 and isa.created_on between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" "+
//			") converted_cases_sold FROM employee_targets et where et.employee_id = "+OrderbookerID+" and et.month = date_format(date("+Utilities.getSQLDate(StartDate)+"),'%m') and et.year = date_format(date("+Utilities.getSQLDate(StartDate)+"),'%Y')");
//		
//		if (rs15.first()){
//			TargetCC = Math.round(rs15.getDouble("converted_cases_target"));
//			TargetAchievedCC = Math.round(rs15.getDouble("converted_cases_sold"));
//		}
		
		
		
		
		
		
		s2.executeUpdate("insert into temp_orderbooker_performance (name, avg_time_in, avg_time_out, outlets_in_pjp, outlets_ordered, converted_cases_sold, gross_revenue, eco, bill_productivity, sku_per_bill, pack_size_per_bill, drop_size, orderbookers_worked,unplanned_calls, converted_cases_csd, converted_cases_ncb, sku_per_order_csd, sku_per_order_ncb, ssrb_250, ssrb_240, ssrb_total, target_days_left, target_cc, target_achieved_cc, target_days_past, cr_assigned, is_ncsd, yesterday_planned_calls, yesterday_productive_calls, distributor_names, region_name)"+
								"values('"+OrderbookerID+" - "+OrderbookerName+"','"+Utilities.getDisplayTimeFormat(AvgTimeIn)+"','"+Utilities.getDisplayTimeFormat(AvgTimeOut)+"',"+PlannedOutletsCount+","+OrdersFromPlannedOutlets+","+ConvertedCasesSold+","+GrossRevenue+","+ECO+","+BillProductivity+","+SKUPerBill+","+PackPerBill+","+DropSize+",0,"+UnplannedOutletCount+","+ConvertedCasesSoldCSD+","+ConvertedCasesSoldNCB+","+SKUPerBillCSD+","+SKUPerBillNCB+","+ConvertedCasesSoldSSRB250+","+ConvertedCasesSoldSSRB240+","+ConvertedCasesSoldSSRBTotal+","+DaysLeft+","+TargetCC+","+TargetAchievedCC+","+DaysPast+", 0,0,"+YesterdayPlannedOutletsCount+","+YesterdayOrdersFromPlannedOutlets+",'"+DistributorNames+"','"+RSM.USER_ID+"-"+RSM.USER_DISPLAY_NAME+"') ");
		
		
		
	
	}
}

%>


<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>