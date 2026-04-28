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
<%@page import="com.pbc.employee.EmployeeHierarchy"%>
<%@page import="com.pbc.common.User"%>

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
ds.createConnectionToReplica();

Statement s = ds.createStatement();
Statement s2 = ds.createStatement();
Statement s3 = ds.createStatement();
Statement s4 = ds.createStatement();
Statement s5 = ds.createStatement();

//Date date = Utilities.parseDate(request.getParameter("Date"));


Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");
StartDate = EndDate;

if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	StartDate = new Date();
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}


//System.out.print("StartDate = "+StartDate);
//System.out.print("EndDate = "+EndDate);


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
	WhereHOD = " and co.snd_id in ("+HODIDs+") ";	
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
	WhereRSM = " and co.rsm_id in ("+RSMIDs+") ";	
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
	WhereDistributors = " and co.distributor_id in ("+DistributorIDs+") ";
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
	OrderBookerIDsWher =" and dbpl.assigned_to in ("+OrderBookerIDs+") ";
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


//if (WhereHOD.length() > 0 || WhereASM.length() > 0 || WhereTDM.length() > 0 || WhereSM.length() > 0){


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
							<th data-priority="1"  style="text-align:center; width:19%;">&nbsp;</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#F7FFF7;" atitle="Percetange of orders taken before 10AM">Check In</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#F7FFF7;" atitle="Percetange of orders taken after 6PM">Check Out</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FFFFE6;" atitle="Average time of first order booked">Planned Calls</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FFFFE6;" atitle="Average time of last order booked">Productive Calls</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FFFFE6;" atitle="No. of Outlets in PJP for selected date period">Unplanned Calls</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FFFFE6;" atitle="ECO = Unique Outlets Ordered / Planned Unique Outlets">Productivity</th>							
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FFFFE6;" atitle="Bill Productivity = Unqiue Outlets Invoiced / Planned Universe">Drop Size</th>							
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FFFFE6;" atitle="SKU/Bill = Total Lines Sold / Total Invoices (excluding promotions)">SKU/Order</th>
							<th data-priority="1"  style="text-align:center; width:8%;background-color:#FAFAF8;" taitle="Pack Size/Bill = Total SKUs per Invoice / Total Invoices (excluding promotions)">Converted Cases (Orders)</th>
							<th data-priority="1"  style="text-align:center; width:8%;background-color:#FAFAF8;" taitle="Drop Size = Total Quantity per Invoice / Total Invoices (excluding promotions)">Converted Cases (Sales)</th>
							<th data-priority="1"  style="text-align:center; width:8%;background-color:#FAFAF8;" taitle="Converted Cases Sold (Excluding Promotions)">Amount (Orders)</th>
							<th data-priority="1"  style="text-align:center; width:8%;background-color:#FAFAF8;" taitle="Gross of Invoices">Amount (Sales)</th>
					    </tr>
					  </thead> 
					<tbody>
						<%

						s.executeUpdate("SET SESSION group_concat_max_len = 1000000");
						
						s.executeUpdate("create temporary table temp_orderbooker_performance (name varchar(500), percent_before_10am double, percent_after_6pm double, avg_time_in varchar(20), avg_time_out varchar(20),outlets_in_pjp int(5), outlets_ordered int(5), converted_cases_sold int(10), gross_revenue double, eco double, bill_productivity double, sku_per_bill double, pack_size_per_bill double, drop_size double, orderbookers_worked double, unplanned_calls double, converted_cases_csd double, converted_cases_ncb double, sku_per_order_csd double, sku_per_order_ncb double, ssrb_250 double, ssrb_240 double, ssrb_total double, target_days_left double, target_cc double, target_achieved_cc double, target_days_past double, cr_assigned double, is_ncsd double, yesterday_planned_calls double, yesterday_productive_calls double, distributor_names varchar(500), region_name varchar(200), amount_sales double, sales_converted_cases double)");
												
						
						
						ResultSet rs80 = s5.executeQuery("select distinct dbpl.assigned_to, (select display_name from users where id = dbpl.assigned_to) orderbooker_name, (select is_active from users where id = dbpl.assigned_to) is_active from distributor_beat_plan_log dbpl force index (distributor_beat_plan_log_date_assigned_day_number) where dbpl.log_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and dbpl.distributor_id in (select co.distributor_id from common_distributors co where is_active = 1 "+WhereDistributors+WhereHOD+WhereRSM+") and dbpl.distributor_id in (select distinct distributor_id from mobile_order where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+") and dbpl.day_number = "+Utilities.getDayOfWeekByDate(StartDate)+" and dbpl.assigned_to is not null "+OrderBookerIDsWher);
						while(rs80.next()){
							
							long OrderbookerID = rs80.getLong(1);
							String OrderbookerName = rs80.getString(2);
						
							
							String OrderIDs = "0";
							String OutletIDsOrdered = "0";
							String DaysOfWeekIDs = Utilities.getDayOfWeekByDate(StartDate)+"";
							double OrderCount = 0;
							double UniqueOutletsOrdered = 0;
						
						ResultSet rs = s.executeQuery("select mo.created_by, group_concat(mo.id) order_ids, group_concat(distinct dayofweek(mo.created_on)) days_of_week, count(mo.id) order_count, count(distinct mo.outlet_id) unique_outlets_ordered, group_concat(distinct mo.outlet_id) outlet_ids_ordered from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and mo.created_by = "+rs80.getLong(1)+" group by mo.created_by");
						if(rs.first()){
							OrderIDs = rs.getString("order_ids");
							OutletIDsOrdered = rs.getString("outlet_ids_ordered");
							OrderCount = rs.getInt("order_count");
							UniqueOutletsOrdered = rs.getInt("unique_outlets_ordered");
						}
							
							int isNCSD = 0;
							
							double OrderBookersWorked = 0;
							double PlannedOutletsCount = 0;
							String PlannedOutletIDs = "";
							
							int OrderBookersAssigned = 0;
							ResultSet rs2 = s3.executeQuery("select count(distinct outlet_id), group_concat(distinct outlet_id) from distributor_beat_plan_log force index (distributor_beat_plan_log_date_assigned_day_number) where log_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and assigned_to = "+OrderbookerID+" and day_number in ("+DaysOfWeekIDs+")");
							if (rs2.first()){
								PlannedOutletsCount = rs2.getDouble(1);
								PlannedOutletIDs = rs2.getString(2);
							}
							
							
							// Yesterday Productivity
							
							//Calendar c = Calendar.getInstance();
							//c.setTime(Yesterday);
							int YesterdayDayOfWeek = 0;//c.get(Calendar.DAY_OF_WEEK);			
							double YesterdayPlannedOutletsCount = 0;
							String YesterdayPlannedOutletIDs = "";
							/*
							ResultSet rs51 = s3.executeQuery("select count(distinct outlet_id), group_concat(distinct outlet_id) from distributor_beat_plan_log force index (distributor_beat_plan_log_date_assigned_day_number) where log_date between "+Utilities.getSQLDate(Yesterday)+" and "+Utilities.getSQLDate(Yesterday)+" and assigned_to = "+OrderbookerID+" and day_number in ("+YesterdayDayOfWeek+")");
							if (rs51.first()){
								YesterdayPlannedOutletsCount = rs51.getDouble(1);
								YesterdayPlannedOutletIDs = rs51.getString(2);
							}
							
							double YesterdayOrdersFromPlannedOutlets = 0;
							ResultSet rs52 = s3.executeQuery("select count(distinct outlet_id) from mobile_order where created_on between "+Utilities.getSQLDate(Yesterday)+" and "+Utilities.getSQLDateNext(Yesterday)+" and outlet_id in ("+YesterdayPlannedOutletIDs+") and created_by = "+OrderbookerID);
							if (rs52.first()){
								YesterdayOrdersFromPlannedOutlets = rs52.getDouble(1);
							}
							*/
							// Yesterday Productivity
							
							/*
							 "select distinct concat(mo.distributor_id,'-',(select name from common_distributors where distributor_id = mo.distributor_id)) from mobile_order mo where mo.id in ("+OrderIDs+") order by distributor_id"
							 * 
							 * 
							 * "select distinct concat(mo.distributor_id,'-',right((select name from common_distributors where distributor_id = mo.distributor_id),15)) from mobile_order mo where mo.id in ("+OrderIDs+") order by distributor_id"
							 * */
							
							String DistributorNames = ""; 
							
							ResultSet rs70 = s3.executeQuery("select distinct concat(dbpl.distributor_id,'-',right((select name from common_distributors where distributor_id = dbpl.distributor_id),23)) from distributor_beat_plan_log dbpl force index (distributor_beat_plan_log_date_assigned_day_number) where dbpl.log_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and dbpl.assigned_to = "+OrderbookerID+" and dbpl.day_number in ("+DaysOfWeekIDs+")");
							while (rs70.next()){
								if (!rs70.isFirst()){
									DistributorNames += "\n";
								}
								DistributorNames += Utilities.truncateStringToMax(rs70.getString(1),55);
							}
							
							
							User RSM = EmployeeHierarchy.getRSM(OrderbookerID);
							
							double OrdersFromPlannedOutlets = 0;
							ResultSet rs10 = s3.executeQuery("select count(id) from common_outlets where id in ("+PlannedOutletIDs+") and id in ("+OutletIDsOrdered+")");
							if (rs10.first()){
								OrdersFromPlannedOutlets = rs10.getDouble(1);
							}
							/*
							
							ResultSet rs3 = s3.executeQuery("select sum(((mop.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mop.is_promotion = 0 and mo.id in ("+OrderIDs+") ");
							if(rs3.first()){
								ConvertedCasesSold = rs3.getDouble(1);
							}
							*/
							double ConvertedCasesSoldCSD = 0;
							ResultSet rs12 = s3.executeQuery("select sum(((mop.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where ipv.type_id = 1 and mop.is_promotion = 0 and mo.id in ("+OrderIDs+") ");
							if(rs12.first()){
								ConvertedCasesSoldCSD = rs12.getDouble(1);
							}
							
							double ConvertedCasesSoldNCB = 0;
							ResultSet rs11 = s3.executeQuery("select sum(((mop.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where ipv.type_id = 2 and mop.is_promotion = 0 and mo.id in ("+OrderIDs+") ");
							if(rs11.first()){
								ConvertedCasesSoldNCB = rs11.getDouble(1);
							}
							double ConvertedCasesSold = ConvertedCasesSoldCSD + ConvertedCasesSoldNCB;
							
							double ConvertedCasesSoldSSRB250 = 0;
							ResultSet rs16 = s3.executeQuery("select sum(((mop.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where ipv.ssrb_type_id = 1 and mop.is_promotion = 0 and mo.id in ("+OrderIDs+") ");
							if(rs16.first()){
								ConvertedCasesSoldSSRB250 = rs16.getDouble(1);
							}
							
							double ConvertedCasesSoldSSRB240 = 0;
							ResultSet rs17 = s3.executeQuery("select sum(((mop.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where ipv.ssrb_type_id = 2 and mop.is_promotion = 0 and mo.id in ("+OrderIDs+") ");
							if(rs17.first()){
								ConvertedCasesSoldSSRB240 = rs17.getDouble(1);
							}
							double ConvertedCasesSoldSSRBTotal = ConvertedCasesSoldSSRB250 + ConvertedCasesSoldSSRB240;
							

							double ConvertedCasesTotal = 0;
							ResultSet rs61 = s3.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+") ");
							if(rs61.first()){
								ConvertedCasesTotal = rs61.getDouble(1);
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
							
							double AmountSales = 0;
							ResultSet rs91 = s3.executeQuery("select sum(net_amount) from inventory_sales_adjusted where order_id in ("+OrderIDs+")");
							if(rs91.first()){
								AmountSales = rs91.getDouble(1);
							}
							
							double ECO = 0;
							if (PlannedOutletsCount != 0){
								ECO = (OrdersFromPlannedOutlets / PlannedOutletsCount) * 100;
							}
							if (ECO > 100){
								ECO = 100;
							}
							
							double UnplannedOutletCount = UniqueOutletsOrdered - OrdersFromPlannedOutlets;
							
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

							double TotalLinesSoldCSD = 0;
							ResultSet rs13 = s3.executeQuery("select count(*) total_lines_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products ip on mop.product_id = ip.id where ip.type_id = 1 and mop.is_promotion = 0 and mo.id in ("+OrderIDs+")");
							if(rs13.first()){
								TotalLinesSoldCSD = rs13.getDouble("total_lines_sold");
							}
							
							double TotalLinesSoldNCB = 0;
							ResultSet rs14 = s3.executeQuery("select count(*) total_lines_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products ip on mop.product_id = ip.id where ip.type_id = 2 and mop.is_promotion = 0 and mo.id in ("+OrderIDs+")");
							if(rs14.first()){
								TotalLinesSoldNCB = rs14.getDouble("total_lines_sold");
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
							/*
							ResultSet rs15 = s3.executeQuery("SELECT id, et.employee_id, et.month, et.year, ( "+
									"select sum(((etp.quantity * ip.unit_per_case) * ip.liquid_in_ml) / ip.conversion_rate_in_ml) converted_cases from employee_targets_packages etp join inventory_packages ip on etp.package_id = ip.id where etp.id = et.id "+
								") converted_cases_target, ( "+
									"select sum((isap.total_units * ipv.liquid_in_ml / ipv.conversion_rate_in_ml)) converted_cases_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.booked_by = "+OrderbookerID+" and isap.is_promotion = 0 and isa.created_on between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" "+
								") converted_cases_sold FROM employee_targets et where et.employee_id = "+OrderbookerID+" and et.month = date_format(date("+Utilities.getSQLDate(StartDate)+"),'%m') and et.year = date_format(date("+Utilities.getSQLDate(StartDate)+"),'%Y')");
							
							if (rs15.first()){
								TargetCC = Math.round(rs15.getDouble("converted_cases_target"));
								TargetAchievedCC = Math.round(rs15.getDouble("converted_cases_sold"));
							}
							*/
							
							
							
							
							
							s2.executeUpdate("insert into temp_orderbooker_performance (name, avg_time_in, avg_time_out, outlets_in_pjp, outlets_ordered, converted_cases_sold, gross_revenue, eco, bill_productivity, sku_per_bill, pack_size_per_bill, drop_size, orderbookers_worked,unplanned_calls, converted_cases_csd, converted_cases_ncb, sku_per_order_csd, sku_per_order_ncb, ssrb_250, ssrb_240, ssrb_total, target_days_left, target_cc, target_achieved_cc, target_days_past, cr_assigned, is_ncsd, yesterday_planned_calls, yesterday_productive_calls, distributor_names, region_name, amount_sales, sales_converted_cases)"+
													"values('"+OrderbookerID+" - "+OrderbookerName+"','"+Utilities.getDisplayTimeFormat(AvgTimeIn)+"','"+Utilities.getDisplayTimeFormat(AvgTimeOut)+"',"+PlannedOutletsCount+","+OrdersFromPlannedOutlets+","+ConvertedCasesSold+","+GrossRevenue+","+ECO+","+BillProductivity+","+SKUPerBill+","+PackPerBill+","+DropSize+","+OrderBookersWorked+","+UnplannedOutletCount+","+ConvertedCasesSoldCSD+","+ConvertedCasesSoldNCB+","+SKUPerBillCSD+","+SKUPerBillNCB+","+ConvertedCasesSoldSSRB250+","+ConvertedCasesSoldSSRB240+","+ConvertedCasesSoldSSRBTotal+","+DaysLeft+","+TargetCC+","+TargetAchievedCC+","+DaysPast+", "+OrderBookersAssigned+","+isNCSD+","+YesterdayPlannedOutletsCount+",0,'"+DistributorNames+"','"+RSM.USER_ID+"-"+RSM.USER_DISPLAY_NAME+"',"+AmountSales+","+ConvertedCasesTotal+") ");
							
							
							
						
						}

						
						%>
						<%
						double TotalValueOutletsPJP=0;
						double TotalValueOutletsOrdered=0;
				        double TotalValueUnplannedCalls=0;
				        
				        double TotalValueConvertedCases=0;
				        double TotalValueConvertedCasesSold=0;
				        double TotalValueOrderAmount=0;
				        double TotalValueAmountSales=0;
				        
						
						int TotalOutletsPJP = 0;
						int TotalOutletsOrdered = 0;
						int TotalConvertedCases = 0;
						double TotalConvertedCasesCSD = 0;
						double TotalConvertedCasesNCB = 0;
						double TotalOrderAmount = 0;
						
						
						double TotalConvertedCasesSSRB250 = 0;
						double TotalConvertedCasesSSRB240 = 0;
						double TotalConvertedCasesSSRBTotal = 0;
						
						long TotalTargetCC = 0;
						long TotalTargetAchievedCC = 0;
						
						long TotalUnplannedCalls = 0;
						
						boolean isNCBRowPresented = false;
						
						double TotalYesterdayPlannedCalls = 0;
						double TotalYesterdayProductiveCalls = 0;
						
						
						int TotalGrandRowTotal=0;
						
						
						ResultSet rs1 = s3.executeQuery("select * from temp_orderbooker_performance order by distributor_names");
						while(rs1.next()){
							
							
							String DistributorNames = rs1.getString("distributor_names");
							
							int OutletsPJP = rs1.getInt("outlets_in_pjp");
							int OutletsOrdered = rs1.getInt("outlets_ordered");
							double OrderAmount = rs1.getDouble("gross_revenue"); 
							double AmountSales = rs1.getDouble("amount_sales");
							double UnplannedCalls = rs1.getDouble("unplanned_calls");

							TotalOutletsPJP += OutletsPJP;
							TotalOutletsOrdered += OutletsOrdered;
							TotalOrderAmount += OrderAmount;
							
							
							double YesterdayPlannedCalls = rs1.getDouble("yesterday_planned_calls");
							double YesterdayProductiveCalls = rs1.getDouble("yesterday_productive_calls");
							
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
							double ConvertedCasesSold = rs1.getDouble("sales_converted_cases");
							
							
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
												
						
							
					        String TimeIn =  rs1.getString("avg_time_in");
					        if (TimeIn == null || TimeIn.equals("null")){
					        	TimeIn = "";
					        }
					        String TimeOut =  rs1.getString("avg_time_out");
					        if (TimeOut == null || TimeOut.equals("null")){
					        	TimeOut = "";
					        }

						
					        
					        TotalValueOutletsPJP += OutletsPJP;
					        TotalValueOutletsOrdered += OutletsOrdered;
					        TotalValueUnplannedCalls += UnplannedCalls;
					        
					        TotalValueConvertedCases += ConvertedCases;
					        TotalValueConvertedCasesSold += ConvertedCasesSold;
					        TotalValueOrderAmount += OrderAmount;
					        TotalValueAmountSales += AmountSales;
					        
						%>
						
						<tr>
							<td><%=Utilities.truncateStringToMax(rs1.getString("name"),20) %></td>							
							<td style="text-align:center;background-color:#F7FFF7;"><%=TimeIn %></td>
							<td style="text-align:center;background-color:#F7FFF7;"><%=TimeOut %></td>
							<td style="text-align:center;background-color:#FFFFE6;"><%=Utilities.getDisplayCurrencyFormatRounded(OutletsPJP) %></td>
							<td style="text-align:center;background-color:#FFFFE6;"><%=Utilities.getDisplayCurrencyFormatRounded(OutletsOrdered) %></td>
							<td style="text-align:center;background-color:#FFFFE6;"><%=Utilities.getDisplayCurrencyFormatRounded(UnplannedCalls) %></td>							
							<td style="text-align:center;background-color:#FFFFE6;"><%=Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("eco"))%>%</td>							
							<td style="text-align:center;background-color:#FFFFE6;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(rs1.getDouble("drop_size")) %></td>							
							<td style="text-align:center;background-color:#FFFFE6;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(rs1.getDouble("sku_per_bill")) %></td>
							<td style="text-align:center;background-color:#FAFAF8;"><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCases)%></td>
							<td style="text-align:center;background-color:#FAFAF8;"><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesSold)%></td>
							<td style="text-align:right;background-color:#FAFAF8;"><%=Utilities.getDisplayCurrencyFormatRounded(OrderAmount) %></td>
							<td style="text-align:right;background-color:#FAFAF8;"><%=Utilities.getDisplayCurrencyFormatRounded(AmountSales) %></td>
							
						</tr>
						
						<%
						TotalGrandRowTotal++;
						}
						
						s.executeUpdate("drop temporary table temp_orderbooker_performance");
						
						%>
						<tr>
						<%
						for(int i=0;i<1;i++){
							%>
							
							<td>Total</td>							
							<td style="text-align:center;background-color:#F7FFF7;"></td>
							<td style="text-align:center;background-color:#F7FFF7;"></td>
							<td style="text-align:center;background-color:#FFFFE6;"><%=Utilities.getDisplayCurrencyFormatRounded(TotalValueOutletsPJP) %></td>
							<td style="text-align:center;background-color:#FFFFE6;"><%=Utilities.getDisplayCurrencyFormatRounded(TotalValueOutletsOrdered) %></td>
							<td style="text-align:center;background-color:#FFFFE6;"><%=Utilities.getDisplayCurrencyFormatRounded(TotalValueUnplannedCalls) %></td>							
							<td style="text-align:center;background-color:#FFFFE6;"></td>							
							<td style="text-align:center;background-color:#FFFFE6;"></td>							
							<td style="text-align:center;background-color:#FFFFE6;"></td>
							<td style="text-align:center;background-color:#FAFAF8;"><%=Utilities.getDisplayCurrencyFormatRounded(TotalValueConvertedCases)%></td>
							<td style="text-align:center;background-color:#FAFAF8;"><%=Utilities.getDisplayCurrencyFormatRounded(TotalValueConvertedCasesSold)%></td>
							<td style="text-align:right;background-color:#FAFAF8;"><%=Utilities.getDisplayCurrencyFormatRounded(TotalValueOrderAmount) %></td>
							<td style="text-align:right;background-color:#FAFAF8;"><%=Utilities.getDisplayCurrencyFormatRounded(TotalValueAmountSales) %></td>
							
						
							<%
						}
						
						%>
						</tr>
						
					</tbody>
							
				</table>
		</td>
	</tr>
</table>

	</li>	
</ul>

<%

//}else{
	%>
	<%
//}

s3.close();
s2.close();
s.close();
ds.dropConnection();
%>