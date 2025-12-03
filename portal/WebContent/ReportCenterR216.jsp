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
int FeatureID = 271;

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
							
							<th>Request ID</th>
							<th data-priority="1"  style="text-align:center;  width:30%">Customer</th>				
							<th data-priority="1"  style="text-align:center;  width:10%">Credit Limit</th>							
							<th data-priority="1"  style="text-align:center;  width:10%">Validity</th>
							<th data-priority="1"  style="text-align:center;  width:10%">Reason</th>
							<th data-priority="1"  style="text-align:center;  width:10%">Cheque Number</th>
							<th data-priority="1"  style="text-align:center;  width:10%">Branch</th>
							<th data-priority="1"  style="text-align:center;  width:10%">Bank</th>	
							<th data-priority="1"  style="text-align:center;  width:10%"></th>	
							
													
					    </tr>
					    
					  </thead> 
						
						<%
						long ID=0;
						long IstrumentID=0;
						long WarehouseID=0;
						long AccountNumber=0;
						
						
						
						
						int i=1;
						
						long CLRequestID=0;
						long CLCustomerID=0;
						
						
						
						ResultSet rs = s.executeQuery("select id,customer_id,customer_name, credit_limit,valid_from,valid_to,request_id,comments,cheque_no,cheque_bank,cheque_branch,status_id,is_processed from ( "+
								" SELECT id,customer_id,(select name from common_distributors where distributor_id=customer_id ) customer_name, credit_limit,valid_from,valid_to,request_id,comments,ifnull(cheque_no,'') cheque_no,ifnull(cheque_bank,'') cheque_bank,ifnull(cheque_branch,'') cheque_branch, (SELECT status_id FROM workflow_requests wr where wr.request_id=glcccr.request_id) status_id,is_processed FROM gl_customer_credit_limit_request glcccr "+
								" ) x where x.status_id=2 and x.is_processed=0");
						while(rs.next()){
						
							CLRequestID = rs.getLong("request_id");
							CLCustomerID = rs.getLong("customer_id");
							ID = rs.getLong("id");
							
						%>				
						
						<tr>
																
							<td><%=CLRequestID %></td>
							<td>
							<%=CLCustomerID %>-<%=rs.getString("customer_name") %>
							<input type="hidden" name="CLRequestID" id="CLRequestID_<%=CLRequestID %>" value="<%=i+""+CLRequestID%>"/>
							<input type="hidden" name="CLCustomerID" id="CLCustomerID_<%=CLCustomerID %>" value="<%=i+""+CLCustomerID%>"/>
							</td>
							<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(rs.getDouble("credit_limit"))%></td>																							
							<td><%=Utilities.getDisplayDateFormat(rs.getDate("valid_to")) %></td>
							<td><%=rs.getString("comments") %></td>
							<td><%=rs.getString("cheque_no") %></td>
							<td><%=rs.getString("cheque_branch") %></td>
							<td><%=rs.getString("cheque_bank") %></td>
							<td>
								<fieldset data-role="controlgroup" data-type="horizontal" data-mini="true" >								    
								    <input type="radio" name="PostingOptions" id="PostingOptionsID_<%=ID%>" value="1" onClick="CreditLimitProcessing('<%=CLRequestID %>','<%=CLCustomerID %>','<%=ID%>')">
								    <label for="PostingOptionsID_<%=ID%>">Process</label>
								    							    
								</fieldset>
							</td>
							
							
								
						</tr>
						<%
						i++;
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