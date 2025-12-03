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
int FeatureID = 183;

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
	WhereHOD = " and customer_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and customer_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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
	WhereDistributors = " and customer_id in ("+DistributorIDs+") ";
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
	WhereSM = " and customer_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
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
	WhereTDM = " and customer_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
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
	WhereASM = " and customer_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
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
           
  //Primary Inovice Status 
  
  		 boolean IsPrimaryInvoiceStatusSelected=false;
         String SelectedPrimaryInvoiceStatusArray[] = null;
         if (session.getAttribute(UniqueSessionID+"_SR1SelectedPrimaryInvoiceStatus") != null){
         	SelectedPrimaryInvoiceStatusArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedPrimaryInvoiceStatus");           	
         }
         
         String LiftingStatusWhere="";
         if(SelectedPrimaryInvoiceStatusArray != null){
        	 if (SelectedPrimaryInvoiceStatusArray.length==1){ //check only if it has only one selection
        		 
        	  		if(SelectedPrimaryInvoiceStatusArray[0].equals("Lifted")){
        	  			LiftingStatusWhere=" having lifted='Yes'"; 
        		  }else if(SelectedPrimaryInvoiceStatusArray[0].equals("NotLifted")){
        			  LiftingStatusWhere=" having lifted='No'"; 
        		  }
        	  } 
         }
  	
           
           
           
          // System.out.println("Hello "+WarehouseIDs);
%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
		
			User: <%=SessionUserID %> - <%= (String)session.getAttribute("UserDisplayName") %>
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					
					 
					<tbody>
						<%
						
						
						
						%>
						<thead>
					    
						
					    <tr style="font-size:11px;">							
							<th data-priority="1"  style="text-align:center;  width:12%">Posting Date</th>
							<th data-priority="1"  style="text-align:center;  width:10%">Order#</th>				
							<th data-priority="1"  style="text-align:center;  width:10%">Order Date</th>
							<th data-priority="1"  style="text-align:center;  width:10%">Invoice#</th>
							<th data-priority="1"  style="text-align:center;  width:10%">Invoice Date</th>
							<th data-priority="1"  style="text-align:center;  width:33%">Customer</th>
							<th data-priority="1"  style="text-align:center;  width:10%">Invoice Amount</th>
							<th data-priority="1"  style="text-align:center;  width:5%">Lifted</th>	
								

												
							
							
													
					    </tr>
					    
					  </thead> 
						
						<%
						//System.out.println("SELECT id,order_no,order_date,invoice_no,invoice_date,invoice_amount,customer_id,(select name from common_distributors cd where cd.distributor_id=glip.customer_id) customer_name,current_balance,credit_limit FROM gl_invoice_posting glip where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereWarehouse+WhereWarehouse1+WhereASM+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereDistributors);
						double Total=0;
						ResultSet rs = s.executeQuery("SELECT id,order_no,order_date,invoice_no,invoice_date,invoice_amount,customer_id,(select name from common_distributors cd where cd.distributor_id=glip.customer_id) customer_name,current_balance,credit_limit, created_on,if((select invoice_no from inventory_delivery_note where invoice_no = glip.invoice_no) is null,'No','Yes') lifted FROM gl_invoice_posting glip where created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+WhereWarehouse+WhereASM+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereDistributors+LiftingStatusWhere+" and glip.created_by="+SessionUserID);
						while(rs.next()){
							Total += rs.getDouble("invoice_amount");
						%>
						
						
						<tr>
						<td style="text-align:left;"><%= Utilities.getDisplayDateTimeFormat(rs.getTimestamp("created_on")) %></td>
							<td style="text-align:left;"><%=rs.getLong("order_no") %></td>			
							<td style="text-align:left;"><%if(rs.getDate("order_date")!=null){%><%= Utilities.getDisplayDateFormat(rs.getDate("order_date")) %><%} %></td>
							<td style="text-align:left;"><%=rs.getLong("invoice_no") %></td>
							<td style="text-align:left;"><%if(rs.getDate("invoice_date")!=null){%><%= Utilities.getDisplayDateFormat(rs.getDate("invoice_date")) %><%} %></td>
							<td style="text-align:left;"><%=rs.getLong("customer_id") %> - <%=rs.getString("customer_name") %></td>
							<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(rs.getDouble("invoice_amount")) %></td>
							<td style="text-align:center;"><%=rs.getString("lifted") %></td>			
							
														
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
						<th>Total</th>
						<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(Total) %></td>
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