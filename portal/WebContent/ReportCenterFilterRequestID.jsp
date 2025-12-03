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

long UniqueVoID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

long RequestID=0;
boolean IsRequestIDSelected=false;

if (session.getAttribute(UniqueVoID+"_SR1SelectedRequestID") != null){
	RequestID = (Long)session.getAttribute(UniqueVoID+"_SR1SelectedRequestID");	
	
	if(RequestID != 0)
	{
		IsRequestIDSelected=true;		
	}
}

%>

<style>
a{
font-size:10pt;
font-weight: normal;
}
</style>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Request ID</li>




<li style="font-weight:normal;">
<input type="text" name="RequestID" id="RequestID" placeholder="Request ID" value="<%if(RequestID !=0){ %><%=RequestID %><%} %>" data-mini="true"   maxlength="10">


</li>

     <li data-icon="check">
     <a href="#" onClick="AddPackagesIntoSession();" data-iconpos="left">Apply Specified Data</a>     
     
     
</li>
      </ul>  	
          
            
            
        	