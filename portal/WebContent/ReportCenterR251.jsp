<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.outlet.LedgerTransaction"%>
<%@page import="com.pbc.outlet.Advance"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@page import="com.pbc.util.Utilities"%>
<jsp:useBean id="bean" class="com.pbc.outlet.OutletDashboard" scope="page"/>
<jsp:setProperty name="bean" property="*"/>
<%
if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}
%>


<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 315;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));
if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}
//
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
	WhereOutlets = " and outlet_id in ("+OutletIds+") ";	
	System.out.println(WhereOutlets);
}

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
	OrderBookerIDsWher =" and created_by in ("+OrderBookerIDs+") ";
}


//Distributor

boolean IsDistributorSelected=false;
long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	IsOrderBookerSelected=true;
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");
	IsDistributorSelected = true;
}else{
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
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
	WhereDistributors = " and census_distributor_id in ("+DistributorIDs+") ";
	//out.print("Hello "+WhereDistributors);
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
	WhereHOD = " and dbpav.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and dbpav.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
}



//Outlet Type


String OutletTypes="";
String SelectedOutletTypeArray[]={};
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutletType") != null){
	SelectedOutletTypeArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutletType");
	//OutletTypes = Utilities.serializeForSQL(SelectedOutletTypeArray);
}

String WhereDiscountedAll = "";
String WhereDiscountedFixed = "";
String WhereDiscountedPerCase = "";
String WhereActive = "";
String WhereDeactivated = "";
String WhereNonDiscountedAll ="";


for(int i=0;i<SelectedOutletTypeArray.length;i++){
	if(SelectedOutletTypeArray[i].equals("Discounted - All")){	
		WhereDiscountedAll = " and outlet_id in (select outlet_id from sampling where active = 1) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Discounted - Fixed")){	
		WhereDiscountedFixed = " and outlet_id in (select outlet_id from sampling where active = 1 and date(now()) between fixed_valid_from and fixed_valid_to and fixed_company_share != 0) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Discounted - Per Case")){	
		WhereDiscountedPerCase = " and outlet_id in (select distinct outlet_id from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where s.active = 1 and date(now()) between sp.valid_from and sp.valid_to) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Non Discounted")){	
		WhereNonDiscountedAll = " and outlet_id not in (select outlet_id from sampling where active = 1) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Active")){	
		WhereActive = " and co.is_active=1 ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Deactivated")){	
		WhereDeactivated = " and co.is_active=0 ";
	}
}




//CensusUser


String CensusUserIDs="";
long SelectedCensusUserArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedCensusUser") != null){
	SelectedCensusUserArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedCensusUser");
	CensusUserIDs = Utilities.serializeForSQL(SelectedCensusUserArray);
}

String WhereCensusUser = "";
if (CensusUserIDs.length() > 0){
	WhereCensusUser = " and created_by in ("+CensusUserIDs+") ";	
}

System.out.println(WhereCensusUser);

%>


<script type="text/javascript">



	function redirect(url){
		document.getElementById("check").action = url;
		document.getElementById("check").submit();
	}
	
	

	


		
</script>


