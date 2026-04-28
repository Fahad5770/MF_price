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
int FeatureID = 296;





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
	WhereDistributors = " and dbp.distributor_id in ("+DistributorIDs+") ";
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
							<th data-priority="1"  style="text-align:center; width:5%;">Sno.</th>
							<th data-priority="1"  style="text-align:center; width:9%;">Route Name</th>
							<th data-priority="1"  style="text-align:center; width:10%;">Pre-Seller Name</th>
							<th data-priority="1"  style="text-align:center; width:5%;"></th>
							<th data-priority="1"  style="text-align:center; width:6%;">Avg. Daily Target</th>
							<th data-priority="1"  style="text-align:center; width:6%;">Last Day Sale</th>
							<th data-priority="1"  style="text-align:center; width:6%;">Avg. Daily Balance Req.</th>
							<th data-priority="1"  style="text-align:center; width:6%;">MTD Sale</th>
							<th data-priority="1"  style="text-align:center; width:6%;">Monthly Target</th>
							<th data-priority="1"  style="text-align:center; width:5%;">MTD vs. Target %</th>
							<th data-priority="1"  style="text-align:center; width:5%;">CSD Packs</th>
							<th data-priority="1"  style="text-align:center; width:5%;">Avg. Daily Target</th>
							<th data-priority="1"  style="text-align:center; width:5%;">Last Day Sale</th>
							<th data-priority="1"  style="text-align:center; width:6%;">Avg. Daily Balance Req.</th>
							<th data-priority="1"  style="text-align:center; width:5%;">MTD Sale</th>
							<th data-priority="1"  style="text-align:center; width:5%;">Monthly Target</th>
							<th data-priority="1"  style="text-align:center; width:5%;">MTD vs. Target %</th>				
									
					    </tr>
					  </thead> 
						<tbody>
						
						
						
							<%
							
							
							double GrandTotalAvgDailyTargets=0;
							double GrandTotalAvgDailyTargetsSting=0;
							double GrandTotalAvgDailyTargetsSlice=0;
							double GrandTotalAvgDailyTargetsSSRB=0;
							double GrandTotalAvgDailyTargetsOtherSS=0;
							double GrandTotalAvgDailyTargets1501750=0;
							double GrandTotalAvgDailyTargets1000=0;
							double GrandTotalAvgDailyTargets2250=0;
							double GrandTotalAvgDailyTargetsTotal=0;
							
							double GrandTotalMTDTargets=0;
							double GrandTotalMTDTargetsSting=0;
							double GrandTotalMTDTargetsSlice=0;
							double GrandTotalMTDTargetsSSRB=0;
							double GrandTotalMTDTargetsOtherSS=0;
							double GrandTotalMTDTargets1501750=0;
							double GrandTotalMTDTargets1000=0;
							double GrandTotalMTDTargets2250=0;
							double GrandTotalMTDTargetsTotal=0;
							
							double GrandTotalYesterdaySalesCSD=0;
							double GrandTotalYesterdaySalesSting=0;
							double GrandTotalYesterdaySalesSlice=0;
							double GrandTotalYesterdaySalesSSRB=0;
							double GrandTotalYesterdaySalesOtherSS=0;
							double GrandTotalYesterdaySales1501750=0;
							double GrandTotalYesterdaySales1000=0;
							double GrandTotalYesterdaySales2250=0;
							double GrandTotalYesterdaySalesTotal=0;
							double GrandTotalYesterdaySalesAF=0;
							
							double GrandTotalMTDSalesCSD=0;
							double GrandTotalMTDSalesSting=0;
							double GrandTotalMTDSalesSlice=0;
							double GrandTotalMTDSalesSSRB=0;
							double GrandTotalMTDSalesOtherSS=0;
							double GrandTotalMTDSales15001750=0;
							double GrandTotalMTDSales1000=0;
							double GrandTotalMTDSales2250=0;
							double GrandTotalMTDSalesTotal=0;
							double GrandTotalMTDSalesAF=0;
							
							double GrandTotalMTDVsTargtetCSD=0;
							double GrandTotalMTDVsTargtetSting=0;
							double GrandTotalMTDVsTargtetSlice=0;
							double GrandTotalMTDVsTargtetSSRB=0;
							double GrandTotalMTDVsTargtetOtherSS=0;
							double GrandTotalMTDVsTargtet15001750=0;
							double GrandTotalMTDVsTargtet1000=0;
							double GrandTotalMTDVsTargtet2250=0;
							double GrandTotalMTDVsTargtetTotal=0;
							
							double GrandTotalBalanceReqCSD=0;
							double GrandTotalBalanceReqSting=0;
							double GrandTotalBalanceReqSlice=0;
							double GrandTotalBalanceReqSSRB=0;
							double GrandTotalBalanceReqOtherSS=0;
							double GrandTotalBalanceReq15001750=0;
							double GrandTotalBalanceReq1000=0;
							double GrandTotalBalanceReq2250=0;
							double GrandTotalBalanceReqTotal=0;
							
							
							
							
							int Srno=1;
							ResultSet rs = s.executeQuery("SELECT dbpu.assigned_to,u.display_name,dbpu.id, dbp.label pjp_name FROM distributor_beat_plan_users dbpu join users u on dbpu.assigned_to=u.id join distributor_beat_plan dbp on dbp.id=dbpu.id where 1=1  "+WhereDistributors+" order by dbpu.id ");
							while(rs.next()){
								
								Calendar c1 = Calendar.getInstance();
								c1.setTime(StartDate);
								int NumberOfDays=c1.getActualMaximum(Calendar.DAY_OF_MONTH);
								
								/* /////////Avg. Daily Target/////// */
								
								 double AvgDailyTargetCSD=0;
								double AvgDailyTargets=0;								
								double AvgDailyTargetsFinal=0;
								
								double AvgDailyTargetsSting=0;
								double AvgDailyTargetsStingFinal=0;
								
								double AvgDailyTargetsSlice=0;
								double AvgDailyTargetsSliceFinal=0;
								
								double AvgDailyTargetsSSRB=0;
								double AvgDailyTargetsSSRBFinal=0;
								
								double AvgDailyTargetsOtherSS=0;
								double AvgDailyTargetsOtherSSFinal=0;
								
								double AvgDailyTargets50175=0;
								double AvgDailyTargets50175Final=0;
								
								double AvgDailyTargets1000=0;
								double AvgDailyTargets1000Final=0;
								
								double AvgDailyTargets2250=0;
								double AvgDailyTargets2250Final=0;
								
								double MonthlyTargetTotal=0;
								
								
								//System.out.println("SELECT sum(etp.quantity) daily_target FROM pep.employee_targets et join employee_targets_packages etp on et.id=etp.id where et.employee_id="+rs.getLong("assigned_to") +" and et.month=month("+Utilities.getSQLDate(StartDate)+") and et.year=year("+Utilities.getSQLDate(StartDate)+")");
								
								
								ResultSet rs1 = s2.executeQuery("SELECT sum(etp.quantity) daily_target FROM pep.employee_targets et join employee_targets_packages etp on et.id=etp.id where et.employee_id="+rs.getLong("assigned_to") +" and et.month=month("+Utilities.getSQLDate(StartDate)+") and et.year=year("+Utilities.getSQLDate(StartDate)+")");
								while(rs1.next()){
									AvgDailyTargets = rs1.getDouble("daily_target");
								}
								
								//Sting
								
								ResultSet rs12 = s2.executeQuery("SELECT sum(etp.quantity) daily_target FROM pep.employee_targets et join employee_targets_packages etp on et.id=etp.id where et.employee_id="+rs.getLong("assigned_to") +" and et.month=month("+Utilities.getSQLDate(StartDate)+") and et.year=year("+Utilities.getSQLDate(StartDate)+") and etp.package_id=12");
								while(rs12.next()){
									AvgDailyTargetsSting = rs12.getDouble("daily_target");
								}
								
								//Slice
								
								ResultSet rs123 = s2.executeQuery("SELECT sum(etp.quantity) daily_target FROM pep.employee_targets et join employee_targets_packages etp on et.id=etp.id where et.employee_id="+rs.getLong("assigned_to") +" and et.month=month("+Utilities.getSQLDate(StartDate)+") and et.year=year("+Utilities.getSQLDate(StartDate)+") and etp.package_id=16");
								while(rs123.next()){
									AvgDailyTargetsSlice = rs123.getDouble("daily_target");
								}
								AvgDailyTargets= AvgDailyTargets - AvgDailyTargetsSting - AvgDailyTargetsSlice;
								
								//SSRB
								
								ResultSet rs1238 = s2.executeQuery("SELECT sum(etp.quantity) daily_target FROM pep.employee_targets et join employee_targets_packages etp on et.id=etp.id where et.employee_id="+rs.getLong("assigned_to") +" and et.month=month("+Utilities.getSQLDate(StartDate)+") and et.year=year("+Utilities.getSQLDate(StartDate)+") and etp.package_id in(11,12)");
								while(rs1238.next()){
									AvgDailyTargetsSSRB = rs1238.getDouble("daily_target");
								}
								
								//System.out.println("SELECT sum(etp.quantity) daily_target FROM pep.employee_targets et join employee_targets_packages etp on et.id=etp.id where et.employee_id="+rs.getLong("assigned_to") +" and et.month=month("+Utilities.getSQLDate(StartDate)+") and et.year=year("+Utilities.getSQLDate(StartDate)+") and etp.package_id in(11,12)");
								//System.out.println("Hello - "+AvgDailyTargetsSSRB);
								
								
								//OtherSS
								
								ResultSet rs1239 = s2.executeQuery("SELECT sum(etp.quantity) daily_target FROM pep.employee_targets et join employee_targets_packages etp on et.id=etp.id where et.employee_id="+rs.getLong("assigned_to") +" and et.month=month("+Utilities.getSQLDate(StartDate)+") and et.year=year("+Utilities.getSQLDate(StartDate)+") and etp.package_id in(28,27,6,9)");
								while(rs1239.next()){
									AvgDailyTargetsOtherSS = rs1239.getDouble("daily_target");
								}
								
								
									//1.5/1.75
								
								ResultSet rs1234 = s2.executeQuery("SELECT sum(etp.quantity) daily_target FROM pep.employee_targets et join employee_targets_packages etp on et.id=etp.id where et.employee_id="+rs.getLong("assigned_to") +" and et.month=month("+Utilities.getSQLDate(StartDate)+") and et.year=year("+Utilities.getSQLDate(StartDate)+") and etp.package_id in (2,24)");
								while(rs1234.next()){
									AvgDailyTargets50175 = rs1234.getDouble("daily_target");
								}
								
								//1000
								
								ResultSet rs1235 = s2.executeQuery("SELECT sum(etp.quantity) daily_target FROM pep.employee_targets et join employee_targets_packages etp on et.id=etp.id where et.employee_id="+rs.getLong("assigned_to") +" and et.month=month("+Utilities.getSQLDate(StartDate)+") and et.year=year("+Utilities.getSQLDate(StartDate)+") and etp.package_id in (3)");
								while(rs1235.next()){
									AvgDailyTargets1000 = rs1235.getDouble("daily_target");
								}
								
								//2250
								
								ResultSet rs1236 = s2.executeQuery("SELECT sum(etp.quantity) daily_target FROM pep.employee_targets et join employee_targets_packages etp on et.id=etp.id where et.employee_id="+rs.getLong("assigned_to") +" and et.month=month("+Utilities.getSQLDate(StartDate)+") and et.year=year("+Utilities.getSQLDate(StartDate)+") and etp.package_id in (5)");
								while(rs1236.next()){
									AvgDailyTargets2250 = rs1236.getDouble("daily_target");
								}
								
								
								//System.out.println("Hello "+c1.getActualMaximum(Calendar.DAY_OF_MONTH) );
																
								
								AvgDailyTargetCSD = (AvgDailyTargets)/(NumberOfDays-Utilities.getFridayCountByDate(StartDate));
								
								AvgDailyTargetsFinal = (AvgDailyTargets+AvgDailyTargetsSting+AvgDailyTargetsSlice) /(NumberOfDays-Utilities.getFridayCountByDate(StartDate));
								AvgDailyTargetsStingFinal = AvgDailyTargetsSting/(NumberOfDays-Utilities.getFridayCountByDate(StartDate));
								AvgDailyTargetsSliceFinal = AvgDailyTargetsSlice/(NumberOfDays-Utilities.getFridayCountByDate(StartDate));
								AvgDailyTargetsSSRBFinal = AvgDailyTargetsSSRB/(NumberOfDays-Utilities.getFridayCountByDate(StartDate));
								AvgDailyTargetsOtherSSFinal = AvgDailyTargetsOtherSS/(NumberOfDays-Utilities.getFridayCountByDate(StartDate));
								AvgDailyTargets50175Final = AvgDailyTargets50175/(NumberOfDays-Utilities.getFridayCountByDate(StartDate));
								AvgDailyTargets1000Final = AvgDailyTargets1000/(NumberOfDays-Utilities.getFridayCountByDate(StartDate));
								AvgDailyTargets2250Final = AvgDailyTargets2250/(NumberOfDays-Utilities.getFridayCountByDate(StartDate));
								
								
								MonthlyTargetTotal =AvgDailyTargets+AvgDailyTargetsSting+AvgDailyTargetsSlice;
								
								//System.out.println(NumberOfDays+" - "+Utilities.numberOfFridays(StartDate)+" - "+AvgDailyTargets+" - "+AvgDailyTargetsFinal);
								
								
								
								
								//////////////////////////////
								
								
								
								////////Last Day Sale///////////
								///////////////////////////////
								
								double YesterdaySalesCSD=0;
								double YesterdaySalesSting=0;
								double YesterdaySalesAF=0;
								double YesterdaySalesSlice=0;
								double YesterdaySalesSSRB=0;
								double YesterdaySalesOtherSS=0;
								double YesterdaySales150175=0;
								double YesterdaySales1000=0;
								double YesterdaySales2250=0;
								
								double TotalYesterdaySales=0;
								
								
								
								
								
								
								//For CSD
								ResultSet rs2 = s2.executeQuery("SELECT sum(total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products  where cache_lrb_type_id=1  and cache_order_created_on_date between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and cache_booked_by="+rs.getLong("assigned_to"));
								//ResultSet rs2 = s2.executeQuery("SELECT sum(isap.total_units/ip.unit_per_sku) sale FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products ip on isap.product_id=ip.id where ip.lrb_type_id=1  and created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(StartDate,-1))+" and isa.booked_by="+rs.getLong("assigned_to"));
								while(rs2.next()){
									YesterdaySalesCSD = rs2.getDouble("sale");
								}
								
								//For Sting - Energy Drink
								ResultSet rs21 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=3  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and cache_booked_by="+rs.getLong("assigned_to"));
								//ResultSet rs21 = s2.executeQuery("SELECT sum(isap.total_units/ip.unit_per_sku) sale FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products ip on isap.product_id=ip.id where ip.lrb_type_id=3  and created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(StartDate,-1))+" and isa.booked_by="+rs.getLong("assigned_to"));
								while(rs21.next()){
									YesterdaySalesSting = rs21.getDouble("sale");
								}
								
								//For AF - Water
								
								ResultSet rs212 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=2  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and cache_booked_by="+rs.getLong("assigned_to"));
								//ResultSet rs212 = s2.executeQuery("SELECT sum(isap.total_units/ip.unit_per_sku) sale FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products ip on isap.product_id=ip.id where ip.lrb_type_id=2  and created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(StartDate,-1))+" and isa.booked_by="+rs.getLong("assigned_to"));
								while(rs212.next()){
									YesterdaySalesAF = rs212.getDouble("sale");
								}
								
								//For Slice - Juice
								ResultSet rs2123 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=4  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and cache_booked_by="+rs.getLong("assigned_to"));
								//ResultSet rs2123 = s2.executeQuery("SELECT sum(isap.total_units/ip.unit_per_sku) sale FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products ip on isap.product_id=ip.id where ip.lrb_type_id=4  and created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(StartDate,-1))+" and isa.booked_by="+rs.getLong("assigned_to"));
								while(rs2123.next()){
									YesterdaySalesSlice = rs2123.getDouble("sale");
								}
								
								//For SSRB
								ResultSet rs2128 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and cache_package_id in(11,12) and cache_booked_by="+rs.getLong("assigned_to"));
								//ResultSet rs2128 = s2.executeQuery("SELECT sum(isap.total_units/ip.unit_per_sku) sale FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products ip on isap.product_id=ip.id where ip.lrb_type_id=1  and created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(StartDate,-1))+" and isa.booked_by="+rs.getLong("assigned_to")+" and ip.package_id in(11,12)");
								while(rs2128.next()){
									YesterdaySalesSSRB = rs2128.getDouble("sale");
								}
								
								//For OtherSS
								ResultSet rs2129 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and cache_package_id in(28,27,6,9) and cache_booked_by="+rs.getLong("assigned_to"));
								//ResultSet rs2129 = s2.executeQuery("SELECT sum(isap.total_units/ip.unit_per_sku) sale FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products ip on isap.product_id=ip.id where ip.lrb_type_id=1  and created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(StartDate,-1))+" and isa.booked_by="+rs.getLong("assigned_to")+" and ip.package_id in(28,27,6,9)");
								while(rs2129.next()){
									YesterdaySalesOtherSS = rs2129.getDouble("sale");
								}
								
								//1.5/1.75
								ResultSet rs2124 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and cache_package_id in(2,24) and cache_booked_by="+rs.getLong("assigned_to"));
								//ResultSet rs2124 = s2.executeQuery("SELECT sum(isap.total_units/ip.unit_per_sku) sale FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products ip on isap.product_id=ip.id where ip.lrb_type_id=1  and created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(StartDate,-1))+" and isa.booked_by="+rs.getLong("assigned_to")+" and ip.package_id in(2,24)");
								while(rs2124.next()){
									YesterdaySales150175 = rs2124.getDouble("sale");
								}
								
								
								//1000
								ResultSet rs2125 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and cache_package_id in(3) and cache_booked_by="+rs.getLong("assigned_to"));
								//ResultSet rs2125 = s2.executeQuery("SELECT sum(isap.total_units/ip.unit_per_sku) sale FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products ip on isap.product_id=ip.id where ip.lrb_type_id=1  and created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(StartDate,-1))+" and isa.booked_by="+rs.getLong("assigned_to")+" and ip.package_id in(3)");
								while(rs2125.next()){
									YesterdaySales1000 = rs2125.getDouble("sale");
								}
								
								//2250
								ResultSet rs2126 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and cache_package_id in(5)  and cache_booked_by="+rs.getLong("assigned_to"));
								//ResultSet rs2126 = s2.executeQuery("SELECT sum(isap.total_units/ip.unit_per_sku) sale FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products ip on isap.product_id=ip.id where ip.lrb_type_id=1  and created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(StartDate,-1))+" and isa.booked_by="+rs.getLong("assigned_to")+" and ip.package_id in(5)");
								while(rs2126.next()){
									YesterdaySales2250 = rs2126.getDouble("sale");
								}
								
								TotalYesterdaySales = YesterdaySalesCSD+YesterdaySalesSting+YesterdaySalesAF+YesterdaySalesSlice;
								
								////////////////////////////////
								
								
								
								////////MTD Sale///////////
								///////////////////////////////
								
								double MTDSalesCSD=0;
								double MTDSalesSting=0;
								double MTDSalesAF=0;
								double MTDSalesSlice=0;
								double MTDSalesSSRB=0;
								double MTDSalesOtherSS=0;
								double MTDSales150175=0;
								double MTDSales1000=0;
								double MTDSales2250=0;
								
								double TotalMTDSales=0;
								
								Date MTDStartDate = Utilities.getStartDateByDate(StartDate);
								Date MTDEndDate = StartDate;  //MTD End Date will be equal to entered date of report
								
								//System.out.println(Utilities.getDisplayDateFormat(MTDStartDate)+" - "+Utilities.getDisplayDateFormat(MTDEndDate));
								
								
								//For CSD
								ResultSet MTDrs2 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" and cache_booked_by="+rs.getLong("assigned_to"));
								while(MTDrs2.next()){
									MTDSalesCSD = MTDrs2.getDouble("sale");
								}
								
								//For Sting - Energy Drink
								ResultSet MTDrs21 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=3  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" and cache_booked_by="+rs.getLong("assigned_to"));
								while(MTDrs21.next()){
									MTDSalesSting = MTDrs21.getDouble("sale");
								}
								
								//For AF - Water
								ResultSet MTDrs212 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=2  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" and cache_booked_by="+rs.getLong("assigned_to"));
								while(MTDrs212.next()){
									MTDSalesAF = MTDrs212.getDouble("sale");
								}
								
								//For Slice - Juice
								ResultSet MTDrs2123 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=4  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" and cache_booked_by="+rs.getLong("assigned_to"));
								while(MTDrs2123.next()){
									MTDSalesSlice = MTDrs2123.getDouble("sale");
								}
								
								//For SSRB
								ResultSet MTDrs2128 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" and cache_package_id in(11,12) and cache_booked_by="+rs.getLong("assigned_to"));
								while(MTDrs2128.next()){
									MTDSalesSSRB = MTDrs2128.getDouble("sale");
								}
								
								//For OtherSS
								ResultSet MTDrs2129 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" and cache_package_id in(28,27,6,9) and cache_booked_by="+rs.getLong("assigned_to"));
								while(MTDrs2129.next()){
									MTDSalesOtherSS = MTDrs2129.getDouble("sale");
								}
								
								//1.5/1.75
								ResultSet MTDrs2124 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" and cache_package_id in(2,24)  and cache_booked_by="+rs.getLong("assigned_to"));
								while(MTDrs2124.next()){
									MTDSales150175 = MTDrs2124.getDouble("sale");
								}
								
								
								//1000
								ResultSet MTDrs2125 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" and cache_package_id in(3) and cache_booked_by="+rs.getLong("assigned_to"));
								while(MTDrs2125.next()){
									MTDSales1000 = MTDrs2125.getDouble("sale");
								}
								
								//2250
								ResultSet MTDrs2126 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" and cache_package_id in(5)  and cache_booked_by="+rs.getLong("assigned_to"));
								while(MTDrs2126.next()){
									MTDSales2250 = MTDrs2126.getDouble("sale");
								}
								
								TotalMTDSales = MTDSalesCSD+MTDSalesSting+MTDSalesAF+MTDSalesSlice;
								
								////////////////////////////////
								
								 //AvgDailyTargetCSD = AvgDailyTargetsFinal-AvgDailyTargetsStingFinal-AvgDailyTargetsSliceFinal;
							
							
								//////////////////////////////////////////
								////////////MTD vs. Target %/////////////
								
								double MTDVsTargtetCSD=0;
								double MTDVsTargtetSting=0;
								//double MTDVsTargtetAF=(MTDSalesCSD/AvgDailyTargetsAF)*100;
								double MTDVsTargtetSlice=0;
								double MTDVsTargtetSSRB=0;
								double MTDVsTargtetOtherSS=0;
								double MTDVsTargtet1501725=0;
								double MTDVsTargtet1000=0;
								double MTDVsTargtet2250=0;
								
								double MTDVsTargtetTotal = 0;
								
								
								if(AvgDailyTargets!=0){
									 MTDVsTargtetCSD=(MTDSalesCSD/AvgDailyTargets)*100;	
								}
								
								if(AvgDailyTargetsSting!=0){
									MTDVsTargtetSting=(MTDSalesSting/AvgDailyTargetsSting)*100;	
								}
								
								if(AvgDailyTargetsSlice!=0){
									MTDVsTargtetSlice=(MTDSalesSlice/AvgDailyTargetsSlice)*100;	
								}
								
								if(AvgDailyTargetsSSRB!=0){
									MTDVsTargtetSSRB=(MTDSalesSSRB/AvgDailyTargetsSSRB)*100;	
								}
								 
								if(AvgDailyTargetsOtherSS!=0){
									MTDVsTargtetOtherSS=(MTDSalesOtherSS/AvgDailyTargetsOtherSS)*100;	
								}
								 
								if(AvgDailyTargets50175!=0){
									 MTDVsTargtet1501725=(MTDSales150175/AvgDailyTargets50175)*100;
								}
								
								if(AvgDailyTargets1000!=0){
									MTDVsTargtet1000=(MTDSales1000/AvgDailyTargets1000)*100;	
								}
								
								if(AvgDailyTargets2250!=0){
									MTDVsTargtet2250=(MTDSales2250/AvgDailyTargets2250)*100;	
								}
								 
								if(MonthlyTargetTotal!=0){
									MTDVsTargtetTotal = (TotalMTDSales/MonthlyTargetTotal)*100;	
								}
								
								 
								
								/////////////////////////////////////////
							
								
								/////////////////////////////////////////
								///////////Avg. Daily Balance Req.///////
								
								//MTD Target-MTDSale/Remaining days of Month
								
								int StartDateDays = Utilities.getDayNumberByDate(StartDate);
								int EndDateDays = Utilities.getDayNumberByDate(Utilities.getEndDateByDate(StartDate));
								int RemaingDaysOfMonth=EndDateDays-StartDateDays;
								
								
								double BalanceReqCSD=0;
								double BalanceReqSting=0;
								double BalanceReqSlice=0;
								double BalanceReqSSRB=0;
								double BalanceReqOtherSS=0;
								double BalanceReq1501725=0;
								double BalanceReq1000=0;
								double BalanceReq2250=0;
								double BalanceReqTotal=0;
								
								if(RemaingDaysOfMonth!=0){
									if(AvgDailyTargets!=0){
										BalanceReqCSD =  (AvgDailyTargets-MTDSalesCSD)/RemaingDaysOfMonth;
									}
									
									if(AvgDailyTargetsSting!=0){
										BalanceReqSting = (AvgDailyTargetsSting-MTDSalesSting)/RemaingDaysOfMonth;
									}
									
									if(AvgDailyTargetsSSRB!=0){
										BalanceReqSSRB = (AvgDailyTargetsSSRB-MTDSalesSSRB)/RemaingDaysOfMonth;	
									}
									
									if(AvgDailyTargetsOtherSS!=0){
										BalanceReqOtherSS = (AvgDailyTargetsOtherSS-MTDSalesOtherSS)/RemaingDaysOfMonth;
									}
									
									if(AvgDailyTargets50175!=0){
										BalanceReq1501725 = (AvgDailyTargets50175-MTDSales150175)/RemaingDaysOfMonth;
									}
									
									if(AvgDailyTargets1000!=0){
										BalanceReq1000 = (AvgDailyTargets1000-MTDSales1000)/RemaingDaysOfMonth;
									}
									
									if(AvgDailyTargets2250!=0){
										BalanceReq2250 = (AvgDailyTargets2250-MTDSales2250)/RemaingDaysOfMonth;	
									}
									
									if(MonthlyTargetTotal!=0){
										BalanceReqTotal = (MonthlyTargetTotal-TotalMTDSales)/RemaingDaysOfMonth;
									}
									
									
								}
								
									
								
								
								
								//System.out.println(StartDateDays+" - "+EndDateDays);
								
								
								
								/////////////////////////////////////////
							
							%>
							
							
							 <tr style="font-size:11px;">
								<td data-priority="1"  style="text-align:left; " rowspan="5"><%=Srno %></td>
								<td data-priority="1"  style="text-align:left; " rowspan="5"><%=rs.getString("pjp_name") %></td>
								<td data-priority="1"  style="text-align:left; " rowspan="5"><%=rs.getString("display_name") %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;">CSD</td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(AvgDailyTargetCSD!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(AvgDailyTargetCSD) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(YesterdaySalesCSD!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(YesterdaySalesCSD) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(BalanceReqCSD!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(BalanceReqCSD) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(MTDSalesCSD!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(MTDSalesCSD) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(AvgDailyTargets!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(AvgDailyTargets) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(MTDVsTargtetCSD!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(MTDVsTargtetCSD) %>%<%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;">SSRB</td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(AvgDailyTargetsSSRBFinal!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(AvgDailyTargetsSSRBFinal) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(YesterdaySalesSSRB!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(YesterdaySalesSSRB) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(BalanceReqSSRB!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(BalanceReqSSRB) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(MTDSalesSSRB!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(MTDSalesSSRB)%><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(AvgDailyTargetsSSRB!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(AvgDailyTargetsSSRB) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(MTDVsTargtetSSRB!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(MTDVsTargtetSSRB) %>%<%} %></td>				
									
					   	 </tr>
					   	 
					   	 <tr style="font-size:11px;">
								
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;">Sting</th>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(AvgDailyTargetsStingFinal!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(AvgDailyTargetsStingFinal) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(YesterdaySalesSting!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(YesterdaySalesSting) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(BalanceReqSting!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(BalanceReqSting) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(MTDSalesSting!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(MTDSalesSting)%><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(AvgDailyTargetsSting!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(AvgDailyTargetsSting) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(MTDVsTargtetSting!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(MTDVsTargtetSting)%>%<%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;">Other SS</td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(AvgDailyTargetsOtherSSFinal!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(AvgDailyTargetsOtherSSFinal) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(YesterdaySalesOtherSS!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(YesterdaySalesOtherSS) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(BalanceReqOtherSS!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(BalanceReqOtherSS) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(MTDSalesOtherSS!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(MTDSalesOtherSS)%><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(AvgDailyTargetsOtherSS!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(AvgDailyTargetsOtherSS) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(MTDVsTargtetOtherSS!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(MTDVsTargtetOtherSS) %>%<%} %></td>				
									
					   	 </tr>
					   	 
					   	 <tr style="font-size:11px;">
								
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;">AF</td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(YesterdaySalesAF!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(YesterdaySalesAF) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(MTDSalesAF!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(MTDSalesAF)%><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;">1.5/1.75</td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(AvgDailyTargets50175Final!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(AvgDailyTargets50175Final) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(YesterdaySales150175!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(YesterdaySales150175) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(BalanceReq1501725!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(BalanceReq1501725) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(MTDSales150175!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(MTDSales150175)%><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(AvgDailyTargets50175!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(AvgDailyTargets50175) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(MTDVsTargtet1501725!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(MTDVsTargtet1501725) %>%<%} %></td>				
									
					   	 </tr>
					   	 
					   	 <tr style="font-size:11px;">
								
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;">Slice</td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(AvgDailyTargetsSliceFinal!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(AvgDailyTargetsSliceFinal) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(YesterdaySalesSlice!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(YesterdaySalesSlice) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(BalanceReqSlice!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(BalanceReqSlice) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(MTDSalesSlice!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(MTDSalesSlice)%><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(AvgDailyTargetsSlice!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(AvgDailyTargetsSlice) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(MTDVsTargtetSlice!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(MTDVsTargtetSlice) %>%<%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;">1L</td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(AvgDailyTargets1000Final!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(AvgDailyTargets1000Final) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(YesterdaySales1000!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(YesterdaySales1000) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(BalanceReq1000!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(BalanceReq1000) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(MTDSales1000!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(MTDSales1000) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(AvgDailyTargets1000!=0){%><%=Utilities.getDisplayCurrencyFormat(AvgDailyTargets1000) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(MTDVsTargtet1000!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(MTDVsTargtet1000) %>%<%} %></td>				
									
					   	 </tr>
					   	  <tr style="font-size:11px;">
								
								<td data-priority="1"  style="text-align:center; background-color:#f7fff7;"><b>Total</b></td>
								<td data-priority="1"  style="text-align:center; background-color:#f7fff7;"><%if(AvgDailyTargetsFinal!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(AvgDailyTargetsFinal) %><%} %></td>
								<td data-priority="1"  style="text-align:center;background-color:#f7fff7; "><%if(TotalYesterdaySales!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(TotalYesterdaySales) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#f7fff7;"><%if(BalanceReqTotal!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(BalanceReqTotal) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#f7fff7;"><%if(TotalMTDSales!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(TotalMTDSales) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#f7fff7;"><%if(MonthlyTargetTotal!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(MonthlyTargetTotal) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#f7fff7;"><%if(MTDVsTargtetTotal!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(MTDVsTargtetTotal) %>%<%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;">2.25</td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(AvgDailyTargets2250Final!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(AvgDailyTargets2250Final) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(YesterdaySales2250!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(YesterdaySales2250) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(BalanceReq2250!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(BalanceReq2250) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(MTDSales2250!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(MTDSales2250)%><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(AvgDailyTargets2250!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(AvgDailyTargets2250) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(MTDVsTargtet2250!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(MTDVsTargtet2250) %>%<%} %></td>				
									
					   	 </tr>
					   	 
							<%
							Srno++;
							
							//Grand Total
							
							 
							 GrandTotalAvgDailyTargets+=AvgDailyTargetCSD;
							 GrandTotalAvgDailyTargetsSting+=AvgDailyTargetsStingFinal;
							 GrandTotalAvgDailyTargetsSlice+=AvgDailyTargetsSliceFinal;
							 GrandTotalAvgDailyTargetsSSRB+=AvgDailyTargetsSSRBFinal;
							 GrandTotalAvgDailyTargetsOtherSS+=AvgDailyTargetsOtherSSFinal;
							 GrandTotalAvgDailyTargets1501750+=AvgDailyTargets50175Final;
							 GrandTotalAvgDailyTargets1000+=AvgDailyTargets1000Final;
							 GrandTotalAvgDailyTargets2250+=AvgDailyTargets2250Final;
							 
							 
							 GrandTotalMTDTargets+=AvgDailyTargets;
							 GrandTotalMTDTargetsSting+=AvgDailyTargetsSting;
							 GrandTotalMTDTargetsSlice+=AvgDailyTargetsSlice;
							 GrandTotalMTDTargetsSSRB+=AvgDailyTargetsSSRB;
							 GrandTotalMTDTargetsOtherSS+=AvgDailyTargetsOtherSS;
							 GrandTotalMTDTargets1501750+=AvgDailyTargets50175;
							 GrandTotalMTDTargets1000+=AvgDailyTargets1000;
							 GrandTotalMTDTargets2250+=AvgDailyTargets2250;
							 
							
							
							 GrandTotalYesterdaySalesCSD+=YesterdaySalesCSD;
							 GrandTotalYesterdaySalesSting+=YesterdaySalesSting;
							 GrandTotalYesterdaySalesSlice+=YesterdaySalesSlice;
							 GrandTotalYesterdaySalesSSRB+=YesterdaySalesSSRB;
							 GrandTotalYesterdaySalesOtherSS+=YesterdaySalesOtherSS;
							 GrandTotalYesterdaySales1501750+=YesterdaySales150175;
							 GrandTotalYesterdaySales1000+=YesterdaySales1000;
							 GrandTotalYesterdaySales2250+=YesterdaySales2250;
							 GrandTotalYesterdaySalesAF+=YesterdaySalesAF;
							
							 GrandTotalMTDSalesCSD+=MTDSalesCSD;
							 GrandTotalMTDSalesSting+=MTDSalesSting;
							 GrandTotalMTDSalesSlice+=MTDSalesSlice;
							 GrandTotalMTDSalesSSRB+=MTDSalesSSRB;
							 GrandTotalMTDSalesOtherSS+=MTDSalesOtherSS;
							 GrandTotalMTDSales15001750+=MTDSales150175;
							 GrandTotalMTDSales1000+=MTDSales1000;
							 GrandTotalMTDSales2250+=MTDSales2250;
							 GrandTotalMTDSalesAF+=MTDSalesAF;
							
							
							 if(GrandTotalMTDTargets!=0){
								 GrandTotalMTDVsTargtetCSD =(GrandTotalMTDSalesCSD/GrandTotalMTDTargets)*100;
							 }
							 
							 if(GrandTotalMTDTargetsSting!=0){
								 GrandTotalMTDVsTargtetSting=(GrandTotalMTDSalesSting/GrandTotalMTDTargetsSting)*100;
							 }
							 
							 if(GrandTotalMTDTargetsSlice!=0){
								 GrandTotalMTDVsTargtetSlice=(GrandTotalMTDSalesSlice/GrandTotalMTDTargetsSlice)*100;
							 }
							
							 if(GrandTotalMTDTargetsSSRB!=0){
								 GrandTotalMTDVsTargtetSSRB=(GrandTotalMTDSalesSSRB/GrandTotalMTDTargetsSSRB)*100;	 
							 }
							 
							 if(GrandTotalMTDTargetsOtherSS!=0){
								 GrandTotalMTDVsTargtetOtherSS=(GrandTotalMTDSalesOtherSS/GrandTotalMTDTargetsOtherSS)*100;
							 }
							 
							 if(GrandTotalMTDTargets1501750!=0){
								 GrandTotalMTDVsTargtet15001750=(GrandTotalMTDSales15001750/GrandTotalMTDTargets1501750)*100;	 
							 }
							 
							if(GrandTotalMTDTargets1000!=0){
								 GrandTotalMTDVsTargtet1000=(GrandTotalMTDSales1000/GrandTotalMTDTargets1000)*100;
							}
							
							if(GrandTotalMTDTargets2250!=0){
								GrandTotalMTDVsTargtet2250=(GrandTotalMTDSales2250/GrandTotalMTDTargets2250)*100;
							}
							
							 
							
							if(RemaingDaysOfMonth!=0){
								
								if(GrandTotalMTDTargets!=0){
									GrandTotalBalanceReqCSD=(GrandTotalMTDTargets-GrandTotalMTDSalesCSD)/RemaingDaysOfMonth;
								}
								if(GrandTotalMTDTargetsSting!=0){
									GrandTotalBalanceReqSting=(GrandTotalMTDTargetsSting-GrandTotalMTDSalesSting)/RemaingDaysOfMonth;								
								}
								if(GrandTotalMTDTargetsSlice!=0){
									GrandTotalBalanceReqSlice=(GrandTotalMTDTargetsSlice-GrandTotalMTDSalesSlice)/RemaingDaysOfMonth;
								}
								if(GrandTotalMTDTargetsSSRB!=0){
									GrandTotalBalanceReqSSRB=(GrandTotalMTDTargetsSSRB-GrandTotalMTDSalesSSRB)/RemaingDaysOfMonth;
								}
								if(GrandTotalMTDTargetsOtherSS!=0){
									GrandTotalBalanceReqOtherSS=(GrandTotalMTDTargetsOtherSS-GrandTotalMTDSalesOtherSS)/RemaingDaysOfMonth;
								}
								if(GrandTotalMTDTargets1501750!=0){
									GrandTotalBalanceReq15001750=(GrandTotalMTDTargets1501750-GrandTotalMTDSales15001750)/RemaingDaysOfMonth;
								}
								if(GrandTotalMTDTargets1000!=0){
									GrandTotalBalanceReq1000=(GrandTotalMTDTargets1000-GrandTotalMTDSales1000)/RemaingDaysOfMonth;
								}
								if(GrandTotalMTDTargets2250!=0){
									GrandTotalBalanceReq2250=(GrandTotalMTDTargets2250-GrandTotalMTDSales2250)/RemaingDaysOfMonth;
								}
								
							
								 
							}
							 
							
							
							
							
							
							//Total of Total
							 GrandTotalAvgDailyTargetsTotal+=AvgDailyTargetsFinal;
							 GrandTotalYesterdaySalesTotal+=TotalYesterdaySales;
							 GrandTotalMTDSalesTotal+=TotalMTDSales;
							 
							 
							 GrandTotalMTDTargetsTotal+= (AvgDailyTargets+AvgDailyTargetsSting+AvgDailyTargetsSlice);
							 
							 if(RemaingDaysOfMonth!=0 && GrandTotalMTDTargetsTotal!=0){
								 GrandTotalBalanceReqTotal=(GrandTotalMTDTargetsTotal-GrandTotalMTDSalesTotal)/RemaingDaysOfMonth;	 
							 }
							 
							 if(GrandTotalMTDTargetsTotal!=0){
								 GrandTotalMTDVsTargtetTotal=(GrandTotalMTDSalesTotal/GrandTotalMTDTargetsTotal)*100;	 
							 }
							 
							 
							
							
							
							
							}
							
							%>
							
						<!--  Grand Total  -->	
							
							 <tr style="font-size:11px;">
								<td data-priority="1"  style="text-align:center; " rowspan="5" colspan="3">Total</td>
								
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;">CSD</td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(GrandTotalAvgDailyTargets!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalAvgDailyTargets) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(GrandTotalYesterdaySalesCSD!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalYesterdaySalesCSD) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(GrandTotalBalanceReqCSD!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalBalanceReqCSD) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(GrandTotalMTDSalesCSD!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDSalesCSD) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(GrandTotalMTDTargets!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDTargets) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(GrandTotalMTDVsTargtetCSD!=0){ %><%=Utilities.getDisplayCurrencyFormat(GrandTotalMTDVsTargtetCSD) %>%<%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;">SSRB</td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalAvgDailyTargetsSSRB!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalAvgDailyTargetsSSRB) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalYesterdaySalesSSRB!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalYesterdaySalesSSRB) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalBalanceReqSSRB!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalBalanceReqSSRB) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalMTDSalesSSRB!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDSalesSSRB) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalMTDTargetsSSRB!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDTargetsSSRB) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalMTDVsTargtetSSRB!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDVsTargtetSSRB) %>%<%} %></td>				
									
					   	 </tr>
						 <tr style="font-size:11px;">
								
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;">Sting</th>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(GrandTotalAvgDailyTargetsSting!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalAvgDailyTargetsSting) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(GrandTotalYesterdaySalesSting!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalYesterdaySalesSting) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(GrandTotalBalanceReqSting!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalBalanceReqSting) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(GrandTotalMTDSalesSting!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDSalesSting) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(GrandTotalMTDTargetsSting!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDTargetsSting) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(GrandTotalMTDVsTargtetSting!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDVsTargtetSting) %>%<%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;">Other SS</td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalAvgDailyTargetsOtherSS!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalAvgDailyTargetsOtherSS) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalYesterdaySalesOtherSS!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalYesterdaySalesOtherSS) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalBalanceReqOtherSS!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalBalanceReqOtherSS) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalMTDSalesOtherSS!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDSalesOtherSS) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalMTDTargetsOtherSS!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDTargetsOtherSS) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalMTDVsTargtetSting!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDVsTargtetSting) %>%<%} %></td>				
									
					   	 </tr>
					   	 
					   	 <tr style="font-size:11px;">
								
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;">AF</td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(GrandTotalYesterdaySalesAF!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalYesterdaySalesAF) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(GrandTotalMTDSalesAF!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDSalesAF) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;">1.5/1.75</td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalAvgDailyTargets1501750!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalAvgDailyTargets1501750) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalYesterdaySales1501750!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalYesterdaySales1501750) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalBalanceReq15001750!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalBalanceReq15001750) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalMTDSales15001750!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDSales15001750) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalMTDTargets1501750!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDTargets1501750) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalMTDVsTargtet15001750!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDVsTargtet15001750) %>%<%} %></td>				
									
					   	 </tr>
					   	 
					   	 <tr style="font-size:11px;">
								
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;">Slice</td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(GrandTotalAvgDailyTargetsSlice!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalAvgDailyTargetsSlice) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(GrandTotalYesterdaySalesSlice!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalYesterdaySalesSlice) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(GrandTotalBalanceReqSlice!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalBalanceReqSlice) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(GrandTotalMTDSalesSlice!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDSalesSlice) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(GrandTotalMTDTargetsSlice!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDTargetsSlice) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#ffffe6;"><%if(GrandTotalMTDVsTargtetSlice!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDVsTargtetSlice) %>%<%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;">1L</td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalAvgDailyTargets1000!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalAvgDailyTargets1000) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalYesterdaySales1000!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalYesterdaySales1000) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalBalanceReq1000!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalBalanceReq1000) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalMTDSales1000!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDSales1000) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalMTDTargets1000!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDTargets1000) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalMTDVsTargtet1000!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDVsTargtet1000) %>%<%} %></td>				
									
					   	 </tr>	
					   	 <tr style="font-size:11px;">
								
								<td data-priority="1"  style="text-align:center; background-color:#f7fff7;"><b>Total</b></td>
								<td data-priority="1"  style="text-align:center; background-color:#f7fff7;"><%if(GrandTotalAvgDailyTargetsTotal!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalAvgDailyTargetsTotal) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#f7fff7;"><%if(GrandTotalYesterdaySalesTotal!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalYesterdaySalesTotal) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#f7fff7;"><%if(GrandTotalBalanceReqTotal!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalBalanceReqTotal) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#f7fff7;"><%if(GrandTotalMTDSalesTotal!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDSalesTotal) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#f7fff7;"><%if(GrandTotalMTDTargetsTotal!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDTargetsTotal) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#f7fff7;"><%if(GrandTotalMTDVsTargtetTotal!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDVsTargtetTotal) %>%<%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;">2.25</td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalAvgDailyTargets2250!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalAvgDailyTargets2250) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalYesterdaySales2250!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalYesterdaySales2250) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalBalanceReq2250!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalBalanceReq2250) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalMTDSales2250!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDSales2250) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalMTDTargets2250!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDTargets2250) %><%} %></td>
								<td data-priority="1"  style="text-align:center; background-color:#fafaf8;"><%if(GrandTotalMTDVsTargtet2250!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalMTDVsTargtet2250) %>%<%} %></td>				
									
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