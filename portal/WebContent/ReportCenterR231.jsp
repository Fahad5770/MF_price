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
int FeatureID = 292;





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
					   	<th colspan="3" style="b1ackground-color:#A2A2A3; color:white;"></th>
					   	<th colspan="1"  style="text-align:center; background-color:#F7FFF7;">Current Stock</th>
					   	<th colspan="2" style="text-align:center; background-color:#FFFFE6; ">Average Daily Sale</th>
					   	<th colspan="2" style="text-align:center; background-color:#FAFAF8;">Stock Days</th>
					   </tr>
					   
					    <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center; ">Distributor</th>							
							<th data-priority="1"  style="text-align:center; ">Packages</th>
							<th data-priority="1"  style="text-align:center; ">Brands</th>
							<th data-priority="1"  style="text-align:center; background-color:#F7FFF7;">R/C</th>
							<th data-priority="1"  style="text-align:center; background-color:#FFFFE6;">Last Month Actual</th>
							<th data-priority="1"  style="text-align:center; background-color:#FFFFE6;">Current Month Target</th>
							<th data-priority="1"  style="text-align:center; background-color:#FAFAF8;">Last Month Actual</th>
							<th data-priority="1"  style="text-align:center; background-color:#FAFAF8;">Current Month Target</th>
														
					    </tr>
					  </thead> 
						<tbody>
							<%
							// Last month
							
							int month = Utilities.getMonthNumberByDate(EndDate);
							int year = Utilities.getYearByDate(EndDate);
							Date StartDateLastMonth = Utilities.getStartDateByMonth(month-2, year);
							Date EndDateLastMonth = Utilities.getEndDateByMonth(month-2, year);
							
							int DaysInLastMonth = Utilities.getDayNumberByDate(EndDateLastMonth);
							
							long TotalBrandCount=0;
							
							double GrandTotalBalance = 0;
							double GrandTotalSecondarySales = 0;
							double GrandTotalLiftingTarget = 0;
							
							ResultSet rs = s.executeQuery("SELECT *  FROM common_distributors cd where 1=1 "+WhereDistributors+WhereHOD+WhereRSM);
							while(rs.next()){
								long BrandCountForDistributor=0;
								int i=0;
								long BrandCount=1;
								int DistCount=0;
								long DistributorID = rs.getLong("distributor_id");
								
								double DistributorTotalBalance = 0;
								double DistributorTotalSecondarySales = 0;
								double DistributorTotalLiftingTarget = 0;
								ResultSet rs3 = s3.executeQuery("SELECT distinct ipv.package_id, ipv.package_label FROM pep.inventory_products_view ipv where ipv.category_id = 1 and ipv.is_visible = 1 order by ipv.package_id");
								while(rs3.next()){
									int PackageID = rs3.getInt("package_id");
									
									double PackageTotalBalance = 0;
									double PackageTotalSecondarySales = 0;
									
									ResultSet rs1 = s2.executeQuery("SELECT ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, ipv.product_id, ipv.unit_per_sku, (SELECT sum(total_units_received-total_units_issued) FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id = "+DistributorID+" and idsp.product_id = ipv.product_id) balance, (select sum(isap.total_units) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.distributor_id = "+DistributorID+" and isap.product_id = ipv.product_id and isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDateNext(EndDateLastMonth)+") total_secondary_sales FROM pep.inventory_products_view ipv where ipv.category_id = 1 and ipv.is_visible = 1 and ipv.package_id = "+PackageID+" having balance > 0 order by ipv.package_id");
									while(rs1.next()){
									
										int BrandID = rs1.getInt("brand_id");
										int ProductID = rs1.getInt("product_id");
										double UnitPerSKU = rs1.getDouble("unit_per_sku");
										double balance = rs1.getDouble("balance");
										
										double TotalSecondarySales = rs1.getDouble("total_secondary_sales") / UnitPerSKU;
										
										double BalanceRawCases = balance / UnitPerSKU;
										
										double AverageSecondaySales = TotalSecondarySales / DaysInLastMonth;
										
										double StockDaysLastMonthActual = 0;
										if (AverageSecondaySales != 0){
											StockDaysLastMonthActual = BalanceRawCases / AverageSecondaySales;
										}
										
										PackageTotalBalance += BalanceRawCases;
										DistributorTotalBalance += BalanceRawCases; 
										GrandTotalBalance += BalanceRawCases;
										
										PackageTotalSecondarySales += TotalSecondarySales;
										DistributorTotalSecondarySales += TotalSecondarySales;
										GrandTotalSecondarySales += TotalSecondarySales;
										%>
											
										<tr>
											<td><%=rs.getLong("distributor_id") %> - <%=rs.getString("name") %></td>
											<td><%=rs1.getString("package_label") %></td>								
											<td><%=rs1.getString("brand_label") %></td>
											<td style="text-align: right; background-color:#F7FFF7;"><%=Utilities.getDisplayCurrencyFormatRounded(BalanceRawCases) %></td>
											<td style="text-align: right; background-color:#FFFFE6;"><%=Utilities.getDisplayCurrencyFormatRounded(AverageSecondaySales) %></td>
											<td style="background-color:#FFFFE6;"></td>
											<td style="text-align: right;background-color:#FAFAF8;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(StockDaysLastMonthActual) %></td>
											<td style="background-color:#FAFAF8;"></td>
												
										</tr>
										<%
										}
									
									if (PackageTotalBalance != 0){
										
										double TargetLifting = 0;
										ResultSet rs11 = s2.executeQuery("SELECT dtp.quantity FROM pep.distributor_targets dt join distributor_targets_packages dtp on dt.id = dtp.id where month = "+month+" and year = "+year+" and distributor_id = "+DistributorID+" and package_id ="+PackageID);
										if (rs11.first()){
											TargetLifting = rs11.getDouble(1);
										}
										DistributorTotalLiftingTarget += TargetLifting;
										GrandTotalLiftingTarget +=TargetLifting;
												
										double DailyTargetLifting = TargetLifting / Utilities.getDayNumberByDate(Utilities.getEndDateByDate(EndDate));
										
										double PackageAverageSecondaySales = PackageTotalSecondarySales / DaysInLastMonth;
										
										double PackageStockDaysLastMonthActual = 0;
										if (PackageAverageSecondaySales != 0){
											PackageStockDaysLastMonthActual = PackageTotalBalance / PackageAverageSecondaySales;
										}
										
										
										double StockDaysCurrentMonthTarget = 0;
										if (DailyTargetLifting != 0){
											StockDaysCurrentMonthTarget = PackageTotalBalance / DailyTargetLifting;
										}
										
										
										
									%>
										<tr>
											<td><%=rs.getLong("distributor_id") %> - <%=rs.getString("name") %></td>
											<td><%=rs3.getString("package_label") %></td>								
											<td><b>Total</b></td>
											<td style="text-align: right;background-color:#F7FFF7;"><b><%=Utilities.getDisplayCurrencyFormatRounded(PackageTotalBalance) %></b></td>
											<td style="text-align: right;background-color:#FFFFE6;"><b><%=Utilities.getDisplayCurrencyFormatRounded(PackageAverageSecondaySales) %></b></td>
											<td style="text-align: right;background-color:#FFFFE6;"><b><%=Utilities.getDisplayCurrencyFormatRounded(DailyTargetLifting) %></b></td>
											<td style="text-align: right;background-color:#FAFAF8;"><b><%=Utilities.getDisplayCurrencyFormatOneDecimal(PackageStockDaysLastMonthActual) %></b></td>
											<td style="text-align: right;background-color:#FAFAF8;"><b><%=Utilities.getDisplayCurrencyFormatOneDecimal(StockDaysCurrentMonthTarget) %></b></td>
												
										</tr>
									<%
									}
								}
										
										
								double DistributorAverageSecondaySales = DistributorTotalSecondarySales / DaysInLastMonth;

								
								double DistributorStockDaysLastMonthActual = 0;
								if (DistributorAverageSecondaySales != 0){
									DistributorStockDaysLastMonthActual = DistributorTotalBalance / DistributorAverageSecondaySales;
								}
								
								double DistributorDailyTargetLifting = DistributorTotalLiftingTarget / Utilities.getDayNumberByDate(Utilities.getEndDateByDate(EndDate));
								

								double DistributorStockDaysCurrentMonthTarget = 0;
								if (DistributorDailyTargetLifting != 0){
									DistributorStockDaysCurrentMonthTarget = DistributorTotalBalance / DistributorDailyTargetLifting;
								}

										%>
							<tr>
								<td><%=rs.getLong("distributor_id") %> - <%=rs.getString("name") %></td>
								<td>Total</td>
								<td></td>
								<td style="text-align: right;background-color:#F7FFF7;"><b><%=Utilities.getDisplayCurrencyFormatRounded(DistributorTotalBalance) %></b></td>
								<td style="text-align: right;background-color:#FFFFE6;"><b><%=Utilities.getDisplayCurrencyFormatRounded(DistributorAverageSecondaySales) %></b></td>
								<td style="text-align: right;background-color:#FFFFE6;"><b><%=Utilities.getDisplayCurrencyFormatOneDecimal(DistributorDailyTargetLifting) %></b></td>
								<td style="text-align: right;background-color:#FAFAF8;"><b><%=Utilities.getDisplayCurrencyFormatOneDecimal(DistributorStockDaysLastMonthActual) %></b></td>
								<td style="text-align: right;background-color:#FAFAF8;"><b><%=Utilities.getDisplayCurrencyFormatOneDecimal(DistributorStockDaysCurrentMonthTarget) %></b></td>
							</tr>
							
							<%
							}
							
							
							double GrandAverageSecondaySales = GrandTotalSecondarySales / DaysInLastMonth;
							
							
							double GrandStockDaysLastMonthActual = 0;
							if (GrandAverageSecondaySales != 0){
								GrandStockDaysLastMonthActual = GrandTotalBalance / GrandAverageSecondaySales;
							}
							
							double GrandDailyTargetLifting = GrandTotalLiftingTarget / Utilities.getDayNumberByDate(Utilities.getEndDateByDate(EndDate));
							
							
							
							double GrandStockDaysCurrentMonthTarget = 0;
							if (GrandDailyTargetLifting != 0){
								GrandStockDaysCurrentMonthTarget = GrandTotalBalance / GrandDailyTargetLifting;
							}
							
							%>
							<tr>
								<td>Total</td>
								<td>Grand Total</td>
								<td></td>
								<td style="text-align: right;background-color:#F7FFF7;"><b><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotalBalance) %></b></td>
								<td style="text-align: right;background-color:#FFFFE6;"><b><%=Utilities.getDisplayCurrencyFormatRounded(GrandAverageSecondaySales) %></b></td>
								<td style="text-align: right;background-color:#FFFFE6;"><b><%=Utilities.getDisplayCurrencyFormatOneDecimal(GrandDailyTargetLifting) %></b></td>
								<td style="text-align: right;background-color:#FAFAF8;"><b><%=Utilities.getDisplayCurrencyFormatOneDecimal(GrandStockDaysLastMonthActual) %></b></td>
								<td style="text-align: right;background-color:#FAFAF8;"><b><%=Utilities.getDisplayCurrencyFormatOneDecimal(GrandStockDaysCurrentMonthTarget) %></b></td>
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