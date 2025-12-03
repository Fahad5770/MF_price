<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.Calendar" %>


<%


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 254;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();



%>

<script>

function SaveFormSubmit(){
	
	
	
	$.mobile.loading( 'show');
	
	$('#CurrentYear1').val( $("#CurrentYear").val() );
	$('#CurrentMonth1').val(  $("#Month").val() );
	$('#LastYear1').val(  $("#LastYear").val() );
	//alert($('#CurrentYear1').val());
	
	$('#DistAreaAllocationFileUpload').submit();
	
	
	$.mobile.loading( 'hide');
	    	
	
}



</script>
<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<body>

<div data-role="page" id="ComplaintResolve" data-url="ComplaintResolve" data-theme="d">

    <div data-role="header" data-theme="c" data-position="fixed">
	    <div>
		    <div style="float:left; width:10%">
		    	<a href="home.jsp" data-role="button" data-theme="d" data-inline="true" data-ajax="false" ><img src="images/logofull.svg" style="width: 50px"></a>
		    </div>
		    <div style="float:left; width:90%;b1ackground-color:Red; text-align:center;">
		    	<h1 style="font-size: 14pt;float:left; margin-left:45%; text-align:center;">Distributor Area Allocation</h1>
			</div>
		</div>
	</div>
	
	<div data-role="content" data-theme="d">

		<%
		Calendar prevYear = Calendar.getInstance();
		Calendar currentYear = Calendar.getInstance();
	    
		
		%>	
			
			<form id="TaskResolveForm" action="/" method="post" >
			
				<input type="hidden" name="IsResolved" id="IsResolved" value="1" > 
				
				<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					
					<tr>
						<td valign="bottom" style="border: 0px; width: 50%"><label for="LastYear" data-mini="true">Last Year</label>
							<select name="lastYear" id="LastYear">
								<%for(int i=0;i<2;i++){ 
									 prevYear.add(Calendar.YEAR, -i);
								%>
								
								<option><%=prevYear.get(Calendar.YEAR) %></option>
								
								<%} %>
							</select>
						</td>
						
					</tr>
					
					<tr>
						<td valign="bottom" style="border: 0px; width: 50%"><label for="CurrentYear" data-mini="true">Current Year</label>
						<select name="CurrentYear" id="CurrentYear">
								<%for(int i=0;i<2;i++){ 
									currentYear.add(Calendar.YEAR, +i);
								%>
								
								<option><%=currentYear.get(Calendar.YEAR) %></option>
								
								<%} %>
							</select>
						
						</td>
						
					</tr>
					<tr>
						<td valign="bottom" style="border: 0px; width: 50%"><label for="Month" data-mini="true">Month</label>
						<select name="Month" id="Month">
							
							
							<option value="1">January</option>
							<option value="2">February</option>
							<option value="3">March</option>
							<option value="4">April</option>
							<option value="5">May</option>
							<option value="6">June</option>
							<option value="7">July</option>
							<option value="8">August</option>
							<option value="9">September</option>
							<option value="10">October</option>
							<option value="11">November</option>
							<option value="12">December</option>
								

							
							</select>
						</td>
						
					</tr>
					
					
					
				</table>
			</form>
			
			<form name="DistAreaAllocationFileUpload" id="DistAreaAllocationFileUpload" action="distributor/DistributorAreaAllocationUploadFile" method="post" enctype="multipart/form-data" data-ajax="false"  >
			<input type="hidden" name="CurrentYear1" id="CurrentYear1" value="" >
			<input type="hidden" name="CurrentMonth1" id="CurrentMonth1" >
			<input type="hidden" name="LastYear1" id="LastYear1" >
				<table>
					<tr>
						<td>Upload File</td>
					</tr>
					<tr>
						<td><input type="file" name="file1" ></td>
						
					</tr>
					
					
				</table>
			</form>
			
			</div>
			
			
		<div data-role="footer" data-position="fixed" data-theme="b">
			<div>
				<button data-icon="check" data-theme="a" data-inline="true"  onClick="SaveFormSubmit()">Save</button>
			</div>
		</div>
		
		<jsp:include page="LookupOutletSearchPopup.jsp" > 
	    	<jsp:param value="OutletSearchCallBackComplaintFormWeb" name="CallBack" />
	    	<jsp:param value="<%=FeatureID %>" name="OutletSearchFeatureID" />
	    </jsp:include><!-- Include Outlet Search -->
			
</div>
</body>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>