package com.pbc.outlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

@WebServlet(description = "Outlet Administration Execute", urlPatterns = { "/outlet/UpdateOutletTaxInfoExecute" })
@SuppressWarnings("unused")
public class UpdateOutletTaxInfoExecute extends HttpServlet {
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
	private String NTN;
	private String STN;
	private int IsRegister;
	
    public UpdateOutletTaxInfoExecute() {
        super();
    }

    
	@SuppressWarnings("unchecked")
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
		this.NTN = Utilities.filterString(request.getParameter("NtnID"), 1, 100);
		this.STN = Utilities.filterString(request.getParameter("StnID"), 1, 100);		
		this.DiscountDisbursementID = Utilities.parseInt(request.getParameter("discountdisbursement"));
		
		long SMSNumber = Utilities.parseLong(request.getParameter("smsnumber"));
		this.IsFiler=Utilities.parseInt(request.getParameter("isFiler"));
		this.IsRegister=Utilities.parseInt(request.getParameter("isRegisterID"));

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

			s.executeUpdate("update common_outlets set NTN='"+NTN+"', STN='"+STN+"', is_register="+IsRegister+", is_filer="+IsFiler+" where id="+this.OutletID);

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
