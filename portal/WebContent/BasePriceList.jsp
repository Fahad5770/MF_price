<%@page import="java.util.Date"%>
<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@include file="include/ValidateSession.jsp"%>




<%
	long SessionUserID = Long.parseLong((String) session.getAttribute("UserID"));

	 if (Utilities.isAuthorized(376, SessionUserID) == false) {
		response.sendRedirect("AccessDenied.jsp");
	} 

	String EditID = "0";
	boolean isEditCase = false;

	if (request.getParameter("BrandExchangeID") != null) {
		EditID = request.getParameter("BrandExchangeID");
		isEditCase = true;
	}

	Datasource ds = new Datasource();
	ds.createConnection();

	Statement s = ds.createStatement();
	Statement s1 = ds.createStatement();
	Statement s2 = ds.createStatement();
	Statement s3 = ds.createStatement();
	Statement s4 = ds.createStatement();
%>
<html>


<head>
<jsp:include page="include/StandaloneSrc.jsp" />
<!-- JQM Base -->
<script src="js/BasePriceList.js?11334=123"></script>
<script src="js/lookups.js"></script>


</head>


<body>

	<div data-role="page" id="BrandExchange" data-url="BrandExchange"
		data-theme="d">

		<jsp:include page="Header2.jsp">
			<jsp:param value="Base Price List" name="title" />
		</jsp:include>

		<div data-role="content" data-theme="d">
			<form name="ProductPromotionsMainForm" id="ProductPromotionsMainForm">
				<input type="hidden" name="isEditCase" id="isEditCase" value="0" />
				<input type="hidden" name="ProductPromotionMasterTableID"
					id="ProductPromotionMasterTableID" value="0" /> <input
					type="hidden" name="UserID" id="UserID" value="<%=SessionUserID%>" />
				<input type="hidden" id="IsCheckedAllCheckboxes" value="0" /> <input
					type="hidden" id="IsCheckedAllCheckboxesSales" value="0" />

				<table border="0" style="width: 100%;">

					<tr style="font-size: 10pt; font-weight: 400;">

						<td style="width: 100%;">Start Date</td>




					</tr>
					<tr>

						<td style="width: 100%;"><input type="date"
							placeholder="DD/MM/YYYY" id="BasePriceListValidFrom"
							name="BasePriceListValidFrom" data-mini="true" value="">
						</td>



					</tr>


				</table>

				<br /> <br />

				<ul data-role="listview" data-inset="false" data-divider-theme="c">



					<li>
						<div class="ui-grid-solo">
							<div class="ui-grid-solo" style="border: 1px solid #eee;">
								<div class="ui-bar " style="min-height: 60px">
									<ul data-role="listview" data-inset="false"
										data-divider-theme="c">
										<li data-role="list-divider">Product Price List</li>
										<li>


											<table border="0" width="100%">
												<tr>
													<td>Product Type</td>
													<td>Product Code</td>
													<td>Package Name</td>
													<td>Brand Name</td>
													<td>Raw Case</td>



												</tr>

												<%
													ResultSet rs1 = s2
															.executeQuery("SELECT * from  pep.inventory_products_view where is_visible=1 and category_id=1 order by package_label, brand_label desc");
													Date currentdate = new Date();
													while (rs1.next()) {
														ResultSet rs = s.executeQuery(
																"SELECT ipv.category_id,ipv.category_label,ipv.sap_code,package_label,ipv.brand_label,iplpb.raw_case,iplpb.unit,iplpb.product_id FROM pep.inventory_products_view as ipv inner join inventory_price_list_products_base as iplpb on ipv.product_id=iplpb.product_id where iplpb.product_id="
																		+ rs1.getInt("product_id") + " and Year(iplpb.end_date)=" + 2999);
														int i = 0;
														if (rs.next()) {
												%>
												<tr>

													<td valign="top" style="width: 18%"><input type="text"
														placeholder="Product Type" id="PriceListProductType"
														name="PriceListProductType" data-mini="true"
														value="<%=rs.getString("category_label")%>"
														readonly="readonly" tabindex="-1"> <input
														type="hidden" name="PriceListSubTableID"
														id="PriceListSubTableID"
														value="<%=rs.getString("category_id")%>" /></td>
													<td valign="top" style="width: 18%"><input type="text"
														placeholder="Product Code" id="PriceListSapCode"
														name="PriceListSapCode" data-mini="true"
														value="<%=rs.getString("sap_code")%>" readonly="readonly"
														tabindex="-1"> <input type="hidden"
														id="PriceListProductCode" name="PriceListProductCode"
														value="<%=rs.getString("product_id")%>"></td>
													<td valign="top" style="width: 16%"><input type="text"
														placeholder="Package" id="PriceListPackageName"
														name="PriceListPackageName" data-mini="true"
														value="<%=rs.getString("package_label")%>"
														readonly="readonly" tabindex="-1"></td>
													<td valign="top" style="width: 16%"><input type="text"
														placeholder="Brand" id="PriceListBrandName"
														name="PriceListBrandName" data-mini="true"
														value="<%=rs.getString("brand_label")%>"
														readonly="readonly" tabindex="-1"></td>
													<td valign="top" style="width: 16%"><input type="text"
														placeholder="Raw Case" id="PriceListRawCase"
														name="PriceListRawCase" data-mini="true"
														value="<%=rs.getString("raw_case")%>"></td>


												</tr>
												<%
													i++;
														}

														else {
												%>


												<tr>

													<td valign="top" style="width: 18%"><input type="text"
														placeholder="Product Type" id="PriceListProductType"
														name="PriceListProductType" data-mini="true"
														value="<%=rs1.getString("category_label")%>"
														readonly="readonly" tabindex="-1"> <input
														type="hidden" name="PriceListSubTableID"
														id="PriceListSubTableID"
														value="<%=rs1.getString("category_id")%>" /></td>
													<td valign="top" style="width: 18%"><input type="text"
														placeholder="Product Code" id="PriceListSapCode"
														name="PriceListSapCode" data-mini="true"
														value="<%=rs1.getString("sap_code")%>" readonly="readonly"
														tabindex="-1"> <input type="hidden"
														id="PriceListProductCode" name="PriceListProductCode"
														value="<%=rs1.getString("product_id")%>"></td>
													<td valign="top" style="width: 16%"><input type="text"
														placeholder="Package" id="PriceListPackageName"
														name="PriceListPackageName" data-mini="true"
														value="<%=rs1.getString("package_label")%>"
														readonly="readonly" tabindex="-1"></td>
													<td valign="top" style="width: 16%"><input type="text"
														placeholder="Brand" id="PriceListBrandName"
														name="PriceListBrandName" data-mini="true"
														value="<%=rs1.getString("brand_label")%>"
														readonly="readonly" tabindex="-1"></td>
													<td valign="top" style="width: 16%"><input type="text"
														placeholder="Raw Case" id="PriceListRawCase"
														name="PriceListRawCase" data-mini="true" value="<%=""%>">

													</td>


												</tr>
												<%
													}
													}
												%>


												<%
													
												%>







											</table>



										</li>
									</ul>
									<br />
									<%-- <table>
										<tr>
											<td valign="top"><span id="SpanBrandExchangePackage">
													<%=bean.getPackageList()%>
											</span></td>
										</tr>
										<tr>
											<td>
												<!--<select id='ProductPromotionsProductCodeIssue' name='ProductPromotionsProductCodeIssue' data-mini='true' >
						<option value="">Select Brand</option>						
					</select>-->
												<fieldset data-role="controlgroup">
													<div
														style="width: 500px; float: left; font-weight: 400; font-size: 10pt;"
														id="SpanProductBaePriceList"></div>
												</fieldset>

											</td>
										</tr>
										<tr>
											<td style="width: 25%;" valign="top"><input type="text"
												placeholder="Raw Cases" id="BasePriceListRawCases"
												name="BasePriceListRawCases" onblur="AddSalesToTable()"
												data-mini="true"></td>
										<tr>

										</tr>
										<tr>

											<td><span id="ProductInfoSpan"
												style="padding-left: 20px; font-size: 10pt; font-family: Helvetica, Arial, sans-serif"></span></td>
										</tr>
									</table> --%>





									<input type="hidden" name="BrandExchangeMainFormRemarks"
										id="BrandExchangeMainFormRemarks" value="">
									<div style="width: 80%"></div>
								</div>
							</div>


						</div> <!-- /grid-a -->


					</li>
			</form>

			</ul>

		</div>
		<!-- /content -->

		<div data-role="footer" data-position="fixed" data-theme="b">

			<div>
				<table style="width: 100%;">
					<tr>
						<td><a data-icon="check" data-theme="a" data-role="button"
							data-inline="true" id="BrandPriceListSave" href="#"
							class="ui-disabled" onClick="BasePriceListSubmit();">Save</a>
							<button data-icon="check" data-theme="b" data-inline="true"
								id="BrandExchangeReset"
								onClick="javascript:window.location='BasePriceList.jsp'">Reset</button>
						</td>
				</table>
			</div>

		</div>







	</div>

</body>
</html>
<%
s.close();
ds.dropConnection();

%>