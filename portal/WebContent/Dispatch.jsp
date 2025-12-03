<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>




<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<script src="js/lookups.js"></script>
<script src="js/Dispatch.js?1=1"></script>


<%
long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 56;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect(Utilities.getAccessDeniedPageURL(request));
}

String EditID = "0";
String EditFromDate="";
String EditToDate="";
boolean isEditCase = false;

if( request.getParameter("DispatchID") != null ){
	EditID = request.getParameter("DispatchID");	
	isEditCase = true;
}


long DistributonID =0; //Utilities.parseLong(request.getParameter("DispatchMainDistributorID"));

//From Date
//int FromDateHour = Utilities.parseInt(request.getParameter("DispatchMainFromDateHour"));
//int FromDateMinutes = Utilities.parseInt(request.getParameter("DispatchMainFromDateMinutes"));
//Date FromDate = Utilities.parseDateTime(request.getParameter("DispatchMainFromDate"), FromDateHour, FromDateMinutes);
int InventorySalesTypeID = Utilities.parseInt(request.getParameter("InventorySalesType"));
String InventorySalesTypeIDWhere="";
if(InventorySalesTypeID!=0) //not for all
{
	InventorySalesTypeIDWhere = " and invs.type_id="+InventorySalesTypeID;
}



//To Date
//int ToDateHour = Utilities.parseInt(request.getParameter("DispatchMainToDateHour"));
//int ToDateMinutes = Utilities.parseInt(request.getParameter("DispatchtMainToDateMinutes"));
//Date ToDate = Utilities.parseDateTime(request.getParameter("DispatchtMainToDate"), ToDateHour, ToDateMinutes);

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();


Statement s = c.createStatement();
Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, 56);

int DispatchIDEdit=0;
String ByVehicle="";
String ByHand="";
long DriverIDEdit=0;
long VehicleIDEdit=0;
String VehicleTypeEdit="";
String PackageLabel ="";
String Units="";
Date CreatedOnEditCase = new java.util.Date();
int EditCaseExecute=0;
boolean IsSameDateForDelete=false;
int IsSpotSelling=0;
if(isEditCase)
{
	EditCaseExecute=1;
	//getting master table record
	ResultSet rss= s.executeQuery("select * from inventory_sales_dispatch where id="+EditID); 
	if(rss.first())
	{
		DispatchIDEdit = Utilities.parseInt(rss.getString("dispatch_type"));
		VehicleIDEdit = Utilities.parseLong(rss.getString("vehicle_id"));
		DriverIDEdit = Utilities.parseLong(rss.getString("driver_id"));
		CreatedOnEditCase = rss.getDate("created_on");
		IsSpotSelling = rss.getInt("is_spot_selling");
	}
	
	//checking for same day format
	if(DateUtils.isSameDay(new java.util.Date(), CreatedOnEditCase))
	{
		IsSameDateForDelete = true;
		//System.out.println("Same day");
	}
	
	//getting vehicle type
	
	ResultSet rsss= s.executeQuery("SELECT * FROM distribtuor_vehicles dv,distributor_vehicles_types dt where dv.type_id=dt.id and dv.id="+VehicleIDEdit); 
	if(rsss.first())
	{
		VehicleTypeEdit = rsss.getString("label");
	}
	if(DispatchIDEdit ==1)
	{
		ByVehicle="selected";		
	}
	else if(DispatchIDEdit ==2)
	{
		ByHand="selected";		
	}
}



%>
<% 
String DistributorName = "";
long DistributorID = 0;


if( UserDistributor != null ){
	if(UserDistributor.length>1) //if it has more than 1 distributor
	{
		response.sendRedirect("AccessDenied.jsp");
	}
	else
	{
		DistributorName = UserDistributor[0].DISTRIBUTOR_NAME;
		DistributorID = UserDistributor[0].DISTRIBUTOR_ID;
	}
}

%>

