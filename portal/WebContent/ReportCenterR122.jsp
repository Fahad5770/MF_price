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
int FeatureID = 123;

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
					 	   	<th data-priority="1"  style="text-align:center; width: 7.7% ">Lifting Date</th>
					 	   	<th data-priority="1"  style="text-align:center; width: 7.7% ">Posting Date</th>
							<th data-priority="1"  style="text-align:center; width: 7.7% ">Order#</th>
							<th data-priority="1"  style="text-align:center; width: 7.7% ">Invoice#</th>
							<th data-priority="1"  style="text-align:center; width: 23% ">Distributor</th>	
							<th data-priority="1"  style="text-align:center; width: 7.7% ">Balance</th>
							<th data-priority="1"  style="text-align:center; width: 7.7% ">Cash</th>
							<th data-priority="1"  style="text-align:center; width: 7.7% ">Advance</th>
							<th data-priority="1"  style="text-align:center; width: 7.7% ">Credit</th>
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
					double TotalInvoiceAmount = 0;
					
					ResultSet rs = s.executeQuery("select created_on, sap_order_no, invoice_no, distributor_id, (select name from common_distributors where distributor_id = idn.distributor_id) distributor_name, invoice_amount from inventory_delivery_note idn where created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+" ");
					while(rs.next()){
						Date LiftingDate = rs.getDate("created_on");
						String OrderNo = rs.getString("sap_order_no");
						String InvoiceNo = rs.getString("invoice_no");
						long DistributorID = rs.getLong("distributor_id");
						String DistributorName = rs.getString("distributor_name");
						double InvoiceAmount = rs.getDouble("invoice_amount");
						
						TotalInvoiceAmount += InvoiceAmount;
						
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
								
							
								double difference = BalanceBeforeOrderPosting - InvoiceAmount;
								
								if (difference > 0){
									if (DateUtils.isSameDay(OrderPostingDate, LiftingDate)){
										Cash += InvoiceAmount;
									}else{
										AdvanceCash += InvoiceAmount;
									}
								}else{
									Credit += (difference - BalanceBeforeOrderPosting) *-1;
								}
								
							
							TotalCash += Cash;
							TotalAdvanceCash += AdvanceCash;
							TotalCredit += Credit;
							TotalFreeStock += FreeStock;
							
					%>
					<tr>
						<td><%=Utilities.getDisplayDateFormat(LiftingDate) %></td>
						<td><%=Utilities.getDisplayDateFormat(OrderPostingDate) %></td>
						<td><%=OrderNo %></td>
						<td><%=InvoiceNo %></td>
						<td><%=Utilities.truncateStringToMax(DistributorID+"-"+DistributorName,25)%></td>
						<td style="text-align: right"><% if (BalanceBeforeOrderPosting!=0){out.print(Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(BalanceBeforeOrderPosting));} %></td>
						<td style="text-align: right"><% if (Cash!=0){out.print(Utilities.getDisplayCurrencyFormat(Cash));} %></td>
						<td style="text-align: right"><% if (AdvanceCash!=0){out.print(Utilities.getDisplayCurrencyFormat(AdvanceCash));} %></td>
						<td style="text-align: right"><% if (Credit!=0){out.print(Utilities.getDisplayCurrencyFormat(Credit));} %></td>
						<td></td>
						<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(InvoiceAmount) %></td>
					</tr>
					<%
					}
					%>
					<tr>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td style="text-align: right"><% if (TotalCash!=0){out.print(Utilities.getDisplayCurrencyFormat(TotalCash));} %></td>
						<td style="text-align: right"><% if (TotalAdvanceCash!=0){out.print(Utilities.getDisplayCurrencyFormat(TotalAdvanceCash));} %></td>
						<td style="text-align: right"><% if (TotalCredit!=0){out.print(Utilities.getDisplayCurrencyFormat(TotalCredit));} %></td>
						<td></td>
						<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(TotalInvoiceAmount) %></td>
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