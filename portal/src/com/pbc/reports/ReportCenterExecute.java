package com.pbc.reports;

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

import com.pbc.outlet.Outlet;
import com.pbc.util.Datasource;
import com.pbc.util.UserAccess;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;
import java.util.*;


@WebServlet(description = "Report Center Execute ", urlPatterns = { "/reports/ReportCenterExecute" })
public class ReportCenterExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ReportCenterExecute() {
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
		
		long SelectedSKU[]=null;
		long SelectedChannels[]=null;
		long SelectedCity[]=null;
		long SelectedPackages[]=null;
		long SelectedBrands[]=null;
		long SelectedDistributor[]=null;
		long SelectedFromDistributor[]=null;
		long SelectedToDistributor[]=null;
		long SelectedOrderBookers[]=null;
		long SelectedVehicles[]=null;
		long SelectedEmployees[]=null;
		long SelectedOutlets[]=null;
		long SelectedPJP[]=null;
		long SelectedHOD[]=null;
		long SelectedRSM[]=null;
		long SelectedSM[]=null;
		long SelectedTDM[]=null;
		long SelectedASM[]=null;
		long SelectedSalesType[]=null;
		String SelectedOutletType[]=null;
		long SelectedOutletsManual[]=null;
		String SelectedComplaintType[]=null;
		long SelectedRegion[]=null;
		long SelectedAccountType[]=null;
		long SelectedCashInstruments[]=null;
		long SelectedCashInstrumentsMultiple[]=null;
		long SelectedGlEmployee[]=null;
		String SelectedPrimaryInvoiceStatus[]=null;
		String SelectedDiscountType[]=null;
		long SelectedSamplingCreditSlipTypes[]=null;
		
		String SelectedEmptyReason[]=null;
		String SelectedMovementType[]=null;
		
		long SelectedGTMCategory[]=null;
		long SelectedEmptyLossType[]=null;
		
		long SelectedEmptyReceiptType[]=null;
		
		String LiftingType[] = null;
		
		long SelectedCensusUser[]=null;
		
		long SelectedPalletize[]=null;
		long SelectedHauContractor[]=null;
		
		long SelectedDistributorType[]=null;
		long SelectedDistributorOrderStatus[]=null;
		long SelectedUserEmployees[]=null;
		
		String SelectedUserEmployeesWithText = null;
		
		
		
		Date StartDate;
		Date EndDate;
		Date StartDateTime;
		Date EndDateTime;
		int StartDateTimeHour;
		int StartDateTimeMinutes;
		int EndDateTimeHour;
		int EndDateTimeMinutes;
		
