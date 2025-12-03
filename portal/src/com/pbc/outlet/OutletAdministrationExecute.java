package com.pbc.outlet;

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

import com.google.gson.JsonObject;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Outlet Administration Execute", urlPatterns = { "/outlet/OutletAdministrationExecute" })
public class OutletAdministrationExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private long OutletID;
	private long DistributorID[];
	private int ChannelID;
	private long NFCTagID;
	private long SAPCustomerID;
	private String ExceptionMessage;
	private int SegmentID;
	private int AgreedDailyAvgSales;
	private int VPOClassifications;
	private String BankAlfalahNumber;
	private int DiscountDisbursementID;
	private int IsFiler;
    public OutletAdministrationExecute() {
        super();
    }

    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
		
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();
		
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
	 	
		this.OutletID = Utilities.parseLong(request.getParameter("OutletID"));
		this.DistributorID = Utilities.parseLong(request.getParameterValues("OutletAdministrationDistributorID"));
		this.ChannelID = Utilities.parseInt(request.getParameter("ChannelID"));
		this.NFCTagID = Utilities.parseLong(request.getParameter("NfcTagID"));
		this.SAPCustomerID = Utilities.parseLong(request.getParameter("SAPCustomerID"));
		this.SegmentID = Utilities.parseInt(request.getParameter("SegmentID"));
		this.AgreedDailyAvgSales = Utilities.parseInt(request.getParameter("AgreedDailyAverageSales"));
		this.VPOClassifications = Utilities.parseInt(request.getParameter("VPOClassifications"));
		this.BankAlfalahNumber=Utilities.filterString(request.getParameter("Anumber"), 1, 100);
		
		//System.out.println("with this "+ this.BankAlfalahNumber);
		//System.out.println("filter"+Utilities.filterString(request.getParameter("Anumber"), 1, 100));
		//System.out.println("withoutFilert"+request.getParameter("Anumber"));
		
		this.DiscountDisbursementID = Utilities.parseInt(request.getParameter("discountdisbursement"));
		
		long SMSNumber = Utilities.parseLong(request.getParameter("smsnumber"));
		this.IsFiler=Utilities.parseInt(request.getParameter("isFiler"));
		//System.out.print("IsFiler "+IsFiler);
		
		if(this.OutletID > 0){
			
			boolean isSuccess = processRequest(SMSNumber,UserID);
			if(isSuccess){
				obj.put("success", "true");
			}else{
				obj.put("success", "false");
				obj.put("error", this.ExceptionMessage);
			}
			
		}else{
			obj.put("success", "false");
			obj.put("error", "Server could not be reached.");
		}
		
		out.print(obj);
		out.close();
		
	}
	
	public boolean processRequest(long SMSNumber,String UserID){
		
		Datasource ds = new Datasource();
		boolean isSuccess = false;
		
		try {
			
			ds.createConnection();
			Statement s = ds.createStatement();
			
			s.executeUpdate("delete from common_outlets_distributors where outlet_id="+this.OutletID);
			if(this.DistributorID != null){
				for(int i = 0; i < this.DistributorID.length; i++){
					s.executeUpdate("insert into common_outlets_distributors (outlet_id, distributor_id) values ("+this.OutletID+", "+this.DistributorID[i]+") ");
				}
			}
			
			//storing sms number
			s.executeUpdate("delete from common_outlets_contacts_sms where outlet_id="+this.OutletID);
			if(SMSNumber !=0){				
				s.executeUpdate("insert into common_outlets_contacts_sms (outlet_id, mobile_no,created_on,created_by) values ("+this.OutletID+", "+SMSNumber+",now(),'"+UserID+"') ");
				
			}
			
			
			String ChannelID = null;
			if(this.ChannelID > 0){
				ChannelID = this.ChannelID+"";
			}
			
			String NFCTagIDVal = null;
			if(NFCTagID > 0){
				NFCTagIDVal = NFCTagID+"";
			}
			
			String SAPCustomerIDVal = null;
			if(SAPCustomerID > 0){
				SAPCustomerIDVal = SAPCustomerID+"";
			}
			
			String SegmentIDVal = null;
			if(this.SegmentID > 0){
				SegmentIDVal = this.SegmentID+"";
			}
			
			if(this.ChannelID == 9 || this.ChannelID == 8){
				SegmentIDVal = "5";
			}
			
			String AgreedDailyAvgSalesVal = null;
			if(this.AgreedDailyAvgSales > 0){
				AgreedDailyAvgSalesVal = this.AgreedDailyAvgSales+"";
			}
			
			String VPOClassificationStr = null;
			if(this.VPOClassifications > 0){
				VPOClassificationStr = this.VPOClassifications + "";
			}
			
			String BankAlfalahNumberStr = null;
			
			//System.out.println(BankAlfalahNumberStr);
			if(!this.BankAlfalahNumber.equals("")){
				BankAlfalahNumberStr = this.BankAlfalahNumber + "";
			}
			
			String DiscountDisbursementStr = null;
			if(this.DiscountDisbursementID > 0){
				DiscountDisbursementStr = this.DiscountDisbursementID + "";
			}
			
			//
			
			//
			if(BankAlfalahNumberStr!=null){

				s.executeUpdate("update common_outlets set channel_id="+ChannelID+", nfc_tag_id="+NFCTagIDVal+", sap_customer_id="+SAPCustomerIDVal+", segment_id="+SegmentIDVal+", agreed_daily_average_sales="+AgreedDailyAvgSalesVal+", vpo_classifications_id="+VPOClassificationStr+",account_number_bank_alfalah='"+BankAlfalahNumberStr+"',discount_disbursement_id="+DiscountDisbursementStr+",is_filer="+IsFiler+"  where id="+this.OutletID);
					
			}
			else{

				s.executeUpdate("update common_outlets set channel_id="+ChannelID+", nfc_tag_id="+NFCTagIDVal+", sap_customer_id="+SAPCustomerIDVal+", segment_id="+SegmentIDVal+", agreed_daily_average_sales="+AgreedDailyAvgSalesVal+", vpo_classifications_id="+VPOClassificationStr+",account_number_bank_alfalah="+BankAlfalahNumberStr+",discount_disbursement_id="+DiscountDisbursementStr+",is_filer="+IsFiler+"  where id="+this.OutletID);
				
			}
			
			
			s.close();
			
			isSuccess = true;
			
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isSuccess = false;
			this.ExceptionMessage = e.toString();
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		return isSuccess;
		
	}
	
	
}
