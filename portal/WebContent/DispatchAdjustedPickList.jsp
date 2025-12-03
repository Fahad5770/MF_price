<%@page import="com.pbc.util.NumberToWords"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="org.apache.commons.lang3.text.WordUtils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>

<style>

.formattedRow{
	border-bottom:1px solid #000;
	padding-top:5px;
	padding-bottom:5px;

}

</style>

<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 61;

long DistributorID = 0;
String DistributorName = "";


if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}else{
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);


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
	
}

Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();


long DispatchID = 0;
if( request.getParameter("DispatchID") != null ){
	DispatchID = Utilities.parseLong(request.getParameter("DispatchID"));
}

Statement s = c.createStatement();
Statement s2 = c.createStatement();

String DriverName = "";
String VehicleName = "";
String UVID = "";
int DispatchTypeIDD11=0;
ResultSet rs1 = s.executeQuery("SELECT uvid, vehicle_id, (SELECT vehicle_no FROM distribtuor_vehicles where id=vehicle_id) vehicle_name, driver_id, (SELECT name FROM distributor_employees where id=driver_id) driver_name,dispatch_type FROM inventory_sales_dispatch where id="+DispatchID);
if(rs1.first()){
	DriverName = rs1.getString("driver_name");
	VehicleName = rs1.getString("vehicle_name");
	UVID = rs1.getString("uvid");
	DispatchTypeIDD11 = rs1.getInt("dispatch_type");
}

%>
<html>

<head>

