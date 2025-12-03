<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>

<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 238;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));
if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}
//
Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

//Date date = Utilities.parseDate(request.getParameter("Date"));

Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");

if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	StartDate = new Date();
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}
boolean IsOutletSelected=false;
String OutletIds="";
long SelectedOutletArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets") != null){
	IsOutletSelected=true;
	SelectedOutletArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets");
	OutletIds = Utilities.serializeForSQL(SelectedOutletArray);
}

String WhereOutlets = "";
if (OutletIds.length() > 0){
	WhereOutlets = " and outlet_id in ("+OutletIds+") ";	
}

boolean IsOrderBookerSelected=false;

int OrderBookerArrayLength=0;
long SelectedOrderBookerArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers");
	
	IsOrderBookerSelected=true;
	OrderBookerArrayLength=SelectedOrderBookerArray.length;
}



String OrderBookerIDs = "";
if(SelectedOrderBookerArray != null && SelectedOrderBookerArray.length > 0){
	for(int i = 0; i < SelectedOrderBookerArray.length; i++){
		if(i == 0){
			OrderBookerIDs += SelectedOrderBookerArray[i];
		}else{
			OrderBookerIDs += ", "+SelectedOrderBookerArray[i];
		}
	}
}
String OrderBookerIDsWher="";
if(OrderBookerIDs.length()>0){
	OrderBookerIDsWher =" and created_by in ("+OrderBookerIDs+") ";
}


//Distributor

long SelectedDistributorsArray[] = null;
boolean IsDistributorSelected=false;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	IsOrderBookerSelected=true;
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors"); 
	IsDistributorSelected = true;
}else{
}

String DistributorIDs = "";
String WhereDistributors = "";
String WhereDistributorOutletsIDs = "";
String WhereDistributorOutlets = "";
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
	ResultSet rsd = s.executeQuery("SELECT id FROM common_outlets where cache_distributor_id in ("+DistributorIDs+")");
	int counter = 0;
	while(rsd.next()){		
		if(counter == 0){
			WhereDistributorOutletsIDs += rsd.getString("id");
		}else{
			WhereDistributorOutletsIDs += ", " + rsd.getString("id");
		}
		
		counter++;		
	}
	
	WhereDistributorOutlets = " and outlet_id in ("+WhereDistributorOutletsIDs+")";
	
}


%>




<table border="0" style="width: 100%">
	<tr>
		
		
		<td style="width: 30%" valign="top">
		
			
			
			
				
			<%
			
			if((IsDistributorSelected && SelectedDistributorsArray.length == 1) || IsOutletSelected ){
				
				
			
			%>	
				
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="c" data-count-theme="c" style="margin-top:-13px;">
				<li data-role="list-divider" data-theme="a">Outlet Sale</li>
				<li>
					<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
						<tr>
							<th style="width: 20%">Outlet</th>
							<th style="width: 20%; text-align: center;">Date</th>
							<th style="width: 20%; text-align: center;">Raw Cases</th>
							<th style="width: 20%; text-align: center;">Converted Cases</th>
							<th style="width: 20%; text-align: center;">Amount</th>
						</tr>					
						<%
						
						double GrandTotalRawCases = 0;
						double GrandTotalConvertedCases = 0;
						double GrandTotalAmount = 0;
						
						String Sql2 = "select id, name from common_outlets where id in (select distinct outlet_id from inventory_sales_adjusted isa where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereOutlets+WhereDistributorOutlets+")";
						ResultSet rs2 = s2.executeQuery(Sql2);
						while(rs2.next()){
							
							double TotalRawCases = 0;
							double TotalConvertedCases = 0;
							double TotalAmount = 0;
							
							%>
							<tr>
								<td colspan="5"><%=rs2.getString("id")%> - <%=rs2.getString("name")%></td>
							</tr>
							<%
							
							String Sql = "select outlet_id, date(created_on) created_on, sum((isap.total_units/ipv.unit_per_sku)) raw, sum((isap.total_units*ipv.liquid_in_ml))/6000 converted, sum(isap.net_amount) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isap.is_promotion=0 and outlet_id="+rs2.getString("id")+" group by date(created_on) order by created_on";
							ResultSet rs = s.executeQuery(Sql);
							while(rs.next()){
								
								TotalRawCases += rs.getDouble(3);
								TotalConvertedCases += rs.getDouble(4);
								TotalAmount += rs.getDouble(5);

								GrandTotalRawCases += rs.getDouble(3);
								GrandTotalConvertedCases += rs.getDouble(4);
								GrandTotalAmount += rs.getDouble(5);
								
								%>
								<tr>
									<td>&nbsp;</td>
									<td style="text-align: center;"><%=Utilities.getDisplayDateFormat(rs.getDate("created_on"))%></td>
									<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(rs.getDouble(3))%></td>
									<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(rs.getDouble(4))%></td>
									<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(rs.getDouble(5))%></td>
								</tr>
								<%
							}
							
							%>
							<tr>
								<td>&nbsp;</td>
								<td style="text-align: center; font-weight: bold">&nbsp;</td>
								<td style="text-align: right; font-weight: bold"><%=Utilities.getDisplayCurrencyFormat(TotalRawCases)%></td>
								<td style="text-align: right; font-weight: bold"><%=Utilities.getDisplayCurrencyFormat(TotalConvertedCases)%></td>
								<td style="text-align: right; font-weight: bold"><%=Utilities.getDisplayCurrencyFormat(TotalAmount)%></td>
							</tr>
							<%
							
						}
						%>
						
						<tr>
							<td style="text-align: center; font-weight: bold">Total</td>
							<td style="text-align: center; font-weight: bold">&nbsp;</td>
							<td style="text-align: right; font-weight: bold"><%=Utilities.getDisplayCurrencyFormat(GrandTotalRawCases)%></td>
							<td style="text-align: right; font-weight: bold"><%=Utilities.getDisplayCurrencyFormat(GrandTotalConvertedCases)%></td>
							<td style="text-align: right; font-weight: bold"><%=Utilities.getDisplayCurrencyFormat(GrandTotalAmount)%></td>
						</tr>
						
					</table>
				</li>
			</ul>
		
		<%
					
			}else{ %>
			
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="c" data-count-theme="c" style="margin-top:-13px;">
				<li data-role="list-divider" data-theme="a">Orders</li>
			
			<li style="font-size: 12px; font-weight: normal">Please select an Outlet or Distributor(Only One)</li>
			
			</ul>
			
			<%} %>
		
		</td>
				
		
	</tr>
</table>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>