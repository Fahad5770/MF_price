<%@page import="com.pbc.util.NumberToWords"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>

<%@page import="org.apache.commons.lang3.text.WordUtils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();

Statement s = c.createStatement();
Statement s2 = c.createStatement();
//getting user name 
String UserName ="";
ResultSet rs2 = s.executeQuery("SELECT DISPLAY_NAME FROM users where id="+SessionUserID);
if(rs2.first()){
	UserName = rs2.getString("display_name");
}
 

long WarehouseStockTransferID = Utilities.parseLong(request.getParameter("WarehouseStockTransferID"));
long GRNWarehouseBarCode = Utilities.parseLong(request.getParameter("GRNWarehouseBarCode"));

%>
<html>

<head>

</head>
<body style="font-family:Helvetica,Arial,sans-serif">
		  
			  <div style="width: 8.27in; height: 3.8in; border: solid 1px; margin-top: 5px; page-break-after: always">
			   <table border="0" style="width:100%">
              	<tr>
                	<td align="left"><img src="images/logo.svg" style="width: 30px"></td>
                    <td valign="middle" style="font-weight:600"><%=Utilities.getCompanyName()%></td>
                    <td style="text-align: right; font-weight: 700; padding-right:10px">Stock Transfer</td>
                    
                </tr>
              </table>
               	<table border="0" style="width: 100%; margin-top: 5px; padding-right: 10px;">
                <%
                Date CreatedOn=null;
                long barcode=0;
                String VehicleName ="";
                String Query ="";
                if(GRNWarehouseBarCode>0){//means it has barcode
                	Query = "select * from inventory_warehouse_transfers where uvid="+GRNWarehouseBarCode;
                   //System.out.println(Query);
                }else{
                	Query = "select * from inventory_warehouse_transfers where id="+WarehouseStockTransferID;
                }
                
                ResultSet rs = s.executeQuery(Query);
               if(rs.first()){
            	    CreatedOn = rs.getDate("created_on");
            	    barcode = rs.getLong("uvid");
            	    VehicleName = rs.getString("vehicle_no");
            	    WarehouseStockTransferID = rs.getInt("id");
               }
                %>
                <tr>
                	 <td style="width: 20%;"><img src="/barbecue/barcode?data=<%=barcode%>&height=20&type=Code128&headless=false&drawText=yes" ><br></td>
                    
                    <td style="width: 20%; text-align: right; font-size: 12px; " nowrap="nowrap"><%=Utilities.getDisplayDateTimeFormat(CreatedOn)%></td>
                </tr>
                <tr>
			  			<td style="text-align: center">
				  			<table style="width: 100%; font-size: 12px; padding-left: 15px;">
				  				<tr>
				  					
				  					<td colspan="2" style="width: 64%; padding-right: 150px;text-align: center;"><%=barcode%></td>
				  				</tr>
				  			</table>
				  		</td>
				  		<td align="right" style="font-size: 12px"><b>Gatepass#</b> <%=WarehouseStockTransferID %></td>
			  	</tr>
                
                </table>
			  	<hr />
			  	<table style="width: 70%;  padding-left: 10px;padding-right: 10px; font-size: 12px" border="0" cellpadding="0" cellspacing="0">
			  		<tr style="font-weight: 700; font-size:12px;">
			  			<td class="formattedRow" nowrap style="border-bottom: 1px solid #000;padding-top:5px; padding-bottom:5px;">Product Code</td>
			  			<td class="formattedRow" nowrap style="border-bottom: 1px solid #000;padding-top:5px; padding-bottom:5px;">Package</td>
			  			<td class="formattedRow" nowrap style="border-bottom: 1px solid #000;padding-top:5px; padding-bottom:5px;">Brand</td>
			  			<td class="formattedRow" nowrap style="text-align: right; border-bottom: 1px solid #000;padding-top:5px; padding-bottom:5px;">Quantity</td>
			  			
			  			
			  		</tr>
			  		<%
			  		ResultSet rs1 = s.executeQuery("select * from inventory_warehouse_transfers_products iwtp,inventory_products_view ipv where iwtp.id="+WarehouseStockTransferID+" and iwtp.product_id=ipv.product_id");
		              while(rs1.next()){
			  		
			  		%>
			  		
			  		<tr>
			  			<td class="formattedRow" style="border-bottom: 1px solid #000;padding-top:5px; padding-bottom:5px;"><%=rs1.getLong("sap_code") %></td>
			  			<td class="formattedRow" style="border-bottom: 1px solid #000;padding-top:5px; padding-bottom:5px;"><%=rs1.getString("package_label") %></td>
			  			<td class="formattedRow" style="border-bottom: 1px solid #000;padding-top:5px; padding-bottom:5px;"><%=rs1.getString("brand_label") %></td>
			  			<td class="formattedRow" style="text-align: right; padding-right:5px; border-bottom: 1px solid #000;padding-top:5px; padding-bottom:5px;"><%= Utilities.convertToRawCases(rs1.getLong("total_units"), rs1.getInt("unit_per_sku")) %></td>
			  			
			  		</tr>
			  		<%
		              }
			  		%>
			  		</table>
			  		
			  		<table border="0" style="width: 70%; margin-top: 5px; padding-left: 5px;padding-right: 5px;padding-top: 5px; font-size: 12px;">
                	<tr>
                		<td><b>User:</b> <%= UserName %></td>
                		<td>&nbsp;</td>
                		<td>&nbsp;</td>
                		
                		<td style="text-align:right"><b>Veh#:</b> <%=VehicleName%></td>
                	</tr>
                </table>
			  </div>
			  

</body>
</html>
<%
s2.close();
s.close();
ds.dropConnection();

%>