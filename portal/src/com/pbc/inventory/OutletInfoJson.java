package com.pbc.inventory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;

@WebServlet(description = "Outlet InfoJson", urlPatterns = { "/inventory/OutletInfoJson" })
public class OutletInfoJson extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public OutletInfoJson() {
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
        Datasource ds = new Datasource();

        String outletIDStr = request.getParameter("OutletID");

        if (outletIDStr == null || outletIDStr.trim().isEmpty()) {
            obj.put("success", "false");
            obj.put("error", "OutletID is missing.");
            out.print(obj.toJSONString());
            return;
        }

        try {
            int outletID = Integer.parseInt(outletIDStr.trim());

            ds.createConnection();
            Statement s = ds.createStatement();

           
            
            System.out.println("SELECT *, " +
            "(SELECT region_name FROM common_regions cr WHERE cr.region_id = co.region_id) AS regionName " +
            "FROM common_outlets co WHERE id = " + outletID);
            
            String query = "SELECT *, " +
                    "(SELECT region_name FROM common_regions cr WHERE cr.region_id = co.region_id) AS regionName " +
                    "FROM common_outlets co WHERE id = " + outletID;

            ResultSet rs = s.executeQuery(query);

            if (rs.next()) {
                obj.put("success", "true");
                obj.put("name", rs.getString("name"));
                obj.put("address", rs.getString("address"));
                obj.put("region", rs.getString("regionName"));
            } else {
                obj.put("success", "false");
                obj.put("error", "No outlet found with ID: " + outletID);
            }

            rs.close();
            s.close();
            ds.dropConnection();

        } catch (NumberFormatException e) {
            obj.put("success", "false");
            obj.put("error", "Invalid Outlet ID format.");
        } catch (Exception e) {
            obj.put("success", "false");
            obj.put("error", e.getMessage());
            e.printStackTrace();
        }

        out.print(obj.toJSONString());
        out.close();
    }
}
