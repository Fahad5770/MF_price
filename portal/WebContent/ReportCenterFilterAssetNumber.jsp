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
			<input type="button" name="Apply" id="Apply" data-theme="a" value="Asset Number" style="text-align: left" data-mini="true">
			
           <%
           boolean CheckBoxSelected=false; 
           String Query="";
           String OrderBookerIds="";
           String DistributorIds="";
           long SelectedOrderBookerArray[] = null;
           long SelectedDistributorArray[] = null;
           boolean IsEmptyResultSet = true;
           
           
           long AssetNumber=0;
           boolean IsAssetNumberSelected=false;

           if (session.getAttribute(UniqueSessionID+"_SR1SelectedAssetNumber") != null){
        	   AssetNumber = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedAssetNumber");	
           	
           	if(AssetNumber != 0)
           	{
           		IsAssetNumberSelected=true;		
           	}
           }
            
            	
            	%>
            	          	
				  		
           			<input type="text" name="AssetNumberID" id="AssetNumberID" placeholder="Asset Number" value="<%if(AssetNumber !=0){ %><%=AssetNumber %><%} %>" data-mini="true"  maxlength="20">
           			
				
            	<%
            	
           
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	