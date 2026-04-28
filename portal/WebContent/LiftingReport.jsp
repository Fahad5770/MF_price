<%@page import="org.omg.CORBA.portable.RemarshalException"%>
<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<script src="js/LiftingReport.js"></script>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(37, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}


Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();


String params = request.getParameter("params");

int Region = Utilities.parseInt(request.getParameter("LiftingReportMainRegion"));
long Distributor = Utilities.parseLong(request.getParameter("LiftingReportMainDistributor"));
int Snd = Utilities.parseInt(request.getParameter("LiftingReportMainSnd"));
long InvoiceNumber = Utilities.parseLong(request.getParameter("LiftingReportMainInvoiceNumber"));

int PaymentMethod = Utilities.parseInt(request.getParameter("LiftingReportMainPaymentMethod"));
long SaleOrderNumber = Utilities.parseLong(request.getParameter("LiftingReportMainSaleOrderNumber"));
String VehicleNumber = Utilities.filterString(request.getParameter("LiftingReportMainVehicleNumber"), 1, 100);

int VehicleType = Utilities.parseInt(request.getParameter("LiftingReportMainVehicleType"));
String Remakrs = Utilities.filterString(request.getParameter("LiftingReportMainRemakrs"), 1, 100);
String BatchCode = Utilities.filterString(request.getParameter("LiftingReportBatchCode"), 1, 100);
long UserSAPCode = Utilities.parseLong(request.getParameter("LiftingReportUser"));

int PackageID = Utilities.parseInt(request.getParameter("LiftingReportPackage"));
int BrandID = Utilities.parseInt(request.getParameter("LiftingReportBrand"));

/*
String Status = Utilities.filterString(request.getParameter("LiftingReportStatus"), 1, 100); 
*/
int FromDateHour = Utilities.parseInt(request.getParameter("LiftingReportMainFromDateHour"));
int FromDateMinutes = Utilities.parseInt(request.getParameter("LiftingReportMainFromDateMinutes"));
Date FromDate = Utilities.parseDateTime(request.getParameter("LiftingReportMainFromDate"), FromDateHour, FromDateMinutes);

int ToDateHour = Utilities.parseInt(request.getParameter("LiftingReportMainToDateHour"));
int ToDateMinutes = Utilities.parseInt(request.getParameter("LiftingReportMainToDateMinutes"));
Date ToDate = Utilities.parseDateTime(request.getParameter("LiftingReportMainToDate"), ToDateHour, ToDateMinutes);

int WarehouseID = Utilities.parseInt(request.getParameter("LiftingReportMainWarehouseID"));

String url = "LiftingReport.jsp?LiftingReportMainFromDate="+request.getParameter("LiftingReportMainFromDate")+"&LiftingReportMainFromDateHour="+FromDateHour+"&LiftingReportMainFromDateMinutes="+FromDateMinutes+"&LiftingReportMainToDate="+request.getParameter("LiftingReportMainToDate")+"&LiftingReportMainToDateHour="+ToDateHour+"&LiftingReportMainToDateMinutes="+ToDateMinutes+"&LiftingReportMainRegion="+Region+"&LiftingReportMainSnd="+Snd+"&LiftingReportMainInvoiceNumber="+InvoiceNumber+"&LiftingReportMainPaymentMethod="+PaymentMethod+"&LiftingReportMainSaleOrderNumber="+SaleOrderNumber+"&LiftingReportMainVehicleNumber="+VehicleNumber+"&LiftingReportMainVehicleType="+VehicleType+"&LiftingReportMainRemakrs="+Remakrs+"&LiftingReportBatchCode="+BatchCode+"&LiftingReportUser="+UserSAPCode+"&LiftingReportPackage="+PackageID+"&LiftingReportBrand="+BrandID+"&LiftingReportMainWarehouseID="+WarehouseID;
 
%>

<div data-role="header" data-id="LiftingHeader" data-theme="d">
    <h1>LIFTING REPORT</h1>
    <a href="LiftingReportMain.jsp" data-role="button" data-theme="d" data-inline="true" data-ajax="false" data-icon="back">Back</a>
    
</div>

<div data-role="content" data-theme="d">

