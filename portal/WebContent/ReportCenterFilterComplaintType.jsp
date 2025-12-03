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
Datasource ds = new Datasource();
ds.createConnection();
Statement s = ds.createStatement(); 


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
<li data-role="list-divider" data-theme="a">Complaint Type</li>




<li style="font-weight:normal;">

<select name="ComplaintType" id="ComplaintType" data-mini="true">
    <option value="standard">All</option>
    <%
    boolean IsComplaintTypeSelected=false;
    long SelectedComplaintType = 0;
    if (session.getAttribute(UniqueVoID+"_SR1SelectedComplaintType") != null){
    	SelectedComplaintType = (Long)session.getAttribute(UniqueVoID+"_SR1SelectedComplaintType");           	
    }
    
    
    ResultSet rs = s.executeQuery("select * from crm_complaints_types");
    while(rs.next()){
    	
    	if(rs.getLong("id")==SelectedComplaintType)
		{
    		IsComplaintTypeSelected = true;            			
		} 
    	
    %>
    <option value="<%=rs.getLong("id")%>" <%if(IsComplaintTypeSelected){ %>selected<%} %>><%=rs.getString("label") %></option>
    <%
    IsComplaintTypeSelected = false;
    }
    %>
</select>

</li>

     <li data-icon="check">
     <a href="#" onClick="AddPackagesIntoSession();" data-iconpos="left">Apply Specified Data</a>     
     
     
</li>
      </ul>  	
          
            
            
        	