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
<script>

function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}



function CreditLimitProcessing(RequestID,CustomerID,ID){
	
	//alert(RequestID+" - "+CustomerID);
	
	$.mobile.showPageLoadingMsg();
	//alert($("#UniqueVoucherID").val());
	$.ajax({
		
		url: "cash/GLCreditLimitApproveExecute",
		data: {
			RequestIDD : RequestID,
			CustomerIDD : CustomerID
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			if(json.success == "true"){				
				$("#PostingOptionsID_"+ID).checkboxradio('disable');
				$("#PostingOptionsIDD_"+ID).checkboxradio('disable');
				$.mobile.hidePageLoadingMsg();
				//location = "ReportCenter.jsp?ReportID=66";
			}else{
				alert(json.error);
				//alert("Server could not be reached");
				$.mobile.hidePageLoadingMsg();
				
			}
		},
		error:function(xhr, status){
			$.mobile.hidePageLoadingMsg();
			alert("Server could not be reached.");
			//location = "ReportCenter.jsp?ReportID=50";			
		}
		
	});
	
	
	}


</script>

<style>
td{
font-size: 8pt;
}

</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 343;

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
	WhereHOD = " and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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


//SM

///System.out.println("WhereDistributors : "+WhereDistributors);
String SMIDs="";
long SelectedSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedSM") != null){
	SelectedSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedSM");
	SMIDs = Utilities.serializeForSQL(SelectedSMArray);
}

String WhereSM = "";
if (SMIDs.length() > 0){
	WhereSM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
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
	WhereTDM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
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
	WhereASM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
}


//Sales Type


String AccountTypeIDs="";
long SelectedAccountTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedAccountType") != null){
	SelectedAccountTypeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedAccountType");
	AccountTypeIDs = Utilities.serializeForSQL(SelectedAccountTypeArray);
}

String WhereSalesType = "";
if (AccountTypeIDs.length() > 0){
	WhereSalesType = " and type_id in ("+AccountTypeIDs+")";	
}

long CustomerID = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedCustomerID") != null){
	CustomerID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedCustomerID");
}


//Transaction Account


String CashInstrumentsIDs="";
long SelectedCashInstrumentsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedCashInstruments") != null){
	SelectedCashInstrumentsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedCashInstruments");
	CashInstrumentsIDs = Utilities.serializeForSQL(SelectedCashInstrumentsArray);
}

String WhereCashInstruments = " and account_id=''";
if (CashInstrumentsIDs.length() > 0){
	WhereCashInstruments = " and  account_id in ("+CashInstrumentsIDs+") ";	
}

//


String WarehouseIDs="";
           long SelectedWarehouseArray[] = null;
           if (session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse") != null){
           	SelectedWarehouseArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse");
           	WarehouseIDs = Utilities.serializeForSQL(SelectedWarehouseArray);
           }
           String WhereWarehouse = "";
           if (WarehouseIDs.length() > 0){
           	WhereWarehouse = " and glcp.warehouse_id in ("+WarehouseIDs+") ";	
           }
           
           
         //cash instruments multiple


           String CashInstrumentsMultipleIDs="";
           long SelectedCashInstrumentsMultipleArray[] = null;
           if (session.getAttribute(UniqueSessionID+"_SR1SelectedCashInstrumentsMultiple") != null){
           	SelectedCashInstrumentsMultipleArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedCashInstrumentsMultiple");
           	CashInstrumentsMultipleIDs = Utilities.serializeForSQL(SelectedCashInstrumentsMultipleArray);
           }

           String WhereCashInstrumentsMultiple = "";
           if (CashInstrumentsMultipleIDs.length() > 0){
           	WhereCashInstrumentsMultiple = " and  glcpi.instrument_id in ("+CashInstrumentsMultipleIDs+") ";	
           }
           
           
           long RequestIDs = 0;
           if(session.getAttribute(UniqueSessionID+"_SR1SelectedRequestID") != null){
            RequestIDs = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedRequestID");
           }
           
           String WhereRequestID = "";
           if (RequestIDs!= 0){
        	   WhereRequestID = " and  wr.request_id in ("+RequestIDs+") ";	
           }

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>

