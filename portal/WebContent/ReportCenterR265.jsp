<%@page import="java.awt.List"%>

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
int FeatureID = 332;

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

//System.out.println(WhereBrand);

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
	WhereDistributors = " and distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}



//Lifting Type 


String SelectedLiftingTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedLiftingType") != null){
	SelectedLiftingTypeArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedLiftingType");
}

String WhereLiftingType = "";
if(SelectedLiftingTypeArray!=null){
	for(int i=0;i<SelectedLiftingTypeArray.length;i++){
		if(SelectedLiftingTypeArray[i].equals("Internal")){
			WhereLiftingType = " and idn.outsourced_primary_sales_id is null ";
		}else if(SelectedLiftingTypeArray[i].equals("Other Plant")){
			WhereLiftingType = " and idn.outsourced_primary_sales_id is not null ";
		}
	}
}

//System.out.println("Hello "+WhereLiftingType);



long SND_ID = 0;
long iDistributorID = 0;
String iPJPIDs = "0";
ResultSet irs = s.executeQuery("select group_concat(id), distributor_id from distributor_beat_plan where asm_id=386 group by distributor_id limit 1");
if (irs.first()){
	iDistributorID = irs.getLong(2);
	iPJPIDs = irs.getString(1);
}






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
					   		 
							
							<%
							
							//String[] DiscountTitle= {"Gross Revenue","","","","","","","","","","",""};
							
							

							
							
							
							
							//ResultSet rs21 = s.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  is_visible = 1 and category_id = 1 and package_label not like '%POST%'  and package_label not like '%CO2%'  and package_label not like '%BULK%'  and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
						//	while(rs21.next()){
								%>
								<%-- <th data-priority="1"  style="text-align:center; " colspan="<%=rs21.getInt("package_count")%>"><%=rs21.getString("type_name") %></th> --%>
								<%
							//}
							
							%>
							
							<%
								ResultSet rs21 = s.executeQuery("SELECT count(*) as ProdinPackage,package_label FROM inventory_products_view where 1=1 "+ WherePackage + WhereBrand + " group by package_id  order by package_id");
								while (rs21.next()) {
								%>
								<th data-priority="1" style="text-align: center;"
									colspan="<%=rs21.getInt("ProdinPackage")%>"><%=rs21.getString("package_label")%></th>
								<%
									}
								%>
							
												
					    </tr>
					   
					   
					    <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
							<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
							
<!-- 							<th>Converted</th>   -->
							
							
							<%
							int PackageCount = 0;
							
						
							int ArrayCount=0;
							
							/* ResultSet rs12 = s7.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  is_visible = 1 and category_id = 1 and package_label not like '%POST%'  and package_label not like '%CO2%'  and package_label not like '%BULK%'  and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs12.next()){
								
								ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where is_visible = 1 and category_id = 1 and package_label not like '%POST%'  and package_label not like '%CO2%'  and package_label not like '%BULK%' and lrb_type_id="+rs12.getLong("lrb_type_id")+" order by package_sort_order");
								while(rs2.next()){ */
							%>
								<%-- <th data-priority="1"  style="text-align:center; "><%=rs2.getString("package_label") %></th> --%>
							<%
							/* ArrayCount++;
							PackageCount++;
								}
							} */
							%>
							
							
							<%
							ResultSet rs12 = s7.executeQuery("SELECT package_id,package_label FROM inventory_products_view where 1=1 "+ WherePackage + WhereBrand + " group by package_id order by package_id");
							while (rs12.next()) {
								ResultSet rs2 = s2.executeQuery("SELECT product_id,package_id,brand_id,brand_label,package_label FROM inventory_products_view  where 1=1"+ WhereBrand + " and package_id=" + rs12.getInt(1));
								while (rs2.next()) {
								%>
								<th data-priority="1" style="text-align: center;"><%=rs2.getString("brand_label")%></th>
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
						long TotalConverted=0;
						
						
						String OutletName = "";
						long OutletID=0;
						
						String distributorName="";
						//System.out.println("select * from common_distributors where "+WhereHOD+WhereRSM+" and distributor_id in ("+DistributorIDs+") and distributor_id in (select distinct distributor_id from inventory_delivery_note)");
						ResultSet rs1 = s8.executeQuery("SELECT id as outlet_id,name,distributor_id,(select name from pep.common_distributors cd where cd.distributor_id=co.distributor_id)as distributor_name FROM pep.common_outlets co where co.id in (select distinct outlet_id from inventory_sales_dispatch isd join inventory_sales_dispatch_adjusted_products isdap on isd.id = isdap.dispatch_id where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereDistributors+WhereHOD+")"); //distributor query

						while(rs1.next()){
							
							OutletName = rs1.getInt("outlet_id")+" - "+rs1.getString("name");
							
							OutletID = rs1.getLong("outlet_id");
							
							
							distributorName=rs1.getInt("distributor_id")+" - "+rs1.getString("distributor_name");
							
						//	DistributorID = rs1.getInt("id");
							
							int PackageIndex = 0;
						%>
						<tr>
								<td><%=Utilities.truncateStringToMax((OutletName),30) %></td>
								<td><%=distributorName %></td>
								
							 	
														<%
							
							ResultSet rs121 = s7.executeQuery("SELECT package_id,package_label FROM inventory_products_view where 1=1 "+ WherePackage + " group by package_id order by package_id");
							while (rs121.next()) {
								int PackageID=rs121.getInt(1);
								
								ResultSet rs2 = s2.executeQuery("SELECT product_id,package_id,brand_id,brand_label,package_label FROM inventory_products_view  where 1=1"+ WhereBrand + " and package_id=" + PackageID);
								while (rs2.next()) {
									int BrandID=rs2.getInt("brand_id");
									int ProductID=rs2.getInt("product_id");
									
									long QuantityReturned=0;
							
									
									
									long TotalQuantityDispatched=0;
									long TotalQuantitySold=0;
														
									ResultSet q1=s5.executeQuery("select sum(isdap.total_units/ipv.unit_per_sku) total_returns from inventory_sales_dispatch_adjusted_products isdap  join inventory_products_view ipv on isdap.product_id = ipv.product_id  where dispatch_id in (SELECT id FROM pep.inventory_sales_dispatch where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereDistributors+WhereHOD+") and isdap.outlet_id="+OutletID+" and ipv.package_id ="+PackageID+" and ipv.brand_id="+BrandID);
									while(q1.next())
									{ 
										QuantityReturned=q1.getLong("total_returns");
									}
																
									
							
									PackageTotal[PackageIndex]+=QuantityReturned;
							
						
							%>

					
							<%
						
								
							
							%>
								<td style="text-align: right;"><%if(QuantityReturned!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(QuantityReturned) %><%} %></td>							
							<%
							PackageIndex++;
								
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
						 for(int i=0;i<PackageTotal.length;i++){
							 %>
							 
							 <td style="text-align: right;"><%if(PackageTotal[i]!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(PackageTotal[i]) %><%} %></td>
							 
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
s8.close();
s7.close();
s6.close();
s5.close();
s4.close();
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>