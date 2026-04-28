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
	int FeatureID = 367;

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

	int TotalBrands=0;
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
	
double Total_qty=0;
double Total_sale=0;

String wheredistribution="";
String distribution="";
String whereorderbooker="";
String orderbooker="";
// for KPO

ResultSet rs31 = s.executeQuery("select * from users where type_id=2 and distributor_id is not null and ID="+SessionUserID+"");
	int i=0;
	while (rs31.next()) 
	{
		if (i == 0) {
			distribution+=rs31.getString("distributor_id");
		} else {
			distribution += ", " + rs31.getString("distributor_id");
		}
		
		i++;
	}
	if(i>0 && distribution!="100914" )
	{wheredistribution=" and cache_distributor_id in ("+distribution+") ";}
	
	
// for mde


		


ResultSet rs32 = s.executeQuery("SELECT distinct dbpav.asm_id,display_name FROM distributor_beat_plan_all_view dbpav, users u where dbpav.asm_id=u.id and dbpav.asm_id="+SessionUserID+"");
i=0;
while (rs32.next()) 
{
	ResultSet rs33 = s2.executeQuery("SELECT dbp.id,dbp.label,dbpu.assigned_to,(SELECT DISPLAY_NAME FROM users where id=dbpu.assigned_to) orderbooker_name FROM distributor_beat_plan dbp join distributor_beat_plan_users dbpu on dbpu.id=dbp.id  where dbp.asm_id="+rs32.getInt("asm_id")+"");
	i=0;
	while (rs33.next()) 
	{
		if (i == 0) {
			orderbooker+=rs33.getString("assigned_to");
		} else {
			orderbooker += ", " + rs33.getString("assigned_to");
		}
		
		i++;
	}
	
	
}
if(i>0 && orderbooker!="2271")
{whereorderbooker=" and cache_booked_by in ("+orderbooker+") ";
}


ResultSet rs39 = s.executeQuery("select distinct(distributor_id) from distributor_beat_plan_view where asm_id="+SessionUserID+" ");
i=0;
while (rs39.next()) 
{
	if(i>0 && orderbooker!="2271" && distribution!="100914" ){
		distribution+=rs39.getString("distributor_id");
	} else {
		distribution += ", " + rs39.getString("distributor_id");
	}
	
	i++;
}
if(i>0 && distribution!="100914" )
{wheredistribution=" and cache_distributor_id in ("+distribution+") ";}
//for orderbooker

