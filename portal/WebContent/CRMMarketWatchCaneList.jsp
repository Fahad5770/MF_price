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
int FeatureID = 218;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionKSML();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();


Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");

if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	
	StartDate = new Date();
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}





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
	WhereDistributors = " and cmw.distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}

%>

<script type="text/javascript">



//alert(TaskType);
function checkAll(){
	$('input[name="checkbox-mini"]').prop( "checked", true ).checkboxradio( "refresh" );
}

function unCheckAll(){
	$('input[name="checkbox-mini"]').prop( "checked", false ).checkboxradio( "refresh" );
}

function SerializeOrderIDs(){
	var UrlParams = "&TaskID=0";
	var len = $('input[name="checkbox-mini"]').length;
	for(var i = 0; i < len; i++){
		if($('input[name="checkbox-mini"]')[i].checked == true){
			UrlParams += "&TaskID="+$('input[name="checkbox-mini"]')[i].value;
		}
	}
	return UrlParams;
}

function OrderInvoicingDoInvoice(ParamTaskIDs){
	if( ParamTaskIDs!= ""){
		
		var url_str = "crm/TaskForwardExecute";
		if(TaskType == '1'){
			url_str = "crm/TaskForwardResolveExecute";
		}
		
		
		//alert(url_str);
		
		 
		$.mobile.showPageLoadingMsg();
		$.ajax({
		    url: url_str,
		    data: "1=1"+ParamTaskIDs,
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.success == "true"){
		    		$.mobile.hidePageLoadingMsg();
		    	}else{
		    		alert("Server could not be reached.");
		    	}
		    	//
		    	//window.location = 'ReportCenter.jsp?ReportID=54';
		    	//alert("success");
		    	//window.location='OrderInvoicing.jsp';
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
		
	}
}



function ChangeColor(MarketWatchOutletID){
 //$('input[name="styledText"]').css('font-weight', 'normal');
 
 //alert($("#datathememw"));
	
	
	//font-weight:bold;background-color:#2373a5; color:white;
	//$("#"+MarketWatchOutletID).attr("data-theme","a");
	//$("#"+MarketWatchOutletID).listview('refresh');
	
		//$("#"+MarketWatchOutletID).css("background-color","#2373a5");
		//$("#"+MarketWatchOutletID).css("color","#fff");
		//alert();
	//$("#"+MarketWatchOutletID).css("font-size","12");
	
}
</script>

<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="c" data-count-theme="c" style="margin-top: -13px;" id="datathememw">
				<li data-role="list-divider" data-theme="a">List</li>
				
				
				
				<%
				Date LastDate = Utilities.parseDate("01/01/1997");
				
				
				//System.out.println("SELECT id,mill_id,(select label from crman_mills where id=mill_id) mill_name,center_type,center_id,(select name from crman_centers where id=center_id) center_name,(select circle from crman_centers where id=center_id) center_circle FROM crman_valid_messages where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
				
				ResultSet rs22 = s.executeQuery("SELECT id,mill_id,(select label from crman_mills where id=mill_id) mill_name,center_type,center_id,(select name from crman_centers where id=center_id) center_name,(select circle from crman_centers where id=center_id) center_circle FROM crman_valid_messages where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));
				
				//System.out.println("SELECT outlet_id,outlet_name FROM crm_market_watch where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));
				int counter = 0;
				while(rs22.next()){
					
					
					
					%>
					
					<!-- /////////////////////////////////////////////////////////////////////// -->
					
					
					<li style="height: 40px; " >
						
						<a data-ajax="false" id="mw_outlet_id_<%=rs22.getString("id")%>" href="#"  style=" " onclick="OpenMarketWatchCaneForm('<%=rs22.getString("id")%>');" style="font-size:10px" >
							<span style="font-size: 12px; font-weight: bold">
							<%=rs22.getString("mill_name")%> - <%=rs22.getString("center_type") %><br/>
							<%=rs22.getString("center_name")%> - <%=rs22.getString("center_circle")%>
							</span>
						</a>
					</li>
					
					
					
					
					<%


				}
				%>
					
					
			</ul>


<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>