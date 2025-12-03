<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%
if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Statement s = ds.createStatement();

long OutletID = Utilities.parseLong(request.getParameter("OutletID"));
boolean isActive = false;
long RequestID = 0;
long SamplingID = 0;
Date ActivatedOn = null;

ResultSet rs = s.executeQuery("select request_id, sampling_id, activated_on from sampling where outlet_id = "+OutletID+" and active = 1");
if (rs.first()){
	isActive = true;
	RequestID = rs.getLong(1);
	SamplingID = rs.getLong(2);
	ActivatedOn = rs.getDate(3);
}

s.close();
ds.dropConnection();
%>
<ul data-role="listview" data-inset="true" style="margin-left: 10px;margin-right: 10px;">
	<li data-role="list-divider">Discount Status</li>
	<li>
	<%
	if (isActive){
	%>
	<table style="width: 100%">
		<tr>
			<td>
		    <label for="DeactivationRequestID"><span style="padding-left: 7px;">Request ID</span><input type="text" id="DeactivationRequestID" name="DeactivationRequestID" value="<%=RequestID%>" readonly="readonly"></label>
		    </td>
		    <td>
		    <label for="DeactivationSamplingID">Sampling ID</label><input type="text" id="DeactivationSamplingID" name="DeactivationSamplingID" value="<%=SamplingID%>" readonly="readonly">
		    </td>
		    <td>
		    <label for="DeactivationActivatedOn">Activated On</label><input type="text" id="DeactivationActivatedOn" name="DeactivationActivatedOn" value="<%= Utilities.getDisplayDateFormat(ActivatedOn) %>" readonly="readonly">
		    </td>
		    <td>
		    <label for="DeactivationActivatedOn">Deactivation Date</label><input type="text" id="DeactivationActivatedOn" name="DeactivatedOn" value="<%= Utilities.getDisplayDateFormat(new Date()) %>" >
		    </td>
		</tr>
	</table>
	<%
	}else{
	%>
	No active discounts.
	<%
	}
	%>
	</li>
</ul>