<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}

long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 491;  
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp"); 
}

Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();
%>

<script>

var FeatureIDValue = <%=FeatureID%>;

</script>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
	<script src="js/lookups.js"></script>
	<script src="js/UpdateOutletTaxInfo.js?1111"></script>   
	  
	<title>PBC Enterprise Portal</title>
	
</head>
<body>

<div data-role="page" id="OutletTaxinfo" data-url="OutletTaxinfo" data-theme="d">
	
    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Outlets" name="title"/>
    </jsp:include>
     <!-- /header -->
    
    <div data-role="content" data-theme="d" style="padding: 0px;margin: 0;">
				
		      <form name="OutletAdministrationForm" id="OutletAdministrationForm" method="POST" data-ajax="false" action="#">
    <ul data-role="listview" data-inset="true" style="margin-left: 10px; margin-right: 10px;">
        <li data-role="list-divider">Outlet</li>
        <li>
            <table border="0" style="width: 100%; margin-top: 0px; padding: 0px;" class="workflow_form_font">
                <tr>
                    <td style="width: 100%" valign="top">
                        <fieldset class="ui-grid-c">
                            <div class="ui-block-a" style="padding-left: 5px;">
                                <label for="OutletID">Outlet ID</label>
                                <input type="text" id="OutletID" name="OutletID" data-mini="true">
                            </div>
                            <div class="ui-block-b" style="padding-left: 5px;">
                                <label for="OutletName">Outlet Name</label>
                                <input type="text" id="OutletName" name="OutletName" data-mini="true" readonly="readonly">
                            </div>
                            <div class="ui-block-c" style="padding-left: 5px;">
                                <label for="NtnID">NTN</label>
                                <input type="text" id="NtnID" name="NtnID" data-mini="true">
                            </div>
                            <div class="ui-block-d" style="padding-left: 5px;">
                                <label for="StnID">STN</label>
                                <input type="text" id="StnID" name="StnID" data-mini="true">
                            </div>
                        </fieldset>
                        <fieldset class="ui-grid-b">
                            <div class="ui-block-b" style="padding-left: 5px;">
                                <label for="isRegisterID">&nbsp;&nbsp;</label>
                                <label><input type="checkbox" name="isRegisterID" id="isRegisterID" data-mini="true"/> Register</label>
                            </div>
                            <div class="ui-block-b" style="padding-left: 5px;">
                                <label for="Filer">&nbsp;&nbsp;</label>
                                <label><input type="checkbox" name="isFiler" id="isFilerID" data-mini="true"/> Filer</label>
                            </div>
                        </fieldset>
                    </td>
                </tr>
            </table>
        </li>
    </ul>
</form>

					
<!-- <form name="PJPForm" id="PJPForm" action="BeatPlanCreate.jsp" method="post" data-ajax="false" >
	<input type="hidden" name="BeatPlanID" id="BeatPlanID" value="" >
	<input type="hidden" name="DistributorID" id="DistributorID" value="" >
</form> -->

<!-- <form name="DistributorForm" id="DistributorForm" action="DistributorAdministration.jsp" method="post" data-ajax="false" >
	<input type="hidden" name="DistributorFormDistributorID" id="DistributorFormDistributorID" value="" >
</form> -->
						
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<table>
		<tr>
		<td>
		<button data-icon="check" data-theme="a" data-inline="true" id="OutletAdministrationSaveButton" onclick="OutletTaxSubmit()">Update</button>
		</td>
		<td>
		<!-- 
			<a data-role="button" href="#" data-icon="grid" data-theme="b" id="SamplingDeactivationOpenButton" class="ui-disabled" target="_blank">Open Approved Document</a>
		 -->
		</td>
		</tr>
		</table>
	</div>    	
    </div>


<%-- <jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="DistributorSearchCallBackOutletAdministration" name="CallBack" />
    	<jsp:param value="76" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Outlet Search --> --%>

</div>
</body>
</html>


<%
ds.dropConnection();
String success = request.getParameter("success");
							    		
if (success != null && success.equals("false")){
	%>
	<script>
	alert("Error: Could not be deactivated");
	</script>
<%
}
%>