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
         
           boolean IsEmptyResultSet = true;
           
          

           
           
              
          	
         
	       
           
           //System.out.println(Query);
           
            
            
            boolean IsLiftingTypeSelected1=false;
            boolean IsLiftingTypeSelected2=false;
            String SelectedLiftingTypeArray[] = null;
            if (session.getAttribute(UniqueVoID+"_SR1SelectedLiftingType") != null){
            	SelectedLiftingTypeArray = (String[])session.getAttribute(UniqueVoID+"_SR1SelectedLiftingType");           	
            }
           
            	if (session.getAttribute(UniqueVoID+"_SR1SelectedLiftingType") != null){
            		for(int i=0;i<SelectedLiftingTypeArray.length;i++)
                	{            		
              			if(SelectedLiftingTypeArray[i].equals("Internal"))
              			{
              				IsLiftingTypeSelected1 = true;            			
              			}else{
              				IsLiftingTypeSelected2 = true;
              			}
                	}
            	}
            	
            	
            	%>    		
           			<input type="checkbox" name="LiftingTypeCheckbox" id="LiftingType_1" value="Internal" <%if(IsLiftingTypeSelected1){ %>checked<%} %> data-mini="true">
   					<label for="LiftingType_1" ><span style="font-size:9pt; font-weight:normal;">Internal</span></label>
   					
   					<input type="checkbox" name="LiftingTypeCheckbox" id="LiftingType_2" value="Other Plant" <%if(IsLiftingTypeSelected2){ %>checked<%} %> data-mini="true">
   					<label for="LiftingType_2" ><span style="font-size:9pt; font-weight:normal;">Other Plant</span></label> 
            		
				
            	<%
            	
            
            
           
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	