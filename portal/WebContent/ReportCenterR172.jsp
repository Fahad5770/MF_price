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
int FeatureID = 208;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	//response.sendRedirect("AccessDenied.jsp");
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
String WhereCustomerID ="";
if (CustomerID != 0){
	WhereCustomerID = " and customer_id in ("+CustomerID+") ";	
}

//System.out.println("hello "+WhereCustomerID);

   //
String WarehouseTitle="All";
   
String WarehouseTitle1="";

           String WarehouseIDs= "";
           String WhereWarehouse1 ="";
           long SelectedWarehouseArray[] = null;
           if (session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse") != null){
           	SelectedWarehouseArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse");
           	WarehouseIDs = Utilities.serializeForSQL(SelectedWarehouseArray);
           }
           String WhereWarehouse = "";
           if (WarehouseIDs.length() > 0){
           		WhereWarehouse = " and glcr.warehouse_id in ("+WarehouseIDs+") ";
           		WhereWarehouse1 = " and id in("+WarehouseIDs+")";
           }
               
           
           if (WarehouseIDs.length() > 0){
          		ResultSet rs = s.executeQuery("select label from common_warehouses where 1=1"+WhereWarehouse1);	
          		while(rs.next()){
          			WarehouseTitle1 += rs.getString("label")+",";
          		}
          		
          		WarehouseTitle = WarehouseTitle1.substring(0,WarehouseTitle1.length()-1); //removing last comma
          }
           
  
//Outlet Contract Status
String SelectedOutletContractStatusArray[]={};
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutletContractStatus") != null){
	SelectedOutletContractStatusArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutletContractStatus");
	//OutletTypes = Utilities.serializeForSQL(SelectedOutletTypeArray);
}

String WhereNewOutlets = "";
String WhereReneweContracts = "";
String WhereConvertedtoPEPSI = "";
String WhereConvertedtoCoke = "";




for(int i=0;i<SelectedOutletContractStatusArray.length;i++){
	
	
	if(SelectedOutletContractStatusArray[i].equals("New Outlets")){	
		WhereNewOutlets = "";
	}
	
	if(SelectedOutletContractStatusArray[i].equals("Renewed Contracts")){	
		WhereReneweContracts="";
	}
	
	if(SelectedOutletContractStatusArray[i].equals("Converted to PEPSI")){	
		WhereConvertedtoPEPSI="";
	}
	
	if(SelectedOutletContractStatusArray[i].equals("Converted to Coke")){	
		WhereConvertedtoCoke="";
	}
}       
  
 

//Region


String RegionIDs="";
long SelectedRegionArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRegion") != null){
	SelectedRegionArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRegion");
	RegionIDs = Utilities.serializeForSQL(SelectedRegionArray);
}

