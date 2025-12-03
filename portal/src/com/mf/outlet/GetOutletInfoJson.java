package com.mf.outlet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mf.modals.Outlet;
import com.pbc.util.Datasource;

public class GetOutletInfoJson {

	public static List<Outlet> beat_plan_rows(Datasource ds, int user_id) {
		List<Outlet> outlets = new ArrayList<Outlet>();
		try {

			Statement s = ds.createStatement();
			System.out.println(
					"select co.pic_channel_id, (select label from pci_sub_channel where id=co.pic_channel_id) channel_label,co.radius, dbpv.outlet_id, co.name outlet_name , dbpv.distributor_id, dbpv.label pjp_label, dbpv.id beat_plan_id, co.radius , dbpv.day_number, coc.contact_name owner, co.address, coc.contact_number telepohone, co.nfc_tag_id, co.lat, co.lng, co.area_label, co.sub_area_label,co.accuracy,co.IsGeoFence, dbpv.is_alternative from distributor_beat_plan_view dbpv join common_outlets co on dbpv.outlet_id = co.id join common_outlets_contacts coc on dbpv.outlet_id = coc.outlet_id and coc.is_primary = 1 where dbpv.assigned_to = "
							+ user_id);
			ResultSet rsOutlets = s.executeQuery(
					"select co.pic_channel_id, (select label from pci_sub_channel where id=co.pic_channel_id) channel_label,co.radius, dbpv.outlet_id, co.name outlet_name , dbpv.distributor_id, dbpv.label pjp_label, dbpv.id beat_plan_id, co.radius , dbpv.day_number, coc.contact_name owner, co.address, coc.contact_number telepohone, co.nfc_tag_id, co.lat, co.lng, co.area_label, co.sub_area_label,co.accuracy,co.IsGeoFence, dbpv.is_alternative from distributor_beat_plan_view dbpv join common_outlets co on dbpv.outlet_id = co.id join common_outlets_contacts coc on dbpv.outlet_id = coc.outlet_id and coc.is_primary = 1 where dbpv.assigned_to = "
							+ user_id);
			while (rsOutlets.next()) {
				Outlet outlet = new Outlet(rsOutlets.getInt("IsGeoFence"), rsOutlets.getInt("beat_plan_id"),
						rsOutlets.getLong("distributor_id"), rsOutlets.getString("pjp_label"),
						rsOutlets.getInt("radius"), rsOutlets.getLong("outlet_id"), rsOutlets.getString("outlet_name"),
						rsOutlets.getInt("day_number"), rsOutlets.getString("owner"), rsOutlets.getString("address"),
						rsOutlets.getString("telepohone"), rsOutlets.getInt("nfc_tag_id"), rsOutlets.getInt("accuracy"),
						rsOutlets.getInt("pic_channel_id"), rsOutlets.getString("channel_label"), "", "", 0,
						rsOutlets.getDouble("lat"), rsOutlets.getDouble("lng"), rsOutlets.getString("area_label"),
						rsOutlets.getString("sub_area_label"), (rsOutlets.getInt("is_alternative") == 1), "", "", "",1);

				outlets.add(outlet);
//				/System.out.println(outlet.toString());
			}
			s.close();

		} catch (SQLException e) {
			System.out.println("Outlet Info Error : " + e);
		}
		return outlets;
	}

	public static String get_outletIds_by_userId(Datasource ds, int user_id) {

		String outletIds = "";
		List<Outlet> outlets = beat_plan_rows(ds, user_id);

		int count = 0;
		for (Outlet outlet : outlets) {
			outletIds += (count == 0) ? outlet.getOutlet_id() : "," + outlet.getOutlet_id();
			count++;
		}

		return outletIds;
	}

}
