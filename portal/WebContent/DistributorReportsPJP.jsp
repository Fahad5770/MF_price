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
			<input type="button" name="Apply" id="Apply" data-theme="a" value="PJPs" style="text-align: left" data-mini="true">
			
           <%
           boolean CheckBoxSelected=false; 
           String Query="";
           String OrderBookerIds="";
           String DistributorIds="";
           long SelectedOrderBookerArray[] = null;
           long SelectedDistributorArray[] = null;
           boolean IsEmptyResultSet = true;
           boolean IsEmptyDistributor = true;
           
           
           
            if(session.getAttribute("SR1SelectedDistributors") != null){
        	   SelectedDistributorArray = (long[])session.getAttribute("SR1SelectedDistributors");
        	   DistributorIds = Utilities.serializeForSQL(SelectedDistributorArray); 
        	   
        	   Query = "SELECT distinct id, label FROM pep.distributor_beat_plan_view where distributor_id in ("+DistributorIds+")";
        	   IsEmptyDistributor = false;	
           }    //checking for orderbooker      
          	
	       else{
	    	   Query = "select id, name from common_outlets where id=-1";
	       }
           
           //System.out.println(Query);
           
            ResultSet rs = s.executeQuery(Query);
            
            boolean IsPJPSelected=false;
            long SelectedPJPArray[] = null;
            if (session.getAttribute("SR1SelectedPJP") != null){
            	SelectedPJPArray = (long[])session.getAttribute("SR1SelectedPJP");           	
            }
            while(rs.next()){
            	IsEmptyResultSet = false;
            	if (session.getAttribute("SR1SelectedPJP") != null){
            		for(int i=0;i<SelectedPJPArray.length;i++)
                	{            		
              			if(rs.getLong("id")==SelectedPJPArray[i])
              			{
              				IsPJPSelected = true;            			
              			}            		
                	}
            	}
            	
            	
            	%>    		
           			<input type="checkbox" name="PJPCheckbox" id="PJP_<%=rs.getString("id") %>" value="<%=rs.getString("id") %>" <%if(IsPJPSelected){ %>checked<%} %> data-mini="true">
   					<label for="PJP_<%=rs.getString("id") %>" ><span style="font-size:9pt; font-weight:normal;"><%=rs.getString("id") %> - <%=rs.getString("label") %></span></label> 
            		
				
            	<%
            	IsPJPSelected=false;
            }
            if(IsEmptyDistributor){
            	%>
            	<table>
            	<tr>
            		<td style="font-size:10pt; padding:10px;">Please Select at least one distributor</td>
            	</tr>
            	
            	</table>
            	
            	
            <%
            }
            
            else if(IsEmptyResultSet){
            	%>
            	<table>
            	<tr>
            		<td style="font-size:10pt; padding:10px;">No PJP Found</td>
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
            
            
        	