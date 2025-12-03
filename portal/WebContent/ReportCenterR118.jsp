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
int FeatureID = 118;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionToReplica();
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
	WhereHOD = " and cd.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and cd.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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
%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>
<%

Date[] d = Utilities.getPastMonthsInDate(EndDate, 12);

%>

<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size:11px;">
		
							<th data-priority="1"  style="text-align:center;  ">Distributor</th>	
							<th data-priority="1"  style="text-align:center;  ">Category</th>
							<%
							for (int i = 0; i < d.length; i++){
								
								String month = Utilities.truncateStringToMax(Utilities.getMonthNameByNumber(Utilities.getMonthNumberByDate(d[i])),3);
								int year = Utilities.getYearByDate(d[i])-2000;
							%>						
							<th data-priority="1"  style="text-align:center;  "><%=month %>/<%=year %></th>							
							<%
							}
							%>
							<th style="text-align:center;  ">Total</th>						
					    </tr>
					  </thead> 
					<tbody>
					<%
					double Total[] = new double[d.length];
					double GrandTotal = 0;
					ResultSet rs = s.executeQuery("select cd.distributor_id, cd.name, (select label from common_distributors_categories where id = cd.category_id) category from common_distributors cd where 1=1 "+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+WhereDistributors+" and distributor_id in (select distributor_id from inventory_delivery_note where created_on between "+Utilities.getSQLDateLifting(d[0])+" and "+Utilities.getSQLDateLifting(Utilities.getEndDateByDate(d[d.length-1]) )+" ) order by category_id");
					//System.out.println("select cd.distributor_id, cd.name, (select label from common_distributors_categories where id = cd.category_id) category from common_distributors cd where 1=1 "+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+WhereDistributors+" and distributor_id in (select distributor_id from inventory_delivery_note where created_on between "+Utilities.getSQLDateLifting(d[0])+" and "+Utilities.getSQLDateLifting(Utilities.getEndDateByDate(d[d.length-1]) )+" ) order by category_id");
					while(rs.next()){
						long DistributorID = rs.getLong(1);
						String DistributorName = Utilities.truncateStringToMax(rs.getString(2),15);
						String category = rs.getString(3);
					%>
						<tr>
							<td><%=DistributorID %> - <%=DistributorName %></td>
							<td><%=category%></td>
							<%
							double TotalConvertedCases = 0;
							for (int i = 0; i < d.length; i++){
								Date iStartDate = d[i];
								Date iEndDate = Utilities.getEndDateByDate(iStartDate);
								
								double ConvertedCases = 0;
								ResultSet rs2 = s2.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id = "+DistributorID+" and created_on between "+Utilities.getSQLDateLifting(iStartDate)+" and "+Utilities.getSQLDateNextLifting(iEndDate)+" and created_on < "+Utilities.getSQLDateNextLifting(EndDate)+"");
								
								if (rs2.first()){
									ConvertedCases = rs2.getDouble(1);
								}
								TotalConvertedCases += ConvertedCases;
								Total[i] += ConvertedCases;
							%>
							<td style="text-align:right"><%if(ConvertedCases!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCases) %><%}else{ %>&nbsp;<%} %></td>							
							<%
							}
							GrandTotal += TotalConvertedCases;
							%>
							<td style="text-align:right"><%if(TotalConvertedCases!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(TotalConvertedCases) %><%}else{ %>&nbsp;<%} %></td>
						</tr>
					<%
					}
					
					%>
						<tr>
							<td></td>
							<td></td>
							<%
							
							for (int i = 0; i < d.length; i++){
							%>
							<td style="text-align:right"><%if(Total[i]!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(Total[i]) %><%}else{ %>&nbsp;<%} %></td>							
							<%
							}
							%>
							<td style="text-align:right"><%if(GrandTotal!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotal) %><%}else{ %>&nbsp;<%} %></td>
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