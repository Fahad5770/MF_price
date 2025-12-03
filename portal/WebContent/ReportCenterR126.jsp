<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="com.pbc.reports.SalesIndex"%>
<%@page import="java.util.Calendar"%>
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
th{
font-size: 8pt;
}

#map {
        width: 100%;
        height: 200px;
        margin-top: 10px;
      }

</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 128;

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

//System.out.println("sDate "+StartDate);

if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	//StartDate = new Date(); // add code of start of current month if first time report opens
	Calendar cc = Calendar.getInstance();   // this takes current date
    cc.set(Calendar.DAY_OF_MONTH, 1);
    StartDate = cc.getTime();
     
	
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
	WhereOutlets = " and outlet_id in ("+OutletIds+") ";	
}


String UserAccessWhere = "";
if(FeatureID != 0)
{
	Distributor [] DistributorObj = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
	String DistributorsIds = UserAccess.getDistributorQueryString(DistributorObj);
	if(!DistributorsIds.equals("")){
		UserAccessWhere = " and distributor_id in("+DistributorsIds+")";
	}
}

//Asset Number

long AssetNumber=0;
String AssetNumberOutletID="";
String AssetNumberWhere="";

           boolean IsAssetNumberSelected=false;

           if (session.getAttribute(UniqueSessionID+"_SR1SelectedAssetNumber") != null){
        	   AssetNumber = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedAssetNumber");	
           	
           	
           }
           if(AssetNumber!=0){
        	   ResultSet rs = s.executeQuery("select outlet_id FROM common_assets where main_asset_number = "+AssetNumber);
        	   if(rs.first()){
        		   AssetNumberOutletID = rs.getString("outlet_id");  
        	   }
           }
           boolean IsValidAssetNumber=false;
           //System.out.println(AssetNumber);
           
           //updating the outlet ids
           if(!AssetNumberOutletID.equals("")){ //invalide asset number
        	   IsValidAssetNumber = true;
           }
           
           if(AssetNumber !=0 && OutletIds.equals("")){ //mean outlet id not select and only asset number select
        	   //System.out.println("asfsd");
        	   OutletIds =AssetNumberOutletID;
           }
           


%>

