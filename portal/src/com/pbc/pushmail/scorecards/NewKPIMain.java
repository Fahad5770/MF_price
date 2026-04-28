package com.pbc.pushmail.scorecards;

import java.sql.SQLException;

import com.pbc.util.Utilities;

public class NewKPIMain {

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 * @throws Exception
	 */
	public static void main(String[] args)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, Exception {
		// TODO Auto-generated method stub

		String filename_pjp_mtd = "KPI_MTD_Test_" + Utilities.getSQLDateWithoutSeprator(new java.util.Date()) + ".xlsx";

		new com.pbc.pushmail.scorecards.NewKPIsExcel()
				.createPdf("/home/ftpshared/Testing/" + filename_pjp_mtd, 0, false); // 5012
//		new com.pbc.pushmail.scorecards.NewKPIsExcel()
//		.createPdf("D:\\" + filename_pjp_mtd, 5012, false);
	}

}
