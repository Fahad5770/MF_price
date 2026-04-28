<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<jsp:useBean id="bean" class="com.pbc.outlet.OutletDashboard" scope="page"/>
<jsp:setProperty name="bean" property="*"/>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 207;
if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}


if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

boolean isEditCase = false;
long EditID = Utilities.parseLong(request.getParameter("DistributorTargetID"));
if(EditID > 0){
	isEditCase = true;
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();



%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script>
		
			var isEditCase = <%=isEditCase%>;
			var EditID = <%=EditID%>;
			var FeatureID = <%=FeatureID%>;
		
		</script>
						
		<script src="js/SamplingPlannedSales.js"></script>
		<script src="js/lookups.js"></script>
		

		<style>
		
			.radio_style
			  {
			      display: block;
			      width: 80px;
			      height: 50px;
			      background-repeat: no-repeat;
			      background-position: -231px 0px;
			  }
			  
			  .ui-table-reflow.ui-responsive{
			  	display:block;
			  }
		
		</style>
		
		
		
	</head>
	
<body>

<div data-role="page" id="SamplingPlannedSalesPage" data-url="SamplingPlannedSalesPage" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Sampling Planned Sales" name="title"/>
    </jsp:include>
    <!-- /header -->
    
    <div data-role="content" data-theme="d">
    
    <form action="test2.jsp" name="MainForm" id="MainForm" >
		<ul data-role="listview" data-inset="false" data-divider-theme="c">
		
			<li>
			
			    	<input type="hidden" name="EditID" id="EditID" value="<%=EditID%>" >
			    	
			        <table style="width:100%" border="0">
			        	<tr>
			        		<td valign="top" style="padding: 0px; width: 50%" colspan="2">
			        			<table border="0" style="width: 100%" cellpadding="0">
			        				<tr> 
										<td style="width:20%">
											<label for="OutletID" data-mini="true">Outlet ID</label>
											<input type="text" name="OutletID" id="OutletID" data-mini="true" aplaceholder="Outlet ID" onchange="getOutletInfoJson()" >
											<input type="hidden" name="MonthCycle" id="MonthCycle" value="" >
											<input type="hidden" name="ProductGroupID" id="ProductGroupID" value="" >
										</td>
										<td style="width:70%">
											<label for="OutletName" data-mini="true">Outlet Name</label>
											<input type="text" name="OutletName" id="OutletName" data-mini="true" aplaceholder="Outlet Name" readonly="readonly" >
										</td>
									</tr>
			        			</table>
			        		</td>
			        	
			        		<td>
			        			<label for="StartDate" data-mini="true">Start Date</label>
			        			<input type="text" name="StartDate" id="StartDate" value="" aplaceholder="Start Date" readonly="readonly" data-mini="true" >
			        		</td>
			        		<td>
			        			<label for="EndDate" data-mini="true">End Date</label>
			        			<input type="text" name="EndDate" id="EndDate" value="" aplaceholder="End Date" readonly="readonly" data-mini="true" >
			        		</td>
			        	</tr>
			        	<tr>
			        		<td>
			        			<label for="RequestID" data-mini="true">Request ID</label>
			        			<input type="text" name="RequestID" id="RequestID" value="" aplaceholder="RequestID" readonly="readonly" data-mini="true" >
			        		</td>
			        		<td>
			        			<label for="ActivatedOn" data-mini="true">Activated On</label>
			        			<input type="text" name="ActivatedOn" id="ActivatedOn" value="" aplaceholder="Activated On" readonly="readonly" data-mini="true" >
			        		</td>
			        	</tr>
			        	
			        </table>
			        
				
				
			</li>
			
			<li data-role="list-divider">CSD</li>
			
			<li>
			
				<table border=0 style="width: 100%">
					<tr>
						<td style="width: 50%" valign="top">
						
							<table border=0 data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt; width:100%">
							  <thead>
							    <tr class="ui-bar-c">
									<th data-priority="1">Package</th>
									<th data-priority="1">Qty</th>
									<th data-priority="1">Action</th>
									<th data-priority="1">&nbsp;</th>
							    </tr>
							  </thead>
							  
								<tbody id="DistributorTargetsTableBody">
									
									<%
									ResultSet rs = s.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where category_id = 1 and type_id = 1 order by package_label");
									while(rs.next()){
										%>
										<tr>
												<td style="width: 30%">
													<%=rs.getString("package_label")%>
													<input type="hidden" name="package_id" id="package_id" value="<%=rs.getString("package_id")%>" >
												</td>
												
												<td style="width: 30%">
													<input type="text" name="qty" id="qty_<%=rs.getString("package_id")%>" value="" data-mini="true" size="10" style="text-align: center" onkeyup="UpdateAnyField(this.value, <%=rs.getString("package_id")%>)" onchange="CheckQuantity(0, <%=rs.getString("package_id")%>)" >
												</td>
												
												<td style="width: 30%">
													<input type="button" value=" >> " data-mini="true" onclick="ToggleBrands('brand_table_<%=rs.getString("package_id")%>')" >
												</td>
												
												<td style="width: 30%; text-align: right">
													<table style="width: 100%; display: none" id="brand_table_<%=rs.getString("package_id")%>">
														<tr>
															<td nowrap style="width: 50%">Any</td>
															<td><input type="text" name="qty_<%=rs.getString("package_id")%>_any" id="qty_<%=rs.getString("package_id")%>_any" value="" data-mini="true" style="text-align: center; width: 100px" readonly="readonly"  tabindex="-1" ></td>
														</tr>
														
														<%
														//ResultSet rs2 = s2.executeQuery("SELECT brand_id, ( SELECT label FROM inventory_brands where id=brand_id ) brand_name FROM inventory_products where category_id=1 and package_id="+rs.getString("package_id"));
														ResultSet rs2 = s2.executeQuery("SELECT brand_id, brand_label FROM inventory_products_view where category_id=1 and type_id=1 and package_id="+rs.getString("package_id"));
														while(rs2.next()){
															%>
																<tr>
																	<td nowrap style="width: 50%"><%=rs2.getString("brand_label")%></td>
																	<td>
																		<input type="text" name="qty_<%=rs.getString("package_id")%>" id="qty_<%=rs.getString("package_id")%>_<%=rs2.getString("brand_id")%>" value="" data-mini="true" style="text-align: center; width: 100px" onchange="CheckQuantity(this.id, <%=rs.getString("package_id")%>)" >
																		<input type="hidden" name="brand_id_<%=rs.getString("package_id")%>" id="brand_id_<%=rs.getString("package_id")%>" value="<%=rs2.getString("brand_id")%>" >
																	</td>
																</tr>
															<%
														}
														%>
														
													</table>
												</td>
												
											</tr>
										<%
									}
									%>
									
								</tbody>
							</table>
												
						</td>
						<td style="width: 50%">&nbsp;</td>
					</tr>
				</table>
			
			
									
					
			</li>
			
			<li data-role="list-divider">NCB</li>
			<li>
				
				<table border=0 style="width: 100%">
					<tr>
						<td style="width: 50%" valign="top">
						
							<table border=0 data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt; width:100%">
							  <thead>
							    <tr class="ui-bar-c">
									<th data-priority="1">Package</th>
									<th data-priority="1">Qty</th>
									<th data-priority="1">Action</th>
									<th data-priority="1">&nbsp;</th>
							    </tr>
							  </thead>
							  
								<tbody id="DistributorTargetsTableBody">
									
									<%
									ResultSet rs3  = s.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where category_id = 1 and type_id = 2 order by package_label");
									while(rs3.next()){
										%>
										<tr>
												<td style="width: 30%">
													<%=rs3.getString("package_label")%>
													<input type="hidden" name="package_id_t2" id="package_id_t2" value="<%=rs3.getString("package_id")%>" >
												</td>
												
												<td style="width: 30%">
													<input type="text" name="qty_t2" id="qty_<%=rs3.getString("package_id")%>_t2" value="" data-mini="true" size="10" style="text-align: center" onkeyup="UpdateAnyField2(this.value, <%=rs3.getString("package_id")%>)" onchange="CheckQuantity2(0, <%=rs3.getString("package_id")%>)" >
												</td>
												
												<td style="width: 30%">
													<input type="button" value=" >> " data-mini="true" onclick="ToggleBrands('brand_table_<%=rs3.getString("package_id")%>_t2')" >
												</td>
												
												<td style="width: 30%; text-align: right">
													<table style="width: 100%; display: none" id="brand_table_<%=rs3.getString("package_id")%>_t2">
														<tr>
															<td nowrap style="width: 50%">Any</td>
															<td><input type="text" name="qty_<%=rs3.getString("package_id")%>_any_t2" id="qty_<%=rs3.getString("package_id")%>_any_t2" value="" data-mini="true" style="text-align: center; width: 100px" readonly="readonly"  tabindex="-1" ></td>
														</tr>
														
														<%
														ResultSet rs4 = s2.executeQuery("SELECT brand_id, brand_label FROM inventory_products_view where category_id=1 and type_id=2 and package_id="+rs3.getString("package_id"));
														while(rs4.next()){
															%>
																<tr>
																	<td nowrap style="width: 50%"><%=rs4.getString("brand_label")%></td>
																	<td>
																		<input type="text" name="qty_<%=rs3.getString("package_id")%>_t2" id="qty_<%=rs3.getString("package_id")%>_<%=rs4.getString("brand_id")%>_t2" value="" data-mini="true" style="text-align: center; width: 100px" onchange="CheckQuantity2(this.id, <%=rs3.getString("package_id")%>)" >
																		<input type="hidden" name="brand_id_<%=rs3.getString("package_id")%>_t2" id="brand_id_<%=rs3.getString("package_id")%>_t2" value="<%=rs4.getString("brand_id")%>" >
																	</td>
																</tr>
															<%
														}
														%>
														
													</table>
												</td>
												
											</tr>
										<%
									}
									%>
									
								</tbody>
							</table>
												
						</td>
						<td style="width: 50%">&nbsp;</td>
					</tr>
				</table>
				
			</li>
		
			
	    </ul>   
	    
	    
	</form> 
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
		<div>
			<table style="width: 100%;">
				<tr>
					<td>
						
						<table>
							<tr>
								<td><a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="DistributorTargetsSave" href="#" onClick="FormSubmit();">Save</a></td>
								<td><button data-icon="check" data-theme="b" data-inline="true" id="DistributorTargetsReset" onClick="javascript:window.location='SamplingPlannedSales.jsp'" >Reset</button></td>
								
							</tr>
						</table>
						
					</td>
					<td align="right">
	                    <!-- <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="DistributorTargetsSearch" >Search</a> -->
					</td>
	                
				</tr>
			</table>
		</div>
	    	
    </div>
    
    <jsp:include page="LookupOutletSearchPopup.jsp" > 
    	<jsp:param value="OutletSearchCallBackSamplingPlannedSale" name="CallBack" />
    	<jsp:param value="<%=FeatureID%>" name="OutletSearchFeatureID" />
    </jsp:include><!-- Include Outlet Search -->
</div>



<div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:600px; max-height: 600px" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >

			<form data-ajax="false" id="DistributorTargetsSearch" onSubmit="return showSearchContent()">
	            <table >
	            	<tr>
						<td astyle="width:20%">
							<input type="text" name="DistributorTargetsSearchDistributorID" id="DistributorTargetsSearchDistributorID" data-mini="true" placeholder="Distributor ID" onchange="getDistributorName()" size="12" >
							
						</td>
						<td astyle="width:60%">
							<input type="text" name="DistributorTargetsSearchDistributorName" id="DistributorTargetsSearchDistributorName" data-mini="true" placeholder="Distributor Name" readonly="readonly" >
						</td>
						<td astyle="width:10%"><input type="submit" value="Search" data-mini="true" ></td>
	                </tr>
	                 
	            </table>
	        </form>

	        <div id="SearchContent" style="padding: 5px">
			
				   
	        
	        </div>
            
        </div>
    </div>
</div>
</body>
</html>

<%
s.close();
ds.dropConnection();
%>