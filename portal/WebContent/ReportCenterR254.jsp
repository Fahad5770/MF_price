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
int FeatureID = 318;

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
Statement s6 = c.createStatement();
Statement s7 = c.createStatement();
//Date date = Utilities.parseDate(request.getParameter("Date"));


Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");
//StartDate=Utilities.parseDate("24/09/2015");
//EndDate=Utilities.parseDate("25/09/2015");

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

long SelectedBrandsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedBrands") != null){
   	SelectedBrandsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedBrands");           	
}

String BrandIDs = "";
String WhereBrand = "";

if(SelectedBrandsArray!= null && SelectedBrandsArray.length > 0){
	for(int i = 0; i < SelectedBrandsArray.length; i++){
		if(i == 0){
			BrandIDs += SelectedBrandsArray[i]+"";
		}else{
			BrandIDs += ", "+SelectedBrandsArray[i]+"";
		}
	}
	WhereBrand = " and ipv.brand_id in ("+BrandIDs+") ";
}

//System.out.println(WhereBrand);

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


Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);

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


//Lifting Type 


String SelectedLiftingTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedLiftingType") != null){
	SelectedLiftingTypeArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedLiftingType");
}

String WhereLiftingType = "";
if(SelectedLiftingTypeArray!=null){
	for(int i=0;i<SelectedLiftingTypeArray.length;i++){
		if(SelectedLiftingTypeArray[i].equals("Internal")){
			WhereLiftingType = " and idn.outsourced_primary_sales_id is null ";
		}else if(SelectedLiftingTypeArray[i].equals("Other Plant")){
			WhereLiftingType = " and idn.outsourced_primary_sales_id is not null ";
		}
	}
}


//Pallatize
String PalletizeTypes="";
long SelectedPalletizeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPalletize") != null){
	SelectedPalletizeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPalletize");
	PalletizeTypes = Utilities.serializeForSQL(SelectedPalletizeArray);
	//System.out.println("palateize type"+PalletizeTypes);
}

String WherePalletizeType = "";
if (PalletizeTypes.length() > 0){
	WherePalletizeType = " and idn.palletize_type_id in ("+PalletizeTypes+") ";	
	//System.out.println(WherePalletizeType);
}


//System.out.println("Hello "+WhereLiftingType);

///

///

