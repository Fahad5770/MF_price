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
int FeatureID = 213;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	//response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

Datasource ds2 = new Datasource();
ds2.createConnectionToSAPDB();
Statement sap = ds2.createStatement();
Statement sap2 = ds2.createStatement();
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
	WhereDistributors = " and cd.distributor_id in ("+DistributorIDs+") ";
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
	WhereSM = " and cd.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
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
String WhereCustomerID ="";
if (CustomerID != 0){
	WhereCustomerID = " and gotc.customer_id in ("+CustomerID+") ";	
}


long DistributorID = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
	DistributorID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
}
String WhereDistributorID ="";
if (DistributorID != 0){
	WhereDistributorID = " and distributor_id in ("+DistributorID+") ";	
}
           
  //System.out.println("Hello --- "+WhereDistributorID);        
%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Upfront Discount</li>
<li>

<%






//sap.executeUpdate("drop table temp_invoice_list");
sap.executeUpdate("create global temporary table temp_invoice_list(invoice_no VARCHAR2(30 BYTE))");

s.executeUpdate("create temporary table temp_upfront_discount (vbeln bigint(10), distributor_id bigint(10), matnr int(10), discount double)");

ds2.startTransaction();

ResultSet rs1 = s.executeQuery("SELECT invoice_no FROM inventory_delivery_note where created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate) +WhereDistributorID);
while(rs1.next()){
	sap.executeUpdate("insert into temp_invoice_list values ( '"+ Utilities.addLeadingZeros(rs1.getString("invoice_no"), 10)+"' ) ");
}


double TotalDiscount = 0;
ResultSet rsSAP = sap2.executeQuery("select vbrk.vbeln, vbrk.fkart, vbrk.knumv, vbrp.posnr, vbrk.kurrf_dat, vbrp.pstyv, vbrp.matnr, vbrp.fkimg quantity, (vbrp.kzwi1+ /*vbrp.kzwi2 + vbrp.kzwi3 +*/ vbrp.kzwi6) gross_value, vbrp.kzwi4 unloading, vbrp.kzwi5 freight, vbrp.vrkme, vbrk.kunrg from sapsr3.vbrp vbrp join sapsr3.vbrk on vbrk.vbeln = vbrp.vbeln where vbrk.fkart in ('ZDIS','ZMRS','ZDFR') and vbrk.vbeln in (select invoice_no from temp_invoice_list) order by vbrk.vbeln");
while(rsSAP.next()){
	
	double UpfrontDiscount = 0;
	ResultSet rs4 = s.executeQuery("select kbetr from sap_konv where knumv = "+rsSAP.getLong("knumv")+" and kposn = "+rsSAP.getInt("posnr"));
	if (rs4.first()){
		UpfrontDiscount = rs4.getDouble(1);
	}
	UpfrontDiscount = UpfrontDiscount * rsSAP.getInt("quantity");
	
	if (UpfrontDiscount != 0){
		UpfrontDiscount = UpfrontDiscount * -1;
		s2.executeUpdate("insert into temp_upfront_discount values ("+rsSAP.getLong("vbeln")+","+rsSAP.getLong("kunrg")+","+rsSAP.getLong("matnr")+","+UpfrontDiscount+" )");
	}
	
	TotalDiscount += UpfrontDiscount;
	
}

//out.println(TotalDiscount);
%>

<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 
					
						
						<thead>
					    

					    <tr style="font-size:11px;">
					    							
							<th style="width: 20%;">Customer Name</th>
							<%
							int PackageCount = 0;
							
							
							ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view ipv where ipv.category_id = 1 and ipv.sap_code in (select distinct sap_code from temp_upfront_discount) ");
							while(rs2.next()){
							
								PackageCount++;
							
							%>
							<th data-priority="1"  style="text-align:center; "><%=rs2.getString("package_label")%></th>
							<%
							}
							long PackageTotal[] = new long[PackageCount];
							
							for (int i = 0; i < PackageTotal.length; i++){
								PackageTotal[i] = 0;
								
							}
							%>
							<th>Amount</th>
										
					    </tr>
					    
					  </thead> 
						
						<tbody>
						<%
						double Total=0;
						double RowWiseTotal=0;
						double GrandTotal=0;
						
						ResultSet rs = s.executeQuery("SELECT cd.distributor_id, cd.name FROM common_distributors cd where cd.distributor_id in (select distinct distributor_id from temp_upfront_discount) "+WhereHOD+WhereRSM);
						
						while(rs.next()){
							
							%>
							<tr>
							<td><%=rs.getString("distributor_id") %><%if(rs.getString("name")!=null){ %> - <%=rs.getString("name") %><%} %></td>							
							<%
							int PackageIndex = 0;
							ResultSet rs4 = s3.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view ipv where ipv.category_id = 1 and ipv.sap_code in (select distinct sap_code from temp_upfront_discount)");
							while(rs4.next()){
								
								double DiscountAmount = 0;
								ResultSet rs3 = s2.executeQuery("select sum(tud.discount) from temp_upfront_discount tud join inventory_products ip on tud.matnr = ip.sap_code where tud.distributor_id = "+rs.getString("distributor_id")+" and ip.package_id = "+rs4.getInt("package_id"));
								if(rs3.first()){
									DiscountAmount = rs3.getDouble(1);
									PackageTotal[PackageIndex] += DiscountAmount;
									RowWiseTotal += DiscountAmount;
								}
								
								%>
									<td style="text-align:right;"><%if(DiscountAmount!=0){%><%= Utilities.getDisplayCurrencyFormat(DiscountAmount) %><%} %></td>
							<%
							PackageIndex++;
							
							}
							
							
						%>
						<td style="text-align:right;"><%if(RowWiseTotal!=0){%><%=Utilities.getDisplayCurrencyFormat(RowWiseTotal) %><%} %></td>
						</tr>
						<%
						GrandTotal += RowWiseTotal;
						RowWiseTotal=0;
						}
						%>
						<tr>
						<td>Total</td>
						<%
						
						for (int i = 0; i < PackageTotal.length; i++){
							
							%>
							<td style="text-align:right;"><%if(PackageTotal[i]!=0){%><%=Utilities.getDisplayCurrencyFormat(PackageTotal[i])%><%} %></td> 
						<%
						}
						%>
						<td style="text-align:right;"><%if(GrandTotal!=0){%><%=Utilities.getDisplayCurrencyFormat(GrandTotal)%><%} %></td>
						</tr>
					</tbody>
							
				</table>
		</td>
	</tr>
</table>

	</li>	
</ul>

<%
ds2.commit();
sap.executeUpdate("drop table temp_invoice_list");

sap.close();

s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
ds2.dropConnection();
%>