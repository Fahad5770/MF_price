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
int FeatureID = 331;

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


String ProductsLifted ="-1";
//System.out.println("SELECT group_concat(distinct package_id) FROM employee_targets et join employee_targets_packages etp on et.id = etp.id where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y')");
ResultSet rs41 = s6.executeQuery("SELECT group_concat(distinct package_id) FROM employee_targets et join employee_targets_packages etp on et.id = etp.id where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y')");

if (rs41.first()){
	ProductsLifted = rs41.getString(1);
}



long SelectedEmployeeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedEmployees") != null){
	SelectedEmployeeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedEmployees");           	
}
String EmployeeIDs = "";
String WhereEmployee = "";
if(SelectedEmployeeArray != null && SelectedEmployeeArray.length > 0){
	for(int i = 0; i < SelectedEmployeeArray.length; i++){
		if(i == 0){
			EmployeeIDs += SelectedEmployeeArray[i];
		}else{
			EmployeeIDs += ", "+SelectedEmployeeArray[i];
		}
	}
	WhereEmployee = " and et.employee_id in ("+EmployeeIDs+") ";
}



%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">PSR Targets Report</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					   <tr style="font-size:11px;">
					   		<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
					   		<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
					   		<!-- <th></th> -->
							
							<%
							
							//String[] DiscountTitle= {"Gross Revenue","","","","","","","","","","",""};
							
							
							ResultSet rs21 = s.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  package_id in ("+ProductsLifted+") and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs21.next()){
								%>
								<th data-priority="1"  style="text-align:center; " colspan="<%=rs21.getInt("package_count")%>"><%=rs21.getString("type_name") %></th>
								<%
							}
							
							%>
												
					    </tr>
					   
					   
					    <tr style="font-size:11px;">
							<th>PJP ID</th>
							<th>Employee ID</th>
							<!-- <th>Converted</th> -->
							
							
							<%
							int PackageCount = 0;
							
						
							int ArrayCount=0;
							
							ResultSet rs12 = s7.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  package_id in ("+ProductsLifted+") and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs12.next()){
								
								ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  package_id in ("+ProductsLifted+") "+WherePackage+" and category_id = 1 and lrb_type_id="+rs12.getLong("lrb_type_id")+" order by package_sort_order");
								while(rs2.next()){
							%>
								<th data-priority="1"  style="text-align:center; "><%=rs2.getString("package_label") %></th>
							<%
							ArrayCount++;
							PackageCount++;
								}
							}
							%>
							
												
					    </tr>
					  </thead> 
					<tbody>
						<%
						long EmployeeID=0;
						long PacakgeID=0;
						long TotalConverted=0;
						int pjpId=0;
						
						//ResultSet rs1 = s8.executeQuery("select * from users where 1=1 and ID in (select distinct employee_id from employee_targets where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y'))"); //distributor query
						System.out.println("select pjp_id ,employee_id, (select DISPLAY_NAME from users where id=employee_id) as name from employee_targets where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y')");
						ResultSet rs1 = s8.executeQuery("select pjp_id ,employee_id, (select DISPLAY_NAME from users where id=employee_id) as name from employee_targets where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y')"); //distributor query

						while(rs1.next()){
							EmployeeID=0;
							 PacakgeID=0;
							 TotalConverted=0;
							 pjpId=0;
							EmployeeID = rs1.getInt("employee_id");
							pjpId = rs1.getInt("pjp_id");
							int PackageIndex = 0;
						%>
						<tr>
						<td><%=pjpId%></td>
								<td><%= (EmployeeID == 0 ) ? "" : Utilities.truncateStringToMax((EmployeeID + " - "+ rs1.getString("name")),30) %></td>
							<%-- <%
							//Converted
							long ConvertedCases=0;
							
							//ResultSet rs311 = s4.executeQuery("select sum(quantity*ipv.liquid_in_ml)/6000 quantity from employee_targets_packages etp join employee_targets et on etp.id= et.id join inventory_products_view ipv on etp.package_id = ipv.package_id where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y') and ipv.category_id=1 and et.employee_id = "+EmployeeID);	
							ResultSet rs311 = s4.executeQuery("select sum(quantity) quantity from employee_targets_packages etp join employee_targets et on etp.id= et.id join inventory_products_view ipv on etp.package_id = ipv.package_id where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y') and ipv.category_id=1 and et.employee_id = "+EmployeeID);	
								while(rs311.next()){
									ConvertedCases = rs311.getLong("quantity");
								}
							
								TotalConverted +=ConvertedCases;
							
							%>
							
							<td style="text-align: right;"><%if(ConvertedCases!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCases) %><%} %></td> --%>
							
														<%
							
							ResultSet rs31 = s3.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  package_id in ("+ProductsLifted+") and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs31.next()){								
								long lrb_type_id=rs31.getLong("lrb_type_id");
								ResultSet rs4 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  package_id in ("+ProductsLifted+") and category_id = 1 and lrb_type_id="+lrb_type_id+" order by package_sort_order");
								while(rs4.next()){
							
							
									long PackageID=rs4.getLong("package_id");
									
						
						
							int unit_per_sku=0;
							long qty=0;
							
								//	System.out.println("select quantity from employee_targets_packages etp join employee_targets et on etp.id= et.id join inventory_products_view ipv on ipv.package_id=etp.package_id where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y') and et.employee_id = "+DistributorID+ " and ipv.package_id ="+PackageID+" and ipv.lrb_type_id="+lrb_type_id);
								//	ResultSet rs3 = s5.executeQuery("SELECT et.id,et.employee_id,etp.package_id,etp.quantity q1,etpb.brand_id,etpb.package_id, sum(etpb.quantity) q2 FROM employee_targets et  join employee_targets_packages etp on et.id=etp.id  join pep.employee_targets_packages_brands etpb on et.id=etpb.id and etp.package_id=etpb.package_id where et.month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and et.year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y') and et.employee_id="+EmployeeID+ " and etp.package_id="+PackageID+" and  brand_id in (SELECT brand_id FROM pep.inventory_products_view where lrb_type_id="+lrb_type_id+" and package_id="+PackageID+" and category_id=1)");	
									ResultSet rs3 = s5.executeQuery("SELECT sum(etpb.quantity) q2 FROM employee_targets et  join employee_targets_packages etp on et.id=etp.id join employee_targets_packages_brands etpb on et.id=etpb.id and etp.package_id=etpb.package_id  where et.month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and et.year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y') and et.pjp_id="+pjpId+" and  etp.package_id="+PackageID+" and  etpb.brand_id in (SELECT brand_id FROM inventory_products_view where lrb_type_id="+lrb_type_id+" and package_id="+PackageID+" and category_id=1)");
									while(rs3.next()){
									 qty = rs3.getLong("q2");
									
									//unit_per_sku = rs3.getInt("unit_per_sku");
							//		PackageTotal[PackageIndex] += qty;
// 									if(unit_per_sku > 0){
// 										PackageTotalUnitPerSKU[PackageIndex] = unit_per_sku;
// 									}
									
							%>
<%-- 								<td style="text-align: right;"><%= Utilities.convertToRawCases(qty,unit_per_sku) %></td>  --%>

					
							<%
							PackageIndex++;
								}
								
							
							%>
								<td style="text-align: right;"><%= Utilities.getDisplayCurrencyFormatRounded(qty) %></td>							
							<%
								}	
							}
							%>

						</tr>
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