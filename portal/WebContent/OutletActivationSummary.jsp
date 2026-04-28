<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@include file="include/ValidateSession.jsp"%>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@include file="include/ValidateSession.jsp"%>






<%

long SessionUserID = Utilities.parseLong((String) session.getAttribute("UserID"));

int FeatureID = 480;
if (Utilities.isAuthorized(FeatureID, SessionUserID) == false) {
	response.sendRedirect(Utilities.getAccessDeniedPageURL(request));
}
	long OUTLETID = Utilities.parseLong(request.getParameter("OUTLETID"));
    long PriceListID = Utilities.parseLong(request.getParameter("pricelistid"));
   




Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();
Statement s1 = ds.createStatement();



int accountType=0;
String outletName="";
String outletAddress="";
int outletType=0;
int outletRegion=0;
int outletDistributor=0;
int outletCategory=0;
double outletLat=0;
double outletLng=0;
int outletChannel=0;
int outletHandler=0;
int outletGeoRadisu=0;
int isFiler=0;
int isGeoFence=0;
int isActivePJP=0;
int FilerTypeId=0;
String ownerName="";
String ownerContact ="";
String ownerCNIC ="";
String ownerNTN ="";
String ownerSTRN="";
String keyLocation="";
int CreditLimit=0;

Date CreatedDate = new java.util.Date();
Date outletDate = null;
boolean flag=false;
if(OUTLETID>0){
	
	System.out.println("select *,(select contact_name from common_outlets_contacts coc where coc.outlet_id=co.id and coc.is_primary=1) as contact_name,(select contact_number from common_outlets_contacts coc where coc.outlet_id=co.id and coc.is_primary=1) as contact_number,(select contact_nic from common_outlets_contacts coc where coc.outlet_id=co.id and coc.is_primary=1) as contact_nic from common_outlets co where id="+OUTLETID+"");

	ResultSet rs=s.executeQuery("select *,(select is_active from distributor_beat_plan_schedule pjps where pjps.outlet_id=co.id limit 1) as is_active_in_pjp,(select contact_name from common_outlets_contacts coc where coc.outlet_id=co.id and coc.is_primary=1) as contact_name,(select contact_number from common_outlets_contacts coc where coc.outlet_id=co.id and coc.is_primary=1) as contact_number,(select contact_nic from common_outlets_contacts coc where coc.outlet_id=co.id and coc.is_primary=1) as contact_nic from common_outlets co where id="+OUTLETID+"");
	if(rs.first()){
		flag=true;
		outletName=rs.getString("name");
		outletAddress=rs.getString("address");
		outletType=rs.getInt("type_id");
		outletRegion=rs.getInt("region_id");
		outletDistributor=rs.getInt("distributor_id");
		outletCategory=rs.getInt("category_id");
		outletLat=rs.getDouble("lat");
		outletLng=rs.getDouble("lng");
		outletChannel=rs.getInt("pic_channel_id");
		outletHandler=0;
		outletGeoRadisu=rs.getInt("geo_radius");
		isFiler=rs.getInt("is_filer");
		isGeoFence=rs.getInt("is_geo_fence");
		isActivePJP=rs.getInt("is_active_in_pjp");
		outletDate=rs.getDate("created_on");
		ownerName=rs.getString("contact_name");
		ownerContact =rs.getString("contact_number");
		keyLocation="";
		CreditLimit=0;
		accountType=0;
		FilerTypeId=0;

		if(ownerName==null){
	ownerName="";
		}
		if(ownerContact==null){
	ownerContact="";
		}
		ownerCNIC =rs.getString("contact_nic");
		if(ownerCNIC==null){
	ownerCNIC="";
		}
		
	ownerNTN="";
		
		
		
			ownerSTRN="";
	
		
		
		if(keyLocation==null){
			keyLocation="";
		}
		
	}
	rs.close();
}



