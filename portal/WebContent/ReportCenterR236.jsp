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
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="java.util.Calendar"%>


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
int FeatureID = 297;





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
	WhereHOD = " and isap.cache_distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and isap.cache_distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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
	WhereDistributors = " and isap.cache_distributor_id in ("+DistributorIDs+") ";
	
	
	//out.print(WhereDistributors);
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
	WhereSM = " and isap.cache_distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
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
	WhereTDM = " and cd.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
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
	WhereASM = " and cd.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
}


//Sales Type


String AccountTypeIDs="";
long SelectedAccountTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedAccountType") != null){
	SelectedAccountTypeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedAccountType");
	AccountTypeIDs = Utilities.serializeForSQL(SelectedAccountTypeArray);
}

String WhereSalesType = "";
if (AccountTypeIDs.length() > 0){
	WhereSalesType = " and type_id in ("+AccountTypeIDs+")";	
}

long CustomerID = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedCustomerID") != null){
	CustomerID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedCustomerID");
}


//Transaction Account


String CashInstrumentsIDs="";
long SelectedCashInstrumentsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedCashInstruments") != null){
	SelectedCashInstrumentsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedCashInstruments");
	CashInstrumentsIDs = Utilities.serializeForSQL(SelectedCashInstrumentsArray);
}

String WhereCashInstruments = " and account_id=''";
if (CashInstrumentsIDs.length() > 0){
	WhereCashInstruments = " and  account_id in ("+CashInstrumentsIDs+") ";	
}

//


String WarehouseIDs="";
String WarehouseIDs1="";
           long SelectedWarehouseArray[] = null;
           long SelectedWarehouseArray1[] = null;
           if (session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse") != null){
           	SelectedWarehouseArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse");
           	
           	//check for scope warehouse
           	
           	UserAccess u = new UserAccess();
           	Warehouse[] WarehouseList = u.getUserFeatureWarehouse(SessionUserID,FeatureID);
           			           			
           	
            WarehouseIDs1 = u.getWarehousesQueryString(WarehouseList); 
           	WarehouseIDs = Utilities.serializeForSQL(SelectedWarehouseArray1);
           
           }else{
        	   //else getting scope warehouse
        	   UserAccess u = new UserAccess();
               Warehouse[] WarehouseList = u.getUserFeatureWarehouse(SessionUserID,FeatureID);
        	   WarehouseIDs = u.getWarehousesQueryString(WarehouseList);  
           }
           
           
           String WhereWarehouse = "";
           String WhereWarehouse1 = "";
           if (WarehouseIDs.length() > 0){
           	WhereWarehouse = " and glcr.warehouse_id in ("+WarehouseIDs+") ";	
           }
           if (WarehouseIDs1.length() > 0){
              	WhereWarehouse1 = " and glcr.warehouse_id in ("+WarehouseIDs1+") ";	
              }
           
           
         //Gl Employee


           String GlEmployeeIDs="";
           long SelectedGlEmployeeArray[] = null;
           if (session.getAttribute(UniqueSessionID+"_SR1SelectedGlEmployee") != null){
           	SelectedGlEmployeeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedGlEmployee");
           	GlEmployeeIDs = Utilities.serializeForSQL(SelectedGlEmployeeArray);
           }

           String WhereGlEmployee = "";
           if (GlEmployeeIDs.length() > 0){
           	WhereGlEmployee = " and glcr.created_by="+GlEmployeeIDs;	
           }        
           
           

String BrandIDs = "1,7,4,10,42,2,14,12,16,23,5";           
           
