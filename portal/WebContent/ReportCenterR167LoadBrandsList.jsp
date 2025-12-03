<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>


        	<ul data-role="listview" data-inset="true"> 
            <%
			
            Datasource ds = new Datasource();
			ds.createConnection();
			Connection c = ds.getConnection();
			Statement s = c.createStatement();
            
            int DocumentID = Utilities.parseInt(request.getParameter("DocumentID"));
            int PackageID = Utilities.parseInt(request.getParameter("PackageID"));
            int TypeID = Utilities.parseInt(request.getParameter("TypeID"));
            
            ResultSet rs = s.executeQuery("SELECT brand_id, (select brand_label from inventory_products_view where brand_id=inventory_sales_promotions_products_brands.brand_id limit 1) brand_label FROM inventory_sales_promotions_products_brands where id="+DocumentID+" and package_id="+PackageID+" and type_id="+TypeID);
            while(rs.next()){
            	%>
            	
            	<li>
					<span style="font-size: 10pt; font-weight: 400;"><%=rs.getString("brand_label")%></span>
				</li>
				
            	<%
            }
            
            s.close();
            c.close();
            ds.dropConnection();
            
            %>
        	</ul>