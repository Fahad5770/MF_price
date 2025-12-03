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

tr {
    border: 1px solid black;
}
</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 321;



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
<script>

function outletImages(id){
	alert(id);
//	$("#SearchContent").focus();
	//document.getElementById("DeliveryNoteFromDate").disabled = "disabled";
	//document.getElementById("DeliveryNoteToDate").disabled = "disabled";
	$("#R370New34" ).popup( "open" );
	 $.get('RequestOutletImages.jsp?outletId='+id, function(data) {
		
		  $("#SearchContent").html(data);
		  $("#SearchContent").trigger('create');
		  
	}); 
	//return false;

}
</script>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Outlet Chanel Tagging</li>
<li>
<table border=0 data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt; width:100%">
							  <thead>
							    <tr class="ui-bar-c">
							<th data-priority="1"  style="text-align:left; width:10%; ">Request ID</th>
							<th data-priority="1"  style="text-align:left; width:10%;">Outlet ID</th>							
							<th data-priority="1"  style="text-align:left; width:10%;">Outlet Name</th>
							<th data-priority="1"  style="text-align:left; width:10%;">Created ON</th>
							<th data-priority="1"  style="text-align:left; width:10%;">Created By</th>
							<th data-priority="1"  style="text-align:left; width:10%;">Image 1</th>
							<th data-priority="1"  style="text-align:left; width:10%;">Image 2</th>
							<th data-priority="1"  style="text-align:left; width:10%;">Image 3</th>
							<th data-priority="1"  style="text-align:left; width:10%;">Image 4</th>
							
							    </tr>
							  </thead>
							  
								<tbody id="DistributorTargetsTableBody">
									
									<%
									String day="";
									//System.out.println("SELECT ecl.id,ecl.distributor_id,(select name from common_distributors cd where cd.distributor_id=ecl.distributor_id) dist_name,ecl.credit_type,(select label from  ec_empty_credit_types ect where ect.id=ecl.credit_type) credit_type_name,ecl.reason,ecl.start_date,ecl.end_date FROM ec_empty_credit_limit ecl  where ecl.is_active=1 and "+Utilities.getSQLDate(StartDate)+" between start_date and end_date"+WhereCustomerID);
									//System.out.println("");
									ResultSet rs = s.executeQuery("SELECT request_id FROM pep.workflow_requests where request_id in (10930,10717,8386,8384,8105,7966) order by request_id");
									while(rs.next()){
										ResultSet rsOutlet = s2.executeQuery("SELECT outlet_id,outlet_name,created_on,created_by FROM pep.common_outlets_request where request_id in ("+rs.getInt("request_id")+")");
										if(rsOutlet.first()){
										%>
										<tr>
										<td style="font-size:16px"><%=rs.getString("request_id") %></td>
								<td ><%=rsOutlet.getString("outlet_id") %></td>
								<td ><%=rsOutlet.getString("outlet_name") %></td>
								<td><%=Utilities.getDisplayDateFormat(rsOutlet.getDate("created_on")) %></td>								
								<td><%=rsOutlet.getString("created_by") %></td>
												
														
												<%
												
												
												
											
												
													int i = 0;
												//System.out.println("select * from mobile_common_outlets_files where outlet_id=" + outletId+" order by created_on desc limit 4");
													ResultSet rsi = s3.executeQuery("SELECT * FROM pep.mobile_outlets_request_files where outlet_request_id=" +rsOutlet.getString("outlet_id")+" order by created_on desc limit 4");
													while (rsi.next()) {
												%>
												<td>
									
										<img src="mobile/MobileFileDownloadOutlet?file=<%=rsi.getString("filename")%>" style="width:100px; height:100px;" />
										
										</td>
												<%
													}
										}
										%></tr><%
									}
									%>		
														</tbody>
													</table>

	</li>	
</ul>
<div data-role="popup" id="R370New34" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:600px; max-height: 600px" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h3>Outlet Images</h3>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >

			

        <div id="SearchContent">
        </div>
            
        </div>
    </div>
     <jsp:include page="LookupDistributorSearchPopup.jsp"> 
    	<jsp:param value="DistributorSearchCallBackAtDistributorReports" name="CallBack" />
    	<jsp:param value="44" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Distributor Search -->


<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>