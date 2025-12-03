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
	int FeatureID = 365;

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
		WhereBrand = " and brand_id in (" + BrandIDs + ") ";
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

	//System.out.print(WherePackage);
	//HOD

	String HODIDs = "";
	long SelectedHODArray[] = null;
	if (session.getAttribute(UniqueSessionID + "_SR1SelectedHOD") != null) {
		SelectedHODArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedHOD");
		HODIDs = Utilities.serializeForSQL(SelectedHODArray);
	}

	String WhereHOD = "";
	if (HODIDs.length() > 0) {
		WhereHOD = " and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("
				+ HODIDs + ")) ";
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
		WhereRSM = " and distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("
				+ RSMIDs + ")) ";
	}

	//SM

	String SMIDs = "";
	long SelectedSMArray[] = null;
	if (session.getAttribute(UniqueSessionID + "_SR1SelectedSM") != null) {
		SelectedSMArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedSM");
		SMIDs = Utilities.serializeForSQL(SelectedSMArray);
	}

	String WhereSM = "";
	if (SMIDs.length() > 0) {
		WhereSM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("
				+ SMIDs + ")) ";
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
		WhereTDM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("
				+ TDMIDs + ")) ";
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
		WhereASM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("
				+ ASMIDs + ")) ";
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

	String ProductsLifted = "-1";
	//System.out.println("SELECT group_concat(distinct package_id) FROM employee_targets et join employee_targets_packages etp on et.id = etp.id where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y')");
	ResultSet rs41 = s6.executeQuery(
			"SELECT group_concat(distinct package_id) FROM employee_targets et join employee_targets_packages etp on et.id = etp.id where month=date_format("
					+ Utilities.getSQLDate(StartDate) + ",'%m') and year=date_format("
					+ Utilities.getSQLDate(StartDate) + ",'%Y')");

	if (rs41.first()) {
		ProductsLifted = rs41.getString(1);
	}

	long SelectedEmployeeArray[] = null;
	if (session.getAttribute(UniqueSessionID + "_SR1SelectedEmployees") != null) {
		SelectedEmployeeArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedEmployees");
	}
	String EmployeeIDs = "";
	String WhereEmployee = "";
	if (SelectedEmployeeArray != null && SelectedEmployeeArray.length > 0) {
		for (int i = 0; i < SelectedEmployeeArray.length; i++) {
			if (i == 0) {
				EmployeeIDs += SelectedEmployeeArray[i];
			} else {
				EmployeeIDs += ", " + SelectedEmployeeArray[i];
			}
		}
		WhereEmployee = " and et.employee_id in (" + EmployeeIDs + ") ";
	}

	//OrderBooker

	int OrderBookerArrayLength = 0;
	long SelectedOrderBookerArray[] = null;
	String OrderBookerIDsWhere = "";
	if (session.getAttribute(UniqueSessionID + "_SR1SelectedOrderBookers") != null) {
		SelectedOrderBookerArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedOrderBookers");

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

		if (OrderBookerIDs.length() > 0) {
			OrderBookerIDsWhere = " and mo.created_by in (" + OrderBookerIDs + ") ";
		}

	}
%>

<ul data-role="listview" data-inset="true"
	style="font-size: 10pt; font-weight: normal; margin-top: -10px;"
	data-icon="false">
	<li data-role="list-divider" data-theme="a">Summary</li>
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
								<th>&nbsp;</th>

								<%
									ResultSet rs21 = s.executeQuery(
											"SELECT count(*) as ProdinPackage,package_label FROM inventory_products_view where 1=1 "
													+ WherePackage + WhereBrand + " group by package_id  order by package_id");
									while (rs21.next()) {
								%>
								<th data-priority="1" style="text-align: center;"
									colspan="<%=rs21.getInt("ProdinPackage")%>"><%=rs21.getString("package_label")%></th>
								<%
									}
								%>
								<th>&nbsp;</th>
							</tr>


							<tr style="font-size: 11px;">
								<th data-priority="1" style="text-align: center;">OUTLET
									NAME</th>



								<%
									int PackageCount = 0;

									int ArrayCount = 0;

									ResultSet rs12 = s7.executeQuery("SELECT package_id,package_label FROM inventory_products_view where 1=1 "
											+ WherePackage + WhereBrand + " group by package_id order by package_id");
									while (rs12.next()) {
										ResultSet rs2 = s2.executeQuery(
												"SELECT product_id,package_id,brand_id,brand_label,package_label FROM inventory_products_view  where 1=1"
														+ WhereBrand + " and package_id=" + rs12.getInt(1));
										while (rs2.next()) {
								%>
								<th data-priority="1" style="text-align: center;"><%=rs2.getString("brand_label")%></th>
								<%
									ArrayCount++;
											PackageCount++;
										}
									}
								%>
								<th>
								Last Order
								</th>

							</tr>
						</thead>
						<tbody>
							<%
								long EmployeeID = 0;
								long PacakgeID = 0;
								long TotalConverted = 0;

								//System.out.println("select * from common_distributors where "+WhereHOD+WhereRSM+" and distributor_id in ("+DistributorIDs+") and distributor_id in (select distinct distributor_id from inventory_delivery_note)");
								ResultSet rs15 = s11.executeQuery(
										"select distinct(common_outlets.id), common_outlets.name FROM  common_outlets  INNER JOIN  inventory_sales_returns ON common_outlets.id=inventory_sales_returns.outlet_id where inventory_sales_returns.created_on BETWEEN "
												+ Utilities.getSQLDate(StartDate) + " AND " + Utilities.getSQLDateNext(EndDate));
								while (rs15.next()) {
							%>
							<tr>
								<td><%=rs15.getString("id")%> - <%=rs15.getString("name")%></td>
								<%
									ResultSet rs14 = s3
												.executeQuery("SELECT package_id,package_label FROM inventory_products_view where 1=1 "
														+ WherePackage + WhereBrand + " group by package_id order by package_id");
										while (rs14.next()) {

											ResultSet rs2 = s2.executeQuery(
													"SELECT product_id,package_id,brand_id,brand_label,package_label FROM inventory_products_view  where 1=1"
															+ WhereBrand + " and package_id=" + rs14.getInt(1));
											while (rs2.next()) {

												ResultSet rs1 = s81.executeQuery(
														"SELECT proview.product_id,proview.package_label,(select sum(returndetail.total_units) from inventory_sales_returns_products as returndetail join inventory_sales_returns as returned on returndetail.id=returned.id where  product_id=proview.product_id and returned.outlet_id="
																+ rs15.getLong("id") + " and returned.created_on BETWEEN "
																+ Utilities.getSQLDate(StartDate) + " AND " + Utilities.getSQLDateNext(EndDate)
																+ ")  as total_returns FROM inventory_products_view as proview where proview.brand_id="
																+ rs2.getLong("brand_id") + " and " + "proview.package_id=" + rs14.getInt(1));
								%>

								<%
									while (rs1.next()) {

													int PackageIndex = 0;
								%>




								<td style="text-align: center;">
									<%
										if (rs1.getString("total_returns") != null) {
									%> <%=rs1.getString("total_returns")%> <%
 	} else {
 %> <%=""%> <%
 	}
 %>
								</td>
								
							


								<%
									}

											}
										}
								%>

								<%
								//Lat Order
								Date LastOrder = new Date();
								
								ResultSet rs35 = s81.executeQuery("select max(created_on) from mobile_order mo where mo.outlet_id="+rs15.getLong("id")+" and mo.created_on<="+Utilities.getSQLDate(EndDate));
								if(rs35.first()){
									LastOrder = rs35.getDate(1);
								%>
								
								<td>
								<%=Utilities.getDisplayDateFormat(LastOrder) %>
								
								</td>
								<%
								}
								%>

							</tr>
							<%
								}
							%>

							<!-- Total -->
							<tr>
								<td data-priority="1" style="text-align: left;">Total</td>
								<%
									ResultSet rs13 = s7.executeQuery("SELECT package_id,package_label FROM inventory_products_view where 1=1 "
											+ WherePackage + WhereBrand + " group by package_id order by package_id");
									while (rs13.next()) {

										ResultSet rs2 = s2.executeQuery(
												"SELECT product_id,package_id,brand_id,brand_label,package_label FROM inventory_products_view  where 1=1"
														+ WhereBrand + " and package_id=" + rs13.getInt(1));
										while (rs2.next()) {

											ResultSet rs1 = s81.executeQuery(
													"SELECT proview.product_id,proview.package_label,(select sum(returndetail.total_units) from inventory_sales_returns_products as returndetail join inventory_sales_returns as returned on returndetail.id=returned.id where product_id=proview.product_id and returned.created_on BETWEEN "
															+ Utilities.getSQLDate(StartDate) + " AND " + Utilities.getSQLDateNext(EndDate)
															+ ")  as total_returns FROM inventory_products_view as proview where proview.brand_id="
															+ rs2.getLong("brand_id") + " and " + "proview.package_id=" + rs13.getInt(1));
								%>

								<%
									while (rs1.next()) {

												int PackageIndex = 0;
								%>




								<td style="text-align: center;">
									<%
										if (rs1.getString("total_returns") != null) {
									%> <%=rs1.getString("total_returns")%> <%
 	} else {
 %> <%=""%> <%
 	}
 %>
								</td>



								<%
									}

										}
									}
								%>
								<th>&nbsp;</th>
							</tr>
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