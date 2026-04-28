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
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Calendar"%>
 
 
 <script src="js/jquery.excoloSlider.min.js"></script>
<link href="css/jquery.excoloSlider.css" rel="stylesheet">
 
<script>

function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}

function runSlider(SliderID){
	//alert(SliderID);
	
	
	if($("#IsSliderAlreadyClicked_"+SliderID).val()=="0"){
		$("#slider_"+SliderID).excoloSlider();
		$("#IsSliderAlreadyClicked_"+SliderID).val("1");
	} 
	
	 
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
int FeatureID = 146;

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

//Date date = Utilities.parseDate(request.getParameter("Date"));



Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");

String WhereDate = "";


if(session.getAttribute(UniqueSessionID+"_SR1StartDate") != null && session.getAttribute(UniqueSessionID+"_SR1EndDate") != null){
	WhereDate =" and cca.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate);
}

//if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
//	EndDate = new Date();
//}

//System.out.print("StartDate = "+StartDate);
//System.out.print("EndDate = "+EndDate);


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
	WhereHOD = " and isa.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and isa.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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
	WhereSM = " and isa.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
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
	WhereTDM = " and  isa.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
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
	WhereASM = " and isa.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
}

//Distributor

long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");           	
}else{
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
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
			DistributorIDs += "0,"+SelectedDistributorsArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorsArray[i];
		}
	}
	WhereDistributors = " and isa.distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}

//







%>



<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>
<%


//System.out.println(StartDateMonth6+" - "+EndDateMonth6);

%>

<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size:11px;">
							
							
							<th data-priority="1"  style="text-align:center; ">Outlet</th>							
							<th data-priority="1"  style="text-align:center; ">Number of Outlets</th>
							<th data-priority="1"  style="text-align:center;  ">Hand to Hand Discount</th>							
							<%
							int PackageCount = 0;
							ResultSet rs4 = s3.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where product_id in (SELECT distinct iisap.product_id FROM inventory_sales_adjusted iisa join inventory_sales_adjusted_products iisap on iisa.id = iisap.id where iisa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and iisap.hand_discount_amount != 0) "+WherePackage+" order by package_sort_order");
							while(rs4.next()){
								PackageCount++;
							%>
							<th data-priority="1"  style="text-align:center; "><%=rs4.getString("package_label")%></th>
							<%
							}
							%>						
					    </tr>
					  </thead> 
					<tbody>
					
					<%
					int ColSpanCount = PackageCount+3;
					ResultSet rs1 = s2.executeQuery("select * from common_distributors cd  where cd.distributor_id in("+DistributorIDs+")");
					while(rs1.next()){					
					
					%>
					<tr>
						<td style="background:#ececec" colspan="<%=ColSpanCount%>"><%=rs1.getLong("distributor_id") %> - <%=rs1.getString("name") %></td>
					</tr>
					
					<%
						String Query = "SELECT isa.outlet_id,isa.distributor_id,(select co.name from common_outlets co where co.id=isa.outlet_id) outlet_name, sum(isap.hand_discount_amount) discount_amount, ("
						 +" SELECT count(distinct iisa.outlet_id) FROM inventory_sales_adjusted iisa join inventory_sales_adjusted_products iisap on iisa.id = iisap.id where iisa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and hand_discount_amount != 0 and iisa.outlet_id = isa.outlet_id" 
						 +" ) outlet_count FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.distributor_id="+rs1.getLong("distributor_id")+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+" and isap.hand_discount_amount != 0 group by outlet_id";
						 
						 ResultSet rs = s.executeQuery(Query);
						 while(rs.next()){					
						%>
						
							<tr>
								<td style="text-align:left"><%=rs.getLong("outlet_id") %> - <%=rs.getString("outlet_name") %></td>							
								<td style="text-align:left"><%=rs.getInt("outlet_count") %></td>
								<td style="text-align:left"><%=Utilities.getDisplayCurrencyFormat(rs.getDouble("discount_amount")) %></td>
								<%
								rs4.beforeFirst();
								while(rs4.next()){
									

									// outlet id, package
								String Query1 = "SELECT sum(isap.hand_discount_amount) discount_amount FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.distributor_id="+rs1.getLong("distributor_id")+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+" and isap.hand_discount_amount != 0 and isa.outlet_id = "+rs.getLong("outlet_id")+" and isap.product_id in (select id from inventory_products where package_id ="+rs4.getLong("package_id")+")";
									ResultSet rs5 = s4.executeQuery(Query1);
											if(rs5.first()){
									
									%>
									<td style="text-align:left"><%=Utilities.getDisplayCurrencyFormat(rs5.getDouble("discount_amount")) %></td>
									<%
											}
								}
								
								
																					
								 %>
							</tr>
						<%
						 }
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

s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>