String WhereRegion = "";
if (RegionIDs.length() > 0){
	WhereRegion = " and co.region_id in ("+RegionIDs+") ";	
}

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Outlet Planned vs Actual Sales</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 
					
						
						<thead>
					    
							
					    <tr style="font-size:11px;">
					    							
							<th style="width: 15%;">Outlet</th>
							<th style="width: 15%;">Distributor</th>
							<th style="width: 8%;">Advance Issued</th>
							<th style="width: 8%;">Current Balance</th>
							<th style="width: 8%;">Chillers Injected</th>
							<th style="width: 8%;">Planned Period</th>
							<th style="width: 8%;">Planned Sales</th>
							<th style="width: 8%;">Actual Sales</th>
							<th style="width: 7%;">Sales (%)</th>
							<th style="width: 7%;">Days Left</th>
							<th style="width: 7%;">Plan</th>
										
					    </tr>
					    
					  </thead> 
						
						<tbody>
						<%
						double TotalChiller=0;
						double TotalLedgerBlnce=0;
						double TotalPlannedSales=0;
						double TotalActualSales=0;
						double TotalAdvanceIssued = 0;
						ResultSet rs = s.executeQuery("select co.id, co.name, co.cache_distributor_id distributor_id_i, (select name from common_distributors where distributor_id = distributor_id_i) distributor_name, (SELECT count(*) FROM common_assets where outlet_id = co.id and tot_status = 'INJECTED') tot_injected, (select ifnull(sum(debit),0) - ifnull(sum(credit),0) from sampling_posting_accounts where outlet_id = co.id) ledger_balance, sps.start_date, sps.end_date, (SELECT sum(((spsp.quantity * ip.unit_per_case * ip.liquid_in_ml) / ip.conversion_rate_in_ml)) converted_cases FROM sampling_planned_sales_packages spsp join inventory_packages ip on spsp.package_id = ip.id where spsp.id = sps.id) planned_sales, (SELECT sum(((ss.quty_quant * ipv.unit_per_sku * ipv.liquid_in_ml) / ipv.conversion_rate_in_ml)) converted_cases FROM sap_sales ss join inventory_products_view ipv on ss.material_matnr = ipv.sap_code where ss.outlet_id = co.id and ss.created_on_erdat between sps.start_date and sps.end_date) actual_sales, to_days(sps.end_date) - to_days(curdate()) days_left, sps.request_id, s.sampling_id, s.advance_company_share FROM sampling s join sampling_planned_sales sps on s.request_id = sps.request_id join common_outlets co on s.outlet_id = co.id where s.active=1 "+WhereRegion+" order by days_left");
						while(rs.next()){
							
							TotalChiller+= rs.getLong("tot_injected");
							TotalLedgerBlnce+=rs.getDouble("ledger_balance");
							TotalPlannedSales+=rs.getDouble("planned_sales");
							TotalActualSales+=rs.getDouble("actual_sales");
							TotalAdvanceIssued+=rs.getDouble("advance_company_share");
						%>
						<tr>
							<td><%=rs.getLong("id") %> - <%=Utilities.truncateStringToMax(rs.getString("name"),20) %></td>
							<td><%if (rs.getLong("distributor_id_i") != 0){%><%=rs.getLong("distributor_id_i") %> - <%= Utilities.truncateStringToMax(rs.getString("distributor_name"), 20)  %><%} %></td>
							<td style="text-align:right"><%= Utilities.getDisplayCurrencyFormatRoundedAccounting(rs.getDouble("advance_company_share")) %></td>
							<td style="text-align:right"><%= Utilities.getDisplayCurrencyFormatRoundedAccounting(rs.getDouble("ledger_balance")) %></td>
							<td style="text-align:center"><%=rs.getLong("tot_injected") %></td>
							<td style="text-align:center"><%=Utilities.getDisplayDateFormat(rs.getDate("start_date"))%><br><%=Utilities.getDisplayDateFormat(rs.getDate("end_date")) %></td>
							<td style="text-align:right"><%=Utilities.getDisplayCurrencyFormat(rs.getDouble("planned_sales")) %></td>
							<td style="text-align:right"><%=Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("actual_sales")) %></td>
							<td style="text-align:center"><%= Utilities.getDisplayCurrencyFormatRounded((rs.getDouble("actual_sales")/rs.getDouble("planned_sales"))*100)%>%</td>
							<td style="text-align:center"><%if(rs.getLong("days_left")>0){%><%=rs.getLong("days_left") %><%} else{ %>0<%} %></td>
							<td style="text-align:center">
								<a href="#popupDialog16" data-rel="popup" data-position-to="window" data-transition="pop" id="PercaseView" onClick="R172LoadPlannedPackagesQuantity(<%=rs.getString("request_id")%>, <%=rs.getString("sampling_id")%>);">View</a>
							</td>
							
						</tr>
						<%
						
						}
						%>
						<tr>
						<td></td>
						<th>Total</th>
						<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormat(TotalAdvanceIssued) %></td>
						<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormat(TotalLedgerBlnce) %></td>
						
						<td style="text-align:center"><%=Math.round(TotalChiller) %></td>
						<td></td>
						<td style="text-align:right"><%=Utilities.getDisplayCurrencyFormat(TotalPlannedSales) %></td>
						<td style="text-align:right"><%=Utilities.getDisplayCurrencyFormatRounded(TotalActualSales) %></td>
						<td style="text-align:center"><%= Utilities.getDisplayCurrencyFormatRounded((TotalActualSales/TotalPlannedSales)*100)%>%</td>
						
						<td></td>
						
						
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