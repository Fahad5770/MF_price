<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>


        <!-- 	<ul data-role="listview" data-inset="true">  -->
        	
            <%
            
            long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
            int FeatureID = 321;


            if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
            	response.sendRedirect("AccessDenied.jsp");
            }
            
            Datasource ds = new Datasource();
			ds.createConnection();
			Connection c = ds.getConnection();
			Statement s = c.createStatement();
			
			String outletId =request.getParameter("outletId");
			System.out.println("outletId "+outletId);
            	%>
            	<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
				     <tr>
				     	<td>
				     
				     		<table class="GridWithBorder">
				     		
								<%
								
								
								String[] arr = {"", "", "",""};  
								
									int i = 0;
								//System.out.println("select * from mobile_common_outlets_files where outlet_id=" + outletId+" order by created_on desc limit 4");
									ResultSet rsi = s.executeQuery("select * from mobile_common_outlets_files where outlet_id=" + outletId+" order by created_on desc limit 4");
									while (rsi.next()) {
										arr[i] = rsi.getString("filename");
										i++;
										}
									if(i > 0){
								%>
								
										<tr>
										
										<td>
									
										<h3></h3><br/>
									<center>
										<img src="mobile/MobileFileDownloadOutlet?file=<%=arr[0]%>" style="width:200px; height:200px;" />
										</center>
										</td>
										<%if(!arr[1].equals("")){%>
										<td>
									
										<h3></h3><br/>
									<center>
										<img src="mobile/MobileFileDownloadOutlet?file=<%=arr[1]%>" style="width:200px; height:200px;" />
										</center>
										</td>
										<%} %>
										</tr>
										
										<tr>
										<%if(!arr[2].equals("")){%>
										<td>
									
										<h3></h3><br/>
									<center>
										<img src="mobile/MobileFileDownloadOutlet?file=<%=arr[2]%>" style="width:200px; height:200px;" />
										</center>
										</td>
										
										<td>
									<%} 
									if(!arr[3].equals("")){%>
										<h3></h3><br/>
									<center>
										<img src="mobile/MobileFileDownloadOutlet?file=<%=arr[3]%>" style="width:200px; height:200px;" />
										</center>
										</td>
										<%} %>
										</tr>
							
								
								<%}else{%><tr><td>No images Found</td></tr><%} %>
							</table>
				     
				     	</td>
				    </tr>
				</table>
 <% 
        
            s.close();
            c.close();
            ds.dropConnection();
            %>
        	