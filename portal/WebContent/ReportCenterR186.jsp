<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>


<script>

function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}

</script>

<style>
td{
font-size: 8pt;
}

</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 227;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}


Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();



//Date date = Utilities.parseDate(request.getParameter("Date"));


Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");




if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	//StartDate = new Date(); // add code of start of current month if first time report opens
	Calendar cc = Calendar.getInstance();   // this takes current date
    cc.set(Calendar.DAY_OF_MONTH, 1);
    StartDate = cc.getTime();
     
	
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}


int SelectedMonth = Utilities.getMonthNumberByDate(StartDate);
int SelectedYear = Utilities.getYearByDate(StartDate);



long SelectedPackagesArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPackages") != null){
   	SelectedPackagesArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPackages");           	
}

String PackageIDs = "";
String WherePackage = "";

if(SelectedPackagesArray!= null && SelectedPackagesArray.length > 0){
	for(int i = 0; i < SelectedPackagesArray.length; i++){
		if(i == 0){
			PackageIDs += SelectedPackagesArray[i]+"";
		}else{
			PackageIDs += ", "+SelectedPackagesArray[i]+"";
		}
	}
	WherePackage = " and package_id in ("+PackageIDs+") ";
}

//HOD


String HODIDs="";
long SelectedHODArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedHOD") != null){
	SelectedHODArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedHOD");
	HODIDs = Utilities.serializeForSQL(SelectedHODArray);
}

String WhereHOD = "";
String WhereHOD1 = "";
String WhereHOD2 = "";
if (HODIDs.length() > 0){
	WhereHOD = " and customer_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
	WhereHOD1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";
	WhereHOD2 = " and distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";
}


//RSM


String RSMIDs="";
long SelectedRSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRSM") != null){
	SelectedRSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRSM");
	RSMIDs = Utilities.serializeForSQL(SelectedRSMArray);
}

String WhereRSM = "";
String WhereRSM1 = "";
String WhereRSM2 = "";
if (RSMIDs.length() > 0){
	WhereRSM = " and customer_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
	WhereRSM1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
	WhereRSM2 = " and distributor_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
}

//SM


String SMIDs="";
long SelectedSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedSM") != null){
	SelectedSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedSM");
	SMIDs = Utilities.serializeForSQL(SelectedSMArray);
}

String WhereSM = "";
if (SMIDs.length() > 0){
	WhereSM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
}

//TDM


String TDMIDs="";
long SelectedTDMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedTDM") != null){
	SelectedTDMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedTDM");
	TDMIDs = Utilities.serializeForSQL(SelectedTDMArray);
}

String WhereTDM = "";
String WhereTDM1 = "";
String WhereTDM2 = "";
if (TDMIDs.length() > 0){
	WhereTDM = " and customer_id in(SELECT distributor_id FROM common_distributors where tdm_id in ("+TDMIDs+")) ";
	WhereTDM1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where tdm_id in ("+TDMIDs+")) ";
	WhereTDM2 = " and distributor_id in(SELECT distributor_id FROM common_distributors where tdm_id in ("+TDMIDs+")) ";
}

//ASM


String ASMIDs="";
long SelectedASMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedASM") != null){
	SelectedASMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedASM");
	ASMIDs = Utilities.serializeForSQL(SelectedASMArray);
}

String WhereASM = "";
if (ASMIDs.length() > 0){
	WhereASM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
}

Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);


long SelectedBrandArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedBrands") != null){
	   SelectedBrandArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedBrands");           	
}


String BrandIDs = "";
String WhereBrand = "";

if(SelectedBrandArray!= null && SelectedBrandArray.length > 0){
	for(int i = 0; i < SelectedBrandArray.length; i++){
		if(i == 0){
			BrandIDs += SelectedBrandArray[i]+"";
		}else{
			BrandIDs += ", "+SelectedBrandArray[i]+"";
		}
		
	}

	WhereBrand = " and ipv.brand_id in ("+BrandIDs+") ";
}





