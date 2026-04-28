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
int FeatureID = 339;

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
//Date date = Utilities.parseDate(request.getParameter("Date"));


Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");
//StartDate=Utilities.parseDate("24/09/2015");
//EndDate=Utilities.parseDate("25/09/2015");

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


//Pallatize
String PalletizeTypes="";
long SelectedPalletizeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPalletize") != null){
	SelectedPalletizeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPalletize");
	PalletizeTypes = Utilities.serializeForSQL(SelectedPalletizeArray);
	//System.out.println("palateize type"+PalletizeTypes);
}

String WherePalletizeType = "";
if (PalletizeTypes.length() > 0){
	WherePalletizeType = " and idn.palletize_type_id in ("+PalletizeTypes+") ";	
	//System.out.println(WherePalletizeType);
}



//haulag Contractor
String HauContractorID="";
long SelectedHauContractorArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedHauContractor") != null){
	SelectedHauContractorArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedHauContractor");
	HauContractorID = Utilities.serializeForSQL(SelectedHauContractorArray);
	//System.out.println("palateize type"+HauContractorID);
}

String WhereHauContractorID = "";
if (HauContractorID.length() > 0){
	WhereHauContractorID = " and idn.freight_contractor_id in ("+HauContractorID+")";	
	//System.out.println(WhereHauContractorID);
}


//System.out.println("Hello "+WhereLiftingType);

///

///

int DataFlag=0;
ResultSet rs41 = s.executeQuery("SELECT created_on,outlet_id,(select name from common_outlets co where co.id=mo.outlet_id) outlet_name,distributor_id,(select name from common_distributors cd where cd.distributor_id=mo.distributor_id) distributor_name,created_by as booked_by,(select display_name from users u where u.id=mo.created_by) booked_by_name, moa.barcode FROM pep.mobile_order mo join mobile_order_assets moa on mo.unedited_order_id=moa.id where mo.created_on between"+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" ");
if (rs41.first()){
	DataFlag=1;
}

//System.out.println("SELECT distinct package_id, package_label FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id=ipv.product_id where  ipv.product_id in ("+ProductsLifted+") "+WherePalletizeType+" and category_id = 1  order by package_sort_order");
/////String CheckQuery="SELECT distinct package_id, package_label FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id=ipv.product_id where  ipv.product_id in ("+ProductsLifted+") "+WherePalletizeType+" and category_id = 1  order by package_sort_order";
///ResultSet rs11 = s.executeQuery(CheckQuery);
///if(!rs11.next()){
	//DataFlag=1;
////} 


%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
		<%if(DataFlag==1){ 
			%>
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					   <tr style="font-size:11px;">
					  	  <th data-priority="1"  style="text-align:center;" >Scanning Date</th>
					  	  <th data-priority="1"  style="text-align:center; ">Outlet</th>
		            		 <th data-priority="1"  style="text-align:center;">Distributor</th>
		            		 <th data-priority="1"  style="text-align:center;" >Booked By</th>  
							 <th data-priority="1"  style="text-align:center;" >Bar Code</th>  
							   
												
					    </tr> 
					  </thead> 
						
						<!-- New Addition-->
							
					<!-- 	//////////////////////////////////////////////////////     -->
					
					<% 
		           
		            long DistributorID=0;
		            String DistributorName="";
					long OutletID=0;
					String OutletName="";
					long BookerID=0;
					String BookerName="";
					String BarCodeDate="";
					String BarCode="";
					
					
		            String Query1="SELECT created_on,outlet_id,(select name from common_outlets co where co.id=mo.outlet_id) outlet_name,distributor_id,(select name from common_distributors cd where cd.distributor_id=mo.distributor_id) distributor_name,created_by as booked_by,(select display_name from users u where u.id=mo.created_by) booked_by_name, moa.barcode FROM pep.mobile_order mo join mobile_order_assets moa on mo.unedited_order_id=moa.id where mo.created_on between"+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" ";
		            //System.out.println(Query1);
		            ResultSet rs1 = s2.executeQuery(Query1);
					while(rs1.next()){
						
					   	DistributorID=rs1.getLong("distributor_id");
			            DistributorName= rs1.getString("distributor_name");
						OutletID=rs1.getLong("outlet_id");
						OutletName= rs1.getString("outlet_name");
						BookerID=rs1.getLong("booked_by");
						BookerName= rs1.getString("booked_by_name");
						BarCodeDate=Utilities.getDisplayDateFormat(rs1.getDate("created_on"));
						BarCode=rs1.getString("barcode");
						
					
							
						%>
						<tr>
						<td> <%=BarCodeDate %></td>		
			         		<td> <%=OutletID%>-<%=OutletName %>  </td>
			         		<td> <%=DistributorID%>-<%=DistributorName %></td>
			         		<td> <%=BookerID%>-<%=BookerName %>  </td>
			         		<td> <%=BarCode%></td>
			         		
						 </tr>
						<%		
								
						} /* s2 while loop ends here  */
			             %>	
						<!-- New Addition Ends -->	
				</table>
		</td>
	</tr>
</table>
<%
		}else{
			%>
			<span style="font-size:13px; text-decoration:none; padding-top:5px;">No Data is Available.</span>
			<% 		
			}
			%>		

	</li>	
</ul>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>