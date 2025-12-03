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

<script>

</script>

        	
        	
            <%
            String ClickIDD = Utilities.filterString(request.getParameter("ClickID"),1,100);
            
            
            Datasource ds = new Datasource();
            ds.createConnection();
            Statement s = ds.createStatement();        
           %>
           
           
           
         
			<fieldset data-role="controlgroup" >
			<input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true">
			
			
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
        	   Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, 67);
        	   DistributorIds = UserAccess.getDistributorQueryString(UserDistributor);
        	   //System.out.println("I am in else");
           }
           //System.out.println(DistributorIds);
            ResultSet rs = s.executeQuery("select * from employee_view where sap_code in (SELECT distinct assigned_to FROM employee_beat_plan ebp join employee_beat_plan_schedule ebps on ebp.beat_plan_id = ebps.beat_plan_id join outletmaster om on ebps.outlet_id = om.Outlet_ID where om.Customer_ID in ("+DistributorIds+"))");
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
           			<input type="checkbox" name="OrderBookerCheckBox" id="OrderBookerID_<%=rs.getString("sap_code") %>" value="<%=rs.getString("sap_code") %>" <%if(IsOrderBookerSelected){ %>checked<%} %> onClick="ChangeLabelOrderBooker('<%=ClickIDD%>','PackageID_<%=rs.getString("sap_code") %>')" data-mini="true">
   					<label for="OrderBookerID_<%=rs.getString("sap_code") %>" ><span style="font-size:10pt; font-weight:normal;"><%=rs.getString("sap_code")+" - "+rs.getString("first_name")+" "+rs.getString("last_name") %></span></label> 
            		
				
            	<%
            	IsOrderBookerSelected=false;
            }
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true">
            </fieldset>
           <!--</ul>-->
            
            
        	