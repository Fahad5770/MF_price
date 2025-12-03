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
			<input type="button" name="Apply" id="Apply" data-theme="a" value="SM" style="text-align: left" data-mini="true">
			
			<input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
			
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
           
            ResultSet rs = s.executeQuery("SELECT distinct dbpav.sm_id,display_name FROM distributor_beat_plan_all_view dbpav, users u  where dbpav.sm_id=u.id");
            
            boolean IsSMSelected=false;
            long SelectedSMArray[] = null;
            if (session.getAttribute(UniqueVoID+"_SR1SelectedSM") != null){
            	SelectedSMArray = (long[])session.getAttribute(UniqueVoID+"_SR1SelectedSM");           	
            }
            while(rs.next()){
            	IsEmptyResultSet = false;
            	if (session.getAttribute(UniqueVoID+"_SR1SelectedSM") != null){
            		for(int i=0;i<SelectedSMArray.length;i++)
                	{            		
              			if(rs.getLong("sm_id")==SelectedSMArray[i])
              			{
              				IsSMSelected = true;            			
              			}            		
                	}
            	}
            	
            	
            	%>    		
           			<input type="checkbox" name="SMCheckbox" id="SM_<%=rs.getString("sm_id") %>" value="<%=rs.getString("sm_id") %>" <%if(IsSMSelected){ %>checked<%} %> data-mini="true">
   					<label for="SM_<%=rs.getString("sm_id") %>" ><span style="font-size:9pt; font-weight:normal;"><%=rs.getString("sm_id") %> - <%=rs.getString("display_name") %></span></label> 
            		
				
            	<%
            	IsSMSelected=false;
            }
            
            
           
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	