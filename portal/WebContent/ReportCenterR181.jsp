<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="com.pbc.reports.SalesIndex"%>
<%@page import="java.util.Calendar"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>



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
int FeatureID = 221;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

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
	WherePackage = " and bieoi.package_id in ("+PackageIDs+") ";
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
	WhereHOD = " and (snd_id in ("+HODIDs+") or rsm_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and mo.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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
	WhereDistributors = " and mo.distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}

//OrderBooker

boolean IsOrderBookerSelected=false;

int OrderBookerArrayLength=0;
long SelectedOrderBookerArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers");
	
	IsOrderBookerSelected=true;
	OrderBookerArrayLength=SelectedOrderBookerArray.length;
}



String OrderBookerIDs = "";
if(SelectedOrderBookerArray != null && SelectedOrderBookerArray.length > 0){
	for(int i = 0; i < SelectedOrderBookerArray.length; i++){
		if(i == 0){
			OrderBookerIDs += SelectedOrderBookerArray[i];
		}else{
			OrderBookerIDs += ", "+SelectedOrderBookerArray[i];
		}
	}
}
String OrderBookerIDsWher="";
if(OrderBookerIDs.length()>0){
	OrderBookerIDsWher =" and mo.created_by in ("+OrderBookerIDs+") ";
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
	WhereSM = " and mo.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
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
	WhereTDM = " and mo.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
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
	WhereASM = " and mo.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
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
	WhereBrand = " and ip.brand_id in ("+BrandIDs+") ";
}


long DistributorID = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
	DistributorID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
}
String WhereDistributorID ="";
if (DistributorID != 0){
	WhereDistributorID = " and bieoi.customer_id in ("+DistributorID+") ";	
}



//Empty Reason


String EmptyReasonIDs="";
String SelectedEmptyReasonArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedEmptyReason") != null){
	SelectedEmptyReasonArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedEmptyReason");
	EmptyReasonIDs = Utilities.serializeForSQL(SelectedEmptyReasonArray);
}

String WhereEmptyReason = "";
if (EmptyReasonIDs.length() > 0){
	WhereEmptyReason = " and bieoi.reason in("+EmptyReasonIDs+")";	
}


//Empty Reason


String MovementTypeIDs="";
String SelectedMovementTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedMovementType") != null){
	SelectedMovementTypeArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedMovementType");
	MovementTypeIDs = Utilities.serializeForSQL(SelectedMovementTypeArray);
}

String WhereMovementType = "";
if (MovementTypeIDs.length() > 0){
	WhereMovementType = " and bieoi.movmt in("+MovementTypeIDs+")";	
}

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Empty Transaction</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size:10px;">
							<th data-priority="1"  style=" width:10%;" title="">Document#</th>							
							<th data-priority="1"  style=" width:10%;" title="">Document Date</th>							
							<th data-priority="1"  style=" width:15%;" title="">Distributor</th>
							<th data-priority="1"  style=" width:15%;" title="">Package Name</th>
							<th data-priority="1"  style=" width:15%;" title="">Vehicle#</th>
							<th data-priority="1"  style=" width:15%;" title="">Movement Type</th>
							<th data-priority="1"  style="text-align:center; width:10%;" title="">Cases</th>
							<th data-priority="1"  style="text-align:center; width:10%;" title="">Units</th>
					    </tr>
					  </thead> 
					<tbody>
						
						<%
						long GTotalRawCases=0;
						long GTotalUnits=0;
						
						ResultSet rs = s.executeQuery("SELECT customer_id,(select name from common_distributors cd where cd.distributor_id=bieoi.customer_id) distributor_name,bieoi.document_id,bieoi.document_date,bieoi.package_id,(select label from inventory_packages ip where ip.id=bieoi.package_id) package_name,bieoi.vehicle_no,bieoi.movmt,bieoi.total_units FROM "+ds.logDatabaseName()+".bi_empty_other_issuance bieoi where bieoi.document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+WhereDistributorID+WherePackage+WhereEmptyReason+WhereMovementType);
						while(rs.next()){
							
							int UnitPerSKU=0;
							long[] RawCasesUnits;
							long TotalUnits=0;
							
							ResultSet rs1 = s2.executeQuery("select unit_per_case from inventory_packages where id="+rs.getLong("package_id"));
							if(rs1.first()){
								UnitPerSKU = rs1.getInt("unit_per_case");
							}
							TotalUnits = rs.getInt("total_units");
							RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnits, UnitPerSKU);
							
							GTotalRawCases+=RawCasesUnits[0];
							GTotalUnits+=RawCasesUnits[1];
							
							%>
							<tr>
								<td><%=rs.getString("document_id") %></td>
								<td><%=Utilities.getDisplayDateFormat(rs.getDate("document_date")) %></td>
								<td><%=rs.getLong("customer_id") %> - <%=rs.getString("distributor_name") %></td>
								<td><%=rs.getString("package_name") %></td>
								<td><%=rs.getString("vehicle_no") %></td>
								<td><%=rs.getString("movmt") %></td>
								<td style="text-align:center;"><%=RawCasesUnits[0] %></td>
								<td style="text-align:center;"><%=RawCasesUnits[1] %></td>
							</tr>
							
							
							
							<%
						}
						
						
						%>
						
						<tr>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<th>Total</th>
							<td style="text-align:center;"><%=GTotalRawCases %></td>
							<td style="text-align:center;"><%=GTotalUnits %></td>
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