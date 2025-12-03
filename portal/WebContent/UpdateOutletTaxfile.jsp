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
	String PageID = "UpdateOutletTax";
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 497;
if ((String)session.getAttribute("UserID") == null){
	//response.sendRedirect("index.jsp");
}


/* if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
} */

boolean isEditCase = false;
long EditID = Utilities.parseLong(request.getParameter("OrderBookerTargetID"));
if(EditID > 0)
{
	isEditCase = true;
}


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		
		
		
	 <link href="lib/jquery.fineuploader-3.4.1/fineuploader-3.4.1.css" rel="stylesheet">
	
	
	
	<script src="lib/jquery.fineuploader-3.4.1/jquery.fineuploader-3.4.1.min.js?123=123"></script>				
		<script src="js/OrderBookerTargets.js"></script>
		<script src="js/lookups.js"></script>
		

		<style>
		
			.radio_style
			  {
			      display: block;
			      width: 80px;
			      height: 50px;
			      background-repeat: no-repeat;
			      background-position: -231px 0px;
			  }
			  
			  .ui-table-reflow.ui-responsive{
			  	display:block;
			  }
		
		</style>
		
		<script>
  
  
  $( document ).delegate("#MarkmaticsPrices", "pageinit", function()
{
  
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
          endpoint: 'outlet/UploadOutletsTaxInfo',
          paramsInBody: true,
             params: {             
              /* DistributorID:  function(){
                  return $("#Distributors").val();
                 },
              CityID:  function(){
                     return $("#City").val();
                    } */
             }
         }
     }).on('error', function(event, id, name, reason) {
      

     })
.on('complete', function(event, id, name, responseJSON){
      if(responseJSON.Success=='true'){
       alert("File uploaded successfully ");
       window.location="UpdateOutletTaxfile.jsp";
      }else{
       alert("Error ! Please Upload Again.");
       window.location="UpdateOutletTaxfile.jsp";
      }
      
      
      //showAttachments(pageid);
      //$.mobile.changePage(pageid);
      
     });
  
     $('#triggerUpload').click(function() {
     // alert("in click");
      manualuploader.fineUploader('uploadStoredFiles');
     });
     
  });
  </script>
		
	</head>
	
<body>

<div data-role="page" id="MarkmaticsPrices" data-url="MarkmaticsPrices" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Upload Outlets tax info" name="title"/>
    </jsp:include>
    <!-- /header -->
    
    <div data-role="content" data-theme="d">
    
    <form action="" name="DistributorTargetsMainForm" id="DistributorTargetsMainForm" >
		<ul data-role="listview" data-inset="false" data-divider-theme="c">
		
			<li>
			    	
			        <table style="width:70%" border="0">
						
						

			        	<tr>
			        		<td style="width:20px; font-weight:normal;">Select CSV file to upload Outlets tax info </td>
			        		<td style="width:30%; font-weight:normal;" >
			        			<a href="#" style="f1loat: left; m1argin-right: 10px; width:250px;" data-role="button" data-icon="arrow-u" data-corners="false" data-theme="b"  class="WorkflowAttachButton" >Select File</a>
			        		</td>
			        		
				        	
			        	
			        	
			        	</tr>
			        	
			        </table>
			        
				
				
			</li>
			
			 
		
			
		
			
	    </ul>   
	
	</form> 
    </div><!-- /content -->
    
  
</table>
				<div data-role="popup" id="popup_workflow_attach" data-overlay-theme="a" data-theme="c" data-dismissible="false" style="max-width:600px;min-width:500px;max-height:500px;" class="ui-corner-all">
				    <div data-role="header" data-theme="b" class="ui-corner-top">
				        <h1>Attach Files</h1>
				    </div>				
				    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">

						<div id="manual-fine-uploader"></div>
						<br>
						<input type="hidden" id="month1" />
   						<input type="hidden" id="year1" />
   						<input type="hidden" id="userid" />
						
				        <button id="triggerUpload" data-role="button" data-inline="true" data-theme="b">Attach</button>
				        <a href="#<%= PageID %>" data-role="button" data-inline="true" data-theme="c">Close</a>
				    </div>
				</div> 
    
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
		<div>
			<table style="width: 100%;">
				<tr>
					<td>
						
						<table>
							
						</table>
						
					</td>

	                
				</tr>
			</table>
		</div>
	    	
    </div>
    
    <jsp:include page="LookupEmployeeSearchPopup.jsp" > 
    	<jsp:param value="EmployeeSearchCallBack" name="CallBack" />
    </jsp:include><!-- Include Employee Search -->



<div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:600px; max-height: 600px" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >

			<form data-ajax="false" id="DistributorTargetsSearch" onSubmit="return showSearchContent()">
	            <table >
	            	<tr>
						<td astyle="width:20%">
							<input type="text" name="SearchEmployeeID" id="SearchEmployeeID" data-mini="true" placeholder="Employee ID" onchange="getOrderBookerName22()" size="12" >
							
						</td>
						<td astyle="width:60%">
							<input type="text" name="SearchEmployeeName" id="SearchEmployeeName" data-mini="true" placeholder="Employee Name" readonly="readonly" >
						</td>
						<td astyle="width:10%"><input type="submit" value="Search" data-mini="true" ></td>
	                </tr>
	                 
	            </table>
	        </form>

	        <div id="SearchContent" style="padding: 5px">
			
				   
	        
	        </div>
            
        </div>
    </div>
</div>
</body>
</html>