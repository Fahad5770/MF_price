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
int FeatureID = 223;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
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
Statement s11= c.createStatement();
Statement s12 = c.createStatement();
Statement s13 = c.createStatement();
Statement s14 = c.createStatement();
Statement s15 = c.createStatement();
Statement s16 = c.createStatement();
Statement s17 = c.createStatement();
Statement s18 = c.createStatement();


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

Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);


//creating temporary table

Datasource dsSAP = new Datasource();
dsSAP.createConnectionToSAPDB();
Statement sSAP = dsSAP.createStatement();


Date MinTargetDate = new Date();
Date MaxTargetDate = new Date();

//System.out.println("select min(dt.start_date), max(dt.end_date) from distributor_targets dt where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y')  and dt.type_id=3");

ResultSet rs14 = s3.executeQuery("select min(dt.start_date), max(dt.end_date) from distributor_targets dt where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y')  and dt.type_id=3");
if(rs14.first()){
	MinTargetDate = rs14.getDate(1);
	MaxTargetDate = rs14.getDate(2); //Utilities.parseDate("10/01/2014");//rs14.getDate(1);
}




s.executeUpdate("CREATE temporary TABLE dist_targets_temp (order_number varchar(100) , distributor_id varchar(100) ,distributor_name varchar(100) ,entry_date date ,order_date date ,fksak varchar(100) ,abgru varchar(100) ,posnr varchar(100) ,sap_code varchar(100) ,arktx varchar(100) , type varchar(100) , raw_case int, units int,total_units int , pstyv varchar(100) )");

s.executeUpdate("CREATE temporary TABLE dist_targets_report_temp (distributor_id int,distributor_name varchar(100),sales decimal(18,2),target decimal(18,2), percentage varchar(50),package_id int,type_id int,snd_id int, rsm_id int, tdm_id int)");


String QueryPrt="";
int UnitPerSKU=0;
int TotalUnits=0;

//System.out.println("Hello "+StartDate);

SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");


//System.out.println("SELECT vbak.vbeln order_number, vbak.kunnr distributor_id, vbak.erdat entry_date, vbak.audat order_date, vbuk.fksak, vbap.ABGRU, vbap.posnr, vbap.matnr, vbap.arktx, vbap.vrkme, vbap.KWMENG, vbap.pstyv FROM sapsr3.vbak vbak join sapsr3.vbuk vbuk on vbak.vbeln = vbuk.vbeln join sapsr3.vbap vbap on vbak.vbeln = vbap.vbeln where vbak.auart in ('ZDIS','ZMRS','ZDFR') and vbak.audat between  '"+df.format(StartDate)+"' and '"+df.format(EndDate)+"' and vbuk.vbtyp = 'C'");

//System.out.println("SELECT vbak.vbeln order_number, vbak.kunnr distributor_id, vbak.erdat entry_date, vbak.audat order_date, vbuk.fksak, vbap.ABGRU, vbap.posnr, vbap.matnr, vbap.arktx, vbap.vrkme, vbap.KWMENG, vbap.pstyv FROM sapsr3.vbak vbak join "+dsSAP.getSAPDatabaseAlias()+".vbuk vbuk on vbak.vbeln = vbuk.vbeln join sapsr3.vbap vbap on vbak.vbeln = vbap.vbeln where vbak.auart in ('ZDIS','ZMRS','ZDFR') and vbak.audat between  '"+df.format(StartDate)+"' and '"+df.format(EndDate)+"' and vbuk.vbtyp = 'C'");


