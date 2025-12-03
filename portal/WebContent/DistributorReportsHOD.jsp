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
			<input type="button" name="Apply" id="Apply" data-theme="a" value="HODs" style="text-align: left" data-mini="true">
			
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
           
            ResultSet rs = s.executeQuery("SELECT distinct cd.snd,display_name FROM common_distributors cd, users u  where cd.snd=u.id");
            
            boolean IsHODSelected=false;
            long SelectedHODArray[] = null;
            if (session.getAttribute("SR1SelectedHOD") != null){
            	SelectedHODArray = (long[])session.getAttribute("SR1SelectedHOD");           	
            }
            while(rs.next()){
            	IsEmptyResultSet = false;
            	if (session.getAttribute("SR1SelectedHOD") != null){
            		for(int i=0;i<SelectedHODArray.length;i++)
                	{            		
              			if(rs.getLong("snd")==SelectedHODArray[i])
              			{
              				IsHODSelected = true;            			
              			}            		
                	}
            	}
            	
            	
            	%>    		
           			<input type="checkbox" name="HODCheckbox" id="HOD_<%=rs.getString("snd") %>" value="<%=rs.getString("snd") %>" <%if(IsHODSelected){ %>checked<%} %> data-mini="true">
   					<label for="HOD_<%=rs.getString("snd") %>" ><span style="font-size:9pt; font-weight:normal;"><%=rs.getString("snd") %> - <%=rs.getString("display_name") %></span></label> 
            		
				
            	<%
            	IsHODSelected=false;
            }
            
            
           
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	