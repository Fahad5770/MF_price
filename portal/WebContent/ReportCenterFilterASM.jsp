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
			<input type="button" name="Apply" id="Apply" data-theme="a" value="TSO" style="text-align: left" data-mini="true">
			<input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
           
           <%
           boolean CheckBoxSelected=false; 
           String Query="";
           String OrderBookerIds="";
           String DistributorIds="";
           long SelectedOrderBookerArray[] = null;
           long SelectedDistributorArray[] = null;
           boolean IsEmptyResultSet = true;
           boolean IsEmptyDistributor = true;
           
           
         
           if (session.getAttribute(UniqueVoID+"_SR1SelectedDistributors") != null){
        	   SelectedDistributorArray = (long[])session.getAttribute(UniqueVoID+"_SR1SelectedDistributors"); 
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
	       
           
           //System.out.println(Query);
           
            ResultSet rs = s.executeQuery("SELECT distinct dbpav.asm_id,display_name FROM distributor_beat_plan_all_view dbpav, users u  where dbpav.asm_id=u.id and  dbpav.distributor_id in ("+DistributorIds+")");
            
            boolean IsASMSelected=false;
            long SelectedASMArray[] = null;
            if (session.getAttribute(UniqueVoID+"_SR1SelectedASM") != null){
            	SelectedASMArray = (long[])session.getAttribute(UniqueVoID+"_SR1SelectedASM");           	
            }
            while(rs.next()){
            	IsEmptyResultSet = false;
            	if (session.getAttribute(UniqueVoID+"_SR1SelectedASM") != null){
            		for(int i=0;i<SelectedASMArray.length;i++)
                	{            		
              			if(rs.getLong("asm_id")==SelectedASMArray[i])
              			{
              				IsASMSelected = true;            			
              			}            		
                	}
            	}
            	
            	
            	%>    		
           			<input type="checkbox" name="ASMCheckbox" id="ASM_<%=rs.getString("asm_id") %>" value="<%=rs.getString("asm_id") %>" <%if(IsASMSelected){ %>checked<%} %> data-mini="true">
   					<label for="ASM_<%=rs.getString("asm_id") %>" ><span style="font-size:9pt; font-weight:normal;"><%=rs.getString("asm_id") %> - <%=rs.getString("display_name") %></span></label> 
            		
				
            	<%
            	IsASMSelected=false;
            }
            
            
           
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	