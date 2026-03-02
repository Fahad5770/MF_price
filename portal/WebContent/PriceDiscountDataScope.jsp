<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
			
			<div  data-theme="c" data-content-theme="d" id="datascope">
	    		
			
	    		<input type="hidden" name="UserIDSapCode" value="<%=request.getParameter("UserID")%>"/>
				<input type="hidden" name="PriceDiscountMasterTableID" value="<%=request.getParameter("pricediscountid")%>"/>
	    		
	    		
		    		<%
			long ProductDiscountID = Utilities.parseLong(request.getParameter("pricediscountid"));
			Datasource ds = new Datasource();	
			ds.createConnection();
			Statement s = ds.createStatement();
			
			////////////////////////////////////
			
			%>
		    	
		    <div class="ui-grid-b">
			    <div class="ui-block-a" >
			    	<div class="ui-bar " style="min-height:60px"><!-- block 1 -->
			    	<div data-role="collapsible" data-collapsed="false" data-theme="c" data-content-theme="d" id="datascope" data-mini="true">
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
					ResultSet rs3 = s.executeQuery("select * from inventory_price_discount_region ipr, common_regions cr  where cr.region_id=ipr.region_id and ipr.price_discount_id="+ProductDiscountID);
					
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
			    	<div data-role="collapsible" data-collapsed="false" data-theme="c" data-content-theme="d" id="datascope" data-mini="true">
		    		<h4 id="TitleGoesHere" style="width:100%">Distributor</h4>
		    		
		    		<table border="0" width="100%" id="DistributorTable">
					<tr>
						<td style="width:20%">
							<input type="text" name="DistributorID2" id="DistributorID2" placeholder="ID" data-mini="true" onChange="getDistributorName2()"/>
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
					 System.out.println("select * from common_distributors cd ,inventory_price_discount_distributor uad where cd.distributor_id = uad.distributor_id and uad.price_discount_id="+ProductDiscountID);
					 ResultSet rs4 = s.executeQuery("select * from common_distributors cd ,inventory_price_discount_distributor uad where cd.distributor_id = uad.distributor_id and uad.price_discount_id="+ProductDiscountID);
					
					while(rs4.next())
					{
						String FunctionName = "onClick=RemoveList('DistributorIDDynamicallyAdded_"+rs4.getString("distributor_id")+"')";
				    	%>
				    	<li data-mini="true" data-icon="delete" id="DistributorIDDynamicallyAdded_<%= rs4.getString("distributor_id")%>"><input type='hidden' name='DistributorIDhiddenfield' value='<%=rs4.getString("distributor_id")%>,Distributor'/><a href='#' <%=FunctionName%>><%=rs4.getString("distributor_id")%> - <%=rs4.getString("name") %></a></li>
				   		<script>				    	
				    		setTimeout(function(){		
				    			$('#DistributorID2').on('dblclick', function(e, data){        		
				    	    		$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
				    	    			lookupDistributorInit();
				    	    		} );
				    	    		$('#LookupDistributorSearch').popup("open");
				    	    		
				    	    	});
				    		}, 2000);				    	
				    	</script>
				   <%  	
					}
					 %>   
					</ul>
					
					
					</div>
				</div>
				
				
			    	</div>
			    </div>
			    
			    
			    <div class="ui-block-c">
	    				<div class="ui-bar " style="min-height:60px">
	    					<div data-role="collapsible" data-collapsed="false" data-theme="c" data-content-theme="d" id="datascope" data-mini="true">
		    		<h4 id="TitleGoesHere" style="width:100%">Channels</h4>
		    		
		    		
		   
		    		<%
			
			
			
			ResultSet rsChannel = s.executeQuery("select id, label from pci_sub_channel");
			%>
			<table width="100%" border="0">
			<tr>
				<td style="width:70%">
					<select name="ChannelSelect" id="ChannelSelect" data-mini="true">
					<option value="-1" data-mini="true">Select Channel</option>
						<%
						while(rsChannel.next())
						{
						%>						
						    <option value="<%=rsChannel.getString("id")%>" data-mini="true"><%=rsChannel.getString("label")%></option>
						<%
						} 
						%>    
					</select>
				</td>
				<td style="width:30%"><a href="#" data-role="button" data-icon="plus" data-iconpos="left" data-inline="true" onClick="AddChannel()" data-mini="true">Add</a></td>
			</tr>			
			</table>
			<div id="AddeddChannel" style="width:70%">
					<ul data-role="listview" data-inset="true" id="populateChannnelDataUl" data-mini="true">					    
					  <%
					   //selecting Region
					   System.out.println("SELECT ipd.id, pci_sub_channel_id, ch.label FROM inventory_price_discount ipd join inventory_price_discount_channel ipdc on ipd.id=ipdc.price_discount_id join pci_sub_channel ch on ch.id=ipdc.pci_sub_channel_id where ipd.id="+ProductDiscountID);
					ResultSet rsAddedChannel = s.executeQuery("SELECT ipd.id, pci_sub_channel_id, ch.label FROM inventory_price_discount ipd join inventory_price_discount_channel ipdc on ipd.id=ipdc.price_discount_id join pci_sub_channel ch on ch.id=ipdc.pci_sub_channel_id where ipd.id="+ProductDiscountID);
					
					while(rsAddedChannel.next())
					{
						String FunctionName = "onClick=RemoveList('ChannelDynamicallyAdded_"+rsAddedChannel.getString("pci_sub_channel_id")+"')";
				    	%>
				    	<li data-mini="true" data-icon="delete" id="ChannelDynamicallyAdded_<%= rsAddedChannel.getString("pci_sub_channel_id")%>"><input type='hidden' name='ChannelIDhiddenfield' value='<%=rsAddedChannel.getString("pci_sub_channel_id")%>,Channel'/><a href='#' <%=FunctionName%>><%=rsAddedChannel.getString("label") %></a></li>
				    	
				   <%  	
					}
					 %>    
					</ul>
		    	
		    	
		    		
				</div>
				
	    				</div>
	    			</div>
			    
			    
			    
			</div>
					
					
					
					
					
					
				
				</div>
				
	<%
	s.close();
	ds.dropConnection();
	
	%>		
			