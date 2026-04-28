// 
// Decompiled by Procyon v0.5.36
// 

package com.pbc.reports;

import com.pbc.common.Distributor;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.nio.file.Files;
import java.io.File;
import com.pbc.util.Datasource;
import java.util.Date;
import java.sql.SQLException;
import com.pbc.util.UserAccess;
import com.pbc.util.Utilities;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet({ "/reports/ReportsR339DownloadExcel" })
public class ReportsR339DownloadExcel extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    }
    
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ReportsR339DownloadExcel...");
        final HttpSession session = request.getSession();
        final long UserID = Long.parseLong((String)session.getAttribute("UserID"));
        final int FeatureID = 434;
        final long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));
        final long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
        try {
            if (!UserAccess.isAuthorized(FeatureID, SessionUserID, request)) {
                response.sendRedirect("AccessDenied.jsp");
            }
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException | IOException ex2) {
            final Exception ex = null;
            final Exception e1 = ex;
            e1.printStackTrace();
        }
        final Date DateToday = new Date();
        Date StartDate = (Date)session.getAttribute(String.valueOf(UniqueSessionID) + "_SR1StartDate");
        Date EndDate = (Date)session.getAttribute(String.valueOf(UniqueSessionID) + "_SR1EndDate");
        if (session.getAttribute(String.valueOf(UniqueSessionID) + "_SR1StartDate") == null) {
            StartDate = new Date();
        }
        if (session.getAttribute(String.valueOf(UniqueSessionID) + "_SR1EndDate") == null) {
            EndDate = new Date();
        }
        long[] SelectedPackagesArray = null;
        if (session.getAttribute(String.valueOf(UniqueSessionID) + "_SR1SelectedPackages") != null) {
            SelectedPackagesArray = (long[])session.getAttribute(String.valueOf(UniqueSessionID) + "_SR1SelectedPackages");
        }
        String PackageIDs = "";
        String WherePackage = "";
        if (SelectedPackagesArray != null && SelectedPackagesArray.length > 0) {
            for (int i = 0; i < SelectedPackagesArray.length; ++i) {
                if (i == 0) {
                    PackageIDs = String.valueOf(PackageIDs) + SelectedPackagesArray[i];
                }
                else {
                    PackageIDs = String.valueOf(PackageIDs) + ", " + SelectedPackagesArray[i];
                }
            }
            WherePackage = " and package_id in (" + PackageIDs + ") ";
        }
      //TDM


        String TDMIDs="";
        long[] SelectedTDMArray = null;
        if (session.getAttribute(String.valueOf(UniqueSessionID)+"_SR1SelectedTDM") != null){
        	SelectedTDMArray = (long[])session.getAttribute(String.valueOf(UniqueSessionID)+"_SR1SelectedTDM");
        	TDMIDs = Utilities.serializeForSQL(SelectedTDMArray);
        }

        String WhereTDM = "";
        if (TDMIDs.length() > 0){
        	WhereTDM = " and distributor_id in (SELECT distributor_id FROM common_distributors where tdm_id in ("+TDMIDs+")) ";	
        }

        String HODIDs = "";
        long[] SelectedHODArray = null;
        if (session.getAttribute(String.valueOf(UniqueSessionID) + "_SR1SelectedHOD") != null) {
            SelectedHODArray = (long[])session.getAttribute(String.valueOf(UniqueSessionID) + "_SR1SelectedHOD");
            HODIDs = Utilities.serializeForSQL(SelectedHODArray);
        }
        String WhereHOD = "";
        if (HODIDs.length() > 0) {
            WhereHOD = " and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (" + HODIDs + ")) ";
        }
        String RSMIDs = "";
        long[] SelectedRSMArray = null;
        if (session.getAttribute(String.valueOf(UniqueSessionID) + "_SR1SelectedRSM") != null) {
            SelectedRSMArray = (long[])session.getAttribute(String.valueOf(UniqueSessionID) + "_SR1SelectedRSM");
            RSMIDs = Utilities.serializeForSQL(SelectedRSMArray);
        }
        String WhereRSM = "";
        if (RSMIDs.length() > 0) {
            WhereRSM = " and distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in (" + RSMIDs + ")) ";
        }
        System.out.println("WhereRSM == " + WhereRSM);
        long[] SelectedDistributorsArray = null;
        boolean IsDistributorSelected = false;
        if (session.getAttribute(String.valueOf(UniqueSessionID) + "_SR1SelectedDistributors") != null) {
            SelectedDistributorsArray = (long[])session.getAttribute(String.valueOf(UniqueSessionID) + "_SR1SelectedDistributors");
            IsDistributorSelected = true;
        }
        else {
            try {
                final Distributor[] UserDistributor = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
                SelectedDistributorsArray = new long[UserDistributor.length];
                for (int x = 0; x < UserDistributor.length; ++x) {
                    SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
                }
            }
            catch (ClassNotFoundException e2) {
                e2.printStackTrace();
            }
            catch (InstantiationException e3) {
                e3.printStackTrace();
            }
            catch (IllegalAccessException e4) {
                e4.printStackTrace();
            }
            catch (SQLException e5) {
                e5.printStackTrace();
            }
        }
        String DistributorIDs = "";
        String WhereDistributors = "";
        if (SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0) {
            for (int j = 0; j < SelectedDistributorsArray.length; ++j) {
                if (j == 0) {
                    DistributorIDs = String.valueOf(DistributorIDs) + SelectedDistributorsArray[j];
                }
                else {
                    DistributorIDs = String.valueOf(DistributorIDs) + ", " + SelectedDistributorsArray[j];
                }
            }
            WhereDistributors = " and distributor_id in (" + DistributorIDs + ") ";
        }
        String PJPIDs = "";
        long[] SelectedPJPArray = null;
        if (session.getAttribute(String.valueOf(UniqueSessionID) + "_SR1SelectedPJP") != null) {
            SelectedPJPArray = (long[])session.getAttribute(String.valueOf(UniqueSessionID) + "_SR1SelectedPJP");
            PJPIDs = Utilities.serializeForSQL(SelectedPJPArray);
        }
        String WherePJP = "";
        if (PJPIDs.length() > 0) {
            WherePJP = " and codv.outlet_id in (SELECT distinct outlet_id FROM distributor_beat_plan_schedule where id in(" + PJPIDs + "))";
        }
        boolean IsOrderBookerSelected = false;
        int OrderBookerArrayLength = 0;
        long[] SelectedOrderBookerArray = null;
        if (session.getAttribute(String.valueOf(UniqueSessionID) + "_SR1SelectedOrderBookers") != null) {
            SelectedOrderBookerArray = (long[])session.getAttribute(String.valueOf(UniqueSessionID) + "_SR1SelectedOrderBookers");
            IsOrderBookerSelected = true;
            OrderBookerArrayLength = SelectedOrderBookerArray.length;
        }
        String OrderBookerIDs = "";
        if (SelectedOrderBookerArray != null && SelectedOrderBookerArray.length > 0) {
            for (int k = 0; k < SelectedOrderBookerArray.length; ++k) {
                if (k == 0) {
                    OrderBookerIDs = String.valueOf(OrderBookerIDs) + SelectedOrderBookerArray[k];
                }
                else {
                    OrderBookerIDs = String.valueOf(OrderBookerIDs) + ", " + SelectedOrderBookerArray[k];
                }
            }
        }
        String OrderBookerIDsWhere = "";
        if (OrderBookerIDs.length() > 0) {
            OrderBookerIDsWhere = " and mo.created_by in (" + OrderBookerIDs + ") ";
        }
        long[] SelectedBrandsArray = null;
        if (session.getAttribute(String.valueOf(UniqueSessionID) + "_SR1SelectedBrands") != null) {
            SelectedBrandsArray = (long[])session.getAttribute(String.valueOf(UniqueSessionID) + "_SR1SelectedBrands");
        }
        String BrandIDs = "";
        String WhereBrand = "";
        if (SelectedBrandsArray != null && SelectedBrandsArray.length > 0) {
            for (int l = 0; l < SelectedBrandsArray.length; ++l) {
                if (l == 0) {
                    BrandIDs = String.valueOf(BrandIDs) + SelectedBrandsArray[l];
                }
                else {
                    BrandIDs = String.valueOf(BrandIDs) + ", " + SelectedBrandsArray[l];
                }
            }
            WhereBrand = " and ipv.brand_id in (" + BrandIDs + ") ";
        }
        final String FileName = "ReportsR339_" + Utilities.getSQLDateWithoutSeprator(DateToday) + ".xlsx";
        final Datasource ds = new Datasource();
        try {
            final long Before = System.currentTimeMillis();
            new Report339DistributorSalesExcel().createExcel(String.valueOf(Utilities.getCommonFilePath()) + "/" + FileName, StartDate, EndDate, SessionUserID, WherePackage, DistributorIDs, WhereDistributors, SelectedDistributorsArray, WhereBrand);
            final File file = new File(Utilities.getCommonFilePath(), FileName);
            response.setHeader("Content-Type", this.getServletContext().getMimeType(FileName));
            response.setHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
            final long After = System.currentTimeMillis();
            System.out.println(After - Before);
            Files.copy(file.toPath(), (OutputStream)response.getOutputStream());
        }
        catch (Exception e6) {
            e6.printStackTrace();
        }
    }
}
