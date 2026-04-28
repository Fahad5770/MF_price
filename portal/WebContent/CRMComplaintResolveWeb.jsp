<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>


<%


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 142;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

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

<script>

function ComplaintResolveSubmit(){
	
	if( $('#ComplaintID').val() == "" ){
		alert("Please enter Complaint ID");
		document.getElementById("ComplaintID").focus();
		return false;
	}
	
	$.mobile.loading( 'show');
	$.ajax({
	    url: "crm/ComplaintExecute",
	    data: $('#ComplaintResolveForm').serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.loading( 'hide');
	    	
	    	if (json.success == "true"){
	    		$('#value1').val( json.InsertedComplaintID );
	    		$('#ComplaintResolveFileUpload').submit();
	    		//window.location = 'CRMComplaintResolveWeb.jsp';
	    	}else{
	    		alert('Server could not be reached.');
	    	}
	    	
	    },
	    error: function( xhr, status ) {
	    	$.mobile.loading( 'hide');
	    	alert("Server could not be reached.");
	    }
	});
	
}

function ShowDetail(){
	
	if( $('#ComplaintID').val() == '' ){
		return false;
	}
	
	$.mobile.loading( 'show');
	$.ajax({
	    url: "CRMComplaintResolveSetSession.jsp",
	    data: {
	    	ComplaintID : $('#ComplaintID').val()
	    },
	    type: "POST",
	    dataType : "html",
	    success: function( response ) {
	    	$.mobile.loading( 'hide');
	    	
	    	//alert("v = "+response);
	    	window.open("ReportCenter.jsp?ReportID=52&UUID=45039483932");
	    	
	    },
	    error: function( xhr, status ) {
	    	$.mobile.loading( 'hide');
	    	alert("Server could not be reached.");
	    }
	});
	
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
		    	<h1 style="font-size: 14pt;float:left; margin-left:45%; text-align:center;">Complaint Resolve</h1>
			</div>
		</div>
	</div>
	
	<div data-role="content" data-theme="d">

			
			
			<form id="ComplaintResolveForm" action="/" method="post" >
			
				<input type="hidden" name="IsResolved" id="IsResolved" value="1" > 
				
				<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					
					<tr>
						<td valign="bottom" style="border: 0px; width: 50%"><label for="ComplaintID" data-mini="true">Complaint ID</label><input type="text" name="ComplaintID" id="ComplaintID" placeholder="" value="" data-mini="true" onChange="getOutletName();" ondblclick="OpenPopup()" ></td>
						<td style=" border: 0px; width: 50%">
							
							<a data-theme="c" data-role="button" data-inline="true" data-mini="true"  href="#" style="margin-top: 25px" onclick="ShowDetail()" >Show Detail</a> 
						</td>
					</tr>
					
					
					<tr>
						<td style="border: 0px" colspan="2">
							<label for="Description" data-mini="true">Resolve Description</label>
							<textarea name="Description" id="Description" style="width: 100%; height: 100px" data-mini="true"></textarea>
						</td>
					</tr>
					
					
					
				</table>
			</form>
			
			<form name="ComplaintResolveFileUpload" id="ComplaintResolveFileUpload" action="crm/WebUploadImage" method="post" enctype="multipart/form-data" data-ajax="false"  >
			<input type="hidden" name="value1" id="value1" value="" >
			<input type="hidden" name="value2" value="1" >
				<table>
					<tr>
						<td>Upload Images</td>
					</tr>
					<tr>
						<td><input type="file" name="file1" ></td>
						<td><input type="file" name="file2" ></td>
					</tr>
					
					<tr>
						<td><input type="file" name="file3" ></td>
						<td><input type="file" name="file4" ></td>
					</tr>
					<tr>
						<td><input type="file" name="file5" ></td>
					</tr>
				</table>
			</form>
			
			</div>
			
			
		<div data-role="footer" data-position="fixed" data-theme="b">
			<div>
				<button data-icon="check" data-theme="a" data-inline="true"  onClick="ComplaintResolveSubmit()">Save</button>
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