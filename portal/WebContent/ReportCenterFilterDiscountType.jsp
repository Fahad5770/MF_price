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
			<input type="button" name="Apply" id="Apply" data-theme="a" value="Discount Type" style="text-align: left" data-mini="true">
			
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
           
           
            
            boolean IsDiscountTypeSelected1=false;
            boolean IsDiscountTypeSelected2=false;
            boolean IsDiscountTypeSelected3=false;
            boolean IsDiscountTypeSelected4=false;
           
            
            String SelectedDiscountTypeArray[] = null;
            if (session.getAttribute(UniqueVoID+"_SR1SelectedDiscountType") != null){
            	SelectedDiscountTypeArray = (String[])session.getAttribute(UniqueVoID+"_SR1SelectedDiscountType");           	
            }
           
            	IsEmptyResultSet = false;
            	if (session.getAttribute(UniqueVoID+"_SR1SelectedDiscountType") != null){
            		for(int i=0;i<SelectedDiscountTypeArray.length;i++)
                	{            		
              			if(SelectedDiscountTypeArray[i].equals("Fixed"))
              			{
              				IsDiscountTypeSelected1 = true;            			
              			}  
              			if(SelectedDiscountTypeArray[i].equals("Per Case"))
              			{
              				IsDiscountTypeSelected2 = true;            			
              			}  
              			if(SelectedDiscountTypeArray[i].equals("Fixed & Per Case"))
              			{
              				IsDiscountTypeSelected3 = true;            			
              			}  
              			if(SelectedDiscountTypeArray[i].equals("Advance Amount"))
              			{
              				IsDiscountTypeSelected4 = true;            			
              			}  
              			
                	}
            	}
            	
            	
            	%>    		
           			<input type="checkbox" name="DiscountTypeCheckbox" id="OutletType_1" value="Fixed" <%if(IsDiscountTypeSelected1){ %>checked<%} %> data-mini="true">
   					<label for="OutletType_1" ><span style="font-size:9pt; font-weight:normal;">Fixed</span></label>
   					
   					<input type="checkbox" name="DiscountTypeCheckbox" id="OutletType_2" value="Per Case" <%if(IsDiscountTypeSelected2){ %>checked<%} %> data-mini="true">
   					<label for="OutletType_2" ><span style="font-size:9pt; font-weight:normal;">Per Case</span></label>
   					
   					<!-- <input type="checkbox" name="DiscountTypeCheckbox" id="OutletType_3" value="Fixed & Per Case" <%if(IsDiscountTypeSelected3){ %>checked<%} %> data-mini="true">
   					<label for="OutletType_3" ><span style="font-size:9pt; font-weight:normal;">Fixed & Per Case</span></label> -->
   					
   					<input type="checkbox" name="DiscountTypeCheckbox" id="OutletType_6" value="Advance Amount" <%if(IsDiscountTypeSelected4){ %>checked<%} %> data-mini="true">
   					<label for="OutletType_6" ><span style="font-size:9pt; font-weight:normal;">Advance Amount</span></label>
   					
   					
            		
				
            	<%
            	
            	s.close();            
            	ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	