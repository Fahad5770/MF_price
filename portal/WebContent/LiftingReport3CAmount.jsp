<%@page import="org.omg.CORBA.portable.RemarshalException"%>
<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<script src="js/LiftingReport.js"></script>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>

<script>

function redirect(DistributorID, SapOrderNo, Month, Day){
	
	document.getElementById("LiftingReportMainDistributor").value= DistributorID;
	
	if( SapOrderNo == '0' ){
		document.getElementById("SapOrderNo").disabled=true;
	}else{
		document.getElementById("SapOrderNo").disabled=false;
		document.getElementById("SapOrderNo").value= SapOrderNo;
	}
	
	if( Month == '0' ){
		document.getElementById("Month").disabled=true;
	}else{
		document.getElementById("Month").disabled=false;
		document.getElementById("Month").value= Month;
	}
	
	if( Day == '0' ){
		document.getElementById("Day").disabled=true;
	}else{
		document.getElementById("Day").disabled=false;
		document.getElementById("Day").value= Day; 
		
	}
	
	//alert($('#PassParams').serialize());
	
	document.getElementById("PassParams").submit();
}

</script>

<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(49, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}


Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();


String params = request.getParameter("params");

int Region = Utilities.parseInt(request.getParameter("LiftingReportMainRegion"));
long Distributor = Utilities.parseLong(request.getParameter("LiftingReportMainDistributor"));
int Snd = Utilities.parseInt(request.getParameter("LiftingReportMainSnd"));
long InvoiceNumber = Utilities.parseLong(request.getParameter("LiftingReportMainInvoiceNumber"));

int PaymentMethod = Utilities.parseInt(request.getParameter("LiftingReportMainPaymentMethod"));
long SaleOrderNumber = Utilities.parseLong(request.getParameter("LiftingReportMainSaleOrderNumber"));
String VehicleNumber = Utilities.filterString(request.getParameter("LiftingReportMainVehicleNumber"), 1, 100);

int VehicleType = Utilities.parseInt(request.getParameter("LiftingReportMainVehicleType"));  
String Remakrs = Utilities.filterString(request.getParameter("LiftingReportMainRemakrs"), 1, 100);
String BatchCode = Utilities.filterString(request.getParameter("LiftingReportBatchCode"), 1, 100);
long UserSAPCode = Utilities.parseLong(request.getParameter("LiftingReportUser"));
String Type = Utilities.filterString(request.getParameter("LiftingReportMainType"), 1, 100);

String isShiftedToOtherPlant = Utilities.filterString(request.getParameter("isShiftedToOtherPlant"), 1, 100);
System.out.println("================================================> isShiftedToOtherPlant = "+isShiftedToOtherPlant); 

int PackageID = Utilities.parseInt(request.getParameter("LiftingReportPackage"));
int BrandID = Utilities.parseInt(request.getParameter("LiftingReportBrand"));

/*
String Status = Utilities.filterString(request.getParameter("LiftingReportStatus"), 1, 100); 
*/
int FromDateHour = Utilities.parseInt(request.getParameter("LiftingReportMainFromDateHour"));
int FromDateMinutes = Utilities.parseInt(request.getParameter("LiftingReportMainFromDateMinutes"));
Date FromDate = Utilities.parseDateTime(request.getParameter("LiftingReportMainFromDate"), FromDateHour, FromDateMinutes);

int ToDateHour = Utilities.parseInt(request.getParameter("LiftingReportMainToDateHour"));
int ToDateMinutes = Utilities.parseInt(request.getParameter("LiftingReportMainToDateMinutes"));
Date ToDate = Utilities.parseDateTime(request.getParameter("LiftingReportMainToDate"), ToDateHour, ToDateMinutes);

int WarehouseID = Utilities.parseInt(request.getParameter("LiftingReportMainWarehouseID"));

