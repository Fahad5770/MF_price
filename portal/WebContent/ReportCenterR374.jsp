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
<%@page import="java.util.Calendar"%>

<script src="js/lookups.js"></script>
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
int FeatureID = 495;

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
	Calendar cal = Calendar.getInstance();
    cal.setTime(StartDate);
    cal.add(Calendar.MONTH, -1);
    StartDate = cal.getTime();
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
boolean IsOutletSelected=false;
String OutletIds="";
long SelectedOutletArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets") != null){
	IsOutletSelected=true;
	SelectedOutletArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets");
	OutletIds = Utilities.serializeForSQL(SelectedOutletArray);
}

System.out.println("OutletIds : "+OutletIds);

/* if(OutletIds.length()<1){
	//SelectedOutletArray = (long[])request.getParameter("UniqueSessionID");
	OutletIds = request.getParameter("outletID");
} */
//out.print(OutletIds);
System.out.println("OutletIds : "+OutletIds);

String WhereOutlets = "";
if (OutletIds.length() > 0){
	WhereOutlets = " and outlet_id in ("+OutletIds+") ";	
	System.out.println(WhereOutlets);
}
//
%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Ledger</li>
<li>
<%if(OutletIds.length()>0){ %>

<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 
					<tbody>
						<%
						long AccountID =0;
						int counter=0;
						String OutletTitle ="";
						double Balance=0;
						Date CurrentDate = new Date();
						
						
						ResultSet rs = s.executeQuery("select name from common_outlets where id="+OutletIds);
						System.out.println("select name from common_outlets where id="+OutletIds);
						if(rs.first()){
							//AccountID = rs.getLong("id");
							OutletTitle = rs.getString("name");
						}
						
						double OpeningBalance = 0;
						//opening balance
						
						ResultSet rs2 = s.executeQuery("Select sum(debit), sum(credit) FROM gl_transactions_sales where outlet_id="+OutletIds+" and created_on_date < "+Utilities.getSQLDate(StartDate));
						if(rs2.first()){
							OpeningBalance = rs2.getDouble(1) - rs2.getDouble(2);
						}
						
						Balance = OpeningBalance;
						%>
						
						<thead>
					    <tr>

					    
							<th colspan=8><%=OutletIds%> - <%=OutletTitle %></th>
						</tr>
					    <tr style="font-size:11px;">							
							
							
							<th data-priority="1"  style="text-align:center;  width:10%">Date</th>
							<!-- <th data-priority="1"  style="text-align:center;  width:10%">Remarks</th> -->
							<th data-priority="1"  style="text-align:center;  width:10%">Invoice#</th>	
							<th data-priority="1"  style="text-align:center;  width:10%">User</th>											
							<th data-priority="1"  style="text-align:center;  width:13%">Debit</th>
							<th data-priority="1"  style="text-align:center;  width:13%">Credit</th>
							<th data-priority="1"  style="text-align:center;  width:13%">Balance</th>
							<th data-priority="1"  style="text-align:center;  width:13%">Comments</th>
							
																
							
							
													
					    </tr>
					     <tr>
							<td><%=Utilities.getDisplayDateFormat(StartDate) %></td>
							<td>Opening Balance</td>
							<td>&nbsp;</td>
							<td style="text-align:right;background-color:#F6F6F6;"><%if(OpeningBalance>=0){ %><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(OpeningBalance) %><%} %></td>
							<td style="text-align:right;background-color:#F6F6F6;"><%if(OpeningBalance<0){ %><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(OpeningBalance*-1) %><%} %></td>
							<td style="background-color:#F6F6F6;">&nbsp;</td>
						</tr>
					  </thead> 
						
						<%
						System.out.println("Select *,(select display_name from users u where u.id=gls.created_by) display_name  FROM gl_transactions_sales gls   where outlet_id="+OutletIds+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" order by create_on, gls.id");
						ResultSet rs1 = s.executeQuery("Select *,(select display_name from users u where u.id=gls.created_by) display_name  FROM gl_transactions_sales gls   where outlet_id="+OutletIds+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" order by created_on , gls.id");
						
						while(rs1.next()){
							
							Balance = (Balance + rs1.getDouble("debit")) - rs1.getDouble("credit"); 
							Date CreatedOn = new Date();
							ResultSet rs21 = s2.executeQuery("SELECT created_on FROM inventory_sales_invoices where id="+rs1.getString("sales_id"));
							if(rs21.first()){
								CreatedOn = rs21.getDate(1);
							}
							
							if(rs1.getDouble("credit")>0){
								CreatedOn = rs1.getDate("created_on");
							}
						%>
												
						<tr>										
										
							<td ><%=Utilities.getDisplayDateFormat(CreatedOn) %></td>
						<%-- 	<td ><%=rs1.getString("remarks")%></td> --%>
							<td><%=rs1.getString("sales_id") %></td>
							
							<td ><%=Utilities.truncateStringToMax(rs1.getString("created_by") + " - " +rs1.getString("display_name"),16) %></td>	
				
							<td style="text-align:right;background-color:#F6F6F6;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(rs1.getDouble("debit")) %></td>
							<td style="text-align:right;background-color:#F6F6F6;"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(rs1.getDouble("credit")) %></td>
							
							<%
							String BalanceValue = Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccountingDr(Balance);
							%>
							
							<td style="text-align:right;background-color:<% if(BalanceValue.contains("Dr")){ out.print("#ffffb2;"); }else{ out.print("#F6F6F6;"); } %>"><%=BalanceValue%></td>
						    <td><%=rs1.getString("comments") %></td>							
															
						</tr>
						<%
						counter++;
						}
						%>
						
						
					</tbody>
							
				</table>
		</td>
	</tr>
</table>

<%}else{%>
	
	
	Please Select Outlet.
	
	
<%
}
%>



	</li>	
</ul>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>