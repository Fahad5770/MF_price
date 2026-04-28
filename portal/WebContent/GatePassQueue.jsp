<%@page import="org.omg.CORBA.portable.RemarshalException"%>
<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.UserAccess"%>
<%


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
String WarehouseIds = UserAccess.getWarehousesQueryString(UserAccess.getUserFeatureWarehouse(SessionUserID, 35));

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();

%>


<div data-role="content" data-theme="d">

<table border=0 style="width:100%">
	<tr>
		
		<td style="width:20%" valign="top">
		<%
			String where = "";
		%>
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="b">
				
			
			<li data-role="list-divider">Pending</li>
				<li>
				<table border=0 style="font-size:13px; width:100%"  data-role="table" id="SamplingSummary" data-mode="reflow" class="ui-responsive table-stroke">
					  <thead>
					    <tr>
							<th data-priority="2" >Order#</th>							
							<th data-priority="1"  >Date</th>
							
							
					    </tr>
					  </thead>
			
			            <%
			            
						//String Sql1 = "select delivery_id, sap_order_no,created_on,idn.is_delivered from inventory_delivery_note idn where date_format(idn.created_on,'%Y-%m-%d')  = curdate() and idn.is_delivered=1 order by created_on desc";
			           String Sql1 = "select delivery_id, sap_order_no,created_on,idn.is_delivered from inventory_delivery_note idn where idn.is_delivered=0 and idn.warehouse_id in ("+WarehouseIds+") order by created_on desc";
			                //System.out.println(Sql);
			                ResultSet rs1 = s2.executeQuery(Sql1);
			                while( rs1.next() ){
			                	
			                	%>
			                	
			                	<tr>
			    					<td ><%=rs1.getLong("sap_order_no")%></td>
			    					<td><%=Utilities.getDisplayDateTimeFormat(rs1.getTimestamp("created_on"))  %></td>
			    					
			    					 
			    		    	</tr>
			                	
			                	<%
			                }
			                %>
			</table>
			</li>
			<li data-role="list-divider">Delivered</li>
				<li>
				<table border=0 style="font-size:13px; width:100%"  data-role="table" id="SamplingSummary" data-mode="reflow" class="ui-responsive table-stroke">
					  <thead>
					    <tr>
							<th data-priority="2" >Order#</th>							
							<th data-priority="1"  >Date</th>
							
							
					    </tr>
					  </thead>
			
			            <%
							String Sql = "select delivery_id, sap_order_no,created_on,idn.is_delivered from inventory_delivery_note idn where date_format(idn.created_on,'%Y-%m-%d')  = curdate() and idn.is_delivered=1 and idn.warehouse_id in ("+WarehouseIds+") order by created_on desc";
			            //String Sql = "select delivery_id, sap_order_no,created_on,idn.is_delivered from inventory_delivery_note idn where date_format(idn.created_on,'%Y-%m-%d')  = subdate(curdate(), 1) and idn.is_delivered=1 order by created_on desc";
			                //System.out.println(Sql);
			                ResultSet rs2 = s2.executeQuery(Sql);
			                while( rs2.next() ){
			                	
			                	%>
			                	
			                	<tr>
			    					<td ><%=rs2.getLong("sap_order_no")%></td>
			    					<td><%=Utilities.getDisplayDateTimeFormat(rs2.getTimestamp("created_on"))  %></td>
			    					
			    					 
			    		    	</tr>
			                	
			                	<%
			                }
			                %>
			</table>
			</li>
			
			</ul>
		
		</td>
	</tr>
</table>
</div>
<%
c.close();
ds.dropConnection();
%>