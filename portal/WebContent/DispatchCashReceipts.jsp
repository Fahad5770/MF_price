<%@page import="java.lang.reflect.Array"%>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.Date"%>
<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<%@page import="com.pbc.common.Distributor"%>

<%
System.out.println("Helkllllll");

long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(422, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

String EditID = "0";
boolean isEditCase = false;


String DeskSaleIDPrint = "0";
boolean isPrintCase = false;

if( request.getParameter("DeskSaleIDPrint") != null ){
	DeskSaleIDPrint = request.getParameter("DeskSaleIDPrint");
	isPrintCase = true;
}

Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, 59);

 
String DistributorName = "";
long DistributorID = 0;


if( UserDistributor != null ){
	if(UserDistributor.length>1) //if it has more than 1 distributor
	{
		response.sendRedirect("AccessDenied.jsp");
	}
	else
	{
		DistributorName = UserDistributor[0].DISTRIBUTOR_NAME;
		DistributorID = UserDistributor[0].DISTRIBUTOR_ID;
	}
}




Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();

%>
<html>


<head>

		
		
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
        <script src="js/lookups.js"></script>
        <script src="js/DispatchCashReceipts.js?2345=4567"></script>
        
       
		
        
        
</head>
<style>

/*	.ui-select{
		width:200px !important;
	}
*/
</style>
<body>

<div data-role="page" id="DispatchCashReceiptsMainInner" data-url="DispatchCashReceiptsMainInner" data-theme="d">

    <div data-role="header" data-theme="c" data-position="fixed">
    <div>
		

    
    <div style="float:left; width:10%">
    	<a href="DispatchCashReceiptsMain.jsp" data-role="button" data-theme="d" data-inline="true" data-ajax="false" data-icon="back">Back</a>
    </div>
    <div style="float:left; width:90%;b1ackground-color:Red; text-align:center;"><h1 style="font-size: 14pt;float:left; margin-left:45%; text-align:center;">Dispatch Cash Receipts</h1>
    
	    <!-- <div data-role="navbar" style="width:20%; float:right; margin-right:3px; margin-top:10px;">
	    	<ul>
	        	<li><a href="javascript:LiquidReturns()" class="ui-btn-active" data-ajax="false">Cash Receipts</a></li>
	        	
	    	</ul>
		</div>/navbar -->
	</div>
    
	</div>
		


</div>
    
    <div data-role="content" data-theme="d">
	
<%
long DispatchID =0;
String VehicleName="";
String VehicleType="";
String DriverName="";
double InvoiceAmount=0;
double CashReceived=0;
double RemainingBalance=0;
int totalSales=0;
 DispatchID =Utilities.parseLong(request.getParameter("DispatchID"));
 VehicleName=Utilities.filterString(request.getParameter("DisptachReturnsVehicleNumber"),1,100);
 VehicleType=Utilities.filterString(request.getParameter("DisptachVehicleType"),1,100);
 DriverName=Utilities.filterString(request.getParameter("DisptachReturnsDriverName"),1,100);
 ResultSet rs11 = s3.executeQuery("SELECT sales_id FROM pep.inventory_sales_dispatch_invoices where id="+DispatchID+" union SELECT sales_id FROM pep.inventory_sales_dispatch_extra_invoices where  id="+DispatchID);
	
	while(rs11.next())
	{
		long SalesID = rs11.getLong("sales_id");
		ResultSet rs12 = s4.executeQuery("SELECT net_amount FROM pep.inventory_sales_adjusted where id="+SalesID);
		
		while(rs12.next())
		{
			InvoiceAmount += rs12.getLong("net_amount");
		}
		totalSales++;
	}	
	
	ResultSet rs13 = s4.executeQuery("SELECT cash_received FROM pep.inventory_sales_dispatch where id="+DispatchID);
	
	while(rs13.next())
	{
		CashReceived += rs13.getLong("cash_received");
	}	
	RemainingBalance = CashReceived;
