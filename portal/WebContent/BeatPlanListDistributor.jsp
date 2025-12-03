<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>

<%
long SessionUserID = Long.parseLong((String) session.getAttribute("UserID"));
//String checkHibernation = (SessionUserID != 204220064) ? " and is_active=1" : "";
int FeatureID = 82;

if (Utilities.isAuthorized(FeatureID, SessionUserID) == false) {
	response.sendRedirect("AccessDenied.jsp");
}

Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
%>

<jsp:include page="include/StandaloneSrc.jsp" />
<!-- JQM Base -->
<script src="js/lookups.js"></script>
<script>

function OpenInNewTab(url){
  var win=window.open(url, '_blank');
  win.focus();
}

function BeatPlanRedirect(BeatPlanID, DistributorID){
	//alert('BeatPlanID = '+BeatPlanID);
	$('#BeatPlanID').val(BeatPlanID);
	$('#DistributorID').val(DistributorID);
	
	$('#BeatPlanListDistributorPostForm').submit();
	
}

function selectEmployee(DistributorID){
	$('#CurrentDistributorID').val(DistributorID);
	
	$( "#LookupEmployeeSearch" ).on( "popupbeforeposition", function( event, ui ) {
		lookupEmployeeInit();
	} );
	$('#LookupEmployeeSearch').popup("open");
	
}

function EmployeeSearchCallBackBeatPlanListDistributor(SAPCode, EmployeeName){
	//alert("distributor id = "+$('#CurrentDistributorID').val()+", employee sap code = "+SAPCode+", employee name= "+EmployeeName);
	
	$.mobile.loading( 'show');
		
		$.ajax({
		    url: "distributor/BeatPlanListDistributorUpdateKPO",
		    data: {
		    	CurrentDistributorID : $('#CurrentDistributorID').val(),
		    	KPOID: SAPCode
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	$.mobile.loading( 'hide');
		    	if (json.success == "true"){
		    		
		    		var html = "<table align='right'><tr><td colspan='2' style='border: 0px; text-align: right; padding: 0px'><a href='#' data-mini='true' onclick='selectEmployee("+$('#CurrentDistributorID').val()+")' data-role='button' data-icon='plus' data-iconpos='notext' data-theme='c' data-inline='true' style='margin-top: 0px; margin-left: 0px; margin-bottom: 0px'> </a></td></tr>";
		    		
		    		for(var i = 0; i < json.rows.length; i++){
		    			html += "<tr><td nowrap='nowrap' style='font-size: 12px; border: 0px; padding-top: 12px'>"+json.rows[i].KPOID+" - "+json.rows[i].KPOName+"</td><td style='border: 0px; padding: 0px; padding-top: 5px'><a href='#' data-mini='true' onclick='removeEmployee("+$('#CurrentDistributorID').val()+", "+json.rows[i].KPOID+")' data-role='button' data-icon='minus' data-iconpos='notext' data-theme='c' data-inline='true' style='margin-top: 0px; margin-left: 0px; margin-bottom: 0px'> </a></td></tr>";
		    		}
		    		
		    		html += "</table>";
		    		
		    		$('#DistributorTD_'+$('#CurrentDistributorID').val()).html(html).trigger('create');
		    		
		    		//window.location = 'ManageDistributors.jsp';
		    	}
		    },
		    error: function( xhr, status ) {
		    	$.mobile.loading( 'hide');
		    	alert("Server could not be reached.");
		    }
		});
	
}

