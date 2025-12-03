<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>


<%


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 224;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));
long EditID = Utilities.parseLong(request.getParameter("EditID"));

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

long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");           	
}else{
	/*
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	//
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	}*/
}

long SelectedOrderBookerArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers");           	
}

String OutletIds="";
long SelectedOutletArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets") != null){
	SelectedOutletArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets");
	OutletIds = Utilities.serializeForSQL(SelectedOutletArray);
}

String WhereOutlets = "";
if (OutletIds.length() > 0){
	WhereOutlets = " and mo.outlet_id in ("+OutletIds+") ";
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

String OrderBookerIDs = "";
String WhereOrderBooker = "";
if(SelectedOrderBookerArray != null && SelectedOrderBookerArray.length > 0){
	for(int i = 0; i < SelectedOrderBookerArray.length; i++){
		if(i == 0){
			OrderBookerIDs += SelectedOrderBookerArray[i];
		}else{
			OrderBookerIDs += ", "+SelectedOrderBookerArray[i];
		}
	}
	WhereOrderBooker = " and mo.created_by in ("+OrderBookerIDs+") ";
}






long WatchID = Utilities.parseLong(request.getParameter("WatchID"));

//System.out.println(WatchID);

String OutletID = "";
String OutletName = "";
String OutletAddress = "";
String OutletContactNo = "";
String OutletContactPerson = "";

String ComplaintDescription = "";
//String ComplaintDescriptionUrdu = "";
String FowardTo = "";
String RegionID = "";
String DistributorID = "";
String DistributorName = "";
boolean isResolved = false;
String ResolveDescription = "";
String MarketWatchRateName ="";
String MarketWatchRateName1 ="";
boolean RowFlag=false;

%>

<script>


function TaskFormSubmit(){
	
	$.mobile.loading( 'show');
	$.ajax({
	    url: "crm/ResearchReviewExecute",
	    data: $('#ResearchReviewForm').serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ){
	    	$.mobile.loading( 'hide');
	    	if (json.success == "true"){
	    		window.location = 'ReportCenter.jsp?ReportID=102';
	    	}else{
	    		alert('Server could not be reached.');
	    	}
	    },
	    error: function( xhr, status ) {
	    	$.mobile.loading( 'hide');
	    	alert("Server could not be reached.");
	    }
	});
	
}

function setTOTRows(){
	var length = $('input[name=ChillersCompanyIDHidden]').length;
	for(var i = 0; i < length; i++){
		if( $('input[name=ChillersCompanyIDHidden]')[i].value == 1 ){
			
			var ChillerQty = $('input[name=ChillersQuantity]')[i].value;
			var content = "";
			for(var c = 0; c < ChillerQty; c++){
				content += "<tr>";
					content += "<td style='border: 0px'><input type='text' name='AssetCode' id='AssetCode' placeholder='' value='' data-mini='true' ></td>";
					content += "<td style='border: 0px'><input type='text' name='YearOfIssuance' id='YearOfIssuance' placeholder='' value='' data-mini='true' ></td>";
					content += "<td style='border: 0px'><input type='text' name='Comments' id='Comments' placeholder='' value='' data-mini='true' ></td>";
				content += "</tr>";
			}
			
			$('#ResearchReviewFormTOTTable').empty();
			$('#ResearchReviewFormTOTTable').append(content).trigger('create');
		}
	}
}

function populateBrandsPepsi(){	
	var len = brands_pepsi_ids.length;
	for(var i = 0; i < len; i++){
		$('#'+brands_pepsi_ids[i]).prop('checked',true).checkboxradio('refresh');
	}
}

function populateBrandsCoke(){	
	var len = brands_coke_ids.length;
	for(var i = 0; i < len; i++){
		$('#'+brands_coke_ids[i]).prop('checked',true).checkboxradio('refresh');
	}
}

function populateHiddenFields(ref){
	//alert("populateHiddenFields() : "+ref);
	//alert($('#StockAvailabilityBrandsValue'+ref).prop("checked"));
	if( $('#StockAvailabilityBrandsValue'+ref).prop("checked") == false ){
		$('#StockAvailabilityBrandsCompanyIDHidden'+ref).prop("disabled", true);
		$('#StockAvailabilityBrandsPackageIDHidden'+ref).prop("disabled", true);
		$('#StockAvailabilityBrandsBrandIDHidden'+ref).prop("disabled", true);
	}else{
		$('#StockAvailabilityBrandsCompanyIDHidden'+ref).prop("disabled", false);
		$('#StockAvailabilityBrandsPackageIDHidden'+ref).prop("disabled", false);
		$('#StockAvailabilityBrandsBrandIDHidden'+ref).prop("disabled", false);
	}
}

