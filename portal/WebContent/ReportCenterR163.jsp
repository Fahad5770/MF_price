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
int FeatureID = 194;

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


Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");

if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	StartDate = new Date();
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}

//out.print("StartDate = "+StartDate);
//out.print("EndDate = "+EndDate);


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
boolean IsDistributorSelected=false;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors"); 
	IsDistributorSelected = true;
}else{

	/*
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
		
		long DistributorArray[] = new long[1];
		DistributorArray[0] = UserDistributor[x].DISTRIBUTOR_ID;
		session.setAttribute(UniqueSessionID+"_SR1SelectedDistributors",DistributorArray);
		break;
	}*/
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


//PJP


String PJPIDs="";
long SelectedPJPArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPJP") != null){
	SelectedPJPArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPJP");
	PJPIDs = Utilities.serializeForSQL(SelectedPJPArray);
}

String WherePJP = "";
if (PJPIDs.length() > 0){
	WherePJP = " and codv.outlet_id in (SELECT distinct outlet_id FROM distributor_beat_plan_schedule where id in("+PJPIDs+"))";	
}

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Sales</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					
					 <thead>
					    <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center; ">Outlet Type</th>
							
							<%
							int PackageCount = 0;
							
							//System.out.println("SELECT  package_id, package_label, unit_per_sku FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM inventory_sales_adjusted_products) and package_id in (select distinct ipv.package_id from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap ON isa.id = isap.id join inventory_products_view ipv ON isap.product_id = ipv.product_id where isa.adjusted_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+") "+WherePackage+" order by package_sort_order");
							
							ResultSet rs = s2.executeQuery("SELECT  distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM inventory_sales_adjusted_products) and package_id in (select distinct ip.package_id from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap ON isa.id = isap.id join inventory_products ip ON isap.product_id = ip.id where isa.adjusted_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+") "+WherePackage+" order by package_sort_order");
							int  PacakgeID=0;
							int OutletID=0;
							double GrandTotal=0;
							while(rs.next()){
								
								PackageCount++;
							
							%>
							<th data-priority="1"  style="text-align:center; " >
							<%=rs.getString("package_label")%>							
							
							</th>
							
							<%
							}
							
							long PackageTotal[] = new long[PackageCount];
							int PackageTotalUnitPerSKU[] = new int[PackageCount];
							long PackageTotalPromotion[] = new long[PackageCount];
							
							for (int i = 0; i < PackageTotal.length; i++){
								PackageTotal[i] = 0;								
								PackageTotalPromotion[i]=0;
								PackageTotalUnitPerSKU[i]=0;
							}
							%>	
						<th style="text-align:center; ">Converted Cases</th>					
					    </tr>
					
						    <%
						    rs.beforeFirst();					   
						    	
						    %>
						    
					  </thead> 
					<tbody>
						<%
						double TotalConverteCases=0;
						double TotalConverteCasesFinal=0;
						//System.out.println("select * from com mon_distributors where is_active=1 and type_id=2"+WhereHOD);
						ResultSet rs1 = s.executeQuery("select id,label from common_outlets_types where id in (select distinct co.type_id from inventory_sales_adjusted isa join common_outlets co on isa.outlet_id = co.id where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" ) order by id");
						while(rs1.next()){
							OutletID = rs1.getInt(1);
						%>
						<tr>
								<td><%=rs1.getString(1) + " - "+ rs1.getString(2) %></td>
								
							<%
							
							//ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM pep.mobile_order_products) "+WherePackage+" order by package_sort_order");//package query
							rs.beforeFirst();
							int PackageIndex = 0;
							while(rs.next()){
							int unit_per_sku=0;
							
							PacakgeID=rs.getInt("package_id");
								
								//System.out.println("select sum(total_units) qty,ipv.unit_per_sku from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.adjusted_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.distributor_id = "+DistributorID+" and ipv.package_id ="+PacakgeID+WherePackage);	
								//ResultSet rs3 = s3.executeQuery("select sum(total_units) qty,ipv.unit_per_sku from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.adjusted_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.distributor_id = "+DistributorID+" and ipv.package_id ="+PacakgeID+WherePackage);	
								
								ResultSet rs3 = s3.executeQuery("select "+    
										"sum(total_units) sale_amount "+							        
								",ipv.unit_per_sku, ipv.liquid_in_ml, ipv.conversion_rate_in_ml "+
								"from "+
								    "inventory_sales_adjusted isa "+
								        "join "+
								    "inventory_sales_adjusted_products isap ON isa.id = isap.id "+
								        "join "+
								    "inventory_products_view ipv ON isap.product_id = ipv.product_id join common_outlets co on isa.outlet_id = co.id "+
								"where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and co.type_id="+rs1.getInt("id")+" and isap.is_promotion = 0 and ipv.package_id ="+PacakgeID+WherePackage);
								
								
								
								while(rs3.next()){
									
									
									
									double liquid_in_ml = rs3.getDouble("liquid_in_ml");
									long sales_amount = rs3.getLong("sale_amount");
									long sales_amount_promotion = 0;//rs3.getLong("sale_amount_promotion");
									unit_per_sku = rs3.getInt("unit_per_sku");
									
									PackageTotal[PackageIndex] += sales_amount;
									PackageTotalPromotion[PackageIndex] += sales_amount_promotion;
									
									if(unit_per_sku > 0){
										PackageTotalUnitPerSKU[PackageIndex] = unit_per_sku;
									}
									
									double converted_cases = (sales_amount * liquid_in_ml) / rs3.getDouble("conversion_rate_in_ml");
									//System.out.println(converted_cases);
									
									TotalConverteCases+=converted_cases;
									
							%>
								
								
								<td style="text-align: right;"><%= Utilities.convertToRawCases(sales_amount,unit_per_sku) %> </td> 
								
								
							<%
								}
								
								%>
								
								<%
								PackageIndex++;
							}
							%>
							<td style="text-align:right;"><%=Math.round(TotalConverteCases) %></td>
						</tr>
						<%
						TotalConverteCasesFinal+=TotalConverteCases;
						TotalConverteCases=0;
						
						}
						%>
						<tr>
						
						<td style="font-weight:bold">Total</td>
						<%
						
						for (int i = 0; i < PackageTotal.length; i++){
							
							%>
							
							<td style="text-align: right;"><%=Utilities.convertToRawCases(PackageTotal[i]+PackageTotalPromotion[i],PackageTotalUnitPerSKU[i])%></td> 
							
						<%
						}
						%>
						<td style="text-align: right;"><%=Math.round(TotalConverteCasesFinal) %></td>
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