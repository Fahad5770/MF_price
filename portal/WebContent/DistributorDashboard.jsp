<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="com.pbc.common.Distributor"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}

long DistributorCode = Utilities.parseLong(request.getParameter("DistributorCode"));

if(UserAccess.isAuthorized(44, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}else{
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, 44);
	boolean isAllowed = false;
	if( UserDistributor != null ){
		for (int i = 0; i< UserDistributor.length; i++){
			if(UserDistributor[i].DISTRIBUTOR_ID == DistributorCode){
				isAllowed = true;
			}
		}
	}
	if (isAllowed == false){
		response.sendRedirect("AccessDenied.jsp");
	}
}


int PageIndex = Utilities.parseInt(request.getParameter("PageIndex"));
if (PageIndex == 0){
	PageIndex = 1;
}

String DistributorName = "";

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

ResultSet rs = s.executeQuery("select name from common_distributors where distributor_id="+DistributorCode);
if(rs.first()){
	DistributorName = rs.getString("name");
}

s.close();
ds.dropConnection();

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="js/DistributorDashboard.js"></script>
		<script src="js/DistributorReports.js"></script>
		<script src="js/lookups.js"></script>
		
	</head>
	
	
<body>
<%
//int OutletID = Utilities.parseInt(request.getParameter("OutletID"));
%>

<div data-role="page" id="DistributorDashboard" data-url="DistributorDashboard" >


    <jsp:include page="DistributorDashboardHeader.jsp" >
    	<jsp:param value="<%=DistributorName%>" name="DistributorNameToShow"/>
    	<jsp:param value="<%=DistributorCode%>" name="DistributorCode"/>
    	<jsp:param value="<%=PageIndex%>" name="PageIndex"/>
    	<jsp:param value="Activity Summary" name="ReportTitleToShow"/>
    </jsp:include>
    <!-- /header -->
    
    <div data-role="content" data-theme="d">
        
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" >
    
    </div>
    

    
</div>
</body>
</html>