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
			<input type="button" name="Apply" id="Apply" data-theme="a" value="TDM" style="text-align: left" data-mini="true">
			
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
           
           
           String RSMIDs="";
           long SelectedRSMArray[] = null;
           if (session.getAttribute(UniqueVoID+"_SR1SelectedRSM") != null){
           	SelectedRSMArray = (long[])session.getAttribute(UniqueVoID+"_SR1SelectedRSM");
           	RSMIDs = Utilities.serializeForSQL(SelectedRSMArray);
           }
           
           String WhereRSM = "";
           if (RSMIDs.length() > 0){
        	   WhereRSM = " and cd.rsm_id in ("+RSMIDs+") ";
           }
	       
           String HODIDs="";
           long SelectedHODArray[] = null;
           if (session.getAttribute(UniqueVoID+"_SR1SelectedHOD") != null){
           	SelectedHODArray = (long[])session.getAttribute(UniqueVoID+"_SR1SelectedHOD");
           	HODIDs = Utilities.serializeForSQL(SelectedHODArray);
           }

           String WhereHOD = "";
           if (HODIDs.length() > 0){
           	WhereHOD = " and cd.snd_id in ("+HODIDs+") ";	
           }
           
           //System.out.println(Query);
           
            ResultSet rs = s.executeQuery("SELECT distinct cd.tdm_id,display_name FROM common_distributors cd, users u where cd.tdm_id=u.id and tdm_id is not null "+WhereRSM+WhereHOD);
            
            boolean IsTDMSelected=false;
            long SelectedTDMArray[] = null;
            if (session.getAttribute(UniqueVoID+"_SR1SelectedTDM") != null){
            	SelectedTDMArray = (long[])session.getAttribute(UniqueVoID+"_SR1SelectedTDM");           	
            }
            while(rs.next()){
            	IsEmptyResultSet = false;
            	if (session.getAttribute(UniqueVoID+"_SR1SelectedTDM") != null){
            		for(int i=0;i<SelectedTDMArray.length;i++)
                	{            		
              			if(rs.getLong("tdm_id")==SelectedTDMArray[i])
              			{
              				IsTDMSelected = true;            			
              			}            		
                	}
            	}
            	
            	
            	%>    		
           			<input type="checkbox" name="TDMCheckbox" id="TDM_<%=rs.getString("tdm_id") %>" value="<%=rs.getString("tdm_id") %>" <%if(IsTDMSelected){ %>checked<%} %> data-mini="true">
   					<label for="TDM_<%=rs.getString("tdm_id") %>" ><span style="font-size:9pt; font-weight:normal;"><%=rs.getString("tdm_id") %> - <%=rs.getString("display_name") %></span></label> 
            		
				
            	<%
            	IsTDMSelected=false;
            }
            
            
           
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	