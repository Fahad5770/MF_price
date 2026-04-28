<%@page import="com.pbc.util.NumberToWords"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="org.apache.commons.lang3.text.WordUtils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@include file="include/ValidateSession.jsp"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>

<style>
.formattedRow {
	border-bottom: 1px solid #000;
	padding-top: 5px;
	padding-bottom: 5px;
}
</style>

<%

//Date today= new Date();
//Date StartDate= Utilities.parseDate("01/07/2024");
//System.out.println(StartDate);
//boolean isTaxShows = today.after(StartDate);

long SessionUserID = Long.parseLong((String) session.getAttribute("UserID"));
int FeatureID = 61;

long DistributorID = 0;
String DistributorName = "";

if (Utilities.isAuthorized(FeatureID, SessionUserID) == false) {
	response.sendRedirect("AccessDenied.jsp");
} else {
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);

	if (UserDistributor != null) {
		if (UserDistributor.length > 1) //if it has more than 1 distributor
		{
	response.sendRedirect("AccessDenied.jsp");
		} else {
	DistributorName = UserDistributor[0].DISTRIBUTOR_NAME;
	DistributorID = UserDistributor[0].DISTRIBUTOR_ID;
		}
	}

}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();

int DispatchID = 0;
if (request.getParameter("DispatchID") != null) {
	DispatchID = Utilities.parseInt(request.getParameter("DispatchID"));
}

Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();

String DriverName = "";
String VehicleName = "";
int DispatchTypeID11 = 0;

ResultSet rs1 = s.executeQuery(
		"SELECT vehicle_id, (SELECT vehicle_no FROM distribtuor_vehicles where id=vehicle_id) vehicle_name, driver_id, (SELECT name FROM distributor_employees where id=driver_id) driver_name,dispatch_type FROM inventory_sales_dispatch where id="
		+ DispatchID);
if (rs1.first()) {
	DriverName = rs1.getString("driver_name");
	VehicleName = rs1.getString("vehicle_name");
	DispatchTypeID11 = rs1.getInt("dispatch_type");
}
%>
<html>

<head>
<script>
	function duplicate() {
		var c1 = document.getElementsByName("ColumnOne");
		var c2 = document.getElementsByName("ColumnTwo");
		var x = 0;
		for (x = 0; x < c1.length; x++) {
			c2[x].innerHTML = c1[x].innerHTML;
		}
	}
</script>
</head>

