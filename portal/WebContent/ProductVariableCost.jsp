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
<%@include file="include/ValidateSession.jsp"%>


<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 239;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

%>
<html>


<head>
	<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
	<script src="js/lookups.js"></script>
	<script src="js/ProductVariableCost.js"></script>
</head>

<body>

<div data-role="page" id="VariableCostPage" data-url="VariableCostPage" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Variable Cost" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:75%">
	    	<div class="ui-bar " style="min-height:60px">
				<form name="VariableCostExecute" id="VariableCostExecute" action="#" method="post" >
				<!-- <table border=0 style="font-size:13px; font-weight: 400; width:50%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">-->
				<table border=0 style="font-size:13px; font-weight: 400; width:70%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					<tr>
						<th style="width: 20%">Product Code</th>
						<th style="width: 50%">Product</th>
						<th style="width: 30%">Cost</th>
					</tr>
					
					<%
					ResultSet rs = s.executeQuery("SELECT ipvc.sap_code, ipv.package_id, concat(package_label, ' - ', brand_label) product_name, ipvc.cost FROM inventory_products_variable_costs ipvc join inventory_products_view ipv on ipvc.sap_code=ipv.sap_code order by package_id");
					while(rs.next()){
						%>
						<tr>
							<td style="width: 20%; border: 0px"><input type="text" name="SapCode" id="SapCode" data-mini="true" value="<%=rs.getString("sap_code")%>" style="text-align: left" readonly="readonly" tabindex="-1" ></td>
							<td style="width: 50%; border: 0px"><input type="text" name="Product" id="Product" data-mini="true" value="<%=rs.getString("product_name")%>" style="text-align: left" readonly="readonly" tabindex="-1" ></td>
							<td style="width: 30%; border: 0px"><input type="text" name="Cost" id="Cost" data-mini="true" value="<%=rs.getString("cost")%>" style="text-align: center" ></td>
						</tr>
						<%
					}
					%>
					
				</table>				
				</form>
	    	</div>
	    </div>
	</div><!-- /grid-a -->
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
		<div>
			<table style="width: 100%;">
				<tr>
					<td>					
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="SubmitButton" href="#" onClick="FormSubmit();" aclass="ui-disabled" >Update</a>
						<button data-icon="check" data-theme="b" data-inline="true" onClick="javascript:window.location='ProductVariableCost.jsp'" >Reset</button>
						
					</td>
	                
				</tr>
			</table>
		</div>
    </div>
    
</div>

</body>
</html>
<%
s.close();
ds.dropConnection();
%>