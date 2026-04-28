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
int FeatureID = 166;

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
	WhereSM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
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
	WhereTDM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
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
	WhereASM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
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
           long SelectedWarehouseArray[] = null;
           if (session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse") != null){
           	SelectedWarehouseArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse");
           	WarehouseIDs = Utilities.serializeForSQL(SelectedWarehouseArray);
           }
           String WhereWarehouse = "";
           if (WarehouseIDs.length() > 0){
           	WhereWarehouse = " and warehouse_id in ("+WarehouseIDs+") ";	
           }
%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					<%
					if(SelectedWarehouseArray != null && SelectedWarehouseArray.length==1){ //only one warehouse allowed
					
					%>
					 
					<tbody>
						<%
						long AccountID =0;
						int counter=0;
						String AccountTitle ="";
						double Balance=0;
						
						double OpeningBalance = 0;
						
						Date CurrentDate = new Date();
						
						//opening balance
						
						
					double TotalOpeningBalance = 0;
					double TotalDebitBalance = 0;
					double TotalCreditBalance = 0;
					double TotalBalance = 0;
						
					double ReportTotalDebit = 0;
					double ReportTotalCredit = 0;
						
						
						%>
						<thead>
					    
						
					    <tr style="font-size:11px;">							
							
							<th data-priority="1"  style="text-align:center;  width:20%">Description</th>				
							<th data-priority="1"  style="text-align:center;  width:20%">Opening Balance</th>
							<th data-priority="1"  style="text-align:center;  width:20%">Receipts</th>
							<th data-priority="1"  style="text-align:center;  width:20%">Payments</th>
							<th data-priority="1"  style="text-align:center;  width:20%">Balance</th>									
							
							
													
					    </tr>
					    
					  </thead> 
						
						<%
						if(WhereWarehouse !=""){
							ResultSet rs = s.executeQuery("SELECT gci.id,gci.label,gci.category_id,gci.capture_reference_no,gci.capture_date,ga.id as account_id, ga.type_id,ga.customer_id,ga.warehouse_id FROM gl_cash_instruments gci join gl_accounts ga on gci.category_id = ga.category_id where 1=1 "+WhereWarehouse);
							while(rs.next()){
								
								ResultSet rs3 = s3.executeQuery("select sum(glta.debit), sum(glta.credit) from gl_transactions_accounts glta join gl_transactions glt  on glta.id=glt.id where account_id="+rs.getLong("account_id")+" and created_on < "+Utilities.getSQLDateLifting(StartDate));
								if(rs3.first()){
									OpeningBalance = rs3.getDouble(1) - rs3.getDouble(2);
								}
								Balance = OpeningBalance;
								
								ResultSet rs1 = s2.executeQuery("select *,sum(glta.debit)  as total_debit, sum(glta.credit) as total_credit  from gl_transactions_accounts glta join gl_transactions glt  on glta.id=glt.id where account_id="+rs.getLong("account_id")+" and created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate));
								if(rs1.first()){
								Balance = (Balance + rs1.getDouble("total_debit")) - rs1.getDouble("total_credit");
								
								ReportTotalDebit = rs1.getDouble("total_debit");
								ReportTotalCredit = rs1.getDouble("total_credit");
								
								TotalOpeningBalance += OpeningBalance;
								TotalDebitBalance += rs1.getDouble("total_debit");
								TotalCreditBalance += rs1.getDouble("total_credit");
								TotalBalance += Balance;
								}
						%>
						
						
						<tr>										
							<td><%=rs.getString("label") %></td>
							<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccountingDr(OpeningBalance) %></td>			
							<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(ReportTotalDebit) %></td>
							<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(ReportTotalCredit) %></td>
							<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccountingDr(Balance) %></td>
							
								
						</tr>
						<%
								
							}
						}
						%>
						<tr>
						<th>Total</th>
						<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccountingDr(TotalOpeningBalance) %></td>
						<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(TotalDebitBalance) %></td>
						<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(TotalCreditBalance) %></td>
						<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccountingDr(TotalBalance) %></td>
						</tr>
						
					</tbody>
					<%
					}else{
					%>	
					Please select only one warehouse.
					<%
					}
					%>	
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