<!DOCTYPE html> 
<html> 

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
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<head> 


<script src="js/lookups.js"></script>
<script src="js/ReportCenter.js?500011111111111199999999999111111=11999999999991111111"></script>

<script>

function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}

</script>

<style>
body {
	font: normal 11px auto "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
	color: #4f6b72;
	background: #E6EAE9;
}

a {
	color: #c75f3e;
}

#mytable {
	width: 700px;
	padding: 0;
	margin: 0;
}

caption {
	padding: 0 0 5px 0;
	width: 700px;	 
	font: italic 11px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
	text-align: right;
}

th {
	font: bold 11px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
	color: #4f6b72;
	border-right: 1px solid #C1DAD7;
	border-bottom: 1px solid #C1DAD7;
	border-top: 1px solid #C1DAD7;
	letter-spacing: 2px;
	text-transform: uppercase;
	text-align: left;
	padding: 6px 6px 6px 12px;
	background: #CAE8EA url(images/bg_header.jpg) no-repeat;
}

th.nobg {
	border-top: 0;
	border-left: 0;
	border-right: 1px solid #C1DAD7;
	background: none;
}

td {
	border-right: 1px solid #C1DAD7;
	border-bottom: 1px solid #C1DAD7;
	background: #fff;
	padding: 6px 6px 6px 12px;
	color: #4f6b72;
}


td.alt {
	background: #F5FAFA;
	color: #797268;
}

th.spec {
	border-left: 1px solid #C1DAD7;
	border-top: 0;
	background: #fff url(images/bullet1.gif) no-repeat;
	font: bold 10px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
}

th.specalt {
	border-left: 1px solid #C1DAD7;
	border-top: 0;
	background: #f5fafa url(images/bullet2.gif) no-repeat;
	font: bold 10px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
	color: #797268;
}





</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 290;

//System.out.println(UniqueSessionID+" - "+SessionUserID);

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

//System.out.print("StartDate = "+StartDate);
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
	WhereHOD = " and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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
	WhereDistributors = " and cd.distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
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
	WhereSM = " and cd.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
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
	WhereTDM = " and cd.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
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
	WhereASM = " and cd.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
}


//Sales Type


String AccountTypeIDs="";
long SelectedAccountTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedAccountType") != null){
	SelectedAccountTypeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedAccountType");
	AccountTypeIDs = Utilities.serializeForSQL(SelectedAccountTypeArray);
}

String WhereSalesType = "";
if (AccountTypeIDs.length() > 0){
	WhereSalesType = " and type_id in ("+AccountTypeIDs+")";	
}

long CustomerID = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedCustomerID") != null){
	CustomerID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedCustomerID");
}




//Transaction Account


String CashInstrumentsIDs="";
long SelectedCashInstrumentsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedCashInstruments") != null){
	SelectedCashInstrumentsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedCashInstruments");
	CashInstrumentsIDs = Utilities.serializeForSQL(SelectedCashInstrumentsArray);
}

String WhereCashInstruments = " and account_id=''";
if (CashInstrumentsIDs.length() > 0){
	WhereCashInstruments = " and  account_id in ("+CashInstrumentsIDs+") ";	
}

//


String WarehouseIDs="";
String WarehouseIDs1="";
           long SelectedWarehouseArray[] = null;
           long SelectedWarehouseArray1[] = null;
           if (session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse") != null){
           	SelectedWarehouseArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse");
           	
           	//check for scope warehouse
           	
           	UserAccess u = new UserAccess();
           	Warehouse[] WarehouseList = u.getUserFeatureWarehouse(SessionUserID,FeatureID);
           			           			
           	
            WarehouseIDs1 = u.getWarehousesQueryString(WarehouseList); 
           	WarehouseIDs = Utilities.serializeForSQL(SelectedWarehouseArray1);
           
           }else{
        	   //else getting scope warehouse
        	   UserAccess u = new UserAccess();
               Warehouse[] WarehouseList = u.getUserFeatureWarehouse(SessionUserID,FeatureID);
        	   WarehouseIDs = u.getWarehousesQueryString(WarehouseList);  
           }
           
           
           String WhereWarehouse = "";
           String WhereWarehouse1 = "";
           if (WarehouseIDs.length() > 0){
           	WhereWarehouse = " and glcr.warehouse_id in ("+WarehouseIDs+") ";	
           }
           if (WarehouseIDs1.length() > 0){
              	WhereWarehouse1 = " and glcr.warehouse_id in ("+WarehouseIDs1+") ";	
              }
           
           
         //Gl Employee


           String GlEmployeeIDs="";
           long SelectedGlEmployeeArray[] = null;
           if (session.getAttribute(UniqueSessionID+"_SR1SelectedGlEmployee") != null){
           	SelectedGlEmployeeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedGlEmployee");
           	GlEmployeeIDs = Utilities.serializeForSQL(SelectedGlEmployeeArray);
           }

           String WhereGlEmployee = "";
           if (GlEmployeeIDs.length() > 0){
           	WhereGlEmployee = " and glcr.created_by="+GlEmployeeIDs;	
           }        
           
         //customer filter

           long DistributorID1 = 0;
           if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
           	DistributorID1 = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
           }

          // System.out.println(DistributorID1);
           
           String WhereCustomerID ="";
           

           if (DistributorID1 != 0){
           	WhereCustomerID = " and plant_id in ("+DistributorID1+") ";
           	

           }

          // System.out.println("Hello - "+WhereCustomerID);        
          
