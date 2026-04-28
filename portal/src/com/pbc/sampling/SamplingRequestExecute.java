package com.pbc.sampling;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.employee.EmployeeHierarchy;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Executes Sampling Workflow Request", urlPatterns = { "/sampling/SamplingRequestExecute" })
public class SamplingRequestExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SamplingRequestExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();
		JSONObject obj=new JSONObject();
		
		
		boolean isLastStep = Boolean.parseBoolean(request.getParameter("isLastStep"));
		
		
		
		long SamplingID = Utilities.parseLong(Utilities.filterString(request.getParameter("SamplingID"), 0, MaxLength.CURRENCY));
		int StepID = Utilities.parseInt(Utilities.filterString(request.getParameter("StepID"), 0, MaxLength.CURRENCY));
		int NextStepID = Utilities.parseInt(Utilities.filterString(request.getParameter("NextStepID"), 0, MaxLength.CURRENCY));
		int NextActionID = Utilities.parseInt(Utilities.filterString(request.getParameter("NextActionID"), 0, MaxLength.CURRENCY));
		long NextUserID = Utilities.parseLong(Utilities.filterString(request.getParameter("NextUserID"), 0, MaxLength.EMPLOYEE_ID));
		long RequestID = Utilities.parseLong(Utilities.filterString(request.getParameter("RequestID"), 0, MaxLength.CURRENCY));

		long uvid = Utilities.parseLong(request.getParameter("uvid"));
		
		boolean isFirstStep = false;
		if (SamplingID == 0){
			isFirstStep = true;
		}
		
		
		long OutletID = Utilities.parseLong(Utilities.filterString(request.getParameter("OutletID"), 0, MaxLength.OUTLET_ID));
		String OutletName = Utilities.filterString(request.getParameter("OutletName"), 1, MaxLength.OUTLET_NAME);
		String BusinessType = Utilities.filterString(request.getParameter("BusinessType"), 1, MaxLength.OUTLET_BUSINESS_TYPE_NAME);
		String address = Utilities.filterString(request.getParameter("address"), 1, MaxLength.OUTLET_ADDRESS);
		String region = Utilities.filterString(request.getParameter("region"), 1, MaxLength.OUTLET_REGION_NAME);
		long asm = Utilities.parseLong(Utilities.filterString(request.getParameter("asm"), 0, MaxLength.EMPLOYEE_ID));
		long cr = Utilities.parseLong(Utilities.filterString(request.getParameter("cr"), 0, MaxLength.EMPLOYEE_ID));
		String market = Utilities.filterString(request.getParameter("market"), 1, MaxLength.OUTLET_MARKET_NAME);
		String vehicle = Utilities.filterString(request.getParameter("vehicle"), 1, MaxLength.VEHICLE_NO);
		
		double AdvanceCompanyShare = Utilities.parseDouble(Utilities.filterString(request.getParameter("AdvanceCompanyShare"), 0, MaxLength.CURRENCY));
		double AdvanceAgencyShare = Utilities.parseDouble(Utilities.filterString(request.getParameter("AdvanceAgencyShare"), 0, MaxLength.CURRENCY));
		
		Date AdvanceValidFrom = Utilities.parseDate(Utilities.filterString(request.getParameter("AdvanceValidFrom"), 0, MaxLength.DATE));
		Date AdvanceValidTo = Utilities.parseDate(Utilities.filterString(request.getParameter("AdvanceValidTo"), 0, MaxLength.DATE));
		
		
		double FixedCompanyShare = Utilities.parseDouble(Utilities.filterString(request.getParameter("FixedCompanyShare"), 0, MaxLength.CURRENCY));
		double FixedAgencyShare = Utilities.parseDouble(Utilities.filterString(request.getParameter("FixedAgencyShare"), 0, MaxLength.CURRENCY));
		double FixedDeductionTerm = Utilities.parseDouble(Utilities.filterString(request.getParameter("FixedDeductionTerm"), 0, MaxLength.CURRENCY));
		double FixedCompanyShareOP = Utilities.parseDouble(Utilities.filterString(request.getParameter("FixedCompanyShareOP"), 0, MaxLength.CURRENCY));
		double FixedAgencyShareOP = Utilities.parseDouble(Utilities.filterString(request.getParameter("FixedAgencyShareOP"), 0, MaxLength.CURRENCY));
		double FixedDeductionTermOP = Utilities.parseDouble(Utilities.filterString(request.getParameter("FixedDeductionTermOP"), 0, MaxLength.CURRENCY));
		Date FixedValidFrom = Utilities.parseDate(Utilities.filterString(request.getParameter("FixedValidFrom"), 0, MaxLength.DATE));
		Date FixedValidTo = Utilities.parseDate(Utilities.filterString(request.getParameter("FixedValidTo"), 0, MaxLength.DATE));
		
		
		
		int DetailRows = Utilities.parseInt(Utilities.filterString(request.getParameter("DetailRows"), 0, 2));
		
		int PerCasePackage[] = Utilities.parseInt(Utilities.filterString(Utilities.getSerialParameterValues("PerCasePackage", DetailRows, request), 0, MaxLength.CURRENCY));
		String PerCaseBrand[] = Utilities.filterString(Utilities.getSerialParameterValues("PerCaseBrand", DetailRows, request), 1, MaxLength.CURRENCY);
		//System.out.println(PerCaseBrand[0]);
		
		double PerCaseCompanyShare[] = Utilities.parseDouble(Utilities.filterString(Utilities.getSerialParameterValues("PerCaseCompanyShare", DetailRows, request), 0, MaxLength.CURRENCY));
		double PerCaseAgencyShare[] = Utilities.parseDouble(Utilities.filterString(Utilities.getSerialParameterValues("PerCaseAgencyShare", DetailRows, request), 0, MaxLength.CURRENCY));
		double PerCaseDeductionTerm[] = Utilities.parseDouble(Utilities.filterString(Utilities.getSerialParameterValues("PerCaseDeductionTerm", DetailRows, request), 0, MaxLength.CURRENCY));
		Date PerCaseValidFrom[] = Utilities.parseDate(Utilities.filterString(Utilities.getSerialParameterValues("PerCaseValidFrom", DetailRows, request), 0, MaxLength.DATE));
		Date PerCaseValidTo[] = Utilities.parseDate(Utilities.filterString(Utilities.getSerialParameterValues("PerCaseValidTo", DetailRows, request), 0, MaxLength.DATE));
		int PerCaseHandToHand[] = Utilities.parseInt(Utilities.filterString(Utilities.getSerialParameterValues("PerCaseHandToHand", DetailRows, request), 0, MaxLength.CURRENCY));
		
		int SalesThresholdApply = Utilities.parseInt(request.getParameter("SalesThresholdApply"));
		int FixedThresholdPercentage[] = Utilities.parseInt(Utilities.getSerialParameterValues("SalesThresholdPercentage", 5, request));
		double FixedThresholdSales[] = Utilities.parseDouble(Utilities.getSerialParameterValues("SalesThresholdSales", 5, request));
		double FixedThresholdDiscount[] = Utilities.parseDouble(Utilities.getSerialParameterValues("SalesThresholdDiscount", 5, request));
		
		String WorkflowStepRemarks = Utilities.filterString(request.getParameter("WorkflowStepRemarks"), 1, MaxLength.WORKFLOW_REMARKS);
		
		
		
		
		if (StepID == 2 && region != null){
			
			NextUserID = EmployeeHierarchy.getHeadOfSales().USER_ID;
			/*
			if (region.equals("RM5") || region.equals("RM8")){
				NextUserID = EmployeeHierarchy.getHeadOfSales().USER_ID;
			}else if (region.equals("RM2") || region.equals("RM3") || region.equals("RM6")){
				NextUserID = EmployeeHierarchy.getSDHead(4).USER_ID;
			}else{
				NextUserID = EmployeeHierarchy.getHeadOfSales().USER_ID;;
			}
			*/
		}		// End of Patch
		
		Datasource ds = new Datasource();
		
		try {
			
			//ds.createConnection();
			ds.createConnection();
			ds.startTransaction();
			 
			Workflow wf = new Workflow();
			
				wf.doStepAction(RequestID, StepID, isLastStep, NextUserID, NextActionID, WorkflowStepRemarks);
		
			wf.close();
			
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			
			String query = "INSERT INTO sampling (request_id, outlet_id, outlet_name, business_type, address, region, asm, cr, market, vehicle, advance_company_share, advance_agency_share, advance_valid_from, advance_valid_to, fixed_company_share, fixed_agency_share, fixed_deduction_term, fixed_valid_from, fixed_valid_to, active, activated_on, deactivated_on, fixed_company_share_offpeak, fixed_agency_share_offpeak, fixed_deduction_term_offpeak)"+
					"VALUES"+
					"("+RequestID+", '"+OutletID+"', '"+OutletName+"', '"+BusinessType+"', '"+address+"', '"+region+"', '"+asm+"', '"+cr+"', '"+market+"', '"+vehicle+"', '"+AdvanceCompanyShare+"', '"+AdvanceAgencyShare+"', "+Utilities.getSQLDate(AdvanceValidFrom)+", "+Utilities.getSQLDate(AdvanceValidTo)+", '"+FixedCompanyShare+"', '"+FixedAgencyShare+"', '"+FixedDeductionTerm+"', "+Utilities.getSQLDate(FixedValidFrom)+", "+Utilities.getSQLDate(FixedValidTo)+", 0, '2001-01-01','2001-01-01', '"+FixedCompanyShareOP+"','"+FixedAgencyShareOP+"','"+FixedDeductionTermOP+"')";
			
			if (SamplingID > 0){
				query = "REPLACE INTO sampling (sampling_id, request_id, outlet_id, outlet_name, business_type, address, region, asm, cr, market, vehicle, advance_company_share, advance_agency_share, advance_valid_from, advance_valid_to, fixed_company_share, fixed_agency_share, fixed_deduction_term, fixed_valid_from, fixed_valid_to, active, activated_on, deactivated_on, fixed_company_share_offpeak, fixed_agency_share_offpeak, fixed_deduction_term_offpeak)"+
						"VALUES"+
						"("+SamplingID+", "+RequestID+", '"+OutletID+"', '"+OutletName+"', '"+BusinessType+"', '"+address+"', '"+region+"', '"+asm+"', '"+cr+"', '"+market+"', '"+vehicle+"', '"+AdvanceCompanyShare+"', '"+AdvanceAgencyShare+"', "+Utilities.getSQLDate(AdvanceValidFrom)+", "+Utilities.getSQLDate(AdvanceValidTo)+", '"+FixedCompanyShare+"', '"+FixedAgencyShare+"', '"+FixedDeductionTerm+"', "+Utilities.getSQLDate(FixedValidFrom)+", "+Utilities.getSQLDate(FixedValidTo)+", 0, '2001-01-01','2001-01-01', '"+FixedCompanyShareOP+"','"+FixedAgencyShareOP+"','"+FixedDeductionTermOP+"')";
			}
			
			int updated = s.executeUpdate(query);
			
			response.setContentType("application/json");
			
			if (updated > 0){
				
				if (SamplingID > 0){
					
					s.executeUpdate("delete from sampling_percase where sampling_id = "+SamplingID);
					s.executeUpdate("delete from sampling_fixed_threshold where sampling_id = "+SamplingID);
					
				}else{
					ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
					if (rs.first()){
						SamplingID = rs.getLong(1);
					}
				}
				for (int i = 0; i < DetailRows; i++){
					
					
					if (PerCasePackage[i] != 0){
						//System.out.println("Brand "+PerCaseBrand[i]);
						 //if(Utilities.parseInt(PerCaseBrand[i])
						 //String B=PerCaseBrand[i];
						 String Endresult=PerCaseBrand[i];
						 if(Endresult.startsWith("a_") ) {
							// String result=PerCaseBrand[i];
							// System.out.println("Result"+Endresult);
								String[] split11 = Endresult.split("a_");
								PerCaseBrand[i]=split11[1];
								 //System.out.println("LRB TYPE::: "+PerCaseBrand[i]);
								
								 ResultSet rs22 = s2.executeQuery("SELECT distinct brand_id FROM pep.inventory_products_view join inventory_products_lrb_types on id=lrb_type_id where category_id=1 and is_visible=1 and package_id="+PerCasePackage[i]+" and lrb_type_id="+Utilities.parseInt(PerCaseBrand[i])+";");
									while(rs22.next()){
										 //System.out.println("REPLACE INTO sampling_percase (sampling_id,package,agency_share,company_share,deduction_term,valid_from,valid_to, brand_id,hand_to_hand) VALUES ("+SamplingID+", "+PerCasePackage[i]+", "+PerCaseAgencyShare[i]+", "+PerCaseCompanyShare[i]+", "+PerCaseDeductionTerm[i]+","+Utilities.getSQLDate(PerCaseValidFrom[i])+", "+Utilities.getSQLDate(PerCaseValidTo[i])+", "+rs22.getInt("brand_id")+", "+PerCaseHandToHand[i]+")");
										 s.executeUpdate("REPLACE INTO sampling_percase (sampling_id,package,agency_share,company_share,deduction_term,valid_from,valid_to, brand_id,hand_to_hand) VALUES ("+SamplingID+", "+PerCasePackage[i]+", "+PerCaseAgencyShare[i]+", "+PerCaseCompanyShare[i]+", "+PerCaseDeductionTerm[i]+","+Utilities.getSQLDate(PerCaseValidFrom[i])+", "+Utilities.getSQLDate(PerCaseValidTo[i])+", "+rs22.getInt("brand_id")+", "+PerCaseHandToHand[i]+")");
											
									}   
						 }else {
							 s.executeUpdate("REPLACE INTO sampling_percase (sampling_id,package,agency_share,company_share,deduction_term,valid_from,valid_to, brand_id,hand_to_hand) VALUES ("+SamplingID+", "+PerCasePackage[i]+", "+PerCaseAgencyShare[i]+", "+PerCaseCompanyShare[i]+", "+PerCaseDeductionTerm[i]+","+Utilities.getSQLDate(PerCaseValidFrom[i])+", "+Utilities.getSQLDate(PerCaseValidTo[i])+", "+Utilities.parseInt(PerCaseBrand[i])+", "+PerCaseHandToHand[i]+")");
						 }
						
						
					}
				}
				if (FixedThresholdSales[0] > 0 && SalesThresholdApply == 1){
					for (int i = 0; i < 5; i++){
							s.executeUpdate("REPLACE INTO sampling_fixed_threshold (sampling_id, percentage, converted_sales, discount) VALUES ("+SamplingID+", "+FixedThresholdPercentage[i]+", "+FixedThresholdSales[i]+", "+FixedThresholdDiscount[i]+")");
					}
				}
				// Deactivate Previous and Activate Current
				if (isLastStep == true){
					s.executeUpdate("update sampling set active = 0, deactivated_on = now(), deactivation_timestamp = now(), deactivated_by = "+UserID+" where outlet_id = "+OutletID+" and active = 1");
					s.executeUpdate("update sampling set active = 1, activated_on = now(), deactivated_on = cast('9999-09-09' as datetime) where sampling_id = "+SamplingID);
				}
				
				// Post Advance Sampling Transaction
				if (AdvanceCompanyShare > 0 && isLastStep == true){
					//uvid
					SamplingPosting sp = new SamplingPosting();
					sp.postNewAdvanceSampling(SamplingID, OutletID, Long.parseLong(UserID), AdvanceCompanyShare, "Advance Sampling", uvid);
					sp.close();
				}
				
				// Start a chat conversation
				if (isLastStep == false){
					WorkflowChat chat = new WorkflowChat(RequestID);
					if (WorkflowStepRemarks != null && WorkflowStepRemarks.length() > 0){
						chat.createConversation(Long.parseLong(UserID), NextUserID, WorkflowStepRemarks);
					}
					chat.close();
				}
								
				obj.put("success", "true");
				obj.put("RequestID", ""+RequestID);
			}else{
				
				
				
				obj.put("success", "false");
				obj.put("error", "Unknown Error");
			}
			ds.commit();
			s.close();
			
			ds.dropConnection();
			
		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
			
			obj.put("success", "false");
			obj.put("error", e.toString());
		}finally{
			
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		out.print(obj);
		out.close();
		
	}
	
}
