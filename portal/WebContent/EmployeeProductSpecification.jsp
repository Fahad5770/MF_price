<%@page import="com.pbc.outlet.LedgerTransaction"%>
<%@page import="com.pbc.outlet.Advance"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>



<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 80;

if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

long EmployeeCode = Utilities.parseLong(request.getParameter("EmployeeCode"));
String EmployeeName = "";

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

boolean isEditCase = false;
int EditID = 0;
ResultSet rs_edit = s.executeQuery("SELECT employee_product_specification_id FROM employee_product_specification where employee_sap_code="+EmployeeCode);
if(rs_edit.first()){
	EditID = rs_edit.getInt("employee_product_specification_id");
	isEditCase = true;
}


ResultSet rs = s.executeQuery("select vorna, nachn from sap_pa0002 where pernr="+EmployeeCode);
if(rs.first()){
	EmployeeName = rs.getString("vorna")+" "+rs.getString("nachn");
}

String ProductGroupOptions = "<option value=''>Select Product Specification Group</option>";
ResultSet rs2 = s.executeQuery("SELECT * FROM employee_product_groups");
while(rs2.next()){
	ProductGroupOptions += "<option value='"+rs2.getString("product_group_id")+"'>"+rs2.getString("product_group_name")+"</option>";
}

%>

<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="lib/highcharts301/js/highcharts.js"></script>
		<script src="lib/highcharts301/js/highcharts-more.js"></script>
		<script src="js/ProductSecification.js"></script>
		<script src="js/lookups.js"></script>
		
		<script>
			<% if(isEditCase){ %>
				getEmployeeProductSpecificationInfoJson(<%=EditID%>);
			<% } %>
			
		</script>
		
</head>


<div data-role="page" id="ProductSpecification" data-url="ProductSpecification" data-theme="d">

    <jsp:include page="EmployeeBeatPlanHeader.jsp" >
    	<jsp:param value="<%=EmployeeName%>" name="title"/>
    	<jsp:param value="2" name="tab"/>
    	<jsp:param value="<%=EmployeeCode%>" name="EmployeeCode"/>
    </jsp:include>
     <!-- /header --> 
    
    <div data-role="content" data-theme="d">
        <form name="ProductSpecificationForm" id="ProductSpecificationForm" data-ajax="false" action="#ProductSpecification" >
			<input type="hidden" name="EmployeeCode" id="EmployeeCode" value="<%=EmployeeCode%>" >
			<input type="hidden" name="EditID" id="EditID" value="<%=EditID%>" >
			<input type="hidden" name="EmployeeSAPCode" id="EmployeeSAPCode" value="<%=EmployeeCode%>" >
			
			<div style="width:60%">
				<div style="text-align:right"><input type="button" value="New Group" data-inline="true" data-mini="true" onClick="window.location='EmployeeProductGroups.jsp'" data-icon="plus"  ></div>			
				<ul data-role="listview" data-inset="true">
				
					<li data-role="fieldcontain">
			            <label for="EmployeeProductGroup">Product Group:</label>
			            
			            <select name="EmployeeProductGroup" id="EmployeeProductGroup" onchange="DisplayProductList(this.value)" data-mini="true">
							<%=ProductGroupOptions%>
						</select>
			        </li> 
				
				</ul>
			</div>
			<div id="ProductList"></div>
			
	    </form> 
    </div><!-- /content -->

    <div data-role="footer" data-position="fixed" data-theme="b">
    
		<div>
			<table style="width: 100%;">
				<tr>
					<td>
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="ProductSpecificationSave" href="#" onClick="ProductSpecificationSubmit();">Save</a>
						<button data-icon="check" data-theme="b" data-inline="true" id="ProductSpecificationReset" onClick="ProductSpecificationReset()" >Reset</button>
					</td>
				</tr>
			</table>
		</div>
	    	
    </div><!-- /footer -->
<jsp:include page="LookupEmployeeSearchPopup.jsp" > 
    	<jsp:param value="EmployeeSearchCallBack" name="CallBack" />
    </jsp:include><!-- Include Employee Search -->
</div>

<%
s.close();
ds.dropConnection();
%>
</body>
</html>