<%@page import="org.apache.poi.util.SystemOutLogger"%>
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

<%@ page import="java.util.*"%>

<script>
	function redirect(url) {
		document.getElementById("check").action = url;
		document.getElementById("check").submit();
	}
</script>

<style>
td {
	font-size: 8pt;
}
</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

long SessionUserID = Long.parseLong((String) session.getAttribute("UserID"));
int FeatureID = 465;

if (UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false) {
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
Statement s6 = c.createStatement();
Statement s7 = c.createStatement();
Statement s8 = c.createStatement();
Statement s9 = c.createStatement();
Statement s10 = c.createStatement();
Statement s11 = c.createStatement();
Statement s81 = c.createStatement();
//Date date = Utilities.parseDate(request.getParameter("Date"));

ArrayList<Double> Attainmentlist = new ArrayList<Double>();
ArrayList<Double> Targetlist = new ArrayList<Double>();

LinkedHashMap<Integer, Long> TotalAttainmentlist = new LinkedHashMap<Integer, Long>();
LinkedHashMap<Integer, Long> TotalTargetlist = new LinkedHashMap<Integer, Long>();
LinkedHashMap<Integer, Long> TotalBOMlist = new LinkedHashMap<Integer, Long>();

int CounterTarget = 0;
int CounterAttainment = 0;
int CounterBOM = 0;

Date StartDate = (Date) session.getAttribute(UniqueSessionID + "_SR1StartDate");
Date EndDate = (Date) session.getAttribute(UniqueSessionID + "_SR1EndDate");

if (session.getAttribute(UniqueSessionID + "_SR1StartDate") == null) {
	StartDate = new Date();
}

if (session.getAttribute(UniqueSessionID + "_SR1EndDate") == null) {
	EndDate = new Date();
}

Date StartDateAttaintment = null;
Date EndDateAttaintment = null;

int Month = Utilities.getMonthNumberByDate(StartDate);
int Year = Utilities.getYearByDate(EndDate);
int NumberOfDays = Utilities.getNumberofDaysOfMonth(Month, Year);

int CurrentMonth = Utilities.getMonthNumberByDate(new Date());
int CurrentDay = Utilities.getDayNumberByDate(new Date());

Long RemainingDays = (long) (NumberOfDays - CurrentDay);

if (CurrentMonth == Month) {

	System.out.println("Its Cuurent Month ");
	StartDateAttaintment = Utilities.parseDate("01/" + Month + "/" + Year);
	EndDateAttaintment = Utilities.parseDate(CurrentDay + "/" + Month + "/" + Year);

} else {

	System.out.println("Its Not Cuurent Month ");
	StartDateAttaintment = Utilities.parseDate("01/" + Month + "/" + Year);
	EndDateAttaintment = Utilities.parseDate(NumberOfDays + "/" + Month + "/" + Year);
}

System.out.println(
		"StartDateAttaintment " + StartDateAttaintment + " --- " + "StartDateAttaintment " + EndDateAttaintment);

long SelectedPackagesArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedPackages") != null) {
	SelectedPackagesArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedPackages");
}

long SelectedBrandsArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedBrands") != null) {
	SelectedBrandsArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedBrands");
}

String BrandIDs = "";
String WhereBrand = "";

if (SelectedBrandsArray != null && SelectedBrandsArray.length > 0) {
	for (int i = 0; i < SelectedBrandsArray.length; i++) {
		if (i == 0) {
	BrandIDs += SelectedBrandsArray[i] + "";
		} else {
	BrandIDs += ", " + SelectedBrandsArray[i] + "";
		}
	}
	WhereBrand = " and ipv.brand_id in (" + BrandIDs + ") ";
}

//System.out.println(WhereBrand);

String PackageIDs = "";
String WherePackage = "";

if (SelectedPackagesArray != null && SelectedPackagesArray.length > 0) {
	for (int i = 0; i < SelectedPackagesArray.length; i++) {
		if (i == 0) {
	PackageIDs += SelectedPackagesArray[i] + "";
		} else {
	PackageIDs += ", " + SelectedPackagesArray[i] + "";
		}
	}
	WherePackage = " and package_id in (" + PackageIDs + ") ";
}

