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
int FeatureID = 92;

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




long SelectedDistributorArray[] = null;
if (session.getAttribute("SR1SelectedDistributors") != null){
	SelectedDistributorArray = (long[])session.getAttribute("SR1SelectedDistributors");           	
}



String DistributorIDs = "";
if(SelectedDistributorArray != null && SelectedDistributorArray.length > 0){
	for(int i = 0; i < SelectedDistributorArray.length; i++){
		//System.out.println("zulqurnan");
		if(i == 0){
			DistributorIDs += SelectedDistributorArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorArray[i];
		}
	}
}

%>


<script>



function redirect(ReceiptFromProdMainID){
	
	//alert(SapOrderNo);
	var url = 'DistributorReportsReceiptFromProductionGRNSummary.jsp?ReceiptFromProdMainID='+ReceiptFromProdMainID;
	/*if(OrderID == ''){
		url = 'DistributorReportsLiftingReportSummary.jsp';
	}*/
	$("#OrderActivityReportTD").html("<img src='images/snake-loader.gif'>");
	
	$.mobile.showPageLoadingMsg();
	$.get(url, function(data) {
		  $.mobile.hidePageLoadingMsg();
		  $("#OrderActivityReportTD").html(data);
		  $("#OrderActivityReportTD").trigger('create');
		 
	});
}


	
	
	function OrderBookerGetOrderActivityReport(OrderID){
		
		var url = 'DistributorReportsReceiptFromProductionGRNSummary.jsp?OrderID='+OrderID;
		if(OrderID == ''){
			url = 'DistributorReportsReceiptFromProductionGRNSummary.jsp';
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
				<li data-role="list-divider" data-theme="a">Receipt From Production</li>
				<%
				Date LastDate = Utilities.parseDate("01/01/1997");
				//System.out.println("select * inventory_production_receipts where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" order by created_on desc");
				ResultSet rs22 = s.executeQuery("select * from inventory_production_receipts where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and is_received = 1 order by created_on desc");
				
				int counter = 0;
				
				
				
				
				while(rs22.next()){
					
					
					if(!DateUtils.isSameDay(LastDate, rs22.getDate("created_on")) ){
	            		
	            		
	            		
	            		%>
	            		<li data-role="list-divider" style="font-size: 12px;"><%=Utilities.getDisplayFullDateFormat(rs22.getDate("created_on"))%>
	            		
	            		</li>
	            		<%
	            	}
					
					
					%>
					<li><a data-ajax="false" href="#" onclick="redirect(<%=rs22.getString("id") %>)" style="font-size:13px; font-weight:normal;">Receipt# <b><%=rs22.getString("id") %></b>&nbsp;&nbsp;<%if(rs22.getInt("is_received")==1){ %>[Received]<%}else{ %>[Pending]<%} %><br/><%=Utilities.getDisplayDateTimeFormat(rs22.getTimestamp("created_on"))  %>&nbsp;
					<span class="ui-li-count" style="font-size:13px"><%=rs22.getString("vehicle_no") %></span>
					</a></li>
					
					<%
					
					LastDate = rs22.getDate("created_on");
					counter++;
				}
				%>
				
				
	           
				<%if(counter==0){
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