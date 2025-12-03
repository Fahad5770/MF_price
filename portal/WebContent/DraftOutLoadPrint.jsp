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
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>

<style>

.formattedRow{
	border-bottom:1px solid #000;
	padding-top:5px;
	padding-bottom:5px;

}

</style>

<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 329;

long DistributorID = 0;
String DistributorName = "";

//if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	//response.sendRedirect("AccessDenied.jsp");
//}else{
	//Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);


	//if( UserDistributor != null ){
		//if(UserDistributor.length>1) //if it has more than 1 distributor
		//{
			//response.sendRedirect("AccessDenied.jsp");
		//}
		//else
		//{
			//DistributorName = UserDistributor[0].DISTRIBUTOR_NAME;
			//DistributorID = UserDistributor[0].DISTRIBUTOR_ID;
		//}
	//}
	
//}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();

int SapOrderNo = Utilities.parseInt(request.getParameter("DraftID"));
//int SapOrderNo = Utilities.parseInt(request.getParameter("DraftID"));
//if( request.getParameter("DispatchID") != null ){
	//SapOrderNo = Utilities.parseInt(request.getParameter("DispatchID"));
//}

Statement s = c.createStatement();
Statement s1 = c.createStatement();
Statement s2 = c.createStatement();



String DriverName = "";
String VehicleName = "";
int DispatchTypeID11=0;
ResultSet rs1 = s.executeQuery("SELECT vehicle_id, (SELECT vehicle_no FROM distribtuor_vehicles where id=vehicle_id) vehicle_name, driver_id, (SELECT name FROM distributor_employees where id=driver_id) driver_name,dispatch_type FROM inventory_sales_dispatch where id="+SapOrderNo);
if(rs1.first()){
	DriverName = rs1.getString("driver_name");
	VehicleName = rs1.getString("vehicle_name");
	DispatchTypeID11 = rs1.getInt("dispatch_type");
}

%>
<html>

<head>
<script>
function duplicate(){
	var c1 = document.getElementsByName("ColumnOne");
	var c2 = document.getElementsByName("ColumnTwo");
	var x  = 0;
	for (x = 0; x < c1.length; x++){
		c2[x].innerHTML = c1[x].innerHTML;
	}
}
</script>
</head>

