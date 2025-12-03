<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.outlet.InventoryPackage"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.outlet.MonthlyDiscount"%>
<%@page import="com.pbc.outlet.Advance"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@page import="com.pbc.util.Utilities"%>
<jsp:useBean id="bean" class="com.pbc.outlet.OutletDashboard" scope="page"/>
<jsp:setProperty name="bean" property="*"/>
<%
if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}

int CurrentYear = Utilities.getYearByDate(new Date());

int SelectedYear = Utilities.parseInt(request.getParameter("SelectedYear"));
if(SelectedYear == 0){
	SelectedYear = CurrentYear;
}

int PreviousYear = SelectedYear - 1;
int NextYear = SelectedYear + 1;

Date LastMonth = bean.getLastDateOfDiscount();
Advance advances[] = bean.getAdvancesIssued();


double TotalAdvances = 0;
for (int i = 0; i < advances.length; i++){
	 TotalAdvances += advances[i].AMOUNT;
}

int NumberOfAdvancesIssued = advances.length;

double TotalSampling = 0;
double TotalDeduction = 0;
double TotalPayable = 0;
double TotalAdjustment = 0;
double TotalNetPayable = 0;
MonthlyDiscount slips[] = bean.getSlips();
for (int i = 0; i < slips.length; i++){
	 TotalSampling += slips[i].SAMPLING_AMOUNT;
	 TotalDeduction += slips[i].DEDUCTION_AGAINST_ADVANCE;
	 TotalPayable += slips[i].PAYABLE;
	 TotalAdjustment += slips[i].MANUAL_ADJUSTMENT;
	 TotalNetPayable += slips[i].NET_PAYABLE;
}

int MonthCount = slips.length;

double AverageDeduction = TotalDeduction / MonthCount;

double CurrentBalance = bean.OUTLET.getCurrentBalance();

