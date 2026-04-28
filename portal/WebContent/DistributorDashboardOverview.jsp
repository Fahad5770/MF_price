<%@page import="com.pbc.common.User"%>
<%@page import="com.pbc.employee.EmployeeHierarchy"%>
<%@page import="com.pbc.employee.OrderBookerDashboard"%>
<%@page import="com.pbc.distributor.DistributorDashboard"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.Date"%>
<jsp:useBean id="bean" class="com.pbc.outlet.OutletDashboard" scope="page"/>
<jsp:setProperty name="bean" property="*"/>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.sql.SQLException"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}

if(UserAccess.isAuthorized(44, SessionUserID, request) == false){
	//System.out.println("hello");
	response.sendRedirect(Utilities.getAccessDeniedPageURL(request));
}

long DistributorCode = Utilities.parseLong(request.getParameter("DistributorCode"));

long EmployeeCode = 2300;//Utilities.parseLong(request.getParameter("EmployeeCode"));

if (UserAccess.isDistributorAllowed(DistributorCode, SessionUserID, 44) == false){
	response.sendRedirect(Utilities.getAccessDeniedPageURL(request));
}

Datasource ds2 = new Datasource();
ds2.createConnectionToReplica();
Statement s3 = ds2.createStatement();

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();

//populateData(c, c2, StartDate, EndDate, DistributorCode);

DistributorDashboard Distributor = new DistributorDashboard();

//int DateAttributes[] = Distributor.getDateAttributesInNumbers(Distributor.getSalesMinMaxDates(DistributorCode)[0]);
//int ChartYear = DateAttributes[0];
//int ChartMonth = DateAttributes[1] - 1;
//int ChartDay = DateAttributes[2];


String LiftingVsSecondaryChartDates = "";
String LiftingVsSecondaryChartLiftingValues = "";
String LiftingVsSecondaryChartSecondaryValues = "";
Date ListDate[] = Utilities.getPastMonthsInDate(new Date(), 12);
int counter = 1;

double TotalLifting = 0;
double TotalSales = 0;

for(Date curdate: ListDate){
	
	String seperator = ", ";
	if(counter == 1){
		seperator = "";
	}	
	
	
	Calendar cal = Calendar.getInstance();
	cal.setTime(curdate);
	
	int CurrentMonth = cal.get(Calendar.MONTH);
	int CurrentYear = cal.get(Calendar.YEAR);
	
	Date StartDate = Utilities.getStartDateByMonth(CurrentMonth, CurrentYear);
	Date EndDate = Utilities.getEndDateByMonth(CurrentMonth, CurrentYear);
	
	//Utilities.getDisplayDateFormat(val)
	
	LiftingVsSecondaryChartDates += seperator+ "'"+Utilities.getDisplayDateShortMonthYearFormat(curdate)+"'";
	
	ResultSet rs5 = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id = "+DistributorCode+" and created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate));
	if(rs5.first()){
		LiftingVsSecondaryChartLiftingValues += seperator + Math.round(rs5.getDouble(1));
		TotalLifting += rs5.getDouble(1);
	}
	
	
	//System.out.println("select sum(isap.total_units * ip.liquid_per_unit)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products ip on isap.product_id = ip.id where distributor_id = "+DistributorCode+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));
	ResultSet rs6 = s3.executeQuery("select sum(isap.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where distributor_id = "+DistributorCode+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));
	if(rs6.first()){
		
		LiftingVsSecondaryChartSecondaryValues += seperator + Math.round(rs6.getDouble(1));
		TotalSales += rs6.getDouble(1);
	}
	
	
	
	counter++;
}

Date StartDate = Utilities.getDateByDays(-90);

Date EndDate = new Date();



