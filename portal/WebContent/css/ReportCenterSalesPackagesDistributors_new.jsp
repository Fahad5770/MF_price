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
int FeatureID = 100;

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
	WhereHOD = " and distributor_id in (SELECT distributor_id FROM common_distributors where snd in ("+HODIDs+")) ";	
}





%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Sales Packages Distributors</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
							<%
							int PackageCount = 0;
							
							ResultSet rs = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM inventory_sales_adjusted_products) "+WherePackage+" order by package_sort_order");
							int PacakgeID=0;
							int DistributorID=0;
							double GrandTotal=0;
							while(rs.next()){
							
								PackageCount++;
							
							%>
							<th data-priority="1"  style="text-align:center; "><%=rs.getString("package_label")%></th>
							<%
							}
							
							long PackageTotal[] = new long[PackageCount];
							int PackageTotalUnitPerSKU[] = new int[PackageCount];
							for (int i = 0; i < PackageTotal.length; i++){
								PackageTotal[i] = 0;
								PackageTotalUnitPerSKU[i]=0;
							}
							%>							
					    </tr>
					  </thead> 
					<tbody>
						<%
						//System.out.println("select * from common_distributors where is_active=1 and type_id=2"+WhereHOD);
						ResultSet rs1 = s.executeQuery("select * from common_distributors where is_active=1 and type_id=2"+WhereHOD); //distributor query
						while(rs1.next()){
							DistributorID = rs1.getInt("distributor_id");
						%>
						<tr>
								<td><%=DistributorID + " - "+ rs1.getString("name") %></td>
							<%
							
							//ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM pep.mobile_order_products) "+WherePackage+" order by package_sort_order");//package query
							rs.beforeFirst();
							int PackageIndex = 0;
							while(rs.next()){
							PacakgeID=rs.getInt("package_id");
								
							//select sum(total_units) qty from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.adjusted_on between '2014-03-01' and '2014-03-30' and isa.distributor_id = 100669 and ipv.package_id = 1	
								ResultSet rs3 = s3.executeQuery("select sum(total_units) qty,ipv.unit_per_sku from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.adjusted_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.distributor_id = "+DistributorID+" and ipv.package_id ="+PacakgeID+WherePackage);	
								while(rs3.next()){
									long qty = rs3.getLong("qty");
									int unit_per_sku = rs3.getInt("unit_per_sku");
									PackageTotal[PackageIndex] += qty;
									PackageTotalUnitPerSKU[PackageIndex] = unit_per_sku;
									
							%>
								<td style="text-align: right;"><%= Utilities.convertToRawCases(qty,unit_per_sku) %></td> 
							<%
								}
								
								
								PackageIndex++;
							}
							%>
						</tr>
						<%
						}
						%>
						<tr>
						<td style="font-weight:bold">Total</td>
						<%
						System.out.println("===============================> ");
						for (int i = 0; i < PackageTotal.length; i++){
							System.out.println("====> PackageTotal[i]= "+PackageTotal[i]+", PackageTotalUnitPerSKU="+PackageTotalUnitPerSKU[i]);
							%>
							<td style="text-align: right;">12</td> 
						<%
						}
						%>
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