		PrintWriter out = response.getWriter();
		long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));
		
		//System.out.println("Unique Session ID "+UniqueSessionID);
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==1) // 1 for package
		{
			 SelectedPackages=Utilities.parseLong(request.getParameterValues("PackagesCheckBox"));
			 session.setAttribute(UniqueSessionID+"_SR1SelectedPackages", SelectedPackages );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==2) // 2 for brand
		{
			SelectedBrands=Utilities.parseLong(request.getParameterValues("BrandsCheckBox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedBrands", SelectedBrands );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==3) // 3 for distributor
		{
			SelectedDistributor=Utilities.parseLong(request.getParameterValues("DistributorCheckBox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedDistributors", SelectedDistributor );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==4) // 4 for Order Bookers
		{
			SelectedOrderBookers=Utilities.parseLong(request.getParameterValues("OrderBookerCheckBox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedOrderBookers", SelectedOrderBookers );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==6) // 4 for Order Bookers
		{
			SelectedVehicles=Utilities.parseLong(request.getParameterValues("VehicleCheckBox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedVehicles", SelectedVehicles );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==7) // 4 for Order Bookers
		{
			SelectedEmployees=Utilities.parseLong(request.getParameterValues("EmployeeCheckBox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedEmployees", SelectedEmployees );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==8) //8 for Outlets
		{
			SelectedOutlets=Utilities.parseLong(request.getParameterValues("OutletCheckBox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedOutlets", SelectedOutlets );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==9) //9 for pjp
		{
			SelectedPJP=Utilities.parseLong(request.getParameterValues("PJPCheckbox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedPJP", SelectedPJP );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==10) //9 for HOD
		{
			SelectedHOD=Utilities.parseLong(request.getParameterValues("HODCheckbox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedHOD", SelectedHOD );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==11) //11 for RSM
		{
			
			SelectedRSM=Utilities.parseLong(request.getParameterValues("RSMCheckbox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedRSM", SelectedRSM );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==12) //12 for Warehouse
		{
			
			SelectedRSM=Utilities.parseLong(request.getParameterValues("WarehouseCheckbox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedWarehouse", SelectedRSM );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==13) //13 for Outlets Manual
		{
			int FeatureIDManual = Utilities.parseInt(request.getParameter("FeatureIDForOutletManual"));			
			SelectedOutletsManual=Utilities.parseLong(request.getParameterValues("OutletIDManual"));
			String OutletNameManual=Utilities.filterString(request.getParameter("OutletNameManual"), 1, 200);
			
			try {
				if(SelectedOutletsManual[0]!=0){
					if(UserAccess.isOutletAllowed(SelectedOutletsManual[0], Utilities.parseLong(UserID), FeatureIDManual)){
						session.setAttribute( UniqueSessionID+"_SR1SelectedOutlets", SelectedOutletsManual );
						session.setAttribute( UniqueSessionID+"_SR1SelectedOutletsNameManual",OutletNameManual );
					}
				}else{
					session.removeAttribute(UniqueSessionID+"_SR1SelectedOutlets");
					session.removeAttribute(UniqueSessionID+"_SR1SelectedOutletsNameManual");
				}
				
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
						
				
			
			
			
			
		}
		
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==5) // 5for Date Range
		{
			StartDate = Utilities.parseDate(request.getParameter("StartDate"));
			EndDate = Utilities.parseDate(request.getParameter("EndDate"));
			String SelectedDateType = Utilities.filterString(request.getParameter("SelectedDateType"), 1, 20);
			
			//SelectedOrderBookers=Utilities.parseLong(request.getParameterValues("OrderBookerCheckBox"));
			session.setAttribute( UniqueSessionID+"_SR1StartDate", StartDate );
			session.setAttribute( UniqueSessionID+"_SR1EndDate", EndDate );
			session.setAttribute( UniqueSessionID+"_SR1DateType", SelectedDateType );
			//System.out.print(SelectedDateType);
		
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==14) // 14 for Date Range new
		{
			int Month = Utilities.parseInt(request.getParameter("StartDate"));
			int Year = Utilities.parseInt(request.getParameter("EndDate"));
			
			  Calendar calendar = Calendar.getInstance();
			  int year = Year;
			  int month = Month;
			  int date = 1;
			  calendar.set(year, month, date);
			  int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			  
			  int newmonth = month+1; //+1 is because in java Jan =00 and Feb = 01
			  
			  String LocalStartDate = "01/"+newmonth+"/"+year;
			  String LocalEndDate = days+"/"+newmonth+"/"+year;
			  
			  
			  //System.out.println("St date End date: " + LocalStartDate+" - "+LocalEndDate);
			  
			  
			  StartDate = Utilities.parseDate(LocalStartDate);
			  EndDate = Utilities.parseDate(LocalEndDate);
				
			  session.setAttribute( UniqueSessionID+"_SR1StartDate", StartDate );
			  session.setAttribute( UniqueSessionID+"_SR1EndDate", EndDate );
		
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==15) //15 for Custome Days
		{				
			int GreaterDays = Utilities.parseInt(request.getParameter("DateRangeNewFilterGreaterDays"));
			int LessDays = Utilities.parseInt(request.getParameter("DateRangeNewFilterLessDays"));
			
					
			session.setAttribute( UniqueSessionID+"_SR1SelectedDaysGreaterThan", GreaterDays );
			session.setAttribute( UniqueSessionID+"_SR1SelectedDaysLessThan",LessDays );
					
			
		}
		
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==16) //16 for OutletType
		{
			
			SelectedOutletType=Utilities.filterString(request.getParameterValues("OutletTypeCheckbox"),1,100);
			session.setAttribute( UniqueSessionID+"_SR1SelectedOutletType", SelectedOutletType );
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==17) //17 for Complaint ID
		{				
			long ComplaintID = Utilities.parseInt(request.getParameter("ComplaintID"));
					
			session.setAttribute( UniqueSessionID+"_SR1SelectedComplaintID", ComplaintID );
			
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==18) //18 for Complaint Type
		{				
			long ComplaintType = Utilities.parseInt(request.getParameter("ComplaintType"));
					
			session.setAttribute( UniqueSessionID+"_SR1SelectedComplaintType", ComplaintType );
			
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==19) //19 for Complaint Status
		{
			
			SelectedComplaintType=Utilities.filterString(request.getParameterValues("ComplaintStatusCheckbox"),1,100);
			session.setAttribute( UniqueSessionID+"_SR1SelectedComplaintStatus", SelectedComplaintType );
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==20) //20 for SM
		{
			
			SelectedSM=Utilities.parseLong(request.getParameterValues("SMCheckbox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedSM", SelectedSM );
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==21) //21 for TDM
		{
			
			SelectedTDM=Utilities.parseLong(request.getParameterValues("TDMCheckbox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedTDM", SelectedTDM );
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==22) //22 for ASM
		{
			
			SelectedASM=Utilities.parseLong(request.getParameterValues("ASMCheckbox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedASM", SelectedASM );
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==23) //23 for Sales Type
		{
			
			SelectedSalesType=Utilities.parseLong(request.getParameterValues("SalesTypeCheckbox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedSalesType", SelectedSalesType );
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==24) //24 for Order ID
		{				
			long OrderID = Utilities.parseInt(request.getParameter("OrderID"));					
			
			session.setAttribute( UniqueSessionID+"_SR1SelectedOrderID", OrderID );
			
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==25) //25 for Region
		{
			
			SelectedRegion=Utilities.parseLong(request.getParameterValues("RegionCheckbox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedRegion", SelectedRegion );
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==26) //26 for Account Type 
		{
			
			SelectedAccountType=Utilities.parseLong(request.getParameterValues("AccountTypeCheckbox"));
			
			session.setAttribute( UniqueSessionID+"_SR1SelectedAccountType", SelectedAccountType );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==27) //27 for Customer ID
		{				
			long CustomerID = Utilities.parseInt(request.getParameter("CustomerID"));
					
			//System.out.println("hurrrr "+CustomerID);
			
			session.setAttribute( UniqueSessionID+"_SR1SelectedCustomerID", CustomerID );
			SelectedAccountType=Utilities.parseLong(request.getParameterValues("AccountTypeCheckbox"));
			
			session.setAttribute( UniqueSessionID+"_SR1SelectedAccountType", SelectedAccountType );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==28) //28 for Transaction Account
		{
			
			SelectedCashInstruments=Utilities.parseLong(request.getParameterValues("CashInstrumentsRadio"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedCashInstruments", SelectedCashInstruments );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==29) //29 for Gl Employee
		{
			SelectedGlEmployee=Utilities.parseLong(request.getParameterValues("GlEmployeeCheckbox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedGlEmployee", SelectedGlEmployee );
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==30) //30 for Primary Invoices Status
		{
			
			SelectedPrimaryInvoiceStatus=Utilities.filterString(request.getParameterValues("PrimaryInvoiceStatus"),1,100);
			session.setAttribute( UniqueSessionID+"_SR1SelectedPrimaryInvoiceStatus",SelectedPrimaryInvoiceStatus );
		}
		
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==31) //31 for Cash instrument Multiple
		{
			
			SelectedCashInstrumentsMultiple=Utilities.parseLong(request.getParameterValues("CashInstrumentsMultiple"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedCashInstrumentsMultiple", SelectedCashInstrumentsMultiple );
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==32) //32 for DiscountType
		{
			
			SelectedDiscountType=Utilities.filterString(request.getParameterValues("DiscountTypeCheckbox"),1,100);
			session.setAttribute( UniqueSessionID+"_SR1SelectedDiscountType", SelectedDiscountType );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==33) //33 for Distributor ID
		{				
			long DistributorID = Utilities.parseInt(request.getParameter("DistributorID"));
					
			session.setAttribute( UniqueSessionID+"_SR1SelectedDistributorID", DistributorID );
			
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==34) //34 for Asset Number
		{				
			long AssetNumber = Utilities.parseLong(request.getParameter("AssetNumberID"));
					
			session.setAttribute( UniqueSessionID+"_SR1SelectedAssetNumber", AssetNumber );
			
		}
		
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==35) //35 for SamplingCreditSlipTypes
		{
			SelectedSamplingCreditSlipTypes=Utilities.parseLong(request.getParameterValues("SamplingCreditSlipTypesCheckbox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedSamplingCreditSlipTypes", SelectedSamplingCreditSlipTypes );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==36) //36 for Empty Reason
		{
			SelectedEmptyReason=Utilities.filterString(request.getParameterValues("EmptyReasonCheckbox"),101,100);
			session.setAttribute( UniqueSessionID+"_SR1SelectedEmptyReason", SelectedEmptyReason );
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==37) //37 for Empty Reason
		{
			SelectedMovementType=Utilities.filterString(request.getParameterValues("MovementTypeCheckbox"),101,100);
			session.setAttribute( UniqueSessionID+"_SR1SelectedMovementType", SelectedMovementType );
		}
		
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==38) //38 for GTM Category
		{
			SelectedGTMCategory=Utilities.parseLong(request.getParameterValues("GTMCategoryCheckbox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedGTMCategory", SelectedGTMCategory );
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==39) //39 for EmptyLossType
		{
			SelectedEmptyLossType=Utilities.parseLong(request.getParameterValues("EmptyLossTypeCheckbox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedEmptyLossType", SelectedEmptyLossType );
		}
		/*for(int i=0;i<SelectedPackages.length;i++)
		{
			System.out.println("Hello "+SelectedPackages[i]);
		}*/
		
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==40) //40 for Empty Receipt Type
		{
			SelectedEmptyReceiptType=Utilities.parseLong(request.getParameterValues("EmptyReceiptTypeCheckbox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedEmptyReceiptType", SelectedEmptyReceiptType );
		}
		
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==41) // 40for DateTime Range
		{
			//System.out.println("hello");
			StartDateTime = Utilities.parseDate(request.getParameter("StartDateTime"));			
			StartDateTimeHour = Utilities.parseInt(request.getParameter("StartDateTimeHour"));
			StartDateTimeMinutes = Utilities.parseInt(request.getParameter("StartDateTimeMinutes"));
			
			EndDateTime = Utilities.parseDate(request.getParameter("EndDateTime"));
			EndDateTimeHour = Utilities.parseInt(request.getParameter("EndDateTimeHour"));
			EndDateTimeMinutes = Utilities.parseInt(request.getParameter("EndDateTimeMinutes"));
			
			String SelectedDateTimeType = Utilities.filterString(request.getParameter("SelectedDateType"), 1, 20);
			
			//SelectedOrderBookers=Utilities.parseLong(request.getParameterValues("OrderBookerCheckBox"));
			session.setAttribute( UniqueSessionID+"_SR1StartDateTime", StartDateTime );
			session.setAttribute( UniqueSessionID+"_SR1StartDateTimeHour", StartDateTimeHour );
			session.setAttribute( UniqueSessionID+"_SR1StartDateTimeMinutes", StartDateTimeMinutes );
			session.setAttribute( UniqueSessionID+"_SR1EndDateTime", EndDateTime );
			session.setAttribute( UniqueSessionID+"_SR1EndDateTimeHour", EndDateTimeHour );
			session.setAttribute( UniqueSessionID+"_SR1EndDateTimeMinutes", EndDateTimeMinutes );
			
			session.setAttribute( UniqueSessionID+"_SR1DateTimeType", SelectedDateTimeType );
			//System.out.print(SelectedDateType);
		
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==42) //42 for Plant ID
		{				
			long PlantID = Utilities.parseInt(request.getParameter("PlantID"));
					
			session.setAttribute( UniqueSessionID+"_SR1SelectedPlantID", PlantID );
			
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==43) //43 for Lifting Type
		{				
			 LiftingType = Utilities.filterString(request.getParameterValues("LiftingTypeCheckbox"),101,100);
					
			session.setAttribute( UniqueSessionID+"_SR1SelectedLiftingType", LiftingType );
			
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==44) //44 for Census User
		{
			SelectedCensusUser=Utilities.parseLong(request.getParameterValues("CensusUserCheckbox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedCensusUser", SelectedCensusUser );
		}
		
		

		if(Utilities.parseInt(request.getParameter("FilterType"))==45) //45 for Palletize
		{
			
			SelectedPalletize=Utilities.parseLong(request.getParameterValues("PalletizeCheckbox"));
			
			session.setAttribute( UniqueSessionID+"_SR1SelectedPalletize", SelectedPalletize );
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==46) //46 for hauContractor
		{
			
			SelectedHauContractor=Utilities.parseLong(request.getParameterValues("HauContractorCheckbox"));
			
			session.setAttribute( UniqueSessionID+"_SR1SelectedHauContractor", SelectedHauContractor );
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==47) //47 for Distributor Type
		{
			
			SelectedDistributorType=Utilities.parseLong(request.getParameterValues("DistributorTypeCheckbox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedDistributorType", SelectedDistributorType );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==48) //48 for Distributor Order Status
		{
			
			SelectedDistributorOrderStatus=Utilities.parseLong(request.getParameterValues("DistributorOrderStatusCheckbox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedDistributorOrderStatus", SelectedDistributorOrderStatus );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==49) //49 for Employee Filter 
		{
			
			SelectedUserEmployees=Utilities.parseLong(request.getParameterValues("UserEmployeeCheckBox"));
			session.setAttribute( UniqueSessionID+"_SR1UserSelectedEmployees", SelectedUserEmployees );
				
				
		}
		if (Utilities.parseInt(request.getParameter("FilterType")) == 50) // 50 for 
		{

			SelectedUserEmployeesWithText =(String)request.getParameter("SearchId");
			session.setAttribute(UniqueSessionID + "_SR1UserSelectedEmployeesWithText", SelectedUserEmployeesWithText);

		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))  == 51) // 51 for Channel
		{
			 SelectedChannels=Utilities.parseLong(request.getParameterValues("ChannelsCheckBox"));
			 session.setAttribute(UniqueSessionID+"_SR1SelectedChannels", SelectedChannels );
		}

		if(Utilities.parseInt(request.getParameter("FilterType"))  == 52) // 52 for City
		{
			 SelectedCity=Utilities.parseLong(request.getParameterValues("CityCheckBox"));
			 session.setAttribute(UniqueSessionID+"_SR1SelectedCity", SelectedCity );
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))  == 53) // 53 for SKU
		{
			 SelectedSKU=Utilities.parseLong(request.getParameterValues("SKUCheckBox"));
			 session.setAttribute(UniqueSessionID+"_SR1SelectedSKU", SelectedSKU );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==54) // 3 for distributor
		{
			SelectedFromDistributor=Utilities.parseLong(request.getParameterValues("FromDistributorCheckBox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedFromDistributors", SelectedFromDistributor );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==55) // 3 for distributor
		{
			SelectedToDistributor=Utilities.parseLong(request.getParameterValues("ToDistributorCheckBox"));
			session.setAttribute( UniqueSessionID+"_SR1SelectedToDistributors", SelectedToDistributor );
		}
		
		
		
		JSONObject obj = new JSONObject();		
		try {
			
			obj.put("success", "true");				
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("success", "false");
			//obj.put("error", e.toString());
			obj.put("error", "");
			e.printStackTrace();
		}
		
		
		
		out.print(obj);
		out.close();
		
	}
	
}