ResultSet rs35 = s.executeQuery("select distinct dbpv.assigned_to from distributor_beat_plan_view dbpv where dbpv.assigned_to="+SessionUserID+" ");
	i=0;
	while (rs35.next()) 
	{
		if (i == 0) {
			orderbooker+=rs35.getString("assigned_to");
		} else {
			orderbooker += ", " + rs35.getString("assigned_to");
		}
		
		i++;
	}
	if(i>0 && orderbooker!="2271")
	{whereorderbooker=" and cache_booked_by in ("+orderbooker+") ";}
	
	ResultSet rs40 = s.executeQuery("select distinct(distributor_id) from distributor_beat_plan_view where assigned_to="+SessionUserID+" ");
	i=0;
	while (rs40.next()) 
	{
		if (i == 0) {
			distribution+=rs40.getString("distributor_id");
		} else {
			distribution += ", " + rs40.getString("distributor_id");
		}
		
		i++;
	}
	if(i>0 && distribution!="100914" )
	{wheredistribution=" and cache_distributor_id in ("+distribution+") ";}

	//FOR SND

	/* ResultSet rs36 = s.executeQuery("select distinct(snd_id) from distributor_beat_plan_view where snd_id="+SessionUserID+"");
	i=0;
	while (rs36.next()) 
	{
		ResultSet rs37 = s2.executeQuery("select distinct(assigned_to) from distributor_beat_plan_view where snd_id="+rs36.getInt("snd_id")+"");
		i=0;
		while (rs37.next()) 
		{
			if (i == 0) {
				orderbooker+=rs37.getString("assigned_to");
			} else {
				orderbooker += ", " + rs37.getString("assigned_to");
			}
			
			i++;
		}
		
		
	}
	if(i>0 && orderbooker!="2271" )
	{whereorderbooker=" and cache_booked_by in ("+orderbooker+") ";
	} */
	
	ResultSet rs38 = s.executeQuery("select distinct(distributor_id) from distributor_beat_plan_view where snd_id="+SessionUserID+" ");
	i=0;
	while (rs38.next()) 
	{
		if (i == 0) {
			distribution+=rs38.getString("distributor_id");
		} else {
			distribution += ", " + rs38.getString("distributor_id");
		}
		
		i++;
	}
	if(i>0 && distribution!="100914" )
	{wheredistribution=" and cache_distributor_id in ("+distribution+") ";}
	
	
	if( orderbooker.equals("2271"))
	{whereorderbooker="";
	}
	if(distribution.equals("100914"))
	{wheredistribution="";
	}
	
	
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
							<tr style="font-size: 11px;">
								<!-- <th data-priority="1" style="text-align: center;">&nbsp;</th>
								 -->
								
 							<th data-priority="1"  style="text-align: center;">PSR Name</th>
								
								<% String Colour="";
									//String[] DiscountTitle= {"Gross Revenue","","","","","","","","","","",""};
								ResultSet rs1 = s.executeQuery("SELECT distinct(id) package_id FROM inventory_packages;");
								while (rs1.next()) {
								
									ResultSet rs2 = s2.executeQuery("select lrb_type_id,count(brand_id) brand_count,(select label from inventory_packages ip where ip.id="+ rs1.getInt("package_id")+") package_name from inventory_products_view  where package_id="+ rs1.getInt("package_id")+" and category_id=1 and is_visible=1");
									while (rs2.next()) 
									{   
										Colour="style='text-align:center'";
										if(rs2.getInt("lrb_type_id")==2)
										{Colour="style='background-color:#F6F6F6;text-align:center'";										
										}
										TotalBrands+=rs2.getInt("brand_count");
								%>
								<th data-priority="1"  <%=Colour %>
									colspan="<%=rs2.getInt("brand_count")%>"><%=rs2.getString("package_name")%></th>
								<%
									}
								}
								%>
						
						<th data-priority="1"  style="text-align: center;" colspan="2"> Converted</th>
						<th>Amount</th>
							</tr>
							
					
					       <tr style="font-size: 11px;">
							<th data-priority="1" style="text-align: center;"></th>
								
								<%
									//String[] DiscountTitle= {"Gross Revenue","","","","","","","","","","",""};
								ResultSet rs3 = s.executeQuery("SELECT distinct(id) package_id FROM inventory_packages;");
								while (rs3.next()) {
								
									ResultSet rs4 = s2.executeQuery("select distinct(brand_id),brand_label,lrb_type_id from inventory_products_view  where package_id="+ rs3.getInt("package_id")+" and category_id=1 and is_visible=1 order by brand_id");
									while (rs4.next()) {
										
										Colour="style='text-align:center'";
										if(rs4.getInt("lrb_type_id")==2)
										{Colour="style='background-color:#F6F6F6;text-align:center'";										
										}
								%>
								<th data-priority="1" <%=Colour %>><%=rs4.getString("brand_label")%></th>
								<%
									}
								}
								%>
						<th data-priority="1" style="text-align: center;"> Beeta</th>
						<th data-priority="1" style="background-color:#F6F6F6;text-align: center;"> Sugar</th>
						<!-- <th></th> -->
						<th></th>
							</tr>
			</thead>
			<tbody>

				<%
				double Total_BEETA_qty=0;
				double Total_BEETA_sale=0;
				
				double Total_SUGAR_qty=0;
				double Total_SUGAR_sale=0;
					int Count = 0;
					long Distributor=0;
				    ResultSet rs5 = s.executeQuery(
							"SELECT distinct cache_distributor_id as distributor_id,(select name from common_distributors where distributor_id=cache_distributor_id) as name from inventory_sales_adjusted_products as isap where isap.cache_created_on_date between "
									+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate) +wheredistribution);
					while (rs5.next()) {
						double DistWiseBEETATotalQty=0;
						double DistWiseBEETATotalAmount=0;
						
						double DistWiseSUGARTotalQty=0;
						double DistWiseSUGARTotalAmount=0;
						if (rs5.getLong("distributor_id") != 100914) {
							Count = 0;
							
							Distributor=rs5.getLong("distributor_id");
				%>
				<tr>

					<td data-priority="1"
						style="background-color: #F6F6F6; font-weight: bold" colspan="<%=TotalBrands+2+2%>"><%=rs5.getString("distributor_id") + "-" + rs5.getString("name")%></td>
				</tr>

	<%
				
				double Qty=0;
				double Amount=0;
				long BookedBy=0;
			    String Orderbooker="";
			    
			    /* System.out.println("query   :"+
						"SELECT isap.cache_booked_by,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap where isap.cache_created_on_date between "
								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)
								+ " and cache_distributor_id=" + Distributor
								+ whereorderbooker + " group by cache_booked_by;"); */
					ResultSet rs6 = s2.executeQuery(
									"SELECT isap.cache_booked_by,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap where isap.cache_created_on_date between "
											+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)
											+ " and cache_distributor_id=" + Distributor
											+ whereorderbooker + " group by cache_booked_by;");
							while (rs6.next()) {
								
								BookedBy = rs6.getLong("cache_booked_by");
								
								if(rs6.getString("cache_booked_by")==null || rs6.getString("cache_booked_by")=="null"){
									Orderbooker="Desk Sales";
								}else{
									 Orderbooker = rs6.getString("cache_booked_by")+" - "+rs6.getString("orderbooker_name");
								}
								%>
								<tr>
									
									<td><%=Orderbooker%></td>
								<%
								ResultSet rs7 = s3.executeQuery("SELECT distinct(id) package_id FROM inventory_packages;");
								while (rs7.next()) {
								
									ResultSet rs8 = s4.executeQuery("select distinct(brand_id),brand_label from inventory_products_view  where package_id="+ rs7.getInt("package_id")+" and category_id=1 and is_visible=1 order by brand_id");
									while (rs8.next()) {
										
										int productid=0;
										
										ResultSet rs9 = s5.executeQuery("SELECT lrb_type_id,product_id FROM inventory_products_view where package_id="+ rs7.getInt("package_id")+" and brand_id="+ rs8.getInt("brand_id")+"");
										if(rs9.first()) {
											
											Colour="style='text-align:center'";
											if(rs9.getInt("lrb_type_id")==2)
											{Colour="style='background-color:#F6F6F6;text-align:center'";										
											}
											productid = rs9.getInt("product_id");
										}
											
										double Qty2=0;
										String Query="";
											
											if(BookedBy!=0){
												Query="SELECT isap.cache_booked_by,Sum(isap.total_units/isap.cache_units_per_sku) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap where isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate) + "  and " + Utilities.getSQLDate(EndDate)+" and cache_distributor_id="+Distributor+" and product_id="+productid+ whereorderbooker +"  and cache_booked_by =" +BookedBy+" group by cache_booked_by;";
											}else if(BookedBy==0){
												Query="SELECT isap.cache_booked_by,Sum(isap.total_units/isap.cache_units_per_sku) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap where isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate) + "  and " + Utilities.getSQLDate(EndDate)+" and cache_distributor_id="+Distributor+" and product_id="+productid+ whereorderbooker +" and cache_booked_by is null group by cache_booked_by;";
											}
											
											
											ResultSet rs10  = s6.executeQuery(Query);
											
											while (rs10.next())
											{
												
												Qty2=rs10.getDouble("Qty");
										
											}
											
											
											//Adding Desk Sales to Orderbooker sales
											
											double DeskQty=0;
											double DeskAmount=0;
											
											ResultSet rs23 = s5.executeQuery("SELECT isap.cache_booked_by,Sum(isap.total_units/isap.cache_units_per_sku) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id where isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)+" and isap.cache_distributor_id="+Distributor+" and product_id="+productid+" and isa.outlet_id in (SELECT distinct outlet_id FROM distributor_beat_plan_view where assigned_to="+BookedBy+") "+whereorderbooker+" and isa.type_id=1 group by cache_booked_by");
											if(rs23.first()){
												DeskQty = rs23.getDouble("Qty");
												DeskAmount = rs23.getDouble("total_sales");
												
											}

											Qty2 = Qty2 +DeskQty;
											//Amount = Amount+DeskAmount;
											
											
									
										%>
										
										<td <%=Colour %>><%if(Qty2!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(Qty2)%><%} %></td>
										<%
								
									}
								}
								
								
								//BEETA QTY
								double BEETA_Total_qty=0;
								double BEETA_Total_sale_Amount=0;
								double BEETA_Qty=0;
								double BEETA_sale_Amount=0;
								String Q="";
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
					
					
					//BEETA QTY
					
								
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
					
		
		/* //System.out.println(Q); */
		
		
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
								String Query2="";
								if(BookedBy!=0){
									Query2="SELECT isap.cache_booked_by,Sum(isap.total_units/isap.cache_units_per_sku) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap where isap.cache_created_on_date between "
											+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)
											+ " and cache_distributor_id=" + Distributor
											+ " "+whereorderbooker+" and isap.cache_booked_by = " + BookedBy + " group by cache_booked_by;";
								}else if(BookedBy==0){
									Query2="SELECT isap.cache_booked_by,Sum(isap.total_units/isap.cache_units_per_sku) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap where isap.cache_created_on_date between "
											+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)
											+ " and cache_distributor_id=" + Distributor
											+ whereorderbooker+ " and isap.cache_booked_by is null group by cache_booked_by;";
								}
								//System.out.println(Query2);
								ResultSet rs24 = s6.executeQuery(Query2);
								if (rs24.first()) 
								{
									%>
									
									<td style="text-align: center"><%if(BEETA_Qty!=0){%><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(BEETA_Qty)%><%} %>
					</td>
					<td style='background-color:#F6F6F6;text-align:center'><%if(SUGAR_Qty!=0){%><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(SUGAR_Qty)%><%} %>
					</td>
									<%-- <td style="text-align: center"><%if(rs24.getDouble("Qty")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs24.getDouble("Qty"))%><%} %></td>
									 --%><td style="text-align: center"><%if(rs24.getDouble("total_sales")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs24.getDouble("total_sales"))%><%} %></td>
										
									<%
									
								}else
								{
									
									%>
									<td></td>
									<td></td>
									<!-- <td></td> -->
									<td></td>
									<%
								}
								%>
								
								</tr>	
							

							<%
									}
							}
						%>
						<tr>	
						<td style="background-color:#e6ffec;font-weight: bold;text-align: center">Distribution Total	</td>
				<% 
						ResultSet rs25 = s3.executeQuery("SELECT distinct(id) package_id FROM inventory_packages;");
						while (rs25.next()) {
						
							ResultSet rs26 = s4.executeQuery("select distinct(brand_id),brand_label from inventory_products_view  where package_id="+ rs25.getInt("package_id")+" and category_id=1 and is_visible=1 order by brand_id");
							while (rs26.next()) {
								
								int productid=0;
								
								ResultSet rs27 = s5.executeQuery("SELECT lrb_type_id,product_id FROM inventory_products_view where package_id="+ rs25.getInt("package_id")+" and brand_id="+ rs26.getInt("brand_id")+"");
								if(rs27.first()) {
									Colour="style='background-color:#e6ffec;text-align:center'";
									if(rs27.getInt("lrb_type_id")==2)
									{Colour="style='background-color:#e6ffec;text-align:center'";										
									}
									productid = rs27.getInt("product_id");
									
									ResultSet rs28 = s6.executeQuery("SELECT Sum(isap.total_units/isap.cache_units_per_sku) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap where isap.cache_created_on_date between "
											+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)
											+ " and product_id="+productid+whereorderbooker+" and cache_distributor_id=" + Distributor
											+ "  ");
									if(rs28.first()) {
										%>
										<td <%=Colour %>><%if(rs28.getInt("QTY")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs28.getInt("QTY"))%><%} %></td>
										
									<%}
									
								}}}
						
						
						String Query3="";
						
							Query3="SELECT isap.cache_booked_by,Sum(isap.total_units/isap.cache_units_per_sku) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap where isap.cache_created_on_date between "
									+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)
									+ whereorderbooker+" and cache_distributor_id=" + Distributor;
						
						
						ResultSet rs29 = s6.executeQuery(Query3);
						if (rs29.first()) 
						{
							%>
							
							<td style="background-color:#e6ffec;text-align: center" ><%if(DistWiseBEETATotalQty!=0){%><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(DistWiseBEETATotalQty)%><%} %>
								</td>
								<td style="background-color:#e6ffec;text-align: center" ><%if(DistWiseSUGARTotalQty!=0){%><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(DistWiseSUGARTotalQty)%><%} %>
								</td>
							<%-- <td style="text-align: center"><%if(rs29.getDouble("Qty")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs29.getDouble("Qty"))%><%} %></td>
							 --%>
							 <td style="background-color:#e6ffec;text-align: center"><%if(rs29.getDouble("total_sales")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs29.getDouble("total_sales"))%><%} %></td>
								
							<%
							
						}else
						{
							
							%>
							<td style="background-color:#e6ffec;text-align: center"></td>
							<td style="background-color:#e6ffec;text-align: center"></td>
							<!-- <td></td> -->
							<td style="background-color:#e6ffec;text-align: center"></td>
							<%
						}
					%> </tr><% 
					}
					
				%>
				<tr>	
						<td style="background-color:#FFFFCC;font-weight: bold;text-align: center">Grand Total</td>
				<% 
						ResultSet rs25 = s3.executeQuery("SELECT distinct(id) package_id FROM inventory_packages;");
						while (rs25.next()) {
						
							ResultSet rs26 = s4.executeQuery("select distinct(brand_id),brand_label,lrb_type_id from inventory_products_view  where package_id="+ rs25.getInt("package_id")+" and category_id=1 and is_visible=1 order by brand_id");
							while (rs26.next()) {
								Colour="style='background-color:#FFFFCC;font-weight: bold;text-align: center'";
								if(rs26.getInt("lrb_type_id")==2)
								{Colour="style='background-color:#FFFFCC;font-weight: bold;text-align: center'";										
								}
								int productid=0;
								
								ResultSet rs27 = s5.executeQuery("SELECT product_id FROM inventory_products_view where package_id="+ rs25.getInt("package_id")+" and brand_id="+ rs26.getInt("brand_id")+"");
								if(rs27.first()) {
									productid = rs27.getInt("product_id");
									
									ResultSet rs28 = s6.executeQuery("SELECT Sum(isap.total_units/isap.cache_units_per_sku) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap where isap.cache_created_on_date between "
											+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)
											+ wheredistribution+whereorderbooker+ " and product_id="+productid+" ");
									if(rs28.first()) {
										%>
										<td <%=Colour %>><%if(rs28.getInt("QTY")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs28.getInt("QTY"))%><%} %></td>
										
									<%}
									
								}}}
						
						String Query3="";
						
							Query3="SELECT isap.cache_booked_by,Sum(isap.total_units/isap.cache_units_per_sku) Qty, sum(isap.net_amount) total_sales ,(select DISPLAY_NAME from users where id= isap.cache_booked_by) as orderbooker_name from inventory_sales_adjusted_products as isap where isap.cache_created_on_date between "
									+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)+whereorderbooker+wheredistribution ;
						
						
						ResultSet rs29 = s6.executeQuery(Query3);
						if (rs29.first()) 
						{
							%>
							
							<td style="background-color:#FFFFCC;font-weight: bold;text-align: center""><%if(Total_BEETA_qty!=0){%><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(Total_BEETA_qty)%><%} %>
					</td>
					<td style="background-color:#FFFFCC;font-weight: bold;text-align: center"><%if(Total_SUGAR_qty!=0){%><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(Total_SUGAR_qty)%><%} %>
					</td>
							<%-- <td style="text-align: center"><%if(rs29.getDouble("Qty")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs29.getDouble("Qty"))%><%} %></td>
							 --%><td style="background-color:#FFFFCC;font-weight: bold;text-align: center"><%if(rs29.getDouble("total_sales")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs29.getDouble("total_sales"))%><%} %></td>
								
							<%
							
						}else
						{
							
							%>
							<td></td>
							<td></td>
							<!-- <td></td> -->
							<td></td>
							<%
						}
					%> </tr>
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