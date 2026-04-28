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
        	
        		
        	
            <%
			
            Datasource ds = new Datasource();
			ds.createConnection();
			Connection c = ds.getConnection();
			Statement s = c.createStatement();
            
            int DocumentID = Utilities.parseInt(request.getParameter("DocumentID"));
            //System.out.println(DocumentID);
//            String query="select filename,uri from mobile_order_unedited_files where id="+DocumentID;

            
            %>
         	
         	
         	
         	
         	
				
				<table>
					
					<%
					//System.out.println("select * from mrd_census_files where id="+TableMainID);
					
					int i=1;
					ResultSet rsi = s.executeQuery("select * from mobile_order_unedited_files where id="+DocumentID);
					
					while(rsi.next()){
					%>
						
 				
						<%if(i==1){ %>
						<tr>
						<td><img src="../OrderImages/<%=rsi.getString("filename") %>" style="width:250px; height:250px;"/></td>
						<%} %>
						<td>&nbsp;</td>
						<%if(i==2){ %>
						<td><img src="../OrderImages/<%=rsi.getString("filename") %>" style="width:250px; height:250px;"/></td>
						</tr>
						<%} %>
					
					<%if(i==3){%>
					<tr>
						 
						<td><img src="../OrderImages/<%=rsi.getString("file_name") %>" style="width:250px; height:250px;"/></td>
						<%} %>
						<td>&nbsp;</td>
						<%if(i==4){ %>
						<td><img src="../OrderImages/<%=rsi.getString("file_name") %>" style="width:250px; height:250px;"/></td>
						
					</tr>
					<%} %>
					<%if(i==5){%>
					<tr>
						 
						<td><img src="../OrderImages/<%=rsi.getString("file_name") %>" style="width:250px; height:250px;"/></td>
						
						
					</tr>
					<%} %>
					
					<%
					i++;
					}
					%>
				
				</table>
				
         	
         
         	
         	
         	
         	
         	
         	
         	
         	
         	
         	
         	
         	
         	
         	
         	
         	
	        <%
            s.close();
            c.close();
            ds.dropConnection();
            
            %></table>
		
  </body>  	
</html>        	