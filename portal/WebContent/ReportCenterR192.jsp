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
<%@page import="com.pbc.common.Region"%>

<script src="js/lookups.js"></script>
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
int FeatureID = 232;

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
Statement s5 = c.createStatement();
Statement s6 = c.createStatement();

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
	WherePackage = " and ip.package_id in ("+PackageIDs+") ";
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
	WhereHOD = " and ect.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";
	WhereHOD1 = " and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";
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
	WhereRSM = " and ect.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
	WhereRSM1 = " and distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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
String WhereTDM1 = "";
if (TDMIDs.length() > 0){
	WhereTDM = " and ect.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";
	WhereTDM1 = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";
}

//region

Region [] RegionObj = UserAccess.getUserFeatureRegion(SessionUserID, FeatureID);
String RegionIDs = UserAccess.getRegionQueryString(RegionObj);

long SelectedRegionArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRegion") != null){
	SelectedRegionArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRegion");
	RegionIDs = Utilities.serializeForSQL(SelectedRegionArray);
}

String WhereRegion = "";
String WhereRegion1 = "";
if (RegionIDs.length() > 0){
	WhereRegion = " and ect.distributor_id in (SELECT distributor_id FROM common_distributors where region_id in ("+RegionIDs+")) ";	
	WhereRegion1 = " and distributor_id in (SELECT distributor_id FROM common_distributors where region_id in ("+RegionIDs+")) ";	
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


//Sales Type


String AccountTypeIDs="";
long SelectedAccountTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedAccountType") != null){
	SelectedAccountTypeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedAccountType");
	AccountTypeIDs = Utilities.serializeForSQL(SelectedAccountTypeArray);
}

String WhereSalesType = "";
if (AccountTypeIDs.length() > 0){
	WhereSalesType = " and type_id in ("+AccountTypeIDs+")";	
}

//customer filter

long DistributorID = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
	DistributorID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
}
String WhereCustomerID ="";

if (DistributorID != 0){
	WhereCustomerID = " and distributor_id in ("+DistributorID+") ";

}

//System.out.println("Hello "+WhereCustomerID);

//EmptyLossType


String EmptyLossTypeIDs="";
long SelectedEmptyLossTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedEmptyLossType") != null){
	SelectedEmptyLossTypeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedEmptyLossType");
	EmptyLossTypeIDs = Utilities.serializeForSQL(SelectedEmptyLossTypeArray);
}

String WhereEmptyLossType = "";
String WhereEmptyLossType1 = "";
if (EmptyLossTypeIDs.length() > 0){
	WhereEmptyLossType = " and type_id in ("+EmptyLossTypeIDs+") ";	
	WhereEmptyLossType1 = " and ect.type_id in ("+EmptyLossTypeIDs+") ";	
}


