<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>


        	
            <table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt; width:80%; margin-top:20px; margin-left:0px; margin-bottom:20px;">
	    		<thead>
				    <tr class="ui-bar-c">
						<th data-priority="1">Package</th>
						<th data-priority="1">Brand</th>
						<th data-priority="1">Product Code</th>
				    </tr>
				</thead>
        	
            <%
            

			Datasource ds = new Datasource();
			ds.createConnection();
			Connection c = ds.getConnection();
			Statement s = c.createStatement();
            
			int EmployeeProductGroupID = Utilities.parseInt(request.getParameter("EmployeeProductGroupID"));
			
            ResultSet rs = s.executeQuery("SELECT product_id, (select package_id from inventory_products where id=product_id) package_id, (select brand_id from inventory_products where id=product_id) brand_id, (select label from inventory_packages where id=package_id) package_name, (select label from inventory_brands where id=brand_id) brand_name, (select sap_code from inventory_products where id=product_id) sap_code FROM employee_product_groups_list where product_group_id="+EmployeeProductGroupID+" order by package_name, brand_name");
	    	while(rs.next()){
	    		%>
	    		<tr>
	    			<td><%=rs.getString("package_name")%></td>
	    			<td><%=rs.getString("brand_name")%></td>
	    			<td><%=rs.getString("sap_code")%></td>
	    		</tr>
	    		<%
	    	}
	    	%>
	    	</table>
            
            <%
            
            
            s.close();
            c.close();
            ds.dropConnection();
            %>
        	</ul>