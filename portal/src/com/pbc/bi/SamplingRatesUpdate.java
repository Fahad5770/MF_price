package com.pbc.bi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class SamplingRatesUpdate {
	
	
	public static String StartDateMySQL = "2016-04-01";
	public static String StartDateOracle = "20160401";
	
	public static void main(String[] args) {
		
		try{
			
			
			
			processStep1(); // Fetch from SAP, breakup
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	public static void processStep1() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		Datasource ds = new Datasource();
		
		try{
			ds.createConnection();
			
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			
			
			
			
			int counter=1;
			ResultSet rs = s.executeQuery("SELECT * FROM pep.discon_rationaliation_outlet_wise_rate where id<600   order by outlet_code");
			while(rs.next()){
				String OutletID = rs.getString("outlet_code");
				counter = rs.getInt("id");
				long k100MLPETRateFromSampling=0;
				long k1500MLPETRateFromSampling=0;
				long k1750MLPETRateFromSampling=0;
				long k500MLPETAQRateFromSampling=0;
				long k1500MLPETAQRateFromSampling=0;
				
				
				
				long k100MLPETRateFromExcel=rs.getLong("PET_1000_ML_OLD");
				long k1500MLPETRateFromExcel=rs.getLong("PET_1750_or_1500_ML_OLD");
				long k1750MLPETRateFromExcel=rs.getLong("PET_1750_or_1500_ML_OLD");
				long k500MLPETAQRateFromExcel=rs.getLong("PET_500_ML_AQ_OLD");
				long k1500MLPETAQRateFromExcel=rs.getLong("PET_1500_ML_AQ_OLD");
				
				long SamplingID=0;
				
				String OutletIDString="";
				
				
				//OutletIDString=OutletID;
				
				/////For 100ML PET
				
				//System.out.println("SELECT * FROM pep.sampling_percase where sampling_id in (SELECT sampling_id FROM pep.sampling where outlet_id='"+OutletID+"' and active=1) and package in (3)");
				ResultSet rs1 = s2.executeQuery("SELECT * FROM pep.sampling_percase where sampling_id in (SELECT sampling_id FROM pep.sampling where outlet_id='"+OutletID+"' and active=1) and package in (3)");
				if(rs1.first()){
					k100MLPETRateFromSampling=rs1.getLong("company_share");
					SamplingID = rs1.getLong("sampling_id");
				}
				
				if(k100MLPETRateFromSampling==k100MLPETRateFromExcel){
					///System.out.println("Yes rates are same for 100 ML "+k100MLPETRateFromSampling+" - New rate will be "+rs.getLong("PET_1000_ML_NEW"));
					try{
						String UpdateQuery1000ML = "update sampling_percase set company_share="+rs.getLong("PET_1000_ML_NEW")+" where sampling_id="+SamplingID+" and package=3";
						s3.executeUpdate(UpdateQuery1000ML);
					}catch(Exception e){
						//ds.rollback();
						e.printStackTrace();
					}
					//System.out.println(UpdateQuery1000ML);
					
					
				}else{
					String UpdateQuery1000ML = "update sampling_percase set company_share="+rs.getLong("PET_1000_ML_NEW")+" where sampling_id="+SamplingID+" and package=3";
					s3.executeUpdate(UpdateQuery1000ML);
				}
				
				//System.out.println("Company Share did not match for Package = 3 - OutletID = "+OutletIDString);
				///////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////	////////////////////////////////////////////
				
				
				/////For 1500ML PET
				
				ResultSet rs11 = s2.executeQuery("SELECT * FROM pep.sampling_percase where sampling_id in (SELECT sampling_id FROM pep.sampling where outlet_id='"+OutletID+"' and active=1) and package in (2)");
				if(rs11.first()){
					k1500MLPETRateFromSampling=rs11.getLong("company_share");
				}
				
				if(k1500MLPETRateFromSampling==k1500MLPETRateFromExcel){
					//System.out.println("Yes rates are same for 1500 ML "+k1500MLPETRateFromSampling+" - New rate will be "+rs.getLong("PET_1750_or_1500_ML_New"));
					try{
					String UpdateQuery1500ML = "update sampling_percase set company_share="+rs.getLong("PET_1750_or_1500_ML_New")+" where sampling_id="+SamplingID+" and package=2";
					s3.executeUpdate(UpdateQuery1500ML);
					}catch(Exception e){
						//ds.rollback();
						e.printStackTrace();
					}
					//System.out.println(UpdateQuery1500ML);
					
					
				}else{
					String UpdateQuery1500ML = "update sampling_percase set company_share="+rs.getLong("PET_1750_or_1500_ML_New")+" where sampling_id="+SamplingID+" and package=2";
					s3.executeUpdate(UpdateQuery1500ML);
				}
				//System.out.println("Company Share did not match for Package = 2 - OutletID = "+OutletIDString);
				
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				
				
				
			/////For 1750ML PET
				
			ResultSet rs1750 = s2.executeQuery("SELECT * FROM pep.sampling_percase where sampling_id in (SELECT sampling_id FROM pep.sampling where outlet_id='"+OutletID+"' and active=1) and package in (24)");
			if(rs1750.first()){
				k1750MLPETRateFromSampling=rs1750.getLong("company_share");
			}
			
			if(k1750MLPETRateFromSampling==k1750MLPETRateFromExcel){
				//System.out.println("Yes rates are same for 1750 ML "+k1750MLPETRateFromSampling+" - New rate will be "+rs.getLong("PET_1750_or_1500_ML_New"));
				try{
				String UpdateQuery1750ML = "update sampling_percase set company_share="+rs.getLong("PET_1750_or_1500_ML_New")+" where sampling_id="+SamplingID+" and package=24";
				s3.executeUpdate(UpdateQuery1750ML);
				}catch(Exception e){
					//ds.rollback();
					e.printStackTrace();
				}
				//System.out.println(UpdateQuery1750ML);
				
				//
			}else{
				String UpdateQuery1750ML = "update sampling_percase set company_share="+rs.getLong("PET_1750_or_1500_ML_New")+" where sampling_id="+SamplingID+" and package=24";
				s3.executeUpdate(UpdateQuery1750ML);
			}
			//System.out.println("Company Share did not match for Package = 24 - OutletID = "+OutletIDString);
			
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				
			
			
		/////For 500ML PET AQ
			
			ResultSet rs500 = s2.executeQuery("SELECT * FROM pep.sampling_percase where sampling_id in (SELECT sampling_id FROM pep.sampling where outlet_id='"+OutletID+"' and active=1) and package in (6) and brand_id=12");
			if(rs500.first()){
				k500MLPETAQRateFromSampling=rs500.getLong("company_share");
			}
			
			if(k500MLPETAQRateFromSampling==k500MLPETAQRateFromExcel){
				//System.out.println("Yes rates are same for 500 ML PET AQ "+k500MLPETAQRateFromSampling+" - New rate will be "+rs.getLong("PET_500_ML_AQ_New"));
				try{
				String UpdateQuery500MLPETAQ = "update sampling_percase set company_share="+rs.getLong("PET_500_ML_AQ_New")+" where sampling_id="+SamplingID+" and package=6 and brand_id=12";
				s3.executeUpdate(UpdateQuery500MLPETAQ);
				}catch(Exception e){
					//ds.rollback();
					e.printStackTrace();
				}
				//System.out.println(UpdateQuery500MLPETAQ);
				
				//
			}else{
				String UpdateQuery500MLPETAQ = "update sampling_percase set company_share="+rs.getLong("PET_500_ML_AQ_New")+" where sampling_id="+SamplingID+" and package=6 and brand_id=12";
				s3.executeUpdate(UpdateQuery500MLPETAQ);
			}
			
			//System.out.println("Company Share did not match for Package = 6 - OutletID = "+OutletIDString);
			
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			
			
			
		/////For 1500ML PET AQ
			
					ResultSet rs1500 = s2.executeQuery("SELECT * FROM pep.sampling_percase where sampling_id in (SELECT sampling_id FROM pep.sampling where outlet_id='"+OutletID+"' and active=1) and package in (2) and brand_id=12");
					if(rs1500.first()){
						k1500MLPETAQRateFromSampling=rs1500.getLong("company_share");
					}
					
					if(k1500MLPETAQRateFromSampling==k1500MLPETAQRateFromExcel){
						//System.out.println("Yes rates are same for 1500 ML PET AQ "+k1500MLPETAQRateFromSampling+" - New rate will be "+rs.getLong("PET_1500_ML_AQ_New"));
						try{
						String UpdateQuery1500MLPETAQ = "update sampling_percase set company_share="+rs.getLong("PET_1500_ML_AQ_New")+" where sampling_id="+SamplingID+" and package=2 and brand_id=12";
						s3.executeUpdate(UpdateQuery1500MLPETAQ);
						}catch(Exception e){
							//ds.rollback();
							e.printStackTrace();
						}
						//System.out.println(UpdateQuery1500MLPETAQ);
						
						//
					}else{
						String UpdateQuery1500MLPETAQ = "update sampling_percase set company_share="+rs.getLong("PET_1500_ML_AQ_New")+" where sampling_id="+SamplingID+" and package=2 and brand_id=12";
						s3.executeUpdate(UpdateQuery1500MLPETAQ);
					}
					
					
//counter++;
					
					if(OutletIDString!=""){
						//System.out.println(counter+" - Company Share did not match for - OutletID = "+OutletIDString);
					}
					
					
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
			
				
			}
			
			
			
			
			
		
						
						
				
			
			
			
			
			
			
			
		}catch(Exception e){
			//ds.rollback();
			e.printStackTrace();
		}finally{
			ds.dropConnection();
		}
	}
	
	
	
	
	

	
	
	
	
	
}
