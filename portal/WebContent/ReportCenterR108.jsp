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
<%@page import="com.pbc.distributor.DistributorDashboard"%>

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
int FeatureID = 107;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();

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

//Distributor

long SelectedDistributorsArray[] = null;
boolean IsDistributorSelected=false;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors"); 
	IsDistributorSelected = true;
}else{

	/*
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
		
		long DistributorArray[] = new long[1];
		DistributorArray[0] = UserDistributor[x].DISTRIBUTOR_ID;
		session.setAttribute(UniqueSessionID+"_SR1SelectedDistributors",DistributorArray);
		break;
	}*/
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
	WhereDistributors = " and distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}


//PJP


String PJPIDs="";
long SelectedPJPArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPJP") != null){
	SelectedPJPArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPJP");
	PJPIDs = Utilities.serializeForSQL(SelectedPJPArray);
}

String WherePJP = "";
if (PJPIDs.length() > 0){
	WherePJP = " and codv.outlet_id in (SELECT distinct outlet_id FROM distributor_beat_plan_schedule where id in("+PJPIDs+"))";	
}



%>


<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Sales - Package and Distributor (Discount)</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <%
					 if(IsDistributorSelected){ %>
					 <thead>
					   
					    <tr>
					     <th>&nbsp;</th>
					    	<%
					    	String ProductIDs = "0";
					    	ResultSet rs1 = s.executeQuery("select group_concat(distinct isap.product_id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and distributor_id in ("+DistributorIDs+")");
					    	if (rs1.first()){
					    		ProductIDs = rs1.getString(1);
					    	}
					    	
					    	ResultSet rs101 = s.executeQuery("SELECT lrb_type_id, (select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where lrb_type_id is not null and product_id in ("+ProductIDs+") group by lrb_type_id");
							while(rs101.next()){
								%>
								<th data-priority="1"  style="text-align:center; " colspan="<%=rs101.getInt("packge_count")*3%>"><%=rs101.getString("type_name") %></th>
								<%
							}
					    	
					    	
					    	%>
					    
					    
					    </tr>
					    
					    <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
							
							
							<%
							int PackageCount = 0;
							ResultSet rs=null;
							int ArrayCount=0;
							ResultSet rs10 = s.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where lrb_type_id is not null and product_id in (select distinct isap.product_id from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and distributor_id in ("+DistributorIDs+")) group by lrb_type_id");
							while(rs10.next()){
							
							int PacakgeID=0;
							double GrandTotal=0;
							rs = s2.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where 1=1 "+WherePackage+ " and lrb_type_id="+rs10.getInt("lrb_type_id")+" and product_id in (select distinct isap.product_id from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and distributor_id in ("+DistributorIDs+")) order by package_sort_order");
							while(rs.next()){
								ArrayCount++;
								PackageCount++;
								%>
								<th data-priority="1" colspan="3"  style="text-align:center; " ><%=rs.getString("package_label")%></th>
								<%
							}
							}
							
							double[] DiscountedTotal = new double[ArrayCount];
							double[] NonDiscountedTotal = new double[ArrayCount];
							
							for(int j=0;j<ArrayCount;j++){
								DiscountedTotal[j]=0;
								NonDiscountedTotal[j]=0;
								//System.out.println(j);
							}
							
							
							%>	
												
					    </tr>
					    <tr style="font-size:11px;">
						    <th data-priority="1"  style="text-align:center; ">&nbsp;</th>
						    <%
						    //rs.beforeFirst();
						   for(int i=0;i<ArrayCount;i++){
						    	
						    %>
						   
						    <th data-priority="1"  style="text-align:center; min-width:25px;background-color:#F6F6F6;">D</th>
						    <th data-priority="1"  style="text-align:center; min-width:25px;">N</th>
						    <th data-priority="1"  style="text-align:center; min-width:25px;">R</th>
					    <%} %>
					    
					    </tr>
					    
					  </thead> 
					<tbody>
					<%
					
					ResultSet rs31 = s3.executeQuery("select id, name from common_outlets where id in (select outlet_id from common_outlets_distributors_view where distributor_id in ("+DistributorIDs+")) and id in (select distinct outlet_id from inventory_sales_adjusted where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+")");
					while(rs31.next()){
						long OutletID = rs31.getLong(1);
						String OutletName = rs31.getString(2);
					%>
						<tr>
								<td><%=OutletID%>-<%= OutletName%></td>
							<%
							int TotalRowCounter=0;
							rs10 = s.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where lrb_type_id is not null and product_id in ("+ProductIDs+") group by lrb_type_id");
							while(rs10.next()){
								
								int iTypeID = rs10.getInt(1);
								
								double GrandTotal=0;
								rs = s2.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where 1=1 "+WherePackage+" and lrb_type_id="+rs10.getInt("lrb_type_id")+" and product_id in ("+ProductIDs+") order by package_sort_order");
								while(rs.next()){
									int iPackageID = rs.getInt(1);
									
									double DiscountRate = 0;
									double DiscountedQty = 0;
									double NonDiscountedQty = 0;
									Date CurrentDate = StartDate;
									while(CurrentDate.before(Utilities.getDateByDays(EndDate,1))){
										
										ResultSet rs32 = s4.executeQuery("select sum(non_discounted_qty), sum(discounted_qty),discount_rate from ("+
												" select outlet_id, package_id, brand_id, if(discount_rate is null,qty,0) non_discounted_qty, if(discount_rate is null,0,qty) discounted_qty, discount_rate from ( "+
														" select isa.outlet_id, ipv.package_id, ipv.brand_id, sum(isap.raw_cases) qty, ( "+ 
															 " select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where date("+Utilities.getSQLDate(CurrentDate)+") between s.activated_on and s.deactivated_on and date("+Utilities.getSQLDate(CurrentDate)+") between sp.valid_from and sp.valid_to and sp.package = ipv.package_id  and sp.brand_id = ipv.brand_id and sp.brand_id != 0 and s.outlet_id = isa.outlet_id "+ 
																" union all "+ 
															 " select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id join inventory_products ip on sp.package = ip.package_id and ip.category_id = 1 where date("+Utilities.getSQLDate(CurrentDate)+") between s.activated_on and s.deactivated_on and date("+Utilities.getSQLDate(CurrentDate)+") between sp.valid_from and sp.valid_to and sp.package = ipv.package_id and sp.brand_id = 0 and s.outlet_id = isa.outlet_id limit 1 "+ 
															 " ) discount_rate from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id "+ 
														" where isa.created_on between date("+Utilities.getSQLDate(CurrentDate)+") and date("+Utilities.getSQLDateNext(CurrentDate)+") and isa.outlet_id = "+OutletID+" and isap.is_promotion = 0 and ipv.package_id = "+iPackageID+" and ipv.lrb_type_id = "+iTypeID+" group by isa.outlet_id, ipv.package_id, ipv.brand_id "+
													" ) tab2 "+
												" ) tab3 group by outlet_id ");
										if(rs32.first()){
											NonDiscountedQty += rs32.getDouble(1);
											DiscountedQty += rs32.getDouble(2);
											DiscountRate = rs32.getDouble(3);
										}
									
										CurrentDate = Utilities.getDateByDays(CurrentDate,1);
									}
									
									DiscountedTotal[TotalRowCounter] += DiscountedQty;
									NonDiscountedTotal[TotalRowCounter] += NonDiscountedQty;
									
									TotalRowCounter++;
									
									%>
									<td style="text-align: right; background-color:#F6F6F6;"><%if (DiscountedQty!=0){out.println(Utilities.getDisplayCurrencyFormatRounded(DiscountedQty));} %></td> 
									<td style="text-align: right;"><%if (NonDiscountedQty!=0){out.println(Utilities.getDisplayCurrencyFormatRounded(NonDiscountedQty));} %></td> 
									<td style="text-align: right;"><%if (DiscountRate!=0){out.println(Utilities.getDisplayCurrencyFormatRounded(DiscountRate));} %></td>
									<%
									
								}
							}		
							%>
						</tr>
					<%
					}
					%>	
						 <tr>
							<td style="font-weight:bold">Total</td>
						 <%
						 for(int t=0;t<DiscountedTotal.length;t++){						 
						 %>
							<td style="text-align: right;background-color:#F6F6F6;"><%if(DiscountedTotal[t]!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(DiscountedTotal[t]) %><%} %></td> 
							<td style="text-align: right;"><%if(NonDiscountedTotal[t]!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(NonDiscountedTotal[t]) %><%} %></td>
							<td style="text-align: right;"></td>
						<%
						 }
						%>	
							
						</tr>
						 <tr>
							<td style="font-weight:bold">Total (%)</td>
						 <%
						 for(int t=0;t<DiscountedTotal.length;t++){
							 
							 double percentD = 0;
							 double percentN = 0;
							 double total = DiscountedTotal[t] + NonDiscountedTotal[t];
							 if (total != 0){
								 percentD = ((DiscountedTotal[t] * 1d)  /  total) * 100 ;
								 percentN = ((NonDiscountedTotal[t] * 1d) /  total) * 100;
							 }
							 
						 %>
							<td style="text-align: right;background-color:#F6F6F6;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(percentD) %>%</td> 
							<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(percentN) %>%</td>
							<td style="text-align: right;"></td>
						<%
						 }
						%>	
						</tr>
						 <tr>
							<td style="font-weight:bold"></td>
						 <%
						 for(int t=0;t<DiscountedTotal.length;t++){
						 %>
						    <th data-priority="1"  style="text-align:center; min-width:25px;background-color:#F6F6F6;">D</th>
						    <th data-priority="1"  style="text-align:center; min-width:25px;">N</th>
						    <th data-priority="1"  style="text-align:center; min-width:25px;">R</th>
						<%
						 }
						%>	
						</tr>
						
					</tbody>
						
				</table>
				
				
		</td>
	</tr>
</table>
<table border=0 style="font-size:13px; font-weight: 400; width:25%; margin-top:10px;" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
				
				 <tr style="font-size:11px;background:#ececec;">
						 	<th>Legend</th>
						 </tr>
						 <tr>
						 <td>D = Discounted</td>
						 </tr>
						 <tr>
						 <td>N = Non Discounted</td>
						 </tr>
						 <tr>
						 <td>R = Rate of Discount</td>
						 </tr>
				</table>
<%} else{ %>
					
						<p style="padding-left:10px;padding-top:20px;">Please select at least one  distributor</p>
					
					
					
					<%} %>		
	</li>	
</ul>

<%
//dd.close();
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>