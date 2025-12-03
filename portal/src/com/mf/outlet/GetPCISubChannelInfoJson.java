package com.mf.outlet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mf.modals.PCISubChannels;
import com.pbc.util.Datasource;

public class GetPCISubChannelInfoJson {

	public static List<PCISubChannels> get_pci_sub_channel(Datasource ds) {
		List<PCISubChannels> pciChannels = new ArrayList<PCISubChannels>();

		try {

			Statement s = ds.createStatement();

			ResultSet rsOChannels = s.executeQuery("SELECT * FROM pep.pci_sub_channel");
			while (rsOChannels.next()) {
				PCISubChannels pciChannel = new PCISubChannels(rsOChannels.getInt("id"),
						rsOChannels.getString("label"));
				pciChannels.add(pciChannel);
			}

			s.close();

		} catch (SQLException e) {
			System.out.println("User Details Error :- " + e);

		}

		return pciChannels;
	}

}
