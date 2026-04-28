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
			<input type="button" name="Apply" id="Apply" data-theme="a" value="Empty Transaction Types" style="text-align: left" data-mini="true">
			
           <%
           boolean CheckBoxSelected=false; 
           String Query="";
           String OrderBookerIds="";
           String DistributorIds="";
           long SelectedOrderBookerArray[] = null;
           long SelectedDistributorArray[] = null;
           boolean IsEmptyResultSet = true;
           boolean IsEmptyDistributor = true;
           
           
              
          	
	       
           
           //System.out.println(Query);
           
            ResultSet rs = s.executeQuery("SELECT distinct reason,(select label from "+ds.logDatabaseName()+".bi_empty_transaction_reason bietr where bietr.id=reason) label FROM "+ds.logDatabaseName()+".bi_empty_other_issuance"); 
            
            
            boolean IsEmptyReasonSelected=false;
            String SelectedEmptyReasonArray[] = null;
            if (session.getAttribute(UniqueVoID+"_SR1SelectedEmptyReason") != null){
            	SelectedEmptyReasonArray = (String[])session.getAttribute(UniqueVoID+"_SR1SelectedEmptyReason");           	
            }
            while(rs.next()){
            	IsEmptyResultSet = false;
            	if (session.getAttribute(UniqueVoID+"_SR1SelectedEmptyReason") != null){
            		for(int i=0;i<SelectedEmptyReasonArray.length;i++)
                	{            		
              			if(rs.getString("reason").equals(SelectedEmptyReasonArray[i]))
              			{
              				IsEmptyReasonSelected = true;            			
              			}            		
                	}
            	}
            	
            	
            	%>    		
           			<input type="checkbox" name="EmptyReasonCheckbox" id="EmptyReason_<%=rs.getString("reason") %>" value="<%=rs.getString("reason") %>" <%if(IsEmptyReasonSelected){ %>checked<%} %> data-mini="true">
   					<label for="EmptyReason_<%=rs.getString("reason") %>" ><span style="font-size:9pt; font-weight:normal;"><%=rs.getString("reason") %> - <%=rs.getString("label") %></span></label> 
            		
				
            	<%
            	IsEmptyReasonSelected=false;
            }
            
            
           
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	