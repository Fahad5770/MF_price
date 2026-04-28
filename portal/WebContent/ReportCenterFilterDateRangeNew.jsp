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




<li>
Select Month<br>

<select name="StartDate" id="StartDate" data-mini="true">
    <%
    int MonthSession = Utilities.getMonthNumberByDate(SelectedStartDate);
    for(int x=1;x<13;x++){ %>
    <option value="<%=x-1%>" <% if (x == MonthSession){out.print("selected");} %>><%=Utilities.getMonthNameByNumber(x) %></option>
    <%} %>    
    
    
</select><br/>
Select Year<br>

<select name="EndDate" id="EndDate" data-mini="true">
<%
      
		int yearSession = Utilities.getYearByDate(SelectedStartDate);
//System.out.println(yearSession);
		int year = Utilities.getYearByDate(new java.util.Date());
      
      for (int i = 2013; i < (year+2); i++){
      %>
      <option value="<%=i%>" <% if (i == yearSession){out.print("selected");} %>><%=i %></option>
      <%
      }
      %>
</select><br/>
</li>

     <li data-icon="check" <%if (DateType.equals("Specified Date")){%>data-theme="e"<%} %>>
     <a href="#" onClick="AddPackagesIntoSession();LoadDateRangeNew('LoadDateRangeHyperlink');" data-iconpos="left">Apply Specified Date</a>     
     
     
</li>
      </ul>  	
<input type="hidden" name="SelectedDateType" id="SelectedDateType" value="Today">           
            
            
        	