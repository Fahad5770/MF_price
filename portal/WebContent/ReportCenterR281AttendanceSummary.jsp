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
        height: 300px;
        margin-top: 10px;
      }
</style>

<%


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 351;
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

long CreatedBy = Utilities.parseLong(request.getParameter("CreatedBy"));

Date CurrentDate =Utilities.parseDate(request.getParameter("d"));


boolean IsMapShow=false;
double latt=0;
double lang=0;

double olat = 0;
double olng = 0;



boolean IsMapShow2=false;
double latt2=0;
double lang2=0;

double olat2 = 0;
double olng2 = 0;

String OrderBookerName="";
String AttendanceType="";
Date CheckInDate= new Date();
long CheckInAccu=0;
String DeviceID="";
String DeviceIDIssued="";


String AttendanceType2="";
Date CheckOutDate= new Date();
long CheckOutAccu=0;
String PictureName="";
String PictureName2="";
String uri1="";
String uri2="";
String DeviceID2="";
String DeviceIDIssued2="";
int year1=0, year2=0;


if(CreatedBy > 0){
	IsMapShow = true;
	ResultSet rs3 = s.executeQuery("select uri, year,created_by, (select display_name from users u where u.id=created_by ) orderbooker_name, lat,lng,accuracy, min(created_on) created_on, filename,device_id from mobile_order_booker_attendance where created_by="+CreatedBy+" and created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and attendance_type=1");
	if(rs3.first()){
		AttendanceType="Check In";
		OrderBookerName = rs3.getLong("created_by")+" - "+rs3.getString("orderbooker_name") + " | "+ Utilities.getDisplayFullDateFormatShort(rs3.getDate("created_on"));
		latt = rs3.getDouble("lat");
		lang = rs3.getDouble("lng");
		CheckInDate = rs3.getTimestamp("created_on");
		CheckInAccu = rs3.getLong("accuracy");
		PictureName = rs3.getString("filename");
		uri1 = rs3.getString("uri");
		year1 = rs3.getInt("year");
		
		 DeviceID = rs3.getString("device_id");
		
		ResultSet rs4 = s2.executeQuery("SELECT md.issued_to, (select display_name from users u where u.id=md.issued_to) issue_name FROM pep.mobile_devices md where md.uuid='"+DeviceID+"'");
		if(rs4.first()){
			DeviceIDIssued=rs4.getLong("issued_to")+" - "+rs4.getString("issue_name");
			
		} 
		//olat = rs3.getDouble("olat");
		//olng = rs3.getDouble("olng");
		
	}
	
	
	
	
	//long AttendanceID=0;
	ResultSet rs31 = s.executeQuery("select uri, year,created_by, (select display_name from users u where u.id=created_by ) orderbooker_name, lat,lng,accuracy, max(created_on) created_on,filename, device_id from mobile_order_booker_attendance where created_by="+CreatedBy+" and created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and attendance_type=2");
	if(rs31.first()){
		AttendanceType2="Check Out";
		//OrderBookerName = rs31.getLong("created_by")+" - "+rs31.getString("orderbooker_name") + " | "+ Utilities.getDisplayFullDateFormatShort(rs31.getDate("created_on"));
		
		CheckOutDate = rs31.getTimestamp("created_on");
		CheckOutAccu = rs31.getLong("accuracy");
		
		PictureName2 = rs31.getString("filename");
		uri2 = rs31.getString("uri");
		year2 = rs31.getInt("year");
	
		olat = rs31.getDouble("lat");
		olng = rs31.getDouble("lng");
		
		
		DeviceID2 = rs31.getString("device_id");
		
		 ResultSet rs4 = s2.executeQuery("SELECT issued_to, (select display_name from users u where u.id=md.issued_to) issue_name FROM pep.mobile_devices md where md.uuid='"+DeviceID2+"'");
		if(rs4.first()){
			DeviceIDIssued2=rs4.getLong("issued_to")+" - "+rs4.getString("issue_name");
			
		} 
		
	}
	
	
	
	
	
	
	
}




%>



			<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-13px;" data-icon="false">
			<li data-role="list-divider" data-theme="a"><%=OrderBookerName%></li>
			<li>
			
			
				<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
					<tr>
						
						
						
						<td style="width: 100%" valign="top">
							
							<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
								<tr>
									<td><b>Attendance Type:</b> <%=AttendanceType %></td>
									<td><b>Attendance Time:</b> <%if(CheckInDate!=null){%><%=Utilities.getDisplayDateTimeFormat(CheckInDate) %><%} %></td>
									<td><b>Accuracy:</b> <%=CheckInAccu %></td>
								</tr>
								<tr>
									<td colsapn="3">&nbsp;</td>
								</tr>
								<tr>
									<td><b>Attendance Type:</b> <%=AttendanceType2 %></td>
									<td><b>Attendance Time:</b> <%if(CheckOutDate!=null){%><%=Utilities.getDisplayDateTimeFormat(CheckOutDate) %><%} %></td>
									<td><b>Accuracy:</b> <%=CheckOutAccu %></td>
								</tr>
								
								<tr>
									<td><b>Check-In Device ID:</b> <%if(DeviceID!=null){%><%=DeviceID %><%} %></td>
									<td><b>Issued-To:</b> <%=DeviceIDIssued %></td>
									
								</tr>
								<tr>
									<td><b>Check-Out Device ID:</b> <%if(DeviceID2!=null){%><%=DeviceID2 %><%} %></td>
									<td><b>Issued-To:</b> <%=DeviceIDIssued2 %></td>
									
								</tr>
								
								<tr>
									<td colspan="3"></td>
									
								</tr>
								<tr>
									<td><b>Check In</b></td>
									<td>&nbsp;</td>
									<td><b>Check Out</b></td>
								</tr>
								<tr>
								
									<td >
									 <img src="<%=( (year1!=0) ? "common/CommonFileDownloadFromPath" : "mobile/MobileFileDownloadMDE" ) %>?file=<%=PictureName%>&filePath=<%=uri1%>" style="width:250px; height:250px;" />
<%-- 									<img  src="mobile/MobileFileDownloadMDE?file=<%=PictureName %>" style="width:250px; height:250px;" /></td>
 --%>									<td>&nbsp;</td>
									<td >
									<%-- <img  src="mobile/MobileFileDownloadMDE?file=<%=PictureName2 %>" style="width:250px; height:250px;" /> --%>
									 <img src="<%=( (year2!=0) ? "common/CommonFileDownloadFromPath" : "mobile/MobileFileDownloadMDE" ) %>?file=<%=PictureName2%>&filePath=<%=uri2%>" style="width:250px; height:250px;" />
									</td>
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
				<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-5px;" data-icon="false">
				
					<li data-role="list-divider" data-theme="a">Order Location</li>
					<li>
						
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
				    			      title: '<%=OrderBookerName%>'
				    			      
				    			  });
				    			  markers[1] = new google.maps.Marker({
				    			      position: new google.maps.LatLng(<%=olat%>,<%=olng%>),
				    			      map: map,
				    			      title: '<%=OrderBookerName%>',
				    			      icon: 'images/markers/letter_o.png'
				    			  });
				    			  
				    			  google.maps.event.addListener(markers[0], 'click', function() {
				    			    infowindow.open(map,markers[0]);
				    			    var infoWindowContent = "<%=OrderBookerName%><br>"; 
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