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

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(83, SessionUserID) == false){
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

%>
<html>


<head>

		
		
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
        <script src="js/lookups.js"></script>
        <script src="js/DispatchEmptyReturns.js"></script>
        
       
		
        
        
</head>
<style>

/*	.ui-select{
		width:200px !important;
	}
*/
</style>
<body>

<div data-role="page" id="DispatchEmptyReturnsMainInner" data-url="DispatchReturnsMainInner" data-theme="d">

    <div data-role="header" data-theme="c" data-position="fixed">
    <div>
		

    
    <div style="float:left; width:10%">
    	<a href="DispatchReturnsMain.jsp" data-role="button" data-theme="d" data-inline="true" data-ajax="false" data-icon="back">Back</a>
    </div>
    <div style="float:left; width:90%;b1ackground-color:Red; text-align:center;"><h1 style="font-size: 14pt;float:left; margin-left:45%; text-align:center;">Dispatch Empty Returns</h1>
    
	    <div data-role="navbar" style="width:20%; float:right; margin-right:3px; margin-top:10px;">
	    	<ul>
	        	<li><a href="javascript:LiquidReturns()"  data-ajax="false">Liquid Returns</a></li>
	        	
	        	<li><a href="javascript:EmptyReturns()" class="ui-btn-active" data-ajax="false">Empty Returns</a></li>
	        	
	    	</ul>
		</div><!-- /navbar -->
	</div>
    
	</div>
		


</div>
    
    <div data-role="content" data-theme="d">
	
<%
long DispatchID =0;
String VehicleName="";
String VehicleType="";
String DriverName="";

 DispatchID =Utilities.parseLong(request.getParameter("DispatchID"));
 VehicleName=Utilities.filterString(request.getParameter("DisptachReturnsVehicleNumber"),1,100);
 VehicleType=Utilities.filterString(request.getParameter("DisptachVehicleType"),1,100);
 DriverName=Utilities.filterString(request.getParameter("DisptachReturnsDriverName"),1,100);

%>
	<form id="ReturnsGenerrateForm" name="ReturnsGenerrateForm" action="DispatchReturns.jsp" method="POST" data-ajax="false">
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
	
		
		<tr>
			<td style="width: 15%;" >
				<input type="text" placeholder="Vehicle#" id="DispatchReturnsVehicleNumber" name="DispatchReturnsVehicleNumber" data-mini="true" maxlength="10" value="<%=VehicleName%>" readonly>
			</td>
			<td style="width: 20%;">
				<input type="text" placeholder="Driver Name" id="DispatchReturnsDriverName" name="DispatchReturnsDriverName" data-mini="true" readonly="readonly" tabindex="-1" value="<%=DriverName%>" readonly>
			</td>
			<td style="width: 45%;">
			<input type="text" placeholder="Vehicle type" id="DispatchReturnsVehicleType" name="DispatchReturnsVehicleType" data-mini="true" readonly="readonly" tabindex="-1" value="<%=VehicleType%>" readonly>
			</td>
			
		</tr>
		 
		</table>
	</li>
    
    <li data-role="list-divider">Products</li>
    <li>	
	<form id="DispatchReturnsForm" data-ajax="false" action="" onSubmit="return DispatchReturnsAddProduct();">		
    
    <table>
		<tr>
			<!-- <td valign="top" style="padding-top:5px">
				<a  href="#" data-role="button" data-icon="grid" data-iconpos="notext" data-theme="c" data-inline="true" data-corners="false">Arrow left</a>
			</td> -->
			<td valign="top" style="width:10%">
				<input type="text" placeholder="Product Code" id="DispatchReturnsProductCode" name="DispatchReturnsProductCode" data-mini="true" >
			</td>
			<td valign="top" style="width:10%">
				<input  type="text" placeholder="Raw Cases" id="DispatchReturnsRawCases" name="DispatchReturnsRawCases" data-mini="true">
			</td>
			<td valign="top" style="width:10%">
				<input  type="text" placeholder="Bottles" id="DispatchReturnsUnits" name="DispatchReturnsUnits" data-mini="true" >
			</td>
			<%
							boolean EmptyResultSet1 = true;
							//ResultSet rs1 = s.executeQuery("select * from inventory_sales_dispatch_returned_products isdrp,inventory_products_view ipv where isdrp.product_id=ipv.product_id and is_empty=1 and dispatch_id="+DispatchID);
							ResultSet rs1 = s.executeQuery("SELECT * FROM inventory_sales_dispatch Where is_adjusted=1 and id="+DispatchID);
							while(rs1.next())
							{
								EmptyResultSet1 = false;
							}	
						%>
			
			<td valign="top" id="AddDispatchReturnsBtnTD" <%if(!EmptyResultSet1){ %>class="ui-disabled"  <%} %>>
			
			
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
							<th data-priority="1">Product Code</th>
							<th data-priority="1">Package</th>
							<th data-priority="1">Brand</th>				
							<th data-priority="1" >Raw Cases</th>
							<th data-priority="1" >Bottles</th>
							
							<th data-priority="1">&nbsp;</th>
					    </tr>
					  </thead>
					  
						<tbody id="DispatchReturnsTableBody">
						<%
							boolean EmptyResultSet = true;
							ResultSet rs = s.executeQuery("select ipv.sap_code, ipv.unit_per_sku, ipv.package_label, ipv.brand_label, isdrp.raw_cases, isdrp.units, isdrp.total_units, ipv.liquid_in_ml, isdrp.product_id, ipv.package_id, ipv.brand_id from inventory_sales_dispatch_returned_products isdrp,inventory_products_view ipv where isdrp.product_id=ipv.product_id and dispatch_id="+DispatchID+" and isdrp.is_empty=1");
							int rowcount=0;
							while(rs.next())
							{
								EmptyResultSet = false;
								rowcount++;
						%>
							
							<tr id='DeskSale<%=rowcount%>'>
								<td><%=rs.getString("sap_code") %><input type='hidden' name='DispatchReturnsMainFormUnitPerSKU' value='<%=rs.getString("unit_per_sku")%>'><input type='hidden' name='DispatchReturnsMainFormProductName' value='<%=rs.getString("sap_code") %>'></td>
								<td><%=rs.getString("package_label") %><input type='hidden' name='DispatchReturnsMainFormPackageLabel' value='<%=rs.getString("package_label") %>' ><input type='hidden' name='PackageID' value='<%=rs.getString("package_id") %>' ><input type='hidden' name='DispatchReturnsMainFormLiquidInML' value='<%=rs.getString("liquid_in_ml") %>'><input type='hidden' name='TotalUnitsCalculatedForSummary' value='<%=rs.getString("total_units") %>'></td>						
								<td><%=rs.getString("brand_label") %><input type='hidden' name='DispatchReturnsMainFormBrandLabel' value='<%=rs.getString("brand_label") %>' ><input type='hidden' name='BrandID' value='<%=rs.getString("brand_id") %>' ><input type='hidden' id='ProductCode' name='ProductCode' value='<%=rs.getString("sap_code") %>'><input type='hidden' name='ProductID' value='<%=rs.getString("product_id") %>'></td>
								<td><%=rs.getInt("raw_cases") %><input type='hidden' name='DispatchReturnsMainFormRawCases' value='<%=rs.getInt("raw_cases") %>'></td>
								<td><%=rs.getInt("units") %><input type='hidden' name='DispatchReturnsMainFormUnits' value='<%=rs.getInt("units") %>'></td>							
								<!-- <td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' >Delete</a></td>-->
								<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick="DeskSaleDeleteRow('DeskSale<%=rowcount%>')">Delete</a></td>
							</tr>
						<%
						
							}
							if(EmptyResultSet)
							{
						%>	
							<tr id="NoProductRow">
								<td colspan="10" style="margin: 1px; padding: 0px;">
									<div style="width: 100%; background-color: #FFFFFF; padding: 5px;">No products added.</div>
								</td>
							</tr>	
						<% 	
							}
						%>
						<input type="hidden" name="RowMaxID" id="RowMaxID" value="<%=rowcount%>" >
						</tbody>
					</table>
        		</td>
        		<td id="DeskSalePackageStatusTable" style="width:30%;" valign="top">
        			<div id="DispatchReturnsSummary"></div>
        			
        		</td>
        	</tr>
        	<tr>
        		<td id="ProductStatistics" style="width:30%;" valign="top">&nbsp;</td>
        	</tr>
        	<tr>
        		<td>&nbsp;</td>
        	</tr>
        	
        </table>
        
		<!-- <a data-role="button" data-inline="true" data-theme="c" ><span id="SamplingReceivingTotal">Total: 0.00</span></a> -->
		
		<input type="hidden" id="DistributorID" name="DistributorID">
		<input type="hidden" id="NoLiquRequiredToggleBit" name="NoLiquRequiredToggleBit" value="0">
		<table border="0" style="font-size: 10pt; width:25%">
			<tr>
				<td style="width:30%" id="NoLiquidReqTd">
				<fieldset data-role="controlgroup">				   
				    <input type="checkbox" name="NoLiquReq" id="NoLiquReq" onClick="NoLiquRequired15();">
				    <label for="NoLiquReq">No empty returned</label>				    
				</fieldset>				
				</td>
			</tr>
		</table>
		
	</form>
	</li>
	</ul>
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
	<div>
		<table style="width: 100%;">
			<tr>
				<td <%if(!EmptyResultSet1){ %>class="ui-disabled"  <%} %>>
				
					<%if(EmptyResultSet1){ %>
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="DispatchReturnsSave" href="#" class="ui-disabled" onClick="DispatchReturnsSubmit();">Save</a>
                   <%} %>
                   
                   
                   <!--   <a data-icon="check" data-theme="b" data-role="button" data-inline="true" id="DeskSalePrint" href="DeskSalePrint.jsp?DeliveryID=<% if( isEditCase ){ out.print(EditID); }else if( isPrintCase ){ out.print(DeskSaleIDPrint); } %>" target="_blank" <% if( isEditCase == false && isPrintCase == false ){ %> class="ui-disabled" <% } %>  >Print</a> -->
                    
                    <!-- <a data-icon="check" data-theme="b" data-role="button" data-inline="true" id="DeskSalePrint" href="DeskSalePrint.jsp?DeliveryID=<% //if( isEditCase ){ out.print(EditID); }else if( isPrintCase ){ out.print(DeliveryIDPrint); } %>" target="_blank" <% //if( isEditCase == false && isPrintCase == false ){ %> class="ui-disabled" <% //} %> >Print</a> -->
                    
                    
                    <% if( isEditCase ) { %>
                    	<!-- <a data-icon="check" data-theme="b" data-role="button" data-inline="true" id="DeskSaleDelete" href="#"  onClick="DeskSaleDelete();">Delete</a> -->
                    <% } %>
				</td>
                <!-- <td align="right">
                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="DeskSaleSearch" >Search</a>
				</td> -->
			</tr>
		</table>
	</div>
	    	
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
    </jsp:include><!-- Include Outlet Search -->
</div>

</body>
</html>
<%

s.close();
c.close();
ds.dropConnection();

%>