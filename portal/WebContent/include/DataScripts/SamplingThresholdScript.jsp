<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.util.UserAccess"%>
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
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="../../include/ValidateSession.jsp" %>




<%
asdasdadasd
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(51, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();
Statement s1 = ds.createStatement();

%>
<html>


<head>
		<jsp:include page="../../include/StandaloneSrc.jsp" /> <!-- JQM Base -->
        <script src="../../js/lookups.js"></script>
        <script src="../../js/PriceList.js"></script>
        
        
</head>

<body>

<div data-role="page" id="PriceList" data-url="PriceList" data-theme="d">

    <jsp:include page="../../Header2.jsp" >
    	<jsp:param value="Sampling Thrseshold Script" name="title"/>

    		

    </jsp:include>
    
    <div data-role="content" data-theme="d">
	
	<%
	ResultSet rs = s.executeQuery("SELECT sampling_id, fixed_company_share FROM pep.sampling where active = 1 and fixed_company_share != 0");
	while(rs.next()){
		s1.executeUpdate("insert ignore into sampling_fixed_threshold(sampling_id,percentage,converted_sales,discount) values("+rs.getInt("sampling_id")+",100,100,"+rs.getDouble("fixed_company_share")+")");
		s1.executeUpdate("insert ignore into sampling_fixed_threshold(sampling_id,percentage,converted_sales,discount) values("+rs.getInt("sampling_id")+",60,60,"+rs.getDouble("fixed_company_share")+")");
		s1.executeUpdate("insert ignore into sampling_fixed_threshold(sampling_id,percentage,converted_sales,discount) values("+rs.getInt("sampling_id")+",30,30,"+rs.getDouble("fixed_company_share")+")");
		s1.executeUpdate("insert ignore into sampling_fixed_threshold(sampling_id,percentage,converted_sales,discount) values("+rs.getInt("sampling_id")+",15,15,"+rs.getDouble("fixed_company_share")+")");
		s1.executeUpdate("insert ignore into sampling_fixed_threshold(sampling_id,percentage,converted_sales,discount) values("+rs.getInt("sampling_id")+",5,5,0)");
	}
	
	%>
	
	
	
	
    </div><!-- /content -->
    <jsp:include page="../../LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="DistributorSearchCallBackForUserRights" name="CallBack" />
    </jsp:include><!-- Include Distributor Search -->
    <div data-role="footer" data-position="fixed" data-theme="b">
    
	<div>
		<table style="width: 100%;">
			<tr>
				<td>
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="DeliveryNoteSave" href="#"  onClick="SavePriceList();">Save</a>
                    
                   
                    
                    <button data-icon="check" data-theme="b" data-inline="true" id="DeliveryNoteReset" onClick="javascript:window.location='PriceList.jsp'" >Reset</button>
                    
				</td>
                <td align="right">
                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="PriceListSearch" >Search</a>
				</td>
			</tr>
		</table>
	</div>
	    	
    </div>
    

	

	
 
</div>

</body>
</html>
<%
s.close();
s1.close();
ds.dropConnection();
%>