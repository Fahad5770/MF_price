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
<%@page import="com.pbc.common.Region"%>
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
           
           <input type="hidden" name="FilterType" id="FilterType" value="1" >
           
        <fieldset data-role="controlgroup" data-corners="true" style="margin-left: 0px; margin-top: -10px; padding-top: 0px;">
			<input type="button" name="Apply" id="Apply" data-theme="a" value="Region" style="text-align: left" data-mini="true">
				
           <%
           
            long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
            int FeatureID = 27;
            
            long SelectedRegions[] = null;
            if (session.getAttribute("MonthlyDiscountRegions") != null){
            	SelectedRegions = (long[])session.getAttribute("MonthlyDiscountRegions"); 
            }
            
            Region UserRegion[] = UserAccess.getUserFeatureRegion(SessionUserID, FeatureID);
            
            String RegionIds = UserAccess.getRegionQueryString(UserRegion);
            
            String where = "";
            if(UserRegion != null && UserRegion.length > 0){
            	where = " where region_id in ( "+RegionIds+" ) ";
            }
            
            
           	ResultSet rs2 = s.executeQuery("SELECT * FROM common_regions"+where);
           	boolean CheckBoxSelected=false; 
           	
           	while(rs2.next()){
           		
           		if (session.getAttribute("MonthlyDiscountRegions") != null){
            		for(int i=0;i<SelectedRegions.length;i++)
                	{            		
              			if(rs2.getLong("region_id") == SelectedRegions[i])
              			{
              				CheckBoxSelected = true;            			
              			}
                	}
            	}
           		
           		       		
           		%> 
          			<input type="checkbox" name="RegionCheckBox" id="RegionID_<%=rs2.getLong("region_id") %>" value="<%=rs2.getLong("region_id") %>" <%if(CheckBoxSelected){ %>checked<%} %> data-mini="true">
  					<label for="RegionID_<%=rs2.getLong("region_id") %>" ><span style="font-size:9pt; font-weight:normal;"><%=rs2.getString("region_short_name") %> - <%=rs2.getString("region_name") %> </span></label> 
           		
           		<%
           		CheckBoxSelected=false; 
           		
           	}
            
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           
            
            
        	