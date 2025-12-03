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

if(Utilities.isAuthorized(379, SessionUserID) == false){
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
        <script src="js/SalesPeriodStatus.js?12assssass2a3aa3=1232"></script>
        
        
        
</head>

<body>

<div data-role="page" id="UserRight" data-url="UserRight" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Close Sales Period" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">
	<form id="SalesPeriodChangeForm" data-ajax="false" >
<input type="hidden" id="togglebit" value="0"/>
	
	<ul data-role="listview" data-inset="false" data-divider-theme="c">
		<li>
			<table border="0"  style="width: 70%;font-size: 13px; font-weight: 400; width: 100%"
						cellpadding="0" cellspacing="0" 
						class="GridWithBorder">
				<thead>
				<tr>
					<th style="width:10%;text-align: center; background-color:#558bbb; color:#fff;">Sr#</th>
					<th style="width:30%;text-align: center; background-color:#558bbb;color:#fff;">Distributor</th>
					<th style="width:20%;text-align: center; background-color:#558bbb;color:#fff;">Month</th>
					<th style="width:20%;text-align: center; background-color:#558bbb;color:#fff;">Year</th>
					
					<th style="width:10%;text-align: center; b1ackground-color:#558bbb;color:#fff;">
						<input type="checkbox" name="CheckAll" id="CheckAll" value="all" data-mini="true" onClick="CheckAllMark()">
    					<label for="CheckAll" >Select All</label>
    
					</th>
				</tr>
				</thead>
				<tbody>
				
				
				<%
				int counter=1;
				ResultSet rs = s.executeQuery("SELECT *,(select name from common_distributors cd where cd.distributor_id=isps.distributor_id) distributor_name FROM inventory_sales_period_status isps where /*isps.month_number<=month(now()) and isps.year_number<=year(now())*/ is_closed=0");
				while(rs.next()){
					int PeriodID=rs.getInt("id");
					int Month=rs.getInt("month_number");
					int Year=rs.getInt("year_number");
					int IsClosed=rs.getInt("is_closed");
					long DistributorID = rs.getLong("distributor_id");
					
					String DistributorName = DistributorID+" - "+rs.getString("distributor_name");
					
					
				%>
				<tr>
					<td style="width:10%; text-align: center;"><%=counter %></td>
					<td style="width:30%;text-align: left;"><%=DistributorName %></td>
					<td style="width:20%;text-align: center;"><%=Utilities.getMonthNameByNumber(Month) %></td>
					<td style="width:20%;text-align: center;"><%=Year %></td>
					
					<td style="width:10%;text-align: center;" id="StatusCheckBoxes">
						<input type="checkbox" name="ActionCheckbox" id="ActionCheckbox_<%=PeriodID %>" class="AllClassC" value="<%=PeriodID %>" data-mini="true">
    					<label for="ActionCheckbox_<%=PeriodID %>" >Close</label>
    
					</td>
						
					
					</td>
				</tr>
				<%
				counter++;
				
				} %>
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
					 <a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="DeliveryNoteSave" href="#" onClick="ClosePeriodStatus();">Close Period</a>
                    
                    <button data-icon="check" data-theme="b" data-inline="true" id="" onClick="javascript:window.location='SalesPeriodStatusChange.jsp'" >Reset</button>
                   
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