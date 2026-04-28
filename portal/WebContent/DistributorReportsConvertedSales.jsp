<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<script>

function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}

</script>

<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 96;

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

//Date date = Utilities.parseDate(request.getParameter("Date"));


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

long SelectedOrderBookerArray[] = null;
if (session.getAttribute("SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute("SR1SelectedOrderBookers");           	
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
	WhereDistributors = " and isa.distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}

String OrderBookerIDs = "";
String WhereOrderBooker = "";
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
	OrderBookerIDsWher =" and isa.booked_by in ("+OrderBookerIDs+") ";
}


String OutletIds="";
long SelectedOutletArray[] = null;
if (session.getAttribute("SR1SelectedOutlets") != null){
	SelectedOutletArray = (long[])session.getAttribute("SR1SelectedOutlets");
	OutletIds = Utilities.serializeForSQL(SelectedOutletArray);
}

String WhereOutlets = "";
if (OutletIds.length() > 0){
	WhereOutlets = " and isa.outlet_id in ("+OutletIds+") ";	
}


//PJP


String PJPIDs="";
long SelectedPJPArray[] = null;
if (session.getAttribute("SR1SelectedPJP") != null){
	SelectedPJPArray = (long[])session.getAttribute("SR1SelectedPJP");
	PJPIDs = Utilities.serializeForSQL(SelectedPJPArray);
}

String WherePJP = "";
if (PJPIDs.length() > 0){
	WherePJP = " and isa.outlet_id in (SELECT distinct outlet_id FROM pep.distributor_beat_plan_view where id in ("+PJPIDs+")) ";	
}


//PJP


String HODIDs="";
long SelectedHODArray[] = null;
if (session.getAttribute("SR1SelectedHOD") != null){
	SelectedHODArray = (long[])session.getAttribute("SR1SelectedHOD");
	HODIDs = Utilities.serializeForSQL(SelectedHODArray);
}

String WhereHOD = "";
if (HODIDs.length() > 0){
	WhereHOD = " and isa.distributor_id in (SELECT distributor_id FROM common_distributors where snd in ("+HODIDs+")) ";	
}




%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Converted Sales</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size:11px;">
							<th data-priority="2" style="width: 28%" >&nbsp;</th>
							<th data-priority="1"  style="text-align:center; width: 12%">Plan</th>
							<th data-priority="1"  style="text-align:center; width: 12%">Actual</th>							
					    </tr>
					  </thead> 
					
					<%
					ResultSet rs = s.executeQuery("select sum(converted_cases) total_converted_cases, sum(total_amount) total_amount from ("+
													" SELECT isap.product_id, isap.total_units, (isap.total_units * ipv.liquid_in_ml / conversion_rate_in_ml) converted_cases, isap.total_amount FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id WHERE isap.is_promotion = 0 and isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereDistributors+OrderBookerIDsWher+WhereOutlets+WherePJP+WhereHOD+
												    " ) tab1");
					
					/*System.out.println("select sum(converted_cases) total_converted_cases, sum(total_amount) total_amount from ("+
							" SELECT isap.product_id, isap.total_units, (isap.total_units * ipv.liquid_in_ml / conversion_rate_in_ml) converted_cases, isap.total_amount FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id WHERE isap.is_promotion = 0 and isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereDistributors+OrderBookerIDsWher+WhereOutlets+WherePJP+WhereHOD+
						    " ) tab1");*/
					if(rs.first()){
						
					
					%>		
							
							<tr>
		    					<td style="padding-left:20px">Converted Cases</td>
		    					<td style="text-align:center; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec">N/A</td>
		    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("total_converted_cases")) %></td>
		    					
		    		    	</tr>
		    		    	<tr>
		    		    	<%
		    		    	double AvgPriceSold=0;
		    		    	if(rs.getDouble("total_converted_cases")!=0){
		    		    		 AvgPriceSold = rs.getDouble("total_amount")/rs.getDouble("total_converted_cases");			    		    		
		    		    	}
		    		    	
		    		    	%>
		    					<td style="padding-left:20px">Average Price Sold</td>
		    					<td style="text-align:center; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec">N/A</td>
		    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%= Utilities.getDisplayCurrencyFormat(AvgPriceSold)%></td>
		    					
		    		    	</tr>
		    		    	<%
					}
		    		%>  
		    		    	<tr>
		    					<td style="padding-left:20px">% Compliance to Mix</td>
		    					<td style="text-align:center; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec">N/A</td>
		    					<td style="text-align:center; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec">N/A</td>
		    					
		    		    	</tr>
		    		    	<tr>
		    					<td style="padding-left:20px">Receivable</td>
		    					<td style="text-align:center; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec">N/A</td>
		    					<td style="text-align:center; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec">N/A</td>
		    					
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