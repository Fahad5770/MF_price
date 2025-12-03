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

 if(Utilities.isAuthorized(431, SessionUserID) == false){
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
        <script src="js/BulkDiscount.js?12=11212"></script>
        
        
</head>
<script type="text/javascript">
function Add(ii,product_id){
	
	var Str = "<tr>"
				+ "<td><input  type='hidden' id='BulkDiscountProductCode' name='BulkDiscountProductCode'  value='"+product_id+"'></td><td></td><td></td><td></td>"
				+ "<td valign='top' style='width:16%' ><input class='ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-d ui-mini' type='text' placeholder='Raw Case' id='BulkDiscountRawCase' name='BulkDiscountRawCase' data-mini='true' value=''></td>"
				+ "<td valign='top' style='width:16%'><input class='ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-d ui-mini'  type='text' placeholder='Raw Case' id='BulkDiscountBottle' name='BulkDiscountBottle' data-mini='true' tabindex=''-1'  value=''></td>"
				+ "<td valign='top' style='width:16%'><input class='ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-d ui-mini' type='text' placeholder='Discount'id='Discountper' name='Discountper' data-mini='true' tabindex='0' value=''></td>";
				+ "</tr>"
		$("#MyTable"+ii).append(Str);

}

</script>
<body>

<div data-role="page" id="BulkDiscount" data-url="BulkDiscount" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Bulk Discount" name="title"/>

    		

    </jsp:include>
    
    <div data-role="content" data-theme="d">
	

	
	<form id="BulkDiscountForm" data-ajax="false">	
	<ul data-role="listview" data-inset="false" data-divider-theme="c">
	<li>
	<input type="hidden" name="UserID" id="UserID" value="<%=SessionUserID%>"/>
	<input type="hidden" name="FeatureIDFOrWhole" id="FeatureIDFOrWhole" value="51"/>
	
	
	<table border="0" style="width: 100%">
	
		<tr>
			<td style="width: 25%;" >
				Bulk Discount Name
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
		Long BulkDiscount = Utilities.parseLong(request.getParameter("Plist"));
		System.out.println("BulkDiscount   ="+ BulkDiscount);
		Date todaydate = new Date(); 
		 
		String FromDate =Utilities.getDisplayDateFormat(todaydate); 
		String ToDate = Utilities.getDisplayDateFormat(todaydate); 
		String label = "";
		String ActiveSelected = "";
		String InactiveSelected = "";
		int isEdit =0;
		String ReadOnly ="";
		String Disabled = "";
		String PciChannelID = "";
		if(BulkDiscount != 0) //edit case 
		{
			if(BulkDiscount ==1) //if global price list
			{			
				ReadOnly="readonly";
				Disabled="ui-disabled";
			}
			System.out.println("select * from inventory_hand_to_hand_discount where id="+BulkDiscount);
			ResultSet rs2 = s.executeQuery("select * from inventory_hand_to_hand_discount where id="+BulkDiscount);
			if(rs2.next())
			{
				FromDate =  Utilities.getDisplayDateFormat(rs2.getDate("valid_from"));
				ToDate =  Utilities.getDisplayDateFormat(rs2.getDate("valid_to"));
				PciChannelID = rs2.getString("pci_channel_id");

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
		<input type="hidden" name="BulkDiscountIDForWhole" id="BulkDiscountIDForWhole" value="<%=BulkDiscount%>"/>
		<input type="hidden" name="PciChannelID" id="PciChannelID" value="<%=PciChannelID%>"/>

				<td style="width: 25%;" >
					<input type="text" placeholder="" id="BulkDiscountLabel" name="BulkDiscountLabel" data-mini="true" value="<%=label%>" <%=ReadOnly%>>
					<input type="hidden" name="BulkDiscountMasterTableID" id="BulkDiscountMasterTableID" value="<%=BulkDiscount%>" />
				</td>
				<td style="width: 25%;">
					<input type="text" placeholder="DD/MM/YYYY" id="BulkDiscountValidFrom" name="BulkDiscountValidFrom" data-mini="true" value="<%=FromDate%>" <%=ReadOnly%>>
				</td>
				<td style="width: 25%;">
					<input type="text" placeholder="DD/MM/YYYY" id="BulkDiscountValidTo" name="BulkDiscountValidTo" data-mini="true" value="<%=ToDate%>" <%=ReadOnly%>>
				</td>
				<td style="width: 25%;">
					<select id="BulkDiscountIsActive" name="BulkDiscountIsActive" data-mini="true" class="<%=Disabled %>">
					<option value="0" <%=InactiveSelected%>>Inactive</option>
					<option value="1" <%=ActiveSelected%>>Active</option>
					</select>
				</td>
            
		</tr>
		 
		
		</table>
	</li>
	 <li data-role="list-divider">Scope</li>
    <div id="BulkDiscountDataScope">
	
	</div>
    <li data-role="list-divider">Product Price List</li>
    <li>	
		
    
    <table border="0" width="100%" id="MyTable23">
		<tr>
			<td>
				Product Type
			</td>
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
				From Quantity
			</td>
			<td>
				To Quantity
			</td>
			<td>
				Discount%
			</td>
			
            
		</tr>
		
		<%		
		
			ResultSet rs = s.executeQuery("select * from inventory_products_view order by category_id,package_sort_order");
			int i=0;
			double EditRawCases=0;
			double EditUnits=0;
	        double Editdiscount=0;
	        
			while(rs.next())
			{
				%>
				
				<tr>	
					<td valign="top" style="width:18%">
						<input type="text" placeholder="Product Type" id="BulkDiscountProductType" name="BulkDiscountProductType" data-mini="true" value="<%=rs.getString("category_label")%>" readonly="readonly" tabindex="-1">
						<input type="hidden" name="BulkDiscountSubTableID" id="BulkDiscountSubTableID" value="<%=rs.getString("category_id")%>"/>
					</td>
					<td valign="top" style="width:18%">
						<input  type="text" placeholder="Product Code" id="BulkDiscountSapCode" name="BulkDiscountSapCode" data-mini="true" value="<%=rs.getString("sap_code")%>" readonly="readonly" tabindex="-1">
						<input  type="hidden" id="BulkDiscountProductCode" name="BulkDiscountProductCode"  value="<%=rs.getString("product_id")%>">
					</td>
					<td valign="top" style="width:16%">
						<input  type="text" placeholder="Package" id="BulkDiscountPackageName" name="BulkDiscountPackageName" data-mini="true" value="<%=rs.getString("package_label")%>" readonly="readonly" tabindex="-1">
						</td>
					<td valign="top" style="width:16%">
						<input  type="text" placeholder="Brand" id="BulkDiscountBrandName" name="BulkDiscountBrandName" data-mini="true" value="<%=rs.getString("brand_label")%>" readonly="readonly" tabindex="-1">
					</td>
					
	
			
		<% 
			int noProdCnt=0;
				if(BulkDiscount!=0)
				{
					int cnt=0;
					ResultSet rs1 = s1.executeQuery("SELECT from_qty,to_qty, discount_percentage  FROM inventory_hand_to_hand_discount_products where hand_discount_id="+BulkDiscount+" and product_id="+Utilities.parseLong(rs.getString("product_id")));
					//System.out.println("SELECT id,product_id, default_discount raw_case,maximum_discount unit FROM inventory_spot_discount_products where id="+SpotDiscount+" and product_id="+Utilities.parseLong(rs.getString("product_id")));
					while(rs1.next())
					{
						 noProdCnt++;
						 EditRawCases = rs1.getDouble("from_qty"); 	
						 EditUnits = rs1.getDouble("to_qty");
						 Editdiscount = rs1.getDouble("discount_percentage");
						 cnt++;
						 if (cnt==1){
						 %>
					
						  <td valign="top" style="width:16%">
							<input  type="text" placeholder="Raw Case" id="BulkDiscountRawCase" name="BulkDiscountRawCase" data-mini="true" value="<%=EditRawCases%>" onKeyup="FillBottles(<%=i%>,this)">
							<input  type="hidden" id="BulkDiscountUPSku_<%=i %>" name="BulkDiscountUPSku_<%=i %>"  value="<%=rs.getString("unit_per_sku")%>">
						</td>
						<td valign="top" style="width:16%">
							<input  type="text" placeholder="Raw Case" id="BulkDiscountBottle_<%=i %>" name="BulkDiscountBottle" data-mini="true" tabindex="-1"  value="<%=EditUnits%>">
						</td>
							<td valign="top" style="width:16%">
							<input  type="text" placeholder="Discount" id="Discountper_<%=i %>" name="Discountper" data-mini="true" tabindex="0" value="<%=Editdiscount%>">
						</td> 
							<td style="width:30%"><a href="#" data-role="button" data-icon="plus" data-iconpos="left" data-inline="true" onClick="Add(<%=i%>,<%=rs.getString("product_id")%>)" data-mini="true">Add</a></td>
						
						</tr>
						<% 
						 }else if(cnt>1){
							%>
							 <tr>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
				
						  <td valign="top" style="width:16%">
						<input  type="hidden" id="BulkDiscountProductCode" name="BulkDiscountProductCode"  value="<%=rs.getString("product_id")%>">
						 
							<input  type="text" placeholder="Raw Case" id="BulkDiscountRawCase" name="BulkDiscountRawCase" data-mini="true" value="<%=EditRawCases%>" onKeyup="FillBottles(<%=i%>,this)">
							<input  type="hidden" id="BulkDiscountUPSku_<%=i %>" name="BulkDiscountUPSku_<%=i %>"  value="<%=rs.getString("unit_per_sku")%>">
						</td>
						<td valign="top" style="width:16%">
							<input  type="text" placeholder="Raw Case" id="BulkDiscountBottle_<%=i %>" name="BulkDiscountBottle" data-mini="true" tabindex="-1"  value="<%=EditUnits%>">
						</td>
							<td valign="top" style="width:16%">
							<input  type="text" placeholder="Discount" id="Discountper_<%=i %>" name="Discountper" data-mini="true" tabindex="0" value="<%=Editdiscount%>">
						</td> 
						
						</tr>
							 <%
						 }
					}
					if(1==2){
						%>
						
						<td valign="top" style="width:16%">
								<input  type="text" placeholder="Raw Case" id="BulkDiscountRawCase" name="BulkDiscountRawCase" data-mini="true" value="<%=EditRawCases%>" onKeyup="FillBottles(<%=i%>,this)">
								<input  type="hidden" id="BulkDiscountUPSku_<%=i %>" name="BulkDiscountUPSku_<%=i %>"  value="<%=rs.getString("unit_per_sku")%>">
							</td>
							<td valign="top" style="width:16%">
								<input  type="text" placeholder="Raw Case" id="BulkDiscountBottle_<%=i %>" name="BulkDiscountBottle" data-mini="true" tabindex="-1"  value="<%=EditUnits%>">
							</td>
								<td valign="top" style="width:16%">
								<input  type="text" placeholder="Discount" id="Discountper_<%=i %>" name="Discountper" data-mini="true" tabindex="0" value="<%=Editdiscount%>">
							</td>
							
							</tr>
							<%
					}
				}
				 System.out.println("noProdCnt"+noProdCnt);

					if(noProdCnt==0){
					 %>
					<td valign="top" style="width:16%">
							<input  type="text" placeholder="Raw Case" id="BulkDiscountRawCase" name="BulkDiscountRawCase" data-mini="true" value="" onKeyup="FillBottles(<%=i%>,this)">
							
						</td><td valign="top" style="width:16%">
							<input  type="text" placeholder="Raw Case" id="BulkDiscountBottle" name="BulkDiscountBottle" data-mini="true" tabindex="-1"  value="">
						</td>
							<td valign="top" style="width:16%">
							<input  type="text" placeholder="Discount" id="Discountper" name="Discountper" data-mini="true" tabindex="0" value="">
						</td>
						<td style="width:30%"><a href="#" data-role="button" data-icon="plus" data-iconpos="left" data-inline="true" onClick="Add(<%=i%>,<%=rs.getString("product_id")%>)" data-mini="true">Add</a></td>
						</tr>
						<tbody id="MyTable<%=i%>">
        </tbody>
						
				
						<%}
				
							
						
		%>
				
			<%
			i++;
			}
				%>
					

	
		   
		   
		   
		  
		
		
	</table>
	
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
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="DeliveryNoteSave" href="#"  onClick="SaveBulkDiscount();">Save</a>
                    
                   
                    
                    <button data-icon="check" data-theme="b" data-inline="true" id="DeliveryNoteReset" onClick="javascript:window.location='BulkDiscount.jsp'" >Reset</button>
                    
				</td>
              <td align="right">
                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id=SpotDiscountSearch" >Search</a>
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
		<li data-role="list-divider">Bulk Discount</li>
		<%
			ResultSet rs1 = s.executeQuery("select * from inventory_hand_to_hand_discount");
		while(rs1.next())
		{
        Date CreatedOn = rs1.getDate("created_on"); 
 		%>
 		<li><a data-ajax="false" href="#" onClick="LoadPerticularBulkDiscount2(<%=rs1.getString("id")%>)">
					<span style="font-size: 10pt; font-weight: 400;"><%=rs1.getString("label") %></span>
				 	<span class="ui-li-count"><%= Utilities.getDisplayDateTimeFormat(CreatedOn)%></span>
				 	
					
					</a>
 		</li>
 		<%
 		} 
 		%> 
 		</ul>          
        </div>
        <form data-ajax="false" action="BulkDiscount.jsp" id="BulkDiscountEditForm" method="POST">
        	<input type="hidden" name="Plist" id="BulkDiscountEditID">
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