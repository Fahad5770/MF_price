<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%


Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, 57);


String DistributorName = "";
long DistributorID = 0;
if( UserDistributor != null ){
	if(UserDistributor.length>1) //if it has more than 1 distributor
	{
		response.sendRedirect("AccessDenied.jsp");
	}
	else
	{
		DistributorName = UserDistributor[0].DISTRIBUTOR_NAME;
		DistributorID = UserDistributor[0].DISTRIBUTOR_ID;
	}
}
%>




<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
<li data-role="list-divider">Orders</li>
<%
long CreatedBy = Utilities.parseLong(request.getParameter("CreatedBy"));
ResultSet rs2 = s.executeQuery("SELECT id, outlet_id, (select name from common_outlets where id=mobile_order.outlet_id) outlet_name, net_amount FROM mobile_order where status_type_id=1 and created_by="+CreatedBy+" and distributor_id = "+DistributorID+" order by net_amount desc");
int counter = 0;
while(rs2.next()){
	%>
	<li style="height: 40px">
	<div style="margin-left: 10px; width: 50px; clear: left; float: left; height: 30px;">
	<form>
	<input type="checkbox" name="checkbox-mini" id="checkbox-mini-<%=counter%>" value="<%=rs2.getString("id")%>" class="custom" data-mini="true" onclick="OrderInvoicingGetBackOrderSummary()">
	<label for="checkbox-mini-<%=counter%>">&nbsp;</label>
	</form>
	</div>
	<a data-ajax="false" href="javascript: OrderInvoicingGetOrderDetail(<%=rs2.getString("id")%>)" style="font-size:12px"><%=rs2.getString("outlet_id")+" - "+rs2.getString("outlet_name")%><span class="ui-li-count" style="font-size:13px"><%=Utilities.getDisplayCurrencyFormat(rs2.getDouble("net_amount"))%></span></a>
	</li>
	<%
	counter++;
}

if(counter == 0){
	%>
	<li style="font-size:13px">&nbsp;</li>
	<%
}

%>

</li>
		<li data-role="list-divider" data-theme="c">Action</li>
		<li>
		
			<table>
				<tr>
					<td><input type="button" value="Check All" data-mini="true" data-theme="d" onclick="checkAll(); OrderInvoicingGetBackOrderSummary()"  ></td>
					<td><input type="button" value="Uncheck All" data-mini="true" data-theme="d" onclick="unCheckAll(); OrderInvoicingGetBackOrderSummary()"  ></td>
					<td><input type="button" value="Invoice Selected" data-mini="true" data-theme="a" onclick="OrderInvoicingDoInvoice(SerializeOrderIDs())"  ></td>
					
					
				</tr>
			</table>
		

		</li>

</ul>

<%

c.close();
ds.dropConnection();
%>