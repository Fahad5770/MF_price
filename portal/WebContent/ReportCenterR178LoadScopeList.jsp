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
        	
        		<li data-role="list-divider" data-theme="a">Region</li>
        	
            <%
			
            Datasource ds = new Datasource();
			ds.createConnection();
			Connection c = ds.getConnection();
			Statement s = c.createStatement();
            
            int DocumentID = Utilities.parseInt(request.getParameter("DocumentID"));
            
            ResultSet rs = s.executeQuery("SELECT region_id, ( SELECT concat(region_short_name, ' - ', region_name) FROM common_regions where region_id=inventory_sales_promotions_request_regions.region_id ) region_name FROM inventory_sales_promotions_request_regions where product_promotion_id="+DocumentID+"  order by region_id");
            while(rs.next()){
            	%>
            	
            	<li>
					<span style="font-size: 10pt; font-weight: 400;"><%=rs.getString("region_name")%></span>
				</li>
				
            	<%
            }
            
            
            %>
            
            	<li data-role="list-divider" data-theme="a">Distributor</li>
            
            <%
            ResultSet rs2 = s.executeQuery("SELECT distributor_id, ( SELECT concat(distributor_id, ' - ', name) FROM common_distributors where distributor_id=inventory_sales_promotions_request_distributors.distributor_id ) distributor_name FROM inventory_sales_promotions_request_distributors where product_promotion_id="+DocumentID+" order by distributor_id");
            while(rs2.next()){
            	%>
            	<li>
					<span style="font-size: 10pt; font-weight: 400;"><%=rs2.getString("distributor_name")%></span>
				</li>
            	<%
            }
            
            %>
            	<li data-role="list-divider" data-theme="a">Outlet</li>
            <%
            ResultSet rs3 = s.executeQuery("SELECT outlet_id, ( SELECT concat(id, ' - ', name) FROM common_outlets where id=inventory_sales_promotions_request_outlet.outlet_id ) outlet_name FROM inventory_sales_promotions_request_outlet where product_promotion_id="+DocumentID+" order by outlet_id");
            while(rs3.next()){
            	%>
            	<li>
					<span style="font-size: 10pt; font-weight: 400;"><%=rs3.getString("outlet_name")%></span>
				</li>
            	<%
            }
            
            
            %>
	        	<li data-role="list-divider" data-theme="a">PJP</li>
	        <%
	        ResultSet rs4 = s.executeQuery("SELECT pjp_id, ( SELECT concat(id, ' - ', label) FROM distributor_beat_plan where id=inventory_sales_promotions_request_pjp.pjp_id ) pjp_name FROM inventory_sales_promotions_request_pjp where product_promotion_id="+DocumentID+" order by pjp_id");
	        while(rs4.next()){
	        %>
        	<li>
				<span style="font-size: 10pt; font-weight: 400;"><%=rs4.getString("pjp_name")%></span>
			</li>
        	<%
	        }
	        
	        
            s.close();
            c.close();
            ds.dropConnection();
            
            %>
        	</ul>