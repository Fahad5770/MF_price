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
<script src="js/DispatchReturns.js?1=11"></script>





<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 422;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

String FormAction = "DispatchCashReceipts.jsp";



Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();


Statement s = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();
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


<div data-role="page" id="DispatchReturnsMain" data-url="DispatchReturnsMain" data-theme="d">

    
    <jsp:include page="Header3.jsp" >
    	<jsp:param value="Dispatch Cash Received" name="title"/>

    		<jsp:param value="<%=DistributorName%>" name="HeaderValue"/>

    </jsp:include>

    
    <div data-role="content" data-theme="d">

	<form id="ReturnsGenerrateForm" name="ReturnsGenerrateForm" action="<%=FormAction%>" method="POST" data-ajax="false">
		<input type="hidden" name="DispatchID" id="DispatchID" value=""/>
		<input type="hidden" name="DisptachReturnsVehicleNumber" id="DisptachReturnsVehicleNumber" value=""/>
		<input type="hidden" name="DisptachReturnsDriverName" id="DisptachReturnsDriverName" value=""/>
		<input type="hidden" name="DisptachVehicleType" id="DisptachVehicleType" value=""/>
    		
    </form>		
    		<ul data-role="listview" data-inset="true" data-mini="true">
			    
			    <li data-role="list-divider" data-theme="c">
			    	
			    		<table border="0" style="width: 100%; font-size:10pt;">
				    		<tr>
					        	<th style="width:10%; text-align:left;">Dispatch ID</th>
					        	<th style="width:13%; text-align:left;">Vehicle Number</th>
					        	<th style="width:13%;text-align:left;"">Vehicle Type</th>
					        	<th style="width:13%;text-align:left;"">Driver Name</th>
					        	<th style="width:13%;text-align:left;"">Date of Departure</th>
					        	<th style="width:13%;text-align:left;">Total Amount</th>
					        	<th style="width:13%;text-align:left;">Cash Received</th>
					        	<th style="width:13%;text-align:left;">Balance</th>
			        		</tr>
			        	</table>
			        	
			     </li>
				    		<%
				    		System.out.println("SELECT isd.id,isd.created_on,isd.created_by,ifnull(isd.vehicle_id,'0') vehicle_id,ifnull((select vehicle_no from distribtuor_vehicles dv where dv.id=isd.vehicle_id),'N/A') vehicle_name, ifnull((select type_id from distribtuor_vehicles dv where dv.id=isd.vehicle_id),'By Hand') vechile_type_id, ifnull((select label from distributor_vehicles_types dvt where dvt.id=vechile_type_id),'N/A') vehicle_type_name,isd.driver_id,ifnull((select name from distributor_employees de where isd.driver_id=de.id ),'By Hand') as driver_name,TIMESTAMPDIFF(HOUR,created_on,now()) hrsince,is_liquid_returned,is_empty_returned,cash_received FROM inventory_sales_dispatch isd where  isd.is_adjusted = 1  and isd.distributor_id = "+DistributorID+" and isd.is_cash_adjusted is null order by isd.id desc");//isd.vehicle_id is not null and isd.dispatch_type = 1
	        		//ResultSet rs = s.executeQuery("SELECT isd.id,isd.created_on,isd.created_by,ifnull(isd.vehicle_id,'0') vehicle_id,ifnull((select vehicle_no from distribtuor_vehicles dv where dv.id=isd.vehicle_id),'N/A') vehicle_name, ifnull((select type_id from distribtuor_vehicles dv where dv.id=isd.vehicle_id),'By Hand') vechile_type_id, ifnull((select label from distributor_vehicles_types dvt where dvt.id=vechile_type_id),'N/A') vehicle_type_name,isd.driver_id,ifnull((select name from distributor_employees de where isd.driver_id=de.id ),'By Hand') as driver_name,TIMESTAMPDIFF(HOUR,created_on,now()) hrsince,is_liquid_returned,is_empty_returned,cash_received FROM inventory_sales_dispatch isd where  isd.is_adjusted = 1  and isd.distributor_id = "+DistributorID+" and isd.dispatch_type = 1  and isd.is_cash_adjusted =1 order by isd.id desc");//isd.vehicle_id is not null and isd.dispatch_type = 1
				    ResultSet rs = s.executeQuery("SELECT isd.id,isd.created_on,isd.created_by,ifnull(isd.vehicle_id,'0') vehicle_id,ifnull((select vehicle_no from distribtuor_vehicles dv where dv.id=isd.vehicle_id),'N/A') vehicle_name, ifnull((select type_id from distribtuor_vehicles dv where dv.id=isd.vehicle_id),'By Hand') vechile_type_id, ifnull((select label from distributor_vehicles_types dvt where dvt.id=vechile_type_id),'N/A') vehicle_type_name,isd.driver_id,ifnull((select name from distributor_employees de where isd.driver_id=de.id ),'By Hand') as driver_name,TIMESTAMPDIFF(HOUR,created_on,now()) hrsince,is_liquid_returned,is_empty_returned,cash_received FROM inventory_sales_dispatch isd where  isd.is_adjusted = 1  and isd.distributor_id = "+DistributorID+" and isd.is_cash_adjusted is null order by isd.id desc");//isd.vehicle_id is not null and isd.dispatch_type = 1
	        		while(rs.next())
	        		{
	        			double InvoiceAmount=0;
	        			double CashReceived=0;
	        			double RemainingBalance=0;
	        			long DispatchID = rs.getLong("id");
	        			int totalSales=0;
	        			 ResultSet rs11 = s3.executeQuery("SELECT sales_id FROM pep.inventory_sales_dispatch_invoices where id="+DispatchID+" union SELECT sales_id FROM pep.inventory_sales_dispatch_extra_invoices where  id="+DispatchID);
	        				
	        				while(rs11.next())
	        				{
	        					long SalesID = rs11.getLong("sales_id");
	        					ResultSet rs12 = s4.executeQuery("SELECT net_amount FROM pep.inventory_sales_adjusted where id="+SalesID);
	        					
	        					while(rs12.next())
	        					{
	        						InvoiceAmount += rs12.getLong("net_amount");
	        					}
	        					totalSales++;
	        				}	
	        				
	        				ResultSet rs13 = s4.executeQuery("SELECT cash_received FROM pep.inventory_sales_dispatch where id="+DispatchID);
	        				
	        				while(rs13.next())
	        				{
	        					CashReceived += rs13.getLong("cash_received");
	        				}	
	        				RemainingBalance = InvoiceAmount-CashReceived;
	        		if(RemainingBalance >0.0 ){%>
		        		<li data-icon="false">
		        			<a href="javascript:onClick=GetAllSalesForReturn('<%=Utilities.parseLong(rs.getString("id"))%>','<%=Utilities.filterString(rs.getString("vehicle_type_name"),1,100) %>','<%=Utilities.filterString(rs.getString("driver_name"),1,100) %>','<%=Utilities.filterString(rs.getString("vehicle_name"),1,100) %>')">
		        				<table border="0" style="width:100%;font-size:10pt;">
		        					<tr>
		        						<td style="width:10%"><%=DispatchID %></td>
		        						<td style="width:13%"><%=Utilities.filterString(rs.getString("vehicle_name"),1,100) %></td>
					        			<td style="width:13%"><%=Utilities.filterString(rs.getString("vehicle_type_name"),1,100) %></td>	
					        			<td style="width:13%"><%=Utilities.filterString(rs.getString("driver_name"),1,100) %></td>	
					        			<td style="width:13%"><%=Utilities.getDisplayDateFormat(rs.getDate("created_on"))%></td>	
					        			<td style="width:13%"><%=InvoiceAmount %></td>
					        			<td style="width:13%"><%=CashReceived %></td>
					        			<td style="width:13%"><%=RemainingBalance %></td>
		        					</tr>
		        				</table>
		        			</a>
		        		</li>		
			        		
	        		<%
	        				}
	        		}
	        		%>
			        	</table>
			    
			    	</a>
			   </li>
			    
			</ul>
    		
    		
    		
		
	


	

    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<!-- <button data-icon="check" data-theme="a" data-inline="true" id="RetursGenerateButton" onClick="GetAllSalesForReturn()">Generate Returns</button>
		 -->
	</div>    	
    </div>
	

</div>

</body>
</html>



<%
s.close();
ds.dropConnection();
%>