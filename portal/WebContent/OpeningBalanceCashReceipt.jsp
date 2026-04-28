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
<script src="js/OpeningBalanceCashReceipt.js?123422=123433"></script>
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
int FeatureID = 438;
int FeatureLiquidID = 85;
int FeatureEmptyID = 83;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

String FormAction = "OpeningBalanceCashReceipt.jsp";



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
	    <div style="float:left; width:90%;b1ackground-color:Red; text-align:center;"><h1 style="font-size: 14pt;float:left; margin-left:45%; text-align:center;">Opening Balance Cash Received</h1>
	    
		    <div data-role="navbar" style="width:20%; float:right; margin-right:3px; margin-top:10px;">
		    	<ul>
		        	<li><a href="DispatchBalanceCashReceiptsMain.jsp"  data-ajax="false">Invoices</a></li>
		        	<li><a href="OpeningBalanceCashReceipt.jsp" class="ui-btn-active" data-ajax="false">Opening Balance</a></li>
		    	</ul>
			</div><!-- /navbar -->
		</div>
	</div>
</div>


    
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
					        	
					        	<th style="width:5%; text-align:left;">Outlet ID</th>
					        	<th style="width:30%; text-align:left;">Outlet Name</th>
					        	<th style="width:20%;text-align:left;"">Opening Balance</th>
					        	<th style="width:20%;text-align:left;"">Remaining Balance</th>
					        	<th style="width:10%;text-align:left;"">Date</th>
					        	<th style="width:10%;text-align:left;"">Amount</th>		
					        	<th style="width:5%; text-align:left;">Receipt</th>				        	
			        		</tr>
			        	</table>
			        	
			     </li>
				    		<%
	        		ResultSet rs = s.executeQuery("SELECT id,name from common_outlets where distributor_id = " + DistributorID);
				    //ResultSet rs = s.executeQuery("SELECT isd.id,isd.created_on,isd.created_by,isd.vehicle_id,(select vehicle_no from distribtuor_vehicles dv where dv.id=isd.vehicle_id) vehicle_name, (select type_id from distribtuor_vehicles dv where dv.id=isd.vehicle_id) vechile_type_id, (select label from distributor_vehicles_types dvt where dvt.id=vechile_type_id) vehicle_type_name,isd.driver_id,(select name from distributor_employees de where isd.driver_id=de.id ) as driver_name,TIMESTAMPDIFF(HOUR,created_on,now()) hrsince FROM inventory_sales_dispatch isd where isd.vehicle_id is not null");
	        		while(rs.next())
	        		{
	        			String OutletName=rs.getString("name");
	        			double OpeningBalance=0.0;
	        			double RemainingBalance=0.0;
	        			double TotalReceived=0.0;
	        			long OutletID = rs.getLong("id");
	        			long OpeningBalanceID=0;
	        			 ResultSet rs11 = s3.executeQuery("SELECT id,amount,created_on FROM pep.gl_outlet_opening_balances where outlet_id="+OutletID+" and is_settled=0 ");
	        				
	        				while(rs11.next())
	        				{
	        					OpeningBalanceID = rs11.getLong("id");
	        					OpeningBalance = rs11.getLong("amount");
	        					RemainingBalance = rs11.getLong("amount");
	        					ResultSet rs1 = s4.executeQuery("select sum(credit) cr from gl_transactions_sales where outlet_opening_balance_id="+OpeningBalanceID);
	        					if(rs1.first()){
	        						TotalReceived = rs1.getDouble("cr"); 
	        					}
	        					RemainingBalance = OpeningBalance - TotalReceived;
	        					
	        		if(RemainingBalance >0.0 ){%>
		        		<li data-icon="false" style="padding-top: 0px;padding-bottom: 0px;" >
		        		<%-- 	<a href="javascript:onClick=GetAllSalesForReturn('<%=Utilities.parseLong(rs.getString("id"))%>')"> --%>
		        				<table border="0" style="width:100%;font-size:10pt;">
		        				<tbody id="myTable11">
		        					<tr >
		        						
		        						
		        						<td style="width:5%"><%=OutletID %></td>
		        						<td style="width:30%"><%=OutletName %></td>
					        			<td style="width:20%"><%=OpeningBalance %></td>	
					        			<td style="width:20%"><%=RemainingBalance %></td>	
					        			<td style="width:10%"><%=Utilities.getDisplayDateFormat(rs11.getDate("created_on"))%></td>	
					        			<td  style="width:10%"> <form><input type="text" data-mini="true" style="width:80px" name="CashReceivedTotal" id="CashReceivedTotal" value='' ></form></td>
										<td  style="width:5%"> <form><input type="button"  class="custom" data-mini="true"  id="checkb<%=OpeningBalanceID%>" name="checkb" value="Receipt" onclick="OpeningBalanceCashReceiptSubmit('<%=OpeningBalanceID %>','<%=RemainingBalance %>')" style="margin-top: 0px;margin-bottom: 0px;""></form></td>
		        					</tr>
		        					</tbody>
		        				</table>
		        			<!-- </a> -->
		        		</li>		
			        		
	        		<%}
	        				}
	        			
	        		}
	        		%>
			        	</table>
			    
			    	</a>
			   </li>
			    
			</ul>
    		</div>
    		<form action="test2.jsp" name="DispatchBalanceMainForm" id="DispatchBalanceMainForm">
    		<input type="hidden" name="OpeningBalanceID" readonly="readonly" id="OpeningBalanceID">
    		<input type="hidden" name="CashReceived" readonly="readonly" id="CashReceived">
    		</form>
    		 <!--    <div style="position: absolute;width: 100%; height: 50vh;top: 50vh; margin-top: 20px;" >
    		    <hr>
    		    	<form action="test2.jsp" name="DispatchBalanceMainForm" id="DispatchBalanceMainForm">
    					<table id="myTable" border="0" style="width:100%;font-size:10pt;">
        					<tr>
        						
					        	<th style="width:10%; text-align:left;">Invoice No.</th>
					        	<th style="width:13%; text-align:left;">Outlet</th>
					        	<th style="width:13%;text-align:left;"">Amount</th>
					        	<th style="width:13%;text-align:left;"">Balance</th>
					        	<th style="width:13%;text-align:left;"">Received</th>

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
				</div> -->
	</div>


	

    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<!-- <a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="DispatchCashReceiptsSave" href="#" class="ui-di2sabled" onClick="OpeningBalanceCashReceiptSubmit();">Save</a> -->
	</div>    	
    </div>
	


</body>
</html>



<%
s.close();
ds.dropConnection();
%>