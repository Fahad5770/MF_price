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
			<input type="button" name="Apply" id="Apply" data-theme="a" value="Cash User" style="text-align: left" data-mini="true">
			
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
           
            ResultSet rs = s.executeQuery("select distinct(created_by),(select display_name from users u where u.id=glcr.created_by) display_name from gl_cash_receipts glcr"); 
            
            
            boolean IsGlEmployeeSelected=false;
            long SelectedGlEmployeeArray[] = null;
            if (session.getAttribute(UniqueVoID+"_SR1SelectedGlEmployee") != null){
            	SelectedGlEmployeeArray = (long[])session.getAttribute(UniqueVoID+"_SR1SelectedGlEmployee");           	
            }
            while(rs.next()){
            	IsEmptyResultSet = false;
            	if (session.getAttribute(UniqueVoID+"_SR1SelectedGlEmployee") != null){
            		for(int i=0;i<SelectedGlEmployeeArray.length;i++)
                	{            		
              			if(rs.getLong("created_by")==SelectedGlEmployeeArray[i])
              			{
              				IsGlEmployeeSelected = true;            			
              			}            		
                	}
            	}
            	
            	
            	%>    		
           			<input type="radio" name="GlEmployeeCheckbox" id="GlEmployee_<%=rs.getString("created_by") %>" value="<%=rs.getString("created_by") %>" <%if(IsGlEmployeeSelected){ %>checked<%} %> data-mini="true">
   					<label for="GlEmployee_<%=rs.getString("created_by") %>" ><span style="font-size:9pt; font-weight:normal;"><%=rs.getString("created_by") %> - <%=rs.getString("display_name") %></span></label> 
            		
				
            	<%
            	IsGlEmployeeSelected=false;
            }
            
            
           
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	