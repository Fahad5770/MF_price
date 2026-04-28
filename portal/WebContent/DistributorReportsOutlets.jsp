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
			<input type="button" name="Apply" id="Apply" data-theme="a" value="Outlets" style="text-align: left" data-mini="true">
			
           <%
           boolean CheckBoxSelected=false; 
           String Query="";
           String OrderBookerIds="";
           String DistributorIds="";
           long SelectedOrderBookerArray[] = null;
           long SelectedDistributorArray[] = null;
           boolean IsEmptyResultSet = true;
           
           //checking for distributor
           if (session.getAttribute("SR1SelectedOrderBookers") != null && session.getAttribute("SR1SelectedDistributors") != null){
	           	SelectedOrderBookerArray = (long[])session.getAttribute("SR1SelectedOrderBookers");
	           	OrderBookerIds = Utilities.serializeForSQL(SelectedOrderBookerArray);
	           
	           	SelectedDistributorArray = (long[])session.getAttribute("SR1SelectedDistributors");
	        	DistributorIds = Utilities.serializeForSQL(SelectedDistributorArray); 
	           	
	           	Query = "select id, name from common_outlets where id in (SELECT outlet_id FROM pep.common_outlets_distributors_view where distributor_id in ("+DistributorIds+") and id in(select distinct dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to  in ("+OrderBookerIds+")))";
	         
	           }
           
           else if(session.getAttribute("SR1SelectedDistributors") != null){
        	   SelectedDistributorArray = (long[])session.getAttribute("SR1SelectedDistributors");
        	   DistributorIds = Utilities.serializeForSQL(SelectedDistributorArray); 
        	   
        	   Query = "select id, name from common_outlets where id in (SELECT outlet_id FROM pep.common_outlets_distributors_view where distributor_id in ("+DistributorIds+"))";
               		
           }    //checking for orderbooker      
          	else if (session.getAttribute("SR1SelectedOrderBookers") != null){
	           	SelectedOrderBookerArray = (long[])session.getAttribute("SR1SelectedOrderBookers");
	           	OrderBookerIds = Utilities.serializeForSQL(SelectedOrderBookerArray);
	           
	           	Query = "select id, name from common_outlets where id in(select distinct dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to  in ("+OrderBookerIds+"))";
	           	
	           /*	Query = "select id, name from common_outlets where id in ("+
	            		"select distinct dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to  in ("+OrderBookerIds+")"+
	            		")";*/
	       }
	       else{
	    	   Query = "select id, name from common_outlets where id=-1";
	       }
           
           //System.out.println(Query);
           
            ResultSet rs = s.executeQuery(Query);
            
            boolean IsOutletSelected=false;
            long SelectedOutletArray[] = null;
            if (session.getAttribute("SR1SelectedOutlets") != null){
            	SelectedOutletArray = (long[])session.getAttribute("SR1SelectedOutlets");           	
            }
            while(rs.next()){
            	IsEmptyResultSet = false;
            	if (session.getAttribute("SR1SelectedOutlets") != null){
            		for(int i=0;i<SelectedOutletArray.length;i++)
                	{            		
              			if(rs.getLong("id")==SelectedOutletArray[i])
              			{
              				IsOutletSelected = true;            			
              			}            		
                	}
            	}
            	
            	
            	%>    		
           			<input type="checkbox" name="OutletCheckBox" id="OutletID_<%=rs.getString("id") %>" value="<%=rs.getString("id") %>" <%if(IsOutletSelected){ %>checked<%} %> data-mini="true">
   					<label for="OutletID_<%=rs.getString("id") %>" ><span style="font-size:9pt; font-weight:normal;"><%=rs.getString("id") %> - <%=rs.getString("name") %></span></label> 
            		
				
            	<%
            	IsOutletSelected=false;
            }
            if(IsEmptyResultSet){
            	%>
            	<table>
            	<tr>
            		<td style="font-size:10pt; padding:10px;">Please select at least one distributor or outlet.</td>
            	</tr>
            	
            	</table>
            	
            	
            <%
            }
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	