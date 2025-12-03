package com.pbc.bi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class SamplingRatesUpdate_3333 {
	
	
	public static String StartDateMySQL = "2016-04-01";
	public static String StartDateOracle = "20160401";
	
	public static void main(String[] args) {
		
		try{
			
			
			
			//processStep1(); // Fetch from SAP, breakup
			
			processStep2();
			
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
			
			
			
			
			
			ResultSet rs = s.executeQuery("SELECT * FROM pep.discon_rationaliation_outlet_wise_rate");
			while(rs.next()){
				long OutletID = rs.getLong("outlet_code");
				
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
				
				/////For 100ML PET
				
				ResultSet rs1 = s2.executeQuery("SELECT * FROM pep.sampling_percase where sampling_id in (SELECT sampling_id FROM pep.sampling where outlet_id="+OutletID+" and active=1) and package in (3)");
				if(rs1.first()){
					k100MLPETRateFromSampling=rs1.getLong("company_share");
					SamplingID = rs1.getLong("sampling_id");
				}
				
				if(k100MLPETRateFromSampling==k100MLPETRateFromExcel){
					///System.out.println("Yes rates are same for 100 ML "+k100MLPETRateFromSampling+" - New rate will be "+rs.getLong("PET_1000_ML_NEW"));
					
					String UpdateQuery1000ML = "update sampling_percase set company_share="+rs.getLong("PET_1000_ML_NEW")+" where sampling_id="+SamplingID+" and package=3";
					
					//System.out.println(UpdateQuery1000ML);
					//s3.executeUpdate(UpdateQuery1000ML);
					
				}else{
					System.out.println("Company Share did not match - OutletID = "+OutletID+" and Rates are "+k100MLPETRateFromSampling+"||"+k100MLPETRateFromExcel);
				}
				
				
				///////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////	////////////////////////////////////////////
				
				
				/////For 1500ML PET
				
				ResultSet rs11 = s2.executeQuery("SELECT * FROM pep.sampling_percase where sampling_id in (SELECT sampling_id FROM pep.sampling where outlet_id="+OutletID+" and active=1) and package in (2)");
				if(rs11.first()){
					k1500MLPETRateFromSampling=rs11.getLong("company_share");
				}
				
				if(k1500MLPETRateFromSampling==k1500MLPETRateFromExcel){
					//System.out.println("Yes rates are same for 1500 ML "+k1500MLPETRateFromSampling+" - New rate will be "+rs.getLong("PET_1750_or_1500_ML_New"));
					
					String UpdateQuery1500ML = "update sampling_percase set company_share="+rs.getLong("PET_1750_or_1500_ML_New")+" where sampling_id="+SamplingID+" and package=2";
					
					//System.out.println(UpdateQuery1500ML);
					
					//s3.executeUpdate(UpdateQuery1500ML);
				}else{
					System.out.println("Company Share did not match - OutletID = "+OutletID+" and Rates are "+k1500MLPETRateFromSampling+"||"+k1500MLPETRateFromExcel);
				}
				
				
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				
				
				
			/////For 1750ML PET
				
			ResultSet rs1750 = s2.executeQuery("SELECT * FROM pep.sampling_percase where sampling_id in (SELECT sampling_id FROM pep.sampling where outlet_id="+OutletID+" and active=1) and package in (24)");
			if(rs1750.first()){
				k1750MLPETRateFromSampling=rs1750.getLong("company_share");
			}
			
			if(k1750MLPETRateFromSampling==k1750MLPETRateFromExcel){
				//System.out.println("Yes rates are same for 1750 ML "+k1750MLPETRateFromSampling+" - New rate will be "+rs.getLong("PET_1750_or_1500_ML_New"));
				
				String UpdateQuery1750ML = "update sampling_percase set company_share="+rs.getLong("PET_1750_or_1500_ML_New")+" where sampling_id="+SamplingID+" and package=24";
				
				//System.out.println(UpdateQuery1750ML);
				
				//s3.executeUpdate(UpdateQuery1750ML);
			}else{
				System.out.println("Company Share did not match - OutletID = "+OutletID+" and Rates are "+k1750MLPETRateFromSampling+"||"+k1750MLPETRateFromExcel);
			}
			
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				
			
			
		/////For 500ML PET AQ
			
			ResultSet rs500 = s2.executeQuery("SELECT * FROM pep.sampling_percase where sampling_id in (SELECT sampling_id FROM pep.sampling where outlet_id="+OutletID+" and active=1) and package in (6) and brand_id=12");
			if(rs500.first()){
				k500MLPETAQRateFromSampling=rs500.getLong("company_share");
			}
			
			if(k500MLPETAQRateFromSampling==k500MLPETAQRateFromExcel){
				//System.out.println("Yes rates are same for 500 ML PET AQ "+k500MLPETAQRateFromSampling+" - New rate will be "+rs.getLong("PET_500_ML_AQ_New"));
				
				String UpdateQuery500MLPETAQ = "update sampling_percase set company_share="+rs.getLong("PET_500_ML_AQ_New")+" where sampling_id="+SamplingID+" and package=6 and brand_id=12";
				
				//System.out.println(UpdateQuery500MLPETAQ);
				
				//s3.executeUpdate(UpdateQuery500MLPETAQ);
			}else{
				System.out.println("Company Share did not match - OutletID = "+OutletID+" and Rates are "+k500MLPETAQRateFromSampling+"||"+k500MLPETAQRateFromExcel);
			}
			
			
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			
			
			
		/////For 1500ML PET AQ
			
					ResultSet rs1500 = s2.executeQuery("SELECT * FROM pep.sampling_percase where sampling_id in (SELECT sampling_id FROM pep.sampling where outlet_id="+OutletID+" and active=1) and package in (2) and brand_id=12");
					if(rs1500.first()){
						k1500MLPETAQRateFromSampling=rs1500.getLong("company_share");
					}
					
					if(k1500MLPETAQRateFromSampling==k1500MLPETAQRateFromExcel){
						//System.out.println("Yes rates are same for 1500 ML PET AQ "+k1500MLPETAQRateFromSampling+" - New rate will be "+rs.getLong("PET_1500_ML_AQ_New"));
						
						String UpdateQuery1500MLPETAQ = "update sampling_percase set company_share="+rs.getLong("PET_1500_ML_AQ_New")+" where sampling_id="+SamplingID+" and package=2 and brand_id=12";
						
						//System.out.println(UpdateQuery1500MLPETAQ);
						
						//s3.executeUpdate(UpdateQuery1500MLPETAQ);
					}else{
						System.out.println("Company Share did not match - OutletID = "+OutletID+" and Rates are "+k1500MLPETAQRateFromSampling+"||"+k1500MLPETAQRateFromExcel);
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
	
	
	
	
	
public static void processStep2() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		Datasource ds = new Datasource();
		
		try{
			ds.createConnection();
			
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			
			
			
			long SamplingID=0;
			
			ResultSet rs = s.executeQuery("SELECT distinct s.sampling_id FROM pep.sampling s join sampling_percase sp on s.sampling_id=sp.sampling_id where sp.package=5 and s.active=1");  //getting all approved sampling for 4u
			while(rs.next()){
				SamplingID = rs.getLong("sampling_id");
				
				ResultSet rs2 = s2.executeQuery("SELECT * FROM pep.sampling_percase where sampling_id = "+SamplingID+" and package=5"); //getting percase records against 4u
				while(rs2.next()){
					
					int BrandID=rs2.getInt("brand_id");
					double AgencyShare=rs2.getDouble("agency_share");
					double CompanyShare=rs2.getDouble("company_share");
					double DeductionTerms=rs2.getDouble("deduction_term");
					int HandtoHand=rs2.getInt("hand_to_hand");
					Date ValidFrom=rs2.getDate("valid_from");
					Date ValidTo=rs2.getDate("valid_to");
					
					
					
					try{
						String InsertionQuery = "insert into sampling_percase(sampling_id,package,brand_id,agency_share,company_share,deduction_term,hand_to_hand,valid_from,valid_to) values("+SamplingID+",29,"+BrandID+","+AgencyShare+","+CompanyShare+","+DeductionTerms+","+HandtoHand+","+Utilities.getSQLDate(Utilities.parseDate("01/08/2017"))+","+Utilities.getSQLDate(ValidTo)+")";
						
						s3.executeUpdate(InsertionQuery);
						
						System.out.println(InsertionQuery);
						
					}catch(Exception e){
						e.printStackTrace();
					}
					
				}
			}
			
			
			
		
						
						
		
			
			
			
			
			
			
		}catch(Exception e){
			//ds.rollback();
			e.printStackTrace();
		}finally{
			ds.dropConnection();
		}
	}
	
	
	
	
	
}