%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
				<thead>
					  <tr style="font-size:11px;">
						  <th style="width:10%; text-align:center;"># of Outlets</th>
						    <th style="width:6%;text-align:center; background-color:#fafaf8">Total</th>
						   <%
						   ResultSet rs = s.executeQuery("SELECT distinct brand_id , (select label from inventory_brands ib where ib.id=brand_id) brand_label FROM pep.inventory_products where brand_id in("+BrandIDs+")");
						   while(rs.next()){
						   %>
						   <th style="width:6%;text-align:center;"><%=rs.getString("brand_label") %></th>
						   
						   <%
						   }
						   %>
						   <th style="width:6%;text-align:center; background-color:#ffffe6;">All CSD*</th>
						   <th style="width:6%;text-align:center; background-color:#f7fff7;">All LRB*</th>
						  
					</tr>
					    
					  </thead> 
						<tbody>
						
						<%
						ResultSet rs1 = s.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM pep.inventory_products where package_id in(12,9,28,6,3,2,24,5,30,16)");
						while(rs1.next()){
							
							
							long PackageID=rs1.getLong("package_id");
						%>
							<tr>
								
								<td><%=rs1.getString("package_label") %></td>
								<%
								long OutletTotal = 0;
								ResultSet rs31 = s3.executeQuery("SELECT count(distinct isa.outlet_id) outletcount FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id where isap.cache_package_id="+PackageID+" and isap.cache_brand_id in("+BrandIDs+") and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" "+WhereDistributors+WhereHOD+WhereRSM);
								if(rs31.first()){
									OutletTotal=rs31.getLong("outletcount");
								}
								%>

								<td style="text-align:center;background-color:#fafaf8;"><%if(OutletTotal!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(OutletTotal)%><%} %></td>
								
							
								<%
								ResultSet rs2 = s2.executeQuery("SELECT distinct brand_id , (select label from inventory_brands ib where ib.id=brand_id) brand_label FROM pep.inventory_products where brand_id in("+BrandIDs+")");
								while(rs2.next()){
									
									long BrandID=rs2.getLong("brand_id");
									
									ResultSet rs3 = s3.executeQuery("SELECT count(distinct outlet_id) outletcount FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id where isap.cache_package_id="+PackageID+" and isap.cache_brand_id="+BrandID+" and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" "+WhereDistributors+WhereHOD+WhereRSM);
									while(rs3.next()){
										
										
									%>
									
									<td style="text-align:center;"><%if(rs3.getLong("outletcount")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs3.getLong("outletcount")) %><%} %></td>		
									
									<%} %>
									
							
						
								<%
								}
								
								//All CSD
								long AllCSDCount = 0;
								ResultSet rs4 = s3.executeQuery("SELECT count(distinct outlet_id) outletcount FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id where isap.cache_package_id="+PackageID+" and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and cache_lrb_type_id=1 and isap.cache_brand_id in ("+BrandIDs+") and isap.cache_brand_id not in(7,10,2,42,12,16,23) "+WhereDistributors+WhereHOD+WhereRSM);
									if(rs4.first()){
										AllCSDCount = rs4.getLong("outletcount");
									}
									%>
										<td style="text-align:center;background-color:#ffffe6;"><%if(AllCSDCount!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(AllCSDCount) %><%} %></td>
									
								
								<%
							//All LRB
								long AllLRBCount = 0;
								ResultSet rs5 = s3.executeQuery("SELECT count(distinct outlet_id) outletcount FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id where isap.cache_package_id="+PackageID+" and isap.cache_order_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isap.cache_brand_id in ("+BrandIDs+") and isap.cache_brand_id not in(7,10,2,42,12,16,23) "+WhereDistributors+WhereHOD+WhereRSM);
									if(rs5.first()){
										AllLRBCount = rs5.getLong("outletcount");
									}
										%>

										<td style="text-align:center;background-color:#f7fff7;"><%if(AllLRBCount!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(AllLRBCount) %><%} %></td>
						</tr>
						<%
						}
						%>
						<tr>
							<td>&nbsp;</td>
						</tr>	
							
						</tbody>
							
				</table>
				<!--  Number of Invoices -->
				<%if(1==1){ %>
				
				<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
				<thead>
					  <tr style="font-size:11px;">
						  <th style="width:10%; text-align:center;"># of Invoices</th>
						   <th style="width:6%;text-align:center;background-color:#fafaf8;">Total</th>
						   <%
						   ResultSet Irs = s.executeQuery("SELECT distinct brand_id , (select label from inventory_brands ib where ib.id=brand_id) brand_label FROM pep.inventory_products where brand_id in("+BrandIDs+")");
						   while(Irs.next()){
						   %>
						   <th style="width:6%;text-align:center;"><%=Irs.getString("brand_label") %></th>
						   
						   <%
						   }
						   %>
						   <th style="width:6%;text-align:center;background-color:#ffffe6;">All CSD*</th>
						   <th style="width:6%;text-align:center;background-color:#f7fff7;">All LRB*</th>
						  
					</tr>
					    
					  </thead> 
						<tbody>
						
						<%
						ResultSet rs12 = s.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM pep.inventory_products where package_id in(12,9,28,6,3,2,24,5,30,16)");
						while(rs12.next()){
							
							
							
						%>
							<tr>
								
								<td><%=rs12.getString("package_label") %></td>
								<%
								double TotalInvoiceCount= 0;
								ResultSet rs31 = s3.executeQuery("SELECT count(distinct isap.id) totalinvoices FROM  inventory_sales_adjusted_products isap  where isap.cache_package_id="+rs12.getLong("package_id")+" and isap.cache_brand_id in("+BrandIDs+") and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" "+WhereDistributors+WhereHOD+WhereRSM);
								if(rs31.first()){
									TotalInvoiceCount = rs31.getLong("totalinvoices");
								}
								%>
								
								<td style="text-align:center;background-color:#fafaf8;"><%if(TotalInvoiceCount!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(TotalInvoiceCount)%><%} %></td>
								
								
								<%
								ResultSet rs2 = s2.executeQuery("SELECT distinct brand_id , (select label from inventory_brands ib where ib.id=brand_id) brand_label FROM pep.inventory_products where brand_id in("+BrandIDs+")");
								while(rs2.next()){
									
									long BrandID=rs2.getLong("brand_id");
									
									ResultSet rs3 = s3.executeQuery("SELECT count(distinct isap.id) outletcount FROM  inventory_sales_adjusted_products isap  where isap.cache_package_id="+rs12.getLong("package_id")+" and isap.cache_brand_id="+BrandID+" and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" "+WhereDistributors+WhereHOD+WhereRSM);
									while(rs3.next()){
										
										
									%>
									
									<td style="text-align:center;"><%if(rs3.getLong("outletcount")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs3.getLong("outletcount")) %><%} %></td>		
									
									<%} %>
									
							
						
								<%
								}
								
								//All CSD
								long CSDInvoiceCount = 0;
								ResultSet rs4 = s3.executeQuery("SELECT count(distinct isap.id) invoicecsd FROM  inventory_sales_adjusted_products isap  where isap.cache_package_id="+rs12.getLong("package_id")+" and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and cache_lrb_type_id=1 and isap.cache_brand_id in ("+BrandIDs+") and isap.cache_brand_id not in(7,10,2,42,12,16,23) "+WhereDistributors+WhereHOD+WhereRSM);
									if(rs4.first()){
										CSDInvoiceCount = rs4.getLong("invoicecsd");
									}
										%>
									
										<td style="text-align:center;background-color:#ffffe6;"><%if(CSDInvoiceCount!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(CSDInvoiceCount) %><%} %></td>
								
								
								<%
							//All LRB
								long LRBInvoiceCount = 0;
								ResultSet rs5 = s3.executeQuery("SELECT count(distinct isap.id) invoicelrb FROM  inventory_sales_adjusted_products isap  where isap.cache_package_id="+rs12.getLong("package_id")+" and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isap.cache_brand_id not in(7,10,2,42,12,16,23) and isap.cache_brand_id in ("+BrandIDs+") "+WhereDistributors+WhereHOD+WhereRSM);
									if(rs5.first()){
										LRBInvoiceCount = rs5.getLong("invoicelrb");
									}
										%>

										<td style="text-align:center;background-color:#f7fff7;"><%if(LRBInvoiceCount!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(LRBInvoiceCount) %><%} %></td>
								
								
								
								
						</tr>
						<%
						}
						%>
						<tr>
							<td>&nbsp;</td>
						</tr>	
							
						</tbody>
							
				</table>
				
				<%} %>
				<!-- Volume -->
				
				<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
				<thead>
					  <tr style="font-size:11px;">
						  <th style="width:10%;text-align:center;">Volume(r/c)</th>
						  <th style="width:6%;text-align:center;background-color:#fafaf8;">Total</th>
						   <%
						   ResultSet Vrs = s.executeQuery("SELECT distinct brand_id , (select label from inventory_brands ib where ib.id=brand_id) brand_label FROM pep.inventory_products where brand_id in("+BrandIDs+")");
						   while(Vrs.next()){
						   %>
						   <th style="width:6%;text-align:center;"><%=Vrs.getString("brand_label") %></th>
						   
						   <%
						   }
						   %>
						   <th style="width:6%;text-align:center;background-color:#ffffe6;">All CSD*</th>
						   <th style="width:6%;text-align:center;background-color:#f7fff7;">All LRB*</th>
						   
					</tr>
					    
					  </thead> 
						<tbody>
						<%
						ResultSet Vrs1 = s.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM pep.inventory_products where package_id in(12,9,28,6,3,2,24,5,30,16)");
						while(Vrs1.next()){
						%>
							<tr>
							
								<td><%=Vrs1.getString("package_label") %></td>
								<%
								double TotalRawCases = 0;
								ResultSet rs31 = s3.executeQuery("SELECT sum(total_units/cache_units_per_sku) totalvol FROM  inventory_sales_adjusted_products isap  where isap.cache_package_id="+Vrs1.getLong("package_id")+" and isap.cache_brand_id in("+BrandIDs+") and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" "+WhereDistributors+WhereHOD+WhereRSM);
								if(rs31.first()){
									TotalRawCases = rs31.getDouble("totalvol");
								}
								%>
								
								<td style="text-align:center;background-color:#fafaf8;"><%if(TotalRawCases!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(TotalRawCases)%><%} %></td>
								
								<%
								ResultSet Vrs2 = s2.executeQuery("SELECT distinct brand_id , (select label from inventory_brands ib where ib.id=brand_id) brand_label FROM pep.inventory_products where brand_id in("+BrandIDs+")");
								while(Vrs2.next()){
									
									ResultSet rs3 = s3.executeQuery("SELECT sum(total_units/cache_units_per_sku) vol FROM  inventory_sales_adjusted_products isap  where isap.cache_package_id="+Vrs1.getLong("package_id")+" and isap.cache_brand_id="+Vrs2.getLong("brand_id")+" and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" "+WhereDistributors+WhereHOD+WhereRSM);
									while(rs3.next()){
										
										
									%>
									
									<td style="text-align:center;"><%if(rs3.getDouble("vol")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs3.getDouble("vol")) %><%} %></td>		
									
									<%} %>
									
							
						
								<%
								
								}
								%>
								<%
								//All CSD
								double CSDRawCases = 0;
								ResultSet rs4 = s3.executeQuery("SELECT sum(total_units/cache_units_per_sku) volcsd FROM  inventory_sales_adjusted_products isap  where isap.cache_package_id="+Vrs1.getLong("package_id")+" and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and cache_lrb_type_id=1 and isap.cache_brand_id in ("+BrandIDs+") and isap.cache_brand_id not in(7,10,2,42,12,16,23) "+WhereDistributors+WhereHOD+WhereRSM);
									if(rs4.first()){
										CSDRawCases = rs4.getDouble("volcsd");
									}
										%>

										<td style="text-align:center;background-color:#ffffe6;"><%if(CSDRawCases!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(CSDRawCases) %><%} %></td>
									<%
							//All LRB
								double LRBRawCases = 0;
								ResultSet rs5 = s3.executeQuery("SELECT sum(total_units/cache_units_per_sku) vollrb FROM  inventory_sales_adjusted_products isap  where isap.cache_package_id="+Vrs1.getLong("package_id")+" and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isap.cache_brand_id not in(7,10,2,42,12,16,23) and isap.cache_brand_id in ("+BrandIDs+") "+WhereDistributors+WhereHOD+WhereRSM);
									if(rs5.first()){
										LRBRawCases = rs5.getDouble("vollrb");
									}
										%>

										<td style="text-align:center;background-color:#f7fff7;"><%if(LRBRawCases!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(LRBRawCases) %><%} %></td>
								
						</tr>
						<%
						}
						%>
						<tr>
							<td>&nbsp;</td>
						</tr>	
							
						</tbody>
							
				</table><br>
				* All Excludes Diet P, 7up Free, Mirinda, MD PB, AF, Slice M, Slice O, Slice A
				
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