function removeEmployee(DistributorIDVal, KPOIDVal){
	
	$.mobile.loading( 'show');
	
	$.ajax({
	    url: "distributor/BeatPlanListDistributorUpdateKPO",
	    data: {
	    	CurrentDistributorID : DistributorIDVal,
	    	KPOID: KPOIDVal,
	    	RemoveKPO: true
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.loading( 'hide');
	    	if (json.success == "true"){
	    		
	    		var html = "<table align='right'><tr><td colspan='2' style='border: 0px; text-align: right; padding: 0px'><a href='#' data-mini='true' onclick='selectEmployee("+DistributorIDVal+")' data-role='button' data-icon='plus' data-iconpos='notext' data-theme='c' data-inline='true' style='margin-top: 0px; margin-left: 0px; margin-bottom: 0px'> </a></td></tr>";
	    		
	    		for(var i = 0; i < json.rows.length; i++){
	    			html += "<tr><td nowrap='nowrap' style='font-size: 12px; border: 0px; padding-top: 12px'>"+json.rows[i].KPOID+" - "+json.rows[i].KPOName+"</td><td style='border: 0px; padding: 0px; padding-top: 5px'><a href='#' data-mini='true' onclick='removeEmployee("+DistributorIDVal+", "+json.rows[i].KPOID+")' data-role='button' data-icon='minus' data-iconpos='notext' data-theme='c' data-inline='true' style='margin-top: 0px; margin-left: 0px; margin-bottom: 0px'> </a></td></tr>";
	    		}
	    		
	    		html += "</table>";
	    		
	    		$('#DistributorTD_'+DistributorIDVal).html(html).trigger('create');
	    		
	    		//window.location = 'ManageDistributors.jsp';
	    	}
	    },
	    error: function( xhr, status ) {
	    	$.mobile.loading( 'hide');
	    	alert("Server could not be reached.");
	    }
	});
	
}


