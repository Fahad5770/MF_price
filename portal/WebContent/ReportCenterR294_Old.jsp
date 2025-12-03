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
	int FeatureID = 366;

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
	<li data-role="list-divider" data-theme="a">PSRs Wise Sale Report</li>
<li>
	<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
			<thead>
		<tr>
			<th data-priority="1"
						style="text-align: center; width: 3%;">Serial #</th>
			<th data-priority="1"
						style="text-align: center; width: 15%;">PSR Name</th>
			<th data-priority="1"
						style="text-align: center; width: 15%;">MD Name</th>
			<th data-priority="1"
						style="text-align: center; width: 15%;">QTY</th>
			<th data-priority="1"
						style="text-align: center; width: 15%;">Total</th>

		</tr>
		</thead>
		<tbody>

				<%
					int Count=0;
							ResultSet rs1 = s.executeQuery("SELECT * FROM common_distributors;");
							while (rs1.next()) {

						if (rs1.getLong("distributor_id") != 100914) {
							Count = 0;
							ResultSet rs6 = s6.executeQuery(
									"select distinct(dbp.asm_id) asmid, (SELECT DISPLAY_NAME FROM users where id=dbp.asm_id) as asmname from distributor_beat_plan dbp where dbp.distributor_id="
											+ rs1.getString("distributor_id"));
							if (rs6.next()) {
								if (rs6.getString("asmid") != null) {
									ResultSet rs7 = s7.executeQuery(
											"SELECT dbp.id,dbp.label,dbpu.assigned_to,(SELECT DISPLAY_NAME FROM users where id=dbpu.assigned_to) orderbooker_name FROM distributor_beat_plan dbp join distributor_beat_plan_users dbpu on dbpu.id=dbp.id  where dbp.asm_id="
													+ rs6.getInt("asmid"));
									if (rs7.next()) {
				%>
				<tr>

					<td data-priority="1"
						style="background-color: #F6F6F6; font-weight: bold" colspan="5"><%=rs1.getString("distributor_id") + "-" + rs1.getString("name")%></td>
				</tr>


				<%
					}
								}
							}
							ResultSet rs2 = s2.executeQuery(
									"select distinct(dbp.asm_id) asmid, (SELECT DISPLAY_NAME FROM users where id=dbp.asm_id) as asmname from distributor_beat_plan dbp where dbp.distributor_id="
											+ rs1.getString("distributor_id"));
							while (rs2.next()) {
								if (rs2.getString("asmid") != null) {
									ResultSet rs3 = s3.executeQuery(
											"SELECT dbp.id,dbp.label,dbpu.assigned_to,(SELECT DISPLAY_NAME FROM users where id=dbpu.assigned_to) orderbooker_name FROM distributor_beat_plan dbp join distributor_beat_plan_users dbpu on dbpu.id=dbp.id  where dbp.asm_id="
													+ rs2.getInt("asmid"));
									while (rs3.next()) {
										Count++;
				%>
				<tr>
					<td style="text-align: center"><%=Count%></td>
					<td><%=rs3.getString("assigned_to") + "-" + rs3.getString("orderbooker_name")%></td>
					<td><%=rs2.getString("asmid") + "-" + rs2.getString("asmname")%></td>
					<td style="text-align:right;">
						<%
							
												ResultSet rs5 = s5.executeQuery(
														"SELECT  Sum(isap.total_units/isap.cache_units_per_sku) Qty  from  inventory_sales_adjusted_products as isap where isap.cache_order_created_on_date between "
																+ Utilities.getSQLDate(StartDate) + " and "
																+ Utilities.getSQLDate(EndDate)  + " and isap.cache_booked_by="+ rs3.getInt("assigned_to"));
												
												while (rs5.next()) {
													double Qty = rs5.getDouble("Qty");
													
						%> 
												<%if(Qty!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(Qty)%><%} %>
						<%
							}
						%>

					</td>
					<td style="text-align:right;">
						<%
							ResultSet rs4 = s4.executeQuery(
														"SELECT  Sum(isap.net_amount) as Total  from  inventory_sales_adjusted_products as isap where isap.cache_order_created_on_date between "
																+ Utilities.getSQLDate(StartDate) + " and "
																+ Utilities.getSQLDate(EndDate)+ " and isap.cache_booked_by="+ rs3.getInt("assigned_to"));
												while (rs4.next()) {
													double Amount = rs4.getDouble("Total");
						%> <%if(Amount!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(Amount)%><%} %>
						<%
							}
						%>
					</td>
				</tr>


				<%
					}
								}
							}
						}
					}
				%>
			</tbody>

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