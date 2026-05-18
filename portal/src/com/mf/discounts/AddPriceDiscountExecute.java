package com.mf.discounts;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.bi.BiProcesses;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;

@WebServlet(description = "Price List ", urlPatterns = { "/discounts/AddPriceDiscountExecute" })
public class AddPriceDiscountExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AddPriceDiscountExecute() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		String UserID = null;

		if (session.getAttribute("UserID") != null) {
			UserID = (String) session.getAttribute("UserID");
		}

		if (UserID == null) {
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();

		// Master table
		String PriceDiscountLabel = Utilities.filterString(request.getParameter("PriceDiscountLabel"), 1, 20);
		String ValidFrom = Utilities.filterString(request.getParameter("PriceDiscountValidFrom"), 1, 12);
		String ValidTo = Utilities.filterString(request.getParameter("PriceDiscountValidTo"), 1, 12);
		int isActive = Utilities.parseInt(request.getParameter("PriceDiscountIsActive"));

		Date ValidFromDate = Utilities.parseDate(ValidFrom);
		Date ValidToDate = Utilities.parseDate(ValidTo);

		// Detail table
		int totalRows = Integer.parseInt(request.getParameter("TotalPriceDiscountRows"));

		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		long masterId = 0;
		try {

			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();

			String SaveMsg = "";

			if (Utilities.parseLong(request.getParameter("isEditCase")) == 0)// insertion case master table
			{

				String qMaster = "INSERT INTO inventory_price_discount "
						+ "(discount_name, valid_from, valid_to, is_active, created_by, created_on, updated_by, updated_on) "
						+ " VALUES('" + PriceDiscountLabel + "'," + Utilities.getSQLDate(ValidFromDate) + ", "
						+ Utilities.getSQLDate(ValidToDate) + ", " + isActive + ", " + UserID + ", NOW(), NULL, NULL"
						+ ")";
				System.out.println(qMaster);
				s.executeUpdate(qMaster);
				// getting pricelist id

				ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
				if (rs.first()) {
					masterId = rs.getInt(1);
				}

			} else if (Utilities.parseLong(request.getParameter("isEditCase")) == 1) // updation case for master table
			{
				long PriceDiscountMasterTableID = Utilities
						.parseLong(request.getParameter("PriceDiscountMasterTableID"));
			 System.out.println("I am in edit case "+PriceDiscountMasterTableID);
				s.executeUpdate(
						"update inventory_price_discount set discount_name='" + PriceDiscountLabel + "',valid_from="
								+ Utilities.getSQLDate(ValidFromDate) + ",valid_to=" + Utilities.getSQLDate(ValidToDate)
								+ ",is_active=" + isActive + " where id=" + PriceDiscountMasterTableID);

				System.out.println("delete from inventory_price_discount_products where price_discount_id="
						+ PriceDiscountMasterTableID);
				s.executeUpdate("delete from inventory_price_discount_products where price_discount_id="
						+ PriceDiscountMasterTableID); // deleting
				// table
				s.executeUpdate("delete from inventory_price_discount_channel where price_discount_id="
						+ PriceDiscountMasterTableID); // deleting
				// previous
				// records
				// from
				// detail
				// table

				s.executeUpdate("delete from inventory_price_discount_distributor where price_discount_id="
						+ PriceDiscountMasterTableID); // deleting
				// previous
				// records
				// from
				// detail
				// table

				s.executeUpdate("delete from inventory_price_discount_region where price_discount_id="
						+ PriceDiscountMasterTableID); // deleting
				// previous
				// records
				// from
				// detail
				// table

				masterId = PriceDiscountMasterTableID;
			}

			ResultSet rsLast = s.executeQuery("SELECT LAST_INSERT_ID() AS id");
			if (rsLast.next()) {
				masterId = rsLast.getLong("id");
			}
			rsLast.close();
			// inserting in sub tables

			int inserted = 0;
			for (int i = 0; i < totalRows; i++) {
				int isWithTax = 0;
				int isWithPercentage = 1;
				String pCode = request.getParameter("ProductCode_" + i);
				String pLabel = request.getParameter("ProductLabel_" + i);
				String discStr = request.getParameter("Discount_" + i);
				double disc = Double.parseDouble(discStr);
				String type = request.getParameter("DiscountType_" + i);
				String isWithTaxStr = request.getParameter("CheckBox_" + i);
				String isWithPercentageStr = request.getParameter("PercentageCheckBox_" + i);
				if (isWithTaxStr != null) {
					isWithTax = Utilities.parseInt(isWithTaxStr);
				}
				if (isWithPercentageStr != null) {
					isWithPercentage = Utilities.parseInt(isWithPercentageStr);
				}

				if (disc != 0) {
					String qDetail = "INSERT INTO inventory_price_discount_products"
							+ "(price_discount_id,  product_id, product_name, discount_value, is_percentage, created_by, created_on,is_with_tax) "
							+ "VALUES (" + masterId + ", " + pCode + ", " + "'" + pLabel + "', " + disc + ", " + ""
							+ isWithPercentage + ", " + UserID + ", NOW()" + "," + isWithTax + ")";

					System.out.println(qDetail);
					s.executeUpdate(qDetail);
					inserted++;
				}

			}

			// Region
			if (request.getParameterValues("RegionIDhiddenfield") != null) {

				String[] SelectedRegions = request.getParameterValues("RegionIDhiddenfield");
				for (int ii = 0; ii < SelectedRegions.length; ii++) {
					String[] FeatureIDndRegionID = SelectedRegions[ii].split(","); // RegionID,Feature ID
					// System.out.println("Region ID "+FeatureIDndRegionID[0]+" Feature ID
					// "+FeatureIDndRegionID[1]);
					System.out.println(
							"INSERT INTO `pep`.`inventory_price_discount_region`(`price_discount_id`,`region_id`) VALUES ("
									+ masterId + "," + FeatureIDndRegionID[0] + ")");
					s.executeUpdate(
							"INSERT INTO `pep`.`inventory_price_discount_region`(`price_discount_id`,`region_id`) VALUES ("
									+ masterId + "," + FeatureIDndRegionID[0] + ")");
				}
			}

			// Distributor
			if (request.getParameterValues("DistributorIDhiddenfield") != null) {

				String[] SelectedDistributor = request.getParameterValues("DistributorIDhiddenfield");
				for (int ii = 0; ii < SelectedDistributor.length; ii++) {
					String[] FeatureIDndDistributorID = SelectedDistributor[ii].split(","); // DistributorID,FeatureID
					// System.out.println("Region ID "+FeatureIDndRegionID[0]+" Feature ID
					// "+FeatureIDndRegionID[1]);
					System.out.println(
							"INSERT INTO `pep`.`inventory_price_discount_distributor`(`price_discount_id`,`distributor_id`) VALUES ("
									+ masterId + "," + FeatureIDndDistributorID[0] + ")");
					s.executeUpdate(
							"INSERT INTO `pep`.`inventory_price_discount_distributor`(`price_discount_id`,`distributor_id`) VALUES ("
									+ masterId + "," + FeatureIDndDistributorID[0] + ")");
				}
			}

			// Channel
			if (request.getParameterValues("ChannelIDhiddenfield") != null) {

				String[] SelectedChannels = request.getParameterValues("ChannelIDhiddenfield");
				for (int ii = 0; ii < SelectedChannels.length; ii++) {
					String[] FeatureIDndChannelsID = SelectedChannels[ii].split(","); // Channel,FeatureID
					// System.out.println("Region ID "+FeatureIDndRegionID[0]+" Feature ID
					// "+FeatureIDndRegionID[1]);
					System.out.println(
							"INSERT INTO `pep`.`inventory_price_discount_channel`(`price_discount_id`,`pci_sub_channel_id`) VALUES ("
									+ masterId + "," + FeatureIDndChannelsID[0] + ")");
					s.executeUpdate(
							"INSERT INTO `pep`.`inventory_price_discount_channel`(`price_discount_id`,`pci_sub_channel_id`) VALUES ("
									+ masterId + "," + FeatureIDndChannelsID[0] + ")");
				}
			}

			SaveMsg = "Saved Successfully. Master ID: " + masterId + " | Discounted Products Saved: " + inserted;

			obj.put("error", SaveMsg);
			obj.put("success", "true");
			ds.commit();

			s.close();
			ds.dropConnection();
		} catch (Exception e) {

			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			// TODO Auto-generated catch block
			obj.put("error", e);
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		out.print(obj);
		out.close();

	}

}