<ul data-role="listview" data-inset="true"  style=" font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Outlet Profile</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
				
			<%if(1==1){ %>
			<div class="ui-grid-a">
			    <div class="ui-block-a" style="width:100%">
			    	<div class="ui-bar" style="min-height:60px;">
			    		<table border="0" style="width:100%; " class="GridWithBorder">
							<%
							double lat=0;
							double longi=0;
							String OutletName="";
							
							String SQL = "SELECT name, address, lat, lng FROM common_outlets where id = '"+OutletIds+"'";
							ResultSet rs = s.executeQuery(SQL);
							if(rs.first()){
								lat = rs.getDouble("lat");
								longi = rs.getDouble("lng");
								OutletName = rs.getString("name");
							%>
							
							<tr>
								<td>Outlet</td>
								<td><%=OutletIds %> - <%=rs.getString("name") %></td>
							</tr>
							
							
							<tr>
								<td>Address</td>
								<td><%=rs.getString("address") %></td>
							</tr>
							<%
							}
							%>
					    </table>
					    <ul data-role="listview" data-inset="true" >
							<li data-role="list-divider" data-theme="c">Contact Information</li>
						</ul>
					    <table class="GridWithBorder" style="font-size: 10pt; width:100%; margin-top:10px">
						
							<tr >
								<th data-priority="1" style="width:25%">Contact Name</th>
								<th data-priority="1" style="width:25%">Number</th>
								<th data-priority="1" style="width:25%">NIC</th>
								<th data-priority="1" style="width:25%">Relation</th>
							</tr>
							<%
							//System.out.println("SELECT id, contact_name, contact_number, contact_nic, type_id, (SELECT label FROM common_outlets_contacts_types where id=5) relation FROM common_outlets_contacts where outlet_id="+OutletIds+" and type_id=5 and is_primary=1 ");
							ResultSet rs2 = s2.executeQuery("SELECT id, contact_name, contact_number, contact_nic, type_id, (SELECT label FROM common_outlets_contacts_types where id=5) relation FROM common_outlets_contacts where outlet_id='"+OutletIds+"' and type_id=5 and is_primary=1 ");
							while(rs2.next()){
							%>
							<tr>
								<td style="width:25%"><%=rs2.getString("contact_name") %></td>
								<td style="width:25%"><%=rs2.getString("contact_number") %></td>
								<td style="width:25%"><%=rs2.getString("contact_nic") %></td>
								<td style="width:25%"><%=rs2.getString("relation") %></td>
							</tr>
							<%
							}
							%>
						
					</table>
					 <ul data-role="listview" data-inset="true" >
							<li data-role="list-divider" data-theme="c">Area</li>
					 </ul>
					<table class='GridWithBorder' style='font-size: 10pt; width:100%; margin-top:10px'>
						
	   						<tr >
	   							<th style="width:25%">PJP</th>
	   							<th style="width:25%">Distributor</th>
	   							<th style="width:25%">Order Booker</th>
	   							<th style="width:25%">Product Group</th>
	   						</tr>
	   						<%
	   						ResultSet rs21 = s2.executeQuery("SELECT distinct id,label,distributor_id,distributor_name,product_group_id,product_group_name,assigned_to,assigned_to_name FROM distributor_beat_plan_all_view where outlet_id='"+OutletIds+"'");
	   						while(rs21.next()){
	   						
	   						%>
	   						<tr>
	   							<td style="width:25%"><%=rs21.getString("id") %> - <%=rs21.getString("label") %></td>
	   							<td style="width:25%"><%=rs21.getString("distributor_id") %> - <%=rs21.getString("distributor_name") %></td>
	   							<td style="width:25%"><%=rs21.getString("assigned_to") %> - <%=rs21.getString("assigned_to_name") %></td>
	   							<td style="width:25%"><%=rs21.getString("product_group_id") %> - <%=rs21.getString("product_group_name") %></td>
	   						</tr>
	   						<%
	   						}
	   						%>
				    	
					</table>
					 <ul data-role="listview" data-inset="true" >
							<li data-role="list-divider" data-theme="c">TOT</li>
					 </ul>
					<table class='GridWithBorder' style='font-size: 10pt; width:100%; margin-top:10px'>
						
	   						<tr >
	   							<th>Asset Number</th>
	   							<th>Inventory Number</th>
	   							<th>Agreement Record</th>
	   							<th>Rnd Verification</th>
	   							<th>250ml Quantity</th>
	   							<th>Litre Quantity</th>
	   							<th>Tot Status</th>
	   							<th>Movement Date</th>
	   							
	   						</tr>
	   						<%
	   						ResultSet rs3 = s2.executeQuery("SELECT inventory_number,agreement_record,rnd_verification,250ml_quantity,litre_quantity,tot_status,movement_date,main_asset_number FROM common_assets where outlet_id_parsed='"+OutletIds+"' order by movement_date_parsed desc");
	   						while(rs3.next()){
	   						
	   						%>
	   						
	   						<tr>
	   							<td><%=rs3.getString("main_asset_number") %></td>
	   							<td><%=rs3.getString("inventory_number") %></td>
	   							<td><%=rs3.getString("agreement_record") %></td>
	   							<td><%=rs3.getString("rnd_verification") %></td>
	   							<td><%=rs3.getInt("250ml_quantity") %></td>
	   							<td><%=rs3.getInt("litre_quantity") %></td>
	   							<td><%=rs3.getString("tot_status") %></td>
	   							<td><%=rs3.getString("movement_date") %></td>
	   							
	   							
	   						</tr>
	   						
	   						<%
	   						}
	   						%>
				    	
					</table>
					 <ul data-role="listview" data-inset="true" >
							<li data-role="list-divider" data-theme="c">Map</li>
					 </ul>
					<table class='GridWithBorder' style='font-size: 10pt; width:100%; margin-top:10px'>
						
			    	
			    	<script>
			    	 initMap();

			    	function initMap(){
			    		//alert(<%=lat%>);
			    		  var myLatlng = new google.maps.LatLng(31.427335740000000,73.094063400000000);
			    		  var mapOptions = {
			    		    zoom: 15,
			    		    center: myLatlng
			    		  };

			    		  var map = new google.maps.Map(document.getElementById('map'), mapOptions);

			    		  var contentString = '<div id="content">Outlet ID 6205<br>View Dashboard</div>';

			    		  var infowindow = new google.maps.InfoWindow({
			    		      content: contentString,
			    		      maxWidth: 200
			    		  });
			    		  
			    		  var markers = new Array();
			    		  
			    			 
			    		map.setCenter(new google.maps.LatLng(<%=lat%>,<%=longi%>));
			    				  
			    			  
			    			  markers[0] = new google.maps.Marker({
			    			      position: new google.maps.LatLng(<%=lat%>,<%=longi%>),
			    			      map: map,
			    			      title: '<%=OutletName%>'
			    			      
			    			  });
			    			  google.maps.event.addListener(markers[0], 'click', function() {
			    			    infowindow.open(map,markers[0]);
			    			    
			    			    var infoWindowContent = "<%=OutletIds%>-<%=OutletName%><br>"; 
			    			    infoWindowContent += "<a href='#'>View Detail</a>";
			    			    infowindow.setContent(infoWindowContent);
			    			    //infowi
			    			  });
			    			  
			    	}	 
			    		  
			    			 
			    		
			    	
			    	
			    	
			    	</script>
			    	
					</table>
			    	
			    	<div id="map" style=""></div>
			    	
			    	
			    	<ul data-role="listview" data-inset="true" >
							<li data-role="list-divider" data-theme="c">Open Complaints</li>
					 </ul>
					<table class='GridWithBorder' style='font-size: 10pt; width:100%; margin-top:10px'>
						
	   						<tr >
	   							<th>Complaint ID</th>
	   							<th>Description</th>
	   							<th>Type</th>
	   							<th>Assigned</th>
	   							<th>Resolved</th>
	   							<th>Verified</th>	   							
	   							
	   						</tr>
	   						
	   						<%
	   						ResultSet rs1 = s.executeQuery("select cc.id,cc.description,cct.label,cc.is_assigned,if(cc.is_assigned=1,'Yes','No') assigned_label,cc.is_verified,if(cc.is_verified=1,'Yes','No') verified_label,cc.is_resolved, if(cc.is_resolved=1,'Yes','No') resolved_label from crm_complaints cc join crm_complaints_types cct on cc.type_id=cct.id join crm_complaints_assigned cca on cc.id=cca.id where cc.is_verified=0 and cca.outlet_id='"+OutletIds+"'");
	   						while(rs1.next()){
	   						%>
	   						
	   						<tr>
	   							<td><%=rs1.getLong("id") %></td>
	   							<td><%=rs1.getString("description") %></td>
	   							<td><%=rs1.getString("label") %></td>
	   							<td><%=rs1.getString("assigned_label") %></td>
	   							<td><%=rs1.getString("resolved_label") %></td>
	   							<td><%=rs1.getString("verified_label") %></td>	   							
	   						</tr>
	   						<%
	   						}
	   						%>
	   						
				    	
					</table>
			    	
			    	
			    	
			    	</div>
			    </div>
			    <div class="ui-block-b" style="width:0%">
			    	<div class="ui-bar" style="min-height:60px;">
			    		
			    	</div>
			    </div>
			</div><!-- /grid-a -->
			<%}else{ %>
			<p style="padding-top:20px;">Please enter Outlet ID</p>
			<%} %>
			
			
						
					
							
				
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