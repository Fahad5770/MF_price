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
int FeatureID = 340;

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


String DistributorOrderStatusID="";
long SelectedDistributorOrderStatusArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorOrderStatus") != null){
	SelectedDistributorOrderStatusArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorOrderStatus");
	DistributorOrderStatusID = Utilities.serializeForSQL(SelectedDistributorOrderStatusArray);
}

String WhereDistributorStatusOrder = "";
if (DistributorOrderStatusID.length() > 0){
	WhereDistributorStatusOrder = "  and status_type_id in ("+DistributorOrderStatusID+") ";
	//System.out.println("WhereDistributorStatusOrder "+WhereDistributorStatusOrder);
}


//System.out.println("Hello "+WhereLiftingType);

///

///

int DataFlag=0;
ResultSet rs41 = s.executeQuery("SELECT id as OrderNumber,created_on as Date,Distributor_id,sap_order_no FROM pep.inventory_delivery_order where  created_on between"+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+ WhereDistributorStatusOrder+" ");
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
					  	  <th data-priority="1"  style="text-align:center; ">Order #</th>
		            		 <th data-priority="1"  style="text-align:center;">Date</th>
		            		 <th data-priority="1"  style="text-align:center;" >Distributor</th>  
							 <th data-priority="1"  style="text-align:center;" >Sap order #</th>  
							 <th data-priority="1"  style="text-align:center;" >Status</th>  
												
					    </tr> 
					  </thead> 
						
						<!-- New Addition-->
							
					<!-- 	//////////////////////////////////////////////////////     -->
					
					<% 
		           
		            long DistributorID=0;
		            String DistributorName="";
					long OrderNum=0;
					String Date="";
					long Saporder=0;
					String Status="";
					int counter=0;
					
					
					
		            String Query1="SELECT ido.id as OrderNumber,ido.created_on as Date,ido.status_type_id,(SELECT Label FROM pep.inventory_delivery_order_type idot where idot.id=ido.status_type_id ) status,ido.Distributor_id,(select distinct(name)  from common_distributors cd where cd.distributor_id=ido.Distributor_id) distributor_name,ido.sap_order_no FROM pep.inventory_delivery_order ido where   ido.created_on between"+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+ WhereDistributorStatusOrder+" ";
		           // System.out.println(Query1);
		            ResultSet rs1 = s2.executeQuery(Query1);
					while(rs1.next()){
						
					   	DistributorID=rs1.getLong("Distributor_id");
			            DistributorName= rs1.getString("distributor_name");
			            OrderNum=rs1.getLong("OrderNumber");
						Saporder=rs1.getLong("sap_order_no");
						Status= rs1.getString("status");
						Date=Utilities.getDisplayDateFormat(rs1.getDate("Date"));
						
							
						%>
						<tr>
			         		<td> <%=OrderNum%>  </td>
			         		<td> <%=Date %>  </td>
			         		<td> <%=DistributorID%>-<%=DistributorName %></td>
			         		<td> <%if(Saporder!=0){ %><%=Saporder %> <%}%></td>
			         		<td> <%=Status %></td>		
						 </tr>
						<%		
						counter++;		
						} /* s2 while loop ends here  */
			             %>	
			             <tr>
			         		<td  style="text-align:left"><b> Total Orders</b>  </td>
			         		
			         		<td style="text-align:right" > <b> <%=counter %></b></td>
			         		<td style="text-align:right" colspan="3"> </td>		
						 </tr>
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