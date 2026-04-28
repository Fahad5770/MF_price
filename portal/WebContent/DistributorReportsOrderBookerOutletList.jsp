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

long SelectedOrderBookerArray[] = null;
if (session.getAttribute("SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute("SR1SelectedOrderBookers");           	
}
long OrderBookerID = SelectedOrderBookerArray[0];
%>
<script>
function OutletDashboardRedirect(OutletID){
	$("#DistributorOrderBookerOutletListOutletID").val(OutletID);
	document.getElementById("DistributorOrderBookerOutletListFormID").submit();
}

function OutletDashboardRedirectDistDashboard(DistID){
	//alert();
	$("#DistributorOrderBookerOutletListDistributorFormIDDistributorCode").val(DistID);
	document.getElementById("DistributorOrderBookerOutletListDistributorFormID").submit();
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
							<th data-priority="1" style="text-align:center; width: 20%">Distributor</th>
							<th data-priority="1" style="text-align:center; width: 30%">Address</th>
							<th data-priority="1" style="text-align:center; width: 15%">Contact#</th>
					    </tr>
					    
					    <tr style="background:#F2F2F1">
	   	            		<td align="left" colspan="7">Discounted Outlets (<span id="DiscountedOutletsCountSpan">&nbsp;</span>)</td>
	   	            	</tr>
					    
					    <%
					    	//ResultSet rs = s.executeQuery("select *, (select name from common_distributors where distributor_id=common_outlets.distributor_id) distributor_name, (select contact_number from common_outlets_contacts where outlet_id=common_outlets.id and is_primary = 1) contact_no from common_outlets where id in (select distinct dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to ="+OrderBookerID+" and dbpv.outlet_id in (select outlet_id from sampling where active=1))");
					    	
					    ResultSet rs = s.executeQuery("SELECT co.id, co.name outlet_name, co.address, (select concat(contact_name,'<br>',contact_number) from common_outlets_contacts where outlet_id = co.id and is_primary = 1 limit 1) owner_info FROM common_outlets co where co.id in (select distinct outlet_id from sampling where active = 1) and co.id in (select distinct dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to  in ("+OrderBookerID+"))");
					    int counter = 0;
					    	while(rs.next()){
					    		%>
					    		<tr style="font-size: 11px;">
									<td style="text-align: left;"><a href="#" style="text-decoration:none;color:black;font-size: 11px; font-weight:normal" <%if(Utilities.isAuthorized(31, SessionUserID) == true){%>ondblclick="OutletDashboardRedirect(<%=rs.getString("id")%>)"<%} %>><%=rs.getString("id")%></a></td>
									<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><a href="#" style="text-decoration:none;color:black;font-size: 11px; font-weight:normal" <%if(Utilities.isAuthorized(31, SessionUserID) == true){%>ondblclick="OutletDashboardRedirect(<%=rs.getString("id")%>)"<%} %>><%=rs.getString("outlet_name")%></a></td>
									<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec" nowrap>
									
									<%
									ResultSet rs2 = s2.executeQuery("select dbpv.id, dbpv.label, dbpv.distributor_id, cd.name from distributor_beat_plan_view dbpv join common_distributors cd on dbpv.distributor_id = cd.distributor_id where dbpv.outlet_id = "+rs.getString("id")+" group by dbpv.id, dbpv.label, dbpv.distributor_id, cd.name");
									while(rs2.next()){
									%>
									<a href="#" style="text-decoration:none;color:black;font-size: 11px; font-weight:normal" <%if(Utilities.isAuthorized(44, SessionUserID) == true){%>ondblclick="OutletDashboardRedirectDistDashboard(<%=rs2.getString("distributor_id")%>)"<%} %>><%=rs2.getString("distributor_id") + " - " +rs2.getString("name")%><br><%=rs2.getString("id") +" - " +rs2.getString("label")%></a>
									<%
									}
									%>
									
									</td>
									<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=rs.getString("address")%></td>
									<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=rs.getString("owner_info")%></td>
							    </tr>
					    		<%
					    		counter++;
					    	}
						%>
						<script>
							document.getElementById("DiscountedOutletsCountSpan").innerHTML = <%=counter%>;
						</script>
						<tr style="background:#F2F2F1">
	   	            		<td align="left" colspan="7">Non-Discounted Outlets (<span id="Non-DiscountedOutletsCountSpan">&nbsp;</span>)</td>
	   	            	</tr>
					    
					    <%
					    	ResultSet rs2 = s.executeQuery("SELECT co.id, co.name outlet_name, co.address, (select concat(contact_name,'<br>',contact_number) from common_outlets_contacts where outlet_id = co.id and is_primary = 1 limit 1) owner_info FROM common_outlets co where co.id not in (select distinct outlet_id from sampling where active = 1) and co.id in (select distinct dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to  in ("+OrderBookerID+"))");
					    	counter = 0;
					    	while(rs2.next()){
					    		%>
					    		<tr style="font-size: 11px;">
									<td style="text-align: left;"><a href="#" style="text-decoration:none;color:black;font-size: 11px; font-weight:normal" <%if(Utilities.isAuthorized(31, SessionUserID) == true){%>ondblclick="OutletDashboardRedirect(<%=rs2.getString("id")%>)"<%} %>><%=rs2.getString("id")%></a></td>
									<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><a href="#" style="text-decoration:none;color:black;font-size: 11px; font-weight:normal" <%if(Utilities.isAuthorized(31, SessionUserID) == true){%>ondblclick="OutletDashboardRedirect(<%=rs2.getString("id")%>)"<%} %>><%=rs2.getString("outlet_name")%></a></td>
									<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec" nowrap>
									
									<%
									ResultSet rs3 = s2.executeQuery("select dbpv.id, dbpv.label, dbpv.distributor_id, cd.name from distributor_beat_plan_view dbpv join common_distributors cd on dbpv.distributor_id = cd.distributor_id where dbpv.outlet_id = "+rs2.getString("id")+" group by dbpv.id, dbpv.label, dbpv.distributor_id, cd.name");
									while(rs3.next()){
									%>
									<a href="#" style="text-decoration:none;color:black;font-size: 11px; font-weight:normal" <%if(Utilities.isAuthorized(44, SessionUserID) == true){%>ondblclick="OutletDashboardRedirectDistDashboard(<%=rs3.getString("distributor_id")%>)"<%} %>><%=rs3.getString("distributor_id") + " - " +rs3.getString("name")%><br><%=rs3.getString("id") +" - " +rs3.getString("label")%></a>
									<%
									}
									%>
									
									
									</td>
									<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=rs2.getString("address")%></td>
									<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=rs2.getString("owner_info")%></td>
							    </tr>
					    		<%
					    		counter++;
					    	}
						%>
						<script>
							document.getElementById("Non-DiscountedOutletsCountSpan").innerHTML = <%=counter%>;
						</script>

 	            				
				</table>
		</td>
	</tr>
</table>
 <form id="DistributorOrderBookerOutletListFormID" name="DistributorOrderBookerOutletListFormID" action="OutletDashboard.jsp" method="POST" data-ajax="false" target="_blank">
    	<input type="hidden" name="outletID" id="DistributorOrderBookerOutletListOutletID"/>
 </form>
 
 <form id="DistributorOrderBookerOutletListDistributorFormID" name="DistributorOrderBookerOutletListDistributorFormID" action="DistributorDashboardOverview.jsp" method="POST" data-ajax="false" target="_blank">
    	<input type="hidden" name="DistributorCode" id="DistributorOrderBookerOutletListDistributorFormIDDistributorCode"/>
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