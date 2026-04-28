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
<%@page import="java.io.*"%>


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



@media print
{

  body * {
    visibility: hidden;
  }
  #rptsum  ,#rptsum * {
    visibility: visible;
    font-size: 7px;
  }
  #rptsum    {
    position: absolute;
    left: 0;
    top: 0;
    font-size:  7px;
  }
  #print {
  visibility: hidden;
  
  }
 table { page-break-inside:auto; }
    tr    { page-break-inside:avoid; page-break-after:auto; }
    thead { display:table-header-group; }
    tfoot { display:table-footer-group; }

}


</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 298;





if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();



Datasource dsSAP = new Datasource();
dsSAP.createConnectionToSAPDB();
Connection cSAP = dsSAP.getConnection();
Statement sSAP = cSAP.createStatement();

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
	WhereHOD = " and isap.cache_distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and isap.cache_distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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
	WhereDistributors = " and isap.cache_distributor_id in ("+DistributorIDs+") ";
	
	
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
	WhereSM = " and isap.cache_distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
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
           String WhereCustomerID ="";
           String WhereDistributorID="";

           if (DistributorID1 != 0){
           	WhereCustomerID = " and customer_id in ("+DistributorID1+") ";
           	WhereDistributorID = " and distributor_id in ("+DistributorID1+") ";

           }




        
           
