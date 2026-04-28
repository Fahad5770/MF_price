<%@page import="org.apache.poi.util.SystemOutLogger"%>
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



function ExportToExcel(D1,D2,Distributor,brand,sku,region){
	//alert(D1);
	$.mobile.showPageLoadingMsg();	
	
	$.ajax({
		
		url: "reports/R366ExcelMain",
		data: {
			StartDate: D1,
			EndDate:D2,
			DistributorIDs : Distributor,
			BrandIDs : brand,
			SKUIDs : sku,
			RegionIDs : region
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



</script>

<style>
td {
	font-size: 8pt;
}
</style>


<%
	long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
long UniqueVoID = Utilities.parseLong(request.getParameter("UniqueSessionID"));
int FeatureID = 479;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}





Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");

if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	StartDate = new Date();
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}


System.out.println("Start Date : "+StartDate);
System.out.println("End Date : "+EndDate);


//Distributor Filter
String WhereDistributors = "", DistributorIDs = "";
long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors"); 
	
}else{
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	}
}


if(SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0){
	for(int i = 0; i < SelectedDistributorsArray.length ; i++){
		
		if(i == 0){
			DistributorIDs += SelectedDistributorsArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorsArray[i];
		}
	}
}

System.out.println("DistributorIDs : "+DistributorIDs);

// Brand Filter
long SelectedBrandsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedBrands") != null){
   	SelectedBrandsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedBrands");           	
}

String BrandIDs = "";
if(SelectedBrandsArray!= null && SelectedBrandsArray.length > 0){
	for(int i = 0; i < SelectedBrandsArray.length; i++){
		if(i == 0){
	BrandIDs += SelectedBrandsArray[i]+"";
		}else{
	BrandIDs += ", "+SelectedBrandsArray[i]+"";
		}
	}
}

System.out.println("BrandIDs : "+BrandIDs);


//Region SKU
String  SKUIDs="";
long SelectedSKUArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedSKU") != null){
	SelectedSKUArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedSKU");
	SKUIDs = Utilities.serializeForSQL(SelectedSKUArray);
}

System.out.println("SKUIDs : "+SKUIDs);


//Region Filter
String  RegionIDs="";
long SelectedRegionsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRegion") != null){
	SelectedRegionsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRegion");
	RegionIDs = Utilities.serializeForSQL(SelectedRegionsArray);
}

System.out.println("RegionIDs : "+RegionIDs);

%>


<ul data-role="listview" data-inset="true"
	style="font-size: 10pt; font-weight: normal; margin-top: -10px;"
	data-icon="false">
	<li data-role="list-divider" data-theme="a">Sales Details</li>
	<li>

		<%//if(IsUserKpo){ %>
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
								<td style="text-align: center">
   									
   									<div id="aExcelFileReady">
   										<a onclick="ExportToExcel(<%=Utilities.getSQLDate(StartDate) %>,<%=Utilities.getSQLDateNext(EndDate) %>,'<%=DistributorIDs %>', '<%=BrandIDs %>', '<%=SKUIDs %>', '<%=RegionIDs %>')" href="#"    data-theme="c" data-inline="true" style="margin-top: 0px; margin-left: 0px; margin-bottom: 0px">Export To Excel</a>
   								</div>
						    									
						   </td>							
								
							</tr>
						
		
						</tbody>
									
					</table>
					
						
					
				</td>
			</tr>
		</table>
		<%
		//}
		%>

	</li>
</ul>
