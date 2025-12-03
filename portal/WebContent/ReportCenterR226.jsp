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
<%@page import="com.pbc.common.Region"%>
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
int FeatureID = 287;

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

//System.out.print("Date = "+EndDate+"\n");

int Year = Utilities.getYearByDate(StartDate);
int Month = Utilities.getMonthNumberByDate(StartDate);

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}

//out.print("StartDate = "+StartDate);
//out.print("EndDate = "+EndDate);

//region

Region [] RegionObj = UserAccess.getUserFeatureRegion(SessionUserID, FeatureID);
String RegionIDs = UserAccess.getRegionQueryString(RegionObj);

long SelectedRegionArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRegion") != null){
	SelectedRegionArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRegion");
	RegionIDs = Utilities.serializeForSQL(SelectedRegionArray);
}

String WhereRegion = "";

if (RegionIDs.length() > 0){
	WhereRegion = " and cd.region_id in ("+RegionIDs+") ";	
}


%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Secondary Sales</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">					 
				<thead>
					<tr style="font-size:11px;">
						<th style="text-align: center">Distributor</th>
						<th style="text-align: center">Outlet</th>
						<th style="text-align: center">Package</th>
						<th style="text-align: center">Quantity</th>					 
					</tr>
				</thead>
				
				<tbody>
					
					<%
					System.out.println("SELECT om.customer_id,om.customer_name,ss.outlet_id,sa.outlet_name,pv.package_label,sum(ss.quty_quant) qty FROM sap_sales ss,sampling sa,outletmaster om,inventory_products_view pv, common_distributors cd where sa.outlet_id=ss.outlet_id and om.outlet_id=sa.outlet_id and pv.sap_code=ss.material_matnr and om.customer_id=cd.distributor_id "+WhereRegion+" and sa.active= 1 and month_zmonth = "+Month+" and year_zyear = "+Year+" group by ss.outlet_id, pv.package_label order by om.customer_id");
					ResultSet rs = s.executeQuery("SELECT om.customer_id,om.customer_name,ss.outlet_id,sa.outlet_name,pv.package_label,sum(ss.quty_quant) qty FROM sap_sales ss,sampling sa,outletmaster om,inventory_products_view pv, common_distributors cd where sa.outlet_id=ss.outlet_id and om.outlet_id=sa.outlet_id and pv.sap_code=ss.material_matnr and om.customer_id=cd.distributor_id "+WhereRegion+" and sa.active= 1 and month_zmonth = "+Month+" and year_zyear = "+Year+" group by ss.outlet_id, pv.package_label order by om.customer_id");
					while(rs.next()){
						%>
						<tr style="font-size:11px;">
							<td astyle="text-align: center"><%=rs.getString("customer_id")+" - "+rs.getString("customer_name")%></td>
							<td astyle="text-align: center"><%=rs.getString("outlet_id")+" - "+rs.getString("outlet_name")%></td>
							<td astyle="text-align: center"><%=rs.getString("package_label")%></td>
							<td style="text-align: right"><%=rs.getString("qty")%></td>					 
						</tr>
						<%
					}
					%>
					
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