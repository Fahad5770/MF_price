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
            long UniqueVoID = Utilities.parseLong(request.getParameter("UniqueSessionID"));
            
            Datasource ds = new Datasource();
            ds.createConnection();
            Statement s = ds.createStatement();        
           %>
           
           
           
        <fieldset data-role="controlgroup" data-corners="true" style="margin-left: 0px; margin-top: -10px; padding-top: 0px;">
			<input type="button" name="Apply" id="Apply" data-theme="a" value="Distributors" style="text-align: left" data-mini="true">
			<input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">	
           <%
           boolean CheckBoxSelected=false;           
           long SelectedPackagesArray[] = null;
           if (session.getAttribute(UniqueVoID+"_SR1SelectedDistributors") != null){
           	SelectedPackagesArray = (long[])session.getAttribute(UniqueVoID+"_SR1SelectedDistributors");           	
           }
            long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
            int FeatureID =0;
            if (session.getAttribute("SR1FeatureID") != null)
            {
            	FeatureID = (Integer)session.getAttribute("SR1FeatureID");
            }
            Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
            
            String DistributorIds = UserAccess.getDistributorQueryString(UserDistributor);
            
            
            String OrderBookerIds="";
            long SelectedOrderBookerArray[] = null;
            if (session.getAttribute(UniqueVoID+"_SR1SelectedOrderBookers") != null){
            	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueVoID+"_SR1SelectedOrderBookers");  
            	OrderBookerIds = Utilities.serializeForSQL(SelectedOrderBookerArray);
            }
            
            if (OrderBookerIds.length() == 0){
            
           //for(int x=0;x<UserDistributor.length;x++){
        	   ResultSet rs2 = s.executeQuery("select distributor_id, name from common_distributors where distributor_id in (SELECT distinct distributor_id FROM inventory_delivery_note) and is_active=1 and distributor_id in ("+DistributorIds+")");
            	
            	while(rs2.next()){
            	
            		long DistributorID = rs2.getLong(1);
            		String DistributorName = rs2.getString(2);
        	   if (session.getAttribute(UniqueVoID+"_SR1SelectedDistributors") != null){
            		for(int i=0;i<SelectedPackagesArray.length;i++)
                	{            		
              			if(DistributorID==SelectedPackagesArray[i])
              			{
              				CheckBoxSelected = true;            			
              			}            		
                	}
            	}
            	
            	%>    		
           			<input type="checkbox" name="DistributorCheckBox" id="DistributorID_<%=DistributorID %>" value="<%=DistributorID %>" <%if(CheckBoxSelected){ %>checked<%} %> onClick="ChangeLabelDistributor('<%=ClickIDD%>')" data-mini="true">
   					<label for="DistributorID_<%=DistributorID %>" ><span style="font-size:9pt; font-weight:normal;"><%=DistributorID %> - <%=DistributorName %></span></label> 
            		
				
            	<%
            	CheckBoxSelected=false;
            }
           
            }else{
            	
            	ResultSet rs2 = s.executeQuery("select distributor_id, name from common_distributors where distributor_id in("+
            			 "select distinct dbpv.distributor_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to in ("+OrderBookerIds+")"+
            			") and distributor_id in ("+DistributorIds+")");
            	
            	while(rs2.next()){
            		
             	   if (session.getAttribute(UniqueVoID+"_SR1SelectedDistributors") != null){
               		for(int i=0;i<SelectedPackagesArray.length;i++)
                   	{            		
                 			if(rs2.getLong(1)==SelectedPackagesArray[i])
                 			{
                 				CheckBoxSelected = true;            			
                 			}            		
                   	}
               	}            		
            		%>
           			<input type="checkbox" name="DistributorCheckBox" id="DistributorID_<%=rs2.getLong(1) %>" value="<%=rs2.getLong(1) %>" <%if(CheckBoxSelected){ %>checked<%} %> onClick="ChangeLabelDistributor('<%=ClickIDD%>')" data-mini="true">
   					<label for="DistributorID_<%=rs2.getLong(1) %>" ><span style="font-size:9pt; font-weight:normal;"><%=rs2.getLong(1) %> - <%=rs2.getString(2) %></span></label> 
            		
            		<%
            		CheckBoxSelected=false;
            		
            	}
            }
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           
            
            
        	