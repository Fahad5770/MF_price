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
<%@page import="com.pbc.distributor.DistributorDashboard"%>

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
int FeatureID = 101;

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

//SM


String SMIDs="";
long SelectedSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedSM") != null){
	SelectedSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedSM");
	SMIDs = Utilities.serializeForSQL(SelectedSMArray);
}

String WhereSM = "";
if (SMIDs.length() > 0){
	WhereSM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
}

//TDM


String TDMIDs="";
long SelectedTDMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedTDM") != null){
	SelectedTDMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedTDM");
	TDMIDs = Utilities.serializeForSQL(SelectedTDMArray);
}

String WhereTDM = "";
if (TDMIDs.length() > 0){
	WhereTDM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
}

//ASM


String ASMIDs="";
long SelectedASMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedASM") != null){
	SelectedASMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedASM");
	ASMIDs = Utilities.serializeForSQL(SelectedASMArray);
}

String WhereASM = "";
if (ASMIDs.length() > 0){
	WhereASM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
}


Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);

%>


<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Discounted Sales Summary</li>
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
							
							ResultSet rs = s2.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM inventory_sales_adjusted_products) "+WherePackage+" order by package_sort_order");
							int PacakgeID=0;
							int DistributorID=0;
							double GrandTotal=0;
							while(rs.next()){
								
								PackageCount++;
							
							%>
							<th data-priority="1" colspan="3"  style="text-align:center; " >
							<%=rs.getString("package_label")%>							
							
							</th>
							
							<%
							}
							
							long PackageTotal[] = new long[PackageCount];
							long PackageTotalNonDisc[] = new long[PackageCount];
							for (int i = 0; i < PackageTotal.length; i++){
								PackageTotal[i] = 0;
								PackageTotalNonDisc[i]=0;
							}
							%>	
												
					    </tr>
					    <tr style="font-size:11px;">
						    <th data-priority="1"  style="text-align:center; ">&nbsp;</th>
						    <%
						    rs.beforeFirst();
						    while(rs.next()){
						    	
						    %>
						   
						    <th data-priority="1"  style="text-align:center; min-width:25px;background-color:#F6F6F6;">D</th>
						    <th data-priority="1"  style="text-align:center; min-width:25px;">N</th>
						    <th data-priority="1"  style="text-align:center; min-width:25px;">R</th>
					    <%} %>
					    
					    </tr>
					    
					  </thead> 
					<tbody>
						<%
						//System.out.println("select * from common_distributors where is_active=1 and type_id=2"+WhereHOD);
						//System.out.println("this is ");
						DistributorDashboard dd = new DistributorDashboard();
						ResultSet rs1 = s.executeQuery("select * from common_distributors where is_active=1 and type_id=2"+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+" and distributor_id in ("+DistributorIDs+")"); //distributor query
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
								int UnitPerSKU = rs.getInt("unit_per_sku");
							PacakgeID=rs.getInt("package_id");
								
							//select sum(total_units) qty from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.adjusted_on between '2014-03-01' and '2014-03-30' and isa.distributor_id = 100669 and ipv.package_id = 1	
								//ResultSet rs3 = s3.executeQuery("select sum(total_units) qty from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.adjusted_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.distributor_id = "+DistributorID+" and ipv.package_id ="+PacakgeID+WherePackage);	
							//System.out.println("select sum(total_units) qty from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.adjusted_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.distributor_id = "+DistributorID+" and ipv.package_id ="+PacakgeID+WherePackage);	
							
							//System.out.println("hello "+PackageIDs);
							double DiscSumNonDiscSum[] = new double[2];
							double DiscountedNonDisc =0;							
							double RatioDiscNonDisc=0;
							
							//if(PackageIDs ==""){ //mean no package in session
								DiscSumNonDiscSum = dd.getDistributorDiscountedAndNonDiscounted(DistributorID, PacakgeID, StartDate, EndDate);
							
							//System.out.println("PackID "+PacakgeID);
							
							//}else{
							//	DiscSumNonDiscSum = dd.getDistributorDiscountedAndNonDiscountedMain(DistributorID, PackageIDs, StartDate, EndDate);
							//}
							
							
							DiscountedNonDisc = DiscSumNonDiscSum[0]+ DiscSumNonDiscSum[1];
							if(DiscountedNonDisc !=0){
								RatioDiscNonDisc = (DiscSumNonDiscSum[0]/DiscountedNonDisc)*100; //calculating ratio	
							}
							
							
							PackageTotal[PackageIndex] += DiscSumNonDiscSum[0];
							PackageTotalNonDisc[PackageIndex] += DiscSumNonDiscSum[1];
							
									
							%>
								<td style="text-align: right; background-color:#F6F6F6;" ><%if(DiscSumNonDiscSum[0]!= 0){%><%=Utilities.convertToRawCases(Math.round(DiscSumNonDiscSum[0]), UnitPerSKU)%><%} %></td> 
								<td style="text-align: right;" ><%if(DiscSumNonDiscSum[1]!= 0){%><%=Utilities.convertToRawCases(Math.round(DiscSumNonDiscSum[1]), UnitPerSKU) %><%} %></td> 
								<td style="text-align: right;"><%if(RatioDiscNonDisc!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(RatioDiscNonDisc) %>%<%} %></td>
							<%
								
								
								
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
						rs.beforeFirst();
						int index = 0;
						//double TotalDiscNonDisc=0;
						//double TotalDiscNonDiscRatio=0;
						while(rs.next()){
							int UnitPerSKU = rs.getInt("unit_per_sku");
							double TotalDiscNonDisc=0;
							double TotalDiscNonDiscRatio=0;
							TotalDiscNonDisc = PackageTotal[index] + PackageTotalNonDisc[index]; //D+N
							
							if(TotalDiscNonDisc !=0){
								TotalDiscNonDiscRatio = (PackageTotal[index]/TotalDiscNonDisc)*100;								
							}
							
						//for (int i = 0; i < PackageTotal.length; i++){
							%>
							<td style="text-align: right;background-color:#F6F6F6;"><%if(PackageTotal[index] !=0){%><%= Utilities.convertToRawCases(Math.round(PackageTotal[index]),UnitPerSKU) %><%} %></td> 
							<td style="text-align: right;"><%if(PackageTotalNonDisc[index] !=0){%><%= Utilities.convertToRawCases(Math.round(PackageTotalNonDisc[index]),UnitPerSKU) %><%} %></td>
							<td style="text-align: right;"><%if(TotalDiscNonDiscRatio!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(TotalDiscNonDiscRatio)%>%<%} %></td>
							
						<%
						index++;
						}
						%>
						</tr>
						
						
						
					</tbody>
						
				</table>
				
				
		</td>
	</tr>
</table>
<table border=0 style="font-size:13px; font-weight: 400; width:25%; margin-top:10px;" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
				
				 <tr style="font-size:11px;background:#ececec;">
						 	<th>Legend</th>
						 </tr>
						 <tr>
						 <td>D = Discounted</td>
						 </tr>
						 <tr>
						 <td>N = Non Discounted</td>
						 </tr>
						 <tr>
						 <td>R = Ratio of Discounted to Total Sales</td>
						 </tr>
				</table>

	</li>	
</ul>

<%
dd.close();
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>