<table border=0 style="width:100%">
	<tr>
		<td style="width:30%" valign="top">
		
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
				<li data-role="list-divider">Distributor's Converted Cases</li>
				
				<%
				
				String whereWithoutDistributor = "";
	            
				if(Region > 0){
	            	whereWithoutDistributor += " and cd.region_id = "+Region;
	            }
				if(Snd > 0){
	            	whereWithoutDistributor += " and cd.snd_id = "+Snd;
	            }
	            if(InvoiceNumber > 0){
	            	whereWithoutDistributor += " and idn.invoice_no = "+InvoiceNumber;
	            }
	            if(PaymentMethod > 0){
	            	whereWithoutDistributor += " and idn.payment_method = "+PaymentMethod;
	            }
	            if(SaleOrderNumber > 0){
	            	whereWithoutDistributor += " and idn.sap_order_no = "+SaleOrderNumber;
	            }
	            if( VehicleNumber != null && !VehicleNumber.equals("") && VehicleNumber.length() > 0){
	            	whereWithoutDistributor += " and idn.vehicle_no = "+VehicleNumber;
	            }
	            if(VehicleType > 0){
	            	whereWithoutDistributor += " and idn.vehicle_type = "+VehicleType;
	            }
	            if( Remakrs != null && !Remakrs.equals("") && Remakrs.length() > 0){
	            	whereWithoutDistributor += " and idn.remarks like '%"+Remakrs+"%'";
	            }
	            if( BatchCode != null && !BatchCode.equals("") && BatchCode.length() > 0){
	            	whereWithoutDistributor += " and idnp.batch_code like '%"+BatchCode+"%'";
	            }
	            if(UserSAPCode > 0){
	            	whereWithoutDistributor += " and idn.created_by = "+UserSAPCode;
	            }
	            if(PackageID > 0){
	            	whereWithoutDistributor += " and ip.package_id = "+PackageID;
	            } 
	            if(BrandID > 0){
	            	whereWithoutDistributor += " and ip.brand_id = "+BrandID;
	            }
	            if(WarehouseID > 0){
	            	whereWithoutDistributor += " and idn.warehouse_id = "+WarehouseID;
	            }
	            int FeatureID = 37;
	            
	            //getting user regions
	            
	            			
				String RegionIds = UserAccess.getRegionQueryString(UserAccess.getUserFeatureRegion(SessionUserID, FeatureID));			
				
				String WarehouseIds = UserAccess.getWarehousesQueryString(UserAccess.getUserFeatureWarehouse(SessionUserID, FeatureID));				
				
				String DistributorsIds = UserAccess.getDistributorQueryString(UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID));

				
				whereWithoutDistributor += " and idn.warehouse_id in ("+WarehouseIds+")";
				
				whereWithoutDistributor += " and cd.region_id in ("+RegionIds+")";
				
				whereWithoutDistributor += " and idn.distributor_id in ("+DistributorsIds+")";
				
				
				double TotalDistributorConvertedCases = 0;
				String sql2 = "select idn.distributor_id, (select name from common_distributors where distributor_id=idn.distributor_id) distributor_name, ip.category_id,ip.package_id, ipa.label, ipa.unit_per_case, sum(idnp.total_units)  bottles, sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and idn.created_on between "+Utilities.getSQLFromDateTime(FromDate)+" and "+Utilities.getSQLToDateTime(ToDate)+" and ip.category_id = 1 "+whereWithoutDistributor+" and idn.is_delivered = 1 group by idn.distributor_id order by converted_cases desc";
				//System.out.println(sql2);
				ResultSet rs_distributor = s.executeQuery(sql2);
				while(rs_distributor.next()){
					TotalDistributorConvertedCases += rs_distributor.getDouble("converted_cases");
					%>
					
					<li><a data-ajax="false" href="<%=url+"&LiftingReportMainDistributor="+rs_distributor.getString("idn.distributor_id")%>" style="font-size:13px"><%=rs_distributor.getString("distributor_name")%> <span class="ui-li-count" style="font-size:13px"><%=Math.round(rs_distributor.getDouble("converted_cases"))%></span></a></li>
					
					<%
				}
				%>
				
				<li><a data-ajax="false" href="<%=url+"&LiftingReportMainDistributor=0"%>" style="font-size:13px">All<span class="ui-li-count" style="font-size:13px"><%=Utilities.getDisplayCurrencyFormat(TotalDistributorConvertedCases)%></span></a></li>
				
			</ul>
		
		</td>
		<td style="width:70%" valign="top">
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="b">
				<li data-role="list-divider">Package Summary</li>
				<li>
				<table border=0 style="font-size:13px; width:100%"  data-role="table" id="SamplingSummary" data-mode="reflow" class="ui-responsive table-stroke">
					  <thead>
					    <tr>
					    	
							
							<th data-priority="2" >&nbsp;</th>
							
							<th data-priority="1"  style="text-align:right">Raw Cases</th>
							<th data-priority="persist"  style="text-align:right">Converted Cases</th>
							
					    </tr>
					  </thead>
			
			            <%
			            
			            String where = "";
			            
			            if(Region > 0){
			            	where += " and cd.region_id = "+Region;
			            }
			            if(Snd > 0){
			            	where += " and cd.snd_id = "+Snd;
			            }
			            if(Distributor > 0){
			            	where += " and idn.distributor_id = "+Distributor;
			            }
			            if(InvoiceNumber > 0){
			            	where += " and idn.invoice_no = "+InvoiceNumber;
			            }
			            if(PaymentMethod > 0){
			            	where += " and idn.payment_method = "+PaymentMethod;
			            }
			            if(SaleOrderNumber > 0){
			            	where += " and idn.sap_order_no = "+SaleOrderNumber;
			            }
			            if( VehicleNumber != null && !VehicleNumber.equals("") && VehicleNumber.length() > 0){
			            	where += " and idn.vehicle_no = "+VehicleNumber;
			            }
			            if(VehicleType > 0){
			            	where += " and idn.vehicle_type = "+VehicleType;
			            }
			            if( Remakrs != null && !Remakrs.equals("") && Remakrs.length() > 0){
			            	where += " and idn.remarks like '%"+Remakrs+"%'";
			            }
			            if( BatchCode != null && !BatchCode.equals("") && BatchCode.length() > 0){
			            	where += " and idnp.batch_code like '%"+BatchCode+"%'";
			            }
			            if(UserSAPCode > 0){
			            	where += " and idn.created_by = "+UserSAPCode;
			            }
			            if(PackageID > 0){
			            	where += " and ip.package_id = "+PackageID;
			            } 
			            if(BrandID > 0){
			            	where += " and ip.brand_id = "+BrandID;
			            }
			            if(WarehouseID > 0){
			            	where += " and idn.warehouse_id = "+WarehouseID;
			            }
			            
			            where += " and idn.warehouse_id in ("+WarehouseIds+")";
						
			            where += " and cd.region_id in ("+RegionIds+")";
						
			            where += " and idn.distributor_id in ("+DistributorsIds+")";
			            
			            double TotalConvertedCases = 0;
			            
			            String Sql1 = "select ip.category_id,ip.package_id, ipa.label, ipa.unit_per_case, sum(idnp.total_units)  bottles, sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and idn.created_on between "+Utilities.getSQLFromDateTime(FromDate)+" and "+Utilities.getSQLToDateTime(ToDate)+" and ip.category_id = 1 "+where+" and idn.is_delivered = 1 group by ip.category_id, ip.package_id order by ip.category_id, ipa.sort_order";
			           // System.out.println(Sql1);
			            ResultSet rs1 = s.executeQuery(Sql1);
			            while(rs1.next()){
			            	
			            	TotalConvertedCases += rs1.getDouble("converted_cases");
			            	
			                %>
			                	<tr style="background:#ececec">
			   	            		<td align="left"><%=rs1.getString("ipa.label")%></td>
			   	            		<td style="text-align:right; padding-right:10px"><b><%=Utilities.convertToRawCases(rs1.getLong("bottles"), rs1.getInt("ipa.unit_per_case"))%></b></td>
									<td style="text-align:right; padding-right:10px"><b><%=Utilities.getDisplayCurrencyFormat(rs1.getDouble("converted_cases"))%></b></td>
			   	            	</tr>
			                <%
			                
			                
							String Sql = "select ip.category_id, ip.package_id, ipa.label, ip.brand_id, ib.label, ip.unit_per_sku, sum(idnp.total_units) bottles, sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and idn.created_on between "+Utilities.getSQLFromDateTime(FromDate)+" and "+Utilities.getSQLToDateTime(ToDate)+" and ip.category_id = 1 "+where+" and ip.package_id="+rs1.getInt("ip.package_id")+" and idn.is_delivered = 1  group by ip.category_id, ip.package_id, ip.brand_id order by ip.category_id, ip.package_id, ip.brand_id";
			                //System.out.println(Sql);
			                ResultSet rs2 = s2.executeQuery(Sql);
			                while( rs2.next() ){
			                	
			                	%>
			                	
			                	<tr>
			    					<td style="padding-left:20px"><%=rs2.getString("ib.label")%></td>
			    					<td style="text-align:right; padding-right:10px"><%=Utilities.convertToRawCases( rs2.getLong("bottles"), rs2.getInt("ip.unit_per_sku"))%></td>
			    					<td style="text-align:right; padding-right:10px"><%= Utilities.getDisplayCurrencyFormat( rs2.getDouble("converted_cases"))%></td>
			    		    	</tr>
			                	
			                	<%
			                	
			                }
			               
			            }
			            
			            
			            %>
			            
			            
			                <tr style="background:#ececec">
			                	<td colspan="2">&nbsp;</td>
			                	<td style="text-align:right; padding-right:10px"><b><%=Utilities.getDisplayCurrencyFormat(TotalConvertedCases)%></b></td>
			                </tr>
			            
			            
			</table>
			</li>
			</ul>
		</td>
	</tr>
</table>


	



</div>