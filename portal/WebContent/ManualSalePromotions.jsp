<%@page import="com.pbc.inventory.Product"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.inventory.PromotionItem"%>


<table data-role='table' data-mode='reflow' class='ui-body-d table-stripe ui-responsive' style='font-size: 10pt; width:100%'>
    		
	    		<thead>
		    		<tr class='ui-bar-c'>
		    			<th data-priority='1' nowrap="nowrap">Product Code</th>
			    		<th data-priority='1' nowrap="nowrap">Package</th>
			    		<th data-priority='1' nowrap="nowrap">Brand</th>
			    		<th data-priority='1' style='width:15%; text-align:right' nowrap="nowrap">Raw Cases</th>
			    		<th data-priority='1' style='width:15%; text-align:right' nowrap="nowrap">Bottles</th>
			    		<th data-priority='1' style='width:15%; text-align:right' nowrap="nowrap">Rate</th>
			    		<th data-priority='1' style='width:20%; text-align:right' nowrap="nowrap">Amount</th>
		    		</tr>
	        	</thead>

            <%
            
            Datasource ds = new Datasource();
            ds.createConnection();
            Statement s = ds.createStatement();

            long OutletID = Utilities.parseLong(request.getParameter("DeskSaleOutledIDHidden"));
            
            int ProductID[] = Utilities.parseInt(request.getParameterValues("ProductID"));
            
            int UnitPerSKU[] = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormUnitPerSKU"));
    		int RawCases[] = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormRawCases"));
    		int Units[] = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormUnits"));
    		
    		
			if(ProductID!=null){
        		
					long TotalUnits[] = new long[ProductID.length];
				
    				for(int i = 0; i < ProductID.length; i++){
    					TotalUnits[i] = (RawCases[i] * UnitPerSKU[i]) +  Units[i];;
    				}
    				
    				PromotionItem promotions[] = Product.getPromotionItems(OutletID, ProductID, TotalUnits);
    				
					for (int i = 0; i < promotions.length; i++){
						
						long RawCasesAndUnits[] = Utilities.getRawCasesAndUnits(promotions[i].TOTAL_UNITS, promotions[i].UNIT_PER_SKU);
						
						long ProSAPCode = 0;
						int ProProductID = 0;
						double ProSellingPriceRawCase = 0;
						double ProSellingPriceUnit = 0;
						long ProLiquidInML = 0;
						double ProAmount = 0;
						if (promotions[i].BRANDS.size() == 1){
							Product PromotionProduct = new Product(1, promotions[i].PACKAGE_ID, promotions[i].BRANDS.get(0));
							ProProductID = PromotionProduct.PRODUCT_ID;
							ProSAPCode = PromotionProduct.SAP_CODE;
							double rates[] = Product.getSellingPrice(PromotionProduct.SAP_CODE, OutletID);
							ProSellingPriceRawCase = rates[0];
							ProSellingPriceUnit = rates[1];
							ProLiquidInML = PromotionProduct.LIQUID_IN_ML;
							
							ProAmount = (RawCasesAndUnits[0] * ProSellingPriceRawCase) + (RawCasesAndUnits[1] * ProSellingPriceUnit);
						}						
						%>
						<tr>
						<td nowrap="nowrap"><span id="ProdcutCodeSpan<%=i%>"><%=ProSAPCode %></span></td>
						<td nowrap="nowrap"><%=promotions[i].PACKAGE_LABEL %></td>
						<td>
							<select data-mini="true" name="DeskSalePromotionBrand" id="DeskSalePromotionBrand" onchange="getProductPrice(<%=promotions[i].PACKAGE_ID%>, this.value, <%=OutletID%>, <%=i%>)">
							<%
							if (promotions[i].BRANDS.size() > 1){
							%>
							<option value="">Select</option>
							<%
							}
							%>
							<%
							for (int x = 0; x < promotions[i].BRANDS.size(); x++){
							%>
								<option value="<%=promotions[i].BRANDS.get(x) %>"><%=promotions[i].BRAND_LABELS.get(x) %></option>
							<%
							}
							%>
							</select>
						</td>

						<td style="text-align:right;">
						<%=RawCasesAndUnits[0]%>
						<input type="hidden" name="RawCasePromotionHidden" value="<%=RawCasesAndUnits[0]%>" >
						</td>
						<td style="text-align:right;">
						<%=RawCasesAndUnits[1]%>
						<input type="hidden" name="UnitPromotionHidden" value="<%=RawCasesAndUnits[1]%>" >
						</td>
						<td style="text-align:right;"><span id="RateSpan<%=i%>"><%= Utilities.getDisplayCurrencyFormat(ProSellingPriceRawCase) %></span></td>
						<td style="text-align:right;"><span id="AmountSpan<%=i%>"><%= Utilities.getDisplayCurrencyFormat(ProAmount) %></span>

						<input type="hidden" name="PromotionID" value="<%=promotions[i].PROMOTION_ID%>" >
						<input type="hidden" name="PromotionsProductID" value="<%=ProProductID%>" >
						<input type="hidden" name="PromotionsRawCases" value="<%=RawCasesAndUnits[0]%>" >
						<input type="hidden" name="PromotionsUnits" value="<%=RawCasesAndUnits[1]%>" >
						<input type="hidden" name="PromotionsRateRawCase" value="<%=ProSellingPriceRawCase %>" >
						<input type="hidden" name="PromotionsRateUnit" value="<%=ProSellingPriceUnit %>" >
						<input type="hidden" name="PromotionsUnitPerSKU" value="<%=promotions[i].UNIT_PER_SKU %>" >
						<input type="hidden" name="PromotionsLiquidInML" value="<%=ProLiquidInML %>" >
						<input type="hidden" name="PromotionsAmount" value="<%= Utilities.getDisplayCurrencyFormatSimple(ProAmount) %>" >
	    				</td>
	    			</tr>						
						<%
					}
    				
        	}
    		
			
    		s.close();
            ds.dropConnection();
    		%>
</table>
