package com.pbc.product;

import java.io.IOException;
import java.sql.Statement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.pbc.util.Datasource;

@WebServlet("/product/IncomeTaxServlet")
public class IncomeTaxServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        try {

            double FR   = Double.parseDouble(request.getParameter("FR"));
            double FUR  = Double.parseDouble(request.getParameter("FUR"));
            double NFR  = Double.parseDouble(request.getParameter("NFR"));
            double NFUR = Double.parseDouble(request.getParameter("NFUR"));
            int user    = Integer.parseInt(request.getParameter("created_by"));

            Datasource ds = new Datasource();
            ds.createConnection();

            Statement s  = ds.createStatement();
            Statement s1 = ds.createStatement();

            s.executeUpdate("UPDATE inventory_income_tax SET isActive = 0");

            String query =
                "INSERT INTO inventory_income_tax(FR,FUR,NFR,NFUR,created_on,created_by,isActive) VALUES(" +
                FR + "," + FUR + "," + NFR + "," + NFUR + ",NOW()," + user + ",1)";

            s1.executeUpdate(query);

            ds.dropConnection();

            response.getWriter().print("success");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