%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false" id="rptsum">
<li data-role="list-divider" data-theme="a"><div style ="float:left">Summary</div><div style="float:right"><a href="#" onclick="window.print()" id="print">Print</a></div><div style="clear: both;"></div></li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<br/>
			<table border=0 style="font-size:15px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
				<tr style="font-size:13px;">
					<th style="background-color:#fafaf8;">Distributor</th>
					<th style="background-color:#fafaf8;">Duration</th>
					<th style="background-color:#fafaf8;">First Lifting Date</th>
					<th style="background-color:#fafaf8;">Last Lifting Date</th>
					
					<th style="background-color:#fafaf8;">Number of Outlets</th>
					
				</tr>
				<%
				String Distributor="";
				String DistName="";
				String DistContact="";
				
				ResultSet rs34 = s.executeQuery("SELECT * FROM pep.common_distributors where distributor_id="+DistributorID1);
				if(rs34.first()){
					Distributor = rs34.getLong("distributor_id")+" - "+rs34.getString("name");
					DistName = rs34.getString("name2");
					DistContact = rs34.getString("contact_no");
				}
				
				long TotalOutlets=0;
				
				ResultSet rs35 = s.executeQuery("SELECT count(*) total_outlet FROM pep.common_outlets where cache_distributor_id="+DistributorID1);
				if(rs35.first()){
					TotalOutlets = rs35.getLong("total_outlet");
				}
				
				Date FirstLifting = new Date();
				Date LastLifting = new Date();
				
				ResultSet rs36 = s.executeQuery("SELECT min(created_on) first_lifting FROM pep.inventory_delivery_note where distributor_id="+DistributorID1);
				if(rs36.first()){
					FirstLifting = rs36.getDate("first_lifting");
				}
				
				ResultSet rs37 = s.executeQuery("SELECT max(created_on) last_lifting FROM pep.inventory_delivery_note where distributor_id="+DistributorID1);
				if(rs37.first()){
					LastLifting = rs37.getDate("last_lifting");
				}
				
				
				
				double CreditLimit=0;
				
				//Credit Limit
				
				ResultSet rs23 = s.executeQuery("SELECT cd.distributor_id, cd.name, (select ifnull(sum(debit),0) - ifnull(sum(credit),0) from gl_transactions_accounts where account_id in (select id from gl_accounts where customer_id = cd.distributor_id and type_id = 1)) ledger_balance, (select ifnull(sum(debit),0) - ifnull(sum(credit),0) from gl_transactions_accounts where account_id in (select id from gl_accounts where customer_id = cd.distributor_id and type_id = 3)) one_time_credit_balance, ifnull((SELECT credit_limit FROM gl_customer_credit_limit where customer_id = cd.distributor_id and is_active = 1 and curdate() between valid_from and valid_to limit 1),0) credit_limit, ifnull((SELECT sum(gop.order_amount) FROM gl_order_posting gop where gop.customer_id = cd.distributor_id and gop.is_invoiced = 0 and gop.order_no not in (select sap_order_no from inventory_delivery_note)),0) unlifted_orders, ifnull((SELECT sum(gip.invoice_amount) FROM gl_invoice_posting gip where gip.customer_id = cd.distributor_id and gip.order_no not in (select sap_order_no from inventory_delivery_note)),0) unlifted_invoices FROM pep.common_distributors cd  where 1=1 and cd.distributor_id="+DistributorID1+" having ledger_balance != 0 or one_time_credit_balance != 0 or credit_limit != 0 or unlifted_orders != 0 or unlifted_invoices != 0");
				while(rs23.next()){
				
					CreditLimit = rs23.getDouble("credit_limit");
				}

				
				%>
				
				
				<tr>
					<td style="background-color:#f7fff7; font-size:13px;"><%=Distributor %></td>
					<td style="background-color:#f7fff7; font-size:13px;"><%=Utilities.getDisplayDateFormat(StartDate) %> - <%=Utilities.getDisplayDateFormat(EndDate) %> </td>
					<td style="background-color:#f7fff7; font-size:13px;"><%=Utilities.getDisplayDateFormat(FirstLifting) %></td>
					<td style="background-color:#f7fff7;vfont-size:13px;"><%=Utilities.getDisplayDateFormat(LastLifting) %></td>
					
					<td style="background-color:#f7fff7;"><%=Utilities.getDisplayCurrencyFormat(TotalOutlets) %></td>
					
				</tr>
			</table>
			<br/>
			
			
			
			<table border=0 style="font-size:15px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
				<thead>
					  
					  
					  </thead> 
						<tbody>
						
						
						<%
						long AccountID =0;
						int counter=0;
						
						double Balance=0;
						
						
						//System.out.println("select * from gl_accounts where 1=1 and type_id in (1) "+WhereCustomerID);
						
						ResultSet rs = s.executeQuery("select * from gl_accounts where 1=1 and type_id in (1) "+WhereCustomerID);
						//System.out.println("select * from gl_accounts where 1=1 "+WhereSalesType+" and customer_id="+CustomerID);
						if(rs.first()){
							AccountID = rs.getLong("id");
							
						}
						
						double OpeningBalanceLedger = 0;
						//opening balance
						
						//System.out.println("Select sum(debit), sum(credit) FROM gl_transactions_accounts glta join gl_transactions glt  on glta.id=glt.id where account_id="+AccountID+" and created_on_date < "+Utilities.getSQLDate(StartDate));
						
						ResultSet rs2 = s.executeQuery("Select sum(debit), sum(credit) FROM gl_transactions_accounts glta join gl_transactions glt  on glta.id=glt.id where account_id="+AccountID+" and created_on_date <= "+Utilities.getSQLDate(EndDate));
						if(rs2.first()){
							OpeningBalanceLedger = rs2.getDouble(1) - rs2.getDouble(2);
						}
						
						//Credit
							
							ResultSet rs1 = s.executeQuery("select * from gl_accounts where 1=1 and type_id in (3) "+WhereCustomerID);
							//System.out.println("select * from gl_accounts where 1=1 "+WhereSalesType+" and customer_id="+CustomerID);
							if(rs1.first()){
								AccountID = rs1.getLong("id");
								
							}
							
							double OpeningBalanceCredit = 0;
							//opening balance
							
							//System.out.println("Select sum(debit), sum(credit) FROM gl_transactions_accounts glta join gl_transactions glt  on glta.id=glt.id where account_id="+AccountID+" and created_on_date < "+Utilities.getSQLDate(StartDate));
							
							ResultSet rs21 = s.executeQuery("Select sum(debit), sum(credit) FROM gl_transactions_accounts glta join gl_transactions glt  on glta.id=glt.id where account_id="+AccountID+" and created_on_date <= "+Utilities.getSQLDate(EndDate));
							if(rs21.first()){
								OpeningBalanceCredit = rs21.getDouble(1) - rs21.getDouble(2);
							}
							
							
							//Advance
							
							ResultSet rs12 = s.executeQuery("select * from gl_accounts where 1=1 and type_id in (6) "+WhereCustomerID);
							//System.out.println("select * from gl_accounts where 1=1 "+WhereSalesType+" and customer_id="+CustomerID);
							if(rs12.first()){
								AccountID = rs12.getLong("id");
								
							}
							
							double OpeningBalanceAdvance = 0;
							//opening balance
							
							//System.out.println("Select sum(debit), sum(credit) FROM gl_transactions_accounts glta join gl_transactions glt  on glta.id=glt.id where account_id="+AccountID+" and created_on_date < "+Utilities.getSQLDate(StartDate));
							
							ResultSet rs211 = s.executeQuery("Select sum(debit), sum(credit) FROM gl_transactions_accounts glta join gl_transactions glt  on glta.id=glt.id where account_id="+AccountID+" and created_on_date <= "+Utilities.getSQLDate(EndDate));
							if(rs211.first()){
								OpeningBalanceAdvance = rs211.getDouble(1) - rs211.getDouble(2);
							}
							
							
						
						
						
						
						Balance = OpeningBalanceLedger;
						
						
						
						%>
						
						
						<tr >
							<th colspan="" style="font-size:13px; background-color:#f6f6f6;">Financials</th>
							<th style="font-size:13px; width:156px; background-color:#f6f6f6; text-align:center;">Amount</th>
						</tr>
						<tr >
							<th colspan="2" style="font-size:13px; padding-left:10px;">Accounts Receivable</th>
						</tr>  
						<tr>						
							<td style="padding-left:15px;font-size:13px;">Ledger Balance</td>
							<td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(OpeningBalanceLedger) %></td>
						</tr>
						<tr>						
							<td style="padding-left:15px;font-size:13px;">Credit Balance</td>
							<td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(OpeningBalanceCredit) %></td>
						</tr>
						<tr>						
							<td style="padding-left:15px;font-size:13px;">Advance Balance</td>
							<td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(OpeningBalanceAdvance) %></td>
						</tr>
						<%
						com.pbc.sap.SAPUtilities obj = new com.pbc.sap.SAPUtilities();
						obj.connectPRD();
						
						
						double INC_BAL = 0;
						double ATAX_BAL = 0;
						double CRSAL_BAL = 0;
						double VEH_BAL = 0;
						
						try {
							
							Date FromDate = StartDate;//Utilities.parseDate("01/01/2017");
							Date ToDate = EndDate;
							
							com.sap.conn.jco.JCoTable tab = obj.getDistributorStatement(DistributorID1, FromDate, ToDate);
							
							//System.out.println("Synchronization started for Distributor Statement");
							
							tab.firstRow();
							
							int countInserts = 0;
							int countUpdates = 0;
							
							for(int i = 0; i <= tab.getNumRows(); i++){
								
								//String DistributorID = Utilities.filterString(tab.getString("KUNNR"), 1, 100);
								
								//String Name = Utilities.filterString(tab.getString("NAME1"), 1, 200);
								
								//INC_BAL
								//ATAX_BAL
								//CRSAL_BAL
								//VEH_BAL
								
								INC_BAL = tab.getDouble("INC_BAL");
								ATAX_BAL = tab.getDouble("ATAX_BAL");
								CRSAL_BAL = tab.getDouble("CRSAL_BAL");
								VEH_BAL = tab.getDouble("VEH_BAL");
								
								
								tab.setRow(i+1);
								
							}
							
							//System.out.println(countInserts+" Inserts, "+countUpdates + "Updates");
							//System.out.println("Done!");
							obj.dropConnection();
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}						
						%>
						
						<tr>						
							<td style="padding-left:15px; color: grey;font-size:13px;">Vehicle Account</td>
							<td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(VEH_BAL) %></td>
						</tr>
						<tr>						
							<td style="padding-left:15px; color: grey;font-size:13px;">Account Tax Balance</td>
							<td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(ATAX_BAL) %></td>
						</tr>
						<tr>						
							<td style="padding-left:15px;font-size:13px; color: grey;">PSR Salary Account</td>
							<td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(CRSAL_BAL) %></td>
						</tr>
						<tr>						
							<td style="padding-left:15px;font-size:13px; color: grey;">Incentive Account</td>
							<td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(INC_BAL) %></td>
						</tr>
						
						<%
						double NetBalanceRec=OpeningBalanceLedger+OpeningBalanceCredit+OpeningBalanceAdvance+VEH_BAL+ATAX_BAL+CRSAL_BAL+(INC_BAL);
						
						%>
						
						
						<tr>						
							<td style="padding-left:15px;background-color:#ffffe6;font-size:13px;">Net Balance</td>
							<td style="text-align:right;background-color:#ffffe6;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(NetBalanceRec) %></td>
						</tr>
						
						
						<%
						long Balance17=0;
						long Balance12=0;
						long Balance11=0;
						
						//out.println("SELECT *,(Select sum(total_units_received)-sum(total_units_issued) balance11  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 11 and ect.created_on_date <= "+Utilities.getSQLDate(EndDate)+" and ect.type_id in(1,5,2,8,9,10,11,12) and ect.distributor_id=cd.distributor_id order by ip.package_id desc) balance11,(Select sum(total_units_received)-sum(total_units_issued) balance11  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 12 and ect.created_on_date <= "+Utilities.getSQLDate(EndDate)+" and ect.type_id in(1,5,2,8,9,10,11,12) and ect.distributor_id=cd.distributor_id order by ip.package_id desc) balance12 ,(Select sum(total_units_received)-sum(total_units_issued) balance11  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 17 and ect.created_on_date <= "+Utilities.getSQLDate(EndDate)+" and ect.type_id in(1,5,2,8,9,10,11,12) and ect.distributor_id=cd.distributor_id order by ip.package_id desc) balance17 FROM common_distributors cd where cd.distributor_id in(select distinct distributor_id from ec_transactions)  and distributor_id="+DistributorID1+" and distributor_id in (SELECT distributor_id FROM common_distributors where region_id in (1,2,3,4,5,6,7,8,9,10,11)) ");
						
						ResultSet rs111 = s.executeQuery("SELECT *,(Select sum(total_units_received)-sum(total_units_issued) FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 11 and ect.created_on_date <= "+Utilities.getSQLDate(EndDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id=cd.distributor_id) balance11,(Select sum(total_units_received)-sum(total_units_issued) FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 12 and ect.created_on_date <= "+Utilities.getSQLDate(EndDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id=cd.distributor_id) balance12 ,(Select sum(total_units_received)-sum(total_units_issued)  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 17 and ect.created_on_date <= "+Utilities.getSQLDate(EndDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id=cd.distributor_id) balance17 FROM common_distributors cd where distributor_id="+DistributorID1+"");
						while(rs111.next()){
							
							Balance17=rs111.getLong("balance17");
							Balance12=rs111.getLong("balance12")/24;
							Balance11=rs111.getLong("balance11")/24;
							
							
						}
						
						
						double EmptyCrdit17 = 0;
						double EmptyCrdit12 = 0;
						double EmptyCrdit11 = 0;
						
						if(Balance17<0){ //
							EmptyCrdit17 = Balance17*(-1)*440;
						}
						
						if(Balance12<0){ //
							EmptyCrdit12 = Balance12*(-1)*340;
						}
						
						if(Balance11<0){ //
							EmptyCrdit11 = Balance11*(-1)*340;
						}
						
						
						double EmptyBalance17 = 0;
						double EmptyBalance12 = 0;
						double EmptyBalance11 = 0;
						
						if(Balance17>0){ //
							EmptyBalance17 = Balance17*(-1)*180;
						}
						
						if(Balance12>0){ //
							EmptyBalance12 = Balance12*(-1)*170;
						}
						
						if(Balance11>0){ //
							EmptyBalance11 = Balance11*(-1)*170;
						}
						
						
						%>
						
						<%
						//SAP Query
						
						double SAPVehicle=0;
						double SAPEmpty=0;
						double SAPVehNdEmptyTotal=0;
						
						//System.out.println("select sum(vbrp.KZWI2) vehicle_deduction, sum(vbrp.KZWI3) empty_deduction "+
							//	" from sapsr3.vbrk vbrk join sapsr3.vbuk vbuk on vbrk.vbeln = vbuk.vbeln join sapsr3.vbrp vbrp on vbrk.vbeln = vbrp.vbeln where vbrk.kurrf_dat between "+Utilities.getSQLDateOracle(Utilities.getYearStartDateByDate(StartDate))+" and "+Utilities.getSQLDateOracle(EndDate)+" and vbrk.fksto != 'X' and vbrk.fkart in ('ZDIS','ZMRS','ZDFR') and vbuk.buchk != 'C' and vbrk.kunag = '"+Utilities.addLeadingZeros(DistributorID1+"",10)+"'");
						
						
							 
							 FileWriter fw = null; 
							 BufferedWriter bw = null; 
							 PrintWriter pw = null;

							
							 fw = new FileWriter("D:\\PBC\\Deduction\\101006.txt", true); 
							 bw = new BufferedWriter(fw); 
							 pw = new PrintWriter(bw);
						
						ResultSet rsSAP = sSAP.executeQuery("select vbrp.KZWI2 vehicle_deduction, vbrp.KZWI3 empty_deduction, vbrk.vbeln "+
								" from sapsr3.vbrk vbrk join sapsr3.vbuk vbuk on vbrk.vbeln = vbuk.vbeln join sapsr3.vbrp vbrp on vbrk.vbeln = vbrp.vbeln where vbrk.kurrf_dat between "+Utilities.getSQLDateOracle(StartDate)+" and "+Utilities.getSQLDateOracle(EndDate)+" and vbrk.fksto != 'X' and vbrk.fkart in ('ZDIS','ZMRS','ZDFR','ZCLA') /*and vbuk.buchk != 'C'*/ and vbrk.kunag = '"+Utilities.addLeadingZeros(DistributorID1+"",10)+"'");
						
						while(rsSAP.next()){
							SAPVehicle = rsSAP.getDouble("vehicle_deduction");
							SAPEmpty = rsSAP.getDouble("empty_deduction");
							
							System.out.println("Invoice# : "+ rsSAP.getLong("vbeln"));
							System.out.println("Vehicle : "+SAPVehicle*-1);
							System.out.println("Empty : "+SAPEmpty*-1);
							System.out.println("\n");
							System.out.println(" ");
							
							
							 
							 //File appending
							 
							
							 
							 pw.println(rsSAP.getLong("vbeln")+","+(SAPVehicle*-1)+","+(SAPEmpty*-1)); 
							// pw.println("Vehicle : "+(SAPVehicle*-1)+"\t"); 
							// pw.println("Empty : "+(SAPEmpty*-1)+"\t"); 
							 //pw.println("\n");
							// pw.println("\n");
							


							 
							 
							 
							 
							 
							// objWriter.flush();
							 //objWriter.close();
							
							
							
						}
						
						
						 pw.close();
			             bw.close();
			             fw.close();
						
						SAPVehicle = SAPVehicle*-1;
						SAPEmpty = SAPEmpty*-1;
						
						
						
						
						
						
						SAPVehNdEmptyTotal = SAPVehicle+SAPEmpty;
						%>
						
						
						<tr>
							<th colspan="2" style="font-size:13px; padding-left:10px;">Deductions and Margins</th>
						</tr>
						<tr>						
							<td style="padding-left:15px;font-size:13px;">Vehicle</td>
							<td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(SAPVehicle)%></td>
						</tr>
						<tr>						
							<td style="padding-left:15px;font-size:13px;">Empty</td>
							<td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(SAPEmpty)%></td>
						</tr>
						<tr>						
							<td style="padding-left:15px;background-color:#ffffe6;font-size:13px;">Total</td>
							<td style="text-align:right;background-color:#ffffe6;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(SAPVehNdEmptyTotal)%></td>
						</tr>
						
						<tr>
							<td colspan="2">
								<table style="width:100%;" cellpadding="0" cellspacing="0">
									
									<tr>
										<th style="font-size:13px;">Empty</th>
										<th style="font-size:13px; text-align:center; width:150px;">Quantity</th>
										<th style="font-size:13px; text-align:center; width:150px;">Rate</th>
										<th style="font-size:13px; text-align:center; width:150px;">Total</th>
									
									</tr>
									<tr>
										<th colspan="4" style="font-size:15px; padding-left:15px;">Empty Credit</th>
									</tr>
									
									<tr>						
										<td style="padding-left:20px;font-size:13px;">250 ML SHELL</td>
										<td style="text-align:right;font-size:13px;"><%if(EmptyCrdit17!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance17*-1) %><%} %></td>
										<td style="text-align:right;font-size:13px;"><%if(EmptyCrdit17!=0){%>440<%} %></td>
										<td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(EmptyCrdit17) %></td>
									</tr>
									<tr>						
										<td style="padding-left:20px;font-size:13px;">240 ML SSRB</td>
										<td style="text-align:right;font-size:13px;"><%if(EmptyCrdit12!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance12*-1) %><%} %></td>
										<td style="text-align:right;font-size:13px;"><%if(EmptyCrdit12!=0){%>340<%} %></td>
										<td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(EmptyCrdit12) %></td>
									</tr>
									<tr>						
										<td style="padding-left:20px;font-size:13px;">250 ML STD</td>
										<td style="text-align:right;font-size:13px;"><%if(EmptyCrdit11!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance11*-1) %><%} %></td>
										<td style="text-align:right;font-size:13px;"><%if(EmptyCrdit11!=0){%>340<%} %></td>
										<td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(EmptyCrdit11) %></td>
									</tr>
									
									
									
									
									
									
									<%
									
									long Balance1=0;
									long Balance1N=0;
									int UnitPerSKU1=0;
									
									ResultSet rs51 = s3.executeQuery("SELECT distinct ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_label ,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3,4) and package_id not in (10) and package_id =1 and ip.is_other_brand !=1  order by package_id desc");
									while(rs51.next()){
									
										 Balance1=0;
										 
										
										long BrandID = rs51.getLong("brand_id");
										
										String BrandIDModify = "";
										
										if(BrandID==1 || BrandID==37 || BrandID==38){ //pepsi
											BrandIDModify += "1,24,28,37,38"; //pepsi+coke+gourm cola
										}else if(BrandID==2){ //merinda 
											BrandIDModify += "2,25"; //mirinda+fanta
										}else if(BrandID==4){ //7up
											BrandIDModify += "4,26,29"; //7up+sprite+gourm lemon
										}else if(BrandID==5){ //Dew
											BrandIDModify += "5,27"; //7up+3g
										}else{
											BrandIDModify = rs51.getLong("brand_id")+"";
										}
										
										
										
										ResultSet rs13 = s.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 1  and ip.brand_id in ("+BrandIDModify+") and ect.created_on_date <= "+Utilities.getSQLDate(EndDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id="+DistributorID1+" order by ip.package_id desc");
										if(rs13.first()){
											 Balance1 = rs13.getLong(1) - rs13.getLong(2);
											 UnitPerSKU1 = rs13.getInt(3);
											 
											 
										}
										if(UnitPerSKU1!=0){
											Balance1N += Balance1/UnitPerSKU1;
										}
									}
									
									
									
									
									long Balance18=0;
									long Balance18N=0;
									int UnitPerSKU18=0;
									
									ResultSet rs512 = s3.executeQuery("SELECT distinct ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_label ,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3,4) and package_id not in (10) and package_id =18 and ip.is_other_brand !=1 "+WherePackage+" order by package_id desc");
									while(rs512.next()){
									
											 Balance18=0;
											 
											
											long BrandID = rs512.getLong("brand_id");
											
											String BrandIDModify = "";
											
											if(BrandID==1 || BrandID==37 || BrandID==38){ //pepsi
												BrandIDModify += "1,24,28,37,38"; //pepsi+coke+gourm cola
											}else if(BrandID==2){ //merinda 
												BrandIDModify += "2,25"; //mirinda+fanta
											}else if(BrandID==4){ //7up
												BrandIDModify += "4,26,29"; //7up+sprite+gourm lemon
											}else if(BrandID==5){ //Dew
												BrandIDModify += "5,27"; //7up+3g
											}else{
												BrandIDModify = rs512.getLong("brand_id")+"";
											}
										
										
										
										ResultSet rs18 = s.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 18  and ip.brand_id in ("+BrandIDModify+") and ect.created_on_date <= "+Utilities.getSQLDate(EndDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id="+DistributorID1+" order by ip.package_id desc");
										if(rs18.first()){
											 Balance18 = rs18.getLong(1) - rs18.getLong(2);
											 UnitPerSKU18 = rs18.getInt(3);
											 
											 
										}
										
										if(UnitPerSKU18!=0){
											Balance18N += Balance18/UnitPerSKU18;
										}
									}
									
									
									
									long Balance26=0;
									long Balance26N=0;
									int UnitPerSKU26=0;
									ResultSet rs5121 = s3.executeQuery("SELECT distinct ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_label ,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3,4) and package_id not in (10) and package_id =26 and ip.is_other_brand !=1 "+WherePackage+" order by package_id desc");
									while(rs5121.next()){
									
											 Balance26=0;
											 
											
											long BrandID = rs5121.getLong("brand_id");
											
											String BrandIDModify = "";
											
											if(BrandID==1 || BrandID==37 || BrandID==38){ //pepsi
												BrandIDModify += "1,24,28,37,38"; //pepsi+coke+gourm cola
											}else if(BrandID==2){ //merinda 
												BrandIDModify += "2,25"; //mirinda+fanta
											}else if(BrandID==4){ //7up
												BrandIDModify += "4,26,29"; //7up+sprite+gourm lemon
											}else if(BrandID==5){ //Dew
												BrandIDModify += "5,27"; //7up+3g
											}else{
												BrandIDModify = rs5121.getLong("brand_id")+"";
											}
									
									
											
											
											ResultSet rs26= s.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 26  and ip.brand_id in ("+BrandIDModify+") and  ect.created_on_date <= "+Utilities.getSQLDate(EndDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id="+DistributorID1+" order by ip.package_id desc");
											if(rs26.first()){
												 Balance26 = rs26.getLong(1) - rs26.getLong(2);
												 UnitPerSKU26 = rs26.getInt(3);
												 
												
											}
											
												Balance26N += Balance26;
											
										if(UnitPerSKU26!=0){
											Balance26N=Balance26N/UnitPerSKU26;
										}
									}
									
									
									
									long Balance25=0;
									long Balance25N=0;
									int UnitPerSKU25=0;
									
									ResultSet rs51213 = s3.executeQuery("SELECT distinct ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_label ,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3,4) and package_id not in (10) and package_id =25 and ip.is_other_brand !=1 "+WherePackage+" order by package_id desc");
									while(rs51213.next()){
									
											 Balance25=0;
											 
											
											long BrandID = rs51213.getLong("brand_id");
											
											String BrandIDModify = "";
											
											if(BrandID==1 || BrandID==37 || BrandID==38){ //pepsi
												BrandIDModify += "1,24,28,37,38"; //pepsi+coke+gourm cola
											}else if(BrandID==2){ //merinda 
												BrandIDModify += "2,25"; //mirinda+fanta
											}else if(BrandID==4){ //7up
												BrandIDModify += "4,26,29"; //7up+sprite+gourm lemon
											}else if(BrandID==5){ //Dew
												BrandIDModify += "5,27"; //7up+3g
											}else{
												BrandIDModify = rs51213.getLong("brand_id")+"";
											}
												
												//out.println("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 25  and ip.brand_id in ("+BrandIDModify+") and ect.created_on_date <= "+Utilities.getSQLDate(EndDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id="+DistributorID1+" order by ip.package_id desc");
												ResultSet rs25 = s.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 25  and ip.brand_id in ("+BrandIDModify+") and ect.created_on_date <= "+Utilities.getSQLDate(EndDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id="+DistributorID1+" order by ip.package_id desc");
												if(rs25.first()){
													 Balance25 = rs25.getLong(1) - rs25.getLong(2);
													 UnitPerSKU25 = rs25.getInt(3);
													 
													 
												}
												
												if(UnitPerSKU25!=0){
													Balance25N += Balance25/UnitPerSKU25;
												}
									}
									
									
									long Balance13=0;
									long Balance13N=0;
									int UnitPerSKU13=0;
									
									ResultSet rs51212 = s3.executeQuery("SELECT distinct ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_label ,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3,4) and package_id not in (10) and package_id =13 and ip.is_other_brand !=1 "+WherePackage+" order by package_id desc");
									while(rs51212.next()){
									
											 Balance13=0;
											 
											
											long BrandID = rs51212.getLong("brand_id");
											
											String BrandIDModify = "";
											
											if(BrandID==1 || BrandID==37 || BrandID==38){ //pepsi
												BrandIDModify += "1,24,28,37,38"; //pepsi+coke+gourm cola
											}else if(BrandID==2){ //merinda 
												BrandIDModify += "2,25"; //mirinda+fanta
											}else if(BrandID==4){ //7up
												BrandIDModify += "4,26,29"; //7up+sprite+gourm lemon
											}else if(BrandID==5){ //Dew
												BrandIDModify += "5,27"; //7up+3g
											}else{
												BrandIDModify = rs51212.getLong("brand_id")+"";
											}
									
													
													//out.println("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 13  and ip.brand_id in ("+BrandIDModify+") and ect.created_on_date <= "+Utilities.getSQLDate(EndDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id="+DistributorID1+" order by ip.package_id desc");
													ResultSet rs133 = s.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 13  and ip.brand_id in ("+BrandIDModify+") and ect.created_on_date <= "+Utilities.getSQLDate(EndDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id="+DistributorID1+" order by ip.package_id desc");
													if(rs133.first()){
														 Balance13 = rs133.getLong(1) - rs133.getLong(2);
														 UnitPerSKU13 = rs133.getInt(3);
														 
														 
													}
													if(UnitPerSKU13!=0){
														Balance13N += Balance13/UnitPerSKU13;
													}
									}
													
									
									
									
									double Balance1RT=0;
									double Balance18RT=0;
									double Balance26RT=0;
									double Balance25RT=0;
									double Balance13RT=0;
									
									
									int ShowedBalance1N=0;
									int ShowedBalance18N=0;
									
									
									if(Balance1N<0){
										ShowedBalance1N=340;
										Balance1RT = Balance1N*340*(-1);
									}else{
										ShowedBalance1N=170;
										Balance1RT = Balance1N*170*(-1);
									}
									
									if(Balance18N<0){ //
										ShowedBalance18N=340;
										Balance18RT = Balance18N*340*(-1);
									}else{
										ShowedBalance18N=180;
										Balance18RT = Balance18N*180*(-1);
									}
									
									
									//if(Balance26>0){ //
										Balance26RT = Balance26N*895*(-1);
									//}
									
									
									//if(Balance25>0){ //
										Balance25RT = Balance25N*6700*(-1);
									//}
									
									
									//if(Balance13>0){ //
										Balance13RT = Balance13N*365*(-1);
									//}
									
									
									
									
									///////Wooden Pallet
									////////////////////////////////////////////////////////
									
									
									long Balance31=0;
									long Balance31N=0;
									int UnitPerSKU31=0;
									
									
									long Balance3145=0;
									long Balance3145N=0;
									int UnitPerSKU3145=0;
									
									
									
											 Balance31=0;
											 
											
											
								//Wooden Pallet (O/W)  package=31 & brand=44
													
													//out.println("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 13  and ip.brand_id in ("+BrandIDModify+") and ect.created_on_date <= "+Utilities.getSQLDate(EndDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id="+DistributorID1+" order by ip.package_id desc");
													ResultSet rs133 = s.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 31  and ip.brand_id in (44) and ect.created_on_date <= "+Utilities.getSQLDate(EndDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id="+DistributorID1+" order by ip.package_id desc");
													if(rs133.first()){
														 Balance31 = rs133.getLong(1) - rs133.getLong(2);
														 UnitPerSKU31 = rs133.getInt(3);
														 
														 
													}
													if(UnitPerSKU31!=0){
														Balance31N += Balance31/UnitPerSKU31;
													}
													
													//for TW
													
													 Balance3145=0;
													
													ResultSet rs133t = s.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 31  and ip.brand_id in (45) and ect.created_on_date <= "+Utilities.getSQLDate(EndDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id="+DistributorID1+" order by ip.package_id desc");
													if(rs133t.first()){
														 Balance3145 = rs133t.getLong(1) - rs133t.getLong(2);
														 UnitPerSKU3145 = rs133t.getInt(3);
														 
														 
													}
													if(UnitPerSKU3145!=0){
														Balance3145N += Balance3145/UnitPerSKU3145;
													}
									
													
									
									
									
													double Balance31RT=0;
													double Balance3145RT=0;
													int Rate31RT=2100;
													int Rate3145RT=2200;
													
													
													//if(Balance1>0){ //
														Balance31RT = Balance31N*Rate31RT*(-1);
													//}
													
													//if(Balance18>0){ //
														Balance3145RT = Balance3145N*Rate3145RT*(-1);
													//}
													
													
													
									
									
									
									
									
									
									
									///////////////////////////////////////////////////
									///////////////////////////////////////////////////
									
									%>
									
									<tr>						
										<td style="padding-left:20px;font-size:13px;">1000ML Glass</td> <!-- 1 -->
										<td style="text-align:right;font-size:13px;"><%if(Balance1RT!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance1N*-1) %><%} %></td>
										<td style="text-align:right;font-size:13px;"><%if(Balance1RT!=0){%><%=ShowedBalance1N%><%} %></td>
										<td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance1RT) %></td>
									</tr>
									
									<tr>						
										<td style="padding-left:20px;font-size:13px;">1000ML Shell</td> <!-- 18 -->
										<td style="text-align:right;font-size:13px;"><%if(Balance18RT!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance18N*-1) %><%} %></td>
										<td style="text-align:right;font-size:13px;"><%if(Balance18RT!=0){%><%=ShowedBalance18N%><%} %></td>
										<td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance18RT) %></td>
									</tr>
									
									<tr>						
										<td style="padding-left:20px;font-size:13px;">Plastic Sheet</td> <!-- 26 -->
										<td style="text-align:right;font-size:13px;"><%if(Balance26RT!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance26N*-1) %><%} %></td>
										<td style="text-align:right;font-size:13px;"><%if(Balance26RT!=0){%>895<%} %></td>
										<td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance26RT) %></td>
									</tr>
									
									<tr>						
										<td style="padding-left:20px;font-size:13px;">Plastic Pallets</td> <!-- 25 -->
										<td style="text-align:right;font-size:13px;"><%if(Balance25RT!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance25N*-1) %><%} %></td>
										<td style="text-align:right;font-size:13px;"><%if(Balance25RT!=0){%>6700<%} %></td>
										<td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance25RT) %></td>
									</tr>
									
									
									<tr>						
										<td style="padding-left:20px;font-size:13px;">Bulk Empty</td> <!-- 13 -->
										<td style="text-align:right;font-size:13px;"><%if(Balance13RT!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance13N*-1) %><%} %></td>
										<td style="text-align:right;font-size:13px;"><%if(Balance13RT!=0){%>365<%} %></td>
										<td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance13RT) %></td>
									</tr>
									
									
									
									
									<tr>						
										<td style="padding-left:20px;font-size:13px;">Wooden Pallet (O/W)</td> <!-- 13 -->
										<td style="text-align:right;font-size:13px;"><%if(Balance31RT!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance31N*-1) %><%} %></td>
										<td style="text-align:right;font-size:13px;"><%if(Balance31RT!=0){%><%=Rate31RT%><%} %></td>
										<td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance31RT) %></td>
									</tr>
									
									
									<tr>						
										<td style="padding-left:20px;font-size:13px;">Wooden Pallet (T/W)</td> <!-- 13 -->
										<td style="text-align:right;font-size:13px;"><%if(Balance3145RT!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance3145N*-1) %><%} %></td>
										<td style="text-align:right;font-size:13px;"><%if(Balance3145RT!=0){%><%=Rate3145RT%><%} %></td>
										<td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance3145RT) %></td>
									</tr>
									
									
									<tr>
										<th colspan="4" style="font-size:13px; padding-left:15px;">Empty Balance</th>
									</tr>
									<tr>						
										<td style="padding-left:20px;font-size:13px;">250 ML SHELL</td>
										<td style="text-align:right;font-size:13px;"><%if(EmptyBalance17!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance17*-1) %><%} %></td>
										<td style="text-align:right;font-size:13px;"><%if(EmptyBalance17!=0){%>180<%} %></td>
										<td style="text-align:right;font-size:13px;"><%if(EmptyBalance17!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(EmptyBalance17) %><%} %></td>
									</tr>
									<tr>						
										<td style="padding-left:20px;font-size:13px;">240 ML SSRB</td>
										<td style="text-align:right;font-size:13px;"><%if(EmptyBalance12!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance12*-1) %><%} %></td>
										<td style="text-align:right;font-size:13px;"><%if(EmptyBalance12!=0){%>170<%} %></td>
										<td style="text-align:right;font-size:13px;"><%if(EmptyBalance12!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(EmptyBalance12) %><%} %></td>
									</tr>
									<tr>						
										<td style="padding-left:20px;font-size:13px;">250 ML STD</td>
										<td style="text-align:right;font-size:13px;"><%if(EmptyBalance11!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance11*-1) %><%} %></td>
										<td style="text-align:right;font-size:13px;"><%if(EmptyBalance11!=0){%>170<%} %></td>
										<td style="text-align:right;font-size:13px;"><%if(EmptyBalance11!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(EmptyBalance11) %><%} %></td>
									</tr>
									
									
																<%-- Nasir's Code --%> 
							                  <tr>
							          <th colspan="4" style="font-size:13px; padding-left:15px;">Leakage Allowance</th>
							         </tr>
							     <%
							      long cases=0;
							      String pack_name="";
							      ResultSet rsCases=s.executeQuery(" SELECT (select label from inventory_packages ip where ip.id=tela.package_id) package_name,tela.cases FROM pep.temp_empty_leakage_allowance tela where 1=1 "+WhereDistributorID +" and date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));
							       while(rsCases.next())
							       {
							        cases=rsCases.getLong("tela.cases");
							        pack_name=rsCases.getString("package_name");
							     %>
							         <tr>      
							          <td style="padding-left:20px;font-size:13px;"><%=pack_name%></td>
							          <td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(cases)%></td>
							          <td style="text-align:right;font-size:13px;">0</td>
							          <td style="text-align:right;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(cases*0) %></td>
							         </tr>
							     <%
							       }
							       if(pack_name=="")
							       { %>
							        <tr>      
							               <td style="padding-left:20px;"></td>
							               <td style="padding-left:20px;"></td>
							               <td style="padding-left:20px;"></td>
							         <td style="padding-left:20px;"></td>
							           </tr>
							      <% }
							     %> 
							 <%-- Nasir's Code Ends here--%>
									
									
									
								
								</table>
								
							</td>
						
						</tr>
						
						
						
						<%
						double EmptyBalance=EmptyCrdit17+EmptyCrdit12+EmptyCrdit11+EmptyBalance17+EmptyBalance12+EmptyBalance11+Balance1RT+Balance18RT+Balance26RT+Balance25RT+Balance13RT+Balance31RT+Balance3145RT;
						
						%>
						
						
						<tr>						
							<td style="padding-left:15px;background-color:#ffffe6;font-size:13px;">Net Balance</td>
							<td style="text-align:right;background-color:#ffffe6;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(EmptyBalance) %></td>
						</tr>
						<tr >
							<th  style="font-size:13px; padding-left:10px;background-color:#f7fff7;font-size:13px;">Net Balance</th>
							<th  style="font-size:13px; text-align:right; padding-left:10px; background-color:#f7fff7;font-size:13px;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(EmptyBalance+NetBalanceRec+SAPVehNdEmptyTotal) %></th>
						</tr> 
						
						
						


						
						

											
						<!--  Assets -->
						
						
						
						<tr>
							<th colspan="2" style="font-size:15px; background-color:#f6f6f6;">Assets</th>
						</tr>
						<tr >
							<th colspan="2" style="font-size:13px; padding-left:10px;">Empty</th>
						</tr>
						<tr>						
							<td colspan="2">
								<table style="width:100%;" cellpadding="0" cellspacing="0">
									<tr style="font-size:13px;">
										<th style="text-align:center; b1ackground-color:#f7fff7;font-size:13px;" colspan="3">250 ML SHELL</th>
										<th style="text-align:center; b1ackground-color:#f7fff7;font-size:13px;" colspan="3">240 ML SSRB</th>
										<th style="text-align:center; b1ackground-color:#f7fff7;font-size:13px;" colspan="3">250 ML STD</th>
									</tr>
									<tr>
										<td style="text-align:center; background-color:#f7fff7;font-size:13px;">Balance</td>
										<td style="text-align:center;background-color:#f7fff7;font-size:13px;">Approved Limit</td>
										<td style="text-align:center;background-color:#f7fff7;font-size:13px;">Available Limit</td>
										<td style="text-align:center; background-color:#f7fff7;font-size:13px;">Balance</td>
										<td style="text-align:center;background-color:#f7fff7;font-size:13px;">Approved Limit</td>
										<td style="text-align:center;background-color:#f7fff7;font-size:13px;">Available Limit</td>
										<td style="text-align:center; background-color:#f7fff7;font-size:13px;">Balance</td>
										<td style="text-align:center;background-color:#f7fff7;font-size:13px;">Approved Limit</td>
										<td style="text-align:center;background-color:#f7fff7;font-size:13px;">Available Limit</td>
									</tr>
									
									<%
									
									
									
									
									long LimitShell = 0;
									ResultSet rs123 = s2.executeQuery("SELECT ecl.id,ecl.distributor_id,(select name from common_distributors cd where cd.distributor_id=ecl.distributor_id) dist_name,ecl.credit_type,(select label from  ec_empty_credit_types ect where ect.id=ecl.credit_type) credit_type,ecl.reason,ecl.start_date,ecl.end_date,sum(raw_cases) limitt FROM ec_empty_credit_limit ecl join ec_empty_credit_limit_products eclp on ecl.id=eclp.id  where ecl.is_active=1 and curdate() between ecl.start_date and ecl.end_date and distributor_id="+DistributorID1+" and package_id=17");
									if(rs123.first()){
										LimitShell = rs123.getLong("limitt");
									}
									
								
									long Limit240 = 0;
									ResultSet rs132 = s2.executeQuery("SELECT ecl.id,ecl.distributor_id,(select name from common_distributors cd where cd.distributor_id=ecl.distributor_id) dist_name,ecl.credit_type,(select label from  ec_empty_credit_types ect where ect.id=ecl.credit_type) credit_type,ecl.reason,ecl.start_date,ecl.end_date,sum(raw_cases) limitt FROM ec_empty_credit_limit ecl join ec_empty_credit_limit_products eclp on ecl.id=eclp.id  where ecl.is_active=1 and curdate() between ecl.start_date and ecl.end_date and distributor_id="+DistributorID1+" and package_id=12");
									if(rs132.first()){
										Limit240 = rs132.getLong("limitt");
									}
									
									
									long Limit250 = 0;
									ResultSet rs14 = s2.executeQuery("SELECT ecl.id,ecl.distributor_id,(select name from common_distributors cd where cd.distributor_id=ecl.distributor_id) dist_name,ecl.credit_type,(select label from  ec_empty_credit_types ect where ect.id=ecl.credit_type) credit_type,ecl.reason,ecl.start_date,ecl.end_date,sum(raw_cases) limitt FROM ec_empty_credit_limit ecl join ec_empty_credit_limit_products eclp on ecl.id=eclp.id  where ecl.is_active=1 and curdate() between ecl.start_date and ecl.end_date and distributor_id="+DistributorID1+" and package_id=11");
									if(rs14.first()){
										Limit250 = rs14.getLong("limitt");
									}
									
									//Available limit
									
									long Overlimit17 = Balance17 + LimitShell;
									
									long Overlimit12 = Balance12 + Limit240;
									
									long Overlimit11 = Balance11 + Limit250;
									
									%>
									<tr>
										<td style="text-align:center;font-size:13px;"><%if(Balance17!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance17) %><%} %></td>
										<td style="text-align:center;font-size:13px;"><%if(LimitShell!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(LimitShell) %><%} %></td>
										<td style="text-align:center;font-size:13px;"><%if(Overlimit17!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Overlimit17) %><%} %></td>
										<td style="text-align:center;font-size:13px;"><%if(Balance12!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance12) %><%} %></td>
										<td style="text-align:center;font-size:13px;"><%if(Limit240!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Limit240) %><%} %></td>
										<td style="text-align:center;font-size:13px;"><%if(Overlimit12!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Overlimit12) %><%} %></td>
										<td style="text-align:center;font-size:13px;"><%if(Balance11!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance11) %><%} %></td>
										<td style="text-align:center;font-size:13px;"><%if(Limit250!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Limit250) %><%} %></td>
										<td style="text-align:center;font-size:13px;"><%if(Overlimit11!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(Overlimit11) %><%} %></td>
									</tr>
									
									
									
								</table>
							</td>
						</tr>
						<tr >
							<th colspan="2" style="font-size:13px; padding-left:10px;">Cooler Injection</th>
						</tr>
						<tr>
							<td colspan="2">
								<table style="width:100%;" cellpadding="0" cellspacing="0">
									<tr style="font-size:13px;">
										<th style="text-align:center; background-color:#f7fff7;">Total</th>
										<%
										ResultSet rsT = s2.executeQuery("SELECT year(movement_date_parsed) year FROM pep.common_assets where tot_status = 'INJECTED' and outlet_id_parsed in (select id from common_outlets where cache_distributor_id = "+DistributorID1+") group by year(movement_date_parsed) order by year desc limit 5");
										while(rsT.next()){							
										%>
										<th style="text-align:center; background-color:#f7fff7;font-size:13px;"><%=rsT.getString("year") %></th>
										
										<%
										}
										%>
										
										<th style="text-align:center; background-color:#f7fff7;font-size:13px;">Others</th>
									</tr>
									<tr>
									<%
									//Total Count
									long TotalCoolerCount=0;
									ResultSet rsTot = s2.executeQuery("select sum(no_cooler) total_cooler from ( "+
											  " SELECT year(movement_date_parsed) year, count(*) no_cooler FROM pep.common_assets where tot_status = 'INJECTED' and outlet_id_parsed in (select id from common_outlets where cache_distributor_id = "+DistributorID1+") group by year(movement_date_parsed) order by year "+
								              " ) as tab ");
									if(rsTot.first()){
										TotalCoolerCount = rsTot.getLong("total_cooler");
									}
									
									%>
									
										<td style="text-align:center;font-size:13px;"><%=Utilities.getDisplayCurrencyFormat(TotalCoolerCount) %></td>
										
									<%
									ResultSet rsTOT5Years = s2.executeQuery("SELECT year(movement_date_parsed) year, count(*) no_cooler FROM pep.common_assets where tot_status = 'INJECTED' and outlet_id_parsed in (select id from common_outlets where cache_distributor_id = "+DistributorID1+") group by year(movement_date_parsed) order by year desc limit 5");
									while(rsTOT5Years.next()){
									%>	
										<td style="text-align:center;font-size:13px;"><%=Utilities.getDisplayCurrencyFormat(rsTOT5Years.getLong("no_cooler")) %></td>
									<%
									}
									%>
									<%
									//Others
									int RowCount=0;	
									long RemainingOtherTOTCounter=0;
									
									ResultSet rsToTOther = s2.executeQuery("SELECT year(movement_date_parsed) year, count(*) no_cooler FROM pep.common_assets where tot_status = 'INJECTED' and outlet_id_parsed in (select id from common_outlets where cache_distributor_id = "+DistributorID1+") group by year(movement_date_parsed) order by year desc");
									while(rsToTOther.next()){
										
										if(RowCount>4){ //skipping 1st 5 years
											RemainingOtherTOTCounter += rsToTOther.getLong("no_cooler");
										}
										RowCount++;
									}
									
									
									%>
									
									<td style="text-align:center;font-size:13px;"><%=Utilities.getDisplayCurrencyFormat(RemainingOtherTOTCounter) %></td>
									
									</tr>
									
									
									<tr><td>&nbsp;</td></tr>
						<tr>
							<td >
								<b style="font-size:12px;">Disclaimer:</b><br/>
								<ol>
									<li style="font-size:12px;">Ledger balance has been adjusted for the last month incentive.</li>
									<li style="font-size:12px;">Vehicle account does not cover temporary vehicles issued. If applicable, depreciation/demiurges against temporary vehicles will be charged according to policy.</li>
								</ol>
							
							
							</td>
						</tr>
									
									
								</table>
							
							</td>
							
							
							
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