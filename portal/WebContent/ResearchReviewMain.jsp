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

<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 224;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));
if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}
//
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

long SelectedDistributorsArray[] = null;
boolean IsDistributorSelected=false;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	IsOrderBookerSelected=true;
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors"); 
	IsDistributorSelected = true;
}else{
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

%>


<script type="text/javascript">

	var isResolved = 0;
	var isSent = 0;
	

	var brands_pepsi_ids = [];
	var brands_coke_ids = [];

	function redirect(url){
		document.getElementById("check").action = url;
		document.getElementById("check").submit();
	}
	
	
	
	function OpenMarketWatchForm(WatchID){
		
		//alert("OrderBookerGetOrderActivityReport Function");
		var url = 'ResearchReviewForm.jsp?WatchID='+WatchID+'&UniqueSessionID=<%=UniqueSessionID%>';
		
		$("#TaskFormTD").html("<img src='images/snake-loader.gif'>");
		
		$.mobile.showPageLoadingMsg();
		$.get(url, function(data) {
			  $.mobile.hidePageLoadingMsg();
			  $("#TaskFormTD").html(data);
			  $("#TaskFormTD").trigger('create');
			 
		}).done(function(data){
			populateBrandsPepsi();
			populateBrandsCoke();
			//alert("data loaded = "+brands_pepsi.length);
			//$('input[name="CRMTaskTypes"]').prop( "checked", true ).checkboxradio( "refresh" );
			//setComplaintTypes();
		});
		
	}
	
	function PoplulateTasks(){
		
		$("#TasksDIV").html("<img src='images/snake-loader.gif'>");
		
		$.mobile.showPageLoadingMsg();
		//alert(isResolved+", "+isSent);
		$.get("ResearchReviewList.jsp?&UniqueSessionID=<%=UniqueSessionID%>", function(data) {
			  $.mobile.hidePageLoadingMsg();
			  $("#TasksDIV").html(data); 
			  $("#TasksDIV").trigger('create');
		});
		
		//window.location = "ReportCenter.jsp?ReportID=48&IsAssigned="+IsAssigned;
	}
	
	function setResolveStatus(val){
		isResolved = val;
		PoplulateTasks();
	}
	
	function setSentStatus(val){
		isSent = val;
		PoplulateTasks();
	}
	
	PoplulateTasks();
	
</script>



<table border="0" style="width: 100%">
	<tr>
		
		
		<td style="width: 30%" valign="top">
		
			
			
			
		
			<div id="TasksDIV">&nbsp;</div>
		
		
		</td>
		<td style="width: 70%" valign="top" id="TaskFormTD">&nbsp;</td>
			
				
		
	</tr>
</table>




<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>