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

long ComplaintID=0;
boolean IsComplaintIDSelected=false;

if (session.getAttribute(UniqueVoID+"_SR1SelectedComplaintID") != null){
	ComplaintID = (Long)session.getAttribute(UniqueVoID+"_SR1SelectedComplaintID");	
	
	if(ComplaintID != 0)
	{
		IsComplaintIDSelected=true;		
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
<li data-role="list-divider" data-theme="a">Complaint ID</li>




<li style="font-weight:normal;">
<input type="text" name="ComplaintID" id="ComplaintID" placeholder="Complaint ID" value="<%if(ComplaintID !=0){ %><%=ComplaintID %><%} %>" data-mini="true"   maxlength="10">


</li>

     <li data-icon="check">
     <a href="#" onClick="AddPackagesIntoSession();" data-iconpos="left">Apply Specified Data</a>     
     
     
</li>
      </ul>  	
          
            
            
        	