<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<script>
	function redirect(url) {
		document.getElementById("check").action = url;
		document.getElementById("check").submit();
	}
</script>
<%
	long SessionUserID = Long.parseLong((String) session.getAttribute("UserID"));
	int FeatureID = 362;
	long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

	if (UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false) {
		response.sendRedirect("AccessDenied.jsp");
	}

	Datasource ds = new Datasource();
	ds.createConnectionToReplica();
	Connection c = ds.getConnection();
	Statement s = c.createStatement();
	Statement s2 = c.createStatement();
	Statement s3 = c.createStatement();

	long SelectedUserEmployeesArray[] = null;
	if (session.getAttribute(UniqueSessionID + "_SR1UserSelectedEmployees") != null) {
		SelectedUserEmployeesArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1UserSelectedEmployees");
	}

	String Employeeids = "";
	String WhereEmployeeids = "";

	if (SelectedUserEmployeesArray != null && SelectedUserEmployeesArray.length > 0) {
		for (int i = 0; i < SelectedUserEmployeesArray.length; i++) {
			if (i == 0) {
				Employeeids += SelectedUserEmployeesArray[i] + "";
			} else {
				Employeeids += ", " + SelectedUserEmployeesArray[i] + "";
			}
		}
		WhereEmployeeids = "and ua.user_id in (" + Employeeids + ") ";
	}
	
	
	
	
	System.out.println("WhereEmployeeids : "+WhereEmployeeids);
	
	
%>
<ul data-role="listview" data-inset="true"
	style="font-size: 10pt; font-weight: normal; margin-top: -10px;"
	data-icon="false">

	<li data-role="list-divider" data-theme="a">Employee Features List</li>
	<li>
		<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
			
			<%
if(WhereEmployeeids!=""){

%>
			
			<thead>
				<tr style="font-size: 11px;">

					<th data-priority="1"
						style="text-align: center; width: 15%;">Employee</th>
					<th data-priority="1"
						style="text-align: center; width: 15%;">Feature ID</th>
					<th data-priority="1"
						style="text-align: center; width: 48%;">Feature
						Name</th>
				</tr>
			</thead>
			<%
				System.out.print(
						"SELECT ua.user_id,u.display_name,f.feature_id,f.feature_name,f.description,,IF(f.short_code IS NOT NULL, f.short_code, CONCAT('F','-',f.feature_id)),u.ID  FROM pep.user_access ua join users u on ua.user_id=u.id join features f on ua.feature_id=f.feature_id where 1=1 "
								+ WhereEmployeeids+" order by u.ID");
				int intable = 0;
				ResultSet rs10 = s2.executeQuery(
						"SELECT ua.user_id,u.display_name,f.feature_id,IF(f.short_code IS NOT NULL, f.feature_name, CONCAT('F',f.feature_id,'-',f.feature_name)),f.description,IF(f.short_code IS NOT NULL, f.short_code, ''),u.ID FROM pep.user_access ua join users u on ua.user_id=u.id join features f on ua.feature_id=f.feature_id where u.IS_ACTIVE=1 and 1=1 "
								+ WhereEmployeeids+" order by u.ID");
				while (rs10.next())
					while (rs10.next()) {
						if (intable != rs10.getInt(1)) {
			%>
			<tr style="font-size: 12px;">
				<td colspan="3" style="text-align: center;font-size: 14px;background:#ececec">&nbsp;</td>

			</tr>
			<tr style="font-size: 12px;">
				<td style="text-align: left;"><%=rs10.getString(7)%> - <%=rs10.getString(2)%></td>
				<td style="text-align: left;"><%=rs10.getString(6)%></td>
				<td style="text-align: left;"><%=rs10.getString(4)%></td>
			</tr>
			<%
				intable = rs10.getInt(1);
						} else {
			%>
			<tr style="font-size: 12px;">
				<td style="text-align: left;"><%=rs10.getString(7)%> - <%=rs10.getString(2)%></td>
				<td style="text-align: left;"><%=rs10.getString(6)%></td>
				<td style="text-align: left;"><%=rs10.getString(4)%></td>
			</tr>
			<%
				}
					}
			%>
			
			<%
}
else{
%>
<p>Please select filter for Report.
<%} %>
			
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