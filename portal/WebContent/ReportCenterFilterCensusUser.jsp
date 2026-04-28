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
			<input type="button" name="Apply" id="Apply" data-theme="a" value="Census User" style="text-align: left" data-mini="true">
			<input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
			
           <%
           boolean CheckBoxSelected=false; 
           String Query="";
           String OrderBookerIds="";
          
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
           
            ResultSet rs = s.executeQuery("SELECT distinct mc.created_by,(select display_name from users u where u.id=mc.created_by) user_name from mrd_census mc where  census_distributor_id in ("+DistributorIds+") order by created_by desc"); 
            
            
            boolean IsCensusUserSelected=false;
            long SelectedCensusUserArray[] = null;
            if (session.getAttribute(UniqueVoID+"_SR1SelectedCensusUser") != null){
            	SelectedCensusUserArray = (long[])session.getAttribute(UniqueVoID+"_SR1SelectedCensusUser");           	
            }
            while(rs.next()){
            	IsEmptyResultSet = false;
            	if (session.getAttribute(UniqueVoID+"_SR1SelectedCensusUser") != null){
            		for(int i=0;i<SelectedCensusUserArray.length;i++)
                	{            		
              			if(rs.getLong("created_by")==SelectedCensusUserArray[i])
              			{
              				IsCensusUserSelected = true;            			
              			}            		
                	}
            	}
            	
            	
            	%>    		
           			<input type="checkbox" name="CensusUserCheckbox" id="CensusUser_<%=rs.getString("created_by") %>" value="<%=rs.getString("created_by") %>" <%if(IsCensusUserSelected){ %>checked<%} %> data-mini="true">
   					<label for="CensusUser_<%=rs.getString("created_by") %>" ><span style="font-size:9pt; font-weight:normal;"><%=rs.getString("created_by") %> <%if(rs.getString("user_name")!=null){ %>- <%=rs.getString("user_name") %><%}else{ %>Unrecognized<%} %></span></label> 
            		
				
            	<%
            	IsCensusUserSelected=false;
            }
            
            
           
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	