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
int FeatureID = 371;
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

		<script src="js/GLCreditLimit.js"></script>
	<script>
	$(function(){
		$("#add_city").click(function(){
			var flag=false;
				var city = $("#City").val();
				var city_regex = /^[a-zA-Z]+$/;
				if(city_regex.test(city) == "" || city == " "){
					alert("City Should be in Alphabets");
					flag = true;
				}
				if(flag==false){
				$.ajax({
				    url: "common/CityExecute",
				    data: {
				    	city: city,
				    	operation:"add"
				    },
				    type: "POST",
				    dataType : "json",
				    success: function( json ) {
				    	
				    	if (json.success == "true"){
				    	 	alert("Add City Succesfully");
				    	 	window.location.href="AddCity.jsp";
				    	 }else{
				    		alert("City Already Exists");
				    	}
				    	
				    }, 
				 
			 });
				}
			 
		});

	});  
	function remove(id){
	//	alert(id);
		 
			 $.ajax({
				    url: "common/CityExecute",
				    data: {
				    	cityid: id,
				    	operation:"delete"
				    },
				    type: "POST",
				    success: function( json ) {
				    	if (json.success == "true"){
				    	 	alert("Delete City Succesfully");
				    	 	window.location.href="AddCity.jsp";
				    	 }else if(json.success == "false"){
				    		 alert("There is some problem in Database Constraints");
						    	//window.location.href="Outlets_city_setup.jsp";
				    	}
				    	
				    },
			 });
		 
	}
	</script>
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
	<style> 
	ul .ui-btn-icon-right > .ui-btn-inner > .ui-icon
	{
	  /*  background-color: red;*/
	} 
	td h2{
	    margin-top: -6PX;
    FONT-WEIGHT: 600;
    margin-bottom: 13px;
	}
	</style>
</head>

<body>

<div data-role="page" id="GLCreditLimitPage" data-url="GLCreditLimitPage" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Add City" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">	

<input type="hidden" name="QueryFlag" value="1">
<ul data-role="listview"  data-divider-theme="b" style="margin-top:5px;"> 
<li data-role="list-divider">ADD City</li> </ul>
<table style="width:40%; margin-top:15px;">
<tr> 
	 <td style="width:25%"><input type = "text" placeholder = "Add City" id="City" name="City"></td>
	<td style="width:15%"><a data-icon="plus" data-role="button" data-mini="true" style="width: 50%;" data-theme="b" id="add_city" href="#" >Save</a></td>  
</tr> 
</table>
<table style="width:40%; margin-top:15px;">
<tr> 
<td colspan="4">
<table data-role = "table" data-mode = "column" style="width:100%;">
 <thead style="    background-color: lightgray;">
	<tr > 
	<th>City</th>
	<th style="text-align: center;">Delete</th>
	</tr> 
	</thead>
	<tbody>  
   <%
         ResultSet rs = s.executeQuery("SELECT * FROM common_cities");  
         while(rs.next()){
         %>
         <tr>
 <td style="width:70%;" ><%=rs.getString("label") %></td>
 <td data-role = "main" style="text-align: center;width:30%;" class = "ui-content">
        <a  href="#" onclick="remove(<%=rs.getString("id") %>)" data-role="button" data-icon="delete" data-iconpos="notext" data-theme="c" data-inline="true" data-corners="false" style="border: 0px;
    background-color: transparent;">Delete</a>  
        </td>			   
  </tr>
 <%
         } 
         %>

 </table>
      </td>   
</tr>
 </table>

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