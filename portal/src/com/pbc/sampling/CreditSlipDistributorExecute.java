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

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Credit Slip Distributor Execute", urlPatterns = { "/sampling/CreditSlipDistributorExecute" })
public class CreditSlipDistributorExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CreditSlipDistributorExecute() {
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
		
		long EditID = Utilities.parseLong(request.getParameter("EditID"));
		boolean isEditCase = false;
		
		int Month = Utilities.parseInt(request.getParameter("SamplingCreditSlipMonth"));
		int Year = Utilities.parseInt(request.getParameter("SamplingCreditSlipYear"));
		int Type = Utilities.parseInt(request.getParameter("SamplingCreditSlipType"));
		long DistributorID = Utilities.parseLong(request.getParameter("SamplingCreditSlipDistributorID"));
		
		String SlipDescription = Utilities.filterString(request.getParameter("SamplingCreditSlipDescription"), 1, 1000);
		String Description = Utilities.filterString(request.getParameter("SamplingCreditSlipDescriptionOfficial"), 1, 1000);
		long UniqueVoucherID = Utilities.parseLong(request.getParameter("UniqueVoucherID"));
		
		long OutletIDList[] = Utilities.parseLong(request.getParameterValues("OutletIDList"));
		String OutletNameList[] = Utilities.filterString(request.getParameterValues("OutletNameList"), 1, 100);
		double AmountList[] = Utilities.parseDouble(request.getParameterValues("AmountList"));
		
		
		if(EditID > 0){
			isEditCase = true;
		}
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		Date VoucherDate = new java.util.Date();
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			if(isEditCase){
				ResultSet rs_date_check = s.executeQuery("select created_on from sampling_credit_slip_distributor where id="+EditID);
				if(rs_date_check.first()){
					VoucherDate = rs_date_check.getDate(1);
				}
			}
			
			if( DateUtils.isSameDay(new java.util.Date(), VoucherDate) ){	// false only in editcase on date conflict
			
				ResultSet rs = s.executeQuery("select id from sampling_credit_slip_distributor where uvid="+UniqueVoucherID);
				if(rs.first()){
					obj.put("success", "false");
					obj.put("error", "Already Exists"); 
				}else{
					String SQL = "INSERT INTO `sampling_credit_slip_distributor`(`uvid`,`month`,`year`,`slip_description`,`internal_description`,`type_id`, `created_on`, `created_by`, `distributor_id`)VALUES("+UniqueVoucherID+", "+Month+", "+Year+", '"+SlipDescription+"', '"+Description+"', "+Type+", now(), "+UserID+", "+DistributorID+" )";
					if( isEditCase ){
						SQL = "UPDATE `sampling_credit_slip_distributor` SET `uvid`="+UniqueVoucherID+", `month` = "+Month+", `year` = "+Year+", `slip_description` = '"+SlipDescription+"', `internal_description` = '"+Description+"', `type_id` = "+Type+", `distributor_id`="+DistributorID+" WHERE `id` = "+EditID;
					}
					
					
					s2.executeUpdate(SQL);
					
					long CreditSlipID = 0;
					
					if(!isEditCase){
						ResultSet rs2 = s2.executeQuery("select LAST_INSERT_ID()");
						if(rs2.first()){
							CreditSlipID = rs2.getInt(1);
						}
					}else{
						CreditSlipID = EditID;
					}
					
					s2.executeUpdate("delete from `sampling_credit_slip_distributor_outlets` where id="+CreditSlipID);
					
					for(int i = 0; i < AmountList.length; i++){
						s2.executeUpdate("insert into `sampling_credit_slip_distributor_outlets`(id, outlet_id, amount, outlet_name) values ("+CreditSlipID+", "+OutletIDList[i]+", "+AmountList[i]+", '"+OutletNameList[i]+"') ");
					}
					
					obj.put("success", "true");
				}
			
			}else{
				obj.put("success", "false");
				obj.put("error", "You can only edit Today's voucher");
			}
			
			ds.commit();
			
			s2.close();
			s.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			try {
				
				ds.rollback();
				
				obj.put("success", "false");
				obj.put("error", e.toString());
				e.printStackTrace();
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
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
