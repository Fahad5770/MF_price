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
<%@page import="com.pbc.common.Region"%>
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
int FeatureID = 195;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionToReplica();
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


//Distributor

long SelectedDistributorsArray[] = null;
boolean IsDistributorSelected=false;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors"); 
	IsDistributorSelected = true;
}else{

	/*
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
		
		long DistributorArray[] = new long[1];
		DistributorArray[0] = UserDistributor[x].DISTRIBUTOR_ID;
		session.setAttribute(UniqueSessionID+"_SR1SelectedDistributors",DistributorArray);
		break;
	}*/
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


//PJP


String PJPIDs="";
long SelectedPJPArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPJP") != null){
	SelectedPJPArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPJP");
	PJPIDs = Utilities.serializeForSQL(SelectedPJPArray);
}

String WherePJP = "";
if (PJPIDs.length() > 0){
	WherePJP = " and codv.outlet_id in (SELECT distinct outlet_id FROM distributor_beat_plan_schedule where id in("+PJPIDs+"))";	
}
boolean IsAdvanceAmountSelecte=false;
boolean IsEmptyResultSet = true;
String SelectedDiscountTypeArray[] = null;
String FixedWhere="";
String PerCaseWhere="";
String AdvanceAmountWhere="";

if (session.getAttribute(UniqueSessionID+"_SR1SelectedDiscountType") != null){
	SelectedDiscountTypeArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedDiscountType");
}

	IsEmptyResultSet = false;
	if (session.getAttribute(UniqueSessionID+"_SR1SelectedDiscountType") != null){
		//System.out.println(SelectedDiscountTypeArray.length);
		
		for(int i=0;i<SelectedDiscountTypeArray.length;i++)
    	{            		
  			//System.out.println("hello");
			if(SelectedDiscountTypeArray[i].equals("Fixed"))
  			{
  				FixedWhere = " and fixed_company_share!=0";
  				IsAdvanceAmountSelecte = false;
  			}  
  			if(SelectedDiscountTypeArray[i].equals("Per Case"))
  			{
  				PerCaseWhere = " and per_case='Yes'"; 
  				IsAdvanceAmountSelecte = false;
  			}  
  			
  			if(SelectedDiscountTypeArray[i].equals("Advance Amount"))
  			{
  				//if(SelectedDiscountTypeArray[i].equals("Advance Amount") && SelectedDiscountTypeArray.length==1){ //if only Advance Amount Selected
  				
  					//IsAdvanceAmountSelecte = true;
  				//}else{
  	  				IsAdvanceAmountSelecte = false;
  	  			    AdvanceAmountWhere = " and ledger_balance!=0"; 
  	  			//}
  			}
  			
    	}
	}


Region [] RegionObj = UserAccess.getUserFeatureRegion(SessionUserID, FeatureID);
String RegionIDsInRights = UserAccess.getRegionQueryString(RegionObj);
String RegionIDsSelected = "";

String WhereRegion = "";
//WhereRegion = " and co.region_id in ("+RegionIDsInRights+") ";

long SelectedRegionArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRegion") != null){
	SelectedRegionArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRegion");
	RegionIDsSelected = Utilities.serializeForSQL(SelectedRegionArray);
	//WhereRegion += " and co.region_id in ("+RegionIDsSelected+") ";
}

if(RegionIDsSelected.equals("")){
	RegionIDsSelected = "select region_id from common_regions";
}

WhereRegion = "and co.region_id in ( select region_id from common_regions where region_id in ("+RegionIDsInRights+")  and region_id in ("+RegionIDsSelected+") )";

/*if (RegionIDs.length() > 0){
	//WhereRegion += " and co.region_id in ("+RegionIDs+") ";
}
	*/
	