double PieDeskSale = 0;
ResultSet rs7 = s3.executeQuery("select sum(isap.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where distributor_id = "+DistributorCode+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.type_id=1");
if(rs7.first()){
	PieDeskSale = rs7.getDouble(1);
}

double PieSpotSelling = 0;
ResultSet rs8 = s3.executeQuery("select sum(isap.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where distributor_id = "+DistributorCode+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.type_id=2");
if(rs8.first()){
	PieSpotSelling = rs8.getDouble(1);
}

double PieMobileOrderBooking = 0;
ResultSet rs9 = s3.executeQuery("select sum(isap.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where distributor_id = "+DistributorCode+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.type_id=3");
if(rs9.first()){
	PieMobileOrderBooking = rs9.getDouble(1);
}

StartDate = Utilities.getDateByDays(-15);
String KPIDropSizeList = "";
String KPIDateList = "";
String KPISKUList = "";
String KPIProductivityList = "";

counter = 1;
ResultSet rs10 = s3.executeQuery("SELECT date, drop_size, sku_per_bill, productivity FROM bi_distributors_kpis where date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate) );
while(rs10.next()){
	String seperator = ", ";
	if(counter == 1){
		seperator = "";
	}
	KPIDropSizeList += seperator + rs10.getDouble("drop_size");
	KPIDateList += seperator + "''";
	KPISKUList += seperator + rs10.getDouble("sku_per_bill");
	KPIProductivityList += seperator + rs10.getDouble("productivity");
	counter++;
}

int OutletUniverse = 0;
ResultSet rs11 = s3.executeQuery("select count(*) from common_outlets where cache_distributor_id = "+DistributorCode);
if(rs11.first()){
	OutletUniverse = rs11.getInt(1);
}

int BeatPlans = 0;
ResultSet rs12 = s3.executeQuery("SELECT count(*) FROM pep.distributor_beat_plan where distributor_id = "+DistributorCode);
if(rs12.first()){
	BeatPlans = rs12.getInt(1);
}

int OrderBookers = 0;
ResultSet rs13 = s3.executeQuery("select count(distinct assigned_to) from distributor_beat_plan_users where id in (SELECT id FROM pep.distributor_beat_plan where distributor_id = "+DistributorCode+") and assigned_to is not null");
if(rs13.first()){
	OrderBookers = rs13.getInt(1);
}

int ActiveDevices = 0;
ResultSet rs14 = s3.executeQuery("select count(distinct uuid) from mobile_order where distributor_id = "+DistributorCode+" and created_on between from_days(to_days(curdate())-2) and from_days(to_days(curdate())+1)");
if(rs14.first()){
	ActiveDevices = rs14.getInt(1);
}
if (ActiveDevices > OrderBookers){
	ActiveDevices = OrderBookers;
}

int ZeroSales = 0;
ResultSet rs15 = s3.executeQuery("select count(*) from (select id, to_days(curdate())-to_days(ifnull((select date(max(created_on)) from inventory_sales_adjusted where outlet_id = tab1.id),'2013-01-01')) days_ago from ( select id from common_outlets where cache_distributor_id = "+DistributorCode+") tab1 having days_ago > 30) tab4");
if(rs15.first()){
	ZeroSales = rs15.getInt(1);
}

double ZeroSalesPercentage = (ZeroSales*1d/OutletUniverse*1d)*100;

double LiftingPerDay = TotalLifting/365;
double SalesPerDay = TotalSales/365;
double LiftingToSalesRatio = (TotalSales/TotalLifting) * 100;

double Top10Volume = 0;
double TotalVolume = 0;
ResultSet rs16 = s3.executeQuery("select sum(cc), (select sum(isap.liquid_in_ml)/6000 cc from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where distributor_id = "+DistributorCode+" and created_on between from_days(to_days(curdate())-90) and curdate()) total from (select isa.outlet_id, sum(isap.liquid_in_ml)/6000 cc from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where distributor_id = "+DistributorCode+" and created_on between from_days(to_days(curdate())-90) and curdate() group by outlet_id order by cc desc limit 10) tab1");
if(rs16.first()){
	Top10Volume = rs16.getDouble(1);
	TotalVolume = rs16.getDouble(2);
}
double Top10Percentage = (Top10Volume/TotalVolume)*100;


int DiscountedOutlets = 0;
ResultSet rs17 = s3.executeQuery("select count(*) from sampling where active = 1 and curdate() between activated_on and deactivated_on and outlet_id in (select id from common_outlets where cache_distributor_id = "+DistributorCode+")");
if(rs17.first()){
	DiscountedOutlets = rs17.getInt(1);
}

double DiscountedOutletsPercentage = (DiscountedOutlets*1d/OutletUniverse*1d) * 100;
//

String BackOrderSeries = "";
String BackOrderSeriesLabels = "";
String ReturnSeries = "";
String ReturnSeriesLabels = "";

double TotalBackorders = 0;
double TotalReturns = 0;

int icounter = 0;
for (int i =0; i < 15; i++){
	int difference = 0;
	difference = i - 14;
	
	Date CurrentDate = Utilities.getDateByDays(difference);
	
	if (Utilities.getDayOfWeekByDate(CurrentDate) != 6){ // Exclude Friday
		String seperator = ", ";
		if(icounter == 0){
			seperator = "";
		}	
		icounter++;
	
		double BackordersCC = 0;
		ResultSet rs18 = s3.executeQuery("SELECT sum( ((mopb.total_units * ipv.liquid_in_ml) / ipv.conversion_rate_in_ml)) cc FROM mobile_order_products_backorder mopb join inventory_products_view ipv on mopb.product_id = ipv.product_id join mobile_order mo on mopb.id = mo.id where mo.backordered_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and mo.distributor_id = "+DistributorCode+" ");
		if(rs18.first()){
			BackordersCC = rs18.getDouble(1);
		}
		TotalBackorders += BackordersCC;
		BackOrderSeries += (seperator + Math.round(BackordersCC));
		BackOrderSeriesLabels += (seperator + "''");
		double SalesReturnCC = 0;
		ResultSet rs19 = s3.executeQuery("SELECT sum( ((isdap.total_units * ipv.liquid_in_ml) / ipv.conversion_rate_in_ml)) cc FROM inventory_sales_dispatch_adjusted_products isdap join inventory_products_view ipv on isdap.product_id = ipv.product_id join inventory_sales_adjusted isa on isdap.invoice_id = isa.id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.distributor_id = "+DistributorCode+" and isdap.is_promotion = 0");
		if(rs19.first()){
			SalesReturnCC = rs19.getDouble(1);
		}
		TotalReturns += SalesReturnCC;
		ReturnSeries += (seperator + Math.round(SalesReturnCC));
		ReturnSeriesLabels += (seperator + "''");
	
	}
}

//;

//
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	
	
	
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="lib/highcharts301/js/highcharts.js"></script>
		<script src="js/lookups.js"></script>
		<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
	
	
	
	
	<script>
	
	var PJPMap;
	var PJPMarker;
	
	$(function () {
	    $('#container_sales').highcharts({
	        chart:{
	        	type: 'spline'
	        },
	    	title: {
	            text: '',
	            x: -30 //center
	        },
	        subtitle: {
	            text: '',
	            x: -20
	        },
	        xAxis: {
	            categories: [<%=LiftingVsSecondaryChartDates%>]
	        },
	        yAxis: {
	            title: {
	                text: 'Converted Cases'
	            },
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip: {
	            valueSuffix: ''
	        },
	        legend: {
	            layout: 'horizontal',
	            align: 'center',
	            verticalAlign: 'bottom',
	            borderWidth: 0
	        },
	        credits: {
	            enabled: false
	        },
	        
	        series: [
	                 
							{
							    name: 'Lifting',
							    data: [<%=LiftingVsSecondaryChartLiftingValues%>]
							},
							{
					            name: 'Secondary Sales',
					            data: [<%=LiftingVsSecondaryChartSecondaryValues%>]
					        }
	                 
	                 ]
	    });
	    /*
	    $('#container_sales2').highcharts({
	        chart:{
	        	type: 'spline'
	        },
	    	title: {
	            text: '',
	            x: -30 //center
	        },
	        subtitle: {
	            text: '',
	            x: -20
	        },
	        xAxis: {
	            categories: [<%=LiftingVsSecondaryChartDates%>]
	        },
	        yAxis: {
	            title: {
	                text: 'Converted Cases'
	            },
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip: {
	            valueSuffix: ''
	        },
	        legend: {
	            layout: 'horizontal',
	            align: 'center',
	            verticalAlign: 'bottom',
	            borderWidth: 0
	        },
	        credits: {
	            enabled: false
	        },
	        series: [
	                 
							{
							    name: 'Lifting',
							    data: [<%=LiftingVsSecondaryChartLiftingValues%>]
							}
	                 ]
	    });
	    */
	});
/*	
	$(function () {
	    $('#container_stock').highcharts({
	        chart:{
	        	type: 'spline'
	        },
	    	title: {
	            text: 'Stock',
	            x: -20 //center
	        },
	        subtitle: {
	            text: '',
	            x: -20
	        },
	        xAxis: {
                
                categories: [<%=KPIDateList%>]
            },
	        yAxis: {
	            title: {
	                text: ''
	            }
	        },
	        tooltip: {
	            valueSuffix: ''
	        },
	        legend: {	            
	            enabled: false 
	        },
	        credits: {
	            enabled: false
	        },
	        series: [
	                 
							{
							    name: 'Stock',
							    color: '#006400',
							    data: [<%=KPIDropSizeList%>]
							}
	                 
	                 ]
	    });
	});
	
*/
	$(function () {
	    $('#container_drop_size').highcharts({
	        chart:{
	        	type: 'spline'
	        },
	    	title: {
	            text: 'Drop Size',
	            x: -20 //center
	        },
	        subtitle: {
	            text: '',
	            x: -20
	        },
	        xAxis: {
                
                categories: [<%=KPIDateList%>]
            },
	        yAxis: {
	            title: {
	                text: ''
	            }
	        },
	        tooltip: {
	            valueSuffix: ''
	        },
	        legend: {	            
	            enabled: false 
	        },
	        credits: {
	            enabled: false
	        },
	        series: [
	                 
							{
							    name: 'Drop Size',
							    color: '#006400',
							    data: [<%=KPIDropSizeList%>]
							}
	                 
	                 ]
	    });
	});
	
	$(function () {
	    $('#container_sku_per_bill').highcharts({
	        chart:{
	        	type: 'spline'
	        },
	    	title: {
	            text: 'SKU/Bill',
	            x: -20 //center
	        },
	        subtitle: {
	            text: '',
	            x: -20
	        },
	        xAxis: {
	        	categories: [<%=KPIDateList%>]
	        },
	        yAxis: {
	        	min: 0,
	            title: {
	                text: ''
	            },
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip: {
	            valueSuffix: ''
	        },
	        legend: {	            
	            enabled: false 
	        },
	        credits: {
	            enabled: false
	        },
	        series: [
	                 
							{
							    name: 'SKU',
							    color: '#006400',
							    data: [<%=KPISKUList%>]
							}
	                 
	                 ]
	    });
	});
	
	
	$(function () {
	    $('#container_productivity').highcharts({
	        chart:{
	        	type: 'spline'
	        },
	    	title: {
	            text: 'Productivity',
	            x: -20 //center
	        },
	        subtitle: {
	            text: '',
	            x: -20
	        },
	        xAxis: {
	        	categories: [<%=KPIDateList%>]
	        },
	        yAxis: {
	        	min: 0,
	            title: {
	                text: ''
	            },
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip: {
	            valueSuffix: ''
	        },
	        legend: {	            
	            enabled: false 
	        },
	        credits: {
	            enabled: false
	        },
	        series: [
	                 
							{
							    name: 'Productivity',
							    color: '#006400',
							    data: [<%=KPIProductivityList%>]
							}
	                 
	                 ]
	    });
	});
	
	$(function () {
	    $('#container_back_order').highcharts({
	        chart:{
	        	type: 'spline'
	        },
	        title: {
	            text: '',
	            x: -20 //center
	        },
	        subtitle: {
	            text: '',
	            x: -20
	        },
	        xAxis: {
	        	categories: [<%=BackOrderSeriesLabels%>]
	        },
	        yAxis: {
	        	min: 0,
	            title: {
	                text: ''
	            },
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip: {
	            valueSuffix: ''
	        },
	        legend: {	            
	            enabled: false 
	        },
	        credits: {
	            enabled: false
	        },
	        series: [
	                 
							{
							    name: 'Backorders',
							    data: [<%=BackOrderSeries%>]
							}
	                 
	                 ]
	    });
	});
	
	$(function () {
	    $('#container_returns').highcharts({
	        chart:{
	        	type: 'spline'
	        },
	    	title: {
	            text: '',
	            x: -20 //center
	        },
	        subtitle: {
	            text: '',
	            x: -20
	        },
	        xAxis: {
	        	categories: [<%=ReturnSeriesLabels%>]
	        },
	        yAxis: {
	        	min: 0,
	            title: {
	                text: ''
	            },
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip: {
	            valueSuffix: ''
	        },
	        legend: {	            
	            enabled: false 
	        },
	        credits: {
	            enabled: false
	        },
	        series: [
	                 
							{
							    name: 'Returns',
							    data: [<%=ReturnSeries%>]
							}
	                 
	                 ]
	    });
	});
	
	$(function () { 

	    $(document).ready(function () {

	        // Build the chart
	        $('#container_sales_pie').highcharts({
	            chart: {
	                plotBackgroundColor: null,
	                plotBorderWidth: null,
	                plotShadow: false,
	                type: 'pie'
	            },
	            title: {
	                text: ''
	            },
	            tooltip: {
	                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	            },
	            plotOptions: {
	                pie: {
	                    allowPointSelect: true,
	                    cursor: 'pointer',
	                    dataLabels: {
	                        enabled: false
	                    },
	                    showInLegend: true
	                }
	            },
		        credits: {
		            enabled: false
		        },
		        legend:{
		        	itemStyle:{
		        		fontSize:'9px'
		        	}
		        },
	            series: [{
	                name: 'Outlets',
	                colorByPoint: true,
	                data: [{
	                    name: 'Zero Sale',
	                    y: <%=ZeroSales%>,
	                    sliced: true,
	                    selected: true,
	                    color: '#8b0000'
	                }, {
	                    name: 'Active Outlets	',
	                    y: <%=(OutletUniverse-ZeroSales)%>,
	                    color: '#006400'
	                }]
	            }]
	        });
	        
	        
	        $('#container_sales_pie1').highcharts({
	            chart: {
	                plotBackgroundColor: null,
	                plotBorderWidth: null,
	                plotShadow: false,
	                type: 'pie'
	            },
	            title: {
	                text: ''
	            },
	            tooltip: {
	                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	            },
	            plotOptions: {
	                pie: {
	                    allowPointSelect: true,
	                    cursor: 'pointer',
	                    dataLabels: {
	                        enabled: false
	                    },
	                    showInLegend: true
	                }
	            },
	            legend:{
		        	itemStyle:{
		        		fontSize:'9px'
		        	}
		        },
		        credits: {
		            enabled: false
		        },
	            series: [{
	                name: 'Outlets',
	                colorByPoint: true,
	                data: [{
	                    name: 'Discounted',
	                    y: <%=DiscountedOutlets%>
	                   
	                }, {
	                    name: 'Non-Discounted',
	                    y: <%=(OutletUniverse-DiscountedOutlets)%>
	                    
	                }]
	            }]
	        });
	        
	        
	        $('#container_sales_pie2').highcharts({
	            chart: {
	                plotBackgroundColor: null,
	                plotBorderWidth: null,
	                plotShadow: false,
	                type: 'pie'
	            },
	            title: {
	                text: ''
	            },
	            tooltip: {
	                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	            },
	            plotOptions: {
	                pie: {
	                    allowPointSelect: true,
	                    cursor: 'pointer',
	                    dataLabels: {
	                        enabled: false
	                    },
	                    showInLegend: true
	                }
	            },
		        credits: {
		            enabled: false
		        },
		        legend:{
		        	itemStyle:{
		        		fontSize:'9px'
		        	}
		        },
	            series: [{
	                name: 'Sale Type',
	                colorByPoint: true,
	                data: [ {
	                    name: 'Pre-Selling',
	                    y: <%=PieMobileOrderBooking%>,
	                    color: '#006400'
	                },{
	                    name: 'Spot Sales',
	                    y: <%=PieSpotSelling%>
	                },{
	                    name: 'Desk Sales',
	                    y: <%=PieDeskSale%>
	                }]
	            }]
	        });
	        
	        
	        $('#container_sales_pie3').highcharts({
	            chart: {
	                plotBackgroundColor: null,
	                plotBorderWidth: null,
	                plotShadow: false,
	                type: 'pie'
	            },
	            title: {
	                text: ''
	            },
	            tooltip: {
	                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	            },
	            plotOptions: {
	                pie: {
	                    allowPointSelect: true,
	                    cursor: 'pointer',
	                    dataLabels: {
	                        enabled: false
	                    },
	                    showInLegend: true
	                }
	            },
		        credits: {
		            enabled: false
		        },
		        legend:{
		        	itemStyle:{
		        		fontSize:'9px'
		        	}
		        },
	            series: [{
	                name: 'Volume',
	                colorByPoint: true,
	                data: [{
	                    name: 'Top 10 Outlets',
	                    y: <%=Math.round(Top10Volume)%>,
                    sliced: true,
                    selected: true
	                }, {
	                    name: 'Others',
	                    y: <%=Math.round(TotalVolume-Top10Volume)%>
	                }]
	            }]
	        });
	        
	        
	    });
	});
	
	
	/*$( document ).delegate("#OrderBookerDashboardPage", "pageinit", function() {
		
		var DistdbClickNameArray = new Array();
		DistdbClickNameArray = document.getElementsByName("OrderBookerDashboardOverviewDistdbClickName")
		for(var i =0;i<DistdbClickNameArray.length;i++){
			//alert(DistdbClickNameArray[i].id);
			$( "#OrderBookerDashboardOverviewDistdbClickID_"+DistdbClickNameArray[i].id ).dblclick(function() {				  
				  var htmlText = $(this).html().split("-");  //100669 - ABC
				  //alert(htmlText[0].trim());
				  $("#OrderBookerDashboardOverviewDistributorCode1").val(htmlText[0].trim());
				  document.getElementById("OrderBookerDashboardOverviewFormID1").submit();
				});
		}
		
		
		/*$( "#OrderBookerDashboardOverviewDistdbClickID" ).dblclick(function() {
			  //alert($(this).html());
			  var htmlText = $(this).html().split("-");  //100669 - ABC
			  alert(htmlText[0].trim());
			});)*/
	//});*/
	 
	
	function DistributorDashboardRedirect(DistID){		
		$("#OrderBookerDashboardOverviewDistributorCode1").val(DistID);
		  document.getElementById("OrderBookerDashboardOverviewFormID1").submit();
	}

	
	
	
	</script>
	
	
		
		<style>
		
		
		
		
			.radio_style
			  {
			      display: block;
			      width: 80px;
			      height: 50px;
			      background-repeat: no-repeat;
			      background-position: -231px 0px;
			  }
			  
			  .ui-table-reflow.ui-responsive{
			  	display:block;
			  }
			
				a{
					font-size: 12px;
				
				}
		</style>
		
		<script>
		
		
		 
		function init_map(){
			var myOptions = {zoom:10,center:new google.maps.LatLng(31.4187,73.0791),mapTypeId: google.maps.MapTypeId.ROADMAP};
			map = new google.maps.Map(document.getElementById('gmap_canvas'), myOptions);
			PJPMap = map;
			marker = new google.maps.Marker(
						{
							map: map,
							position: new google.maps.LatLng(31.4187,73.0791),
							title: 'Outlet',
						    icon: 'images/markers/letter_o.png' 
						}
					);
			PJPMarker = marker;
			
			infowindow = new google.maps.InfoWindow({content:'<strong>Outlet</strong><br>Faislabad<br>'});
			
			google.maps.event.addListener(marker, 'click', 
				function(){
					infowindow.open(map,marker);
				});
			
			infowindow.open(map,marker);
		}
		
		google.maps.event.addDomListener(window, 'load', init_map);
		
		function setLocation(Lat, Lng){
			var position = new google.maps.LatLng( Lat, Lng );
			PJPMap.setCenter(position);
			PJPMap.setZoom(15);
			PJPMarker.setPosition(position);
		}
		
		$( document ).delegate("#OrderBookerDashboardPage", "pageinit", function() {
			
			google.maps.event.addDomListener(window, 'load', initialize);
			
			$('#ChangeDistributor').on('click', function(e, data){
				
				$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
					lookupDistributorInit();
				} );
				$('#LookupDistributorSearch').popup("open", {transition:"pop"});
						  
			});	
			
		});
		
		function setDistributorLookupAtDistributorDashboard(SAPCode, Name){
			$('#LookupDistributorSearch').popup("close");
			 
			 $("#DashboardDistributorDistributorCode").val(SAPCode);
			 document.getElementById("DashboardDistributorFormID").submit();
		}
		
		function initialize() {
			  var myLatlng = new google.maps.LatLng(31.427335740000000,73.094063400000000);
			  var mapOptions = {
			    zoom: 13,
			    center: myLatlng
			  };

			  var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

			  var contentString = '<div id="content">Outlet ID 6205<br>View Dashboard</div>';

			  var infowindow = new google.maps.InfoWindow({
			      content: contentString,
			      maxWidth: 200
			  });
			  
			  var markers = new Array();
			  
			  <%
			  long total_records = 0;
			  ResultSet rs21 = s3.executeQuery("SELECT count(outlet_id) total FROM mobile_order where distributor_id="+DistributorCode+" and created_on between curdate() and from_days(to_days(curdate())+1) ");
			  if(rs21.first()){
				  total_records = rs21.getLong(1);
			  }
			  
			  //ResultSet rs2 = Distributor.getMarkers(DistributorCode);
			  ResultSet rs2 = s3.executeQuery("SELECT outlet_id id, (SELECT name FROM pep.common_outlets where id=outlet_id) name, lat, lng, net_amount FROM mobile_order where distributor_id="+DistributorCode+" and created_on between '2016-05-20 23:59' and '2016-05-22 00:00' ");
			  int i = 0;
			  while(rs2.next()){
				  /*
				  String IconType = "";
				  if(rs2.getInt("is_discounted") == 1){
					  IconType = "d";
				  }else{
					  IconType = "o";
				  }*/
				  
				  if(i == 0){
					  %>
					  	map.setCenter(new google.maps.LatLng(<%=rs2.getString("lat")%>,<%=rs2.getString("lng")%>));
					  <%
				  }
				  
				  %>
				  
				  markers[<%=i%>] = new google.maps.Marker({
				      position: new google.maps.LatLng(<%=rs2.getString("lat")%>,<%=rs2.getString("lng")%>),
				      map: map,
				      title: '<%=rs2.getString("name")%>',
				      <% if(i == (total_records-1)){ %>
				      	animation: google.maps.Animation.BOUNCE,
				      <% } %>
				      icon: 'images/markers/truck.png'
				  });
				  google.maps.event.addListener(markers[<%=i%>], 'click', function() {
				    infowindow.open(map,markers[<%=i%>]);
				    
				    var infoWindowContent = "<%=rs2.getString("id")%>-<%=rs2.getString("name")%><br>Rs <%=Utilities.getDisplayCurrencyFormatRounded(rs2.getDouble("net_amount"))%><br>"; 
				    infoWindowContent += "<a href='#'>View Detail</a>";
				    infowindow.setContent(infoWindowContent);
				    //infowi
				  });
				  
				  <%
				  i++;
			  }
			  
			  %>
			  google.maps.event.trigger(markers[<%=total_records-1%>], 'click');

 
			}
		
		
		
		</script>
		
	</head>
	
<body>
<%
int PageIndex = Utilities.parseInt(request.getParameter("PageIndex"));
if (PageIndex == 0){
	PageIndex = 1;
}

String DistributorName="";
ResultSet rs1 = s.executeQuery("select name from common_distributors where distributor_id="+DistributorCode);
if(rs1.first()){
	DistributorName = rs1.getString("name");
}



%>
<div data-role="page" id="OrderBookerDashboardPage" data-url="OrderBookerDashboardPage" data-theme="d">

    <jsp:include page="DistributorDashboardHeader.jsp" >
    	<jsp:param value="<%=DistributorName%>" name="DistributorNameToShow"/>
    	<jsp:param value="<%=DistributorCode%>" name="DistributorCode"/>
    	<jsp:param value="<%=PageIndex%>" name="PageIndex"/>
    	<jsp:param value="Overview" name="ReportTitleToShow"/>
    </jsp:include>
    <!-- /header -->
    
    <div data-role="content" data-theme="d">
	
	<table style="width: 100%" border="0">
	<tr>
	
	<td style="width: 20%" valign="top">

		<ul data-role="listview" data-inset="false" data-theme="d" data-divider-theme="c" data-count-theme="d">
			<li data-role="list-divider">Volume Contribution</li>	
			<li>
				<div id="container_sales_pie3" style="width: auto; height: 200px"></div>
			</li>		
			<li>
				<div id="container_sales_pie2" style="width: auto; height: 200px"></div>
			</li>
			<li data-role="list-divider">Outlets</li>
			<li>
				<div id="container_sales_pie" style="width: auto; height: 200px"></div>
			</li>
			<li>
				<div id="container_sales_pie1" style="width: auto; height: 200px"></div>
			</li>			
		</ul>


	</td>
       <td style="width: 60%;" valign="top">
       
       <div style="margin-left: 25px; margin-top: -15px;">
<ul style="margin-left: -12px" data-role="listview" data-inset="false" data-theme="d" data-divider-theme="c" data-count-theme="d">
    <li data-role="list-divider">Lifting vs. Secondary Sales</li>
    <li>
    
    	<div id="container_sales" style="width: 100%; height: 350px; margin: 0 auto"></div>
    
    </li>

<li data-role="list-divider">Today's Orders, <%=ActiveDevices %> Devices, <%=OrderBookers %> Order Bookers</li>
<li>



<table style="width: 100%" border="0">
	<tr>
		<td><div id="map-canvas" style="width: 100%; height: 380px;"></div></td>
	</tr>
	</table>
	</li>
	<li data-role="list-divider">Performance</li>
	<li>
	<table style="width: 100%" border="0">
	<tr>
		<td>
			
			<table style="width: 100%" border="0">
				
				<tr>					
					<td style="width: 33%"><div id="container_drop_size" style="width: 100%; height: 200px; margin: 0 auto"></div></td>
					<td style="width: 33%"><div id="container_sku_per_bill" style="width: 100%; height: 200px; margin: 0 auto"></div></td>
					<td style="width: 33%"><div id="container_productivity" style="width: 100%; height: 200px; margin: 0 auto"></div></td>
				</tr>
			</table>
			
		</td>
	</tr>
</table>

</li>


    </ul>
</ul>
       </div>
       </td>
       
       <td style="width: 20%" valign="top">
       
       		
       
       
       		<ul data-role="listview" data-inset="false" style="margin-top: -15px;margin-left: 13px; m1ax-width: 300px;" data-divider-theme="c" style="width: 20px;">
				
				<li data-role="list-divider">Outlet Universe</li>
				<li data-icon="false"><a href="#" data-theme="e">Total Outles<span class="ui-li-count" style="font-size: 10;"><%=Utilities.getDisplayCurrencyFormatRounded(OutletUniverse) %></span></a></li>
				<li data-icon="false"><a href="#" data-theme="e">Zero Sale Outlets<span class="ui-li-count" style="font-size: 10;"><%=Math.round(ZeroSalesPercentage) %>%</span></a></li>
				<li data-icon="false"><a href="#" data-theme="e">Discounted Outlets<span class="ui-li-count" style="font-size: 10;"><%=Math.round(DiscountedOutletsPercentage) %>%</span></a></li>
				<li data-role="list-divider">Status</li>
				<li data-icon="false"><a href="#" data-theme="e">PJPs<span class="ui-li-count" style="font-size: 10;"><%=Utilities.getDisplayCurrencyFormatRounded(BeatPlans) %></span></a></li>
				<li data-icon="false"><a href="#" data-theme="e">Order Bookers<span class="ui-li-count" style="font-size: 10;"><%=Utilities.getDisplayCurrencyFormatRounded(OrderBookers) %></span></a></li>
				<li data-icon="false"><a href="#" data-theme="e">Active Devices<span class="ui-li-count" style="font-size: 10;"><%=Utilities.getDisplayCurrencyFormatRounded(ActiveDevices) %></span></a></li>
				<li data-role="list-divider">Sales</li>
				<li data-icon="false"><a href="#" data-theme="e">Lifting / Day (CC)<span class="ui-li-count" style="font-size: 10;"><%=Utilities.getDisplayCurrencyFormatRounded(LiftingPerDay) %></span></a></li>
				<li data-icon="false"><a href="#" data-theme="e">Sales / Day (CC)<span class="ui-li-count" style="font-size: 10;"><%=Utilities.getDisplayCurrencyFormatRounded(SalesPerDay) %></span></a></li>
				<li data-icon="false"><a href="#" data-theme="e">Lifting to Sales Ratio<span class="ui-li-count" style="font-size: 10;"><%=Utilities.getDisplayCurrencyFormatRounded(LiftingToSalesRatio) %>%</span></a></li>
				<li data-icon="false"><a href="#" data-theme="e">Top 10 Outlets (VC)<span class="ui-li-count" style="font-size: 10;"><%=Utilities.getDisplayCurrencyFormatRounded(Top10Percentage) %>%</span></a></li>
				<li data-icon="false"><a href="#" data-theme="e">Backorders / Day (CC)<span class="ui-li-count" style="font-size: 10;"><%=Utilities.getDisplayCurrencyFormatRounded(TotalBackorders/13) %></span></a></li>
				<li data-icon="false"><a href="#" data-theme="e">Returns / Day (CC)<span class="ui-li-count" style="font-size: 10;"><%=Utilities.getDisplayCurrencyFormatRounded(TotalReturns/13) %></span></a></li>
				<!-- <li data-role="list-divider">Outlets</li>
				<li data-icon="false"><a href="#" data-theme="e">Total Assigned<span class="ui-li-count" style="font-size: 10;">---</span></a></li>
				<li data-icon="false"><a href="#" data-theme="e">Discounted<span class="ui-li-count" style="font-size: 10;">---</span></a></li>
				<li data-icon="false"><a href="#" data-theme="e">Non-Discounted<span class="ui-li-count" style="font-size: 10;">---</span></span></a></li>
				<li data-icon="false"><a href="#" data-theme="e">Average Daily Visits<span class="ui-li-count" style="font-size: 10;">---</span></a></li>-->
				
				
				<li data-role="list-divider">Backorders</li>
				<li>
					<div id="container_back_order" style="width: 100%; height: 150px; margin: 0 auto"></div>
					<table style="width:100%; font-size: 8px;"><tr><td>15 days ago</td><td style="text-align:right">Today</td></tr></table>
				</li>
				<li data-role="list-divider">Top Backorders</li>
				<%
				
					ResultSet rs20 = s3.executeQuery("select mopb.product_id, ipv.package_label, ipv.brand_label, sum(mopb.total_units) units, sum(if(mop.is_promotion = 1,0,mopb.total_units * mop.rate_units)) amount, sum(mopb.total_units/ipv.unit_per_sku) order_total, ipv.unit_per_sku, count(distinct mopb.id) orders_count from mobile_order_products mop join mobile_order_products_backorder mopb on mop.id = mopb.id and mop.product_id = mopb.product_id join inventory_products_view ipv on mopb.product_id = ipv.product_id where mopb.id in ("+
						"select id from mobile_order where distributor_id = "+DistributorCode+" and backordered_on between from_days(to_days(curdate())-15) and from_days(to_days(curdate())+1)"+
						") group by mopb.product_id order by order_total desc limit 5");
		          while(rs20.next()){
		        	  %>
		        	  <li data-icon="false"><a href="#" data-theme="e"><%=rs20.getString(2) %> <%=rs20.getString(3) %><span class="ui-li-count" style="font-size: 10;"><%=Utilities.getDisplayCurrencyFormatRounded(rs20.getDouble("order_total")) %></span></a></li>
		        	  <%
		          }				
				%>
				
				<li data-role="list-divider">Returns</li>
				<li>
					<div id="container_returns" style="width: 100%; height: 150px; margin: 0 auto"></div>
					<table style="width:100%; font-size: 8px;"><tr><td>15 days ago</td><td style="text-align:right">Today</td></tr></table>
				</li>
				
				
				
				
							
	   		</ul>
       </td> 
       </tr>
       <!--
       <tr>
       		<td colspan="3">&nbsp;</td>
       </tr>
       <tr>
       		<td colspan="3"><hr></td>
       </tr>
        <tr>
       		<td astyle="width: 25%" valign="top">
       			<div id="container_stock" style="width: 100%; height: 200px; margin: 0 auto"></div>
       		</td>
       		<td astyle="width: 50%" valign="top">
       			<div id="container_sales2" style="width: 100%; height: 350px; margin: 0 auto"></div>
       		</td>
       		<td style="width: 25%" valign="top">
       			&nbsp;
       		</td>
       </tr> -->
       
       <tr>
       		<td colspan="3">       		
	       		<ul data-role="listview" data-inset="false" data-theme="d" data-divider-theme="c" data-count-theme="d">
					<li>	       		
		       			<table class="table-stripe GridWithoutBorder " style="font-size: 10pt; width: 100%;">	       				
		       				<tr class="ui-bar-d">
		       					<th>PJP</th>
		       					<th>Order Booker</th>
		       					<th>Outlets</th>
		       					<th>Zero Sales</th>
		       					<th>Device ID</th>
		       					<th>Device Platform</th>
		       					<th>Last Order</th>
		       					<th>Last Order Location</th>
		       				</tr>
		       				
		       				<%
		       				ResultSet rs = s.executeQuery("SELECT distinct dbpav.id, label ,count(distinct outlet_id) outlet_count,assigned_to,assigned_to_name FROM distributor_beat_plan_all_view dbpav join common_outlets co on dbpav.outlet_id=co.id where 1=1 and dbpav.distributor_id="+DistributorCode+" group by dbpav.id order by outlet_count desc");
		       				while(rs.next()){
		       					
		       					String Orderbooker = rs.getString("assigned_to") +" - "+ rs.getString("assigned_to_name");
		       					if(rs.getString("assigned_to") == null){
		       						Orderbooker = "";
		       					}
		       					
		       					
		       					String DeviceID = "";
		       					String Platform = "";

		       					String BackOrderedDate = "";
		       					String Lat = "";
		       					String Lng = "";
		       					ResultSet rs3 = s2.executeQuery("SELECT uuid, platform, lat, lng, created_on FROM mobile_order where created_by="+rs.getString("assigned_to")+" order by id desc limit 1");
		       					if(rs3.first()){
		       						DeviceID = rs3.getString("uuid");
			       					Platform = rs3.getString("platform");
			       					BackOrderedDate = Utilities.getDisplayDateFormat(rs3.getDate("created_on"));
			       					Lat = rs3.getString("lat");
			       					Lng = rs3.getString("lng");
		       					}
		       					
		       					if(BackOrderedDate == null){
		       						BackOrderedDate = "---";
		       					}
		       					if (Platform.length() > 1){
		       						Platform = "Android "+Platform;
		       					}
		       					int ZeroOutlets = 0;
		       					ResultSet rs4 = s2.executeQuery("select count(*) from ( select outlet_id, to_days(curdate())-to_days(ifnull((select date(max(created_on)) from inventory_sales_adjusted where outlet_id = tab1.outlet_id),'2013-01-01')) days_ago from (SELECT distinct outlet_id FROM distributor_beat_plan_view dbpv join common_distributors cd on dbpv.distributor_id = cd.distributor_id where dbpv.assigned_to="+rs.getString("assigned_to")+" and cd.distributor_id in (select distinct distributor_id from inventory_sales_adjusted) ) tab1 having days_ago > 30 ) temp ");
		       					if(rs4.first()){
		       						ZeroOutlets = rs4.getInt(1);
		       					}
		       				%>		       				
		       				<tr>
		       					<td><%=rs.getString("id")%> - <%=rs.getString("label")%></td>
		       					<td><%=Orderbooker%></td> 
		       					<td style="text-align: center"><%=rs.getString("outlet_count")%></td> 
		       					<td style="text-align: center"><%=ZeroOutlets%></td>
		       					<td><%=DeviceID%></td>
		       					<td><%=Platform%></td>
		       					<td><%=BackOrderedDate%></td>
		       					<td><a href="#popupOrderBookerLocation" onclick="setLocation(<%=Lat%>, <%=Lng%>)" data-rel="popup" data-position-to="window" data-transition="pop">View</a></td>
		       				</tr>
		       				<%
		       				}
		       				%>
		       			</table>	       		
	       			</li>	       			
	       		</ul>
       		</td>
       </tr>
       
       
       </table>
       <br><br>
    </div><!-- /content -->
    
	<div data-role="popup" id="popupOrderBookerLocation" data-theme="c" class="ui-corner-all" style="width: 500px; height: 300px">
	    <div id='gmap_canvas' style='width:500px;height:300px;'></div>
	</div>


	<div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:600px; max-height: 600px" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >

			<form data-ajax="false" id="BeatPlanEditForm" onSubmit="return showSearchContent()">
            <table>
            	<tr>
                	<td>

						<ul id="autocomplete_search" data-role="listview" data-inset="true" data-filter="true" data-filter-placeholder="Search by Employee ID or by Name" data-filter-theme="d"></ul>
						
                    
                    </td>
                    <td>
                    
						<span id="EmployeeNameSearch" style="padding-left:20px"></span>
						<input type="hidden" name="EmployeeIDSearch" id="EmployeeIDSearch" value="" >
                    
                    </td>
                    
                </tr>
                <tr>
                	<td>&nbsp;</td>
                </tr>
                <tr>
                	<td>

						<ul id="autocomplete_outlet_search" data-role="listview" data-inset="true" data-filter="true" data-filter-placeholder="Search by Outlet ID or by Name" data-filter-theme="d"></ul>
						
                    
                    </td>
                    <td>
                    
						<span id="OutletNameSearch" style="padding-left:20px"></span>
						<input type="hidden" name="OutletIDSearch" id="OutletIDSearch" value="" >
                    
                    </td>
                    
                </tr>
                
                <tr>
                	<td>
                    	<button data-role="button" data-icon="search" id="BeatPlanSearchButton" data-iconpos="notext" data-theme="c" data-inline="true" data-corners="false" onClick="showSearchContent();"></button>
                    </td>
                </tr>
            </table>
        </form>

        <div id="EmployeeBeatPlanSearchContent">
        </div>
            
        </div>
    </div>
    
    
    
    <form id="DashboardDistributorFormID" name="DashboardDistributorFormID" action="DistributorDashboardOverview.jsp" method="POST" data-ajax="false">
    	<input type="hidden" name="DistributorCode" id="DashboardDistributorDistributorCode"/>
    </form>
    

    <jsp:include page="LookupEmployeeSearchPopup.jsp" > 
    	<jsp:param value="EmployeeSearchCallBack" name="CallBack" />
    </jsp:include><!-- Include Employee Search -->
    
    <jsp:include page="LookupOutletSearchPopup.jsp" > 
    	<jsp:param value="OutletSearchCallBack" name="CallBack" />
    </jsp:include><!-- Include Outlet Search -->
    
    <jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="setDistributorLookupAtDistributorDashboard" name="CallBack" />
    	<jsp:param value="44" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Distributor Search -->
    

</div>
</body>
</html>

<%
s2.close();
s.close();
c.close();
ds.dropConnection();

s3.close();
//c2.close();
ds2.dropConnection();

Distributor.close();
%>