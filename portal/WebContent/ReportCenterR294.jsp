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
	//System.out.print("ayaz");
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
	Statement s25 = c.createStatement();
	Statement s26 = c.createStatement();
	Statement s27 = c.createStatement();
	Statement s28 = c.createStatement();
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

	String wheredistribution = "";
	String distribution = "";
	String OrderBookerIDs = "";
	String OrderBookerIDsWhere = "";
	//OrderBooker

	 
	int OrderBookerArrayLength = 0;
	long SelectedOrderBookerArray[] = null;
	
	if (session.getAttribute(UniqueSessionID + "_SR1SelectedOrderBookers") != null) {
		SelectedOrderBookerArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedOrderBookers");

		
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
	
	
	// for KPO

	ResultSet rs31 = s.executeQuery(
			"select * from users where type_id=2 and distributor_id is not null and ID=" + SessionUserID + "");
	int i = 0;
	while (rs31.next()) {
		if (i == 0) {
			distribution += rs31.getString("distributor_id");
		} else {
			distribution += ", " + rs31.getString("distributor_id");
		}

		i++;
	}
	if (i > 0 && distribution != "100914") {
		wheredistribution = " and isap.cache_distributor_id in (" + distribution + ") ";
	}
	
	
	// for mde

			ResultSet rs32 = s.executeQuery(
					"SELECT distinct dbpav.asm_id,display_name FROM distributor_beat_plan_all_view dbpav, users u where dbpav.asm_id=u.id and dbpav.asm_id="
							+ SessionUserID + "");
			i = 0;
			while (rs32.next()) {
				ResultSet rs33 = s2.executeQuery(
						"SELECT dbp.id,dbp.label,dbpu.assigned_to,(SELECT DISPLAY_NAME FROM users where id=dbpu.assigned_to) orderbooker_name FROM pep.distributor_beat_plan dbp join distributor_beat_plan_users dbpu on dbpu.id=dbp.id  where dbp.asm_id="
								+ rs32.getInt("asm_id") + "");
				i = 0;
				while (rs33.next()) {
					if (i == 0) {
						OrderBookerIDs += rs33.getString("assigned_to");
					} else {
						OrderBookerIDs += ", " + rs33.getString("assigned_to");
					}

					i++;
				}

			}
			if (i > 0 && OrderBookerIDs != "2271") {
				OrderBookerIDsWhere = " and isap.cache_booked_by in (" + OrderBookerIDs + ") ";
			}

			ResultSet rs39 = s
					.executeQuery("select distinct(distributor_id) from distributor_beat_plan_view where asm_id="
							+ SessionUserID + " ");
			i = 0;
			while (rs39.next()) {
				if (i > 0 && OrderBookerIDs != "2271" && distribution != "100914") {
					distribution += rs39.getString("distributor_id");
				} else {
					distribution += ", " + rs39.getString("distributor_id");
				}

				i++;
			}
			if (i > 0 && distribution != "100914") {
				wheredistribution = " and isap.cache_distributor_id in (" + distribution + ") ";
			}
	//SND
	ResultSet rs38 = s
			.executeQuery("select distinct(distributor_id) from distributor_beat_plan_view where snd_id="
					+ SessionUserID + " ");
	 i = 0;
	while (rs38.next()) {
		if (i == 0) {
			distribution += rs38.getString("distributor_id");
		} else {
			distribution += ", " + rs38.getString("distributor_id");
		}

		i++;
	}
	if (i > 0 && distribution != "100914") {
		wheredistribution = " and isap.cache_distributor_id in (" + distribution + ") ";
	}

	if (OrderBookerIDs.equals("2271")) {
		OrderBookerIDsWhere = "";
	}
	if (distribution.equals("100914")) {
		wheredistribution = "";
	}

double Total_qty=0;
double Total_sale=0;

double Total_BEETA_qty=0;
double Total_BEETA_sale=0;

double Total_SUGAR_qty=0;
double Total_SUGAR_sale=0;


//System.out.println("Start Date "+Utilities.getDisplayDateFormat(StartDate)+" - "+Utilities.getDisplayDateFormat(EndDate));

%>


<ul data-role="listview" data-inset="true"
	style="font-size: 10pt; font-weight: normal; margin-top: -10px;"
	data-icon="false">
	<li data-role="list-divider" data-theme="a">PSRs Wise Sale Report</li>
	<li>
		<table border=0 style="font-size: 13px; font-weight: 400; width: 100%"
			cellpadding="0" cellspacing="0" adata-role="table"
			class="GridWithBorder">
			<thead>
				<tr>
					<th data-priority="1" style="text-align: center; width: 3%;">Serial
						#</th>
					<th data-priority="1" style="text-align: center; width: 15%;">PSR
						Name</th>
					<th data-priority="1" style="text-align: center; width: 15%;">MD
						Name</th>
						<th data-priority="1" style="text-align: center; width: 15%;">Converted Beeta</th>
						<th data-priority="1" style="text-align: center; width: 15%;">Converted Sugar</th>
					<!-- <th data-priority="1" style="text-align: center; width: 15%;">Quantity</th> -->
					<th data-priority="1" style="text-align: center; width: 15%;">Amount</th>

				</tr>
			</thead>
			<tbody>

				<%
					int Count = 0;
					long Distributor=0;
				
					ResultSet rs1 = s.executeQuery(
							"SELECT distinct cache_distributor_id as distributor_id,(select name from common_distributors where distributor_id=cache_distributor_id) as name from inventory_sales_adjusted_products as isap where isap.cache_created_on_date between "
									+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate) +wheredistribution);
					while (rs1.next()) {
						
						double DistWiseTotalQty=0;
						double DistWiseTotalAmount=0;
						
						double DistWiseBEETATotalQty=0;
						double DistWiseBEETATotalAmount=0;
						
						double DistWiseSUGARTotalQty=0;
						double DistWiseSUGARTotalAmount=0;

						if (rs1.getLong("distributor_id") != 100914) {
							Count = 0;
							
							Distributor=rs1.getLong("distributor_id");
				%>
				<tr>

					<td data-priority="1"
						style="background-color: #F6F6F6; font-weight: bold" colspan="6"><%=rs1.getString("distributor_id") + "-" + rs1.getString("name")%></td>
				</tr>


				<%
				double Qty=0;
				double Amount=0;
				long BookedBy=0;
				
				
				
					ResultSet rs2 = s2.executeQuery(
									"SELECT isap.cache_booked_by,Sum(isap.total_units/isap.cache_units_per_sku) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap where isap.cache_created_on_date between "
											+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)
											+ " and cache_distributor_id=" + Distributor
											+ " group by cache_booked_by;");
							while (rs2.next()) {
								
								BookedBy = rs2.getLong("cache_booked_by");
								
								//if (rs2.getString("cache_booked_by") != null) {
									ResultSet rs3 = s3.executeQuery(
											"SELECT distinct asm_id , (select DISPLAY_NAME from users where id= asm_id) as asmname  FROM pep.distributor_beat_plan_view where assigned_to="
													+ BookedBy);
									String ASM="";
									
									while (rs3.next()) {
										
										ASM = rs3.getString("asm_id") + "-" + rs3.getString("asmname");
									}
									//System.out.print(ASM);
										Count++;
										
										Qty=rs2.getDouble("Qty");
										Amount=rs2.getDouble("total_sales");
										
										///////
										
										//Adding Desk Sales to Orderbooker sales
										
										double DeskQty=0;
										double DeskAmount=0;
										String Q="";
										
									  /*  if(BookedBy!=0){
											Q="SELECT isap.cache_booked_by,Sum(isap.total_units/isap.cache_units_per_sku) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id where isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)+" and isap.cache_distributor_id="+Distributor+" and isa.outlet_id in (SELECT distinct outlet_id FROM pep.distributor_beat_plan_view where assigned_to="+BookedBy+") and isa.type_id=1 group by cache_booked_by";
											
										}else{
											Q="SELECT isap.cache_booked_by,Sum(isap.total_units/isap.cache_units_per_sku) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id where isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)+" and isap.cache_distributor_id="+Distributor+"  and isa.type_id=1 group by cache_booked_by";
										} */ 
										
											Q="SELECT isap.cache_booked_by,Sum(isap.total_units/isap.cache_units_per_sku) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id where isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)+" and isap.cache_distributor_id="+Distributor+" and isa.outlet_id in (SELECT distinct outlet_id FROM pep.distributor_beat_plan_view where assigned_to="+BookedBy+") and isa.type_id=1 group by cache_booked_by";
										
											//Q="SELECT isap.cache_booked_by,Sum(isap.total_units/isap.cache_units_per_sku) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id where isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)+" and isap.cache_distributor_id="+Distributor+"  and isa.type_id=1 group by cache_booked_by";
										
										
										ResultSet rs23 = s3.executeQuery(Q);
										
										if(rs23.first()){
											DeskQty = rs23.getDouble("Qty");
											DeskAmount = rs23.getDouble("total_sales");
											
										}

										Qty = Qty +DeskQty;
										Amount = Amount+DeskAmount;
										
										DistWiseTotalQty  +=Qty;
										DistWiseTotalAmount+=Amount;
										
										///////////////////////////////
										
										
										
										Total_qty+=Qty;
										Total_sale+=Amount;
										
										
										
										//BEETA QTY
										double BEETA_Total_qty=0;
										double BEETA_Total_sale_Amount=0;
										double BEETA_Qty=0;
										double BEETA_sale_Amount=0;
										
										Q="";
										if(BookedBy!=0){
											Q="SELECT isap.cache_booked_by,Sum(((isap.total_units/ipv.unit_per_sku)*ipv.unit_per_sku*ipv.liquid_in_ml)/3000) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name  from inventory_sales_adjusted_products isap join inventory_products_view ipv on ipv.product_id=isap.product_id where isap.cache_created_on_date between "+ Utilities.getSQLDate(StartDate) +" and " + Utilities.getSQLDate(EndDate)+ " and isap.cache_distributor_id="+Distributor+" and isap.cache_lrb_type_id=1 and isap.cache_booked_by="+BookedBy+" group by isap.cache_booked_by";
												
										}else{
											Q="SELECT isap.cache_booked_by,Sum(((isap.total_units/ipv.unit_per_sku)*ipv.unit_per_sku*ipv.liquid_in_ml)/3000) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name  from inventory_sales_adjusted_products isap join inventory_products_view ipv on ipv.product_id=isap.product_id where isap.cache_created_on_date between "+ Utilities.getSQLDate(StartDate) +" and " + Utilities.getSQLDate(EndDate)+ " and isap.cache_distributor_id="+Distributor+" and isap.cache_lrb_type_id=1 and isap.cache_booked_by is null group by isap.cache_booked_by";
											
										}
										
										ResultSet rsBEETA = s25.executeQuery(Q);
										
							if (rsBEETA.first())
							{
								BEETA_Qty=rsBEETA.getDouble("Qty");
								BEETA_sale_Amount=rsBEETA.getDouble("total_sales");
							}
										
							
							
							
							
							//Adding Desk Sales to Orderbooker sales
							
							double BEETA_Desk_Qty=0;
							double BEETA_Desk_Amount_Amount=0;
							
							/*  Q="";
							if(BookedBy!=0){
								Q="SELECT isap.cache_booked_by,Sum(isap.total_units/isap.cache_units_per_sku) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id where isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)+" and isap.cache_distributor_id="+Distributor+" and isa.outlet_id in (SELECT distinct outlet_id FROM pep.distributor_beat_plan_view where assigned_to="+BookedBy+") and isa.type_id=1  and cache_lrb_type_id=1 group by cache_booked_by";
							}else{
								Q="SELECT isap.cache_booked_by,Sum(isap.total_units/isap.cache_units_per_sku) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id where isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)+" and isap.cache_distributor_id="+Distributor+" and isa.type_id=1  and cache_lrb_type_id=1 group by cache_booked_by";
							} */
							Q="SELECT isap.cache_booked_by,Sum(((isap.total_units/ipv.unit_per_sku)*ipv.unit_per_sku*ipv.liquid_in_ml)/3000) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)+" and isap.cache_distributor_id="+Distributor+" and isa.outlet_id in (SELECT distinct outlet_id FROM pep.distributor_beat_plan_view where assigned_to="+BookedBy+") and isa.type_id=1  and cache_lrb_type_id=1 group by cache_booked_by";
							
							
							ResultSet rsBEETA2 = s26.executeQuery(Q);
							if(rsBEETA2.first()){
								BEETA_Desk_Qty = rsBEETA2.getDouble("Qty");
								BEETA_Desk_Amount_Amount = rsBEETA2.getDouble("total_sales");
								
							}

							BEETA_Qty = BEETA_Qty +BEETA_Desk_Qty;
							BEETA_sale_Amount = BEETA_sale_Amount+BEETA_Desk_Amount_Amount;
							
							DistWiseBEETATotalQty  +=BEETA_Qty;
							DistWiseBEETATotalAmount+=BEETA_sale_Amount;
							
							///////////////////////////////
							
							
							
							Total_BEETA_qty+=BEETA_Qty;
							Total_BEETA_sale+=BEETA_sale_Amount;
							
							
							// END BEETA QTY
							
							
							
							
							
							
							//SUGAR QTY
							double SUGAR_Total_qty=0;
							double SUGAR_Total_sale_Amount=0;
							double SUGAR_Qty=0;
							double SUGAR_sale_Amount=0;
							
							Q="";
							if(BookedBy!=0){
								Q="SELECT isap.cache_booked_by,Sum(isap.total_units*(SELECT liquid_in_ml FROM inventory_products_view where isap.product_id=inventory_products_view.product_id))/1000 Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap where isap.cache_created_on_date between "
												+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)
												+ " and cache_distributor_id=" + Distributor
												+ " and cache_lrb_type_id=2 and cache_booked_by="+BookedBy+" group by cache_booked_by;";
								
							}else{
								Q="SELECT isap.cache_booked_by,Sum(isap.total_units*(SELECT liquid_in_ml FROM inventory_products_view where isap.product_id=inventory_products_view.product_id))/1000 Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap where isap.cache_created_on_date between "
										+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)
										+ " and cache_distributor_id=" + Distributor
										+ " and cache_lrb_type_id=2 and cache_booked_by is null group by cache_booked_by;";} 
							ResultSet rsSUGAR = s27.executeQuery(Q);
				if (rsSUGAR.first())
				{
					SUGAR_Qty=rsSUGAR.getDouble("Qty");
					SUGAR_sale_Amount=rsSUGAR.getDouble("total_sales");
				}
							
				
				
				
				
				//Adding Desk Sales to Orderbooker sales
				
				double SUGAR_Desk_Qty=0;
				double SUGAR_Desk_Amount_Amount=0;
			
				Q="";
				/* if(BookedBy!=0){
					Q="SELECT isap.cache_booked_by,Sum(isap.total_units/isap.cache_units_per_sku) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id where isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)+" and isap.cache_distributor_id="+Distributor+" and isa.outlet_id in (SELECT distinct outlet_id FROM pep.distributor_beat_plan_view where assigned_to="+BookedBy+") and isa.type_id=1  and cache_lrb_type_id=2 group by cache_booked_by";
				}else{
					Q="SELECT isap.cache_booked_by,Sum(isap.total_units/isap.cache_units_per_sku) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id where isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)+" and isap.cache_distributor_id="+Distributor+" and isa.type_id=1  and cache_lrb_type_id=2 group by cache_booked_by";
				} */
				
				Q="SELECT isap.cache_booked_by,Sum(isap.total_units*(SELECT liquid_in_ml FROM inventory_products_view where isap.product_id=inventory_products_view.product_id))/1000 Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id where isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)+" and isap.cache_distributor_id="+Distributor+" and isa.outlet_id in (SELECT distinct outlet_id FROM pep.distributor_beat_plan_view where assigned_to="+BookedBy+") and isa.type_id=1  and cache_lrb_type_id=2 group by cache_booked_by";
				
				ResultSet rsSUGAR2 = s28.executeQuery(Q);
				if(rsSUGAR2.first()){
					SUGAR_Desk_Qty = rsSUGAR2.getDouble("Qty");
					SUGAR_Desk_Amount_Amount = rsSUGAR2.getDouble("total_sales");
					
				}

				SUGAR_Qty = SUGAR_Qty +SUGAR_Desk_Qty;
				SUGAR_sale_Amount = SUGAR_sale_Amount+SUGAR_Desk_Amount_Amount;
				
				
				
				DistWiseSUGARTotalQty  +=SUGAR_Qty;
				DistWiseSUGARTotalAmount+=SUGAR_sale_Amount;
				
				///////////////////////////////
				
				
				
				Total_SUGAR_qty+=SUGAR_Qty;
				Total_SUGAR_sale+=SUGAR_sale_Amount;
				
				
				// END SUGAR QTY
				
							
							
							
										
										String Orderbooker="";
										
										if(rs2.getString("cache_booked_by")==null || rs2.getString("cache_booked_by")=="null"){
											Orderbooker="Desk Sales";
										}else{
											 Orderbooker = rs2.getString("cache_booked_by")+" - "+rs2.getString("orderbooker_name");
										}
										
										
										
										
										
										
										
										
										
				%>
				<tr>
					<td style="text-align: center"><%=Count%></td>
					<td><%=Orderbooker%></td>
					<td><%=ASM%></td>
					<td style="text-align: right"><%if(BEETA_Qty!=0){%><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(BEETA_Qty)%><%} %>
					</td>
					<td style="text-align: right"><%if(SUGAR_Qty!=0){%><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(SUGAR_Qty)%><%} %>
					</td>
					<%-- <td style="text-align: right"><%=Utilities.getDisplayCurrencyFormatRounded(Qty)%>
					</td> --%>
					<td style="text-align: right"><%=Utilities.getDisplayCurrencyFormatRounded(Amount)%>
					</td>
				</tr>
				

				<%
					
								//}
							}
							%>
							
							<tr>
							
							
							
							<tr>
								<td style="text-align: right; font-weight: bold" colspan=3>Distribution Total</td>
								
								<td style="text-align: right; font-weight: bold" ><%if(DistWiseBEETATotalQty!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(DistWiseBEETATotalQty)%><%} %>
								</td>
								<td style="text-align: right; font-weight: bold" ><%if(DistWiseSUGARTotalQty!=0){%><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(DistWiseSUGARTotalQty)%><%} %>
								</td>
								<%-- <td style="text-align: right; font-weight: bold" ><%=Utilities.getDisplayCurrencyFormatRounded(DistWiseTotalQty)%>
								</td> --%>
								<td style="text-align: right;font-weight: bold"><%=Utilities.getDisplayCurrencyFormatRounded(DistWiseTotalAmount)%>
								</td>
							</tr>
							
							
							
							
							<%
							
							
							
							
						}
					}
				%>
				
				<tr>
					<td style="text-align: right; font-weight: bold" colspan=3>Grand Total</td>
					
					<td style="text-align: right; font-weight: bold"><%if(Total_BEETA_qty!=0){%><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(Total_BEETA_qty)%><%} %>
					</td>
					<td style="text-align: right; font-weight: bold"><%if(Total_SUGAR_qty!=0){%><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(Total_SUGAR_qty)%><%} %>
					</td>
					<%-- <td style="text-align: right; font-weight: bold"><%=Utilities.getDisplayCurrencyFormatRounded(Total_qty)%>
					</td> --%>
					<td style="text-align: right; font-weight: bold"><%=Utilities.getDisplayCurrencyFormatRounded(Total_sale)%>
					</td>
				</tr>
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