//HOD

String HODIDs = "";
long SelectedHODArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedHOD") != null) {
	SelectedHODArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedHOD");
	HODIDs = Utilities.serializeForSQL(SelectedHODArray);
}

String WhereHOD = "";
if (HODIDs.length() > 0) {
	WhereHOD = " and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (" + HODIDs
	+ ")) ";
}

//RSM

String RSMIDs = "";
long SelectedRSMArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedRSM") != null) {
	SelectedRSMArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedRSM");
	RSMIDs = Utilities.serializeForSQL(SelectedRSMArray);
}

String WhereRSM = "";
if (RSMIDs.length() > 0) {
	WhereRSM = " and distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in (" + RSMIDs
	+ ")) ";
}

Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);

//SM

String SMIDs = "";
long SelectedSMArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedSM") != null) {
	SelectedSMArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedSM");
	SMIDs = Utilities.serializeForSQL(SelectedSMArray);
}

String WhereSM = "";
if (SMIDs.length() > 0) {
	WhereSM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in (" + SMIDs
	+ ")) ";
}

//TDM

String TDMIDs = "";
long SelectedTDMArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedTDM") != null) {
	SelectedTDMArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedTDM");
	TDMIDs = Utilities.serializeForSQL(SelectedTDMArray);
}

String WhereTDM = "";
if (TDMIDs.length() > 0) {
	WhereTDM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in (" + TDMIDs
	+ ")) ";
}

//ASM

String ASMIDs = "";
long SelectedASMArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedASM") != null) {
	SelectedASMArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedASM");
	ASMIDs = Utilities.serializeForSQL(SelectedASMArray);
}

String WhereASM = "";
if (ASMIDs.length() > 0) {
	WhereASM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in (" + ASMIDs
	+ ")) ";
}

//Lifting Type 

String SelectedLiftingTypeArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedLiftingType") != null) {
	SelectedLiftingTypeArray = (String[]) session.getAttribute(UniqueSessionID + "_SR1SelectedLiftingType");
}

String WhereLiftingType = "";
if (SelectedLiftingTypeArray != null) {
	for (int i = 0; i < SelectedLiftingTypeArray.length; i++) {
		if (SelectedLiftingTypeArray[i].equals("Internal")) {
	WhereLiftingType = " and idn.outsourced_primary_sales_id is null ";
		} else if (SelectedLiftingTypeArray[i].equals("Other Plant")) {
	WhereLiftingType = " and idn.outsourced_primary_sales_id is not null ";
		}
	}
}

//System.out.println("Hello "+WhereLiftingType);
//OrderBooker

boolean IsOrderBookerSelected = false;

int OrderBookerArrayLength = 0;
long SelectedOrderBookerArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedOrderBookers") != null) {
	SelectedOrderBookerArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedOrderBookers");

	IsOrderBookerSelected = true;
	OrderBookerArrayLength = SelectedOrderBookerArray.length;
}

String OrderBookerIDs = "";
if (SelectedOrderBookerArray != null && SelectedOrderBookerArray.length > 0) {
	for (int i = 0; i < SelectedOrderBookerArray.length; i++) {
		if (i == 0) {
	OrderBookerIDs += SelectedOrderBookerArray[i];
		} else {
	OrderBookerIDs += ", " + SelectedOrderBookerArray[i];
		}
	}
}
String OrderBookerIDsWhere = "";
if (OrderBookerIDs.length() > 0) {
	//OrderBookerIDsWhere =" and order_no in (select mobile_order_no from mobile_order_unedited mou where mou.created_by in ("+OrderBookerIDs+"))";
	OrderBookerIDsWhere = " and u.id in (" + OrderBookerIDs + ") ";
}

//customer filter

long DistributorID1 = 0;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedDistributorID") != null) {
	DistributorID1 = (Long) session.getAttribute(UniqueSessionID + "_SR1SelectedDistributorID");
}
String WhereCustomerID = "";