ResultSet rs5 = sSAP.executeQuery("SELECT vbak.vbeln order_number, vbak.kunnr distributor_id, vbak.erdat entry_date, vbak.audat order_date, vbuk.fksak, vbap.ABGRU, vbap.posnr, vbap.matnr, vbap.arktx, vbap.vrkme, vbap.KWMENG, vbap.pstyv FROM sapsr3.vbak vbak join "+dsSAP.getSAPDatabaseAlias()+".vbuk vbuk on vbak.vbeln = vbuk.vbeln join sapsr3.vbap vbap on vbak.vbeln = vbap.vbeln where vbak.auart in ('ZDIS','ZMRS','ZDFR') and vbak.audat between  '"+df.format(MinTargetDate)+"' and '"+df.format(MaxTargetDate)+"' and vbuk.vbtyp = 'C' and vbap.pstyv != 'TANN' and vbap.ABGRU = ' '");
while(rs5.next()){
	//System.out.println("insert into dist_targets_temp values("+rs5.getInt("order_number")+","+rs5.getInt("distributor_id")+",(select name from common_distributors cd where cd.distributor_id="+rs5.getInt("distributor_id")+") ,"+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs5.getString("entry_date")))+","+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs5.getString("order_date")))+",'"+rs5.getString("fksak")+"','"+rs5.getString("ABGRU")+"','"+rs5.getString("posnr")+"','"+rs5.getString("matnr")+"','"+Utilities.filterString(rs5.getString("arktx"), 2, 100)+"','"+rs5.getString("vrkme")+"',"+rs5.getInt("KWMENG")+",'"+rs5.getString("pstyv")+"')");
	
	long SapCode=Utilities.parseLong(rs5.getString("matnr"));
	
	if(rs5.getString("vrkme").equals("KI") || rs5.getString("vrkme").equals("KAR")){					
		
		QueryPrt ="raw_case";
		ResultSet rs1 = s2.executeQuery("select unit_per_sku from inventory_products where id= (select id from inventory_products where sap_code="+SapCode+")");
		if(rs1.first()){
			UnitPerSKU = rs1.getInt("unit_per_sku");
			TotalUnits = UnitPerSKU*(Utilities.parseInt(rs5.getString("KWMENG")));
		}else{
			//System.out.println("Unit Per SKU does not exist for "+SapCode);
		}
			
	}else{
		QueryPrt ="units";
		TotalUnits = Utilities.parseInt(rs5.getString("KWMENG"));
	}
	
	//System.out.println("Units "+TotalUnits);
	
	
	//System.out.println("insert into dist_targets_temp(order_number  , distributor_id  ,distributor_name  ,entry_date  ,order_date  ,fksak  ,abgru  ,posnr  ,sap_code  ,arktx  , type  , "+QueryPrt+" ,total_units  , pstyv ) values('"+rs5.getString("order_number")+"','"+rs5.getString("distributor_id")+"',(select name from common_distributors cd where cd.distributor_id='"+rs5.getString("distributor_id")+"') ,"+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs5.getString("entry_date")))+","+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs5.getString("order_date")))+",'"+rs5.getString("fksak")+"','"+rs5.getString("ABGRU")+"','"+rs5.getString("posnr")+"','"+rs5.getString("matnr")+"','"+Utilities.filterString(rs5.getString("arktx"), 2, 100)+"','"+rs5.getString("vrkme")+"','"+rs5.getString("KWMENG")+"','"+TotalUnits+"','"+rs5.getString("pstyv")+"')");
	
	s.executeUpdate("insert into dist_targets_temp(order_number  , distributor_id  ,distributor_name  ,entry_date  ,order_date  ,fksak  ,abgru  ,posnr  ,sap_code  ,arktx  , type  , "+QueryPrt+" ,total_units  , pstyv ) values('"+rs5.getString("order_number")+"','"+rs5.getString("distributor_id")+"',(select name from common_distributors cd where cd.distributor_id='"+rs5.getString("distributor_id")+"') ,"+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs5.getString("entry_date")))+","+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs5.getString("order_date")))+",'"+rs5.getString("fksak")+"','"+rs5.getString("ABGRU")+"','"+rs5.getString("posnr")+"',"+SapCode+",'"+Utilities.filterString(rs5.getString("arktx"), 2, 100)+"','"+rs5.getString("vrkme")+"',"+rs5.getInt("KWMENG")+","+TotalUnits+",'"+rs5.getString("pstyv")+"')");
}

