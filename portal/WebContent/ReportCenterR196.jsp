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
int FeatureID = 242;
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
	WhereDistributors = " and cache_distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
	/*ResultSet rsd = s.executeQuery("SELECT id FROM common_outlets where cache_distributor_id in ("+DistributorIDs+")");
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
	*/
}


%>




<table border="0" style="width: 100%">
	<tr>
		
		
		<td style="width: 30%" valign="top">
		
			
			
			
				
			<%
			
			if((IsDistributorSelected && SelectedDistributorsArray.length == 1) ){
				
				
			
			%>	
				
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="c" data-count-theme="c" style="margin-top:-13px;">
				<li data-role="list-divider" data-theme="a">Outlet Productivity</li>
				<li>
					<table border=0 style="font-size:8pt; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
						<tr>
							<th style="width: 20%">Outlet</th>
							<th style="width: 20%; text-align: center;">Planned Calls</th>
							<th style="width: 20%; text-align: center;">Completed Calls</th>
							<th style="width: 20%; text-align: center;">Percentage</th>
						</tr>					
						<%
						
						//String Sql2 = "select id, name from common_outlets where id in (select distinct outlet_id from inventory_sales_adjusted isa where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereOutlets+WhereDistributorOutlets+")";
						String Sql = "SELECT id, name FROM common_outlets where 1=1 "+WhereDistributors+" order by name";
						ResultSet rs = s.executeQuery(Sql);
						
						while(rs.next()){
							
							double NumOfOrders = 0;
							ResultSet rs2 = s2.executeQuery("SELECT count(distinct(created_on_date)) total_num_of_orders FROM inventory_sales_adjusted where created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and outlet_id="+rs.getString("id"));
							if(rs2.first()){
								NumOfOrders = rs2.getInt("total_num_of_orders");
							}
							
							double NumOfPlannedVisits = 0;
							Date CurrentDate = StartDate;
							
							while(true){
								
								ResultSet rs3 = s2.executeQuery("SELECT id FROM distributor_beat_plan_view where outlet_id="+rs.getString("id")+" and day_number=dayofweek("+Utilities.getSQLDate(CurrentDate)+")");
								if(rs3.first()){
									NumOfPlannedVisits++;
								}
								
								if( DateUtils.isSameDay(CurrentDate, EndDate) ){
									break;
								}
								
								CurrentDate = Utilities.getDateByDays(CurrentDate, 1);
							}
							
							double Percentage = 0;
							if(NumOfPlannedVisits > 0){
								Percentage = ( NumOfOrders / NumOfPlannedVisits ) * 100;
							}
							%>	
							
							
													
							<tr>
								<td><%=rs.getString("id")%> - <%=rs.getString("name")%></td>
								<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatRounded(NumOfPlannedVisits)%></td>
								<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatRounded(NumOfOrders)%></td>
								<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatOneDecimal(Percentage)%>%</td>
							</tr>							
							<%
							
						}
						%>
						
					</table>
				</li>
			</ul>
		
		<%
					
			}else{ %>
			
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="c" data-count-theme="c" style="margin-top:-13px;">
				<li data-role="list-divider" data-theme="a">Outlet Productivity</li>
			
			<li style="font-size: 12px; font-weight: normal">Please select a distributor</li>
			
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