package com.pbc.sampling;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;

import com.pbc.bi.BiProcesses;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;

public class SamplingChangeDeactivationDateBulk {
	static Datasource ds = new Datasource();
	public static void main(String[] args) {
		try {
			
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3=ds.createStatement();
			Statement s4=ds.createStatement();
			Statement s5=ds.createStatement();
			Statement s6=ds.createStatement();
			Statement s7=ds.createStatement();
			Statement s8=ds.createStatement();
			Statement s9=ds.createStatement();
			Statement s10=ds.createStatement();
			Statement s11=ds.createStatement();
			Statement s12=ds.createStatement();
			Statement s13=ds.createStatement();
			
	        
	        /*
	         * *******************************************************  
	         
	        String tochangedeactivationdateSql ="";
	        tochangedeactivationdateSql="select * from sampling sg where sg.active=1 and sg.outlet_id in (select outlet_id from temp_sampling_and_sampling_percase tsasp ) order by sampling_id desc";
	       
	        	
	        
	        ResultSet rs = s.executeQuery(tochangedeactivationdateSql);   
	        while(rs.next()){
	             long SamplingID=rs.getLong("sampling_id");
           	     long OutletID=rs.getLong("outlet_id");
           	     long RequestID=rs.getLong("request_id");
		       	 String OutletName=rs.getString("outlet_name");
		       	 String BusinessType=rs.getString("business_type");
		       	 String Address=rs.getString("address");
		       	 String Region=rs.getString("region");
		       	 int Asm=1;///rs.getInt("asm");
		       	 int Cr=rs.getInt("cr");
		       	 String Market=rs.getString("market");
		       	 String Vehicle=rs.getString("vehicle");
		       	 double AdvanceCompanyShare=rs.getDouble("advance_company_share");
		       	 double AdvanceAgencyShare=rs.getDouble("advance_agency_share");
		         Date AdvanceValidFrom=rs.getDate("advance_valid_from");
		       	 Date AdvanceValidTo=rs.getDate("advance_valid_to");
		       	 double FixedCompanyShare=rs.getDouble("fixed_company_share");
		       	 double FixedAgencyShare=rs.getDouble("fixed_agency_share");
		    	 double FixedDeductionTerm=rs.getDouble("fixed_deduction_term");
		    	 Date FixedValidFrom=rs.getDate("fixed_valid_from");
		       	 Date FixedValidTo=rs.getDate("fixed_valid_to");
		       	 int Active=rs.getInt("active");
		       	 String ActivatedOn="2018-04-01";
		       	 String DeactivatedOn="9999-09-09";
		       	 double FixedCompanyShareOffpeak=rs.getDouble("fixed_company_share_offpeak");
		       	 double FixedAgencyShareOffpeak=rs.getDouble("fixed_agency_share_offpeak");
		    	 double FixedDeductionTermOffpeak=rs.getDouble("fixed_company_share_offpeak");
		       	 int DeactivatedBy=rs.getInt("deactivated_by");
		         Timestamp DeactivationTimestamp=rs.getTimestamp("deactivation_timestamp");
		         
		         //sampling Percase Data
		         long NewSamplingID=0;
		   
           	    //To Update the Deactivation Date
        	    String update ="update sampling set deactivated_on='2018-03-31 23:59:59',deactivated_by=1,deactivation_timestamp=now(),active=0 where sampling_id="+SamplingID;
        	    System.out.println(update);
        	    int updated = s2.executeUpdate(update);   
        	    
        	    //to get the new last inserted workflow request_id
        	    long requestIDWorkFlow=0;
        	    
        	    int QueryFlag=0;
        	    
        	   // ResultSet rs12 = s3.executeQuery("Select * from workflow_requests where request_id in (SELECT distinct request_id FROM sampling where sampling_id="+SamplingID+")");
        	    ResultSet rs12 = s3.executeQuery("Select * from workflow_requests where request_id in (SELECT distinct request_id FROM sampling where sampling_id="+SamplingID+")");
        	    while(rs12.next()){
					
					 String WorkFlowQuery ="";
					 WorkFlowQuery="insert into workflow_requests(status_id, process_id, created_on, created_by, current_step_id, current_action_id, current_userid, current_step_on) values ("+rs12.getInt("status_id")+", "+rs12.getInt("process_id")+", "+Utilities.getSQLDateTime(rs12.getTimestamp("created_on"))+","+rs12.getInt("created_by")+", "+rs12.getInt("current_step_id")+", "+rs12.getInt("current_action_id")+", "+rs12.getInt("current_userid")+", "+Utilities.getSQLDateTime(rs12.getTimestamp("current_step_on"))+") ";
		    	    // System.out.println("WorkFlowQuery "+WorkFlowQuery);   
		    	     QueryFlag=s4.executeUpdate(WorkFlowQuery);
				
        	    }  
				
				if(QueryFlag>0) {
    	        	ResultSet rs1 = s5.executeQuery("select LAST_INSERT_ID()");
					if (rs1.first()){
						requestIDWorkFlow = rs1.getLong(1);
						//System.out.println("Last inserted New sampling ID"+requestIDWorkFlow);
					}
				}	
					        	    
        	    // to isert the updated records with new activiation date
        	    String toInsertWithNewDateSql ="";
    	        toInsertWithNewDateSql="insert into sampling(request_id,outlet_id,outlet_name,business_type,address,region,asm,cr,market,vehicle,advance_company_share,advance_agency_share,advance_valid_from,advance_valid_to,fixed_company_share,fixed_agency_share,fixed_deduction_term,fixed_valid_from,fixed_valid_to,active,activated_on,deactivated_on,fixed_company_share_offpeak,fixed_agency_share_offpeak,fixed_deduction_term_offpeak) values ("+requestIDWorkFlow+","+OutletID+", '"+OutletName+"','"+ BusinessType+"', '"+Address+"', '"+Region+"', "+Asm+","+Cr+",'"+Market+"','"+ Vehicle+"', "+AdvanceCompanyShare+","+ AdvanceAgencyShare+","+ Utilities.getSQLDate(AdvanceValidFrom)+","+Utilities.getSQLDate(AdvanceValidTo)+", "+FixedCompanyShare+", "+FixedAgencyShare+","+ FixedDeductionTerm+","+Utilities.getSQLDate(FixedValidFrom)+","+Utilities.getSQLDate(FixedValidTo)+", 1, '"+ActivatedOn+"','"+ DeactivatedOn+"', "+FixedCompanyShareOffpeak+", "+FixedAgencyShareOffpeak+","+ FixedDeductionTermOffpeak+") ";
    	        
    	      //  System.out.println("toInsertWithNewDateSql "+toInsertWithNewDateSql);
    	        int UpdatedDeactivationDate = s6.executeUpdate(toInsertWithNewDateSql);  
    	        
    	        
    	        if(UpdatedDeactivationDate>0) {
    	        	ResultSet rs7 = s7.executeQuery("select LAST_INSERT_ID()");
					if (rs7.first()){
						NewSamplingID = rs7.getLong(1);
						//System.out.println("Last inserted sampling ID"+SamplingID);
						//System.out.println("Last inserted New sampling ID"+NewSamplingID);
						
					}
					
					ResultSet rs2 = s8.executeQuery("SELECT * FROM sampling_percase where sampling_id="+SamplingID);
					while(rs2.next()){
						// System.out.println("Percase insertion"+"Insert INTO sampling_percase (sampling_id,package,agency_share,company_share,deduction_term,valid_from,valid_to, brand_id,hand_to_hand,estimated_volume) VALUES ("+NewSamplingID+", "+rs2.getInt("package")+", "+rs2.getDouble("agency_share")+", "+rs2.getDouble("company_share")+", "+rs2.getDouble("deduction_term")+","+Utilities.getSQLDate(rs2.getDate("valid_from"))+", "+Utilities.getSQLDate(rs2.getDate("valid_to"))+", "+rs2.getInt("brand_id")+", "+rs2.getInt("hand_to_hand")+",0)");
						 s9.executeUpdate("Insert INTO sampling_percase (sampling_id,package,agency_share,company_share,deduction_term,valid_from,valid_to, brand_id,hand_to_hand,estimated_volume) VALUES ("+NewSamplingID+", "+rs2.getInt("package")+", "+rs2.getDouble("agency_share")+", "+rs2.getDouble("company_share")+", "+rs2.getDouble("deduction_term")+","+Utilities.getSQLDate(rs2.getDate("valid_from"))+", "+Utilities.getSQLDate(rs2.getDate("valid_to"))+", "+rs2.getInt("brand_id")+", "+rs2.getInt("hand_to_hand")+",0)");
							
					} 
					
					
    	        }
    	        
    	 
				
           }  //End of tochangedeactivationdateSql LOOP
	       
	        System.out.println("Loops End ");
	        System.out.println(" ");
	        System.out.println("++++++++++++++++++++++++++++++++++++++++++++ ");
	        System.out.println("++++++++++++++++++++++++++++++++++++++++++++ ");
	        System.out.println(" ");
	        
	        
	        ////////////////////////////////////////////////////////////
	         *//////////////////////////////////////////////////////////
	        ////////////////////////////////////////////////////////////
	        
			
	        
	        System.out.println("New Loops Starts ");
	       
	        ///////////////////////////////////////////////////////////
	        //New Addition on 4/26/2018 Starts 
	        
	        //For changing the Rates of the Packages
	     
	       String MainSql="";
	       long OutletID=0;
           	     
           	     String TempTableSql="select * from temp_sampling_and_sampling_percase tsasp";
           	     		
           	     	
           	     ResultSet rs21 = s2.executeQuery(TempTableSql);   
           	     while(rs21.next()){
           	    	 
           	    	OutletID = rs21.getLong("outlet_id");
           	    	long SamplingID=0;
	           	    MainSql="select * from sampling sg where sg.active=1 and outlet_id="+OutletID;
	           	 //   System.out.println(MainSql);
	           	    	 
           	    	ResultSet rs23 =  s3.executeQuery(MainSql);
           	    	if(rs23.first()) {
           	    		SamplingID = rs23.getLong("sampling_id");
           	    	}
       	    	 	
		        	int Rate1500MLPET=rs21.getInt("proposed_1500");
		        	int Rate1750MLPET=rs21.getInt("proposed_1750");
		        	int Rate1000MLPET=rs21.getInt("proposed_1000");
		        	int Rate250MLCAN=rs21.getInt("proposed_250");
		        	//System.out.println("Rate1500MLPET "+Rate1500MLPET);
		        	
	    	    	
		        	
		        	String  Get1500PackBrandIDSql="SELECT package_id,brand_id FROM pep.inventory_products_view where package_id in (2,3,22,14) and is_visible=1 and category_id=1";
				//	System.out.println("GetPackBrandIDSql ==> "+Get1500PackBrandIDSql);
					ResultSet rs11= s11.executeQuery(Get1500PackBrandIDSql);
					while(rs11.next()){
						int PackID=rs11.getInt("package_id");
						int BrandID=rs11.getInt("brand_id");
						
						//System.out.println(PackID +" "+BrandID);
						
						if(PackID==2 && BrandID==12) {
						}else {
							
							double Rate=0;
							
							if(PackID==2) {
								Rate= Rate1500MLPET;
							}else if(PackID==3) {
								Rate= Rate1000MLPET;
							}else if(PackID==22) {
								Rate= Rate1750MLPET;
							}else if(PackID==14) {
								Rate= Rate250MLCAN;
							}
							//System.out.println("Rate"+ Rate);
							
						//	System.out.println("SQLLLLL ==> "+"SELECT * FROM sampling_percase where sampling_id="+SamplingID+" and package="+PackID+" and brand_id="+BrandID);
							ResultSet rs13 = s12.executeQuery("SELECT * FROM sampling_percase where sampling_id="+SamplingID+" and package="+PackID+" and brand_id="+BrandID );
							while(rs13.next()){
								
								 String updatePerCaseRate ="update sampling_percase set company_share="+Rate+" where sampling_id="+SamplingID+" and package="+PackID+" and brand_id="+BrandID ;
					        	// System.out.println(updatePerCaseRate);
					        	 int IFupdated = s13.executeUpdate(updatePerCaseRate); 	
					        	 if(IFupdated>0) {
					        		 
					        		 System.out.println("---------------------UPDATED--------------------------------");
					        	 }
							} 	
						}
					} 
					
		        }
           	   
           	     
	        //}
	        //New Addition on 4/26/2018 Ends 
	        //////////////////////////////////////////////////////////
	        
	         
           	  ds.commit();
	       
		    s13.close();
	        s12.close();
		    s11.close(); 
		    s10.close(); 
	        s9.close();
		    s8.close(); 
		    s7.close(); 
			s6.close();
		    s5.close();
		    s4.close(); 
		    s3.close(); 
			s2.close();
			
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 
				e.printStackTrace();
			}			
			finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		

		
	}

}
