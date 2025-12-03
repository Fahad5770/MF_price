<%@page import="com.sap.conn.jco.ext.SessionException.Type"%>
<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>

<head>

<style>
td{
font-size: 8pt;
}

</style>

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
<%

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

int TypeID = Utilities.parseInt(request.getParameter("Type"));
String TypeLabel = "Complaint Report";
ResultSet rs3 = s.executeQuery("SELECT label FROM fda_complaints_types where id="+TypeID);
if(rs3.first()){
	TypeLabel = rs3.getString(1);
}

String Where = "";
if(TypeID > 0){
	Where = " and type_id="+TypeID;
}

Date FromDate = Utilities.parseDate(request.getParameter("FromDate"));
Date ToDate = Utilities.parseDate(request.getParameter("ToDate"));

%>

<div data-role="page" id="FDAMain" data-url="FDAMain" data-theme="d">

<jsp:include page="Header.jsp" >
 	<jsp:param value="Complaint Report" name="title"/>
 </jsp:include>

<div data-role="content" data-theme="d">

<div class="ui-block-a" style="width:100%">
<div class="ui-bar " style="min-height:60px; border:0px solid">

<!-- <table style="width: 100%">
	<tr>
		<td align="left">
			<h1>Complaint Report</h1>
		</td>
		<td align="right">
			<img src="fda.jpg" astyle="width: 65px">
		</td>
	</tr>
</table>
 -->

<table style="width: 100%">
	<tr>
		<td valign="top" style="width: 30%">
			<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; amargin-top:-10px;" data-icon="false">
				<li data-role="list-divider" data-theme="a">Types</li>
				
				<%
				int i = 0;
				ResultSet rs2 = s.executeQuery("SELECT *, (SELECT count(case_no) FROM fda_complaints where date_of_entry between "+Utilities.getSQLDate(FromDate)+" and "+Utilities.getSQLDate(ToDate)+" and type_id=id and status_id=2 ) count, (SELECT count(case_no) FROM fda_complaints where date_of_entry between "+Utilities.getSQLDate(FromDate)+" and "+Utilities.getSQLDate(ToDate)+" and status_id=2 ) count_all FROM fda_complaints_types order by count desc");
				while(rs2.next()){
					
					if(i == 0){
						%>
						<li><a href="FDAComplaintReportCompleted.jsp?FromDate=<%=request.getParameter("FromDate")%>&ToDate=<%=request.getParameter("ToDate")%>" style="font-size: 12px; font-weight: bold" data-ajax="false">All<span class="ui-li-count" style="font-size:13px"><%=rs2.getString("count_all")%></span></a></li>
						<%
					}
					
					%>					
					<li><a href="FDAComplaintReportCompleted.jsp?Type=<%=rs2.getString("id")%>&FromDate=<%=request.getParameter("FromDate")%>&ToDate=<%=request.getParameter("ToDate")%>" style="font-size: 12px; font-weight: bold" data-ajax="false"><%=rs2.getString("label")%><span class="ui-li-count" style="font-size:13px"><%=rs2.getString("count")%></span></a></li>
					<%
					i++;
				}
				%>
			</ul>
		</td>
		<td valign="top" style="width: 70%">
			<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; amargin-top:-10px;" data-icon="false">
				<li data-role="list-divider" data-theme="a"><%=TypeLabel%></li>
				<li>
				
				
				<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
					<tr>
						
						<td style="width: 100%" valign="top">
							
							<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
								<thead>
									<tr>
										<th>Case No</th>
										<th>Name</th>
										<th>Type</th>
										<th>City</th>
										<th>Contact No</th>
										<th>Date of Entry</th>
										<th>Completion Date</th>
									</tr>
									<%
									ResultSet rs = s.executeQuery("SELECT *, (SELECT label FROM fda_complaints_types where id=type_id) complaint_label FROM fda_complaints where date_of_entry between "+Utilities.getSQLDate(FromDate)+" and "+Utilities.getSQLDate(ToDate)+" and status_id=2 "+Where);
									while(rs.next()){
										
										String DateOfEntry = Utilities.getDisplayDateFormat(Utilities.parseDateYYYYMMDD(rs.getString("date_of_entry")));
										if(rs.getString("date_of_entry") == null){
											DateOfEntry = "";
										}
										
										String StatusOn = Utilities.getDisplayDateFormat(Utilities.parseDateYYYYMMDD(rs.getString("status_on")));
										if(rs.getString("status_on") == null){
											StatusOn = "";
										}
										/*
										if(rs.getString("status_id").equals("2")){
											
										}
										*/
										%>
										<tr>
											<td><%=rs.getString("case_no")%></td>
											<td><%=rs.getString("first_name")%> <%=rs.getString("last_name")%></td>
											<td><%=rs.getString("complaint_label")%></td>
											<td><%=rs.getString("city")%></td>
											<td><%=rs.getString("contact_no")%></td>
											<td><%=DateOfEntry%></td>
											<% if(rs.getString("status_id").equals("2")){ %>
											<td><%=StatusOn%></td>
											<% }else{ %>
											<td>&nbsp;</td>
											<% } %>
										</tr>
										<%
									}
									%>
								</thead>
							</table>	
						</td>
					</tr>
				</table>
					</li>	
				</ul>
		</td>
	</tr>
</table>




</div>
</div>
</div>
</div>

<%
s.close();
c.close();
ds.dropConnection();
%>