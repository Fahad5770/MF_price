<%@page import="org.omg.CORBA.portable.RemarshalException"%>
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

<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%

long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 57;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect(Utilities.getAccessDeniedPageURL(request));
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();


String params = request.getParameter("params");

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

//System.out.println(SapOrderNumber);
%>

<script>

function OrderInvoicingGetOrders(CreatedBy){
	
	$.mobile.showPageLoadingMsg();
	$.get('OrderInvoicingGetOrders.jsp?CreatedBy='+CreatedBy, function(data) {
		  $.mobile.hidePageLoadingMsg();
		  $("#OrderList").html(data);
		  $("#OrderList").trigger('create');
		 
	});
	
}

function OrderInvoicingGetOrderDetail(OrderID){
	$.mobile.showPageLoadingMsg();
	$.get('OrderInvoicingGetOrderDetail.jsp?DistributorID=<%=DistributorID%>&OrderID='+OrderID, function(data) {
		
		$.mobile.hidePageLoadingMsg();
		
		  $("#OrderDetail").html(data);
		  $("#OrderDetail").trigger('create');
		 
	});
	
}

function OrderInvoicingDoInvoice(ParamOrderIDs){
	if( ParamOrderIDs!= ""){
		$.mobile.showPageLoadingMsg();
		$.ajax({
		    url: "mobile/OrderInvoicingExecute",
		    data: "DistributorID=<%=DistributorID%>&StatusID=2"+ParamOrderIDs,
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	$.mobile.hidePageLoadingMsg();
		    	window.location='OrderInvoicing.jsp';
		    },
		    error: function( xhr, status ) {
		    	$.mobile.hidePageLoadingMsg();
		    	alert("Server could not be reached.");
		    }
		});
	}
}

function OrderInvoicingDoCancel(ParamOrderID){
	$.mobile.showPageLoadingMsg();
	$.ajax({
	    url: "mobile/OrderInvoicingExecute",
	    data: {
	    	OrderID: ParamOrderID,
	    	StatusID: '3'
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.hidePageLoadingMsg();
	    	window.location='OrderInvoicing.jsp';
	    },
	    error: function( xhr, status ) {
	    	$.mobile.hidePageLoadingMsg();
	    	alert("Server could not be reached.");
	    }
	});
	
}

function OrderInvoicingDoEditAndInvoice(ParamOrderID){
	$('#OrderID').val(ParamOrderID);
	$('#OrderInvoicingDistributorID').val(<%=DistributorID%>);
	
	document.OrderInvoicingEditAndInvoice.submit();
	
}

function checkAll(){
	$('input[name="checkbox-mini"]').prop( "checked", true ).checkboxradio( "refresh" );
}

function unCheckAll(){
	$('input[name="checkbox-mini"]').prop( "checked", false ).checkboxradio( "refresh" );
}

function SerializeOrderIDs(){
	var UrlParams = "";
	var len = $('input[name="checkbox-mini"]').length;
	for(var i = 0; i < len; i++){
		if($('input[name="checkbox-mini"]')[i].checked == true){
			UrlParams += "&OrderID="+$('input[name="checkbox-mini"]')[i].value;
		}
	}
	return UrlParams;
}

function OrderInvoicingGetBackOrderSummary(){
	
	var SerializedOrderIDsString = SerializeOrderIDs();
	if(SerializedOrderIDsString != ''){
		
		
		
		$.mobile.showPageLoadingMsg();
		$.get('OrderInvoicingBackOrderSummary.jsp?DistributorID=<%=DistributorID%>'+SerializedOrderIDsString, function(data) {
			
			$.mobile.hidePageLoadingMsg();
			
			  $("#BackOrderSummary").html(data);
			  $("#BackOrderSummary").trigger('create');
			 
		});
		
	}else{
		$("#BackOrderSummary").html('<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">				<li data-role="list-divider">Back Order Summary</li>				<li style="font-size:13px">&nbsp;</li>			</ul>');
		$("#BackOrderSummary").trigger('create');
	}
}

</script>


<jsp:include page="Header3.jsp" >
    	<jsp:param value="Order Invoicing" name="title"/>
    	
    	<jsp:param value="<%=DistributorName%>" name="HeaderValue"/>

    </jsp:include>

<div data-role="content" data-theme="d">

<table border=0 style="width:100%">
	<tr>
		<td style="width:30%" valign="top">
		
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
				<li data-role="list-divider">Order Bookers</li>
				
				<%
	            int counter2 = 0;
				ResultSet rs = s.executeQuery("SELECT distinct created_by, count(created_by) as no_of_orders, (select DISPLAY_NAME from users where id=created_by limit 1) display_name, (select mobile_no from mobile_devices where uuid = mobile_order.uuid limit 1) mobile_no FROM mobile_order where distributor_id="+DistributorID+" and status_type_id=1 group by created_by order by no_of_orders desc, display_name");
				while(rs.next()){
					String MobileNo = rs.getString("mobile_no");
					if (MobileNo == null){
						MobileNo = "";
					}else{
						MobileNo = "("+MobileNo+")";
					}
					%>
					<li><a data-ajax="false" href="javascript: OrderInvoicingGetOrders(<%=rs.getString("created_by")%>)" style="font-size:13px"><%=rs.getString("created_by")%> - <%=rs.getString("display_name")%> <span style="font-weight: 200; font-size: 11px;"><%=MobileNo%></span><span class="ui-li-count" style="font-size:13px"><%=rs.getString("no_of_orders")%></span></a></li>
					<%
					counter2++;
				}
				
				if(counter2 == 0){
					%>
					<li style="font-size:12px">&nbsp;</li>
					<%
					
				}
				%>
				
				
			</ul>
			
			
		
		</td>
		
		<td style="width:30%" valign="top">
			<div id="OrderList">
				<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
					<li data-role="list-divider">Orders</li>
					<%
					long CreatedBy = Utilities.parseLong(request.getParameter("CreatedBy"));
					ResultSet rs2 = s.executeQuery("SELECT id, outlet_id, (select name from common_outlets where id=mobile_order.outlet_id) outlet_name, net_amount FROM mobile_order where status_type_id=1  and distributor_id = "+DistributorID+" order by net_amount desc");
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
			</div>
		</td>
		
		<td style="width:30%" valign="top">
			<div id="BackOrderSummary">
				<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
					<li data-role="list-divider">Back Order Summary</li>
					<li style="font-size:13px">&nbsp;</li>
				</ul>
			</div>
			<div id="OrderDetail">
				<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
					<li data-role="list-divider">Products</li>
					<li style="font-size:13px">&nbsp;</li>
				</ul>
			</div>
		</td>
		
	</tr>
</table>


	<form name="OrderInvoicingEditAndInvoice" id="OrderInvoicingEditAndInvoice" action="OrderInvoicingEdit.jsp" method="post">
	
		<input type="hidden" name="OrderID" id="OrderID" value="" >
		<input type="hidden" name="OrderInvoicingDistributorID" id="OrderInvoicingDistributorID" value="" >
	
	</form>



</div>
<%
c.close();
ds.dropConnection();
%>