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
int FeatureID = 204;

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

String WhereCustomerID =" and customer_id in ("+DistributorIDs+") ";
if (CustomerID != 0){
	WhereCustomerID += " and customer_id in ("+CustomerID+") ";	
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
           
         
          
%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Instrument Receipt Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 
					
						
						<thead>
					    
							<tr style="font-size:11px;">
					    							
							<th colspan="7">Date: &nbsp;&nbsp;<span style="font-weight:normal"><%=Utilities.getDisplayDateFormat(StartDate) %> - <%=Utilities.getDisplayDateFormat(EndDate) %></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;Warehouse: &nbsp;&nbsp;&nbsp;<span style="font-weight:normal"><%=WarehouseTitle %></span></th>
							
							
										
					    </tr>
					    <tr style="font-size:11px;">
					    							
							<th style="width: 40%;">Customer Name</th>
							<th style="width: 10%;">Liquid</th>
							<th style="width: 10%;">Security</th>
							<th style="width: 10%;">Credit</th>
							<th style="width: 10%;">Vehicle</th>
							<th style="width: 10%;">Empty</th>
							<th style="width: 10%;">Total</th>
										
					    </tr>
					    
					  </thead> 
						
						<tbody>
						<%
						double LedgerTotal=0;
						double SecurityTotal=0;
						double CreditTotal=0;
						double VehicleTotal=0;
						double DmptyTotal=0;
						
						double RowTotal=0;
						double GrandTotal=0;
						
						ResultSet rs = s.executeQuery("select customer_id,customer_name,sum(ledger) ledger, sum(security_col) security_col,sum(credit) credit,sum(vehicle) vehicle,sum(empty) empty,created_on from ("+
								"SELECT glcr.customer_id,(select name from common_distributors cd where cd.distributor_id=glcr.customer_id) customer_name,glcr.created_on,sum(glcri.amount) amount,case glcr.receipt_type when 1 then sum(glcri.amount)  end ledger,case glcr.receipt_type when 2 then sum(glcri.amount)  end security_col,case glcr.receipt_type when 3 then sum(glcri.amount)  end credit,case glcr.receipt_type when 4 then sum(glcri.amount)  end vehicle,case glcr.receipt_type when 5 then sum(glcri.amount)  end empty FROM gl_cash_receipts  glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glci.is_internal = 0 and created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+WhereCustomerID+WhereWarehouse+" group by glcr.customer_id,glcr.receipt_type order by glcr.customer_id"+
								") tbl group by customer_id");
						
						while(rs.next()){
							
							
							
							LedgerTotal += rs.getDouble("ledger");
							SecurityTotal += rs.getDouble("security_col");
							CreditTotal += rs.getDouble("credit");
							VehicleTotal += rs.getDouble("vehicle");
							DmptyTotal += rs.getDouble("empty");
							
							RowTotal = rs.getDouble("ledger")+rs.getDouble("security_col")+ rs.getDouble("credit")+rs.getDouble("vehicle")+rs.getDouble("empty");
						%>
						<tr>
							<td ><%=rs.getLong("customer_id") %> <%if(rs.getString("customer_name") !=null){%>- <%=rs.getString("customer_name") %><%} %></td>
							<td style="text-align:right;"><%if(rs.getDouble("ledger")!=0){%><%=Utilities.getDisplayCurrencyFormat(rs.getDouble("ledger")) %><%} %></td>
							<td style="text-align:right;"><%if(rs.getDouble("security_col")!=0){%><%=Utilities.getDisplayCurrencyFormat(rs.getDouble("security_col")) %><%} %></td>
							<td style="text-align:right;"><%if(rs.getDouble("credit")!=0){%><%=Utilities.getDisplayCurrencyFormat(rs.getDouble("credit")) %><%} %></td>
							<td style="text-align:right;"><%if(rs.getDouble("vehicle")!=0){%><%=Utilities.getDisplayCurrencyFormat(rs.getDouble("vehicle")) %><%} %></td>
							<td style="text-align:right;"><%if(rs.getDouble("empty")!=0){%><%=Utilities.getDisplayCurrencyFormat(rs.getDouble("empty")) %><%} %></td>
							<td style="text-align:right;"><%if(RowTotal!=0){%><%=Utilities.getDisplayCurrencyFormat(RowTotal) %><%} %></td>
						</tr>
						<%
						GrandTotal+=RowTotal;
						RowTotal=0;
						}
						%>
						<tr>
						<th>
							Total
						</th>
						<td style="text-align:right;">
							<%if(LedgerTotal!=0){%><%=Utilities.getDisplayCurrencyFormat(LedgerTotal) %><%} %>
						</td>
						<td style="text-align:right;">
							<%if(SecurityTotal!=0){%><%=Utilities.getDisplayCurrencyFormat(SecurityTotal) %><%} %>
						</td>
						<td style="text-align:right;">
							<%if(CreditTotal!=0){%><%=Utilities.getDisplayCurrencyFormat(CreditTotal) %><%} %>
						</td>
						<td style="text-align:right;">
							<%if(VehicleTotal!=0){%><%=Utilities.getDisplayCurrencyFormat(VehicleTotal) %><%} %>
						</td>
						<td style="text-align:right;">
							<%if(DmptyTotal!=0){%><%=Utilities.getDisplayCurrencyFormat(DmptyTotal) %><%} %>
						</td>
						<td style="text-align:right;">
							<%if(GrandTotal!=0){%><%=Utilities.getDisplayCurrencyFormat(GrandTotal) %><%}%>
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