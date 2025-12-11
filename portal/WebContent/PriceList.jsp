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

if(Utilities.isAuthorized(51, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();
Statement s1 = ds.createStatement();

%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
        <script src="js/lookups.js"></script>
        <script src="js/PriceList.js?2=2"></script>
        
        
</head>

<body>

<div data-role="page" id="PriceList" data-url="PriceList" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Price List" name="title"/>

    		

    </jsp:include>
    
    <div data-role="content" data-theme="d">
	

	
	<form id="PriceListForm" data-ajax="false">	
	<ul data-role="listview" data-inset="false" data-divider-theme="c">
	<li>
	<input type="hidden" name="UserID" id="UserID" value="<%=SessionUserID%>"/>
	<input type="hidden" name="FeatureIDFOrWhole" id="FeatureIDFOrWhole" value="51"/>
	
	
	<table border="0" style="width: 100%">
	
		<tr>
			<td style="width: 25%;" >
				Price List Name
			</td>
			<td style="width: 25%;">
				Valid From 
			</td>
			<td style="width: 25%;">
			    Valid To
			</td>
			<td style="width: 25%;">
				Active
			</td>
			
            
		</tr>
		<tr>
		<%
		Long PriceList = Utilities.parseLong(request.getParameter("Plist"));
		String FromDate = "";
		String ToDate = "";
		String label = "";
		String ActiveSelected = "";
		String InactiveSelected = "";
		int isEdit =0;
		String ReadOnly ="";
		String Disabled = "";

		
		if(PriceList != 0) //edit case
		{
			if(PriceList ==1) //if global price list
			{			
				ReadOnly="readonly";
				Disabled="ui-disabled";
			}
			ResultSet rs2 = s.executeQuery("select * from inventory_price_list where id="+PriceList);
			if(rs2.next())
			{
				FromDate =  Utilities.getDisplayDateFormat(rs2.getDate("valid_from"));
				ToDate =  Utilities.getDisplayDateFormat(rs2.getDate("valid_to"));
				label = rs2.getString("label");
				isEdit=1;
				if (rs2.getInt("is_active") == 1){
					ActiveSelected = "selected";
				}else{
					InactiveSelected = "selected";
				}
			}
		}		
		%>
		<input type="hidden" name="isEditCase" id="isEditCase" value="<%=isEdit%>" />
		<input type="hidden" name="isEditSelectionCase" id="isEditSelectionCase" value="0"/>
		<input type="hidden" name="PriceListIDForWhole" id="PriceListIDForWhole" value="<%=PriceList%>"/>

				<td style="width: 25%;" >
					<input type="text" placeholder="" id="PriceListLabel" name="PriceListLabel" data-mini="true" value="<%=label%>" <%=ReadOnly%>>
					<input type="hidden" name="PriceListMasterTableID" id="PriceListMasterTableID" value="<%=PriceList%>" />
				</td>
				<td style="width: 25%;">
					<input type="text" placeholder="DD/MM/YYYY" id="PriceListValidFrom" name="PriceListValidFrom" data-mini="true" value="<%=FromDate%>" <%=ReadOnly%>>
				</td>
				<td style="width: 25%;">
					<input type="text" placeholder="DD/MM/YYYY" id="PriceListValidTo" name="PriceListValidTo" data-mini="true" value="<%=ToDate%>" <%=ReadOnly%>>
				</td>
				<td style="width: 25%;">
					<select id="PriceListIsActive" name="PriceListIsActive" data-mini="true" class="<%=Disabled %>">
					<option value="0" <%=InactiveSelected%>>Inactive</option>
					<option value="1" <%=ActiveSelected%>>Active</option>
					</select>
				</td>
            
		</tr>
		 
		
		</table>
	</li>
	 <li data-role="list-divider">Scope</li>
    <div id="PriceListDataScope">
	
	</div>
    <li data-role="list-divider">Product Price List</li>
    <li>	
		
    
    <table border="0" width="100%">
		<tr>
			<!-- <td>
				Product Type
			</td> -->
			<td>
				Product Code 
			</td>
			<td>
			    Package Name
			</td>
			<td>
				Brand Name
			</td>
			<td>
				Raw Case
			</td>
			<td>
				Bottle
			</td>
			
            
		</tr>
		
		<%		
		
			ResultSet rs = s.executeQuery("select * from inventory_products_view order by category_id,package_sort_order");
			int i=0;
			while(rs.next())
			{
				//System.out.println("PriceList Value "+PriceList);
				double EditRawCases=0;
				double EditUnits=0;
				double EditDiscount=0;
		
				if(PriceList!=0)
				{
					ResultSet rs1 = s1.executeQuery("SELECT id,product_id,raw_case,unit,discount FROM inventory_price_list_products where id="+PriceList+" and product_id="+Utilities.parseLong(rs.getString("product_id")));
					//System.out.println("SELECT id,product_id,raw_case,unit FROM pep.inventory_price_list_products where product_id="+Utilities.parseLong(rs.getString("product_id")));
					if(rs1.first())
					{
						 EditRawCases = rs1.getDouble("raw_case");
						 EditUnits = rs1.getDouble("unit");
						 EditDiscount = rs1.getDouble("discount");
					}	
				}
							
						
		%>
					<tr>
				
						<%-- 	<td valign="top" style="width:18%">
								<input type="text" placeholder="Product Type" id="PriceListProductType" name="PriceListProductType" data-mini="true" value="<%=rs.getString("category_label")%>" readonly="readonly" tabindex="-1">
								<input type="hidden" name="PriceListSubTableID" id="PriceListSubTableID" value="<%=rs.getString("category_id")%>"/>
							</td> --%>
							<td valign="top" style="width:18%">
								<input  type="text" placeholder="Product Code" id="PriceListSapCode" name="PriceListSapCode" data-mini="true" value="<%=rs.getString("sap_code")%>" readonly="readonly" tabindex="-1">
								<input  type="hidden" id="PriceListProductCode" name="PriceListProductCode"  value="<%=rs.getString("product_id")%>">
							</td>
							<td valign="top" style="width:12%">
								<input  type="text" placeholder="Package" id="PriceListPackageName" name="PriceListPackageName" data-mini="true" value="<%=rs.getString("package_label")%>" readonly="readonly" tabindex="-1">
							</td>
							<td valign="top" style="width:12%">
								<input  type="text" placeholder="Brand" id="PriceListBrandName" name="PriceListBrandName" data-mini="true" value="<%=rs.getString("brand_label")%>" readonly="readonly" tabindex="-1">
							</td>
							<td valign="top" style="width:12%">
								<input  type="text" placeholder="Raw Case" id="PriceListRawCase" name="PriceListRawCase" data-mini="true" value="<%=EditRawCases%>" onKeyup="FillBottles(<%=i%>,this)">
								<input  type="hidden" id="PriceListUPSku_<%=i %>" name="PriceListUPSku_<%=i %>"  value="<%=rs.getString("unit_per_sku")%>">
							</td>
							<td valign="top" style="width:12%">
								<input  type="text" placeholder="Bottle" id="PriceListBottle_<%=i %>" name="PriceListBottle" data-mini="true" tabindex="-1" readonly="readonly" value="<%=EditUnits%>">
							<%-- </td>
								<td valign="top" style="width:16%">
								<input  type="text" placeholder="Disount" id="discount_<%=i %>" name="PriceListDisount" data-mini="true" tabindex="-1" value="<%=EditDiscount%>">
							</td> --%>
				
					</tr>			
			<%
			i++;
			}
				%>
					

			<% 	
			%>
		   
		   
		   
		   
		  
		
		
	</table>
	

	
	</li>
	</ul>
	
	
	</form>
	
    </div><!-- /content -->
    <jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="DistributorSearchCallBackForUserRights" name="CallBack" />
    </jsp:include><!-- Include Distributor Search -->
    <div data-role="footer" data-position="fixed" data-theme="b">
    
	<div>
		<table style="width: 100%;">
			<tr>
				<td>
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="DeliveryNoteSave" href="#"  onClick="SavePriceList();">Save</a>
                    
                   
                    
                    <button data-icon="check" data-theme="b" data-inline="true" id="DeliveryNoteReset" onClick="javascript:window.location='PriceList.jsp'" >Reset</button>
                    
				</td>
                <td align="right">
                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="PriceListSearch" >Search</a>
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
		<ul data-role="listview" data-inset="true"> 
		<li data-role="list-divider">Price List</li>
		<%
			ResultSet rs1 = s.executeQuery("select * from inventory_price_list");
		while(rs1.next())
		{
        Date CreatedOn = rs1.getDate("created_on");
 		%>
 		<li><a data-ajax="false" href="#" onClick="LoadPerticularPriceList(<%=rs1.getString("id")%>)">
					<span style="font-size: 10pt; font-weight: 400;"><%=rs1.getString("label") %></span>
					<span class="ui-li-count"><%= Utilities.getDisplayDateTimeFormat(CreatedOn)%></span>
					
					</a>
 		</li>
 		<%
 		} 
 		%> 
 		</ul>          
        </div>
        <form data-ajax="false" action="PriceList.jsp" id="PriceListEditForm" method="POST">
        	<input type="hidden" name="Plist" id="PriceListEditID">
        </form>
    </div>
 
</div>

</body>
</html>
<%
s.close();
ds.dropConnection();


bean.close();
%>