package com.mf.outlet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mf.modals.OutletOB;
import com.pbc.util.Datasource;

public class GetOutletInfoJsonforOB {

	public static List<OutletOB> beat_plan_rows(Datasource ds, int user_id) {
		List<OutletOB> outlets = new ArrayList<OutletOB>();
		try {
			ds.createConnection();

			Statement s = ds.createStatement();

			System.out.println("select " + "    dbpv.id beat_plan_id," + "    dbpv.distributor_id," + "    dbpv.label pjp_label,"
					+ "    dbpv.city_id," + "    co.is_filer," +  " coc.contact_number telepohone," +"    co.is_Register," + "    co.Is_Geo_Fence,"
					+ "    co.Geo_Radius,"
					+ "    (SELECT label from pci_sub_channel psc where psc.id=co.pic_channel_id) as channel_label,"
					+ "    (select order_created_on_date " + "        from inventory_sales_adjusted isa "
					+ "        where isa.outlet_id = dbpv.outlet_id " + "          and isa.invoice_amount != 0 "
					+ "          and isa.booked_by = " + user_id + " " + "        order by order_created_on_date desc "
					+ "        limit 1" + "    ) as order_created_on_date," + "    dbpv.outlet_id,"
					+ "    co.name outlet_name," + "    dbpv.day_number," + "    coc.contact_name owner,"
					+ "    co.address," + "    coc.contact_number telephone," + "    co.nfc_tag_id," + "    co.lat,"
					+ "    co.lng," + "    co.area_label," + "    co.sub_area_label," + "    dbpv.is_alternative,"
					+ "    IFNULL(co.pic_channel_id, 0) pic_channel_id1 " + "from distributor_beat_plan_view dbpv "
					+ "join common_outlets co on dbpv.outlet_id = co.id "
					+ "join common_outlets_contacts coc on dbpv.outlet_id = coc.outlet_id and coc.is_primary = 1 "
					+ "where dbpv.outlet_active = 1 " + "  and dbpv.assigned_to = " + user_id);
			ResultSet rsOutlets = s.executeQuery("select " + "    dbpv.id beat_plan_id," + "    dbpv.distributor_id,"
					+ "    dbpv.label pjp_label," + "    dbpv.city_id," + " coc.contact_number telepohone," +"    co.is_filer," + "    co.is_Register,"
					+ "    co.Is_Geo_Fence," + "    co.Geo_Radius,"
					+ "    (SELECT label from pci_sub_channel psc where psc.id=co.pic_channel_id) as channel_label,"
					+ "    (select order_created_on_date " + "        from inventory_sales_adjusted isa "
					+ "        where isa.outlet_id = dbpv.outlet_id " + "          and isa.invoice_amount != 0 "
					+ "          and isa.booked_by = " + user_id + " " + "        order by order_created_on_date desc "
					+ "        limit 1" + "    ) as order_created_on_date," + "    dbpv.outlet_id,"
					+ "    co.name outlet_name," + "    dbpv.day_number," + "    coc.contact_name owner,"
					+ "    co.address," + "    coc.contact_number telephone," + "    co.nfc_tag_id," + "    co.lat,"
					+ "    co.lng," + "    co.area_label," + "    co.sub_area_label," + "    dbpv.is_alternative,"
					+ "    IFNULL(co.pic_channel_id, 0) pic_channel_id1 " + "from distributor_beat_plan_view dbpv "
					+ "join common_outlets co on dbpv.outlet_id = co.id "
					+ "join common_outlets_contacts coc on dbpv.outlet_id = coc.outlet_id and coc.is_primary = 1 "
					+ "where dbpv.outlet_active = 1 " + "  and dbpv.assigned_to = " + user_id);

			while (rsOutlets.next()) {
				OutletOB outlet = new OutletOB(rsOutlets.getLong("outlet_id"), rsOutlets.getString("outlet_name"),
						rsOutlets.getInt("day_number"), rsOutlets.getString("owner"), rsOutlets.getString("address"),
						rsOutlets.getString("telepohone"), rsOutlets.getInt("nfc_tag_id"),
						rsOutlets.getString("order_created_on_date"), ((rsOutlets.getString("channel_label") == null) ? "" : rsOutlets.getString("channel_label")),
						rsOutlets.getDouble("lat"), rsOutlets.getDouble("lng"), rsOutlets.getInt("is_filer"),
						rsOutlets.getInt("is_register"), rsOutlets.getInt("Is_Geo_Fence"),
						rsOutlets.getInt("Geo_Radius"), rsOutlets.getString("area_label"),
						rsOutlets.getString("sub_area_label"), rsOutlets.getInt("is_alternative"),
						rsOutlets.getInt("pic_channel_id1"), rsOutlets.getInt("beat_plan_id"),
						rsOutlets.getLong("distributor_id"), rsOutlets.getString("pjp_label"),
						rsOutlets.getInt("city_id"));

				outlets.add(outlet);
				//System.out.println(outlet.toString());
			}

			s.close();

		} catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.println("Outlet Info Error : " + e);
		}
		return outlets;
	}

	public static String get_outletIds_by_userId(Datasource ds, int user_id) {

		String outletIds = "";
		List<OutletOB> outlets = beat_plan_rows(ds, user_id);

		int count = 0;
		for (OutletOB outlet : outlets) {
			outletIds += (count == 0) ? outlet.getOutletID() : "," + outlet.getOutletID();
			count++;
		}

		return outletIds;
	}

}
