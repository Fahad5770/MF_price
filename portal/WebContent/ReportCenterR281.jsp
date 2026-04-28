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
int FeatureID = 351;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));
if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}
//
Datasource ds = new Datasource();
ds.createConnection();
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

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}
boolean IsOutletSelected=false;
String OutletIds="";
long SelectedOutletArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets") != null){
	IsOutletSelected=true;
	SelectedOutletArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets");
	OutletIds = Utilities.serializeForSQL(SelectedOutletArray);
}

String WhereOutlets = "";
if (OutletIds.length() > 0){
	WhereOutlets = " and outlet_id in ("+OutletIds+") ";	
}

boolean IsOrderBookerSelected=false;

int OrderBookerArrayLength=0;
long SelectedOrderBookerArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers");
	
	IsOrderBookerSelected=true;
	OrderBookerArrayLength=SelectedOrderBookerArray.length;
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


//Distributor

long SelectedDistributorsArray[] = null;
boolean IsDistributorSelected=false;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	IsOrderBookerSelected=true;
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors"); 
	IsDistributorSelected = true;
}else{
}

String DistributorIDs = "";
String WhereDistributors = "";

String WhereDistributors1 = "";
if(SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0){
	for(int i = 0; i < SelectedDistributorsArray.length ; i++){
		
		if(i == 0){
			DistributorIDs += SelectedDistributorsArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorsArray[i];
		}
	}
	WhereDistributors = " and created_by in ( select distinct dbpv.assigned_to from distributor_beat_plan_view dbpv  where dbpv.distributor_id in ("+DistributorIDs+") UNION select distinct dbpv.asm_id from distributor_beat_plan_view dbpv  where dbpv.distributor_id in("+DistributorIDs+") UNION SELECT kpo_id FROM pep.common_distributors_kpos where distributor_id in ("+DistributorIDs+"))";
	
	WhereDistributors1 = " and outlet_id in (select id from common_outlets where distributor_id in ("+DistributorIDs+")) ";
	
	//System.out.println(WhereDistributors);
}


%>


<script type="text/javascript">



	function redirect(url){
		document.getElementById("check").action = url;
		document.getElementById("check").submit();
	}
	
	
	
	function OrderBookerGetOrderActivityReport(flag,CreatedBy,date){
		
		//alert("OrderBookerGetOrderActivityReport Function");
		var url = 'ReportCenterR281AttendanceSummary.jsp?CreatedBy='+CreatedBy+'&MOrder=0&flag='+flag+'&d='+date+'&UniqueSessionID=<%=UniqueSessionID%>';
		if(CreatedBy == 'undefined'){
			url = 'ReportCenterR116OrderSummary.jsp?flag=1&UniqueSessionID=<%=UniqueSessionID%>';
		}
		$("#OrderActivityReportTD").html("<img src='images/snake-loader.gif'>");
		
		$.mobile.showPageLoadingMsg();
		$.get(url, function(data) {
			  $.mobile.hidePageLoadingMsg();
			  $("#OrderActivityReportTD").html(data);
			  $("#OrderActivityReportTD").trigger('create');
			 
		});
		
	}
	
	
	function DistReportsOrderBookerOrderListOutletDashboardRedirect(OutletID1){
		//alert(OutletID1);
		$("#DistReportsOrderBookerOrderListOutletID").val(OutletID1);
		document.getElementById("DistReportsOrderBookerOrderListFormID").submit();
	}
	
	<%if(IsOrderBookerSelected || IsOutletSelected){ 
		%>
		//OrderBookerGetOrderActivityReport(1,0,0);
		<%
	}%>
</script>

<table border="0" style="width: 100%">
	<tr>
		
		
		<td style="width: 30%" valign="top">
		
			
			
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="c" data-count-theme="c" style="margin-top:-13px;">
				<li data-role="list-divider" data-theme="a">Orders</li>
				
		
				
				<%
				Date LastDate = Utilities.parseDate("01/01/1997");
				//System.out.println("SELECT id, outlet_id, (SELECT name FROM common_outlets where id=mobile_order.outlet_id) outlet_name, net_amount, created_on FROM mobile_order where status_type_id in (1,2) and "+OrderBookerIDsWher+" created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereOutlets+" order by created_on desc");
				
				
				//System.out.println("select distinct created_by, (select display_name from users u where u.id=created_by) name, date(created_on) created_on, date(mobile_timestamp) mobile_timestamp from mobile_order_booker_attendance where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereDistributors);
				
				ResultSet rs22 = s.executeQuery("select distinct created_by, (select display_name from users u where u.id=created_by) name, date(created_on) created_on, date(mobile_timestamp) mobile_timestamp from mobile_order_booker_attendance where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereDistributors);
				int counter = 0;
				while(rs22.next()){
					
						
					Date MobileTimestamp = rs22.getTimestamp("mobile_timestamp");
					
							
					if(!DateUtils.isSameDay(LastDate, rs22.getDate("created_on")) ){
	            		%>
	            		<li data-role="list-divider" style="font-size: 10px;"><%=Utilities.getDisplayFullDateFormat(rs22.getDate("created_on"))%></li>
	            		<%
	            	}
					
					
					%>
					<li style="height: 40px" >
						<a data-ajax="false" href="javascript: OrderBookerGetOrderActivityReport(1,<%=rs22.getString("created_by")%>,'<%=Utilities.getDisplayDateFormat(rs22.getDate("created_on")) %>')" style="font-size:10px"><%=rs22.getString("created_by")+" - "+rs22.getString("name")%><br>Mobile Timestamp: <%=Utilities.getDisplayDateFormat(MobileTimestamp)  %></a>
					</li>
					<%
					LastDate = rs22.getDate("created_on");
					counter++;
				}
				
				%>
				
						
				
			</ul>
		
		
		
		</td>
		<td style="width: 70%" valign="top" id="OrderActivityReportTD">&nbsp;</td>
			
				
		
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