function ExportToExcel(PJPID){
	$.ajax({
		
		url: "employee/PJPExcelMain",
		data: {
			PJPID: PJPID
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			$.mobile.hidePageLoadingMsg();
			if(json.success == "true"){
				//alert("hello");
				
				//window.location='file:///D:/PushEmails/Mobile_Census_7233_20170110.xls';
				 //var url='images/Mobile_Census_7233_20170110.xls';  
				 
				//var url = " file:///D:\\/PBC Development\\/EclipseWorkspaceFinal\\/portal\\/WebContent\\/images/Mobile_Census_7233_20170110.xlsx";
				 
				alert("File is created Successfully!");
				
				var url = "mobile/MobileFileDownloadMDE?file="+json.FileName;
				
				alert("url--"+url);
				
				//window.location=url; 
				 
				//window.open(url,"_blank");
				
				 //window.open(url+"", 'Excel', 'width=20,height=10,toolbar=0,menubar=0,scrollbars=no', '_blank');
				
			
				 
				var mydiv = document.getElementById("ExcelFileReady"+PJPID);
				var aTag = document.createElement('a');
				aTag.setAttribute('id','ExcelFileReady'+PJPID);				
				aTag.setAttribute('href',url);
				aTag.setAttribute('target','_blank');
				aTag.innerHTML = "<label style='color:Gray; text-decoration:none; font-weight:bold; cursor: pointer;'>Download</label>";
				
				
				
				$(mydiv).html(aTag);
				
				/* $('#ExcelFileReady'+PJPID).addClass(ui-btn);  
				$('#ExcelFileReady'+PJPID).addClass(ui-btn-up-c);
				$('#ExcelFileReady'+PJPID).addClass(ui-shadow);
				$('#ExcelFileReady'+PJPID).addClass(ui-btn-corner-all);
				$('#ExcelFileReady'+PJPID).addClass(ui-btn-inline);
				$('#ExcelFileReady'+PJPID).addClass(ui-btn-icon-notext);
				 */
				
				
				 
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

<script>
function updateUserStatus(userId, status) {
    $.mobile.showPageLoadingMsg();
    $.ajax({
        url: "common/PJPManagenent", // Your servlet mapping
        data: {
            userId: userId,
            is_active: status
        },
        type: "POST",
        dataType: "json",
        success: function(json) {
            $.mobile.hidePageLoadingMsg();
            if (json.success === "true") {
                alert("Status updated successfully!");
            } else {
                alert(json.error || "Update failed.");
            }
        },
        error: function(xhr, status) {
            $.mobile.hidePageLoadingMsg();
            alert("Server could not be reached.");
        }
    });
}

function updatePJPType(userId, type) {
    $.mobile.showPageLoadingMsg();
    $.ajax({
        url: "common/PJPType", // Your servlet mapping
        data: {
            userId: userId,
            pjp_type: type
        },
        type: "POST",
        dataType: "json",
        success: function(json) {
            $.mobile.hidePageLoadingMsg();
            if (json.success === "true") {
                alert("Status updated successfully!");
            } else {
                alert(json.error || "Update failed.");
            }
        },
        error: function(xhr, status) {
            $.mobile.hidePageLoadingMsg();
            alert("Server could not be reached.");
        }
    });
}

</script>



<div data-role="page" id="DeskSale" data-url="DeskSale" data-theme="d">

	<jsp:include page="Header2.jsp">
		<jsp:param value="PJP Management" name="title" />
	</jsp:include>

	<div data-role="content" data-theme="d">
		<ul data-role="listview" data-inset="true"
			style="font-size: 10pt; font-weight: normal; margin-top: -10px;"
			data-icon="false">
			<li data-role="list-divider" data-theme="a">Distributors</li>
			<li><input type="hidden" name="CurrentDistributorID"
				id="CurrentDistributorID" value="">

				<table style="width: 100%; margin-top: -8px" cellpadding="0"
					cellspacing="0">
					<tr>

						<td style="width: 100%" valign="top">
							<table border=0
								style="font-size: 13px; font-weight: 400; width: 100%"
								cellpadding="0" cellspacing="0" adata-role="table"
								class="GridWithBorder">

								<thead>
									<tr style="font-size: 11px;">
										<th data-priority="2" style="width: 28%">&nbsp;</th>
										<th data-priority="1" style="text-align: center; width: 12%">Outlets</th>
										<th data-priority="1" style="text-align: center; width: 12%">Assigned
											Outlets</th>
										<th data-priority="1" style="text-align: center; width: 12%">Unassigned
											Outlets</th>
										<th data-priority="1" style="text-align: center; width: 12%">PJP</th>
										<th data-priority="1" style="text-align: center; width: 12%">KPO</th>
									</tr>
								</thead>

								<%
								for (int i = 0; i < UserDistributor.length; i++) {

									int NumberOfOutlets = 0;
									ResultSet rs = s.executeQuery("SELECT count(id) no_of_outlets FROM common_outlets where distributor_id="
									+ UserDistributor[i].DISTRIBUTOR_ID);
									if (rs.first()) {
										NumberOfOutlets = rs.getInt("no_of_outlets");
									}

									int AssignedOutlets = 0;
									//ResultSet rs3 = s.executeQuery("SELECT count(distinct outlet_id) total_outlets_assigned FROM distributor_beat_plan_schedule where id in (SELECT id FROM distributor_beat_plan where distributor_id="+UserDistributor[i].DISTRIBUTOR_ID+")"+checkHibernation); 
									ResultSet rs3 = s.executeQuery(
									"SELECT count(distinct outlet_id) total_outlets_assigned FROM distributor_beat_plan_schedule where id in (SELECT id FROM distributor_beat_plan where distributor_id="
											+ UserDistributor[i].DISTRIBUTOR_ID + ")");
									if (rs3.first()) {
										AssignedOutlets = rs3.getInt("total_outlets_assigned");
									}

									int UnAssignedOutlets = 0;
									UnAssignedOutlets = NumberOfOutlets - AssignedOutlets;

									int NumberOfBeatPlans = 0;
									ResultSet rs4 = s
									.executeQuery("SELECT count(id) as total_beatplans FROM distributor_beat_plan where distributor_id="
											+ UserDistributor[i].DISTRIBUTOR_ID);
									if (rs4.first()) {
										NumberOfBeatPlans = rs4.getInt("total_beatplans");
									}

									int KPOID = 0;
									String KPOName = "";
									ResultSet rs5 = s.executeQuery(
									"SELECT kpo_id, (select concat(first_name, ' ', last_name) from employee_view where sap_code=kpo_id ) kpo_name FROM common_distributors_kpos where distributor_id="
											+ UserDistributor[i].DISTRIBUTOR_ID);
								%>


								<tr>
									<td style="padding-left: 20px"><%=UserDistributor[i].DISTRIBUTOR_ID + "-" + UserDistributor[i].DISTRIBUTOR_NAME%></td>
									<td
										style="text-align: right; padding-right: 10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec; text-align: center;"><%=NumberOfOutlets%></td>
									<td
										style="text-align: right; padding-right: 10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec; text-align: center;"><%=AssignedOutlets%></td>
									<td
										style="text-align: right; padding-right: 10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec; text-align: center;"><%=UnAssignedOutlets%></td>
									<td
										style="text-align: right; padding-right: 10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec; text-align: center;"><%=NumberOfBeatPlans%></td>
									<td
										style="text-align: right; padding-right: 10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec; text-align: center;"
										id="DistributorTD_<%=UserDistributor[i].DISTRIBUTOR_ID%>">
										<table style="border: 0px" align="right" cellpadding="0"
											cellspacing="0">
											<tr>
												<td colspan="2"
													style="border: 0px; text-align: right; padding: 0px">
													<a href="#" data-mini="true"
													onclick="selectEmployee('<%=UserDistributor[i].DISTRIBUTOR_ID%>')"
													data-role="button" data-icon="plus" data-iconpos="notext"
													data-theme="c" data-inline="true"
													style="margin-top: 0px; margin-left: 0px; margin-bottom: 0px">
												</a>
												</td>
											</tr>
											<%
											while (rs5.next()) {
												KPOID = rs5.getInt("kpo_id");
												KPOName = rs5.getString("kpo_name");
											%>
											<tr>
												<td nowrap="nowrap"
													style="font-size: 12px; border: 0px; padding-top: 12px"><%=KPOID + " - " + KPOName%></td>
												<td style="border: 0px; padding: 0px; padding-top: 5px"><a
													href="#" data-mini="true"
													onclick="removeEmployee('<%=UserDistributor[i].DISTRIBUTOR_ID%>', '<%=KPOID%>')"
													data-role="button" data-icon="minus" data-iconpos="notext"
													data-theme="c" data-inline="true"
													style="margin-top: 0px; margin-left: 0px; margin-bottom: 0px">
												</a></td>
											</tr>
											<%
											}
											%>
										</table>
									</td>
								</tr>


								<tr>
									<td style="padding-left: 20px">&nbsp;</td>
									<td
										style="text-align: right; padding-right: 10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"
										colspan="5">

										<ul data-role="listview" data-inset="true"
											style="font-size: 10pt; font-weight: normal; width: 100%"
											data-icon="false" data-corners="false">
											<li data-role="list-divider" data-theme="c">PJP</li>
											<li>

												<table align="center"
													style="font-size: 13px; font-weight: 400; width: 100%"
													cellpadding="0" cellspacing="0">
													<thead>
														<tr>
															<th style="width: 5%">ID</th>
															<th style="width: 20%">Name</th>
															<th style="width: 20%">Assigned To</th>
															<th style="width: 15%">No of Outlets</th>
															<th style="width: 10%">Status</th>
															<th style="width: 10%">PJP Type</th>
															<th style="width: 20%; text-align: center"><a
																href="#" data-mini="true"
																onclick="BeatPlanRedirect(0, <%=UserDistributor[i].DISTRIBUTOR_ID%>)"
																data-role="button" data-icon="plus"
																data-iconpos="notext" data-theme="c" data-inline="true"
																style="margin-top: 0px; margin-left: 0px; margin-bottom: 0px">
															</a></th>
														</tr>
													</thead>

													<%
													//	ResultSet rs2 = s.executeQuery("SELECT id, label, (select assigned_to from distributor_beat_plan_users where id=distributor_beat_plan.id) assigned_to, (SELECT DISPLAY_NAME FROM users where id=assigned_to) orderbooker_name, (SELECT count(distinct outlet_id) as no_of_outlets FROM distributor_beat_plan_schedule where id=distributor_beat_plan.id "+checkHibernation+") no_of_outlets FROM distributor_beat_plan where distributor_id="+UserDistributor[i].DISTRIBUTOR_ID+" order by created_on");
													ResultSet rs2 = s.executeQuery(
															"SELECT id, label, is_active , pjp_type , (select assigned_to from distributor_beat_plan_users where id=distributor_beat_plan.id) assigned_to, (SELECT DISPLAY_NAME FROM users where id=assigned_to) orderbooker_name, (SELECT count(distinct outlet_id) as no_of_outlets FROM distributor_beat_plan_schedule where id=distributor_beat_plan.id ) no_of_outlets FROM distributor_beat_plan where distributor_id="
															+ UserDistributor[i].DISTRIBUTOR_ID + " order by created_on");
													while (rs2.next()) {
													%>
													<tr>
														<td><%=rs2.getString("id")%></td>
														<td><%=rs2.getString("label")%></td>
														<td>
															<%
															if (rs2.getString("assigned_to") != null) {
																out.print(rs2.getString("assigned_to") + " - " + rs2.getString("orderbooker_name"));
															} else {
																out.print("---");
															}
															%>
														</td>
														<td><%=rs2.getString("no_of_outlets")%></td>
														
														<td>
														    <select data-mini="true" 
														            onchange="updateUserStatus(<%= rs2.getInt("id") %>, this.value)">
														        <option value="1" <%= (rs2.getInt("is_active") == 1 ? "selected" : "") %>>Active</option>
														        <option value="0" <%= (rs2.getInt("is_active") == 0 ? "selected" : "") %>>Inactive</option>
														    </select>
														</td>
														
														<td>
														    <select data-mini="true" 
														            onchange="updatePJPType(<%= rs2.getInt("id") %>, this.value)">
														        <option value="1" <%= (rs2.getInt("pjp_type") == 1 ? "selected" : "") %>>GT</option>
														        <option value="2" <%= (rs2.getInt("pjp_type") == 2 ? "selected" : "") %>>BA</option>
														    </select>
														</td>


														<td style="text-align: center"><a
															onclick="BeatPlanRedirect(<%=rs2.getString("id")%>, <%=UserDistributor[i].DISTRIBUTOR_ID%>)"
															href="#" data-role="button" data-icon="edit"
															data-iconpos="notext" data-theme="c" data-inline="true"
															style="margin-top: 0px; margin-left: 0px; margin-bottom: 0px">Edit</a>
															<div id="ExcelFileReady<%=rs2.getString("id")%>"
																style="float: right">
																<a onclick="ExportToExcel(<%=rs2.getString("id")%>)"
																	href="#" data-role="button" data-icon="star"
																	data-iconpos="notext" data-theme="c" data-inline="true"
																	style="margin-top: 0px; margin-left: 0px; margin-bottom: 0px">Download</a>
															</div></td>

													</tr>
													<%
													}
													%>


												</table>
											</li>
										</ul>

									</td>
								</tr>

								<%
								}
								%>


							</table>
						</td>
					</tr>
				</table></li>
		</ul>
	</div>
	<form name="BeatPlanListDistributorPostForm"
		id="BeatPlanListDistributorPostForm" action="BeatPlanCreate.jsp"
		method="post" data-ajax="false">
		<input type="hidden" name="BeatPlanID" id="BeatPlanID" value="">
		<input type="hidden" name="DistributorID" id="DistributorID" value="">
	</form>
	<div data-role="footer" data-position="fixed" data-theme="b"></div>

	<jsp:include page="LookupEmployeeSearchPopup.jsp">
		<jsp:param value="EmployeeSearchCallBackBeatPlanListDistributor"
			name="CallBack" />
	</jsp:include><!-- Include Employee Search -->


</div>
<%
s.close();
ds.dropConnection();
%>
