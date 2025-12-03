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

</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 321;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

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
	//System.out.println(WhereOutlets);
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
String WhereDistributorID="";

if(SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0){
	for(int i = 0; i < SelectedDistributorsArray.length ; i++){
		
		if(i == 0){
			DistributorIDs += SelectedDistributorsArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorsArray[i];
		}
	}
	WhereDistributorID = " distributor_id in ("+DistributorIDs+") and distributor_id!=100914 ";
	
}

//System.out.println("WhereDistributorID : "+WhereDistributorID);

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
	//alert(id);
//	$("#SearchContent").focus();
	//document.getElementById("DeliveryNoteFromDate").disabled = "disabled";
	//document.getElementById("DeliveryNoteToDate").disabled = "disabled";
	$("#R370New34" ).popup( "open" );
	 $.get('ReportCenterR257OutletImagesPopUp.jsp?outletId='+id, function(data) {
		
		  $("#SearchContent").html(data);
		  $("#SearchContent").trigger('create');
		  
	}); 
	//return false;

}
</script>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Outlet Chanel Tagging</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top"> 
			<table border=0 style="font-size:13px; font-weight: 400; width:100%;overflow-x: scroll;" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder" >
					 <thead>
					   
					   
					   
					    <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center; width:15%; ">Outlet</th>
							<th data-priority="1"  style="text-align:center; width:11%;">Address</th>							
							<th data-priority="1"  style="text-align:center; width:15%;">Distributor</th>
							<th data-priority="1"  style="text-align:center; width:15%;">Order Booker</th>
							<th data-priority="1"  style="text-align:center; width:6%;">LAT</th>
							<th data-priority="1"  style="text-align:center; width:6%;">Long</th>
							<th data-priority="1"  style="text-align:center; width:10%;">Sub Channel</th>
							<th data-priority="1"  style="text-align:center; width:6%;">Beet Frequency</th>							
							<th data-priority="1"  style="text-align:center; width:7%;">Updated On</th>
							<th data-priority="1"  style="text-align:center; width:14%;">Updated By</th>
							<th data-priority="1"  style="text-align:center; width:10%;">Images</th>
													
					    </tr>
					  </thead> 
					<tbody>
						<%
						String day="",distributorNmae="",orerBooker="";
						//System.out.println("SELECT ecl.id,ecl.distributor_id,(select name from common_distributors cd where cd.distributor_id=ecl.distributor_id) dist_name,ecl.credit_type,(select label from  ec_empty_credit_types ect where ect.id=ecl.credit_type) credit_type_name,ecl.reason,ecl.start_date,ecl.end_date FROM ec_empty_credit_limit ecl  where ecl.is_active=1 and "+Utilities.getSQLDate(StartDate)+" between start_date and end_date"+WhereCustomerID);
						//ResultSet rs = s.executeQuery("select co.id, concat_ws('',co.id,' - ',co.name) Outlet,co.Address,(select concat_ws('',distributor_id,' - ',name)  from common_distributors cd where cd.distributor_id=co.distributor_id) dist_name, round(co.lat,7) lat,round(co.lng,7) lng,(select pc.label from pci_sub_channel pc where pc.id=co.pic_channel_id) pic_label, co.updated_on,updated_by,(select display_name from users u where u.ID=co.updated_by) Updated_by_name from common_outlets co where co.pic_channel_id!='' and updated_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereOutlets +WhereDistributorID+ " order by updated_on desc");
						//ResultSet rs = s.executeQuery("select co.id, concat_ws('',co.id,' - ',co.name) Outlet,co.Address,(select concat_ws('',distributor_id,' - ',name)  from common_distributors cd where cd.distributor_id=dbp.distributor_id) dist_name, round(co.lat,7) lat,round(co.lng,7)lng,(select pc.label from pci_sub_channel pc where pc.id=co.pic_channel_id) pic_label, co.updated_on,co.updated_by, (select display_name from users u where u.ID=co.updated_by) Updated_by_name from distributor_beat_plan_view dbp join common_outlets co  on dbp.outlet_id=co.id where co.pic_channel_id!='' and co.updated_on between  "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereOutlets +WhereDistributorID+ " order by co.updated_on desc");
						System.out.println("select co.id, concat_ws('',co.id,' - ',co.name) Outlet,co.Address, round(co.lat,7) lat,round(co.lng,7)lng,(select pc.label from pci_sub_channel pc where pc.id=co.pic_channel_id) pic_label, co.updated_on,co.updated_by, (select display_name from users u where u.ID=co.updated_by) Updated_by_name from common_outlets co   where co.pic_channel_id!='' and co.updated_on between  "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereOutlets+ " and id in(select outlet_id from distributor_beat_plan_view where "+WhereDistributorID+")  order by co.updated_on desc");
						ResultSet rs = s.executeQuery("select co.id, concat_ws('',co.id,' - ',co.name) Outlet,co.Address, round(co.lat,7) lat,round(co.lng,7)lng,(select pc.label from pci_sub_channel pc where pc.id=co.pic_channel_id) pic_label, co.updated_on,co.updated_by, (select display_name from users u where u.ID=co.updated_by) Updated_by_name from common_outlets co   where co.pic_channel_id!='' and co.updated_on between  "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereOutlets+ " and id in(select outlet_id from distributor_beat_plan_view where "+WhereDistributorID+")  order by co.updated_on desc");
						while(rs.next()){
							ResultSet rsDay = s2.executeQuery("SELECT long_name FROM pep.common_days_of_week where id in (SELECT day_number FROM pep.distributor_beat_plan_schedule where outlet_id="+rs.getString("co.id")+")");
							if(rsDay.first()){
								day= rsDay.getString("long_name");
							}
							
							long outlet_id = rs.getLong("id");
							ResultSet rsDistributor = s2.executeQuery("select concat(distributor_id,'-',(select name from common_distributors cd where cd.distributor_id=pjpv.distributor_id)) as name , (SELECT DISPLAY_NAME FROM users u WHERE u.id = assigned_to) PSR_name from distributor_beat_plan_view pjpv where outlet_id="+rs.getString("co.id")+" and distributor_id!=100914");
							if(rsDistributor.first()){
								distributorNmae= rsDistributor.getString("name");
								orerBooker= rsDistributor.getString("PSR_name");
							}
							%>
							<tr>
								<td ><%=rs.getString("Outlet") %></td>
								<td ><%=rs.getString("Address") %></td>
								<td ><%=distributorNmae %></td>
								<td ><%=orerBooker %></td>
								<td ><%=rs.getString("lat") %></td>
								<td><%=rs.getString("lng") %></td>
								<td><%=rs.getString("pic_label") %></td>
								<td><%=day %></td>
								<td><%=Utilities.getDisplayDateFormat(rs.getDate("updated_on")) %></td>								
								<td><%=rs.getString("updated_by") %> - <%=rs.getString("updated_by_name") %></td>
								<td><a  data-inline="true" id="PhysicalStockAdjustmentSave" href="#" onClick="outletImages(<%=rs.getString("id")%>)"data-rel="popup" data-position-to="window" data-transition="pop">Outlet Images</a></td>
							</tr>
							<%-- <td><a  href="OutletImagesPopUp.html?outletId=<%=rs.getString("id")%> onClick="outletImages(<%=rs.getString("id")%>)"" data-rel="dialog">Outlet Images</a>	</td> --%>				
							
						<%	
						}
						
						
						%>
						
						
						
					</tbody>
							
				</table>
		</td>
	</tr>
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

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>