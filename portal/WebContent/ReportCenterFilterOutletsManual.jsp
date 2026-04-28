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
            long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));
            
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
           if (session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers") != null && session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	           	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers");
	           	OrderBookerIds = Utilities.serializeForSQL(SelectedOrderBookerArray);
	           
	           	SelectedDistributorArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");
	        	DistributorIds = Utilities.serializeForSQL(SelectedDistributorArray); 
	           	
	           	Query = "select id, name from common_outlets where id in (SELECT outlet_id FROM pep.common_outlets_distributors_view where distributor_id in ("+DistributorIds+") and id in(select distinct dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to  in ("+OrderBookerIds+")))";
	         
	           }
           
           else if(session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
        	   SelectedDistributorArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");
        	   DistributorIds = Utilities.serializeForSQL(SelectedDistributorArray); 
        	   
        	   Query = "select id, name from common_outlets where id in (SELECT outlet_id FROM pep.common_outlets_distributors_view where distributor_id in ("+DistributorIds+"))";
               		
           }    //checking for orderbooker      
          	else if (session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers") != null){
	           	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers");
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
            if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets") != null){
            	SelectedOutletArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets");           	
            }
            String OutletIdsManual="";
            String SelectedOutletNameManual="";
            long SelectedOutletArrayManual[] = null;
            if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets") != null){
            	IsOutletSelected=true;
            	SelectedOutletArrayManual = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets");
            	OutletIdsManual = Utilities.serializeForSQL(SelectedOutletArrayManual);
            }
            if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutletsNameManual") != null){
            	
            	SelectedOutletNameManual = session.getAttribute(UniqueSessionID+"_SR1SelectedOutletsNameManual").toString();            	
            }
            	
            	
            	%>
            	
            	<%
				   int FeatureIDSession =0;
				   if (session.getAttribute("SR1FeatureID") != null)
				   {
					  FeatureIDSession = (Integer)session.getAttribute("SR1FeatureID");
				   	//System.out.println("heyyyy "+FeatureID);
				   }
				   
				   %>    		
           			<input type="text" name="OutletIDManual" id="OutletIDManual" placeholder="Outlet ID" value="<%=OutletIdsManual%>" data-mini="true" onChange="getOutletName(); "  maxlength="10">
           			<input type="text" name="OutletNameManual" id="OutletNameManual" placeholder="Outlet Name" value="<%=SelectedOutletNameManual %>" readonly data-mini="true"> 
           			<input type="hidden" name="FeatureIDForOutletManual" id="FeatureIDForOutletManual"  value="<%=FeatureIDSession %>" >
				
            	<%
            	
           
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	