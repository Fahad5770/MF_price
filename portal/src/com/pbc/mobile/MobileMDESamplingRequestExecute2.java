package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.employee.EmployeeHierarchy;
import com.pbc.sampling.SamplingPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Executes Sampling Workflow Request", urlPatterns = { "/mobile/MobileMDESamplingRequestExecute2" })
public class MobileMDESamplingRequestExecute2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MobileMDESamplingRequestExecute2() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	System.out.println("Hello I am in MobileMDESamplingRequestExecute2!!!");
		
		HttpSession session = request.getSession();
		
		PrintWriter out = response.getWriter();
		
		
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		String UserID=Utilities.filterString(mr.getParameter("UserID"), 1, 6);
		
		response.setContentType("application/json");
		JSONObject obj=new JSONObject();
		
		boolean isLastStep = Boolean.parseBoolean(mr.getParameter("isLastStep"));
		
		
		
		long SamplingID = Utilities.parseLong(Utilities.filterString(mr.getParameter("SamplingID"), 0, MaxLength.CURRENCY));
		int StepID = Utilities.parseInt(Utilities.filterString(mr.getParameter("StepID"), 0, MaxLength.CURRENCY));
		int NextStepID = Utilities.parseInt(Utilities.filterString(mr.getParameter("NextStepID"), 0, MaxLength.CURRENCY));
		int NextActionID = Utilities.parseInt(Utilities.filterString(mr.getParameter("NextActionID"), 1, MaxLength.CURRENCY));
		long NextUserID = Utilities.parseLong(Utilities.filterString(mr.getParameter("NextUserID"), 0, MaxLength.EMPLOYEE_ID));
		long RequestID = Utilities.parseLong(Utilities.filterString(mr.getParameter("RequestID"), 0, MaxLength.CURRENCY));

		long uvid = Utilities.parseLong(mr.getParameter("uvid"));
		System.out.println("uvid : "+uvid);
		
		boolean isFirstStep = false;
		if (SamplingID == 0){
			isFirstStep = true;
		}
		
		
		long OutletID = Utilities.parseLong(Utilities.filterString(mr.getParameter("OutletID"), 0, MaxLength.OUTLET_ID));
		String OutletName = Utilities.filterString(mr.getParameter("OutletName"), 1, MaxLength.OUTLET_NAME);
		String SummaryComments = Utilities.filterString(mr.getParameter("SummaryComments"), 1, 1000);
		String BusinessType = Utilities.filterString(mr.getParameter("BusinessType"), 1, MaxLength.OUTLET_BUSINESS_TYPE_NAME);
		String address = Utilities.filterString(mr.getParameter("address"), 1, MaxLength.OUTLET_ADDRESS);
		String region = Utilities.filterString(mr.getParameter("region"), 1, MaxLength.OUTLET_REGION_NAME);
		long asm = Utilities.parseLong(Utilities.filterString(mr.getParameter("asm"), 0, MaxLength.EMPLOYEE_ID));
		long cr = Utilities.parseLong(Utilities.filterString(mr.getParameter("cr"), 0, MaxLength.EMPLOYEE_ID));
		String market = Utilities.filterString(mr.getParameter("market"), 1, MaxLength.OUTLET_MARKET_NAME);
		String vehicle = Utilities.filterString(mr.getParameter("vehicle"), 1, MaxLength.VEHICLE_NO);
		long OneDaySale = Utilities.parseLong(Utilities.filterString(mr.getParameter("TargetPeriodOneDaySale"), 0, MaxLength.CURRENCY));
		
		
		double NetTradeSpend = Utilities.parseDouble(mr.getParameter("NetTradeSpend"));
		
		
		System.out.println("NetTradeSpend" +NetTradeSpend);
		
		
		//new addition
		/*long OwnerName = Utilities.parseLong(Utilities.filterString(mr.getParameter(""), 0, MaxLength.EMPLOYEE_ID));
		long OwnerConact = Utilities.parseLong(Utilities.filterString(mr.getParameter(""), 0, MaxLength.EMPLOYEE_ID));
		long PSR_ID = Utilities.parseLong(Utilities.filterString(mr.getParameter(""), 0, MaxLength.EMPLOYEE_ID));
		long PJP_ID = Utilities.parseLong(Utilities.filterString(mr.getParameter(""), 0, MaxLength.EMPLOYEE_ID));
		long Distributor_ID =Utilities.parseLong(Utilities.filterString(mr.getParameter(""), 0, MaxLength.EMPLOYEE_ID));
		*/
		
		
		
		double AdvanceCompanyShare = Utilities.parseDouble(Utilities.filterString(mr.getParameter("AdvanceCompanyShare"), 0, MaxLength.CURRENCY));
		double AdvanceAgencyShare = Utilities.parseDouble(Utilities.filterString(mr.getParameter("AdvanceAgencyShare"), 0, MaxLength.CURRENCY));
		
		Date AdvanceValidFrom = Utilities.parseDate(Utilities.filterString(mr.getParameter("AdvanceValidFrom"), 0, MaxLength.DATE));
		Date AdvanceValidTo = Utilities.parseDate(Utilities.filterString(mr.getParameter("AdvanceValidTo"), 0, MaxLength.DATE));
		
		
		double FixedCompanyShare = Utilities.parseDouble(Utilities.filterString(mr.getParameter("FixedCompanyShare"), 0, MaxLength.CURRENCY));
		double FixedAgencyShare = Utilities.parseDouble(Utilities.filterString(mr.getParameter("FixedAgencyShare"), 0, MaxLength.CURRENCY));
		double FixedDeductionTerm = Utilities.parseDouble(Utilities.filterString(mr.getParameter("FixedDeductionTerm"), 0, MaxLength.CURRENCY));
		double FixedCompanyShareOP = Utilities.parseDouble(Utilities.filterString(mr.getParameter("FixedCompanyShareOP"), 0, MaxLength.CURRENCY));
		double FixedAgencyShareOP = Utilities.parseDouble(Utilities.filterString(mr.getParameter("FixedAgencyShareOP"), 0, MaxLength.CURRENCY));
		double FixedDeductionTermOP = Utilities.parseDouble(Utilities.filterString(mr.getParameter("FixedDeductionTermOP"), 0, MaxLength.CURRENCY));
		Date FixedValidFrom = Utilities.parseDate(Utilities.filterString(mr.getParameter("FixedValidFrom"), 0, MaxLength.DATE));
		Date FixedValidTo = Utilities.parseDate(Utilities.filterString(mr.getParameter("FixedValidTo"), 0, MaxLength.DATE));
		
		
		
		int DetailRows = Utilities.parseInt(Utilities.filterString(mr.getParameter("DetailRows"), 0, 2));
		
		//Per Case variables
		int PerCasePackage[] = Utilities.parseInt(Utilities.filterString(mr.getParameterValues("PerCasePackage"), 0, MaxLength.CURRENCY));
		//System.out.println("Package"+Arrays.toString(PerCasePackage));
		String PerCaseBrand[] = Utilities.filterString(mr.getParameterValues("PerCaseBrand"), 1, 300);
		double PerDiscountCaseCompanyShare[] = Utilities.parseDouble(Utilities.filterString(mr.getParameterValues("PerDiscountCaseCompanyShare"), 0, MaxLength.CURRENCY));
		//System.out.println("PerDiscountCaseCompanyShare"+Arrays.toString(PerDiscountCaseCompanyShare));
		double PerCaseAgencyShare[] = Utilities.parseDouble(Utilities.filterString(mr.getParameterValues("PerCaseAgencyShare"), 0, MaxLength.CURRENCY));
		double PerCaseDeductionTerm[] = Utilities.parseDouble(Utilities.filterString(mr.getParameterValues("PerCaseDeductionTerm"), 0, MaxLength.CURRENCY));
		Date PerCaseValidFrom[] = Utilities.parseDate(Utilities.filterString(mr.getParameterValues("PerCaseValidFrom"), 0, MaxLength.DATE));
		Date PerCaseValidTo[] = Utilities.parseDate(Utilities.filterString(mr.getParameterValues("PerCaseValidTo"), 0, MaxLength.DATE));
		int PerCaseHandToHand[] = Utilities.parseInt(Utilities.filterString(mr.getParameterValues("PerCaseHandToHand"), 0, MaxLength.CURRENCY));
		double PerCaseVolume[] = Utilities.parseDouble(Utilities.filterString(mr.getParameterValues("PerCaseVolume"), 0, MaxLength.CURRENCY));
		System.out.println("PerCaseVolume "+Arrays.toString(PerCaseVolume));
		System.out.println("PerCaseHandToHand "+Arrays.toString(PerCaseHandToHand));
		
		
		
	
		
	
		
		
		int SalesThresholdApply = Utilities.parseInt(mr.getParameter("SalesThresholdApply"));
		//System.out.println("SalesThresholdApply"+SalesThresholdApply);
		 //Utilities.parseInt(Utilities.getSerialParameterValues("SalesThresholdPercentage", 5, request));
		
		/*
		int FixedThresholdPercentage[] = Utilities.parseInt(mr.getParameterValues("SalesThresholdPercentage"));
		
		
		
		double FixedThresholdSales[] = Utilities.parseDouble(mr.getParameterValues("SalesThresholdSales"));
		
		
		double FixedThresholdDiscount[] = Utilities.parseDouble(mr.getParameterValues("SalesThresholdDiscount"));
		*/
		
		String WorkflowStepRemarks = Utilities.filterString(mr.getParameter("WorkflowStepRemarks"), 1, MaxLength.WORKFLOW_REMARKS);
		
		// Temporary patch as Usman Bhatti left
		//if (isFirstStep == true && region != null){
		
		
		
		
		if (StepID == 2 && region != null){
			
			NextUserID = EmployeeHierarchy.getHeadOfSales().USER_ID;
			/*
			if (region.equals("RM1") || region.equals("RM7")){
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
			
			if (RequestID == 0){
			//	System.out.println("Workflow called - "+UserID+" "+NextUserID+" "+NextActionID+" "+WorkflowStepRemarks);
				
				RequestID = wf.createRequest(1, Integer.parseInt(UserID), NextUserID, NextActionID, WorkflowStepRemarks);
			}else{
				wf.doStepAction(RequestID, StepID, isLastStep, NextUserID, NextActionID, WorkflowStepRemarks);
			}
			
			wf.close();
			
			
			Statement s = ds.createStatement();
			Statement s22=ds.createStatement();
			Statement s23=ds.createStatement();
			
			//Inserting cr and asm as proper values (Orignal Query)
			
			/*
			 * String query = "INSERT INTO sampling (request_id, outlet_id, outlet_name, business_type, address, region, asm, cr, market, vehicle, advance_company_share, advance_agency_share, advance_valid_from, advance_valid_to, fixed_company_share, fixed_agency_share, fixed_deduction_term, fixed_valid_from, fixed_valid_to, active, activated_on, deactivated_on, fixed_company_share_offpeak, fixed_agency_share_offpeak, fixed_deduction_term_offpeak)"+
					"VALUES"+
					"("+RequestID+", '"+OutletID+"', '"+OutletName+"', '"+BusinessType+"', '"+address+"', '"+region+"', '"+asm+"', '"+cr+"', '"+market+"', '"+vehicle+"', '"+AdvanceCompanyShare+"', '"+AdvanceAgencyShare+"', "+Utilities.getSQLDate(AdvanceValidFrom)+", "+Utilities.getSQLDate(AdvanceValidTo)+", '"+FixedCompanyShare+"', '"+FixedAgencyShare+"', '"+FixedDeductionTerm+"', "+Utilities.getSQLDate(FixedValidFrom)+", "+Utilities.getSQLDate(FixedValidTo)+", 0, '2001-01-01','2001-01-01', '"+FixedCompanyShareOP+"','"+FixedAgencyShareOP+"','"+FixedDeductionTermOP+"')";
			
			
			if (SamplingID > 0){
				query = "REPLACE INTO sampling (sampling_id, request_id, outlet_id, outlet_name, business_type, address, region, asm, cr, market, vehicle, advance_company_share, advance_agency_share, advance_valid_from, advance_valid_to, fixed_company_share, fixed_agency_share, fixed_deduction_term, fixed_valid_from, fixed_valid_to, active, activated_on, deactivated_on, fixed_company_share_offpeak, fixed_agency_share_offpeak, fixed_deduction_term_offpeak)"+
						"VALUES"+
						"("+SamplingID+", "+RequestID+", '"+OutletID+"', '"+OutletName+"', '"+BusinessType+"', '"+address+"', '"+region+"', '"+asm+"', '"+cr+"', '"+market+"', '"+vehicle+"', '"+AdvanceCompanyShare+"', '"+AdvanceAgencyShare+"', "+Utilities.getSQLDate(AdvanceValidFrom)+", "+Utilities.getSQLDate(AdvanceValidTo)+", '"+FixedCompanyShare+"', '"+FixedAgencyShare+"', '"+FixedDeductionTerm+"', "+Utilities.getSQLDate(FixedValidFrom)+", "+Utilities.getSQLDate(FixedValidTo)+", 0, '2001-01-01','2001-01-01', '"+FixedCompanyShareOP+"','"+FixedAgencyShareOP+"','"+FixedDeductionTermOP+"')";
			}
			
			*/
			
			//Inserting cr and asm  as null for temporary purposes
			String query = "INSERT INTO sampling (request_id, outlet_id, outlet_name, business_type, address, region, market, vehicle, advance_company_share, advance_agency_share, advance_valid_from, advance_valid_to, fixed_company_share, fixed_agency_share, fixed_deduction_term, fixed_valid_from, fixed_valid_to, active, activated_on, deactivated_on, fixed_company_share_offpeak, fixed_agency_share_offpeak, fixed_deduction_term_offpeak,uvid,comments,one_day_sale,net_trade_spend)"+
					"VALUES"+
					"("+RequestID+", '"+OutletID+"', '"+OutletName+"', '"+BusinessType+"', '"+address+"', '"+region+"','"+market+"', '"+vehicle+"', '"+AdvanceCompanyShare+"', '"+AdvanceAgencyShare+"', "+Utilities.getSQLDate(AdvanceValidFrom)+", "+Utilities.getSQLDate(AdvanceValidTo)+", '"+FixedCompanyShare+"', '"+FixedAgencyShare+"', '"+FixedDeductionTerm+"', "+Utilities.getSQLDate(FixedValidFrom)+", "+Utilities.getSQLDate(FixedValidTo)+", 0, '2001-01-01','2001-01-01', '"+FixedCompanyShareOP+"','"+FixedAgencyShareOP+"','"+FixedDeductionTermOP+"',"+uvid+",'"+SummaryComments+"',"+OneDaySale+","+NetTradeSpend+")";  
			
			if (SamplingID > 0){
				query = "REPLACE INTO sampling (sampling_id, request_id, outlet_id, outlet_name, business_type, address, region,  market, vehicle, advance_company_share, advance_agency_share, advance_valid_from, advance_valid_to, fixed_company_share, fixed_agency_share, fixed_deduction_term, fixed_valid_from, fixed_valid_to, active, activated_on, deactivated_on, fixed_company_share_offpeak, fixed_agency_share_offpeak, fixed_deduction_term_offpeak,uvid,comments,one_day_sale,net_trade_spend)"+
						"VALUES"+
						"("+SamplingID+", "+RequestID+", '"+OutletID+"', '"+OutletName+"', '"+BusinessType+"', '"+address+"', '"+region+"', '"+market+"', '"+vehicle+"', '"+AdvanceCompanyShare+"', '"+AdvanceAgencyShare+"', "+Utilities.getSQLDate(AdvanceValidFrom)+", "+Utilities.getSQLDate(AdvanceValidTo)+", '"+FixedCompanyShare+"', '"+FixedAgencyShare+"', '"+FixedDeductionTerm+"', "+Utilities.getSQLDate(FixedValidFrom)+", "+Utilities.getSQLDate(FixedValidTo)+", 0, '2001-01-01','2001-01-01', '"+FixedCompanyShareOP+"','"+FixedAgencyShareOP+"','"+FixedDeductionTermOP+"',"+uvid+",'"+SummaryComments+"',"+OneDaySale+","+NetTradeSpend+")";
			}
			
		//	System.out.println(query);
			int updated=0;
			try {
			 updated = s.executeUpdate(query);
			}catch (SQLException e) {
			    System.err.println("SQL exception: " + e.getMessage());
			    System.exit(1);
			}
			
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
				
				//System.out.println("P I"+PerCasePackage.length);
				for (int i = 0; i < PerCasePackage.length; i++){
				//	System.out.println("P[ I]"+PerCasePackage[i] );
					if (PerCasePackage[i] != 0){
						
						 String Endresult=PerCaseBrand[i];
						
						// System.out.println("with A Result"+Endresult);
						 if(Endresult.startsWith("a_") ) {
							
							String[] split11 = Endresult.split("a_");
							PerCaseBrand[i]=split11[1];
							
							 
							//System.out.println("Result "+Endresult+" hhhhashd = "+PerCaseBrand[i]+" ===== "+PerCasePackage[i]);
							
							
							String mutlipleBrandQuery23="SELECT brand_id FROM pep.inventory_products_view where package_id="+PerCasePackage[i]+" and lrb_type_id="+Utilities.parseInt(PerCaseBrand[i])+" and category_id=1 and is_visible=1";
							//System.out.println("Upper Query === "+mutlipleBrandQuery23);
							ResultSet rs22 = s22.executeQuery(mutlipleBrandQuery23);
									 
								while(rs22.next()){
									
									String Q = "REPLACE INTO sampling_percase (sampling_id,package,agency_share,company_share,deduction_term,valid_from,valid_to, brand_id,hand_to_hand,estimated_volume) VALUES ("+SamplingID+", "+PerCasePackage[i]+", "+PerCaseAgencyShare[i]+", "+PerDiscountCaseCompanyShare[i]+", "+PerCaseDeductionTerm[i]+","+Utilities.getSQLDate(PerCaseValidFrom[i])+", "+Utilities.getSQLDate(PerCaseValidTo[i])+", "+rs22.getInt("brand_id")+", "+PerCaseHandToHand[i]+","+PerCaseVolume[i]+")";
									
									
									//System.out.println(Q);
									
									s23.executeUpdate("REPLACE INTO sampling_percase (sampling_id,package,agency_share,company_share,deduction_term,valid_from,valid_to, brand_id,hand_to_hand,estimated_volume) VALUES ("+SamplingID+", "+PerCasePackage[i]+", "+PerCaseAgencyShare[i]+", "+PerDiscountCaseCompanyShare[i]+", "+PerCaseDeductionTerm[i]+","+Utilities.getSQLDate(PerCaseValidFrom[i])+", "+Utilities.getSQLDate(PerCaseValidTo[i])+", "+rs22.getInt("brand_id")+", "+PerCaseHandToHand[i]+", "+PerCaseVolume[i]+")");
										
							} 
									
						 }else {
							// System.out.println("worked part");
							
							 int a=s23.executeUpdate("REPLACE INTO sampling_percase (sampling_id,package,agency_share,company_share,deduction_term,valid_from,valid_to, brand_id,hand_to_hand,estimated_volume) VALUES ("+SamplingID+", "+PerCasePackage[i]+", "+PerCaseAgencyShare[i]+", "+PerDiscountCaseCompanyShare[i]+", "+PerCaseDeductionTerm[i]+","+Utilities.getSQLDate(PerCaseValidFrom[i])+", "+Utilities.getSQLDate(PerCaseValidTo[i])+", "+Utilities.parseInt(PerCaseBrand[i])+", "+PerCaseHandToHand[i]+", "+PerCaseVolume[i]+")");
							 if(a>0) {
								// System.out.println("worked part");
							 }else {
								// System.out.println("not part");
							 }
						 }
						
						
					}
					/*
					if (PerCasePackage[i] != 0){
						
						System.out.println("REPLACE INTO sampling_percase (sampling_id,package,agency_share,company_share,deduction_term,valid_from,valid_to, brand_id,hand_to_hand) VALUES ("+SamplingID+", "+PerCasePackage[i]+", "+PerCaseAgencyShare[i]+", "+PerDiscountCaseCompanyShare[i]+", "+PerCaseDeductionTerm[i]+","+Utilities.getSQLDate(PerCaseValidFrom[i])+", "+Utilities.getSQLDate(PerCaseValidTo[i])+", "+PerCaseBrand[i]+", "+PerCaseHandToHand[i]+")");
						
						s.executeUpdate("REPLACE INTO sampling_percase (sampling_id,package,agency_share,company_share,deduction_term,valid_from,valid_to, brand_id,hand_to_hand) VALUES ("+SamplingID+", "+PerCasePackage[i]+", "+PerCaseAgencyShare[i]+", "+PerDiscountCaseCompanyShare[i]+", "+PerCaseDeductionTerm[i]+","+Utilities.getSQLDate(PerCaseValidFrom[i])+", "+Utilities.getSQLDate(PerCaseValidTo[i])+", "+PerCaseBrand[i]+", "+PerCaseHandToHand[i]+")");
					}
					*/
				}
			
				int TOTTypeID[] = Utilities.parseInt(Utilities.filterString(mr.getParameterValues("PerCaseTOTTypeID"), 0, 20));
				
				if(TOTTypeID!=null) {
				//	String TOTTypeName[] = Utilities.filterString(mr.getParameterValues("PerCaseTOTTypeName"), 1, 300);
					int TOTSize[] = Utilities.parseInt(Utilities.filterString(mr.getParameterValues("PerDiscountCaseTOTSize"), 0, 50));
					//System.out.println("PerDiscountCaseCompanyShare"+Arrays.toString(PerDiscountCaseCompanyShare));
					double TOTCost[] = Utilities.parseDouble(Utilities.filterString(mr.getParameterValues("PerDiscountCaseTOTCost"), 0,50));
					
				
					//ToT Records Insertion
					for (int i = 0; i < TOTTypeID.length; i++){
							if (TOTTypeID.length > 0){
								
								 int a=s23.executeUpdate("INSERT INTO sampling_tot (sampling_id,type_id,tot_size,tot_cost) VALUES ("+SamplingID+", "+TOTTypeID[i]+", "+TOTSize[i]+", "+TOTCost[i]+")");
								 if(a>0) {
									// System.out.println("worked part");
								 }else {
									// System.out.println("not part");
								 }	
							
							}
						
					}
				}
				
				
				
				//Signage variables
				int SignageTypeID[] = Utilities.parseInt(Utilities.filterString(mr.getParameterValues("PerCaseSignageTypeID"), 0, 20));
				///System.out.println("SignageTypeID ID"+Arrays.toString(SignageTypeID));
				if(SignageTypeID!=null) {
				
					String SignageTypeName[] = Utilities.filterString(mr.getParameterValues("PerCaseSignageTypeName"), 1, 300);
					int SignageSize[] = Utilities.parseInt(Utilities.filterString(mr.getParameterValues("PerDiscountCaseSignageSize"), 0, 50));
					//System.out.println("PerDiscountCaseCompanyShare"+Arrays.toString(PerDiscountCaseCompanyShare));
					double SignageCost[] = Utilities.parseDouble(Utilities.filterString(mr.getParameterValues("PerDiscountCaseSignageCost"), 0,50));
					//Signage Records Insertion
					for (int i = 0; i < SignageTypeID.length; i++){
						if (SignageTypeID.length > 0){
							
							 int a=s23.executeUpdate("INSERT INTO sampling_signage (sampling_id,signage_id,signage_size,signage_cost) VALUES ("+SamplingID+", "+SignageTypeID[i]+", "+SignageSize[i]+", "+SignageCost[i]+")");
							 if(a>0) {
								// System.out.println("worked part");
							 }else {
								// System.out.println("not part");
							 }	
						
						}
					
					}
				}
				
				
				////////////////////////////////////////////////////////////////////////////////////
				//For testing Purpose commenting starts
				/*
				if (FixedThresholdSales[0] > 0 && SalesThresholdApply == 1){
					for (int i = 0; i < 5; i++){
							System.out.println("Threshoold query"+"REPLACE INTO sampling_fixed_threshold (sampling_id, percentage, converted_sales, discount) VALUES ("+SamplingID+", "+FixedThresholdPercentage[i]+", "+FixedThresholdSales[i]+", "+FixedThresholdDiscount[i]+")");
							s.executeUpdate("REPLACE INTO sampling_fixed_threshold (sampling_id, percentage, converted_sales, discount) VALUES ("+SamplingID+", "+FixedThresholdPercentage[i]+", "+FixedThresholdSales[i]+", "+FixedThresholdDiscount[i]+")");
					}
				}
				*/
				//For testing Purpose commenting ends
				////////////////////////////////////////////////////////////////////////////////////
				
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
				
			//System.out.println("NOOOO");
				
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