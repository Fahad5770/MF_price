<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="org.apache.commons.lang3.text.WordUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>


<script>

$(".tr_class_product").not('first').hover(
  function () {
    $(this).css("background","#ececec");
	$(this).css("cursor","pointer");
  }, 
  function () {
    $(this).css("background","");
  }
);

</script>
	
    <table border=0 style="width:50%">
          
            <tr>
                <th data-priority="1" nowrap="nowrap" style="text-align:left">SAP Code</th>
                <th data-priority="1" nowrap="nowrap" style="text-align:left">Package</th>
                <th data-priority="1" nowrap="nowrap" style="text-align:left">Brand</th>
            </tr>
          
            <%
            

			Datasource ds = new Datasource();
			ds.createConnection();
			Connection c = ds.getConnection();
			Statement s = c.createStatement();
            
			int PackageID = Utilities.parseInt(request.getParameter("ProductSearchFormPackage"));
			int BrandID = Utilities.parseInt(request.getParameter("ProductSearchFormBrand"));
			
			String where = ""; 
            
            if(PackageID > 0){
            	where += " and package_id = "+PackageID;
            }
            if(BrandID > 0){
            	where += " and brand_id = "+BrandID;
            }
            
            ResultSet rs = s.executeQuery("SELECT package_id, brand_id, (select label from inventory_packages where id=package_id) package_label, (select label from inventory_brands where id=brand_id) brand_label, sap_code FROM inventory_products where category_id=1 "+where+" order by package_label, brand_label");
            boolean IsFound = false;
            while(rs.next()){
            	IsFound = true;
            	%>
                <tr class="tr_class_product" onclick="lookupProductOnSelect(<%=rs.getString("sap_code")%>, '<%=rs.getString("package_label")+" - "+rs.getString("brand_label")%>')">            
	                <td style="font-weight: 100;"><%=rs.getString("sap_code")%></td>
                	<td style="font-weight: 100;" nowrap="nowrap"><%=rs.getString("package_label")%></td>
                    <td style="font-weight: 100;" nowrap="nowrap"><%=rs.getString("brand_label")%></td>                    
                </tr>
            	<%
            	
            }
            
			
			if( IsFound == false ){
				%>
                <tr>
                	<td colspan="4" style="text-align:center">No record found.</td>
                </tr>
            	<%
			}
            
            s.close();
            c.close();
            ds.dropConnection();
            %>
        	</table>
            