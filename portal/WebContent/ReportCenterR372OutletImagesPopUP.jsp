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
            int FeatureID = 488;


            if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
            	response.sendRedirect("AccessDenied.jsp");
            }
            
            Datasource ds = new Datasource();
			ds.createConnection();
			Connection c = ds.getConnection();
			Statement s = c.createStatement();
			Statement s2 = c.createStatement();
			
			String id =request.getParameter("id");
			String outletId = request.getParameter("outletId");
			
		
            	%>
            	<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
				     <tr>
				     	<td>
				     
				     		<table class="GridWithBorder">
				     		
								<%
								String shelf1="", shelf2="", poster1="", poster2="", hangler="", woobler="";
								int month=0, year=0, i=0;
									//System.out.println("select filename, month, year, file_type from mobile_merchandiser_files where file_type=1 and id=" + id+" order by file_type");
									ResultSet rsi = s.executeQuery("select filename, month, year, file_type from mobile_merchandiser_files where file_type=1 and id=" + id+" order by file_type");
									while (rsi.next()) {
										 month = rsi.getInt("month");
										 year = rsi.getInt("year");
										 if(i==0){
											 shelf1= rsi.getString("filename");
										 }else{
											 
										 } shelf2= rsi.getString("filename");
										 i++;
									}
									
									
									i=0;
									//System.out.println("select filename, month, year, file_type from mobile_merchandiser_files where file_type=2 and id=" + id+" order by file_type");
									ResultSet rsPoster = s.executeQuery("select filename, month, year, file_type from mobile_merchandiser_files where file_type=2 and id=" + id+" order by file_type");
									while (rsPoster.next()) {
										 month = rsPoster.getInt("month");
										 year = rsPoster.getInt("year");
										 if(i==0){
											 poster1=rsPoster.getString("filename");
										 }else{
											 poster2=rsPoster.getString("filename");
										 } 
										 i++;
									}
								
									//System.out.println("select filename, month, year, file_type from mobile_merchandiser_files where file_type=3 and id=" + id+" order by file_type");
									ResultSet rsHangler = s.executeQuery("select filename, month, year, file_type from mobile_merchandiser_files where file_type=3 and id=" + id+" order by file_type");
									if(rsHangler.first()){
										hangler = rsHangler.getString("filename");
										 month = rsHangler.getInt("month");
										 year = rsHangler.getInt("year");
									}
									
									//System.out.println("select filename, month, year, file_type from mobile_merchandiser_files where file_type=4 and id=" + id+" order by file_type");
									ResultSet rsWoobler = s.executeQuery("select filename, month, year, file_type from mobile_merchandiser_files where file_type=4 and id=" + id+" order by file_type");
									if(rsWoobler.first()){
										woobler = rsWoobler.getString("filename");
										 month = rsWoobler.getInt("month");
										 year = rsWoobler.getInt("year");
									}
									
								%>
								
										<tr>
										
										<td>
									
										<h3>Shelf 1</h3><br/>
									<center>
										<img src="download/MobileFileDownloadMerchendising?file=<%=shelf1%>&year=<%=year%>&month=<%=month%>&outletId=<%=outletId%>" style="width:200px; height:200px;" />
										</center>
										</td>
										
										<td>
										
										<td>
									
										<h3>Shelf 2</h3><br/>
									<center>
										<img src="download/MobileFileDownloadMerchendising?file=<%=shelf2%>&year=<%=year%>&month=<%=month%>&outletId=<%=outletId%>" style="width:200px; height:200px;" />
										</center>
										</td>
										
										<td></tr>
										<%if(!poster1.equals("") || !poster2.equals("")){ %>
										<tr>
										
										<td>
									
										<h3>Poster 1</h3><br/>
									<center>
										<img src="download/MobileFileDownloadMerchendising?file=<%=poster1%>&year=<%=year%>&month=<%=month%>&outletId=<%=outletId%>" style="width:200px; height:200px;" />
										</center>
										</td>
										
										<td>
										<%if(!poster2.equals("")){ %>
										<td>
									
										<h3>Poster 2</h3><br/>
									<center>
										<img src="download/MobileFileDownloadMerchendising?file=<%=poster2%>&year=<%=year%>&month=<%=month%>&outletId=<%=outletId%>" style="width:200px; height:200px;" alt="No Image" />
										</center>
										</td>
										<%} %>
										<td></tr>
								<%} %>
								<%if(!hangler.equals("")){ %>
										<tr>
										<td>
									
										<h3>Hangler</h3><br/>
									<center>
										<img src="download/MobileFileDownloadMerchendising?file=<%=hangler%>&year=<%=year%>&month=<%=month%>&outletId=<%=outletId%>" style="width:200px; height:200px;" alt="No Image" />
										</center>
										</td>
										
										<td>
								<%} %>
									<%if(!woobler.equals("")){ %>
										
										<td>
									
										<h3>Woobler</h3><br/>
									<center>
										<img src="download/MobileFileDownloadMerchendising?file=<%=woobler%>&year=<%=year%>&month=<%=month%>&outletId=<%=outletId%>" style="width:200px; height:200px;" alt="No Image" />
										</center>
										</td>
										
										<td></tr>
								<%} %>
							</table>
				     
				     	</td>
				    </tr>
				</table>
 <% 
        
            s.close();
            c.close();
            ds.dropConnection();
            %>
        	