String url = "LiftingReport3CAmount.jsp?LiftingReportMainFromDate="+request.getParameter("LiftingReportMainFromDate")+"&LiftingReportMainFromDateHour="+FromDateHour+"&LiftingReportMainFromDateMinutes="+FromDateMinutes+"&LiftingReportMainToDate="+request.getParameter("LiftingReportMainToDate")+"&LiftingReportMainToDateHour="+ToDateHour+"&LiftingReportMainToDateMinutes="+ToDateMinutes+"&LiftingReportMainRegion="+Region+"&LiftingReportMainSnd="+Snd+"&LiftingReportMainInvoiceNumber="+InvoiceNumber+"&LiftingReportMainPaymentMethod="+PaymentMethod+"&LiftingReportMainSaleOrderNumber="+SaleOrderNumber+"&LiftingReportMainVehicleNumber="+VehicleNumber+"&LiftingReportMainVehicleType="+VehicleType+"&isShiftedToOtherPlant="+isShiftedToOtherPlant+"&LiftingReportMainType="+Type+"&LiftingReportMainRemakrs="+Remakrs+"&LiftingReportBatchCode="+BatchCode+"&LiftingReportUser="+UserSAPCode+"&LiftingReportPackage="+PackageID+"&LiftingReportBrand="+BrandID+"&LiftingReportMainWarehouseID="+WarehouseID;
 
long SapOrderNumber = Utilities.parseLong(request.getParameter("SapOrderNo"));

String MonthAmount = request.getParameter("Month");
String DayAmount = request.getParameter("Day");


//System.out.println(SapOrderNumber);
%>



<div data-role="header" data-id="LiftingHeader" data-theme="d">
    <h1>LIFTING REPORT</h1>
    <a href="LiftingReportMainAmount.jsp" data-role="button" data-theme="d" data-inline="true" data-ajax="false" data-icon="back">Back</a>
    
</div>

<form id="PassParams" action="LiftingReport3CAmount.jsp" method="post">
	
	<input type="hidden" name="LiftingReportMainFromDate" value="<%=request.getParameter("LiftingReportMainFromDate")%>" >
	<input type="hidden" name="LiftingReportMainFromDateHour" value="<%=FromDateHour%>" >
	<input type="hidden" name="LiftingReportMainFromDateMinutes" value="<%=FromDateMinutes%>" >
	
	<input type="hidden" name="LiftingReportMainToDate" value="<%=request.getParameter("LiftingReportMainToDate")%>" >
	<input type="hidden" name="LiftingReportMainToDateHour" value="<%=ToDateHour%>" >
	<input type="hidden" name="LiftingReportMainToDateMinutes" value="<%=ToDateMinutes%>" >
	
	<input type="hidden" name="LiftingReportMainRegion" value="<%=Region%>" >
	<input type="hidden" name="LiftingReportMainSnd" value="<%=Snd%>" >
	<input type="hidden" name="LiftingReportMainInvoiceNumber" value="<%=InvoiceNumber%>" >
	<input type="hidden" name="LiftingReportMainPaymentMethod" value="<%=PaymentMethod%>" >
	
	<input type="hidden" name="LiftingReportMainSaleOrderNumber" value="<%=SaleOrderNumber%>" >
	<input type="hidden" name="LiftingReportMainVehicleNumber" value="<%=VehicleNumber%>" >
	<input type="hidden" name="LiftingReportMainVehicleType" value="<%=VehicleType%>" >
	<input type="hidden" name="LiftingReportMainType" value="<%=Type%>" >
	
	<input type="hidden" name="isShiftedToOtherPlant" value="<%=isShiftedToOtherPlant%>" > 
	
	<input type="hidden" name="LiftingReportMainRemakrs" value="<%=Remakrs%>" >
	<input type="hidden" name="LiftingReportBatchCode" value="<%=BatchCode%>" >
	<input type="hidden" name="LiftingReportUser" value="<%=UserSAPCode%>" >
	
	<input type="hidden" name="LiftingReportPackage" value="<%=PackageID%>" >
	<input type="hidden" name="LiftingReportBrand" value="<%=BrandID%>" >
	<input type="hidden" name="LiftingReportMainWarehouseID" value="<%=WarehouseID%>" >
	
	<input type="hidden" name="LiftingReportMainDistributor" id="LiftingReportMainDistributor" value="" >
	<input type="hidden" name="SapOrderNo" id="SapOrderNo" value="" >
	
	<input type="hidden" name="Month" id="Month" value="" >
	<input type="hidden" name="Day" id="Day" value="" >
	
