<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>

<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 41;

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

//Date date = Utilities.parseDate(request.getParameter("Date"));

Date StartDate = (Date)session.getAttribute("SR1StartDate");
Date EndDate = (Date)session.getAttribute("SR1EndDate");

if(session.getAttribute("SR1StartDate") == null){
	StartDate = new Date();
}

if(session.getAttribute("SR1EndDate") == null){
	EndDate = new Date();
}

String OutletIds="";
long SelectedOutletArray[] = null;
if (session.getAttribute("SR1SelectedOutlets") != null){
	SelectedOutletArray = (long[])session.getAttribute("SR1SelectedOutlets");
	OutletIds = Utilities.serializeForSQL(SelectedOutletArray);
}

String WhereOutlets = "";
if (OutletIds.length() > 0){
	WhereOutlets = " and outlet_id in ("+OutletIds+") ";	
}


long SelectedOrderBookerArray[] = null;
if (session.getAttribute("SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute("SR1SelectedOrderBookers");           	
}



String OrderBookerIDs = "";
if(SelectedOrderBookerArray != null && SelectedOrderBookerArray.length > 0){
	for(int i = 0; i < SelectedOrderBookerArray.length; i++){
		if(i == 0){
			OrderBookerIDs += SelectedOrderBookerArray[i];
		}else{
			OrderBookerIDs += ", "+SelectedOrderBookerArray[i];
		}
	}
}
String OrderBookerIDsWher="";
if(OrderBookerIDs.length()>0){
	OrderBookerIDsWher =" and created_by in ("+OrderBookerIDs+") ";
}
%>


<script>

	function redirect(url){
		document.getElementById("check").action = url;
		document.getElementById("check").submit();
	}
	
	
	function OrderBookerGetOrderActivityReport(OrderID){
		
		var url = 'DistributorReportsOrderBookerOrderSummary.jsp?OrderID='+OrderID;
		if(OrderID == ''){
			url = 'DistributorReportsOrderBookerOrderSummary.jsp';
		}
		$("#OrderActivityReportTD").html("<img src='images/snake-loader.gif'>");
		
		$.mobile.showPageLoadingMsg();
		$.get(url, function(data) {
			  $.mobile.hidePageLoadingMsg();
			  $("#OrderActivityReportTD").html(data);
			  $("#OrderActivityReportTD").trigger('create');
			 
		});
		
	}
	
	OrderBookerGetOrderActivityReport();
	function DistReportsOrderBookerOrderListOutletDashboardRedirect(OutletID1){
		//alert(OutletID1);
		$("#DistReportsOrderBookerOrderListOutletID").val(OutletID1);
		document.getElementById("DistReportsOrderBookerOrderListFormID").submit();
	}
</script>

<table border="0" style="width: 100%">
	<tr>
		<td style="width: 40%" valign="top">
		
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="c" data-count-theme="c" style="margin-top:-13px;">
				<li data-role="list-divider" data-theme="a">Orders</li>
				<%
				Date LastDate = Utilities.parseDate("01/01/1997");
				//System.out.println("SELECT id, outlet_id, (SELECT name FROM common_outlets where id=mobile_order.outlet_id) outlet_name, net_amount, created_on FROM mobile_order where status_type_id in (1,2) and "+OrderBookerIDsWher+" created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereOutlets+" order by created_on desc");
				ResultSet rs22 = s.executeQuery("SELECT id, outlet_id, (SELECT name FROM common_outlets where id=mobile_order.outlet_id) outlet_name, net_amount, created_on FROM mobile_order where status_type_id in (1,2)  "+OrderBookerIDsWher+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereOutlets+" order by date(created_on) desc, net_amount desc");
				int counter = 0;
				while(rs22.next()){
					
					
					if(!DateUtils.isSameDay(LastDate, rs22.getDate("created_on")) ){
	            		%>
	            		<li data-role="list-divider" style="font-size: 12px;"><%=Utilities.getDisplayFullDateFormat(rs22.getDate("created_on"))%></li>
	            		<%
	            	}
					
					
					%>
					<li style="height: 40px" >
						<a data-ajax="false" href="javascript: OrderBookerGetOrderActivityReport(<%=rs22.getString("id")%>)" style="font-size:12px" <%if(Utilities.isAuthorized(31, SessionUserID) == true){%>ondblclick="DistReportsOrderBookerOrderListOutletDashboardRedirect(<%=rs22.getString("outlet_id")%>)"<%} %>><%=rs22.getString("outlet_id")+" - "+rs22.getString("outlet_name")%><span class="ui-li-count" style="font-size:13px"><%=Utilities.getDisplayCurrencyFormat(rs22.getDouble("net_amount"))%></span></a>
					</li>
					<%
					LastDate = rs22.getDate("created_on");
					counter++;
				}
				if(counter==0){
					%>
					<li style="height: 12px">&nbsp;</li>
					<%
				}
					%>		
				
			</ul>
		
		</td>
		<td style="width: 60%" valign="top" id="OrderActivityReportTD">&nbsp;</td>
	</tr>
</table>
 <form id="DistReportsOrderBookerOrderListFormID" name="DistReportsOrderBookerOrderListFormID" action="OutletDashboard.jsp" method="POST" data-ajax="false" target="_blank">
    	<input type="hidden" name="outletID" id="DistReportsOrderBookerOrderListOutletID"/>
 </form>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>