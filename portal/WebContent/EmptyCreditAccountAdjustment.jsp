<%@page import="java.lang.reflect.Array"%>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.Date"%>
<%@page import="com.mysql.jdbc.Util"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<%@page import="com.pbc.common.Distributor"%>

<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 235;
if(Utilities.isAuthorized(235, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Warehouse UserWarehouse[] = UserAccess.getUserFeatureWarehouse(SessionUserID, FeatureID);

if( UserWarehouse == null || UserWarehouse.length > 1 ){
	response.sendRedirect("AccessDenied.jsp");
}

String EditID = "0";
boolean isEditCase = false;
int isEditHiddenFlag=1; //1 for insertion
 
String CreatedOnEditCase="";

long ReceiptFrmProMasterTableID = Utilities.parseLong(request.getParameter("ID")); //getting id for printing case

if( request.getParameter("ProductionReceiptID") != null ){
	EditID = request.getParameter("ProductionReceiptID");
	CreatedOnEditCase =	request.getParameter("CreatedOnEditCase");
	isEditCase = true;
	//System.out.println("Edit Id "+EditID);
	isEditHiddenFlag=2;//2 for edit
}
/*
Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, 89);

 
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

*/



Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

%>
<html>


<head>

		
		
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
        <script src="js/lookups.js"></script>
        <script src="js/EmptyCreditAccountAdjustment.js?111=111"></script>
        
       
		
<style>
#EmptyCreditReceiptType{

}
</style>        
        
</head>
<style>

/*	.ui-select{
		width:200px !important;
	}
*/
</style>
<body>

<div data-role="page" id="DispatchReturnsMainInner" data-url="DispatchReturnsMainInner" data-theme="d">



<jsp:include page="Header3.jsp" >
    	<jsp:param value="Empty Account Adjustment" name="title"/>

    		<jsp:param value="<%=UserWarehouse[0].WAREHOUSE_NAME%>" name="HeaderValue"/>

    </jsp:include>
    
    
    <div data-role="content" data-theme="d">
	
<%


 

%>
	
	
	<div class="ui-grid-a">
    	<div class="ui-block-a" style="width:40%">
    		<div class="ui-bar ui-1bar-e" style="min-height:60px;">
    		<table border="0" style="width:100%">
    			<tr>
    				<td style="width:50%;"  valign="top">
    					<ul data-role="listview" data-inset="true"  style="font-size:10pt; margin-top:-10px; margin-left:-8px" data-icon="false">
				    			<li data-role="list-divider" data-theme="a">Packages</li>
				    			<%
				    			ResultSet rs3 = s.executeQuery("SELECT DISTINCT package_id,package_label FROM inventory_products_view where category_id in(2,3,4) order by package_sort_order");
				    			while(rs3.next()){
				    			%>
				    			<li><a href="#" style="font-size:10pt; font-weight:normal;" onClick="LoadBrands(<%=rs3.getLong("package_id")%>)"><%=rs3.getString("package_label")%></a></li>
				    			<%} %>
				    		</ul>
    				
    				</td>
    				<td style="width:50%;" valign="top" id="ReceiptFromProdBrandULList">
    					
				    	
				    	
    				</td>
    			</tr>
    		</table>
    			
    		
    		
    		
    		
    		</div>
    	</div>
    <div class="ui-block-b"  style="width:60%">
    	<div class="ui-ba ui-ba1r-e" style="min-height:60px">
    		<ul data-role='listview' data-inset='true'  style='font-size:10pt; margin-top:-2px;margin-left:-8px' data-icon='false'>
    		<li data-role='list-divider' data-theme='a'>Empty Account Adjustment</li>
	<li>
	<table border="0" style="width: 100%">
	
		<%
		String VehicleName="";
		String SerialNo = "";
		long DistributorID=0;
		String DistributorName="";
		boolean EmptyResultSet1 = true;
		if(isEditCase){
			//ResultSet rs1 = s.executeQuery("select * from inventory_sales_dispatch_returned_products isdrp,inventory_products_view ipv where isdrp.product_id=ipv.product_id and is_empty=0 and dispatch_id="+DispatchID);
			ResultSet rs1 = s.executeQuery("SELECT *,(select name from common_distributors cd where cd.distributor_id=ecer.distributor_id) distributor_name FROM ec_empty_receipt ecer Where id="+EditID);
			
			while(rs1.next())
			{
				VehicleName = rs1.getString("vehicle_no");
				EmptyResultSet1 = false;
				SerialNo = rs1.getString("serial_no");
				DistributorID = rs1.getLong("distributor_id");
				DistributorName = rs1.getString("distributor_name");
			}	
		}else{
			//ResultSet rs_max = s.executeQuery("select (max(serial_no)+1) max_serial_no from ec_empty_receipt");
			//if(rs_max.first()){
				//SerialNo = rs_max.getString("max_serial_no");
			//}
		}
		
						%>
		<tr>
			<td><label for="DispatchReturnsVehicleNumber" style="margin:0px">Distributor ID</label></td>
		</tr>
		
		<tr>
        			
        			<td style="w1idth:10%">
        				<input type="text" name="EmptyCreditReceiptMainDistributor" data-mini="true" id="EmptyCreditReceiptMainDistributor" style="width: 100%" onChange="getDistributorName()" value="<%if(DistributorID!=0){ %><%=DistributorID%><%}%>">
        			</td>
        			<td style="w1idth:68%">
        				<input type="text" name="EmptyCreditReceiptMainDistributorName" data-mini="true" id="EmptyCreditReceiptMainDistributorName" readonly="readonly" value="<%=DistributorName%>">
        			</td>
        		</tr>
        		</table>
        		<table style="width: 100%" border=0>				
		<tr>
			<td style="width:30%"><label for="DispatchReturnsVehicleNumber" style="margin:0px">Vehicle#</label></td>
			<td style="width:20%"><label for="EmptyAccAdjustmentType" style="margin:0px">Adjustment Type</label></td>
			<td style="width:50%"><label for="EmptyAccAdjustmentReason" style="margin:0px">Reason</label></td>
			
		</tr>
		<tr>
			<td>
				<input type="text" placeholder="" id="DispatchReturnsVehicleNumber" name="DispatchReturnsVehicleNumber" data-mini="true" maxlength="10" value="<%=VehicleName%>" >
				
			</td>
			<td>
				<select name="EmptyAccAdjustmentType" id="EmptyAccAdjustmentType" data-mini="true">
					<option value="0">Select Adjustment Type</option>
					<%
					ResultSet rs6 = s.executeQuery("select * from ec_empty_adjustment_types");
					while(rs6.next()){
					
					%>
					<option value="<%=rs6.getLong("id")%>"><%=rs6.getString("label") %></option>
					<%
					}
					%>
				</select>
				
			</td>
			<td>
				<input type="text" placeholder="" id="EmptyAccAdjustmentReason" name="EmptyAccAdjustmentReason" data-mini="true" maxlength="100" >
				
			</td>
			
			
		</tr>
		 
		
		</table>
	</li>
    
    <li data-role="list-divider">Products</li>
    <li>	
	<form id="DispatchReturnsForm" data-ajax="false" action="" onSubmit="return DispatchReturnsAddProduct();">		
    
    <table style="width:800px;">
		<tr>
			<!-- <td valign="top" style="padding-top:5px">
				<a  href="#" data-role="button" data-icon="grid" data-iconpos="notext" data-theme="c" data-inline="true" data-corners="false">Arrow left</a>
			</td> -->
			<td valign="top" style="width:200px;">
				<select name="EmptyCreditReceiptType15" id="EmptyCreditReceiptType15" data-mini="true">
					<option value="0" style="min-width:100px;">Select Receipt Type</option>
					<%
					ResultSet rs61 = s.executeQuery("select * from ec_empty_receipt_types");
					while(rs61.next()){
					
					%>
					<option value="<%=rs61.getLong("id")%>"><%=rs61.getString("label") %></option>
					<%
					}
					%>
					
											
				</select>
			</td>
			<td valign="top" >
				<input type="text" placeholder="Product Code" id="DispatchReturnsProductCode" name="DispatchReturnsProductCode" data-mini="true" >
			</td>
			<td valign="top" >
				<input  type="text" placeholder="Raw Cases" id="DispatchReturnsRawCases" name="DispatchReturnsRawCases" data-mini="true">
			</td>
			<td valign="top" >
				<input  type="text" placeholder="Bottles" id="DispatchReturnsUnits" name="DispatchReturnsUnits" data-mini="true" >
			</td>
			
			
			<td valign="top" id="AddDispatchReturnsBtnTD"  >
			
			<!-- <td valign="top" id="AddDispatchReturnsBtnTD" > -->
				<input  type="submit" value="Add" data-inline="true" data-mini="true" data-icon="plus" >
			</td>
			<td><span id="ProductInfoSpan" style="padding-left:20px; font-size:10pt; font-family:Helvetica,Arial,sans-serif"></span></td>
		</tr>
	</table>
	</form>

	
    
    	<form action="test2.jsp" name="DispatchReturnMainForm" id="DispatchReturnMainForm">
    
		<input type="hidden" name="ReceiptFromProductionEditID" id="ReceiptFromProductionEditID" value="<%=EditID%>" >
		<input type="hidden" name="isEditCase" id="isEditCase" value="0"/>
		
		<input type="hidden" name="MainFormSerialNo" id="MainFormSerialNo" value="0"/>
		
		<input type="hidden" name="VehicleNameForTitle" id="VehicleNameForTitle" value="<%=VehicleName%>"/>
		<input type="hidden" name="TotalUnitsForSummar" id="TotalUnitsForSummar" value="0"/>
		<input type="hidden" name="DispatchReturnsVehicleNumberhidden" id="DispatchReturnsVehicleNumberhidden"/>
		<input type="hidden" name="isEditHiddenFlag" id="isEditHiddenFlag" value="<%=isEditHiddenFlag%>" />
		<input type="hidden" name="UniqueVoucherID" id="UniqueVoucherID" value="<%=Utilities.getUniqueVoucherID(SessionUserID)%>"/>
		<input type="Hidden" name="CreatedOnEditCaseMain" id="CreatedOnEditCaseMain" value="<%=CreatedOnEditCase%>"/>
		
		<input type="Hidden" name="EmptyCreditReceiptMainDistributorHidden" id="EmptyCreditReceiptMainDistributorHidden" value="<%=DistributorID%>"/>
		
        <input type="hidden" name="EmptyCreditReceiptIsEditValue" id="EmptyCreditReceiptIsEditValue" value="<%=isEditCase%>" />
        
        <input type="hidden" name="EmptyAccountAdjustmentTypeHidden" id="EmptyAccountAdjustmentTypeHidden" />
         <input type="hidden" name="EmptyAccountAdjustmentReasonHidden" id="EmptyAccountAdjustmentReasonHidden" />
        
         <input type="Hidden" name="EmptyCreditReceiptWarehouseIDHidden" id="EmptyCreditReceiptWarehouseIDHidden" value="<%=UserWarehouse[0].WAREHOUSE_ID%>"/>
        
        
        <table style="width:100%" border="0">
        	<tr>
        		<td style="width:100%" valign="top" rowspan="2">
	        		<table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt; width:100%">
					  <thead>
					    
					    <tr class="ui-bar-c">
							<th data-priority="1">Product Code</th>
							<th data-priority="1">Package</th>
							<th data-priority="1">Brand</th>				
							<th data-priority="1" >Raw Cases</th>
							<th data-priority="1" >Bottles</th>
							<th data-priority="1" >Receipt Type</th>
							
							<th data-priority="1">&nbsp;</th>
					    </tr>
					  </thead>
					  
						<tbody id="DispatchReturnsTableBody">
						
						<%
							boolean EmptyResultSet = true;
							ResultSet rs = s.executeQuery("select ipv.sap_code, ipv.unit_per_sku, ipv.package_label, ipv.brand_label, iprp.raw_cases, iprp.units, iprp.total_units, ipv.liquid_in_ml, iprp.product_id, ipv.package_id, ipv.brand_id,iprp.type_id,(select label from ec_empty_receipt_types ecert where ecert.id=iprp.type_id) label from ec_empty_receipt_products iprp, inventory_products_view ipv where iprp.product_id=ipv.product_id  and iprp.id="+EditID);
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
								<td><%=rs.getString("label") %><input type='hidden' name='EmptyCreditReceiptType' value='<%=rs.getInt("type_id") %>'></td>							
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
		
		
	</form>
	<form id="ReceiptFromProductionPrintingForm" method="post" action="EmptyCreditReceiptPrinting.jsp" target="_blank">
	<input type="hidden" name="ReceiptFromProductionID" id="ReceiptFromProductionID" value="<%=ReceiptFrmProMasterTableID%>"/>
	</form>
	
	</li>
	</ul>
    	</div>
    </div>
</div><!-- /grid-a -->
	
	
	
	
	
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
	<div>
		<table style="width: 100%; " border="0">
			<tr>
				 <td  id="DispatchReturnsSaveTD" style="width:30%">
				
					<table style="width:100%;">
					<tr>
						<td style="width:20%;">
							<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="DispatchReturnsSave" href="#" class="ui-disabled" onClick="ProductionReceiptSubmit();">Save</a>
						</td>
						
					</tr>
					</table>
					
                  
                   
				</td>
				
				
                 
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
                        <span id="FromDateSpan"><input type="text" data-mini="true" name="ReceiptFromProductionFromDate" id="ReceiptFromProductionFromDate" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateFormat(new java.util.Date())%>"></span>
                    
                    </td>
                    <td>
                    
						<span id="ToDateSpan"><input type="text" data-mini="true" name="ReceiptFromProductionToDate" id="ReceiptFromProductionToDate" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateFormat(new java.util.Date())%>" ></span>
                    
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
    <jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="DistributorSearchCallBack" name="CallBack" />
    	<jsp:param value="228" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Distributor Search -->
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