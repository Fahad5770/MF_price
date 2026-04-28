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
		
		url: "inventory/GetZeroSalesExcelMain",
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
int FeatureID = 451;

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


/* Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor); */

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


//System.out.println("Hello "+WhereLiftingType);


String ProductsLifted ="-1";
//System.out.println("SELECT group_concat(distinct package_id) FROM employee_targets et join employee_targets_packages etp on et.id = etp.id where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y')");
ResultSet rs41 = s6.executeQuery("SELECT group_concat(distinct package_id) FROM employee_targets et join employee_targets_packages etp on et.id = etp.id where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y')");

if (rs41.first()){
	ProductsLifted = rs41.getString(1);
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
String OrderBookerIDsWhere="";
if(OrderBookerIDs.length()>0){
	//OrderBookerIDsWhere =" and order_no in (select mobile_order_no from mobile_order_unedited mou where mou.created_by in ("+OrderBookerIDs+"))";
	OrderBookerIDsWhere =" and created_by in ("+OrderBookerIDs+") ";
}



//customer filter

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
 
// System.out.println(DistributorIDs);

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
						
					<% if(DistributorIDs.equals("")){
						%><tr>
								<td style="text-align: left">
   									
   									<P><b>Please select Distributors for Report.</b></P>
						    									
						   </td>							
								
							</tr>
						<%
					}else{ %>
						<tbody>
							
							
							<tr>
								<td style="text-align: center">
   									
   									<div id="aExcelFileReady">
   										<a onclick="ExportToExcel(<%=Utilities.getSQLDate(StartDate) %>,<%=Utilities.getSQLDateNext(EndDate) %>,'<%=DistributorIDs %>')" href="#"    data-theme="c" data-inline="true" style="margin-top: 0px; margin-left: 0px; margin-bottom: 0px">Export To Excel</a>
   								</div>
						    									
						   </td>							
								
							</tr>
						
		
						</tbody>
								<%} %>	
					</table>
					
						
					
				</td>
			</tr>
		</table>
		<%
		//}
		%>

	</li>
</ul>