</form>


<div data-role="content" data-theme="d">

<table border=0 style="width:100%">
	<tr>
		<td style="width:22%" valign="top">
		
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
				<li data-role="list-divider">Distributor's Invoice Value</li>
				
				<%
				
				String whereWithoutDistributor = "";
	            
				if(Region > 0){
	            	whereWithoutDistributor += " and cd.region_id = "+Region;
	            }
				if(Snd > 0){
	            	whereWithoutDistributor += " and cd.snd_id = "+Snd;
	            }
	            if(InvoiceNumber > 0){
	            	whereWithoutDistributor += " and idn.invoice_no = "+InvoiceNumber;
	            }
	            if(PaymentMethod > 0){
	            	whereWithoutDistributor += " and idn.payment_method = "+PaymentMethod;
	            }
	            if(SaleOrderNumber > 0){
	            	whereWithoutDistributor += " and idn.sap_order_no = "+SaleOrderNumber;
	            }
	            if( VehicleNumber != null && !VehicleNumber.equals("") && VehicleNumber.length() > 0){
	            	whereWithoutDistributor += " and idn.vehicle_no = "+VehicleNumber;
	            }
	            if(VehicleType > 0){
	            	whereWithoutDistributor += " and idn.vehicle_type = "+VehicleType;
	            }
	            
	            if (Type.equals("Internal")){
	            	whereWithoutDistributor += " and idn.outsourced_primary_sales_id is null";
	            }
	            if (Type.equals("Outsourced")){
	            	whereWithoutDistributor += " and idn.outsourced_primary_sales_id is not null";
	            }
	            
	            if ( isShiftedToOtherPlant != null && isShiftedToOtherPlant.equals("1")){
	            	whereWithoutDistributor += " and cd.is_shifted_to_other_plant=0 ";
	            }
	            
	            if( Remakrs != null && !Remakrs.equals("") && Remakrs.length() > 0){
	            	whereWithoutDistributor += " and idn.remarks like '%"+Remakrs+"%'";
	            }
	            if( BatchCode != null && !BatchCode.equals("") && BatchCode.length() > 0){
	            	//whereWithoutDistributor += " and idnp.batch_code like '%"+BatchCode+"%'";
	            }
	            if(UserSAPCode > 0){
	            	whereWithoutDistributor += " and idn.created_by = "+UserSAPCode;
	            }
	            if(PackageID > 0){
	            	//whereWithoutDistributor += " and ip.package_id = "+PackageID;
	            } 
	            if(BrandID > 0){
	            	//whereWithoutDistributor += " and ip.brand_id = "+BrandID;
	            }
	            if(WarehouseID > 0){
	            	whereWithoutDistributor += " and idn.warehouse_id = "+WarehouseID;
	            }
	            int FeatureID = 37;
	            
	            //getting user regions
	            
	            			
				String RegionIds = UserAccess.getRegionQueryString(UserAccess.getUserFeatureRegion(SessionUserID, FeatureID));			
				
				String WarehouseIds = UserAccess.getWarehousesQueryString(UserAccess.getUserFeatureWarehouse(SessionUserID, FeatureID));				
				
				String DistributorsIds = UserAccess.getDistributorQueryString(UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID));

				
				whereWithoutDistributor += " and idn.warehouse_id in ("+WarehouseIds+")";
				
				whereWithoutDistributor += " and cd.region_id in ("+RegionIds+")";
				
				whereWithoutDistributor += " and idn.distributor_id in ("+DistributorsIds+")";
				
				
				double TotalDistributorConvertedCases = 0;
				String sql1 = "select idn.distributor_id, (select name from common_distributors where distributor_id=idn.distributor_id) distributor_name, sum(idn.invoice_amount) amount from inventory_delivery_note idn, common_distributors cd where idn.distributor_id = cd.distributor_id and idn.created_on between "+Utilities.getSQLFromDateTime(FromDate)+" and "+Utilities.getSQLToDateTime(ToDate)+" "+whereWithoutDistributor+" group by idn.distributor_id order by amount desc";
				System.out.println("sql1 ============================> "+sql1);
				ResultSet rs_distributor = s.executeQuery(sql1);
				while(rs_distributor.next()){
					TotalDistributorConvertedCases += rs_distributor.getDouble("amount");
					%>
					
					<li <%if(Distributor == rs_distributor.getLong("idn.distributor_id")){ %> data-theme="b" <%} %>><a data-ajax="false" href="#" onclick="redirect(<%=rs_distributor.getString("idn.distributor_id")%>, 0, 0, 0)" style="font-size:13px"><%=rs_distributor.getString("distributor_name")%> <span class="ui-li-count" style="font-size:13px"><%=Utilities.getDisplayCurrencyFormatRounded(rs_distributor.getDouble("amount"))%></span></a></li>
					
					<%
				}
				%>
				
				<li <%if(Distributor==0){ %> data-theme="b"<%} %>><a data-ajax="false" href="#" onclick="redirect(0)" style="font-size:13px">All<span class="ui-li-count" style="font-size:13px"><%=Utilities.getDisplayCurrencyFormatRounded(TotalDistributorConvertedCases)%></span></a></li>
				
			</ul>
		
		</td>
		<td style="width:20%" valign="top">
		<%
			            
			            String where = "";
			            
			            if(Region > 0){
			            	where += " and cd.region_id = "+Region;
			            }
			            if(Snd > 0){
			            	where += " and cd.snd_id = "+Snd;
			            }
			            if(Distributor > 0){
			            	where += " and idn.distributor_id = "+Distributor;
			            }
			            if(InvoiceNumber > 0){
			            	where += " and idn.invoice_no = "+InvoiceNumber;
			            }
			            if(PaymentMethod > 0){
			            	where += " and idn.payment_method = "+PaymentMethod;
			            }
			            if(SaleOrderNumber > 0){
			            	where += " and idn.sap_order_no = "+SaleOrderNumber;
			            }
			            if( VehicleNumber != null && !VehicleNumber.equals("") && VehicleNumber.length() > 0){
			            	where += " and idn.vehicle_no = "+VehicleNumber;
			            }
			            if(VehicleType > 0){
			            	where += " and idn.vehicle_type = "+VehicleType;
			            }
			            
			            if (Type.equals("Internal")){
			            	where += " and idn.outsourced_primary_sales_id is null";
			            }
			            if (Type.equals("Outsourced")){
			            	where += " and idn.outsourced_primary_sales_id is not null";
			            }
			            
			            if ( isShiftedToOtherPlant != null && isShiftedToOtherPlant.equals("1")){
			            	where += " and cd.is_shifted_to_other_plant=0 ";
			            }
			            
			            if( Remakrs != null && !Remakrs.equals("") && Remakrs.length() > 0){
			            	where += " and idn.remarks like '%"+Remakrs+"%'";
			            }
			            if( BatchCode != null && !BatchCode.equals("") && BatchCode.length() > 0){
			            	//where += " and idnp.batch_code like '%"+BatchCode+"%'";
			            }
			            if(UserSAPCode > 0){
			            	where += " and idn.created_by = "+UserSAPCode;
			            }
			            if(PackageID > 0){
			            	//where += " and ip.package_id = "+PackageID;
			            } 
			            if(BrandID > 0){
			            	//where += " and ip.brand_id = "+BrandID;
			            }
			            if(WarehouseID > 0){
			            	where += " and idn.warehouse_id = "+WarehouseID;
			            }
			            
			            where += " and idn.warehouse_id in ("+WarehouseIds+")";
						
			            where += " and cd.region_id in ("+RegionIds+")";
						
			            where += " and idn.distributor_id in ("+DistributorsIds+")";
			            %>
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
				<li data-role="list-divider">Months</li>
				<%
				double totalAmountMonth =0;
				String sql2 = "select date_format(idn.created_on,'%M %Y') mon,idn.is_delivered,idn.is_received,idn.distributor_id,sap_order_no,idn.created_on,created_by,concat(first_name, ' ', last_name) name,sum(idn.invoice_amount) amount from users u,inventory_delivery_note idn,common_distributors cd  where   u.id=idn.created_by and idn.distributor_id = cd.distributor_id and idn.created_on between "+Utilities.getSQLFromDateTime(FromDate)+" and "+Utilities.getSQLToDateTime(ToDate)+ where+" group by mon order by idn.created_on desc";
				System.out.println("sql2 ========================================>"+sql2);
				ResultSet rs_orders  = s.executeQuery(sql2);
				
				while(rs_orders.next()){%>
					<li <%if(MonthAmount != null && MonthAmount.equals(rs_orders.getString("mon"))){ %>data-theme="b"<%} %>><a data-ajax="false" href="#" onclick="redirect(<%=Distributor%>, <%=rs_orders.getString("sap_order_no")%>, '<%=rs_orders.getString("mon")%>', 0)" style="font-size:13px; font-weight:normal;"> <%=rs_orders.getString("mon") %>&nbsp;
					
					<span class="ui-li-count" style="font-size:13px"><%totalAmountMonth += Math.round(rs_orders.getDouble("amount")); %><%=Utilities.getDisplayCurrencyFormatRounded(rs_orders.getDouble("amount")) %></span></a></li>
				<%}
				%>
				<li><a data-ajax="false"  style="font-size:13px; font-weight:normal;"> Total&nbsp;
					
					<span class="ui-li-count" style="font-size:13px"><%=Utilities.getDisplayCurrencyFormatRounded(totalAmountMonth) %></span></a></li>
				  
				
				
				
				<!-- <li><a data-ajax="false" href="<%//=url+"&LiftingReportMainDistributor=0"%>" style="font-size:13px">All<span class="ui-li-count" style="font-size:13px"><%=Utilities.getDisplayCurrencyFormat(TotalDistributorConvertedCases)%></span></a></li> -->
				
			</ul>
		
		</td>
		
		<td style="width:19%" valign="top">
		<%
			            
			            String where2 = "";
			            
			            if(Region > 0){
			            	where2 += " and cd.region_id = "+Region;
			            }
			            if(Snd > 0){
			            	where2 += " and cd.snd_id = "+Snd;
			            }
			            if(Distributor > 0){
			            	where2 += " and idn.distributor_id = "+Distributor;
			            }
			            if(InvoiceNumber > 0){
			            	where2 += " and idn.invoice_no = "+InvoiceNumber;
			            }
			            if(PaymentMethod > 0){
			            	where2 += " and idn.payment_method = "+PaymentMethod;
			            }
			            if(SaleOrderNumber > 0){
			            	where2 += " and idn.sap_order_no = "+SaleOrderNumber;
			            }
			            if( VehicleNumber != null && !VehicleNumber.equals("") && VehicleNumber.length() > 0){
			            	where2 += " and idn.vehicle_no = "+VehicleNumber;
			            }
			            if(VehicleType > 0){
			            	where2 += " and idn.vehicle_type = "+VehicleType;
			            }
			            
			            if (Type.equals("Internal")){
			            	where2 += " and idn.outsourced_primary_sales_id is null";
			            }
			            if (Type.equals("Outsourced")){
			            	where2 += " and idn.outsourced_primary_sales_id is not null";
			            }
			            
			            if ( isShiftedToOtherPlant != null && isShiftedToOtherPlant.equals("1")){
			            	where2 += " and cd.is_shifted_to_other_plant=0 ";
			            }
			            
			            if( Remakrs != null && !Remakrs.equals("") && Remakrs.length() > 0){
			            	where2 += " and idn.remarks like '%"+Remakrs+"%'";
			            }
			            if( BatchCode != null && !BatchCode.equals("") && BatchCode.length() > 0){
			            	//where += " and idnp.batch_code like '%"+BatchCode+"%'";
			            }
			            if(UserSAPCode > 0){
			            	where2 += " and idn.created_by = "+UserSAPCode;
			            }
			            if(PackageID > 0){
			            	//where += " and ip.package_id = "+PackageID;
			            } 
			            if(BrandID > 0){
			            	//where += " and ip.brand_id = "+BrandID;
			            }
			            if(WarehouseID > 0){
			            	where2 += " and idn.warehouse_id = "+WarehouseID;
			            }
			            
			            where2 += " and idn.warehouse_id in ("+WarehouseIds+")";
						
			            where2 += " and cd.region_id in ("+RegionIds+")";
						
			            where2 += " and idn.distributor_id in ("+DistributorsIds+")";
			            %>
			<%if(MonthAmount !=null)
			{ %>
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
				<li data-role="list-divider">Days</li>
				<%
				
				String sql3 = "select date_format(idn.created_on,'%M %d, %Y') mon,idn.is_delivered,idn.is_received,idn.distributor_id,sap_order_no,idn.created_on,created_by,concat(first_name, ' ', last_name) name,sum(idn.invoice_amount) amount from users u,inventory_delivery_note idn,common_distributors cd  where date_format(idn.created_on,'%M %Y') = '"+MonthAmount+"' and  u.id=idn.created_by and idn.distributor_id = cd.distributor_id and idn.created_on between "+Utilities.getSQLFromDateTime(FromDate)+" and "+Utilities.getSQLToDateTime(ToDate)+ where+" group by mon order by idn.created_on desc"; 
				System.out.println("sql3 ==========================================> "+sql3);
				ResultSet rs_orders1  = s.executeQuery(sql3);
				//System.out.println("select date_format(created_on,'%M %d, %Y') mon,idn.is_delivered,idn.is_received,idn.distributor_id,sap_order_no,created_on,created_by,concat(first_name, ' ', last_name) name,sum(idn.invoice_amount) amount from users u,inventory_delivery_note idn,common_distributors cd  where date_format(created_on,'%M %Y') = date_format('"+MonthAmount+"','%M %Y') and  u.id=idn.created_by and idn.distributor_id = cd.distributor_id and idn.created_on between "+Utilities.getSQLFromDateTime(FromDate)+" and "+Utilities.getSQLToDateTime(ToDate)+ where+" group by mon order by created_on desc");
				while(rs_orders1.next()){%>
					<li <%if(DayAmount != null && DayAmount.equals(rs_orders1.getString("mon"))){ %>data-theme="b"<%} %>><a data-ajax="false" href="#" onclick="redirect(<%=Distributor%>, <%=rs_orders1.getString("sap_order_no")%>, '<%=MonthAmount%>', '<%=rs_orders1.getString("mon")%>')" style="font-size:13px; font-weight:normal;"> <%=rs_orders1.getString("mon") %>&nbsp;
					
					<span class="ui-li-count" style="font-size:13px"><%=Utilities.getDisplayCurrencyFormatRounded(rs_orders1.getDouble("amount")) %></span></a></li>
				<%
					}
				
				
				%>
				
				  
				
				
				
				<!-- <li><a data-ajax="false" href="<%//=url+"&LiftingReportMainDistributor=0"%>" style="font-size:13px">All<span class="ui-li-count" style="font-size:13px"><%=Utilities.getDisplayCurrencyFormat(TotalDistributorConvertedCases)%></span></a></li> -->
				
			</ul>
		<%} %>
		</td>
		
		
		
		<td style="width:29%" valign="top">
			<%if(MonthAmount !=null)
			{ %>
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="b">
				<li data-role="list-divider">Documents</li>
				<li>
				<table border=0 style="font-size:13px; width:100%"  data-role="table" id="SamplingSummary" data-mode="reflow" class="ui-responsive table-stroke">
					  <thead>
					    <tr>
					    	
							
							<th data-priority="2" >Order#</th>
							<th data-priority="2" >Invoice#</th>							
							<th data-priority="1"  >Date</th>
							<th data-priority="persist"  style="text-align:right">Amount</th>
							
					    </tr>
					  </thead>
			
			            <%
						String where1 = "";
			            
			            if(Region > 0){
			            	where1 += " and cd.region_id = "+Region;
			            }
			            if(Snd > 0){
			            	where1 += " and cd.snd_id = "+Snd;
			            }
			            if(Distributor > 0){
			            	where1 += " and idn.distributor_id = "+Distributor;
			            }
			            if(InvoiceNumber > 0){
			            	where1 += " and idn.invoice_no = "+InvoiceNumber;
			            }
			            if(PaymentMethod > 0){
			            	where1 += " and idn.payment_method = "+PaymentMethod;
			            }
			            if(SaleOrderNumber > 0){
			            	where1 += " and idn.sap_order_no = "+SaleOrderNumber;
			            }
			            if( VehicleNumber != null && !VehicleNumber.equals("") && VehicleNumber.length() > 0){
			            	where1 += " and idn.vehicle_no = "+VehicleNumber;
			            }
			            if(VehicleType > 0){
			            	where1 += " and idn.vehicle_type = "+VehicleType;
			            }
			            
			            
			            if (Type.equals("Internal")){
			            	where1 += " and idn.outsourced_primary_sales_id is null";
			            }
			            if (Type.equals("Outsourced")){
			            	where1 += " and idn.outsourced_primary_sales_id is not null";
			            }
			            
			            if ( isShiftedToOtherPlant != null && isShiftedToOtherPlant.equals("1")){
			            	where1 += " and cd.is_shifted_to_other_plant=0 ";
			            }
			            
			            if( Remakrs != null && !Remakrs.equals("") && Remakrs.length() > 0){
			            	where1 += " and idn.remarks like '%"+Remakrs+"%'";
			            }
			            if( BatchCode != null && !BatchCode.equals("") && BatchCode.length() > 0){
			            	where1 += " and idnp.batch_code like '%"+BatchCode+"%'";
			            }
			            if(UserSAPCode > 0){
			            	where1 += " and idn.created_by = "+UserSAPCode;
			            }
			            if(PackageID > 0){
			            	where1 += " and ip.package_id = "+PackageID;
			            } 
			            if(BrandID > 0){
			            	where1 += " and ip.brand_id = "+BrandID;
			            }
			            if(WarehouseID > 0){
			            	where1 += " and idn.warehouse_id = "+WarehouseID;
			            }
			            if(SapOrderNumber >0)
			            {
			            	where1 += " and idn.sap_order_no = "+SapOrderNumber;
			            	//System.out.println("hello");
			            }
			            if(DayAmount !=null)
			            {
			            	where += " and date_format(idn.created_on,'%M %d, %Y') = '"+DayAmount+"'";
			            }
			            
			            where1 += " and idn.warehouse_id in ("+WarehouseIds+")";
						
			            where1 += " and cd.region_id in ("+RegionIds+")";
						
			            where1 += " and idn.distributor_id in ("+DistributorsIds+")";
			            double TotalConvertedCases = 0;
			            

			                
							String Sql = "select delivery_id,sap_order_no,idn.created_on,idn.is_delivered,idn.is_received,idn.distributor_id,sap_order_no,idn.created_on,created_by,concat(first_name, ' ', last_name) name,sum(idn.invoice_amount) amount, idn.invoice_no from users u,inventory_delivery_note idn,common_distributors cd  where date_format(idn.created_on,'%M %Y') = '"+MonthAmount+"' and  u.id=idn.created_by and idn.distributor_id = cd.distributor_id and idn.created_on between "+Utilities.getSQLFromDateTime(FromDate)+" and "+Utilities.getSQLToDateTime(ToDate)+ where+" group by delivery_id,sap_order_no order by idn.created_on desc";
			                System.out.println("sql4 ===========================================> "+Sql);
			                ResultSet rs2 = s2.executeQuery(Sql);
			                while( rs2.next() ){
			                	
			                	%>
			                	
			                	<tr>
			    					<td ><%=rs2.getLong("sap_order_no")%></td>
			    					<td ><%=rs2.getLong("invoice_no")%></td>
			    					<td><%=Utilities.getDisplayDateTimeFormat(rs2.getTimestamp("created_on"))  %></td>
			    					<td style="text-align:right; padding-right:10px"><%=Utilities.getDisplayCurrencyFormatRounded(rs2.getDouble("amount")) %></td>
			    					 
			    		    	</tr>
			                	
			                	<%
			                	
			                }
			               
			           
			            
			            
			            %>
			            
			            
			                
			            
			            
			</table>
			</li>
			</ul>
			<%} %>
		</td>
	</tr>
</table>


	



</div>
<%
c.close();
ds.dropConnection();
%>