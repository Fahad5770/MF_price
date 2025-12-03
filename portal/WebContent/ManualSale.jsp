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

<jsp:useBean id="bean" class="com.pbc.inventory.DeliveryNote" scope="page"/>
<jsp:setProperty name="bean" property="*"/>


<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(377, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}


Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, 52);

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

String EditID = "0";
boolean isEditCase = false;

if( request.getParameter("DeskSaleID") != null ){
	EditID = request.getParameter("DeskSaleID");
	isEditCase = true;
}

String DeskSaleIDPrint = "0";
boolean isPrintCase = false;

if( request.getParameter("DeskSaleIDPrint") != null ){
	DeskSaleIDPrint = request.getParameter("DeskSaleIDPrint");
	isPrintCase = true;
}

Warehouse UserWarehouse[] = UserAccess.getUserFeatureWarehouse(SessionUserID, 34);

if( UserWarehouse == null ){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

float TaxRate[] =  new float[2];

ResultSet rs = s.executeQuery("SELECT * FROM inventory_sales_tax_rates order by id");
int i = 0;
while( rs.next() ){
	TaxRate[i] = rs.getFloat("rate");
	i++;
}

%>
<html>


<head>

		<script type="text/javascript">
		
		var SalesTaxPercent = <%=TaxRate[0]%>+100;
		var WHDTaxPercent = <%=TaxRate[1]%>;
		var DistributorID = <%=DistributorID%>;
		
		</script>
		
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
        <script src="js/lookups.js"></script>
        <script src="js/ManualSale.js"></script>
        
        <script>
		
       
	
        
		<% if( isEditCase){ %>
		
			getDeskSaleInfoJson('<%=EditID%>');
		
		<% } %>
		
		
		
		
        </script>
        
</head>
<style>

/*	.ui-select{
		width:200px !important;
	}
*/
</style>
<body>

<div data-role="page" id="DeskSale" data-url="DeskSale" data-theme="d">

    <jsp:include page="Header3.jsp" >
    	<jsp:param value="Manual Sales" name="title"/>
    	
    	<jsp:param value="<%=DistributorName%>" name="HeaderValue"/>

    </jsp:include>
    
    <div data-role="content" data-theme="d">
	

	
	
	<ul data-role="listview" data-inset="false" data-divider-theme="c">
	<li>
	<table border="0" style="width: 100%">
	
		
		<tr>
			<td style="width: 15%;" >
				<input type="text" placeholder="Outlet ID" id="DeskSaleOutletID" name="DeskSaleOutletID" data-mini="true" onChange="getOutletName(); "  maxlength="10">
			</td>
			<td style="width: 20%;">
				<input type="text" placeholder="Outlet Name" id="DeskSaleOutletName" name="DeskSaleOutletName" data-mini="true" readonly="readonly" tabindex="-1" >
			</td>
			<td style="width: 20%;">
				 <input type="text" data-role="date" class="" name="dated" id="dated" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateFormat(new Date()) %>" data-mini="true">
			</td>
			<td style="width: 45%;">
			<input type="text" placeholder="Address" id="OutletAddress" name="OutletAddress" data-mini="true" readonly="readonly" tabindex="-1" >
			</td>
			<td style="width: 20%;">
				<input type="hidden" placeholder="Distributor Name" id="DeskSaleDistributorName" name="DeskSaleDistributorName"  data-mini="true" readonly="readonly" tabindex="-1">
			</td>
			
			
            
		</tr>
		 
		
		</table>
	</li>
    
    <li data-role="list-divider">Products</li> 
    <li>	
	<form id="DeskSaleForm" data-ajax="false" action="#DeskSale" onSubmit="return DeskSaleAddProduct();">		
    
    <table>
		<tr>
			<!-- <td valign="top" style="padding-top:5px">
				<a  href="#" data-role="button" data-icon="grid" data-iconpos="notext" data-theme="c" data-inline="true" data-corners="false">Arrow left</a>
			</td> -->
			<td valign="top" style="width:10%">
				<input type="text" placeholder="Product Code" id="DeskSaleProductCode" name="DeskSaleProductCode" data-mini="true" onBlur="validateProductCode(); getProductInfoJson(this.value)" onchange="setRowEmpty();"  >
			</td>
			<td valign="top" style="width:10%">
				<input  type="text" placeholder="Raw Cases" id="DeskSaleRawCases" name="DeskSaleRawCases" data-mini="true" onchange="DeskSaleCalculate()" readonly="readonly">
			</td>
			<td valign="top" style="width:10%">
				<input  type="text" placeholder="Bottles" id="DeskSaleUnits" name="DeskSaleUnits" data-mini="true" onchange="DeskSaleCalculate()" readonly="readonly">
			</td>
			<td valign="top" style="width:10%">
				<input type="text" placeholder="Rate" id="DeskSaleRate" name="DeskSaleRate" data-mini="true" onchange="DeskSaleCalculate()" readonly="readonly">
				<input type="hidden" id="DeskSaleUnitRate" name="DeskSaleUnitRate" value="" >
			</td>
			<td valign="top" style="width:10%">
				<input  type="text" placeholder="Amount" id="DeskSaleAmount" name="DeskSaleAmount" data-mini="true" readonly="readonly" >
			</td>
			<td valign="top" style="width:10%">
				<input  type="text" placeholder="Discount" id="DeskSaleDiscount" name="DeskSaleDiscount" data-mini="true" onchange="DeskSaleCalculate()" readonly="readonly">
			</td>
			<td valign="top" style="width:10%">
				<input  type="text" placeholder="Net Amount" id="DeskSaleNetAmount" name="DeskSaleNetAmount" data-mini="true" readonly="readonly">
			</td>
			
			<td valign="top">
				<input id="AddRowButton"  type="submit" value="Add" data-inline="true" data-mini="true" data-icon="plus" >
			</td>
			<td><span id="ProductInfoSpan" style="padding-left:20px; font-size:10pt; font-family:Helvetica,Arial,sans-serif"></span></td>
		</tr>
	</table>
	</form>

	<form action="test2.jsp" name="DeskSaleMainForm" id="DeskSaleMainForm">
    
    	<input type="hidden" name="RowMaxID" id="RowMaxID" value="0" >
    
		<input type="hidden" name="DeskSaleEditID" id="DeskSaleEditID" value="<%=EditID%>" >
		
		<input type="hidden" name="DeskSaleOutledIDHidden" id="DeskSaleOutledIDHidden" value="" >
		<input type="hidden" name="DeskSaleDistributorIDHidden" id="DeskSaleDistributorIDHidden" value="<%=DistributorID%>" >
		<input type="hidden" name="DeskSaleRegionIDHidden" id="DeskSaleRegionIDHidden" value="" >
		
		<input type="hidden" name="DeskSaleSalesTaxRateHidden" id="DeskSaleSalesTaxRateHidden" value="<%=TaxRate[0]%>" >
		<input type="hidden" name="DeskSaleWHTaxHidden" id="DeskSaleWHTaxHidden" value="<%=TaxRate[1]%>" >
		<input type="hidden" name="CreatedOnDate" id="CreatedOnDate" value="" >
		
        <input type="hidden" name="UniqueVoucherID" id="UniqueVoucherID" value="<%=Utilities.getUniqueVoucherID(SessionUserID)%>"/>
        
        <table style="width:100%" border="0">
        	<tr>
        		<td style="width:70%" valign="top" rowspan="2">
	        		<table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt; width:100%">
					  <thead>
					    
					    <tr class="ui-bar-c">
							<th data-priority="1">Product Code</th>
							<th data-priority="1">Package</th>
							<th data-priority="1">Brand</th>				
							<th data-priority="1" >Raw Cases</th>
							<th data-priority="1" >Bottles</th>
							<th data-priority="1" >Rate</th>
							<th data-priority="1">Amount</th>
							<th data-priority="1">Discount</th>
							<th data-priority="1">Net Amount</th>
							<th data-priority="1">&nbsp;</th>
					    </tr>
					  </thead>
					  
						<tbody id="DeskSaleTableBody">
						<tr id="NoProductRow">
							<td colspan="10" style="margin: 1px; padding: 0px;">
								<div style="width: 100%; background-color: #FFFFFF; padding: 5px;">No products added.</div>
							</td>
						</tr>
						</tbody>
					</table>
        		</td>
        		<td id="DeskSalePackageStatusTable" style="width:30%;" valign="top">
        			<div class="ui-bar-c" style="text-align:left; padding:7px; font-size: 14px; margin-bottom: 10px">Summary</div>
        			<table>
        				<tr>
        					<td><lable style="font-size: 10pt; font-weight: 400;">Invoice Amount</lable></td>
        					<td><lable style="font-size: 10pt; font-weight: 400;">W.H. Tax</lable></td>
        				</tr>
        				<tr>
        					<td>
        						<input type="hidden" placeholder="0" id="DeskSaleMainFormTotalInvoiceAmount" name="DeskSaleMainFormTotalInvoiceAmount" data-mini="true" readonly="readonly" >
        						<input type="text" placeholder="0" id="DeskSaleMainFormTotalInvoiceAmountField" name="DeskSaleMainFormTotalInvoiceAmountField" data-mini="true" readonly="readonly" >
        					</td>
        					<td>
        						<input type="hidden" placeholder="0" id="DeskSaleMainFormWithHoldingTax" name="DeskSaleMainFormWithHoldingTax" data-mini="true" readonly="readonly" >
        						<input type="text" placeholder="0" id="DeskSaleMainFormWithHoldingTaxField" name="DeskSaleMainFormWithHoldingTaxField" data-mini="true" readonly="readonly" >
        						
        						<input type="hidden" placeholder="0" id="DeskSaleMainFormSalesTax" name="DeskSaleMainFormSalesTax" data-mini="true" readonly="readonly" >
        						<input type="hidden" placeholder="0" id="DeskSaleMainFormSalesTaxField" name="DeskSaleMainFormSalesTaxField" data-mini="true" readonly="readonly" >
        						
        					</td>
        				</tr>
        				
        				
        				<tr>
        					<td><lable style="font-size: 10pt; font-weight: 400;">Total Amount</lable></td>
        					<td><lable style="font-size: 10pt; font-weight: 400;">Net Amount</lable></td>
        				</tr>
        				<tr>
        					<td>
        						<input type="hidden" placeholder="0" id="DeskSaleMainFormTotalAmount" name="DeskSaleMainFormTotalAmount" data-mini="true" readonly="readonly" >
        						<input type="text" placeholder="0" id="DeskSaleMainFormTotalAmountField" name="DeskSaleMainFormTotalAmountField" data-mini="true" readonly="readonly" >
        					</td>
        					<td>
        						<input type="hidden" placeholder="0" id="DeskSaleMainFormFinalNetAmountRounded" name="DeskSaleMainFormFinalNetAmountRounded" data-mini="true" readonly="readonly" >
        						<input type="text" placeholder="0" id="DeskSaleMainFormFinalNetAmountRoundedField" name="DeskSaleMainFormFinalNetAmountRoundedField" data-mini="true" readonly="readonly" >
        					
        						<input type="hidden" placeholder="0" id="DeskSaleMainFormTotalDiscount" name="DeskSaleMainFormTotalDiscount" data-mini="true" onchange="updateInvoiceSummary()" >
        						<input type="hidden" placeholder="0" id="DeskSaleMainFormTotalDiscountField" name="DeskSaleMainFormTotalDiscountField" data-mini="true" onkeyup="updateInvoiceSummary()" >
        					</td>
        				</tr>
        				
        				
        				<tr>
        					<td><lable style="font-size: 10pt; font-weight: 400;"></lable></td>
        				</tr>
        				<tr>
        				
        					<td>
        					
	        					<input type="hidden" placeholder="0" id="DeskSaleMainFormFinalNetAmount" name="DeskSaleMainFormFinalNetAmount" data-mini="true" readonly="readonly" >
	        					<input type="hidden" placeholder="0" id="DeskSaleMainFormFinalNetAmountField" name="DeskSaleMainFormFinalNetAmountField" data-mini="true" readonly="readonly" >
	        					<input type="hidden" placeholder="0" id="DeskSaleMainFormAdjustment" name="DeskSaleMainFormAdjustment" data-mini="true" readonly="readonly" >
        					
        					</td>
        				</tr>
        				
        			</table>
        		</td>
        	</tr>
        	<tr>
        		<td id="ProductStatistics" style="width:30%;" valign="top">&nbsp;</td>
        	</tr>
        	<tr>
        		<td>&nbsp;</td>
        	</tr>
        	<tr>
        		<td>
        		
        		<ul data-role="listview" data-inset="false" data-divider-theme="c" style="margin:0px">
				        <li data-role="list-divider">Promotions</li>
				        <li>
				        	<div id="DeskSalePromotionsDiv">&nbsp;</div>
				        </li>
			        </ul>
        		
        		</td>
        	</tr>
        </table>
        
		<!-- <a data-role="button" data-inline="true" data-theme="c" ><span id="SamplingReceivingTotal">Total: 0.00</span></a> -->
		
		<input type="hidden" id="DistributorID" name="DistributorID">
		
	</form>
	</li>
	</ul>
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
	<div>
		<table style="width: 100%;">
			<tr>
				<td>
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="DeskSaleSave" href="#" class="ui-disabled" onClick="DeskSaleSubmit();">Save</a>
                   <!--   <a data-icon="check" data-theme="b" data-role="button" data-inline="true" id="DeskSalePrint" href="DeskSalePrint.jsp?DeliveryID=<% if( isEditCase ){ out.print(EditID); }else if( isPrintCase ){ out.print(DeskSaleIDPrint); } %>" target="_blank" <% if( isEditCase == false && isPrintCase == false ){ %> class="ui-disabled" <% } %>  >Print</a> -->
                    
                    <!-- <a data-icon="check" data-theme="b" data-role="button" data-inline="true" id="DeskSalePrint" href="DeskSalePrint.jsp?DeliveryID=<% //if( isEditCase ){ out.print(EditID); }else if( isPrintCase ){ out.print(DeliveryIDPrint); } %>" target="_blank" <% //if( isEditCase == false && isPrintCase == false ){ %> class="ui-disabled" <% //} %> >Print</a> -->
                    
                    <button data-icon="check" data-theme="b" data-inline="true" id="DeskSaleReset" onClick="javascript:window.location='ManualSale.jsp'" >Reset</button>
                    <% if( isEditCase ) { %>
                    	<!-- <a data-icon="check" data-theme="b" data-role="button" data-inline="true" id="DeskSaleDelete" href="#"  onClick="DeskSaleDelete();">Delete</a> -->
                    <% } %>
				</td>
                <td align="right">
                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="DeskSaleSearch" >Search</a>
				</td>
			</tr>
		</table>
	</div>
	    	
    </div>
    
    <div data-role="popup" id="popupSearch" style="padding:0px; border:0px;">
        <div data-role="header" data-theme="e" class="ui-corner-top">
    	    <h1 style="font-size: 14px;">Search</h1>
	    </div>
	    
	    <br>
	    <table>
	    	<tr>
	    		<td>
	    			<select name="PackageSearch" id="PackageSearch" onchange="populateBrand(); searchProductCode()">
						<%=bean.getPackageOptions()%>
					</select>
	    		</td>
	    		<td>
	    			<select name="BrandSearch" id="BrandSearch" onchange="searchProductCode()">
						<option value="">Select Brand</option>
					</select>
	    		</td>
	    	</tr>
	    </table>
		
	</div>
	
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
    	<jsp:param value="52" name="OutletSearchFeatureID" />
    </jsp:include><!-- Include Outlet Search -->
</div>

</body>
</html>
<%

s.close();
c.close();
ds.dropConnection();

bean.close();
%>