package com.pbc.tempJavaScripts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.pbc.reports.HaversineDistanceCalculator;
import com.pbc.util.Datasource;

public class UpdateDistanceVisits {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		Datasource ds = new Datasource();
		
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();

			
			int c=1;
				System.out.println("SELECT id,ref_id,visit_lat,visit_lng,outlet_lat,outlet_lng FROM pep.psr_2024_visits where visit_distance=0");
				ResultSet rsData = s.executeQuery("SELECT id,ref_id,visit_lat,visit_lng,outlet_lat,outlet_lng FROM pep.psr_2024_visits where visit_distance=0");
				while (rsData.next()) {
					
					long id= rsData.getLong("id");
					long ref_id= rsData.getLong("ref_id");
					

					double distance = HaversineDistanceCalculator.calculateHaversineDistance(rsData.getDouble("visit_lat"),
							rsData.getDouble("visit_lng"), rsData.getDouble("outlet_lat"), rsData.getDouble("outlet_lng"));

					System.out.println(
							c+" ===> update pep.psr_2024_visits set visit_distance="+distance+" where ref_id="+ref_id+" and id="+id);
					s2.executeUpdate(
							"update pep.psr_2024_visits set visit_distance="+distance+" where ref_id="+ref_id+" and id="+id);
					c++;
				}

		/******** Work End Here ***********/

//		if (date.equals(EndDate)) {
//			break;
//		}
//		date = DateUtils.addDays(date, 1);
//	}

	s.close();
	s2.close();
	
	ds.commit();

} catch (Exception e) {
	ds.rollback();
	e.printStackTrace();
}

}
	
}
