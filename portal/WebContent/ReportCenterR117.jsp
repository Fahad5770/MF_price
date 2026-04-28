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
int FeatureID = 117;

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
	WhereDistributors = " and distributor_id in ("+DistributorIDs+") ";
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
//
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
							
							<%							
							int PJPID=0;							
							%>
							
							<th data-priority="1"  style="text-align:center;" width="55%">Product</th>
							<th data-priority="1"  style="text-align:center;" width="15%">Backorders</th>														
							<th data-priority="1"  style="text-align:center;" width="15%">Cases</th>
							<th data-priority="1"  style="text-align:center;" width="15%">Amount*</th>
													
					    </tr>
					  </thead> 
					<tbody>
						<%
						
						//ResultSet rs1 = s.executeQuery("SELECT id, label, distributor_id FROM distributor_beat_plan where 1=1 "+WhereHOD+WhereRSM+WhereDistributors);
						//System.out.println("SELECT distinct id, label FROM distributor_beat_plan_view  where 1=1 "+WhereHOD+WhereRSM+WhereDistributors);
						//
						//System.out.println("SELECT mopb.product_id, ipv.package_label, ipv.brand_label, sum(mopb.total_units) total_units,ipv.unit_per_sku FROM mobile_order mo join mobile_order_products_backorder mopb on mo.id = mopb.id join inventory_products_view ipv on mopb.product_id = ipv.product_id where mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereDistributors+" group by mopb.product_id order by total_units desc");
						//ResultSet rs1 = s.executeQuery("SELECT mopb.product_id, ipv.package_label, ipv.brand_label, sum(mopb.total_units) total_units,ipv.unit_per_sku, sum(mopb.total_units/ipv.unit_per_sku) order_total FROM mobile_order mo join mobile_order_products_backorder mopb on mo.id = mopb.id join inventory_products_view ipv on mopb.product_id = ipv.product_id where mo.backordered_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+" group by mopb.product_id order by order_total desc");
						//ResultSet rs1 = s.executeQuery("SELECT mopb.product_id, ipv.package_label, ipv.brand_label, sum(mopb.total_units) total_units,ipv.unit_per_sku FROM mobile_order mo join mobile_order_products_backorder mopb on mo.id = mopb.id join inventory_products_view ipv on mopb.product_id = ipv.product_id where mo.created_on between '2014-04-01' and '2014-04-02' group by mopb.product_id order by total_units desc;");
						
						double total_amount = 0;
						double total_backorders = 0;
						ResultSet rs1 = s.executeQuery("select mopb.product_id, ipv.package_label, ipv.brand_label, sum(mopb.total_units) units, sum(if(mop.is_promotion = 1,0,mopb.total_units * mop.rate_units)) amount, sum(mopb.total_units/ipv.unit_per_sku) order_total, ipv.unit_per_sku, count(distinct mopb.id) orders_count from mobile_order_products mop join mobile_order_products_backorder mopb on mop.id = mopb.id and mop.product_id = mopb.product_id join inventory_products_view ipv on mopb.product_id = ipv.product_id where mopb.id in ("+
								"select id from mobile_order where backordered_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+""+
								") group by mopb.product_id order by order_total desc");
						
						while(rs1.next()){
							total_amount += rs1.getDouble("amount");
							total_backorders += rs1.getDouble("orders_count");
						%>
						<tr>
							<td><%=rs1.getString("package_label") %> - <%=rs1.getString("brand_label") %></td>	
							<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(rs1.getInt("orders_count")) %></td>
							<td style="text-align: right;"><%=Utilities.convertToRawCases(rs1.getLong("units"),rs1.getInt("unit_per_sku"))%></td>	 
							<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("amount"))%></td>
						</tr>
						<%
						}
						%>
						<tr>
							<td>Total</td>	
							<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(total_backorders)%></td>
							<td style="text-align: right;"></td>	 
							<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormatRounded(total_amount)%></td>
						</tr>
						
						
					</tbody>
							
				</table>
				
		</td>
	</tr>
</table>
<br><div style="font-weight: normal; font-size: 10px;">* Excluding Promotions</div>
	</li>	
</ul>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>