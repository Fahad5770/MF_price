<%@page import="com.pbc.workflow.ConversationMessage"%>
<%@page import="com.pbc.workflow.WorkflowChat"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>

	<%@include file="include/ValidateSession.jsp" %>
	
	<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
	<script src="js/lookups.js"></script>

	<%
	
	long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));
	
	int SessionUserTypeID = Utilities.parseInt((String)session.getAttribute("UserTypeID"));
	
	String UserDisplayName = (String)session.getAttribute("UserDisplayName");
	String UserPictureURL = (String)session.getAttribute("UserPictureURL");
	
	
	if (session.getAttribute("UserPasswordChangeDate") != null){
	
		Date Date30DaysAgo = Utilities.getDateByDays(-30);
		Date PasswordChangeDate = (Date)session.getAttribute("UserPasswordChangeDate");
		
		boolean PasswordExpired = false;
		
		if(PasswordChangeDate.compareTo(Date30DaysAgo) == -1){
			PasswordExpired = true;
		}
		
		if(PasswordExpired){
			response.sendRedirect("UserChangePassword.jsp");
		}
		
	}else{
		if (session.getAttribute("UserID") != null){
			response.sendRedirect("UserChangePassword.jsp");
		}
	}
	%>
</head>
<body>
<div data-role="page" id="HomePage" data-url="HomePage" data-theme="d">

	<jsp:include page="HomeNavigationPanel.jsp">
		<jsp:param name="title" value="<%= UserDisplayName%>" />
	</jsp:include> <!-- /Left Navigation Panel -->
	 
	<jsp:include page="Header1.jsp" /> <!-- /header -->
    
    <% if( SessionUserTypeID == 1 ){ %>
    	<jsp:include page="HomePrimary.jsp" />
    <% }else{ %>
    	<jsp:include page="HomeSecondary.jsp" />
    <% } %>

    <jsp:include page="Footer1.jsp" /> <!-- /footer -->
    
    <jsp:include page="LookupEmployeeSearchPopup.jsp" >
    	<jsp:param value="setEmployeeLookupAtHome" name="CallBack" />
    </jsp:include><!-- Include Employee Search -->
    
    <form id="DashboardOrderBookerFormID" name="DashboardOrderBookerFormID" action="OrderBookerDashboardOverview.jsp" method="POST" data-ajax="false">
    	<input type="hidden" name="EmployeeCode" id="DashboardOrderBookerEmployeeCode"/>
    </form>
    
    
     <form id="DashboardOutletFormID" name="DashboardOutletFormID" action="OutletDashboard.jsp" method="POST" data-ajax="false">
    	<input type="hidden" name="outletID" id="DashboardOutletoutletID"/>
    </form>
    
    <form id="DashboardDistributorFormID" name="DashboardDistributorFormID" action="DistributorDashboardOverview.jsp" method="POST" data-ajax="false">
    	<input type="hidden" name="DistributorCode" id="DashboardDistributorDistributorCode"/>
    </form>
    
    
    
    <jsp:include page="LookupOutletSearchPopup.jsp" > 
    	<jsp:param value="setOutletLookupAtHome" name="CallBack" />
    	<jsp:param value="31" name="OutletSearchFeatureID" />
    </jsp:include><!-- Include Outlet Search -->
    
    <jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="setDistributorLookupAtHome" name="CallBack" />
    	<jsp:param value="44" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Distributor Search -->
    
    <jsp:include page="LookupRegionSearchPopup.jsp" > 
    	<jsp:param value="setRegionLookupAtHome" name="CallBack" />
    </jsp:include><!-- Include Region Search -->

</div>

</body>
</html>