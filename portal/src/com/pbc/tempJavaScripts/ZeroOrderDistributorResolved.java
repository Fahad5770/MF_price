package com.pbc.tempJavaScripts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;

public class ZeroOrderDistributorResolved {

	public static void main(String[] args) {
		Datasource ds = null;
		Statement s = null;
		Statement s2 = null;
		Statement s3 = null;
		
		try {
			ds = new Datasource();
			ds.createConnection();
			//ds.startTransaction();
			s = ds.createStatement();
			s2 = ds.createStatement();
			s3 = ds.createStatement();
			
			int count=0;
			ResultSet rsZeroDistributors = s.executeQuery("select id,outlet_id,created_by from mobile_order_zero where created_on between '2024-01-01' and '2024-02-15' and distributor_id=0");
			while(rsZeroDistributors.next()) {
				count++;
			long NoOrderID = rsZeroDistributors.getLong("id");
			long outletId = rsZeroDistributors.getLong("outlet_id");
			long userId = rsZeroDistributors.getLong("created_by");
			
			long distributo_id = 0;
			long rsmid = 0;
			long asmid = 0;
			long sndid = 0;
			int pjpid = 0;

			System.out.println(" =================================== "+count+" =====================================");
			ResultSet rsDistributor = s2.executeQuery(
					"select  codv.distributor_id, (SELECT id FROM distributor_beat_plan_view where distributor_id = codv.distributor_id and outlet_id = codv.outlet_id and assigned_to="
							+ userId
							+ " limit 1 ) pjp_id, (select snd_id from common_distributors where distributor_id = codv.distributor_id) snd_id, (select rsm_id from common_distributors where distributor_id = codv.distributor_id) rsm_id from common_outlets_distributors_view codv where codv.outlet_id="
							+ outletId + " order by codv.distributor_id desc");
			if (rsDistributor.first()) {
				distributo_id = rsDistributor.getLong("distributor_id");
				rsmid = rsDistributor.getLong("rsm_id");
				sndid = rsDistributor.getLong("snd_id");
				pjpid = rsDistributor.getInt("pjp_id");
				ResultSet rsASM = s3.executeQuery(
						"SELECT if(asm_id = 0, null,asm_id) as asm_id FROM distributor_beat_plan where id = "
								+ pjpid);
				if (rsASM.first()) {
					asmid = rsASM.getLong("asm_id");
				}
				
				System.out.println("update mobile_order_zero set distributor_id="+distributo_id+", asm_id="+asmid+", rsm_id="+rsmid+", snd_id="+sndid+", beat_plan_id="+pjpid+", region_id = (select region_id from common_distributors where distributor_id = "+distributo_id+" )  where id="+NoOrderID);
				s3.executeUpdate("update mobile_order_zero set distributor_id="+distributo_id+", asm_id="+asmid+", rsm_id="+rsmid+", snd_id="+sndid+", beat_plan_id="+pjpid+", region_id = (select region_id from common_distributors where distributor_id = "+distributo_id+" )  where id="+NoOrderID);
				
				
//				System.out.println("=========================================");
//				System.out.println("Id : "+NoOrderID);
//				System.out.println("outletId : "+outletId);
//				System.out.println("userId : "+userId);
//				System.out.println("distributo_id : "+distributo_id);
//				System.out.println("rsmid : "+rsmid);
//				System.out.println("asmid : "+asmid);
//				System.out.println("sndid : "+sndid);
//				System.out.println("pjpid : "+pjpid);
			}
		
			}
	}catch (Exception e){
		    
		    e.printStackTrace();
		   } finally{
		    try {
		     if (s != null){
		      s.close();
		      s2.close();
		      s3.close();
		      ds.dropConnection();
		     }
		    } catch (SQLException e) {
		     e.printStackTrace();
		    }    
		   }
	}

}
