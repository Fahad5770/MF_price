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
<%@page import="com.pbc.util.Utilities"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>


<%
	long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(378, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();
Statement s2 = ds.createStatement();
%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
        <script src="js/lookups.js"></script>       
        <script src="js/SalesPeriodStatus.js?1=4"></script>
        
        
        
</head>

<body>

<div data-role="page" id="UserRight" data-url="UserRight" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Open Sales Period" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">
	<form id="SalesPeriodForm" data-ajax="false" >

	
	<ul data-role="listview" data-inset="false" data-divider-theme="c">
		<li>
			<table border="0"  style="width: 70%;font-size: 13px; font-weight: 400; width: 100%"
						cellpadding="0" cellspacing="0" 
						>
				<thead>
				
				</thead>
				<tbody>
				
					<tr style="font-size: 10pt; font-weight: 400;">
						<td style="" colspan="2">
						<input type="hidden" name="" id="isDistAdded" value="0"/>
							<div id="SalesPeriodDataScope" style="width:100%"></div>
						</td>
						
					</tr>
					<tr>
						<td>
							<select name="Month" id="Month" data-mini="true">
								<option value="-1">Select Month</option>
							<%for(int i=0;i<=12;i++){ %>
								<option value="<%=i%>"><%=Utilities.getMonthNameByNumber(i+1) %></option>
							<%} %>							
							</select>
						</td>
						<td>
							<select name="Year" id="Year" data-mini="true">
							<option value="-1">Select Year</option>
								<option value="2023">2023</option>
								<option value="2024">2024</option>
								<option value="2025">2025</option>						
							</select>
						</td>
						
						
						
					</tr>
					<tr>
						<td>
	    					<textarea rows="40" col="15" name="remarks" id="remarks" placeholder="Remarks"></textarea>
	    				</td>
					</tr>
				</tbody>
				
			</table>
		</li>
	</ul>
    		
   </form>
	
    	
    	</div>
	
   
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
    
     
    
	<div>
		<table style="width: 100%;">
			<tr>
				<td>
					 <a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="DeliveryNoteSave" href="#" onClick="SavePeriodStatus();">Save</a>
                    
                    <button data-icon="check" data-theme="b" data-inline="true" id="" onClick="javascript:window.location='SalesPeriodStatus.jsp'" >Reset</button>
                   
				</td>
                <!-- <td align="right">
                    <a href="#"  data-icon="check" data-theme="b" data-role="button" data-inline="true"   id="UserSearchDateButton" onclick="UserSearch();">Search</a>
				</td> -->
			</tr>
		</table>
	</div>
	    	
    </div>
    
    
	
	<jsp:include page="LookupProductSearchPopup.jsp" > 
    	<jsp:param value="ProductSearchCallBack" name="CallBack" />
    </jsp:include><!-- 
    Include Product Search -->

	

	<jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="DistributorSearchCallBackForUserRights" name="CallBack" />
    </jsp:include><!-- Include Distributor Search -->
    
    <jsp:include page="LookupUserSearchPopup.jsp" > 
    	<jsp:param value="UserRightsUserEdit" name="CallBack" />
    </jsp:include><!-- Include User Search -->

 </div><!-- /content -->	
</body>
</html>
<%
s2.close();
s.close();
ds.dropConnection();
%>