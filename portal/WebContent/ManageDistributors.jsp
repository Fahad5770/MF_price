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

int FeatureID = 97;

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}


Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

%>

<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<script src="js/ManageDistributors.js"></script>
<script src="js/lookups.js"></script>

<div data-role="page" id="ManageDistributors" data-url="ManageDistributors" data-theme="d">

	<jsp:include page="Header2.jsp" >
    	<jsp:param value="Manage Distributors" name="title"/>
    </jsp:include>
 
	<div data-role="content" data-theme="d">
	<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
	<li data-role="list-divider" data-theme="a">Filter By</li>
	<li>
		<table style="width: 30%">
			<tr>
				<td>
					<select name="region" id="region" data-mini="true" onchange="getDistributorsListByRegion(this.value)" style="width: 100%" >
						<option value="">Select Regions</option>
						<%
						ResultSet rs_regions = s.executeQuery("select * from common_regions where region_id!=9 order by region_short_name");
						while(rs_regions.next()){
							%>
							<option value="<%=rs_regions.getString("region_id")%>"><%=rs_regions.getString("region_short_name")%> - <%=rs_regions.getString("region_name")%></option>
							<%
						}
						%>
					</select>
				</td>
			</tr>
		</table>
	</li>
	
	<li data-role="list-divider" data-theme="a">Distributors</li>
	<li>
		<form name="ManageDistributorsForm" id="ManageDistributorsForm" action="#" method="post">
			<div id="DistributorsDIV">
			&nbsp;
			</div>
		</form>
		</li>	
	</ul>
	</div>

<div data-role="footer" data-position="fixed" data-theme="b">

	<table style="width: 100%;">
		<tr>
			<td>
				<a data-icon="check" data-theme="a" data-role="button" data-inline="true"  href="#" onClick="ManageDistributorSubmit();">Save</a>
			</td>
		</tr>
	</table>

</div>

<jsp:include page="LookupEmployeeSearchPopup.jsp" > 
	<jsp:param value="EmployeeSearchCallBackManageDistributor" name="CallBack" />
</jsp:include><!-- Include Employee Search -->

</div>
<%
s.close();
ds.dropConnection();
%>
