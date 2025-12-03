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
			
            long DistributorID = Utilities.parseLong(request.getParameter("DistributorID"));
            
            Date LastDate = Utilities.parseDate("01/01/1997");
            
            String SQL = "SELECT *, MONTHNAME(STR_TO_DATE(month, '%m')) month_name FROM distributor_targets where distributor_id="+DistributorID+" order by year, month";
            //System.out.println(SQL);
            ResultSet rs = s.executeQuery(SQL);
            
            boolean isFound = false;
            while(rs.next()){
            	isFound = true;
            	%>
            	
            	
            	<li><a data-ajax="false" href="DistributorTargets.jsp?DistributorTargetID=<%=rs.getString("id")%>">
					<span style="font-size: 10pt; font-weight: 400;"><%=rs.getString("distributor_id")%> - <%=rs.getString("month_name")%> - <%=rs.getString("year")%></span>
					</a>
				</li>
				
            	<%
            	LastDate = rs.getDate("created_on");
            }
            
            if(isFound == false){
            	%>
            		<li>No result found.</li>
            	<%
            }
            
            s.close();
            c.close();
            ds.dropConnection();
            %>
        	</ul>