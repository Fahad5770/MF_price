<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
			
			<div  data-theme="c" data-content-theme="d" id="datascope">
	    		
			
	    		<input type="hidden" name="UserIDSapCode" value="<%=request.getParameter("UserID")%>"/>
				<input type="hidden" name="PriceListID" value="<%=request.getParameter("pricelistid")%>"/>
	    		
	    		
		    		<%
			long PriceListID = Utilities.parseLong(request.getParameter("pricelistid"));
			Datasource ds = new Datasource();	
			ds.createConnection();
			Statement s = ds.createStatement();
			
			////////////////////////////////////
			
			%>
		    	
		    <div class="ui-grid-b">
			    <div class="ui-block-a" >
			    	<div class="ui-bar " style="min-height:60px"><!-- block 1 -->
			    	<div data-role="collapsible" data-collapsed="true" data-theme="c" data-content-theme="d" id="datascope" data-mini="true">
		    		<h4 id="TitleGoesHere" style="width:100%">Region</h4>
		    		<%
			
			
			
			ResultSet rs1 = s.executeQuery("select * from common_regions");
			%>
			<table width="100%" border="0">
			<tr>
				<td style="width:70%">
					<select name="RegionSelect" id="RegionSelect" data-mini="true">
					<option value="-1" data-mini="true">Select Region</option>
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
					ResultSet rs3 = s.executeQuery("select * from inventory_price_list_regions ipr, common_regions cr  where cr.region_id=ipr.region_id and ipr.price_list_id="+PriceListID);
					
					while(rs3.next())
					{
						String FunctionName = "onClick=RemoveList('RegionDynamicallyAdded_"+rs3.getString("region_id")+"')";
				    	%>
				    	<li data-mini="true" data-icon="delete" id="RegionDynamicallyAdded_<%= rs3.getString("region_id")%>"><input type='hidden' name='RegionIDhiddenfield' value='<%=rs3.getString("region_id")%>,Region'/><a href='#' <%=FunctionName%>><%=rs3.getString("region_short_name")+" - "+rs3.getString("region_name") %></a></li>
				   <%  	
					}
					 %>    
					</ul>
					</div>
				</div>
			    	</div>
			    </div>
			    <div class="ui-block-b">
			    	<div class="ui-bar " style="min-height:60px"><!--  block 2 -->
			    	<div data-role="collapsible" data-collapsed="true" data-theme="c" data-content-theme="d" id="datascope" data-mini="true">
		    		<h4 id="TitleGoesHere" style="width:100%">Distributor</h4>
		    		
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
		    		<div id="AddeddDistributor" style="width:70%">
					<ul data-role="listview" data-inset="true" id="populateDistributorDataUl" data-mini="true">					    
					 
					 <%
					 ResultSet rs4 = s.executeQuery("select * from common_distributors cd ,inventory_price_list_distributors uad where cd.distributor_id = uad.distributor_id and uad.price_list_id="+PriceListID);
					
					while(rs4.next())
					{
						String FunctionName = "onClick=RemoveList('DistributorIDDynamicallyAdded_"+rs4.getString("distributor_id")+"')";
				    	%>
				    	<li data-mini="true" data-icon="delete" id="DistributorIDDynamicallyAdded_<%= rs4.getString("distributor_id")%>"><input type='hidden' name='DistributorIDhiddenfield' value='<%=rs4.getString("distributor_id")%>,Distributor'/><a href='#' <%=FunctionName%>><%=rs4.getString("distributor_id")%> - <%=rs4.getString("name") %></a></li>
				   <%  	
					}
					 %>   
					</ul>
					
					
					</div>
				</div>
				
				
			    	</div>
			    </div>
			    <div class="ui-block-c">
			    	<div class="ui-bar " style="min-height:60px"><!--  block 3 -->
			    	<div data-role="collapsible" data-collapsed="true" data-theme="c" data-content-theme="d" id="datascope" data-mini="true">
		    		<h4 id="" style="width:100%">Distributor Group</h4>
		    		<%
			
			
			
			ResultSet rs5 = s.executeQuery("select * from common_distributor_groups");
			%>
			<table width="100%" border="0">
			<tr>
				<td style="width:70%">
					<select name="GroupDistributorSelect" id="GroupDistributorSelect" data-mini="true">
					<option value="-1" data-mini="true">Select Distributor Group</option>
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
					ResultSet rs6 = s.executeQuery("select * from inventory_price_list_distributor_groups uadg, common_distributor_groups cdg  where cdg.id=uadg.group_id and price_list_id="+PriceListID);
					
					while(rs6.next())
					{
						String FunctionName = "onClick=RemoveList('GroupDistributorDynamicallyAdded_"+rs6.getString("id")+"')";
				    	%>
				    	<li data-mini="true" data-icon="delete" id="GroupDistributorDynamicallyAdded_<%= rs6.getString("id")%>"><input type='hidden' name='GroupDistributorhiddenfield' value='<%=rs6.getString("id")%>,GroupDistributor'/><a href='#' <%=FunctionName%>><%=rs6.getString("label")%></a></li>
				   <%  	
					}
					 %>    
					</ul>
					</div>
				</div>
			    	</div>
			    </div>
			</div><!-- /grid-b -->		
		    		
				
				
				
					 <div class="ui-grid-b">
						 <div class="ui-block-a">
				    	<div class="ui-bar " style="min-height:60px"><!--  block 2 -->
				    	<div data-role="collapsible" data-collapsed="true" data-theme="c" data-content-theme="d" id="datascope" data-mini="true">
			    		<h4 id="TitleGoesHere" style="width:100%">PJP</h4>
			    		
			    		<table border="0" width="100%" id="PJPTable">
						<tr>
							<td style="width:20%">
								<input type="text" name="PJPID" id="PJPID" placeholder="PJP ID" data-mini="true" onChange="getPJPName()"/>
							</td>
							<td style="width:50%">
								<input type="text" name="PJPName" id="PJPName" placeholder="PJP Name" data-mini="true" readonly /> 
								<input type="hidden" name="isSecondPJPCall" id="isSecondPJPCall" value="0"/>
							</td>
							<td style="width:30%"><a href="#" data-role="button" data-icon="plus" data-iconpos="left" data-inline="true" onClick="AddPJP()" data-mini="true">Add</a></td>
						</tr>
						</table>
			    		<div id="AddeddPJP" style="width:70%">
						<ul data-role="listview" data-inset="true" id="populatePJPDataUl" data-mini="true">					    
						 
						 <%
						 ResultSet rs41 = s.executeQuery("select * from distributor_beat_plan dbp ,inventory_price_list_pjp iplp where dbp.id = iplp.pjp_id and iplp.price_list_id="+PriceListID);
						
						while(rs41.next())
						{
							String FunctionName = "onClick=RemoveList('PJPIDDynamicallyAdded_"+rs41.getString("pjp_id")+"')";
					    	%>
					    	<li data-mini="true" data-icon="delete" id="PJPIDDynamicallyAdded_<%= rs41.getString("pjp_id")%>"><input type='hidden' name='PJPIDhiddenfield' value='<%=rs41.getString("pjp_id")%>,PJP'/><a href='#' <%=FunctionName%>><%=rs41.getString("pjp_id")%> - <%=rs41.getString("label") %></a></li>
					   <%  	
						}
						 %>   
						</ul>
						
						
						</div>
					</div>
					
					
				    	</div>
				    </div>
					 
					 </div>
					
					<!--  
					
					<table style="width:100%" border="0">
					<tr>
					<td style="width:80%"></td>
					<td style="width:20%">
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="WarehouseeSave" href="#" onClick="UserAdditionalRightSubmit();" data-mini="true" >Save</a>
					</td>
					</tr>
					</table>
					-->
					
					
					
				
				</div>
				
	<%
	s.close();
	ds.dropConnection();
	
	%>		
			