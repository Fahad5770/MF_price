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

<jsp:useBean id="bean" class="com.pbc.inventory.DeliveryNote" scope="page"/>
<jsp:setProperty name="bean" property="*"/>


<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(34, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

String EditID = "0";
boolean isEditCase = false;

if( request.getParameter("DeliveryID") != null ){
	EditID = request.getParameter("DeliveryID");
	isEditCase = true;
}

String DeliveryIDPrint = "0";
boolean isPrintCase = false;

if( request.getParameter("DeliveryIDPrint") != null ){
	DeliveryIDPrint = request.getParameter("DeliveryIDPrint");
	isPrintCase = true;
}

Warehouse UserWarehouse[] = UserAccess.getUserFeatureWarehouse(SessionUserID, 349);

if( UserWarehouse == null ){
	//response.sendRedirect("AccessDenied.jsp");
}

%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
        <script src="js/lookups.js"></script>
        <script src="js/DeliveryNoteMoaiz.js?1112=1112"></script>
        
      
</head>
<style>

/*	.ui-select{
		width:200px !important;
	}
*/
</style>
<body>
<% 
String WarehouseName = "";


if( UserWarehouse != null ){
	WarehouseName = "Test";
}

%>
<div data-role="page" id="DeliveryNote" data-url="DeliveryNote" data-theme="d">

    <jsp:include page="Header3.jsp" >
    	<jsp:param value="Delivery Note" name="title"/>

    		<jsp:param value="" name="HeaderValue"/>

    </jsp:include>
    
    <div data-role="content" data-theme="d">
	

	
	
	<ul data-role="listview" data-inset="false" data-divider-theme="c">
	<li>
	<table border="0" style="width: 100%">
	
		
		<tr>
			
			
			<td >
				<input type="text" placeholder="Distributor ID" id="DeliveryNoteDistributorID2" onChange="getDistributorName()" name="DeliveryNoteDistributorID2"  data-mini="true" >
			</td>
			<td colspan="2">
				<input type="text"  placeholder="Distributor Name" id="DistributorName" name="DistributorName" readonly="readonly" data-mini="true" >
			</td>  
			<td style="padding-left:10px">
				<div id="DeliveryNoteDistributorName" ></div>
			</td>
            
		</tr>
		 
		
		<tr> 
			
			
			<td style="width: 15%;" >
				<input type="text" placeholder="Vehicle #" id="DeliveryNoteVehicleNo2" name="DeliveryNoteVehicleNo2" data-mini="true" onCha11nge="DeliveryNoteUpdateChangeStatus()">
			</td>
			
			<td style="width: 15%;">
				<input type="text" placeholder="Remarks" id="DeliveryNoteRemarks2" name="DeliveryNoteRemarks2" data-mini="true" on23Change="DeliveryNoteUpdateChangeStatus()">
			</td>
				<td></td> 
		</tr>
		
	
		
		
		</table>
	</li>
    
    <li data-role="list-divider">Products</li>
    <li>	
	<form id="DeliveryNoteMainForm"  >		
    
    <input type="hidden"  id="DeliveryNoteDistributorID" name="DeliveryNoteDistributorID"  data-mini="true" >
    <input type="hidden"  id="DeliveryNoteVehicleNo" name="DeliveryNoteVehicleNo" >
    <input type="hidden"  id="DeliveryNoteRemarks" name="DeliveryNoteRemarks" >
    <input type="hidden" name="UniqueVoucherID" id="UniqueVoucherID" value="<%=Utilities.getUniqueVoucherID(SessionUserID)%>"/>
    
    
     <table>
    <tr>
			<th>Package</th>
			<th>Brand</th>
			<th>Raw Cases</th>
			<th>Bottles</th>
			<th>Batch Codes</th>
		</tr>
		
		<%
		
		Datasource ds = new Datasource();
		ds.createConnection();
		Connection c = ds.getConnection();
		
		
		Statement s = c.createStatement();
		Statement s2 = c.createStatement();
		Statement s3 = c.createStatement();
		
		int i=1;
		ResultSet rs = s.executeQuery("SELECT * FROM inventory_products_view");
		while(rs.next()){
		
		
		%>
		
		
		<tr>
			<td>
				<input type="text" name="packagename" id="packagename" value="<%=rs.getString("package_label")%>" readonly/>
				<input type='hidden' name='DeliveryNoteMainFormUnitPerSKU' value='<%=rs.getLong("unit_per_sku")%>'>
				<input type='hidden' id='ProductCode' name='ProductCode' value='<%=rs.getLong("sap_code")%>'>
				<input type='hidden' name='ProductID' value='<%=rs.getLong("product_id")%>'>
				<input type='hidden' name='DeliveryNoteMainFormLiquidInML' value='<%=rs.getLong("liquid_in_ml")%>'>
				<%-- <input type='hidden' id='NumberOFRows' value='<%=i%>'>
				 --%>
				
			</td>
			<td><input type="text" name="brandname" id="brandname" value="<%=rs.getString("brand_label")%>" readonly/></td>
			<td><input type="text" name="DeliveryNoteMainFormRawCases" id="rawcases<%=i%>" /></td>
			<td><input type="text" name="DeliveryNoteMainFormUnits" id="bottles<%=i%>" /></td>
			<td><input type="text" name="DeliveryNoteMainFormBatchCode" id="batchcode<%=i%>" /></td>
		</tr>
		<%
		i++;
		}
		%>
		<tr>
			<td>
			
				<input type='hidden' id='NumberOFRows' value='<%=i%>'>
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
				<td>
					
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="DeliveryNoteSave" href="#"  <%if (true){%> onClick="DeliveryNoteSubmit();" <%}   %>>Save</a>
					
					<a data-icon="check" data-theme="b" data-role="button" data-inline="true" id="DeliveryNotePrint" href="DeliveryNotePrint.jsp?DeliveryID=<% if( isEditCase ){ out.print(EditID); }else if( isPrintCase ){ out.print(DeliveryIDPrint); } %>" target="_blank" <% if( isEditCase == false && isPrintCase == false ){ %> class="ui-disabled" <% } %>  >Print</a>
                    
                   
                    <button data-icon="check" data-theme="b" data-inline="true" id="DeliveryNoteReset" onClick="javascript:window.location='DeliveryNoteMoaiz.jsp'" >Reset</button>
                    
				</td>
                 <!--  <td align="right">
                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="DeliveryNoteSearch" >Search</a>
				</td> -->
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

</div>
<script type="text/javascript">

function getDistributorName()
{ 
	var DistributorID = document.getElementById("DeliveryNoteDistributorID2").value;
	$.ajax({
	    url: "common/GetDistributorInfoJson",
	    data: {
	    	DistributorID:DistributorID,
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.exists == "true"){
	    		$("#DistributorName").val(json.DistributorName);
	    		
	    	}else{
	    		alert("Invalid Distributor ID");
	    		$("#DistributorName").val('');
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
	}

</script> 
        
</body>
</html>
<%
bean.close();
%>