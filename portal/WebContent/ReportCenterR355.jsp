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

</script>

<style>
td {
	font-size: 8pt;
}
</style>


<%
	long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 467;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
//Date date = Utilities.parseDate(request.getParameter("Date"));


Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");

if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	StartDate = new Date();
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}



Date StartDateAttaintment = null;
Date EndDateAttaintment =null;

//System.out.println("StartDateAttaintment "+StartDateAttaintment+" --- "+"StartDateAttaintment "+EndDateAttaintment);




String WhereDistributors = "";
String WhereSelectedDistributors = "", DistributorIDs = "";
//_SR1SelectedRegion


long SelectedDistributorsArray[] = null;
boolean IsDistributorSelected=false;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors"); 
	IsDistributorSelected = true;
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
	WhereSelectedDistributors = " and distributor_id in ("+DistributorIDs+") ";
//	System.out.println(WhereSelectedDistributors);
}

int count=0;

// SND Distributors
String SNDDitsributorsIds="", HODIDs="", WhereSelectedSNDDistributors="";
long SelectedHODArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedHOD") != null){
	SelectedHODArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedHOD");
	HODIDs = Utilities.serializeForSQL(SelectedHODArray);
}
if(HODIDs.length() >0){
	System.out.println("select distinct distributor_id from common_distributors where snd_id in ("+HODIDs+")");
	ResultSet rsDis = s.executeQuery("select distinct distributor_id from common_distributors where snd_id in ("+HODIDs+")");
	while(rsDis.next()){
		if(count==0){
			SNDDitsributorsIds = rsDis.getString("distributor_id");
		}else{
			SNDDitsributorsIds += ", "+rsDis.getString("distributor_id");
		}
		count++;
	}
	WhereSelectedSNDDistributors = " and distributor_id in ("+SNDDitsributorsIds+") ";
	//System.out.println(WhereSelectedSNDDistributors);
}

count=0;

//RSM Distributors
String RSMDitsributorsIds="", RSMIDs="", WhereSelectedRSMDistributors="";
long SelectedRSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRSM") != null){
	SelectedRSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRSM");
	RSMIDs = Utilities.serializeForSQL(SelectedRSMArray);
}
if(RSMIDs.length() >0){
	System.out.println("select distinct distributor_id from common_distributors where rsm_id in ("+RSMIDs+")");
	ResultSet rsDis = s.executeQuery("select distinct distributor_id from common_distributors where rsm_id in ("+RSMIDs+")");
	while(rsDis.next()){
		if(count==0){
			RSMDitsributorsIds = rsDis.getString("distributor_id");
		}else{
			RSMDitsributorsIds += ", "+rsDis.getString("distributor_id");
		}
		count++;
	}
	WhereSelectedRSMDistributors = " and distributor_id in ("+RSMDitsributorsIds+") ";
	//System.out.println(WhereSelectedRSMDistributors);
}

count=0;
//Region Distributors
String RegionDitsributorsIds="", RegionIDs="", WhereSelectedRegionDistributors="";
long SelectedRegionArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRegion") != null){
	SelectedRegionArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRegion");
	RegionIDs = Utilities.serializeForSQL(SelectedRegionArray);
}
if(RegionIDs.length() >0){
	System.out.println("select distinct distributor_id from common_distributors where region_id in ("+RegionIDs+")");
	ResultSet rsDis = s.executeQuery("select distinct distributor_id from common_distributors where region_id in ("+RegionIDs+")");
	while(rsDis.next()){
		if(count==0){
			RegionDitsributorsIds = rsDis.getString("distributor_id");
		}else{
			RegionDitsributorsIds += ", "+rsDis.getString("distributor_id");
		}
		count++;
	}
	WhereSelectedRegionDistributors = " and distributor_id in ("+RegionDitsributorsIds+") ";
//	System.out.println(WhereSelectedRegionDistributors);
}


WhereDistributors = WhereSelectedDistributors+WhereSelectedSNDDistributors+WhereSelectedRSMDistributors+WhereSelectedRegionDistributors;
//System.out.println(WhereDistributors);
System.out.println("StartDate "+StartDate);


%>
<script>



function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}



function ExportToExcel(D1,DistIDs){
	//alert(D1);
	$.mobile.showPageLoadingMsg();	
	
	$.ajax({
		
		url: "reports/R355ExcelMain",
		data: {
			StartDate: D1,
			DistributorIDs : DistIDs
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
						
					<%// if(DistributorIDs.equals("")){
						%><!-- <tr>
								<td style="text-align: left">
   									
   									<P><b>Please select Distributors for Report.</b></P>
						    									
						   </td>							
								
							</tr> -->
						<%
				//	}else{ %>
						<tbody>
							
							
							<tr>
								<td style="text-align: center">
   									
   									<div id="aExcelFileReady">
   										<a onclick="ExportToExcel(<%=Utilities.getSQLDate(StartDate) %>,'<%=WhereSelectedDistributors %>')" href="#"    data-theme="c" data-inline="true" style="margin-top: 0px; margin-left: 0px; margin-bottom: 0px">Export To Excel</a>
   								</div>
						    									
						   </td>							
								
							</tr>
						
		
						</tbody>
								<%//} %>	
					</table>
					
						
					
				</td>
			</tr>
		</table>
		<%
		//}
		%>

	</li>
</ul>
