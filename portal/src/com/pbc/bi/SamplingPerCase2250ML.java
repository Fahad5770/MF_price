package com.pbc.bi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

public class SamplingPerCase2250ML {
	static Datasource ds = new Datasource();
	public static void main(String[] args) {
		try {
			
			
			ds.createConnection();
			//ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			Date PresentDate=new Date();
			int SamplingID=0;
			int BrandID =0;
			int HandToHand=0;
			double AgencyShare=0.0;
			double DeductionTerm=0.0;
			double CompanyShare =0.0;
			Date AdvancevalidTo =null;
			Date AdvancevalidFrom=null;
				
			/////////////////////////////////////////Loop Of Sample
			
			String query1="SELECT s.sampling_id,sp.valid_to,sp.valid_from,sp.brand_id,sp.company_share,sp.hand_to_hand,sp.agency_share,sp.deduction_term FROM pep.sampling s join sampling_percase sp on s.sampling_id=sp.sampling_id where s.active=1 and sp.package=5 ";
			
			
			ResultSet rs = s.executeQuery(query1);
			
			while(rs.next()){
				
			
				SamplingID = rs.getInt("sampling_id");
				BrandID = rs.getInt("brand_id");
				AdvancevalidFrom =rs.getDate("valid_from");
				AdvancevalidTo=rs.getDate("valid_to");
				//if(AdvancevalidTo==null){
					//AdvancevalidTo=Utilities.parseDate("31/12/9999");
				//}
				
				CompanyShare=rs.getDouble("company_share");
				HandToHand=rs.getInt("hand_to_hand");;
				AgencyShare=rs.getDouble("agency_share");
				DeductionTerm=rs.getDouble("deduction_term");;
				
			    
			 //////////////////////////////////////////Sample Per Case Package 2250ML Query
				
				//if(AdvancevalidFrom==null ){
					//AdvancevalidFrom=PresentDate;
				//}
				
				String Query2="";
				
				 Query2="insert into sampling_percase (sampling_id,package,brand_id,agency_share,company_share,hand_to_hand,deduction_term, valid_from,valid_to) VALUES ("+SamplingID+",29,"+ BrandID+","+AgencyShare+","+CompanyShare+","+HandToHand+","+DeductionTerm+","+Utilities.getSQLDate(AdvancevalidFrom)+","+Utilities.getSQLDate(AdvancevalidTo)+")";
					
				//System.out.println(Query2);	
	            s2.executeUpdate(Query2);
				
			}
			
			
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
