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
			<input type="button" name="Apply" id="Apply" data-theme="a" value="DistributorType" style="text-align: left" data-mini="true">
			
           <%
           boolean CheckBoxSelected=false; 
           String Query="";
           String OrderBookerIds="";
           //String DistributorIds="";
           long SelectedOrderBookerArray[] = null;
           long SelectedDistributorArray[] = null;
           boolean IsEmptyResultSet = true;
           boolean IsEmptyDistributor = true;
           
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
           
              
          	
           long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
           int FeatureID =0;
           if (session.getAttribute("SR1FeatureID") != null)
           {
           	FeatureID = (Integer)session.getAttribute("SR1FeatureID");
           }
           Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
           
           String DistributorIds = UserAccess.getDistributorQueryString(UserDistributor);
	       
           
           //System.out.println(Query);
           
//            ResultSet rs = s.executeQuery("SELECT distinct is_active as id,case when is_active=0 then 'Non Active' else 'Active' end as status FROM pep.testnasir_dist_type");
            int chkval[]={0,1};            
    		boolean IsDistributorTypeSelected=false;
    		boolean IsDistributorTypeSelected1=false;
            long SelectedDistributorTypeArray[] = null;
            if (session.getAttribute(UniqueVoID+"_SR1SelectedDistributorType") != null){
            	SelectedDistributorTypeArray = (long[])session.getAttribute(UniqueVoID+"_SR1SelectedDistributorType");           	
            }
            int chkval2=0;
        	
//            while(rs.next()){
	           
	            	
	            chkval2++;
    //        	IsEmptyResultSet = false;
            	if (session.getAttribute(UniqueVoID+"_SR1SelectedDistributorType") != null){
            		for(int i=0;i<SelectedDistributorTypeArray.length;i++)
                	{            		
//              			if(rs.getLong("id")==SelectedDistributorTypeArray[i])
              				if(chkval[0]==SelectedDistributorTypeArray[i])
              			{
              				IsDistributorTypeSelected = true;            			
              			}            	
              				else	if(chkval[1]==SelectedDistributorTypeArray[i])
                  			{
                  				IsDistributorTypeSelected1 = true;            			
                  			}            	
                	}
            	}
            	
            	
            	%>
            	    		
           		

				    <input type="checkbox" value="0" id="DistributorType_0" name="DistributorTypeCheckbox" <%if(IsDistributorTypeSelected){ %>checked<%} %>  data-mini="true"> 
				    <label for="DistributorType_0" ><span style="font-size:9pt; font-weight:normal;">Active</span></label>             		
            		<input type="checkbox" value="1" id="DistributorType_1" <%if(IsDistributorTypeSelected1){ %>checked<%} %> name="DistributorTypeCheckbox" data-mini="true">
				    <label for="DistributorType_1" ><span style="font-size:9pt; font-weight:normal;">In Active</span></label> 

            	<%
            	IsDistributorTypeSelected=false;
            	IsDistributorTypeSelected1=false;
           
            
            
           
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	