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
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>


<script>

function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}

function ReportCenterR167LoadBrandsList(ParamDocumentID, ParamPackageID, ParamTypeID){
	
	$('#ReportCenterR167BrandsList').html( "<img src='images/snake-loader.gif' >" );
	
	$.ajax({
	    url: "ReportCenterR174LoadBrandsList.jsp",
	    data: {
	    	DocumentID: ParamDocumentID,
	    	PackageID: ParamPackageID,
	    	TypeID: ParamTypeID
	    },
	    type: "POST",
	    dataType : "html",
	    success: function( html ) {
	    	//alert(html);
	    	$('#ReportCenterR167BrandsList').html( html ).trigger('create');
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
	
}


function ReportCenterR167LoadScopeList(ParamDocumentID){
	
	$('#ReportCenterR167ScopeList').html( "<img src='images/snake-loader.gif' >" );
	
	$.ajax({
	    url: "ReportCenterR174LoadScopeList.jsp",
	    data: {
	    	DocumentID: ParamDocumentID
	    },
	    type: "POST",
	    dataType : "html",
	    success: function( html ) {
	    	//alert(html);
	    	$('#ReportCenterR167ScopeList').html( html ).trigger('create');
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
	
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
int FeatureID = 211;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

//Region
String RegionIDs="";
long SelectedRegionArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRegion") != null){
	SelectedRegionArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRegion");
	RegionIDs = Utilities.serializeForSQL(SelectedRegionArray);
}

String WhereRegion = "";
if (RegionIDs.length() > 0){
	WhereRegion = " and region_id in ("+RegionIDs+") ";	
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
	WhereDistributors = " distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}
      
           
%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Active Promotions</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
				<thead>
					<tr>
						<th colspan="7" >&nbsp;</th>
						<th colspan="4" style="text-align:center">Sale</th>
						<th colspan="4" style="text-align:center">Promotion</th>
						<th style="text-align:center">&nbsp;</th>
					</tr>
					
					<tr>
						<th>Promotion</th>
						<th>Valid From</th>
						<th>Valid To</th>
						<th>Activated On</th>
						<th>Quota in Free Bottles</th>
						<th>Consumed</th>
						<th>Consumed (%)</th>

						<th>Package</th>
						<th>Brand</th>
						<th>Case</th>
						<th>Bottle</th>
						
						<th>Package</th>
						<th>Brand</th>
						<th>Case</th>
						<th>Bottle</th>
						
						<th>Scope</th>
						
					</tr>
				</thead>
				<tbody>
				<%
				//System.out.println("SELECT id, label, valid_from, valid_to, created_on,estimated_sales_volume FROM inventory_sales_promotions_request where id in ( SELECT product_promotion_id FROM inventory_sales_promotions_request_regions where 1=1 "+WhereRegion+" union SELECT product_promotion_id FROM inventory_sales_promotions_request_distributors isprd join common_distributors cd on isprd.distributor_id=cd.distributor_id where 1=1 "+WhereRegion+" ) and is_active=1 and curdate() between valid_from and valid_to order by created_on desc");
				
				ResultSet rs = s.executeQuery("SELECT isp.id, isp.label, isp.valid_from, isp.valid_to, isp.created_on,isp.estimated_sales_volume,isp.request_id,(SELECT sum(total_units) consumed_qty FROM pep.gl_order_posting_promotions glopp where glopp.request_id = isp.request_id ) consumed_qty, (SELECT raw_cases FROM pep.inventory_sales_promotions_request_products where id = isp.id and type_id = 1 limit 1) sales_cases, (SELECT units FROM pep.inventory_sales_promotions_request_products where id = isp.id and type_id = 2 limit 1) free_bottles FROM inventory_sales_promotions_request isp join workflow_requests wr on isp.request_id = wr.request_id where wr.status_id = 2 and id in ( SELECT product_promotion_id FROM inventory_sales_promotions_request_regions where 1=1 "+WhereRegion+" union SELECT product_promotion_id FROM inventory_sales_promotions_request_distributors isprd join common_distributors cd on isprd.distributor_id=cd.distributor_id where 1=1 "+WhereRegion+" ) and is_active=1 and now() between valid_from and cast(date_format(from_days(to_days(valid_to)+1),'%Y-%m-%d 06:00:00') as datetime)  order by created_on desc");
				
				while(rs.next()){
					
					
					
					
				double Quota = (rs.getDouble("free_bottles") / rs.getDouble("sales_cases")) * rs.getDouble("estimated_sales_volume");
				double Consumed = rs.getDouble("consumed_qty");
				double Remaining = (Consumed/Quota)*100;
					
				
					if (Remaining < Math.round(100)){
					%>
						<tr>
							<td nowrap="nowrap"> <%=rs.getString("request_id") +" - " +rs.getString("label")%></td>
							<td><%=Utilities.getDisplayDateFormat(rs.getDate("valid_from"))%></td>
							<td><%=Utilities.getDisplayDateFormat(rs.getDate("valid_to"))%></td>
							<td nowrap="nowrap"><%=Utilities.getDisplayDateFormat(rs.getDate("created_on"))%></td>
							<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(Quota) %></td>
							<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(Consumed) %></td>
							<td style="text-align: center;"><%=Utilities.getDisplayCurrencyFormatRounded(Remaining) %>%</td>
							
							<%
							ResultSet rs2 = s2.executeQuery("SELECT package_id, raw_cases, units, (select package_label from inventory_products_view where package_id=inventory_sales_promotions_request_products.package_id limit 1) package_label FROM inventory_sales_promotions_request_products where id="+rs.getString("id")+" and type_id=1");
							if(rs2.first()){
								%>
								<td nowrap="nowrap"><%=rs2.getString("package_label")%></td>
								<td><a href="#popupDialogReportCenterR167BrandsList" data-rel="popup" data-position-to="window" data-transition="pop" onclick="ReportCenterR167LoadBrandsList(<%=rs.getString("id")%>, <%=rs2.getString("package_id")%>, 1)" >View</a></td>
								<td><%=rs2.getString("raw_cases")%></td>
								<td><%=rs2.getString("units")%></td>
								
								<%
							}
							%>
							
							<%
							ResultSet rs3 = s2.executeQuery("SELECT package_id, raw_cases, units, (select package_label from inventory_products_view where package_id=inventory_sales_promotions_request_products.package_id limit 1) package_label FROM inventory_sales_promotions_request_products where id="+rs.getString("id")+" and type_id=2");
							if(rs3.first()){
								%>
								<td nowrap="nowrap"><%=rs3.getString("package_label")%></td>
								<td><a href="#popupDialogReportCenterR167BrandsList" data-rel="popup" data-position-to="window" data-transition="pop" onclick="ReportCenterR167LoadBrandsList(<%=rs.getString("id")%>, <%=rs3.getString("package_id")%>, 2)" >View</a></td>
								<td><%=rs3.getString("raw_cases")%></td>
								<td><%=rs3.getString("units")%></td>
								<%
							}
							%>
							
							<td><a href="#popupDialogReportCenterR167ScopeList" data-rel="popup" data-position-to="window" data-transition="pop" onclick="ReportCenterR167LoadScopeList(<%=rs.getString("id")%>)" >View</a></td>
							
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