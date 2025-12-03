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
			<!-- <input type="button" name="Apply" id="Apply" data-theme="a" value="RSM" style="text-align: left" data-mini="true"> -->
			<input type="button" name="Apply" id="Apply" data-theme="a" value="ASM" style="text-align: left" data-mini="true">
			
           <%
           boolean CheckBoxSelected=false; 
           String Query="";
           String OrderBookerIds="";
           //String DistributorIds="";
           long SelectedOrderBookerArray[] = null;
           long SelectedDistributorArray[] = null;
           boolean IsEmptyResultSet = true;
           boolean IsEmptyDistributor = true;
           
           String HODIDs="";
           long SelectedHODArray[] = null;
           if (session.getAttribute(UniqueVoID+"_SR1SelectedHOD") != null){
           	SelectedHODArray = (long[])session.getAttribute(UniqueVoID+"_SR1SelectedHOD");
           	HODIDs = Utilities.serializeForSQL(SelectedHODArray);
           }

           String WhereHOD = "";
           if (HODIDs.length() > 0){
           	WhereHOD = " and cd.snd_id in ("+HODIDs+") ";	
           }
           
              
          	
           long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
           int FeatureID =0;
           if (session.getAttribute("SR1FeatureID") != null)
           {
           	FeatureID = (Integer)session.getAttribute("SR1FeatureID");
           }
           Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
           
           String DistributorIds = UserAccess.getDistributorQueryString(UserDistributor);
	       
           
           //System.out.println(Query);
           System.out.println("SELECT distinct cd.rsm_id,display_name FROM common_distributors cd, users u  where cd.rsm_id=u.id and cd.distributor_id in ("+DistributorIds+") "+WhereHOD);
            ResultSet rs = s.executeQuery("SELECT distinct cd.rsm_id,display_name FROM common_distributors cd, users u  where cd.rsm_id=u.id and cd.distributor_id in ("+DistributorIds+") "+WhereHOD);
            
            boolean IsRSMSelected=false;
            long SelectedRSMArray[] = null;
            if (session.getAttribute(UniqueVoID+"_SR1SelectedRSM") != null){
            	SelectedRSMArray = (long[])session.getAttribute(UniqueVoID+"_SR1SelectedRSM");           	
            }
            while(rs.next()){
            	IsEmptyResultSet = false;
            	if (session.getAttribute(UniqueVoID+"_SR1SelectedRSM") != null){
            		for(int i=0;i<SelectedRSMArray.length;i++)
                	{            		
              			if(rs.getLong("rsm_id")==SelectedRSMArray[i])
              			{
              				IsRSMSelected = true;            			
              			}            		
                	}
            	}
            	
            	
            	%>    		
           			<input type="checkbox" name="RSMCheckbox" id="RSM_<%=rs.getString("rsm_id") %>" value="<%=rs.getString("rsm_id") %>" <%if(IsRSMSelected){ %>checked<%} %> data-mini="true">
   					<label for="RSM_<%=rs.getString("rsm_id") %>" ><span style="font-size:9pt; font-weight:normal;"><%=rs.getString("rsm_id") %> - <%=rs.getString("display_name") %></span></label> 
            		
				
            	<%
            	IsRSMSelected=false;
            }
            
            
           
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	