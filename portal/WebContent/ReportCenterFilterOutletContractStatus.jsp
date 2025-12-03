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
			<input type="button" name="Apply" id="Apply" data-theme="a" value="Outlet Contract Status" style="text-align: left" data-mini="true">
			
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
           
           
            
            boolean IsOutletContractStatusSelected1=false;
            boolean IsOutletContractStatusSelected2=false;
            boolean IsOutletContractStatusSelected3=false;
            boolean IsOutletContractStatusSelected4=false;
            
            
            String SelectedOutletContractStatusArray[] = null;
            if (session.getAttribute(UniqueVoID+"_SR1SelectedOutletContractStatus") != null){
            	SelectedOutletContractStatusArray = (String[])session.getAttribute(UniqueVoID+"_SR1SelectedOutletContractStatus");           	
            }
           
            	IsEmptyResultSet = false;
            	if (session.getAttribute(UniqueVoID+"_SR1SelectedOutletContractStatus") != null){
            		for(int i=0;i<SelectedOutletContractStatusArray.length;i++)
                	{            		
              			if(SelectedOutletContractStatusArray[i].equals("New Outlets"))
              			{
              				IsOutletContractStatusSelected1 = true;            			
              			}  
              			if(SelectedOutletContractStatusArray[i].equals("Renewed Contracts"))
              			{
              				IsOutletContractStatusSelected2 = true;            			
              			}  
              			if(SelectedOutletContractStatusArray[i].equals("Converted to PEPSI"))
              			{
              				IsOutletContractStatusSelected3 = true;            			
              			} 
              			if(SelectedOutletContractStatusArray[i].equals("Converted to Coke"))
              			{
              				IsOutletContractStatusSelected4 = true;            			
              			} 
              			
                	}
            	}
            	
            	
            	%>    		
           			<input type="checkbox" name="OutletContractStatusCheckbox" id="OutletContractStatus_1" value="New Outlets" <%if(IsOutletContractStatusSelected1){ %>checked<%} %> data-mini="true">
   					<label for="OutletContractStatus_1" ><span style="font-size:9pt; font-weight:normal;">New Outlets</span></label>
   					
   					<input type="checkbox" name="OutletContractStatusCheckbox" id="OutletContractStatus_2" value="Renewed Contracts" <%if(IsOutletContractStatusSelected2){ %>checked<%} %> data-mini="true">
   					<label for="OutletContractStatus_2" ><span style="font-size:9pt; font-weight:normal;">Renewed Contracts</span></label>
   					
   					<input type="checkbox" name="OutletContractStatusCheckbox" id="OutletContractStatus_3" value="Converted to PEPSI" <%if(IsOutletContractStatusSelected3){ %>checked<%} %> data-mini="true">
   					<label for="OutletContractStatus_3" ><span style="font-size:9pt; font-weight:normal;">Converted to PEPSI</span></label>  
   					
   					<input type="checkbox" name="OutletContractStatusCheckbox" id="OutletContractStatus_4" value="Converted to Coke" <%if(IsOutletContractStatusSelected4){ %>checked<%} %> data-mini="true">
   					<label for="OutletContractStatus_4" ><span style="font-size:9pt; font-weight:normal;">Converted to Coke</span></label>   					
				
            	<%
            	
           
            
            
           
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	