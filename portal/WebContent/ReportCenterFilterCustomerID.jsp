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

long CustomerID=0;
boolean IsCustomerIDSelected=false;

if (session.getAttribute(UniqueVoID+"_SR1SelectedCustomerID") != null){
	CustomerID = (Long)session.getAttribute(UniqueVoID+"_SR1SelectedCustomerID");	
	
	if(CustomerID != 0)
	{
		IsCustomerIDSelected=true;		
	}
}


boolean IsRegionSelected=false;
long SelectedRegionArray[] = null;
if (session.getAttribute(UniqueVoID+"_SR1SelectedAccountType") != null){
	SelectedRegionArray = (long[])session.getAttribute(UniqueVoID+"_SR1SelectedAccountType");           	
}

boolean AccountType1Flag = false;
boolean AccountType2Flag = false;
boolean AccountType3Flag = false;
boolean AccountType4Flag = false;
boolean AccountType5Flag = false;
boolean AccountType6Flag = false;
boolean AccountType7Flag = false;

if (session.getAttribute(UniqueVoID+"_SR1SelectedAccountType") != null){
	for(int i=0; i<SelectedRegionArray.length;i++){
    	if(SelectedRegionArray[i]==1){
    		AccountType1Flag = true;
    	}
    	if(SelectedRegionArray[i]==2){
    		AccountType2Flag = true;
    	}
    	if(SelectedRegionArray[i]==3){
    		AccountType3Flag = true;
    	}
    	if(SelectedRegionArray[i]==4){
    		AccountType4Flag = true;
    	}
    	if(SelectedRegionArray[i]==5){
    		AccountType5Flag = true;
    	}
    	if(SelectedRegionArray[i]==6){
    		AccountType6Flag = true;
    	}
    	if(SelectedRegionArray[i]==7){
    		AccountType7Flag = true;
    	}
    	
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
<li data-role="list-divider" data-theme="a">Customer</li>




<li style="font-weight:normal;">
<input type="text" name="CustomerID" id="CustomerID" placeholder="Customer ID" value="<%if(CustomerID !=0){ %><%=CustomerID %><%} %>" data-mini="true"   maxlength="10" ondblclick="R146CustomerIDLoadDistSearch()">
				
				
					<select data-mini="true" name="AccountTypeCheckbox">
						<option value="0">Account Type:</option>
						<option value="1" <%if(AccountType1Flag){ %>selected<%} %>>Ledger</option>
						<option value="2" <%if(AccountType2Flag){ %>selected<%} %>>Security</option>
						<option value="3" <%if(AccountType3Flag){ %>selected<%} %>>Credit</option>
						<option value="4" <%if(AccountType4Flag){ %>selected<%} %>>Vehicle</option>
						<option value="5" <%if(AccountType5Flag){ %>selected<%} %>>Empty</option>
						<option value="6" <%if(AccountType6Flag){ %>selected<%} %>>Advance</option>
						<option value="7" <%if(AccountType7Flag){ %>selected<%} %>>Incentive</option>
					</select>
					
					
					
            		

</li>

     <li data-icon="check">
     <a href="#" onClick="AddPackagesIntoSession();" data-iconpos="left">Apply Specified Data</a>     
     
     
</li>





      </ul>  	
          
            
            
        	