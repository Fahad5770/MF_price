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
            
            
            
            Datasource ds = new Datasource();
            ds.createConnection();
            Statement s = ds.createStatement();        
           %>
           
           <input type="hidden" name="FilterType" id="FilterType" value="2" >
           
        <fieldset data-role="controlgroup" data-corners="true" style="margin-left: 0px; margin-top: -10px; padding-top: 0px;">
			<input type="button" name="Apply" id="Apply" data-theme="a" value="Distributors" style="text-align: left" data-mini="true">
				
           <%
           
            long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
            int FeatureID = 27;
            
            long SelectedDistributor[] = null;
            if (session.getAttribute("MonthlyDiscountDistributors") != null){
            	SelectedDistributor = (long[])session.getAttribute("MonthlyDiscountDistributors"); 
            }
            
            Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
            
            String DistributorIds = UserAccess.getDistributorQueryString(UserDistributor);
            
            String where = "";
            if(UserDistributor != null && UserDistributor.length > 0){
            	where = " where distributor_id in ( "+DistributorIds+" ) ";
            }
            
           	ResultSet rs2 = s.executeQuery("SELECT distributor_id, name FROM common_distributors "+where);
           	boolean CheckBoxSelected=false; 
           	while(rs2.next()){
           		
           		if (session.getAttribute("MonthlyDiscountDistributors") != null){
           			
            		for(int i=0;i<SelectedDistributor.length;i++)
                	{            		
              			if(rs2.getLong("distributor_id") == SelectedDistributor[i])
              			{
              				CheckBoxSelected = true;            			
              			}
                	}
            	}
           		
           		       		
           		%>
          			<input type="checkbox" name="DistributorCheckBox" id="DistributorID_<%=rs2.getLong("distributor_id") %>" value="<%=rs2.getLong("distributor_id") %>" <%if(CheckBoxSelected){ %>checked<%} %>  data-mini="true">
  					<label for="DistributorID_<%=rs2.getLong("distributor_id") %>" ><span style="font-size:9pt; font-weight:normal;"><%=rs2.getLong("distributor_id") %> - <%=rs2.getString("name") %></span></label> 
           		
           		<%
           		CheckBoxSelected=false; 
           		
           	}
            
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           
            
            
        	