if (DistributorID1 != 0) {
	WhereCustomerID = " and dbp.distributor_id in (" + DistributorID1 + ") ";

}

String ProductsLifted = "-1";
System.out.println(
		"SELECT group_concat(distinct package_id) FROM employee_targets_t2 et join employee_targets_packages_t2 etp on et.id = etp.id where month=date_format("
		+ Utilities.getSQLDate(StartDate) + ",'%m') and year=date_format(" + Utilities.getSQLDate(StartDate)
		+ ",'%Y')");
ResultSet rs41 = s6.executeQuery(
		"SELECT group_concat(distinct package_id) FROM employee_targets_t2 et join employee_targets_packages_t2 etp on et.id = etp.id where month=date_format("
		+ Utilities.getSQLDate(StartDate) + ",'%m') and year=date_format(" + Utilities.getSQLDate(StartDate)
		+ ",'%Y')");

if (rs41.first()) {
	ProductsLifted = rs41.getString(1);
}

class SKus {
	public int packageId = 0;
	public int brandId = 0;
	public int lrbId = 0;
}

ArrayList<SKus> SKUList = new ArrayList<SKus>();

/* ArrayList<String> labels = new ArrayList<String>();
ArrayList<Integer> lrbIds = new ArrayList<Integer>();
ResultSet rs2 = s.executeQuery("SELECT * from inventory_products_lrb_types");
while (rs2.next()) {
	int countZero=0;
	ResultSet rsZero = s2.executeQuery("select count(package_id) as c from inventory_products_view where lrb_type_id="+rs2.getInt("id"));
	if(rsZero.first()) {
		countZero=rsZero.getInt("c");
	}
	if(countZero!=0) {
		lrbIds.add(rs2.getInt("id"));
		labels.add(rs2.getString("label"));
	}else {
		decrement++;
	} */

ArrayList<String> labels = new ArrayList<String>();
ArrayList<Integer> lrbIds = new ArrayList<Integer>();
ResultSet rs2 = s.executeQuery("SELECT * from inventory_products_lrb_types");
while (rs2.next()) {
	lrbIds.add(rs2.getInt("id"));
	labels.add(rs2.getString("label"));
}

ResultSet rs = s.executeQuery("select * from inventory_products_view where lrb_type_id=1");
while (rs.next()) {
	int package_id = rs.getInt("package_id");
	int brand_id = rs.getInt("brand_id");
	int lrb_id = rs.getInt("lrb_type_id");
	//System.out.println(package_id+"-"+brandid+"-"+lrb_id);
	SKus sku = new SKus();

	// Set the properties of the SKU object
	sku.packageId = package_id;
	sku.brandId = brand_id;
	sku.lrbId = lrb_id;

	// Add the SKU object to the SKUSObj ArrayList
	SKUList.add(sku);
	//SKUSObj.add(package_id,brandid,lrb_id);
}

for (int i = 0; i < SKUList.size(); i++) {
	System.out.println(SKUList.get(i).packageId + "-" + SKUList.get(i).brandId + "-" + SKUList.get(i).lrbId);
}
%>

