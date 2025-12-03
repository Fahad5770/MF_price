<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>

<script>

function ReceiptFromProdViewEditFormFromSummary(){
	document.getElementById("ReceiptFromProdViewEditFormFromSummaryForm").submit();
}

function ReceiptFromProdViewPrintFormFromSummary(){
	document.getElementById("ReceiptFromProdViewPrintFormFromSummaryForm").submit();
}

</script>

<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 92;

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

long ReceiptFromProdMainID = Utilities.parseLong(request.getParameter("ReceiptFromProdMainID"));
//System.out.println("Sap Order No "+SapOrderNumber);

//Date date = Utilities.parseDate(request.getParameter("Date"));

Date StartDate = (Date)session.getAttribute("SR1StartDate");
Date EndDate = (Date)session.getAttribute("SR1EndDate");

if(session.getAttribute("SR1StartDate") == null){
	StartDate = new Date();
}

if(session.getAttribute("SR1EndDate") == null){
	EndDate = new Date();
}

long SelectedDistributorArray[] = null;
if (session.getAttribute("SR1SelectedDistributors") != null){
	SelectedDistributorArray = (long[])session.getAttribute("SR1SelectedDistributors");           	
}



String DistributorIDs = "";
if(SelectedDistributorArray != null && SelectedDistributorArray.length > 0){
	for(int i = 0; i < SelectedDistributorArray.length; i++){
		//System.out.println("zulqurnan");
		if(i == 0){
			DistributorIDs += SelectedDistributorArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorArray[i];
		}
	}
}

%>



			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="b" style="margin-top:-13px;">
				<li data-role="list-divider">Receipt Summary</li>
				<li>
				<table border=0 style="font-size:13px; width:100%"  data-role="table" id="SamplingSummary" data-mode="reflow" class="ui-responsive table-stroke">
					  <thead>
					    <tr>
					    	
							
							<th data-priority="2" >&nbsp;</th>
							
							<th data-priority="1"  style="text-align:right">Raw Cases</th>
							<th data-priority="persist"  style="text-align:right">Converted Cases</th>
							
					    </tr>
					  </thead>

			            <%
			            String where1="";
			            if(ReceiptFromProdMainID >0)
			            {
			            	where1 += " and ipr.id = "+ReceiptFromProdMainID;
			            	//System.out.println("hello");
			            }
			            double TotalConvertedCases = 0;
			            
			            String Sql1 = "select ip.category_id,ip.package_id,ipa.label,ipa.unit_per_case,sum(iprp.total_units) bottles,sum(iprp.liquid_in_ml) / ipa.conversion_rate_in_ml converted_cases from inventory_production_receipts ipr, inventory_production_receipts_products iprp, inventory_products ip, inventory_packages ipa, inventory_brands ib where ipr.id = iprp.id and iprp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and ipr.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and ip.category_id = 1 "+ where1+" group by ip.category_id , ip.package_id order by ip.category_id , ipa.sort_order";
			            //System.out.println(Sql1);
			            ResultSet rs1 = s.executeQuery(Sql1);
			            while(rs1.next()){
			            	
			            	TotalConvertedCases += rs1.getDouble("converted_cases");
			            	
			                %>
			                	<tr style="background:#ececec">
			   	            		<td align="left"><%=rs1.getString("ipa.label")%></td>
			   	            		<td style="text-align:right; padding-right:10px"><b><%=Utilities.convertToRawCases(rs1.getLong("bottles"), rs1.getInt("ipa.unit_per_case"))%></b></td>
									<td style="text-align:right; padding-right:10px"><b><%=Utilities.getDisplayCurrencyFormat(rs1.getDouble("converted_cases"))%></b></td>
			   	            	</tr>
			                <%
			                
			                
			                String Sql = "select ip.category_id, ip.package_id, ipa.label, ip.brand_id, ib.label, ip.unit_per_sku, sum(iprp.total_units) bottles, sum(iprp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_production_receipts ipr, inventory_production_receipts_products iprp, inventory_products ip, inventory_packages ipa, inventory_brands ib where ipr.id = iprp.id and iprp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id  and ipr.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and ip.category_id = 1  and ip.package_id="+rs1.getInt("ip.package_id")+"  "+where1+" group by ip.category_id, ip.package_id, ip.brand_id order by ip.category_id, ip.package_id, ip.brand_id";
			                //System.out.println(Sql);
			                ResultSet rs2 = s2.executeQuery(Sql);
			                while( rs2.next() ){
			                	
			                	%>
			                	
			                	<tr>
			    					<td style="padding-left:20px"><%=rs2.getString("ib.label")%></td>
			    					<td style="text-align:right; padding-right:10px"><%=Utilities.convertToRawCases( rs2.getLong("bottles"), rs2.getInt("ip.unit_per_sku"))%></td>
			    					<td style="text-align:right; padding-right:10px"><%= Utilities.getDisplayCurrencyFormat( rs2.getDouble("converted_cases"))%></td>
			    		    	</tr>
			                	
			                	<%
			                	
			                }
			               
			            }
			            
			            
			            %>
			            
			            
			                <tr style="background:#ececec">
			                	<td colspan="2">&nbsp;</td>
			                	<td style="text-align:right; padding-right:10px"><b><%=Utilities.getDisplayCurrencyFormat(TotalConvertedCases)%></b></td>
			                </tr>
			            
			</table>
			</li>
			</ul>

<form id="ReceiptFromProdViewEditFormFromSummaryForm" action="ReceiptFromProduction.jsp" target="_blank" method="post">
<input type="hidden" name="ProductionReceiptID" id="ProductionReceiptID" value="<%=ReceiptFromProdMainID %>"/>
</form>

<form id="ReceiptFromProdViewPrintFormFromSummaryForm" action="ReceiptFromProductionPrinting.jsp" target="_blank" method="post">
<input type="hidden" name="ReceiptFromProductionID" id="ReceiptFromProductionID" value="<%=ReceiptFromProdMainID %>"/>
</form>


<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>