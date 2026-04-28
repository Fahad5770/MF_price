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
<%@page import="java.util.Calendar" %>


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
int FeatureID = 293;





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
	WhereDistributors = " and distributor_id in ("+DistributorIDs+") ";
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
           
           
          
%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
				<thead>
					   
					    <tr style="font-size:11px;">
						   	<th colspan="2" style="b1ackground-color:#A2A2A3; c1olor:white;">Sales in R/C</th>
						   	<th colspan="6"  style="text-align:center; background-color:#F7FFF7;">Day</th>
						   	<th colspan="6" style="text-align:center; background-color:#FFFFE6; ">MTD</th>
						   	<th colspan="6" style="text-align:center; background-color:#FAFAF8;">YTD</th>
						   	
					   </tr>
					   
					   <tr style="font-size:11px;">
						   	<th colspan="2" style="b1ackground-color:#A2A2A3; color:white; width:30%;"></th>
						   	<th colspan="3"  style="text-align:center; background-color:#F7FFF7;">Primary</th>
						   	<th colspan="3" style="text-align:center; background-color:#F7FFF7;">Secondary</th>
						   	<th colspan="3" style="text-align:center;  background-color:#FFFFE6;">Primary</th>
						   	<th colspan="3"  style="text-align:center;  background-color:#FFFFE6;">Secondary</th>
						   	<th colspan="3" style="text-align:center; background-color:#FAFAF8;">Primary</th>
						   	<th colspan="3" style="text-align:center; background-color:#FAFAF8;">Secondary</th>
					   </tr>
					   
					    <tr style="font-size:11px;">
														
							<th data-priority="1"  style="text-align:center; width:25%;">Distributor</th>
							<th data-priority="1"  style="text-align:center; width:5%;">City </th>							
							
							<th data-priority="1"  style="text-align:center; width:5%;background-color:#F7FFF7;">2016</th>
							<th data-priority="1"  style="text-align:center; width:5%;background-color:#F7FFF7;">2015</th>
							<th data-priority="1"  style="text-align:center; width:5%;background-color:#F7FFF7;">Var.</th>
							<th data-priority="1"  style="text-align:center; width:5%;background-color:#F7FFF7;">2016</th>
							<th data-priority="1"  style="text-align:center; width:5%;background-color:#F7FFF7;">2015</th>
							<th data-priority="1"  style="text-align:center; width:5%;background-color:#F7FFF7;">Var.</th>
							<th data-priority="1"  style="text-align:center; width:5%;background-color:#FFFFE6;">2016</th>
							<th data-priority="1"  style="text-align:center; width:5%;background-color:#FFFFE6;">2015</th>
							<th data-priority="1"  style="text-align:center; width:5%;background-color:#FFFFE6;">Var.</th>
							<th data-priority="1"  style="text-align:center; width:5%;background-color:#FFFFE6;">2016</th>
							<th data-priority="1"  style="text-align:center; width:5%;background-color:#FFFFE6;">2015</th>
							<th data-priority="1"  style="text-align:center; width:5%;background-color:#FFFFE6;">Var.</th>
							<th data-priority="1"  style="text-align:center; width:5%;background-color:#FAFAF8;">2016</th>
							<th data-priority="1"  style="text-align:center; width:5%;background-color:#FAFAF8;">2015</th>
							<th data-priority="1"  style="text-align:center; width:5%;background-color:#FAFAF8;">Var.</th>
							<th data-priority="1"  style="text-align:center; width:5%;background-color:#FAFAF8;">2016</th>
							<th data-priority="1"  style="text-align:center; width:5%;background-color:#FAFAF8;">2015</th>
							<th data-priority="1"  style="text-align:center; width:5%;background-color:#FAFAF8;">Var.</th>
							
														
					    </tr>
					  </thead> 
						<tbody>
							<%
							//Date d = new Date();
							
							Calendar cal = Calendar.getInstance();
							
							cal.setTime(EndDate);
							cal.add(Calendar.YEAR, -1); // to get previous year add -1
							Date PreviousYearStartDate = cal.getTime();
							
							
							cal.setTime(EndDate);
							cal.add(Calendar.YEAR, -1); // to get previous year add -1
							Date PreviousYearEndDate = cal.getTime();
							
							
							Date PrevStartDate = PreviousYearStartDate;
							Date PrevEndDate = PreviousYearEndDate;
							
						//	System.out.println(" Start Date "+Utilities.getDisplayDateFormat(StartDate)+" - "+Utilities.getDisplayDateFormat(EndDate));
							
							/* MTD ////////// */
							// //////////////////////////// //
							
							Date MTDStartDate = Utilities.getStartDateByDate(EndDate);
							Date MTDEndDate = EndDate;
							
							Calendar calMTD = Calendar.getInstance();
							
							calMTD.setTime(MTDStartDate);
							calMTD.add(Calendar.YEAR, -1); // to get previous year add -1
							Date PreviousYearMTDStartDate = calMTD.getTime();
							
							
							calMTD.setTime(MTDEndDate);
							calMTD.add(Calendar.YEAR, -1); // to get previous year add -1
							Date PreviousYearMTDEndDate = calMTD.getTime();
							
							
							
							
							//System.out.println(" MTD "+Utilities.getDisplayDateFormat(MTDStartDate)+" - "+Utilities.getDisplayDateFormat(MTDEndDate));
							
							// //////////////////////////// //
							// //////////////////////////// //
							
							
							Date YTDStartDate = Utilities.parseDate("01/01/"+Utilities.getYearByDate(EndDate));
							Date YTDEndDate = EndDate;
							
							Calendar calYTD = Calendar.getInstance();
							
							calYTD.setTime(YTDStartDate);
							calYTD.add(Calendar.YEAR, -1); // to get previous year add -1
							Date PreviousYearYTDStartDate = Utilities.parseDate("01/01/"+Utilities.getYearByDate(calYTD.getTime()));
							
							
							calYTD.setTime(YTDEndDate);
							calYTD.add(Calendar.YEAR, -1); // to get previous year add -1
							Date PreviousYearYTDEndDate = calYTD.getTime();
							
							
							
							//System.out.println(" YTD "+Utilities.getDisplayDateFormat(YTDStartDate)+" - "+Utilities.getDisplayDateFormat(YTDEndDate));
							
							
							double GrandTotalPrimaryRawCases2016ToDay=0;
							double GrandTotalPrimaryRawCases2015ToDay=0;
							double GrandTotalVarPrimaryToday=0;
								
							double GrandTotalPrimaryRawCases2016MTD=0;
							double GrandTotalPrimaryRawCases2015MTD=0;
							double GrandTotalVarPrimaryMTD=0;
								
								
							double GrandTotalPrimaryRawCases2016YTD=0;
							double GrandTotalPrimaryRawCases2015YTD=0;
							double GrandTotalVarPrimaryYTD=0;
								
							double GrandTotalSecondaryRawCases2016ToDay=0;
							double GrandTotalSecondaryRawCases2015ToDay=0;
							double GrandTotalVarSecondaryToday=0;
								
							double GrandTotalSecondaryRawCases2016MTD=0;
							double GrandTotalSecondaryRawCases2015MTD=0;
							double GrandTotalVarSecondaryMTD=0;
								
								
							double GrandTotalSecondaryRawCases2016YTD=0;
							double GrandTotalSecondaryRawCases2015YTD=0;
							double GrandTotalVarSecondaryYTD=0;
							
							
							//System.out.println("SELECT * FROM common_sd_groups where snd_id in (select distinct snd_id from common_distributors where distributor_id in (SELECT distinct distributor_id FROM inventory_sales_adjusted where created_on between "+Utilities.getSQLDate(YTDStartDate)+" and "+Utilities.getSQLDateNext(YTDEndDate)+"))");
							ResultSet rs = s.executeQuery("SELECT * FROM common_sd_groups where snd_id in (select distinct snd_id from common_distributors where distributor_id in (SELECT distinct distributor_id FROM inventory_sales_adjusted where created_on between "+Utilities.getSQLDate(YTDStartDate)+" and "+Utilities.getSQLDateNext(YTDEndDate)+"))");
							while(rs.next()){
							
								double SubTotalPrimaryRawCases2016ToDay=0;
								double SubTotalPrimaryRawCases2015ToDay=0;
								double SubTotalVarPrimaryToday=0;
								
								double SubTotalPrimaryRawCases2016MTD=0;
								double SubTotalPrimaryRawCases2015MTD=0;
								double SubTotalVarPrimaryMTD=0;
								
								
								double SubTotalPrimaryRawCases2016YTD=0;
								double SubTotalPrimaryRawCases2015YTD=0;
								double SubTotalVarPrimaryYTD=0;
								
								double SubTotalSecondaryRawCases2016ToDay=0;
								double SubTotalSecondaryRawCases2015ToDay=0;
								double SubTotalVarSecondaryToday=0;
								
								double SubTotalSecondaryRawCases2016MTD=0;
								double SubTotalSecondaryRawCases2015MTD=0;
								double SubTotalVarSecondaryMTD=0;
								
								
								double SubTotalSecondaryRawCases2016YTD=0;
								double SubTotalSecondaryRawCases2015YTD=0;
								double SubTotalVarSecondaryYTD=0;
								
								
							%>
							
							<tr>
								<th colspan="20" style="background-color:#FAFAF8; font-size:11px;"><%=rs.getString("short_name") %> - <%=rs.getString("long_name") %></th>
							</tr>
							<%
								ResultSet rs1 = s2.executeQuery("SELECT * FROM common_distributors where distributor_id in (SELECT distinct distributor_id FROM inventory_sales_adjusted where created_on between "+Utilities.getSQLDate(YTDStartDate)+" and "+Utilities.getSQLDateNext(YTDEndDate)+") and  snd_id="+rs.getLong("snd_id") +WhereHOD+WhereRSM+WhereDistributors+ " ");
								while(rs1.next()){
									
									long DistributorID=rs1.getLong("distributor_id");
									
									double PrimaryRawCases2016ToDay=0;
									double PrimaryRawCases2015ToDay=0;
									
									double PrimaryRawCases2016MTD=0;
									double PrimaryRawCases2015MTD=0;
									
									
									double PrimaryRawCases2016YTD=0;
									double PrimaryRawCases2015YTD=0;
									
									//Day - Primary - 2016
									//System.out.println("Day\nSELECT sum(idnp.total_units/ipv.unit_per_sku) total_raw_cases FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where created_on between "+Utilities.getSQLDate(EndDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and distributor_id="+DistributorID);
									ResultSet rs3 = s3.executeQuery("SELECT sum(idnp.total_units/ipv.unit_per_sku) total_raw_cases FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where created_on between "+Utilities.getSQLDate(EndDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and distributor_id="+DistributorID);
									if(rs3.first()){
										PrimaryRawCases2016ToDay = rs3.getLong("total_raw_cases");
									}
									
									//Day - Primary - 2015
									//System.out.println("DayLY\nSELECT sum(idnp.total_units/ipv.unit_per_sku) total_raw_cases FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where created_on between "+Utilities.getSQLDate(PrevEndDate)+" and "+Utilities.getSQLDateNext(PrevEndDate)+" and distributor_id="+DistributorID);
									ResultSet rs4 = s3.executeQuery("SELECT sum(idnp.total_units/ipv.unit_per_sku) total_raw_cases FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where created_on between "+Utilities.getSQLDate(PrevEndDate)+" and "+Utilities.getSQLDateNext(PrevEndDate)+" and distributor_id="+DistributorID);
									if(rs4.first()){
										PrimaryRawCases2015ToDay = rs4.getLong("total_raw_cases");
									}
									
//Varience 
									
									double VarPrimaryToday=0;
									if(PrimaryRawCases2015ToDay>0){
										VarPrimaryToday = PrimaryRawCases2016ToDay/(PrimaryRawCases2015ToDay-1);
									}
									
									//System.out.println(VarPrimaryToday);
									
									
									
									//MTD - Primary - 2016
									//System.out.println("MTD\nSELECT sum(idnp.total_units/ipv.unit_per_sku) total_raw_cases FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where created_on between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDateNext(MTDEndDate)+" and distributor_id="+DistributorID);
									ResultSet rs5 = s3.executeQuery("SELECT sum(idnp.total_units/ipv.unit_per_sku) total_raw_cases FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where created_on between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDateNext(MTDEndDate)+" and distributor_id="+DistributorID);
									if(rs5.first()){
										PrimaryRawCases2016MTD = rs5.getLong("total_raw_cases");
									}
									
									//MTD - Primary - 2015
									//System.out.println("MTDLY\nSELECT sum(idnp.total_units/ipv.unit_per_sku) total_raw_cases FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where created_on between "+Utilities.getSQLDate(PreviousYearMTDStartDate)+" and "+Utilities.getSQLDateNext(PreviousYearMTDEndDate)+" and distributor_id="+DistributorID);
									ResultSet rs6 = s3.executeQuery("SELECT sum(idnp.total_units/ipv.unit_per_sku) total_raw_cases FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where created_on between "+Utilities.getSQLDate(PreviousYearMTDStartDate)+" and "+Utilities.getSQLDateNext(PreviousYearMTDEndDate)+" and distributor_id="+DistributorID);
									if(rs6.first()){
										PrimaryRawCases2015MTD = rs6.getLong("total_raw_cases");
									}
									
									double VarPrimaryMTD=0;
									if(PrimaryRawCases2015MTD>0){
										VarPrimaryMTD = PrimaryRawCases2016MTD/(PrimaryRawCases2015MTD-1);
									}
									
									
									//YTD - Primary - 2016
									//System.out.println("YTD\nSELECT sum(idnp.total_units/ipv.unit_per_sku) total_raw_cases FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where created_on between "+Utilities.getSQLDate(YTDStartDate)+" and "+Utilities.getSQLDateNext(YTDEndDate)+" and distributor_id="+DistributorID);
									ResultSet rs7 = s3.executeQuery("SELECT sum(idnp.total_units/ipv.unit_per_sku) total_raw_cases FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where created_on between "+Utilities.getSQLDate(YTDStartDate)+" and "+Utilities.getSQLDateNext(YTDEndDate)+" and distributor_id="+DistributorID);
									if(rs7.first()){
										PrimaryRawCases2016YTD = rs7.getLong("total_raw_cases");
									}
									
									//YTD - Primary - 2015
									//System.out.println("YTD LY\nSELECT sum(idnp.total_units/ipv.unit_per_sku) total_raw_cases FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where created_on between "+Utilities.getSQLDate(PreviousYearYTDStartDate)+" and "+Utilities.getSQLDateNext(PreviousYearYTDEndDate)+" and distributor_id="+DistributorID);
									ResultSet rs8 = s3.executeQuery("SELECT sum(idnp.total_units/ipv.unit_per_sku) total_raw_cases FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where created_on between "+Utilities.getSQLDate(PreviousYearYTDStartDate)+" and "+Utilities.getSQLDateNext(PreviousYearYTDEndDate)+" and distributor_id="+DistributorID);
									if(rs8.first()){
										PrimaryRawCases2015YTD = rs8.getLong("total_raw_cases");
									}
									
									double VarPrimaryYTD=0;
									if(PrimaryRawCases2015YTD>0){
										VarPrimaryYTD = PrimaryRawCases2016YTD/(PrimaryRawCases2015YTD-1);
									}
									
									
									//Secondary ////////////////////////////////////////
									////////////////////////////////////////////////////
									///////////////////////////////////////////////////
									
									
									double SecondaryRawCases2016ToDay=0;
									double SecondaryRawCases2015ToDay=0;
									
									double SecondaryRawCases2016MTD=0;
									double SecondaryRawCases2015MTD=0;
									
									
									double SecondaryRawCases2016YTD=0;
									double SecondaryRawCases2015YTD=0;
									
									//Day - Secondary - 2016
									//"SELECT sum(isap.total_units/ipv.unit_per_sku) total_raw_cases FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between "+Utilities.getSQLDate(EndDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and distributor_id="+DistributorID
									ResultSet rs31 = s3.executeQuery("SELECT sum(isap.total_units/ip.unit_per_case) total_raw_cases FROM inventory_sales_adjusted_products isap join inventory_packages ip on isap.cache_package_id = ip.id where isap.cache_created_on_date between "+Utilities.getSQLDate(EndDate)+" and "+Utilities.getSQLDate(EndDate)+" and isap.cache_distributor_id="+DistributorID);
									if(rs31.first()){
										SecondaryRawCases2016ToDay = rs31.getLong("total_raw_cases");
									}
									
									//Day - Secondary - 2015
									//"SELECT sum(isap.total_units/ipv.unit_per_sku) total_raw_cases FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between "+Utilities.getSQLDate(PrevEndDate)+" and "+Utilities.getSQLDateNext(PrevEndDate)+" and distributor_id="+DistributorID
									ResultSet rs41 = s3.executeQuery("SELECT sum(isap.total_units/ip.unit_per_case) total_raw_cases FROM inventory_sales_adjusted_products isap join inventory_packages ip on isap.cache_package_id = ip.id where isap.cache_created_on_date between "+Utilities.getSQLDate(PrevEndDate)+" and "+Utilities.getSQLDate(PrevEndDate)+" and isap.cache_distributor_id="+DistributorID);
									if(rs41.first()){
										SecondaryRawCases2015ToDay = rs41.getLong("total_raw_cases");
									}
									
									//Varience 
									
									double VarSecondaryToday=0;
									if(SecondaryRawCases2015ToDay>0){
										VarSecondaryToday = SecondaryRawCases2016ToDay/(SecondaryRawCases2015ToDay-1);
									}
									
									//System.out.println(VarPrimaryToday);
									
									
									
									//MTD - Secondary - 2016
									//"SELECT sum(isap.total_units/ipv.unit_per_sku) total_raw_cases FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDateNext(MTDEndDate)+" and distributor_id="+DistributorID
									ResultSet rs51 = s3.executeQuery("SELECT sum(isap.total_units/ip.unit_per_case) total_raw_cases FROM inventory_sales_adjusted_products isap join inventory_packages ip on isap.cache_package_id = ip.id where isap.cache_created_on_date between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" and isap.cache_distributor_id="+DistributorID);
									if(rs51.first()){
										SecondaryRawCases2016MTD = rs51.getLong("total_raw_cases");
									}
									
									//MTD - Secondary - 2015
									//"SELECT sum(isap.total_units/ipv.unit_per_sku) total_raw_cases FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between "+Utilities.getSQLDate(PreviousYearMTDStartDate)+" and "+Utilities.getSQLDateNext(PreviousYearMTDEndDate)+" and distributor_id="+DistributorID
									ResultSet rs61 = s3.executeQuery("SELECT sum(isap.total_units/ip.unit_per_case) total_raw_cases FROM inventory_sales_adjusted_products isap join inventory_packages ip on isap.cache_package_id = ip.id where isap.cache_created_on_date between "+Utilities.getSQLDate(PreviousYearMTDStartDate)+" and "+Utilities.getSQLDate(PreviousYearMTDEndDate)+" and isap.cache_distributor_id="+DistributorID);
									if(rs61.first()){
										SecondaryRawCases2015MTD = rs61.getLong("total_raw_cases");
									}
									
									double VarSecondaryMTD=0;
									if(SecondaryRawCases2015MTD>0){
										VarSecondaryMTD = SecondaryRawCases2016MTD/(SecondaryRawCases2015MTD-1);
									}
									
									
									//YTD - Secondary - 2016
									//"SELECT sum(isap.total_units/ipv.unit_per_sku) total_raw_cases FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between "+Utilities.getSQLDate(YTDStartDate)+" and "+Utilities.getSQLDateNext(YTDEndDate)+" and distributor_id="+DistributorID
									ResultSet rs71 = s3.executeQuery("SELECT sum(isap.total_units/ip.unit_per_case) total_raw_cases FROM inventory_sales_adjusted_products isap join inventory_packages ip on isap.cache_package_id = ip.id where isap.cache_created_on_date between "+Utilities.getSQLDate(YTDStartDate)+" and "+Utilities.getSQLDate(YTDEndDate)+" and isap.cache_distributor_id="+DistributorID);
									if(rs71.first()){
										SecondaryRawCases2016YTD = rs71.getLong("total_raw_cases");
									}
									
									//YTD - Primary - 2015
									//"SELECT sum(isap.total_units/ipv.unit_per_sku) total_raw_cases FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between "+Utilities.getSQLDate(PreviousYearYTDStartDate)+" and "+Utilities.getSQLDateNext(PreviousYearYTDEndDate)+" and distributor_id="+DistributorID
									ResultSet rs81 = s3.executeQuery("SELECT sum(isap.total_units/ip.unit_per_case) total_raw_cases FROM inventory_sales_adjusted_products isap join inventory_packages ip on isap.cache_package_id = ip.id where isap.cache_created_on_date between "+Utilities.getSQLDate(PreviousYearYTDStartDate)+" and "+Utilities.getSQLDate(PreviousYearYTDEndDate)+" and isap.cache_distributor_id="+DistributorID);
									if(rs81.first()){
										SecondaryRawCases2015YTD = rs81.getLong("total_raw_cases");
									}
									
									double VarSecondaryYTD=0;
									if(SecondaryRawCases2015YTD>0){
										VarSecondaryYTD = SecondaryRawCases2016YTD/(SecondaryRawCases2015YTD-1);
									}
									
									//Sub Total
									
									 SubTotalPrimaryRawCases2016ToDay+=PrimaryRawCases2016ToDay;
									 SubTotalPrimaryRawCases2015ToDay+=PrimaryRawCases2015ToDay;
									 SubTotalVarPrimaryToday=SubTotalPrimaryRawCases2016ToDay/(SubTotalPrimaryRawCases2015ToDay-1);
									
									 SubTotalPrimaryRawCases2016MTD+=PrimaryRawCases2016MTD;
									 SubTotalPrimaryRawCases2015MTD+=PrimaryRawCases2015MTD;
									 SubTotalVarPrimaryMTD=SubTotalPrimaryRawCases2016MTD/(SubTotalPrimaryRawCases2015MTD-1);
									
									
									 SubTotalPrimaryRawCases2016YTD+=PrimaryRawCases2016YTD;
									 SubTotalPrimaryRawCases2015YTD+=PrimaryRawCases2015YTD;
									 SubTotalVarPrimaryYTD=SubTotalPrimaryRawCases2016YTD/(SubTotalPrimaryRawCases2015YTD-1);
									
									 SubTotalSecondaryRawCases2016ToDay+=SecondaryRawCases2016ToDay;
									 SubTotalSecondaryRawCases2015ToDay+=SecondaryRawCases2015ToDay;
									 SubTotalVarSecondaryToday=SubTotalSecondaryRawCases2016ToDay/(SubTotalSecondaryRawCases2015ToDay-1);
									
									 SubTotalSecondaryRawCases2016MTD+=SecondaryRawCases2016MTD;
									 SubTotalSecondaryRawCases2015MTD+=SecondaryRawCases2015MTD;
									 SubTotalVarSecondaryMTD=SubTotalSecondaryRawCases2016MTD/(SubTotalSecondaryRawCases2015MTD-1);
									
									
									 SubTotalSecondaryRawCases2016YTD+=SecondaryRawCases2016YTD;
									 SubTotalSecondaryRawCases2015YTD+=SecondaryRawCases2015YTD;
									 SubTotalVarSecondaryYTD=SubTotalSecondaryRawCases2016YTD/(SubTotalSecondaryRawCases2015YTD-1);;
									
									 
									 //Grand Total
									 
									 GrandTotalPrimaryRawCases2016ToDay+=PrimaryRawCases2016ToDay;
									 GrandTotalPrimaryRawCases2015ToDay+=PrimaryRawCases2015ToDay;
									 GrandTotalVarPrimaryToday=GrandTotalPrimaryRawCases2016ToDay/(GrandTotalPrimaryRawCases2015ToDay-1);
									
									 GrandTotalPrimaryRawCases2016MTD+=PrimaryRawCases2016MTD;
									 GrandTotalPrimaryRawCases2015MTD+=PrimaryRawCases2015MTD;
									 GrandTotalVarPrimaryMTD=GrandTotalPrimaryRawCases2016MTD/(GrandTotalPrimaryRawCases2015MTD-1);
									
									
									 GrandTotalPrimaryRawCases2016YTD+=PrimaryRawCases2016YTD;
									 GrandTotalPrimaryRawCases2015YTD+=PrimaryRawCases2015YTD;
									 GrandTotalVarPrimaryYTD=GrandTotalPrimaryRawCases2016YTD/(GrandTotalPrimaryRawCases2015YTD-1);
									
									 GrandTotalSecondaryRawCases2016ToDay+=SecondaryRawCases2016ToDay;
									 GrandTotalSecondaryRawCases2015ToDay+=SecondaryRawCases2015ToDay;
									 GrandTotalVarSecondaryToday=GrandTotalSecondaryRawCases2016ToDay/(GrandTotalSecondaryRawCases2015ToDay-1);
									
									 GrandTotalSecondaryRawCases2016MTD+=SecondaryRawCases2016MTD;
									 GrandTotalSecondaryRawCases2015MTD+=SecondaryRawCases2015MTD;
									 GrandTotalVarSecondaryMTD=GrandTotalSecondaryRawCases2016MTD/(GrandTotalSecondaryRawCases2015MTD-1);
									
									
									 GrandTotalSecondaryRawCases2016YTD+=SecondaryRawCases2016YTD;
									 GrandTotalSecondaryRawCases2015YTD+=SecondaryRawCases2015YTD;
									 GrandTotalVarSecondaryYTD=GrandTotalSecondaryRawCases2016YTD/(GrandTotalSecondaryRawCases2015YTD-1);;
									
									
									%>
									
									
									<tr>
										<td><%=rs1.getLong("distributor_id") %> - <%=Utilities.truncateStringToMax(rs1.getString("name"), 20) %></td>
										<td><%=Utilities.truncateStringToMax(rs1.getString("city"),15) %></td>
										
										<td style="background-color:#F7FFF7; text-align:right;"><%if(PrimaryRawCases2016ToDay>0){ out.println(Utilities.getDisplayCurrencyFormat(PrimaryRawCases2016ToDay));} %></td>
										<td style="background-color:#F7FFF7;text-align:right;"><%if(PrimaryRawCases2015ToDay>0){ out.println(Utilities.getDisplayCurrencyFormat(PrimaryRawCases2015ToDay));} %></td>
										<td style="background-color:#F7FFF7;text-align:center;"><%if(VarPrimaryToday>0){ out.println(Utilities.getDisplayCurrencyFormatOneDecimal(VarPrimaryToday)+"%");} %></td>
										
										<td style="background-color:#F7FFF7;text-align:right;"><%if(SecondaryRawCases2016ToDay>0){ out.println(Utilities.getDisplayCurrencyFormat(SecondaryRawCases2016ToDay));} %></td>
										<td style="background-color:#F7FFF7;text-align:right;"><%if(SecondaryRawCases2015ToDay>0){ out.println(Utilities.getDisplayCurrencyFormat(SecondaryRawCases2015ToDay));} %></td>
										<td style="background-color:#F7FFF7;text-align:center;"><%if(VarSecondaryToday>0){ out.println(Utilities.getDisplayCurrencyFormatOneDecimal(VarSecondaryToday)+"%");} %></td>
										
										<td style="background-color:#FFFFE6;text-align:right;"><%if(PrimaryRawCases2016MTD>0){ out.println(Utilities.getDisplayCurrencyFormat(PrimaryRawCases2016MTD));} %></td>
										<td style="background-color:#FFFFE6;text-align:right;"><%if(PrimaryRawCases2015MTD>0){ out.println(Utilities.getDisplayCurrencyFormat(PrimaryRawCases2015MTD));} %></td>
										<td style="background-color:#FFFFE6;text-align:center;"><%if(VarPrimaryMTD>0){ out.println(Utilities.getDisplayCurrencyFormatOneDecimal(VarPrimaryMTD)+"%");}%></td>
										
										<td style="background-color:#FFFFE6;text-align:right;"><%if(SecondaryRawCases2016MTD>0){ out.println(Utilities.getDisplayCurrencyFormat(SecondaryRawCases2016MTD));} %></td>
										<td style="background-color:#FFFFE6;text-align:right;"><%if(SecondaryRawCases2015MTD>0){ out.println(Utilities.getDisplayCurrencyFormat(SecondaryRawCases2015MTD));} %></td>
										<td style="background-color:#FFFFE6;text-align:center;"><%if(VarSecondaryMTD>0){ out.println(Utilities.getDisplayCurrencyFormatOneDecimal(VarSecondaryMTD)+"%");}%></td>
										
										
										<td style="background-color:#FAFAF8;text-align:right;"><%if(PrimaryRawCases2016YTD>0){ out.println(Utilities.getDisplayCurrencyFormat(PrimaryRawCases2016YTD));} %></td>
										<td style="background-color:#FAFAF8;text-align:right;"><%if(PrimaryRawCases2015YTD>0){ out.println(Utilities.getDisplayCurrencyFormat(PrimaryRawCases2015YTD));} %></td>
										<td style="background-color:#FAFAF8;text-align:center;"><%if(VarPrimaryYTD>0){ out.println(Utilities.getDisplayCurrencyFormatOneDecimal(VarPrimaryYTD)+"%");}%></td>
										
										<td style="background-color:#FAFAF8;text-align:right;"><%if(SecondaryRawCases2016YTD>0){ out.println(Utilities.getDisplayCurrencyFormat(SecondaryRawCases2016YTD));} %></td>
										<td style="background-color:#FAFAF8;text-align:right;"><%if(SecondaryRawCases2015YTD>0){ out.println(Utilities.getDisplayCurrencyFormat(SecondaryRawCases2015YTD));} %></td>
										<td style="background-color:#FAFAF8;text-align:center;"><%if(VarSecondaryYTD>0){ out.println(Utilities.getDisplayCurrencyFormatOneDecimal(VarSecondaryYTD)+"%");}%></td>
										
									</tr>
									
									<%
								}
							%>
							
							<tr>
								<td>Sub Total</td>
								<td></td>
								
								<td style="text-align:right;"><%if(SubTotalPrimaryRawCases2016ToDay>0){ out.println(Utilities.getDisplayCurrencyFormat(SubTotalPrimaryRawCases2016ToDay));} %></td>
								<td style="text-align:right;"><%if(SubTotalPrimaryRawCases2015ToDay>0){ out.println(Utilities.getDisplayCurrencyFormat(SubTotalPrimaryRawCases2015ToDay));} %></td>
								<td style="text-align:center;"><%if(SubTotalVarPrimaryToday>0){ out.println(Utilities.getDisplayCurrencyFormatOneDecimal(SubTotalVarPrimaryToday)+"%");} %></td>
										
								<td style="text-align:right;"><%if(SubTotalSecondaryRawCases2016ToDay>0){ out.println(Utilities.getDisplayCurrencyFormat(SubTotalSecondaryRawCases2016ToDay));} %></td>
								<td style="text-align:right;"><%if(SubTotalSecondaryRawCases2015ToDay>0){ out.println(Utilities.getDisplayCurrencyFormat(SubTotalSecondaryRawCases2015ToDay));} %></td>
								<td style="text-align:center;"><%if(SubTotalVarSecondaryToday>0){ out.println(Utilities.getDisplayCurrencyFormatOneDecimal(SubTotalVarSecondaryToday)+"%");} %></td>
										
								<td style="text-align:right;"><%if(SubTotalPrimaryRawCases2016MTD>0){ out.println(Utilities.getDisplayCurrencyFormat(SubTotalPrimaryRawCases2016MTD));} %></td>
								<td style="text-align:right;"><%if(SubTotalPrimaryRawCases2015MTD>0){ out.println(Utilities.getDisplayCurrencyFormat(SubTotalPrimaryRawCases2015MTD));} %></td>
								<td style="text-align:center;"><%if(SubTotalVarPrimaryMTD>0){ out.println(Utilities.getDisplayCurrencyFormatOneDecimal(SubTotalVarPrimaryMTD)+"%");}%></td>
										
								<td style="text-align:right;"><%if(SubTotalSecondaryRawCases2016MTD>0){ out.println(Utilities.getDisplayCurrencyFormat(SubTotalSecondaryRawCases2016MTD));} %></td>
								<td style="text-align:right;"><%if(SubTotalSecondaryRawCases2015MTD>0){ out.println(Utilities.getDisplayCurrencyFormat(SubTotalSecondaryRawCases2015MTD));} %></td>
								<td style="text-align:center;"><%if(SubTotalVarSecondaryMTD>0){ out.println(Utilities.getDisplayCurrencyFormatOneDecimal(SubTotalVarSecondaryMTD)+"%");}%></td>
										
										
								<td style="text-align:right;"><%if(SubTotalPrimaryRawCases2016YTD>0){ out.println(Utilities.getDisplayCurrencyFormat(SubTotalPrimaryRawCases2016YTD));} %></td>
								<td style="text-align:right;"><%if(SubTotalPrimaryRawCases2015YTD>0){ out.println(Utilities.getDisplayCurrencyFormat(SubTotalPrimaryRawCases2015YTD));} %></td>
								<td style="text-align:center;"><%if(SubTotalVarPrimaryYTD>0){ out.println(Utilities.getDisplayCurrencyFormatOneDecimal(SubTotalVarPrimaryYTD)+"%");}%></td>
										
								<td style="text-align:right;"><%if(SubTotalSecondaryRawCases2016YTD>0){ out.println(Utilities.getDisplayCurrencyFormat(SubTotalSecondaryRawCases2016YTD));} %></td>
								<td style="text-align:right;"><%if(SubTotalSecondaryRawCases2015YTD>0){ out.println(Utilities.getDisplayCurrencyFormat(SubTotalSecondaryRawCases2015YTD));} %></td>
								<td style="text-align:center;"><%if(SubTotalVarSecondaryYTD>0){ out.println(Utilities.getDisplayCurrencyFormatOneDecimal(SubTotalVarSecondaryYTD)+"%");}%></td>
										
								
							</tr>
							
							<%
							}
							
							%>
							
							<tr>
								<td>Grand Total</td>
								<td></td>
								
								<td style="text-align:right;"><%if(GrandTotalPrimaryRawCases2016ToDay>0){ out.println(Utilities.getDisplayCurrencyFormat(GrandTotalPrimaryRawCases2016ToDay));} %></td>
								<td style="text-align:right;"><%if(GrandTotalPrimaryRawCases2015ToDay>0){ out.println(Utilities.getDisplayCurrencyFormat(GrandTotalPrimaryRawCases2015ToDay));} %></td>
								<td style="text-align:center;"><%if(GrandTotalVarPrimaryToday>0){ out.println(Utilities.getDisplayCurrencyFormatOneDecimal(GrandTotalVarPrimaryToday)+"%");} %></td>
										
								<td style="text-align:right;"><%if(GrandTotalSecondaryRawCases2016ToDay>0){ out.println(Utilities.getDisplayCurrencyFormat(GrandTotalSecondaryRawCases2016ToDay));} %></td>
								<td style="text-align:right;"><%if(GrandTotalSecondaryRawCases2015ToDay>0){ out.println(Utilities.getDisplayCurrencyFormat(GrandTotalSecondaryRawCases2015ToDay));} %></td>
								<td style="text-align:center;"><%if(GrandTotalVarSecondaryToday>0){ out.println(Utilities.getDisplayCurrencyFormatOneDecimal(GrandTotalVarSecondaryToday)+"%");} %></td>
										
								<td style="text-align:right;"><%if(GrandTotalPrimaryRawCases2016MTD>0){ out.println(Utilities.getDisplayCurrencyFormat(GrandTotalPrimaryRawCases2016MTD));} %></td>
								<td style="text-align:right;"><%if(GrandTotalPrimaryRawCases2015MTD>0){ out.println(Utilities.getDisplayCurrencyFormat(GrandTotalPrimaryRawCases2015MTD));} %></td>
								<td style="text-align:center;"><%if(GrandTotalVarPrimaryMTD>0){ out.println(Utilities.getDisplayCurrencyFormatOneDecimal(GrandTotalVarPrimaryMTD)+"%");}%></td>
										
								<td style="text-align:right;"><%if(GrandTotalSecondaryRawCases2016MTD>0){ out.println(Utilities.getDisplayCurrencyFormat(GrandTotalSecondaryRawCases2016MTD));} %></td>
								<td style="text-align:right;"><%if(GrandTotalSecondaryRawCases2015MTD>0){ out.println(Utilities.getDisplayCurrencyFormat(GrandTotalSecondaryRawCases2015MTD));} %></td>
								<td style="text-align:center;"><%if(GrandTotalVarSecondaryMTD>0){ out.println(Utilities.getDisplayCurrencyFormatOneDecimal(GrandTotalVarSecondaryMTD)+"%");}%></td>
										
										
								<td style="text-align:right;"><%if(GrandTotalPrimaryRawCases2016YTD>0){ out.println(Utilities.getDisplayCurrencyFormat(GrandTotalPrimaryRawCases2016YTD));} %></td>
								<td style="text-align:right;"><%if(GrandTotalPrimaryRawCases2015YTD>0){ out.println(Utilities.getDisplayCurrencyFormat(GrandTotalPrimaryRawCases2015YTD));} %></td>
								<td style="text-align:center;"><%if(GrandTotalVarPrimaryYTD>0){ out.println(Utilities.getDisplayCurrencyFormatOneDecimal(GrandTotalVarPrimaryYTD)+"%");}%></td>
										
								<td style="text-align:right;"><%if(GrandTotalSecondaryRawCases2016YTD>0){ out.println(Utilities.getDisplayCurrencyFormat(GrandTotalSecondaryRawCases2016YTD));} %></td>
								<td style="text-align:right;"><%if(GrandTotalSecondaryRawCases2015YTD>0){ out.println(Utilities.getDisplayCurrencyFormat(GrandTotalSecondaryRawCases2015YTD));} %></td>
								<td style="text-align:center;"><%if(GrandTotalVarSecondaryYTD>0){ out.println(Utilities.getDisplayCurrencyFormatOneDecimal(GrandTotalVarSecondaryYTD)+"%");}%></td>
										
								
							</tr>
							
							
						</tbody>
							
				</table>
		</td>
	</tr>
</table>

	</li>	
</ul>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>