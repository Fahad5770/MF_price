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
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.pbc.employee.OrderBookerDashboard"%>



 


<style>
td{
font-size: 8pt;
}
 #map {
        width: 100%;
        height: 800px;
        margin-top: 10px;
      }
      
      
 .SelectedBold{
 	font-weight:bold;
 }     
</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

long OrderBookerID = Utilities.parseLong(request.getParameter("OrderBookerID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 263;

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

if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	StartDate = new Date();
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}

//System.out.println("StartDate = "+StartDate);
//System.out.println("EndDate = "+EndDate);


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
	WhereHOD = " and mo.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and mo.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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

//
//OrderBooker
boolean IsOrderBookerSelected=false;
long SelectedOrderBookerArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers");  
	//IsOrderBookerSelected = true;
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
	//WhereOrderBooker = " and created_by in ("+OrderBookerID+") "; //changed the variable bec now it is not coming from session - it is coming as a parameter
}
WhereOrderBooker = " and created_by in ("+OrderBookerID+") "; //changed the variable bec now it is not coming from session - it is coming as a parameter
if(OrderBookerID !=0){
	IsOrderBookerSelected = true;
}
//System.out.println("hell yeah "+OrderBookerID);
//Warehouse


String WarehouseIDs="";
long SelectedWarehouseArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse") != null){
	SelectedWarehouseArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse");
	WarehouseIDs = Utilities.serializeForSQL(SelectedWarehouseArray);
}
//System.out.println(WarehouseIDs);
String WhereWarehouse = "";
if (WarehouseIDs.length() > 0){
	WhereWarehouse = " and idn.warehouse_id in ("+WarehouseIDs+") ";	
}

OrderBookerDashboard OrderBooker = new OrderBookerDashboard();
%>