%><div data-role="page" id="OutletDashboardDiscount" data-url="OutletDashboardDiscount" data-theme="d">

    <jsp:include page="OutletDashboardHeader.jsp" >
    	<jsp:param value="<%=bean.OUTLET.NAME %>" name="title"/>
    	<jsp:param value="4" name="tab"/>
    	<jsp:param value="<%=bean.OUTLET.ID%>" name="OutletID"/>
    </jsp:include>
     <!-- /header -->
    
    <div data-role="content" data-theme="d">
    
    	<table style="width: 100%" border="0">
		<tr>
			<td style="width: 35%;" valign="top">
					<ul data-role="listview" data-inset="true" data-theme="d" data-divider-theme="c" data-count-theme="d" style="width: 100%" >
					    <li data-role="list-divider">Discount Summary</li>
					    <li data-icon="false"><a href="#" >Total Discount (Rs.)<span class="ui-li-count" style="font-size: 10pt;"><%=Utilities.getDisplayCurrencyFormat(bean.getTotalDiscount()) %></span></a></li>
					    <li data-icon="false"><a href="#" >Average Discount (Rs.)<span class="ui-li-count" style="font-size: 10pt;"><%=Utilities.getDisplayCurrencyFormat(bean.getAverageDiscount()) %> / Month</span></a></li>
					    <li data-icon="false"><a href="#" >Average Converted Discount (Rs.)<span class="ui-li-count" style="font-size: 10pt;">5 / Case</span></a></li>
					    <li data-icon="false"><a href="#" >Last Month Discount (<%= Utilities.getDisplayDateMonthYearFormat(LastMonth) %>) <span class="ui-li-count" style="font-size: 10pt;"><%=Utilities.getDisplayCurrencyFormat(bean.getDiscountOfMonth(LastMonth)) %></span></a></li>
					    <li data-role="list-divider">Advances Summary</li>
					    <li data-icon="false"><a href="#" >Total Advances<span class="ui-li-count" style="font-size: 10pt;"><%=Utilities.getDisplayCurrencyFormat(TotalAdvances) %></span></a></li>
					    <li data-icon="false"><a href="#" >Adjustment against Advances<span class="ui-li-count" style="font-size: 10pt;"><%=Utilities.getDisplayCurrencyFormat(TotalDeduction) %></span></a></li>
					    <li data-icon="false"><a href="#" >Average Deduction<span class="ui-li-count" style="font-size: 10pt;"><%=Utilities.getDisplayCurrencyFormat(AverageDeduction) %> / Month</span></a></li>
					    <li data-icon="false"><a href="#" data-theme="a">No. of Advances Issued<span class="ui-li-count" style="font-size: 10pt;"><%=NumberOfAdvancesIssued %></span></a></li>
					    <li data-icon="false"><a href="#" data-theme="a">Current Balance<span class="ui-li-count" style="font-size: 10pt;"><%=Utilities.getDisplayCurrencyFormat(CurrentBalance) %></span></a></li>
					</ul>	
			</td>
			<td style="width: 65%;" valign="top">
	    			<ul data-role="listview" data-inset="true" data-theme="d" data-divider-theme="c" data-count-theme="d" style="width: 100%" >
						<li data-role="list-divider">Discount Trend by Package</li>
						<li data-icon="false"><div id="DiscountChart2" style="min-width: 400px; height: 337px; margin: 0 auto"></div></li>
					</ul>
			</td>
		</tr>
		<tr>
			<td colspan="2">
			
					<ul data-role="listview" data-inset="true" data-theme="d" data-divider-theme="c" data-count-theme="d" style="width: 90%;" > 
					
						<li><div style="padding-bottom: 10px;">Monthly Discount</div>
							<table class="table-stripe GridWithoutBorder " style="font-size: 10pt; width: 100%;">
				         <thead>
				           <tr class="ui-bar-d">
				             <th>Month</th>
				             <th style="text-align: right;">Discount Amount</th>
				             <th style="text-align: right;">Advance Deduction</th>
				             <th style="text-align: right;">Payable</th>
				             <th style="text-align: right;">Adjustment</th>
				             <th style="text-align: right;">Net Payable</th>
				             <th style="text-align: right;">Approval Status</th>
				             <th style="text-align: right;">Slip Status</th>
				             
				             <th style="text-align: right;">Dispatch Status</th>
				             
				           </tr>
				         </thead>
				         <tbody>
				         <%
				         for (int i = 0; i < slips.length; i++){
				         %>
				           <tr>
				             <td><%=Utilities.getDisplayDateMonthYearFormat(slips[i].DATE)%></th>
				             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(slips[i].SAMPLING_AMOUNT) %></td>
				             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(slips[i].DEDUCTION_AGAINST_ADVANCE) %></td>
				             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(slips[i].PAYABLE) %></td>
				             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(slips[i].MANUAL_ADJUSTMENT) %></td>
				             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(slips[i].NET_PAYABLE) %></td>
				             <td style="text-align: right;"><%=slips[i].APPROVAL_STATUS %></td>
				             <td style="text-align: right;"><%=slips[i].SLIP_STATUS %></td>
				             
				             <td style="text-align: right;">
				             	<%
				             	if( slips[i].METHOD_ID != 0 && slips[i].METHOD_ID == 1 ){
				             		out.print("By Hand ["+slips[i].BY_HAND_USERID+"]");
				             	}else if( slips[i].METHOD_ID != 0 && slips[i].METHOD_ID == 2 ){
				             		out.print("By TCS [<a href='http://www.tcscouriers.com/pk/Tracking/Default.aspx?TrackBy=ReferenceNumberHome&trackNo="+slips[i].DISPATCH_CODE+"' target='_blank'>"+slips[i].DISPATCH_CODE+"</a>]");
				             	}else{
				             		out.print("Not Dispatched");
				             	}
				             	%>
				             </td>
				             
				           </tr>
						<%
				         }
						%>
				           <tr>
				             <td>Total</th>
				             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(TotalSampling) %></td>
				             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(TotalDeduction) %></td>
				             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(TotalPayable) %></td>
				             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(TotalAdjustment) %></td>
				             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(TotalNetPayable) %></td>
				             <td></td>
				             <td></td>
				             <td></td>
				           </tr>
						
						</tbody>
						</table>
						</li>
					</ul>											
					<ul data-role="listview" data-inset="true" data-theme="d" data-divider-theme="c" data-count-theme="d" style="width: 100%;" >
					
						<li><div style="padding-bottom: 10px;">Advances Issued</div>

							<table class="table-stripe GridWithoutBorder " style="font-size: 10pt; width: 100%;">
				         <thead>
				           <tr class="ui-bar-d">
				             <th>Date</th>
				             <th>Document #</th>
				             <th>Remarks</th>
				             <th style="text-align: right;">Amount</th>
				           </tr>
				         </thead>
				         <tbody>
				         <%
				         for (int i = 0; i < advances.length; i++){
				         %>
				           <tr>
				             <td><%=Utilities.getDisplayDateFormat(advances[i].DATE)%></th>
				             <td><%=advances[i].APPROVAL_ID %></td>
				             <td><%=advances[i].REMARKS %></td>
				             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(advances[i].AMOUNT)%></td>
				           </tr>
						<%
				         }
						%>
				           <tr>
				             <th></th>
				             <th></th>
				             <th>Total</th>
				             <th style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(TotalAdvances) %></th>
				           </tr>
						
						</tbody>
						</table>
						</li>
					</ul>									           
<%
//Date dates[] = Utilities.getPastMonthsInDate(new Date(), 6);
Date dates[] = Utilities.getPastMonthsInDate(Utilities.getEndDateByMonth(11, SelectedYear), 12);

double TotalDiscount[] = new double[dates.length];

