<%@page import="java.util.Date"%>
<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@include file="include/ValidateSession.jsp" %>


<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 250;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

boolean isPrintCase = false;
long PrintID = Utilities.parseLong(request.getParameter("PrintID"));
if(PrintID > 0){
	isPrintCase = true;
}

Warehouse UserWarehouse[] = UserAccess.getUserFeatureWarehouse(SessionUserID, FeatureID);

if( UserWarehouse == null || UserWarehouse.length > 1 ){
	//response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionKSML();
Connection c = ds.getConnection();
Statement s = c.createStatement();

%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="js/lookups.js"></script>

		<script src="js/CRMMillRates.js?8711=8711"></script>

</head>

<body>

<% 
String WarehouseName = "";
long WarehouseID=0;


if( UserWarehouse != null ){
	WarehouseName = UserWarehouse[0].WAREHOUSE_NAME;
	WarehouseID = UserWarehouse[0].WAREHOUSE_ID;
}

%>

<div data-role="page" id="GLUnpostCashReceiptsPage" data-url="GLUnpostCashReceiptsPage" data-theme="d">

    <jsp:include page="Header3.jsp" >
    	<jsp:param value="Mill Rates" name="title"/>
    	
    </jsp:include>
    
    
    <div data-role="content" data-theme="d">
	
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:100%">
	    	<div class="ui-bar " style="min-height:60px">
	    			<form name="GLCashReceiptsForm" id="GLCashReceiptsForm">
	    			
					<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
						
						<%
						 int ID=0;
					    String Name="";
					    String Zone="";
					    String Circle="";
					    String IsLP="";
					    double Rate=0;
					    double LPRate=0;
					    int MillID=0;
						
						
						ResultSet rs = s.executeQuery("SELECT cc.mill_id, cc.id,name,circle,zone,if(is_lp,'Yes','No') is_lp,rate,tpt_rate FROM crman_centers cc join crman_own_rates cor on cc.id=cor.center_id ");
						while(rs.next()){
						
							ID=rs.getInt("id");
						     Name=rs.getString("name");
						     Zone=rs.getString("zone");
						     Circle=rs.getString("circle");
						     IsLP=rs.getString("is_lp");
						     Rate=rs.getDouble("rate");
						     LPRate=rs.getDouble("tpt_rate");
							 MillID = rs.getInt("mill_id");
							
							
						%>
						
						
						
						
						<tr>
							<td style="width: 10%; border: 0px; padding-right: 2px">
								<label for="TableID" data-mini="true">ID</label>
								<input type="text" name="TableID" id="TableID" value="<%=ID %>" data-mini="true"  data-mini="true" readonly>
							</td>
							<td style="width: 15%; border: 0px; padding-right: 2px">
								<label for="Zone" data-mini="true">Zone</label>
								<input type="text" name="Zone" id="Zone" value="<%=Zone %>" data-mini="true"  data-mini="true" readonly>
							</td>
							<td style="width: 15%; border: 0px; padding-right: 2px">
								<label for="Circle" data-mini="true">Circle</label>
								<input type="text" name="Circle" id="Circle" value="<%=Circle %>" data-mini="true"  data-mini="true" readonly>
							</td>
							
						<td style="width: 15%; border: 0px; padding-right: 2px">
								<label for="Name" data-mini="true">Name</label>
								<input type="text" name="Name" id="Name" value="<%=Name %>" data-mini="true"  data-mini="true" readonly>
							</td>
							<td style="width: 15%; border: 0px; padding-right: 2px">
								<label for="LP" data-mini="true">LP</label>
								<input type="text" name="LP" id="LP" value="<%=IsLP %>" data-mini="true"  data-mini="true" readonly>
							</td>
							<td style="width: 10%; border: 0px; padding-right: 2px">
								<label for="Rate" data-mini="true">Mill ID</label>
								<input type="text" name="MillID" id="MillID" value="<%=MillID %>" data-mini="true"  data-mini="true" readonly>
							</td>
						
						
						
						<td style="width: 10%; border: 0px; padding-right: 2px">
								<label for="Rate" data-mini="true">Rate</label>
								<input type="text" name="Rate" id="Rate" value="<%=Rate %>" data-mini="true"  data-mini="true" >
							</td>
							<td style="width: 10%; border: 0px; padding-right: 2px">
								<label for="LPRate" data-mini="true">TPT </label>
								<input type="text" name="LPRate" id="LPRate" value="<%=LPRate %>" data-mini="true"  data-mini="true" readonly>
							</td>
						
						
						</tr>
						
						<%
						
						}
						
						%>
						
						
						</table>
						
						<input type="hidden" name="MillIDHidden" id="MillIDHidden"/>
						
						</form>
				
				
				
	    	</div>
	    </div>
	    
	</div><!-- /grid-a -->
	
	
	
	<form name="GLCashReceiptsPrintExecute" id="GLCashReceiptsPrintExecute" action="GLCashReceiptPrinting.jsp" method="post" data-ajax="false" target="_blank" >
		<input type="hidden" name="GLCashReceiptID" id="GLCashReceiptID" value="" >
	</form>
	
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
		<div>
			<table style="width: 100%;">
				<tr>
					<td>
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="SubmitButton" href="#" onClick="FormSubmit();" class="u1i-disabled" >Save</a>
						
						
					</td>
	              
				</tr>
			</table>
		</div>
    </div>
    
    
    <jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="DistributorSearchCallBackCashReceipt" name="CallBack" /> 
    	<jsp:param value="<%=FeatureID%>" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Outlet Search -->
    

</div>

</body>
</html>
<%
s.close();
ds.dropConnection();
%>