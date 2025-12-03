<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<style>
td {
	font-size: 8pt;
}
</style>
<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

long SessionUserID = Long.parseLong((String) session.getAttribute("UserID"));
int FeatureID = 471;

if (UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false) {
	response.sendRedirect("AccessDenied.jsp");
}

Date StartDate = (Date) session.getAttribute(UniqueSessionID + "_SR1StartDate");
Date EndDate = (Date) session.getAttribute(UniqueSessionID + "_SR1EndDate");

if (session.getAttribute(UniqueSessionID + "_SR1StartDate") == null) {
	StartDate = new Date();
}

if (session.getAttribute(UniqueSessionID + "_SR1EndDate") == null) {
	EndDate = new Date();
}

//System.out.println("StartDate : "+StartDate);
//System.out.println("EndDate : "+EndDate);

// Distributor Filter
String WhereDistributors = "", DistributorIDs = "";
long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedDistributors") != null) {
	SelectedDistributorsArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedDistributors");

} else {
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];

	for (int x = 0; x < UserDistributor.length; x++) {
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	}
}

if (SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0) {
	for (int i = 0; i < SelectedDistributorsArray.length; i++) {

		if (i == 0) {
	DistributorIDs += SelectedDistributorsArray[i];
		} else {
	DistributorIDs += ", " + SelectedDistributorsArray[i];
		}
	}
}

//System.out.println("DistributorIDs : "+DistributorIDs);

// Order Bookers Filter
String OrderBookersIDs = "";
long SelectedOrderBookersArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedOrderBookers") != null) {
	SelectedOrderBookersArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedOrderBookers");
	OrderBookersIDs = Utilities.serializeForSQL(SelectedOrderBookersArray);
}

//System.out.println("OrderBookersIDs : "+OrderBookersIDs);

//TSO Filter
String TSOIDs = "";
long SelectedTSOsArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedASM") != null) {
	SelectedTSOsArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedASM");
	TSOIDs = Utilities.serializeForSQL(SelectedTSOsArray);
}

//System.out.println("TSOIDs : "+TSOIDs);

//TSO Filter
String ASMIDs = "";
long SelectedASMsArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedRSM") != null) {
	SelectedASMsArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedRSM");
	ASMIDs = Utilities.serializeForSQL(SelectedASMsArray);
}

//Region Filter
String RegionIDs = "";
long SelectedRegionsArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedRegion") != null) {
	SelectedRegionsArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedRegion");
	RegionIDs = Utilities.serializeForSQL(SelectedRegionsArray);
}

//System.out.println("RegionIDs : "+RegionIDs);

//Channel Filter
String ChannelIDs = "";
long SelectedChannelsArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedChannels") != null) {
	SelectedChannelsArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedChannels");
	ChannelIDs = Utilities.serializeForSQL(SelectedChannelsArray);
}

//System.out.println("ChannelIDs : "+ChannelIDs);

//City Filter
String CityIDs = "";
long SelectedCityArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedCity") != null) {
	SelectedCityArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedCity");
	CityIDs = Utilities.serializeForSQL(SelectedCityArray);
}

//System.out.println("CityIDs : "+CityIDs);

// Brand Filter
long SelectedBrandsArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedBrands") != null) {
	SelectedBrandsArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedBrands");
}

String BrandIDs = "";
if (SelectedBrandsArray != null && SelectedBrandsArray.length > 0) {
	for (int i = 0; i < SelectedBrandsArray.length; i++) {
		if (i == 0) {
	BrandIDs += SelectedBrandsArray[i] + "";
		} else {
	BrandIDs += "," + SelectedBrandsArray[i] + "";
		}
	}
}

//System.out.println("BrandIDs : "+BrandIDs);

//Region SKU
String SKUIDs = "";
long SelectedSKUArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedSKU") != null) {
	SelectedSKUArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedSKU");
	SKUIDs = Utilities.serializeForSQL(SelectedSKUArray);
}

//System.out.println("SKUIDs : "+SKUIDs);
%>
<script>



function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}



function ExportToExcel(D1,D2,DistributorIDs,OrderBookersIDs,TSOIDs,ASMIDs,RegionIDs,ChannelIDs,CityIDs,SKUIDs,BrandIDs){
	//alert(D1);
	if($("#reportTypeId").val() < 0){
		alert("Please Select Report Type");
	}else{
	var type = $("#reportTypeId").val();
	$.mobile.showPageLoadingMsg();
	$.ajax({
		
		url: "reports/R358ExcelMain",
		data: {
			StartDate: D1,
			EndDate: D2,
			Distributors : DistributorIDs,
			OrderBookers : OrderBookersIDs,
			TSOs : TSOIDs,
			ASMs : ASMIDs,
			Regions : RegionIDs,
			Channels : ChannelIDs,
			Brands : BrandIDs,
			SKUs : SKUIDs,
			City : CityIDs
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			$.mobile.hidePageLoadingMsg();
			if(json.success == "true"){
				$.mobile.hidePageLoadingMsg();
				alert("File is created Successfully!");
				
				var url = "common/CommonFileDownload?file="+json.FileName;
				var mydiv = document.getElementById("aExcelFileReady");
				var aTag = document.createElement('a');
				aTag.setAttribute('id','ExcelFileReady');				
				aTag.setAttribute('href',url);
				aTag.setAttribute('target','_blank');
				aTag.innerHTML = "<label style='color:Gray; text-decoration:none; font-weight:bold; cursor: pointer;'>Download</label>";
				$(mydiv).html(aTag); 
			}else{
				alert(json.error);
			}
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
		}
		
	});
}
}



</script>

<ul data-role="listview" data-inset="true"
	style="font-size: 10pt; font-weight: normal; margin-top: -10px;"
	data-icon="false">
	<li data-role="list-divider" data-theme="a">Outlet Brand-SKU Wise
		Sales</li>
	<li>
		<table style="width: 100%; margin-top: -8px" cellpadding="0"
			cellspacing="0">
			<tr>

				<td style="width: 100%" valign="top">
					<table border=0
						style="font-size: 18px; font-weight: 400; width: 100%"
						cellpadding="0" cellspacing="0" adata-role="table"
						class="GridWithBorder">
						<tbody>


							<tr>

								<td style="text-align: center; width: 75%;">

									<div id="aExcelFileReady">
										<a
											onclick="ExportToExcel(<%=Utilities.getSQLDate(StartDate)%>, <%=Utilities.getSQLDate(EndDate)%>, '<%=DistributorIDs%>', '<%=OrderBookersIDs%>','<%=TSOIDs%>','<%=ASMIDs%>','<%=RegionIDs%>', '<%=ChannelIDs%>','<%=CityIDs%>','<%=SKUIDs%>','<%=BrandIDs%>')"
											href="#" data-theme="c" data-inline="true"
											style="margin-top: 0px; margin-left: 0px; margin-bottom: 0px">Export
											To Excel</a>
									</div>

								</td>

							</tr>


						</tbody>

					</table>
				</td>
			</tr>
		</table>

	</li>
</ul>
