<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>

<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 158;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

%>
        	<table style="width: 100%">
        	<!-- 
        		<tr>
            		<th style="text-align: left; width: 20%">Invoice#</th>
            		<th style="text-align: left; width: 20%">Amount</th>
            		<th style="text-align: left; width: 20%">Created On</th>
            		<th style="text-align: left; width: 40%">&nbsp;</th>
				</tr>
			-->
        	
            <%
            
            Datasource ds = new Datasource();
			ds.createConnection();
			Connection c = ds.getConnection();
			Statement s = c.createStatement();
            
            long InvoiceNo = Utilities.parseLong(request.getParameter("InvoiceNo"));
            
            
            Date LastDate = Utilities.parseDate("01/01/1997");
            
            String SQL = "SELECT vbeln, kurrf_dat, (select sap_vbrk.NETWR+sap_vbrk.MWSBK from sap_vbrk where vbeln = vbrk.vbeln) invoice_amount, ( SELECT count(vbeln) FROM sap_vbrk_cancelled_invoices where vbeln="+InvoiceNo+" ) is_exists FROM sap_vbrk vbrk where vbeln = "+InvoiceNo; 
            
            ResultSet rs = s.executeQuery(SQL);
            
            boolean isFound = true;
            if(rs.first()){
            	isFound = true;
            	%>
            	
            	<tr>
            		<td><%= rs.getString("vbeln") %></td>
            		<td><%= Utilities.getDisplayCurrencyFormat(rs.getDouble("invoice_amount")) %></td>
            		<td><%=Utilities.getDisplayTimeFormat(rs.getTimestamp("kurrf_dat"))%></td>
            		<td>
            			<div style="width: 50%">
            				<% if( rs.getInt("is_exists") == 0 ){ %>
            				<input type="button" value="Cancel Invoice" data-mini="true" onclick="doCancel()" >
            				<% }else{ %>
            				<input type="button" value="Activate Invoice" data-mini="true" onclick="doActive()" >
            				<% } %>
            			</div>
            		</td>
				</tr>
				
            	<%
            	
            }
			%>
			
            			<div style="width: 50%">
            				<input type="button" value="Force Post" data-mini="true" onclick="doCancel()" >
            			</div>
			
			<%            
            if(isFound == false){
            	%>
            		<tr><td colspan="3">No result found.</td></tr>
            	<%
            }
            
            s.close();
            c.close();
            ds.dropConnection();
            %>
        	</table>