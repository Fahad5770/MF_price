package com.pbc.sampling;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.outlet.OutletDashboard;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;


@WebServlet(description = "Monthly Discount Request Execute", urlPatterns = { "/sampling/MonthlyDiscountRequestExecute" })

public class MonthlyDiscountRequestExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MonthlyDiscountRequestExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
		}
		
		boolean isEditCase = false;
		long EditID = Utilities.parseLong(request.getParameter("EditID"));
		if(EditID > 0){
			isEditCase = true;
		}

		PrintWriter out = response.getWriter();
		JSONObject obj=new JSONObject();
		
		String month = request.getParameter("month");
		//int status = Integer.parseInt(request.getParameter("status"));
		
		
		/////////////int status = Utilities.parseInt(request.getParameter("status"));
		
		int status=1; //hard coded by zulqurnan (05-04-2017) due to some exception
		
		
		long RequestIDVal = Utilities.parseLong(request.getParameter("RequestIDVal"));
		String WorkflowStepRemarks = Utilities.filterString(request.getParameter("WorkflowStepRemarks"), 1, 100);
		int StepID = Utilities.parseInt(request.getParameter("StepID"));
		long NextStepID = Utilities.parseLong(request.getParameter("NextStepID"));
		int NextActionID = Utilities.parseInt(request.getParameter("NextActionID"));
		long NextUserID = Utilities.parseLong(request.getParameter("NextUserID"));
		boolean isLastStep = Utilities.parseBoolean(request.getParameter("isLastStep"));
		
		long SamplingAction[] = Utilities.parseLong(request.getParameterValues("SamplingAction"));
		
		long RequestID[] = Utilities.parseLong(request.getParameterValues("RequestID"));
		long OutletID[] = Utilities.parseLong(request.getParameterValues("OutletID"));
		long SamplingID[] = Utilities.parseLong(request.getParameterValues("SamplingID"));
		long DistributorID[] = Utilities.parseLong(request.getParameterValues("DistributorID"));
		
		
		double CurrentBalance[] = Utilities.parseDoubleAndFilterComma(request.getParameterValues("CurrentBalance"));
		double SamplingAmount[] = Utilities.parseDoubleAndFilterComma(request.getParameterValues("SamplingAmount"));
		double DeductionAgainstAdvance[] = Utilities.parseDoubleAndFilterComma(request.getParameterValues("DeductionAgainstAdvance"));
		double payable[] = Utilities.parseDoubleAndFilterComma(request.getParameterValues("payable"));
		double adjustment[] = Utilities.parseDoubleAndFilterComma(request.getParameterValues("adjustment"));
		double NetPayable[] = Utilities.parseDoubleAndFilterComma(request.getParameterValues("NetPayable"));
		
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s_master = ds.createStatement();
			
			String SQL = "";
			if(isEditCase){
				SQL = "update sampling_monthly_request_document set created_on=now(), created_by="+UserID+"  where id="+EditID;
			}else{
				SQL = "INSERT INTO `sampling_monthly_request_document`(`created_on`,`created_by`)VALUES(now(),"+UserID+")";
			}
			
			s_master.executeUpdate(SQL);
			
			long DocumentID = 0;
			
			if(!isEditCase){
				ResultSet rs_master = s_master.executeQuery("select LAST_INSERT_ID()");
				if (rs_master.first()){
					DocumentID = rs_master.getLong(1);
				}
			}else{
				DocumentID = EditID;
			}
			
			//System.out.println("delete from sampling_monthly_request where document_id="+DocumentID);
			
			s_master.executeUpdate("delete from sampling_monthly_request where document_id="+DocumentID);
			s_master.executeUpdate("delete from sampling_monthly_request_percase where document_id="+DocumentID);
			s_master.executeUpdate("delete from sampling_monthly_request_fixed where document_id="+DocumentID);
			
			for (int i = 0; i < SamplingAction.length; i++){
				
				
				if (SamplingAction[i] == 1){
					Statement s = ds.createStatement();
					
					String UUID = "";
					boolean UUIDExists = true;
					
					do{
					
						ResultSet rs = s.executeQuery("select concat(date_format(curdate(),'%d%m%y'), FLOOR(10000 + RAND() * 89999))");
						if (rs.first()){
							UUID = rs.getString(1);
						}
						
						ResultSet rs2 = s.executeQuery("select barcode from sampling_monthly_request where barcode = "+UUID);
						if (rs2.first()){
							UUIDExists = true;
						}else{
							UUIDExists = false;
						}
					
					}while(UUIDExists == true);
					
					
					boolean ApprovalAlreadyExists = false;
					long ExistingApprovalID = 0;
					ResultSet rs5 = s.executeQuery("select approval_id from sampling_monthly_request where outlet_id = "+OutletID[i]+" and sampling_id = "+SamplingID[i]+" and request_id = "+RequestID[i]+" and month = "+month);
					if (rs5.first()){
						ApprovalAlreadyExists = true;
						ExistingApprovalID = rs5.getLong(1);
					}
					
					if (ApprovalAlreadyExists == true){
						SamplingPosting sp = new SamplingPosting();
						sp.deleteMonthlyAdjustment(ExistingApprovalID);
						sp.close();
						//s.executeUpdate("delete from sampling_monthly_request where approval_id = "+ExistingApprovalID);
					}
					
					s.executeUpdate("INSERT INTO sampling_monthly_request (outlet_id,distributor_id, sampling_id, request_id, month, current_balance, sampling_amount, deduction_against_advance, payable, adjustment, net_payable, status_id, status_on, status_by, barcode, document_id) VALUES ("+OutletID[i]+","+DistributorID[i]+", "+SamplingID[i]+", "+RequestID[i]+", "+month+", "+CurrentBalance[i]+", "+SamplingAmount[i]+", "+DeductionAgainstAdvance[i]+", "+payable[i]+", "+adjustment[i]+", "+NetPayable[i]+", "+status+", now(), "+UserID+", "+UUID+" , "+DocumentID+" )");
					
					long ApprovalID = 0;
					ResultSet rs3 = s.executeQuery("select LAST_INSERT_ID()");
					if (rs3.first()){
						ApprovalID = rs3.getLong(1);
					}					
					
					Date SamplingDate = new Date();
					ResultSet rs4 = s.executeQuery("select month from sampling_monthly_request where approval_id = "+ApprovalID);
					if (rs4.first()){
						SamplingDate = rs4.getDate(1);
					}					
					/*
					if (status == 1 && DeductionAgainstAdvance[i] != 0){
						SamplingPosting sp = new SamplingPosting();
						sp.postMonthlyAdjustment(ApprovalID, SamplingID[i], OutletID[i], Long.parseLong(UserID), 2, DeductionAgainstAdvance[i], "Monthly Adjustment for " + Utilities.getDisplayDateMonthYearFormat(SamplingDate));
						sp.close();
					}
					*/
					// Per Case Patch
					if (status == 1){
						
						s.executeUpdate("delete from sampling_monthly_request_percase where approval_id = "+ApprovalID);
						s.executeUpdate("delete from sampling_monthly_request_fixed where approval_id = "+ApprovalID);
						
						OutletDashboard op = new OutletDashboard();
						op.setOutletID(OutletID[i]);
						
						double FixedSamplingAmount = op.getFixedDiscountAmount(SamplingDate);
						double FixedDeductionAgainstAdvance = op.getFixedDiscountDeductionAmount(SamplingDate);
						double FixedNetPayable = FixedSamplingAmount - FixedDeductionAgainstAdvance;
						
						if (FixedSamplingAmount != 0 || FixedDeductionAgainstAdvance != 0 || FixedNetPayable != 0){
							s.executeUpdate("INSERT INTO sampling_monthly_request_fixed (approval_id, sampling_amount, deduction_against_advance, net_payable, document_id) VALUES ("+ApprovalID+", "+FixedSamplingAmount+", "+FixedDeductionAgainstAdvance+", "+FixedNetPayable+", "+DocumentID+")");
						}
						
						ResultSet rs6 = op.getPerCaseDiscountResultSet(SamplingDate);
						while(rs6.next()){
							
							int PackageID = rs6.getInt("package");
							int BrandID = rs6.getInt("brand_id");
							double CompanyShare = rs6.getDouble("company_share");
							double DeductionTerm = rs6.getDouble("deduction_term");
							int Sales = rs6.getInt("qty");
							
							double PerCaseSamplingAmount = Sales * CompanyShare;
							double PerCaseDeductionAgainstAdvance = Sales * DeductionTerm;
							double PerCaseNetPayable = PerCaseSamplingAmount - PerCaseDeductionAgainstAdvance;
							
							if (Sales != 0 || PerCaseNetPayable != 0 || PerCaseSamplingAmount != 0 || PerCaseDeductionAgainstAdvance != 0){
								s.executeUpdate("INSERT INTO sampling_monthly_request_percase (approval_id,package_id, brand_id, company_share, deduction_term, sales, sampling_amount, deduction_against_advance, net_payable, document_id) VALUES ("+ApprovalID+","+PackageID+", "+BrandID+", "+CompanyShare+", "+DeductionTerm+", "+Sales+", "+PerCaseSamplingAmount+", "+PerCaseDeductionAgainstAdvance+", "+PerCaseNetPayable+", "+DocumentID+")");
							}
						}
						
						op.close();
					}
					
					
					
					s.close();
				}
				
			}
			
			Workflow wf = new Workflow();
			
			if(!isEditCase){
				long WorkFlowRequestID = wf.createRequest(2, Integer.parseInt(UserID), 3728, 2, "request raised");
				s_master.executeUpdate("update sampling_monthly_request_document set request_id="+WorkFlowRequestID+" where id="+DocumentID);
			}else{
				wf.doStepAction(RequestIDVal, StepID, isLastStep, NextUserID, NextActionID, WorkflowStepRemarks);
				
				if(isLastStep){
					
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/sampling/SamplingDashboardExecute");
					dispatcher.forward( request, response );
				}
				
			}
			
			wf.close();
			
			//response.setContentType("application/json");
			
			obj.put("success", "true");
			obj.put("RequestID", ""+RequestIDVal);
			
			s_master.close();
			
			ds.commit();
			
			ds.dropConnection();
			
			
			if(!isEditCase){
				response.sendRedirect("../MonthlyDiscountMain.jsp");
			}
			
		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
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