%>
	<form id="ReturnsGenerrateForm" name="ReturnsGenerrateForm" action="DispatchCashReceipts.jsp" method="POST" data-ajax="false">
		<input type="hidden" name="DispatchID" id="DispatchID" value="<%=DispatchID%>"/>
		<input type="hidden" name="DisptachReturnsVehicleNumber" id="DisptachReturnsVehicleNumber" value="<%=VehicleName%>"/>
		<input type="hidden" name="DisptachReturnsDriverName" id="DisptachReturnsDriverName" value="<%=DriverName%>"/>
		<input type="hidden" name="DisptachVehicleType" id="DisptachVehicleType" value="<%=VehicleType%>"/>
	</form>
	<form id="EmptyGenerrateForm" name="EmptyGenerrateForm" action="DispatchEmptyReturns.jsp" method="POST" data-ajax="false">
		<input type="hidden" name="DispatchID" id="DispatchID" value="<%=DispatchID%>"/>
		<input type="hidden" name="DisptachReturnsVehicleNumber" id="DisptachReturnsVehicleNumber" value="<%=VehicleName%>"/>
		<input type="hidden" name="DisptachReturnsDriverName" id="DisptachReturnsDriverName" value="<%=DriverName%>"/>
		<input type="hidden" name="DisptachVehicleType" id="DisptachVehicleType" value="<%=VehicleType%>"/>
	</form>
	
	<ul data-role="listview" data-inset="false" data-divider-theme="c">
	<li>
	<table border="0" style="width: 100%">
	
		
		<tr >
			<td style="width: 15%;" >
				<input type="text" placeholder="Vehicle#" id="DispatchCashReceiptsVehicleNumber" name="DispatchCashReceiptsVehicleNumber" data-mini="true" maxlength="10" value="<%=VehicleName%>" readonly>
			</td>
			<td style="width: 20%;">
				<input type="text" placeholder="Driver Name" id="DispatchCashReceiptsDriverName" name="DispatchCashReceiptsDriverName" data-mini="true" readonly="readonly" tabindex="-1" value="<%=DriverName%>" readonly>
			</td>
			<td style="width: 45%;">
			<input type="text" placeholder="Vehicle type" id="DispatchCashReceiptsVehicleType" name="DispatchCashReceiptsVehicleType" data-mini="true" readonly="readonly" tabindex="-1" value="<%=VehicleType%>" readonly>
			</td>
			
            
		</tr>
		</table>
		
	</li>
    
    <li data-role="list-divider">Outlets</li>
    <li>	
	<form id="DispatchCashReceiptsForm" data-ajax="false" action="" onSubmit="return DispatchCashReceiptsAddProduct();">		
    
    <table>
		<tr style="display:none">
			<!-- <td valign="top" style="padding-top:5px">
				<a  href="#" data-role="button" data-icon="grid" data-iconpos="notext" data-theme="c" data-inline="true" data-corners="false">Arrow left</a>
			</td> -->
			<td valign="top" style="width:10%">
				<input type="text" placeholder="Product Code" id="DispatchCashReceiptsProductCode" name="DispatchCashReceiptsProductCode" data-mini="true" >
			</td>
			<td valign="top" style="width:10%">
				<input  type="text" placeholder="Raw Cases" id="DispatchCashReceiptsRawCases" name="DispatchCashReceiptsRawCases" data-mini="true">
			</td>
			<td valign="top" style="width:10%">
				<input  type="text" placeholder="Bottles" id="DispatchCashReceiptsUnits" name="DispatchCashReceiptsUnits" data-mini="true" >
			</td>
			<%
							boolean EmptyResultSet1 = true;
							//ResultSet rs1 = s.executeQuery("select * from inventory_sales_dispatch_returned_products isdrp,inventory_products_view ipv where isdrp.product_id=ipv.product_id and is_empty=0 and dispatch_id="+DispatchID);
							ResultSet rs1 = s.executeQuery("SELECT * FROM inventory_sales_dispatch Where is_adjusted=1 and id="+DispatchID);
							
							while(rs1.next())
							{
								EmptyResultSet1 = false;
							}	
						%>
			
			<%-- <td valign="top" id="AddDispatchCashReceiptsBtnTD" <%if(!EmptyResultSet1){ %>class="ui-dis2abled"  <%} %> > --%>
			
		 <td valign="top" id="AddDispatchCashReceiptsBtnTD" > 
				<input  type="submit" value="Add" data-inline="true" data-mini="true" data-icon="plus" >
			</td>
			<td><span id="ProductInfoSpan" style="padding-left:20px; font-size:10pt; font-family:Helvetica,Arial,sans-serif"></span></td>
		</tr>
	</table>
	</form>

	<form action="test2.jsp" name="DispatchReturnMainForm" id="DispatchReturnMainForm">
    
    	
    
		<input type="hidden" name="DeskSaleEditID" id="DeskSaleEditID" value="<%=EditID%>" >
		<input type="hidden" name="isEditCase" id="isEditCase" value="0"/>
		<input type="hidden" name="DispatchIDForInsertion" id="DispatchIDForInsertion" value="<%=DispatchID%>"/>
		<input type="hidden" name="VehicleNameForTitle" id="VehicleNameForTitle" value="<%=VehicleName%>"/>
		<input type="hidden" name="TotalUnitsForSummar" id="TotalUnitsForSummar" value="0"/>
		
		
	
        
        
        <table style="width:100%" border="0">
        	<tr>
        		<td style="width:70%" valign="top" rowspan="2">
	        		<table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt; width:100%">
					  <thead>
					    
					    <tr class="ui-bar-c">
							<th style="padding-top: 12px;" data-priority="1">Invoices</th>
							<th style="padding-top: 12px;"  data-priority="1">Outlet</th>
							<th style="padding-top: 12px;"  data-priority="1" >Total Amount</th>
							<th style="padding-top: 12px;"  data-priority="1" >Cash Received</th>
							 <!-- <th data-priority="1"> 
								 
								<a data-theme="b" data-role="button" data-inline="true"  data-mini="true" href="#" class="ui-di2sabled" onClick="updateSummaryTable()">Update</a>
							</th> -->
					    </tr>
					  </thead>
					  
						<tbody id="DispatchCashReceiptsTableBody">
						
						<%
						
							
						
						
							
							
									String qry = "SELECT sales_id FROM pep.inventory_sales_dispatch_invoices where id="+DispatchID+" union SELECT sales_id FROM pep.inventory_sales_dispatch_extra_invoices where  id="+DispatchID;
									
						System.out.println(qry);
									ResultSet rs2 = s.executeQuery(qry);
									int rowcount=0;
									double TotalNetAmount=0.0;
									double TotalOrderCashReceipts = 0.0;
									while(rs2.next())
									{
									
										long SalesID = rs2.getLong("sales_id");
										
										
										String qry2="SELECT order_id,outlet_id,net_amount FROM pep.inventory_sales_adjusted where id="+SalesID+" and is_settled=0";
										System.out.println(qry2);
										ResultSet rs3 = s2.executeQuery(qry2);
										long  OrderID=0;
										long  OutletID=0;
										double NetAmount=0;
										if(rs3.first()){
											rowcount++;
											OrderID=rs3.getLong("order_id");
											OutletID=rs3.getLong("outlet_id");
											NetAmount=rs3.getLong("net_amount");
											TotalNetAmount+=NetAmount;
										
										
										String qry3="SELECT cash_received FROM pep.inventory_sales_dispatch_cash_receipts where dispatch_id="+DispatchID+" and outlet_id="+OutletID;
										System.out.println(qry3);
										ResultSet rs4 = s2.executeQuery(qry3);
									
										double OrderCashReceipts=0;
										if(rs4.first()){
											OrderCashReceipts=rs4.getDouble("cash_received");
											RemainingBalance -=OrderCashReceipts;
										}
										String qry4="SELECT name FROM pep.common_outlets where id="+OutletID;
										System.out.println(qry4);
										ResultSet rs5 = s2.executeQuery(qry4);
									
										String OutletName="";
										if(rs5.first()){
											OutletName=rs5.getString("name");
										}
										if(OrderCashReceipts==0){
											OrderCashReceipts = NetAmount;
										}
										TotalOrderCashReceipts += OrderCashReceipts;
								%>
										 
									
									<tr id='DeskSale<%=rowcount%>'>
										<td><%=SalesID %><input type='hidden' name='DispatchCashReceiptsOrderID' value='<%=SalesID%>'></td>
										<td><%=OutletID %> - <%=OutletName %><input type='hidden' name='DispatchCashReceiptsOutletID' value='<%=OutletID %>' ></td>						
										<td><%=NetAmount%><input type='hidden' name='DispatchCashReceiptsTotalAmount' value='<%=NetAmount%>'></td>
										<td><input type='text' data-mini=true name='DispatchCashReceiptsOrderCashReceipts' onchange="DispatchCashReceiptsCalculate(<%=totalSales %>)"  id='OrderCashReceipts_<%=rowcount %>' value='<%=OrderCashReceipts %>'></td>
									</tr>
									
								<%
										}
									}
			        				
									RemainingBalance = TotalNetAmount - TotalOrderCashReceipts;
								
								
						%>	
							<tr id="NoProductRow">
								<td colspan="10" style="margin: 1px; padding: 0px;">
									<div style="width: 100%; background-color: #FFFFFF; padding: 5px;"></div>
								</td>
							</tr>	
						<% 	
							
						%>
						<input type="hidden" name="RowMaxID" id="RowMaxID" value="<%=rowcount%>" >
						
						</tbody>
					</table>
        		</td>
        		<td id="DeskSalePackageStatusTable" style="width:30%;" valign="top">
        			<div id="DispatchCashReceiptsSummary"></div>
        			
        		</td>
        	</tr>
        	<tr>
        		<td id="ProductStatistics" style="width:30%;" valign="top">&nbsp;</td>
        	</tr>
        	<tr>
        		<td>&nbsp;</td>
        	</tr>
        	
        </table>
        	
		<table border="0" style="width: 30%">
		<tr >
			<td style="width: 25%;" >
				Total Net Amount
			</td>
			<td style="width: 25%;">
				<input type="text" placeholder="Total Net Amount" id="DispatchCashReceiptsTotalNetAmount" name="DispatchCashReceiptsTotalNetAmount" data-mini="true" readonly="readonly" tabindex="-1" value="<%=TotalNetAmount%>" readonly>
			</td>
			</tr>
		<tr >
			<td style="width: 25%;" >
				Total Cash Received
			</td>
			<td style="width: 25%;">
				<input type="text" placeholder="Cash Received" id="DispatchCashReceiptsCashReceived" name="DispatchCashReceiptsCashReceived" data-mini="true" readonly="readonly" tabindex="-1" value="<%=TotalOrderCashReceipts%>" readonly>
			</td>
			</tr>
			<tr>
			<td style="width: 25%;">
				Remaining Amount			
			</td>
			<td style="width: 25%;">
			<input type="text" placeholder="Remaining Amount" id="DispatchCashReceiptsRemainingAmount" name="DispatchCashReceiptsRemainingAmount" data-mini="true" readonly="readonly"   value="<%=RemainingBalance%>" readonly >
			</td>
			
            
		</tr>
		 
		
		</table>
		<!-- <a data-role="button" data-inline="true" data-theme="c" ><span id="SamplingReceivingTotal">Total: 0.00</span></a> -->
		
		<input type="hidden" id="DistributorID" name="DistributorID">
		<input type="hidden" id="NoLiquRequiredToggleBit" name="NoLiquRequiredToggleBit" value="0">
		<!-- <table border="0" style="font-size: 10pt; width:25%">
			<tr>
				<td style="width:30%" id="NoLiquidReqTd">
				<fieldset data-role="controlgroup">				   
				    <input type="checkbox" name="NoLiquReq" id="NoLiquReq" onClick="NoLiquRequired15();">
				    <label for="NoLiquReq">No returned</label>				    
				</fieldset>				
				
				</td>
			</tr>
		</table> -->
	</form>
	</li>
	</ul>
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
	<div>
		<table style="width: 100%;">
			<tr>
				 <td  id="DispatchCashReceiptsSaveTD">
				
					
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="DispatchCashReceiptsSave" href="#" class="ui-di2sabled" onClick="DispatchCashReceiptsSubmit();">Save</a>
               
                   
				</td>
                <!-- <td align="right">
                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="DeskSaleSearch" >Search</a>
				</td> -->
			</tr>
		</table>
	</div>
	    	
    </div>
    

