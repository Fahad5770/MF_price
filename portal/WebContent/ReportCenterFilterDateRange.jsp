<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%

Date SelectedStartDate;
Date SelectedEndDate;
String StartDate ="";
String EndDate="";
String DateType = "";

long UniqueVoID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


if (session.getAttribute(UniqueVoID+"_SR1DateType") != null){
	DateType = (String)session.getAttribute(UniqueVoID+"_SR1DateType");
}else{
	DateType = "Today";
}
if (session.getAttribute(UniqueVoID+"_SR1StartDate") != null){
	SelectedStartDate = (Date)session.getAttribute(UniqueVoID+"_SR1StartDate");	
	StartDate = Utilities.getDisplayDateFormat(SelectedStartDate);
}
else
{
	
	Date todaydate = new Date(); 
	StartDate = Utilities.getDisplayDateFormat(todaydate);
}
if (session.getAttribute(UniqueVoID+"_SR1EndDate") != null){
	SelectedEndDate = (Date)session.getAttribute(UniqueVoID+"_SR1EndDate");	
	EndDate = Utilities.getDisplayDateFormat(SelectedEndDate);
}
else
{
	Date todaydate = new Date(); 
	EndDate = Utilities.getDisplayDateFormat(todaydate);
}

%>

<style>
a{
font-size:10pt;
font-weight: normal;
}
</style>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Date Range</li>

<li <%if (DateType.equals("Today")){%>data-theme="e"<%} %>><a href="#"  onClick="GetTodayDate()" >Today</a></li>
<li <%if (DateType.equals("This Week")){%>data-theme="e"<%} %>><a href="#"  onClick="GetThisWeekDate()" >This Week</a></li>
<li <%if (DateType.equals("This Month")){%>data-theme="e"<%} %>><a href="#"  onClick="GetThisMonthDate()" >This Month</a></li>
<li <%if (DateType.equals("Yesterday")){%>data-theme="e"<%} %>><a href="#"  onClick="GetYesterdayDate()" >Yesterday</a></li>


<li <%if (DateType.equals("Last Week")){%>data-theme="e"<%} %>><a href="#"  onClick="GetLastWeekDate()" >Last Week</a></li>

<li <%if (DateType.equals("Last Month")){%>data-theme="e"<%} %>><a href="#"  onClick="GetLastMonthDate()" >Last Month</a></li>


<li>
From Date<br>
<input type="text" name="StartDate" id="StartDate" placeholder="From Date" data-mini="true" value="<%=StartDate%>"/><br>
To Date<br>
<input type="text" name="EndDate" id="EndDate" placeholder="To Date" data-mini="true" value="<%=EndDate%>"/>
</li>

     <li data-icon="check" <%if (DateType.equals("Specified Date")){%>data-theme="e"<%} %>>
     <a href="#" onClick="$('#SelectedDateType').val('Specified Date');AddPackagesIntoSession();LoadDateRange('LoadDateRangeHyperlink');" data-iconpos="left">Apply Specified Date</a>     
     
     
</li>
      </ul>  	
<input type="hidden" name="SelectedDateType" id="SelectedDateType" value="Today">           
            
            
        	