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


long ReceiptID = 0;
if( request.getParameter("EmptyCreditReceiptID") != null ){
	ReceiptID = Utilities.parseLong(request.getParameter("EmptyCreditReceiptID"));
}

//System.out.println("hello "+ReceiptID);


Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
%>
<html>

<head>

</head>
<body style="font-family:Helvetica,Arial,sans-serif">
		  <%
		  
		  String UserName="";
		  Date PrintedOn=new Date();
		  
		  //System.out.println(SQLMain);
		  
		  ResultSet rs = s.executeQuery("select ecer.id,ecer.created_on,ecer.vehicle_no,ecer.distributor_id,(select name from common_distributors cd where cd.distributor_id=ecer.distributor_id) distributor_name,ecer.warehouse_id,(select label from common_warehouses cw where cw.id=ecer.warehouse_id) warehouse_name,ecerp.product_id,ip.package_id,(select label from inventory_packages ipp where ipp.id=ip.package_id) package_name, ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) barand_name,ecerp.raw_cases,ecerp.units,ecer.created_by, (select display_name from users u where u.id=ecer.created_by) created_by_name, ecer.arrival_no from ec_empty_receipt ecer join ec_empty_receipt_products ecerp on ecer.id=ecerp.id join inventory_products ip on ecerp.product_id=ip.id where ecer.id="+ReceiptID);
		  if (rs.first()){
			  
			  
			  
			  Date CreatedOn = rs.getTimestamp("created_on");
			  long DistributorID = rs.getLong("distributor_id");
			  String DistributorName = rs.getString("distributor_name");
			 long WarehouseID=rs.getLong("warehouse_id");
			 String WarehouseName=rs.getString("warehouse_name");
			  String VehicleNo = rs.getString("vehicle_no");
			  long ArrivalNo = rs.getLong("arrival_no");
			
			  UserName = rs.getString("created_by_name");
			 
			
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
                    <td style="text-align: center;font-weight: 700;">Empty Receipt</td>
                    <td style="width: 40%;text-align: right;"><%= Utilities.getDisplayDateTimeFormat(CreatedOn) %></td>
                </tr>
                
                </table>
			  	<hr />
			  	<table style="width: 100%;  margin: 20px;" border="0">
			  		
			  		<tr style="font-weight: 700;">				  	
				  		<td>Receipt ID</td>
				  		<td>Arrival No</td>
			  		</tr>
			  		<tr style="font-weight: 700;">
				  		
				  		<td>
				  			<label style="font-weight:normal;"><%=rs.getLong("id")%></label>
				  		</td>
				  		<td>
				  			<label style="font-weight:normal;"><%=ArrivalNo%></label>
				  		</td>
				  		
			  		</tr>
			  		<tr style="font-weight: 700;">
				  		
				  		<td colspan="3">
				  			Distributor
				  		</td>
				  		<td rowspan="2" align="right" style="padding-right: 18px">
			  				<img src="/barbecue/barcode?data=121321&height=50&type=Code128&headless=false&drawText=yes"><br>
			  			</td>
			  		</tr>
			  		<tr>
				  		
				  		<td colspan="3">
				  		<%= DistributorID + " - " + DistributorName %>
				  		</td>
			  		</tr>
                    
                    
                    
			  		
                    
                    <tr style="font-weight: 700;">
				  		<td>
				  			Warehouse
				  		</td>				  		
				  		<td>
				  			Vehicle #
				  		</td>
				  		
				  		
			  		</tr>
			  		<tr>
				  		<td>
				  		<%=WarehouseName %>
				  		</td>
				  		
				  		<td>
					  		<%
								if (VehicleNo !=null &&  !VehicleNo.isEmpty()){
									out.print(VehicleNo);
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
			  			<td class="formattedRow" style="">Type</td>
			  			<td class="formattedRow" nowrap style="text-align: right;">Raw Cases</td>
			  			<td class="formattedRow"  style="text-align: right; width:20%">Bottles</td>
			  			
			  			
			  		</tr>
			  
			  	<%
		  }
		  
		  ResultSet rs1 = s.executeQuery("select ecer.id,ecer.created_on,ecer.vehicle_no,ecer.distributor_id,(select name from common_distributors cd where cd.distributor_id=ecer.distributor_id) distributor_name,ecer.warehouse_id,(select label from common_warehouses cw where cw.id=ecer.warehouse_id) warehouse_name,ecerp.product_id,ip.package_id,(select label from inventory_packages ipp where ipp.id=ip.package_id) package_name, ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_name,ecerp.raw_cases,ecerp.units, ecerp.type_id, (select label from ec_empty_receipt_types ecert where ecert.id=ecerp.type_id) type_name from ec_empty_receipt ecer join ec_empty_receipt_products ecerp on ecer.id=ecerp.id join inventory_products ip on ecerp.product_id=ip.id where ecer.id="+ReceiptID);
		  while(rs1.next()){
				
			  	%>
			  	
			  	<tr >
			  			<td class="formattedRow" nowrap><%=rs1.getLong("product_id") %></td>
			  			<td class="formattedRow" nowrap><%=rs1.getString("package_name") %></td>
			  			<td class="formattedRow" nowrap><%=rs1.getString("brand_name") %></td>
			  			<td class="formattedRow" nowrap><%=rs1.getString("type_name") %></td>
			  			<td class="formattedRow" nowrap style="text-align: right;"><%=rs1.getLong("raw_cases") %></td>
			  			<td class="formattedRow"  style="text-align: right;"><%=rs1.getLong("units") %></td>
			  			
			  			
			  		</tr>
			  				  	
			  	
                
			  
			  <%
			
		  }
		  rs.close();
		  rs1.close();
		  
		  
		  %>
		 </table> 
		 <table style="width: 100%; margin-top: 5px; padding-left: 20px;padding-right: 20px;padding-top: 20px;">
		 	<tr>
		 		<td style="width: 40%;"><%= "User: "+UserName %></td>
		 		<td style="text-align: center;"></td>
		 		<td style="width: 20%; text-align: right;" nowrap="nowrap"><%="Printed On: "+Utilities.getDisplayDateTimeFormat(PrintedOn) %></td>
		 	</tr>
		 </table>
</div>
</body>
</html>
<%

	
s3.close();
s2.close();
s.close();
ds.dropConnection();
%>