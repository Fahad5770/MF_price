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


<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 383;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

boolean isEditCase = false;
long EditID = Utilities.parseLong(request.getParameter("EditID"));
if(EditID > 0){
	isEditCase = true;
}

int DeActivate = Utilities.parseInt(request.getParameter("DeActivate"));

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

String CustomerID = "";
String CustomerName = "";
String CreditLimit = "";
String ValidFrom = "";
String ValidTo = "";

if(isEditCase){
	ResultSet rs = s.executeQuery("select *, (SELECT name FROM common_distributors where distributor_id=customer_id) customer_name from gl_customer_credit_limit where id="+EditID);
	if(rs.first()){
		CustomerID = rs.getString("customer_id");
		CustomerName = rs.getString("customer_name");
		CreditLimit = rs.getString("credit_limit");
		ValidFrom = rs.getString("valid_from");
		ValidTo = rs.getString("valid_to");
	}
}

if(DeActivate > 0){
	s.executeUpdate("update gl_customer_credit_limit set is_active=0, deactivated_on=now(), deactivated_by="+SessionUserID+" where id="+DeActivate);
	response.sendRedirect("GLCreditLimit.jsp");
}

%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="js/lookups.js"></script>

		<script src="js/GLCreditLimit.js"></script>
		
		
		<script>
			function ExportToExcel(){
				var PJPID=0;
				
				$.mobile.loading( 'show');
			$.ajax({
				
				url: "employee/AllPJPDownloadExcelMain",
				data: {
					PJPID: PJPID
				},
				type:"POST",
				dataType:"json",
				success:function(json){
					$.mobile.hidePageLoadingMsg();
					if(json.success == "true"){
						 
						alert("File is created Successfully!");
						
						//var url = "../OrderImages/"+json.FileName;
						
						var url = "employee/PJPFileDownload?file="+json.FileName;
						
						//var url ="/portal/images/markers/letter_c_red.png"; 
						console.log("url"+url);
						var mydiv = document.getElementById("aExcelFileReady");
						var aTag = document.createElement('a');
						aTag.setAttribute('id','ExcelFileReady');
						//aTag.setAttribute('download','myImage.xlsx');
						aTag.setAttribute('href',url);
						aTag.setAttribute('target','_blank');
						aTag.innerHTML = "<label style='color:Gray; text-decoration:none; font-weight:bold; cursor: pointer;'>Download</label>";
						
						$(mydiv).html(aTag);
						 
					}else{
						alert(json.error);
					}
				},
				error:function(xhr, status){
					alert("Server could not be reached.");
				}
				
				});
			}
			
		
		</script>
			<style>
		#logindiv{
		  width: 50% ;
		  max-width:500px;
		  
		  margin-left: auto ;
		  margin-right: auto ;
		}
	</style>
		
</head>

<body>

<div data-role="page" id="GLCreditLimitPage" data-url="GLCreditLimitPage" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Download All PJP's" name="title"/>
    </jsp:include>
    
   
    <div data-role="content" data-theme="d">
	
	<% 
	Date d=new Date();
	String StartDate = Utilities.getDisplayDateFormat(d); 
	%>	
		
	
	<!-- <table style="width:100%; margin-top:15px;">
		<tr>
			<td>
				<div id="aExcelFileReady" st1yle="float:right">
					<a onclick="ExportToExcel()" href="#"    data-theme="c" data-inline="true" style="margin-top: 0px; margin-left: 0px; margin-bottom: 0px">Download All PJPs</a>
				</div>
			</td>
		</tr>
	</table> -->

	<div data-role="content" data-theme="d">
         
	    <div id="logindiv">
	    	<br>
	    	<!-- <img src="images/logofull.svg" style="width: 150px;" class="plogo"> -->
			<div data-role="collapsible" data-theme="b" data-content-theme="b" id="loginpane" data-collapsed="false" data-expanded-icon="grid">
			  <h3 >PJP Downloader</h3>
				
				<table style="width:100%;min-height:200px;">
					<tr>
						<td>
							<div id="aExcelFileReady" style="text-align:center">
								<a onclick="ExportToExcel()" href="#"    data-theme="c" data-inline="true" style="margin-top: 0px; margin-left: 0px; margin-bottom: 0px;font-size:19px">Download All PJPs</a>
							</div>
						</td>
					</tr>
				</table>
			
		</div>
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
		<div>
			<table style="width: 100%;">
				<tr>
					<td>
						
					</td>
	                <!-- <td align="right">
	                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="ViewAllButton" >View All</a>
					</td> -->
				</tr>
			</table>
		</div>
    </div>
    
    

</div>

</body>
</html>
<%
s.close();
ds.dropConnection();
%>