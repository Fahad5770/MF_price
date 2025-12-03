package com.pbc.inventory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.simple.JSONObject;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

@WebServlet(description = "Save Outlet Info", urlPatterns = { "/inventory/SaveOutletInfo" })
public class SaveOutletInfo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public SaveOutletInfo() {
        super();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject obj = new JSONObject();
        PrintWriter out = response.getWriter();
        
        System.out.println("=====================================================================");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("UserID") == null) {
            obj.put("success", false);
            obj.put("error", "User not logged in.");
            out.print(obj.toJSONString());
            return;
        }

        long sessionUserID = Long.parseLong((String) session.getAttribute("UserID"));

        String outletIDStr = request.getParameter("OutletID");
        String outletName = request.getParameter("OutletName");
        String shelfRent = request.getParameter("FixedCompanyShare");
        
        String fromMonth = request.getParameter("FixedValidFrom");
        String toMonth = request.getParameter("FixedValidTo");

        System.out.println("fromMonth : " + fromMonth);
        System.out.println("toMonth : " + toMonth);

        
        Statement s = null;
        ResultSet rs = null;
        Datasource ds = new Datasource();

        try {
            int outletID = Integer.parseInt(outletIDStr.trim());

            ds.createConnection();
            s = ds.createStatement();
        	Date today = new Date();
			int month = Utilities.getMonthNumberByDate(today);
            String query = "INSERT INTO rental_discount (outlet_id, outlet_name, " +
                    "shelf_rent, " +
                    "from_month, to_month, is_active, created_on, created_by, month) " +
                    "VALUES (" + outletID + ", '" + outletName + "', '" +shelfRent + "', " +
                    fromMonth + ", " + toMonth + ", 1, NOW(), " + sessionUserID +"," +month + ")";

            System.out.println("Executing query: " + query);

            int rows = s.executeUpdate(query);

            obj.put("success", "true");
           // obj.put("message", rows > 0 ? "Outlet info saved successfully." : "Failed to insert data.");

        } catch (NumberFormatException e) {
            obj.put("success", "false");
            obj.put("error", "Invalid Outlet ID format.");
        } catch (Exception e) {
            obj.put("success", "false");
            obj.put("error", e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (s != null) s.close();
                ds.dropConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        out.print(obj.toJSONString());
        out.close();
    }
}
