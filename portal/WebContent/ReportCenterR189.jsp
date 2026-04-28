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
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.pbc.common.Region"%>


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
int FeatureID = 231;

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


//Date date = Utilities.parseDate(request.getParameter("Date"));


Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");




if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	//StartDate = new Date(); // add code of start of current month if first time report opens
	Calendar cc = Calendar.getInstance();   // this takes current date
    cc.set(Calendar.DAY_OF_MONTH, 1);
    StartDate = cc.getTime();
     
	
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}


int SelectedMonth = Utilities.getMonthNumberByDate(StartDate);
int SelectedYear = Utilities.getYearByDate(StartDate);



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

//HOD


String HODIDs="";
long SelectedHODArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedHOD") != null){
	SelectedHODArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedHOD");
	HODIDs = Utilities.serializeForSQL(SelectedHODArray);
}


String WhereHOD = "";
String WhereHOD1 = "";

if (HODIDs.length() > 0){
	WhereHOD = " and customer_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
	WhereHOD1 = " and cache_snd_id in ("+HODIDs+") ";

}


//RSM


String RSMIDs="";
long SelectedRSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRSM") != null){
	SelectedRSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRSM");
	RSMIDs = Utilities.serializeForSQL(SelectedRSMArray);
}

String WhereRSM = "";
String WhereRSM1 = "";

if (RSMIDs.length() > 0){
	WhereRSM = " and customer_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
	WhereRSM1 = " and cache_rsm_id in("+RSMIDs+") ";
	
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
	WhereTDM = " and cache_tdm_id in ("+TDMIDs+") ";	
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

Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);


long SelectedBrandArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedBrands") != null){
	   SelectedBrandArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedBrands");           	
}


String BrandIDs = "";
String WhereBrand = "";

if(SelectedBrandArray!= null && SelectedBrandArray.length > 0){
	for(int i = 0; i < SelectedBrandArray.length; i++){
		if(i == 0){
			BrandIDs += SelectedBrandArray[i]+"";
		}else{
			BrandIDs += ", "+SelectedBrandArray[i]+"";
		}
		
	}

	WhereBrand = " and ipv.brand_id in ("+BrandIDs+") ";
}




/*
long DistributorID = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
	DistributorID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
}

System.out.println("DistributorID = "+DistributorID);
String WhereCustomerID ="";
String WhereDistributorID ="";
if (DistributorID != 0){
	WhereCustomerID = " and customer_id in ("+DistributorID+") ";
	WhereDistributorID = " and cache_distributor_id in ("+DistributorIDs+") ";
}else{
	WhereCustomerID = " and customer_id in ("+DistributorIDs+") ";
	WhereDistributorID = " and cache_distributor_id in ("+DistributorIDs+") ";
}
*/

//System.out.println("Hello "+WhereCustomerID);

//Distributor

long SelectedDistributorsArray[] = null;
boolean IsDistributorSelected=false;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){

	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors"); 
	IsDistributorSelected = true;
}else{
}

String DistributorIDs2 = "";
String WhereCustomerID ="";
String WhereDistributorID ="";
if(SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0){
	for(int i = 0; i < SelectedDistributorsArray.length ; i++){
		
		if(i == 0){
			DistributorIDs2 += SelectedDistributorsArray[i];
		}else{
			DistributorIDs2 += ", "+SelectedDistributorsArray[i];
		}
	}
	WhereCustomerID = " and customer_id in ("+DistributorIDs2+") ";
	WhereDistributorID = " and cache_distributor_id in ("+DistributorIDs2+") ";
	//out.print(WhereDistributors);
}

//GTM Category


String GTMCategoryIDs="";
long SelectedGTMCategoryArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedGTMCategory") != null){
	SelectedGTMCategoryArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedGTMCategory");
	GTMCategoryIDs = Utilities.serializeForSQL(SelectedGTMCategoryArray);
}