<ul data-role="listview" data-inset="true"
	style="font-size: 10pt; font-weight: normal; margin-top: -10px;"
	data-icon="false">
	<li data-role="list-divider" data-theme="a">PSR Targets Report</li>
	<li>


		<table style="width: 100%; margin-top: -8px" cellpadding="0"
			cellspacing="0">
			<tr>

				<td style="width: 100%" valign="top">
					<table border=0
						style="font-size: 13px; font-weight: 400; width: 100%"
						cellpadding="0" cellspacing="0" adata-role="table"
						class="GridWithBorder">
						<thead>
							<tr style="font-size: 11px;">
								<th data-priority="1" style="text-align: center;">&nbsp;</th>
								<th style="width: 100px"></th>
								<th></th>

								<%
								/* 	System.out.println(
									"SELECT count( product_id) sku_count,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  package_id in ("
											+ ProductsLifted
											+ ") and is_visible=1 and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
								*/
								ResultSet rs21 = s.executeQuery(
										"SELECT count( product_id) sku_count,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  package_id in ("
										+ ProductsLifted
										+ ") and is_visible=1 and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
								while (rs21.next()) {
								%>
								<th data-priority="1" style="text-align: center;"
									colspan="<%=rs21.getInt("sku_count")%>"><%=rs21.getString("type_name")%></th>
								<%
								}
								%>

							</tr>

							<tr style="font-size: 11px;">
								<th data-priority="1" style="text-align: center;">&nbsp;</th>
								<th></th>
								<th>Converted</th>


								<%
								int PackageCount = 0;

								int ArrayCount = 0;

								for (int i = 0; i < lrbIds.size(); i++) {

									/* 		System.out.println(
											"SELECT concat(package_label, '-', brand_label) as product_label FROM inventory_products_view where  package_id in ("
													+ ProductsLifted + ") "  + " and is_visible=1 and category_id = 1 and lrb_type_id="
													+lrbIds.get(i) + " order by package_sort_order"); */
									ResultSet rs3 = s2.executeQuery(
									"SELECT concat(package_label, '-', brand_label) as product_label FROM inventory_products_view where  package_id in ("
											+ ProductsLifted + ") " + " and is_visible=1 and category_id = 1 and lrb_type_id=" + lrbIds.get(i)
											+ " order by package_sort_order");
									while (rs3.next()) {
								%>
								<th data-priority="1" style="text-align: center;"><%=rs3.getString("product_label")%></th>
								<%
								ArrayCount++;
								PackageCount++;
								}
								}
								%>
							</tr>
						</thead>
						<tbody>
							<%
							long EmployeeID = 0;
							long PacakgeID = 0;
							long TotalConverted = 0;

							long PJPID = 0;

							/* 	System.out.println("select u.id,u.display_name, (select dbpu.id from distributor_beat_plan_users dbpu where dbpu.assigned_to=u.id limit 1) pjp_id from users u where 1=1 and u.ID in (SELECT distinct dbpu.assigned_to FROM pep.distributor_beat_plan dbp join distributor_beat_plan_users dbpu on dbp.id=dbpu.id where 1=1 "
								+ WhereCustomerID + ")" + OrderBookerIDsWhere + ""); */

							ResultSet rs1 = s81.executeQuery(
									"select u.id,u.display_name, (select dbpu.id from distributor_beat_plan_users dbpu where dbpu.assigned_to=u.id limit 1) pjp_id from users u where 1=1 and u.ID in (SELECT distinct dbpu.assigned_to FROM pep.distributor_beat_plan dbp join distributor_beat_plan_users dbpu on dbp.id=dbpu.id where 1=1 "
									+ WhereCustomerID + ")" + OrderBookerIDsWhere + ""); //distributor query
							while (rs1.next()) {
								CounterTarget = 0;
								CounterAttainment = 0;
								CounterBOM = 0;
								Targetlist.clear();
								Attainmentlist.clear();
								EmployeeID = rs1.getInt("id");

								PJPID = rs1.getLong("pjp_id");

								String InvoiceIDsQuery = "1=1";
								int PackageIndex = 0;
							%>
							<tr>
								<td rowspan="3"><%=Utilities.truncateStringToMax((EmployeeID + " - " + rs1.getString("DISPLAY_NAME")), 30)%></td>
								<td>Target</td>
								<%
								//Converted
								long ConvertedCases = 0;
								/* ResultSet rs311 = s4.executeQuery(
										"select sum(quantity*unit_per_case*ip.liquid_in_ml)/6000 quantity from employee_targets_packages etp join employee_targets et on etp.id= et.id join inventory_packages ip on etp.package_id = ip.id where month=date_format("
										+ Utilities.getSQLDate(StartDate) + ",'%m') and year=date_format("
										+ Utilities.getSQLDate(StartDate) + ",'%Y') and et.pjp_id = " + PJPID); */
								System.out.println(
										"select sum(quantity*ip.liquid_in_ml) quantity quantity from employee_targets_packages_t2 etp join employee_targets_t2 et on etp.id= et.id join inventory_packages ip on etp.package_id = ip.id where month=date_format("
										+ Utilities.getSQLDate(StartDate) + ",'%m') and year=date_format(" + Utilities.getSQLDate(StartDate)
										+ ",'%Y') and et.pjp_id = " + PJPID);
								ResultSet rs311 = s4.executeQuery(
										"select sum(quantity*ip.liquid_in_ml) quantity from employee_targets_packages_t2 etp join employee_targets_t2 et on etp.id= et.id join inventory_packages ip on etp.package_id = ip.id where month=date_format("
										+ Utilities.getSQLDate(StartDate) + ",'%m') and year=date_format(" + Utilities.getSQLDate(StartDate)
										+ ",'%Y') and et.pjp_id = " + PJPID);
								while (rs311.next()) {
									ConvertedCases = rs311.getLong("quantity");
								}

								TotalConverted += ConvertedCases;

								//Converted Total Attaintment
								Long TotalAttainedConvertedCases = 0L;

								//System.out.println("select sum((isap.total_units * ip.liquid_in_ml) / ip.conversion_rate_in_ml) AS SD_CONVERTED_CASES from inventory_sales_adjusted_products isap  join inventory_sales_adjusted isa on   isa.id=isap.id join inventory_packages ip on isap.cache_package_id = ip.id  where  isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isap.cache_booked_by="+UserID);
								System.out.println(
										"select sum(isap.total_units * ip.liquid_in_ml) AS SD_CONVERTED_CASES from inventory_sales_adjusted_products isap join inventory_sales_adjusted isd on isd.id=isap.id   join inventory_packages ip on isap.cache_package_id = ip.id where    isap.cache_created_on_date between "
										+ Utilities.getSQLDate(StartDateAttaintment) + " and " + Utilities.getSQLDate(EndDateAttaintment)
										+ " and isd.beat_plan_id=" + PJPID);
								ResultSet rs3111 = s4.executeQuery(
										"select sum(isap.total_units * ip.liquid_in_ml) AS SD_CONVERTED_CASES from inventory_sales_adjusted_products isap join inventory_sales_adjusted isd on isd.id=isap.id   join inventory_packages ip on isap.cache_package_id = ip.id where    isap.cache_created_on_date between "
										+ Utilities.getSQLDate(StartDateAttaintment) + " and " + Utilities.getSQLDate(EndDateAttaintment)
										+ " and isd.beat_plan_id=" + PJPID);
								while (rs3111.next()) {

									TotalAttainedConvertedCases = rs3111.getLong("SD_CONVERTED_CASES");

								}
								++CounterTarget;
								Long ISRECORD = TotalTargetlist.get(CounterTarget);
								System.out.print("====" + ISRECORD);
								if (ISRECORD != null) {
									TotalTargetlist.put(CounterTarget, ISRECORD + ConvertedCases);
								} else {
									TotalTargetlist.put(CounterTarget, ConvertedCases);
								}

								Targetlist.add((double) ConvertedCases);
								%>

								<td style="text-align: right;">
									<%
									if (ConvertedCases != 0) {
									%><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCases)%>
									<%
									}
									%>
								</td>


								<%
								/* ResultSet rs31 = s3.executeQuery(
									"SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  package_id in ("
											+ ProductsLifted
											+ ") and category_id = 1 and lrb_type_id is not null group by lrb_type_id"); */
											for (int i = 0; i < lrbIds.size(); i++) {
								//	long lrb_type_id = rs31.getLong("lrb_type_id");
								System.out.println(
										"SELECT package_id, brand_id,liquid_in_ml FROM inventory_products_view where  package_id in ("
												+ ProductsLifted + ") and category_id = 1 and lrb_type_id=" + lrbIds.get(i)
												+ " order by package_sort_order");
									ResultSet rs4 = s2.executeQuery(
									"SELECT package_id, brand_id,liquid_in_ml FROM inventory_products_view where  package_id in ("
											+ ProductsLifted + ") and category_id = 1 and lrb_type_id=" + lrbIds.get(i)
											+ " order by package_sort_order");
									while (rs4.next()) {

										long PackageID = rs4.getLong("package_id");
										long Brand_id = rs4.getLong("brand_id");
										double liquid_in_ml = rs4.getDouble("liquid_in_ml");

										int unit_per_sku = 0;
										long qty = 0;

										//	System.out.println("select quantity from employee_targets_packages etp join employee_targets et on etp.id= et.id join inventory_products_view ipv on ipv.package_id=etp.package_id where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y') and et.employee_id = "+DistributorID+ " and ipv.package_id ="+PackageID+" and ipv.lrb_type_id="+lrb_type_id);
										System.out.println(
										"SELECT et.id,et.employee_id,etp.package_id,etp.quantity q1, sum(etp.quantity) q2 FROM employee_targets_t2 et  join employee_targets_packages_brands_t2 etp on et.id=etp.id   where et.month=date_format("
												+ Utilities.getSQLDate(StartDate) + ",'%m') and et.year=date_format("
												+ Utilities.getSQLDate(StartDate) + ",'%Y') and et.pjp_id=" + PJPID + " and etp.package_id=" + PackageID+" and etp.brand_id="+Brand_id);
										//old query
										/* 	ResultSet rs3 = s5.executeQuery(
											"SELECT et.id,et.employee_id,etp.package_id,etp.quantity q1,etpb.brand_id,etpb.package_id, sum(etpb.quantity) q2 FROM employee_targets et  join employee_targets_packages etp on et.id=etp.id  join pep.employee_targets_packages_brands etpb on et.id=etpb.id and etp.package_id=etpb.package_id where et.month=date_format("
													+ Utilities.getSQLDate(StartDate) + ",'%m') and et.year=date_format("
													+ Utilities.getSQLDate(StartDate) + ",'%Y') and et.pjp_id=" + PJPID
													+ " and etp.package_id=" + PackageID
													+ " and  brand_id in (SELECT brand_id FROM pep.inventory_products_view where lrb_type_id="
													+ lrb_type_id + " and package_id=" + PackageID + " and category_id=1)"); */
										ResultSet rs3 = s5.executeQuery(
												"SELECT et.id,et.employee_id,etp.package_id,etp.quantity q1, sum(etp.quantity) q2 FROM employee_targets_t2 et  join employee_targets_packages_brands_t2 etp on et.id=etp.id   where et.month=date_format("
														+ Utilities.getSQLDate(StartDate) + ",'%m') and et.year=date_format("
														+ Utilities.getSQLDate(StartDate) + ",'%Y') and et.pjp_id=" + PJPID + " and etp.package_id=" + PackageID+" and etp.brand_id="+Brand_id);

										while (rs3.next()) {
									qty = rs3.getLong("q2");

									//unit_per_sku = rs3.getInt("unit_per_sku");
									//		PackageTotal[PackageIndex] += qty;
									// 									if(unit_per_sku > 0){
									// 										PackageTotalUnitPerSKU[PackageIndex] = unit_per_sku;
									// 									}
								%>
								<%-- 								<td style="text-align: right;"><%= Utilities.convertToRawCases(qty,unit_per_sku) %></td>  --%>


								<%
								PackageIndex++;
								}
								++CounterTarget;
								ISRECORD = null;
								ISRECORD = TotalTargetlist.get(CounterTarget);
								if (ISRECORD != null) {
								TotalTargetlist.put(CounterTarget, ISRECORD + qty);
								} else {
								TotalTargetlist.put(CounterTarget, qty);
								}
								Targetlist.add((double) qty);
								%>
								 <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(qty/liquid_in_ml)%></td> 
								
								<%
								}
								}
								%>

							</tr>
							<!-- Ayaz Code -->
							<tr>
								<td>Attainment</td>
								<td style="text-align: right;">
									<%
									if (TotalAttainedConvertedCases != 0) {
									%><%=Utilities.getDisplayCurrencyFormatRounded(TotalAttainedConvertedCases)%>
									<%
									}

									++CounterAttainment;
									ISRECORD = null;
									ISRECORD = TotalAttainmentlist.get(CounterAttainment);
									if (ISRECORD != null) {
									TotalAttainmentlist.put(CounterAttainment, ISRECORD + TotalAttainedConvertedCases);
									} else {
									TotalAttainmentlist.put(CounterAttainment, TotalAttainedConvertedCases);
									}
									Attainmentlist.add((double) TotalAttainedConvertedCases);
									%>
								</td>
								<%
								long totalskuView = 0;
								long totalunitsISAP = 0;
								long attainment = 0;
							/* 	ResultSet rs33 = s8.executeQuery(
										"SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  package_id in ("
										+ ProductsLifted + ") and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
								while (rs33.next()) { */
									for (int i = 0; i < lrbIds.size(); i++) {
									//System.out.println("1");
									//long lrb_type_id2 = rs33.getLong("lrb_type_id");
									/* ResultSet rs34 = s9.executeQuery(
									"SELECT distinct package_id, package_label FROM inventory_products_view where  package_id in ("
											+ ProductsLifted + ") and category_id = 1 and lrb_type_id=" + lrbIds.get(i)
											+ " order by package_sort_order"); */
								ResultSet rs34 = s9.executeQuery(
									"SELECT package_id, brand_id FROM inventory_products_view where  package_id in ("
									+ ProductsLifted + ") and category_id = 1 and lrb_type_id=" + lrbIds.get(i)
										+ " order by package_sort_order");
									while (rs34.next()) {
										//System.out.println("2");
										long PackageID = rs34.getLong("package_id");
										long Brand_id = rs34.getLong("brand_id");
										//System.out.println("3");								
										ResultSet rs35 = s10
										.executeQuery("SELECT unit_per_sku FROM pep.inventory_products_view where package_id=" + PackageID);

										if (rs35.first()) {
									//System.out.println("4");
									totalskuView = rs35.getLong(1);

										}

										//	System.out.println("SELECT sum(isap.total_units) FROM pep.inventory_sales_adjusted_products isap join inventory_sales_adjusted isa on isa.id=isap.id where isap.cache_booked_by="+EmployeeID+" and isap.cache_package_id="+PackageID+" and isap.cache_created_on_date between 2019/06/01 and 2019/06/27");
										/* ResultSet rs36 = s11.executeQuery(
											"SELECT sum(isap.total_units) FROM pep.inventory_sales_adjusted_products isap   where isap.cache_pjp_id="
													+ PJPID + " and isap.cache_lrb_type_id=" + lrb_type_id2
													+ "  and isap.cache_package_id=" + PackageID
													+ " and isap.cache_created_on_date between "
													+ Utilities.getSQLDate(StartDateAttaintment) + " and "
													+ Utilities.getSQLDate(EndDateAttaintment) + ""); */
										ResultSet rs36 = s11.executeQuery(
										"SELECT sum(isap.total_units) FROM pep.inventory_sales_adjusted_products isap  join inventory_sales_adjusted isd on isd.id=isap.id   where isap.cache_lrb_type_id="
												+ lrbIds.get(i) + "  and isap.cache_package_id=" + PackageID
												+ " and cache_brand_id="+Brand_id+" and  isap.cache_created_on_date between " + Utilities.getSQLDate(StartDateAttaintment)
												+ " and " + Utilities.getSQLDate(EndDateAttaintment) + " isd.beat_plan_id="+PJPID);
										while (rs36.next()) {
									//System.out.println("5");
									totalunitsISAP = rs36.getLong(1);

										}

										/* System.out.println("totalskuView "+totalskuView);
										System.out.println("totalunitsISAP "+totalunitsISAP); */

										if (totalskuView != 0 && totalunitsISAP != 0) {
									attainment = totalunitsISAP / totalskuView;
									++CounterAttainment;
									ISRECORD = null;
									ISRECORD = TotalAttainmentlist.get(CounterAttainment);
									if (ISRECORD != null) {

										TotalAttainmentlist.put(CounterAttainment, ISRECORD + attainment);
									} else {
										TotalAttainmentlist.put(CounterAttainment, attainment);
									}
									Attainmentlist.add((double) attainment);
								%>
								<td style="text-align: right;"><%=attainment%></td>
								<%
								} else {
								++CounterAttainment;
								ISRECORD = null;
								ISRECORD = TotalAttainmentlist.get(CounterAttainment);
								if (ISRECORD != null) {

									TotalAttainmentlist.put(CounterAttainment, ISRECORD + 0);
								} else {
									TotalAttainmentlist.put(CounterAttainment, 0L);
								}

								Long zero = (long) 0;
								Attainmentlist.add((double) zero);
								%>
								<td></td>
								<%
								}
								%>

								<%
								}

								}
								%>


							</tr>
							<tr>
								<td>BOM Required / Day</td>
								<%
								for (int i = 0; i < Targetlist.size(); i++) {
									/* System.out.println("Target => "+Targetlist.get(i));    
									System.out.println("Attainment => "+ Attainmentlist.get(i));
									System.out.println("Remaining per day => "+ ((Targetlist.get(i)- Attainmentlist.get(i)))/RemainingDays);
									 */
									Long Remaining = (long) (Targetlist.get(i) - Attainmentlist.get(i)) / RemainingDays;

									if (Targetlist.get(i) > 0) {
										++CounterBOM;
										ISRECORD = null;
										ISRECORD = TotalBOMlist.get(CounterBOM);
										if (ISRECORD != null) {

									TotalBOMlist.put(CounterBOM, ISRECORD + Remaining);
										} else {
									TotalBOMlist.put(CounterBOM, Remaining);
										}
								%>
								<td style="text-align: right;"><%=Remaining%></td>
								<%
								} else {
								++CounterBOM;
								ISRECORD = null;
								ISRECORD = TotalBOMlist.get(CounterBOM);
								if (ISRECORD != null) {

									TotalBOMlist.put(CounterBOM, ISRECORD + 0);
								} else {
									TotalBOMlist.put(CounterBOM, 0L);
								}
								%>

								<td style="text-align: right;"><%=""%></td>
								<%
								}

								}
								%>
							</tr>

							<%
							}
							%>
							<tr>
								<td colspan="<%=PackageCount%>"></td>
							</tr>
							<tr style="background-color: #F6F6F6;">
								<td style="font-weight: bold; font-size: 12px;" rowspan="3">Total</td>
								<td
									style="text-align: left; font-weight: bold; font-size: 12px;">Total
									Target</td>
								<%
								for (Map.Entry<Integer, Long> entry : TotalTargetlist.entrySet()) {

									Long value = (long) entry.getValue();
								%>
								<td
									style="text-align: right; font-weight: bold; font-size: 12px;"><%=Utilities.getDisplayCurrencyFormatRounded(value)%></td>

								<%
								}
								%>

							</tr>
							<tr style="background-color: #F6F6F6;">
								<td
									style="text-align: left; font-weight: bold; font-size: 12px;">Total
									Attainment</td>
								<%
								for (Map.Entry<Integer, Long> entry : TotalAttainmentlist.entrySet()) {
									Object key = entry.getKey();
									Long value = (long) entry.getValue();
									System.out.println("-----");
									System.out.println(key);
									System.out.println(value);
								%>

								<td
									style="text-align: right; font-weight: bold; font-size: 12px;"><%=Utilities.getDisplayCurrencyFormatRounded(value)%></td>
								<%
								}
								%>

							</tr>
							<tr style="background-color: #F6F6F6;">

								<td
									style="text-align: left; font-weight: bold; font-size: 12px;">Total
									BOM Required / Day</td>

								<%
								for (Map.Entry<Integer, Long> entry : TotalBOMlist.entrySet()) {
									Object key = entry.getKey();
									Long value = (long) entry.getValue();
								%>

								<td
									style="text-align: right; font-weight: bold; font-size: 12px;"><%=Utilities.getDisplayCurrencyFormatRounded(value)%></td>
								<%
								}
								%>
							
						</tbody>

					</table>
				</td>
			</tr>
		</table>

	</li>
</ul>

<%
s8.close();
s7.close();
s6.close();
s5.close();
s4.close();
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>