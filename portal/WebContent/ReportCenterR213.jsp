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
int FeatureID = 267;

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



String SecondaryDistributorString="";
int SecondaryDistributor=0;

String WhereSecDistributor="";

SecondaryDistributorString=(String)session.getAttribute("UserDistributorID");
SecondaryDistributor = Utilities.parseInt(SecondaryDistributorString);


%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<tr >
			
			
			
				<td style="font-size:14px;"><b>Date:</b>&nbsp;&nbsp;<%=Utilities.getDisplayDateFormat(StartDate) %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 
					
					 
					<tbody>
						
						
						<thead>
					 
					    <tr style="font-size:11px;">							
							
							
							<th data-priority="1"  style="text-align:center;  width:25%"></th>
							
							<%
							int ArrayCount=0;
							//for array count
							ResultSet rs6 = s.executeQuery("SELECT count(distinct ip.brand_id) count,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3,4) and package_id not in (10) and ip.is_other_brand !=1"+WherePackage+" group by ip.package_id order by package_id desc");
							while(rs6.next()){
								ArrayCount++;
							}
							
							int PackageBrandArray [] = new int[ArrayCount];
							
							
							//System.out.println(ArrayCount);
							
							%>
							
							
							
							<%
							int co=0;
							ResultSet rs = s.executeQuery("SELECT count(distinct ip.brand_id) count,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3,4) and package_id not in (10) and ip.is_other_brand !=1 "+WherePackage+" group by ip.package_id order by package_id desc");
							while(rs.next()){
								
								PackageBrandArray[co]=rs.getInt("count");
							%>
							<th data-priority="1"  style="text-align:center;abackground-color:#e6e6e6;" colspan="<%=rs.getInt("count")+1%>"><%=rs.getString("package_label") %></th>
																
							<%
							co++;
							}
							
							
							//System.out.println(PackageBrandArray[0]+" - "+PackageBrandArray[1]+" - "+PackageBrandArray[2]);
							int GrandTotalArrayCount=0;
							for(int i=0;i<PackageBrandArray.length;i++){
								GrandTotalArrayCount+=PackageBrandArray[i];
							}
							
							
							
							//System.out.println(PackageBrandArray.length);
							
							%>
							
							 </tr>
							 <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center;  width:25%"></th>
							<%
							ResultSet rs99 = s3.executeQuery("SELECT count(distinct ip.brand_id) count,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3,4) and package_id not in (10) and ip.is_other_brand !=1 "+WherePackage+" group by ip.package_id order by package_id desc");
							while(rs99.next()){							
								int counter=1;
								int ix=0;
								ResultSet rs5 = s.executeQuery("SELECT distinct ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_label ,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3,4) and package_id not in (10) and package_id="+rs99.getLong("package_id")+" and ip.is_other_brand !=1 "+WherePackage+" order by package_id desc");
								while(rs5.next()){
							%>
								<th data-priority="1"  style="text-align:center; "><%=rs5.getString("brand_label") %></th>
								
										
										<%
										
									
								}
								
								%>
								<th data-priority="1"  style="text-align:center;background-color:#F6F6F6;">Total</th>
								
								<%
								GrandTotalArrayCount++;//inc total count for each total col
							}
							
							//System.out.println(GrandTotalArrayCount);
							long GrantTotalArray[]=new long[GrandTotalArrayCount];
							int PackageUnitPerSKU[]=new int[GrandTotalArrayCount];
							
							%>
							
							
													
					    </tr>
					    
					  </thead> 
						
						
						<tbody>
						<%
						
						//System.out.println("SELECT distinct distributor_id,(select name from common_distributors cd where cd.distributor_id=ect.distributor_id) name FROM ec_transactions ect where 1=1 "+WhereHOD+WhereRSM+WhereTDM+WhereRegion);
						boolean flag=true;
						ResultSet rs1 = s.executeQuery("SELECT * FROM common_distributors where distributor_id in(select distinct distributor_id from ec_transactions) and distributor_id ="+SecondaryDistributor+WhereRegion1);
						while(rs1.next()){
							int counter1=1;
							int ix1=0;
							%>
							<tr>
								<td><%=rs1.getLong("distributor_id") %> - <%=rs1.getString("name") %></td>
								<%
								//System.out.println("SELECT distinct ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3) and package_id not in (10) and ip.is_other_brand !=1 order by package_id desc");
								int GrandCounter=0;
								
								ResultSet rs2 = s2.executeQuery("SELECT distinct ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3,4) and package_id not in (10) and ip.is_other_brand !=1 "+WherePackage+" order by package_id desc");
								while(rs2.next()){
									
									int uc=0;
									int uct=0;
									//if(rs2.getLong("package_id")==17){
									//System.out.println("SELECT distinct ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_label ,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3) and package_id not in (10) and package_id ="+rs2.getLong("package_id")+" and ip.is_other_brand !=1 order by package_id desc");
									//}
									//System.out.println("hello "+rs2.getLong("package_id"));
									
									long Balance=0;
									int UnitPerSku=0;
									
									ResultSet rs51 = s5.executeQuery("SELECT distinct ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_label ,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3,4) and package_id not in (10) and package_id ="+rs2.getLong("package_id")+" and ip.is_other_brand !=1 "+WherePackage+" order by package_id desc");
									while(rs51.next()){
									//System.out.println("select id, label from inventory_brands ib where ib.id in(SELECT distinct ip.brand_id FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where ip.package_id ="+rs2.getLong("package_id")+")");
									 Balance=0;
									 UnitPerSku=0;
									
									long BrandID = rs51.getLong("brand_id");
									
									String BrandIDModify = "";
									
									if(BrandID==1 || BrandID==37 || BrandID==38){ //pepsi
										BrandIDModify += "1,24,28,37,38"; //pepsi+coke+gourm cola
									}else if(BrandID==2){ //merinda 
										BrandIDModify += "2,25"; //mirinda+fanta
									}else if(BrandID==4){ //7up
										BrandIDModify += "4,26,29"; //7up+sprite+gourm lemon
									}else if(BrandID==5){ //Dew
										BrandIDModify += "5,27"; //7up+3g
									}else{
										BrandIDModify = rs51.getLong("brand_id")+"";
									}
									
									
									
									ResultSet rs3 = s3.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= "+rs2.getLong("package_id")+" and ip.brand_id in ("+BrandIDModify+") and ect.created_on_date <= "+Utilities.getSQLDate(StartDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13) and ect.distributor_id="+rs1.getLong("distributor_id")+" order by ip.package_id desc");
									if(rs3.first()){
										 Balance = rs3.getLong(1) - rs3.getLong(2);
										 UnitPerSku = rs3.getInt(3);
										 if(UnitPerSku!=0){
											 PackageUnitPerSKU[GrandCounter] = UnitPerSku;
										 }
										 
									}
									
									GrantTotalArray[GrandCounter]+=Balance;
									
								
										
										
									
									//if(rs1.getLong("distributor_id")==200769 ){
										//System.out.println("Normal "+GrantTotalArray[GrandCounter]+" - "+PackageUnitPerSKU[GrandCounter]);
									//}
									
									
									
									GrandCounter++;
									
									
									
									
								%>
								<td style="text-align:right;"><%if(Balance!=0){ %><%=Utilities.convertToRawCasesAccounting(Balance,UnitPerSku) %><%} %></td>
								<%
								
									%>
									
									<%
									
								}
							
								
								%>
								
								
																	
								<%
								long Balance1=0;
								 int UnitPerSku1=0;
									
																	 
									ResultSet rs12 = s6.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= "+rs2.getLong("package_id")+" and ect.created_on_date <= "+Utilities.getSQLDate(StartDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13) and ect.distributor_id="+rs1.getLong("distributor_id")+" order by ip.package_id desc");
									if(rs12.first()){
										Balance1 = rs12.getLong(1) - rs12.getLong(2);
										 UnitPerSku1 = rs12.getInt(3);
										 if(UnitPerSku1!=0){
											 PackageUnitPerSKU[GrandCounter] = UnitPerSku1; 
										 }
										 
									
									%>
									<td style="text-align:right;background-color:#F6F6F6;"><%if(Balance1!=0){ %><%=Utilities.convertToRawCasesAccounting(Balance1,UnitPerSku1) %><%} %></td>
									<%
									
									}
									
									GrantTotalArray[GrandCounter]+=Balance1;
									
									
									
										
										
									
									
									//System.out.println("Total "+GrantTotalArray[GrandCounter]+" - "+PackageUnitPerSKU[GrandCounter]);
									
									GrandCounter++;
									
									
									
									%>
									
									
									
									
									
									<%
									
									
								}
								
								
							%>
							
							</tr>
						<%	
						}
						%>
						<tr>
						<td></td>
						<%
						for(int i=0;i<GrantTotalArray.length;i++){
							
							//System.out.println(GrantTotalArray[i]+" - "+PackageUnitPerSKU[i]);
							
							%>
							<td style="text-align:right;background-color:#F6F6F6;"><%=Utilities.convertToRawCasesAccounting(GrantTotalArray[i],PackageUnitPerSKU[i]) %></td>
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