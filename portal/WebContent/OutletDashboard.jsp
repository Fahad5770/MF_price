<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.Date"%>
<jsp:useBean id="bean" class="com.pbc.outlet.OutletDashboard" scope="page"/>
<jsp:setProperty name="bean" property="*"/>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%
//System.out.println("hello me in outlet dashboard "+request.getParameter("outletID"));

bean.setOutletID(Utilities.parseLong(request.getParameter("outletID")));

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}

int FeatureID = 31;
if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	//System.out.println("1");
	response.sendRedirect(Utilities.getAccessDeniedPageURL(request));
}

if (!UserAccess.isOutletAllowed(bean.OUTLET.ID, SessionUserID, FeatureID)){
	//System.out.println("2 Outlet ID "+bean.OUTLET.ID);
	response.sendRedirect(Utilities.getAccessDeniedPageURL(request));
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="lib/highcharts301/js/highcharts.js"></script>
		<script src="lib/highcharts301/js/highcharts-more.js"></script>
		<script src="js/OutletDashboard.js"></script>
		<script src="js/DistributorReports.js"></script>
		<script src="js/lookups.js"></script>
	
	<script>
	function OutletDashboardDDDistributorDashboardRedirect(DistIDDD){
		$("#OutletDashboardDistributorDistributorCode1D").val(DistIDDD);
		document.getElementById("OutletDashboardDistributorFormIDD11").submit();
	}
	
	</script>
	
		
		<script>
		$(function () {
		    
		        var colors = Highcharts.getOptions().colors,
		            categories = ['250ML STD', '500ML PET', '1500ML PET', '1000 ML PET', '250ML NRB'],
		            name = 'Browser brands',
		            data = [{
		                    y: 55.11,
		                    color: colors[0],
		                    drilldown: {
		                        name: 'Brands in 250ML STD',
		                        categories: ['7-UP', 'M.DEW', 'PEPSI', 'MIRINDA'],
		                        data: [10.85, 7.35, 33.06, 2.81],
		                        color: colors[0]
		                    }
		                }, {
		                    y: 21.63,
		                    color: colors[1],
		                    drilldown: {
		                        name: 'Brands in 500ML PET',
		                        categories: ['PEPSI', 'M.DEW', '7-UP', 'MIRINDA', 'AQUAFINA'],
		                        data: [0.20, 0.83, 1.58, 13.12, 5.43],
		                        color: colors[1]
		                    }
		                }, {
		                    y: 11.94,
		                    color: colors[2],
		                    drilldown: {
		                        name: 'Brands in 1500ML PET',
		                        categories: ['PEPSI', 'M.DEW', '7-UP', 'MIRINDA', 'TEEM',
		                            'AQUAFINA', '7-UP DIET', '7-UP FREE'],
		                        data: [0.12, 0.19, 0.12, 0.36, 0.32, 9.91, 0.50, 0.22],
		                        color: colors[2]
		                    }
		                }, {
		                    y: 7.15,
		                    color: colors[3],
		                    drilldown: {
		                        name: 'Brands in 1000 ML PET',
		                        categories: ['PEPSI', 'M.DEW', '7-UP', 'MIRINDA', 'TEEM',
		                                     'AQUAFINA', '7-UP DIET'],
		                        data: [4.55, 1.42, 0.23, 0.21, 0.20, 0.19, 0.14],
		                        color: colors[3]
		                    }
		                }];
		    
		    
		        // Build the data arrays
		        var browserData = [];
		        var versionsData = [];
		        for (var i = 0; i < data.length; i++) {
		    
		            // add browser data
		            browserData.push({
		                name: categories[i],
		                y: data[i].y,
		                color: data[i].color
		            });
		    
		            // add version data
		            for (var j = 0; j < data[i].drilldown.data.length; j++) {
		                var brightness = 0.2 - (j / data[i].drilldown.data.length) / 5 ;
		                versionsData.push({
		                    name: data[i].drilldown.categories[j],
		                    y: data[i].drilldown.data[j],
		                    color: Highcharts.Color(data[i].color).brighten(brightness).get()
		                });
		            }
		        }
		    
		        // Create the chart
		        $('#container').highcharts({
		            chart: {
		                type: 'pie'
		            },
		            credits: {
		            	enabled: false
		            },		            
		            title: {
		                text: ''
		            },
		            yAxis: {
		                title: {
		                    text: ''
		                }
		            },
		            plotOptions: {
		                pie: {
		                    shadow: false,
		                    center: ['50%', '50%']
		                }
		            },
		            tooltip: {
		        	    valueSuffix: '%'
		            },
		            series: [{
		                name: 'Package',
		                data: browserData,
		                size: '60%',
		                dataLabels: {
		                    formatter: function() {
		                        return this.y > 5 ? this.point.name : null;
		                    },
		                    color: 'white',
		                    distance: -30
		                }
		            }, {
		                name: 'Brands',
		                data: versionsData,
		                size: '80%',
		                innerSize: '60%',
		                dataLabels: {
		                    formatter: function() {
		                        // display only if larger than 1
		                        return this.y > 1 ? '<b>'+ this.point.name +':</b> '+ this.y +'%'  : null;
		                    }
		                }
		            }]
		        });
		    });
		    
<%
Date dates[] = Utilities.getPastMonthsInDate(new Date(), 7);
%>
		$(function () {
	        $('#container2').highcharts({
	            chart: {
	                type: 'areaspline'
	            },
	            title: {
	                text: ''
	            },
	            legend: {
	                layout: 'horizontal',
	                //align: 'left',
	                //verticalAlign: 'top',
	                //x: 150,
	                //y: 100,
	                //floating: true,
	                borderWidth: 1,
	                backgroundColor: '#FFFFFF'
	            },
	            xAxis: {
	                categories: [
						<%
						for (int i = 0; i < dates.length; i++){
							if (i > 0){
								out.print(",");
							}
							out.print("'"+Utilities.getDisplayDateMonthYearFormat(dates[i])+"'");
						}
						%>
	                ],
	                plotBands: [{ // visualize the weekend
	                    from: 4.5,
	                    to: 6.5,
	                    color: 'rgba(68, 170, 213, .2)'
	                }]
	            },
	            yAxis: {
	                title: {
	                    text: 'Discount (Rs.)'
	                }
	            },
	            tooltip: {
	                shared: true,
	                valueSuffix: ''
	            },
	            credits: {
	                enabled: false
	            },
	            plotOptions: {
	                areaspline: {
	                    fillOpacity: 0.5
	                }
	            },
	            series: [{
	                name: 'Total Discount',
	                data: [<%
							for (int i = 0; i < dates.length; i++){
								Date EndDate = Utilities.getEndDateByDate(dates[i]);
								if (i > 0){
									out.print(",");
								}
								out.print(Math.round(bean.getDiscountOfMonth(EndDate)));
							}
					%>
					]
	            }, {
	                name: 'Per Case',
	                data: [<%
							for (int i = 0; i < dates.length; i++){
								Date EndDate = Utilities.getEndDateByDate(dates[i]);
								if (i > 0){
									out.print(",");
								}
								
								out.print(Math.round(bean.getPerCaseDiscountAmountGenerated(EndDate)));
							}
					%>
					]
	            },{
	                name: 'Fixed',
	                data: [<%
							for (int i = 0; i < dates.length; i++){
								Date EndDate = Utilities.getEndDateByDate(dates[i]);
								if (i > 0){
									out.print(",");
								}
								out.print(Math.round(bean.getFixedDiscountAmountGenerated(EndDate)));
							}
					%>
					]
	            }]
	        });
	    });
		
		
		
		$( document ).delegate("#OutletDashboardSales", "pageshow", function() {
			
			$(function () {
			    $('#SalesChart1').highcharts({
			        chart: {
			            type: 'waterfall'
			        },
		            credits: {
		            	enabled: false
		            },	
			        title: {
			            text: ''
			        },

			        xAxis: {
			            type: 'category'
			        },

			        yAxis: {
			            title: {
			                text: 'Converted Cases in thousands'
			            }
			        },

			        legend: {
			            enabled: false
			        },

			        tooltip: {
			            pointFormat: '<b></b> USD'
			        },

			        series: [{
			            upColor: Highcharts.getOptions().colors[2],
			            color: Highcharts.getOptions().colors[3],
			            data: [{
			                name: '1000ML PET',
			                y: 120000
			            }, {
			                name: '250ML STD',
			                y: 569000
			            }, {
			                name: '1500ML PET',
			                y: 231000
			            }, {
			                name: 'Total Sales',
			                isIntermediateSum: true,
			                color: Highcharts.getOptions().colors[1]
			            }, {
			                name: 'PEPSI',
			                y: -342000
			            }, {
			                name: 'TEEM',
			                y: -233000
			            }, {
			                name: 'M.DEW',
			                isSum: true,
			            }],
			            dataLabels: {
			                enabled: true,
			                formatter: function () {
			                	var val = this.y;
			                	if (val < 0){
			                		val = val * -1;
			                	}
			                    return Highcharts.numberFormat(val / 1000, 0, ',') + 'k';
			                },
			                style: {
			                    color: '#FFFFFF',
			                    fontWeight: 'bold'
			                }
			            },
			            pointPadding: 0
			        }]
			    });
			});		
			
			$(function () {
		        $('#SalesChart2').highcharts({
		            chart: {
		                type: 'line'
		            },
		            credits: {
		            	enabled: false
		            },			            
		            title: {
		                text: ''
		            },
		            subtitle: {
		                text: ''
		            },
		            xAxis: {
		                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
		            },
		            yAxis: {
		                title: {
		                    text: 'Sales (Converted Cases) in thousands'
		                }
		            },
		            tooltip: {
		                enabled: false,
		                formatter: function() {
		                    return '<b>'+ this.series.name +'</b><br/>'+
		                        this.x +': '+ this.y +'°C';
		                }
		            },
		            plotOptions: {
		                line: {
		                    dataLabels: {
		                        enabled: true
		                    },
		                    enableMouseTracking: false
		                }
		            },
		            series: [{
		                name: 'Arabain Sweet & Bakers',
		                data: [7.0, 6.9, 9.5, 14.5, 18.4, 21.5, 24.2, 24.5, 23.3, 18.3, 13.9, 9.6]
		            }, {
		                name: 'Nazir Pan Shop (22m)',
		                data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 17.6, 18.2, 16.5, 17.2, 17.5]
		            }, {
		                name: 'Shahid General Store (75m)',
		                data: [10, 11, 12, 10, 11, 12, 11, 12, 11, 8, 9, 10]
		            }]
		        });
		    });		

		});

		
		
		$( document ).delegate("#OutletDashboardDiscount", "pageshow", function() {
			$(function () {
		        $('#DiscountChart2').highcharts({
		            chart: {
		                type: 'area',
		                inverted: true
		            },
		            credits: {
		            	enabled: false
		            },		            
		            title: {
		                text: ''
		            },
		            subtitle: {
		                style: {
		                    position: 'absolute',
		                    right: '0px',
		                    bottom: '10px'
		                }
		            },
		            legend: {
		                layout: 'vertical',
		                align: 'right',
		                verticalAlign: 'top',
		                x: -50,
		                y: 10,
		                floating: true,
		                borderWidth: 1,
		                backgroundColor: '#FFFFFF'
		            },
		            xAxis: {
		                categories: [
		                    'June, 13',
		                    'May, 13',
		                    'April, 13',
		                    'March, 13',
		                    'February, 13',
		                    'January, 13',
		                    'December, 12'
		                ]
		            },
		            yAxis: {
		                title: {
		                    text: 'Discount (Rs.)'
		                },
		                labels: {
		                    formatter: function() {
		                        return this.value;
		                    }
		                },
		                min: 0
		            },
		            plotOptions: {
		                area: {
		                    fillOpacity: 0.5
		                }
		            },
		            series: [{
		                name: '250ML STD',
		                data: [3000, 4000, 3000, 5000, 4000, 10000, 9000]
		            }, {
		                name: '1000ML G.',
		                data: [1000, 3000, 4000, 3000, 3000, 5000, 4000]
		            },{
		                name: '500ML PET',
		                data: [500, 700, 900, 1200, 1400, 1200, 1100]
		            },
		            ]
		        });
		    });
		});
		
				</script>		
	</head>
	<style>
	a{
		font-size: 10pt;
	}
		
	</style>