<script>
<%if(IsOrderBookerSelected){%>
initializeOutlet();


//for Outlet New function
function initializeOutlet() {
	//alert()
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
	  
	  <%
	  if(OrderBookerID!=0){
		  
	 //System.out.println("select id, name, lat, lng, ifnull((select active from sampling where outlet_id = co.id and active = 1),0) is_discounted from common_outlets co where id in(select distinct dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to ="+OrderBookerID+") and lat > 0 ");
	  ResultSet rs2 = s.executeQuery("select id, name, lat, lng, ifnull((select active from sampling where outlet_id = co.id and active = 1),0) is_discounted from common_outlets co where id in(select distinct dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to ="+OrderBookerID+") and lat > 0 ");
	  int i = 0;
	  while(rs2.next()){
		  
		  String IconType = "";
		  if(rs2.getInt("is_discounted") == 1){
			  IconType = "d";
		  }else{
			  IconType = "o";
		  }
		  
		  if(i == 0){
			  %>
			  	map.setCenter(new google.maps.LatLng(<%=rs2.getString("lat")%>,<%=rs2.getString("lng")%>));
			  <%
		  }
		  
		  %>
		  
		  markers[<%=i%>] = new google.maps.Marker({
		      position: new google.maps.LatLng(<%=rs2.getString("lat")%>,<%=rs2.getString("lng")%>),
		      map: map,
		      title: '<%=rs2.getString("name")%>',
		      icon: 'images/markers/letter_<%=IconType%>.png'
		  });
		  google.maps.event.addListener(markers[<%=i%>], 'click', function() {
		    infowindow.open(map,markers[<%=i%>]);
		    
		    var infoWindowContent = "<%=rs2.getString("id")%>-<%=rs2.getString("name")%><br>"; 
		    infoWindowContent += "<a href='#'>View Detail</a>";
		    infowindow.setContent(infoWindowContent);
		    //infowi
		  });
		  
		  <%
		  i++;
	  }
	  %>
	  loadOrderBookerMap(map); //loading orderbooker map
	  <%
	  }
	  %>


	}

<%
//System.out.println("select *,(select outlet_name from outletmaster om where mo.outlet_id=om.outlet_id) outlet_name from mobile_order mo where mo.lat !=0 and mo.lng !=0 and mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateTimeNext(EndDate)+WhereOrderBooker+" order by mo.created_on desc ");
ResultSet rs = s.executeQuery("select *,(select outlet_name from outletmaster om where mo.outlet_id=om.outlet_id) outlet_name from mobile_order mo where mo.lat !=0 and mo.lng !=0 and mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateTimeNext(EndDate)+WhereOrderBooker+" order by mo.created_on desc ");


double FirstLat=0;
double FirstLong=0;
Date LatestOrderDate = new Date();
Date LatestOrderTime = new Date();
Date LatestOrderDateTime = new Date();
String LatestOrderAmount="";
String OutletIDName ="";
String _12HRTimeDisplay ="";
int i=0;
if(rs.first()){ //getting latest lat,lng
	FirstLat = rs.getDouble("lat");
	FirstLong = rs.getDouble("lng");
}
rs.beforeFirst(); //resetting the cursor

//DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//dd/MM/yyyy
Date current_date = new Date();
//String strDate = sdfDate.format(current_date);
//String OrderDBDate="";



%>
function loadOrderBookerMap(map){
	
	
    // Save the positions' history
    var path = [];

    
   

    
   // for(var i = 0; i < 5; i++) {
        <% while(rs.next()){ 
        	if(i==0){ //mean latest record - In Query Order by Desc so Latest record will be first one
        		LatestOrderDate = rs.getDate("mobile_timestamp");	
        		LatestOrderTime = rs.getTime("mobile_timestamp");
        		LatestOrderDateTime = rs.getTimestamp("mobile_timestamp");
        		LatestOrderAmount = Utilities.getDisplayCurrencyFormat(rs.getDouble("net_amount"));
        		OutletIDName = rs.getString("outlet_id")+" - "+rs.getString("outlet_name");
        		
        		
        		//converting to am pm
        		//String _24HourTime = "22:15";
                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                Date _24HourDt = _24HourSDF.parse(LatestOrderTime.toString());  
                _12HRTimeDisplay = _12HourSDF.format(_24HourDt);
                //System.out.println(_12HourSDF.format(_24HourDt));
        	}
        	 //OrderDBDate = sdfDate.format(LatestOrderDateTime);
        	
        	
        	////System.out.println(OrderDBDate);
        	//System.out.println(strDate);
        %>
    	// Create a random point using the user current position and a random generated number.
        // The number will be once positive and once negative using based on the parity of i
        // and to reduce the range the number is divided by 10
       
        
        
        
        path.push(
          new google.maps.LatLng(
        		  <%=rs.getDouble("lat")%>,
        		  <%=rs.getDouble("lng")%>
          )
        );
   <% i++; } 
        
        //calculating days,hrs,mins
        long diff = current_date.getTime() - LatestOrderDateTime.getTime();
        
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        int diffInDays = (int) (diff / (1000 * 60 * 60 * 24));
      
        //System.out.println("Seconds :"+diffSeconds);
        //System.out.println("Mins :"+diffMinutes);
        //System.out.println("Hours :"+diffHours);
        //System.out.println("Days :"+diffInDays);
        
        String TimeDisplay="";
        
        if(diffHours ==0 && diffInDays==0){
        	TimeDisplay = diffMinutes+" Mins since last visit";
        }else if(diffHours ==0 && diffInDays!=0){
        	TimeDisplay = diffInDays+" Days since last visit";
        }else if(diffHours !=0 && diffInDays==0){
        	TimeDisplay = diffHours+" Hours since last visit";
        }else if(diffHours !=0 && diffInDays!=0){
        	TimeDisplay = diffInDays+" Days since last visit";
        }
        
      
      //System.out.println(LatestOrderDate+" - "+((current_date.getTime() - LatestOrderDate.getTime())/ (1000 * 60 * 60 * 24)*24));
  %>
     // }
   var infowindow = new google.maps.InfoWindow({
	      content: "<%=_12HRTimeDisplay+"<br/>"+TimeDisplay+"<br>"+OutletIDName+"<br>Rs "+LatestOrderAmount%>"
	  });
   var marker;
    var latLngBounds = new google.maps.LatLngBounds();
    //alert(path[0]);
    for(var i = 0; i < path.length; i++) {
      latLngBounds.extend(path[i]);
      // Place the marker
      if(i==0){
    	  marker = new google.maps.Marker({
    	        map: map,
    	        position: path[i],
    	        animation: google.maps.Animation.BOUNCE,
    	        title: "Point " + (i + 1),
    	        icon: 'images/markers/truck.png'
    	      }); 
    	  infowindow.open(map,marker);
      }else{
    	  new google.maps.Marker({
  	        map: map,
  	        position: path[i],  	        
  	        title: "Point " + (i + 1),
  	      icon: 'images/markers/truck.png'
  	      }); 
      }
      
    }
    // Creates the polyline object
    var polyline = new google.maps.Polyline({
      map: map,
      path: path,
      strokeColor: '#0000FF',
      strokeOpacity: 0.7,
      strokeWeight: 1
    });
    // Fit the bounds of the generated points
    map.fitBounds(latLngBounds);
	
}
<% } %>
</script>

			
					 <%
					 if(IsOrderBookerSelected){
					 %>
					
					
					
					<div id="map" style="margin-left:10px;"></div>
    			     
    			     <p id="error" ></p>
    			     <%}else{ %>
    			     <p style="padding-left:10px;"><br/>Please select at least one Order booker.</p>
					 <%} %>
		
<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>