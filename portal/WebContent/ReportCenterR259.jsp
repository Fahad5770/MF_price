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
int FeatureID = 323;

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
Statement s7 = c.createStatement();

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
	WhereOutlets = " id in (SELECT id FROM pep.distributor_beat_plan where distributor_id in ("+OutletIds+")) ";	
	//System.out.println(WhereOutlets);
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
	WhereDistributorID = " and  id in (SELECT id FROM pep.distributor_beat_plan where distributor_id in ("+DistributorIDs+")) ";
	
}


//customer filter

long DistributorID1 = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
	DistributorID1 = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
}
String WhereCustomerID ="";

if (DistributorID1 != 0){
	WhereCustomerID = " and  id in (SELECT id FROM pep.distributor_beat_plan where distributor_id in ("+DistributorID1+")) ";

}



//PJP


String PJPIDs="";
long SelectedPJPArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPJP") != null){
	SelectedPJPArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPJP");
	PJPIDs = Utilities.serializeForSQL(SelectedPJPArray);
}

String WherePJP = "";
if (PJPIDs.length() > 0){
	WherePJP = " and id in ("+PJPIDs+")";	
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
							<th data-priority="1"  style="text-align:center; width:15%; ">Outlets</th>
<%--						<th data-priority="1"  style="text-align:center; width:15%;">Cooler Type</th>			 --%>				
							<th data-priority="1"  style="text-align:center; width:15%;">No Of Coolers</th>
							<th data-priority="1"  style="text-align:center; width:10%;">Scanned Coolers</th>							
							<!-- <th data-priority="1"  style="text-align:center; width:10%;">Scanned By</th> -->							
<%--						<th data-priority="1"  style="text-align:center; width:10%;">Scanned Date</th>			 --%>																	
							<th data-priority="1"  style="text-align:center; width:10%;">Exception Percentage</th>							
							<th data-priority="1"  style="text-align:center; width:10%;">Invalid BarCode</th>							

					    </tr>
					  </thead> 
					<tbody>
						<%
						//System.out.println("SELECT ecl.id,ecl.distributor_id,(select name from common_distributors cd where cd.distributor_id=ecl.distributor_id) dist_name,ecl.credit_type,(select label from  ec_empty_credit_types ect where ect.id=ecl.credit_type) credit_type_name,ecl.reason,ecl.start_date,ecl.end_date FROM ec_empty_credit_limit ecl  where ecl.is_active=1 and "+Utilities.getSQLDate(StartDate)+" between start_date and end_date"+WhereCustomerID);
						
						//System.out.println("SELECT distinct outlet_id,(select name from common_outlets where id=distributor_beat_plan_schedule.outlet_id) name FROM pep.distributor_beat_plan_schedule where 1=1 "+WhereCustomerID);
						
						
						//System.out.println("SELECT distinct outlet_id,(select name from common_outlets where id=distributor_beat_plan_schedule.outlet_id) name FROM pep.distributor_beat_plan_schedule where 1=1 and outlet_id in (select distinct outlet_id from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+")"+WhereCustomerID);
						
						String OutletName="";
						
						long TotalNumberOfCooler=0;
						long TotalInvalidCooler=0;
						
						
						ResultSet rs = s.executeQuery("SELECT distinct outlet_id,(select name from common_outlets where id=distributor_beat_plan_schedule.outlet_id) name FROM pep.distributor_beat_plan_schedule where 1=1 and outlet_id in (select distinct outlet_id from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+")"+WhereCustomerID);

						//System.out.println(rs);
						while(rs.next()){
							
							int OutletID=0;
							if (rs.getInt("outlet_id")!=0)
							{
								OutletID=rs.getInt("outlet_id");
								OutletName=rs.getString("name");
							}

							
							
							String Cooler_Type="";
							
							int No_Of_Coolers=0;
							
							int Scanned_Coolers=0;
							ResultSet rs4= s4.executeQuery("SELECT count(*) total_scanned FROM pep.common_assets where outlet_id_parsed in ("+OutletID+") and barcode in (SELECT barcode FROM pep.mobile_order mo join mobile_order_assets moa on  mo.unedited_order_id=moa.id where mo.outlet_id in ("+OutletID+") and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+")");
							if(rs4.first())
							if(rs4.getInt("total_scanned")!=0)
							{Scanned_Coolers= rs4.getInt("total_scanned");}
							 
							
							String Scanned_By="";
							
							Date Scanned_Date=null;
							
							double Exception_Percentage=0;
							ResultSet rs5= s5.executeQuery("SELECT count(*) total_scanned2 FROM pep.common_assets where outlet_id_parsed not in ("+OutletID+") and barcode in (SELECT barcode FROM pep.mobile_order mo join mobile_order_assets moa on  mo.unedited_order_id=moa.id where mo.outlet_id in ("+OutletID+") and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+")");
							if(rs5.first())
							if(rs5.getInt("total_scanned2")!=0)
							{
								Exception_Percentage=(rs5.getInt("total_scanned2")/Scanned_Coolers)*100;	
							}
							
							
							ResultSet rs3=s3.executeQuery("SELECT count(outlet_id) total_cooler FROM pep.common_assets where outlet_id="+OutletID+" and tot_status='INJECTED'");					
							if(rs3.first()){
								No_Of_Coolers=rs3.getInt("total_cooler");
							}
							
							
							int Invalid_BarCode=0;
							ResultSet rs6= s6.executeQuery("select count(*) invalid_barcode  from ( "
									+" SELECT moa.barcode, (select barcode from common_assets ca where ca.barcode = moa.barcode) asset FROM pep.mobile_order mo join mobile_order_assets moa on  mo.unedited_order_id=moa.id where mo.outlet_id in ("+OutletID+") and mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" having asset is null "
									+" ) tab1");
							if(rs6.first())
							if(rs6.getInt("invalid_barcode")!=0)
							{
								Invalid_BarCode= rs6.getInt("invalid_barcode");
							}

									
							TotalNumberOfCooler+=No_Of_Coolers;	
							TotalInvalidCooler+=Invalid_BarCode;
						
							
	%>						
								<tr> 
								<td ><%=OutletID %> - <%=OutletName %></td>
 						<%-- 		<td ><%=Cooler_Type %></td> --%>
								<td style="text-align:center"><%if(No_Of_Coolers!=0){%><%=No_Of_Coolers%><%} %></td>
								<td style="text-align:center"><%if(Scanned_Coolers!=0){%><%=Scanned_Coolers%><%} %></td>
								<!-- <td ><%=Scanned_By%></td> -->
						<%--	<td ><%=Scanned_Date%></td> --%>
								<td style="text-align:center"><%if(Exception_Percentage!=0){%><%=Exception_Percentage %><%} %></td>																					
								<td style="text-align:center"><%if(Invalid_BarCode!=0){%><%=Invalid_BarCode %><%} %></td>																					

							</tr>				
							
						<%}
						%>
						
						
						<tr> 
								<td ></td>
 					
								<td style="text-align:center"><%if(TotalNumberOfCooler!=0){%><%=TotalNumberOfCooler%><%} %></td>
								<td style="text-align:center"></td>
								
					
								<td style="text-align:center"></td>																					
								<td style="text-align:center"><%if(TotalInvalidCooler!=0){%><%=TotalInvalidCooler %><%} %></td>																					

							</tr>				
						
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