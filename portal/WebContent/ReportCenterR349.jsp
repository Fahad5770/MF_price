<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.inventory.StockPosting"%>
<%@page import="java.util.Date"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="com.pbc.common.Distributor"%>
<script>

function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}

</script>

<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 461;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

//Date date = Utilities.parseDate(request.getParameter("Date"));

long SelectedPackagesArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPackages") != null){
   	SelectedPackagesArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPackages");           	
}

long SelectedBrandArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedBrands") != null){
	   SelectedBrandArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedBrands");           	
}

Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");

if(StartDate == null){
	StartDate = new Date();
}

if(EndDate == null){
	EndDate = new Date();
}

Date OpeningDate = DateUtils.addDays(StartDate, -1);

//out.print("StartDate = "+OpeningDate);
//out.print("EndDate = "+EndDate);

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



long SelectedOrderBookerArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers");           	
}




String PackageIDs = "";
String WherePackage = "";

/* if(SelectedPackagesArray!= null && SelectedPackagesArray.length > 0){
	for(int i = 0; i < SelectedPackagesArray.length; i++){
		if(i == 0){
			PackageIDs += SelectedPackagesArray[i]+"";
		}else{
			PackageIDs += ", "+SelectedPackagesArray[i]+"";
		}
	}
	WherePackage = " and package_id in ("+PackageIDs+") ";
}
 */

String BrandIDs = "";
String WhereBrand = "";

/* if(SelectedBrandArray!= null && SelectedBrandArray.length > 0){
	for(int i = 0; i < SelectedBrandArray.length; i++){
		if(i == 0){
			BrandIDs += SelectedBrandArray[i]+"";
		}else{
			BrandIDs += ", "+SelectedBrandArray[i]+"";
		}
		
	}

	WhereBrand = " and brand_id in ("+BrandIDs+") ";
} */

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


%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Stock Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border="0" style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0" class="GridWithBorder">
					 <thead>
					    <tr style="font-size: 11px;">
							
							<th data-priority="1"  style="text-align:left; width: 12%">Distributor ID</th>
							<th data-priority="1"  style="text-align:left; width: 12%" >Brand Label</th>
							<th data-priority="1"  style="text-align:left; width: 12%" >Raw Cases</th>
							<th data-priority="1"  style="text-align:left; width: 12%">Bottles</th>
							<th data-priority="1"  style="text-align:left; width: 12%">Total Units</th>
							<th data-priority="1"  style="text-align:left; width: 12%">Liquid in ML</th>
						<!-- 	<th data-priority="1"  style="text-align:left; width: 12%">Closing Stock</th> -->
					    </tr>
									    
					  </thead> 
					
				<%
				//int arrayAsString =SelectedDistributorsArray;
					System.out.println("SELECT (select name from common_distributors  where distributor_id=idst.to_distributor_id ) as name,idst.to_distributor_id, idstp.product_id, idstp.raw_cases, idstp.units, idstp.total_units, idstp.liquid_in_ml FROM inventory_distributor_stock_transfer idst INNER JOIN inventory_distributor_stock_transfer_products idstp ON idst.id = idstp.id WHERE idst.from_distributor_id in ("+DistributorIDs+")");
					ResultSet rs = s.executeQuery("SELECT (select name from common_distributors  where distributor_id=idst.to_distributor_id ) as name,idst.to_distributor_id, idstp.product_id, idstp.raw_cases, idstp.units, idstp.total_units, idstp.liquid_in_ml FROM inventory_distributor_stock_transfer idst INNER JOIN inventory_distributor_stock_transfer_products idstp ON idst.id = idstp.id WHERE idst.from_distributor_id in ("+DistributorIDs+")");
					while(rs.next()){
						
						int ProductID = rs.getInt("product_id");
						int raw_cases_received = rs.getInt("raw_cases");
						int units_received = rs.getInt("units");
						int total_units_received = rs.getInt("total_units");
						double liquid_in_ml_received = rs.getDouble("liquid_in_ml");
						String Distributor = rs.getString("to_distributor_id");
						String Distributor_Name = rs.getString("name");
						
						%>
						<%-- <tr style="background:#ececec">
	   	            		<td align="left"><%=Distributor%></td>
	   	            		<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec" colspan="8">&nbsp;</td>
	   	            	</tr> --%>
						<%
						 System.out.println("select concat(brand_label, '   ' , package_label) AS brand_labell from inventory_products_view where product_id="+ProductID);
						ResultSet rs2 = s2.executeQuery("select concat(brand_label, '   ' , package_label) AS brand_label from inventory_products_view where product_id="+ProductID);
						while(rs2.next()){
							String BrandID = rs2.getString("brand_label"); 
					
							StockPosting sp = new StockPosting(true);

							//long OpeningUnits = sp.getClosingBalance(SelectedDistributorsArray, ProductID, OpeningDate);
							long ClosingUnits = sp.getClosingBalance(SelectedDistributorsArray, ProductID, EndDate);
							//long IssuedUnitsDispatchOnly = sp.getStockIssuanceDispatchOnly(SelectedDistributorsArray, ProductID, StartDate, EndDate);
							//long IssuedUnitsLessDispatch = sp.getStockIssuanceLessDispatch(SelectedDistributorsArray, ProductID, StartDate, EndDate);
							
						/* 	long ReceivedUnitsLiftingOnly = sp.getStockReceiptsLiftingOnly(SelectedDistributorsArray, ProductID, StartDate, EndDate);
							long ReceivedUnitsLessLifting = sp.getStockReceiptsLessLifting(SelectedDistributorsArray, ProductID, StartDate, EndDate);
							
							 */
							long PendingDispatch = sp.getBalanceafterdispatch(SelectedDistributorsArray , ProductID);
							 System.out.println("ProductID============>" + ProductID);
								System.out.println("ClosingUnits============>" + ClosingUnits);

							 System.out.println("SelectedDistributorsArray============>" + SelectedDistributorsArray);
							System.out.println("PendingDispatch============>" + PendingDispatch);
							long totalUnits= ClosingUnits - PendingDispatch; 
							System.out.println("totalUnits============>" + totalUnits);

		
							%>
							
							<tr>
								<td style="padding-left:20px;text-align:left"><%=Distributor+ ' ' +Distributor_Name %></td>
		    					<td style="padding-left:20px;text-align:left"; padding-right:10px;><%=rs2.getString("brand_label")%></td>
 		    					<td style="text-align:left; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=rs.getInt("raw_cases")%></td>
		    					<td style="text-align:left; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=rs.getInt("units")%></td>
		    					<td style="text-align:left; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=rs.getInt("total_units")%></td>
		    					<td style="text-align:left; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=rs.getDouble("liquid_in_ml")%></td>
								<td style="text-align:left; padding-right:10px; display:none; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%if (totalUnits < 0){%>E<%}else{%><%=totalUnits%><%} %></td>
								
										    				
		    					
		    		    	</tr>
							
							<%
						}
						
					}
					
				%>
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