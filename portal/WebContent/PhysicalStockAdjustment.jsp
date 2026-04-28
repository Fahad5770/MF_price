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
int FeatureID = 79;

Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
String DistributorName = "";
long DistributorID = 0;

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}else if( UserDistributor == null ){
	response.sendRedirect("AccessDenied.jsp");
}

long EditID = 0;
EditID = Utilities.parseLong(request.getParameter("PhysicalStockAdjustmentEditID"));
boolean isEditCase = false;

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

%>
<html>

<head>
		
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
        <script src="js/lookups.js"></script>
        <script src="js/PhysicalStockAdjustment.js?3=3"></script>
        <script type="text/javascript">
        
        <% if(EditID > 0){
        	%>
        	PhysicalStockAdjustmentGetEditInfoJson(<%=EditID%>);
        	<%
        } %>
        
        </script>
</head>
<style>

/*	.ui-select{
		width:200px !important;
	}
*/
</style>
<body>

<div data-role="page" id="PhysicalStockAdjustment" data-url="PhysicalStockAdjustment" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Physical Stock Adjustment" name="title"/>
    </jsp:include>
    
    <div data-role="content" data-theme="d">
	
	<ul data-role="listview" data-inset="false" data-divider-theme="c">
		<li>
			<table style="width: 20%">
				<tr>
					<td>
						<select id="PhysicalStockAdjustmentDistributor" name="PhysicalStockAdjustmentDistributor" data-mini="true" onChange="CheckPendingDispatch()">
							<option value="0">Select Distributor</option>
							
							<%
							for(int i = 0; i < UserDistributor.length; i++){
								%>
									<option value="<%=UserDistributor[i].DISTRIBUTOR_ID%>"><%=UserDistributor[i].DISTRIBUTOR_ID%> - <%=UserDistributor[i].DISTRIBUTOR_NAME%></option>
								<%
							}
							%>
							
						</select>
					</td>
				</tr>
			</table>
		</li>
    <li data-role="list-divider">Products</li>
    <li>
	<form id="PhysicalStockAdjustmentForm" data-ajax="false" action="" onSubmit="return PhysicalStockAdjustmentAddProduct();">		
    
    <table border="0">
		<tr>
			<!-- <td valign="top" style="padding-top:5px">
				<a  href="#" data-role="button" data-icon="grid" data-iconpos="notext" data-theme="c" data-inline="true" data-corners="false">Arrow left</a>
			</td> -->
			<td valign="top" style="width:10%">
				<input type="text" placeholder="Product Code" id="PhysicalStockAdjustmentProductCode" name="PhysicalStockAdjustmentProductCode" data-mini="true" onBlur="getProductInfoJson(this.value)" >
			</td>
			<td valign="top" style="width:10%">
				<input  type="text" placeholder="Raw Cases" id="PhysicalStockAdjustmentRawCases" name="PhysicalStockAdjustmentRawCases" data-mini="true">
			</td>
			<td valign="top" style="width:10%">
				<input  type="text" placeholder="Bottles" id="PhysicalStockAdjustmentUnits" name="PhysicalStockAdjustmentUnits" data-mini="true" >
			</td>
			
			
			<td valign="top" id="AddPhysicalStockAdjustmentBtnTD" style="width:5%"  >
			
			<!-- <td valign="top" id="AddDispatchReturnsBtnTD" > -->
				<input  type="submit" value="Add" data-inline="true" data-mini="true" data-icon="plus" >
			</td>
			<td><span id="ProductInfoSpan" style="padding-left:20px; font-size:10pt; font-family:Helvetica,Arial,sans-serif"></span></td>
		</tr>
	</table>
	</form>

	<form action="test2.jsp" name="PhysicalStockAdjustmentMainForm" id="PhysicalStockAdjustmentMainForm" data-ajax="false">
    
		<input type="hidden" name="PhysicalStockAdjustmentEditID" id="PhysicalStockAdjustmentEditID" value="<%=EditID%>" >
		<input type="hidden" name="PhysicalStockAdjustmentDistributorID" id="PhysicalStockAdjustmentDistributorID" value="" >
		<input type="hidden" name="RowMaxID" id="RowMaxID" value="0" >
		
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
							<th data-priority="1">Raw Cases</th>
							<th data-priority="1">Bottles</th>
							
							<th data-priority="1">&nbsp;</th>
					    </tr>
					  </thead>
					  
						<tbody id="PhysicalStockAdjustmentTableBody">
						
							<tr id="NoProductRow">
								<td colspan="10" style="margin: 1px; padding: 0px;">
									<div style="width: 100%; background-color: #FFFFFF; padding: 5px;">No products added.</div>
								</td>
							</tr>	
						
						</tbody>
					</table>
        		</td>
        		
        	</tr>
        	
        	<tr>
        		<td>&nbsp;</td>
        	</tr>
        	
        </table>
        
		<!-- <a data-role="button" data-inline="true" data-theme="c" ><span id="SamplingReceivingTotal">Total: 0.00</span></a> -->
		
	</form>
	</li>
	</ul>
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
		<div>
			<table style="width: 100%;">
				<tr>
					 <td id="PhysicalStockAdjustmentSaveTD">
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="PhysicalStockAdjustmentSave" href="#" class="ui-disabled" onClick="PhysicalStockAdjustmentSubmit();">Save</a>
						<a data-icon="check" data-theme="b" data-role="button" data-inline="true" id="PhysicalStockAdjustmentReset" href="#" onClick="PhysicalStockAdjustmentReset();">Reset</a>
					</td>
					<td align="right">
                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="PhysicalStockAdjustmentSearch" >Search</a>
				</td>
				</tr>
			</table>
		</div>
    </div>
    
    
    <div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:600px; max-height: 600px" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >

			<form data-ajax="false" id="DeliveryNoteFormDateRange" onSubmit="return showSearchContent()">
            <table>
            	<tr>
                	<td>

                        <span id="FromDateSpan"><input type="text" data-mini="true" name="DeliveryNoteFromDate" id="DeliveryNoteFromDate" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateFormat(new java.util.Date())%>"></span>
                    
                    </td>
                    <td>
                    
						<span id="ToDateSpan"><input type="text" data-mini="true" name="DeliveryNoteToDate" id="DeliveryNoteToDate" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateFormat(new java.util.Date())%>" ></span>
                    
                    </td>
                    <td>
                    	<button data-role="button" data-icon="search" id="DeliveryNoteDateButton" data-iconpos="notext" data-theme="c" data-inline="true" data-corners="false" onClick="showSearchContent();"></button>
                    </td>
                </tr>
            </table>
        </form>

        <div id="SearchContent">
        </div>
            
        </div>
    </div>
	
	<jsp:include page="LookupProductSearchPopup.jsp" >
    	<jsp:param value="ProductSearchCallBack" name="CallBack" />
    </jsp:include><!-- 
    Include Product Search -->


</body>
</html>
<%

s.close();
c.close();
ds.dropConnection();

%>