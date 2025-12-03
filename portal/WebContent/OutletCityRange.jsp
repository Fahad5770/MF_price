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
int FeatureID = 373;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}


Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="js/lookups.js"></script>

		<script src="js/GLCreditLimit.js"></script>
	<script>
	$(function(){
	$("#addCityRange").click(function(){
		
		/* Get Data */
		var city = $("#city").val();
		var start_code = $("#start_code").val();
		var end_code = $("#end_code").val();
	//	alert(city+" "+start_code+" "+end_code);
		/* Flag */ 
		var flag = false;
		
		/*Validation Rules*/
		var scode_regex = /^[0-9]+$/;
		
		/* Validation */
		if(city < 1)
			{
			flag = true;
			//alert("Add City");
			}
		if(scode_regex.test(start_code) == "" || start_code == "" || scode_regex.test(end_code) == "" || end_code == "")
			{
			flag = true;
			alert("Start and end Ranges Should be in numeric");
			}
	//	alert(flag);
		if(flag == false){
			$.ajax({
			    url: "outlet/CityRangeCodeExecute",
			    data: $("#AddRange").serialize(),
			    	
			    type: "POST",
			    dataType : "json",
			    success: function( json ) { 
			    	if (json.success == "true"){
			    		alert("City Range Add Successfully");
			    		window.location.href="OutletCityRange.jsp";
			    	}else{
			    		alert("Ranges Not Found");
			    	}
			    	
			    }, 
			 
		 });
		}
	});
$( "#city" ).change(function() {
			var cityid = $("#city").val();
			//alert(cityid);
		 	$.ajax({
			    url: "outlet/CityRangeCodeJson",
			    data: {
			    	cityid: cityid
			    },
			    type: "POST",
			    success: function( json ) {
			    	if (json.success == "true"){
			    	 	$("#start_code").val(json.StartCode);
			    	 	$("#end_code").val(json.EndCode);
			    	 }
			    },  
			 error:function( json ) {
			    	alert("Server Not Working");
			    },  
		 }); 
			});
	});
	function remove(id){
		//alert(id);
		$.ajax({
		    url: "outlet/CityRangeCodeExecute",
		    data: {
		    	id: id,
		    	operation:"delete"
		    },
		    type: "POST",
		    success: function( json ) {
		    	if (json.success == "true"){
		    		alert("City Range Delete Successfully");
		    		window.location.href="OutletCityRange.jsp";
		    	 }else{
		    		alert("City Range Does Not Delete");
		    	}
		    	
		    },  
		 
	 });
	}
	</script>
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
	<style> 
	
	</style>
</head>

<body>
<scritp src="js/OutletCityCodeRange.js"></scritp>
<div data-role="page" id="GLCreditLimitPage" data-url="GLCreditLimitPage" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="City Outlet Range" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">	

<input type="hidden" name="QueryFlag" value="1">
<ul data-role="listview"  data-divider-theme="b" style="margin-top:5px;"> 
<li data-role="list-divider">City Outlet Range</li> </ul>
<form id="AddRange">
<input type="hidden" name="operation" value="add">
<table style="width:100%; margin-top:15px;">
<tr> 

	<td  style="text-align:center">
		
			<select name="city"  data-mini="true" id="city"  onkeypress="if(event.keyCode==13) foucsnext()" >
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
	<td><input type = "text" placeholder = "start code" value=""  id="start_code"  data-mini="true" name="start_code"  onkeypress="if(event.keyCode==13) foucsnext()" ></td>
	<td ><input type = "text" placeholder = "end code" value=""   id="end_code" data-mini="true" name="end_code"   onkeypress="if(event.keyCode==13) foucsnext()" ></td>
	<td style="width:15%"><a data-icon="plus" data-role="button" data-theme="b" data-mini="true" style="width: 50%;"  id="addCityRange" href="#" >Add</a></td>
	
	</tr>
</table>
</form>

<form >

<table data-role = "table" data-mode = "column"style="width:100%; margin-top:15px;">
	<thead style="    background-color: lightgray;">
	<tr  > 
	<th >City</th>
	<th >Start Code</th> 
	<th>End Code</th>
	<th style="    text-align: center;">Delete</th>
	</tr> 
	</thead>
	<tbody>  
	<% 			
	
	
	ResultSet rsdata = s2.executeQuery("SELECT * FROM common_outlet_code_range");
	int t=1;
	while(rsdata.next()){
		int id = rsdata.getInt("id");
		int City_ID = rsdata.getInt("City_id");
		String city = ""; 
		ResultSet rscity = s3.executeQuery("SELECT * FROM common_cities where id="+City_ID);
		if(rscity.first()){
			city = rscity.getString("label");
		}
		long start = rsdata.getLong("start_code");
		long end = rsdata.getLong("end_code");
		  
	%>	
	<tr> 
	<td ><%=city %></td>
	<td><%=start %></td>
	<td ><%=end %></td>
	<td data-role = "main" style="text-align: center;" class = "ui-content">
        <a  href="#" onclick="remove(<%=id %>)" data-role="button" data-icon="delete" data-iconpos="notext" data-theme="c" data-inline="true" data-corners="false" style="border: 0px;
    background-color: transparent;">Delete</a>  
        </td>			      
		</tr>		 
<% 	 
t++;
	}			  
	%>	 
<!-- <a data-icon="delete" data-role="button" data-theme="c" style="border:0px"data-mini="true"   onclick="addCityRange()" href="#" ></a> -->
	</tbody>
</table>
</form>
<input type="hidden"  id="DistributionID" name="DistributionID" value="">


	
	
	
	
	
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
        <h4>&copy;Al Moiz Co. (Pvt.) Ltd</h4>
    </div><!-- /footer -->
    
    <!-- <div data-role="footer" data-position="fixed" data-theme="b">
		<divstyle="width: 100%;">
			<table style="width: 100%;">
				<tr>
					<td>
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="SubmitButton" href="#" onClick="submitForm()" class="ui-disabled" >Save</a>
						<button data-icon="check" data-theme="b" data-inline="true" id="ResetButton" onClick="javascript:window.location='NewOutletRequest.jsp'" >Reset</button>
						
					</td>
	                <td align="right">
	                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="ViewAllButton" >View All</a>
					</td>
				</tr>
			</table>
		</div>
    </div> -->
    
    
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