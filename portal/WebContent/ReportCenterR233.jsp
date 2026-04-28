<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.pbc.common.Warehouse"%>

<style>
td{
font-size: 8pt;
}
</style>

<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 294;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");
Date EndDateOfMonth = Utilities.getEndDateByDate(EndDate);


Date StartDateOfMonth = Utilities.getStartDateByDate(StartDate);
Date StartDateOfYear = Utilities.getYearStartDateByDate(StartDate);

if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	StartDate = new Date();
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}

int TargetMonth = Utilities.getMonthNumberByDate(EndDate);
int TargetYear = Utilities.getYearByDate(EndDate);


//HOD


String HODIDs="";
long SelectedHODArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedHOD") != null){
	SelectedHODArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedHOD");
	HODIDs = Utilities.serializeForSQL(SelectedHODArray);
}

String WhereHOD = "";
if (HODIDs.length() > 0){
	WhereHOD = " and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
}


//RSM


String RSMIDs="";
long SelectedRSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRSM") != null){
	SelectedRSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRSM");
	RSMIDs = Utilities.serializeForSQL(SelectedRSMArray);
}

String WhereRSM = "";
if (RSMIDs.length() > 0){
	WhereRSM = " and distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
}


//Distributor

long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");           	
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
	WhereDistributors = " and distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
				<thead>
					   
					    <tr style="font-size:11px;">
						   	<th colspan="2">&nbsp;</th>
						   	
						   	<th colspan="2" style="text-align:center; background-color:#F7FFF7">Day</th>
						   	
						   	<th colspan="2" style="text-align:center; background-color:#FFFFFF;">BOM Avg. Daily Req.</th>
						   	
						   	<th colspan="5" style="text-align:center; background-color:#FFFFE6; ">MTD</th>
						   	<th colspan="5" style="text-align:center; background-color:#FAFAF8;">YTD</th>
						   	
					   </tr>
					   
					   <tr style="font-size:11px;">
						   	<th data-priority="1"  style="text-align:center; width:25%;">Distributor</th>
							<th data-priority="1"  style="text-align:center; width:5%;">City </th>
							<!-- <th data-priority="1"  style="text-align:center; width:5%;">Sales Manager</th>-->
						   	
						   	<th style="text-align:center; background-color:#F7FFF7;">Primary</th>
						   	<th style="text-align:center; background-color:#F7FFF7; ">Secondary</th>
						   	
						   	<th style="text-align:center; background-color:#FFFFFF;">Primary</th>
						   	<th style="text-align:center; background-color:#FFFFFF; ">Secondary</th>
						   	
						   	<th style="text-align:center; background-color:#FFFFE6;">Primary</th>
						   	<th style="text-align:center; background-color:#FFFFE6;">Secondary</th>
						   	<th style="text-align:center; background-color:#FFFFE6;">Target</th>
						   	<th style="text-align:center; background-color:#FFFFE6;">%P</th>
						   	<th style="text-align:center; background-color:#FFFFE6;">%S</th>
						   	
						   	<th style="text-align:center; background-color:#FAFAF8;">Primary</th>
						   	<th style="text-align:center; background-color:#FAFAF8;">Secondary</th>
						   	<th style="text-align:center; background-color:#FAFAF8;">Target</th>
						   	<th style="text-align:center; background-color:#FAFAF8;">%P</th>
						   	<th style="text-align:center; background-color:#FAFAF8;">%S</th>
					   </tr>
					   
					  </thead> 
						<tbody>
							<%
							/*
							Date MTDStartDate = Utilities.getStartDateByDate(StartDate);
							Date MTDEndDate = EndDate;
							
							Date YTDStartDate = Utilities.parseDate("01/01/"+Utilities.getYearByDate(StartDate));
							Date YTDEndDate = EndDate;
							*/
							
							//ResultSet rs = s.executeQuery("SELECT * FROM common_sd_groups");
							//while(rs.next()){
							%>
							
							<!-- <tr>
								<td colspan="24" style="background-color:#FAFAF8;"><%//=rs.getString("short_name") %> - <%//=rs.getString("long_name") %></td>
							</tr>-->
							<%
							
							int TotalDaysOfMonth = Utilities.getDayNumberByDate(EndDateOfMonth);
							int DaysOfMonth = Utilities.getDayNumberByDate(EndDate);
							
							double TotalDayPrimary = 0;
							double TotalDaySecondary = 0;
							double TotalMTDPrimary = 0;
							double TotalMTDSecondary = 0;
							double TotalMTDTarget = 0;
							double TotalYTDPrimary = 0;
							double TotalYTDSecondary = 0;
							double TotalYTDTarget = 0;
							
								//System.out.println("SELECT distributor_id, name, city FROM common_distributors where distributor_id in ( SELECT distinct distributor_id FROM inventory_sales_adjusted where created_on between "+Utilities.getSQLDate(StartDateOfYear)+" and "+Utilities.getSQLDate(EndDate)+" ) "+WhereDistributors+WhereHOD+WhereRSM);
								ResultSet rs1 = s2.executeQuery("SELECT distributor_id, name, city FROM common_distributors where distributor_id in ( SELECT distinct distributor_id FROM inventory_sales_adjusted where created_on between "+Utilities.getSQLDate(StartDateOfYear)+" and "+Utilities.getSQLDate(EndDate)+" ) "+WhereDistributors+WhereHOD+WhereRSM+"");
								while(rs1.next()){
									
									long DayPrimaryRawCases = 0;
									ResultSet rs3 = s3.executeQuery("SELECT sum(idnp.total_units/ipv.unit_per_sku) raw_cases FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where idn.created_on between "+Utilities.getSQLDate(EndDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and idn.distributor_id="+rs1.getLong("distributor_id"));
									if(rs3.first()){
										DayPrimaryRawCases = rs3.getLong(1);
									}
									
									TotalDayPrimary +=DayPrimaryRawCases;
											
									long DaySecondaryRawCases = 0;
									ResultSet rs4 = s3.executeQuery("SELECT sum(isap.total_units/ip.unit_per_case) raw_cases FROM inventory_sales_adjusted_products isap join inventory_packages ip on isap.cache_package_id = ip.id where isap.cache_created_on_date between "+Utilities.getSQLDate(EndDate)+" and "+Utilities.getSQLDate(EndDate)+" and isap.cache_distributor_id="+rs1.getLong("distributor_id"));
									if(rs4.first()){
										DaySecondaryRawCases = rs4.getLong(1);
									}
									TotalDaySecondary+=DaySecondaryRawCases;
									
									long MTDPrimaryRawCases = 0;
									ResultSet rs5 = s3.executeQuery("SELECT sum(idnp.total_units/ipv.unit_per_sku) raw_cases FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where idn.created_on between "+Utilities.getSQLDate(StartDateOfMonth)+" and "+Utilities.getSQLDateNext(EndDate)+" and idn.distributor_id="+rs1.getLong("distributor_id"));
									if(rs5.first()){
										MTDPrimaryRawCases = rs5.getLong(1);
									}
									TotalMTDPrimary += MTDPrimaryRawCases;
									
									
									long MTDSecondaryRawCases = 0;
									ResultSet rs6 = s3.executeQuery("SELECT sum(isap.total_units/ip.unit_per_case) raw_cases FROM inventory_sales_adjusted_products isap join inventory_packages ip on isap.cache_package_id = ip.id where isap.cache_created_on_date between "+Utilities.getSQLDate(StartDateOfMonth)+" and "+Utilities.getSQLDate(EndDate)+" and isap.cache_distributor_id="+rs1.getLong("distributor_id"));
									if(rs6.first()){
										MTDSecondaryRawCases = rs6.getLong(1);
									}
									TotalMTDSecondary += MTDSecondaryRawCases;
									
									long YTDPrimaryRawCases = 0;									
									ResultSet rs7 = s3.executeQuery("SELECT sum(idnp.total_units/ipv.unit_per_sku) raw_cases FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where idn.created_on between "+Utilities.getSQLDate(StartDateOfYear)+" and "+Utilities.getSQLDateNext(EndDate)+" and idn.distributor_id="+rs1.getLong("distributor_id"));
									if(rs7.first()){
										YTDPrimaryRawCases = rs7.getLong(1);
									}
									TotalYTDPrimary += YTDPrimaryRawCases;
									
									long YTDSecondaryRawCases = 0;
									ResultSet rs8 = s3.executeQuery("SELECT sum(isap.total_units/ip.unit_per_case) raw_cases FROM inventory_sales_adjusted_products isap join inventory_packages ip on isap.cache_package_id = ip.id where isap.cache_created_on_date between "+Utilities.getSQLDate(StartDateOfYear)+" and "+Utilities.getSQLDate(EndDate)+" and isap.cache_distributor_id="+rs1.getLong("distributor_id"));
									if(rs8.first()){
										YTDSecondaryRawCases = rs8.getLong(1);
									}
									TotalYTDSecondary += YTDSecondaryRawCases;
									
									long MTDTargetRawCases = 0;
									ResultSet rs9 = s3.executeQuery("SELECT sum(dtp.quantity) FROM distributor_targets dt join distributor_targets_packages dtp on dt.id = dtp.id where month = "+TargetMonth+" and year = "+TargetYear+" and distributor_id = "+rs1.getLong(1));
									if(rs9.next()){
										MTDTargetRawCases = rs9.getLong(1);
									}
									
									double YTDTargetRawCases = 0;
									ResultSet rs10 = s3.executeQuery("SELECT sum(dtp.quantity) FROM distributor_targets dt join distributor_targets_packages dtp on dt.id = dtp.id where month < "+TargetMonth+" and year = "+TargetYear+" and distributor_id = "+rs1.getLong(1));
									if(rs10.next()){
										YTDTargetRawCases = rs10.getLong(1);
									}
									
									
									double TargetOfDay = (MTDTargetRawCases * 1.0) / TotalDaysOfMonth;
									
									
									
									
									
									if(Utilities.isCurrentMonth(EndDate)){
										DaysOfMonth = Utilities.getDayNumberByDate(new Date());
									}
									
									double MTDTargetOfMonth = TargetOfDay * DaysOfMonth;
									
									
									TotalMTDTarget += MTDTargetOfMonth;
									/*System.out.println("\n\n\n=========="+MTDTargetRawCases+" / "+TotalDaysOfMonth);
									System.out.println("=========="+TargetOfDay+" * "+DaysOfMonth);
									System.out.println("=========="+MTDTargetOfMonth);
									*/
									
									double MTDPrimaryTarget = ((MTDPrimaryRawCases * 1.0) / MTDTargetOfMonth)* 100;
									double MTDSecondaryTarget = ((MTDSecondaryRawCases * 1.0) / MTDTargetOfMonth) * 100;
									
									YTDTargetRawCases = YTDTargetRawCases + MTDTargetOfMonth;
									
									TotalYTDTarget+=YTDTargetRawCases;
									
									double YTDPrimaryTarget = ((YTDPrimaryRawCases * 1.0) / YTDTargetRawCases) * 100;
									double YTDSecondaryTarget = ((YTDSecondaryRawCases * 1.0) / YTDTargetRawCases) * 100;
									
									
									
									double BOMRequiredPrimary = (MTDTargetRawCases - MTDPrimaryRawCases) / (TotalDaysOfMonth - DaysOfMonth);
									
									double BOMRequiredSecondary = (MTDTargetRawCases - MTDSecondaryRawCases) / (TotalDaysOfMonth - DaysOfMonth);
									%>
									
									<tr>
										<td><%=rs1.getLong("distributor_id") %> - <%=Utilities.truncateStringToMax(rs1.getString("name"), 20) %></td>
										<td><%=Utilities.truncateStringToMax(rs1.getString("city"),20) %></td>
										
										<!-- <td></td>  -->
										
										<td style="text-align: right; background-color:#F7FFF7;"><%=(DayPrimaryRawCases > 0)? Utilities.getDisplayCurrencyFormat(DayPrimaryRawCases): ""%></td>
										<td style="text-align: right; background-color:#F7FFF7;"><%=(DaySecondaryRawCases > 0)? Utilities.getDisplayCurrencyFormat(DaySecondaryRawCases): ""%></td>
										
										<td style="text-align: right;"><%=(BOMRequiredPrimary > 0)? Utilities.getDisplayCurrencyFormatRounded(BOMRequiredPrimary): ""%></td>
										<td style="text-align: right;"><%=(BOMRequiredSecondary > 0)? Utilities.getDisplayCurrencyFormatRounded(BOMRequiredSecondary): ""%></td>
										
										<td style="text-align: right; background-color:#FFFFE6;"><%=(MTDPrimaryRawCases > 0)? Utilities.getDisplayCurrencyFormat(MTDPrimaryRawCases): ""%></td>
										<td style="text-align: right; background-color:#FFFFE6;"><%=(MTDSecondaryRawCases > 0)? Utilities.getDisplayCurrencyFormat(MTDSecondaryRawCases): ""%></td>
										<td style="text-align: right; background-color:#FFFFE6;"><%=(MTDSecondaryRawCases > 0)? Utilities.getDisplayCurrencyFormatRounded(MTDTargetOfMonth): ""%></td>
										<td style="text-align: center; background-color:#FFFFE6;"><%=(MTDSecondaryRawCases > 0)? Utilities.getDisplayCurrencyFormatRounded(MTDPrimaryTarget)+"%": ""%></td>
										<td style="text-align: center; background-color:#FFFFE6;"><%=(MTDSecondaryRawCases > 0)? Utilities.getDisplayCurrencyFormatRounded(MTDSecondaryTarget)+"%": ""%></td>
										
										<td style="text-align: right; background-color:#FAFAF8;"><%=(YTDPrimaryRawCases > 0)? Utilities.getDisplayCurrencyFormat(YTDPrimaryRawCases): ""%></td>
										<td style="text-align: right; background-color:#FAFAF8;"><%=(YTDSecondaryRawCases > 0)? Utilities.getDisplayCurrencyFormat(YTDSecondaryRawCases): ""%></td>
										<td style="text-align: right; background-color:#FAFAF8;"><%=(MTDSecondaryRawCases > 0)? Utilities.getDisplayCurrencyFormatRounded(YTDTargetRawCases): ""%></td>
										<td style="text-align: center; background-color:#FAFAF8;"><%=(MTDSecondaryRawCases > 0)? Utilities.getDisplayCurrencyFormatRounded(YTDPrimaryTarget)+"%": ""%></td>
										<td style="text-align: center; background-color:#FAFAF8;"><%=(MTDSecondaryRawCases > 0)? Utilities.getDisplayCurrencyFormatRounded(YTDSecondaryTarget)+"%": ""%></td>
										
									</tr>
									
									<%
								}
							%>
									<tr>
										<td></td>
										<td></td>
										
										<!-- <td></td>  -->
										
										<%
										double TotalMTDPrimaryTarget = (TotalMTDPrimary / TotalMTDTarget) * 100;
										double TotalMTDSecondaryTarget = (TotalMTDSecondary / TotalMTDTarget) * 100;
										
										double TotalYTDPrimaryTarget = (TotalYTDPrimary / TotalYTDTarget) * 100;
										double TotalYTDSecondaryTarget = (TotalYTDSecondary / TotalYTDTarget) * 100;
										
										double TotalBOMRequiredPrimary = (TotalMTDTarget - TotalMTDPrimary) / (TotalDaysOfMonth - DaysOfMonth);
										double TotalBOMRequiredSecondary = (TotalMTDTarget - TotalMTDSecondary) / (TotalDaysOfMonth - DaysOfMonth);
										
										%>
										
										<td style="text-align: right; background-color:#F7FFF7;"><%=(TotalDayPrimary > 0)? Utilities.getDisplayCurrencyFormat(TotalDayPrimary): ""%></td>
										<td style="text-align: right; background-color:#F7FFF7;"><%=(TotalDaySecondary > 0)? Utilities.getDisplayCurrencyFormat(TotalDaySecondary): ""%></td>
										
										<td style="text-align: right;"><%=(TotalBOMRequiredPrimary > 0)? Utilities.getDisplayCurrencyFormatRounded(TotalBOMRequiredPrimary): ""%></td>
										<td style="text-align: right;"><%=(TotalBOMRequiredSecondary > 0)? Utilities.getDisplayCurrencyFormatRounded(TotalBOMRequiredSecondary): ""%></td>
										
										<td style="text-align: right; background-color:#FFFFE6;"><%=(TotalMTDPrimary > 0)? Utilities.getDisplayCurrencyFormat(TotalMTDPrimary): ""%></td>
										<td style="text-align: right; background-color:#FFFFE6;"><%=(TotalMTDSecondary > 0)? Utilities.getDisplayCurrencyFormat(TotalMTDSecondary): ""%></td>
										<td style="text-align: right; background-color:#FFFFE6;"><%=(TotalMTDTarget > 0)? Utilities.getDisplayCurrencyFormatRounded(TotalMTDTarget): ""%></td>
										<td style="text-align: center; background-color:#FFFFE6;"><%=(TotalMTDPrimaryTarget > 0)? Utilities.getDisplayCurrencyFormatRounded(TotalMTDPrimaryTarget): ""%>%</td>
										<td style="text-align: center; background-color:#FFFFE6;"><%=(TotalMTDSecondaryTarget > 0)? Utilities.getDisplayCurrencyFormatRounded(TotalMTDSecondaryTarget): ""%>%</td>
										
										<td style="text-align: right; background-color:#FAFAF8;"><%=(TotalYTDPrimary > 0)? Utilities.getDisplayCurrencyFormat(TotalYTDPrimary): ""%></td>
										<td style="text-align: right; background-color:#FAFAF8;"><%=(TotalYTDSecondary > 0)? Utilities.getDisplayCurrencyFormat(TotalYTDSecondary): ""%></td>
										<td style="text-align: right; background-color:#FAFAF8;"><%=(TotalYTDTarget > 0)? Utilities.getDisplayCurrencyFormatRounded(TotalYTDTarget): ""%></td>
										<td style="text-align: center; background-color:#FAFAF8;"><%=(TotalYTDPrimaryTarget > 0)? Utilities.getDisplayCurrencyFormatRounded(TotalYTDPrimaryTarget): ""%>%</td>
										<td style="text-align: center; background-color:#FAFAF8;"><%=(TotalYTDSecondaryTarget > 0)? Utilities.getDisplayCurrencyFormatRounded(TotalYTDSecondaryTarget): ""%>%</td>
										
									</tr>
							
							<%
							//}
							
							%>
							
						</tbody>
							
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