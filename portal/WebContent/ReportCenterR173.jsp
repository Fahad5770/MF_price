<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="com.pbc.reports.SalesIndex"%>
<%@page import="java.util.Calendar"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>



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


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 210;

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


//out.print("StartDate = "+StartDate);
//out.print("EndDate = "+EndDate);


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
if (HODIDs.length() > 0){
	WhereHOD = " and (snd_id in ("+HODIDs+") or rsm_id in ("+HODIDs+")) ";	
}





//RSM


String RSMIDs="";
long SelectedRSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRSM") != null){
	SelectedRSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRSM");
	RSMIDs = Utilities.serializeForSQL(SelectedRSMArray);
}

String WhereRSM = "";
if (RSMIDs.length() > 0){
	WhereRSM = " and mo.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
}


//Distributor

long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");           	
}else{
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	}
}

String DistributorIDs = "";
String WhereDistributors = "";
if(SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0){
	for(int i = 0; i < SelectedDistributorsArray.length ; i++){
		if(i == 0){
			DistributorIDs += SelectedDistributorsArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorsArray[i];
		}
	}
	WhereDistributors = " and mo.distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}

//OrderBooker

boolean IsOrderBookerSelected=false;

int OrderBookerArrayLength=0;
long SelectedOrderBookerArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers");
	
	IsOrderBookerSelected=true;
	OrderBookerArrayLength=SelectedOrderBookerArray.length;
}



String OrderBookerIDs = "";
if(SelectedOrderBookerArray != null && SelectedOrderBookerArray.length > 0){
	for(int i = 0; i < SelectedOrderBookerArray.length; i++){
		if(i == 0){
			OrderBookerIDs += SelectedOrderBookerArray[i];
		}else{
			OrderBookerIDs += ", "+SelectedOrderBookerArray[i];
		}
	}
}
String OrderBookerIDsWher="";
if(OrderBookerIDs.length()>0){
	OrderBookerIDsWher =" and mo.created_by in ("+OrderBookerIDs+") ";
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
	WhereSM = " and mo.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
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
	WhereTDM = " and mo.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
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
	WhereASM = " and mo.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
}


