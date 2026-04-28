<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
			
			<div  data-theme="c" data-content-theme="d" id="datascope">
	    		
			
	    		<input type="hidden" name="UserIDSapCode" value="<%=request.getParameter("UserID")%>"/>
				<input  type="hidden"  name="PciChannelID" value="<%=request.getParameter("BulkDiscountID")%>"/>
			
	    		
		    		<%
			long BulkDiscountID = Utilities.parseLong(request.getParameter("PciChannelID"));
		    		System.out.println("BulkDiscountID = "+ BulkDiscountID);

			Datasource ds = new Datasource();	
			ds.createConnection();
			Statement s = ds.createStatement();

			////////////////////////////////////
			
			%>
		    	
		    <div class="ui-grid-b">
			    <div class="ui-block-a" >
			    	<div class="ui-bar " style="min-height:60px"><!-- block 1 -->
			 
		    		<%
			
			
			
			ResultSet rs1 = s.executeQuery("select * from pci_sub_channel");
			%>
			<table width="100%" border="0">
			<tr>
				<td style="width:70%">
					<select name="PCISelect" id="PCISelect" data-mini="true">
					<option value="0" data-mini="true">Select Channel</option>
						<%
						while(rs1.next())
						{
						%>			
		
						    <option <%if(BulkDiscountID == rs1.getLong("id")){ %> selected <%} %> value="<%=rs1.getString("id")%>" data-mini="true"><%=rs1.getString("id")+" - "+rs1.getString("label") %></option>
						<%
						} 
						%>    
					</select>
				</td>
			</tr>
			
			</table>
		
				</div>
			    	</div>
			    </div>
		
				    </div> 
					 
	
					
					
					
				
				</div>
				
	<%
	s.close();
	ds.dropConnection();
	
	%>		
<%-- 			<%if(BulkDiscountID != 0){ %> selected <%} %> --%>