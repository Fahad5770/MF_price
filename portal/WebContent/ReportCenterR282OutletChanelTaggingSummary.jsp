<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>


<style>
#map {
        width: 100%;
        height: 500px;
        margin-top: 10px;
      }
</style>

<%


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 352;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


int Flag=Utilities.parseInt(request.getParameter("flag"));
long MobileOrder=Utilities.parseLong(request.getParameter("MOrder"));

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

long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");           	
}else{
	/*
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	//
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	}*/
}

long SelectedOrderBookerArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers");           	
}

String OutletIds="";
long SelectedOutletArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets") != null){
	SelectedOutletArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets");
	OutletIds = Utilities.serializeForSQL(SelectedOutletArray);
}

String WhereOutlets = "";
if (OutletIds.length() > 0){
	WhereOutlets = " and mo.outlet_id in ("+OutletIds+") ";	
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

String OrderBookerIDs = "";
String WhereOrderBooker = "";
if(SelectedOrderBookerArray != null && SelectedOrderBookerArray.length > 0){
	for(int i = 0; i < SelectedOrderBookerArray.length; i++){
		if(i == 0){
			OrderBookerIDs += SelectedOrderBookerArray[i];
		}else{
			OrderBookerIDs += ", "+SelectedOrderBookerArray[i];
		}
	}
	WhereOrderBooker = " and mo.created_by in ("+OrderBookerIDs+") ";
}

long OutletID = Utilities.parseLong(request.getParameter("OutletID"));



boolean IsMapShow=false;
double latt=0;
double lang=0;
long accu=0;

double olat = 0;
double olng = 0;



boolean IsMapShow2=false;
double latt2=0;
double lang2=0;

double olat2 = 0;
double olng2 = 0;


String OutletName="";
String AttendanceType="";
Date CheckInDate= new Date();
long CheckInAccu=0;
String Address="";
String Distributor="";
String Chanel="";
String Subchanel="";
Date UpdatedOn=null;
String UpdatedBy="";





if(OutletID > 0){
	IsMapShow = true;
	ResultSet rs3 = s.executeQuery("SELECT co.id,co.name,co.address,co.distributor_id,(select name from common_distributors cd where cd.distributor_id=co.distributor_id) distributor_name, co.lat, co.lng ,co.accuracy ,co.updated_on, co.updated_by, (select display_name from users u where u.id=co.updated_by) updated_by_name,co.pic_channel_id, psc.label as sub_channel_label, pc.label as parent_label FROM pep.common_outlets co join pci_sub_channel psc on co.pic_channel_id=psc.id join pci_channel pc on psc.parent_channel_id=pc.id where co.id="+OutletID+" and  updated_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));
	if(rs3.first()){
	
		OutletName = rs3.getString("id") +"-"+ rs3.getString("name") ;
		Distributor=rs3.getString("distributor_id") +"-"+ rs3.getString("distributor_name") ;
		latt = rs3.getDouble("lat");
		lang = rs3.getDouble("lng");
		accu=rs3.getLong("accuracy");
		Chanel = rs3.getString("parent_label");
		Subchanel = rs3.getString("sub_channel_label");
		UpdatedOn= rs3.getTimestamp("updated_on");
		UpdatedBy = rs3.getString("updated_by")+" - "+rs3.getString("updated_by_name");
		
		
		
		
	}
	
	
	
	
	
	/* ResultSet rs31 = s.executeQuery("select created_by, (select display_name from users u where u.id=created_by ) orderbooker_name, lat,lng,accuracy, max(created_on) created_on,filename from mobile_order_booker_attendance where created_by="+CreatedBy+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and attendance_type=2");
	//if(rs31.first()){
		AttendanceType2="Check Out";
		//OrderBookerName = rs31.getLong("created_by")+" - "+rs31.getString("orderbooker_name") + " | "+ Utilities.getDisplayFullDateFormatShort(rs31.getDate("created_on"));
		
		CheckOutDate = rs31.getTimestamp("created_on");
		CheckOutAccu = rs31.getLong("accuracy");
		
		PictureName2 = rs31.getString("filename");
		
		olat = rs31.getDouble("lat");
		olng = rs31.getDouble("lng");
		
	}
	 */
	
}




%>



			<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-13px;" data-icon="false">
			<li data-role="list-divider" data-theme="a"><%=OutletName%></li>
			<li>
			
			
				<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
					<tr>
						
						
						
						<td style="width: 100%" valign="top">
							
							<table border=0 style="margin-top:4%; font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
								<tr>
									<td><b>Distributor :</b> <%=Distributor %></td>
									<td><b> Channel :</b> <%=Chanel %></td>
									<td><b>Sub Channel :</b> <%=Subchanel %></td>
								</tr>
								
								<tr>
									<td><b>Updated On :</b> <%if(UpdatedOn!=null){%><%=Utilities.getDisplayDateTimeFormat(UpdatedOn) %><%} %></td>
									<td><b> Updated By :</b> <%=UpdatedBy %></td>
									<td><b> Accuracy :</b> <%=accu %></td>
									
								</tr>
							
							</table>
								
								
						</td>
						
					</tr>
					
					
				</table>
			
				</li>	
			</ul>
			<%
			
			
				if(IsMapShow && latt !=0 && lang !=0){
				%>
				<ul data-role="listview" data-inset="true"  style="margin-top:3%;font-size:10pt; font-weight: normal; margin-top:-5px;" data-icon="false">
				
					<li data-role="list-divider" data-theme="a">Outlet Location</li>
					<li style="height:500px">
						
						<script>
				    	 initMap();
	
				    	function initMap(){			    		
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
				    		  
				    			 
				    		map.setCenter(new google.maps.LatLng(<%=latt%>,<%=lang%>));
				    				  
				    			  
				    			  markers[0] = new google.maps.Marker({
				    			      position: new google.maps.LatLng(<%=latt%>,<%=lang%>),
				    			      map: map,
				    			      title: '<%=OutletName%>'
				    			      
				    			  });
				    			
				    			  
				    			  google.maps.event.addListener(markers[0], 'click', function() {
				    			    infowindow.open(map,markers[0]);
				    			    var infoWindowContent = "<%=OutletName%><br>"; 
				    			    infoWindowContent += "<a href='#'>View Detail</a>";
				    			    infowindow.setContent(infoWindowContent);
				    			    
				    			  });
				    			  
				    	}	 
				    		  
				    			 
				    		
				    	
				    	
				    	
				    	</script>
						<div id="map" style=""></div>			
					</li>
				</ul>
				<%
				}
			%>
<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>