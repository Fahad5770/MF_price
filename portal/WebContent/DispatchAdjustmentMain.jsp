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


<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<script src="js/lookups.js"></script>
<script src="js/DispatchAdjustment.js?2222=555"></script>





<%
long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 60;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}


Datasource ds = new Datasource();
ds.createConnection();


Statement s = ds.createStatement();
Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, 60);
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


<div data-role="page" id="DispatchAdjustmentMain" data-url="DispatchAdjustmentMain" data-theme="d">

    
    <jsp:include page="Header3.jsp" >
    	<jsp:param value="Dispatch Adjustment" name="title"/>

    		<jsp:param value="<%=DistributorName%>" name="HeaderValue"/>

    </jsp:include>

    
    <div data-role="content" data-theme="d">

	<form id="DispatchGenerrateForm" name="DispatchGenerrateForm" action="DispatchAdjustment.jsp" method="POST" data-ajax="false">
		<input type="hidden" name="DispatchID" id="DispatchID" value=""/>
		
    		
    		
    		<ul data-role="listview" data-inset="true" data-mini="true">
			    
			    <li data-role="list-divider" data-theme="c">
			    	
			    		<table border="0" style="width: 100%; font-size:10pt;">
				    		<tr>
					        	<th style="width:10%; text-align:left;">Dispatch ID</th>
					        	<th style="width:20%; text-align:left;">Vehicle Number</th>
					        	<th style="width:10%;text-align:left;">Vehicle Type</th>
					        	<th style="width:20%;text-align:left;">Driver Name</th>
					        	<th style="width:20%;text-align:left;">Date of Departure</th>
					        	<th style="width:20%;text-align:left;">Hours Since</th>
			        		</tr>
			        	</table>
			        	
			     </li>
				    		<%
				    	//	System.out.println("SELECT isd.id,isd.created_on,isd.created_by,isd.vehicle_id,(select vehicle_no from distribtuor_vehicles dv where dv.id=isd.vehicle_id) vehicle_name, (select type_id from distribtuor_vehicles dv where dv.id=isd.vehicle_id) vechile_type_id, (select label from distributor_vehicles_types dvt where dvt.id=vechile_type_id) vehicle_type_name,isd.driver_id,(select name from distributor_employees de where isd.driver_id=de.id ) as driver_name,TIMESTAMPDIFF(HOUR,created_on,now()) hrsince FROM inventory_sales_dispatch isd where isd.vehicle_id is not null and isd.is_liquid_returned=1 and isd.is_empty_returned=1 and isd.is_adjusted = 0  and isd.distributor_id = "+DistributorID+" and isd.is_blocked=0 order by isd.id desc");
				    //		
	        	//	ResultSet rs = s.executeQuery("SELECT isd.id,isd.created_on,isd.created_by,isd.vehicle_id,(select vehicle_no from distribtuor_vehicles dv where dv.id=isd.vehicle_id) vehicle_name, (select type_id from distribtuor_vehicles dv where dv.id=isd.vehicle_id) vechile_type_id, (select label from distributor_vehicles_types dvt where dvt.id=vechile_type_id) vehicle_type_name,isd.driver_id,(select name from distributor_employees de where isd.driver_id=de.id ) as driver_name,TIMESTAMPDIFF(HOUR,created_on,now()) hrsince FROM inventory_sales_dispatch isd where isd.vehicle_id is not null and isd.is_liquid_returned=1 and isd.is_empty_returned=1 and isd.is_adjusted = 0  and isd.distributor_id = "+DistributorID+" and isd.is_blocked=0 order by isd.id desc");
	        			System.out.println("SELECT isd.is_adjusted,isd.id,isd.created_on,isd.created_by,isd.vehicle_id,(select vehicle_no from distribtuor_vehicles dv where dv.id=isd.vehicle_id) vehicle_name, (select type_id from distribtuor_vehicles dv where dv.id=isd.vehicle_id) vechile_type_id, (select label from distributor_vehicles_types dvt where dvt.id=vechile_type_id) vehicle_type_name,isd.driver_id,(select name from distributor_employees de where isd.driver_id=de.id ) as driver_name,TIMESTAMPDIFF(HOUR,created_on,now()) hrsince,is_liquid_returned,is_empty_returned FROM inventory_sales_dispatch isd where isd.vehicle_id is not null and isd.is_adjusted != 1 and isd.dispatch_type = 1 and isd.distributor_id = "+DistributorID+" order by isd.id desc");
	        		ResultSet rs = s.executeQuery("SELECT isd.is_adjusted,isd.id,isd.created_on,isd.created_by,isd.vehicle_id,(select vehicle_no from distribtuor_vehicles dv where dv.id=isd.vehicle_id) vehicle_name, (select type_id from distribtuor_vehicles dv where dv.id=isd.vehicle_id) vechile_type_id, (select label from distributor_vehicles_types dvt where dvt.id=vechile_type_id) vehicle_type_name,isd.driver_id,(select name from distributor_employees de where isd.driver_id=de.id ) as driver_name,TIMESTAMPDIFF(HOUR,created_on,now()) hrsince,is_liquid_returned,is_empty_returned FROM inventory_sales_dispatch isd where isd.vehicle_id is not null and isd.is_adjusted != 1 and isd.dispatch_type = 1 and isd.distributor_id = "+DistributorID+" order by isd.id desc");
				    //ResultSet rs = s.executeQuery("SELECT isd.id,isd.created_on,isd.created_by,isd.vehicle_id,(select vehicle_no from distribtuor_vehicles dv where dv.id=isd.vehicle_id) vehicle_name, (select type_id from distribtuor_vehicles dv where dv.id=isd.vehicle_id) vechile_type_id, (select label from distributor_vehicles_types dvt where dvt.id=vechile_type_id) vehicle_type_name,isd.driver_id,(select name from distributor_employees de where isd.driver_id=de.id ) as driver_name,TIMESTAMPDIFF(HOUR,created_on,now()) hrsince FROM inventory_sales_dispatch isd where isd.vehicle_id is not null");
				    while(rs.next())
	        		{
				    	
							                          		
							        		
				        		
		        		
				    	
				  // if(rs.getInt("hrsince")<72){
	        		%>
		        		<li data-icon="false">
		        			<a href="javascript:onClick=GetAllSalesForAdjustment('<%=Utilities.parseLong(rs.getString("id"))%>')">
		        				<table border="0" style="width:100%;font-size:10pt;">
		        					<tr>
		        						<td style="width:10%"><%=Utilities.parseLong(rs.getString("id")) %></td>
		        						<td style="width:20%"><%=Utilities.filterString(rs.getString("vehicle_name"),1,100) %></td>
					        			<td style="width:10%"><%=Utilities.filterString(rs.getString("vehicle_type_name"),1,100) %></td>	
					        			<td style="width:20%"><%=Utilities.filterString(rs.getString("driver_name"),1,100) %></td>	
					        			<td style="width:20%"><%=Utilities.getDisplayDateFormat(rs.getDate("created_on"))%></td>	
					        			<td style="width:20%"><%=Utilities.parseInt(rs.getString("hrsince")) %></td>
		        					</tr>
		        				</table>
		        			</a>
		        		</li>
		        		
				        		
						                          		
						        		
			        		
	        		<%
				 //	 }
	        		}
	        		%>
			        	</table>
			    
			    	</a>
			   </li>
			    
			</ul>
    		
    		
    		
		
	</form>


	

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