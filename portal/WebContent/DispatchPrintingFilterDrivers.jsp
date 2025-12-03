<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.Date"%>
<%


Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

Date DispatchDate = Utilities.parseDate(request.getParameter("DispatchPrintingDriverDateFilter"));
long DistributorID = Utilities.parseLong(request.getParameter("DistributorID"));

%>

<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
	<li data-role="list-divider">Dispatch</li>
	
	<%
		int counter = 0;
		
		ResultSet rs = s.executeQuery("SELECT distinct vehicle_id, (SELECT vehicle_no FROM distribtuor_vehicles where id=vehicle_id) vehicle_name, driver_id, (SELECT name FROM distributor_employees where id=driver_id) driver_name, id,dispatch_type FROM inventory_sales_dispatch where distributor_id="+DistributorID+" and created_on between "+Utilities.getSQLDate(DispatchDate)+" and "+Utilities.getSQLDateNext(DispatchDate));
		//System.out.println("SELECT distinct vehicle_id, (SELECT vehicle_no FROM distribtuor_vehicles where id=vehicle_id) vehicle_name, driver_id, (SELECT name FROM distributor_employees where id=driver_id) driver_name, id FROM inventory_sales_dispatch where distributor_id="+DistributorID+" and created_on between "+Utilities.getSQLDate(DispatchDate)+" and "+Utilities.getSQLDateNext(DispatchDate));
		while(rs.next()){
			%>
			<%
			if(rs.getInt("dispatch_type") == 2){
			%>
			<li><a data-ajax="false" href="javascript: DispatchPrintingGetInvoices(<%=rs.getString("id")%>)" style="font-size:13px">By Hand<span class="ui-li-count" style="font-size:13px"><%=rs.getString("id")%></span></a></li>	
			<%}
			else{
			%>
			<li><a data-ajax="false" href="javascript: DispatchPrintingGetInvoices(<%=rs.getString("id")%>)" style="font-size:13px"><%=rs.getString("driver_name")%> <span style="font-weight: normal;"><%=rs.getString("vehicle_name")%></span><span class="ui-li-count" style="font-size:13px"><%=rs.getString("id")%></span></a></li>
			<%
			}
			counter++;
		}
		
		if(counter == 0){
			%>
				<li style="font-size: 13px">&nbsp;</li>
			<%
		}
		
	%>
</ul>

<%

c.close();
ds.dropConnection();
%>