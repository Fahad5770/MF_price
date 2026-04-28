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
int FeatureID = 108;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
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
	WhereHOD = " and mo.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
<li data-role="list-divider" data-theme="a">Order Booker KPIs</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size:10px;">
							<th data-priority="1"  style="text-align:center; width:23%;">&nbsp;</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#F7FFF7;" title="No. of Order Bookers worked in selected date period">Order Bookers Worked</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FFFFE6;" title="Mobile Timestamp of First Order">Check In</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FFFFE6;" title="Mobile Timestamp of Last Order">Check Out</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#F7FFF7;" title="No. of Outlets in PJP for selected date period">Outlets Planned</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#F7FFF7;" title="No. of Outlets placed Order in selected date period">Outlets Ordered</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FFFFE6;" title="Strike Rate = Unique Outlets Ordered / Planned Unique Outlets">Strike Rate</th>							
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FFFFE6;" title="SKU/Order = Total Lines Sold / Total Orders (excluding promotions)">SKU/Order</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FFFFE6;" title="Pack Size/Order = Total SKUs per Order / Total Orders (excluding promotions)">Pack Size/Order</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FFFFE6;" title="Drop Size = Total Quantity per Order / Total Order (excluding promotions)">Drop Size</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FAFAF8;" title="Converted Cases Ordered (Excluding Promotions)">Converted Cases Ordered</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FAFAF8;" title="Amount of Orders">Order Amount</th>
					    </tr>
					  </thead> 
					<tbody>
						<%

						
						s.executeUpdate("SET SESSION group_concat_max_len = 1000000");
						
						s.executeUpdate("create temporary table temp_orderbooker_performance (name varchar(500), percent_before_10am double, percent_after_6pm double, avg_time_in varchar(20), avg_time_out varchar(20),outlets_in_pjp int(5), outlets_ordered int(5), converted_cases_sold int(10), gross_revenue double, eco double, bill_productivity double, sku_per_bill double, pack_size_per_bill double, drop_size double, orderbookers_worked double)");
												
						ResultSet rs = s.executeQuery("select mo.distributor_id, (select name from common_distributors where distributor_id = mo.distributor_id) distributor_name, group_concat(mo.id) order_ids, group_concat(distinct dayofweek(mo.created_on)) days_of_week, count(mo.id) order_count, count(distinct mo.outlet_id) unique_outlets_ordered, count(distinct created_by) orderbookers_worked from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+OrderBookerIDsWher+" group by mo.distributor_id");
						
						while(rs.next()){
							long DistributorID = rs.getLong(1);
							String DistributorName = rs.getString(2);
							
							double OrderBookersWorked = rs.getDouble("orderbookers_worked");
							
							String OrderIDs = rs.getString("order_ids");
							String DaysOfWeekIDs = rs.getString("days_of_week");
							double OrderCount = rs.getInt("order_count"); 
							double OrderCountBefore10AM = 0;//rs.getInt("count_before_10am");
							double OrderCountAfter6PM = 0;//rs.getInt("count_after_6pm");
							double UniqueOutletsOrdered = rs.getInt("unique_outlets_ordered");
							
							//System.out.println(CRID+" "+OrderCount+" "+ OrderCountBefore10AM +" " +OrderCountAfter6PM);
							/*
							double PercentBefore10AM = 0;
							if (OrderCount != 0){
								PercentBefore10AM = (OrderCountBefore10AM / OrderCount) * 100;
							}
							double PercentAfter6PM = 0;
							if (OrderCount != 0){
								PercentAfter6PM = (OrderCountAfter6PM / OrderCount) * 100;
							}
							*/
							double PlannedOutletsCount = 0;
							
							ResultSet rs2 = s3.executeQuery("select count(distinct outlet_id) from distributor_beat_plan_log force index (distributor_beat_plan_log_date_distributor_id_day_number) where log_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and distributor_id = "+DistributorID+" and day_number in ("+DaysOfWeekIDs+") and assigned_to is not null");
							if (rs2.first()){
								PlannedOutletsCount = rs2.getDouble(1);
							}
							
							double ConvertedCasesSold = 0;
							ResultSet rs3 = s3.executeQuery("select sum(((mop.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mop.is_promotion = 0 and mo.id in ("+OrderIDs+") ");
							if(rs3.first()){
								ConvertedCasesSold = rs3.getDouble(1);
							}
							
							double GrossRevenue = 0;
							double UniqueOutletsInvoiced = 0;
							double InvoiceCount = 0;
							ResultSet rs4 = s3.executeQuery("select sum(net_amount), count(distinct outlet_id), count(id) from mobile_order where id in ("+OrderIDs+") and net_amount != 0");
							if(rs4.first()){
								GrossRevenue = rs4.getDouble(1);
								UniqueOutletsInvoiced = rs4.getDouble(2);
								InvoiceCount = rs4.getDouble(3);
							}
							if (DistributorID == 100669){
								System.out.println("100669\nselect sum(net_amount), count(distinct outlet_id), count(id) from mobile_order where id in ("+OrderIDs+") and net_amount != 0");
							}
							double ECO = 0;
							if (PlannedOutletsCount != 0){
								ECO = (UniqueOutletsOrdered / PlannedOutletsCount) * 100;
							}
							if (ECO > 100){
								ECO = 100;
							}
							
							double BillProductivity = 0;
							if (PlannedOutletsCount != 0){
								BillProductivity = (UniqueOutletsInvoiced / PlannedOutletsCount) * 100;
							}
							if (BillProductivity > 100){
								BillProductivity = 100;
							}
							
							double TotalLinesSold = 0;
							ResultSet rs5 = s3.executeQuery("select count(*) total_lines_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mop.is_promotion = 0 and mo.id in ("+OrderIDs+")");
							if(rs5.first()){
							 TotalLinesSold = rs5.getDouble("total_lines_sold");
							}
							
							double TotalQuantitySold = 0;
							ResultSet rs9 = s3.executeQuery("select sum(mop.raw_cases) total_qty_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mop.is_promotion = 0 and mo.id in ("+OrderIDs+")");
							if(rs9.first()){
								TotalQuantitySold = rs9.getDouble("total_qty_sold");
							}
							
							double SKUPerBill = 0;
							if (InvoiceCount != 0){
							 SKUPerBill = TotalLinesSold / InvoiceCount;
							}
							
							double DropSize = 0;
							if (InvoiceCount != 0){
								 DropSize = TotalQuantitySold / InvoiceCount;
							}
							
							double PackagesSold = 0;
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
							
							s2.executeUpdate("insert into temp_orderbooker_performance (name, avg_time_in, avg_time_out, outlets_in_pjp, outlets_ordered, converted_cases_sold, gross_revenue, eco, bill_productivity, sku_per_bill, pack_size_per_bill, drop_size, orderbookers_worked)"+
													"values('"+DistributorID+" - "+DistributorName+"','"+Utilities.getDisplayTimeFormat(AvgTimeIn)+"','"+Utilities.getDisplayTimeFormat(AvgTimeOut)+"',"+PlannedOutletsCount+","+UniqueOutletsOrdered+","+ConvertedCasesSold+","+GrossRevenue+","+ECO+","+BillProductivity+","+SKUPerBill+","+PackPerBill+","+DropSize+","+OrderBookersWorked+") ");
							
						}
						
						int TotalOutletsPJP = 0;
						int TotalOutletsOrdered = 0;
						int TotalConvertedCases = 0;
						double TotalOrderAmount = 0;
						
						ResultSet rs1 = s3.executeQuery("select * from temp_orderbooker_performance order by eco desc");
						while(rs1.next()){	
							int OutletsPJP = rs1.getInt("outlets_in_pjp");
							int OutletsOrdered = rs1.getInt("outlets_ordered");
							int ConvertedCases = rs1.getInt("converted_cases_sold");
							double OrderAmount = rs1.getDouble("gross_revenue"); 
							TotalOutletsPJP += OutletsPJP;
							TotalOutletsOrdered += OutletsOrdered;
							TotalConvertedCases += ConvertedCases;
							TotalOrderAmount += OrderAmount;
						%>
						<tr>
							<td><%=rs1.getString("name") %></td>							
							<td style="text-align:center;background-color:#F7FFF7;"><%if (rs1.getDouble("orderbookers_worked") != 0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("orderbookers_worked")) %><%} %></td>
							<td style="text-align:center;background-color:#FFFFE6;"><%=rs1.getString("avg_time_in") %></td>
							<td style="text-align:center;background-color:#FFFFE6;"><%=rs1.getString("avg_time_out") %></td>							
							<td style="text-align:center;background-color:#F7FFF7;"><%=OutletsPJP %></td>
							<td style="text-align:center;background-color:#F7FFF7;"><%=OutletsOrdered %></td>
							<td style="text-align:center;background-color:#FFFFE6;"><%=Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("eco")) %>%</td>
							<td style="text-align:center;background-color:#FFFFE6;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(rs1.getDouble("sku_per_bill")) %></td>							
							<td style="text-align:center;background-color:#FFFFE6;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(rs1.getDouble("pack_size_per_bill")) %></td>							
							<td style="text-align:center;background-color:#FFFFE6;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(rs1.getDouble("drop_size")) %></td>
							<td style="text-align:center;background-color:#FAFAF8;"><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCases) %></td>
							<td style="text-align:right;background-color:#FAFAF8;"><%=Utilities.getDisplayCurrencyFormatRounded(OrderAmount) %></td>
							
						</tr>
						<%
						
						}
						
						s.executeUpdate("drop temporary table temp_orderbooker_performance");
						%>
						
						<tr>
							<td></td>							
							<td style="text-align:center;background-color:#F7FFF7;"></td>
							<td style="text-align:center;background-color:#FFFFE6;"></td>
							<td style="text-align:center;background-color:#FFFFE6;"></td>							
							<td style="text-align:center;background-color:#F7FFF7;"><%=TotalOutletsPJP %></td>
							<td style="text-align:center;background-color:#F7FFF7;"><%=TotalOutletsOrdered %></td>
							<td style="text-align:center;background-color:#FFFFE6;"></td>
							<td style="text-align:center;background-color:#FFFFE6;"></td>							
							<td style="text-align:center;background-color:#FFFFE6;"></td>							
							<td style="text-align:center;background-color:#FFFFE6;"></td>
							<td style="text-align:center;background-color:#FAFAF8;"><%=Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCases) %></td>
							<td style="text-align:right;background-color:#FAFAF8;"><%=Utilities.getDisplayCurrencyFormatRounded(TotalOrderAmount) %></td>
							
						</tr>
						
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