<body style="font-family: Helvetica, Arial, sans-serif"
	onload="duplicate();">


	<%
	String SQLMain = "";
	boolean isFound = false;

	SQLMain = "SELECT isi.brand_discount_amount,isi.order_id,isi.id, isi.created_on, isi.outlet_id, concat(om.Outlet_Name, ' ', om.Bsi_Name) as outlet_name, om.address as outlet_address, om.Owner as owner, om.Telepohone as telephone, isi.uvid, isi.invoice_amount, isi.wh_tax_amount, isi.total_amount, isi.discount, isi.net_amount, isi.distributor_id, (select name from common_distributors where distributor_id=isi.distributor_id) distributor_name , (SELECT DISPLAY_NAME FROM users where id=isi.created_by) user_name, (SELECT DISPLAY_NAME FROM users where id=isi.booked_by) booked_by_name,fraction_adjustment FROM inventory_sales_invoices isi, outletmaster om where isi.outlet_id=om.Outlet_ID and isi.id in (select sales_id from inventory_sales_dispatch_invoices where id="
			+ DispatchID + ") order by isi.id ";

	//System.out.println(SQLMain);

	ResultSet rs = s.executeQuery(SQLMain);
	while (rs.next()) {

		isFound = true;

		int InvoiceID = rs.getInt("id");
		Date CreatedOn = rs.getTimestamp("created_on");

		long OutletID = rs.getLong("outlet_id");
		String OutletName = rs.getString("outlet_name");
		String OutletAddress = rs.getString("outlet_address");
		String Owner = rs.getString("owner");
		String Telephone = rs.getString("telephone");

		long DistributorID2 = rs.getLong("distributor_id");
		String DistributorName2 = rs.getString("distributor_name");

		long barcode = rs.getLong("uvid");
		String UserName = rs.getString("user_name");

		double InvoiceAmount = rs.getDouble("invoice_amount");

		double WHTaxAmount = rs.getDouble("wh_tax_amount");
		double TotalAmount = rs.getDouble("total_amount");
		double Discount = rs.getDouble("discount");
		double FinalNetAmount = rs.getDouble("net_amount");

		String BookedByName = "Desk";
		if (rs.getString("booked_by_name") != null) {
			BookedByName = rs.getString("booked_by_name");
		}

		double FractAdj = rs.getDouble("fraction_adjustment");
	%>


	<table style="width: 11.1in; border: 0px; page-break-after: always">
		<tr>
			<td>
				<div style="width: 5.5in; border: solid 1px; margin-top: 5px;"
					name="ColumnOne">

					<table border="0" style="width: 100%">
						<tr>
							<td align="left"></td>
							<!-- <td valign="middle" style="font-weight:600"><%=Utilities.getCompanyName()%></td> -->
							<td valign="middle" style="font-weight: 600"><%=DistributorName2%></td>
							<td
								style="text-align: right; font-weight: 700; padding-right: 10px">SALE
								INVOICE</td>

						</tr>
					</table>

					<table border="0"
						style="width: 100%; margin-top: 5px; padding-left: 10px; padding-right: 10px;">
						<tr>
							<td style="width: 20%;"><img
								src="/barbecue/barcode?data=<%=barcode%>&height=20&type=Code128&headless=false&drawText=yes"><br></td>

							<td style="width: 20%; text-align: right; font-size: 12px"
								nowrap="nowrap"><%=Utilities.getDisplayDateTimeFormat(CreatedOn)%></td>
						</tr>
						<tr>
							<td style="text-align: center">
								<table style="width: 100%; font-size: 12px;">
									<tr>
										<td align="left" style="width: 33%; padding-left: 19px">Dispatch#
											<%=DispatchID%></td>
										<td align="right" style="width: 64%; padding-right: 35px"><%=barcode%></td>
									</tr>
								</table>
							</td>
							<td align="right" style="font-size: 12px"><b>Invoice#</b> <%=InvoiceID%></td>
						</tr>

					</table>
					<hr />
					<table style="width: 100%; margin: 10px; font-size: 12px"
						border="0">


						<tr>
							<td style="width: 50%"><b>Outlet</b></td>
							<td style="width: 50%"><b>Owner</b></td>
						</tr>
						<tr>
							<td><%=OutletID + " - " + OutletName%></td>
							<td><%=Owner%></td>
						</tr>
						<tr>
							<td><b>Address</b></td>
							<td><b>Contact No</b></td>
						</tr>
						<tr>
							<td><%=OutletAddress%></td>
							<td><%=Telephone%></td>
						</tr>


						<tr>
							<td><b>Distributor</b></td>
						</tr>
						<tr>
							<td><%=DistributorID + " - " + DistributorName%></td>
						</tr>

					</table>


					<table
						style="width: 100%; padding-left: 10px; padding-right: 10px; font-size: 12px"
						border="0" cellpadding="0" cellspacing="0">
						<tr style="font-weight: 700; font-size: 12px">
							<td class="formattedRow" nowrap>Product Code</td>
							<td class="formattedRow" nowrap>Package</td>
							<td class="formattedRow" nowrap>Brand</td>
							<td class="formattedRow" nowrap style="text-align: center;">Quantity</td>
							<td class="formattedRow" nowrap style="text-align: center;">Rate</td>
							<td class="formattedRow" nowrap style="text-align: center;">Amount</td>
							<td class="formattedRow" nowrap style="text-align: center;">Discount</td>
							<td class="formattedRow" nowrap style="text-align: center;">Tax</td>
							<td class="formattedRow" nowrap style="text-align: center;">Net
								Amount</td>
						</tr>
						<%
						
						double totalSalesTax = 0,totalInconmeTax = 0;
						double TotalAmountBase = 0, totalTax=0;

						double TotalAmountHandDisc = 0;

						int PromotionCounter = 0;
						//System.out.println("SELECT isip.id, isip.product_id, ipv.sap_code, ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, isip.total_units, isip.rate_raw_cases, isip.total_amount, isip.is_promotion, ipv.unit_per_sku FROM inventory_sales_invoices_products isip, inventory_products_view ipv where isip.product_id=ipv.product_id and isip.id="+InvoiceID+" order by isip.is_promotion, ipv.package_label, ipv.brand_label");

						//System.out.println("SELECT isip.id, isip.product_id, ipv.sap_code, ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, isip.total_units, isip.rate_raw_cases, isip.total_amount, isip.is_promotion, ipv.unit_per_sku,isip.hand_discount_amount, ipv.lrb_type_id FROM inventory_sales_invoices_products isip, inventory_products_view ipv where isip.product_id=ipv.product_id and isip.id="+InvoiceID+" order by isip.is_promotion, ipv.package_label, ipv.brand_label");

						ResultSet rs2 = s2.executeQuery(
								"SELECT isip.salesTax,isip.wh_tax_amount, isip.incomeTax ,isip.id, isip.product_id, ipv.sap_code, ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, isip.total_units, isip.rate_raw_cases, isip.total_amount, isip.is_promotion, ipv.unit_per_sku,isip.hand_discount_amount, ipv.lrb_type_id FROM inventory_sales_invoices_products isip, inventory_products_view ipv where isip.product_id=ipv.product_id and isip.id="
								+ InvoiceID + " order by isip.is_promotion, ipv.package_label, ipv.brand_label");
						while (rs2.next()) {
							int PackageID = rs2.getInt("package_id");
							int BrandID = rs2.getInt("brand_id");
							String Package = Utilities.filterString(rs2.getString("package_label"), 2, 100);
							String Brand = Utilities.filterString(rs2.getString("brand_label"), 2, 100);
							long ProductCode = rs2.getLong("sap_code");
							long TotalUnits = rs2.getInt("total_units");
							double Rate = rs2.getDouble("rate_raw_cases");
							double NetAmount = rs2.getDouble("total_amount");
							int IsPromotion = rs2.getInt("is_promotion");
							int UnitPerSKU = rs2.getInt("unit_per_sku");
							int LrbTypeID = rs2.getInt("lrb_type_id");

							int ProductID = rs2.getInt("product_id");

							double HandToHandDisc = rs2.getDouble("hand_discount_amount");
							
							double tax = rs2.getDouble("salesTax") + rs2.getDouble("incomeTax");
							totalSalesTax += rs2.getDouble("salesTax");
							totalInconmeTax += rs2.getDouble("incomeTax");
							
							 totalTax+=rs2.getDouble("wh_tax_amount");
							
							TotalAmountHandDisc += HandToHandDisc;

							////////Updated patch by Zulqurnan on 26/10/2017 - for price list rates

							double RateRawCaseBase = 0;
							double RateUnitBase = 0;

							if (LrbTypeID == 3) { //For Only Gur
								ResultSet rs3 = s3.executeQuery("select * from inventory_price_list_products_base where product_id=" + ProductID
								+ " and " + Utilities.getSQLDate(CreatedOn) + " between start_date and end_date");
								while (rs3.next()) {
							RateRawCaseBase = rs3.getDouble("raw_case");
							RateUnitBase = rs3.getDouble("unit");
								}
							}

							long ArrayData[] = Utilities.getRawCasesAndUnits(TotalUnits, UnitPerSKU);

							double RawCasesAmount = ArrayData[0] * RateRawCaseBase;
							double UnitCasesAmount = ArrayData[1] * RateUnitBase;

							if (ProductID == 45) {
								//System.out.println(ArrayData[0]+" - "+RateRawCaseBase);
							}

							double NetAmountBase = RawCasesAmount + UnitCasesAmount;//RateUnitBase * TotalUnits;
							TotalAmountBase += NetAmount;

							double DiscountValue = NetAmountBase - NetAmount;

							////////////////////////////////////////////////////////////////////////

							if (IsPromotion == 1) {

								if (PromotionCounter == 0) {
						%>
						<tr>
							<td colspan="7"
								style="font-weight: bold; border-bottom: 1px solid #000; padding-top: 5px; padding-bottom: 5px; text-align: left;">Promotion</td>
						</tr>
						<%
						}

						PromotionCounter++;
						}

						if (ProductID == 45) {
						//System.out.println(RateRawCaseBase);
						}

						if (LrbTypeID == 3) {
						%>
						<tr>
							<td class="formattedRow"><%=ProductCode%></td>
							<td class="formattedRow"><%=Package%></td>
							<td class="formattedRow"><%=Brand%></td>
							<td class="formattedRow"
								style="text-align: center; padding-right: 5px;"><%=Utilities.convertToRawCases(TotalUnits, UnitPerSKU)%></td>
							<td class="formattedRow"
								style="text-align: center; padding-right: 5px;"><%=Utilities.getDisplayCurrencyFormat(RateRawCaseBase)%></td>
							<%-- Rate --%>
							<td class="formattedRow"
								style="text-align: center; padding-right: 5px;"><%=Utilities.getDisplayCurrencyFormat(NetAmountBase)%></td>
							<%-- NetAmount --%>
							<td class="formattedRow"
								style="text-align: center; padding-right: 5px;">
								<%
								if (DiscountValue != 0) {
								%><%=Utilities.getDisplayCurrencyFormat(DiscountValue)%>
								<%
								} else {
								%>
								<%
								}
								%>
							</td>
							<td class="formattedRow"
								style="text-align: center; padding-right: 5px;"><%=Utilities.getDisplayCurrencyFormat(tax)%></td>
							<%-- NetAmount --%>
							<td class="formattedRow"
								style="text-align: center; padding-right: 5px;"><%=Utilities.getDisplayCurrencyFormat(NetAmount+tax)%></td>
							<%-- NetAmount --%>

						</tr>
						<%
						} else {
						%>
						<tr>
							<td class="formattedRow"><%=ProductCode%></td>
							<td class="formattedRow"><%=Package%></td>
							<td class="formattedRow"><%=Brand%></td>
							<td class="formattedRow"
								style="text-align: center; padding-right: 5px;"><%=Utilities.convertToRawCases(TotalUnits, UnitPerSKU)%></td>
							<td class="formattedRow"
								style="text-align: center; padding-right: 5px;"><%=Utilities.getDisplayCurrencyFormat(Rate)%></td>
							<%-- Rate --%>
							<td class="formattedRow"
								style="text-align: center; padding-right: 5px;"><%=Utilities.getDisplayCurrencyFormat(NetAmount)%></td>
							<%-- NetAmount --%>
							<td class="formattedRow"
								style="text-align: center; padding-right: 5px;"></td>
								<%-- Tax --%>
								<td class="formattedRow"
								style="text-align: center; padding-right: 5px;"><%=Utilities.getDisplayCurrencyFormat(tax)%></td>
							<%-- NetAmount --%>
							<td class="formattedRow"
								style="text-align: center; padding-right: 5px;"><%=Utilities.getDisplayCurrencyFormat(NetAmount + tax)%></td>
							<%-- NetAmount --%>

						</tr>
						<%
						}
						}

						double TotalDiscountAmount = (TotalAmountBase + WHTaxAmount + FractAdj) - FinalNetAmount;
						if (TotalDiscountAmount > 0) {
						Discount = TotalDiscountAmount;
						}

						Discount = Discount - TotalAmountHandDisc;
						%>
						<tr>
							<td colspan="7"
								style="font-weight: bold; padding-top: 11px; padding-bottom: 5px; text-align: left;">Brand
								Discount</td>
						</tr>
						<tr>
							<td colspan="3"
								style="font-weight: bold; border-bottom: 1px solid #000; padding-top: 5px; padding-bottom: 5px; text-align: left;">Brand Name</td>
								<td colspan="2"
								style="font-weight: bold; border-bottom: 1px solid #000; padding-top: 5px; padding-bottom: 5px; text-align: left;">Cartons</td>
								<td colspan="2"
								style="font-weight: bold; border-bottom: 1px solid #000; padding-top: 5px; padding-bottom: 5px; text-align: left;">Amount</td>
						</tr>
						<%
						if(rs.getDouble("brand_discount_amount")!=0){
							long orderID = rs.getLong("order_id");
							ResultSet rsBrandDiscount = s2.executeQuery("select (select label from inventory_products_lrb_types lrb where lrb.id=mobd.brand_id) as brand, mobd.cartons, mobd.brand_discount_amount from mobile_order_brand_discount mobd  where mobd.id="+orderID);
							while(rsBrandDiscount.next()){
						%>
						<tr>
						<td  colspan="3" class="formattedRow"><%=rsBrandDiscount.getString("brand")%></td>
						<td colspan="2" class="formattedRow"><%=rsBrandDiscount.getInt("cartons")%></td>
						<td colspan="2" class="formattedRow"><%=Utilities.getDisplayCurrencyFormat(rsBrandDiscount.getDouble("brand_discount_amount"))%></td>
						</tr>	
						<%}} %>
					<tr>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td colspan="7" style="text-align: right; padding-right: 5px">Grand
								Total:</td>
							<td style="text-align: right; padding-right: 5px;"><%=Utilities.getDisplayCurrencyFormat(TotalAmountBase)%></td>
						</tr>

						<tr>
							<td colspan="7" style="text-align: right; padding-right: 5px">Advance
								Tax:</td>
							<td style="text-align: right; padding-right: 5px;"><%=Utilities.getDisplayCurrencyFormat(totalTax)%></td>
						</tr>
						
						<tr>
							<td colspan="7" style="text-align: right; padding-right: 5px">Advance
								Income Tax :</td>
							<td style="text-align: right; padding-right: 5px;"><%=Utilities.getDisplayCurrencyFormat(totalInconmeTax)%></td>
						</tr>
					
					<% 
					
					
						//double taxAmount = (FinalNetAmount*2.5)/100;  %>
						<%-- <tr>
							<td colspan="7" style="text-align: right; padding-right: 5px">Tax 2.5 % :</td>
							<td style="text-align: right; padding-right: 5px;"><%=Utilities.getDisplayCurrencyFormat(taxAmount)%></td>
						</tr> --%>
						<!--<tr>
			  			<td colspan="5" style="text-align:right; padding-right:5px">P. Discount:</td>
			  			<td style="text-align:right; padding-right:5px;"><%=Utilities.getDisplayCurrencyFormat(Discount)%></td>
			  		</tr>
			  		<tr>
			  			<td colspan="5" style="text-align:right; padding-right:5px">H. Discount:</td>
			  			<td style="text-align:right; padding-right:5px;"><%=Utilities.getDisplayCurrencyFormat(TotalAmountHandDisc)%></td>
			  		</tr> -->
			  		<tr>
							<td colspan="7" style="text-align: right; padding-right: 5px">Brand Discount Amount :</td>
							<td style="text-align: right; padding-right: 5px;"><%=rs.getDouble("brand_discount_amount")%></td>
						</tr>
						<tr>
							<td colspan="7" style="text-align: right; padding-right: 5px"><b>Net
									Amount:</b></td>
							<td
							<% //FinalNetAmount = FinalNetAmount + taxAmount; %>
								style="text-align: right; padding-right: 5px; border: 1px solid #000"><b><%=Utilities.getDisplayCurrencyFormat(FinalNetAmount-rs.getDouble("brand_discount_amount"))%></b></td>
						</tr>

					</table>

					<table
						style="width: 100%; margin-top: 5px; padding-left: 20px; padding-right: 20px; padding-top: 20px; font-size: 12px; font-size: 12px">
						<tr>
							<td><b>User:</b> <%=UserName%></td>
							<td><b>Booker:</b> <%=BookedByName%></td>
							<td><b>Driver:</b> <%
 if (DispatchTypeID11 != 2) {
 %><%=DriverName%>
								<%
								}
								%></td>
							<td><b>Veh#:</b> <%
 if (DispatchTypeID11 != 2) {
 %><%=VehicleName%>
								<%
								} else {
								%>By Hand<%
								}
								%></td>
						</tr>

					</table>

				</div>
			</td>
			<td>
				<div style="width: 5.5in; border: solid 1px; margin-top: 5px;"
					name="ColumnTwo"></div>
			</td>
		</tr>
	</table>
	<%
	}
	%>


</body>
</html>
<%
s2.close();
s.close();
ds.dropConnection();
%>
