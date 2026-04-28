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
int FeatureID = 325;

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

//outlet
boolean IsOutletSelected=false;
String OutletIds="";
long SelectedOutletArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets") != null){
	IsOutletSelected=true;
	SelectedOutletArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets");
	OutletIds = Utilities.serializeForSQL(SelectedOutletArray);
}

String WhereOutlets = "";
if (OutletIds.length() > 0){
	WhereOutlets = " and id in ("+OutletIds+") ";	
	System.out.println(WhereOutlets);
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
boolean IsDistributorSelected=false;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){

	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors"); 
	IsDistributorSelected = true;
}else{
}

String DistributorIDs = "";
String WhereDistributorID="";

if(SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0){
	for(int i = 0; i < SelectedDistributorsArray.length ; i++){
		
		if(i == 0){
			DistributorIDs += SelectedDistributorsArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorsArray[i];
		}
	}
	WhereDistributorID = " and distributor_id in ("+DistributorIDs+") ";
	
}


//customer filter

long DistributorID1 = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
	DistributorID1 = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
}
String WhereCustomerID ="";

if (DistributorID1 != 0){
	WhereCustomerID = " and distributor_id in ("+DistributorID1+") ";

}

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Test</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					   
					   
					   
					    <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center;  ">SR #</th>
							<th data-priority="1"  style="text-align:center; ">Outlet</th>
						    <th data-priority="1"  style="text-align:center; ">Status</th>								
							<th data-priority="1"  style="text-align:center; ">Outlet Address</th>
							<th data-priority="1"  style="text-align:center; ">Shop Category</th>							
							<th data-priority="1"  style="text-align:center; ">Owner Name/Contact #</th>							
							<th data-priority="1"  style="text-align:center; ">CNIC No.</th>								
							<th data-priority="1"  style="text-align:center;  ">PSR SAP ID </th>
							<th data-priority="1"  style="text-align:center; ">MDE SAP ID</th>							
							<th data-priority="1"  style="text-align:center; ">MDE Contact NO.</th>
							<th data-priority="1"  style="text-align:center; ">Vehicle No </th>							
							<th data-priority="1"  style="text-align:center; ">Outlet Status</th>							
							<th data-priority="1"  style="text-align:center; ">Beat Plan</th>								
							<th data-priority="1"  style="text-align:center; ">Distribution Name & ID</th>								
							<th data-priority="1"  style="text-align:center; ">RM</th>								

					    </tr>
					  </thead> 
					<tbody>
						<%
						
						long OutletId=0;
						String OutletName="";
						String OutletAddress="";
						String Shopcategory="";
						String OwnerName="";
						String OutletContactNum="";
						String CnicNum="";
						String VehicalNum="";
						long RequestBy=0;
						long DistributorID=0;
						String DistributorName="";
						long RMID=0;
						int srno=1;
						long CRID =0;	
						long RequestBy_ =0;
						String ContactNum = "";	
						int outletStatus = 0;
						int outletStatus1 = 0;

						
						ResultSet rs = s.executeQuery("SELECT *,(select name from common_distributors cd where cd.distributor_id=cor.distributor_id) distributor_name,(select label from mrd_census_sub_channel mcsc where mcsc.id=cor.sub_channel_id) sub_channel,(select label from common_outlets_vpo_classifications vpo_class where vpo_class.id=cor.vpo_classifications_id) vpo_classification, (select id from common_outlets co where co.kpo_request_id=cor.id) outlet_id_final FROM pep.common_outlets_request cor where cor.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+"");
						while(rs.next()){

							 OutletId = rs.getLong("outlet_id_final");
							 
							 outletStatus = rs.getInt("is_approved");
							 outletStatus1 = rs.getInt("is_declined");
							 
							 OutletName =rs.getString("outlet_name");
							 OutletAddress =rs.getString("outlet_address");
							 Shopcategory =rs.getString("shop_category"); 
							 OwnerName =rs.getString("owner_name");
							 OutletContactNum =rs.getString("owner_contact_number");					
							 CnicNum = rs.getString("owner_cnic_number");					
							 VehicalNum =rs.getString("vehicle_number");
							 RequestBy=rs.getLong("request_by");//beatplan
							 DistributorID =rs.getLong("distributor_id");
							 DistributorName=rs.getString("distributor_name");
							 RMID =rs.getLong("rm_id");
							 RequestBy_=rs.getLong("request_by");
							 ContactNum =rs.getString("requester_contact_number"); 
							 CRID =rs.getLong("cr_id");		

	%>						
								<tr> 
								<td style="width:13px"><%=srno %></td>
								<td ><%if(OutletId!=0){%><%=OutletId%><%} %> - <%if(OutletName !=null){%><%=OutletName%><%} %></td>
								
								<%
								String status = "";
								if (outletStatus1 == 1) {
								    status = "Declined";
								} else if (outletStatus == 1) {
								    status = "Approved";
								} else {
								    status = "Pending";
								} 
								%>	
												
								<td><%= status %></td>
								<td ><%if(OutletAddress!=null){%><%=OutletAddress%><%} %> </td>
								<td ><%if(Shopcategory!=null){%><%=Shopcategory%><%} %></td>
								<td ><%if(OwnerName!=null){%><%=OwnerName%><%} %> - <%if(OutletContactNum!=null){%><%=OutletContactNum%><%} %></td>
								<td ><%if(CnicNum!=null){%><%=CnicNum%><%} %></td>
								<td><%if(CRID!=0){%><%=CRID%><%} %></td>
								<td><%if(RequestBy_!=0){%><%=RequestBy_%><%} %></td>
								<td><%if(ContactNum!=null){%><%=ContactNum%><%} %></td>
								<td ><%if(VehicalNum!=null){%><%=VehicalNum%><%} %> </td>
								<td>-</td>								
								<td ><%if(RequestBy!=0){%><%=RequestBy%><%} %></td>
				  				<td ><%if(DistributorID!=0){%><%=DistributorID%><%} %> - <%if(DistributorName !=null){%><%=DistributorName%><%} %></td>
								<td ><%if(RMID!=0){%><%=RMID%><%} %></td>

							</tr>				
							
						<%srno++;}
						%>
						
						
						
					</tbody>
							
				</table>
		</td>
	</tr>
</table>

	</li>	
</ul>





<%
s4.close();
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>