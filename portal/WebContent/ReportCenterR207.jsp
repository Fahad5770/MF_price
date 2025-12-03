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
int FeatureID = 259;

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
	WhereRegion = " and distributor_id in (SELECT distributor_id FROM common_distributors where region_id in ("+RegionIDs+")) ";	
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





//System.out.println("Helllo "+DistributorIDs);

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	
			<tr >
			
			<%
			String RegionLabel="";
			ResultSet rs34 = s.executeQuery("select * from common_regions where region_id in("+RegionIDs+")");
			while(rs34.next()){
				RegionLabel += rs34.getString("region_short_name")+" - "+rs34.getString("region_name")+"&nbsp;&nbsp;&nbsp;";
			}
			%>
			
				<td style="font-size:14px;"><b>Date:</b>&nbsp;&nbsp;<%=Utilities.getDisplayDateFormat(StartDate) %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Region: </b><%=RegionLabel %></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			
			
	
	
	
</table>



<table style="border=0; font-size:11px; f1ont-weight: 400;"cellpadding="0" cellspacing="0"   class="GridWithBorder">
					 
					
					 
					<tbody>
						
						
						<thead>
					 
					    <tr style="font-size:11px;">							
							
							
							<th data-priority="1"  style="text-align:center; "></th>
							
							<%
							int ArrayCount=0;
							//for array count
							ResultSet rs6 = s.executeQuery("SELECT count(distinct ip.brand_id) count,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3) and package_id  in (11,12,17) and ip.is_other_brand !=1"+WherePackage+" group by ip.package_id order by package_id desc");
							while(rs6.next()){
								ArrayCount++;
							}
							
							int PackageBrandArray [] = new int[ArrayCount];
							
							
							//System.out.println(ArrayCount);
							
							%>
							
							
							
							<%
							int co=0;
							ResultSet rs = s.executeQuery("SELECT count(distinct ip.brand_id) count,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3) and package_id  in (11,12,17) and ip.is_other_brand !=1 "+WherePackage+" group by ip.package_id order by package_id desc");
							while(rs.next()){
								
								PackageBrandArray[co]=rs.getInt("count");
							%>
							<th data-priority="1"  style="text-align:center;abackground-color:#e6e6e6; " colspan="3"><%=rs.getString("package_label") %></th>
																
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
							
							<%
							ResultSet rs99 = s3.executeQuery("SELECT count(distinct ip.brand_id) count,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3) and package_id  in (11,12,17) and ip.is_other_brand !=1 "+WherePackage+" group by ip.package_id order by package_id desc");
							while(rs99.next()){							
								int counter=1;
								int ix=0;
								
							%>
								
								
										
									
							
								
								<%
								GrandTotalArrayCount++;//inc total count for each total col
							}
							
							//System.out.println(GrandTotalArrayCount);
							long GrantTotalArray[]=new long[GrandTotalArrayCount];
							int PackageUnitPerSKU[]=new int[GrandTotalArrayCount];
							
							%>
							
							 </tr>
							<tr>
								<td style="width:37%;"></td>
								<td style="text-align:center; width:7%">Balance</td>
								<td style="text-align:center;width7%">Approved Limit</td>
								<td style="text-align:center;width:7%">Available Limit</td>
								<td style="text-align:center; width:7%">Balance</td>
								<td style="text-align:center;width:7%">Approved Limit</td>
								<td style="text-align:center;width:7%">Available Limit</td>
								<td style="text-align:center; width:7%">Balance</td>
								<td style="text-align:center;width:7%">Approved Limit</td>
								<td style="text-align:center;width:7%">Available Limit</td>
							
							</tr>
					    
					  </thead> 
						
						
						<tbody>
						<%
						
						//System.out.println(StartDate);
						
						//System.out.println("SELECT distinct distributor_id,(select name from common_distributors cd where cd.distributor_id=ect.distributor_id) name FROM ec_transactions ect where 1=1 "+WhereHOD+WhereRSM+WhereTDM+WhereRegion);
						boolean flag=true;
						ResultSet rs1 = s.executeQuery("SELECT *,(Select sum(total_units_received)-sum(total_units_issued) balance11  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 11 and ect.created_on_date <= "+Utilities.getSQLDate(StartDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id=cd.distributor_id order by ip.package_id desc) balance11,(Select sum(total_units_received)-sum(total_units_issued) balance11  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 12 and ect.created_on_date <= "+Utilities.getSQLDate(StartDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id=cd.distributor_id order by ip.package_id desc) balance12 ,(Select sum(total_units_received)-sum(total_units_issued) balance11  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 17 and ect.created_on_date <= "+Utilities.getSQLDate(StartDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id=cd.distributor_id order by ip.package_id desc) balance17 FROM common_distributors cd where cd.distributor_id in(select distinct distributor_id from ec_transactions)  "+WhereDistributors+WhereRegion+"  having (balance11<0 || balance12<0 || balance17<0) ");
													  // "SELECT *,(Select sum(total_units_received)-sum(total_units_issued) balance11  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 11 and ect.created_on_date <= "+Utilities.getSQLDate(StartDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id=cd.distributor_id order by ip.package_id desc) balance11,(Select sum(total_units_received)-sum(total_units_issued) balance11  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 12 and ect.created_on_date <= "+Utilities.getSQLDate(StartDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id=cd.distributor_id order by ip.package_id desc) balance12 ,(Select sum(total_units_received)-sum(total_units_issued) balance11  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 17 and ect.created_on_date <= "+Utilities.getSQLDate(StartDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id=cd.distributor_id order by ip.package_id desc) balance17 FROM common_distributors cd where cd.distributor_id in(select distinct distributor_id from ec_transactions)  and distributor_id="+DistributorID1+" and distributor_id in (SELECT distributor_id FROM common_distributors where region_id in (1,2,3,4,5,6,7,8,9,10,11)) having (balance11<0 || balance12<0 || balance17<0) "
						
						while(rs1.next()){
							int counter1=1;
							int ix1=0;
							%>
							<tr>
								<td><%=rs1.getLong("distributor_id") %> - <%=rs1.getString("name") %></td>
								<td style="text-align:right;"><%if(rs1.getLong("balance17")!=0){%><%=Utilities.convertToRawCasesAccounting(rs1.getLong("balance17"),1) %><%} %></td>
								
								<%
								long LimitShell = 0;
								ResultSet rs12 = s2.executeQuery("SELECT ecl.id,ecl.distributor_id,(select name from common_distributors cd where cd.distributor_id=ecl.distributor_id) dist_name,ecl.credit_type,(select label from  ec_empty_credit_types ect where ect.id=ecl.credit_type) credit_type,ecl.reason,ecl.start_date,ecl.end_date,sum(raw_cases) limitt FROM ec_empty_credit_limit ecl join ec_empty_credit_limit_products eclp on ecl.id=eclp.id  where ecl.is_active=1 and curdate() between ecl.start_date and ecl.end_date and distributor_id="+rs1.getLong("distributor_id")+" and package_id=17");
								if(rs12.first()){
									LimitShell = rs12.getLong("limitt");
								}
								%>
									<td style="text-align:right;"><%if(LimitShell!=0){%><%=LimitShell %><%} %></td>
									
								<%
								//}
								
								long Overlimit17 = rs1.getLong("balance17") + LimitShell;
								
								
								%>
								
								<td style="text-align:right;"><%if(Overlimit17!=0){%><%=Utilities.convertToRawCasesAccounting(Overlimit17,1) %><%} %></td>
								
								
								<td style="text-align:right;"><%if(rs1.getLong("balance12")!=0){%><%=Utilities.convertToRawCasesAccounting(rs1.getLong("balance12"),24) %><%} %></td>
								<%
								long Limit240 = 0;
								ResultSet rs13 = s2.executeQuery("SELECT ecl.id,ecl.distributor_id,(select name from common_distributors cd where cd.distributor_id=ecl.distributor_id) dist_name,ecl.credit_type,(select label from  ec_empty_credit_types ect where ect.id=ecl.credit_type) credit_type,ecl.reason,ecl.start_date,ecl.end_date,sum(raw_cases) limitt FROM ec_empty_credit_limit ecl join ec_empty_credit_limit_products eclp on ecl.id=eclp.id  where ecl.is_active=1 and curdate() between ecl.start_date and ecl.end_date and distributor_id="+rs1.getLong("distributor_id")+" and package_id=12");
								if(rs13.first()){
									Limit240 = rs13.getLong("limitt");
								}
								%>
									
									<td style="text-align:right;"><%if(Limit240!=0){%><%=Limit240 %><%} %></td>
									
								<%
								//}
								
								long Overlimit12 = rs1.getLong("balance12") + Limit240*24;
								
								%>
								<td style="text-align:right;"><%if(Overlimit12!=0){%><%=Utilities.convertToRawCasesAccounting(Overlimit12,24) %><%} %></td>
								
								
								
								
								
								<td style="text-align:right;"><%if(rs1.getLong("balance11")!=0){%><%=Utilities.convertToRawCasesAccounting(rs1.getLong("balance11"),24) %><%} %></td>
								<%
								long Limit250 = 0;
								ResultSet rs14 = s2.executeQuery("SELECT ecl.id,ecl.distributor_id,(select name from common_distributors cd where cd.distributor_id=ecl.distributor_id) dist_name,ecl.credit_type,(select label from  ec_empty_credit_types ect where ect.id=ecl.credit_type) credit_type,ecl.reason,ecl.start_date,ecl.end_date,sum(raw_cases) limitt FROM ec_empty_credit_limit ecl join ec_empty_credit_limit_products eclp on ecl.id=eclp.id  where ecl.is_active=1 and curdate() between ecl.start_date and ecl.end_date and distributor_id="+rs1.getLong("distributor_id")+" and package_id=11");
								if(rs14.first()){
									Limit250 = rs14.getLong("limitt");
								}
								%>
									
									<td style="text-align:right;"><%if(Limit250!=0){%><%=Limit250 %><%} %></td>
									
								<%
								//}
								
								
								long Overlimit11 = rs1.getLong("balance11") + Limit250*24;
								%>
							
							<td style="text-align:right;"><%if(Overlimit11!=0){%><%=Utilities.convertToRawCasesAccounting(Overlimit11,24) %><%} %></td>
							</tr>
						<%	
						}
						%>
						
						<!-- 
						
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
						 -->
					</tbody>
					
							
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