</head>
<body style="font-family:Helvetica,Arial,sans-serif">

		<div style="width: 8.27in; border: solid 1px; margin-top: 5px; page-break-after: always">
              
              <table border="0" style="width: 100%">
              	<tr>
                	<td align="left" width="1"><img src="images/logo.svg" style="width: 30px"></td>
                    <td align="left" valign="middle" style="font-weight:600"><%=Utilities.getCompanyName()%></td>
                    <td align="right" style="padding-right: 20px"><b>Pick List</b><br><span style="font-weight: normal; font-size: 10px">(Adjusted)</span></td>
                </tr>
              </table>
              
			  <table border="0" style="width: 100%; margin-top: 5px; padding-left: 20px;padding-right: 20px">
                <tr>
                	<td style="width: 20%;"><img src="/barbecue/barcode?data=<%=UVID%>&height=20&type=Code128&headless=false&drawText=yes"></td>
                    <td style="text-align: center;font-weight: 700;">&nbsp;</td>
                    <td style="width: 20%;text-align: right;" nowrap="nowrap"><%= Utilities.getDisplayDateTimeFormat(new Date()) %></td>
                </tr>
                <tr>
                	<td style="text-align:center">
                		
                		
                		<table style="width: 100%; font-size: 12px;">
			  				<tr>
			  					<td align="left" style="width: 33%; padding-left: 19px">Dispatch# <%= DispatchID %></td>
			  					<td align="right" style="width: 64%; padding-right: 20px"><%=UVID%></td>
			  				</tr>
			  			</table>
                		
                	</td>
                	<td>&nbsp;</td>
                	<td>&nbsp;</td>
                </tr>
                
              </table>
			  	<hr />
			  	
		  		<table style="width: 95%; margin-bottom:10px" border="0" align="center">
		  			<tr>
		  				<td style="width: 49%" valign="top">
		  					<div align="left" style="padding-left:0px;font-weight: 700; margin-bottom:5px; font-size: 15px"><b>Products</b></div>
			  				<table cellspacing="0" cellpadding="0" align="left" border=0 style="width:100%; margin-bottom:10px; font-size: 12px"  data-role="table" data-mode="reflow" class="ui-responsive table-stroke">
									  <!-- <thead>
									    <tr>
									    	
											
											<th data-priority="2" >&nbsp;</th>
											
											<th data-priority="1"  style="text-align:right">Quantity</th>
											
											
									    </tr>
									  </thead>
									 -->
								<%
									long TotalRawCases = 0;
									long TotalBottles = 0;
									ResultSet rs3 = s.executeQuery("SELECT isip.id, isip.product_id, ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, sum(isip.total_units) bottles, ipv.unit_per_sku FROM inventory_sales_adjusted_products isip, inventory_products_view ipv where isip.product_id=ipv.product_id and isip.id in (select sales_id from (select sales_id from inventory_sales_dispatch_invoices where id="+DispatchID+" union SELECT sales_id FROM inventory_sales_dispatch_extra_invoices where id = "+DispatchID+") tab1) group by ipv.package_id order by ipv.package_sort_order");
									while(rs3.next()){
										
										TotalRawCases += Utilities.getRawCasesAndUnits(rs3.getLong("bottles"), rs3.getInt("unit_per_sku"))[0];
										TotalBottles += Utilities.getRawCasesAndUnits(rs3.getLong("bottles"), rs3.getInt("unit_per_sku"))[1];
										
										%>
										<tr style="background:#ececec">
					   	            		<td align="left"><%=rs3.getString("package_label")%></td>
					   	            		<td style="text-align:right; padding-right:10px"><b><%=Utilities.convertToRawCases(rs3.getLong("bottles"), rs3.getInt("unit_per_sku"))%></b></td>
					   	            	</tr>
										<%
										
										ResultSet rs4 = s2.executeQuery("SELECT isip.id, isip.product_id, ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, sum(isip.total_units) bottles, ipv.unit_per_sku FROM inventory_sales_adjusted_products isip, inventory_products_view ipv where isip.product_id=ipv.product_id and isip.id in (select sales_id from (select sales_id from inventory_sales_dispatch_invoices where id="+DispatchID+" union SELECT sales_id FROM inventory_sales_dispatch_extra_invoices where id = "+DispatchID+") tab1) and ipv.package_id="+rs3.getString("package_id")+" group by ipv.brand_id order by ipv.brand_label");
										while(rs4.next()){
											%>
											
											<tr>
						    					<td style="padding-left:50px; border-bottom:1px solid #000;"><%=rs4.getString("brand_label")%></td>
						    					<td style="text-align:right; padding-right:50px; border-bottom:1px solid #000;"><%=Utilities.convertToRawCases( rs4.getLong("bottles"), rs4.getInt("unit_per_sku"))%></td>
						    		    	</tr>
											
											<%
										}
										
									}
									
									%>
									
									<tr style="background:#ececec">
				   	            		<td align="left">Total</td>
				   	            		<td style="text-align:right; padding-right:10px"><b><%=Utilities.formatCasesAndUnits(TotalRawCases, TotalBottles)%></b></td>
				   	            	</tr>
									
									<%
								%>
								
							</table>
		  				
		  				</td>
		  				<td style="width: 2%">&nbsp;</td>
		  				<td style="width: 49%" valign="top">
		  					
		  					<table style="width: 100%; font-size: 12px;">
		  						
		  						<tr>
		  							<td><b>Driver Name</b></td>
		  							<td><%if(DispatchTypeIDD11!=2){%><%=DriverName%><%} %></td>
		  							<td><b>Vehicle No</b></td>
		  							<td><%if(DispatchTypeIDD11 !=2){%><%=VehicleName%><%}else{ %>By Hand<%} %></td>
		  						</tr>
		  						
		  						<tr>
		  							<td><b>Distributor</b></td>
		  							<td colspan="3"><%=DistributorID+"-"+DistributorName%></td>
		  						</tr>
		  						
		  						<tr>
		  							<td><b>Remarks</b></td>
		  						</tr>
		  						<tr>
		  							<td colspan="4" style="border-bottom: 1px solid #000;">&nbsp;</td>
		  						</tr>
		  						<tr>
		  							<td colspan="4" style="border-bottom: 1px solid #000;">&nbsp;</td>
		  						</tr>
		  					</table>
		  					
		  				</td>
		  				
		  			</tr>
		  		</table>
				  
			  	</div>
			  	<div style="width: 8.27in; border: solid 1px; margin-top: 5px; ">
			  	
			  	<table border="0" style="width: 100%">
	              	<tr>
	                	<td align="left" width="1"><img src="images/logo.svg" style="width: 30px"></td>
	                    <td align="left" valign="middle" style="font-weight:600"><%=Utilities.getCompanyName()%></td>
	                    <td align="right" style="padding-right: 20px"><b>Pick List</b><br><span style="font-weight: normal; font-size: 10px">(Adjusted)</span></td>
	                </tr>
	            </table>
	              
				<table style="width: 100%; margin-top: 5px; padding-left: 20px;padding-right: 20px">
	                <tr>
	                	<td style="width: 20%;"><img src="/barbecue/barcode?data=<%=UVID%>&height=20&type=Code128&headless=false&drawText=yes"></td>
	                    <td style="text-align: center;font-weight: 700;">&nbsp;</td>
	                    <td style="width: 20%;text-align: right;" nowrap="nowrap"><%= Utilities.getDisplayDateTimeFormat(new Date()) %></td>
	                </tr>
	                <tr>
	                	<td style="text-align:center">
	                		<table style="width: 100%; font-size: 12px;">
				  				<tr>
				  					<td align="left" style="width: 33%; padding-left: 19px">Dispatch# <%= DispatchID %></td>
				  					<td align="right" style="width: 64%; padding-right: 20px"><%=UVID%></td>
				  				</tr>
				  			</table>
	                	</td>
	                	<td>&nbsp;</td>
	                	<td>&nbsp;</td>
	                </tr>
	            </table>
			  	<hr />
			  	<div align="left" style="padding-left:20px;font-weight: 700; margin-bottom:5px; font-size: 15px"><b>Invoices</b></div>
			  	
			  	<table align="center" style="width: 95%; font-size:12px; margin-bottom:10px" border="0" cellspacing="0">
			  		
			  		<thead>
			  			<tr>
			  				<th align="left" nowrap="nowrap" style="width: 0%">Sr #</th>
			  				<th align="left" nowrap="nowrap" style="width: 0%">Inv #</th>
			  				<th align="left" nowrap="nowrap" style="width: 20%; padding-left:5px">Outlet</th>
			  				<th align="left" nowrap="nowrap" style="width: 20%; padding-left:5px">Address</th>
			  				<th align="left" nowrap="nowrap" style="width: 10%; padding-left:5px">Person</th>
			  				<th align="left" nowrap="nowrap" style="width: 0%; padding-left:5px">Phone</th>
			  				<th align="left" nowrap="nowrap" style="width: 0%; padding-left:5px">Dispatched</th>
			  				<th align="left" nowrap="nowrap" style="width: 0%; padding-left:5px">Adjusted</th>
			  				<th align="left" nowrap="nowrap" style="width: 10%; padding-left:5px">Received</th>
			  				<th align="left" nowrap="nowrap" style="width: 20%; padding-left:5px">Remarks</th>
			  			</tr>
			  		</thead>
			  		
			  	

		  <%
		  
		  String SQLMain = "";
		  
		  SQLMain = "SELECT isi.id, isi.created_on, isi.outlet_id, concat(om.Outlet_Name, ' ', om.Bsi_Name) as outlet_name, om.address as outlet_address, om.Owner as owner, om.Telepohone as telephone, isi.net_amount, (SELECT net_amount FROM inventory_sales_invoices where id=isi.id) dispatched_net_amount FROM inventory_sales_adjusted isi, outletmaster om where isi.outlet_id=om.Outlet_ID and (isi.id in (select sales_id from inventory_sales_dispatch_invoices where id="+DispatchID+") or isi.id in (SELECT sales_id FROM inventory_sales_dispatch_extra_invoices where id = "+DispatchID+")) order by isi.id";
		  
		  //System.out.println(SQLMain);
		  int counter = 1;
		  double TotalRowAmount = 0;
		  double TotalDispatchRowAmount = 0;
		  ResultSet rs = s.executeQuery(SQLMain);
		  while (rs.next()){
			 
			  int InvoiceID = rs.getInt("id");
			  long OutletID = rs.getLong("outlet_id");
			  String OutletName = rs.getString("outlet_name");
			  String OutletAddress = rs.getString("outlet_address");
			  String Owner = rs.getString("owner");
			  String Telephone = rs.getString("telephone");
			  double NetAmount = rs.getDouble("net_amount");
			  double DispatchedNetAmount = rs.getDouble("dispatched_net_amount");
			  
			  TotalRowAmount += NetAmount;
			  TotalDispatchRowAmount += DispatchedNetAmount;
			  %>
              
		  			<tr style="height: 50px">
		  				<td valign="bottom" style="padding-left:5px; border-bottom: 1px solid #ccc"><%=counter%></td>
		  				<td valign="bottom" style="padding-left:5px; border-bottom: 1px solid #ccc"><%=InvoiceID%></td>
		  				<td valign="bottom" style="padding-left:5px; border-bottom: 1px solid #ccc"><%="<b>"+OutletID+"</b>-"+OutletName%></td>
		  				<td valign="bottom" style="padding-left:5px; border-bottom: 1px solid #ccc"><%=OutletAddress%></td>
		  				<td valign="bottom" style="padding-left:5px; border-bottom: 1px solid #ccc"><%=Owner%></td>
		  				<td valign="bottom" style="padding-left:5px; border-bottom: 1px solid #ccc"><%=Telephone%></td>
		  				<td valign="bottom" align="right" style="padding-left:5px; border-bottom: 1px solid #ccc; border-left: 1px solid #ccc; border-right: 1px solid #ccc"><%=Utilities.getDisplayCurrencyFormat(DispatchedNetAmount)%></td>
		  				<td valign="bottom" align="right" style="padding-left:5px; border-bottom: 1px solid #ccc; border-left: 1px solid #ccc; border-right: 1px solid #ccc"><%=Utilities.getDisplayCurrencyFormat(NetAmount)%></td>
		  				<td valign="bottom" align="left" style="padding-left:5px; border-bottom: 1px solid #ccc">&nbsp;</td>
		  				<td valign="bottom" align="left" style="padding-left:5px; border-bottom: 1px solid #ccc">&nbsp;</td>
		  			</tr>
			  	
				<%
				counter++;
				}
				rs.close();
				%>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
		  				<td valign="bottom" style="padding-left:5px">&nbsp;</td>
		  				<td valign="bottom" style="padding-left:5px">&nbsp;</td>
		  				<td valign="bottom" style="padding-left:5px">&nbsp;</td>
		  				<td valign="bottom" style="padding-left:5px">&nbsp;</td>
		  				<td valign="bottom" style="padding-left:5px">&nbsp;</td>
		  				<td valign="bottom" style="padding-left:5px">&nbsp;</td>
		  				<td valign="bottom" align="right" style="padding-left:5px; border: 1px solid #000"><b><%=Utilities.getDisplayCurrencyFormat(TotalDispatchRowAmount)%></b></td>
		  				<td valign="bottom" align="right" style="padding-left:5px; border: 1px solid #000"><b><%=Utilities.getDisplayCurrencyFormat(TotalRowAmount)%></b></td>
		  				<td valign="bottom" align="left" style="padding-left:5px">&nbsp;</td>
		  				<td valign="bottom" align="left" style="padding-left:5px">&nbsp;</td>
		  			</tr>
				
		  </table>
		  
		  
	</div>
</body>
</html>
<%


s2.close();
s.close();
ds.dropConnection();
%>