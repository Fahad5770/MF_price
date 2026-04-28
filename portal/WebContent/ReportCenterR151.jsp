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
int FeatureID = 173;

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


String WarehouseIDs= UserAccess.getWarehousesQueryString(UserAccess.getUserFeatureWarehouse(SessionUserID, FeatureID));
           long SelectedWarehouseArray[] = null;
           if (session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse") != null){
           	SelectedWarehouseArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse");
           	WarehouseIDs = Utilities.serializeForSQL(SelectedWarehouseArray);
           }
           String WhereWarehouse = "";
           if (WarehouseIDs.length() > 0){
           		WhereWarehouse = " and glcr.warehouse_id in ("+WarehouseIDs+") ";	
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
						<%if(GlEmployeeIDs!=""){ 
						ResultSet rs4 = s.executeQuery("select id,display_name from users where id="+GlEmployeeIDs);
						if(rs4.first()){ 
						%>
						<tr style="font-size:11px;">
							<th>User : <%=rs4.getLong("id") %> - <%=rs4.getString("display_name") %></th>
						</tr>
						<%}} %>
			</table>
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 
					<tbody>
						
						<thead>
					    

					    <tr style="font-size:11px;">							
							
							<th>Customer</th>
							<%
							int ColCount = 0;
							ResultSet rs = s.executeQuery("SELECT * FROM gl_cash_instruments order by id");
							while(rs.next()){
								ColCount++;
							%>
							
							<th data-priority="1"  style="text-align:center; min-width:30px;"><%=rs.getString("short_label") %></th>				
																
							<%
							}
							//System.out.println(ColCount);
							double AmountTotal[] = new double[ColCount];
							
							for (int i = 0; i < AmountTotal.length; i++){
								AmountTotal[i] = 0;								
							}
							
							
							%>
							<th style="text-align:center">Total</th>
													
					    </tr>
					    
					  </thead> 
						
						
						
						
						
							<%
							
								//System.out.println("select distributor_id,name from common_distributors where distributor_id in (select distinct(customer_id) from gl_cash_receipts) where 1=1 "+WhereDistributors+WhereASM+WhereHOD+WhereRSM+WhereSM+WhereTDM);
								
								ResultSet rs2 = s3.executeQuery("select distributor_id,name from common_distributors where distributor_id in (select distinct(customer_id) from gl_cash_receipts where created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+" and warehouse_id in ("+WarehouseIDs+")) "+WhereHOD+" "+WhereRSM);
								while(rs2.next()){	
									int ArrayIndexCounter=0;
									double RowTotal=0;
								%>
								<tr style="font-size:9px;">										
									
									<td style="font-size:9px;"><%=rs2.getString("distributor_id") %> - <%=rs2.getString("name") %></td>
								<% 
								rs.beforeFirst(); 
								
								while(rs.next()){	
								
									ResultSet rs1 = s2.executeQuery("SELECT glcr.id,glcr.customer_id,(select name from common_distributors cd where cd.distributor_id=glcr.customer_id) customer_name,glcr.receipt_type,glcr.created_on,"+
												" glcr.created_on_date,glcr.created_by,glcr.warehouse_id,glcri.instrument_id,"+
												" (select glci.label from gl_cash_instruments glci where glci.id=glcri.instrument_id) instrument_name,"+
												" amount, sum(amount) total_amount FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri"+
												" on glcr.id=glcri.id where 1=1 "+WhereGlEmployee+" and glcri.instrument_id="+rs.getLong("id")+" and glcr.customer_id="+rs2.getLong("distributor_id")+WhereWarehouse+" and created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+" group by glcri.instrument_id ");
										
										if(rs1.first()){
											AmountTotal[ArrayIndexCounter] += rs1.getDouble("total_amount");
											RowTotal+=rs1.getDouble("total_amount");
										%>	
										
										<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(rs1.getDouble("total_amount")) %></td>	
											
										<%	
										ArrayIndexCounter++;
										}else{
											%>
											<td></td>
										<%
										ArrayIndexCounter++;
										}
										%>
										
										<%
								}
									
									%>
									<td style="text-align:right;"><%if(RowTotal!=0.0){%><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(RowTotal) %><%} %></td>
									</tr>
									<%
								}//end of rs2 while
							
							
							%>
							
								
						
						
						
						<tr>
						
						<th>Total</th>
						<% 
						double GrandTotal = 0;
						for(int x=0;x<AmountTotal.length;x++){
							GrandTotal += AmountTotal[x];
						%>
							<td style="text-align:right;"><%if(AmountTotal[x]!=0.0){%><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(AmountTotal[x]) %><%} %></td>	
						<%
						}
						%>
						<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(GrandTotal) %></td>				
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