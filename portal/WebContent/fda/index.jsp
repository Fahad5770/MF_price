<%@page import="org.omg.CORBA.portable.RemarshalException"%>
<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>

<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>

<%

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();


String params = request.getParameter("params");



//System.out.println(SapOrderNumber);
%>
<head>

	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet"  href="../lib/jqm130/jquery.mobile-1.3.0.min.css">
	 <link href="../lib/jquery.fineuploader-3.4.1/fineuploader-3.4.1.css" rel="stylesheet">
	
	<script src="../lib/jquery-1.9.1.min.js"></script>
	<script src="../lib/jqm130/jquery.mobile-1.3.0.min.js"></script>
	<script src="../lib/jquery-validation/jquery.validate.js"></script>
	<script src="../lib/jqueryui1102/js/jquery-ui-1.10.2.custom.min.js"></script>
	<script src="../lib/jquery.fineuploader-3.4.1/jquery.fineuploader-3.4.1.min.js"></script>
	
	<title>PBC Enterprise Portal</title>
	<script src="../js/home.js"></script>
	<link rel="stylesheet"  href="../css/home.css">

</head>
<div data-role="page" id="FDAMain" data-url="FDAMain" data-theme="d">



<div data-role="content" data-theme="d">

<div class="ui-block-a" style="width:100%">
	    	<div class="ui-bar " style="min-height:60px">

<table style="width: 100%">
	<tr>
		<td align="left">
			<h1>FDA Home</h1>
		</td>
		<td align="right">
			<img src="fda.jpg" astyle="width: 65px">
		</td>
	</tr>
</table>

<br/>
<table border=0 style="width:75%" align="center">
	<tr>
		<td style="width:30%" valign="top">
		
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c" data-filter="true" data-filter-placeholder="Search ..." data-mini="true">
				<li>
					<a data-ajax="false" href="FDAComplaintForm.jsp" style="font-size:13px">
						Complaint Form
						<br/><span style="padding-top:10px; font-weight:normal;">One window cell complaint form</span>
					</a>
				</li>
				<li>
					<a data-ajax="false" href="FDAComplaintReportMain.jsp" style="font-size:13px">
						Complaint Report
						<br/><span style="padding-top:10px; font-weight:normal;">List of all In-process Complaints</span>
					</a>
				</li>
				<li>
					<a data-ajax="false" href="FDAComplaintReportMainCompleted.jsp" style="font-size:13px">
						Complaint Report
						<br/><span style="padding-top:10px; font-weight:normal;">List of all Completed Complaints</span>
					</a>
				</li>
				<li>
					<a data-ajax="false" href="FDAChangeComplaintStatus.jsp" style="font-size:13px">
						Change Status
						<br/><span style="padding-top:10px; font-weight:normal;">Change Status of Complaint</span>
					</a>
				</li>
			</ul>
			
			
		
		</td>
		<!-- 
		<td style="width:70%" valign="top">
			<div id="OrderList">
				<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
					<li data-role="list-divider">Orders</li>
					<li><a data-ajax="false" href="" style="font-size:11px;font-weight:normal">Sales Activity Summary</a></li>
					<li><a data-ajax="false" href="" style="font-size:11px">Stock Summary</a></li>
					
					</ul>
			</div>
		</td>
		
	 -->
		
	</tr>
</table>


	<form name="OrderInvoicingEditAndInvoice" id="OrderInvoicingEditAndInvoice" action="OrderInvoicingEdit.jsp" method="post">
	
		<input type="hidden" name="OrderID" id="OrderID" value="" >
		<input type="hidden" name="OrderInvoicingDistributorID" id="OrderInvoicingDistributorID" value="" >
	
	</form>


</div>
</div>
</div>
</div>
<%
c.close();
ds.dropConnection();
%>