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
			<input type="button" name="Apply" id="Apply" data-theme="a" value="Outlet Type" style="text-align: left" data-mini="true">
			
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
           
           
            
            boolean IsOutletTypeSelected1=false;
            boolean IsOutletTypeSelected2=false;
            boolean IsOutletTypeSelected3=false;
            boolean IsOutletTypeSelected4=false;
            boolean IsOutletTypeSelected5=false;
            boolean IsOutletTypeSelected6=false;
            
            String SelectedOutletTypeArray[] = null;
            if (session.getAttribute(UniqueVoID+"_SR1SelectedOutletType") != null){
            	SelectedOutletTypeArray = (String[])session.getAttribute(UniqueVoID+"_SR1SelectedOutletType");           	
            }
           
            	IsEmptyResultSet = false;
            	if (session.getAttribute(UniqueVoID+"_SR1SelectedOutletType") != null){
            		for(int i=0;i<SelectedOutletTypeArray.length;i++)
                	{            		
              			if(SelectedOutletTypeArray[i].equals("Discounted - All"))
              			{
              				IsOutletTypeSelected1 = true;            			
              			}  
              			if(SelectedOutletTypeArray[i].equals("Discounted - Fixed"))
              			{
              				IsOutletTypeSelected2 = true;            			
              			}  
              			if(SelectedOutletTypeArray[i].equals("Discounted - Per Case"))
              			{
              				IsOutletTypeSelected3 = true;            			
              			}  
              			if(SelectedOutletTypeArray[i].equals("Active"))
              			{
              				IsOutletTypeSelected4 = true;            			
              			}  
              			if(SelectedOutletTypeArray[i].equals("Deactivated"))
              			{
              				IsOutletTypeSelected5 = true;            			
              			} 
              			if(SelectedOutletTypeArray[i].equals("Non Discounted"))
              			{
              				IsOutletTypeSelected6 = true;            			
              			} 
                	}
            	}
            	
            	
            	%>    		
           			<input type="checkbox" name="OutletTypeCheckbox" id="OutletType_1" value="Discounted - All" <%if(IsOutletTypeSelected1){ %>checked<%} %> data-mini="true">
   					<label for="OutletType_1" ><span style="font-size:9pt; font-weight:normal;">Discounted - All</span></label>
   					
   					<input type="checkbox" name="OutletTypeCheckbox" id="OutletType_2" value="Discounted - Fixed" <%if(IsOutletTypeSelected2){ %>checked<%} %> data-mini="true">
   					<label for="OutletType_2" ><span style="font-size:9pt; font-weight:normal;">Discounted - Fixed</span></label>
   					
   					<input type="checkbox" name="OutletTypeCheckbox" id="OutletType_3" value="Discounted - Per Case" <%if(IsOutletTypeSelected3){ %>checked<%} %> data-mini="true">
   					<label for="OutletType_3" ><span style="font-size:9pt; font-weight:normal;">Discounted - Per Case</span></label>
   					
   					<input type="checkbox" name="OutletTypeCheckbox" id="OutletType_6" value="Non Discounted" <%if(IsOutletTypeSelected6){ %>checked<%} %> data-mini="true">
   					<label for="OutletType_6" ><span style="font-size:9pt; font-weight:normal;">Non Discounted</span></label>
   					
   					<input type="checkbox" name="OutletTypeCheckbox" id="OutletType_4" value="Active" <%if(IsOutletTypeSelected4){ %>checked<%} %> data-mini="true">
   					<label for="OutletType_4" ><span style="font-size:9pt; font-weight:normal;">Active</span></label>
   					
   					<input type="checkbox" name="OutletTypeCheckbox" id="OutletType_5" value="Deactivated" <%if(IsOutletTypeSelected5){ %>checked<%} %> data-mini="true">
   					<label for="OutletType_5" ><span style="font-size:9pt; font-weight:normal;">Deactivated</span></label> 
            		
				
            	<%
            	
           
            
            
           
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	