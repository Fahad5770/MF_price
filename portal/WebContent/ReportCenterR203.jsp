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
int FeatureID = 255;

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



Datasource ds2 = new Datasource();
ds2.createConnectionToSAPDB();
Statement sap = ds2.createStatement();
Statement sap2 = ds2.createStatement();

//sap.executeUpdate("drop table temp_invoice_list");

long rand = Math.round(Math.random())+Math.round(Math.random());

sap.executeUpdate("create global temporary table temp_invoice_list"+rand+"(invoice_no VARCHAR2(30 BYTE))");

s.executeUpdate("create temporary table temp_revenue_lifting (vbeln bigint(10), distributor_id bigint(10), matnr int(10), total_units double)");


ds2.startTransaction();

ResultSet rs9 = s.executeQuery("SELECT invoice_no FROM inventory_delivery_note where created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate) +WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM);
while(rs9.next()){
	sap.executeUpdate("insert into temp_invoice_list"+rand+" values ( '"+ Utilities.addLeadingZeros(rs9.getString("invoice_no"), 10)+"' ) ");
}

//System.out.println("select vbrk.vbeln, vbrk.fkart, vbrk.knumv, vbrp.posnr, vbrk.kurrf_dat, vbrp.pstyv, vbrp.matnr, vbrp.fkimg quantity, (vbrp.kzwi1+ /*vbrp.kzwi2 + vbrp.kzwi3 +*/ vbrp.kzwi6) gross_value, vbrp.kzwi4 unloading, vbrp.kzwi5 freight, vbrp.vrkme, vbrk.kunrg from sapsr3.vbrp vbrp join sapsr3.vbrk on vbrk.vbeln = vbrp.vbeln where vbrk.fkart in ('ZDIS','ZMRS','ZDFR') and vbrk.vbeln in (select invoice_no from temp_invoice_list) order by vbrk.vbeln");
ResultSet rsSAP = sap2.executeQuery("select vbrk.vbeln, vbrk.fkart, vbrk.knumv, vbrp.posnr, vbrk.kurrf_dat, vbrp.pstyv, vbrp.matnr, vbrp.fkimg quantity, (vbrp.kzwi1+ /*vbrp.kzwi2 + vbrp.kzwi3 +*/ vbrp.kzwi6) gross_value, vbrp.kzwi4 unloading, vbrp.kzwi5 freight, vbrp.vrkme, vbrk.kunrg from sapsr3.vbrp vbrp join sapsr3.vbrk on vbrk.vbeln = vbrp.vbeln where vbrk.fkart in ('ZDIS','ZMRS','ZDFR') and vbrp.pstyv!='TANN' and vbrk.vbeln in (select invoice_no from temp_invoice_list"+rand+") order by vbrk.vbeln");
while(rsSAP.next()){
	
	long SAPCode = Utilities.parseLong(rsSAP.getString("matnr"));
	int quantity = rsSAP.getInt("quantity");
	String TransactionType = rsSAP.getString("vrkme");
	long UnitPerSKU = 1;
	
	if(TransactionType.equals("KI") || TransactionType.equals("KAR")){		
		ResultSet rs1 = s.executeQuery("select unit_per_sku from inventory_products where id= (select id from inventory_products where sap_code="+SAPCode+")");
		if(rs1.first()){
			UnitPerSKU = rs1.getInt("unit_per_sku");
		}		
	}
	
	long TotalUnits = quantity * UnitPerSKU;
	
	s2.executeUpdate("insert into temp_revenue_lifting values ("+rsSAP.getLong("vbeln")+","+rsSAP.getLong("kunrg")+","+SAPCode+","+TotalUnits+" )");
	
}


ds2.commit();
sap.executeUpdate("drop table temp_invoice_list"+rand+"");

sap.close();

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Revenue based Lifting</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					   <tr style="font-size:11px;">
					   		<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
					   		<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
							
							<%
							
							//String[] DiscountTitle= {"Gross Revenue","","","","","","","","","","",""};
							
							
							ResultSet rs21 = s.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  sap_code in (select distinct matnr from temp_revenue_lifting) and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs21.next()){
								%>
								<th data-priority="1"  style="text-align:center; " colspan="<%=rs21.getInt("package_count")%>"><%=rs21.getString("type_name") %></th>
								<%
							}
							
							%>
												
					    </tr>
					   
					   
					    <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
							<th data-priority="1"  style="text-align:center; ">Converted</th>
							
							
							<%
							int PackageCount = 0;
							
						
							int ArrayCount=0;
							
							ResultSet rs12 = s.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  sap_code in (select distinct matnr from temp_revenue_lifting) and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs12.next()){
								
								ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  sap_code in (select distinct matnr from temp_revenue_lifting) "+WherePackage+" and category_id = 1 and lrb_type_id="+rs12.getLong("lrb_type_id")+" order by package_sort_order");
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
						double TotalConverted = 0;
						//System.out.println("select * from common_distributors where "+WhereHOD+WhereRSM+" and distributor_id in ("+DistributorIDs+") and distributor_id in (select distinct distributor_id from inventory_delivery_note)");
						ResultSet rs1 = s.executeQuery("select * from common_distributors where distributor_id in (select distinct distributor_id from temp_revenue_lifting)"); //distributor query
						while(rs1.next()){
							DistributorID = rs1.getInt("distributor_id");
							
							int PackageIndex = 0;
							
							double converted = 0;
							ResultSet rs24 = s4.executeQuery("select sum(trl.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml from temp_revenue_lifting trl join inventory_products_view ipv on trl.matnr = ipv.sap_code where trl.distributor_id = "+DistributorID);
							if (rs24.first()){
								converted = rs24.getDouble(1);
							}							
							TotalConverted += converted;
						%>
						<tr>
								<td><%=DistributorID + " - "+ Utilities.truncateStringToMax(rs1.getString("name"),20) %></td>
								<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(converted) %></td>
							<%
							
							ResultSet rs31 = s3.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  sap_code in (select distinct matnr from temp_revenue_lifting) and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs31.next()){								
								ResultSet rs4 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  sap_code in (select distinct matnr from temp_revenue_lifting) "+WherePackage+" and category_id = 1 and lrb_type_id="+rs31.getLong("lrb_type_id")+" order by package_sort_order");
								while(rs4.next()){
							
								int TypeID = rs31.getInt(2);
								int PackageID = rs4.getInt(1);
								double cases = 0;
								
								ResultSet rs22 = s4.executeQuery("select sum(trl.total_units)/ipv.unit_per_sku from temp_revenue_lifting trl join inventory_products_view ipv on trl.matnr = ipv.sap_code where trl.distributor_id = "+DistributorID+" and ipv.package_id = "+PackageID+" and ipv.lrb_type_id = "+TypeID);
								if (rs22.first()){
									cases = rs22.getDouble(1);
								}							
								PackageTotal[PackageIndex] += Math.round(cases);	
									
								
							%>
								<td style="text-align: right;"><% if (cases != 0){out.print(Utilities.getDisplayCurrencyFormat(cases));} %></td> 
							<%
							PackageIndex++;
								}
								
								
								
							
							%>
							
							<%
								}	
							}
							%>
						</tr>
						
						<tr>
						<td style="font-weight:bold">Total</td>
						<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(TotalConverted) %></td>
						<%
						
						for (int i = 0; i < PackageTotal.length; i++){
							
							%>
							<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(PackageTotal[i])%></td> 
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
ds2.dropConnection();
%>