%>
</head>
<body>


<br/>



<table style="width: 2830px; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 2500px" valign="top">
			
			<h2>PJP KPIs</h2>
			
			
			
			
			
			<%
if(DistributorID1!=0){
			long RegionID=0;
			String RegionName="";
			long SNDID=0;
			String SNDName="";
			String DistributorName="";
			
			
			ResultSet rs1 = s.executeQuery("SELECT name,region_id,(select region_short_name from common_regions cr where cr.region_id=cd.region_id) region_name,snd_id,(select display_name from users u where u.id=cd.snd_id) snd_name FROM common_distributors cd where distributor_id="+DistributorID1);
			while(rs1.next()){
				 RegionID= rs1.getLong("region_id");
				 RegionName=rs1.getString("region_name");
				 SNDID=rs1.getLong("snd_id");
				 SNDName=rs1.getString("snd_name");
				 DistributorName=rs1.getString("name");
			}
			%>
			
			
			<b>From:</b> <%=Utilities.getDisplayDateFormat(StartDate) %> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>To:</b> <%=Utilities.getDisplayDateFormat(EndDate) %>
			
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>SD:</b> <%=SNDID%> - <%=SNDName %>
			
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Region:</b> <%=RegionName %>
			
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Distributor:</b> <%=DistributorID1%> - <%=DistributorName %>
			
			
			<br/><br/>
			<table border="0" id="mytable" style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
			
			<tr style="font-size:11px;">
				<th colspan="7"></th>
				<th colspan="10" style="text-align:center;">ORDER KPI</th>
				<th colspan="14" style="text-align:center;">DELIVERY KPI</th>
			</tr>
			
			<tr style="font-size:11px;">
				
				<th style="width:80px;">PJP ID</th>
				<th style="width:160px;">PJP Name</th>
				<th style="width:400px;">Order Booker</th>
				<th style="width:50px;">Total Outlets</th>
				<th style="width:100px;"># of Avg. Daily Sched. Calls</th>
				<th style="width:50px;"># of Scheduled Calls</th>
				<th style="width:50px;"># of Actual Sched. Calls</th>
				<th style="width:50px;">Call Completion Rate %</th>
				<th style="width:50px;"># of Unplan. Calls/ Orders</th>
				<th style="width:50px;">Unplanned calls %</th>
				<th style="width:50px;"># of Plan. Orders</th>
				<th style="width:60px;">Total Orders</th>
				<th style="width:60px;">Strike Rate</th>
				<th style="width:60px;">Ordered Qty (r/c)</th>
				<th style="width:80px;">Drop Size</th>
				<th style="width:80px;">Total # of SKU</th>
				<th style="width:80px;">Avg. SKU / Order</th>
				<th style="width:80px;">Actual Sale (r/c)</th>
				<th style="width:80px;">Avg. Daily Target (r/c)</th>
				<th style="width:120px;">Avg. Daily Actual Sale (r/c)</th>
				<th style="width:120px;">BOM Avg. Daily Sale Req. (r/c)</th>
				<th style="width:80px;">Prior Day Ordered Qty (r/c)</th>
				<th style="width:80px;">Delivery Vs. Order %</th>
				<th style="width:80px;">Load Out (r/c)</th>
				<th style="width:80px;">Full In (r/c)</th>
				<th style="width:80px;">Full In %</th>
				<th style="width:80px;">Drop Size</th>
				<th style="width:80px;">Total # of SKU</th>
				<th style="width:80px;">Avg. SKU / Invoice</th>
				<th style="width:80px;"># of Zero Sale Outlets</th>
				<th style="width:80px;">Whole-sale % of total</th>
				
				
				
				
			</tr>
			
			
			<%
			long PJPID =0;
			String PJPName="";
			
			int GrandTotalOutlets=0;
			int GrandAvgDailySchCallTotal=0;
			double GrandTotalScheduledCalls=0;
			double GrandTotalOrdersFromPlannedOutlets=0;
			double GrandOrderProductivity=0;
			double GrandTotalUnplannedCalls=0;
			double GrandTotalOrders=0;
			double GrandTotalTotalQuantitySold=0;
			double GrandDropSize=0;
			double GrandTotalTotalLinesSold=0;
			double GrandSKUPerOrder=0;
			double GrandD_TotalTotalQuantitySold=0;
			double GrandSalesPerDay=0;
			double GrandD_TotalQuantityDispatched=0;
			double GrandD_Returns=0;
			double GrandD_DropSize=0;
			double GrandD_TotalTotalLinesSold=0;
			double GrandSKUPerInvoice=0;
			double GrandZeroSalesCount=0;
			
			s.executeUpdate("SET SESSION group_concat_max_len = 1000000");
			
			ResultSet rs2 = s.executeQuery("SELECT dbp.id, dbp.label, (select count(distinct outlet_id) from distributor_beat_plan_schedule where id = dbp.id) outlet_count, (select assigned_to from distributor_beat_plan_users where id = dbp.id) assigned_to, (select group_concat(distinct outlet_id) from distributor_beat_plan_schedule where id = dbp.id) total_outlet_ids FROM pep.distributor_beat_plan dbp where dbp.distributor_id = "+DistributorID1+" and dbp.label not like '%desk%' having outlet_count != 0");
			while(rs2.next()){
				
				PJPID = rs2.getLong("id");
				PJPName = rs2.getString("label");
				int TotalOutlets = rs2.getInt(3);
				
				GrandTotalOutlets += TotalOutlets;
				
				int AvgDailySchCall=0;
				double ScheduledCalls=0;//rs2.getInt("scheduled_calls");
				long OrderBookerID = rs2.getInt("assigned_to");
				String TotalOutletIDs = rs2.getString("total_outlet_ids");
				System.out.println(TotalOutletIDs);
				String OrderBookerName="";
				
				double ZeroSalesCount = 0;
				ResultSet rs50 = s2.executeQuery("select count(*) from (select outlet_id, to_days(curdate())-to_days(max(created_on)) days from inventory_sales_adjusted where outlet_id in ("+TotalOutletIDs+") group by outlet_id having days > 15) tab1");
				if (rs50.first()){
					ZeroSalesCount = rs50.getDouble(1);
				}
				
				
				GrandZeroSalesCount +=ZeroSalesCount;
					//select avg(ct) from (select day_number, count(outlet_id) ct from distributor_beat_plan_schedule where id = 55 group by day_number) tab1
				ResultSet rs3 = s2.executeQuery("select avg(ct) avg_daily_sch_call from (select day_number, count(outlet_id) ct from distributor_beat_plan_schedule where id = "+PJPID+" group by day_number) tab1");
				while(rs3.next()){
					 AvgDailySchCall = rs3.getInt("avg_daily_sch_call");
				}
				
				GrandAvgDailySchCallTotal += AvgDailySchCall;
				
				ResultSet rs4 = s2.executeQuery("SELECT display_name FROM users where id="+OrderBookerID);
				while(rs4.next()){
					OrderBookerName = rs4.getString("display_name");
				}
				
				
				double TotalOrdersFromPlannedOutlets = 0;
				double TotalUnplannedCalls = 0;
				double TotalTotalQuantitySold = 0;
				double TotalOrderCount = 0;
				double TotalTotalLinesSold = 0;
				double D_TotalTotalQuantitySold = 0;
				double D_TotalQuantityDispatched = 0;
				double D_TotalInvoiceCount = 0;
				double D_TotalTotalLinesSold = 0;
				Date CurrentDate = StartDate;
				int i = 0;
				
				double days = 0;
				while(true){
					days++;
					double PlannedOutletsCount = 0;
					String PlannedOutletIDs = "";

					ResultSet rs5 = s3.executeQuery("select count(distinct outlet_id), group_concat(distinct outlet_id) from distributor_beat_plan_log where log_date between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDate(CurrentDate)+" and distributor_id = "+DistributorID1+" and day_number = dayofweek(log_date) and id = "+PJPID);
					if (rs5.first()){
						PlannedOutletsCount = rs5.getDouble(1);
						PlannedOutletIDs = rs5.getString(2);
					}
					
					ScheduledCalls += PlannedOutletsCount;
					
					
					
					//
					
					
					String OrderIDs = "";
					String OutletIDsOrdered = "";
					int OrderCount = 0;
					int UniqueOutletsOrdered = 0;
					ResultSet rs45 = s3.executeQuery("select group_concat(mo.id) order_ids, count(mo.id) order_count, count(distinct mo.outlet_id) unique_outlets_ordered, group_concat(distinct mo.outlet_id) outlet_ids_ordered from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and mo.beat_plan_id ="+PJPID);
					if(rs45.first()){
						OrderIDs = rs45.getString("order_ids");
						OutletIDsOrdered = rs45.getString("outlet_ids_ordered");
						OrderCount = rs45.getInt("order_count");
						UniqueOutletsOrdered = rs45.getInt("unique_outlets_ordered");
					}
					TotalOrderCount += OrderCount;
					
					
					double OrdersFromPlannedOutlets = 0;
					ResultSet rs10 = s3.executeQuery("select count(id) from common_outlets where id in ("+PlannedOutletIDs+") and id in ("+OutletIDsOrdered+")");
					if (rs10.first()){
						OrdersFromPlannedOutlets = rs10.getDouble(1);
					}
					TotalOrdersFromPlannedOutlets += OrdersFromPlannedOutlets;
					
					

					
					double UnplannedOutletCount = UniqueOutletsOrdered - OrdersFromPlannedOutlets; 
					TotalUnplannedCalls += UnplannedOutletCount;
					
					
					
					
					double TotalQuantitySold = 0;
					ResultSet rs9 = s3.executeQuery("select sum(mop.raw_cases) total_qty_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mop.is_promotion = 0 and mo.id in ("+OrderIDs+")");
					if(rs9.first()){
						TotalQuantitySold = rs9.getDouble("total_qty_sold");
					}
					
					TotalTotalQuantitySold += TotalQuantitySold;
					
					
					
					
					double TotalLinesSold = 0;
					ResultSet rs12 = s3.executeQuery("select count(*) total_lines_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mop.is_promotion = 0 and mo.id in ("+OrderIDs+")");
					if(rs12.first()){
						TotalLinesSold = rs12.getDouble("total_lines_sold");
					}
					TotalTotalLinesSold += TotalLinesSold;
					
					
					
					
					/*
					DELIVERY KPIs
					*/
					
					double D_TotalQuantitySold = 0;
					ResultSet D_rs9 = s3.executeQuery("select sum(isap.raw_cases) total_qty_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+")");
					if(D_rs9.first()){
						D_TotalQuantitySold = D_rs9.getDouble("total_qty_sold");
					}
					D_TotalTotalQuantitySold += D_TotalQuantitySold;
					
					
					
					double D_QuantityDispatched = 0;
					ResultSet D_rs1 = s3.executeQuery("select sum(isip.raw_cases) from inventory_sales_invoices isi join inventory_sales_invoices_products isip on isi.id = isip.id where isi.order_id in ("+OrderIDs+") and isip.is_promotion = 0");
					if (D_rs1.first()){
						D_QuantityDispatched = D_rs1.getDouble(1);
					}
					D_TotalQuantityDispatched += D_QuantityDispatched;
					
					
					
					
					double InvoiceCount = 0;
					
					ResultSet D_rs4 = s3.executeQuery("select count(id) from inventory_sales_adjusted where order_id in ("+OrderIDs+") /* and outlet_id in ("+PlannedOutletIDs+") */ and net_amount != 0");
					if(D_rs4.first()){
						InvoiceCount = D_rs4.getDouble(1);
					}
					D_TotalInvoiceCount += InvoiceCount;
				
					
					double D_TotalLinesSold = 0;
					ResultSet D_rs5 = s3.executeQuery("select count(*) total_lines_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isap.is_promotion = 0 and isa.order_id in ("+OrderIDs+")");
					if(D_rs5.first()){
						D_TotalLinesSold = D_rs5.getDouble("total_lines_sold");
					}
					D_TotalTotalLinesSold += D_TotalLinesSold;
					
					
					
					if(DateUtils.isSameDay(CurrentDate, EndDate)){
						break;
					}

					CurrentDate = Utilities.getDateByDays(CurrentDate, 1);
					i++;
					
					
				
				}
				
				double OrderProductivity = 0;
				if (ScheduledCalls != 0){
					OrderProductivity = (TotalOrdersFromPlannedOutlets / ScheduledCalls) * 100;
				}
				
				
				
				
				
				double UnplannedCallsPercentage = 0;
				if (ScheduledCalls != 0){
					UnplannedCallsPercentage = (TotalUnplannedCalls / ScheduledCalls) * 100;
				}
				
				double TotalOrders = TotalOrdersFromPlannedOutlets + TotalUnplannedCalls;
				
				GrandTotalOrders +=TotalOrders;
				
				double DropSize = 0;
				if (TotalOrderCount != 0){
					 DropSize = TotalTotalQuantitySold / TotalOrderCount;
				}
				
				GrandDropSize +=DropSize;
				
				double D_DropSize = 0;
				if (D_TotalInvoiceCount != 0){
					D_DropSize = D_TotalTotalQuantitySold / D_TotalInvoiceCount;
				}
				
				GrandD_DropSize +=D_DropSize;

				if (PJPID == 55){
					System.out.println(TotalTotalQuantitySold+" "+D_TotalTotalQuantitySold);
					System.out.println(TotalOrderCount+" "+D_TotalInvoiceCount);
				}
				
				double SKUPerOrder = 0;
				if (TotalOrderCount != 0){
					SKUPerOrder = TotalTotalLinesSold / TotalOrderCount;
				}
				
				GrandSKUPerOrder +=SKUPerOrder; 
				
				double SKUPerInvoice = 0;
				if (D_TotalInvoiceCount != 0){
					SKUPerInvoice = D_TotalTotalLinesSold / D_TotalInvoiceCount;
				}
				
				GrandSKUPerInvoice +=SKUPerInvoice;
				
				double SalesPerDay = D_TotalTotalQuantitySold / days;
				
				
				GrandSalesPerDay += SalesPerDay;
				
				double DeliveryOrderPercentage = 0;
				if (TotalTotalQuantitySold != 0){
					DeliveryOrderPercentage = (D_TotalTotalQuantitySold / TotalTotalQuantitySold) * 100;
				}
				
				double D_Returns = D_TotalQuantityDispatched - D_TotalTotalQuantitySold;
				
				GrandD_Returns +=D_Returns;
				
				double D_ReturnsPercentage = 0;
				if (D_TotalQuantityDispatched != 0){
					D_ReturnsPercentage = (D_Returns / D_TotalQuantityDispatched) * 100;
				}
				
				GrandTotalScheduledCalls +=ScheduledCalls;
				GrandTotalOrdersFromPlannedOutlets += TotalOrdersFromPlannedOutlets;
				GrandTotalUnplannedCalls += TotalUnplannedCalls;
				GrandTotalTotalQuantitySold +=TotalTotalQuantitySold;
				GrandTotalTotalLinesSold +=TotalTotalLinesSold;
				GrandD_TotalTotalQuantitySold+=D_TotalTotalQuantitySold;
				GrandD_TotalQuantityDispatched +=D_TotalQuantityDispatched;
				
				GrandD_TotalTotalLinesSold+=D_TotalTotalLinesSold;
			%>
			
			
			
			<tr style="font-size:11px;">
				
				<td><%=PJPID %></td>
				<td ><%=Utilities.truncateStringToMax(PJPName, 22)  %></td>
				<td><%= OrderBookerID%> - <%=Utilities.truncateStringToMax(OrderBookerName, 20)%></td>
				<td style="text-align: center;"><%=TotalOutlets %></td>
				<td style="text-align: center;"><%=AvgDailySchCall %></td>
				<td style="text-align: center;"><%=Math.round(ScheduledCalls) %></td>
				<td style="text-align: center;"><%=Math.round(TotalOrdersFromPlannedOutlets) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(OrderProductivity) %>%</td>
				<td style="text-align: center;"><%=Math.round(TotalUnplannedCalls) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(UnplannedCallsPercentage) %>%</td>
				<td style="text-align: center;"><%=Math.round(TotalOrdersFromPlannedOutlets) %></td>
				<td style="text-align: center;"><%=Math.round(TotalOrders) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(OrderProductivity) %>%</td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatRounded(TotalTotalQuantitySold) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(DropSize) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatRounded(TotalTotalLinesSold) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(SKUPerOrder) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatRounded(D_TotalTotalQuantitySold) %></td>
				<td ></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatRounded(SalesPerDay) %></td>
				<td ></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatRounded(TotalTotalQuantitySold) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(DeliveryOrderPercentage) %>%</td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatRounded(D_TotalQuantityDispatched) %></td>
				
				<td style="text-align: center;"><% if (D_Returns > 0){out.print(Utilities.getDisplayCurrencyFormatRounded(D_Returns));} %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(D_ReturnsPercentage) %>%</td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(D_DropSize) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(D_TotalTotalLinesSold) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(SKUPerInvoice) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatRounded(ZeroSalesCount) %></td>
				
				<td ></td>
				
				
				
				
			</tr>	
			
			<%
			}
			
			GrandOrderProductivity = (GrandTotalOrdersFromPlannedOutlets / GrandTotalScheduledCalls) * 100;
			
			double GrandUnplannedCallsPercentage = 0;
			if (GrandTotalScheduledCalls != 0){
				GrandUnplannedCallsPercentage = (GrandTotalUnplannedCalls / GrandTotalScheduledCalls) * 100;
			}
			
			double GrandDeliveryOrderPercentage = 0;
			if (GrandTotalTotalQuantitySold != 0){
				GrandDeliveryOrderPercentage = (GrandD_TotalTotalQuantitySold / GrandTotalTotalQuantitySold) * 100;
			}
			
			double GrandD_ReturnsPercentage = 0;
			if (GrandD_TotalQuantityDispatched != 0){
				GrandD_ReturnsPercentage = (GrandD_Returns / GrandD_TotalQuantityDispatched) * 100;
			}
			
			%>
			
			
			<tr style="font-size:11px;">
				
				<td ></td>
				<td ></td>
				<td>Total</td>
				<td style="text-align: center;"><%=GrandTotalOutlets %></td>
				<td style="text-align: center;"><%=GrandAvgDailySchCallTotal %></td>
				<td style="text-align: center;"><%=Math.round(GrandTotalScheduledCalls) %></td>
				<td style="text-align: center;"><%=Math.round(GrandTotalOrdersFromPlannedOutlets) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(GrandOrderProductivity) %>%</td>
				<td style="text-align: center;"><%=Math.round(GrandTotalUnplannedCalls) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(GrandUnplannedCallsPercentage) %>%</td>
				<td style="text-align: center;"><%=Math.round(GrandTotalOrdersFromPlannedOutlets) %></td>
				<td style="text-align: center;"><%=Math.round(GrandTotalOrders) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(GrandOrderProductivity) %>%</td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalTotalQuantitySold) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(GrandDropSize) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalTotalLinesSold) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(GrandSKUPerOrder) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatRounded(GrandD_TotalTotalQuantitySold) %></td>
				<td ></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatRounded(GrandSalesPerDay) %></td>
				<td ></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalTotalQuantitySold) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(GrandDeliveryOrderPercentage) %>%</td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatRounded(GrandD_TotalQuantityDispatched) %></td>
				
				<td style="text-align: center;"><% if (GrandD_Returns > 0){out.print(Utilities.getDisplayCurrencyFormatRounded(GrandD_Returns));} %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(GrandD_ReturnsPercentage) %>%</td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(GrandD_DropSize) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(GrandD_TotalTotalLinesSold) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(GrandSKUPerInvoice) %></td>
				<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatRounded(GrandZeroSalesCount) %></td>
				<td style="text-align: center;"></td>
				
				
				
				
				
				
			</tr>	
							
			</table>
		</td>
	</tr>
</table>
<%
}else{
	out.println("<label style='font-size:15px;'>Please select distributor</label>");
}
%>
	
<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>

</body>
</html>