<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 
					<tbody>
						
						
						
						<thead>
					    
						
					    <tr style="font-size:11px;">							
							
							<th style="text-align:center;  width:20%">Request ID</th>
							<th data-priority="1"  s1tyle="text-align:center;  width:20%">Request Date</th>
							<th data-priority="1"  s1tyle="text-align:center;  width:20%">Process Name</th>				
							<th data-priority="1"  st1yle="text-align:center;  width:20%">Status Name</th>
							<th data-priority="1"  st1yle="text-align:center;  width:20%">Outlet Name</th>
							<th data-priority="1"  st1yle="text-align:center;  width:20%">Distributor Name</th>
							<th data-priority="1"  styl1e="text-align:center;  width:20%">Level</th>
							<th data-priority="1"  sty1le="text-align:center;  width:20%">Action Name</th>
							<th data-priority="1"  styl1e="text-align:center;  width:20%">Current User</th>														
							
						 </tr>
					    
					  </thead> 
						
						
						<%
						
						long RequestID=0;
						String StatusName="";
						String ProcessName="";
						String ActionName="";
						String CurrentUser="";
						int StepID=0, processID=0;
						
						//System.out.println("SELECT wr.request_id,process_id,(select process_name from workflow_processes wp where wp.process_id=wr.process_id) process_name,status_id,(select status_label from workflow_requests_status wrs where wrs.status_id=wr.status_id) status_name, current_action_id,(select action_label from workflow_actions wa where wa.action_id=wr.current_action_id) action_name,current_step_id,wr.process_id FROM pep.workflow_requests wr where wr.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereRequestID+" and wr.request_id in (SELECT request_id FROM pep.common_outlets_request where 1=1 "+WhereDistributors+") order by wr.request_id desc");
						ResultSet rs = s.executeQuery("SELECT wr.created_on,wr.request_id,process_id,(select process_name from workflow_processes wp where wp.process_id=wr.process_id) process_name,status_id,(select status_label from workflow_requests_status wrs where wrs.status_id=wr.status_id) status_name, current_action_id,(select action_label from workflow_actions wa where wa.action_id=wr.current_action_id) action_name,current_step_id,wr.process_id FROM pep.workflow_requests wr where  wr.status_id not in (2,3) order by wr.request_id desc");
						while(rs.next()){
							RequestID = rs.getLong("request_id");
							ProcessName = rs.getString("process_name");
							StatusName = rs.getString("status_name");
							ActionName = rs.getString("action_name");
							StepID = rs.getInt("current_step_id");
							processID=rs.getInt("process_id");
							
							ResultSet rs2 = s2.executeQuery("SELECT user_id,(select display_name from users where id=user_id) user_name FROM pep.workflow_requests_steps where request_id="+RequestID+" and step_id="+StepID);
							if(rs2.first()){
								CurrentUser = rs2.getString("user_id")+" - "+rs2.getString("user_name");
							}
							
							String disName="", outletName="";
							ResultSet rs3 = s2.executeQuery("SELECT outlet_name, (select name from common_distributors cd where cd.distributor_id=cor.distributor_id) as distributor_name FROM pep.common_outlets_request cor where request_id="+RequestID);
							if(rs3.first()){
								disName=rs3.getString("distributor_name");
								outletName=rs3.getString("outlet_name");
							}
							
							String level="";
							ResultSet rs4 = s2.executeQuery("SELECT comments FROM workflow_processes_steps where process_id="+processID+" and step_id="+StepID);
							if(rs4.first()){
								level=rs4.getString("comments");
							}
							%>
							
							<tr>
								<td style="text-align:center;  width:5%"><%=RequestID %></th>
								<td><%=Utilities.getDisplayDateTimeFormatUniversal(rs.getTimestamp("created_on")) %></td>
								<td data-priority="1"  s1tyle="text-align:center;  width:20%"><%=ProcessName %></td>				
								<td data-priority="1"  st1yle="text-align:center;  width:20%"><%=StatusName %></td>
								<td data-priority="1"  s1tyle="text-align:center;  width:20%"><%=outletName %></td>
								<td data-priority="1"  s1tyle="text-align:center;  width:20%"><%=disName %></td>
								<td data-priority="1"  s1tyle="text-align:center;  width:20%"><%=level %></td>
								<td data-priority="1"  sty1le="text-align:center;  width:20%"><%=ActionName %></td>
								<td data-priority="1"  styl1e="text-align:center;  width:20%"><%=CurrentUser %></td>	
							
							
							</tr>
							
							
							<%
						}
						
						
						%>
						
						
						
						
					</tbody>
						
													
				</table>
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