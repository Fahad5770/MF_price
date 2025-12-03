<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
			
			<div  data-theme="c" data-content-theme="d" id="datascope">
	    		
			
	    		<input type="hidden" name="UserIDSapCode" value="<%=request.getParameter("UserID")%>"/>
				<input type="hidden" name="ProductPromotionIDDD" value="<%=request.getParameter("productpromotionid")%>"/>
	    		
	    		
		    		<%
			long ProductPromotionID = Utilities.parseLong(request.getParameter("productpromotionid"));
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
					ResultSet rs3 = s.executeQuery("select * from inventory_sales_promotions_regions ipr, common_regions cr  where cr.region_id=ipr.region_id and ipr.product_promotion_id="+ProductPromotionID);
					
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
					 ResultSet rs4 = s.executeQuery("select * from common_distributors cd ,inventory_sales_promotions_distributors uad where cd.distributor_id = uad.distributor_id and uad.product_promotion_id="+ProductPromotionID);
					
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
		    		<h4 id="TitleGoesHere" style="width:100%">Outlet</h4>
		    		
		    		<table border="0" width="100%" id="DistributorTable">
					<tr>
						<td style="width: 20%;" >
							<input type="text" placeholder="Outlet ID" id="DeskSaleOutletID" name="DeskSaleOutletID" data-mini="true" onChange="getOutletName(); getOutletMasterInfo()"  maxlength="10">
						</td>
						<td style="width: 50%;">
							<input type="text" placeholder="Outlet Name" id="DeskSaleOutletName" name="DeskSaleOutletName" data-mini="true" readonly="readonly" tabindex="-1" >
						</td>
						
						<td style="width:30%"><a href="#" data-role="button" data-icon="plus" data-iconpos="left" data-inline="true" onClick="AddOutlet()" data-mini="true">Add</a></td>
					</tr>
					</table>
		    		<div id="AddeddOutlet" style="width:70%">
					<ul data-role="listview" data-inset="true" id="populateOutletDataUl" data-mini="true">					    
					 <%
					   //selecting Region
					ResultSet rs7 = s.executeQuery("select * from inventory_sales_promotions_outlet uadg, outletmaster om  where om.outlet_id=uadg.outlet_id and product_promotion_id="+ProductPromotionID);
					
					while(rs7.next())
					{
						String FunctionName = "onClick=RemoveList('OutletIDDynamicallyAdded_"+rs7.getString("outlet_id")+"')";
				    	%>
				    	
				   		<li data-mini='true' data-icon='delete' id="OutletIDDynamicallyAdded_<%= rs7.getString("outlet_id")%>" <%=FunctionName%>><input type='hidden' id="OutletIDForHiddenFieldID_<%= rs7.getString("outlet_id")%>" name='OutletIDhiddenfield' value="<%=rs7.getString("outlet_id")%>,Outlet"/><a href='#'><%= rs7.getString("outlet_id")%> - <%= rs7.getString("outlet_name")%></a></li>
				  <script>
					setTimeout(function(){
						$('#DeskSaleOutletID').on('dblclick', function(e, data){		
							$( "#LookupOutletSearch" ).on( "popupbeforeposition", function( event, ui ) {
								lookupOutletInit();
							} );
							$('#LookupOutletSearch').popup("open");
									  
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
			    
			    
			    
			</div><!-- /grid-b -->		
		    		
				<div class="ui-grid-b">
	    			
	    			<div class="ui-block-b">
	    				<div class="ui-bar" style="min-height:60px">
	    				<div data-role="collapsible" data-collapsed="false" data-theme="c" data-content-theme="d" id="datascope" data-mini="true">
		    		<h4 id="TitleGoesHere" style="width:100%">PJP</h4>
		    		
		    		<table border="0" width="100%" id="DistributorTable">
					<tr>
						<td style="width:20%">
            				<input type="text" name="ProductPromotionPJPID" id="ProductPromotionPJPID" onChange="getPJPName()" placeholder="PJP ID" data-mini="true">
            			</td>
            			<td style="width:50%">
            				<input type="text" name="ProductPromotionPJPName" id="ProductPromotionPJPName" readonly placeholder="PJP Name" data-mini="true">
            			</td>
						
						<td style="width:30%"><a href="#" data-role="button" data-icon="plus" data-iconpos="left" data-inline="true" onClick="AddPJP()" data-mini="true">Add</a></td>
					</tr>
					</table>
		    		<div id="AddeddPJP" style="width:70%">
					<ul data-role="listview" data-inset="true" id="populatePJPDataUl" data-mini="true">					    
					 <%
					   //selecting Region
					
					   ResultSet rs8 = s.executeQuery("select *, (SELECT label FROM distributor_beat_plan where id=pjp_id) pjp_label from inventory_sales_promotions_pjp where product_promotion_id="+ProductPromotionID);
					
					while(rs8.next())
					{
						String FunctionName = "onClick=RemoveList('PJPDynamicallyAdded_"+rs8.getString("pjp_id")+"')";						
				    	%>		    	
				   		
				  		<li data-mini='true' data-icon='delete' id='PJPDynamicallyAdded_<%=rs8.getString("pjp_id")%>' <%=FunctionName%>><input type='hidden' id='PJPForHiddenFieldID_<%=rs8.getString("pjp_id")%>' name='PJPhiddenfield' value='<%=rs8.getString("pjp_id")%>'/><a href='#'><%=rs8.getString("pjp_id")%> - <%=rs8.getString("pjp_label")%></a></li>
				  
					
				   <%  	
					}
					 %>    
					 
					</ul>
					
					
					</div>
				</div>
				
	    				</div>
	    			</div>
	    			
				</div><!-- /grid-a -->
				
				
					
					
					
					
					
					
				
				</div>
				
	<%
	s.close();
	ds.dropConnection();
	
	%>		
			