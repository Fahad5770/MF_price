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
     		 <table   Style="width:100%;bo1rder: 1px solid #BEBEBE; overflow: scroll;" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder" >
        	
        		<tr Style="b1order: 1px solid black">
					<td style="font-size: 10pt; font-weight: 400;width:70%"; >
						<table Style="width:100%;">
							<tr>
								<th style="width:70px;">Package</th>
								<th style="width:70px;">RawCases</th>
							</tr>
        	
            <%
			
            
            Datasource ds = new Datasource();
			ds.createConnection();
			Connection c = ds.getConnection();
			Statement s = c.createStatement();
            
            int DocumentID = Utilities.parseInt(request.getParameter("DocumentID"));
            //System.out.println(DocumentID);
            String query="SELECT  eclrp.package_id,(select ipv.label from inventory_packages ipv where ipv.id=eclrp.package_id) package_name,eclrp.raw_cases FROM pep.ec_empty_credit_limit_request_products  eclrp where eclrp.id="+DocumentID+"  ";
           //System.out.println(query);
            ResultSet rs2 = s.executeQuery(query);
            while(rs2.next()){
            	%>
            	
							
							<tr style="text-align:left">
								<td > <%=rs2.getString("package_name")%></td>			
								<td><%=rs2.getString("raw_cases")%></td>
							</tr>
						
            	<%
            }
            
            %>
         	
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