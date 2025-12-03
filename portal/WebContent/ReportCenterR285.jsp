<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.ArrayList"%>
<script>

function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}

</script>

<style>
td{
font-size: 8pt;
}

</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 355;

//if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	//response.sendRedirect("AccessDenied.jsp");
//}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();
Statement s5 = c.createStatement();
Statement s6 = c.createStatement();

Statement s7 = c.createStatement();
Statement s8 = c.createStatement();

//Date date = Utilities.parseDate(request.getParameter("Date"));


Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");

if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	StartDate = new Date();
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}

//out.print("StartDate = "+StartDate);
//out.print("EndDate = "+EndDate);


long SelectedPackagesArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPackages") != null){
   	SelectedPackagesArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPackages");           	
}

String PackageIDs = "";
String WherePackage = "";

if(SelectedPackagesArray!= null && SelectedPackagesArray.length > 0){
	for(int i = 0; i < SelectedPackagesArray.length; i++){
		if(i == 0){
			PackageIDs += SelectedPackagesArray[i]+"";
		}else{
			PackageIDs += ", "+SelectedPackagesArray[i]+"";
		}
	}
	WherePackage = " and package_id in ("+PackageIDs+") ";
}

long SelectedBrandsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedBrands") != null){
   	SelectedBrandsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedBrands");           	
}

String BrandIDs = "";
String WhereBrand = "";

if(SelectedBrandsArray!= null && SelectedBrandsArray.length > 0){
	for(int i = 0; i < SelectedBrandsArray.length; i++){
		if(i == 0){
			BrandIDs += SelectedBrandsArray[i]+"";
		}else{
			BrandIDs += ", "+SelectedBrandsArray[i]+"";
		}
	}
	WhereBrand = " and brand_id in ("+BrandIDs+") ";
}

//HOD


String HODIDs="";
long SelectedHODArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedHOD") != null){
	SelectedHODArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedHOD");
	HODIDs = Utilities.serializeForSQL(SelectedHODArray);
}

String WhereHOD = "";
if (HODIDs.length() > 0){
	WhereHOD = " and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
}


//RSM


String RSMIDs="";
long SelectedRSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRSM") != null){
	SelectedRSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRSM");
	RSMIDs = Utilities.serializeForSQL(SelectedRSMArray);
}

String WhereRSM = "";
if (RSMIDs.length() > 0){
	WhereRSM = " and distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
}


//SM


String SMIDs="";
long SelectedSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedSM") != null){
	SelectedSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedSM");
	SMIDs = Utilities.serializeForSQL(SelectedSMArray);
}

String WhereSM = "";
if (SMIDs.length() > 0){
	WhereSM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
}

//TDM


String TDMIDs="";
long SelectedTDMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedTDM") != null){
	SelectedTDMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedTDM");
	TDMIDs = Utilities.serializeForSQL(SelectedTDMArray);
}

String WhereTDM = "";
if (TDMIDs.length() > 0){
	WhereTDM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
}

//ASM


String ASMIDs="";
long SelectedASMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedASM") != null){
	SelectedASMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedASM");
	ASMIDs = Utilities.serializeForSQL(SelectedASMArray);
}

String WhereASM = "";
if (ASMIDs.length() > 0){
	WhereASM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
}
//PJP


String PJPIDs="";
long SelectedPJPArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPJP") != null){
	SelectedPJPArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPJP");
	PJPIDs = Utilities.serializeForSQL(SelectedPJPArray);
}

/* String WherePJP = "";
if (PJPIDs.length() > 0){
	WherePJP = " and isa.beat_plan_id in ("+PJPIDs+")";	
} */

String WherePJP = "";
if (PJPIDs.length() > 0){
	WherePJP = " and isa.outlet_id in (SELECT distinct outlet_id FROM distributor_beat_plan_schedule where id in("+PJPIDs+"))";	
}


//Distributor

long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");           	
}else{
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	}
}

String DistributorIDs = "";
String WhereDistributors = "";
if(SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0){
	for(int i = 0; i < SelectedDistributorsArray.length ; i++){
		if(i == 0){
			DistributorIDs += SelectedDistributorsArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorsArray[i];
		}
	}
	WhereDistributors = " and isa.distributor_id in ("+DistributorIDs+") ";
	
}




//OrderBooker

boolean IsOrderBookerSelected=false;

int OrderBookerArrayLength=0;
long SelectedOrderBookerArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers");
	
	IsOrderBookerSelected=true;
	OrderBookerArrayLength=SelectedOrderBookerArray.length;
}



