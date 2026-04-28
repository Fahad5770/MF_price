<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="com.pbc.util.UserAccess"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.common.Distributor"%>


<jsp:useBean id="bean" class="com.pbc.inventory.DeliveryNote" scope="page"/>
<jsp:setProperty name="bean" property="*"/>





<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<script src="js/lookups.js"></script>
<script src="js/DispatchBalanceCashReceipts.js?12344=34563451"></script>
<script>
$(document).ready(function(){
  $("#myInput").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $("#myTable11 tr").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
  });
});
</script>




<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 493;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

String FormAction = "DispatchBalanceCashReceipts.jsp";



Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();


Statement s = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();
Statement s5 = c.createStatement();
Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, 59);
%>
<% 
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

<div data-role="header" data-theme="c" data-position="fixed">
    <div>
	    <div style="float:left; width:10%">
	    	<a href="home.jsp" data-role="button" data-theme="d" data-inline="true" data-ajax="false" >Home</a>
	    </div>
	    <div style="float:left; width:90%;b1ackground-color:Red; text-align:center;"><h1 style="font-size: 14pt;float:left; margin-left:45%; text-align:center;">Balance Cash Received</h1>
	    
		    <div data-role="navbar" style="width:20%; float:right; margin-right:3px; margin-top:10px;">
		    	<ul>
		        	<li><a href="DispatchBalanceCashReceiptsMain.jsp" class="ui-btn-active" data-ajax="false">Invoices</a></li>
		        	<li><a href="OpeningBalanceCashReceipt.jsp" data-ajax="false">Opening Balance</a></li>
		    	</ul>
			</div><!-- /navbar -->
		</div>
	</div>
</div>
<%--<div data-role="page" id="DispatchReturnsMain" data-url="DispatchReturnsMain" data-theme="d">

    
   <%--  <jsp:include page="Header3.jsp" >
    	<jsp:param value="Balance Cash Received" name="title"/>

    		<jsp:param value="<%=DistributorName%>" name="HeaderValue"/>
				<ul>
		        	<li><a href="DispatchBalanceCashReceiptsMain.jsp" class="ui-btn-active" data-ajax="false">Invoices</a></li>
		        	<li><a href="OpeningBalanceCashReceipt.jsp" data-ajax="false">Opening Balance</a></li>
		    	</ul>
    </jsp:include> --%>

    
    <div data-role="content" data-theme="d">

	<form id="ReturnsGenerrateForm" name="ReturnsGenerrateForm" action="<%=FormAction%>" method="POST" data-ajax="false">
		<input type="hidden" name="DispatchID" id="DispatchID" value=""/>
	
    		
    </form>	
  <div 	>
    <div 	style="width: 100%; height: 40vh;">
    <!-- <input id="myInput" type="text" placeholder="Search.."> -->