<%-- 	
	<jsp:include page="LookupProductSearchPopup.jsp" > 
    	<jsp:param value="ProductSearchCallBack" name="CallBack" />
    </jsp:include><!-- 
    Include Product Search -->

	<div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:600px; max-height: 600px" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >

			<form data-ajax="false" id="DeskSaleFormDateRange" onSubmit="return showSearchContent()">
            <table>
            	<tr>
                	<td>
                        <span id="FromDateSpan"><input type="text" data-mini="true" name="DeskSaleFromDate" id="DeskSaleFromDate" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateFormat(new java.util.Date())%>"></span>
                    
                    </td>
                    <td>
                    
						<span id="ToDateSpan"><input type="text" data-mini="true" name="DeskSaleToDate" id="DeskSaleToDate" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateFormat(new java.util.Date())%>" ></span>
                    
                    </td>
                    <td>
                    	<button data-role="button" data-icon="search" id="DeskSaleDateButton" data-iconpos="notext" data-theme="c" data-inline="true" data-corners="false" onClick="showSearchContent();"></button>
                    </td>
                </tr>
            </table>
        </form>

        <div id="SearchContent">
        </div>
            
        </div>
    </div>
 <jsp:include page="LookupOutletSearchPopup.jsp" > 
    	<jsp:param value="OutletSearchCallBackDeskSale" name="CallBack" />
    </jsp:include><!-- Include Outlet Search -->
</div> --%>

</body>
</html>
<%

s.close();
c.close();
ds.dropConnection();

%>