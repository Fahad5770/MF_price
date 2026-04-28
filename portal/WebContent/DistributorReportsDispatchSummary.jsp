<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>

<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 70;

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

Date StartDate = (Date)session.getAttribute("SR1StartDate");
Date EndDate = (Date)session.getAttribute("SR1EndDate");

if(session.getAttribute("SR1StartDate") == null){
	StartDate = new Date();
}

if(session.getAttribute("SR1EndDate") == null){
	EndDate = new Date();
}

//out.print("StartDate = "+StartDate);
//out.print("EndDate = "+EndDate);

long SelectedDistributorsArray[] = null;
if (session.getAttribute("SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute("SR1SelectedDistributors");           	
}else{
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	}
}


String DistributorIDs = "";
String WhereDistributors = "";
if(SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0){
	for(int i = 0; i < SelectedDistributorsArray.length ; i++){
		if(i == 0){
			DistributorIDs += SelectedDistributorsArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorsArray[i];
		}
	}
}


long SelectedVehicleArray[] = null;
if (session.getAttribute("SR1SelectedVehicles") != null){
	SelectedVehicleArray = (long[])session.getAttribute("SR1SelectedVehicles");           	
}


String VehicleIDs = "";
String WhereVehicle = "";
if(SelectedVehicleArray != null && SelectedVehicleArray.length > 0){
	for(int i = 0; i < SelectedVehicleArray.length; i++){
		if(i == 0){
			VehicleIDs += SelectedVehicleArray[i];
		}else{
			VehicleIDs += ", "+SelectedVehicleArray[i];
		}
	}
	WhereVehicle = " and isd.vehicle_id in ("+VehicleIDs+") ";
}

long SelectedEmployeeArray[] = null;
if (session.getAttribute("SR1SelectedEmployees") != null){
	SelectedEmployeeArray = (long[])session.getAttribute("SR1SelectedEmployees");           	
}
String EmployeeIDs = "";
String WhereEmployee = "";
if(SelectedEmployeeArray != null && SelectedEmployeeArray.length > 0){
	for(int i = 0; i < SelectedEmployeeArray.length; i++){
		if(i == 0){
			EmployeeIDs += SelectedEmployeeArray[i];
		}else{
			EmployeeIDs += ", "+SelectedEmployeeArray[i];
		}
	}
	WhereEmployee = " and isd.driver_id in ("+EmployeeIDs+") ";
}

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Dispatch Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size: 11px;">
							<th data-priority="1" style="text-align:center; width: 13%">Date</th>
							<th data-priority="1" style="text-align:center; width: 12%">Dispatch#</th>
							<th data-priority="1" style="text-align:center; width: 20%">Vehicle</th>
							<th data-priority="1" style="text-align:center; width: 25%">Driver</th>
							<th data-priority="1" style="text-align:center; width: 15%">Dispatch Amount</th>
							<th data-priority="1" style="text-align:center; width: 15%">Adjusted Amount</th>
					    </tr>
						<tr style="background:#ececec">
	   	            		<td align="left">Adjusted</td>
	   	            		<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="6">&nbsp;</td>
	   	            	</tr>
							<%
							double TotalDispatchAmount =0;
							double TotalAdjustedAmount =0;
							
							ResultSet rs = s.executeQuery("SELECT isd.id, isd.adjusted_on, isd.vehicle_id, (select vehicle_no from distribtuor_vehicles where id = isd.vehicle_id) vehicle_label, isd.driver_id, (select name from distributor_employees where id = isd.driver_id) driver_name, sum(isi.net_amount) dispatch_amount, sum(isa.net_amount) adjusted_amount FROM inventory_sales_dispatch isd join inventory_sales_dispatch_invoices_view isdi on isd.id = isdi.id join inventory_sales_adjusted isa on isdi.sales_id = isa.id join inventory_sales_invoices isi on isa.id = isi.id where isd.distributor_id in ("+DistributorIDs+") and isd.is_adjusted = 1 and isd.adjusted_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereVehicle+" "+WhereEmployee+" group by isd.id");
							while(rs.next()){
								double DispatchAmount = rs.getDouble("dispatch_amount");
								double AdjustedAmount = rs.getDouble("adjusted_amount");
								
								TotalDispatchAmount += DispatchAmount;
								TotalAdjustedAmount += AdjustedAmount;
							%>
							<tr style="font-size: 12px;">
		    					<td style="text-align: left;"><%= Utilities.getDisplayDateFormat(rs.getDate(2)) %></td>
		    					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=rs.getLong(1) %></td>
		    					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=rs.getString("vehicle_label") %></td>
		    					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=rs.getString("driver_name") %></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.getDisplayCurrencyFormatRounded(DispatchAmount) %></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.getDisplayCurrencyFormatRounded(AdjustedAmount) %></td>
		    		    	</tr>
							<%
							}
							%>
							<tr style="font-size: 12px;">
		    					<td style="text-align: right;" colspan="4"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.getDisplayCurrencyFormatRounded(TotalDispatchAmount) %></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.getDisplayCurrencyFormatRounded(TotalAdjustedAmount) %></td>
		    		    	</tr>
						<tr style="background:#ececec">
	   	            		<td align="left">Unadjusted</td>
	   	            		<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="6">&nbsp;</td>
	   	            	</tr>	
							<%
							double TotalDispatchAmountUnadjusted =0;
							
							ResultSet rs2 = s.executeQuery("SELECT isd.id, isd.created_on, isd.vehicle_id, (select vehicle_no from distribtuor_vehicles where id = isd.vehicle_id) vehicle_label, isd.driver_id, (select name from distributor_employees where id = isd.driver_id) driver_name, sum(isi.net_amount) dispatch_amount FROM inventory_sales_dispatch isd join inventory_sales_dispatch_invoices_view isdi on isd.id = isdi.id join inventory_sales_invoices isi on isdi.sales_id = isi.id where isd.distributor_id in ("+DistributorIDs+") and isd.is_adjusted = 0 and isd.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereVehicle+" "+WhereEmployee+" group by isd.id");
							while(rs2.next()){
								double DispatchAmount = rs2.getDouble("dispatch_amount");
								
								TotalDispatchAmountUnadjusted += DispatchAmount;
							%>
							<tr style="font-size: 12px;">
		    					<td style="text-align: left;"><%= Utilities.getDisplayDateFormat(rs2.getDate(2)) %></td>
		    					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=rs2.getLong(1) %></td>
		    					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=rs2.getString("vehicle_label") %></td>
		    					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=rs2.getString("driver_name") %></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.getDisplayCurrencyFormatRounded(DispatchAmount) %></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    		    	</tr>
							<%
							}
							%>
							<tr style="font-size: 12px;">
		    					<td style="text-align: right;" colspan="4"></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.getDisplayCurrencyFormatRounded(TotalDispatchAmountUnadjusted) %></td>
		    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"></td>
		    		    	</tr>	   	            				
				</table>
		</td>
	</tr>
</table>

	</li>	
</ul>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>