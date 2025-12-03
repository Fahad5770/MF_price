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
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>


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
int FeatureID = 226;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();
Statement s5 = c.createStatement();


//Date date = Utilities.parseDate(request.getParameter("Date"));


Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");




if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	//StartDate = new Date(); // add code of start of current month if first time report opens
	Calendar cc = Calendar.getInstance();   // this takes current date
    cc.set(Calendar.DAY_OF_MONTH, 1);
    StartDate = cc.getTime();
     
	
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}


int SelectedMonth = Utilities.getMonthNumberByDate(StartDate);
int SelectedYear = Utilities.getYearByDate(StartDate);



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
String WhereHOD1 = "";
String WhereHOD2 = "";
String WhereHOD3 = "";
String WhereHOD4 = "";
if (HODIDs.length() > 0){
	WhereHOD = " and distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
	
}


//RSM


String RSMIDs="";
long SelectedRSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRSM") != null){
	SelectedRSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRSM");
	RSMIDs = Utilities.serializeForSQL(SelectedRSMArray);
}

String WhereRSM = "";
String WhereRSM1 = "";
String WhereRSM2 = "";
String WhereRSM3 = "";
if (RSMIDs.length() > 0){
	WhereRSM = " and distributor_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
	
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



long SelectedBrandArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedBrands") != null){
	   SelectedBrandArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedBrands");           	
}


String BrandIDs = "";
String WhereBrand = "";

if(SelectedBrandArray!= null && SelectedBrandArray.length > 0){
	for(int i = 0; i < SelectedBrandArray.length; i++){
		if(i == 0){
			BrandIDs += SelectedBrandArray[i]+"";
		}else{
			BrandIDs += ", "+SelectedBrandArray[i]+"";
		}
		
	}

	WhereBrand = " and ipv.brand_id in ("+BrandIDs+") ";
}




//Distributor

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



long DistributorID = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
	DistributorID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
}
String WhereCustomerID ="";
String WhereCustomerID1 ="";
String WhereCustomerID2 ="";
String WhereCustomerID3 ="";
if (DistributorID != 0){
	WhereCustomerID = " and customer_id in ("+DistributorID+") ";
	WhereCustomerID1 = " and isa.distributor_id in ("+DistributorID+") ";
	WhereCustomerID2 = " and tab1.distributor_id in ("+DistributorID+") ";
	WhereCustomerID3 = " and distributor_id in ("+DistributorID+") ";

}else{
	WhereCustomerID = " and customer_id in ("+DistributorIDs+") ";
	WhereCustomerID1 = " and isa.distributor_id in ("+DistributorIDs+") ";
	WhereCustomerID2 = " and tab1.distributor_id in ("+DistributorIDs+") ";
	WhereCustomerID3 = " and distributor_id in ("+DistributorIDs+") ";
	
}


//System.out.println("Hello "+WhereCustomerID);



//GTM Category


String GTMCategoryIDs="";
long SelectedGTMCategoryArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedGTMCategory") != null){
	SelectedGTMCategoryArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedGTMCategory");
	GTMCategoryIDs = Utilities.serializeForSQL(SelectedGTMCategoryArray);
}

String WhereGTMCategory = "";
String WhereGTMCategory1 = "";
String WhereGTMCategory2 = "";
String WhereGTMCategory3 = "";
if (GTMCategoryIDs.length() > 0){
	WhereGTMCategory = " and customer_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";	
	WhereGTMCategory1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";
	WhereGTMCategory2 = " and tab1.distributor_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";
	WhereGTMCategory3 = " and distributor_id in(SELECT distributor_id FROM common_distributors where category_id in ("+GTMCategoryIDs+")) ";
}


//System.out.println("Hello "+WhereCustomerID);





%>