if (WhereHOD.length() > 0 || WhereASM.length() > 0 || WhereTDM.length() > 0 || WhereSM.length() > 0){


%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Order Booker Incentive</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size:10px;">
							<th data-priority="1"  style="text-align:center; width:12%;background-color:#F7FFF7;" title="">Date</th>							
							<th data-priority="1"  style="text-align:center; width:12%;background-color:#FFFFE6;" title="">Order Productivity</th>							
							<th data-priority="1"  style="text-align:center; width:12%;background-color:#FFFFE6;" title="">Bill Productivity</th>
							<th data-priority="1"  style="text-align:center; width:12%;background-color:#FFFFE6;" title="">Drop Size</th>
							<th data-priority="1"  style="text-align:center; width:12%;background-color:#FAFAF8;" title="">Incentive</th>
					
							
													
					    </tr>
					  </thead> 
					<tbody>
						<%
						
						ResultSet rs = s.executeQuery("select mo.created_by, (select display_name from users where id = mo.created_by) cr_name from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+OrderBookerIDsWher+" group by mo.created_by");
						while(rs.next()){
							long CRID = rs.getLong(1);
							String CRName = rs.getString(2);
							
							%>
							<tr>
								<td colspan="5"><b><%=CRID + "-"+CRName %></b></td>
							</tr>
							<%
							if (StartDate != null && EndDate != null){
								
								
								double TotalIncentive = 0; 
								double TotalOrdersFromPlannedOutlets = 0;
								double TotalPlannedOutletsCount = 0;
								double TotalUniqueOutletsInvoiced = 0;
								double TotalOrderCount = 0;
								double TotalTotalQuantitySold = 0;
								
								Date CurrentDate = StartDate;
								int i = 0;
								while(true){
									
											//ResultSet rs80 = s2.executeQuery("select distinct dbpl.assigned_to, (select display_name from users where id = dbpl.assigned_to) orderbooker_name, (select is_active from users where id = dbpl.assigned_to) is_active from distributor_beat_plan_log dbpl force index (distributor_beat_plan_log_date_assigned_day_number) where dbpl.log_date between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDate(CurrentDate)+" and dbpl.distributor_id in (select distributor_id from common_distributors where is_active = 1 "+WhereHOD+") and dbpl.distributor_id in (select distinct distributor_id from mobile_order where created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+") and dbpl.day_number = "+Utilities.getDayOfWeekByDate(CurrentDate)+" and dbpl.assigned_to = "+CRID);
											//while(rs80.next()){
												
												long OrderbookerID = CRID;
												//String OrderbookerName = rs80.getString(2);
											
												
												String OrderIDs = "0";
												String OutletIDsOrdered = "0";
												String DaysOfWeekIDs = Utilities.getDayOfWeekByDate(CurrentDate)+"";
												double OrderCount = 0;
												double UniqueOutletsOrdered = 0;
											
											ResultSet rs45 = s3.executeQuery("select mo.created_by, group_concat(mo.id) order_ids, group_concat(distinct dayofweek(mo.created_on)) days_of_week, count(mo.id) order_count, count(distinct mo.outlet_id) unique_outlets_ordered, group_concat(distinct mo.outlet_id) outlet_ids_ordered from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and mo.created_by = "+CRID+" group by mo.created_by");
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
												
												double DropSize = 0;
												if (InvoiceCount != 0){
													 DropSize = TotalQuantitySold / OrderCount;
												}
									
												double incentive = 0;
												
												if (OrderProductivity >= 70 && BillProductivity >= 90 && DropSize >= 4){
													incentive = 150;
													TotalIncentive += incentive;
												}
												
												%>
					
												<tr>
												<td style="text-align:center;"><%= Utilities.getDisplayDateFormat(CurrentDate) %></td>							
																		
												<td style="text-align:center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(OrderProductivity) %>%</td>							
												<td style="text-align:center;"><%=Utilities.getDisplayCurrencyFormatRounded(BillProductivity) %>%</td>
												<td style="text-align:center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(DropSize) %></td>
												<td style="text-align: right;<%if (incentive > 0){%>background-color:#F7FFF7;<%} %>"> <%=Utilities.getDisplayCurrencyFormatOneDecimal(incentive) %></td>	
												
												</tr>
												
												<%
										
											//}
											if(CurrentDate.equals(EndDate)){
												break;
											}
											CurrentDate = DateUtils.addDays(CurrentDate, 1);
											i++;
								}
								
								
								double TotalOrderProductivity = 0;
								if (TotalPlannedOutletsCount != 0){
									TotalOrderProductivity = (TotalOrdersFromPlannedOutlets / TotalPlannedOutletsCount) * 100;
								}
								
								double TotalBillProductivity = 0;
								if (TotalOrdersFromPlannedOutlets != 0){
									TotalBillProductivity = (TotalUniqueOutletsInvoiced / TotalOrdersFromPlannedOutlets) * 100;
								}
					
								double TotalDropSize = 0;
								if (TotalOrderCount != 0){
									 TotalDropSize = TotalTotalQuantitySold / TotalOrderCount;
								}
								
								
								%>
												<tr>
												<td colspan="1" style="text-align: right">Total</td>
												<td colspan="1" style="text-align: center"><%=Utilities.getDisplayCurrencyFormatOneDecimal(TotalOrderProductivity) %>%</td>
												<td colspan="1" style="text-align: center"><%=Utilities.getDisplayCurrencyFormatOneDecimal(TotalBillProductivity) %>%</td>
												<td colspan="1" style="text-align: center"><%=Utilities.getDisplayCurrencyFormatOneDecimal(TotalDropSize) %></td>
												<td  style="text-align: right; font-weight: bold;<%if (TotalIncentive > 0){%>background-color:#F7FFF7;<%} %>" > <%=Utilities.getDisplayCurrencyFormatOneDecimal(TotalIncentive) %></td>	
												</tr>
								
								<%
							}
							
							
							
							%>


							
							<%
						}
						
						%>
						
					</tbody>
							
				</table>
		</td>
	</tr>
</table>

	</li>	
</ul>

<%

}else{
	%>
	<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
	<li data-role="list-divider" data-theme="a">Order Booker KPIs</li>
	<li>
	<span style="font-weight: normal;">Please select a Date Range and HOD to proceed</span>
	</li>
	</ul>
	<%
}

s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>