</script>

<%

ResultSet rs2 = s.executeQuery("SELECT *, (select name from common_distributors cd where cd.distributor_id=mr.distributor_id) distributor_name,(select display_name from users u where u.id=mr.created_by) created_by_name, (SELECT label FROM common_outlets_channels where id=mr.outlet_business_type) business_type_label, (SELECT label FROM mrd_research_agreement_estimate where id=agreement_estimate_id) agreement_estimate, (SELECT label FROM mrd_research_discount_estimate where id=discount_estimate_id) discount_estimate FROM mrd_research mr where mr.id="+WatchID);
if(rs2.first()){
	
	
%>

<form id="ResearchReviewForm" action="/" method="post" >
			<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-13px;" data-icon="false">
			<li data-role="list-divider" data-theme="a">Detail</li>
			<li>
			
			
			
				<input type="hidden" name="IsOutletValid" id="IsOutletValid" value="false" >
			
				<input type="hidden" name="EditID" id="EditID" value="<%=rs2.getString("id")%>" >
				
				
				
				<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					
					<tr>
						<td style="border: 0px"><label for="OutletID" data-mini="true">Outlet ID</label><input type="text" name="OutletID" id="OutletID" placeholder="" value="<%=rs2.getInt("outlet_id")%>" data-mini="true" onChange="getOutletName(false); " onclick="OpenPopup()" readonly="readonly" ></td>
						<td style="border: 0px"><label for="OutletName" data-mini="true">Outlet Name</label><input type="text" name="OutletName" id="OutletName" placeholder="" value="<%=rs2.getString("outlet_name")%>" data-mini="true" readonly="readonly" ></td>
					</tr>
					
					<tr>
						<td style="border: 0px" colspan="2"><label for="OutletAddress" data-mini="true">Outlet Address</label><input type="text" name="OutletAddress" id="OutletAddress" placeholder="" value="<%=rs2.getString("outlet_address")%>" data-mini="true" ></td>
					</tr>
					
					<tr>
						<td style="border: 0px"><label for="DistributorID" data-mini="true">Distributor ID</label><input type="text" name="DistributorID" id="DistributorID" placeholder="" value="<%=rs2.getInt("distributor_id")%>" data-mini="true" readonly="readonly" ></td>
						<td style="border: 0px"><label for="DistName" data-mini="true">Distributor Name</label><input type="text" name="DistName" id="DistName" placeholder="" value="<%=rs2.getString("distributor_name")%>" data-mini="true" readonly="readonly" ></td>
					</tr>
					<tr>
						<td style="border: 0px">
							<label  data-mini="true" for="CreatedBy">Created By</label>
							<input type="text" name="CreatedBy" id="CreatedBy" placeholder="" value="<%=rs2.getString("created_by")%> - <%=rs2.getString("created_by_name")%>" data-mini="true" readonly>
						</td>
					</tr>
					<tr>
						<td style="border: 0px"><label for="OutletBusinessType" data-mini="true">Business Type</label>
							<!-- <input type="text" name="OutletBusinessType" id="OutletBusinessType" placeholder="" value="<%//=rs2.getString("business_type_label")%>" data-mini="true" > -->
							<select name="OutletBusinessType" id="OutletBusinessType" data-mini="true">
								<option value="">Select</option>
								<%
								ResultSet rs = s2.executeQuery("SELECT * FROM common_outlets_channels");
								while(rs.next()){
									%>
									<option value="<%=rs.getString("id")%>"><%=rs.getString("label")%></option>
									<%
								}
								%>
							</select>
							<script>
								$('#OutletBusinessType').val(<%=rs2.getString("outlet_business_type")%>).change();
							</script>
						</td>
						<td style="border: 0px"><label for="OutletContactPerson" data-mini="true">Contact Person</label><input type="text" name="OutletContactPerson" id="OutletContactPerson" placeholder="" value="<%=rs2.getString("outlet_contact_person")%>" data-mini="true" >&nbsp;</td>
					</tr>
					<tr>
						<td style="border: 0px"><label for="OutletContactNo" data-mini="true">Contact No</label><input type="text" name="OutletContactNo" id="OutletContactNo" placeholder="" value="<%=rs2.getString("outlet_contact_no")%>" data-mini="true" ></td>
						<td style="border: 0px"><label for="OutletContactPersonCnic" data-mini="true">CNIC No</label><input type="text" name="OutletContactPersonCnic" id="OutletContactPersonCnic" placeholder="" value="<%=rs2.getString("outlet_contact_person_cnic")%>" data-mini="true" >&nbsp;</td>
					</tr>
					<tr>
						<td style="border: 0px">
							<label for="OutletSegment" data-mini="true">Segment</label>
							<!--<input type="text" name="OutletSegment" id="OutletSegment" placeholder="" value="<%// if( rs2.getString("outlet_segment").equals("1") ){ out.print("Urban"); }else{ out.print("Rural"); } %>" data-mini="true" >-->
							
							<select name="OutletSegment" id="OutletSegment" data-mini="true">
								<option value="">Select</option>
								<option value="1">Urban</option>
								<option value="2">Rural</option>
							</select>
							<script>
								$('#OutletSegment').val(<%=rs2.getString("outlet_segment")%>).change();
							</script>
						</td>
					</tr>
				</table>
			
				</li>
				<li data-role="list-divider" data-theme="c">Status</li>
				<li>
					<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
						
						<tr>
							<td style="border: 0px">
								<label for="ClosedDays" data-mini="true">ClosedDays</label>
								<!-- <input type="text" name="ClosedDays" id="ClosedDays" placeholder="" value="<%//=rs2.getString("closed_days")%>" data-mini="true" >-->
								
								<select name="ClosedDays" id="ClosedDays" data-mini="true">
									<option value="">Select</option>
									<%
									ResultSet rs7 = s2.executeQuery("SELECT * FROM mrd_research_closed_days");
									while(rs7.next()){
									%>
										<option value="<%=rs7.getString("id")%>"><%=rs7.getString("label")%></option>
									<%
									}
									%>
								</select>
								<script>
									$('#ClosedDays').val("<%=rs2.getString("closed_days")%>").change();
								</script>
								
							</td>
							<td style="border: 0px">
								<label for="OutletStatus" data-mini="true">Outlet Status</label>
								<!-- <input type="text" name="OutletStatus" id="OutletStatus" placeholder="" value="<% //if( rs2.getString("outlet_status").equals("1") ){ out.print("CSD"); }else{ out.print("FMCG"); } %>" data-mini="true" > -->
								
								<select name="OutletStatus" id="OutletStatus" data-mini="true">
									<option value="">Select</option>
									<option value="1">CSD</option>
									<option value="2">FMCG</option>
								</select>
								<script>
									$('#OutletStatus').val("<%=rs2.getString("outlet_status")%>").change();
								</script>
								
							</td>
						</tr>
						<tr>
							<td style="border: 0px">
								<label for="OutletClosedStatus" data-mini="true">Outlet Closed</label>
								<select name="OutletClosedStatus" id="OutletClosedStatus" data-mini="true">
									<option value="0">Select</option>
									<option value="1">Permanently</option>
									<option value="2">Temporarily</option>
								</select>
								<script>
									$('#OutletClosedStatus').val("<%=rs2.getString("outlet_closed_status")%>").change();
								</script>
							</td>
						</tr>
					</table>
				</li>
				<li data-role="list-divider" data-theme="c">No of Chillers</li>
				<li>
					<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
						<tr>
							<td style="border: 0px"><label for="ChillersCompanyID">Company</label></td>
							<td style="border: 0px"><label for="ChillersQuantity">No of Chillers</label></td>
						</tr>
						<%
						ResultSet rs4 = s2.executeQuery("SELECT *, (SELECT label FROM mrd_research_companies where id=company_id) company_name FROM mrd_research_company_chillers where id="+rs2.getString("id")+" order by company_id");
						while(rs4.next()){
						%>
						<tr>
							<td style="border: 0px">
								<input type="hidden" name="ChillersCompanyIDHidden" id="ChillersCompanyIDHidden" value="<%=rs4.getString("company_id")%>" >
								<input type="text" name="ChillersCompanyID" id="ChillersCompanyID" placeholder="" value="<%=rs4.getString("company_name")%>" data-mini="true" readonly="readonly" tabindex="-1" >
							</td>
							<td style="border: 0px"><input type="text" name="ChillersQuantity" id="ChillersQuantity" placeholder="" value="<%=rs4.getString("no_of_chillers")%>" data-mini="true" onchange="setTOTRows()" ></td>
						</tr>
						<%
						}
						%>
					</table>
				</li>
				<li data-role="list-divider" data-theme="c">SSRB Glass Quantity</li>
				<li>
					<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
						<tr>
							<td style="border: 0px"><label for="SSRBGlassCompanyID">Company</label></td>
							<td style="border: 0px"><label for="SSRBGlassQuantity">Quantity</label></td>
						</tr>
						<%
						ResultSet rs5 = s2.executeQuery("SELECT *, (SELECT label FROM mrd_research_companies where id=company_id) company_name FROM mrd_research_stock_ssrb where id="+rs2.getString("id")+" order by company_id");
						while(rs5.next()){
						%>
						<tr>
							<input type="hidden" name="SSRBGlassCompanyIDHidden" id="SSRBGlassCompanyIDHidden" value="<%=rs5.getString("company_id")%>" >
							<td style="border: 0px"><input type="text" name="SSRBGlassCompanyID" id="SSRBGlassCompanyID" placeholder="" value="<%=rs5.getString("company_name")%>" data-mini="true" readonly="readonly" tabindex="-1" ></td>
							<td style="border: 0px"><input type="text" name="SSRBGlassQuantity" id="SSRBGlassQuantity" placeholder="" value="<%=rs5.getString("quantity")%>" data-mini="true" ></td>
						</tr>
						<%
						}
						%>
					</table>
				</li>
				<li data-role="list-divider" data-theme="c">Stock Availability (Cooler Stock)</li>
				<li>
				
					<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
						<tr>
							<!-- <td style="border: 0px"><label for="StockAvailabilityCompanyID">Company</label></td> -->
							<td style="border: 0px"><label for="StockAvailabilityPackageID">Package</label></td>
							<td style="border: 0px"><label for="StockAvailabilityBrandID">Brand</label></td>
							<td style="border: 0px"><label for="StockAvailabilityQuantity">Quantity</label></td>
						</tr>
				
					<%
					ResultSet rs8 = s2.executeQuery("SELECT * FROM mrd_research_companies");
					while(rs8.next()){
						
						%>
						<tr>
							<td style="border: 0px; font-weight: bold; text-align:left; background: #ececec" colspan="4"><%=rs8.getString("label")%></td>
						</tr>
						<%
						
						ResultSet rs6 = s3.executeQuery("SELECT *, (SELECT label FROM mrd_research_companies where id=company_id) company_name, (select package_label from inventory_products_view where package_id=mrsa.package_id limit 1) package_label, (select brand_label from inventory_products_view where brand_id=mrsa.brand_id limit 1) brand_label FROM mrd_research_stock_availability mrsa where id="+rs2.getString("id")+" and company_id="+rs8.getString("id")+" and stock_type_id=1 order by company_id, package_id");
						while(rs6.next()){
						
						%>
						
						<tr>
							<input type="hidden" name="StockAvailabilityCompanyIDHidden" id="StockAvailabilityCompanyIDHidden" value="<%=rs6.getString("company_id")%>" >
							<input type="hidden" name="StockAvailabilityPackageIDHidden" id="StockAvailabilityPackageIDHidden" value="<%=rs6.getString("package_id")%>" >
							<input type="hidden" name="StockAvailabilityStockTypeIDHidden" id="StockAvailabilityStockTypeIDHidden" value="<%=rs6.getString("stock_type_id")%>" >
							<input type="hidden" name="StockAvailabilityBrandIDHidden" id="StockAvailabilityBrandIDHidden" value="<%=rs6.getString("brand_id")%>" >
							
							<!-- <td style="border: 0px"><input type="text" name="StockAvailabilityCompanyID" id="StockAvailabilityCompanyID" placeholder="" value="<%//=rs6.getString("company_name")%>" data-mini="true" readonly="readonly" tabindex="-1" ></td> -->
							<td style="border: 0px"><input type="text" name="StockAvailabilityPackageID" id="StockAvailabilityPackageID" placeholder="" value="<%=rs6.getString("package_label")%>" data-mini="true" readonly="readonly" tabindex="-1" ></td>
							<td style="border: 0px"><input type="text" name="StockAvailabilityBrandID" id="StockAvailabilityBrandID" placeholder="" value="<%=(rs6.getString("brand_label") != null) ? rs6.getString("brand_label") : "CSD"%>" data-mini="true" readonly="readonly" tabindex="-1" ></td>
							<td style="border: 0px"><input type="text" name="StockAvailabilityQuantity" id="StockAvailabilityQuantity" placeholder="" value="<%=rs6.getString("quantity")%>" data-mini="true" ></td>
						</tr>
						
						<%
						}
					}
					%>
					</table>
				</li>
				
				<li data-role="list-divider" data-theme="c">Stock Availability (Floor Stock)</li>
				<li>
				
					<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
						<tr>
							<!-- <td style="border: 0px"><label for="StockAvailabilityCompanyID">Company</label></td> -->
							<td style="border: 0px"><label for="StockAvailabilityPackageID">Package</label></td>
							<td style="border: 0px"><label for="StockAvailabilityBrandID">Brand</label></td>
							<td style="border: 0px"><label for="StockAvailabilityQuantity">Quantity</label></td>
						</tr>
				
					<%
					ResultSet rs9 = s2.executeQuery("SELECT * FROM mrd_research_companies");
					while(rs9.next()){
						
						%>
						<tr>
							<td style="border: 0px; font-weight: bold; text-align:left; background: #ececec" colspan="4"><%=rs9.getString("label")%></td>
						</tr>
						<%
						
						ResultSet rs6 = s3.executeQuery("SELECT *, (SELECT label FROM mrd_research_companies where id=company_id) company_name, (select package_label from inventory_products_view where package_id=mrsa.package_id limit 1) package_label, (select brand_label from inventory_products_view where brand_id=mrsa.brand_id limit 1) brand_label FROM mrd_research_stock_availability mrsa where id="+rs2.getString("id")+" and company_id="+rs9.getString("id")+" and stock_type_id=2 order by company_id, package_id");
						while(rs6.next()){
						
						%>
						
						<tr>
							<input type="hidden" name="StockAvailabilityCompanyIDHidden" id="StockAvailabilityCompanyIDHidden" value="<%=rs6.getString("company_id")%>" >
							<input type="hidden" name="StockAvailabilityPackageIDHidden" id="StockAvailabilityPackageIDHidden" value="<%=rs6.getString("package_id")%>" >
							<input type="hidden" name="StockAvailabilityStockTypeIDHidden" id="StockAvailabilityStockTypeIDHidden" value="<%=rs6.getString("stock_type_id")%>" >
							<input type="hidden" name="StockAvailabilityBrandIDHidden" id="StockAvailabilityBrandIDHidden" value="<%=rs6.getString("brand_id")%>" >
							
							<!-- <td style="border: 0px"><input type="text" name="StockAvailabilityCompanyID" id="StockAvailabilityCompanyID" placeholder="" value="<%//=rs6.getString("company_name")%>" data-mini="true" readonly="readonly" tabindex="-1" ></td> -->
							<td style="border: 0px"><input type="text" name="StockAvailabilityPackageID" id="StockAvailabilityPackageID" placeholder="" value="<%=rs6.getString("package_label")%>" data-mini="true" readonly="readonly" tabindex="-1" ></td>
							<td style="border: 0px"><input type="text" name="StockAvailabilityBrandID" id="StockAvailabilityBrandID" placeholder="" value="<%=(rs6.getString("brand_label") != null) ? rs6.getString("brand_label") : "CSD"%>" data-mini="true" readonly="readonly" tabindex="-1" ></td>
							<td style="border: 0px"><input type="text" name="StockAvailabilityQuantity" id="StockAvailabilityQuantity" placeholder="" value="<%=rs6.getString("quantity")%>" data-mini="true" ></td>
						</tr>
						
						<%
						}
					}
					%>
					</table>
				</li>
				
				<li data-role="list-divider" data-theme="c">Brands (Pepsi)</li>
				<li>
					<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					<%
					int counter = 0;
					ResultSet rs10 = s2.executeQuery("SELECT distinct package_id, (SELECT label FROM inventory_packages where id=package_id) package_label FROM mrd_research_stock_availability_brands where company_id in (1, 5, 6) and id="+rs2.getString("id"));
					while(rs10.next()){
						%>
						<tr>
							<td style="border: 0px; font-weight: bold; text-align:left; background: #ececec" colspan="4"><%=rs10.getString("package_label")%></td>
						</tr>
						<%
						
						ResultSet rs11 = s3.executeQuery("SELECT *, (SELECT label FROM inventory_brands where id=brand_id) brand_label FROM mrd_research_stock_availability_brands where company_id in (1, 5, 6) and id="+rs2.getString("id")+" and package_id="+rs10.getString("package_id"));
						while(rs11.next()){
							%>
							<tr>
								<td style="border: 0px; text-align:left; padding: 0px">
								
									<input type="hidden" name="StockAvailabilityBrandsCompanyIDHidden" id="StockAvailabilityBrandsCompanyIDHidden<%=rs10.getString("package_id")%>_<%=rs11.getString("brand_id")%>" value="1" >
									<input type="hidden" name="StockAvailabilityBrandsPackageIDHidden" id="StockAvailabilityBrandsPackageIDHidden<%=rs10.getString("package_id")%>_<%=rs11.getString("brand_id")%>" value="<%=rs10.getString("package_id")%>" >
									<input type="hidden" name="StockAvailabilityBrandsBrandIDHidden" id="StockAvailabilityBrandsBrandIDHidden<%=rs10.getString("package_id")%>_<%=rs11.getString("brand_id")%>" value="<%=rs11.getString("brand_id")%>" >
								
									<label for="StockAvailabilityBrandsValue<%=rs10.getString("package_id")%>_<%=rs11.getString("brand_id")%>"><%=rs11.getString("brand_label")%></label>
									<input type="checkbox" name="StockAvailabilityBrandsValueHidden" id="StockAvailabilityBrandsValue<%=rs10.getString("package_id")%>_<%=rs11.getString("brand_id")%>" data-mini="true" value="1" onclick="populateHiddenFields('<%=rs10.getString("package_id")%>_<%=rs11.getString("brand_id")%>')" >
									
									<script>
									brands_pepsi_ids[<%=counter%>] = "StockAvailabilityBrandsValue<%=rs10.getString("package_id")%>_<%=rs11.getString("brand_id")%>";
									</script>
									
								</td>
							</tr>
							<%
							counter++;
						}
						
					}
					%>
					</table>
				</li>
				
				<li data-role="list-divider" data-theme="c">Brands (Coke)</li>
				<li>
					<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					<%
					int counter2 = 0;
					ResultSet rs12 = s2.executeQuery("SELECT distinct package_id, (SELECT label FROM inventory_packages where id=package_id) package_label FROM mrd_research_stock_availability_brands where company_id=2 and id="+rs2.getString("id"));
					while(rs12.next()){
						%>
						<tr>
							<td style="border: 0px; font-weight: bold; text-align:left; background: #ececec" colspan="4"><%=rs12.getString("package_label")%></td>
						</tr>
						<%
						
						ResultSet rs11 = s3.executeQuery("SELECT *, (SELECT label FROM inventory_brands where id=brand_id) brand_label FROM mrd_research_stock_availability_brands where company_id=2 and id="+rs2.getString("id")+" and package_id="+rs12.getString("package_id"));
						while(rs11.next()){
							%>
							<tr>
								<td style="border: 0px; text-align:left; padding: 0px">
									
									<input type="hidden" name="StockAvailabilityBrandsCompanyIDHidden" id="StockAvailabilityBrandsCompanyIDHidden<%=rs12.getString("package_id")%>_<%=rs11.getString("brand_id")%>" value="2" >
									<input type="hidden" name="StockAvailabilityBrandsPackageIDHidden" id="StockAvailabilityBrandsPackageIDHidden<%=rs12.getString("package_id")%>_<%=rs11.getString("brand_id")%>" value="<%=rs12.getString("package_id")%>" >
									<input type="hidden" name="StockAvailabilityBrandsBrandIDHidden" id="StockAvailabilityBrandsBrandIDHidden<%=rs12.getString("package_id")%>_<%=rs11.getString("brand_id")%>" value="<%=rs11.getString("brand_id")%>" >										
									
									<label for="StockAvailabilityBrandsValue<%=rs12.getString("package_id")%>_<%=rs11.getString("brand_id")%>"><%=rs11.getString("brand_label")%></label>
									<input type="checkbox" name="StockAvailabilityBrandsValueHidden" id="StockAvailabilityBrandsValue<%=rs12.getString("package_id")%>_<%=rs11.getString("brand_id")%>" data-mini="true" value="1" onclick="populateHiddenFields('<%=rs12.getString("package_id")%>_<%=rs11.getString("brand_id")%>')" >
									
									<script>
									brands_coke_ids[<%=counter2%>] = "StockAvailabilityBrandsValue<%=rs12.getString("package_id")%>_<%=rs11.getString("brand_id")%>";
									</script>
									
								</td>
							</tr>
							<%
							counter2++;
						}
						
					}
					%>
					</table>
				</li>
				
				
				<li data-role="list-divider" data-theme="c">Discount</li>
				<li>
					<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
						
						<tr>
							<td style="border: 0px">
								<label for="AgreementStatus" data-mini="true">Agreement Status</label>
								<!-- <input type="text" name="AgreementStatus" id="AgreementStatus" placeholder="" value="<% //if( rs2.getString("agreement_status").equals("1") ){ out.print("Yes"); }else{ out.print("No"); } %>" data-mini="true" > -->
								
								<select name="AgreementStatus" id="AgreementStatus" data-mini="true">
									<option value="">Select</option>
									<option value="1">Yes</option>
									<option value="2">No</option>
								</select>
								<script>
									$('#AgreementStatus').val("<%=rs2.getString("agreement_status")%>").change();
								</script>
								
							</td>
						</tr>						
						<tr>
							<td style="border: 0px">
								<label for="AgreementEstimate" data-mini="true">Agreement Estimate</label>
								<!-- <input type="text" name="AgreementEstimate" id="AgreementEstimate" placeholder="" value="<%//=rs2.getString("agreement_estimate")%>" data-mini="true" > -->
								
								<select name="AgreementEstimate" id="AgreementEstimate" data-mini="true">
									<option value="0">Select</option>
									<option value="1">Rs. 10k to 25k</option>
									<option value="2">Rs. 25k to 50k</option>
									<option value="3">Rs. 50k to 100k</option>
								</select>
								<script>
									$('#AgreementEstimate').val("<%=rs2.getString("agreement_estimate_id")%>").change();
								</script>
								
							</td>
						</tr>						
						<tr>
							<td style="border: 0px">
								<label for="DiscountStatus" data-mini="true">Discount Status</label>
								<!-- <input type="text" name="DiscountStatus" id="DiscountStatus" placeholder="" value="<% // if( rs2.getString("discount_status").equals("1") ){ out.print("Yes"); }else{ out.print("No"); } %>" data-mini="true" > -->
								
								<select name="DiscountStatus" id="DiscountStatus" data-mini="true">
									<option value="">Select</option>
									<option value="1">Yes</option>
									<option value="2">No</option>
								</select>
								<script>
									$('#DiscountStatus').val("<%=rs2.getString("discount_status")%>").change();
								</script>
								
							</td>
						</tr>						
						<tr>
							<td style="border: 0px">
								<label for="DiscountEstimate" data-mini="true">Discount Estimate</label>
								<!-- <input type="text" name="DiscountEstimate" id="DiscountEstimate" placeholder="" value="<%//=rs2.getString("discount_estimate")%>" data-mini="true" > -->
								
								<select name="DiscountEstimate" id="DiscountEstimate" data-mini="true">
									<option value="0">Select</option>
									<option value="1">Rs. 10 to 30</option>
									<option value="2">Rs. 30 to 50</option>
									<option value="3">Rs. 50 to 70</option>
								</select>
								<script>
									$('#DiscountEstimate').val("<%=rs2.getString("discount_estimate_id")%>").change();
								</script>
								
							</td>
						</tr>
						
					</table>
				</li>
				
				<li data-role="list-divider" data-theme="c">TOT</li>
				<li>
					<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
						<tr>
							<td style="border: 0px"><label for="AssetCode">Asset Code</label></td>
							<td style="border: 0px"><label for="YearOfIssuance">Year Of Issuance</label></td>
							<td style="border: 0px"><label for="Comments">Comments</label></td>
						</tr>
						<tbody id="ResearchReviewFormTOTTable">
						<%
						ResultSet rs3 = s2.executeQuery("SELECT * FROM mrd_research_tot where id="+rs2.getString("id"));
						while(rs3.next()){
						%>
						<tr>
							<td style="border: 0px"><input type="text" name="AssetCode" id="AssetCode" placeholder="" value="<%=rs3.getString("asset_code")%>" data-mini="true" ></td>
							<td style="border: 0px"><input type="text" name="YearOfIssuance" id="YearOfIssuance" placeholder="" value="<%=rs3.getString("year_of_issuance")%>" data-mini="true" ></td>
							<td style="border: 0px"><input type="text" name="Comments" id="Comments" placeholder="" value="<%=rs3.getString("comments")%>" data-mini="true" ></td>
						</tr>
						<%
						}
						%>
						</tbody>
					</table>
				</li>
				<li data-role="list-divider" data-theme="c">Chillers</li>
				<li>
					<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
						<tr>
							<td style="border: 0px">
								<label for="PepsiChillerSellingCoke" data-mini="true">Pepsi Chiller Selling Coke</label>
								<select name="PepsiChillerSellingCoke" id="PepsiChillerSellingCoke" data-mini="true">
									<!-- <option value="0">Select</option> -->
									<option value="1">Yes</option>
									<option value="0">No</option>
								</select>
								<script>
									$('#PepsiChillerSellingCoke').val('<%=rs2.getString("pepsi_chiller_selling_coke")%>').change();
								</script>
							</td>
							<td style="border: 0px">
								<label for="PepsiChillerSellingGourmet" data-mini="true">Pepsi Chiller Selling Gourmet</label>
								<select name="PepsiChillerSellingGourmet" id="PepsiChillerSellingGourmet" data-mini="true">
									<!-- <option value="0">Select</option> -->
									<option value="1">Yes</option>
									<option value="0">No</option>
								</select>
								<script>
									$('#PepsiChillerSellingGourmet').val('<%=rs2.getString("pepsi_chiller_selling_gourmet")%>').change();
								</script>
							</td>
						</tr>
						
						<tr>
							<td style="border: 0px">
								<label for="CokeChillerSellingPepsi" data-mini="true">Coke Chiller Selling Pepsi</label>
								<select name="CokeChillerSellingPepsi" id="CokeChillerSellingPepsi" data-mini="true">
									<!-- <option value="0">Select</option> -->
									<option value="1">Yes</option>
									<option value="0">No</option>
								</select>
								<script>
									$('#CokeChillerSellingPepsi').val('<%=rs2.getString("coke_chiller_selling_pepsi")%>').change();
								</script>
							</td>
							<td style="border: 0px">
								<label for="CokeChillerSellingGourmet" data-mini="true">Coke Chiller Selling Gourmet</label>
								<select name="CokeChillerSellingGourmet" id="CokeChillerSellingGourmet" data-mini="true">
									<!-- <option value="0">Select</option> -->
									<option value="1">Yes</option>
									<option value="0">No</option>
								</select>
								<script>
									$('#CokeChillerSellingGourmet').val('<%=rs2.getString("coke_chiller_selling_gourmet")%>').change();
								</script>
							</td>
						</tr>
						
						<tr>
							<td style="border: 0px">
								<label for="GourmetChillerSellingPepsi" data-mini="true">Gourmet Chiller Selling Pepsi</label>
								<select name="GourmetChillerSellingPepsi" id="GourmetChillerSellingPepsi" data-mini="true">
									<!-- <option value="0">Select</option> -->
									<option value="1">Yes</option>
									<option value="0">No</option>
								</select>
								<script>
									$('#GourmetChillerSellingPepsi').val('<%=rs2.getString("gourmet_chiller_selling_pepsi")%>').change();
								</script>
							</td>
							<td style="border: 0px">
								<label for="GourmetChillerSellingCoke" data-mini="true">Gourmet Chiller Selling Coke</label>
								<select name="GourmetChillerSellingCoke" id="GourmetChillerSellingCoke" data-mini="true">
									<!-- <option value="0">Select</option> -->
									<option value="1">Yes</option>
									<option value="0">No</option>
								</select>
								<script>
									$('#GourmetChillerSellingCoke').val('<%=rs2.getString("gourmet_chiller_selling_coke")%>').change();
								</script>
							</td>
						</tr>
						
						<tr>
							<td style="border: 0px">
								<label for="ChillerAirPercentage" data-mini="true">Chiller Air Percentage</label>
								<input name="ChillerAirPercentage" id="ChillerAirPercentage" data-mini="true" value="<%=rs2.getString("chiller_air_percentage")%>">
									
								<script>
									$('#ChillerAirPercentage').val('<%=rs2.getString("chiller_air_percentage")%>').change();
								</script>
							</td>
							<td style="border: 0px">
								<label for="ChillerOutOfStock" data-mini="true">Chiller Out of Stock</label>
								<select name="ChillerOutOfStock" id="ChillerOutOfStock" data-mini="true">
									<!-- <option value="0">Select</option> -->
									<option value="1">Yes</option>
									<option value="0">No</option>
								</select>
								<script>
									$('#ChillerOutOfStock').val('<%=rs2.getString("chiller_out_of_stock")%>').change();
								</script>
							</td>
						</tr>
						
						<tr>
							<td style="border: 0px">
								<label for="OutOfOrder" data-mini="true">Out of Order</label>
								<select name="OutOfOrder" id="OutOfOrder" data-mini="true">
									<option value="0">Select</option>
									<option value="1">With Complain</option>
									<option value="2">Without Complain</option>
								</select>
								<script>
									$('#OutOfOrder').val('<%=rs2.getString("out_of_order")%>').change();
								</script>
							</td>
						</tr>
						
					</table>
				</li>
				
				
				<li data-role="list-divider" data-theme="c">Action</li>
				<li>
					<table>
						<tr>
							
							<td style="padding-left: 5px">
								<input type="button" value="Save" data-theme="c" data-mini="true" onclick="TaskFormSubmit()" >
							</td>
							
						</tr>
					</table>
				</li>
			</ul>
	</form>	

<%
}

s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>