%>			
					<ul data-role="listview" data-inset="true" data-theme="d" data-divider-theme="c" data-count-theme="d" style="width: 100%;" >
					
						<li><div style="padding-bottom: 10px;">Projection</div>
			
							<!--  <table data-role="table" id="SalesComparisonTable" data-mode="columntoggle" class="ui-body-d ui-shadow table-stripe ui-responsive" data-column-btn-theme="c" data-column-btn-text="Export to Excel" data-column-popup-theme="a" style="font-size: 10pt;"> -->
							
							<table style="width: 25%" border=0>
								<tr>
									<td>
										
										<a id="OutletDashboardSalesLink" href="#" data-transition="none" style="display:none" >Sales</a>
										<script>
										
											var OutletID = <%=bean.OUTLET.ID%>;
						
											function reloadOutletDashboardSales(SelectedYear){
												$('#OutletDashboardSalesLink').attr("href", "OutletDashboardDiscount.jsp?outletID="+OutletID+"&ReportTitle=Discount&SelectedYear="+SelectedYear);
												$('#OutletDashboardSalesLink').trigger('click');
											}
											
										</script>
											<select name="FilterByYear" id="FilterByYear" data-mini="true" onchange="reloadOutletDashboardSales(this.value)">
												<option value="<%=PreviousYear%>" <% //if( SelectedYear == PreviousYear ){ out.print("selected"); } %> ><%=PreviousYear%></option>
												<option value="<%=CurrentYear%>" <% //if( SelectedYear == SelectedYear ){ out.print("selected"); } %> selected="selected" ><%=SelectedYear%></option>
												<option value="<%=NextYear%>" <% //if( SelectedYear == NextYear ){ out.print("selected"); } %> ><%=NextYear%></option>
											</select>
											
									</td>
								</tr>
							</table>
							
							<table class="table-stripe GridWithBorder " style="font-size: 10pt; width: 100%;">
								
								
								
							         <thead>
							           <tr>
							             <td colspan="2">Status</td>
							             <%
							             int count = 0;
							             for (Date idate: dates){
							            	 Date iEndDate = Utilities.getEndDateByDate(idate);
											 String status = "Not Generated";
							            	 boolean generated = false;
							            	 for (int i = 0; i < slips.length; i++){
							            		 if (DateUtils.isSameDay(slips[i].DATE, iEndDate)){
							            			 status = slips[i].APPROVAL_STATUS;
							            			 generated = true;
							            		 }
							            	 }
							             %>
							             <td colspan="2" style="text-align: center; <%if (status.equals("Approved")){%>color: green;<%}%>"><%=status%></td>
							             <%
							             count++;
							             }
							             %>							             
							           </tr>
							           <tr class="ui-bar-d">
							           		
							             <th colspan="2"></th>
							             <%
							             for (Date idate: dates){
							             %>
							             	<th colspan="2" style="text-align: center;"><%= Utilities.getDisplayDateMonthYearFormat(idate) %></th>
							             <%
							             }
							             %>
							           </tr>
							           
							           <tr aclass="ui-bar-d">
							             <th>Package</th>
							             <th>Brand</th>
							             <%
							             for (Date idate: dates){
							             %>
							             <th style="width: 6.5%; text-align: center;">Rate</th>
							             <th style="width: 6.5%; text-align: center;">Discount</th>
							             <%
							             }
							             %>
							           </tr>
							         </thead>
							         <tbody>
							         
							           <tr>
							             <td colspan="2">Fixed Discount</td>
							             <%
							             count = 0;
							             for (Date idate: dates){
							            	 Date iEndDate = Utilities.getEndDateByDate(idate);
							            	 double FixedSampling = bean.getFixedDiscountAmount(iEndDate);
							            	 
							            	 TotalDiscount[count] += FixedSampling;
							             %>
							             <td></td>
							             <td style="text-align: center;"><% if (FixedSampling != 0){ out.print(Utilities.getDisplayCurrencyFormat(FixedSampling));  } %></td>
							             <%
							             count++;
							             }
							             %>							             
							           </tr>
							           <%
							           InventoryPackage PackList[] = bean.getDiscountPackageListWithBrands();
							           for (InventoryPackage pack : PackList ){
							           %>
							           <tr>
							             <td><%=pack.LABEL %></td>
							             <td><%=pack.BRAND_LABEL %></td>
							             <%
							             count = 0;
							             for (Date idate: dates){
							            	 Date iEndDate = Utilities.getEndDateByDate(idate);
							            	 double discount[] = bean.getPerCaseDiscountAmountAndRate(iEndDate, pack.ID, pack.BRAND_ID);
							            	 TotalDiscount[count] += discount[1];
							             %>
							             <td style="text-align: center;"><%if (discount[0] != 0){out.print(Utilities.getDisplayCurrencyFormat(discount[0]));} %></td>
							             <td style="text-align: center;"><%if (discount[1] != 0){out.print(Utilities.getDisplayCurrencyFormat(discount[1]));} %></td>
							             <%
							             count++;
							             }
							             %>
							           </tr>
							           <%
							           }
							           %>
							           <tr >
							             <th colspan="2">Total</th>
							             <%
							             count = 0;
							             for (Date idate: dates){
							             %>
							             <th></th>
							             <th style="text-align: center;"><%if (TotalDiscount[count] != 0){out.print(Utilities.getDisplayCurrencyFormat(TotalDiscount[count]));} %></th>
							             <%
							             count++;
							             }
							             %>
							           </tr>
							         </tbody>
							       </table>
							       </li>
							       </ul>
			</td>
		</tr>
		</table>
		
	</div>        

    <jsp:include page="Footer1.jsp" /> <!-- /footer -->

</div>

<%
bean.close();
%>
</body>
</html>