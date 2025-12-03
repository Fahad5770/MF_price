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
<%@page import="com.pbc.common.Distributor"%>

<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 372;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}


Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s1=c.createStatement();
Statement s2=c.createStatement();


/* if(true){
	is_edit=true;
} */
%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="js/lookups.js"></script>

		<script src="js/GLCreditLimit.js"></script>
	<script src="js/AddDistributor.js?122=211"></script>
</head>

<body>

<div data-role="page" id="GLCreditLimitPage" data-url="GLCreditLimitPage" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="ADD Distributor" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">	

<ul data-role="listview"  data-divider-theme="b" style="margin-top:5px;"> 
<li data-role="list-divider">ADD Distributor</li> </ul>    
<form id="add_distributor">
<table style="width:100%; margin-top:15px;">
<tr>  
<td style="width:15%;"><input type = "text" data-mini="true" placeholder = "Distributor Id"  id="distributor_id"  name="distributor_id"  onkeypress="if(event.keyCode==13) foucsnext()"></td>
<td style="width:25%;" ><input type = "text" data-mini="true" placeholder = "Distributor Name"  id="distributor_name"  name="distributor_name"  onkeypress="if(event.keyCode==13) foucsnext()" ></td>	
	<td  style="text-align:center;width:25%;">
		
			<select  name="cityid"  data-mini="true" id="cityid"  onkeypress="if(event.keyCode==13) foucsnext()" >
				<option value="-1">Select City</option>
	<% 			
	int CityID=0;
	String Label="";
	
	ResultSet rsCity = s.executeQuery("SELECT * FROM common_cities");
	while(rsCity.next()){
		CityID = rsCity.getInt("id");
		Label = rsCity.getString("label");
		
		  
	%>	
	
				        <option value="<%=CityID%>"><%=Label%></option>
				      
				 
				   <% 	
	}			  
	%>	
			  </select> 
		 
	</td>
<td  style="text-align:center">
		
			<select  name="productid" data-mini="true"  id="productid"  onkeypress="if(event.keyCode==13) foucsnext()" >
				<option value="-1">Select Product Group</option>
	<% 			
	int ProductGroupId=0;
	String Name ="";  
	
	ResultSet rsProductGroup = s2.executeQuery("SELECT * FROM employee_product_groups");
	while(rsProductGroup.next()){
		ProductGroupId = rsProductGroup.getInt("product_group_id");
		Name = rsProductGroup.getString("product_group_name");
		
		  
	%>	
	
				        <option value="<%=ProductGroupId%>"><%=Name%></option>
				      
				 
				   <% 	
	}			  
	%>	
			  </select> 
		 
	</td> 
	
	</tr>
	<tr>
	
		<td  style="text-align:center;width:25%;">
		
			<select  name="regionid" data-mini="true"   id="regionid"  onkeypress="if(event.keyCode==13) foucsnext()" >
				<option value="-1">Select Region</option>
	<% 			
	int RegionID=0;
	String RName="";
	
	ResultSet rsRegion = s1.executeQuery("SELECT * FROM common_regions");
	while(rsRegion.next()){
		RegionID = rsRegion.getInt("region_id");
		RName = rsRegion.getString("region_name");
		
		  
	%>	
	
				        <option value="<%=RegionID%>"><%=RName%></option>
				      
				 
				   <% 	
	}			  
	%>	
			  </select>  
		 
	</td>
	<td>
	<input name="address" data-mini="true" id="address" placeholder="Address Here"> 
	</td>
	<td style="width:15%"><a data-icon="plus" data-role="button" data-theme="b" data-mini="true" style="width: 25%;"  id="AddDistributor" href="#" >Add</a></td>
	<td></td> 
	</tr>
</table>
</form>
<input type="hidden"  id="DistributionID" name="DistributionID" value="">


	
	
	
	
	
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
        <h4>&copy;Al Moiz Co. (Pvt.) Ltd</h4>
    </div><!-- /footer -->

    
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