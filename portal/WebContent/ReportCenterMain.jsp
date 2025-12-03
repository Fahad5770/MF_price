<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>

<%

long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 99;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect(Utilities.getAccessDeniedPageURL(request));
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();


String params = request.getParameter("params");



//System.out.println(SapOrderNumber);
%>

<script>
</script>

<div data-role="page" id="ReportCenterMain" data-url="ReportCenterMain" data-theme="d">

<jsp:include page="Header2.jsp" >
    	<jsp:param value="Report Center" name="title"/>
    </jsp:include>

<div data-role="content" data-theme="d">
<br/>
<table border=0 style="width:75%" align="center">
	<tr>
		<td style="width:30%" valign="top">
		
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c" data-filter="true" data-filter-placeholder="Search ..." data-mini="true">
				<%
            ResultSet rs = s.executeQuery("select group_id, group_name from feature_groups where group_id = 10 order by sort_order");
            while(rs.next()){
            %>
				<li data-role="list-divider"><%=rs.getString("group_name") %></li>
				
				<%
            	ResultSet rs2 = s2.executeQuery("select feature_name, url, ajax_call,description from features where group_id = "+rs.getString(1)+" and active = 1 and feature_id in (select feature_id from user_access where user_id = "+SessionUserID+") order by sort_order");
            	while(rs2.next()){
				%>
				<li>
					<a data-ajax="false" href="<%=rs2.getString("url") %>&<%=Math.random() %><%=Math.random() %>=<%=Math.random() %><%=Math.random() %>" style="font-size:13px">
						<%=rs2.getString("feature_name") %>
						<br/><span style="padding-top:10px; font-weight:normal;"><%=rs2.getString("description") %></span>
					</a>
				</li>
				
			<%
            	}
            }
			%>	
				
			</ul>
			
			
		
		</td>
		<!-- 
		<td style="width:70%" valign="top">
			<div id="OrderList">
				<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
					<li data-role="list-divider">Orders</li>
					<li><a data-ajax="false" href="" style="font-size:11px;font-weight:normal">Sales Activity Summary</a></li>
					<li><a data-ajax="false" href="" style="font-size:11px">Stock Summary</a></li>
					
					</ul>
			</div>
		</td>
		
	 -->
		
	</tr>
</table>


	<form name="OrderInvoicingEditAndInvoice" id="OrderInvoicingEditAndInvoice" action="OrderInvoicingEdit.jsp" method="post">
	
		<input type="hidden" name="OrderID" id="OrderID" value="" >
		<input type="hidden" name="OrderInvoicingDistributorID" id="OrderInvoicingDistributorID" value="" >
	
	</form>



</div>
</div>
<%
c.close();
ds.dropConnection();
%>