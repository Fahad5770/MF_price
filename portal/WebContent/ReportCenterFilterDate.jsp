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

<li <%if (DateType.equals("Today")){%>data-theme="e"<%} %>><a href="#"  onClick="GetTodayDate1()" >Today</a></li>

<li <%if (DateType.equals("Yesterday")){%>data-theme="e"<%} %>><a href="#"  onClick="GetYesterdayDate1()" >Yesterday</a></li>





<li>

<input type="hidden" name="StartDate" id="StartDate" placeholder="From Date" data-mini="true" value="<%=EndDate%>"/><br>
Date<br>
<input type="text" name="EndDate" id="EndDate" placeholder="To Date" data-mini="true" value="<%=EndDate%>"/>
</li>

     <li data-icon="check" <%if (DateType.equals("Specified Date")){%>data-theme="e"<%} %>>
     <a href="#" onClick="$('#SelectedDateType').val('Specified Date');AddPackagesIntoSession();LoadDate('LoadDateRangeHyperlink1');" data-iconpos="left">Apply Specified Date</a>     
     
     
</li>
      </ul>  	
<input type="hidden" name="SelectedDateType" id="SelectedDateType" value="Today">           
            
            
        	