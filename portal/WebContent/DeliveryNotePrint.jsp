<%@page import="com.pbc.util.NumberToWords"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="org.apache.commons.lang3.text.WordUtils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>

<style>

.formattedRow{
	border-bottom:1px solid #000;
	padding-top:5px;
	padding-bottom:5px;

}

</style>

<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

/*if(Utilities.isAuthorized(34, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}*/

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();


long DeliveryID = 0;
if( request.getParameter("DeliveryID") != null ){
	DeliveryID = Utilities.parseLong(request.getParameter("DeliveryID"));
}

boolean isBarcode = false;
String Barcode = "";
if( request.getParameter("Barcode") != null ){
	Barcode = Utilities.filterString(request.getParameter("Barcode"), 0, 100);
	
	if( Barcode.length() > 0 ){
		isBarcode = true;
	}
	
}
String Document = "";
if( request.getParameter("Document") != null ){
	Document = Utilities.filterString(request.getParameter("Document"), 1, 100);
}


Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
%>
<html>

<head>

</head>
<body style="font-family:Helvetica,Arial,sans-serif">
		  <%
		  
		  String SQLMain = "";
		  boolean isFound = false;
		  
		  if( isBarcode ){
				SQLMain = "SELECT created_on, distributor_id, remarks, vehicle_no, sap_order_no, distributor_id as distributor_id_val, (SELECT name FROM common_distributors where distributor_id=distributor_id_val) distributor_name, barcode, delivery_id, is_delivered, is_received, created_by, (SELECT DISPLAY_NAME FROM users where id=created_by) user_name, payment_method, (select label from inventory_delivery_note_payment_methods where id=payment_method) payment_method_label, invoice_no, vehicle_type, (select label from inventory_delivery_note_vehicle_types where id=vehicle_type) vehichle_type_label FROM inventory_delivery_note where barcode="+Barcode;
		  }else{
			  SQLMain = "SELECT created_on, distributor_id, remarks, vehicle_no, sap_order_no, distributor_id as distributor_id_val, (SELECT name FROM common_distributors where distributor_id=distributor_id_val) distributor_name, barcode, delivery_id, is_delivered, is_received, created_by, (SELECT DISPLAY_NAME FROM users where id=created_by) user_name, payment_method, (select label from inventory_delivery_note_payment_methods where id=payment_method) payment_method_label, invoice_no, vehicle_type, (select label from inventory_delivery_note_vehicle_types where id=vehicle_type) vehichle_type_label FROM inventory_delivery_note where delivery_id="+DeliveryID;
		  }
		  
		  //System.out.println(SQLMain);
		  
		  ResultSet rs = s.executeQuery(SQLMain);
		  while (rs.next()){
			  
			  if( Document.equals("GatePass") && rs.getInt("is_delivered") != 0 ){
				  %>
				  <p>Document already delivered.</p>
				  <script type="text/javascript">setSubmitButton(false);</script>
				  <%
				  break;
			  }
			  
			  if( Document.equals("GRN") && rs.getInt("is_received") != 0 ){
				  %>
				  <p>Document already received.</p>
				  <script type="text/javascript">setSubmitButton(false);</script>
				  <%
				  break;
			  }
			  
			  isFound = true;
			  
			  Date CreatedOn = rs.getTimestamp("created_on");
			  long DistributorID = rs.getLong("distributor_id");
			  String DistributorName = rs.getString("distributor_name");
			  String SAPOrderNo = rs.getString("sap_order_no");
			  String VehicleNo = rs.getString("vehicle_no");
			  String Remarks = rs.getString("remarks");
			  long barcode = rs.getLong("barcode");
			  String UserName = rs.getString("user_name");
			  String PaymentMethod = rs.getString("payment_method_label");
			  long InvoiceNo = rs.getLong("invoice_no");
			  String VehicleType = rs.getString("vehichle_type_label");
			  %>
              
              
              
			  <div style="width: 8.27in; border: solid 1px; margin-top: 5px; ">
              
              <table>
              	<tr>
                	<td align="left"><img src="images/logo.svg" style="width: 30px"></td>
                    <td valign="middle" style="font-weight:600"><%=Utilities.getCompanyName()%></td>
                    
                </tr>
              </table>
              
			  	<table style="width: 100%; margin-top: 5px; padding-left: 20px;padding-right: 20px;">
                <tr>
                	<td style="width: 20%;"></td>
                    <td style="text-align: center;font-weight: 700;">DELIVERY NOTE</td>
                    <td style="width: 20%;text-align: right;"><%= Utilities.getDisplayDateTimeFormat(CreatedOn) %></td>
                </tr>
                
                </table>
			  	<hr />
			  	<table style="width: 100%;  margin: 20px;" border="0">
			  		
			  		<tr style="font-weight: 700;">
				  		
				  		<td colspan="3">
				  			Distributor
				  		</td>
				  		<td rowspan="2" align="right" style="padding-right: 18px">
			  				<img src="/barbecue/barcode?data=<%=barcode%>&height=50&type=Code128&headless=false&drawText=yes"><br>
			  			</td>
			  		</tr>
			  		<tr>
				  		
				  		<td colspan="3">
				  		<%= DistributorID + " - " + DistributorName %>
				  		</td>
			  		</tr>
                    
                    
                    <tr style="font-weight: 700;">
				  		
				  		<td colspan="3">
				  			Payment Method
				  		</td>
				  		<td style="text-align: right; padding-right: 103px;">
				  			<%=barcode%>
				  		</td>
				  		
			  		</tr>
			  		<tr>
				  		
				  		<td colspan="4">
				  		<%=PaymentMethod%>
				  		</td>
			  		</tr>
                    
                    <tr style="font-weight: 700;">
				  		<td>
				  			Sale Order #
				  		</td>
				  		<td>
				  			Invoice #
				  		</td>
				  		
				  		<td>
				  			Vehicle Type
				  		</td>
				  		<td>
				  			Vehicle #
				  		</td>
				  		
				  		
			  		</tr>
			  		<tr>
				  		<td>
				  		<% 
							if (SAPOrderNo !=null &&  !SAPOrderNo.isEmpty()){
								out.print(SAPOrderNo);
							}
						
						%>
				  		</td>
				  		<td>
				  			<%=InvoiceNo%>
				  		</td>
				  		
				  		<td nowrap="nowrap">
				  			<%=VehicleType%>
				  		</td>
				  		<td>
					  		<%
								if (VehicleNo !=null &&  !VehicleNo.isEmpty()){
									out.print(VehicleNo);
								}
							
							%>
				  		</td>
				  		
			  		</tr>
                    
                    <tr style="font-weight: 700;">
				  		<td style="width: 25%" colspan="4">
				  			Remarks
				  		</td>
				  		
			  		</tr>
			  		<tr>
				  		<td colspan="4">
				  		<%
							if (Remarks !=null &&  !Remarks.isEmpty()){
								out.print(Remarks);
							}
						
						%>
				  		</td>
				  		
			  		</tr>
                    
			  	</table>
			  	<table style="width: 100%;  padding-left: 20px;padding-right: 20px;" border="0" cellpadding="0" cellspacing="0">
			  		<tr style="font-weight: 700;">
			  			<td class="formattedRow" nowrap>Product Code</td>
			  			<td class="formattedRow" nowrap>Package</td>
			  			<td class="formattedRow" nowrap>Brand</td>
			  			<td class="formattedRow" nowrap style="text-align: right;">Raw Cases</td>
			  			<td class="formattedRow" nowrap style="text-align: right;">Bottles</td>
			  			<td class="formattedRow" nowrap style="text-align: right; width:20%">Batch Code</td>
			  		</tr>
			  	<%
			  	int PreviousCategoryID = 0;
			  	
			  	s3.executeUpdate("create temporary table temp_delivery_note_products (category_id int(1), package_id int(11), brand_id int(11), package_name varchar(45), brand_name varchar(45), product_sap_code int(11), raw_cases int(11), units int(11))");
			  	
				//ResultSet rs2 = s2.executeQuery("SELECT product_id, raw_cases, (select package_id from inventory_products where id=product_id) package_id, (select brand_id from inventory_products where id=product_id) brand_id, (select sap_code from inventory_products where id=product_id) sap_code, (SELECT label FROM inventory_packages where id=package_id) package_name, (SELECT label FROM inventory_brands where id=brand_id) brand_name, units, batch_code FROM inventory_delivery_note_products where delivery_id="+rs.getLong(8)+" order by package_id, brand_id");
			  	ResultSet rs2 = s2.executeQuery("SELECT product_id, raw_cases, units, batch_code, (select sap_code from inventory_products where id=product_id) sap_code, (select package_id from inventory_products where id=product_id) package_id, (select brand_id from inventory_products where id=product_id) brand_id, ip.category_id, (SELECT label FROM inventory_packages where id=package_id) package_name, (SELECT label FROM inventory_brands where id=brand_id) brand_name, (select label from inventory_categories where id=ip.category_id) category_name, (select sort_order from inventory_packages where id=package_id) package_sort_order FROM inventory_delivery_note_products idnp, inventory_products ip where idnp.product_id = ip.id and idnp.delivery_id="+rs.getLong("delivery_id")+" order by ip.category_id, package_sort_order, ip.brand_id");
				while (rs2.next()){
					int PackageID = rs2.getInt("package_id");
					int BrandID = rs2.getInt("brand_id");
					String Package = Utilities.filterString(rs2.getString("package_name"), 2, 100);
					String Brand = Utilities.filterString(rs2.getString("brand_name"), 2, 100);
					long ProductCode = rs2.getLong("sap_code");
					int RawCases = rs2.getInt("raw_cases");
					int Units = rs2.getInt("units");
					String BatchCode = rs2.getString("batch_code");
					String CategoryName = rs2.getString("category_name");
					int CategoryID = rs2.getInt("category_id");
					
					s3.executeUpdate("insert into temp_delivery_note_products (category_id, package_id, brand_id, package_name, brand_name, product_sap_code, raw_cases, units) values ("+CategoryID+", "+PackageID+", "+BrandID+", '"+Package+"', '"+Brand+"', "+ProductCode+", "+RawCases+", "+Units+")");
					
			  	%>
			  	
			  		<% if( CategoryID != PreviousCategoryID ){ %>
			  	
					  		<tr>
					  			<td colspan="7"  style="font-weight: bold; border-bottom: 1px solid #000; padding-top:5px; padding-bottom:5px; text-align: left;"><%=CategoryName%></td>
					  		</tr>
			  		
			  		<%
			  				PreviousCategoryID = CategoryID;
			  			} 
			  		%>
			  		
			  		<tr>
			  			<td class="formattedRow"><%= ProductCode %></td>
			  			<td class="formattedRow"><%= Package %></td>
			  			<td class="formattedRow"><%= Brand %></td>
			  			<td class="formattedRow" style="text-align: right; padding-right:5px;"><%= RawCases %></td>
			  			<td class="formattedRow" style="text-align: right; padding-right:5px;"><%= Units %></td>
			  			<td class="formattedRow" style="text-align: right; padding-right:5px;"><%= BatchCode %></td>
			  		</tr>
			  	<%
				}
				
				
				
				rs2.close();
			  	%>			  	
			  	</table>
                
                <table style="width: 100%; margin-top: 5px; padding-left: 20px;padding-right: 20px;padding-top: 20px;"><tr><td style="width: 40%;font-weight: 700;">&nbsp;</td><td style="text-align: center;font-weight: 700;"></td><td style="width: 20%; text-align: right;" nowrap="nowrap"><%= "User: "+UserName %></td></tr></table>
                
			  </div>
			  
			  <div style="width: 8.27in; border: solid 1px; margin-top: 5px; padding-bottom:10px">
			  	<div style="text-align: center;font-weight: 700; background:#cecece; padding-top:5px; padding-bottom:5px">SUMMARY</div>
			  	
			  	<table style="width:100%" border="0">
			  		<tr>
			  			<td style="width:50%;" valign="top">
			  				<table style="width: 100%;  padding-left: 20px;padding-right: 20px;" border="0" cellpadding="0" cellspacing="0">
			  					
				  					<tr style="font-weight: 700;">
				  						<td class="formattedRow">Package</td>
				  						<td class="formattedRow" style="text-align:right">Quantity</td>
				  					</tr>
			  					
			  					<%
			  					String sql = "select category_id, package_id, brand_id, package_name, brand_name, product_sap_code, sum(raw_cases) as raw_cases, sum(units) as units, (select sort_order from inventory_packages where id=package_id) package_sort_order from temp_delivery_note_products where category_id=1 group by category_id, package_id order by package_sort_order";
			  					
			  					ResultSet rs3 = s3.executeQuery(sql);
			  					while(rs3.next()){
			  						
			  						String AttachBottles = "";
			  						if(rs3.getInt("units") > 0){
			  							AttachBottles = "/"+rs3.getInt("units");
			  						}
			  						
			  					%>
			  					
			  					<tr>
			  						<td class="formattedRow"><%=rs3.getString("package_name")%></td>
			  						<td class="formattedRow" style="text-align: right"><%=rs3.getString("raw_cases")+AttachBottles%></td>
			  					</tr>
			  					
			  					<%
			  					}
			  					rs3.close();
			  					%>
			  					
			  				</table>
			  			</td>
			  			<td style="width:50%" valign="top">
			  				
			  				
			  				<table style="width: 100%;  padding-left: 20px;padding-right: 20px;" border="0" cellpadding="0" cellspacing="0">
			  					
				  					<tr style="font-weight: 700;">
				  						<td class="formattedRow">Package</td>
				  						<td class="formattedRow">Brand</td>
				  						<td class="formattedRow" style="text-align:right">Quantity</td>
				  					</tr>
			  					
			  					<%
			  					String sql4 = "select category_id, package_id, brand_id, package_name, brand_name, product_sap_code, sum(raw_cases) as raw_cases, sum(units) as units, (select sort_order from inventory_packages where id=package_id) package_sort_order from temp_delivery_note_products where category_id=1 group by product_sap_code order by package_sort_order";
			  					
			  					ResultSet rs4 = s3.executeQuery(sql4);
			  					while(rs4.next()){
			  						
			  						String AttachBottles = "";
			  						if(rs4.getInt("units") > 0){
			  							AttachBottles = "/"+rs4.getInt("units");
			  						}
			  						
			  					%>
			  					
			  					<tr>
			  						<td class="formattedRow"><%=rs4.getString("package_name")%></td>
			  						<td class="formattedRow"><%=rs4.getString("brand_name")%></td>
			  						<td class="formattedRow" style="text-align: right"><%=rs4.getString("raw_cases")+AttachBottles%></td>
			  					</tr>
			  					
			  					<%
			  					}
			  					rs4.close();
			  					%>
			  					
			  				</table>
			  				
			  			</td>
			  		</tr>
			  	</table>
			  	
			  </div>
			  
				
			  
			  <%
			
		  }
		  rs.close();
		  
		  if(isFound == false){
			  %>
			  	<script type="text/javascript">setSubmitButton(false);</script>
			  <%
		  }else{
			  %>
			  	<script type="text/javascript">setSubmitButton(true);</script>
			  <%
		  }
		  
		  %>
		  

</body>
</html>
<%

	s3.executeUpdate("drop temporary table if exists temp_delivery_note_products ");

s3.close();
s2.close();
s.close();
ds.dropConnection();
%>