%>
							
							<%
							
							String SAPCodes = "0";

							ResultSet rs60 = s.executeQuery("SELECT group_concat(distinct sap_code) from dist_targets_temp dtt where dtt.order_date between "+Utilities.getSQLDate(MinTargetDate)+" and "+Utilities.getSQLDate(MaxTargetDate));
							if (rs60.next()){
								SAPCodes = rs60.getString(1);
							}

							int PackageCount = 0;
							int ArrayCount=0;
							int PackageID=0;
							int DistributorID=0;
							double GrandTotal=0;
							String DistributorName="";
							
							int SNDID=0;
							int RSMID=0;
							int TDMID=0;
							
							//System.out.println("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  product_id in (SELECT distinct idnp.product_id FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id where idn.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+") "+WherePackage+ "order by package_sort_order");
							
							ResultSet rs11 = s.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
							while(rs11.next()){
							ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id="+rs11.getInt("lrb_type_id")+" order by package_sort_order");
							
							
							while(rs2.next()){
								
								PackageCount++;
							
								ArrayCount++;
							}
							
							//PackageCount = PackageCount+1; //1 plus for extra column of converted cases
							
							
						}
						long SalesTotal[] = new long[ArrayCount+1];
						int TargetTotal[] = new int[ArrayCount+1];
						long PercentageTotal[] = new long[ArrayCount+1]; //+1 for converted cases
						
						for (int i = 0; i < SalesTotal.length; i++){
							SalesTotal[i] = 0;								
							TargetTotal[i]=0;
							PercentageTotal[i]=0;
						}
						
						
						
						%>
							
						
					
					
						<%
								
											ResultSet rs1 = s.executeQuery("select * from common_distributors where 1=1 "+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+" and distributor_id in ("+DistributorIDs+") and distributor_id in (select dt.distributor_id from distributor_targets dt where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y') and dt.type_id=3)"); //distributor query
											
											while(rs1.next()){
												
												double TDMWiseTotalSales=0;
												double TDMWiseTotalTarget=0;
												
												DistributorID = rs1.getInt("distributor_id");
												DistributorName = rs1.getString("name");
												SNDID = rs1.getInt("snd_id");
												RSMID = rs1.getInt("rsm_id");
												TDMID = rs1.getInt("tdm_id");
												
												
												Date TargetStartDate = new Date();
												Date TargetEndDate = new Date();
					
												ResultSet rs19 = s3.executeQuery("select dt.start_date, dt.end_date from distributor_targets dt where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y') and dt.distributor_id = "+DistributorID+" and dt.type_id=3");
												if(rs19.first()){
													TargetStartDate = rs19.getDate(1);
													TargetEndDate = rs19.getDate(2);
												}
												
											%>
											
											
												<%
												
												//ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM pep.mobile_order_products) "+WherePackage+" order by package_sort_order");//package query
												//rs.beforeFirst();
												
												int PackageIndex = 0;
												long SalesConvertedCases=0;
												long TargetConvertedCases=0;
												double ConvertedCasesPercentage=0;
												
												
												
												
												//Converted Cases 
												
												ResultSet rs6 = s3.executeQuery("select "+    
															"sum(((total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml ) ) sale_amount "+																		        
													",ipv.unit_per_sku,ipv.conversion_rate_in_ml,ipv.liquid_in_ml "+
													"from "+
													    "dist_targets_temp dtt "+
													        "join "+								   
													    "inventory_products_view ipv ON dtt.sap_code=ipv.sap_code "+
													"where ipv.category_id = 1 and dtt.order_date between "+Utilities.getSQLDate(TargetStartDate)+" and "+Utilities.getSQLDateNext(TargetEndDate)+" and dtt.distributor_id = "+DistributorID);
												
												if(rs6.first()){								
														SalesConvertedCases = rs6.getLong("sale_amount");	
														TDMWiseTotalSales+=SalesConvertedCases;
												}
												 
												
												ResultSet rs7 = s3.executeQuery("select sum((dtp.quantity*ip.unit_per_case*ip.liquid_in_ml)/ip.conversion_rate_in_ml) quantity,ip.liquid_in_ml,ip.conversion_rate_in_ml from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id join inventory_packages ip on dtp.package_id = ip.id where dt.distributor_id="+DistributorID+" and month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y') and dt.type_id=3");
												
												//System.out.println("select sum((dtp.quantity*ip.unit_per_case*ip.liquid_in_ml)/ip.conversion_rate_in_ml) quantity,ip.liquid_in_ml,ip.conversion_rate_in_ml from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id join inventory_packages ip on dtp.package_id = ip.id where dt.distributor_id="+DistributorID+" and month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y') and dt.type_id=1");
												if(rs7.first()){								
														TargetConvertedCases = rs7.getLong("quantity");	
														TDMWiseTotalTarget+=TargetConvertedCases;
												}
												
												if(TargetConvertedCases!=0){
													ConvertedCasesPercentage = ((SalesConvertedCases * 1d) / (TargetConvertedCases * 1d) )*100;
												}
												
												SalesTotal[PackageIndex] += SalesConvertedCases;
												TargetTotal[PackageIndex] += TargetConvertedCases;							
												
												
												PackageIndex++;
												
												s7.executeUpdate("insert into dist_targets_report_temp(distributor_id,distributor_name,sales,target, percentage,package_id,type_id,snd_id, rsm_id, tdm_id) values("+DistributorID+",'"+DistributorName+"',"+SalesConvertedCases+","+TargetConvertedCases+",'"+ConvertedCasesPercentage+"',-1,-1,"+SNDID+","+RSMID+","+TDMID+")");
												
												
												%>
												
												
												
												
												
												<%
												ResultSet rs12 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
												while(rs12.next()){
													
													int TypeID = rs12.getInt("lrb_type_id");
													ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id="+rs12.getInt("lrb_type_id")+" order by package_sort_order");
													
													while(rs.next()){
													int unit_per_sku=0;
													PackageID=rs.getInt("package_id");
													
														double target_sales_amount=0;
														int target_unit_per_sku=0;
														double percentage=0;
					
														ResultSet rs4 = s3.executeQuery("select dtp.quantity from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.distributor_id="+DistributorID+" and month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y') and dtp.package_id="+PackageID+WherePackage+" and dt.type_id=3");
														if(rs4.first()){
															target_sales_amount=rs4.getDouble("quantity");	
														}
														
														if (PackageID == 2 || PackageID == 6){ // 500ML, 1500ML
															if (TypeID == 1 || TypeID == 2 || TypeID == 3 || TypeID == 4){ // Water, Energy Drink, Juices
																ResultSet rs20 = s3.executeQuery("select sum(dtpb.quantity) from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id join distributor_targets_packages_brands dtpb on dtp.id = dtpb.id and dtp.package_id = dtpb.package_id where dt.distributor_id="+DistributorID+" and month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y') and dtp.package_id="+PackageID+" and dt.type_id=3 and dtpb.brand_id in (select brand_id from inventory_products where lrb_type_id = "+TypeID+")");
																if(rs20.first()){
																	target_sales_amount=rs20.getDouble(1);	
																}
															}
														}
														
														long sales_amount=0;
														ResultSet rs3 = s3.executeQuery("select sum(total_units) sale_amount, ipv.unit_per_sku from dist_targets_temp dtt join inventory_products_view ipv ON dtt.sap_code = ipv.sap_code where ipv.category_id = 1 and dtt.order_date between "+Utilities.getSQLDate(TargetStartDate)+" and "+Utilities.getSQLDate(TargetEndDate)+" and dtt.distributor_id = "+DistributorID+" and ipv.package_id="+PackageID+WherePackage+" and ipv.lrb_type_id="+TypeID);
														if(rs3.first()){
															sales_amount = rs3.getLong("sale_amount");									
															unit_per_sku = rs3.getInt("unit_per_sku");								
														}
														
														long SalesRawCases = Utilities.getRawCasesAndUnits(sales_amount, unit_per_sku)[0];
														
														if(target_sales_amount!=0){
															percentage = (Utilities.parseDouble(SalesRawCases+"")/target_sales_amount)*100;	
														}	
														
														SalesTotal[PackageIndex] += SalesRawCases;
														TargetTotal[PackageIndex] += target_sales_amount;
														
													%>
													
														
														<%
														
														//insert it into temp table
														
														s7.executeUpdate("insert into dist_targets_report_temp(distributor_id,distributor_name,sales,target, percentage,package_id,type_id,snd_id, rsm_id, tdm_id) values("+DistributorID+",'"+DistributorName+"',"+SalesRawCases+","+target_sales_amount+",'"+percentage+"',"+PackageID+","+TypeID+","+SNDID+","+RSMID+","+TDMID+")");
														
														PackageIndex++;
													}
												}
												
												%>
										
											
											
											
											
											
											<%
										}
						
											
											
											
									


						%>
						
						
					
					
					
					
					
					<!--  -------------------------------- -->
