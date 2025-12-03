<%@page import="sun.nio.ch.Net"%>
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



function EmptyCreditLimitProcessing(RequestID,CustomerID,ID){
	
	//alert(RequestID+" - "+CustomerID);
	
	$.mobile.showPageLoadingMsg();
	//alert($("#UniqueVoucherID").val());
	$.ajax({
		
		url: "cash/GLEmptyCreditLimitApproveExecute",
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
	
function ReportCenterR276ProductRawCases(ParamDocumentID){
	
	$('#ReportCenterR276ProductRawCases').html( "<img src='images/snake-loader.gif' >" );
	
	$.ajax({
	    url: "ReportCenterR276ProductRawCases.jsp",
	    data: {
	    	DocumentID: ParamDocumentID
	    },
	    type: "POST",
	    dataType : "html",
	    success: function( html ) {
	    	//alert(html);
	    	$('#ReportCenterR276ProductRawCases').html( html ).trigger('create');
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}	


function ReportCenterR276Pictures(ParamDocumentID){
	
	$('#ReportCenterR276Pictures').html( "<img src='images/snake-loader.gif' >" );
	
	$.ajax({
	    url: "ReportCenterR276Pictures.jsp",
	    data: {
	    	DocumentID: ParamDocumentID
	    },
	    type: "POST",
	    dataType : "html",
	    success: function( html ) {
	    	//alert(html);
	    	$('#ReportCenterR276Pictures').html( html ).trigger('create');
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}	


</script>

<style>
td{
font-size: 8pt;
}

.ui-badge-container {
  position: relative;
 
}
.my-badge {

  color: gray;
  padding: 1px 4px;
  position: absolute;

  top: -7px;
  z-index: 999;
  border-radius: .8em;

}
</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 344;

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
							
							<th data-priority="1"  style="text-align:center;  width:1%">Order #</th>	
							<th data-priority="1"  style="text-align:center;  width:5%">Order Date</th>	
							<th data-priority="1"  style="text-align:center;  width:6%">Order Time</th>	
							<th data-priority="1"  style="text-align:center;  width:12%">Outlet</th>	
							<th data-priority="1"  style="text-align:center;  width:11%">Distributor</th>	
							<th data-priority="1"  style="text-align:center;  width:8%">Booked By</th>	
							<th data-priority="1"  style="text-align:center;  width:8%">Status</th>				
							<th data-priority="1"  style="text-align:center;  width:1%">Latitude</th>							
							<th data-priority="1"  style="text-align:center;  width:1%">Longitude</th>
							<th data-priority="1"  style="text-align:center;  width:1%">Accuracy</th>						
							<th data-priority="1"  style="text-align:center;  width:1%">Barcode</th>						
							<th data-priority="1"  style="text-align:center;  width:1%">Pictures</th>						
					    </tr>
					    
					  </thead> 
						
						<%
						
						int OrderID=0;
						long MobileOrderNo=0;
						String Outlet="";
						String Distributor="";
						String BookedBy="";
						String Status="";
						double Lat=0;
						double Lng=0;
						double Accuracy=0;
						
						long UnedittedID=0;
						
						
						int i=1;
						
						String SQL="select mo.id,mo.mobile_order_no,date(mo.created_on) CreatedDate,time(created_on) CreatedTime,(select concat(co.id,' - ', co.name ) from common_outlets co where co.id=mo.outlet_id ) Outlet ,(select concat(cd.distributor_id,' - ', cd.name ) from common_distributors cd where cd.distributor_id=mo.distributor_id ) customer_name,(select concat(u.ID,' - ', u.Display_Name ) from users u where u.id=mo.created_by ) BookedBy,(select most.label from mobile_order_status_types most where most.id=mo.status_type_id) Status,mo.lat,mo.lng,mo.accuracy,mo.unedited_order_id from mobile_order mo where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+"";
						System.out.println(SQL);
						ResultSet rs = s.executeQuery(SQL);
						while(rs.next()){
						
							OrderID=rs.getInt("mo.id");
							MobileOrderNo=rs.getLong("mo.mobile_order_no");
							Outlet=rs.getString("Outlet");
							Distributor=rs.getString("customer_name");
							BookedBy=rs.getString("BookedBy");
							Status=rs.getString("Status");
							Lat=rs.getDouble("mo.lat");
							Lng=rs.getDouble("mo.lng");
							Accuracy=rs.getDouble("mo.accuracy");
							
							UnedittedID = rs.getLong("unedited_order_id");
							
							
							
							
						%>				
						
						<tr style="font-size:10px;">
																
							 
							<td><%=MobileOrderNo %>	</td>
							<td style="text-align:center;"><%=Utilities.getDisplayDateFormat(rs.getDate("CreatedDate")) %></td>																						
							<td style="text-align:center;"><%=Utilities.getDisplayTimeFormat(rs.getTime("CreatedTime")) %></td>
							<td ><%=Outlet %></td>
							<td ><%=Distributor %></td>
							<td ><%=BookedBy %></td>
							<td style="text-align:center;"><%=Status %></td>
							<td style="text-align:center;" ><%=Lat %></td>
							<td style="text-align:center;"><%=Lng %></td>
							<td style="text-align:center;"><%=Accuracy %></td>
							
							<td style="text-align:center;"><a href="#popupDialogReportCenterR276ProductRawCases" data-rel="popup" data-position-to="window" data-transition="pop" onclick="ReportCenterR276ProductRawCases(<%=UnedittedID%>)" >View</a> </td>																						
							<td style="text-align:center;"><a href="#popupDialogReportCenterR276Pictures" data-rel="popup" data-position-to="window" data-transition="pop" onclick="ReportCenterR276Pictures(<%=UnedittedID%>)" >View</a> </td>																													
							
								
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