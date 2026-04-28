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
            String ClickIDD = Utilities.filterString(request.getParameter("ClickID"),1,100);
            
            
            Datasource ds = new Datasource();
            ds.createConnection();
            Statement s = ds.createStatement();        
           %>
           
           
           
         
			<fieldset data-role="controlgroup" data-corners="true" style="margin-left: 0px; margin-top: -10px; padding-top: 0px;">
			<input type="button" name="Apply" id="Apply" data-theme="a" value="Order Bookers" style="text-align: left" data-mini="true">
			
			
			<!--  <legend style="font-weight:bold; font-size:10pt;">All Packages</legend>-->
           <%
           boolean CheckBoxSelected=false; 
           String DistributorIds="";
           long SelectedDistributorArray[] = null;
           if (session.getAttribute("SR1SelectedDistributors") != null){
        	   SelectedDistributorArray = (long[])session.getAttribute("SR1SelectedDistributors"); 
        	   DistributorIds = Utilities.serializeForSQL(SelectedDistributorArray);
        	   //System.out.println("I am in if");
           }
           else
           {
        	   long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
        	   int FeatureID =0;
               if (session.getAttribute("SR1FeatureID") != null)
               {
               	FeatureID = (Integer)session.getAttribute("SR1FeatureID");
               }
        	   Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
        	   DistributorIds = UserAccess.getDistributorQueryString(UserDistributor);
        	   //System.out.println("I am in else");
           }
           //System.out.println(DistributorIds);
            ResultSet rs = s.executeQuery("select * from employee_view where sap_code in (select distinct dbpv.assigned_to from distributor_beat_plan_view dbpv  where dbpv.distributor_id in ("+DistributorIds+"))");
            boolean IsOrderBookerSelected=false;
            long SelectedOrderBookerArray[] = null;
            if (session.getAttribute("SR1SelectedOrderBookers") != null){
            	SelectedOrderBookerArray = (long[])session.getAttribute("SR1SelectedOrderBookers");           	
            }
            while(rs.next()){
            	
            	if (session.getAttribute("SR1SelectedOrderBookers") != null){
            		for(int i=0;i<SelectedOrderBookerArray.length;i++)
                	{            		
              			if(rs.getLong("sap_code")==SelectedOrderBookerArray[i])
              			{
              				IsOrderBookerSelected = true;            			
              			}            		
                	}
            	}
            	
            	%>    		
           			<input type="checkbox" name="OrderBookerCheckBox" id="OrderBookerID_<%=rs.getString("sap_code") %>" value="<%=rs.getString("sap_code") %>" <%if(IsOrderBookerSelected){ %>checked<%} %> data-mini="true">
   					<label for="OrderBookerID_<%=rs.getString("sap_code") %>" ><span style="font-size:9pt; font-weight:normal;"><%=rs.getString("sap_code")+" - "+rs.getString("first_name")+" "+rs.getString("last_name") %></span></label> 
            		
				
            	<%
            	IsOrderBookerSelected=false;
            }
            s.close();            
            ds.dropConnection();
            %>
            <!-- 
           			<input type="checkbox" name="CheckBoxSelectAll" id="OrderBookerIDAll" value="Select All" onClick="SelectAllCheckboxes('OrderBookerCheckBox');" data-mini="true">
   					<label for="OrderBookerIDAll" ><span style="font-size:9pt; font-weight:normal;">Select All</span></label> 
             -->
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            