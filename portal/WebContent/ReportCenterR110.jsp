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
int FeatureID = 109;

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
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
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


%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Performance</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
							<%							
							int PJPID=0;							
							%>
							
							<th data-priority="1"  style="text-align:center; width:70px; ">SKU/Bill</th>
							<th data-priority="1"  style="text-align:center; width:70px;">Drop Size</th>
							<th data-priority="1"  style="text-align:center; width:70px; ">Productivity</th>
													
					    </tr>
					  </thead> 
					<tbody>
						<%
						
						ResultSet rs1 = s.executeQuery("select * from common_distributors where is_active=1 and type_id=2"+WhereHOD+WhereRSM+WhereDistributors);
						//System.out.println("SELECT distinct id, label FROM distributor_beat_plan_view  where 1=1 "+WhereHOD+WhereRSM+WhereDistributors);
						
						while(rs1.next()){
							//PJPID = rs1.getInt("distributor_id");
							long DistributorID = rs1.getLong("distributor_id");
						%>
						<tr>
								<td><%=DistributorID + " - "+ rs1.getString("name") %></td>
							 
								<%
								ResultSet rs2 = s2.executeQuery("SELECT sum(total_no_of_invoice_items)/sum(total_no_of_invoices) sku_per_bill,sum(raw_cases_sold)/sum(total_no_of_invoices) drop_size,(( sum(total_no_of_orders) / sum(total_visits_due) )*100) productivity"+
																" FROM pep.bi_pjp_daily where pjp_id in(select id from distributor_beat_plan where distributor_id="+DistributorID+") and dated between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
								if(rs2.first()){
								%>
								
								<td style="text-align: right; width:70px;"><%if(rs2.getLong("sku_per_bill")!=0){%><%=rs2.getLong("sku_per_bill") %><%} %></td> 
								<td style="text-align: right; width:70px;"><%if(rs2.getLong("drop_size")!=0){%><%=rs2.getLong("drop_size") %><%} %></td> 
								<td style="text-align: right; width:70px;"><%if(rs2.getLong("productivity")!=0){%><%=rs2.getLong("productivity") %>%<%} %></td> 
								<%
								}
								%>
						</tr>
						<%
						}
						%>
						<tr>
						<%
					    
						ResultSet rs3 = s2.executeQuery("SELECT sum(total_no_of_invoice_items)/sum(total_no_of_invoices) sku_per_bill_total,sum(raw_cases_sold)/sum(total_no_of_invoices) drop_size_total,(( sum(total_no_of_orders) / sum(total_visits_due) )*100) productivity_total"+
								" FROM pep.bi_pjp_daily where pjp_id in(select id from distributor_beat_plan where distributor_id in(select distributor_id from common_distributors where is_active=1 and type_id=2"+WhereHOD+WhereRSM+WhereDistributors+")) and dated between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
						if(rs3.next()){
						%>
						
						
							<td>Total</td>
							<td style="text-align: right; width:70px;"><%if(rs3.getLong("sku_per_bill_total")!=0){%><%=rs3.getLong("sku_per_bill_total") %><%} %></td> 
							<td style="text-align: right; width:70px;"><%if(rs3.getLong("drop_size_total")!=0){%><%=rs3.getLong("drop_size_total") %><%} %></td> 
							<td style="text-align: right; width:70px;"><%if(rs3.getLong("productivity_total")!=0){%><%=rs3.getLong("productivity_total") %>%<%} %></td> 
						<%} %>
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