<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<jsp:useBean id="bean" class="com.pbc.outlet.OutletDashboard" scope="page"/>
<jsp:setProperty name="bean" property="*"/>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@ page import="java.io.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%
String PageID = "PSRTargets";
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 468;
if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

boolean isEditCase = false;
long EditID = Utilities.parseLong(request.getParameter("OrderBookerTargetID"));
if(EditID > 0){
	isEditCase = true;
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		
		<script>
			var isEditCase = <%=isEditCase%>;
			var EditID = <%=EditID%>;
		</script>
		
		<link href="lib/jquery.fineuploader-3.4.1/fineuploader-3.4.1.css" rel="stylesheet">
		<script src="lib/jquery.fineuploader-3.4.1/jquery.fineuploader-3.4.1.min.js?123=123"></script>				
		<script src="js/OrderBookerTargets.js"></script>
		<script src="js/lookups.js"></script>
		
		<style>
			.radio_style {
			      display: block;
			      width: 80px;
			      height: 50px;
			      background-repeat: no-repeat;
			      background-position: -231px 0px;
			}
			.ui-table-reflow.ui-responsive {
			  	display:block;
			}
		</style>
		
		<script>
		  $( document ).delegate("#PSRTargets", "pageinit", function() {
			  $(".WorkflowAttachButton").on( "click", function( event, ui ) {
				  $("#userid").val(<%=SessionUserID%>);
				  $("#popup_workflow_attach" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
			  });

			  var manualuploader = $('#manual-fine-uploader').fineUploader({
			         autoUpload: false,
			         text: {
			          uploadButton: '<i class="icon-plus icon-white"></i> Select Files'
			         },
			         request: {
			          endpoint: 'outlet/UploadFileForGeoFancing',
			          paramsInBody: true,
			             params: {
			              UserID:  function(){
			               return $("#userid").val();
			              }
			             }
			         }
			     }).on('complete', function(event, id, name, responseJSON){
			      if(responseJSON.Success=='true'){
			       alert("File uploaded successfully ");
			       window.location="OutletsGeofancingUpload.jsp";
			      }else{
			       alert("This File Contains Error.");
			       window.location="OutletsGeofancingUpload.jsp";
			      }
			     });

			     $('#triggerUpload').click(function() {
			      manualuploader.fineUploader('uploadStoredFiles');
			     });
		  });
		</script>
	</head>
	
<body>

<div data-role="page" id="PSRTargets" data-url="PSRTargets" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Upload Outlets Geo Fancing" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">
    
    <form action="test2.jsp" name="DistributorTargetsMainForm" id="DistributorTargetsMainForm" >
		<ul data-role="listview" data-inset="false" data-divider-theme="c">
		
			<li>
			    	<input type="hidden" name="EditID" id="EditID" value="<%=EditID%>" >
			    	
			        <table style="width:70%" border="0">
			        	<tr>
			        		<td style="width:20px; font-weight:normal;">
			        			Select CSV file to upload Geo Fancing
			        		</td>
			        		<td style="width:30%; font-weight:normal;" >
			        			<a href="#" style="float: left; margin-right: 10px; width:250px;" 
			        				data-role="button" data-icon="arrow-u" data-corners="false" 
			        				data-theme="b" class="WorkflowAttachButton">
			        				Select File
			        			</a>
			        		</td>
			        	</tr>
			        	<tr>
			        		<td colspan="2" style="padding-top:10px;">
			        			<!-- 📥 Download Sample CSV Format -->
			        			<a href="download/Upload_File_For_Geo_Fence.csv" 
			        				style="text-decoration:none; color:blue;" target="_blank">
			        				 Download Sample CSV Format
			        			</a>
			        		</td>
			        	</tr>
			        </table>
			</li>
	    </ul>   
	</form> 
    </div><!-- /content -->
    
    <!-- Upload Popup -->
	<div data-role="popup" id="popup_workflow_attach" data-overlay-theme="a" data-theme="c" 
	     data-dismissible="false" style="max-width:600px;min-width:500px;max-height:500px;" class="ui-corner-all">
	    <div data-role="header" data-theme="b" class="ui-corner-top">
	        <h1>Attach Files</h1>
	    </div>				
	    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
			<div id="manual-fine-uploader"></div>
			<br>
			<input type="hidden" id="userid" />
			<button id="triggerUpload" data-role="button" data-inline="true" data-theme="b">Attach</button>
			<a href="#<%= PageID %>" data-role="button" data-inline="true" data-theme="c">Close</a>
	    </div>
	</div> 
    
    <div data-role="footer" data-position="fixed" data-theme="b"></div>
</div>
</body>
</html>

<%
s.close();
ds.dropConnection();
%>
