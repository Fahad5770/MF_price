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
int FeatureID = 277;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
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
	WhereBrand = " and ipv.brand_id in ("+BrandIDs+") ";
}

System.out.println(WhereBrand);

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


Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);

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


//customer filter

long DistributorID1 = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
	DistributorID1 = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
}
String WhereCustomerID ="";
String WhereCustomerID1 ="";

if (DistributorID1 != 0){
	WhereCustomerID = " and plant_id in ("+DistributorID1+") ";
	WhereCustomerID1 = " and distributor_id in ("+DistributorID1+") ";

}

//System.out.println("Hello - "+WhereCustomerID);
%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					   <tr style="font-size:11px;">
					   		<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
					   		<th></th>
							
							<%
							
							//String[] DiscountTitle= {"Gross Revenue","","","","","","","","","","",""};
							
							
							ResultSet rs21 = s.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM inventory_delivery_note_products) and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs21.next()){
								%>
								<th data-priority="1"  style="text-align:center; " colspan="<%=rs21.getInt("package_count")%>"><%=rs21.getString("type_name") %></th>
								<%
							}
							
							%>
												
					    </tr>
					   
					   
					    <tr style="font-size:11px;">
							<th></th>
							<th data-priority="1"  style="text-align:center; ">Converted</th>
							
							
							
							<%
							int PackageCount = 0;
							int TotalConverted=0;
							
						
							int ArrayCount=0;
							
							ResultSet rs12 = s.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM inventory_delivery_note_products) and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs12.next()){
								
								ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM inventory_delivery_note_products) "+WherePackage+" and category_id = 1 and lrb_type_id="+rs12.getLong("lrb_type_id")+" order by package_sort_order");
								while(rs2.next()){
							%>
								<th data-priority="1"  style="text-align:center; "><%=rs2.getString("package_label") %></th>
							<%
							ArrayCount++;
							PackageCount++;
								}
							}
							%>
							
							<%
							long PackageTotal[] = new long[PackageCount];
							int PackageTotalUnitPerSKU[] = new int[PackageCount];
							for (int i = 0; i < PackageTotal.length; i++){
								PackageTotal[i] = 0;
								PackageTotalUnitPerSKU[i]=0;
							}
							
							//System.out.println(PackageCount);
							%>							
					    </tr>
					  </thead> 
					<tbody>
						<%
						long DistributorID=0;
						long PacakgeID=0;
						//System.out.println("hasdhfdsaf");
						//System.out.println("select * from common_distributors where 1=1 and distributor_id in ("+DistributorID1+") and distributor_id in ("+DistributorIDs+") and distributor_id in (select distinct plant_id from primary_purchases_orders where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+")");
						ResultSet rs1 = s.executeQuery("select * from common_distributors where 1=1 "+WhereCustomerID1+" and distributor_id in ("+DistributorIDs+") and distributor_id in (select distinct plant_id from primary_purchases_orders where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+")"); //distributor query
						while(rs1.next()){
							DistributorID = rs1.getInt("distributor_id");
							
							int PackageIndex = 0;
							 
							
						%>
						<tr>
								<td><%=DistributorID + " - "+ rs1.getString("name") %></td>
							<%
								long ConvertedSales=0;
								int UnitPerSKUCon=0;
								ResultSet rs23 = s3.executeQuery("select sum(total_units*ipv.liquid_in_ml)/6000 converted,ipv.unit_per_sku from primary_purchases_orders ppo join primary_purchases_orders_products ppop on ppo.id = ppop.id join inventory_products_view ipv on ppop.product_id = ipv.product_id where ppo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and ppo.plant_id = "+DistributorID+" and ipv.category_id = 1");
								if(rs23.first()){
									ConvertedSales = rs23.getLong("converted");
									UnitPerSKUCon = rs23.getInt("unit_per_sku");
								}
								
								TotalConverted += ConvertedSales;
								%>
								
								
								<td  style="text-align: right;"><%=ConvertedSales%></td>
							
							
							<%
							
							ResultSet rs31 = s3.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM inventory_delivery_note_products) and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs31.next()){								
								ResultSet rs4 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM inventory_delivery_note_products) "+WherePackage+" and category_id = 1 and lrb_type_id="+rs31.getLong("lrb_type_id")+" order by package_sort_order");
								while(rs4.next()){
							
							
							
							
						
							int unit_per_sku=0;
							//System.out.println("select sum(total_units) qty,ipv.unit_per_sku from primary_sales_orders pso join primary_sales_orders_products psop on pso.id = psop.id join inventory_products_view ipv on psop.product_id = ipv.product_id where pso.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and pso.distributor_id = "+DistributorID+" and ipv.category_id = 1 and ipv.package_id ="+rs4.getLong("package_id")+" and ipv.lrb_type_id="+rs31.getLong("lrb_type_id")+WherePackage+" "+WhereBrand);
							
									ResultSet rs3 = s4.executeQuery("select sum(total_units) qty,ipv.unit_per_sku from primary_purchases_orders ppo join primary_purchases_orders_products ppop on ppo.id = ppop.id join inventory_products_view ipv on ppop.product_id = ipv.product_id where ppo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and ppo.plant_id = "+DistributorID+" and ipv.category_id = 1 and ipv.package_id ="+rs4.getLong("package_id")+" and ipv.lrb_type_id="+rs31.getLong("lrb_type_id")+WherePackage+" "+WhereBrand);	
								while(rs3.next()){
									long qty = rs3.getLong("qty");
									unit_per_sku = rs3.getInt("unit_per_sku");
									PackageTotal[PackageIndex] += qty;
									if(unit_per_sku > 0){
										PackageTotalUnitPerSKU[PackageIndex] = unit_per_sku;
									}
									
							%>
								<td style="text-align: right;"><%= Utilities.convertToRawCases(qty,unit_per_sku) %></td> 
							<%
							PackageIndex++;
								}
								
								
								
							
							%>
							
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
						<td  style="text-align: right;"><%=TotalConverted %></td>
						<%
						
						for (int i = 0; i < PackageTotal.length; i++){
							
							%>
							<td style="text-align: right;"><%=Utilities.convertToRawCases(PackageTotal[i],PackageTotalUnitPerSKU[i])%></td> 
						<%
						}
						%>
						</tr>
						
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