//Brand

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
	WhereBrand = " and ip.brand_id in ("+BrandIDs+") ";
}
%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 
					
					 
					<tbody>
						
						
						<thead>
					 
					    <tr style="font-size:11px;">							
							
							
							<th data-priority="1"  style="text-align:center;  width:15%"></th>
							
							<%
							int ArrayCount=0;
							//for array count
							ResultSet rs6 = s.executeQuery("SELECT count(distinct ip.brand_id) count,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2) and package_id not in (10) group by ip.package_id order by package_id desc");
							while(rs6.next()){
								ArrayCount++;
							}
							
							int PackageBrandArray [] = new int[ArrayCount];
							
							
							%>
							
							
							
							<%
							int co=0;
							ResultSet rs = s.executeQuery("SELECT count(distinct ip.brand_id) count,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2) and package_id not in (10) group by ip.package_id order by package_id desc");
							while(rs.next()){
								
								PackageBrandArray[co]=rs.getInt("count");
							%>
							<th data-priority="1"  style="text-align:center; " colspan="<%=rs.getInt("count")+1%>"><%=rs.getString("package_label") %></th>
																
							<%
							co++;
							}
							
							
							//System.out.println(PackageBrandArray[0]+" - "+PackageBrandArray[1]+" - "+PackageBrandArray[2]);
							%>
							
							 </tr>
							 <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center;  width:15%"></th>
							<%
							int counter=1;
							int ix=0;
							ResultSet rs5 = s.executeQuery("SELECT distinct ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_label ,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2) and package_id not in (10) order by ip.brand_id");
							while(rs5.next()){
						%>
							<th data-priority="1"  style="text-align:center; "><%=rs5.getString("brand_label") %></th>
							<%
							
								if(counter==PackageBrandArray[ix]){
									%>
									<th data-priority="1"  style="text-align:center; ">Total</th>
									<%
									counter=0;
									ix++;
								}
							counter++;
							}
							%>
							
							
													
					    </tr>
					    
					  </thead> 
						
						
						<tbody>
						<tr>
							<th>Opening Balance</th>
							<%
							
							long OpeningBalance = 0;
							
							int UnitPerSkuOpening=0;
							
							int counter12=1;
							int ix12=0;
								ResultSet rs22 = s2.executeQuery("select id as package_id from inventory_packages where id in (SELECT ip.package_id FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2) and package_id not in (10) group by ip.package_id) order by id desc");
								while(rs22.next()){
									
									//System.out.println("select id, label from inventory_brands ib where ib.id in(SELECT distinct ip.brand_id FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where ip.package_id ="+rs2.getLong("package_id")+")");
									
									ResultSet rs51 = s5.executeQuery("SELECT id as brand_id,label from inventory_brands where id in( select distinct ip.brand_id FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where  ip.package_id ="+rs22.getLong("package_id") +" order by package_id )");
									while(rs51.next()){
									//System.out.println("select id, label from inventory_brands ib where ib.id in(SELECT distinct ip.brand_id FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where ip.package_id ="+rs2.getLong("package_id")+")");
									long Balance=0;
									int UnitPerSku=0;
									ResultSet rs3 = s3.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= "+rs22.getLong("package_id")+" and ip.brand_id="+rs51.getLong("brand_id")+" and ect.type_id in(1,5) and  ect.created_on_date < "+Utilities.getSQLDate(StartDate)+WhereHOD+WhereRSM+WhereTDM+WhereRegion+" order by ip.package_id desc");
									if(rs3.first()){
										OpeningBalance = rs3.getLong(1) - rs3.getLong(2);
										UnitPerSkuOpening = rs3.getInt(3);
									}
									
									Balance = OpeningBalance;
									
									String OpeningBalance1="";
									
									if(OpeningBalance>=0){ 
										OpeningBalance1 = Utilities.convertToRawCases(OpeningBalance,UnitPerSkuOpening);
										}
									if(OpeningBalance<0){ 
										OpeningBalance1 = Utilities.convertToRawCases(OpeningBalance*-1,UnitPerSkuOpening);
										}
									if(OpeningBalance!=0){ 
										OpeningBalance1 = Utilities.convertToRawCasesAccounting(OpeningBalance,UnitPerSkuOpening);
										}
										
									
									
								%>
								<td style="text-align:right;"><%=OpeningBalance1 %></td>
								<%
								if(counter12==PackageBrandArray[ix12]){
									
									ResultSet rs12 = s6.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= "+rs22.getLong("package_id")+" and ect.type_id in(1,5) and  ect.created_on_date < "+Utilities.getSQLDate(StartDate)+WhereHOD+WhereRSM+WhereTDM+WhereRegion+" order by ip.package_id desc");
									if(rs12.first()){
										OpeningBalance = rs12.getLong(1) - rs12.getLong(2);
										UnitPerSkuOpening = rs12.getInt(3);
									
										
										if(OpeningBalance>=0){ 
											OpeningBalance1 = Utilities.convertToRawCases(OpeningBalance,UnitPerSkuOpening);
											}
										if(OpeningBalance<0){ 
											OpeningBalance1 = Utilities.convertToRawCases(OpeningBalance*-1,UnitPerSkuOpening);
											}
										if(OpeningBalance!=0){ 
											OpeningBalance1 = Utilities.convertToRawCasesAccounting(OpeningBalance,UnitPerSkuOpening);
											}
										
									%>
									<td style="text-align:right;"><%=OpeningBalance1 %></td>
									<%
									counter12=0;
									ix12++;
									}
								}
							counter12++;
								
								%>
								
								
																	
								<%
								
									}
									
									
								}
								
								
							%>
						</tr>
						
						<%
						
						//System.out.println("SELECT distinct distributor_id,(select name from common_distributors cd where cd.distributor_id=ect.distributor_id) name FROM ec_transactions ect where 1=1 "+WhereHOD+WhereRSM+WhereTDM+WhereRegion);
						boolean flag=true;
						
							int counter1=1;
							int ix1=0;
							%>
							<tr>
								<th>Issued</th>
								<%
								ResultSet rs2 = s2.executeQuery("select id as package_id from inventory_packages where id in (SELECT ip.package_id FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2) and package_id not in (10) group by ip.package_id) order by id desc");
								while(rs2.next()){
									
									//System.out.println("select id, label from inventory_brands ib where ib.id in(SELECT distinct ip.brand_id FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where ip.package_id ="+rs2.getLong("package_id")+")");
									
									ResultSet rs51 = s5.executeQuery("SELECT id as brand_id,label from inventory_brands where id in( select distinct ip.brand_id FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where  ip.package_id ="+rs2.getLong("package_id") +" order by package_id )");
									while(rs51.next()){
									//System.out.println("select id, label from inventory_brands ib where ib.id in(SELECT distinct ip.brand_id FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where ip.package_id ="+rs2.getLong("package_id")+")");
									long Balance=0;
									int UnitPerSku=0;
									ResultSet rs3 = s3.executeQuery("Select sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= "+rs2.getLong("package_id")+" and ip.brand_id="+rs51.getLong("brand_id")+" and ect.type_id in(1,5) and ect.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereHOD+WhereRSM+WhereTDM+WhereRegion+" order by ip.package_id desc");
									if(rs3.first()){
										 Balance = rs3.getLong(1);
										 UnitPerSku = rs3.getInt(2);
									}	
								%>
								<td style="text-align:right;"><%if(Balance!=0){ %><%=Utilities.convertToRawCasesAccounting(Balance,UnitPerSku) %><%} %></td>
								<%
								if(counter1==PackageBrandArray[ix1]){
									
									ResultSet rs12 = s6.executeQuery("Select sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= "+rs2.getLong("package_id")+" and ect.type_id in(1,5) and ect.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereHOD+WhereRSM+WhereTDM+WhereRegion+" order by ip.package_id desc");
									if(rs12.first()){
										Balance = rs12.getLong(1);
										 UnitPerSku = rs12.getInt(2);
									
									%>
									<td style="text-align:right;"><%if(Balance!=0){ %><%=Utilities.convertToRawCasesAccounting(Balance,UnitPerSku) %><%} %></td>
									<%
									counter1=0;
									ix1++;
									}
								}
							counter1++;
								
								%>
								
								
																	
								<%
								
									}
									
									
								}
								
								
							%>
							
							</tr>
							<tr>
							<%
							int counter11=1;
							int ix11=0;
							%>
								<th>Received</th>
								<%
								ResultSet rs21 = s2.executeQuery("select id as package_id from inventory_packages where id in (SELECT ip.package_id FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2) and package_id not in (10) group by ip.package_id) order by id desc");
								while(rs21.next()){
									
									//System.out.println("select id, label from inventory_brands ib where ib.id in(SELECT distinct ip.brand_id FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where ip.package_id ="+rs21.getLong("package_id")+")");
									
									ResultSet rs51 = s5.executeQuery("SELECT id as brand_id,label from inventory_brands where id in( select distinct ip.brand_id FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where  ip.package_id ="+rs21.getLong("package_id") +" order by package_id )");
									while(rs51.next()){
									//System.out.println("select id, label from inventory_brands ib where ib.id in(SELECT distinct ip.brand_id FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where ip.package_id ="+rs2.getLong("package_id")+")");
									long Balance=0;
									int UnitPerSku=0;
									ResultSet rs3 = s3.executeQuery("Select sum(total_units_received),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= "+rs21.getLong("package_id")+" and ip.brand_id="+rs51.getLong("brand_id")+" and ect.type_id in(1,5) and ect.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereHOD+WhereRSM+WhereTDM+WhereRegion+" order by ip.package_id desc");
									if(rs3.first()){
										 Balance = rs3.getLong(1);
										 UnitPerSku = rs3.getInt(2);
									}	
								%>
								<td style="text-align:right;"><%if(Balance!=0){ %><%=Utilities.convertToRawCasesAccounting(Balance,UnitPerSku) %><%} %></td>
								<%
								if(counter11==PackageBrandArray[ix11]){
									
									ResultSet rs12 = s6.executeQuery("Select sum(total_units_received),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= "+rs21.getLong("package_id")+" and ect.type_id in(1,5) and ect.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereHOD+WhereRSM+WhereTDM+WhereRegion+" order by ip.package_id desc");
									if(rs12.first()){
										Balance = rs12.getLong(1);
										 UnitPerSku = rs12.getInt(2);
									
									%>
									<td style="text-align:right;"><%if(Balance!=0){ %><%=Utilities.convertToRawCasesAccounting(Balance,UnitPerSku) %><%} %></td>
									<%
									counter11=0;
									ix11++;
									}
								}
							counter11++;
								
								%>
								
								
																	
								<%
								
									}
									
									
								}
								
								
							%>
							
							</tr>
						<tr>
							<th>Closing Balance</th>
							<%
							
							long ClosingBalance = 0;
							
							int UnitPerSkuClosing=0;
							
							int counter121=1;
							int ix121=0;
								ResultSet rs221 = s2.executeQuery("select id as package_id from inventory_packages where id in (SELECT ip.package_id FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2) and package_id not in (10) group by ip.package_id) order by id desc");
								while(rs221.next()){
									
									//System.out.println("select id, label from inventory_brands ib where ib.id in(SELECT distinct ip.brand_id FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where ip.package_id ="+rs2.getLong("package_id")+")");
									
									ResultSet rs51 = s5.executeQuery("SELECT id as brand_id,label from inventory_brands where id in( select distinct ip.brand_id FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where  ip.package_id ="+rs221.getLong("package_id") +" order by package_id )");
									while(rs51.next()){
									//System.out.println("select id, label from inventory_brands ib where ib.id in(SELECT distinct ip.brand_id FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where ip.package_id ="+rs2.getLong("package_id")+")");
									long Balance=0;
									int UnitPerSku=0;
									ResultSet rs3 = s3.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= "+rs221.getLong("package_id")+" and ip.brand_id="+rs51.getLong("brand_id")+" and ect.type_id in(1,5) and  ect.created_on_date < "+Utilities.getSQLDateNext(EndDate)+WhereHOD+WhereRSM+WhereTDM+WhereRegion+" order by ip.package_id desc");
									if(rs3.first()){
										ClosingBalance = rs3.getLong(1) - rs3.getLong(2);
										UnitPerSkuClosing = rs3.getInt(3);
									}
									
									Balance = ClosingBalance;
									
									String ClosingBalance1="";
									
									if(ClosingBalance>=0){ 
										ClosingBalance1 = Utilities.convertToRawCases(ClosingBalance,UnitPerSkuClosing);
										}
									if(ClosingBalance<0){ 
										ClosingBalance1 = Utilities.convertToRawCases(ClosingBalance*-1,UnitPerSkuClosing);
										}
									if(ClosingBalance!=0){ 
										ClosingBalance1 = Utilities.convertToRawCasesAccounting(ClosingBalance,UnitPerSkuClosing);
										}
										
									
									
								%>
								<td style="text-align:right;"><%=ClosingBalance1 %></td>
								<%
								if(counter121==PackageBrandArray[ix121]){
									
									ResultSet rs12 = s6.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= "+rs221.getLong("package_id")+" and ect.type_id in(1,5) and  ect.created_on_date < "+Utilities.getSQLDateNext(EndDate)+WhereHOD+WhereRSM+WhereTDM+WhereRegion+" order by ip.package_id desc");
									if(rs12.first()){
										ClosingBalance = rs12.getLong(1) - rs12.getLong(2);
										UnitPerSkuClosing = rs12.getInt(3);
									
										
										if(ClosingBalance>=0){ 
											ClosingBalance1 = Utilities.convertToRawCases(ClosingBalance,UnitPerSkuClosing);
											}
										if(ClosingBalance<0){ 
											ClosingBalance1 = Utilities.convertToRawCases(ClosingBalance*-1,UnitPerSkuClosing);
											}
										if(ClosingBalance!=0){ 
											ClosingBalance1 = Utilities.convertToRawCasesAccounting(ClosingBalance,UnitPerSkuClosing);
											}
										
									%>
									<td style="text-align:right;"><%=ClosingBalance1 %></td>
									<%
									counter121=0;
									ix121++;
									}
								}
							counter121++;
								
								%>
								
								
																	
								<%
								
									}
									
									
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