String OrderBookerIDs = "";
if(SelectedOrderBookerArray != null && SelectedOrderBookerArray.length > 0){
	for(int i = 0; i < SelectedOrderBookerArray.length; i++){
		if(i == 0){
			OrderBookerIDs += SelectedOrderBookerArray[i];
		}else{
			OrderBookerIDs += ", "+SelectedOrderBookerArray[i];
		}
	}
}
String OrderBookerIDsWhere="";
if(OrderBookerIDs.length()>0){
	OrderBookerIDsWhere =" and order_no in (select mobile_order_no from mobile_order_unedited mou where mou.created_by in ("+OrderBookerIDs+"))";
}



//outlet
boolean IsOutletSelected=false;
String OutletIds="";
long SelectedOutletArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets") != null){
	IsOutletSelected=true;
	SelectedOutletArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets");
	OutletIds = Utilities.serializeForSQL(SelectedOutletArray);
}

String WhereOutlets = "";
if (OutletIds.length() > 0){
	WhereOutlets = " and isa.outlet_id in ("+OutletIds+") ";	
	//System.out.println(WhereOutlets);
}

//Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
//String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);



//Sales Type


String SalesTypeIDs="";
long SelectedSalesTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedSalesType") != null){
	SelectedSalesTypeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedSalesType");
	SalesTypeIDs = Utilities.serializeForSQL(SelectedSalesTypeArray);
}