String WhereGTMCategory = "";
String WhereGTMCategory1 = "";
String WhereGTMCategory2 = "";
String WhereGTMCategory3 = "";
if (GTMCategoryIDs.length() > 0){
	WhereGTMCategory = " and customer_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";	
	WhereGTMCategory1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";
	WhereGTMCategory2 = " and tab1.distributor_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";
	WhereGTMCategory3 = " and distributor_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";
}


//System.out.println("Hello "+WhereCustomerID);

//Region
String RegionIDs="";
long SelectedRegionArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRegion") != null){
	SelectedRegionArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRegion");
	RegionIDs = Utilities.serializeForSQL(SelectedRegionArray);
}


Region[] RegionObj = UserAccess.getUserFeatureRegion(SessionUserID, FeatureID);
String ScopeRegionIDs = UserAccess.getRegionQueryString(RegionObj);


String WhereRegion = "";
if (RegionIDs.length() > 0){
	WhereRegion = " and region_id in ("+RegionIDs+") ";	
}



%>





<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Per Case Discounts</li>
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
							
							
							ResultSet rs = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
							while(rs.next()){
								%>
								<th data-priority="1"  style="text-align:center; " colspan="<%=rs.getInt("packge_count")%>"><%=rs.getString("type_name") %></th>
								<%
							}
							
							%>
												
					    </tr>
					    
					    <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center; ">Outlet</th>
							<th data-priority="1"  style="text-align:center; ">Address</th>
							
							<%
							int ArrayCount=0;
							
							ResultSet rs1 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
							while(rs1.next()){
								
								ResultSet rs2 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+rs1.getInt("product_type_id")+WherePackage);
								while(rs2.next()){
							%>
								<th data-priority="1"  style="text-align:center; "><%=rs2.getString("package_label") %></th>
							<%
							ArrayCount++;
								}
							}
							%>
							
							<!-- <th data-priority="1"  style="text-align:center; ">Total</th> -->
					    </tr>					    
					  </thead> 
					<tbody>						
						<%
						
						String Sql = "SELECT id, name, address FROM common_outlets where id in (select outlet_id from sampling where active = 1) and region_id in ("+ScopeRegionIDs+") "+WhereDistributorID+WhereHOD1+WhereRSM1+WhereTDM+WhereRegion;						//System.out.println(Sql);
						ResultSet rs2 = s.executeQuery(Sql);
						while(rs2.next()){
							%>
							<tr>
								<td><%=Utilities.truncateStringToMax(rs2.getString("id")+" - "+rs2.getString("name"),30)%></td>
								<td><%=Utilities.truncateStringToMax(rs2.getString("address"),30)%></td>
							<%
							//double TotalCompanyShare = 0;
							ResultSet rs3 = s3.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
							while(rs3.next()){								
								ResultSet rs4 = s4.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+rs3.getInt("product_type_id")+WherePackage);
								while(rs4.next()){
									
									double CompanyShare = 0;
									
									//System.out.println("SELECT company_share FROM sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where s.outlet_id = "+rs2.getString("id")+" and s.active = 1 and curdate() between sp.valid_from and sp.valid_to and package = "+rs4.getString("package_id")+" and (brand_id in (select distinct brand_id from inventory_products where lrb_type_id = "+rs3.getInt("product_type_id")+") or brand_id = 0) limit 1");
									ResultSet rs5 = s5.executeQuery("SELECT company_share FROM sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where s.outlet_id = "+rs2.getString("id")+" and s.active = 1 and curdate() between sp.valid_from and sp.valid_to and package = "+rs4.getString("package_id")+" and (brand_id in (select distinct brand_id from inventory_products where lrb_type_id = "+rs3.getInt("product_type_id")+") or brand_id = 0) limit 1");
									if(rs5.first()){
										CompanyShare = rs5.getDouble("company_share");
										//TotalCompanyShare += CompanyShare;
									}
									%>
										<td style="text-align: right;"><% if(CompanyShare > 0) out.print(CompanyShare); %></td>
									<%
								}	
							}
							%>
								<!-- <td style="text-align: right;"><% // if(TotalCompanyShare > 0) out.print(Utilities.getDisplayCurrencyFormatRounded(TotalCompanyShare)); %></td> -->
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
s5.close();
s4.close();
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>