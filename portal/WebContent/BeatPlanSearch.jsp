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
            long OutletID = Utilities.parseLong(request.getParameter("OutletID"));
            long DashboardEmployeeCode = Utilities.parseLong(request.getParameter("DashboardEmployeeCode"));
            
            String where = "";
            
            if(EmployeeID > 0){
            	where += " and ebp.assigned_to = "+EmployeeID;
            }
            
            if(OutletID > 0){
            	where += " and ebps.outlet_id = "+OutletID;
            }
            
            ResultSet rs = s.executeQuery("SELECT distinct ebp.beat_plan_id, ebp.assigned_to, ebp.created_on FROM employee_beat_plan ebp, employee_beat_plan_schedule ebps where ebp.beat_plan_id=ebps.beat_plan_id "+where+"  order by ebp.created_on desc");
            
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
            	
            	<li><a data-ajax="false" href="EmployeeDashboard.jsp?EmployeeCode=<%=DashboardEmployeeCode%>&BeatPlanID=<%=rs.getString("beat_plan_id")%>">
					<span style="font-size: 10pt; font-weight: 400;"><%=rs.getString("assigned_to")%></span>
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