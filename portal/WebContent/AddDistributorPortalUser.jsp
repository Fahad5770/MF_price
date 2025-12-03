<%@page import="java.util.Date"%>
<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>

asdfasdf


<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if(UserAccess.isAuthorized(328, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
        <script src="js/UserChromeActivation.js"></script>
        
        
        
</head>

<body>

 <script type="text/javascript">
        $(document).ready(function () {
        	
        	$('#userid , #distributor').keypress(function (e) {
        	    var regex = new RegExp("^[0-9]+$");
        	    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
        	    if (regex.test(str)) {
        	        return true;
        	    }

        	    e.preventDefault();
        	    return false;
        	});
  	
        	
        	$(function() {
        	    var content = $('#distributor').val();

        	    $('#distributor').keyup(function() { 
        	        if ($('#distributor').val() != content) {
        	            content = $('#distributor').val();
        	            $('#userid').val($('#distributor').val());
        	        }
        	    });
        	});
        	
    //    $("#TrackDist").addClass("disabledbutton");
   //     $("#ActivePromo").addClass("disabledbutton");
   //     $("#PlaceOrder").addClass("disabledbutton");
   //     $("#UploadOrders").addClass("disabledbutton");
   //     $("#StockPos").addClass("disabledbutton");
        
        });
          </script>

<div data-role="page" id="UserChromeActivation" data-url="UserChromeActivation" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Add Distributor Portal New User" name="title"/>
    </jsp:include>
    
    
    <div data-role="content" data-theme="d">
	<table style="width: 100%; margin-top:7px;">
			<tr>
							<td style="width:30%">
				<p style="margin-top: 15px;margin-bottom: 2px">Distributor</p>
					<input type="text" placeholder="Distributor" id="distributor" name="distributor" >
				</td>
			
				
				<td style="width:30%">
				<p style="margin-top: 15px;margin-bottom: 2px">Password</p>
					<input type="password" placeholder="New Password" id="newpwd" name="newpwd" >
				</td>
				
				<td style="width:30%">
				<p style="margin-top: 15px;margin-bottom: 2px">Confirm Password</p>
					<input type="password" placeholder="Confirm Password" id="confirmpwd" name="confirmpwd"  >
				</td>
				</tr>

					<tr>
				<td style="width:30%">
				<p style="margin-top: 15px;margin-bottom: 2px">First Name</p>
					<input type="text" placeholder="First Name" id="firstname" name="firstname" >
				</td>
				
				<td style="width:30%">
				<p style="margin-top: 15px;margin-bottom: 2px">Last Name</p>
					<input type="text" placeholder="Last Name" id="lastname" name="lastname" >
				</td>
			
				<td style="width:30%">
				<p style="margin-top: 15px;margin-bottom: 2px">Display Name</p>
					<input type="text" placeholder="Display Name" id="displayname" name="displayname" >
				</td>
				</tr>
				<tr>
				<td style="width:30%">
				<p style="margin-top: 15px;margin-bottom: 2px">Designation</p>
					<input type="text" placeholder="Designation" id="designation" name="designation" >
				</td>
			
				<td style="width:30%">
				<p style="margin-top: 15px;margin-bottom: 2px">Department</p>
					<input type="text" placeholder="Department" id="dept" name="dept" >
				</td>
			
				<td style="width:30%">
				<p style="margin-top: 15px;margin-bottom: 2px">Email</p>
					<input type="text" placeholder="Email" id="email" name="email" >
				</td>
			</tr>
				<td style="width:30%">
				<p style="margin-top: 15px;margin-bottom: 2px">User ID</p>
					<input type="text" placeholder="User ID" id="userid" name="userid" readonly >
				</td>
					
				<tr>

			</tr>
		</table>
   
	
	
		<script src="js/AddDistributorPortalUser.js?111=111"></script>
	
	
	
    </div><!-- /content -->
    
      <div data-role="footer" data-position="fixed" data-theme="b">
		<div>
			<table style="width: 100%;">
				<tr>
					<td>
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="SaveBtn" href="#" onClick="adduser()" aclass="ui-disabled" >Save</a>
						
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