<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>
<%if(OutletIds.length()>0){ %>
  <table border="1" data-role="table" id="table-custom-2" class="ui-body-d ui-shadow table-stripe ui-responsive" data-column-btn-theme="c" data-column-popup-theme="a" style="font-size: 10pt;">
         <thead>
           <tr class="ui-bar-d">
             <th >Request ID</th>
             <th>Advance Company Share</th>
             <th>Fixed Company Share</th>             
             <th>Fixed Deduction Term</th>
             <th>Fixed Valid From</th>
             <th>Fixed Valid To</th>
             <th>Fixed Company Share Offpeak</th>
             <th>Fixed Deduction Term Offpeak</th>
             <th>Activated On</th>
             <th>Deactivated On</th>
             <th>Deactivation Timestamp</th>
             <th>Active</th>
           </tr>
         </thead>
         <tbody>
         	<%
         		ResultSet rs = s.executeQuery("SELECT request_id, sampling_id, advance_company_share, fixed_company_share, fixed_deduction_term, fixed_valid_from, fixed_valid_to,  active, activated_on, deactivated_on, fixed_company_share_offpeak, fixed_deduction_term_offpeak, deactivation_timestamp, ( SELECT count(sampling_id) FROM sampling_percase where sampling_id=sampling.sampling_id ) per_case_rows FROM sampling where outlet_id = "+OutletIds+" order by activated_on desc, active desc");
         		while( rs.next() ){
         			
         			int PerCaseRows = rs.getInt("per_case_rows");
         			
         			%>
         			<tr>
						<td><%=rs.getString("request_id")%></td>
						<td><%=rs.getString("advance_company_share")%></td>
						<td><%=rs.getString("fixed_company_share")%></td>
						<td><%=rs.getString("fixed_deduction_term")%></td>
						<td><%=Utilities.getDisplayDateFormat(rs.getDate("fixed_valid_from"))%></td>
						<td><%=Utilities.getDisplayDateFormat(rs.getDate("fixed_valid_to"))%></td>
						
						
						<td><%=rs.getString("fixed_company_share_offpeak")%></td>
						<td><%=rs.getString("fixed_deduction_term_offpeak")%></td>
						<td><%=Utilities.getDisplayDateFormat(rs.getTimestamp("activated_on"))%></td>
						<td><%=Utilities.getDisplayDateFormat(rs.getTimestamp("deactivated_on"))%></td>
						<td><% if( rs.getTimestamp("deactivation_timestamp") != null ){ out.print(Utilities.getDisplayDateFormat(rs.getTimestamp("deactivation_timestamp"))+" "+Utilities.getDisplayTimeFormat(rs.getTimestamp("deactivation_timestamp"))); } %></td>
						<td <% if( rs.getString("active").equals("1") ){%> style="background: #000; color: #fff;" <% } %> ><%=rs.getString("active")%></td>
         			</tr>
         			<% if( PerCaseRows > 0 ){ %>
         			<tr>
         				<td colspan="4">&nbsp;</td>
         				<td colspan="8" >
         					<div style="width: 100%;">
         					<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
         						<li data-role="list-divider">Sampling Detail </li>
         						<li>
		         					<table align="center" style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0">
		         					
		         						<thead>
		         						<tr>
			         						<th>Package</th>
			         						<th>Brand</th>
			         						<th>Company Share</th>
			         						<th>Deduction Term</th>
			         						<th>Valid From</th>
		         							<th>Valid To</th>
		         						</tr>
		         						</thead>
		         						<%
		         						ResultSet rs2 = s2.executeQuery("SELECT *, ( SELECT label FROM inventory_packages where id=sampling_percase.package ) package_label, ( SELECT label FROM inventory_brands where id=sampling_percase.brand_id ) brand_label FROM sampling_percase where sampling_id="+rs.getString("sampling_id"));
		         						while( rs2.next() ){
		         							%>
		         							<tr>
				         						<td><%=rs2.getString("package_label")%></td>
				         						<td><% if( rs2.getString("brand_label") != null ){ out.print( rs2.getString("brand_label") ); }else{ out.print("All"); } %></td>
				         						<td><%=rs2.getString("company_share")%></td>
				         						<td><%=rs2.getString("deduction_term")%></td>
				         						<td><%=Utilities.getDisplayDateFormat(rs2.getDate("valid_from"))%></td>
			         							<td><%=Utilities.getDisplayDateFormat(rs2.getDate("valid_to"))%></td>
			         						</tr>
		         							<%
		         						}
		         						%>
		         					</table>
	         					</li>
         					</ul>
         					</div>
         				</td>
         			</tr>
         			<%
         			} // end if
         		}
         	%>
         	
         </tbody>
       </table>           

<%
bean.close();
%>

<%}else{%>
	
	
	Please Select Outlet. 
	
	
<%
}
%>
</li>	
</ul>
 

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>