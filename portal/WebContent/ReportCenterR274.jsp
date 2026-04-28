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
int FeatureID = 342;

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
							
							<th style="text-align:center;  width:10%">Distributor</th>
							<th data-priority="1"  style="text-align:center;  width:10%">Order#</th>				
							<th data-priority="1"  style="text-align:center;  width:7%">Punching Date</th>
							<th data-priority="1"  style="text-align:center;  width:7%">Punching Time</th>
							<th data-priority="1"  style="text-align:center;  width:5%">Quantity (R/C)</th>														
							<th data-priority="1"  style="text-align:center;  width:7%">Quantity (C/C)</th>	
							<th data-priority="1"  style="text-align:center;  width:7%">Warehouse</th>
							<th data-priority="1"  style="text-align:center;  width:7%">Order Status</th>							
							<th data-priority="1"  style="text-align:center;  width:10%">Invoice Number</th>
							<th data-priority="1"  style="text-align:center;  width:7%">Invoice Date</th>
							<th data-priority="1"  style="text-align:center;  width:7%">Invoice Time</th>
							<th data-priority="1"  style="text-align:center;  width:7%">Amount</th>	
							<th data-priority="1"  style="text-align:center;  width:10%">Delivery ID</th>	
							<th data-priority="1"  style="text-align:center;  width:7%">Delivery Date</th>								
							<th data-priority="1"  style="text-align:center;  width:5%">Delivery Time</th>									
															
							
							
						 </tr>
					    
					  </thead> 
						
						<%
						
						String Distributor="";
						long Sale_Order_After_Posted=0;
						long Invoice_no=0;
						int i=1;
						
						Date InvoiceDate=null;
						
						
						//ResultSet rs = s.executeQuery("select (select concat(cd.distributor_id,' - ', cd.name ) from common_distributors cd where cd.distributor_id=gop.customer_id ) customer_name,gop.order_no Sales_Orders_After_Posted,date(gop.created_on) Punching_Date,time(gop.created_on) Punching_Time,(select gip.invoice_no from gl_invoice_posting gip where gip.order_no=gop.order_no) invoice_no from gl_order_posting gop where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));
						
						ResultSet rs = s.executeQuery("select (select concat(cd.distributor_id,' - ', cd.name ) from common_distributors cd where cd.distributor_id=gop.customer_id ) customer_name,gop.order_no Sales_Orders_After_Posted,date(gop.created_on) Punching_Date,time(gop.created_on) Punching_Time,(select gip.invoice_no from gl_invoice_posting gip where gip.order_no=gop.order_no) invoice_no, (select gip.created_on from gl_invoice_posting gip where gip.order_no=gop.order_no) invoice_date from gl_order_posting gop where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));
						
						while(rs.next()){
						
							Distributor = rs.getString("customer_name");
							Sale_Order_After_Posted = rs.getLong("Sales_Orders_After_Posted");
							Invoice_no = rs.getLong("invoice_no");
							InvoiceDate = rs.getTimestamp("invoice_date");
							
							
							
							//Delivery Note Enteries
							
							long DeliveryID=0;
							Date DeliveryDate=null;
							Date DeliveryTime = null;
							String DeliveryLocation="";
							long RawCases=0;
							long ConvertedCases=0;
							double Amount=0;
							int IsDeliverd=0;
							
							String OrderStatus="";
							
							if(Sale_Order_After_Posted==2000488832){
								System.out.println("SELECT *,(select label from common_warehouses where id=warehouse_id) ware_house FROM pep.inventory_delivery_note where sap_order_no="+Sale_Order_After_Posted);
							}
							
							
							ResultSet rs2 = s2.executeQuery("SELECT *,(select label from common_warehouses where id=warehouse_id) ware_house FROM pep.inventory_delivery_note where sap_order_no="+Sale_Order_After_Posted);
							while(rs2.next()){
								
								DeliveryID= rs2.getLong("delivery_id");
								DeliveryDate = rs2.getDate("created_on");
								DeliveryTime = rs2.getTime("created_on");
								DeliveryLocation = rs2.getString("ware_house");
								Amount = rs2.getDouble("invoice_amount");
								IsDeliverd = rs2.getInt("is_delivered");
							}
							
							ResultSet rs3 = s2.executeQuery("SELECT sum(total_units/unit_per_sku) raw_cases FROM pep.inventory_delivery_note_products idnp join inventory_products ip on ip.id=idnp.product_id where delivery_id="+DeliveryID+" and category_id=1");
							if(rs3.first()){
								RawCases = rs3.getLong("raw_cases");
							}
							
							
							
							ResultSet rs4 = s2.executeQuery("SELECT sum(liquid_in_ml)/6000 converted_cases FROM pep.inventory_delivery_note_products idnp join inventory_products ip on ip.id=idnp.product_id where delivery_id="+DeliveryID+" and category_id=1");
							if(rs4.first()){
								ConvertedCases = rs4.getLong("converted_cases");
							}
							
							
							
							//////////////////////////////////////
							
							if(DeliveryID!=0){
								if(IsDeliverd==0){
									OrderStatus="Settled ";
								}else{
									OrderStatus="Delivered";
								}
								
							}else if(Invoice_no!=0){
								OrderStatus="Invoiced";
							}else{
								OrderStatus="Pending";
							}
							
							
							
							
						%>				
						
						<tr>
																
							<td ><%=Distributor %></td>
							<td style="text-align:center;"><%=Sale_Order_After_Posted %></td>
							<td style="text-align:center;"><%=Utilities.getDisplayDateFormat(rs.getDate("Punching_Date")) %></td>																						
							<td style="text-align:center;"><%=Utilities.getDisplayTimeFormat(rs.getTime("Punching_Time")) %></td>
							
							<td style="text-align:right;"><% if(RawCases!=0){%><%=RawCases %><%} %></td>
							<td style="text-align:right;"><%if(ConvertedCases!=0){%><%=ConvertedCases %><%} %></td>
							<td ><%=DeliveryLocation %></td>
							<td style="text-align:center;"><%=OrderStatus %></td>
							<td style="text-align:center;"><% if(Invoice_no!=0) {%><%=Invoice_no %><%} else{%><%} %></td>
							
							
							<td style="text-align:center;"><%if(InvoiceDate!=null){%><%=Utilities.getDisplayDateFormat(InvoiceDate) %><%} %></td>
							<td style="text-align:center;"><%if(InvoiceDate!=null){%><%=Utilities.getDisplayTimeFormat(InvoiceDate) %><%} %></td>
							<td style="text-align:right;"><%if(Amount!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(Amount) %><%} %></td>
							<td style="text-align:center;"><%if(DeliveryID!=0){%><%=DeliveryID %><%} %></td>
							<td style="text-align:center;"><%if(DeliveryDate!=null){%><%=Utilities.getDisplayDateFormat(DeliveryDate) %><%} %></td>
							<td style="text-align:center;"><%if(DeliveryTime!=null){%><%=Utilities.getDisplayTimeFormat(DeliveryTime) %><%} %></td>
							
							
							
							
							
														
							
							
								
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