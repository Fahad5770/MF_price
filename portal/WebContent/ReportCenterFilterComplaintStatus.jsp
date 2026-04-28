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
<style>
.ui-btn {
text-align: left
}
</style>
<script>

</script>

            <%
            long UniqueVoID = Utilities.parseLong(request.getParameter("UniqueSessionID"));
            String ClickIDD = Utilities.filterString(request.getParameter("ClickID"),1,100);
            
            Datasource ds = new Datasource();
            ds.createConnection();
            Statement s = ds.createStatement();        
           %>
           
         
			<fieldset data-role="controlgroup" data-corners="true" style="margin-left: 0px; margin-top: -10px; padding-top: 0px;">
			<input type="button" name="Apply" id="Apply" data-theme="a" value="RSM" style="text-align: left" data-mini="true">
			
           <%
           boolean CheckBoxSelected=false; 
           String Query="";
           String OrderBookerIds="";
           String DistributorIds="";
           long SelectedOrderBookerArray[] = null;
           long SelectedDistributorArray[] = null;
           boolean IsEmptyResultSet = true;
           boolean IsEmptyDistributor = true;
           
           
              
          	
	       
           
           //System.out.println(Query);
           
           
            
            boolean IsComplaintStatusSelected1=false;
            boolean IsComplaintStatusSelected2=false;
            boolean IsComplaintStatusSelected3=false;
            
            
            String SelectedOutletTypeArray[] = null;
            if (session.getAttribute(UniqueVoID+"_SR1SelectedComplaintStatus") != null){
            	SelectedOutletTypeArray = (String[])session.getAttribute(UniqueVoID+"_SR1SelectedComplaintStatus");           	
            }
           
            	IsEmptyResultSet = false;
            	if (session.getAttribute(UniqueVoID+"_SR1SelectedComplaintStatus") != null){
            		for(int i=0;i<SelectedOutletTypeArray.length;i++)
                	{            		
              			if(SelectedOutletTypeArray[i].equals("Assigned"))
              			{
              				IsComplaintStatusSelected1 = true;            			
              			}  
              			if(SelectedOutletTypeArray[i].equals("Resolved"))
              			{
              				IsComplaintStatusSelected2 = true;            			
              			}  
              			if(SelectedOutletTypeArray[i].equals("Verified"))
              			{
              				IsComplaintStatusSelected3 = true;            			
              			}  
              			
                	}
            	}
            	
            	
            	%>    		
           			<input type="checkbox" name="ComplaintStatusCheckbox" id="ComplaintStatus_1" value="Assigned" <%if(IsComplaintStatusSelected1){ %>checked<%} %> data-mini="true">
   					<label for="ComplaintStatus_1" ><span style="font-size:9pt; font-weight:normal;">Assigned</span></label>
   					
   					<input type="checkbox" name="ComplaintStatusCheckbox" id="ComplaintStatus_2" value="Resolved" <%if(IsComplaintStatusSelected2){ %>checked<%} %> data-mini="true">
   					<label for="ComplaintStatus_2" ><span style="font-size:9pt; font-weight:normal;">Resolved</span></label>
   					
   					<input type="checkbox" name="ComplaintStatusCheckbox" id="ComplaintStatus_3" value="Verified" <%if(IsComplaintStatusSelected3){ %>checked<%} %> data-mini="true">
   					<label for="ComplaintStatus_3" ><span style="font-size:9pt; font-weight:normal;">Verified</span></label>   					
				
            	<%
            	
           
            
            
           
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	