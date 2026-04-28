package com.pbc.sampling;

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

import org.json.simple.JSONObject;

import com.pbc.outlet.OutletDashboard;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;


@WebServlet(description = "Executes sampling dashboard requests", urlPatterns = { "/sampling/SamplingDashboardExecute" })

public class SamplingDashboardExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SamplingDashboardExecute() {
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

		PrintWriter out = response.getWriter();
		JSONObject obj=new JSONObject();
		
		String month = request.getParameter("month");
		int status = Integer.parseInt(request.getParameter("status"));
		
		
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
						
						ResultSet rs2 = s.executeQuery("select barcode from sampling_monthly_approval where barcode = "+UUID);
						if (rs2.first()){
							UUIDExists = true;
						}else{
							UUIDExists = false;
						}
					
					}while(UUIDExists == true);
					
					
					boolean ApprovalAlreadyExists = false;
					long ExistingApprovalID = 0;
					ResultSet rs5 = s.executeQuery("select approval_id from sampling_monthly_approval where outlet_id = "+OutletID[i]+" and sampling_id = "+SamplingID[i]+" and request_id = "+RequestID[i]+" and month = "+month);
					if (rs5.first()){
						ApprovalAlreadyExists = true;
						ExistingApprovalID = rs5.getLong(1);
					}
					
					if (ApprovalAlreadyExists == true){
						SamplingPosting sp = new SamplingPosting();
						sp.deleteMonthlyAdjustment(ExistingApprovalID);
						sp.close();
						s.executeUpdate("delete from sampling_monthly_approval where approval_id = "+ExistingApprovalID);
					}
					
					s.executeUpdate("INSERT INTO sampling_monthly_approval (outlet_id,distributor_id, sampling_id, request_id, month, current_balance, sampling_amount, deduction_against_advance, payable, adjustment, net_payable, status_id, status_on, status_by, barcode) VALUES ("+OutletID[i]+","+DistributorID[i]+", "+SamplingID[i]+", "+RequestID[i]+", "+month+", "+CurrentBalance[i]+", "+SamplingAmount[i]+", "+DeductionAgainstAdvance[i]+", "+payable[i]+", "+adjustment[i]+", "+NetPayable[i]+", "+status+", now(), "+UserID+", "+UUID+"  )");
					
					long ApprovalID = 0;
					ResultSet rs3 = s.executeQuery("select LAST_INSERT_ID()");
					if (rs3.first()){
						ApprovalID = rs3.getLong(1);
					}					
					
					Date SamplingDate = new Date();
					ResultSet rs4 = s.executeQuery("select month from sampling_monthly_approval where approval_id = "+ApprovalID);
					if (rs4.first()){
						SamplingDate = rs4.getDate(1);
					}					
					
					if (status == 1 && DeductionAgainstAdvance[i] != 0){
						SamplingPosting sp = new SamplingPosting();
						sp.postMonthlyAdjustment(ApprovalID, SamplingID[i], OutletID[i], Long.parseLong(UserID), 2, DeductionAgainstAdvance[i], "Monthly Adjustment for " + Utilities.getDisplayDateMonthYearFormat(SamplingDate));
						sp.close();
					}
					
					// Per Case Patch
					if (status == 1){
						
						s.executeUpdate("delete from sampling_monthly_approval_percase where approval_id = "+ApprovalID);
						s.executeUpdate("delete from sampling_monthly_approval_fixed where approval_id = "+ApprovalID);
						
						OutletDashboard op = new OutletDashboard();
						op.setOutletID(OutletID[i]);
						
						double FixedSamplingAmount = op.getFixedDiscountAmount(SamplingDate);
						double FixedDeductionAgainstAdvance = op.getFixedDiscountDeductionAmount(SamplingDate);
						double FixedNetPayable = FixedSamplingAmount - FixedDeductionAgainstAdvance;
						
						if (FixedSamplingAmount != 0 || FixedDeductionAgainstAdvance != 0 || FixedNetPayable != 0){
							s.executeUpdate("INSERT INTO sampling_monthly_approval_fixed (approval_id, sampling_amount, deduction_against_advance, net_payable) VALUES ("+ApprovalID+", "+FixedSamplingAmount+", "+FixedDeductionAgainstAdvance+", "+FixedNetPayable+")");
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
								s.executeUpdate("INSERT INTO sampling_monthly_approval_percase (approval_id,package_id, brand_id, company_share, deduction_term, sales, sampling_amount, deduction_against_advance, net_payable) VALUES ("+ApprovalID+","+PackageID+", "+BrandID+", "+CompanyShare+", "+DeductionTerm+", "+Sales+", "+PerCaseSamplingAmount+", "+PerCaseDeductionAgainstAdvance+", "+PerCaseNetPayable+")");
							}
						}
						
						op.close();
					}
					
					
					s.close();
				}
				
			}
			
			//response.setContentType("application/json");
			
			//obj.put("success", "true");
			//obj.put("RequestID", ""+RequestID);
			
			ds.commit();
			
			ds.dropConnection();
			
			response.sendRedirect("../SamplingDashboard.jsp?"+request.getParameter("ReturnParams")+"&params="+java.net.URLEncoder.encode(request.getParameter("ReturnParams"), "UTF-8"));
			
			
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