<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Primary Sales vs Targets</li>
<li>
<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    
					    <tr style="font-size:11px;">
					   		<th data-priority="1"  style="text-align:center; min-width:150px;">&nbsp;</th>
							<th  colspan="3" style="text-align:center; ">&nbsp;</th>
							<%
							
							//String[] DiscountTitle= {"Gross Revenue","","","","","","","","","","",""};
							
							
							ResultSet rs101 = s.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
							while(rs101.next()){
								%>
								<th data-priority="1"  style="text-align:center; " colspan="<%=rs101.getInt("packge_count")*3%>"><%=rs101.getString("type_name") %></th>
								<%
							}
							
							%>
												
					    </tr>
					    
					    <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
							<th colspan="3" style="text-align:center; ">Converted Cases</th>
							<%
							
							
							//System.out.println("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  product_id in (SELECT distinct idnp.product_id FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id where idn.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+") "+WherePackage+ "order by package_sort_order");
							
							ResultSet rs111 = s.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
							while(rs111.next()){
							ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id="+rs111.getInt("lrb_type_id")+" order by package_sort_order");
							
							
							while(rs2.next()){
								
								PackageCount++;
							
							%>
							<th data-priority="1" colspan="3"  style="text-align:center; " >
							<%=rs2.getString("package_label")%>							
							
							</th>
							
							<%
							
							}
							
							//PackageCount = PackageCount+1; //1 plus for extra column of converted cases
							
							
						}
						
						
						
						
						%>
							
												
					    </tr>
					    <tr style="font-size:11px;">
						    <th data-priority="1"  style="text-align:center; ">&nbsp;</th>
						    <th data-priority="1"  style="text-align:center; min-width:30px;">S</th>
						    <th data-priority="1"  style="text-align:center; min-width:30px;">T</th>
						    <th data-priority="1"  style="text-align:center; min-width:30px;background-color:#F6F6F6;">P</th>
						    <%
						    for(int i=0;i<ArrayCount;i++){
						    	
						    %>
						   
						    <th data-priority="1"  style="text-align:center; min-width:30px;">S</th>
						    <th data-priority="1"  style="text-align:center; min-width:30px;">T</th>
						    <th data-priority="1"  style="text-align:center; min-width:30px;background-color:#F6F6F6;">P</th>
					    <%} %>
					    
					    </tr>
					  </thead> 
					<tbody>
					<%
					long DistributorIDD1=0;
					long DistributorIDD=0;
					
					long RSMID1=0;
					String RSMName="";
					
					long SNDID1=0;
					String SNDName="";
					
					
					ResultSet rs1132 = s16.executeQuery("SELECT distinct snd_id,(select display_name from users u where u.id=snd_id) snd_name FROM dist_targets_report_temp where snd_id is not null");
					while(rs1132.next()){
						SNDID1 = rs1132.getLong("snd_id");
						SNDName = rs1132.getString("snd_name");
						%>
						<tr>
							<td style="background-color:#d0d0d0; font-weight: bold; font-size: 12px;"><%= SNDID1+ " - "+ SNDName %></td>	
									
									<%
									ResultSet rs3152 = s17.executeQuery("select sum(sales) sum_converted,sum(target) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and snd_id="+SNDID1);
									if(rs3152.first()){
										double CasesCovertedPercentage1=0;
										double SalesCoverted = rs3152.getDouble("sum_converted");
										double TargetCoverted = rs3152.getDouble("sum_targeted");
										
										if(TargetCoverted!=0){
											CasesCovertedPercentage1 = ((SalesCoverted * 1d) / (TargetCoverted * 1d) )*100;
										}
									%>
									
									
									<td style="text-align: right; background-color:#d0d0d0; font-weight: bold; font-size: 12px;"><%if(SalesCoverted!=0){%><%=Utilities.getDisplayCurrencyFormat(SalesCoverted) %><%} %></td>
									<td style="text-align: right; background-color:#d0d0d0; font-weight: bold; font-size: 12px;"><%if(TargetCoverted!=0){%><%=Utilities.getDisplayCurrencyFormat(TargetCoverted) %><%} %></td>									
									<td style="text-align: right; background-color:#d0d0d0; font-weight: bold; font-size: 12px;"><%if(CasesCovertedPercentage1!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage1) %>%<%} %></td>
							<%
									}
							ResultSet rs1232 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
							while(rs1232.next()){
								
								int TypeID3 = rs1232.getInt("lrb_type_id");
								ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id="+rs1232.getInt("lrb_type_id")+" order by package_sort_order");
								
								while(rs.next()){
								int unit_per_sku=0;
								PackageID=rs.getInt("package_id");
								//System.out.println("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and tdm_id="+TDMID1);
								
								ResultSet rs315 = s18.executeQuery("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and snd_id="+SNDID1);
								if(rs315.first()){
									double CasesPercentage1=0;
									double Sales = rs315.getDouble("sum_sales");
									double Target = rs315.getDouble("sum_target");
									
									if(Target!=0){	
										CasesPercentage1 = ((Sales * 1d) / (Target * 1d) )*100;
									}
									
									
								%>
								
									
									<td style="text-align: right; background-color:#d0d0d0; font-weight: bold; font-size: 12px;"><%if(Sales!=0){%><%=Utilities.getDisplayCurrencyFormat(Sales) %><%} %></td>
									<td style="text-align: right; background-color:#d0d0d0; font-weight: bold; font-size: 12px;"><%if(Target!=0){%><%=Utilities.getDisplayCurrencyFormat(Target) %><%} %></td>									
									<td style="text-align: right; background-color:#d0d0d0; font-weight: bold; font-size: 12px;"><%if(CasesPercentage1!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(CasesPercentage1) %>%<%} %></td>
								<%
								}
								}
							}
							
							
							%>
							</tr>
					
					<%
					
					
					ResultSet rs1131 = s13.executeQuery("SELECT distinct rsm_id,(select display_name from users u where u.id=rsm_id) rsm_name FROM dist_targets_report_temp where snd_id="+SNDID1);
					while(rs1131.next()){
						RSMID1 = rs1131.getLong("rsm_id");
						RSMName = rs1131.getString("rsm_name");
						%>
						<tr>
							<td style="background-color:#e3e2e2;font-weight: bold; font-size: 10px;"><%= RSMID1+ " - "+ RSMName %></td>	
									
									<%
									ResultSet rs3151 = s14.executeQuery("select sum(sales) sum_converted,sum(target) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and rsm_id="+RSMID1+" and snd_id="+SNDID1);
									if(rs3151.first()){
										double CasesCovertedPercentage1=0;
										double SalesCoverted = rs3151.getDouble("sum_converted");
										double TargetCoverted = rs3151.getDouble("sum_targeted");
										
										if(TargetCoverted!=0){
											CasesCovertedPercentage1 = ((SalesCoverted * 1d) / (TargetCoverted * 1d) )*100;
										}
										
									%>
									
									
									<td style="text-align: right; background-color:#e3e2e2;font-weight: bold; font-size: 10px;"><%if(SalesCoverted!=0){%><%=Utilities.getDisplayCurrencyFormat(SalesCoverted) %><%} %></td>
									<td style="text-align: right; background-color:#e3e2e2;font-weight: bold; font-size: 10px;"><%if(TargetCoverted!=0){%><%=Utilities.getDisplayCurrencyFormat(TargetCoverted) %><%} %></td>									
									<td style="text-align: right;background-color:#e3e2e2;font-weight: bold; font-size: 10px;"><%if(CasesCovertedPercentage1!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage1) %>%<%} %></td>
							<%
									}
							ResultSet rs1231 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
							while(rs1231.next()){
								
								int TypeID3 = rs1231.getInt("lrb_type_id");
								ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id="+rs1231.getInt("lrb_type_id")+" order by package_sort_order");
								
								while(rs.next()){
								int unit_per_sku=0;
								PackageID=rs.getInt("package_id");
								//System.out.println("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and tdm_id="+TDMID1);
								
								ResultSet rs314 = s15.executeQuery("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and rsm_id="+RSMID1+" and snd_id="+SNDID1);
								if(rs314.first()){
									double CasesPercentage1=0;
									double Sales = rs314.getDouble("sum_sales");
									double Target = rs314.getDouble("sum_target");
									
									if(Target!=0){
										CasesPercentage1 = ((Sales * 1d) / (Target * 1d) )*100;
									}
									
									
								%>
								
									
									<td style="text-align: right; background-color:#e3e2e2;font-weight: bold; font-size: 10px;"><%if(Sales!=0){%><%=Utilities.getDisplayCurrencyFormat(Sales) %><%} %></td>
									<td style="text-align: right; background-color:#e3e2e2;font-weight: bold; font-size: 10px;"><%if(Target!=0){%><%=Utilities.getDisplayCurrencyFormat(Target) %><%} %></td>									
									<td style="text-align: right;background-color:#e3e2e2;font-weight: bold; font-size: 10px;"><%if(CasesPercentage1!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(CasesPercentage1) %>%<%} %></td>
								<%
								}
								}
							}
							
							
							%>
							</tr>
							
					
					
				<%	
					
					
					long TDMID1 = 0;
					String TDMName = "";
					
					
					
					ResultSet rs113 = s10.executeQuery("SELECT distinct tdm_id,(select display_name from users u where u.id=tdm_id) tdm_name FROM dist_targets_report_temp where rsm_id="+RSMID1+" and snd_id="+SNDID1);
					while(rs113.next()){
						TDMID1 = rs113.getLong("tdm_id");
						TDMName = rs113.getString("tdm_name");
						
						
						String TDMDisplay = TDMID1+ " - "+ TDMName;
						if (TDMID1 == 0){
							TDMDisplay = "Unassigned";
						}
						%>
						<tr>
							<td style="background-color:#ECECDC;font-weight: normal; font-size: 10px;"><%= TDMDisplay %></td>	
									
									<%
									ResultSet rs315 = s8.executeQuery("select sum(sales) sum_converted,sum(target) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and tdm_id="+TDMID1+" and rsm_id="+RSMID1+" and snd_id="+SNDID1);
									if(rs315.first()){
										double CasesCovertedPercentage1=0;
										double SalesCoverted = rs315.getDouble("sum_converted");
										double TargetCoverted = rs315.getDouble("sum_targeted");
										
										if(TargetCoverted!=0){
											CasesCovertedPercentage1 = ((SalesCoverted * 1d) / (TargetCoverted * 1d) )*100;
										}
									%>
									
									
									<td style="text-align: right; background-color:#ECECDC;font-weight: normal; font-size: 10px;"><%if(SalesCoverted!=0){%><%=Utilities.getDisplayCurrencyFormat(SalesCoverted) %><%} %></td>
									<td style="text-align: right; background-color:#ECECDC;font-weight: normal; font-size: 10px;"><%if(TargetCoverted!=0){%><%=Utilities.getDisplayCurrencyFormat(TargetCoverted) %><%} %></td>
									<td style="text-align: right;background-color:#ECECDC;font-weight: normal; font-size: 10px;"><%if(CasesCovertedPercentage1!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage1) %>%<%} %></td>
							<%
									}
							ResultSet rs123 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in (SELECT distinct sap_code from dist_targets_temp dtt where dtt.order_date between "+Utilities.getSQLDate(MinTargetDate)+" and "+Utilities.getSQLDate(MaxTargetDate)+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
							while(rs123.next()){
								
								int TypeID3 = rs123.getInt("lrb_type_id");
								ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in (SELECT distinct sap_code from dist_targets_temp dtt where dtt.order_date between "+Utilities.getSQLDate(MinTargetDate)+" and "+Utilities.getSQLDate(MaxTargetDate)+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id="+rs123.getInt("lrb_type_id")+" order by package_sort_order");
								
								while(rs.next()){
								int unit_per_sku=0;
								PackageID=rs.getInt("package_id");
								//System.out.println("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and tdm_id="+TDMID1);
								
								ResultSet rs314 = s8.executeQuery("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and tdm_id="+TDMID1+" and rsm_id="+RSMID1+" and snd_id="+SNDID1);
								if(rs314.first()){
									double CasesPercentage1=0;
									double Sales = rs314.getDouble("sum_sales");
									double Target = rs314.getDouble("sum_target");
									
									if(Target!=0){
										CasesPercentage1 = ((Sales * 1d) / (Target * 1d) )*100;
									}
									
									
								%>
								
									
									<td style="text-align: right; background-color:#ECECDC;font-weight: normal; font-size: 10px;"><%if(Sales!=0){%><%=Utilities.getDisplayCurrencyFormat(Sales)%><%} %></td>
									<td style="text-align: right; background-color:#ECECDC;font-weight: normal; font-size: 10px;"><%if(Target!=0){%><%=Utilities.getDisplayCurrencyFormat(Target) %><%} %></td>									
									<td style="text-align: right;background-color:#ECECDC;font-weight: normal; font-size: 10px;"><%if(CasesPercentage1!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(CasesPercentage1) %>%<%} %></td>
								<%
								}
								}
							}
							
							
							%>
							</tr>
							
						
						
					<%
					
					String WhereTDMi = " and tdm_id="+TDMID1;
					if (TDMID1 == 0){
						WhereTDMi = " and tdm_id is null ";
					}
					
					ResultSet rs112 = s.executeQuery("select * from common_distributors where 1=1 "+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+" and distributor_id in ("+DistributorIDs+") and distributor_id in (select dt.distributor_id from distributor_targets dt where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y') and dt.type_id=3) "+WhereTDMi+" and rsm_id="+RSMID1+" and snd_id="+SNDID1); //distributor query
					while(rs112.next()){
						
						DistributorIDD = rs112.getLong("distributor_id");
						%>
						
						
					<tr>
						<td style="font-weight: normal; font-size: 9px;"><%= DistributorIDD+ " - "+ rs112.getString("name") %></td>	
						
						<%
						
						ResultSet rs314 = s8.executeQuery("select * from dist_targets_report_temp where package_id=-1 and type_id=-1 and distributor_id="+rs112.getLong("distributor_id"));
						while(rs314.next()){
							
							double SalesConvertedCases1 = rs314.getDouble("sales");
							double TargetConvertedCases1 = rs314.getDouble("target");
							int ConvertedCasesPercentage1 = rs314.getInt("percentage");
							
							%>
							<td style="text-align: right;"><%if(SalesConvertedCases1!=0){%><%=Utilities.getDisplayCurrencyFormat(SalesConvertedCases1) %><%} %></td>
							<td style="text-align: right;"><%if(TargetConvertedCases1!=0){%><%=Utilities.getDisplayCurrencyFormat(TargetConvertedCases1) %><%} %></td>
							<td style="text-align: right;<%if(ConvertedCasesPercentage1 == 0){%>abackground-color:#F6F6F6;<%} else if(ConvertedCasesPercentage1<40){%>abackground-color:#F6F6F6;<%} else if(ConvertedCasesPercentage1 >=40 && ConvertedCasesPercentage1 <70){%>abackground-color:#E9F0FF;<%} else if(ConvertedCasesPercentage1>=70 && ConvertedCasesPercentage1<90){%>abackground-color:#FFFFB2;<%} else if(ConvertedCasesPercentage1>=90){%>abackground-color:#C9FFC9;<%}%>"><%if(ConvertedCasesPercentage1!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesPercentage1) %>%<%} %></td>
						
							
							
							<%
						}
						
						
						%>
						
						
						<%
						
						ResultSet rs12 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in (SELECT distinct sap_code from dist_targets_temp dtt where dtt.order_date between "+Utilities.getSQLDate(MinTargetDate)+" and "+Utilities.getSQLDate(MaxTargetDate)+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
					while(rs12.next()){
						
						int TypeID = rs12.getInt("lrb_type_id");
						
						ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in (SELECT distinct sap_code from dist_targets_temp dtt where dtt.order_date between "+Utilities.getSQLDate(MinTargetDate)+" and "+Utilities.getSQLDate(MaxTargetDate)+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id="+rs12.getInt("lrb_type_id")+" order by package_sort_order");
						
						while(rs.next()){
						int unit_per_sku=0;
						PackageID=rs.getInt("package_id");
						
						ResultSet rs313 = s8.executeQuery("select * from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID+" and distributor_id="+rs112.getLong("distributor_id"));
						while(rs313.next()){
							
							
							
							
							double SalesRawCases1 = rs313.getDouble("sales");
							double target_sales_amount1 = rs313.getDouble("target");
							int percentage1 = rs313.getInt("percentage");
							
							%>
							<%if(DistributorIDD1!=DistributorIDD){ %>
								
							<%} DistributorIDD1=DistributorIDD; %>
							
							
							<td style="text-align: right;"><%if(SalesRawCases1!=0){%><%=  Utilities.getDisplayCurrencyFormatRounded(SalesRawCases1)  %><%} %></td> 
							<td style="text-align: right;"><%if(target_sales_amount1!=0){%><%= Utilities.getDisplayCurrencyFormatRounded(target_sales_amount1)%><%} %></td>
							<td style="text-align: right;<%if((int)percentage1 == 0){%>abackground-color:#F6F6F6;<%} else if((int)percentage1<40){%>abackground-color:#F6F6F6;<%} else if((int)percentage1 >=40 && (int)percentage1 <70){%>abackground-color:#E9F0FF;<%} else if((int)percentage1>=70 && (int)percentage1<90){%>abackground-color:#FFFFB2;<%} else if((int)percentage1>=90){%>abackground-color:#C9FFC9;<%}%>"><%if((int)percentage1 !=0){%><%=Utilities.getDisplayCurrencyFormatRounded(percentage1) %>%<%} %></td>
														
							
							<%
						}
						
						
						}
						
					}
					%>	
				
					</tr>		
				<%	
				
				
					}
					}//end of tdm loop
					}//end of RSM loop
				    }//end of SND loop
					
					%>
					
					
					
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
						 <td>S = Sale</td>
						 </tr>
						 <tr>
						 <td>T = Target</td>
						 </tr>
						 <tr>
						 <td>P = Percentage</td>
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