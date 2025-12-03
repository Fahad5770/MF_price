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


if (session.getAttribute(UniqueVoID+"_SR1DateTimeType") != null){
	DateType = (String)session.getAttribute(UniqueVoID+"_SR1DateTimeType");
}else{
	DateType = "Today";
}
if (session.getAttribute(UniqueVoID+"_SR1StartDateTime") != null){
	SelectedStartDate = (Date)session.getAttribute(UniqueVoID+"_SR1StartDateTime");	
	StartDate = Utilities.getDisplayDateFormat(SelectedStartDate);
}
else
{
	
	Date todaydate = new Date(); 
	StartDate = Utilities.getDisplayDateFormat(todaydate);
}
if (session.getAttribute(UniqueVoID+"_SR1EndDateTime") != null){
	SelectedEndDate = (Date)session.getAttribute(UniqueVoID+"_SR1EndDateTime");	
	EndDate = Utilities.getDisplayDateFormat(SelectedEndDate);
	System.out.println("hello 15  "+EndDate);
}
else
{
	Date todaydate = new Date(); 
	EndDate = Utilities.getDisplayDateFormat(todaydate);
	System.out.println("hello "+EndDate);
}

int StartDateTimeHour=0;
if (session.getAttribute(UniqueVoID+"_SR1StartDateTimeHour") != null){
	StartDateTimeHour = (Integer)session.getAttribute(UniqueVoID+"_SR1StartDateTimeHour");
}else{
	StartDateTimeHour =6;
}

int StartDateTimeMin=0;
if (session.getAttribute(UniqueVoID+"_SR1StartDateTimeMinutes") != null){
	StartDateTimeMin = (Integer)session.getAttribute(UniqueVoID+"_SR1StartDateTimeMinutes");
}else{
	StartDateTimeMin=0;
}


//////////////////

int EndDateTimeHour=0;
if (session.getAttribute(UniqueVoID+"_SR1EndDateTimeHour") != null){
	EndDateTimeHour = (Integer)session.getAttribute(UniqueVoID+"_SR1EndDateTimeHour");
}else{
	EndDateTimeHour =0;
}

int EndDateTimeMin=0;
if (session.getAttribute(UniqueVoID+"_SR1EndDateTimeMinutes") != null){
	EndDateTimeMin = (Integer)session.getAttribute(UniqueVoID+"_SR1EndDateTimeMinutes");
}else{
	EndDateTimeMin=0;
}

//System.out.println("heeeee "+StartDateTimeHour);

String StartDateTimeHourSelected="";
String Hours = "";
for(int i = 0; i < 24; i++){
	
	if(i==0){
		StartDateTimeHourSelected="selected";	
	}else{
		StartDateTimeHourSelected="";
	}
	
	String ZeroDigit = "";
	if(i < 10){
		ZeroDigit = "0";
	}
	Hours += "<option value="+i+" "+StartDateTimeHourSelected+">"+ZeroDigit+i+"</option>";
	
	
}

String StartDateTimeMinSelected="";
String Minutes = "";
for(int i = 0; i < 60; i++){
	
	if(i==0){
		StartDateTimeMinSelected="selected";	
	}else{
		StartDateTimeMinSelected="";
	}
	
	String ZeroDigit = "";
	if(i < 10){
		ZeroDigit = "0";
	}
	
	Minutes += "<option value="+i+" "+StartDateTimeMinSelected+">"+ZeroDigit+i+"</option>";
	
}


/// end

String EndDateTimeHourSelected="";
String EHours = "";
for(int i = 0; i < 24; i++){
	
	if(i==23){
		EndDateTimeHourSelected="selected";	
	}else{
		EndDateTimeHourSelected="";
	}
	
	String ZeroDigit = "";
	if(i < 10){
		ZeroDigit = "0";
	}
	EHours += "<option value="+i+" "+EndDateTimeHourSelected+">"+ZeroDigit+i+"</option>";
	
	
}

String EndDateTimeMinSelected="";
String EMinutes = "";
for(int i = 0; i < 60; i++){
	
	if(i==59){
		EndDateTimeMinSelected="selected";	
	}else{
		EndDateTimeMinSelected="";
	}
	
	String ZeroDigit = "";
	if(i < 10){
		ZeroDigit = "0";
	}
	
	EMinutes += "<option value="+i+" "+EndDateTimeMinSelected+">"+ZeroDigit+i+"</option>";
	
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

<li <%if (DateType.equals("Today")){%>data-theme="e"<%} %>><a href="#"  onClick="GetTodayDateTime()" >Today</a></li>
<li <%if (DateType.equals("This Week")){%>data-theme="e"<%} %>><a href="#"  onClick="GetThisWeekDateTime()" >This Week</a></li>
<li <%if (DateType.equals("This Month")){%>data-theme="e"<%} %>><a href="#"  onClick="GetThisMonthDateTime()" >This Month</a></li>
<li <%if (DateType.equals("Yesterday")){%>data-theme="e"<%} %>><a href="#"  onClick="GetYesterdayDateTime()" >Yesterday</a></li>


<li <%if (DateType.equals("Last Week")){%>data-theme="e"<%} %>><a href="#"  onClick="GetLastWeekDateTime()" >Last Week</a></li>

<li <%if (DateType.equals("Last Month")){%>data-theme="e"<%} %>><a href="#"  onClick="GetLastMonthDateTime()" >Last Month</a></li>


<li>
From Date<br>
<input type="text" name="StartDateTime" id="StartDateTime" placeholder="From Date" data-mini="true" value="<%=StartDate%>"/>
Time<br>
<select name="StartDateTimeHour" id="StartDateTimeHour" data-mini="true">
  	<%=Hours%>
</select>
<select name="StartDateTimeMinutes" id="StartDateTimeMinutes" data-mini="true">
  	<%=Minutes%>
</select>
<br>
To Date<br>
<input type="text" name="EndDateTime" id="EndDateTime" placeholder="To Date" data-mini="true" value="<%=EndDate%>"/>
Time<br>
<select name="EndDateTimeHour" id="EndDateTimeHour" data-mini="true">
  	<%=EHours%>
</select>
<select name="EndDateTimeMinutes" id="EndDateTimeMinutes" data-mini="true">
  	<%=EMinutes%>
</select>
</li>

     <li data-icon="check" <%if (DateType.equals("Specified Date")){%>data-theme="e"<%} %>>
     <a href="#" onClick="$('#SelectedDateType').val('Specified Date');AddPackagesIntoSession();LoadDateTimeRange('LoadDateTimeRangeHyperlink');" data-iconpos="left">Apply Specified Date</a>     
     
     
</li>
      </ul>  	
<input type="hidden" name="SelectedDateType" id="SelectedDateType" value="Today">

<script type="text/javascript">



</script>
        	