long DistributorID = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
	DistributorID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
}
String WhereCustomerID ="";
String WhereCustomerID1 ="";
String WhereCustomerID2 ="";
if (DistributorID != 0){
	WhereCustomerID = " and customer_id in ("+DistributorID+") ";
	WhereCustomerID1 = " and isa.distributor_id in ("+DistributorID+") ";
	WhereCustomerID2 = " and distributor_id in ("+DistributorID+") ";
}else{
	WhereCustomerID = " and customer_id in ("+DistributorIDs+") ";
	WhereCustomerID1 = " and isa.distributor_id in ("+DistributorIDs+") ";
	WhereCustomerID2 = " and distributor_id in ("+DistributorIDs+") ";
	
}


//GTM Category


String GTMCategoryIDs="";
long SelectedGTMCategoryArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedGTMCategory") != null){
	SelectedGTMCategoryArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedGTMCategory");
	GTMCategoryIDs = Utilities.serializeForSQL(SelectedGTMCategoryArray);
}

String WhereGTMCategory = "";
String WhereGTMCategory1 = "";
String WhereGTMCategory2 = "";
String WhereGTMCategory3 = "";
if (GTMCategoryIDs.length() > 0){
	WhereGTMCategory = " and customer_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";	
	WhereGTMCategory1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";
	WhereGTMCategory2 = " and tab1.distributor_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";
	WhereGTMCategory3 = " and distributor_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";
}



%>





