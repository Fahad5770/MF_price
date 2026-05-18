package com.pbc.employee;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.simple.JSONObject;

@WebServlet(
   description = "Create All PJP Excel File to Download",
   urlPatterns = {"/employee/AllPJPDownloadExcelMain"}
)
public class AllPJPDownloadExcelMain extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      HttpSession session = request.getSession();
      String UserID = null;
      if (session.getAttribute("UserID") != null) {
         UserID = (String)session.getAttribute("UserID");
      }

      if (UserID == null) {
         response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
      }

      PrintWriter out = response.getWriter();
      JSONObject json = new JSONObject();
      Datasource ds = new Datasource();

      try {
         ds.createConnection();
         Statement s = ds.createStatement();
         String filename_PJP = "All_PJP_" + Utilities.getSQLDateWithoutSeprator(new Date()) + ".xlsx";
         (new AllPJPDownloadExcel()).createPdf(Utilities.getOrderImagesPath() + "/" + filename_PJP, 0L);
         json.put("success", "true");
         json.put("FileName", filename_PJP);
         s.close();
      } catch (Exception var18) {
         System.out.println(var18);
         json.put("success", "false");
         json.put("error", var18.toString());
      } finally {
         try {
            ds.dropConnection();
         } catch (SQLException var17) {
            var17.printStackTrace();
         }

      }

      out.print(json);
   }
}
