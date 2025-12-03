<%@page import="com.pbc.outlet.InventoryPackage"%>
<%@page import="com.pbc.outlet.MonthlyDiscount"%>
<%@page import="com.pbc.outlet.Advance"%>
<%@page import="java.util.Date"%>
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

Date dates[] = Utilities.getPastMonthsInDate(Utilities.getEndDateByMonth(11, SelectedYear), 12);



double SummaryTotals[] = new double[dates.length];
%>
<div data-role="page" id="OutletDashboardSales" data-url="OutletDashboardSales" data-theme="d">

    <jsp:include page="OutletDashboardHeader.jsp" >
    	<jsp:param value="<%=bean.OUTLET.NAME %>" name="title"/>
    	<jsp:param value="3" name="tab"/>
    	<jsp:param value="<%=bean.OUTLET.ID%>" name="OutletID"/>
    </jsp:include>
     <!-- /header -->
    
    <div data-role="content" data-theme="d">
    
    	<table style="width: 100%">
    		<tr>
    			<td valign="top" style="width: 50%">
    			
	    			<ul data-role="listview" data-inset="true" data-theme="d" data-divider-theme="c" data-count-theme="d" style="width: 100%" >
						<li data-role="list-divider">Sales including Outlets in Proximity</li>
						<li data-icon="false"><div id="SalesChart2" style="min-width: 400px; height: 400px; margin: 0 auto"></div></li>
					</ul>
				</td>
				<td valign="top" style="width: 50%">
	    			<ul data-role="listview" data-inset="true" data-theme="d" data-divider-theme="c" data-count-theme="d" style="width: 100%" >
						<li data-role="list-divider">Total Sales Analysis</li>
						<li data-icon="false"><div id="SalesChart1" style="min-width: 400px; height: 400px; margin: 0 auto"></div></li>
					</ul>
				
				</td>
			</tr>
			<tr>
				<td colspan="2"><hr></td>
			</tr>
			<tr>
				<td>
					<div style="width: 50%">
					<a id="OutletDashboardSalesLink" href="#" data-transition="none" style="display:none" >Sales</a>
					<script>
					
						var OutletID = <%=bean.OUTLET.ID%>;
	
						function reloadOutletDashboardSales(SelectedYear){
							$('#OutletDashboardSalesLink').attr("href", "OutletDashboardSales.jsp?outletID="+OutletID+"&ReportTitle=Sales&SelectedYear="+SelectedYear);
							$('#OutletDashboardSalesLink').trigger('click');
						}
						
					</script>
					
						
						<select name="FilterByYear" id="FilterByYear" data-mini="true" onchange="reloadOutletDashboardSales(this.value)">
							<option value="<%=PreviousYear%>" <% //if( SelectedYear == PreviousYear ){ out.print("selected"); } %> ><%=PreviousYear%></option>
							<option value="<%=CurrentYear%>" <% //if( SelectedYear == CurrentYear ){ out.print("selected"); } %> selected="selected" ><%=SelectedYear%></option>
							<option value="<%=NextYear%>" <% //if( SelectedYear == NextYear ){ out.print("selected"); } %> ><%=NextYear%></option>
						</select>
					</div>
				</td>
			</tr>
		<tr>
			<td colspan="2" style="text-align: right;">
							<table data-role="table" id="SalesComparisonTable" data-mode="table" class="ui-body-d ui-shadow table-stripe ui-responsive" style="font-size: 10pt;">
							         <thead>
							           <tr class="ui-bar-d">
							             <th>Package</th>
							             <%
							             for (Date idate : dates){
							             %>
							             	<th><%= Utilities.getDisplayDateMonthYearFormat(idate) %></th>
							             <%
							             }
							             %>
							             <th>Total</th>
							           </tr>
							         </thead>
							         <tbody>
										<%
										InventoryPackage PackList[] = bean.getPackageList();
										for (InventoryPackage pack : PackList ){
										%>
							           <tr>
							             <td><%=pack.LABEL %></th>
							             <%
							             double TotalQty = 0;
							             int count = 0;
							             for (Date idate : dates){
							            	 int imonth = Utilities.getMonthNumberByDate(idate);
							            	 int iyear = Utilities.getYearByDate(idate);
							            	 double qty[] = bean.getSalesByPackage(pack.ID, imonth, iyear);
							            	 TotalQty += qty[0];
							            	 SummaryTotals[count] += qty[1];
							             %>
							             	<td style="text-align: center;"><%if (qty[0] != 0){out.print(Math.round(qty[0]));}%></td>
							             <%
							             count++;
							             }
							             %>
							             <td style="text-align: center;"><%if (TotalQty != 0){out.print(Math.round(TotalQty));}%></td>
							           </tr>
							           <%
										}
							           %>
							           <tr>
							             <th>Converted Total</th>
							             <%
							             double ConvertedTotal = 0;
							             for (double total : SummaryTotals){
							            	 ConvertedTotal += total;
							             %>
							             	<th style="text-align: center;"><%= Math.round(total)%></th>
							             <%
							             }
							             %>
							             <th style="text-align: center;"><%if (ConvertedTotal != 0){out.print(Math.round(ConvertedTotal));}%></th>
							           </tr>
							           
							         </tbody>
							       </table>        
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
