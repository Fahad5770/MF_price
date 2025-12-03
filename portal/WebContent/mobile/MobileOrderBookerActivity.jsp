<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="com.pbc.mobile.MobileRequest"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>



 <style>
      /* Always set the map height explicitly to define the size of the div
       * element that contains the map. */
      #map {
        height: 100%;
      }
      /* Optional: Makes the sample page fill the window. */
      html, body {
        height: 100%;
        margin: 0;
        padding: 0;
      }
    </style>

<script>


function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}

</script>

<%
int FeatureID = 65;

MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 3000));

if (!mr.isExpired()){

long UserID = Utilities.parseLong(mr.getParameter("UserID"));
String Password = Utilities.filterString(mr.getParameter("Identity"), 1, 100);
String DeviceID = Utilities.filterString(mr.getParameter("UVID"), 1, 100);

long OrderBookerID = Utilities.parseLong(mr.getParameter("OutletID"));
System.out.println("OrderBookerID"+OrderBookerID);

boolean isValid = true;

boolean isDeviceValid = UserAccess.isMobileDeviceValid(DeviceID);
if(isDeviceValid){
	boolean isUserValid = UserAccess.isMobileUserValid(UserID, Password);
	if(isUserValid){
		if(Utilities.isAuthorized(FeatureID, UserID) == false){
			isValid = false;
			response.sendRedirect("AccessDenied.jsp");
		}
	}else{
		isValid = false;
		response.sendRedirect("AccessDenied.jsp");
	}
}else{
	isValid = false;
	response.sendRedirect("AccessDenied.jsp");
}

if (isValid){
	

Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s1 = c.createStatement();

s.executeUpdate("SET SESSION group_concat_max_len = 1000000");

Date StartDate = Utilities.parseDate(mr.getParameter("FromDate"));
Date EndDate = Utilities.parseDate(mr.getParameter("ToDate"));


String OrderBookerIDsWher="";
if(OrderBookerID>0){
	OrderBookerIDsWher =" and created_by in ("+OrderBookerID+") ";
}

//String OrderIDsQuery = "SELECT distinct mo.id FROM mobile_order mo where mo.status_type_id in (1,2) and mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+ ""+OrderBookerIDsWher ;

/* String OrderIDsQuery2 = "SELECT group_concat(mo.id) FROM mobile_order mo where mo.outlet_id in (select distinct dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to ="+UserID+") and mo.status_type_id in (1,2) and mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate);
if(OutletID > 0){
	OrderIDsQuery2 = "SELECT group_concat(mo.id) FROM mobile_order mo where mo.outlet_id in ("+OutletID+") and mo.status_type_id in (1,2) and mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate);
}


String OrderIDsQuery = "0";
ResultSet rs91 = s.executeQuery(OrderIDsQuery2);
if (rs91.first()){
	OrderIDsQuery = rs91.getString(1);
}

String InvoiceIDsQuery = "0";
ResultSet rs92 = s.executeQuery("select group_concat(id) from inventory_sales_invoices where order_id in ("+OrderIDsQuery+")");
if (rs92.first()){
	InvoiceIDsQuery = rs92.getString(1);
}
 */
 long OrderID = Utilities.parseLong(request.getParameter("OrderID"));

String WherePackage = "";
String WhereBrand = "";
 
String OrderIDsQuery = "SELECT distinct mo.id FROM mobile_order mo where mo.status_type_id in (1,2,3) and mo.id="+OrderID;

WherePackage = " and package_id in (";

ResultSet rs = s.executeQuery("SELECT distinct(ipv.package_id) FROM mobile_order_products mop, inventory_products_view ipv where mop.product_id=ipv.product_id and mop.id="+OrderID);
int counter = 0;
while(rs.next()){
	if(counter > 0){
		WherePackage += ", ";
	}
	WherePackage += rs.getString("package_id");
	counter++;
}
WherePackage += ")";

WhereBrand = " and brand_id in (";

ResultSet rs2 = s.executeQuery("SELECT distinct(ipv.brand_id) FROM mobile_order_products mop, inventory_products_view ipv where mop.product_id=ipv.product_id and mop.id="+OrderID);
int counter2 = 0;
while(rs2.next()){
	if(counter2 > 0){
		WhereBrand += ", ";
	}
	WhereBrand += rs2.getString("brand_id");
	counter2++;
}
WhereBrand += ")";

 
 
%>



<table style="width: 100%" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			 <li data-role="list-divider" style="background:#4596ce;height:28px;color:white;font-size: 14px;text-align:center;padding:2%"><b>Orders</b> </li>
			<table border=0 style="font-size:13px; font-weight: 400; width:100%;" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 
					 <thead>
					    <tr style="font-size: 11px;height:30px" >
							<th data-priority="1"  style="text-align:center;width:40%;background:#A9A9A9;color:white ">Outlet</th>
							<th data-priority="1"   style="text-align:center;width:35%;background:#A9A9A9;color:white  ">Products</th>
							<th data-priority="1"   style="text-align:center;width:25%;background:#A9A9A9;color:white " >Amount</th>
					    </tr>
					  </thead> 
					<%
					ResultSet rs22 = s1.executeQuery("SELECT id, outlet_id, (SELECT name FROM common_outlets where id=mobile_order.outlet_id) outlet_name, net_amount, created_on, mobile_timestamp, status_type_id,lat,lng FROM mobile_order where status_type_id in (1,2,3)  "+OrderBookerIDsWher+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+"  order by date(created_on) desc, mobile_timestamp desc");
					int RowChecker=1;
					while(rs22.next()){
						int isCancelled = 0;					
						if (rs22.getInt("status_type_id") == 3){
							isCancelled = 1;
						}
							
						Date MobileTimestamp = rs22.getTimestamp("mobile_timestamp");
						String addColorToTr="";
						
						if(RowChecker%2==0){
							addColorToTr="background:#F0F0F0";
							System.out.println("addColorToTr"+addColorToTr);
						}
						
						String Shop=rs22.getString("outlet_id")+" - "+rs22.getString("outlet_name");
						System.out.println(rs22.getDouble("lat"));
						%>		
						
						<tr style="font-size: 10px;height:30px;"  >
							<td style="<%=addColorToTr%>">
							
								<a href="javascript:void(0);" onclick="showMyMAp(<%=rs22.getDouble("lat")%>,<%=rs22.getDouble("lng")%>);" ><%=rs22.getString("outlet_id")+" - "+rs22.getString("outlet_name")%></a></br>
								<%=Utilities.getDisplayDateTimeFormat(MobileTimestamp)  %>
							</td>
							
							<td  style="text-align:left; border-left: 1px solid #606060; border-right: 1px solid #606060; ">
							
								<%
								double TotalAmountOrdersBooked = 0;
									double TotalAmountSales = 0;
									%>
										<table>
												<tr>
									<%
									
									int counter11=0;
									ResultSet rs21 = s.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where category_id=1  order by package_sort_order");
									while(rs21.next()){
										
										int PackageID = rs21.getInt("package_id");
										int Quantity=0;
									
											
											String QuantityOrdersBooked = "";
											double AmountOrdersBooked = 0;
											ResultSet rs3 = s3.executeQuery("SELECT sum(total_units) bottles, sum(if (mop.is_promotion=1, 0, mop.net_amount)) amount, ipv.unit_per_sku FROM mobile_order mo, mobile_order_products mop, inventory_products_view ipv where mo.id = mop.id and mop.product_id=ipv.product_id and mo.id in ("+rs22.getInt("id")+")  and ipv.package_id="+PackageID+"  and ipv.category_id = 1  group by mop.product_id");
											System.out.println("SELECT sum(total_units) bottles, sum(if (mop.is_promotion=1, 0, mop.net_amount)) amount, ipv.unit_per_sku FROM mobile_order mo, mobile_order_products mop, inventory_products_view ipv where mo.id = mop.id and mop.product_id=ipv.product_id and mo.id in ("+rs22.getInt("id")+") and  ipv.package_id="+PackageID+"  and ipv.category_id = 1  group by mop.product_id");
											while(rs3.next()){
												QuantityOrdersBooked = Utilities.convertToRawCases( rs3.getLong("bottles"), rs3.getInt("unit_per_sku"));
												AmountOrdersBooked = rs3.getDouble("amount");
												
												Quantity+=Integer.parseInt(Utilities.convertToRawCases( rs3.getLong("bottles"), rs3.getInt("unit_per_sku")));
												
											}
											
											if(QuantityOrdersBooked!="" && QuantityOrdersBooked!="0"){
												%>
												<td><%=rs21.getString("package_label")%></td>
					    						<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec">- <%=Quantity%> <%if(counter!=2) {%>,<%} %></td>
					    					<%
											}
											
											
										counter11++;
									}
							%>
										</tr>	
											
											</table>
							</td>
							
							<td  style="text-align:right; border-left: 1px solid #606060; border-right: 1px solid #606060; <%=addColorToTr%>">
							
								<%=Utilities.getDisplayCurrencyFormat(rs22.getDouble("net_amount"))%>
							</td>
							
						</tr>
						<%
						
						RowChecker++;
						
				}		
				%>
				</table>
				  <li data-role="list-divider" style="background:#4596ce;height:28px;color:white;font-size: 14px;text-align:center;padding:2%;margin-top:4%"><b>No Order </b></li>
				  
				<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
		    		   
		    		 <thead>
					   <tr style="font-size: 11px;height:30px" >
							<th data-priority="1"  style="text-align:center;width:50%;background:#A9A9A9;color:white ">Outlet</th>
					   
					   
							<th data-priority="2"   style="text-align:center;width:50%;background:#A9A9A9;color:white  " >Reason:</th>
					    </tr>
					    
					    
					  </thead> 
					  
						<% 
				//System.out.println("SELECT id, outlet_id, (SELECT name FROM common_outlets where id=mobile_order_zero.outlet_id) outlet_name,created_on,mobile_order_no,mobile_timestamp FROM mobile_order_zero where  created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+OrderBookerIDsWher+WhereDistributors1+"  "+WhereOutlets+" order by date(created_on) desc, mobile_timestamp desc");
				ResultSet rs23 = s1.executeQuery("SELECT id, outlet_id, (SELECT name FROM common_outlets where id=mobile_order_zero.outlet_id) outlet_name,created_on,mobile_order_no,mobile_timestamp,lat,lng FROM mobile_order_zero where  created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+OrderBookerIDsWher+" order by date(created_on) desc, mobile_timestamp desc");
				int RowCheckerNoorder=1;
				while(rs23.next()){
					long MobileOrderNumber =rs23.getLong("mobile_order_no");				
					
						
					Date MobileTimestamp = rs23.getTimestamp("mobile_timestamp");
					String addColorToTr="";
					
					if(RowCheckerNoorder%2==0){
						addColorToTr="background-color:	#F0F0F0";
						System.out.println("addColorToTr"+addColorToTr);
					}
					%>
					
						<tr style="font-size: 10px;height:30px;<%=addColorToTr%>"  >
							<td>
							
								<a href="javascript:void(0);" onclick="showMyMApNOOrder(<%=rs23.getDouble("lat")%>,<%=rs23.getDouble("lng")%>);" ><%=rs23.getString("outlet_id")+" - "+rs23.getString("outlet_name")%></a></br>
								</br>
								<%=Utilities.getDisplayDateTimeFormat(MobileTimestamp)  %>
							</td>
							
							<td  style="text-align:right; border-left: 1px solid #606060; border-right: 1px solid #606060; padding-right: 5px">
								<table border=0 style="font-size:13px; margin-top:20px ; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
										
									
									
									<% 
							         ResultSet rs11 = s.executeQuery("SELECT moz.comments,moz.no_order_reason_type_v2,(select label from mobile_order_no_order_reason_type where mobile_order_no_order_reason_type.id=moz.no_order_reason_type_v2) name from mobile_order_zero moz where moz.id="+rs23.getInt("id") );
							         while(rs11.next()){
							          
							          String Reasonlabel = rs11.getString("name");
							          String ReasonComments = rs11.getString("comments");
							          int  Reasontype = rs11.getInt("no_order_reason_type_v2");
							          
							          %>
							          <tr style="background:#ececec">
							                       <td align="left"><b>Reason</b> : <%=Reasonlabel %> </td>
							                      
							          
							           <%
							                       if(Reasontype==6){
							                        
							                        %> 
							                        <tr style="background:#ececec">  
							                        <td align="left"><b>Comments : </b> : <%=ReasonComments %> </td>
							                       </tr>
							                       
							                       
							                       <%
							                       }
							                       
							                       
							                       
							          
							          
							         }
							         %>
									
									
									
								</table>
								
							</td>
							
							
						</tr>
					
					
					
					
					<%
					/* LastDate = rs23.getDate("created_on");
					NoOrdercounter++; */
					
					RowCheckerNoorder++;
				}
				
				
				
			%>
				</table>
				
		</td>
		
		
	</tr>
	
</table>
<script>

</script>

<!-- <script src="http://maps.googleapis.com/maps/api/js"></script> -->
<!-- <script>

function alertabc(){
	alert("ABC");
}
initMap();
var map;
function initMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: -34.397, lng: 150.644},
    zoom: 8
  });
}
</script> -->



<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();

}

}else{
	out.print("Error Code: 101");
}
%>