if(flag){
%>


    	
		<form id="OutletEditForm" name="OutletEditForm" action="" method="POST" data-ajax="false">
			<input type="hidden"  id="EditOutletID" name="EditOutletID" value="<%=OUTLETID%>">
			<input type="hidden"  id="AccountTypeIDHidden" name="AccountTypeIDHidden" value="<%=accountType%>">
			<ul data-role="listview" data-inset="false" data-divider-theme="c">
				<li>

					<table border="0" style="width: 100%">

						<tr>
							<td style="width: 25%;">Is Active</td>
							<td style="width: 25%;">OutLet Name</td>
							<td style="width: 25%;">Outlet Address</td>
							<td style="width: 25%;">Outlet Region</td>
						

						</tr>
						<tr>
						<td style="width: 25%;"><input type="checkbox" value="1"  <%= (isActivePJP==1 ? "checked" :" " ) %>
								name="isActivePJP" id="isActivePJP" class="custom"
								data-mini="true" /> <label for="isActivePJP">Is Active</label></td>
							<td style="width: 25%;"><input type="text" readonly="readonly"
								placeholder="Outlet Name" id="OutletName" name="OutletName"
								data-mini="true" value="<%=outletName%>"></td>
							<td style="width: 25%;"><input type="text" readonly="readonly"
								placeholder="Outlet Address" id="OutletAddress" name="OutletAddress"
								data-mini="true" value="<%=outletAddress%>"></td>
							<td style="width: 25%;"><select id="OutletRegion" disabled="disabled"
								name="OutletRegion" data-mini="true">
								<option value="-1">Select Outlet Region</option>
								<% ResultSet rs1 = s.executeQuery("select * from common_regions");
								while (rs1.next()) {
									int regionID=rs1.getInt("region_id");
									String regionLabel=rs1.getString("region_name");
								%>
									
									<option <%= (regionID==outletRegion ? "selected" :" " ) %> value="<%=regionID%>"><%=regionLabel %></option>
								<% } %>	
							</select></td>
			



						</tr>
					</table>
				</li>
				<li>

					<table border="0" style="width: 100%">

						<tr>
							<td style="width: 25%;">Distributor</td>
							<td style="width: 25%;">Category</td>
							<td style="width: 25%;">Lat</td>
							<td style="width: 25%;">Lng</td>

						</tr>
						<tr> 
							<td style="width: 25%;"><select id="OutletDistributor" disabled="disabled"
								name="OutletDistributor" data-mini="true">
									<option value="-1">Select Outlet Distributor</option>
									<% ResultSet rs3 = s.executeQuery("select * from common_distributors");
								while (rs3.next()) {
									int distributorID=rs3.getInt("distributor_id");
									String distributorLabel=rs3.getString("name");
								%>
									
									<option <%= (distributorID==outletDistributor ? "selected" :" " ) %> value="<%=distributorID%>"><%=distributorLabel %></option>
								<% } %>	
							</select></td>
							<td style="width: 25%;"><select id="OutletCategory" disabled="disabled"
								name="OutletCategory" data-mini="true">
								<option value="-1">Select Outlet Category</option>
										<% ResultSet rs4 = s.executeQuery("SELECT * FROM common_outlets_categories");
								while (rs4.next()) {
									int categoryID=rs4.getInt("id");
									String categoryLabel=rs4.getString("label");
								%>
									
									<option <%= (categoryID==outletCategory ? "selected" :" " ) %> value="<%=categoryID%>"><%=categoryLabel %></option>
								<% } %>
							</select></td>
							<td style="width: 25%;"><input type="text" placeholder="" readonly="readonly"
								id="OutletLat" name="OutletLat" data-mini="true" value="<%=outletLat%>"></td>
							<td style="width: 25%;"><input type="text" placeholder="" readonly="readonly"
								id="OutletLng" name="OutletLng" data-mini="true" value="<%=outletLng%>"></td>


						</tr>
					</table>
				</li>
				<li>

					<table border="0" style="width: 100%">

						<tr>
						
							<td style="width: 25%;">Channel</td>
							<td style="width: 25%;"></td>
						</tr>
						<tr>
						
							<td style="width: 25%;"><select id="OutletChannel"	name="OutletChannel" data-mini="true">
								<option value="-1">Select Outlet Channel</option>
									<% ResultSet rs5 = s.executeQuery("SELECT * FROM pci_sub_channel");
								while (rs5.next()) {
									int channelID=rs5.getInt("id");
									String channelLabel=rs5.getString("label");
								%>
									
									<option <%= (channelID==outletChannel ? "selected" :" " ) %> value="<%=channelID%>"><%=channelLabel %></option>
								<% } %>
							</select></td>
							<td><a  data-icon="check" data-mini="true" data-role="button" data-theme="a" data-role="button" 
							data-inline="true" id="OutletSave"  href="#" onClick="SaveOutletActivation(<%=OUTLETID %>)" >Save</a>
						</td>
						</tr>
						
						
						 <tr>
				     	<td>
				     
				     		<table class="GridWithBorder">
				     		<tr>
								<%
						
								
									int i = 1, year=0;
									ResultSet rsi = s.executeQuery("select * from mobile_outlets_request_files where outlet_request_id in (select mobile_transaction_no from common_outlets where id="+OUTLETID+")");
									while (rsi.next()) {
										year = rsi.getInt("year");
								%>
								
									
									
									<td>
										<h3></h3><br/>
									 <center>
									
										 <img src="<%=( (year!=0) ? "common/CommonFileDownloadFromPath" : "mobile/MobileFileDownloadMDE" ) %>?file=<%=rsi.getString("filename")%>&filePath=<%=rsi.getString("uri")%>" style="width:500px; height:500px;" />
										
									</center>
									
									</td>
									
									
									
								<%
									i++;
									}
								%>
								</tr>
							</table>
				     
				     	</td>
				    </tr>
				   
				    
					</table>
				</li>
				
		
				
				
				
				
			</ul>
		</form>
		
		<% } else{ %>
			
		<div style="margin-top:20px;margin-left:40%;width:10%">
			
					<span >No Outlet Found</span>
			
		</div>
		
		<%} %>
	
<%
s.close();
s1.close();
ds.dropConnection();

%>		
<%!public static String getDisplayDateFormatInternal(Date val) {

	val=Utilities.getDateByDays(1);
	
	if (val != null) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return "" + format.format(val);

	} else {

		return null;

	}

}%>	

<script type="text/javascript">

function SaveOutletActivation(outlet_id)
{
	var checked = document.getElementById("isActivePJP").checked;
	var active = (checked ? 1 : 0);
	
	//alert(D1);
	$.mobile.showPageLoadingMsg();	
	
	$.ajax({
		
		url: "outlet/ActiveOutletExecute",
		data: {
			active: active,
			outlet: outlet_id,
			channel_id :  document.getElementById("OutletChannel").value,
			channel_update : (<%=outletChannel%> == document.getElementById("OutletChannel").value )
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			$.mobile.hidePageLoadingMsg();
			if(json.success == "true"){
				$.mobile.hidePageLoadingMsg();
				alert("Outlet Active/Deactive or channel update Successfully!");
				window.location="OutletActivation.jsp";
				
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
