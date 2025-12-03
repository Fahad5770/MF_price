<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>


        	<ul data-role="listview" data-inset="true"> 
        	
            <%
            long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
            int FeatureID = 153;
            if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
            	response.sendRedirect("AccessDenied.jsp");
            }

            Datasource ds = new Datasource();
			ds.createConnection();
			Connection c = ds.getConnection();
			Statement s = c.createStatement();
			
            Date FromDate = Utilities.parseDate(request.getParameter("FromDate"));
            Date ToDate = Utilities.parseDateTime(request.getParameter("ToDate"), 23, 59);
            
            Date LastDate = Utilities.parseDate("01/01/1997");
            
            String SQL = "select * from sampling_credit_slip where created_on between "+Utilities.getSQLFromDateTime(FromDate)+" and "+Utilities.getSQLToDateTime(ToDate)+" order by created_on desc ";
            //System.out.println(SQL);
            ResultSet rs = s.executeQuery(SQL);
            
            boolean isFound = false;
            while(rs.next()){
            	isFound = true;
            	%>
            	
            	<% 
            	if(!DateUtils.isSameDay(LastDate, rs.getDate("created_on")) ){
            		%>
            		<li data-role="list-divider"><%=Utilities.getDisplayFullDateFormat(rs.getDate("created_on"))%></li>
            		<%
            	}
            	%>
            	<li><a data-ajax="false" href="SamplingCreditSlip.jsp?EditID=<%=rs.getString("id")%>">
					<span style="font-size: 10pt; font-weight: 400;"><%= Utilities.getMonthNameByNumber(rs.getInt("month")) %> <%=rs.getInt("year")%></span>
					<span class="ui-li-count"><%=Utilities.getDisplayTimeFormat(rs.getTimestamp("created_on"))%></span>
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