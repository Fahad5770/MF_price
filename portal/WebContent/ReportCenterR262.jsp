<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>

<%@page import="java.sql.*"%>
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

String lat="";
String lng="";
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 326;

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
Statement s5 = c.createStatement();

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
	IsOrderBookerSelected = true;
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
	WhereOrderBooker = " and created_by in ("+OrderBookerIDs+") ";
}


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

//SM


String SMIDs="";
long SelectedSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedSM") != null){
	SelectedSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedSM");
	SMIDs = Utilities.serializeForSQL(SelectedSMArray);
}

String WhereSM = "";
if (SMIDs.length() > 0){
	WhereSM = " and mo.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
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
	WhereTDM = " and mo.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
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
	WhereASM = " and mo.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
}
%>
<script>

function OrderBookerTracking(OrderBookerID){
	
	var url="";
		
		url = 'ReportCenterR120MapSummary.jsp?OrderBookerID='+OrderBookerID+'&UniqueSessionID=<%=UniqueSessionID%>';
	
	
	$("#FilterType").val("4"); //4 for Order Bookers
	$("#OrderBookerIDCheckbox").val(OrderBookerID);
	
	
	
	$("#OrderActivityReportTD").html("<img src='images/snake-loader.gif'>");
	
	
	$("#OrderBookerHyperlinkID_"+$("#TempStoringIDOfLastClick").val()).css('font-weight','normal'); //reseting the old click font
	
	$("#OrderBookerHyperlinkID_"+OrderBookerID).css('font-weight','bold');
	
	$("#TempStoringIDOfLastClick").val(OrderBookerID); //storing first clicked id - will use it to reset the font to normal when other link will be clicked
	//alert($("#TempStoringIDOfLastClick").val());
	
	$.mobile.showPageLoadingMsg();
	$.get(url, function(data) {
		  $.mobile.hidePageLoadingMsg();
		  $("#OrderActivityReportTD").html(data);
		  $("#OrderActivityReportTD").trigger('create');
		 
	});
	
	
}
//OrderBookerTracking(0);

</script>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Maps</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<input type="hidden" id="TempStoringIDOfLastClick" />
	<tr>
		
		<td style="width:35%" valign="top" >
		
		<%
		String DistributorIds="";
        long SelectedDistributorArray[] = null;
        if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
     	   SelectedDistributorArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors"); 
     	   DistributorIds = Utilities.serializeForSQL(SelectedDistributorArray);
     	   //System.out.println("I am in if");
        }
        else
        {
     	  
            if (session.getAttribute("SR1FeatureID") != null)
            {
            	FeatureID = (Integer)session.getAttribute("SR1FeatureID");
            }
     	   Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
     	   DistributorIds = UserAccess.getDistributorQueryString(UserDistributor);
     	   //System.out.println("I am in else");
        }
		
		
		//ResultSet rs = s.executeQuery("select * from employee_view where sap_code in (select distinct dbpv.assigned_to from distributor_beat_plan_view dbpv  where dbpv.distributor_id in ("+DistributorIds+") "+WhereHOD+WhereRSM+")");
		//System.out.println("select mo.created_by, u.DISPLAY_NAME, count(*) no_of_orders, sum(mo.net_amount) total_amount from mobile_order mo join users u on u.id = mo.created_by where mo.mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateTimeNext(EndDate)+" and mo.distributor_id in ("+DistributorIds+")"+WhereHOD+WhereRSM+" group by mo.created_by");
		%>
		
		
			
			
			
			
			
			
			
			
			
			
			<script>

initializemap();


function initializemap() {
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

    	 
    map.setCenter(new google.maps.LatLng("65.34234","23.32423542"));
    		  
    <%
	  ResultSet rs2 = s.executeQuery("select * from mrd_census where  created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateTimeNext(EndDate)+"");
int i=0;	  

	  while(rs2.next()){
		  
		  String FinalOutletName="";
		  if (rs2.getString("outlet_name")!="" || rs2.getString("outlet_name")!=null || rs2.getString("outlet_name")!="No Outlet Found")
		  {
			  FinalOutletName=rs2.getString("outlet_name");
		  }
		  else if (rs2.getString("outlet_name_actual")!="" || rs2.getString("outlet_name_actual")!=null)
		  {
			  FinalOutletName=rs2.getString("outlet_name_actual");
		  }
		  else 
		  {
			  FinalOutletName=rs2.getString("outlet_board_name");
		  }
		  
		  String IconType = "";
		  if(rs2.getInt("census_exclusivity_agreement_1") == 1){
			  IconType = "p";
		  }else if(rs2.getInt("census_exclusivity_agreement_1") == 2){
			  IconType = "c";
		  } else if(rs2.getInt("census_exclusivity_agreement_1") == 3 || rs2.getInt("census_exclusivity_agreement_1") == 4  || rs2.getInt("census_exclusivity_agreement_1") == 5){
			  IconType = "o1";
		  }
		  else if(rs2.getInt("census_exclusivity_agreement_1") == 6){
			  IconType = "n";
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
	      title: '<%=FinalOutletName%>',
	      icon: 'images/markers/letter_<%=IconType%>.png'
	  });
	  google.maps.event.addListener(markers[<%=i%>], 'click', function() {
	    infowindow.open(map,markers[<%=i%>]);
	    
	    var infoWindowContent = "<%=rs2.getString("outlet_id")%>-<%=FinalOutletName%><br>"; 
	    infoWindowContent += "<a href='#'>View Detail</a>";
	    infowindow.setContent(infoWindowContent);
	  });

    <% i++;
	  }
    %>
}

</script>
			
			
		
		</table>
		<br>
		</td>
		<td style="width: 65%" valign="top" id="OrderActivityReportTD">
				<div id="map" style="margin-left:10px;"></div>
					 <!--  load map here -->
					
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