<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:8pt; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size:8pt;">
					   		<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
					   		<th data-priority="1"  style="text-align:center; " colspan="3">&nbsp;</th>
							<%
							
							ResultSet rs21 = s.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM inventory_delivery_note_products) and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs21.next()){
								%>
								<th data-priority="1"  style="text-align:center; " colspan="<%=rs21.getInt("package_count")*3%>"><%=rs21.getString("type_name") %></th>
								<%
							}
							
							%>
						
											
					    </tr>
					    
					    <tr style="font-size:8pt;">
							<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
							<th data-priority="1"  style="text-align:center; "colspan="3">Converted</th>
							
							<%
							
							int PackageCount = 0;
							
							
							int ArrayCount=0;
							ResultSet rs12 = s.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM inventory_delivery_note_products) and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs12.next()){
								
								ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM inventory_delivery_note_products) "+WherePackage+" and category_id = 1 and lrb_type_id="+rs12.getLong("lrb_type_id")+" order by package_sort_order");
								while(rs2.next()){
							%>
								<th data-priority="1"  style="text-align:center; " colspan="3"><%=rs2.getString("package_label") %></th>
							<%
							
								}
							}
								
							
							
							//System.out.println(ArrayCount);
							
							
							%>
							
											
						    </tr>
						    
						     <tr style="font-size:8pt;">
							<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
							<th data-priority="1"  style="text-align:center; ">L</th>
								<th data-priority="1"  style="text-align:center; ">S</th>
								<th data-priority="1"  style="text-align:center; ">C</th>
							
							<%
							
							
							ResultSet rs11 = s.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM inventory_delivery_note_products) and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs11.next()){
								
								ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM inventory_delivery_note_products) "+WherePackage+" and category_id = 1 and lrb_type_id="+rs11.getLong("lrb_type_id")+" order by package_sort_order");
								while(rs2.next()){
							%>
								<th data-priority="1"  style="text-align:center; ">L</th>
								<th data-priority="1"  style="text-align:center; ">S</th>
								<th data-priority="1"  style="text-align:center; ">C</th>
							<%
							
								}
							}
							
							//System.out.println(ArrayCount);
							
							
							%>
							
										
						    </tr>
						    
						    
						    
					    	<%
					    	ResultSet rs50 = s5.executeQuery("select * from common_distributors where is_active=1 and type_id=2"+WhereHOD+WhereRSM+WhereDistributors);
					    	while(rs50.next()){
					    		
					    		
					    		
								double GrossRevenueArray[] = new double [ArrayCount];
								double SalesPromotionArray[] = new double [ArrayCount];
								double UpfrontDiscountArray[] = new double [ArrayCount];
								double PerCaseDiscount[] = new double [ArrayCount];
								double FixedDiscountArray[] = new double [ArrayCount];
								double FreightArray[] = new double [ArrayCount];
								double UnloadingArray[] = new double [ArrayCount];
								double CasesSoldArray[] = new double [ArrayCount];
								double CasesSoldArray1[] = new double [ArrayCount];
								double NetRevenueArray[] = new double [ArrayCount];
					    		
					    	long CustomerID = rs50.getLong(1);
					    	String CustomerName = rs50.getString(2);
					    	
					    	
					    	
					    	%>
					    	<tr><td><%=CustomerID %>-<%=CustomerName %></td>
					    	<%
					    	//Converted
					    	double convertedLiftqtyl=0;
					    	 int convertedLiftunit_per_sku=0;
					    	 
					    	 double convertedSecqtyl=0;
					    	 int convertedSecunit_per_sku=0;
					    	 
					    	// System.out.println("Converted lifting --- "+"select sum(total_units*ipv.liquid_in_ml) qty,ipv.unit_per_sku from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where idn.created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+" and idn.distributor_id = "+CustomerID+" and ipv.category_id = 1 ");
					    	 
					    	ResultSet rs311 = s4.executeQuery("select sum(total_units*ipv.liquid_in_ml) qty,ipv.unit_per_sku from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where idn.created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+" and idn.distributor_id = "+CustomerID+" and ipv.category_id = 1 ");
					    	if(rs311.first()){
					    		  convertedLiftqtyl = rs311.getDouble("qty");
								 
					    	}
					    	
					    	//converted secondary
					    	
					    	//System.out.println("Converted Secondary --- "+"select sum(total_units*ipv.liquid_in_ml) qty,ipv.unit_per_sku from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+" and isa.distributor_id = "+CustomerID);
					    	
					    	ResultSet rs312 = s4.executeQuery("select sum(total_units*ipv.liquid_in_ml) qty from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+" and isa.distributor_id = "+CustomerID);
					    	if(rs312.first()){
					    		convertedSecqtyl = rs312.getDouble("qty");
					    		
					    	}
					    	
					    	
					    	double conversion1=0;
							if(convertedLiftqtyl!=0){
							 conversion1 = (convertedSecqtyl/convertedLiftqtyl)*100;
							}else{
								conversion1=0;
							}
					    	
							//System.out.println("Converted %age "+conversion1);
							
							
					    	%>
					    	<td style="text-align: right;"><%= Utilities.getDisplayCurrencyFormat(convertedLiftqtyl/6000) %></td>
					    	<td><%= Utilities.getDisplayCurrencyFormat(convertedSecqtyl/6000) %></td>
					    	<td style="text-align: right;"><%if(conversion1!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(conversion1) %>%<%} %></td>
					    	
					    	
					    	
					    	
					    	<%
					    	
					    	int lm1=0;
					    	double CasesSoldConverted1=0;
					    	double ConvertedSalesPromotion=0;
					    	double ConvertedUpfront=0;
							ResultSet rs101 = s.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM inventory_delivery_note_products) and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs101.next()){
								int TypeID = rs101.getInt(2);
								
								ResultSet rs4 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM inventory_delivery_note_products) "+WherePackage+" and category_id = 1 and lrb_type_id="+TypeID+" order by package_sort_order");
								while(rs4.next()){
									
									
									int PackageID = rs4.getInt(1);
									
									int unit_per_sku=0;
									double qtys=0;
									double qtyl=0;
									
									%>
									
									<%

									ResultSet rs3 = s4.executeQuery("select sum(total_units) qty,ipv.unit_per_sku from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where idn.created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+" and idn.distributor_id = "+CustomerID+" and ipv.category_id = 1 and ipv.package_id ="+PackageID+" and ipv.lrb_type_id="+TypeID+WherePackage+" "+WhereBrand);	
								while(rs3.next()){
									 qtyl = rs3.getDouble("qty");
									unit_per_sku = rs3.getInt("unit_per_sku");
									
									
									%>
									 
							<%
							
								}
								
								
								
							
							%>
									<td style="text-align: right;"><%= Utilities.convertToRawCases(Math.round(qtyl) ,unit_per_sku) %></td>
									
									<%

									ResultSet rs31 = s4.executeQuery("select sum(total_units) qty,ipv.unit_per_sku from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+" and isa.distributor_id = "+CustomerID+"  and ipv.package_id ="+PackageID+" and ipv.lrb_type_id="+TypeID+WherePackage+" "+WhereBrand);	
								while(rs31.next()){
									 qtys = rs31.getDouble("qty");
									unit_per_sku = rs31.getInt("unit_per_sku");
									
									
									%>
									
							<%
							
								}
								
								%>
								<td style="text-align: right;"><%= Utilities.convertToRawCases(Math.round(qtys),unit_per_sku) %></td> 
								
								<%
								double conversion=0;
								if(qtyl!=0){
								 conversion = (qtys/qtyl)*100;
								}else{
									conversion=0;
								}
							
								//System.out.println("Normal %age "+conversion);
								
							%>
								
									
									
									<td style="text-align: right;"><%if(conversion!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(conversion) %>%<%}%></td>
									<%	
									
								
								}
							}
							
							
							
							%>
					    	
							
				    	</tr>
				    	<%
				    	}
				    	%>
					  </thead> 
							
				</table>
		</td>
	</tr>
</table>


<table border=0 style="font-size:13px; font-weight: 400; width:25%; margin-top:10px;" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
				
				 <tr style="font-size:11px;background:#ececec;">
						 	<th>Legend</th>
						 </tr>
						 <tr>
						 <td>L = Lifting</td>
						 </tr>
						 <tr>
						 <td>S = Secondary</td>
						 </tr>
						 <tr>
						 <td>C = Conversion</td>
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