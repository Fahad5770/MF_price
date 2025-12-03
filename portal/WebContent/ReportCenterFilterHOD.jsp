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
			<input type="button" name="Apply" id="Apply" data-theme="a" value="HOD" style="text-align: left" data-mini="true">
			
           <%
           boolean CheckBoxSelected=false; 
           String Query="";
           String OrderBookerIds="";
           //String DistributorIds="";
           long SelectedOrderBookerArray[] = null;
           long SelectedDistributorArray[] = null;
           boolean IsEmptyResultSet = true;
           boolean IsEmptyDistributor = true;
           
           
              
           long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
           int FeatureID =0;
           if (session.getAttribute("SR1FeatureID") != null)
           {
           	FeatureID = (Integer)session.getAttribute("SR1FeatureID");
           }
           Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
           
           String DistributorIds = UserAccess.getDistributorQueryString(UserDistributor);
          	
	       
           
           //System.out.println(Query);
           
            ResultSet rs = s.executeQuery("SELECT distinct cd.snd_id,display_name FROM common_distributors cd, users u  where cd.snd_id=u.id and cd.distributor_id in ("+DistributorIds+")"); 
            
            
            boolean IsHODSelected=false;
            long SelectedHODArray[] = null;
            if (session.getAttribute(UniqueVoID+"_SR1SelectedHOD") != null){
            	SelectedHODArray = (long[])session.getAttribute(UniqueVoID+"_SR1SelectedHOD");           	
            }
            while(rs.next()){
            	IsEmptyResultSet = false;
            	if (session.getAttribute(UniqueVoID+"_SR1SelectedHOD") != null){
            		for(int i=0;i<SelectedHODArray.length;i++)
                	{            		
              			if(rs.getLong("snd_id")==SelectedHODArray[i])
              			{
              				IsHODSelected = true;            			
              			}            		
                	}
            	}
            	
            	
            	%>    		
           			<input type="checkbox" name="HODCheckbox" id="HOD_<%=rs.getString("snd_id") %>" value="<%=rs.getString("snd_id") %>" <%if(IsHODSelected){ %>checked<%} %> data-mini="true">
   					<label for="HOD_<%=rs.getString("snd_id") %>" ><span style="font-size:9pt; font-weight:normal;"><%=rs.getString("snd_id") %> - <%=rs.getString("display_name") %></span></label> 
            		
				
            	<%
            	IsHODSelected=false;
            }
            
            
           
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	