<!--     		<ul data-role="listview" data-inset="true" data-mini="true" > -->
		    <ul data-role="listview" data-inset="true" class="ui-icon-alt"  data-theme="d" data-divider-theme="a" data-count-theme="c" data-filter="true" data-filter-placeholder="Search ..." data-mini="true">
			    <li data-role="list-divider" data-theme="c">
			    	
			    		<table border="0" style="width: 100%; font-size:10pt;">
				    		<tr>
					        	<th style="width:5%; text-align:left;">Add</th>
					        	<th style="width:10%; text-align:left;">Invoice No.</th>
					        	<th style="width:5%; text-align:left;">Outlet ID</th>
					        	<th style="width:30%; text-align:left;">Outlet Name</th>
					        	<th style="width:20%;text-align:left;"">Amount</th>
					        	<th style="width:20%;text-align:left;"">Balance</th>
					        	<th style="width:10%;text-align:left;"">Date</th>					        	
			        		</tr>
			        	</table>
			        	
			     </li>
				    		<%
	        		ResultSet rs = s.executeQuery("SELECT isd.id,cash_received FROM inventory_sales_dispatch isd where isd.vehicle_id is not null and isd.is_adjusted = 1 and isd.dispatch_type = 1 and isd.distributor_id = "+DistributorID+" and  isd.is_cash_adjusted =1 order by isd.created_on ");
				    //ResultSet rs = s.executeQuery("SELECT isd.id,isd.created_on,isd.created_by,isd.vehicle_id,(select vehicle_no from distribtuor_vehicles dv where dv.id=isd.vehicle_id) vehicle_name, (select type_id from distribtuor_vehicles dv where dv.id=isd.vehicle_id) vechile_type_id, (select label from distributor_vehicles_types dvt where dvt.id=vechile_type_id) vehicle_type_name,isd.driver_id,(select name from distributor_employees de where isd.driver_id=de.id ) as driver_name,TIMESTAMPDIFF(HOUR,created_on,now()) hrsince FROM inventory_sales_dispatch isd where isd.vehicle_id is not null");
	        		while(rs.next())
	        		{
	        			String OutletName="";
	        			double InvoiceAmount=0;
	        			long OutletID=0;
	        			double CashReceived=0;
	        			double RemainingBalance=0.0;
	        			long DispatchID = rs.getLong("id");
	        			int totalSales=0;
	        			 ResultSet rs11 = s3.executeQuery("SELECT sales_id FROM pep.inventory_sales_dispatch_invoices where id="+DispatchID+" union SELECT sales_id FROM pep.inventory_sales_dispatch_extra_invoices where  id="+DispatchID);
	        				
	        				while(rs11.next())
	        				{
	        					long SalesID = rs11.getLong("sales_id");
	        					ResultSet rs12 = s4.executeQuery("SELECT net_amount,outlet_id,adjusted_on FROM pep.inventory_sales_adjusted where id="+SalesID+" and is_settled=0");
	        					
	        					while(rs12.next())
	        					{
	        						InvoiceAmount = rs12.getLong("net_amount");
	        						OutletID = rs12.getLong("outlet_id");

	        				
			        				ResultSet rs13 = s5.executeQuery("SELECT cash_received FROM pep.inventory_sales_dispatch_cash_receipts where dispatch_id="+DispatchID+" and outlet_id="+OutletID);
			        				
			        				while(rs13.next())
			        				{
			        					CashReceived = rs13.getLong("cash_received");
			        				}
			        				
									ResultSet rs1311 = s5.executeQuery("SELECT cash_received FROM pep.inventory_sales_dispatch_receipts_invoices where sales_id="+SalesID+" and dispatch_id="+DispatchID+" and outlet_id="+OutletID);
			        				
			        				while(rs1311.next())
			        				{
			        					CashReceived += rs1311.getLong("cash_received");
			        				}
									
			        				ResultSet rs131 = s5.executeQuery("SELECT name FROM pep.common_outlets where id="+OutletID);
			        				
			        				while(rs131.next())
			        				{
			        					OutletName = rs131.getString("name");
			        				}	
			        				RemainingBalance = InvoiceAmount-CashReceived;
	        		if(RemainingBalance >0.0 ){%>
		        		<li data-icon="false" style="padding-top: 0px;padding-bottom: 0px;" >
		        		<%-- 	<a href="javascript:onClick=GetAllSalesForReturn('<%=Utilities.parseLong(rs.getString("id"))%>')"> --%>
		        				<table border="0" style="width:100%;font-size:10pt;">
		        				<tbody id="myTable11">
		        					<tr >
		        						<td  style="width:5%"> <form><input type="checkbox"  class="custom" data-mini="true"  id="checkb<%=SalesID %>" name="checkb" value="1" onclick="AddRow('<%=DispatchID %>','<%=SalesID %>','<%=OutletID %>','<%=InvoiceAmount %>','<%=RemainingBalance %>','<%=OutletName %>')"><label for="checkb<%=SalesID %>" style="margin-top: 0px;margin-bottom: 0px;">&nbsp;</label></form></td>
		        						<td  style="width:10%"><%=SalesID %></td>
		        						<td style="width:5%"><%=OutletID %></td>
		        						<td style="width:30%"><%=OutletName %></td>
					        			<td style="width:20%"><%=InvoiceAmount %></td>	
					        			<td style="width:20%"><%=RemainingBalance %></td>	
					        			<td style="width:10%"><%=Utilities.getDisplayDateFormat(rs12.getDate("adjusted_on"))%></td>	
		        					</tr>
		        					</tbody>
		        				</table>
		        			<!-- </a> -->
		        		</li>		
			        		
	        		<%}
	        				}
	        			}
	        		}
	        		%>
			        	</table>
			    
			    	</a>
			   </li>
			    
			</ul>
    		</div>
    		
    		    <div style="position: absolute;width: 100%; height: 50vh;top: 50vh; margin-top: 20px;" >
    		    <hr>
    		    	<form action="test2.jsp" name="DispatchBalanceMainForm" id="DispatchBalanceMainForm">
    					<table id="myTable" border="0" style="width:100%;font-size:10pt;">
        					<tr>
        						
					        	<th style="width:10%; text-align:left;">Invoice No.</th>
					        	<th style="width:13%; text-align:left;">Outlet</th>
					        	<th style="width:13%;text-align:left;"">Amount</th>
					        	<th style="width:13%;text-align:left;"">Balance</th>
					        	<th style="width:13%;text-align:left;"">Received</th>
								<th style="width:13%;text-align:left;"">Comments</th>
        					</tr>
		        		</table>
		        		
		        		<table id="myTable1" border="0" style="width:90%;font-size:10pt;">
        					<tr>
        						
					        	
					        	<th style="width:88%;text-align:Right;"">Total Received</th>
					        	<th style="width:12%;text-align:left;""><input type="text" data-mini="true" style="width:80px" name="CashReceivedTotal" readonly="readonly" id="CashReceivedTotal" value='' readonly>
					        	<input type="hidden" name="FullyAdjusted" readonly="readonly" id="FullyAdjusted">
					        	<input type="hidden" name="InvoiceAmountTotal" readonly="readonly" id="InvoiceAmountTotal">
					        	</th>
					        	

        					</tr>
		        		</table>
		    		</form>
				</div>
	</div>


	

    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="DispatchCashReceiptsSave" href="#" class="ui-di2sabled" onClick="DispatchBalanceCashReceiptsSubmit();">Save</a>
	</div>    	
    </div>
	

<!-- </div> -->

</body>
</html>



<%
s.close();
ds.dropConnection();
%>