String WhereSalesType = "";
if (SalesTypeIDs.length() > 0){
	WhereSalesType = " and isa.type_id in ("+SalesTypeIDs+")";	
}

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Outlet Sales Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					 	<%
					 //	WherePJP=" and isa.outlet_id in (SELECT distinct outlet_id FROM distributor_beat_plan_schedule where id in(974))";

					 	if(WherePJP!="" && WherePJP !=null){
					 	%>
					 	
					 			<tr style="font-size: 11px;">
								<th data-priority="1" style="text-align: center;">Outlet</th>
								<th data-priority="1" style="text-align: center;">Channel</th>
								<th data-priority="1" style="text-align: center;">Order
									Booker</th>
								<th data-priority="1" style="text-align: center;">Date</th>
								<%
								ArrayList<Integer> ids = new ArrayList<Integer>();
								ArrayList<String> brands = new ArrayList<String>();
								ResultSet rs142 = s2.executeQuery(
										"select ip.id,(SELECT label FROM inventory_brands ib WHERE ib.id = ip.brand_id) AS brand, (SELECT label FROM inventory_packages ipp WHERE ipp.id = ip.package_id) pack FROM inventory_products ip");
								while (rs142.next()) {
									ids.add(rs142.getInt("id"));
									brands.add(rs142.getString("brand"));
									
								%>
								<th data-priority="1" style="text-align: center;"><%=rs142.getString("pack")%></th>

								<%
								}
								%>

							</tr>

						</thead>
						<tbody>
							<tr style="font-size: 11px;">
								<th data-priority="1" style="text-align: center;">&nbsp;</th>
								<th data-priority="1" style="text-align: center;">&nbsp;</th>
								<th data-priority="1" style="text-align: center;">&nbsp;</th>
								<th data-priority="1" style="text-align: center;">&nbsp;</th>
								<%
								/* ResultSet rs144 = s2.executeQuery(
										"select ip.id,(SELECT label FROM inventory_brands ib WHERE ib.id = ip.brand_id) AS brand, (SELECT label FROM inventory_packages ipp WHERE ipp.id = ip.package_id) pack FROM inventory_products ip");
								String Brand = ""; */
								for (int b = 0; b < brands.size(); b++) {
								%>
								<th data-priority="1" style="text-align: center;"><%=brands.get(b)%></th>
								<%
								}
								%>


								<th data-priority="1" style="text-align: center;">Converted
									Sales</th>
								<th data-priority="1" style="text-align: center;">Amount</th>
							</tr>
						<%
						
						
						
						long PSRID=0;
						String PSRLabel="";
						long PutOutletSeparator=0;
						String TrStyle="";
						
						double TotalArray[]=new double[58];
						
						
							long OutletID=0;
							String OutletName="";
							Date CreatedOn=null;
							
							 String Channel="";
							 double[] TotalSum = new double[ids.size()];
							 int TotalUnitPerSKU70G=0, TotalUnitPerSKU140G=0;
							double TotalConverted=0,MegaAccount=0;
					//		System.out.println("SELECT isa.id,isa.outlet_id,(select name from common_outlets co where co.id=isa.outlet_id ) displayName,sum(isap.raw_cases),isa.created_on,isa.booked_by assigned_to,(SELECT DISPLAY_NAME FROM users where id=isa.booked_by) orderbooker_name,(select pic_channel_id from common_outlets co where co.id=isa.outlet_id ) channel_id,(select psc.label from pci_sub_channel psc where psc.id=channel_id) channel FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id   where  isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WherePJP+WhereSalesType+WhereDistributors+" and booked_by!=0 group by isa.outlet_id order by sum(isap.raw_cases) desc" );
					System.out.println("SELECT isa.id,isa.outlet_id,(select name from common_outlets co where co.id=isa.outlet_id ) displayName,sum(isap.raw_cases),isa.created_on,isa.booked_by assigned_to,(SELECT DISPLAY_NAME FROM users where id=isa.booked_by) orderbooker_name,(select pic_channel_id from common_outlets co where co.id=isa.outlet_id ) channel_id,(select psc.label from pci_sub_channel psc where psc.id=channel_id) channel FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id   where  isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WherePJP+WhereSalesType+WhereDistributors+" and (booked_by!=0 or booked_by is null) group by isa.outlet_id order by sum(isap.raw_cases) desc" );
							ResultSet rs1=s7.executeQuery("SELECT isa.id,isa.outlet_id,(select name from common_outlets co where co.id=isa.outlet_id ) displayName,sum(isap.raw_cases),isa.created_on,isa.booked_by assigned_to,(SELECT DISPLAY_NAME FROM users where id=isa.booked_by) orderbooker_name,(select pic_channel_id from common_outlets co where co.id=isa.outlet_id ) channel_id,(select psc.label from pci_sub_channel psc where psc.id=channel_id) channel FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id   where  isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WherePJP+WhereSalesType+WhereDistributors+" and (booked_by!=0 or booked_by is null) group by isa.outlet_id order by sum(isap.raw_cases) desc" );
							while(rs1.next()){

								OutletID=rs1.getLong("outlet_id");
								OutletName=rs1.getString("displayName");
								CreatedOn =  rs1.getTimestamp ("created_on");	
								
								PSRID=rs1.getLong("assigned_to");
								PSRLabel=rs1.getString("orderbooker_name");
								
								Channel=rs1.getString("channel");
						        
						        if(Channel==null){
						         Channel="";
						        }

									

										

											/************** Dynamic Logic Start ******************/

											//closed
											double  TotalAmount=0;
										//	System.out.println("SELECT sum(isap.net_amount) FROM  inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.outlet_id="+OutletID+" and isap.product_id in (select ip.id FROM inventory_products ip) order by isa.id desc");
											ResultSet rs2231=s2.executeQuery("SELECT sum(isap.net_amount) FROM  inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.outlet_id="+OutletID+" and isap.product_id in (select ip.id FROM inventory_products ip) order by isa.id desc");
											if(rs2231.first()){
												TotalAmount = rs2231.getDouble(1);
												MegaAccount += TotalAmount;
											}
											System.out.println("PSRLabel "+PSRLabel);
											
									%>
									<tr style="font-size: 11px;">
										<td style="text-align: left;"><%=OutletID%> - <%=OutletName%>
											<%
											if (PSRLabel != null) {
											%> (<%=PSRLabel%>) <%
											}
											%></td>
										<td style="text-align: left;"><%=Channel%></td>
										<td style="text-align: left;"><%=PSRLabel%></td>
										<td style="text-align: left;"><%=Utilities.getDisplayDateFormat(StartDate)%></td>
										<%
										String p = "";
										double convertedCal = 0, tempData = 0;
										
										for (int pid = 0; pid < ids.size(); pid++) {
											tempData = 0;
										%>
										<td style="text-align: center;">
											<%
											if (ids.get(pid) != 15 && ids.get(pid) != 16 && ids.get(pid) != 59 && ids.get(pid) != 21 && ids.get(pid) != 22) { // except for raw case
												String Query="";
													
												/* if(ids.get(pid)== 43 ){
													Query = "SELECT sum(total_units/12) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.outlet_id="+OutletID+" and isap.product_id in (43)  order by isa.id desc";
												}else if(ids.get(pid)== 44){
													Query = "SELECT sum(total_units/10) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.outlet_id="+OutletID+" and isap.product_id in (44)  order by isa.id desc";
												}else if(ids.get(pid)== 60){
													Query = "SELECT sum(total_units/12) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.outlet_id="+OutletID+" and isap.product_id in (60)  order by isa.id desc";
												}else if(ids.get(pid)== 3){
													Query="SELECT sum(total_units/24) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=3 order by isa.id desc";	
												}else {*/
													Query="SELECT sum(total_units/unit_per_sku) FROM  inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "
															+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)
															+ " and isa.outlet_id=" + OutletID + " and isap.product_id=" + ids.get(pid)
															+ "  order by isa.id desc";
													
												//}
												ResultSet rs91 = s4.executeQuery(Query);
															if (rs91.first()) {
													tempData = rs91.getDouble(1);
												
													
														
														TotalSum[pid] += tempData;
													
													 
													out.print((tempData != 0) ? tempData : "");
													if(ids.get(pid) == 2 || ids.get(pid) == 6 || ids.get(pid) == 7){
														convertedCal += (tempData * 500)  / 3000;
													}else if(ids.get(pid) == 3){
														convertedCal += (tempData * 25*24) / 3000;
													}else if(ids.get(pid) == 1 || ids.get(pid) == 4 || ids.get(pid) == 5 || ids.get(pid) == 8 || ids.get(pid) == 9){
														convertedCal += (tempData * 250) / 3000;
													}else{
														convertedCal = convertedCal + tempData;
													}
												}

											} else {
										/* 	System.out.println("SELECT sum(total_units), ipv.unit_per_sku FROM  inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "
													+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+ " and isa.outlet_id=" + OutletID + " and isap.product_id=" + ids.get(pid)
													+ "  order by isa.id desc"); */
												ResultSet rs91 = s4.executeQuery(
												"SELECT sum(total_units), ipv.unit_per_sku FROM  inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "
												+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+ " and isa.outlet_id=" + OutletID + " and isap.product_id=" + ids.get(pid)
												+ "  order by isa.id desc");
												if (rs91.first()) {
													tempData = rs91.getDouble(1);
													
													 if(ids.get(pid)== 15 ){
														 TotalUnitPerSKU70G = rs91.getInt(2);
													}else if(ids.get(pid)== 16 ){
														TotalUnitPerSKU140G= rs91.getInt(2);
													}
													
													
													TotalSum[pid] += tempData;
													
													p = Utilities.convertToRawCases((long) tempData, rs91.getInt(2));
												//	System.out.println(ids.get(pid)+"-"+p);
													out.println(p.equals("0") ? "" : p);
													convertedCal += tempData;
												}
											}
											%>
										</td>
										<%
										}
										TotalConverted += convertedCal;
										%>
										<td style="text-align: left;"><%=convertedCal%></td>
										<td style="text-align: left;"><%=TotalAmount%></td>
										<%
										

										/************** Dynamic Logic End ******************/
										%>
									</tr>
									<%
									
							
								
							}//End oF Main Result Set		
						
						
						%>
		
						<tr style="font-size: 11px;">
								<th data-priority="1" style="text-align: center;">&nbsp;</th>
								<th data-priority="1" style="text-align: center;">&nbsp;</th>
								<th data-priority="1" style="text-align: center;">&nbsp;</th>
								<th data-priority="1" style="text-align: center;">Total</th>

								<%
								for (int pid = 0; pid < TotalSum.length; pid++) {
								%>
								<th data-priority="1" style="text-align: center;">
								<%
								if(ids.get(pid) == 15){
								//	System.out.println(Utilities.convertToRawCases((long)TotalSum[pid], TotalUnitPerSKU70G));
								out.print(Utilities.convertToRawCases((long)TotalSum[pid], TotalUnitPerSKU70G));
								}else if(ids.get(pid) == 16){
									// System.out.println(Utilities.convertToRawCases((long)TotalSum[pid], TotalUnitPerSKU140G));
									out.print(Utilities.convertToRawCases((long)TotalSum[pid], TotalUnitPerSKU140G));
								}else{
									out.print(Utilities.getDisplayCurrencyFormatOneDecimal(TotalSum[pid]));
								}
								 %></th>
								<%
								}
								%>

								<th data-priority="1" style="text-align: center;"><%=TotalConverted%></th>
								<th data-priority="1" style="text-align: center;"><%=MegaAccount%></th>
							</tr>
							<%
							}

							else {
							%>
							<b>Please Select PJP First.</b>

							<%
							}
							%>

						</tbody>

					</table>


				</td>
			</tr>
		</table>

	</li>
</ul>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>