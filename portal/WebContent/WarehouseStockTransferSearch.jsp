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
			
            Date FromDate = Utilities.parseDate(request.getParameter("FromDate"));
            Date ToDate = Utilities.parseDateTime(request.getParameter("ToDate"), 23, 59);
            
            Date LastDate = Utilities.parseDate("01/01/1997");
            
            String SQL = "select *  FROM inventory_warehouse_transfers where created_on between "+Utilities.getSQLDate(FromDate)+" and "+Utilities.getSQLDateNext(ToDate)+" order by created_on desc";
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
            	<li>
            	
            	<a data-ajax="true" href="" onClick="LoadEditCase(<%=rs.getString("id")%>)">
					<span style="font-size: 10pt; font-weight: 400;"><%=rs.getString("vehicle_no")%></span>
					
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
            %>
            <form method="post" action="WarehouseStockTransfer.jsp" id="ReceiptFromProductionSearchForm">
				<input type="hidden" name="ProductionReceiptID" id="ProductionReceiptID" value=""/>
			</form>
            <%
            s.close();
            c.close();
            ds.dropConnection();
            %>
        	</ul>