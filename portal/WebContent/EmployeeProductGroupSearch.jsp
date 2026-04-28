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
            
            ResultSet rs = s.executeQuery("SELECT * FROM employee_product_groups");
            
            Date LastDate = Utilities.parseDate("01/01/1997");
            
            while(rs.next()){
            	
            	%>
            	
            	<% 
            	if(!DateUtils.isSameDay(LastDate, rs.getDate("created_on")) ){
            		%>
            		<li data-role="list-divider"><%=Utilities.getDisplayFullDateFormat(rs.getDate("created_on"))%></li>
            		<%
            	}
            	
            	%>
            	
            	<li><a data-ajax="false" href="EmployeeProductGroups.jsp?EmployeeProductGroupID=<%=rs.getString("product_group_id")%>&rand=<%=Math.random()%>">
					<span style="font-size: 10pt; font-weight: 400;"><%=rs.getString("product_group_name")%></span>
					<span class="ui-li-count"><%=Utilities.getDisplayTimeFormat(rs.getTimestamp("created_on"))%></span>
					</a>
				</li>
				
            	<%
            	LastDate = rs.getDate("created_on");
            }
            
            
            s.close();
            c.close();
            ds.dropConnection();
            %>
        	</ul>