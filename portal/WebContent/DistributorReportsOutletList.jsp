<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>

<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 71;

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

Date StartDate = (Date)session.getAttribute("SR1StartDate");
Date EndDate = (Date)session.getAttribute("SR1EndDate");

if(session.getAttribute("SR1StartDate") == null){
	StartDate = new Date();
}

if(session.getAttribute("SR1EndDate") == null){
	EndDate = new Date();
}

//out.print("StartDate = "+StartDate);
//out.print("EndDate = "+EndDate);

long SelectedDistributorsArray[] = null;
if (session.getAttribute("SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute("SR1SelectedDistributors");           	
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
}


long SelectedVehicleArray[] = null;
if (session.getAttribute("SR1SelectedVehicles") != null){
	SelectedVehicleArray = (long[])session.getAttribute("SR1SelectedVehicles");           	
}


String VehicleIDs = "";
String WhereVehicle = "";
if(SelectedVehicleArray != null && SelectedVehicleArray.length > 0){
	for(int i = 0; i < SelectedVehicleArray.length; i++){
		if(i == 0){
			VehicleIDs += SelectedVehicleArray[i];
		}else{
			VehicleIDs += ", "+SelectedVehicleArray[i];
		}
	}
	WhereVehicle = " and isd.vehicle_id in ("+VehicleIDs+") ";
}

long SelectedEmployeeArray[] = null;
if (session.getAttribute("SR1SelectedEmployees") != null){
	SelectedEmployeeArray = (long[])session.getAttribute("SR1SelectedEmployees");           	
}
String EmployeeIDs = "";
String WhereEmployee = "";
if(SelectedEmployeeArray != null && SelectedEmployeeArray.length > 0){
	for(int i = 0; i < SelectedEmployeeArray.length; i++){
		if(i == 0){
			EmployeeIDs += SelectedEmployeeArray[i];
		}else{
			EmployeeIDs += ", "+SelectedEmployeeArray[i];
		}
	}
	WhereEmployee = " and isd.driver_id in ("+EmployeeIDs+") ";
}


long SelectedOrderBookerArray[] = null;
if (session.getAttribute("SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute("SR1SelectedOrderBookers");           	
}
String OrderBookerIDs = "";
String WhereOrderBooker = "";
String WhereOrderBookerCount = "";

if(SelectedOrderBookerArray != null && SelectedOrderBookerArray.length > 0){
	for(int i = 0; i < SelectedOrderBookerArray.length; i++){
		if(i == 0){
			OrderBookerIDs += SelectedOrderBookerArray[i];
		}else{
			OrderBookerIDs += ", "+SelectedOrderBookerArray[i];
		}
	}
	WhereOrderBooker = " and co.id in (select distinct dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to  in ("+OrderBookerIDs+")) ";
	WhereOrderBookerCount = " and dbpv.outlet_id in (select distinct dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to  in ("+OrderBookerIDs+")) ";
}

%>
<script>
function DistReportsOutletListOutletDashboardRedirect(OutletIDDD){
	//alert(OutletIDDD);
	$("#DistReportsOutletListOutletID").val(OutletIDDD);
	document.getElementById("DistReportsOutletListFormID1").submit();	
}

function DistReportsOutletListOrderBookerDashboardRedirect(OrderBookerIDDD){
	$("#DistReportsOutletListOrderBookerDashboardEmployeeCode").val(OrderBookerIDDD);
	document.getElementById("DistReportsOutletListOrderBookerDashboardFormID1").submit();	
}

</script>



<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Outlet List</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size: 11px;">
							<th data-priority="1" style="text-align:center; width: 6%">ID</th>
							<th data-priority="1" style="text-align:center; width: 29%">Name</th>
							<th data-priority="1" style="text-align:center; width: 20%">PJP</th>
							<th data-priority="1" style="text-align:center; width: 30%">Address</th>
							<th data-priority="1" style="text-align:center; width: 15%">Contact#</th>
					    </tr>
					    <%
					    
					    ResultSet rs1 = s.executeQuery("select distributor_id, name from common_distributors where distributor_id in ("+DistributorIDs+")");
					    while(rs1.next()){
					    	long DistributorID = rs1.getLong(1);
					    	String DistributorName = rs1.getString(2);
					    %>
						<tr style="background:#E4E4E3">
	   	            		<td align="left" colspan="7"><%= DistributorID +" - "+DistributorName %></td>
	   	            	</tr>
	   	            	<%
	   	            	int CountDiscounted = 0;
	   	            	ResultSet rs4 = s2.executeQuery("SELECT count(distinct dbpv.outlet_id) FROM distributor_beat_plan_view dbpv where dbpv.distributor_id = "+DistributorID+" and dbpv.outlet_id in (select distinct outlet_id from sampling where active = 1) "+WhereOrderBookerCount+" order by dbpv.outlet_id");
	   	            	if (rs4.first()){
	   	            		CountDiscounted = rs4.getInt(1);
	   	            	}
	   	            	%>
						<tr style="background:#F2F2F1">
	   	            		<td align="left" colspan="7">Discounted (<%=CountDiscounted %>)</td>
	   	            	</tr>
	   	            	<%
	   	            	if (true){
							ResultSet rs = s2.executeQuery("SELECT co.id, co.name outlet_name, co.address, (select concat(contact_name,'<br>',contact_number) from common_outlets_contacts where outlet_id = co.id and is_primary = 1 limit 1) owner_info, (select concat(dbpv.assigned_to, ' - ',u.display_name,'<br>',dbpv.id,' - ',dbpv.label) from distributor_beat_plan_view dbpv join users u on dbpv.assigned_to = u.id where dbpv.outlet_id = co.id limit 1) order_booker, (select dbpv.assigned_to from distributor_beat_plan_view dbpv join users u on dbpv.assigned_to = u.id where dbpv.outlet_id = co.id limit 1) order_booker_id FROM common_outlets co join common_outlets_distributors_view codv on co.id = codv.outlet_id where codv.distributor_id = "+DistributorID+" and co.id in (select distinct outlet_id from sampling where active = 1) "+WhereOrderBooker+" order by order_booker");
							
							while(rs.next()){
								long OutletID = rs.getLong(1);
								
								String OrderBooker = rs.getString("order_booker");
								
								if (OrderBooker == null){
									OrderBooker = "Not Assigned";
								}								
							%>
							<tr style="font-size: 11px;">
		    					<td style="text-align: left;"><a href="#" style="text-decoration:none;color:black;font-size: 11px; font-weight:normal" <%if(Utilities.isAuthorized(31, SessionUserID) == true){%>ondblclick="DistReportsOutletListOutletDashboardRedirect(<%=OutletID%>)"<%} %>><%= OutletID  %></a></td>
		    					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><a href="#" style="text-decoration:none;color:black;font-size: 11px; font-weight:normal" <%if(Utilities.isAuthorized(31, SessionUserID) == true){%>ondblclick="DistReportsOutletListOutletDashboardRedirect(<%=OutletID%>)"<%} %>><%=rs.getString(2) %></a></td>
		    					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><a href="#" style="text-decoration:none;color:black;font-size: 11px; font-weight:normal" <%if(Utilities.isAuthorized(41, SessionUserID) == true && rs.getLong("order_booker_id") !=0){%>ondblclick="DistReportsOutletListOrderBookerDashboardRedirect(<%=rs.getLong("order_booker_id")%>)"<%} %>><%=OrderBooker %></a></td>
		    					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=rs.getString("address") %></td>
		    					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=rs.getString("owner_info") %></td>
		    		    	</tr>
							<%
							}
					    }
							%>	   	            	
						
							   	            	<%
	   	            	int CountNonDiscounted = 0;
	   	            	ResultSet rs5 = s2.executeQuery("SELECT count(distinct dbpv.outlet_id) FROM distributor_beat_plan_view dbpv where dbpv.distributor_id = "+DistributorID+" and dbpv.outlet_id not in (select distinct outlet_id from sampling where active = 1) "+WhereOrderBookerCount+" order by dbpv.outlet_id");
	   	            	if (rs5.first()){
	   	            		CountNonDiscounted = rs5.getInt(1);
	   	            	}
	   	            	%>
						
						<tr style="background:#F2F2F1">
	   	            		<td align="left" colspan="7">Non-Discounted (<%=CountNonDiscounted %>)</td>
	   	            	</tr>
	   	            	<%
							ResultSet rs = s2.executeQuery("SELECT co.id, co.name outlet_name, co.address, (select concat(contact_name,'<br>',contact_number) from common_outlets_contacts where outlet_id = co.id and is_primary = 1 limit 1) owner_info, (select concat(dbpv.assigned_to, ' - ',u.display_name,'<br>',dbpv.id,' - ',dbpv.label) from distributor_beat_plan_view dbpv join users u on dbpv.assigned_to = u.id where dbpv.outlet_id = co.id limit 1) order_booker, (select dbpv.assigned_to from distributor_beat_plan_view dbpv join users u on dbpv.assigned_to = u.id where dbpv.outlet_id = co.id limit 1) order_booker_id FROM common_outlets co join common_outlets_distributors_view codv on co.id = codv.outlet_id where codv.distributor_id = "+DistributorID+" and co.id not in (select distinct outlet_id from sampling where active = 1) "+WhereOrderBooker+" order by order_booker");
							while(rs.next()){
								long OutletID = rs.getLong(1);
								
								String OrderBooker = rs.getString("order_booker");
								
								if (OrderBooker == null){
									OrderBooker = "Not Assigned";
								}
								
								
								
								
								
							%>
							<tr style="font-size: 11px;">
		    					<td style="text-align: left;"><a href="#" style="text-decoration:none;color:black;font-size: 11px; font-weight:normal" <%if(Utilities.isAuthorized(31, SessionUserID) == true){%>ondblclick="DistReportsOutletListOutletDashboardRedirect(<%=OutletID%>)"<%} %>><%= OutletID  %></a></td>
		    					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><a href="#" style="text-decoration:none;color:black;font-size: 11px; font-weight:normal" <%if(Utilities.isAuthorized(31, SessionUserID) == true){%>ondblclick="DistReportsOutletListOutletDashboardRedirect(<%=OutletID%>)"<%} %>><%=rs.getString(2) %></a></td>
		    					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><a href="#" style="text-decoration:none;color:black;font-size: 11px; font-weight:normal" <%if(Utilities.isAuthorized(41, SessionUserID) == true && rs.getLong("order_booker_id") !=0){%>ondblclick="DistReportsOutletListOrderBookerDashboardRedirect(<%=rs.getLong("order_booker_id")%>)"<%} %>><%=OrderBooker %></a></td>
		    					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=rs.getString("address") %></td>
		    					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=rs.getString("owner_info")%></td>
		    		    	</tr>
							<%
							}
							%>	   		   	            	

						<%
					    }
						%>

 	            				
				</table>
		</td>
	</tr>
</table>
 <form id="DistReportsOutletListFormID1" name="DistReportsOutletListFormID1" action="OutletDashboard.jsp" method="POST" data-ajax="false" target="_blank">
    	<input type="hidden" name="outletID" id="DistReportsOutletListOutletID"/>
    </form>
    
    
    <form id="DistReportsOutletListOrderBookerDashboardFormID1" name="DistReportsOutletListOrderBookerDashboardFormID1" action="OrderBookerDashboardOverview.jsp" method="POST" data-ajax="false" target="_blank">
    	<input type="hidden" name="EmployeeCode" id="DistReportsOutletListOrderBookerDashboardEmployeeCode"/>
    </form>
	</li>	
</ul>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>