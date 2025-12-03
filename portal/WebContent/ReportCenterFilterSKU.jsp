<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>

<style>
.ui-btn {
text-align: left
}
</style>

<script>

</script>

        	
        	
            <%
            
            long UniqueVoID = Utilities.parseLong(request.getParameter("UniqueSessionID"));
            
            String ClickedTitle = Utilities.filterString(request.getParameter("Title"),1, 500);
            String ClickIDD = Utilities.filterString(request.getParameter("ClickID"),1,100);
            
            
            Datasource ds = new Datasource();
            ds.createConnection();
            Statement s = ds.createStatement();        
           %>
           
           
           
         
<fieldset data-role="controlgroup" data-corners="true" style="margin-left: 0px; margin-top: -10px; padding-top: 0px;">
			<input type="button" name="Apply" id="Apply" data-theme="a" value="SKUs" style="text-align: left" data-mini="true">
			<input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
			
			<%
			
			long SelectedBrandsArray[] = null;
			if (session.getAttribute(UniqueVoID+"_SR1SelectedBrands") != null){
			   	SelectedBrandsArray = (long[])session.getAttribute(UniqueVoID+"_SR1SelectedBrands");           	
			}






			String BrandIDs = "";
			String WhereBrand = "";

			if(SelectedBrandsArray!= null && SelectedBrandsArray.length > 0){
				for(int i = 0; i < SelectedBrandsArray.length; i++){
					if(i == 0){
				BrandIDs += SelectedBrandsArray[i]+"";
					}else{
				BrandIDs += ", "+SelectedBrandsArray[i]+"";
					}
				}
				WhereBrand = " where lrb_type_id in ("+BrandIDs+") ";
			}
			
			%>
			
			
			<!--  <legend style="font-weight:bold; font-size:10pt;">All Packages</legend>-->
           <%
           boolean CheckBoxSelected=false;           
           long SelectedSKUArray[] = null;
           if (session.getAttribute(UniqueVoID+"_SR1SelectedSKU") != null){
        	   SelectedSKUArray = (long[])session.getAttribute(UniqueVoID+"_SR1SelectedSKU");           	
           }
           
           System.out.println("select product_id, concat(brand_label,' ',package_label) as product from inventory_products_view "+WhereBrand);
            ResultSet rs = s.executeQuery("select product_id, concat(brand_label,' ',package_label) as product from inventory_products_view "+WhereBrand);
            
            while(rs.next()){
            	if (session.getAttribute(UniqueVoID+"_SR1SelectedSKU") != null){
            		for(int i=0;i<SelectedSKUArray.length;i++)
                	{            		
              			if(rs.getLong("product_id")==SelectedSKUArray[i])
              			{
              				CheckBoxSelected = true;            			
              			}            		
                	}
            	}
            	
            	%>    	
            		<input type="checkbox" name="SKUCheckBox" id="SKUID_<%=rs.getString("product_id") %>" value="<%=rs.getString("product_id") %>" <%if(CheckBoxSelected){ %>checked<%} %> onClick="ChangeLabelSKU('<%=ClickIDD%>')" data-mini="true">
   					<label for="SKUID_<%=rs.getString("product_id") %>" ><span style="font-size:9pt; font-weight:normal;"><%=rs.getString("product") %></span></label> 
				
            	<%
            	CheckBoxSelected=false;
            }
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	