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
Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();


if (session.getAttribute("UserID") != null){
	

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 170;

String OldPassword = Utilities.filterString(request.getParameter("OldPassword"), 1, 100);
String NewPassword = Utilities.filterString(request.getParameter("NewPassword"), 1, 100);
boolean isOldPasswordTrue = true;


if(OldPassword != null){
	//System.out.println("select password, md5('"+OldPassword+"') from users where id="+SessionUserID);
	ResultSet rs = s.executeQuery("select password, md5('"+OldPassword+"') from users where id="+SessionUserID);
	if(rs.first()){
		if(!rs.getString(1).equals(rs.getString(2))){
			isOldPasswordTrue = false;
		}else{
			s2.executeUpdate("update users set password=md5('"+NewPassword+"'), password_changed_on=now() where id="+SessionUserID);
			session.setAttribute("UserPasswordChangeDate", new java.util.Date());
			//session.setAttribute("PasswordExpired", false);
			response.sendRedirect("home.jsp");
		}
	}
	
}


%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="js/UserChangePassword.js"></script>
<script>

var isOldPasswordTrue = '<%=isOldPasswordTrue%>';
if(isOldPasswordTrue == 'false'){
	alert("Current password is incorrect.");
}

</script>
</head>

<body>

<div data-role="page" id="UserChangePasswordPage" data-url="UserChangePasswordPage" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Change Password" name="title"/>
    </jsp:include>
    
    
    <div data-role="content" data-theme="d">
	
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:75%">
	    	<div class="ui-bar " style="min-height:60px">
	    		
				<form name="UserChangePasswordPageForm" id="UserChangePasswordPageForm" action="UserChangePassword.jsp" method="post" data-ajax="false" >
				
				<ul data-role="listview" data-inset="false" data-divider-theme="c" style="margin-top:20px; margin-left:2px;">
	    
				    
				    <li>
				    
					    <table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="OldPassword" data-mini="true">Current Password</label>
									<input type="password" name="OldPassword" id="OldPassword" value="" data-mini="true" data-mini="true" >
								</td>
								</tr><tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="NewPassword" data-mini="true">New Password</label>
									<input type="password" name="NewPassword" id="NewPassword" value="" data-mini="true" data-mini="true" >
								</td>
								</tr><tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="ConfirmPassword" data-mini="true">Confirm Password</label>
									<input type="password" name="ConfirmPassword" id="ConfirmPassword" value="" data-mini="true" data-mini="true" >
								</td>
							</tr>
							
							
						</table>
				    	
					</li>
					
				</ul>
				</form>
	    	</div>
	    </div>
	    
	</div><!-- /grid-a -->
	
	
	
	
	
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
		<div>
			<table style="width: 100%;">
				<tr>
					<td>
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="SubmitButton" href="#" onClick="FormSubmit();" aclass="ui-disabled" >Change</a>
						<button data-icon="check" data-theme="b" data-inline="true" onClick="javascript:window.location='UserChangePassword.jsp'" >Reset</button>
						
					</td>
	                
				</tr>
			</table>
		</div>
    </div>
    
</div>

</body>
</html>
<%
}
s2.close();
s.close();
ds.dropConnection();
%>