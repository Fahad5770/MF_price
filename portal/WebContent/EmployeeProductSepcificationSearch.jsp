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
            
            long EmployeeID = Utilities.parseLong(request.getParameter("EmployeeID"));
            long DashboardEmployeeCode = Utilities.parseLong(request.getParameter("DashboardEmployeeCode"));
            
            ResultSet rs = s.executeQuery("SELECT *, (SELECT concat(vorna,' ',nachn) as name FROM sap_pa0002 where pernr=employee_id limit 1) employee_name FROM employee_product_specification where employee_id="+EmployeeID);
            
            Date LastDate = Utilities.parseDate("01/01/1997");
            int flag = 0;
            while(rs.next()){
            	flag++;
            	%>
            	
            	<% 
            	if(!DateUtils.isSameDay(LastDate, rs.getDate("created_on")) ){
            		%>
            		<li data-role="list-divider"><%=Utilities.getDisplayFullDateFormat(rs.getDate("created_on"))%></li>
            		<%
            	}
            	
            	%>
            	
            	<li><a data-ajax="false" href="EmployeeProductSpecification.jsp?EmployeeCode=<%=DashboardEmployeeCode%>&ProductSpecificationID=<%=rs.getString("employee_product_specification_id")%>">
					<span style="font-size: 10pt; font-weight: 400;"><%=rs.getString("employee_name")+" ("+rs.getString("employee_id")+")"%></span>
					<span class="ui-li-count"><%=Utilities.getDisplayTimeFormat(rs.getTimestamp("created_on"))%></span>
					</a>
				</li>
				
            	<%
            	LastDate = rs.getDate("created_on");
            }
            
            if(flag == 0){
            	%>
            		<li>
					<span style="font-size: 10pt; font-weight: 400;">No Result Found.</span>
					</a>
				</li>
            	<%
            }
            
            
            s.close();
            c.close();
            ds.dropConnection();
            %>
        	</ul>