<%@ page import="java.util.Date"%><%@ page import="com.pbc.util.Utilities"%><%@ page import="java.util.Calendar"%><%@ page import="com.pbc.util.Datasource"%><%@ page import="com.pbc.util.Datasource"%><%@ page import="java.sql.Statement"%><%@ page import="java.sql.ResultSet"%>
<%@include file="../include/ValidateSession.jsp" %>
<%

long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>March 2016</title>
	<link rel="stylesheet"  href="../lib/jqm130/jquery.mobile-1.3.0.min.css">
	<script src="../lib/jquery-1.9.1.min.js"></script>
	<script src="../lib/jqm130/jquery.mobile-1.3.0.min.js"></script>

	<script>
	function load(){
	
	$.ajax({
	    url: "getLifting.jsp",
	    type: "POST",
	    dataType : "html",
	    success: function( json ) {
	    	document.getElementById("itoday").innerHTML = json;
	    	$.ajax({
	    	    url: "getLiftingThisMonth.jsp",
	    	    type: "POST",
	    	    dataType : "html",
	    	    success: function( json ) {
	    	    	document.getElementById("imonth").innerHTML = json;
	    	    	$.ajax({
	    	    	    url: "getLiftingThisYear.jsp",
	    	    	    type: "POST",
	    	    	    dataType : "html",
	    	    	    success: function( json ) {
	    	    	    	document.getElementById("iyear").innerHTML = json;
	    	    		},
	    	    	    error: function( xhr, status ) {
	    	    	    	alert("Server could not be reached.");
	    	    	    }
	    	    	});

	    		},
	    	    error: function( xhr, status ) {
	    	    	alert("Server could not be reached.");
	    	    }
	    	});	    	
		},
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});


	}
	
	load();
	var abc = setInterval("load();",60000);
	</script>
</head>



<body>

						<div align="center" style="width: 100%;">
	<table style="width: 900px; aborder: 0px solid #D6D6D6;float: center;background-color: white;">
			<tr>
			<td style="width: 200px;"></td>
			<td style="width: 500px; font-family: impact; font-size: 30px; text-align: center;">March 2016</td>
			<td style="width: 200px; text-align: right;"><img src="theialogosmall.png" width="80"></td>
			</tr>
			<tr>
			<td style="width: 200px;" colspan="3">
			<br>
				<p>

Today
    <a href="#" data-role="button" data-theme="c" adata-inline="true"><span id="itoday">Loading...</span></a>
<br>
This Month
    <a href="#" data-role="button" data-theme="b" adata-inline="true"><span id="imonth">Loading...</span></a>
<br>
This Year
    <a href="#" data-role="button" data-theme="a" adata-inline="true"><span id="iyear">Loading...</span></a>
<br>
<br>
<br>
<br>

    <a href="#" data-role="button" data-theme="e" data-inline="true" data-ajax="false" onclick="document.getElementById('ilabel').innerHTML='Closed';clearInterval(abc);return false;"><span id="ilabel">Close Period</span></a>
</p>
			</td>

			</tr>
	</table>
</div>
</body>
</html>