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
int FeatureID = 135;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
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

int IsAssigned = 0;

int IsAssignedParam = Utilities.parseInt(request.getParameter("IsAssigned"));
if( IsAssignedParam > 0 ){
	IsAssigned = IsAssignedParam;
}


%>


<script type="text/javascript">

 






	function redirect(url){
		document.getElementById("check").action = url;
		document.getElementById("check").submit();
	}
	
	
	
	function OpenComplaintForm(ComplaintID, IsAssigned){
		
		//alert("OrderBookerGetOrderActivityReport Function");
		var url = 'CRMComplaintForm.jsp?ComplaintID='+ComplaintID+'&IsAssigned='+IsAssigned+'&UniqueSessionID=<%=UniqueSessionID%>';
		
		$("#ComplaintFormTD").html("<img src='images/snake-loader.gif'>");
		
		$.mobile.showPageLoadingMsg();
		$.get(url, function(data) {
			  $.mobile.hidePageLoadingMsg();
			  $("#ComplaintFormTD").html(data);
			  $("#ComplaintFormTD").trigger('create');
			 
		});
		
	}
	
	function PoplulateComplaints(IsAssigned){
		/*
		var IsChecked = $( "#ReportCenterR134IsAssigned" ).is(":checked");
		
		if( IsChecked ){
			window.location = "ReportCenter.jsp?ReportID=47&IsAssigned=1";
		}else{
			window.location = "ReportCenter.jsp?ReportID=47&IsAssigned=0"; 
		}*/
		
		
		$("#ComplaintsDIV").html("<img src='images/snake-loader.gif'>");
		
		$.mobile.showPageLoadingMsg();
		$.get("CRMComplaintList.jsp?IsAssigned="+IsAssigned+'&UniqueSessionID=<%=UniqueSessionID%>', function(data) {
			  $.mobile.hidePageLoadingMsg();
			  $("#ComplaintsDIV").html(data);
			  $("#ComplaintsDIV").trigger('create');
			 
		});
		
		//window.location = "ReportCenter.jsp?ReportID=48&IsAssigned="+IsAssigned; 
		
	}
	
	
	
	PoplulateComplaints(0);
	
</script>



<table border="0" style="width: 100%">
	<tr>
		
		
		<td style="width: 30%" valign="top">
		<!-- 
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="c" data-count-theme="c" style="margin-top:-13px;">
				<li data-role="list-divider" data-theme="a">By Status</li>
				
				<li> -->
				<!-- 
					<form>
					    <fieldset data-role="controlgroup">
					        
					        <input type="checkbox" name="ReportCenterR134IsAssigned" id="ReportCenterR134IsAssigned" <%// if( IsAssigned == 1 ){ %> checked="checked" <% //}else{ out.print(""); } %> data-mini="true" onclick="PoplulateComplaints()"  > 
					        <label for="ReportCenterR134IsAssigned">Assigned</label>
					        
					    </fieldset>
					</form>
					
					 -->
					<fieldset data-role="controlgroup"  style="margin-top:-13px;">
						
					     	<input type="radio" name="radio-choice" id="radio-choice-1" value="choice-1" checked="checked" data-mini="true" onclick="PoplulateComplaints(0)"  />
					     	<label for="radio-choice-1">Unassigned</label>
					
					     	<input type="radio" name="radio-choice" id="radio-choice-2" value="choice-2" data-mini="true" onclick="PoplulateComplaints(1)" <% if( IsAssigned == 1 ){ %> checked="checked" <% }else{ out.print(""); } %>  />
					     	<label for="radio-choice-2">Assigned</label>
					
					</fieldset>
					
				<!-- 	
				</li>
				
			</ul>
			 -->
			<div id="ComplaintsDIV">
			
		</div>
		
		
		</td>
		<td style="width: 70%" valign="top" id="ComplaintFormTD">&nbsp;</td>
			
				
		
	</tr>
</table>




<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>