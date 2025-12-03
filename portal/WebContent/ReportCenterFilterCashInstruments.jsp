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
			<input type="button" name="Apply" id="Apply" data-theme="a" value="Cash Instruments" style="text-align: left" data-mini="true">
			
           <%
           boolean CheckBoxSelected=false; 
           String Query="";
           String OrderBookerIds="";
           String DistributorIds="";
           long SelectedOrderBookerArray[] = null;
           long SelectedDistributorArray[] = null;
           boolean IsEmptyResultSet = true;
           boolean IsEmptyDistributor = true;
           
           
           String WarehouseIDs="";
           long SelectedWarehouseArray[] = null;
           if (session.getAttribute(UniqueVoID+"_SR1SelectedWarehouse") != null){
           	SelectedWarehouseArray = (long[])session.getAttribute(UniqueVoID+"_SR1SelectedWarehouse");
           	WarehouseIDs = Utilities.serializeForSQL(SelectedWarehouseArray);
           }
           String WhereWarehouse = "";
           if (WarehouseIDs.length() > 0){
           	WhereWarehouse = " and warehouse_id in ("+WarehouseIDs+") ";	
           }

	       //if multiple warehouse selected then show msg plz select only 1 warehouse
           if(SelectedWarehouseArray != null){
		       if(SelectedWarehouseArray.length == 1){ //only one warehouse selected
		       
		      // System.out.println("SELECT * from gl_accounts where  "+WhereWarehouse);
		            ResultSet rs = s.executeQuery("SELECT * from gl_accounts where 1=1 "+WhereWarehouse); 
		            
		            
		            boolean IsCashInsturmentsSelected=false;
		            long SelectedCashInsturmentsArray[] = null;
		            if (session.getAttribute(UniqueVoID+"_SR1SelectedCashInstruments") != null){
		            	SelectedCashInsturmentsArray = (long[])session.getAttribute(UniqueVoID+"_SR1SelectedCashInstruments");           	
		            }
		            while(rs.next()){
		            	IsEmptyResultSet = false;
		            	if (session.getAttribute(UniqueVoID+"_SR1SelectedCashInstruments") != null){
		            		for(int i=0;i<SelectedCashInsturmentsArray.length;i++)
		                	{            		
		              			if(rs.getLong("id")==SelectedCashInsturmentsArray[i])
		              			{
		              				IsCashInsturmentsSelected = true;            			
		              			}            		
		                	}
		            	}
		            	
		            	
		      	    	   
		      	       
		            	%>  
		            	  		
		           			<input type="radio" name="CashInstrumentsRadio" id="CashInstruments_<%=rs.getString("id") %>" value="<%=rs.getString("id") %>" <%if(IsCashInsturmentsSelected){ %>checked<%} %> data-mini="true">
		   					<label for="CashInstruments_<%=rs.getString("id") %>" ><span style="font-size:9pt; font-weight:normal;"><%=rs.getString("label") %></span></label> 
		            		
						
		            	
		            		 
		            		 <%
		            	 
		            		 IsCashInsturmentsSelected=false;
		            }
		       }else{%>
		       <p></p>
	      		 <label style="font-weight:normal; font-size:10pt; padding:5px;">Please select only one warehouse</label>
	      		 <p></p>
	      	 <%}
           }
           else
           {%>
           <p></p>
        	   <label style="font-weight:normal; font-size:10pt; padding:5px;">Please select one warehouse</label>
        	   <p></p>
          <% }
            
           
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	