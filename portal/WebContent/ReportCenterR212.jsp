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
int FeatureID = 266;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

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

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}




long SelectedDistributorArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");           	
}


boolean IsDistributorSelected=false;
String DistributorIDs = "";
if(SelectedDistributorArray != null && SelectedDistributorArray.length > 0){
	IsDistributorSelected = true;
	for(int i = 0; i < SelectedDistributorArray.length; i++){
		//System.out.println("zulqurnan");
		if(i == 0){
			DistributorIDs += SelectedDistributorArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorArray[i];
		}
	}
}


String SecondaryDistributorString="";
int SecondaryDistributor=0;

String WhereSecDistributor="";

SecondaryDistributorString=(String)session.getAttribute("UserDistributorID");
SecondaryDistributor = Utilities.parseInt(SecondaryDistributorString);

%>


<script>



function redirect(DistributorID, SapOrderNo){
	
	//alert(SapOrderNo);
	var url = 'ReportCenterR212LiftingSummary.jsp?SapOrderNo='+SapOrderNo+'&UniqueSessionID=<%=UniqueSessionID%>';
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
		
		var url = 'ReportCenterR212LiftingSummary.jsp?OrderID='+OrderID+'&UniqueSessionID=<%=UniqueSessionID%>';
		if(OrderID == ''){
			url = 'ReportCenterR212LiftingSummary.jsp?UniqueSessionID=<%=UniqueSessionID%>';
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
				//ResultSet rs_orders  = s.executeQuery("select idn.delivery_id,idn.is_delivered,idn.is_received, idn.distributor_id,sap_order_no,idn.created_on,created_by,concat(first_name,' ',last_name) name,sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from users u  ,inventory_delivery_note idn,  common_distributors cd,inventory_delivery_note_products idnp, inventory_packages ipa,inventory_products ip where  idn.delivery_id=idnp.delivery_id and idnp.product_id=ip.id and ip.package_id=ipa.id and ip.category_id = 1 and u.id=idn.created_by and idn.distributor_id = cd.distributor_id and idn.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+ " and idn.distributor_id in ("+SelectedOrderBookerArray+") group by idn.delivery_id order by created_on desc");
				//System.out.println("select idn.delivery_id,idn.is_delivered,idn.is_received, idn.distributor_id,sap_order_no,idn.created_on,created_by,concat(first_name,' ',last_name) name,sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases,idn.invoice_amount from users u  ,inventory_delivery_note idn,  common_distributors cd,inventory_delivery_note_products idnp, inventory_packages ipa,inventory_products ip where  idn.delivery_id=idnp.delivery_id and idnp.product_id=ip.id and ip.package_id=ipa.id and ip.category_id = 1 and u.id=idn.created_by and idn.distributor_id = cd.distributor_id and idn.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+ " and idn.distributor_id in ("+DistributorIDs+") group by idn.delivery_id order by created_on desc");
				ResultSet rs22 = s.executeQuery("select idn.delivery_id,idn.is_delivered,idn.is_received, idn.distributor_id,sap_order_no,idn.created_on,created_by,concat(first_name,' ',last_name) name,sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases,idn.invoice_amount, idn.barcode from users u  ,inventory_delivery_note idn,  common_distributors cd,inventory_delivery_note_products idnp, inventory_packages ipa,inventory_products ip where  idn.delivery_id=idnp.delivery_id and idnp.product_id=ip.id and ip.package_id=ipa.id and ip.category_id = 1 and u.id=idn.created_by and idn.distributor_id = cd.distributor_id and idn.created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+ " and idn.distributor_id ="+SecondaryDistributor+" group by idn.delivery_id order by created_on desc");
				
				int counter = 0;
				double DateWiseTotal=0;
				double GrandTotal=0;
				boolean IsInvoiceAmountLiftingReport=false;
				//checking if this user has the rights of invoice amount lifting report then show it to all the invoices amounts
				int InvoiceAmountFeatureID = 49;				
				if(!Utilities.isAuthorized(InvoiceAmountFeatureID, SessionUserID) == false){					
					IsInvoiceAmountLiftingReport=true;
				}
				
				while(rs22.next()){
					
					
					if(!DateUtils.isSameDay(LastDate, rs22.getDate("created_on")) ){
	            		
	            		//System.out.println("select idn.delivery_id,date(idn.created_on),invoice_amount,sum(invoice_amount) sub_total from inventory_delivery_note idn where  date(idn.created_on) ='"+Utilities.getSQLDate(rs22.getDate("created_on"))+"' and idn.distributor_id in (100669) group by date(idn.created_on)  order by idn.created_on desc");
						ResultSet rs3 = s2.executeQuery("select sum(invoice_amount) sub_total from inventory_delivery_note idn where idn.created_on between "+Utilities.getSQLDateLifting(rs22.getDate("created_on"))+" and "+Utilities.getSQLDateNextLifting(rs22.getDate("created_on"))+" and idn.distributor_id ="+SecondaryDistributor);
	            		if(rs3.first()){
	            			DateWiseTotal = rs3.getDouble("sub_total");
	            		}
	            		
	            		%>
	            		<li data-role="list-divider" style="font-size: 12px;"><%=Utilities.getDisplayFullDateFormat(rs22.getDate("created_on"))%>
	            		<%if(IsInvoiceAmountLiftingReport){ %>
	            			<span class="ui-li-count" style="font-size:13px"><%=Utilities.getDisplayCurrencyFormatRounded(DateWiseTotal) %></span>
	            		<%} %>
	            		</li>
	            		<%
	            	}
					
					
					%>
					<li><a data-ajax="false" href="#" onclick="redirect(<%=rs22.getString("idn.distributor_id")%>, <%=rs22.getString("sap_order_no")%>)" style="font-size:13px; font-weight:normal;">Order# <%=rs22.getString("sap_order_no") %><br/>Barcode# <%=rs22.getString("barcode") %>&nbsp;&nbsp;<%=Utilities.getDisplayDateTimeFormat(rs22.getTimestamp("created_on"))  %>&nbsp;<%
					
					String msg = "";
					if(rs22.getInt("is_delivered")==1){
						msg="[Delivered]";
					}  
					if(rs22.getInt("is_received")==1){
						msg="[Received]";
					}  
					if(rs22.getInt("is_delivered")==0 && rs22.getInt("is_received")==0){
						msg="[Pending]";
					} 
					out.println(msg);
					%><%if(IsInvoiceAmountLiftingReport){ %><span class="ui-li-count" style="font-size:13px"><%=Utilities.getDisplayCurrencyFormatRounded(rs22.getDouble("invoice_amount")) %></span><%} %></a></li>
					
					<%
					GrandTotal += rs22.getDouble("invoice_amount");
					LastDate = rs22.getDate("created_on");
					counter++;
				}
				%>
				<%if(IsInvoiceAmountLiftingReport){ %>
				<li data-role="list-divider" style="font-size: 12px;">Grand Total            		
	            	<span class="ui-li-count" style="font-size:13px"><%=Utilities.getDisplayCurrencyFormatRounded(GrandTotal) %></span>
	            </li>
	            <%} %>
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