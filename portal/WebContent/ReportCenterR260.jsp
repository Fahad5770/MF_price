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
int FeatureID = 324;

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

//outlet
boolean IsOutletSelected=false;
String OutletIds="";
long SelectedOutletArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets") != null){
	IsOutletSelected=true;
	SelectedOutletArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets");
	OutletIds = Utilities.serializeForSQL(SelectedOutletArray);
}

String WhereOutlets = "";
if (OutletIds.length() > 0){
	WhereOutlets = " and id in ("+OutletIds+") ";	
	System.out.println(WhereOutlets);
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


//Distributor

long SelectedDistributorsArray[] = null;
boolean IsDistributorSelected=false;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){

	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors"); 
	IsDistributorSelected = true;
}else{
}

String DistributorIDs = "";
String WhereDistributorID="";

if(SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0){
	for(int i = 0; i < SelectedDistributorsArray.length ; i++){
		
		if(i == 0){
			DistributorIDs += SelectedDistributorsArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorsArray[i];
		}
	}
	WhereDistributorID = " and distributor_id in ("+DistributorIDs+") ";
	
}


//customer filter

long DistributorID1 = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
	DistributorID1 = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
}
String WhereCustomerID ="";

if (DistributorID1 != 0){
	WhereCustomerID = " and distributor_id in ("+DistributorID1+") ";

}

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
					    <th data-priority="1"  style="text-align:center; width:7%; ">PJP ID</th>
							<th data-priority="1"  style="text-align:center; width:10%; ">PJP Name</th>
							<th data-priority="1"  style="text-align:center; width:5%;">PJP Status</th>
							<th data-priority="1"  style="text-align:center; width:5%;">PJP Type</th>
							<th data-priority="1"  style="text-align:center; width:10%;">Total Outlets Assigned</th>	
							<th data-priority="1"  style="text-align:center; width:5%;">Distributor ID</th>
							<th data-priority="1"  style="text-align:center; width:10%;">Distributor</th>							
							<th data-priority="1"  style="text-align:center; width:5%;">RSM ID</th>
							<th data-priority="1"  style="text-align:center; width:10%;">RSM</th>
							<th data-priority="1"  style="text-align:center; width:5%;">ASM ID</th>
							<th data-priority="1"  style="text-align:center; width:10%;">ASM</th>
							<th data-priority="1"  style="text-align:center; width:5%;">TSO ID</th>
							<th data-priority="1"  style="text-align:center; width:15%;">TSO</th>							
							<th data-priority="1"  style="text-align:center; width:5%;">PSR ID</th>
							<th data-priority="1"  style="text-align:center; width:10%;">PSR</th>
													
					    </tr>
					  </thead> 
					<tbody>
						<%
						//System.out.println("SELECT dbp.id, label,dbp.distributor_id,dbp.is_active,dbp.pjp_type,(select name from common_distributors cd where cd.distributor_id=dbp.distributor_id) distributor_name, dbp.sm_id,dbp.tdm_id,dbp.asm_id,(select display_name from users u where u.id=dbp.asm_id)asm_name,dbpu.assigned_to, (select display_name from users u where u.id=dbpu.assigned_to) psr_name, (select snd_id from common_distributors where distributor_id = dbp.distributor_id) snd_id, (select tdm_id from common_distributors where distributor_id = dbp.distributor_id) um_id FROM pep.distributor_beat_plan dbp left join distributor_beat_plan_users dbpu on dbp.id=dbpu.id order by dbp.distributor_id asc");
						ResultSet rs = s.executeQuery("SELECT dbp.id, label,dbp.distributor_id,dbp.is_active,dbp.pjp_type,(select name from common_distributors cd where cd.distributor_id=dbp.distributor_id) distributor_name, dbp.sm_id,dbp.tdm_id,dbp.asm_id,(select display_name from users u where u.id=dbp.asm_id)asm_name,dbpu.assigned_to, (select display_name from users u where u.id=dbpu.assigned_to) psr_name, (select snd_id from common_distributors where distributor_id = dbp.distributor_id) snd_id, (select tdm_id from common_distributors where distributor_id = dbp.distributor_id) um_id FROM pep.distributor_beat_plan dbp left join distributor_beat_plan_users dbpu on dbp.id=dbpu.id order by dbp.distributor_id asc");
						while(rs.next()){
							
							
				
							int PJP_Id=0,Distributor_ID=0,SM_ID=0,TDM_ID=0,ASM_ID=0,Assigned_To=0;
							String PJP_Name="",Distributor_Name="",ASM_Name="",PSR_Name="";
							
							if (rs.getInt("dbp.id")!=0)
							{
								PJP_Id=rs.getInt("dbp.id");
							}
							if(rs.getString("label")!="")
							{
								PJP_Name=rs.getString("label");
							}
							if(rs.getInt("dbp.distributor_id")!=0)
							{
								Distributor_ID=rs.getInt("dbp.distributor_id");
							}
							if(rs.getString("distributor_name")!="")
							{
								Distributor_Name=rs.getString("distributor_name");
							}

							if(rs.getInt("dbp.sm_id")!=0)
							{
								SM_ID=rs.getInt("dbp.sm_id");
							}
							

							if(rs.getInt("dbp.tdm_id")!=0)
							{
								TDM_ID=rs.getInt("dbp.tdm_id");
							}
							
							if(rs.getInt("dbp.asm_id")!=0)
							{
								ASM_ID=rs.getInt("dbp.asm_id");
							}
							
							if(rs.getString("asm_name")!="")
							{
								ASM_Name=rs.getString("asm_name");
							}

							if(rs.getInt("dbpu.assigned_to")!=0)
							{
								Assigned_To=rs.getInt("dbpu.assigned_to");
							}
							
							if(rs.getString("psr_name")!="")
							{
								PSR_Name=rs.getString("psr_name");
							}
							
							String SNDID = rs.getString("snd_id");
							String UMID = rs.getString("um_id");
							int status = rs.getInt("is_active");
							int pjp_type = rs.getInt("pjp_type");

							
							
							String SND_NAME = "";
							String UM_NAME = "";
							
							ResultSet rs2 = s2.executeQuery("select display_name from users where id = "+SNDID);
							while(rs2.next()){
								SND_NAME = rs2.getString(1);
							}
							ResultSet rs3 = s2.executeQuery("select display_name from users where id = "+UMID);
							while(rs3.next()){
								UM_NAME = rs3.getString(1);
							}
							if (UMID == null){
								UMID = "";
							}
							if (SNDID == null){
								SNDID = "";
							}
							
							int totalOutlets = 0;
							System.out.println("SELECT COUNT(DISTINCT outlet_id) as total_outlet FROM distributor_beat_plan_view  WHERE id  ="+PJP_Id+" ");
							ResultSet rs21 = s2.executeQuery("SELECT COUNT(DISTINCT outlet_id) as total_outlet FROM distributor_beat_plan_view  WHERE id  ="+PJP_Id+" ");
							while(rs21.next()){
								totalOutlets = rs21.getInt("total_outlet");
							}
							
	%>						
								<tr> 
								<td ><%if(PJP_Id!=0){%><%=PJP_Id%><%} %></td>
								<td ><%if(PJP_Id!=0){%><%=PJP_Id%><%} %> - <%if(PJP_Name !=null){%><%=PJP_Name%><%} %></td>
								<td> <% if (status == 1) { %> Active <% } else { %>Inactive<% } %></td>
								<td> <% if (pjp_type == 1) { %> GT <% } else { %>BA<% } %></td>
								<td ><%=totalOutlets%></td>
								<td ><%if(Distributor_ID!=0){%><%=Distributor_ID%><%} %></td>
								<td ><%if(Distributor_ID!=0){%><%=Distributor_ID%><%} %> - <%if(Distributor_Name!=null){%><%=Distributor_Name%><%} %></td>
								<td ><%=SNDID%></td>
								<td ><%=SND_NAME%></td>
								<td ><%=UMID%></td>
								<td ><%=UM_NAME%></td>
								<td ><%if(ASM_ID!=0){%><%=ASM_ID%><%} %></td>
								<td ><%if(ASM_ID!=0){%><%=ASM_ID%><%} %> - <%if(ASM_Name!=null){%><%=ASM_Name%><%} %></td>
								<td ><%if(Assigned_To!=0){%><%=Assigned_To%><%} %> </td>
								<td ><%if(Assigned_To!=0){%><%=Assigned_To%><%} %> - <%if(PSR_Name!=null){%><%=PSR_Name%><%} %></td>

							</tr>				
							
						<%}
						%>
						
						
						
					</tbody>
							
				</table>
		</td>
	</tr>
</table>

	</li>	
</ul>





<%
s4.close();
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>