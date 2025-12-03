
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
int FeatureID = 61;

long DistributorID = 0;
String DistributorName = "";

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}else{
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);


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

$( document ).ready(function() {
	DispatchPrintingFilterDrivers();
});

function DispatchPrintingGetInvoices(DispatchID){
	
	$.get('DispatchPrintingGetInvoices.jsp?DispatchID='+DispatchID, function(data) {
		
		  $("#InvoiceList").html(data);
		  $("#InvoiceList").trigger('create');
		 
	});
	
}


function DispatchPrintingFilterDrivers(){
	
	$.get('DispatchPrintingFilterDrivers.jsp?DispatchPrintingDriverDateFilter='+$('#DispatchPrintingDriverDateFilter').val()+'&DistributorID=<%=DistributorID%>', function(data) {
		
		  $("#DispatchPrintingDriverDIV").html(data);
		  $("#DispatchPrintingDriverDIV").trigger('create');
		 
	});
	
}



</script>


<div data-role="header" data-theme="c" data-position="fixed">
    <div>
	    <div style="float:left; width:10%">
	    	<a href="home.jsp" data-role="button" data-theme="d" data-inline="true" data-ajax="false" ><img src="images/logofull.svg" style="width: 50px"></a>
	    </div>
	    <div style="float:left; width:90%;b1ackground-color:Red; text-align:center;"><h1 style="font-size: 14pt;float:left; margin-left:45%; text-align:center;">Dispatch Printing</h1>
	    
		    <div data-role="navbar" style="width:20%; float:right; margin-right:3px; margin-top:10px;">
		    	<ul>
		        	<li><a href="DispatchPrinting.jsp" class="ui-btn-active" data-ajax="false">Dispatched</a></li>
		        	<li><a href="DispatchAdjustedPrinting.jsp" data-ajax="false">Adjusted</a></li>
		    	</ul>
			</div><!-- /navbar -->
		</div>
	</div>
</div>

<div data-role="content" data-theme="d">

<table border=0 style="width:100%">
	<tr>
		<td style="width:30%" valign="top">
			<div id="DispatchPrintingDriverDIV">
				
			</div>
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
				
				<li data-role="list-divider" data-theme="a">Dispatched On</li>
				<li>
				
					<table style="width: 100%">
						<tr>
							<td><label for="DispatchPrintingDriverDateFilter" style="margin:0px">Date</label></td>
						</tr>
						<tr>
							<td><input id="DispatchPrintingDriverDateFilter" name="DispatchPrintingDriverDateFilter" type="text" placeholder="DD/MM/YYYY" data-mini="true" value="<%=Utilities.getDisplayDateFormat(new java.util.Date())%>"  ></td>
						</tr>
					</table>
				
				</li>
				<li data-icon="check" >
					<a href="#" onClick="DispatchPrintingFilterDrivers()" data-iconpos="left">Apply</a>     
				</li>
			</ul>
			
			
		</td>
		
		<td style="width:70%" valign="top">
			<div id="InvoiceList">
			
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td valign="top" style="width:30%">
						<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
							
							<li data-role="list-divider">Invoices</li>
							
							<li style="font-size:13px">&nbsp;</li>
							
						</ul>
					</td>
					<td valign="top" style="width:30%; padding-left: 5px">
						<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
							
							<li data-role="list-divider">Summary</li>
							
							<li style="font-size:13px">&nbsp;</li>
							
						</ul>
					</td>
				</tr>
			</table>
			
				
			</div>
		</td>
		<!-- 
		<td style="width:30%" valign="top">
			<div id="InvoiceDetail">
				<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
					<li data-role="list-divider">Products</li>
					<li style="font-size:13px">&nbsp;</li>
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
<%
s2.close();
s.close();
c.close();
ds.dropConnection();
%>