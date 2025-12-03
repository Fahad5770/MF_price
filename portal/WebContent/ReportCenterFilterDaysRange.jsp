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

Date SelectedStartDate = null;
Date SelectedEndDate;
String StartDate ="";
String EndDate="";
String DateType = "";

int DaysGreaterThan=0;
int DaysLessThan=0;

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

//days range

if (session.getAttribute(UniqueVoID+"_SR1SelectedDaysGreaterThan") != null){
	DaysGreaterThan = (Integer)session.getAttribute(UniqueVoID+"_SR1SelectedDaysGreaterThan");
}

if (session.getAttribute(UniqueVoID+"_SR1SelectedDaysLessThan") != null){
	DaysLessThan = (Integer)session.getAttribute(UniqueVoID+"_SR1SelectedDaysLessThan");
}




%>

<style>
a{
font-size:10pt;
font-weight: normal;
}
</style>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Days</li>




<li style="font-weight:normal;">
Greater Than<br>
<input type="text" name="DateRangeNewFilterGreaterDays" id="DateRangeNewFilterGreaterDays"  value="<%=DaysGreaterThan %>" data-mini="true"   maxlength="3">

<br/>
Less Than<br>
<input type="text" name="DateRangeNewFilterLessDays" id="DateRangeNewFilterLessDays"  value="<%=DaysLessThan %>" data-mini="true"   maxlength="3">
<br/>
</li>

     <li data-icon="check" <%if (DateType.equals("Specified Date")){%>data-theme="e"<%} %>>
     <a href="#" onClick="AddPackagesIntoSession();" data-iconpos="left">Apply Specified Date</a>     
     
     
</li>
      </ul>  	
<input type="hidden" name="SelectedDateType" id="SelectedDateType" value="Today">           
            
            
        	