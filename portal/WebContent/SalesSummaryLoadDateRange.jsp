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
if (session.getAttribute("SR1StartDate") != null){
	SelectedStartDate = (Date)session.getAttribute("SR1StartDate");	
	StartDate = Utilities.getDisplayDateFormat(SelectedStartDate);
}
if (session.getAttribute("SR1EndDate") != null){
	SelectedEndDate = (Date)session.getAttribute("SR1EndDate");	
	EndDate = Utilities.getDisplayDateFormat(SelectedEndDate);
}

%>
<fieldset data-role="controlgroup">
			<input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true">

    
        <input type="radio" name="radio-choice-b" id="radio-choice-c" value="list" checked="checked" data-mini="true">
        <label for="radio-choice-c">Today</label>
        <input type="radio" name="radio-choice-b" id="radio-choice-d" value="grid" data-mini="true">
        <label for="radio-choice-d">This week</label>
        <input type="radio" name="radio-choice-b" id="radio-choice-e" value="gallery" data-mini="true">
        <label for="radio-choice-e">This month</label>
        <input type="radio" name="radio-choice-b" id="radio-choice-f" value="list"  data-mini="true">
        <label for="radio-choice-f">Yesterday</label>
        <input type="radio" name="radio-choice-b" id="radio-choice-g" value="grid" data-mini="true">
        <label for="radio-choice-g">Last week</label>
        <input type="radio" name="radio-choice-b" id="radio-choice-h" value="gallery" data-mini="true">
        <label for="radio-choice-h">Last month</label>
		
		<label style="font-size:10pt;">From Date</label>
		<input type="text" name="StartDate" id="StartDate" placeholder="From Date" data-mini="true" value="<%=StartDate%>"/>
		<label style="font-size:10pt;">To Date</label>
		<input type="text" name="EndDate" id="EndDate" placeholder="To Date" data-mini="true" value="<%=EndDate%>"/>
			

	<input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true">
 
    </fieldset>

        	
        	
            
           
            
            
        	