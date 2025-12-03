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
int FeatureID = 180;

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
           
           
          
%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 
					<tbody>
						
						<thead>
					    

					    <tr style="font-size:11px;">							
							<th style="width: 30%;">Customer</th>
							<th style="width: 10%;">Unallocated Advance</th>
							<th style="width: 10%;">Advance against Orders not lifted</th>
							<th style="width: 10%;">Total Advance</th>
							<th style="width: 10%;">Credit Limit</th>
							<th style="width: 10%;">Credit Limit Utilized</th>
							<th style="width: 10%;">One Time Credit</th>
							<th style="width: 10%;">Total Credit</th>
					    </tr>
					    
					  </thead> 
						
						<tbody>
						<%
						/*
						Datasource dssap = new Datasource();
						dssap.createConnectionToSAPDB();
						Statement ssap = dssap.createStatement();
						
						s.executeUpdate("create temporary table temp_invoices_list (transaction_date date, distributor_id bigint(11), distributor_name varchar(255),  invoice_number bigint(11), invoice_amount double )");	
						double TotalInvoiceAmount = 0;
						
						*/
						//String Query = "select vbrk.kurrf_dat /*Transaction Date*/, vbrk.fkdat /*Billing Date*/, vbrk.erdat, vbrk.aedat /*Changed On*/, vbrk.vbeln, vbrk.netwr+vbrk.mwsbk invoice_amount, vbrk.fkart, vbrk.kunag /*Customer*/, kna1.name1"+
						//		" from sapsr3.vbrk vbrk join sapsr3.vbuk vbuk on vbrk.vbeln = vbuk.vbeln join sapsr3.kna1 kna1 on vbrk.kunag = kna1.kunnr"+
						//		" where vbrk.kurrf_dat between '20120101' and "+Utilities.getSQLDateOracle(new Date())+" and vbrk.fksto != 'X' /* Invoice not Cancelled */ and vbrk.ZZCDS_STATUS != 'X' "+ 
						//		" "+WhereHOD+" and vbrk.fkart in ('ZDIS', 'ZMRS', 'ZCLA', 'ZSAM', 'ZCAN', 'ZTGT', 'ZOSM','ZDFR')"+ 
						//		" /* and kna1.CASSD != 'X' and kna1.FAKSD != 'X' and kna1.LIFSD != 'X' */ ";
						/*
						ResultSet rs0 = ssap.executeQuery(Query);
						while(rs0.next()){
							s.executeUpdate("insert into temp_invoices_list values("+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs0.getString("kurrf_dat")))+", "+rs0.getLong("kunag")+",'"+Utilities.filterString(rs0.getString("name1"), 1, 200)+"',"+rs0.getLong("vbeln")+","+rs0.getDouble("invoice_amount")+")");
						}
						
						dssap.dropConnection();
						*/
						
						double TotalCreadit=0;
						double CreditLimit=0;
						double OneTimeCreditBalance=0;
						
												
						double TotalTotalCredit=0;
						double TotalCreditLimit=0;
						double TotalOneTimeCreditBalance=0;
						double TotalLadgerBalance=0;
						
						double TotalUnliftedOrders = 0;
						
						double TotalLedgerDr = 0;
						double TotalLedgerCr = 0;
						double TotalTotalAdvance = 0;
						
						ResultSet rs = s.executeQuery("SELECT cd.distributor_id, cd.name, (select ifnull(sum(debit),0) - ifnull(sum(credit),0) from gl_transactions_accounts where account_id in (select id from gl_accounts where customer_id = cd.distributor_id and type_id = 1)) ledger_balance, (select ifnull(sum(debit),0) - ifnull(sum(credit),0) from gl_transactions_accounts where account_id in (select id from gl_accounts where customer_id = cd.distributor_id and type_id = 3)) one_time_credit_balance, ifnull((SELECT credit_limit FROM gl_customer_credit_limit where customer_id = cd.distributor_id and is_active = 1 and curdate() between valid_from and valid_to limit 1),0) credit_limit, ifnull((SELECT sum(gop.order_amount) FROM gl_order_posting gop where gop.customer_id = cd.distributor_id and gop.is_invoiced = 0 and gop.order_no not in (select sap_order_no from inventory_delivery_note)),0) unlifted_orders, ifnull((SELECT sum(gip.invoice_amount) FROM gl_invoice_posting gip where gip.customer_id = cd.distributor_id and gip.order_no not in (select sap_order_no from inventory_delivery_note)),0) unlifted_invoices FROM pep.common_distributors cd  where 1=1 "+WhereDistributors+WhereASM+WhereHOD+WhereRSM+WhereSM+WhereTDM+" having ledger_balance != 0 or one_time_credit_balance != 0 or credit_limit != 0 or unlifted_orders != 0 or unlifted_invoices != 0");
						while(rs.next()){
						
							CreditLimit = rs.getDouble("credit_limit");
							OneTimeCreditBalance = rs.getDouble("one_time_credit_balance");
							double LedgerBalance = rs.getDouble("ledger_balance");
							double LedgerDr = 0;
							double LedgerCr = 0;
							if (LedgerBalance < 0){
								LedgerCr = LedgerBalance*-1;
							}else{
								LedgerDr = LedgerBalance;
							}
							
							TotalLedgerDr += LedgerDr;
							TotalLedgerCr += LedgerCr;
							
							
							
							double UnliftedOrders = rs.getDouble("unlifted_orders") + rs.getDouble("unlifted_invoices");
							
							
							double TotalAdvance = LedgerCr + UnliftedOrders;
							
							double TotalCredit = LedgerDr + OneTimeCreditBalance;
							
							TotalTotalCredit += TotalCredit;
							
							TotalTotalAdvance += TotalAdvance;
							
							TotalUnliftedOrders += UnliftedOrders;
							

							TotalCreditLimit+=CreditLimit;
							TotalOneTimeCreditBalance+=OneTimeCreditBalance;
							
						%>
						<tr>
							<td ><%=rs.getLong("distributor_id") %> - <%=rs.getString("name") %></td>
							<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(LedgerCr) %></td>
							<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(UnliftedOrders) %></td>
							<td style="text-align:right;background-color:#F6F6F6;""><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(TotalAdvance) %></td>
							<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(CreditLimit) %></td>
							<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(LedgerDr) %></td>
							<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(OneTimeCreditBalance) %></td>
							<td style="text-align:right;background-color:#F6F6F6;""><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(TotalCredit) %></td>
						</tr>
						<%
						}
						%>
						
						<tr>
						<th>Total</th>
							<td style="text-align:right;background-color:#F6F6F6;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(TotalLedgerCr) %></td>
							<td style="text-align:right;background-color:#F6F6F6;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(TotalUnliftedOrders) %></td>
							<td style="text-align:right;background-color:#F6F6F6;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(TotalTotalAdvance) %></td>
							
							
							<td style="text-align:right;background-color:#F6F6F6;" ><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(TotalCreditLimit) %></td>
							<td style="text-align:right;background-color:#F6F6F6;" ><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(TotalLedgerDr) %></td>							
							<td style="text-align:right;background-color:#F6F6F6;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(TotalOneTimeCreditBalance) %></td>
							<td style="text-align:right;background-color:#F6F6F6;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(TotalTotalCredit) %></td>
						
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