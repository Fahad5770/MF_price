package com.mf.products;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import com.mf.modals.Brand;
import com.mf.modals.LRB;
import com.mf.modals.Package;
import com.mf.modals.Product;
import com.mf.modals.ProductStockPosition;
import com.mf.utils.MFAPIFunctions;
import com.pbc.inventory.StockPosting;
import com.pbc.util.Datasource;

public class GetProductsInfoJson {

	public static List<ProductStockPosition> get_products_stock(Datasource ds, int user_id) {

		List<ProductStockPosition> productStockPositionList = new ArrayList<ProductStockPosition>();

		try {

			Statement s = ds.createStatement();
//			System.out.println(
//					"SELECT distributor_id FROM distributor_beat_plan_view where assigned_to=" + user_id + " limit 1");
			ResultSet rsDistributor = s.executeQuery(
					"SELECT distributor_id FROM distributor_beat_plan_view where assigned_to=" + user_id + " limit 1");
			long distributorId = (rsDistributor.first()) ? distributorId = rsDistributor.getLong(1) : 0;

			long SelectedDistributorsArray[] = { distributorId };

			StockPosting sp = new StockPosting(true);

			int ProductID = 0;
			int UnitPerSKU = 0;
			ResultSet rs3 = s.executeQuery("SELECT ipv.product_id, ipv.unit_per_sku from inventory_products_view ipv ");
			while (rs3.next()) {

				ProductID = rs3.getInt(1);
				UnitPerSKU = rs3.getInt(2);

				Date YesterdayDate = DateUtils.addDays(new Date(), -1);
				long ClosingUnits = sp.getClosingBalance(SelectedDistributorsArray, ProductID, YesterdayDate);

				ProductStockPosition productStockPosition = new ProductStockPosition(ProductID, ClosingUnits,
						MFAPIFunctions.convertToRawCases(ClosingUnits, UnitPerSKU));

				productStockPositionList.add(productStockPosition);
			}

			sp.close();

			s.close();
			ds.dropConnection();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productStockPositionList;

	}

	public static int get_product_group_id(Datasource ds, int user_id) {
		int product_group_id = 0;
		try {

			Statement s = ds.createStatement();

			System.out.println(
					"SELECT employee_product_group_id FROM employee_product_specification where employee_sap_code="
							+ user_id);
			ResultSet rsProductgroupId = s.executeQuery(
					"SELECT employee_product_group_id FROM employee_product_specification where employee_sap_code="
							+ user_id);

			product_group_id = (rsProductgroupId.first()) ? rsProductgroupId.getInt("employee_product_group_id") : 0;

			s.close();
			return product_group_id;
		} catch (SQLException e) {
			System.out.println("User Info Error : " + e);
			return product_group_id;
		}
	}

	public static List<Product> get_product_by_group_id(Datasource ds, int product_group_id) {

		List<Product> products = new ArrayList<Product>();

		try {

			Statement s = ds.createStatement();

			System.out.println(
					"Products ==>> SELECT ip.sap_code,epl.product_id, ib.label brand, ipa.label package, ipa.sort_order as sort_order, ipa.unit_per_case as unit_per_case, ipa.id as package_id, ib.id as brand_id, ipl.id lrb_type_id, ipl.label lrb FROM employee_product_groups_list epl, inventory_products ip, inventory_brands ib, inventory_packages ipa, inventory_products_lrb_types ipl where epl.product_id = ip.id and ip.brand_id = ib.id and ip.package_id = ipa.id and ipl.id=ip.lrb_type_id and epl.product_group_id="
							+ product_group_id);

			ResultSet rsProducts = s.executeQuery(
					"SELECT ip.sap_code,epl.product_id, ib.label brand, ipa.label package, ipa.sort_order as sort_order, ipa.unit_per_case as unit_per_case, ipa.id as package_id, ib.id as brand_id, ipl.id lrb_type_id, ipl.label lrb FROM employee_product_groups_list epl, inventory_products ip, inventory_brands ib, inventory_packages ipa, inventory_products_lrb_types ipl where epl.product_id = ip.id and ip.brand_id = ib.id and ip.package_id = ipa.id and ipl.id=ip.lrb_type_id and epl.product_group_id="
							+ product_group_id);
			while (rsProducts.next()) {

				Package pkg = new Package(rsProducts.getInt("package_id"),
						rsProducts.getString("package") + "-" + rsProducts.getString("brand"), "", 0.0,
						rsProducts.getInt("sort_order"));

				Brand brand = new Brand(rsProducts.getInt("brand_id"), rsProducts.getString("brand"), 1);

				LRB lrb = new LRB(rsProducts.getInt("lrb_type_id"), rsProducts.getString("lrb"));

				Product product = new Product(rsProducts.getInt("product_id"), pkg, brand, lrb,
						rsProducts.getInt("unit_per_case"), 1, 0, 1, 1, rsProducts.getInt("sap_code"));

				products.add(product);
			}

			s.close();

		} catch (SQLException e) {
			System.out.println("User Details Error :- " + e);

		}
		return products;
	}
}