String ProductsLifted ="-1";
ResultSet rs41 = s.executeQuery("SELECT group_concat(distinct product_id) FROM inventory_delivery_note i_idn join inventory_delivery_note_products i_idnp on i_idn.delivery_id = i_idnp.delivery_id where created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+"");
if (rs41.first()){
	ProductsLifted = rs41.getString(1);
}
int DataFlag=0;
//System.out.println("SELECT distinct package_id, package_label FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id=ipv.product_id where  ipv.product_id in ("+ProductsLifted+") "+WherePalletizeType+" and category_id = 1  order by package_sort_order");
/////String CheckQuery="SELECT distinct package_id, package_label FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id=ipv.product_id where  ipv.product_id in ("+ProductsLifted+") "+WherePalletizeType+" and category_id = 1  order by package_sort_order";
///ResultSet rs11 = s.executeQuery(CheckQuery);
///if(!rs11.next()){
	//DataFlag=1;
////} 



String WarehouseIDs="";

           long SelectedWarehouseArray[] = null;
          
           if (session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse") != null){
           	SelectedWarehouseArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse");
           	
           	//check for scope warehouse
           	
           	UserAccess u = new UserAccess();
           	Warehouse[] WarehouseList = u.getUserFeatureWarehouse(SessionUserID,FeatureID);
           			           			
           	
          
           	WarehouseIDs = Utilities.serializeForSQL(SelectedWarehouseArray);
           
           }else{
        	   //else getting scope warehouse
        	   UserAccess u = new UserAccess();
               Warehouse[] WarehouseList = u.getUserFeatureWarehouse(SessionUserID,FeatureID);
        	   WarehouseIDs = u.getWarehousesQueryString(WarehouseList);  
           }
           
           
           String WhereWarehouse = "";
          
           if (WarehouseIDs.length() > 0){
           	WhereWarehouse = " and idn.warehouse_id in ("+WarehouseIDs+") ";	
           }
           



%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Stock Dispatching Report</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
		<%if(DataFlag==0){ 
			%>
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					   <tr style="font-size:11px;">
					   		<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
						   	<th data-priority="1"  style="text-align:center; "></th>
						   	 	<th data-priority="1"  style="text-align:center; "></th>
							
							
							<%
							
							//String[] DiscountTitle= {"Gross Revenue","","","","","","","","","","",""};
							
							//System.out.println(" "+"SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  product_id in ("+ProductsLifted+") and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							ResultSet rs21 = s.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  product_id in ("+ProductsLifted+") and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs21.next()){
								%>
								<th data-priority="1"  style="text-align:center; " colspan="<%=rs21.getInt("package_count")%>"><%=rs21.getString("type_name") %></th>
								<%
							}
							
							%>
												
					    </tr>
					   
					   
					    <tr style="font-size:11px;">
							
							  <th data-priority="1"  style="text-align:center; ">Distributor</th>
		            		 <th data-priority="1"  style="text-align:center;">Vehicle</th>
		            		 <th data-priority="1"  style="text-align:center;" >Sale Order</th>  
							
							
							<%
							int PackageCount = 0;
							
						
							int ArrayCount=0;
							//System.out.println("Upper 1st Query"+"SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  product_id in ("+ProductsLifted+") and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							ResultSet rs12 = s.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  product_id in ("+ProductsLifted+") and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs12.next()){
								//System.out.println("Upper2nd Query"+"SELECT distinct package_id, package_label FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id=ipv.product_id where  ipv.product_id in ("+ProductsLifted+") "+WherePalletizeType+" and category_id = 1 and lrb_type_id="+rs12.getLong("lrb_type_id")+" order by package_sort_order");
								ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id=ipv.product_id where  ipv.product_id in ("+ProductsLifted+")  and category_id = 1 and lrb_type_id="+rs12.getLong("lrb_type_id")+" order by package_sort_order");
																
								
								
								while(rs2.next()){
							%>
								<th data-priority="1"  style="text-align:center; "><%=rs2.getString("package_label") %></th>
							<%
							ArrayCount++;
							PackageCount++;
								}
							}
							%>
							
							<%
							long PackageTotal[] = new long[PackageCount];
							int PackageTotalUnitPerSKU[] = new int[PackageCount];
							for (int i = 0; i < PackageTotal.length; i++){
								PackageTotal[i] = 0;
								PackageTotalUnitPerSKU[i]=0;
							}
							
							//System.out.println(PackageCount);
							%>							
					    </tr>
					  </thead> 
						<tr>
					<!-- 	//////////////////////////////////////////////////////     -->
					
						 	  <% 
		           
		            int DistributorID=0;
		            String DistributorName=null;
		            String VehcileNumber= null;
					long SapOrder=0;
					
		            String Query2="SELECT distinct (distributor_id),(select name from common_distributors cd where cd.distributor_id=idn.distributor_id) name,idn.vehicle_no,sap_order_no FROM pep.inventory_delivery_note idn  where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WherePalletizeType+" "+WhereWarehouse;
					//System.out.println(Query2);
		            ResultSet rs1 = s3.executeQuery(Query2);
					while(rs1.next()){
						
						DistributorID = rs1.getInt("distributor_id");
						DistributorName= rs1.getString("name");
						VehcileNumber= rs1.getString("vehicle_no");
						SapOrder= rs1.getLong("sap_order_no");
						int PackageIndex = 0;  
						
						%>
					<tr>
		         		<td> <%=DistributorID%>-<%=DistributorName %>  </td>
		         		<td> <%=VehcileNumber %> </td>
		         		<td> <%=SapOrder %> </td>
						 
						
						
						<!-- Patch -->
					
						
						<%
							
							//System.out.println("Query resultSet 31 "+"SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  product_id in ("+ProductsLifted+") and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							ResultSet rs31 = s4.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  product_id in ("+ProductsLifted+") and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs31.next()){	
							long lrbType=rs31.getLong("lrb_type_id");
								
								//System.out.println("Query ResultSet 4 "+"SELECT distinct package_id, package_label FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id=ipv.product_id where  ipv.product_id in ("+ProductsLifted+") "+WherePalletizeType+" and category_id = 1 and lrb_type_id="+lrbType+" order by package_sort_order");
								ResultSet rs4 = s5.executeQuery("SELECT distinct package_id, package_label FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id=ipv.product_id where  ipv.product_id in ("+ProductsLifted+")  and category_id = 1 and lrb_type_id="+lrbType+" order by package_sort_order");
								while(rs4.next()){
							
							
									
									
									int packID=rs4.getInt("package_id");
						
						
							String Query4="SELECT sum(idnp.total_units/ipv.unit_per_sku)  sum1,ipv.unit_per_sku FROM pep.inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id=ipv.product_id where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and package_id in ("+packID+") and lrb_type_id="+lrbType+" and idn.distributor_id="+DistributorID+" and idn.sap_order_no="+SapOrder+" and ipv.category_id=1 order by idn.delivery_id desc;";
							//System.out.println(Query4);	
							
							//String Query4="SELECT sum(idnp.total_units/ipv.unit_per_sku)  sum1,ipv.unit_per_sku FROM pep.inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id=idnp.delivery_id join inventory_products_view ipv on idnp.product_id=ipv.product_id where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and package_id in ("+packID+") and lrb_type_id="+rs31.getLong("lrb_type_id")+" and idn.distributor_id="+DistributorID+" and idn.sap_order_no="+SapOrder+" and ipv.category_id=1 order by idn.delivery_id desc;";
							//System.out.println(Query4);
							ResultSet rs5 = s6.executeQuery(Query4);
							long qty=0;
							if(rs5.first()){	
								//System.out.println(rs3.getLong("unit_per_sku"));
								 qty = rs5.getLong("sum1");
								PackageTotal[PackageIndex] += qty;
								//System.out.println(rs3.getInt("sum1") );
							
		            %> 
		            
		           		
		           		
		           		
		           	  
		           	  <%
		           			PackageIndex++;
		           	 // System.out.println("PackageIndex"+PackageIndex);
								}/* s4 while loop ends here  */
						%>
							<td><%if(qty!=0){%><%=qty %><%} %></td>
							<%
								}	
							}
							%>
						
						
						
						
						<!-- patch -->
						
						
						
						
					 </tr>
					<%		
							
					} /* s2 while loop ends here  */
		             %>	 
						
						<!--  /////////////////////////////////////////////////////    -->
						 <tr>
								<td style="font-weight:bold"></td>
								<td style="font-weight:bold">Total</td>
								<td style="font-weight:bold"></td>
								<%
								
								for (int i = 0; i < PackageTotal.length; i++){
									
									%>
									<td ><%=PackageTotal[i]%></td> 
								<%
								}
								%>
					</tr>
						
						
						</tr>
							
				</table>
		</td>
	</tr>
</table>
<%
		}else{
			%>
			<span style="font-size:13px; text-decoration:none; padding-top:5px;">No Data is Available.</span>
			<% 		
			}
			%>		

	</li>	
</ul>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>