<div data-role="page" id="DispatchMain" data-url="DispatchMain" data-theme="d">

    
    <jsp:include page="Header3.jsp" >
    	<jsp:param value="Dispatch" name="title"/>

    		<jsp:param value="<%=DistributorName%>" name="HeaderValue"/>

    </jsp:include>

    
    <div data-role="content" data-theme="d">
    	 <form id="DispatchGenerateSalesForm" name="DispatchGenerateSalesForm" action="" method="POST" data-ajax="false">
    	 <input type="hidden" name="UniqueVoucherID" id="UniqueVoucherID" value="<%=Utilities.getUniqueVoucherID(SessionUserID)%>"/>
    		<input type="hidden" name="isEditCase" id="isEditCase" value="<%=EditCaseExecute%>"/>
    		<input type="hidden" name="DistributorIDD" id="DistributorIDD" value="<%=DistributorID%>"/>
    		<input type="hidden" name="EditIDForExecute" id="EditIDForExecute" value="<%=EditID%>"/>
    		<ul data-role="listview" data-inset="false" data-divider-theme="c">
    		
	        <li>
	       
	        	<table border="0" style="width: 100%">
					<tr>
						<td style="width: 20%;" >
							<select name="DispatchVehicleType" id="DispatchVehicleType" data-mini="true" onChange="DisableByHand()" >
							<option value="">Select Dispatch Type</option>
							<option value="1" <%=ByVehicle%>>By Vehicle</option>	
							<!-- <option value="2" <%=ByHand %>>By Hand</option> -->							
							</select>
						</td>
						<td style="width: 20%;" id="DispatchVehicleSelectTD">
							<select name="DispatchVehicleSelect" id="DispatchVehicleSelect" data-mini="true" onChange="ShowVehicleType()" >
							<option value="">Select Vehicle Number</option>
							<%
							ResultSet rs1 = s.executeQuery("SELECT * FROM distribtuor_vehicles dv,distributor_vehicles_types dvt where dv.type_id=dvt.id and distributor_id="+DistributorID);
							while(rs1.next())
							{
							%>
							<option value="<%=rs1.getString("id")+","+rs1.getString("label") %>"<%if(Utilities.parseLong(rs1.getString("id")) == VehicleIDEdit){ %>selected<%} %>><%=rs1.getString("vehicle_no")%></option>
							<%
							} 
							%>
							</select>
						</td>
						<td style="width: 10%;" class="ui-disabled"> 
						<input type="text" placeholder="Vehicle Type" name="VehicleType" id="VehicleType" readonly value="<%=VehicleTypeEdit%>"/>
						</td>
						<td style="width: 20%;" id="DistributorDriverNameTD">
							<select name="DistributorDriverName" id="DistributorDriverName" data-mini="true" >
							<option value="">Select Driver</option>
							<%
							ResultSet rs2 = s.executeQuery("SELECT * FROM distributor_employees where distributor_id="+DistributorID);
							while(rs2.next())
							{
							%>
								<option value="<%=rs2.getString("id")%>" <%if(Utilities.parseLong(rs2.getString("id"))==DriverIDEdit){ %>selected<%} %>><%=rs2.getString("name") %></option>
							<%
							}
							%>
							</select>
							
							
							
						</td>
					</tr>
					<tr>
					<td <%if(IsSpotSelling==1 && isEditCase){ %>class="ui-disabled"<%} %>>
						<input type="checkbox" name="SpotSellingCheckBx" id="SpotSellingCheckBx" class="custom" data-mini="true" onClick="SpotSellingCheckBoxClick()" <%if(IsSpotSelling==1){ %> checked <%} %>/>
							<label for="SpotSellingCheckBx">Spot Selling</label>
							<input type="hidden" name="SpotSellingCheckBxHidden" id="SpotSellingCheckBxHidden" value="0"/>
					</td>
					</tr>
				</table>
	        </li>
			<li data-role="list-divider">Invoices</li>
			<li data-role="fieldcontain">
			<div class="ui-grid-a">
			    <div class="ui-block-a" style="width:70%">
			    	<div class="ui-bar" style="min-height:60px">
			    			
						           <table data-role="table" id="SamplingSummary" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt; width:100%" >
									  <thead>
									    <tr class="ui-bar-c">
														
									    </tr>
									  </thead>
									  <tbody id="DispatchInvoicesList"> 
									  
									  <%
									  ResultSet rs=null;
									  String where="";
									  //if edit case
									  if(isEditCase)
									  {
										  where = " and invs.id in(select sales_id from inventory_sales_dispatch_invoices where id ="+EditID+")";
										 										  
									  }
									  else
									  {
										  where = " and invs.is_dispatched=0";
										  //where = " and invs.created_on between "+Utilities.getSQLFromDateTime(FromDate)+" and "+Utilities.getSQLToDateTime(ToDate);  
									  }
									  
									  //System.out.println("SELECT id,invs.distributor_id,outlet_id,(select outlet_name from outletmaster om where om.outlet_id=invs.outlet_id) outlet_name,(select bsi_name from outletmaster om where om.outlet_id=invs.outlet_id) bsi_name,invs.created_by,(select concat(u.first_name,' ',u.last_name) from users u where invs.created_by=u.id) created_by_name,invs.total_amount,invs.discount,invs.net_amount FROM inventory_sales invs where invs.distributor_id="+DistributorID+where);
									  
									  rs = s.executeQuery("SELECT id,brand_discount_amount,invs.distributor_id,IFNULL((select concat(first_name,' ',last_name) as booked_name1 from users u where u.id=invs.booked_by),'Desk Sale') as booked_named,outlet_id,(select name from common_outlets om where om.id=invs.outlet_id) outlet_name,'' bsi_name,invs.created_by,(select concat(u.first_name,' ',u.last_name) from users u where invs.created_by=u.id) created_by_name,invs.total_amount,invs.discount,invs.net_amount,invs.created_on FROM inventory_sales_invoices invs where invs.distributor_id="+DistributorID+where+" order by invs.booked_by,invs.created_on desc");
									 
									 // System.out.println("SELECT id,invs.distributor_id,IFNULL((select concat(first_name,' ',last_name) as booked_name1 from users u where u.id=invs.booked_by),'Desk Sale') as booked_named,outlet_id,(select outlet_name from outletmaster om where om.outlet_id=invs.outlet_id) outlet_name,(select bsi_name from outletmaster om where om.outlet_id=invs.outlet_id) bsi_name,invs.created_by,(select concat(u.first_name,' ',u.last_name) from users u where invs.created_by=u.id) created_by_name,invs.total_amount,invs.discount,invs.net_amount,invs.created_on FROM inventory_sales_invoices invs where invs.is_dispatched=0 and invs.distributor_id="+DistributorID+where+" order by invs.booked_by,invs.created_on desc");
									  int i=0;
									  String BookedBy="";
									  String IsNull="";
									  Date LastDate = Utilities.parseDate("01/01/1997");
									  while(rs.next())
									  {
										//System.out.println("Last Date "+LastDate);  
										//System.out.println("DB Date "+rs.getDate("created_on"));
									  %>
									  <tr>
									  <td colspan="6">
									  
										<table style="font-size: 10pt; width:100%" >
									  	<thead>	
									<%
										    
										  if(!BookedBy.equals(rs.getString("booked_named")))
										  {
											  %>
											   	<tr class="ui-bar-d">
													<th colspan="2" style="width:55%"><%=rs.getString("booked_named")%></th>	
													<th style="width:10%">Invoice#</th>
									    			<th style="width:15%">Created By</th>
									    			<th style="width:20%">Amount</th>													
									    		</tr>
										<%
										  }
												if(!DateUtils.isSameDay(LastDate, rs.getDate("created_on")))
											    {
													//System.out.println("Hello");
										%>
													<tr class="ui-bar-b" >
														<th colspan="5"><%=Utilities.getDisplayDateFormat(rs.getTimestamp("created_on"))%></th>														
										    			
										    			
										    		</tr>
											   
									<%	  	   }
										  
									  %>
									  	</thead>
								 		 </table>
									  </td>
									  </tr>
									  <tr >
									 
										  	<td <%if(isEditCase){%>class="ui-disabled"<%} %> style="width:10%;">
											  	<input type="hidden" name="InventorySalesID" id="InventorySalesID" value="<%=rs.getString("id")%>"/>
											  	<select name="DispatchSalesSelect" id="<%=rs.getString("id")%>" class="ClassDispatchSalesSelect" data-role="slider" data-mini="true">
									        		<option value="0"></option> <!-- default value 0 - unselected -->
									       		 	<option value="1"></option>
									    		</select>
										 	</td>
										  	<td style="width:45%"><%=rs.getString("outlet_id")+" - "+rs.getString("outlet_name")+" "+rs.getString("bsi_name")%><input type="hidden" name="distributor_id" value="<%=rs.getString("outlet_id")%>"/></td>									
											<td style="width:10%"><%=rs.getString("id")%></td>
											<td style="width:15%"><%=rs.getString("created_by")+" - "+rs.getString("created_by_name")%><input type="hidden" name="created_by" value="<%=rs.getString("created_by")%>"/></td>
											<!--  <td style="text-align:left;"style="text-align:right;"><%=Utilities.getDisplayCurrencyFormat(rs.getDouble("total_amount"))%><input type="hidden" name="total_amount" value="<%=rs.getString("total_amount")%>"/></td>
											<td ><%=rs.getString("discount")%><input type="hidden" name="discount" value="<%=rs.getString("discount")%>"/></td>-->
											<td style="text-align:left;width:20%;"><%=Utilities.getDisplayCurrencyFormat(rs.getDouble("net_amount") - rs.getDouble("brand_discount_amount"))%><input type="hidden" name="net_amount" value="<%=rs.getString("net_amount")%>"/></td>	
									  </tr>
									  <%
									  i++;
									  BookedBy=rs.getString("booked_named");
									  LastDate = rs.getDate("created_on");
									  }
									  %>
									  </tbody>
								  </table>
						       	        
					      
						
			    	</div>
			    </div>
			    <div class="ui-block-b" style="width:30%">
			    	<div class="ui-bar" style="min-height:60px">
			    		<div id="DispatchSalesSummary" style="position:fixed;width:24%">
					    			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
										<li data-role="list-divider">Summary</li>
										<li>
											<table border="0" cellspacing="0" style="font-size:13px; width:100%;"  data-mode="reflow" class="ui-responsive table-stroke">
		
												<thead>
													<tr>
														<th align="left">Product</th>
														<th align="right">Quantity</th>														
													</tr>
												</thead>
												<thead>
													<tr>
														<th colspan="4">&nbsp;</th>
													</tr>
												</thead>
												<tbody> 
												 	<%
												 	ResultSet rsSummaryEdit = s.executeQuery("SELECT ipv.package_id, ipv.package_label, ipv.unit_per_sku, sum(isp.total_units) units FROM inventory_sales_invoices_products isp, inventory_products_view ipv where isp.product_id = ipv.product_id and isp.id IN(select sales_id from inventory_sales_dispatch_invoices where id ="+EditID+") group by ipv.package_id, ipv.package_label");
												 	while(rsSummaryEdit.next())
												 	{
												 	%>
													 	<tr>
															<td><%=rsSummaryEdit.getString("package_label") %></td>
															<td style="text-align:right;"><%=Utilities.convertToRawCases(rsSummaryEdit.getLong("units"), rsSummaryEdit.getInt(3))  %></td>
													    </tr>
												    <%
												 	}
												    %>
												 </tbody>
											</table>
										</li>
									</ul>
					    			
					    			
			    		</div>
			    	</div>
			    </div>
			</div><!-- /grid-a -->
			 </li>
		</ul>
		</form>
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
		<div>
			
			<table style="width:100%">
			<tr>
			<%if(!isEditCase){ %>
			<td  style="width:5%" id="LiftingReportGenerateButtonTD">
			<button data-icon="check" data-theme="a" data-inline="true" id="LiftingReportGenerateButton" onClick="DispatchSalesSubmit();">Dispatch</button>
			
			</td>
			<%} %>
			<td  style="width:5%" id="DispatchDeleteButtonTD">
			<button data-icon="check" data-theme="b" data-inline="true" id="DispatchDeleteButtonButton" onClick="DispatchDeleteubmit();">Cancel Selected Invoices</button>
			
			</td>
			<%
			if(isEditCase)
			{ 
				%>
					<td style="width:5%" >
						<button data-icon="check" data-theme="a" data-inline="true"  id="DispatchSalesUpdateButton" onClick="DispatchSalesUpdate();" >Update</button>
					</td>
				<%
			}
			%>
			
			<%
			//System.out.println(EditID);
			ResultSet rss = s.executeQuery("select * from inventory_sales_dispatch where is_empty_returned=0 and is_liquid_returned=0 and id="+EditID);
			if(rss.first())
			{			
			
				if(isEditCase)
				{ 
			%>
				<td style="width:5%" id="DispatchSalesDeleteTD">
					<button data-icon="check" data-theme="a" data-inline="true"  id="DispatchSalesDeleteButton" onClick="DeleteDispatchSales();" >Delete</button>
				</td>
			<%
				}
			}
			%>
			<td align="right">
                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="DispatchSearch" >Search</a>
				</td>
			</tr>
			</table>
		</div>  
		
		<div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:600px; max-height: 600px" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >

			<form data-ajax="false" id="DeskSaleFormDateRange" onSubmit="return showSearchContent()">
            <table>
            	<tr>
                	<td>
                        <span id="FromDateSpan"><input type="text" data-mini="true" name="DispatchSaleFromDate" id="DispatchSaleFromDate" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateFormat(new java.util.Date())%>"></span>
                    
                    </td>
                    <td>
                    
						<span id="ToDateSpan"><input type="text" data-mini="true" name="DispatchSaleToDate" id="DispatchSaleToDate" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateFormat(new java.util.Date())%>" ></span>
                    
                    </td>
                    <td>
                    	<button data-role="button" data-icon="search" id="DeskSaleDateButton" data-iconpos="notext" data-theme="c" data-inline="true" data-corners="false" onClick="showSearchContent();"></button>
                    </td>
                </tr>
            </table>
        </form>

        <div id="SearchContent">
        </div>
            
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