%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Discounted Outlets</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					
					 <thead>
					    <tr style="font-size:11px;">
							<th>#</th>
							<th data-priority="1"  style="text-align:center; " >Request ID</th>
							<th data-priority="1"  style="text-align:center; " >Outlet</th>
								<th data-priority="1"  style="text-align:center; " >Address</th>
							<th data-priority="1"  style="text-align:center; " >Distributor</th>
							<th data-priority="1"  style="text-align:center; " >Ledger Balance</th>
							<th data-priority="1"  style="text-align:center; " >Fixed Discount</th>
							<th data-priority="1"  style="text-align:center; " >Fixed Validity</th>
							<th data-priority="1"  style="text-align:center; " >Per Case</th>
							<th data-priority="1"  style="text-align:center; " >Activated On</th>
					    </tr>
					  </thead>
					<tbody>
					<%
					double TotalBalance = 0;
					

					String Query = "";
					int serial=1;
					//if(IsAdvanceAmountSelecte){
						//Query="select s.sampling_id, s.outlet_id, co.name, (select ifnull(sum(debit),0) - ifnull(sum(credit),0) from sampling_posting_accounts where outlet_id = s.outlet_id) ledger_balance, fixed_company_share, fixed_valid_from, fixed_valid_to, if ((select sampling_id from sampling_percase where sampling_id = s.sampling_id and curdate() between valid_from and valid_to limit 1) is null,'No','Yes') per_case, activated_on from sampling s join common_outlets co on s.outlet_id = co.id group by outlet_idhaving (ledger_balance != 0) "+AdvanceAmountWhere;
					//}else{
						 Query ="select s.sampling_id, s.outlet_id, co.name, (select ifnull(sum(debit),0) - ifnull(sum(credit),0) from sampling_posting_accounts where outlet_id = s.outlet_id) ledger_balance, fixed_company_share, fixed_valid_from, fixed_valid_to, if ((select sampling_id from sampling_percase where sampling_id = s.sampling_id and curdate() between valid_from and valid_to limit 1) is null,'No','Yes') per_case, activated_on, s.request_id, co.cache_distributor_id, co.cache_distributor_name, co.address from sampling s join common_outlets co on s.outlet_id = co.id where s.active = 1 "+WhereRegion+" having (/* ledger_balance != 0 or */   curdate() between fixed_valid_from and fixed_valid_to or per_case = 'Yes') "+FixedWhere+PerCaseWhere+AdvanceAmountWhere+" order by cache_distributor_id, activated_on desc ";
					//}
					//System.out.println(Query);
					ResultSet rs = s.executeQuery(Query);
					while(rs.next()){						
					
						TotalBalance += rs.getDouble("ledger_balance");
						
						String OutletLabel = rs.getLong("outlet_id")+" - "+rs.getString("name");
						String DistributorLabel = "";
						if (rs.getLong("cache_distributor_id") != 0){
							DistributorLabel = rs.getLong("cache_distributor_id") + " - " + rs.getString("cache_distributor_name");
						}
					%>
					
						<tr>
						<td><%=serial %></td>
						<td><%=rs.getLong("request_id") %></td>
						<td><%=Utilities.truncateStringToMax(OutletLabel, 30) %></td>
						<td><%=Utilities.truncateStringToMax(rs.getString("address"), 30) %></td>
						<td><%=Utilities.truncateStringToMax(DistributorLabel, 30) %></td>
						<td style="text-align:right"><%if(rs.getDouble("ledger_balance")!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(rs.getDouble("ledger_balance")) %><%} %></td>
						<td style="text-align:right"><%if(rs.getLong("fixed_company_share")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs.getLong("fixed_company_share")) %><%} %></td>
						<td><%if(rs.getDate("fixed_valid_from")!=null && rs.getDate("fixed_valid_to")!=null){%><%=Utilities.getDisplayDateFormat(rs.getDate("fixed_valid_from"))%> - <%=Utilities.getDisplayDateFormat(rs.getDate("fixed_valid_to")) %><%} %></td>
						<td><%if(rs.getString("per_case").equals("Yes")){%><a href="#popupDialog15" data-rel="popup" data-position-to="window" data-transition="pop" id="PercaseView" onClick="R164LoadPerCaseView(<%=rs.getLong("sampling_id")%>);">View</a><%} else{ %><%=rs.getString("per_case") %><%} %></td>
						<td><%=Utilities.getDisplayDateFormat(rs.getDate("activated_on")) %></td>
						</tr>
						
						<%
						serial++;
					}
						%>
						
						<tr>
						
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td style="text-align:right"><%if(TotalBalance!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalBalance) %><%} %></td>
						<td style="text-align:right"></td>
						<td></td>
						<td></td>
						<td></td>
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