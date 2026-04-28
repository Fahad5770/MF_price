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
	WhereHOD = " and customer_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
	WhereHOD1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
	WhereHOD2 = " and tab1.distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";
	WhereHOD3 = " and distributor_id in(SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";
	WhereHOD4 = " and ss.customer_kunnr in(SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";
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
	WhereRSM = " and customer_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
	WhereRSM1 = " and isa.distributor_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
	WhereRSM2 = " and tab1.distributor_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
	WhereRSM3 = " and distributor_id in(SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";
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

Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);


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
							
							<%
							
							//String[] DiscountTitle= {"Gross Revenue","","","","","","","","","","",""};
							
							
							ResultSet rs = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
							while(rs.next()){
								%>
								<th data-priority="1"  style="text-align:center; " colspan="<%=rs.getInt("packge_count")%>"><%=rs.getString("type_name") %></th>
								<%
							}
							
							%>
							<th data-priority="1"  style="text-align:center; ">&nbsp;</th>					
					    </tr>
					    
					    <tr style="font-size:8pt;">
							<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
							
							<%
							int ArrayCount=0;
							
							ResultSet rs1 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
							while(rs1.next()){
								
								ResultSet rs2 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+rs1.getInt("product_type_id")+WherePackage);
								while(rs2.next()){
							%>
								<th data-priority="1"  style="text-align:center; "><%=rs2.getString("package_label") %></th>
							<%
							ArrayCount++;
								}
							}
							
							//System.out.println(ArrayCount);
							
							
							%>
							
							<th data-priority="1"  style="text-align:center; ">Total</th>					
						    </tr>
					    	<%
					    	ResultSet rs50 = s5.executeQuery("select distinct customer_id, (select name from common_distributors where distributor_id = customer_id) distributor_name from "+ds.logDatabaseName()+".bi_percase_price_invoice where 1 = 1 "+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+" and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
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
					    	
					    	int lm1=0;
					    	double CasesSoldConverted1=0;
					    	double ConvertedSalesPromotion=0;
					    	double ConvertedUpfront=0;
							ResultSet rs101 = s.executeQuery("SELECT product_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=product_type_id) type_name,count(distinct package_id) packge_count FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where 1=1 "+WherePackage+" group by product_type_id order by product_type_id");
							while(rs101.next()){
								int TypeID = rs101.getInt(1);
								
								ResultSet rs4 = s2.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM "+ds.logDatabaseName()+".bi_percase_price_invoice where product_type_id="+TypeID+WherePackage);
								while(rs4.next()){
									int PackageID = rs4.getInt(1);
									
									
									if (true){ // cases sold
										double GrossValue = 0;
										double CCGross = 0;
										ResultSet rs5 = s3.executeQuery("select sum(bppi.quantity), sum(((bppi.quantity*ip.unit_per_case)*ip.liquid_in_ml)/ip.conversion_rate_in_ml) from peplogs.bi_percase_price_invoice bppi join inventory_packages ip on bppi.package_id = ip.id where customer_id = "+CustomerID+" "+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+" and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and bppi.package_id="+PackageID+" and bppi.product_type_id="+TypeID);
										if (rs5.first()){
											GrossValue = rs5.getDouble(1);
											CCGross = rs5.getDouble(2);
										}
										CasesSoldArray1[lm1]=GrossValue;
										CasesSoldConverted1+=CCGross;
									}
									
									
									if (true){ // promotion
										double GrossValue = 0;
										double GrossValue1 = 0;
										ResultSet rs5 = s3.executeQuery("select sum(free_stock), sum(upfront_discount)*-1 from "+ds.logDatabaseName()+".bi_percase_price_invoice where customer_id = "+CustomerID+" and package_id = "+PackageID+" and product_type_id = "+TypeID+WhereHOD+WhereRSM+WhereCustomerID+WhereGTMCategory+"  and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
										if (rs5.first()){
											GrossValue = rs5.getDouble(1);
											GrossValue1 = rs5.getDouble(2);
										}
										SalesPromotionArray[lm1]=GrossValue;
										ConvertedSalesPromotion+=GrossValue;
										
										UpfrontDiscountArray[lm1]=GrossValue1;
										ConvertedUpfront+=GrossValue1;
										
										
									}
									
									double TotalDiscount = 0;
									if (CasesSoldArray1[lm1] != 0){
										TotalDiscount = (SalesPromotionArray[lm1] + UpfrontDiscountArray[lm1]) / CasesSoldArray1[lm1];
									}
									%>
									<td style="text-align: right;"><%if(TotalDiscount!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(TotalDiscount)%><%} %></td>
									<%	
									
								lm1++;
								}
							}
							
							
							double TotalDiscountConverted = 0;
							if (CasesSoldConverted1 != 0){
								TotalDiscountConverted = (ConvertedSalesPromotion + ConvertedUpfront) / CasesSoldConverted1;
							}
							%>
					    	<td style="text-align: right;"><%if(TotalDiscountConverted!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(TotalDiscountConverted) %><%} %></td>
				    	</tr>
				    	<%
				    	}
				    	%>
					  </thead> 
							
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