<body>
<%
//int OutletID = Utilities.parseInt(request.getParameter("OutletID"));
%>

<div data-role="page" id="OutletDashboardSummary" data-url="OutletDashboardSummary" data-theme="d">

    <jsp:include page="OutletDashboardHeader.jsp" >
    	<jsp:param value="<%=bean.OUTLET.NAME %>" name="title"/>
    	<jsp:param value="1" name="tab"/>
    	<jsp:param value="<%=bean.OUTLET.ID%>" name="OutletID"/>
    </jsp:include>
     <!-- /header -->
    
    <div data-role="content" data-theme="d">
        
        <table style="width: 100%;">
			<tr>
				<td style="width: 30%" valign="top">
				
					<ul data-role="listview" data-inset="true" data-theme="d" data-divider-theme="c" data-count-theme="d" style="width: 100%" >
					    <li data-role="list-divider">Status</li>
					    <li data-icon="false"><a href="index.html" data-theme="e">Discount Status<span class="ui-li-count" style="font-size: 10pt;"><%if (bean.isDiscountActive()){ %>Active<%}else{ %>Not Active<%} %></span></a></li>
					    <li data-icon="false"><a href="index.html" data-theme="e">Ledger Balance<span class="ui-li-count" style="font-size: 10pt;"><%=Utilities.getDisplayCurrencyFormat(bean.OUTLET.getCurrentBalance())%></span></a></li>
					    <li data-role="list-divider">Sales</li>
					    <li data-icon="false"><a href="index.html" data-theme="e">Monthly Average (Rs.)<span class="ui-li-count" style="font-size: 10pt;"><%=Utilities.getDisplayCurrencyFormat(Math.round(bean.getAverageSalesAmount()))%> / M</span></a></li>
					    <li data-icon="false"><a href="index.html" data-theme="e">Monthly Average (Converted)<span class="ui-li-count" style="font-size: 10pt;"><%=Utilities.getDisplayCurrencyFormat(Math.round(bean.getAverageSalesConvertedCases()))%> / M</span></a></li>
					    <li data-icon="false"><a href="index.html" data-theme="e">Total Sales (Rs.)<span class="ui-li-count" style="font-size: 10pt;"><%=Utilities.getDisplayCurrencyFormat(Math.round(bean.getTotalSalesAmount()))%></span></a></li>
					    <li data-icon="false"><a href="index.html" data-theme="e">Total Sales (Converted)<span class="ui-li-count" style="font-size: 10pt;"><%=Utilities.getDisplayCurrencyFormat(Math.round(bean.getTotalSalesConvertedCases()))%></span></a></li>
					    <li data-role="list-divider">Discount</li>
					    <li data-icon="false"><a href="index.html" data-theme="e">Fixed<span class="ui-li-count" style="font-size: 10pt;"><%if (bean.isFixedDiscountActive()){ %>Active<%}else{ %>Not Active<%} %></span></a></li>
					    <li data-icon="false"><a href="index.html" data-theme="e">Per Case<span class="ui-li-count" style="font-size: 10pt;"><%if (bean.isPerCaseDiscountActive()){ %>Active<%}else{ %>Not Active<%} %></span></a></li>
					    <li data-role="list-divider">Demographics</li>
					    <li data-icon="false" style="height: 255px;"><div style="width: 100%; height: 235px; border: 1px solid"><%if (bean.OUTLET.LATITUDE != 0 && bean.OUTLET.LONGITUDE != 0){ %><img style="width: 100%; margin-top: 0; padding-top: 0;" src="http://maps.googleapis.com/maps/api/staticmap?center=<%=bean.OUTLET.LATITUDE %>,<%=bean.OUTLET.LONGITUDE %>&zoom=14&size=285x220&markers=color:blue%7Clabel:S%7C<%=bean.OUTLET.LATITUDE %>,<%=bean.OUTLET.LONGITUDE %>&sensor=false"><%} %><div style="padding-top: 5px;"><font size="2"><%= bean.OUTLET.ADDRESS %></font></div></div></li>
					    <li data-icon="false"><a href="index.html" data-theme="e">Region<span class="ui-li-count" style="font-size: 10pt;"><%=bean.OUTLET.REGION_SHORT_NAME %></span></a></li>
					    <li data-icon="false"><a href="#" data-theme="e" <%if(Utilities.isAuthorized(44, SessionUserID) == true){%>ondblclick="OutletDashboardDDDistributorDashboardRedirect(<%=bean.OUTLET.DISTRIBUTOR_ID%>)"<%} %>>Distributor<span class="ui-li-count" style="font-size: 10pt;"><%=bean.OUTLET.DISTRIBUTOR_ID +" - "+bean.OUTLET.DISTRIBUTOR_NAME %></span></a></li>
					    <li data-icon="false"><a href="index.html" data-theme="e">RSM<span class="ui-li-count" style="font-size: 10pt;"><%=bean.OUTLET.RSM_ID %></span></a></li>
					    <li data-icon="false"><a href="index.html" data-theme="e">ASM<span class="ui-li-count" style="font-size: 10pt;"><%=bean.OUTLET.ASM_ID %></span></a></li>
					    <li data-icon="false"><a href="index.html" data-theme="e">CR<span class="ui-li-count" style="font-size: 10pt;"><%=bean.OUTLET.CR_ID %></span></a></li>
					</ul>
				
				</td>
				<td valign="top">
				<ul data-role="listview" data-inset="true" data-theme="d" data-divider-theme="c" data-count-theme="d" style="width: 100%" >
					<li data-role="list-divider">Discount Trend</li>
					<li data-icon="false"><div id="container2" style="min-width: 400px; height: 370px; margin: 0 auto"></div></div></li>
					<li data-role="list-divider">Sales in Packages and Brands</li>
					<li data-icon="false"><div id="container" style="min-width: 400px; height: 400px; margin: 0 auto"></div></li>
				</ul>
				</td>
			</tr>
        </table>
        	<form id="OutletDashboardDistributorFormIDD11" name="OutletDashboardDistributorFormIDD11" action="DistributorDashboardOverview.jsp" method="POST" data-ajax="false" target="_blank">
    			<input type="hidden" name="DistributorCode" id="OutletDashboardDistributorDistributorCode1D"/>
    		</form>
    </div><!-- /content -->
    
    <div data-role="popup" id="popupMenu" data-theme="b">
        <ul data-role="listview" data-inset="true" style="min-width:210px;" data-theme="b">
            <li><a href="#">Sales</a></li>
            <li><a href="#">Monthly Discount</a></li>
            <li><a href="#">Fixed Discount</a></li>
            <li><a href="#">Advances Issued</a></li>
        </ul>
	</div>
	
	<jsp:include page="LookupOutletSearchPopup.jsp" > 
    	<jsp:param value="OutletSearchCallBack" name="CallBack" />
    </jsp:include><!-- Include Outlet Search -->

    <jsp:include page="Footer1.jsp" /> <!-- /footer -->
<%
bean.close();
%>
</div>
</body>
</html>