<body style="font-family:Helvetica,Arial,sans-serif" >
	<%
		  
		  String SQLMain = "";
		  boolean isFound = false;
		  
		 // SQLMain = "SELECT isi.id, isi.created_on, isi.outlet_id, concat(om.Outlet_Name, ' ', om.Bsi_Name) as outlet_name, om.address as outlet_address, om.Owner as owner, om.Telepohone as telephone, isi.uvid, isi.invoice_amount, isi.wh_tax_amount, isi.total_amount, isi.discount, isi.net_amount, isi.distributor_id, (select name from common_distributors where distributor_id=isi.distributor_id) distributor_name , (SELECT DISPLAY_NAME FROM users where id=isi.created_by) user_name, (SELECT DISPLAY_NAME FROM users where id=isi.booked_by) booked_by_name FROM inventory_sales_invoices isi, outletmaster om where isi.outlet_id=om.Outlet_ID and isi.id in (select sales_id from inventory_sales_dispatch_invoices where id="+DispatchID+") order by isi.id ";
		 SQLMain = "SELECT idol.id,idol.invoice_no, idol.created_on,idol.printed_on,idol.distributor_id, (select name from common_distributors where distributor_id=idol.distributor_id) distributor_name,(SELECT DISPLAY_NAME FROM users where id=idol.created_by) user_name,idol.remarks,idol.invoice_no,idol.uvid,idol.sap_order_no from inventory_draft_outlet_load idol where  idol.sap_order_no="+SapOrderNo;
		 
		  //System.out.println(SQLMain);
		  
		  ResultSet rs = s.executeQuery(SQLMain);
		  while (rs.next()){
			  //isFound = true;
		  int DraftID = rs.getInt("id");
		  long InnoviceNo=rs.getLong("invoice_no");
		  Date CreatedOn = rs.getTimestamp("created_on");
		  Date PrintedOn=rs.getTimestamp("printed_on");
		  DistributorID = rs.getLong("distributor_id");
		  DistributorName = rs.getString("distributor_name");
		  if(DistributorName==null){
			  DistributorName="";
		  }
		  String UserName = rs.getString("user_name");
		  long SalesOrderNo=rs.getLong("sap_order_no");
		  String Remarks=rs.getString("remarks");
		  if(Remarks==null){
			  Remarks="";
		  }
		 //String UpdateIsPrinted="Update inventory_draft_outlet_load set is_printed='1',printed_on=now() where sap_order_no="+SapOrderNo;
		  //int Updated=s1.executeUpdate(UpdateIsPrinted);
	%>
              
              
              <table style="width: 11.1in; border: 0px; page-break-after:always ">
              <tr>
              <td>
			  <div style="width: 5.5in; border: solid 1px; margin-top: 5px;" name="ColumnOne">
              
              <table border="0" style="width:100%">
              	<tr>
                	<td align="left"><img src="images/logo.svg" style="width: 30px"></td>
                    <td valign="middle" style="font-weight:600"><%=Utilities.getCompanyName()%></td>
                    <td style="text-align: right; font-weight: 700; padding-right:10px">Draft Out Load</td>
                    
                </tr>
              </table>
              
			  	<table border="0" style="width: 100%; margin-top: 5px; padding-left: 10px;padding-right: 10px;">
                <tr>
                	<td style="width: 20%; text-align: left; font-size: 12px"><b>Printed on : </b><%=Utilities.getDisplayDateTimeFormat(PrintedOn)%></td>
                    
                    <td style="width: 20%; text-align: right; font-size: 12px" nowrap="nowrap"><b>Created on : </b><%=Utilities.getDisplayDateTimeFormat(CreatedOn)%></td>
                </tr>
               
                
                </table>
			  	<hr />
			  	<table style="width: 100%;  margin: 10px; font-size: 12px" border="0">
			  		<tr>
				  		<td><b>Distributor</b></td>
				  	</tr>
				  	<tr>
				  		<td><%= DistributorID + " - " + DistributorName %></td>
			  		</tr>
			  	</table>
			  	
			  	<table style="width: 75%;  margin: 10px; font-size: 12px" border="0">
			  		
			  		<tr>
				  		<td><b>Sale Order#</b></td>
				  		<td><b>Innovice#</b></td>
				  		<td><b>Vehcile Type</b></td>
				  		<td><b>Vehcile#</b></td>
				  	</tr>
				  	<tr>
				  		<td><%=SalesOrderNo%></td>
				  		<td><%= InnoviceNo %></td>
				  		<td></td>
				  		<td></td>
			  		</tr>
			  		<tr>
				  		<td col-span="4"><b>Remarks</b></td>
				  	</tr>
				  	<tr>
				  		<td col-span="4"><%=Remarks%></td>
			  		</tr>
			  		
			  	</table>
			  	
			  	
			  	<table style="width: 100%;  padding-left: 10px;padding-right: 10px; font-size: 12px" border="0" cellpadding="0" cellspacing="0">
			  		<tr style="font-weight: 700; font-size:12px">
			  			<td class="formattedRow" style="width:20%" nowrap>Product Code</td>
			  			<td class="formattedRow" style="width:15%" nowrap>Package</td>
			  			<td class="formattedRow" style="width:15%"  nowrap>Brand</td>
			  			<td class="formattedRow" nowrap style="text-align: right;width:10%">Raw Cases</td>
			  			<td class="formattedRow" nowrap style="text-align: right;width:15%">Bottles</td>
			  			<td class="formattedRow" nowrap style="text-align: right;width:25%">Batch Code</td>
			  			
			  		</tr>
			  	<%
			  	//int PromotionCounter = 0;
			  	int PreviousCategoryID = 0;
			  //	System.out.println("SELECT idolp.draft_id, idolp.product_id, ipv.sap_code, ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, idolp.total_units, idolp.raw_cases,idolp.batch_code, ipv.unit_per_sku,ipv.category_id,ipv.category_label FROM inventory_draft_outlet_load_products idolp, inventory_products_view ipv where idolp.product_id=ipv.product_id and idolp.draft_id="+DraftID+" order by  ipv.package_label, ipv.brand_label");
			  	ResultSet rs2 = s2.executeQuery("SELECT idolp.draft_id, idolp.product_id, ipv.sap_code, ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, idolp.total_units, idolp.raw_cases,idolp.batch_code, ipv.unit_per_sku,ipv.category_id,ipv.category_label FROM inventory_draft_outlet_load_products idolp, inventory_products_view ipv where idolp.product_id=ipv.product_id and idolp.draft_id="+DraftID+" order by  ipv.package_label, ipv.brand_label");
				while (rs2.next()){
					//System.out.println("hello");
					int PackageID = rs2.getInt("package_id");
					int BrandID = rs2.getInt("brand_id");
					String Package = Utilities.filterString(rs2.getString("package_label"), 2, 100);
					String Brand = Utilities.filterString(rs2.getString("brand_label"), 2, 100);
					long ProductCode = rs2.getLong("sap_code");
					int Bottles = rs2.getInt("total_units");
					double RawCases = rs2.getDouble("raw_cases");
					String BatchCode = rs2.getString("batch_code");
					String CategoryName = rs2.getString("category_label");
					int CategoryID = rs2.getInt("category_id");
					if(BatchCode==null){
						BatchCode="";
					}else if(BatchCode!=null &&  CategoryID!=1){
						BatchCode="";
					}
					int UnitPerSKU = rs2.getInt("unit_per_sku");
					
			  	%>
			  	
			  	<tr>
			  		<td Style="color:white"><span><br></span></td>
			  	</tr>
			  	<% if( CategoryID != PreviousCategoryID ){ %>
			  	
					  		<tr>
					  			<td colspan="6"  style="font-weight: bold; border-bottom: 1px solid #000; padding-top:5px; padding-bottom:5px; text-align: left;"><%=CategoryName%></td>
					  		</tr>
			  		<%
			  				PreviousCategoryID = CategoryID;
			  			} 
			  		%>
			  		<tr>
			  			<td class="forma1ttedRow"><%= ProductCode %></td>
			  			<td class="form1attedRow"><%= Package %></td>
			  			<td class="for1mattedRow"><%= Brand %></td>
			  			<td class="fo1rmattedRow" style="text-align: right; padding-right:5px;"><%= Utilities.convertToRawCases(Bottles, UnitPerSKU) %></td>
			  			<td class="f1ormattedRow" style="text-align: right; padding-right:5px;"><%= Bottles%></td>
			  			<td  style="text-align: right; padding-right:5px;"><%= BatchCode%></td>
			  				
			  		</tr>
			  		<tr>
			  		
			  			<td style="color:white"><span>
			  			<br>
			  			</span></td>
			  		</tr>
			  		<tr>
			  			<td style="color:white"><span><br></span></td>
			  		</tr>
			  		
			  	<%
				}				
			  	%>
			  		
			  		<tr>
			  			<td>&nbsp;</td>
			  		</tr>
			  		
			  		
			  	</table>
			  	
                <table style="width: 100%; margin-top: 5px; padding-left: 20px;padding-right: 20px;padding-top: 20px; font-size: 12px; font-size: 12px">
                	<tr>
                		<td col-span="4" style="text-align:right"><b>User:</b><%=UserName %></td>
                	</tr>
                </table>
                
			  </div>
			  </td>
			  <td>
			 
				</td>
			  </tr>
			  </table>
			  <%
			
		  }
		  
		  
		  %>
		  

</body>
</html>
<%



s2.close();
s.close();
ds.dropConnection();
%>