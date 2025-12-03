package com.pbc.empty;

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

import com.pbc.inventory.StockPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Empty Credit Issuance ", urlPatterns = { "/empty/EmptyCreditCreditLimitExecute" })
public class EmptyCreditCreditLimitExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public EmptyCreditCreditLimitExecute() {
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
		
		long DispatchID =Utilities.parseLong(request.getParameter("DispatchIDForInsertion"));
		
		int[] PackageID = Utilities.parseInt(request.getParameterValues("PackageID"));
		
		int[] RawCases=Utilities.parseInt(request.getParameterValues("DispatchReturnsMainFormRawCases"));
		int[] Units=Utilities.parseInt(request.getParameterValues("DispatchReturnsMainFormUnits"));
		long[] LiquidInMl=Utilities.parseLong(request.getParameterValues("DispatchReturnsMainFormLiquidInML"));
		long[] UnitPerSKU = Utilities.parseLong(request.getParameterValues("DispatchReturnsMainFormUnitPerSKU"));
		long[] TypeID = Utilities.parseLong(request.getParameterValues("EmptyCreditReceiptType"));
		
		String VehicleNumber = Utilities.filterString(request.getParameter("DispatchReturnsVehicleNumberhidden"), 1, 150);
		long SerialNo = Utilities.parseLong(request.getParameter("MainFormSerialNo"));
		
		long CreditType = Utilities.parseLong(request.getParameter("EmptyCreditTypeHidden"));		
		String AdjustmentReason = Utilities.filterString(request.getParameter("EmptyAccountAdjustmentReasonHidden"), 1, 300);
		
		
		
		int isEditHiddenFlag = Utilities.parseInt(request.getParameter("isEditHiddenFlag"));
		long ReceiptFromProductionEditID = Utilities.parseLong(request.getParameter("ReceiptFromProductionEditID"));
		long UniqueVoucherID = Utilities.parseLong(request.getParameter("UniqueVoucherID"));
		long DistributorID = Utilities.parseLong(request.getParameter("EmptyCreditReceiptMainDistributorHidden"));
		
		
		
		
		Date ECLimitStartDate = Utilities.parseDate(request.getParameter("EmptyCreditLimitStartDateHidden"));
		Date ECLimitEndDate = Utilities.parseDate(request.getParameter("EmptyCreditLimitEndDateHidden"));
		
		//System.out.println(ECLimitEndDate);
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();	
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			long MasterTableID=0;
			Date date = new Date();
			boolean IsSameDay=false;
			boolean IsReceivedValid=false;
			
			
			
			
			
			//System.out.println("insert into inventory_production_receipts(created_on,created_by,vehicle_no) values(now(),"+UserID+",'"+VehicleNumber+"')");
			if(isEditHiddenFlag==1){ //1=insertion
				//System.out.println("insert into inventory_production_receipts(created_on,created_by,vehicle_no,uvid, serial_no) values(now(),"+UserID+",'"+VehicleNumber+"',"+UniqueVoucherID+", "+SerialNo+")");
				///s.executeUpdate("insert into ec_empty_credit_limit_request(created_on,created_by,uvid, distributor_id,credit_type,reason,start_date,end_date) values(now(),"+UserID+","+UniqueVoucherID+", "+DistributorID+","+CreditType+",'"+AdjustmentReason+"',"+Utilities.getSQLDate(ECLimitStartDate)+","+Utilities.getSQLDate(ECLimitEndDate)+")");
				
				s.executeUpdate("insert into ec_empty_credit_limit_request(created_on,created_by,uvid, distributor_id,credit_type,reason,start_date,end_date) values(now(),"+UserID+","+UniqueVoucherID+", "+DistributorID+","+CreditType+",'"+AdjustmentReason+"',"+Utilities.getSQLDate(ECLimitStartDate)+","+Utilities.getSQLDate(ECLimitEndDate)+")");
				
				ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
				if(rs.first()){
					MasterTableID = rs.getInt(1); 
				}
				IsSameDay = true; //making this true so that our sub table query can run in insertion case
				IsReceivedValid=true;
				
				
			}
			
			
			//in update case
			
			
				if(PackageID !=null) {
					for(int i=0; i<PackageID.length; i++) {
						long TotalUnits =(RawCases[i]*UnitPerSKU[i]);
						long LiquInML = TotalUnits*LiquidInMl[i];
						////s.executeUpdate("insert into ec_empty_credit_limit_request (id,package_id,raw_cases,units,total_units,liquid_in_ml,type_id) values("+MasterTableID+","+PackageID[i]+","+RawCases[i]+",0,"+TotalUnits+","+LiquInML+",1)");  //type id=1 for sound - it is fixed
						
						s.executeUpdate("insert into ec_empty_credit_limit_request_products (id,package_id,raw_cases,units,total_units,liquid_in_ml,type_id) values("+MasterTableID+","+PackageID[i]+","+RawCases[i]+",0,"+TotalUnits+","+LiquInML+",1)");  //type id=1 for sound - it is fixed
					}
					
					obj.put("success", "true");
					obj.put("MasterTableID", MasterTableID);
				}
				
				
				long NextUserID = 0;
				
				///ResultSet rs1 = s.executeQuery("SELECT user_id FROM workflow_processes_steps where step_id=2 and process_id=9");
				ResultSet rs1 = s.executeQuery("SELECT snd_id FROM pep.common_distributors where distributor_id="+DistributorID);
				if(rs1.first()){
					NextUserID = rs1.getLong("snd_id");
				}
				if (NextUserID == 0 || NextUserID == 1){
					NextUserID = 2911;
				}
				
				
				Workflow wf = new Workflow();
				long WorkFlowRequestID = wf.createRequest(9, Integer.parseInt(UserID), NextUserID, 4, "Empty Credit Limit Request Raised");
				s.executeUpdate("update ec_empty_credit_limit_request set request_id="+WorkFlowRequestID+" where id="+MasterTableID);
			
			
			ds.commit();
				
			
			s.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		} finally {
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
