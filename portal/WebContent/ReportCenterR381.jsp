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



function ExportToExcel(D1,D2,DistIDs){
	//alert(D1);
	$.mobile.showPageLoadingMsg();	
	
	$.ajax({
		
		url: "inventory/R381GetSalesExcelMain",
		data: {
			StartDate: D1,
			EndDate:D2,
			DistributorIDs : DistIDs
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			$.mobile.hidePageLoadingMsg();
			if(json.success == "true"){
				$.mobile.hidePageLoadingMsg();
				alert("File is created Successfully!");
				
				var url = "common/CommonFileCSVDownload?file="+json.FileName;

				
			
				 
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
Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, 0);
String DistributorIds = UserAccess.getDistributorQueryString(UserDistributor);
int FeatureID = 374;

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
Statement s8 = c.createStatement();
Statement s9 = c.createStatement();
Statement s10 = c.createStatement();
Statement s11 = c.createStatement();
Statement s81 = c.createStatement();
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


int Month =Utilities.getMonthNumberByDate(StartDate);
int  Year = Utilities.getYearByDate(EndDate);
int NumberOfDays=Utilities.getNumberofDaysOfMonth(Month, Year);

int CurrentMonth=Utilities.getMonthNumberByDate(new Date());
int CurrentDay=Utilities.getDayNumberByDate(new Date());


if(CurrentMonth==Month){
	
	System.out.println("Its Cuurent Month ");	
	StartDateAttaintment = Utilities.parseDate("01/"+Month+"/"+Year);
	EndDateAttaintment =  Utilities.parseDate(CurrentDay+"/"+Month+"/"+Year);
	
}else{
	
	System.out.println("Its Not Cuurent Month ");
	StartDateAttaintment = Utilities.parseDate("01/"+Month+"/"+Year);
	EndDateAttaintment =  Utilities.parseDate(NumberOfDays+"/"+Month+"/"+Year);
}


//System.out.println("StartDateAttaintment "+StartDateAttaintment+" --- "+"StartDateAttaintment "+EndDateAttaintment);





long SelectedPackagesArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPackages") != null){
   	SelectedPackagesArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPackages");           	
}

long SelectedBrandsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedBrands") != null){
   	SelectedBrandsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedBrands");           	
}





long DistributorID1 = 0;
/* if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
	DistributorID1 = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
}
String WhereCustomerID ="";

if (DistributorID1 != 0){
	WhereCustomerID = " and distributor_id in ("+DistributorID1+") ";

} */


//Distributor
long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");           	
}else{
	/* Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	} */
}
String DistributorIDs="";

if(SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0){
	for(int i = 0; i < SelectedDistributorsArray.length ; i++){
		if(i == 0){
	DistributorIDs += SelectedDistributorsArray[i];
		}else{
	DistributorIDs += ", "+SelectedDistributorsArray[i];
		}
	}
	//WhereDistributors = " and distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}

if(DistributorIDs.equals("")){
    DistributorIDs = DistributorIds;
}

System.out.println(DistributorIDs);


/*Adding User Based Check Starts*/

//String DistributorIDs = "";
/* String WhereDistributors = "";

boolean IsUserKpo=false;

// for KPO
ResultSet rs31 = s.executeQuery("select * from users where type_id=2 and distributor_id is not null and ID="+SessionUserID+"");

if (rs31.first()) 
{
	IsUserKpo=true;
	DistributorIDs=rs31.getString("distributor_id");
	
}
if(IsUserKpo){
	WhereDistributors = " and distributor_id in ("+DistributorIDs+") ";
} */
//System.out.println("WhereDistributors "+WhereDistributors);
/*Adding User Based Check Ends*/
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
   										<a onclick="ExportToExcel(<%=Utilities.getSQLDate(StartDate) %>,<%=Utilities.getSQLDateNext(EndDate) %>,'<%=DistributorIDs %>')" href="#"    data-theme="c" data-inline="true" style="margin-top: 0px; margin-left: 0px; margin-bottom: 0px">Export To Excel</a>
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
