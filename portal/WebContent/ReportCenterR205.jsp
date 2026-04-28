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
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
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
int FeatureID = 257;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();
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
	WhereHOD = " and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and idn.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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
	WhereDistributors = " and idn.distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}

//

//Warehouse


String WarehouseIDs="";
long SelectedWarehouseArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse") != null){
	SelectedWarehouseArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse");
	WarehouseIDs = Utilities.serializeForSQL(SelectedWarehouseArray);
}
//System.out.println(WarehouseIDs);
String WhereWarehouse = "";
if (WarehouseIDs.length() > 0){
	WhereWarehouse = " and idn.warehouse_id in ("+WarehouseIDs+") ";	
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
	WhereSM = " and idn.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
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
	WhereTDM = " and idn.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
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
	WhereASM = " and idn.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
}



Datasource dsSAP = new Datasource();
dsSAP.createConnectionToSAPDB();
Statement sSAP = dsSAP.createStatement();


%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>
<%


//System.out.println(StartDateMonth6+" - "+EndDateMonth6);

%>

<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size:11px;">
					 	   	<th data-priority="1"  style="text-align:center; width: 7.7% ">Date</th>
							<th data-priority="1"  style="text-align:center; width: 7.7% ">Order#</th>
							<th data-priority="1"  style="text-align:center; width: 29.7% ">Distributor</th>	
							<th data-priority="1"  style="text-align:center; width: 9.3% ">Balance</th>
							<th data-priority="1"  style="text-align:center; width: 7.7% ">Cash</th>
							<th data-priority="1"  style="text-align:center; width: 7.7% ">Advance</th>
							<th data-priority="1"  style="text-align:center; width: 7.7% ">Credit</th>
							<th data-priority="1"  style="text-align:center; width: 7.7% ">Promotion</th>
							<th data-priority="1"  style="text-align:center; width: 7.7% ">Free Stock</th>
							<th data-priority="1"  style="text-align:center; width: 7.7% ">Total</th>
					    </tr>
					    
					  </thead> 
					<tbody>
					<%
					double TotalCash = 0;
					double TotalAdvanceCash = 0;
					double TotalCredit = 0;
					double TotalFreeStock = 0;
					double TotalPromoStock = 0;
					double TotalInvoiceAmount = 0;
					
					//System.out.println("select created_on, sap_order_no, invoice_no, distributor_id, (select name from common_distributors where distributor_id = idn.distributor_id) distributor_name, invoice_amount from inventory_delivery_note idn where created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+" ");
					
					ResultSet rs = s.executeQuery("select created_on, order_no, customer_id, (select name from common_distributors where distributor_id = glop.customer_id) distributor_name, order_amount from gl_order_posting glop where created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+" ");
					while(rs.next()){
						Date PostingDate = rs.getDate("created_on");
						String OrderNo = rs.getString("order_no");
						long DistributorID = rs.getLong("customer_id");
						String DistributorName = rs.getString("distributor_name");
						double OrderAmount = rs.getDouble("order_amount");
						
						
						//System.out.println("select sum((total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)  from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join  inventory_products_view ipv on idnp.product_id = ipv.product_id where idn.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and idnp.delivery_id="+rs.getLong("delivery_id"));
						
						//double InvoiceAmountConverted=0;
							//InvoiceAmountConverted = rs3.getDouble("sum_converted");
						double OrderCC = 0;
						ResultSet rs111 = sSAP.executeQuery("select posnr, matnr, arktx, vrkme, KWMENG from "+ds.getSAPDatabaseAlias()+".vbap where vbeln = '"+Utilities.addLeadingZeros(OrderNo, 10)+"' and pstyv != 'TANN'");
						while(rs111.next()){
							
							int iRawCases = 0;
							int iUnits = 0;
							long iTotalUnits = 0;
							int iProductID = 0;
							int iUnitPerSKU = 0;
							double iLiquidInMl = 0;
							ResultSet rs7 = s4.executeQuery("SELECT product_id, unit_per_sku, liquid_in_ml FROM inventory_products_view where sap_code="+rs111.getString("matnr"));
							if(rs7.first()){
								iProductID = rs7.getInt("product_id");
								iUnitPerSKU = rs7.getInt("unit_per_sku");
								iLiquidInMl = rs7.getDouble(3);
							}

							if(rs111.getString("vrkme").equals("KI") || rs111.getString("vrkme").equals("KAR")){
								iRawCases = Utilities.parseInt(rs111.getString("KWMENG"));
								iUnits = 0;
							}else{
								iRawCases = 0;
								iUnits = Utilities.parseInt(rs111.getString("KWMENG"));
							}
							
							iTotalUnits = (iRawCases * iUnitPerSKU) + iUnits;
							
							OrderCC += (((iTotalUnits * 1d) * iLiquidInMl)/6000d);
						}
							

						
						int UnitPerSKU=0;
						int TotalUnits=0;
						
						int SAPQuantity=0;
						long SAPSapID=0;
						
						double FreeStockConverted=0;
						double PromoConverted=0;
						
						ResultSet rs113 = sSAP.executeQuery("select posnr, matnr, arktx, vrkme, KWMENG from "+ds.getSAPDatabaseAlias()+".vbap where vbeln = '"+Utilities.addLeadingZeros(OrderNo, 10)+"' and pstyv = 'TANN'");
						while(rs113.next()){
							
							int iRawCases = 0;
							int iUnits = 0;
							long iTotalUnits = 0;
							int iProductID = 0;
							int iUnitPerSKU = 0;
							double iLiquidInMl = 0;
							ResultSet rs7 = s4.executeQuery("SELECT product_id, unit_per_sku, liquid_in_ml FROM inventory_products_view where sap_code="+rs113.getString("matnr"));
							if(rs7.first()){
								iProductID = rs7.getInt("product_id");
								iUnitPerSKU = rs7.getInt("unit_per_sku");
								iLiquidInMl = rs7.getDouble(3);
							}

							if(rs113.getString("vrkme").equals("KI") || rs113.getString("vrkme").equals("KAR")){
								iRawCases = Utilities.parseInt(rs113.getString("KWMENG"));
								iUnits = 0;
							}else{
								iRawCases = 0;
								iUnits = Utilities.parseInt(rs113.getString("KWMENG"));
							}
							
							iTotalUnits = (iRawCases * iUnitPerSKU) + iUnits;
							
								PromoConverted += (((iTotalUnits * 1d) * iLiquidInMl)/6000d);
						}
						
						
						ResultSet rs112 = sSAP.executeQuery("select vbak.vbeln from "+ds.getSAPDatabaseAlias()+".vbak where vbak.auart not in ('ZDIS','ZMRS','ZDFR') and vbak.vbeln = '"+Utilities.addLeadingZeros(OrderNo, 10)+"'");
						while(rs112.next()){
							
							//SAPQuantity = rs112.getInt("quantity");
							//SAPSapID = rs112.getLong("matnr");
							/*
							ResultSet rs1 = s2.executeQuery("SELECT liquid_in_ml,conversion_rate_in_ml,unit_per_sku FROM pep.inventory_products_view where sap_code="+SAPSapID);
							if(rs1.first()){
								UnitPerSKU = rs1.getInt("unit_per_sku");
								TotalUnits = UnitPerSKU*SAPQuantity;
								if(rs1.getInt("conversion_rate_in_ml")!=0){
									FreeStockConverted += (TotalUnits*rs1.getInt("liquid_in_ml")/rs1.getInt("conversion_rate_in_ml"));
									InvoiceAmount = 0;
								}
								
							}
							*/
							
							//System.out.println("OK");
							
							FreeStockConverted += OrderCC;
							OrderAmount = 0;
							/*if (Utilities.parseLong(OrderNo) == 70014198){
								System.out.println(InvoiceAmountConverted+" "+FreeStockConverted);
							}*/
							
						}
						
						
						//TotalInvoiceAmount += InvoiceAmount;
						
						
						TotalInvoiceAmount += OrderCC;
						
						// Cash Scenario
						double Cash = 0;
						double AdvanceCash = 0;
						double Credit = 0;
						double FreeStock = 0;
						
							// Get Order Posting Date
							Date OrderPostingDate = null;
							long OrderPostingID = 0;
							double BalanceBeforeOrderPosting = 0;//Distributor.getLedgerBalanceBeforeOrderPosting(DistributorID,OrderPostingID) * -1;
							
							ResultSet rs2 = s2.executeQuery("select id, created_on, current_balance from gl_order_posting where order_no = "+OrderNo);
							if (rs2.first()){
								OrderPostingDate = rs2.getDate(2);
								OrderPostingID = rs2.getLong(1);
								BalanceBeforeOrderPosting = rs2.getDouble(3);
							}
								
							
								double difference = BalanceBeforeOrderPosting - OrderAmount;
								
								if (difference > 0){
									Cash += OrderAmount;
									//AdvanceCash += OrderAmount;
								}else{
									Credit += (difference - BalanceBeforeOrderPosting) *-1;
								}
								
							
							//TotalCash += Cash;
							//TotalAdvanceCash += AdvanceCash;
							//TotalCredit += Credit;
							//TotalFreeStock += FreeStock;
							
							
							double CashPge =0;
							double AdvanceCashPge =0;
							double CreditPge =0;	
							
							if(OrderAmount!=0){
							 CashPge = (Cash/OrderAmount)*100;
							 AdvanceCashPge =  (AdvanceCash/OrderAmount)*100;
							 CreditPge =  (Credit/OrderAmount)*100;							
							}
							
							//System.out.println(CashPge+" - "+AdvanceCashPge+" - "+CreditPge);
							
							
							double CashPgeAmount=0;
							double AdvanceCashPgeAmount =  0;
							double CreditPgeAmount =  0;
							
							
								 CashPgeAmount = (CashPge/100)*(OrderCC-PromoConverted);
								 AdvanceCashPgeAmount =  (AdvanceCashPge/100)*(OrderCC-PromoConverted);
								 CreditPgeAmount =  (CreditPge/100)*(OrderCC-PromoConverted);
							
							
							TotalCash += CashPgeAmount;
							TotalAdvanceCash += AdvanceCashPgeAmount;
							TotalCredit += CreditPgeAmount;
							
							TotalFreeStock += FreeStockConverted;
							TotalPromoStock += PromoConverted;
							
					%>
					<tr>
						<td><%=Utilities.getDisplayDateFormat(OrderPostingDate) %></td>
						<td><%=OrderNo %></td>
						<td><%=Utilities.truncateStringToMax(DistributorID+"-"+DistributorName,30)%></td>
						<td style="text-align: right"><% if (BalanceBeforeOrderPosting!=0){out.print(Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(BalanceBeforeOrderPosting));} %></td>
						<td style="text-align: right"><% if (CashPgeAmount!=0){out.print(Utilities.getDisplayCurrencyFormat(CashPgeAmount));} %></td>
						<td style="text-align: right"><% if (AdvanceCashPgeAmount!=0){out.print(Utilities.getDisplayCurrencyFormat(AdvanceCashPgeAmount));} %></td>
						<td style="text-align: right"><% if (CreditPgeAmount!=0){out.print(Utilities.getDisplayCurrencyFormat(CreditPgeAmount));} %></td>
						<td style="text-align: right"><% if (PromoConverted!=0){out.print(Utilities.getDisplayCurrencyFormat(PromoConverted));} %></td>
						<td style="text-align: right"><% if (FreeStockConverted!=0){out.print(Utilities.getDisplayCurrencyFormat(FreeStockConverted));} %></td>
						<td style="text-align: right;"><%if(OrderCC!=0){%><%=Utilities.getDisplayCurrencyFormat(OrderCC) %><%} %></td>
					</tr>
					<%
					}
					%>
					<tr>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td style="text-align: right"><% if (TotalCash!=0){out.print(Utilities.getDisplayCurrencyFormat(TotalCash));} %></td>
						<td style="text-align: right"><% if (TotalAdvanceCash!=0){out.print(Utilities.getDisplayCurrencyFormat(TotalAdvanceCash));} %></td>
						<td style="text-align: right"><% if (TotalCredit!=0){out.print(Utilities.getDisplayCurrencyFormat(TotalCredit));} %></td>
						<td style="text-align: right;"><%if(TotalPromoStock!=0){%><%=Utilities.getDisplayCurrencyFormat(TotalPromoStock) %><%} %></td>
						<td style="text-align: right;"><%if(TotalFreeStock!=0){%><%=Utilities.getDisplayCurrencyFormat(TotalFreeStock) %><%} %></td>
						<td style="text-align: right;"><%if(TotalInvoiceAmount!=0){%><%=Utilities.getDisplayCurrencyFormat(TotalInvoiceAmount) %><%} %></td>
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