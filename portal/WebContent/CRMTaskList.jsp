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
int FeatureID = 144;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
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

int IsResolved = Utilities.parseInt(request.getParameter("IsResolved"));
int IsSent = Utilities.parseInt(request.getParameter("IsSent"));

%>

<script type="text/javascript">

var TaskType = '<%=IsResolved%>';

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

</script>

<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="c" data-count-theme="c" astyle="margin-top: -13px;">
				<li data-role="list-divider" data-theme="a">Tasks</li>
				
				
				
				<%
				Date LastDate = Utilities.parseDate("01/01/1997");
				//System.out.println( "SELECT * FROM crm_complaints where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" " );
				String Where = "";
				
				//Where = " and cca.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" order by cca.created_on desc ";
				
				String ExcludeToday = "";
				/*
				if (SessionUserID == 2612 | SessionUserID == 2381){
					
				}else{
					//Date TodayDate = new Date();
					//ExcludeToday = " and created_on < "+ Utilities.getSQLDate(new Date()) +" ";
				}
				*/
				String SQL = "";
				String OrderByDateColumn = "";
				
				
				if( IsResolved == 0  ){
					OrderByDateColumn = "created_on";
					if( IsSent == 0 ){
						SQL = "SELECT cca.id, ifnull(cca.outlet_id,'') outlet_id, cca.outlet_name, cca.created_on, cca.sent_on, cca.resolved_on, ifnull((select '1' from crm_tasks_list ctl where ctl.id = cca.id and ((ctl.type_id >= 1 and ctl.type_id <= 14) or (ctl.type_id >= 21 and ctl.type_id <= 24)) limit 1),0) is_valid FROM crm_tasks cca where cca.is_sent=0 and cca.is_resolved=0 and cca.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+ExcludeToday+" having is_valid = 1 order by cca.created_on desc ";
					}else{
						SQL = "SELECT cca.id, ifnull(cca.outlet_id,'') outlet_id, cca.outlet_name, cca.created_on, cca.sent_on, cca.resolved_on, ifnull((select '1' from crm_tasks_list ctl where ctl.id = cca.id and ((ctl.type_id >= 1 and ctl.type_id <= 14) or (ctl.type_id >= 21 and ctl.type_id <= 24)) limit 1),0) is_valid FROM crm_tasks cca where cca.is_sent=1 and cca.is_resolved=0 and cca.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+ExcludeToday+" having is_valid = 1 order by cca.created_on desc ";
					}
				}
				
				if( IsResolved == 1  ){
					OrderByDateColumn = "resolved_on";
					if( IsSent == 0 ){
						SQL = "SELECT cca.id, ifnull(cca.outlet_id,'') outlet_id, cca.outlet_name, cca.created_on, cca.sent_on, cca.resolved_on, ifnull((select '1' from crm_tasks_list ctl where ctl.id = cca.id and ((ctl.type_id >= 1 and ctl.type_id <= 14) or (ctl.type_id >= 21 and ctl.type_id <= 24)) limit 1),0) is_valid FROM crm_tasks cca where cca.is_sent=0 and cca.is_resolved=1 and cca.resolved_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" having is_valid = 1 order by cca.resolved_on desc ";
					}else{
						SQL = "SELECT cca.id, ifnull(cca.outlet_id,'') outlet_id, cca.outlet_name, cca.created_on, cca.sent_on, cca.resolved_on, ifnull((select '1' from crm_tasks_list ctl where ctl.id = cca.id and ((ctl.type_id >= 1 and ctl.type_id <= 14) or (ctl.type_id >= 21 and ctl.type_id <= 24)) limit 1),0) is_valid FROM crm_tasks cca where cca.is_sent=1 and cca.is_resolved=1 and cca.resolved_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" having is_valid = 1 order by cca.resolved_on desc ";
					}
				}
				/*
				if( IsSent == 1 ){
					SQL = "SELECT cca.id, ifnull(cca.outlet_id,'') outlet_id, cca.outlet_name, cca.created_on, cca.sent_on, ifnull((select '1' from crm_tasks_list ctl where ctl.id = cca.id and ((ctl.type_id >= 1 and ctl.type_id <= 14) or (ctl.type_id >= 21 and ctl.type_id <= 24)) limit 1),0) is_valid FROM crm_tasks cca where cca.is_sent=1 and cca.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+ExcludeToday+" having is_valid = 1 order by cca.sent_on desc ";
				}
				
				if( IsTaskSent == 2 ){
					SQL = "SELECT cca.id, ifnull(cca.outlet_id,'') outlet_id, cca.outlet_name, cca.created_on, cca.resolved_on, ifnull((select '1' from crm_tasks_list ctl where ctl.id = cca.id and ((ctl.type_id >= 1 and ctl.type_id <= 14) or (ctl.type_id >= 21 and ctl.type_id <= 24)) limit 1),0) is_valid FROM crm_tasks cca where cca.is_resolved=1 and cca.resolved_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" having is_valid = 1 order by cca.resolved_on desc ";
				}
				*/
				//System.out.println(SQL);
				
				ResultSet rs22 = s.executeQuery(SQL);
				int counter = 0;
				while(rs22.next()){
					
					Date OrderByDate = rs22.getDate(OrderByDateColumn);
					/*
					if( IsTaskSent == 1 ){
						OrderByDate = rs22.getDate("created_on");
					}
					
					if( IsTaskSent == 2 ){
						OrderByDate = rs22.getDate("resolved_on");
					}
					*/
					if(!DateUtils.isSameDay(LastDate, OrderByDate) ){ 
	            		%>
	            		<li data-role="list-divider" style="font-size: 10px;"><%=Utilities.getDisplayFullDateFormat(OrderByDate)%></li>
	            		<%
	            	}
					
					%>
					
					<!-- /////////////////////////////////////////////////////////////////////// -->
					
					
					<li style="height: 40px">
						<div style="margin-left: 10px; width: 50px; clear: left; float: left; height: 30px;">
							<form>
								<input type="checkbox" name="checkbox-mini" id="checkbox-mini-<%=counter%>" value="<%=rs22.getString("id")%>" class="custom" data-mini="true" >
								<label for="checkbox-mini-<%=counter%>">&nbsp;</label>
							</form>
						</div>
						<a data-ajax="false" href="#" onclick="OpenComplaintForm('<%=rs22.getString("id")%>', 0);" style="font-size:10px" ><span style="font-size: 12px; font-weight: bold"><%//=rs22.getString("id")%><%=rs22.getString("outlet_id")%> - <%=rs22.getString("outlet_name")%></span></a>
					</li>
					
					
					<!-- /////////////////////////////////////////////////////////////////////// -->
					 
					<!-- 
					<li style="height: 40px" >
						<a data-ajax="false" href="#" onclick="OpenComplaintForm('<%//=rs22.getString("id")%>', 0);" style="font-size:10px" ><span style="font-size: 12px; font-weight: bold"><%//=rs22.getString("id")%><%//=rs22.getString("outlet_id")%> - <%//=rs22.getString("outlet_name")%></span></a>
					</li>
					 -->
					
					<%
					LastDate = OrderByDate;
					counter++;
				}
				if(counter==0){
					%>
					<li style="height: 12px">&nbsp;</li>
					<%
				}
					%>
					
					<li data-role="list-divider" data-theme="c">Action</li> 
					<li>
						<table>
							<tr>
								<td><input type="button" value="Check All" data-mini="true" data-theme="d" onclick="checkAll();"  ></td>
								<td><input type="button" value="Uncheck All" data-mini="true" data-theme="d" onclick="unCheckAll();"  ></td>
								<td><input type="button" value="Foward Tasks" data-mini="true" data-theme="a" onclick="OrderInvoicingDoInvoice(SerializeOrderIDs())"  ></td>
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