<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Revenue Per Case</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
				<thead>
					<tr>
						<td colspan="2" style="text-align: center">&nbsp;</td>
						<td colspan="4" style="text-align: center">Discounts</td>
						<td colspan="3" style="text-align: center">Other Costs</td>
						<td colspan="1" style="text-align: center">&nbsp;</td>
						<td colspan="2" style="text-align: center">Converted Cases</td>
					</tr>
					<tr style="font-size:11px;">
						<th style="width: 20%">&nbsp;</th>
						<th style="width: 7.2%">Gross Revenue</th>
						<th style="width: 7.2%">Sales Promotion</th>
						<th style="width: 7.2%">Upfront Discount</th>
						<th style="width: 7.2%">Retailer - Variable</th>
						<th style="width: 7.2%">Retailer - Fixed</th>
						<th style="width: 7.2%">Freight</th>
						<th style="width: 7.2%">Unloading</th>
						<th style="width: 7.2%">Haulage</th>
						<th style="width: 7.2%">Net Revenue</th>
						<th style="width: 7.2%">Primary</th>
						<th style="width: 7.2%">Secondary</th>
					</tr>
				</thead>
				
				<tbody>
				
					<%
					
					double TotalGrossRevenue = 0;
					double TotalSalesPromotion = 0;
					double TotalUpFrontDiscount = 0;
					double TotalPerCaseDiscount = 0;
					double TotalFixedDiscount = 0;
					double TotalFreight = 0;
					double TotalUnloading = 0;
					double TotalHaulageDiscount = 0;
					double TotalCasesSold = 0;
					double TotalNetRevenue = 0;
					double TotalRate = 0;
					double TotalSecondaryCC = 0;
					double TotalSecondaryDiscount = 0;

					
					String SQL = "SELECT customer_id, (SELECT name FROM common_distributors where distributor_id=customer_id) customer_name, sum(gross_value) gross_revenue, sum(upfront_discount) upfront_discount, sum(free_stock) sales_promotion, sum(freight) freight, sum(unloading) unloading, package_id, product_type_id FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate )+" "+WhereHOD+WhereRSM+WhereTDM+WhereCustomerID+" and product_type_id in (1, 2, 3, 4) group by customer_id";
					//System.out.println(SQL);
					ResultSet rs = s.executeQuery(SQL);
					while(rs.next()){
						
						long CustomerID = rs.getLong("customer_id");
						double UpFrontDiscount = rs.getDouble("upfront_discount") * (-1);
						double Freight = rs.getDouble("freight") * (-1);
						double Unloading = rs.getDouble("unloading") * (-1);
						
						
						TotalGrossRevenue += rs.getDouble("gross_revenue");
						TotalSalesPromotion += rs.getDouble("sales_promotion");
						TotalUpFrontDiscount += UpFrontDiscount;
						
						
						//////////////////////////////////////////Cases Sold ////////////////////////////////////////////
										
						double CasesSold = 0;
						ResultSet rs6 = s3.executeQuery("select sum(bppi.quantity), sum(((bppi.quantity*ip.unit_per_case)*ip.liquid_in_ml)/ip.conversion_rate_in_ml) cc from peplogs.bi_percase_price_invoice bppi join inventory_packages ip on bppi.package_id = ip.id where customer_id = "+CustomerID+" and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
						if (rs6.first()){
							CasesSold = rs6.getDouble(2);
						}
						
						///////////////////////////////////////////////////////////////////////////////////////////////////////
						
						TotalCasesSold += CasesSold;
						
					%>
				
					<tr>
						<td><%=CustomerID%> - <%=Utilities.truncateStringToMax(rs.getString("customer_name"), 22)%></td>
						<td style="text-align: right"><% if( rs.getDouble("gross_revenue") > 0 ) out.print(Utilities.getDisplayCurrencyFormatOneDecimal(rs.getDouble("gross_revenue")/CasesSold)); %></td>
						<td style="text-align: right"><% if( rs.getDouble("sales_promotion") > 0 ) out.print(Utilities.getDisplayCurrencyFormatOneDecimal(rs.getDouble("sales_promotion")/CasesSold)); %></td>
						<td style="text-align: right"><% if( UpFrontDiscount > 0 ) out.print(Utilities.getDisplayCurrencyFormatOneDecimal(UpFrontDiscount/CasesSold)); %></td>
						
						<%
						////////////////////////////////////////// per case discount //////////////////////////////////////////
						int PackageID = rs.getInt("package_id");
						int TypeID = rs.getInt("product_type_id");
						double GrossValue = 0;
						
						Date CurrentDate = StartDate;
						
						double SecondaryCC = 0;
						ResultSet rs17 = s3.executeQuery("SELECT sum(isap.total_units*ipv.liquid_in_ml)/6000 FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isap.is_promotion = 0 and distributor_id = "+CustomerID);
						if (rs17.first()){
							SecondaryCC += rs17.getDouble(1);
						}
						TotalSecondaryCC += SecondaryCC;
						
						while(CurrentDate.before(Utilities.getDateByDays(EndDate,1))){
							
							ResultSet rs16 = s3.executeQuery("select sum(discount_value) from ( "+
									 " select package_id, brand_id, sum(qty*discounted) discount_value, (select ip.lrb_type_id from inventory_products ip where ip.category_id =1 and ip.package_id = tab1.package_id and ip.brand_id = tab1.brand_id) product_type_id from ( "+
											 " select isa.outlet_id, ipv.package_id, ipv.brand_id, sum(isap.raw_cases) qty, ( "+
											 " select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where date("+Utilities.getSQLDate(CurrentDate)+") between s.activated_on and s.deactivated_on and date("+Utilities.getSQLDate(CurrentDate)+") between sp.valid_from and sp.valid_to and sp.package = ipv.package_id  and sp.brand_id = ipv.brand_id and sp.brand_id != 0 and s.outlet_id = isa.outlet_id "+
											  " union all "+
											 " select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id join inventory_products ip on sp.package = ip.package_id and ip.category_id = 1 where date("+Utilities.getSQLDate(CurrentDate)+") between s.activated_on and s.deactivated_on and date("+Utilities.getSQLDate(CurrentDate)+") between sp.valid_from and sp.valid_to and sp.package = ipv.package_id and sp.brand_id = 0 and s.outlet_id = isa.outlet_id limit 1 "+
											  " ) discounted "+
											  " from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where 1=1 "+/*WhereHOD1+WhereRSM1+*/" and isa.distributor_id = "+CustomerID+" "+/*WhereGTMCategory1+*/" and isa.created_on between date("+Utilities.getSQLDate(CurrentDate)+") and date("+Utilities.getSQLDateNext(CurrentDate)+") and isap.is_promotion = 0 group by isa.outlet_id, ipv.package_id, ipv.brand_id having discounted is not null "+
											  " ) tab1 group by package_id, brand_id "+
											  " ) tab2 "/*where package_id = "+PackageID+" and product_type_id = "+TypeID*/);
							
							if(rs16.first()){
								GrossValue +=rs16.getDouble(1);
							}
							
							CurrentDate = Utilities.getDateByDays(CurrentDate,1);
						}
						
						///////////////////////////////////////////////////////////////////////////////////////////////////////
						TotalSecondaryDiscount += GrossValue;
						
						double PerCaseDiscount = 0;
						if (SecondaryCC != 0){
							PerCaseDiscount = (GrossValue/SecondaryCC);
						}
						
						
						//TotalPerCaseDiscount += PerCaseDiscount;
						
						%>						
						
						<td style="text-align: right"><% if( PerCaseDiscount != 0 ) out.print(Utilities.getDisplayCurrencyFormatOneDecimal(PerCaseDiscount)); %></td>
						
						<%
						
						////////////////////////////////////////// fixed discount /////////////////////////////////////////////
						
						double FixedDiscount = 0;
						double GrossValue2 = 0;
						CurrentDate = StartDate;
						
						while(CurrentDate.before(Utilities.getDateByDays(EndDate,1))){
							
							ResultSet rs16 = s3.executeQuery("select sum(fixed_company_share)/30 from ( "+
									" SELECT fixed_company_share, (select distributor_id from common_outlets_distributors_view where outlet_id = co.id limit 1) distributor_id FROM sampling s join common_outlets co on s.outlet_id = co.id where s.active = 1 and "+Utilities.getSQLDate(CurrentDate)+" between s.fixed_valid_from and s.fixed_valid_to and s.fixed_company_share != 0  "+
									" ) tab1 where distributor_id =  "+CustomerID/* where 1=1 "+WhereHOD2+WhereRSM2+WhereCustomerID2+WhereGTMCategory2*/);
							
							if(rs16.first()){
								GrossValue2 +=rs16.getDouble(1);
							}
							
							
							
							CurrentDate = Utilities.getDateByDays(CurrentDate,1);
						}
						
						///////////////////////////////////////////////////////////////////////////////////////////////////////
						FixedDiscount = GrossValue2;
						double FixedDiscountRate = 0;
						if (SecondaryCC != 0){
							FixedDiscountRate = FixedDiscount/SecondaryCC;
						}
						
						
						TotalFixedDiscount += FixedDiscount;
						TotalFreight += Freight;
						TotalUnloading += Unloading;
						%>
						
						<td style="text-align: right"><% if( FixedDiscountRate != 0 ) out.print(Utilities.getDisplayCurrencyFormatOneDecimal(FixedDiscountRate)); %></td>
						<td style="text-align: right"><% if( Freight != 0 ) out.print(Utilities.getDisplayCurrencyFormatOneDecimal(Freight/CasesSold)); %></td>
						<td style="text-align: right"><% if( Unloading != 0 ) out.print(Utilities.getDisplayCurrencyFormatOneDecimal(Unloading/CasesSold)); %></td>
						
						<%
						
						////////////////////////////////////////// haulage discount ////////////////////////////////////////////
						
						double HaulageDiscount = 0;
						ResultSet rs72 = s3.executeQuery("SELECT sum(freight_amount) FROM inventory_delivery_note where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+/*WhereHOD2+WhereRSM2+*/" and distributor_id = "+CustomerID+" "/*+WhereGTMCategory3*/);
						if(rs72.first()){
							HaulageDiscount = rs72.getDouble(1);
						}
						
						///////////////////////////////////////////////////////////////////////////////////////////////////////
						
						TotalHaulageDiscount += HaulageDiscount;
						
						%>
						
						<td style="text-align: right"><% if( HaulageDiscount > 0 ) out.print(Utilities.getDisplayCurrencyFormatOneDecimal(HaulageDiscount/CasesSold)); %></td>
						
						
						<%
						
						double NetRevenue = 0;
						NetRevenue = rs.getDouble("gross_revenue") - (rs.getDouble("sales_promotion")+(rs.getDouble("upfront_discount")*-1)+(rs.getDouble("freight")*-1)+(rs.getDouble("unloading")*-1)+HaulageDiscount);
						TotalNetRevenue += NetRevenue;
						%>
						
						<%
						double Rate = (NetRevenue / CasesSold)-PerCaseDiscount-FixedDiscountRate;
						//TotalRate += Rate;
						%>
						<td style="text-align: right"><% if( Rate != 0 ) out.print(Utilities.getDisplayCurrencyFormatOneDecimal(Rate)); %></td>
						<td style="text-align: right"><% if( CasesSold != 0 ) out.print(Utilities.getDisplayCurrencyFormatOneDecimal(CasesSold)); %></td>
						<td style="text-align: right"><% if( SecondaryCC != 0 ) out.print(Utilities.getDisplayCurrencyFormatOneDecimal(SecondaryCC)); %></td>
						
					</tr>
					<%
					}
					
					double TotalSecondaryRate = 0;
					if (TotalSecondaryCC != 0){
						TotalSecondaryRate = TotalSecondaryDiscount / TotalSecondaryCC;
					}
					
					double TotalFixedRate = 0;
					if (TotalSecondaryCC != 0){
						TotalFixedRate = TotalFixedDiscount / TotalSecondaryCC;
					}
					
					double AllNet = 0;
					if (TotalCasesSold != 0){
						AllNet = (TotalNetRevenue/TotalCasesSold) - TotalSecondaryRate - TotalFixedRate;
					}
					%>
					
					<tr>
						<th>Total</th>
						<th style="text-align: right"><%=Utilities.getDisplayCurrencyFormatOneDecimal(TotalGrossRevenue/TotalCasesSold)%></th>
						<th style="text-align: right"><%=Utilities.getDisplayCurrencyFormatOneDecimal(TotalSalesPromotion/TotalCasesSold)%></th>
						<th style="text-align: right"><%=Utilities.getDisplayCurrencyFormatOneDecimal(TotalUpFrontDiscount/TotalCasesSold)%></th>
						<th style="text-align: right"><%=Utilities.getDisplayCurrencyFormatOneDecimal(TotalSecondaryRate)%></th>
						<th style="text-align: right"><%=Utilities.getDisplayCurrencyFormatOneDecimal(TotalFixedRate)%></th>
						<th style="text-align: right"><%=Utilities.getDisplayCurrencyFormatOneDecimal(TotalFreight/TotalCasesSold)%></th>
						<th style="text-align: right"><%=Utilities.getDisplayCurrencyFormatOneDecimal(TotalUnloading/TotalCasesSold)%></th>
						<th style="text-align: right"><%=Utilities.getDisplayCurrencyFormatOneDecimal(TotalHaulageDiscount/TotalCasesSold)%></th>
						<th style="text-align: right"><%=Utilities.getDisplayCurrencyFormatOneDecimal(AllNet)%></th>
						<th style="text-align: right"><%=Utilities.getDisplayCurrencyFormatOneDecimal(TotalCasesSold)%></th>
						<th style="text-align: right"><%=Utilities.getDisplayCurrencyFormatOneDecimal(TotalSecondaryCC)%></th>

					</tr>
					
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