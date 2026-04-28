<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>



<html>       
  <body>	
     		 <table   Style="width:100%;" cellpadding="0" cellspacing="0"  >
        	
        		<tr >
					<td style="width: 100%" valign="top">
						<table Style="width:100%; font-size: 10pt; font-weight: 400;width:100%;" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder" ">
							<tr>
								<th style="width:70px;">Customer</th>
								<th style="width:70px;">Consumption</th>
							</tr>
        	
            <%
			
            Datasource ds = new Datasource();
			ds.createConnection();
			Connection c = ds.getConnection();
			Statement s = c.createStatement();
			
			double TotalConsumption =0;
            
            int DocumentID = Utilities.parseInt(request.getParameter("DocumentID"));
            //System.out.println(DocumentID);
            String query="SELECT customer_id,(select name from common_distributors cd where cd.distributor_id=glop.customer_id) customer_name,sum(total_units) quota FROM pep.gl_order_posting glop join gl_order_posting_promotions glopp on glop.id=glopp.id where glopp.request_id="+DocumentID+" group by customer_id ";
           //System.out.println(query);
            ResultSet rs2 = s.executeQuery(query);
            while(rs2.next()){
            	
            	TotalConsumption+=rs2.getDouble("quota");
            	%>
            	
							
							<tr style="text-align:left">
								<td ><%=rs2.getString("customer_id")%> - <%=rs2.getString("customer_name")%></td>			
								<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormat(rs2.getDouble("quota"))%></td>
							</tr>
						
            	<%
            }
            
            %>
         	
         	<tr>
         		<td><b>Total</b></td>
         		<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormat(TotalConsumption) %></td>
         	</tr>
         	
	        <%
            s.close();
            c.close();
            ds.dropConnection();
            
            %></table>
					</td>
				</tr>
             	
        	</table>
  </body>  	
</html>        	