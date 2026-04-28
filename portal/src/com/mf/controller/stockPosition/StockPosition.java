package com.mf.controller.stockPosition;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;

import com.mf.controller.outletregistration.ORFunctions;
import com.mf.dao.StockPositionDetailResponse;
import com.mf.dao.StockPositionResponse;
import com.mf.interfaces.IStockPosition;
import com.mf.modals.ResponseModal;
import com.pbc.inventory.StockPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class StockPosition implements IStockPosition {

	@Override
	public ResponseModal GetStockPosition(JSONObject jsonData, HttpServletRequest request)
			throws ClassNotFoundException, IllegalAccessException, IllegalStateException, InstantiationException,
			IOException, ServletException, FileUploadException {
		return stock_position(jsonData, request);
	}

	private ResponseModal stock_position(JSONObject jsonData, HttpServletRequest request) {
		ResponseModal ResponseModal = new ResponseModal();

		final Number UserId = (Number) jsonData.get("user_id");
		int userId = UserId.intValue();

		Datasource ds = new Datasource();

		try {

			ds.createConnection();
			Statement s = ds.createStatement();

			StockPosting sp = new StockPosting(true);

			StockPositionResponse stockPositionResponse = new StockPositionResponse();

			List<StockPositionDetailResponse> stockPositionDetailList = new ArrayList<StockPositionDetailResponse>();
			ORFunctions orFunctions = new ORFunctions();


			int ProductID = 0;
			int UnitPerSKU = 0;
			long distributorId = orFunctions.GetDistributorByBeatPlanID(ds, orFunctions.GetBeatPlanIDByOrderBooker(ds, userId));
		
			
			System.out.println(
					"SELECT ipv.product_id, ipv.unit_per_sku, CONCAT(ipv.package_label, ' - ', ipv.brand_label) AS label from inventory_products_view ipv;"
					+ "");
			
			ResultSet rsProducts = s.executeQuery(
					"SELECT ipv.product_id, ipv.unit_per_sku, CONCAT(ipv.package_label, ' - ', ipv.brand_label) AS label from inventory_products_view ipv;"
					+ "");
			
			while (rsProducts.next()) {
				ProductID = rsProducts.getInt(1);
				UnitPerSKU = rsProducts.getInt(2);

				Date YesterdayDate = DateUtils.addDays(new Date(), 1);
				long ClosingUnits = sp.getClosingBalance(distributorId, ProductID, YesterdayDate);

				StockPositionDetailResponse stockPositionDetailResponse = new StockPositionDetailResponse(ProductID,
						ClosingUnits, Utilities.convertToRawCases(ClosingUnits, UnitPerSKU),
						rsProducts.getString("label"));

				System.out.println("Closing Stock " + ClosingUnits);

				stockPositionDetailList.add(stockPositionDetailResponse);

			}

			stockPositionResponse.setStosckDetails(stockPositionDetailList);

			ResponseModal.setStatus(true);
			ResponseModal.setData(stockPositionResponse.getIntoJson());

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ResponseModal.setErrorResponse("Stock Position Server Error");
		}

		return ResponseModal;
	}

}
