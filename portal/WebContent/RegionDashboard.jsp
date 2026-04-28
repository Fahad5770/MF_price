<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<jsp:useBean id="bean" class="com.pbc.outlet.OutletDashboard" scope="page"/>
<jsp:setProperty name="bean" property="*"/>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}

if(Utilities.isAuthorized(41, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}


long RegionCode = Utilities.parseLong(request.getParameter("RegionCode"));
String RegionName = "";

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

ResultSet rs = s.executeQuery("SELECT concat( region_short_name, ' - ', region_name ) name FROM common_regions where region_id="+RegionCode);
if(rs.first()){
	RegionName = rs.getString("name");
}


s.close();
ds.dropConnection();

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="lib/highcharts301/js/highcharts.js"></script>
		<script src="lib/highcharts301/js/highcharts-more.js"></script>
		<script src="js/RegionDashboard.js"></script>
		<script src="js/lookups.js"></script>
		
	</head>
	
<body>
<%
//int OutletID = Utilities.parseInt(request.getParameter("OutletID"));
%>

<div data-role="page" id="RegionDashboard" data-url="RegionDashboard" data-theme="d">

    <jsp:include page="RegionDashboardHeader.jsp" >
    	<jsp:param value="<%=RegionName%>" name="title"/>
    	<jsp:param value="1" name="tab"/>
    	<jsp:param value="<%=RegionCode%>" name="RegionCode"/>
    </jsp:include>
    <!-- /header -->
    
    <div data-role="content" data-theme="d">
        
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
		<div>
			<table style="width: 100%;">
				<tr>
					<td>
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="BeatPlanSave" href="#" aonClick="BeatPlanSubmit();" class="ui-disabled">Save</a>
						
	                    
					</td>
					<!-- <td align="right">
	                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="BeatPlanSearch" >Search</a>
					</td> -->
	                
				</tr>
			</table>
		</div>
	    	
    </div>
    
    <jsp:include page="LookupRegionSearchPopup.jsp" > 
    	<jsp:param value="RegionSearchCallBack" name="CallBack" />
    </jsp:include><!-- Include Region Search -->
    
</div>
</body>
</html>