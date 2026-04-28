<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
 
<%
long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(46, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}
%>
			
			<div data-role="collapsible" data-collapsed="false" data-theme="b" data-content-theme="d" id="datascope"  data-mini="true">
	    		
			<input type="hidden" name="UserIDSapCode" value="<%=request.getParameter("UserID")%>"/>
			<input type="hidden" name="FeatureID" value="<%=request.getParameter("featureid")%>"/>
	    		
	    		
	    		<h4 id="TitleGoesHere"><%=request.getParameter("title") %></h4>
	    		
			
				<div data-role="collapsible" data-collapsed="false" data-theme="c" data-content-theme="d" id="datascope" data-mini="true">
		    		<h4 id="TitleGoesHere">Warehouse</h4>
		    		
		    		<%
			
			Datasource ds = new Datasource();	
			ds.createConnection();
			Statement s = ds.createStatement();
			
			
			
			
			
			////////////////////////////////////
			
			
			
			
			
			
			//ResultSet rs = s.executeQuery("select * from common_warehouses");
			ResultSet rs = s.executeQuery("SELECT uaaw.warehouse_id as id, cw.label FROM user_access_admin_warehouses uaaw, common_warehouses cw where uaaw.warehouse_id=cw.id and uaaw.user_id="+SessionUserID+" and uaaw.feature_id=46");
			%>
			<table width="100%" border="0">
			<tr>
				<td style="width:70%">
					<select name="WarehouseSelect" id="WarehouseSelect"  data-mini="true">
					<option value="-1" >Select Warehouse</option>
						<%
						while(rs.next())
						{
						%>
						
						    <option value="<%=rs.getString("id")%>" data-mini="true"><%=rs.getString("label") %></option>
						<%
						} 
						%>    
					</select>
				</td>
				<td style="width:30%"><a href="#" data-role="button" data-icon="plus" data-iconpos="left" data-inline="true" onClick="AddWareHouse()" data-mini="true">Add</a></td>
			</tr>
			
			</table>
		    	<div id="AddeddWareHouse" style="width:70%">
					<ul data-role="listview" data-inset="true" id="populateDataUl" data-mini="true">					    
					   <%
					   //selecting warehouse
					
					ResultSet rs2 = s.executeQuery("select *,(select label from common_warehouses cw where cw.id = uaw.warehouse_id) as warehousename from user_access_warehouses uaw where user_id="+request.getParameter("UserID")+" AND feature_id="+request.getParameter("featureid"));
					
					while(rs2.next())
					{
						String FunctionName = "onClick=RemoveList('WarehouseDynamicallyAdded_"+rs2.getString("warehouse_id")+"')";
				    	%>
				    	<li data-mini="true" data-icon="delete" id="WarehouseDynamicallyAdded_<%= rs2.getString("warehouse_id")%>"><input type='hidden' name='Warehousehiddenfield' value='<%=rs2.getString("warehouse_id")%>,<%=rs2.getString("feature_id")%>,Warehouse'/><a href='#' <%=FunctionName%>><%=rs2.getString("warehousename")%></a></li>
				   <%  	
					}
					 %>  
					    
					</ul>
					</div>	
		    		
		    		
				</div>
				<div data-role="collapsible" data-collapsed="false" data-theme="c" data-content-theme="d" id="datascope"  data-mini="true">
		    		<h4 id="TitleGoesHere">Region</h4>
		    		<%
			
			
			
			//ResultSet rs1 = s.executeQuery("select * from common_regions");
		    ResultSet rs1 = s.executeQuery("SELECT uaar.region_id, cr.region_short_name, cr.region_name FROM user_access_admin_regions uaar, common_regions cr where uaar.region_id=cr.region_id and uaar.user_id="+SessionUserID+" and uaar.feature_id=46");
		   	
			%>
			<table width="100%" border="0">
			<tr>
				<td style="width:70%">
					<select name="RegionSelect" id="RegionSelect"  data-mini="true">
					<option value="-1" >Select Region</option>
						<%
						while(rs1.next())
						{
						%>						
						    <option value="<%=rs1.getString("region_id")%>" data-mini="true"><%=rs1.getString("region_short_name")+" - "+rs1.getString("region_name") %></option>
						<%
						} 
						%>    
					</select>
				</td>
				<td style="width:30%"><a href="#" data-role="button" data-icon="plus" data-iconpos="left" data-inline="true" onClick="AddRegion()" data-mini="true">Add</a></td>
			</tr>
			
			</table>
			<div id="AddeddRegion" style="width:70%">
					<ul data-role="listview" data-inset="true" id="populateRegionDataUl" data-mini="true">					    
					  <%
					   //selecting Region
					ResultSet rs3 = s.executeQuery("select * from user_access_regions uar, common_regions cr  where cr.region_id=uar.region_id and user_id="+request.getParameter("UserID")+" AND feature_id="+request.getParameter("featureid"));
					
					while(rs3.next())
					{
						String FunctionName = "onClick=RemoveList('RegionDynamicallyAdded_"+rs3.getString("region_id")+"')";
				    	%>
				    	<li data-mini="true" data-icon="delete" id="RegionDynamicallyAdded_<%= rs3.getString("region_id")%>"><input type='hidden' name='RegionIDhiddenfield' value='<%=rs3.getString("region_id")%>,<%=rs3.getString("feature_id")%>,Region'/><a href='#' <%=FunctionName%>><%=rs3.getString("region_short_name")+" - "+rs3.getString("region_name") %></a></li>
				   <%  	
					}
					 %>    
					</ul>
					</div>
				</div>
				<div data-role="collapsible" data-collapsed="false" data-theme="c" data-content-theme="d" id="datascope"  data-mini="true">
		    		<h4 id="TitleGoesHere">Distributor</h4>
		    		
		    		<table border="0" width="100%" id="DistributorTable">
					<tr>
						<td style="width:20%">
							<input type="text" name="DistributorID2" id="DistributorID2" placeholder="Distributor ID" data-mini="true" onChange="getDistributorName2()"/>
						</td>
						<td style="width:50%">
							<input type="text" name="DistributorName2" id="DistributorName2" placeholder="Distributor Name" data-mini="true" readonly /> 
							<input type="hidden" name="isSecondDistCall" id="isSecondDistCall" value="0"/>
						</td>
						<td style="width:30%"><a href="#" data-role="button" data-icon="plus" data-iconpos="left" data-inline="true" onClick="AddDistributor()" data-mini="true">Add</a></td>
					</tr>
					</table>
		    		<div>
		    		
		    		<%
		    			ResultSet rs_distributor = s.executeQuery("SELECT uaad.distributor_id FROM user_access_admin_distributors uaad where uaad.user_id="+SessionUserID+" and uaad.feature_id=46");
		    			while(rs_distributor.next()){
		    				%>
		    					<input type="hidden" name="AdminScopeDistributors" id="AdminScopeDistributors" value="<%=rs_distributor.getString("distributor_id")%>" >
		    				<%
		    			}
		    		%>
		    		
		    		</div>
				
				
				<div id="AddeddDistributor" style="width:70%">
					<ul data-role="listview" data-inset="true" id="populateDistributorDataUl" data-mini="true">					    
					 
					 <%
					 ResultSet rs4 = s.executeQuery("select * from common_distributors cd ,user_access_distributors uad where cd.distributor_id = uad.distributor_id and user_id="+request.getParameter("UserID")+" AND feature_id="+request.getParameter("featureid"));
					
					while(rs4.next())
					{
						String FunctionName = "onClick=RemoveList('DistributorIDDynamicallyAdded_"+rs4.getString("distributor_id")+"')";
				    	%>
				    	<li data-mini="true" data-icon="delete" id="DistributorIDDynamicallyAdded_<%= rs4.getString("distributor_id")%>"><input type='hidden' name='DistributorIDhiddenfield' value='<%=rs4.getString("distributor_id")%>,<%=rs4.getString("feature_id")%>,Distributor'/><a href='#' <%=FunctionName%>><%= rs4.getString("distributor_id")%> - <%=rs4.getString("name") %></a></li>
				   <%  	
					}
					 %>   
					</ul>
					
					
					</div>
				</div>	
					<div data-role="collapsible" data-collapsed="false" data-theme="c" data-content-theme="d" id="datascope"  data-mini="true">
		    		<h4 id="">Distributor Group</h4>
		    		<%
			
			
			
			//ResultSet rs5 = s.executeQuery("select * from common_distributor_groups");
		    ResultSet rs5 = s.executeQuery("SELECT uaadgps.group_id as id, cdgps.label FROM user_access_admin_distributor_groups uaadgps, common_distributor_groups cdgps where uaadgps.group_id=cdgps.id and uaadgps.user_id="+SessionUserID+" and uaadgps.feature_id=46");
			%>
			<table width="100%" border="0">
			<tr>
				<td style="width:70%">
					<select name="GroupDistributorSelect" id="GroupDistributorSelect"  data-mini="true">
					<option value="-1">Select Distributor Group</option>
						<%
						while(rs5.next())
						{
						%>						
						    <option value="<%=rs5.getString("id")%>" data-mini="true"><%=rs5.getString("label")%></option>
						<%
						} 
						%>    
					</select>
				</td>
				<td style="width:30%"><a href="#" data-role="button" data-icon="plus" data-iconpos="left" data-inline="true" onClick="AddGroupDistributor()" data-mini="true">Add</a></td>
			</tr>
			
			</table>
			<div id="AddeddGroupDistributor" style="width:70%">
					<ul data-role="listview" data-inset="true" id="populateGroupDistributorDataUl" data-mini="true">					    
					  <%
					   //selecting Region
					ResultSet rs6 = s.executeQuery("select * from user_access_distributor_groups uadg, common_distributor_groups cdg  where cdg.id=uadg.group_id and user_id="+request.getParameter("UserID")+" AND feature_id="+request.getParameter("featureid"));
					
					while(rs6.next())
					{
						String FunctionName = "onClick=RemoveList('GroupDistributorDynamicallyAdded_"+rs6.getString("id")+"')";
				    	%>
				    	<li data-mini="true" data-icon="delete" id="GroupDistributorDynamicallyAdded_<%= rs6.getString("id")%>"><input type='hidden' name='GroupDistributorhiddenfield' value='<%=rs6.getString("id")%>,<%=rs6.getString("feature_id")%>,GroupDistributor'/><a href='#' <%=FunctionName%>><%=rs6.getString("label")%></a></li>
				   <%  	
					}
					 %>    
					</ul>
					</div>
				</div>
					
					
					<table style="width:100%" border="0">
					<tr>
					<td style="width:80%"></td>
					<td style="width:20%; text-align: right; ">
					<a data-icon="check" data-theme="b" data-role="button" data-inline="true" id="WarehouseeSave" href="#" onClick="UserAdditionalRightSubmit();" data-mini="true" >Save</a>
					</td>
					</tr>
					</table>
					
					
					
					
				
				</div>
				
	<%
	s.close();
	ds.dropConnection();
	
	%>		
			