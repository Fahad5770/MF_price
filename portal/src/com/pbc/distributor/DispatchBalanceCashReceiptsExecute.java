package com.pbc.distributor;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;

@WebServlet(
   description = "Dispatch Balance Receipts ",
   urlPatterns = {"/distributor/DispatchBalanceCashReceiptsExecute"}
)
public class DispatchBalanceCashReceiptsExecute extends HttpServlet {
   private static final long serialVersionUID = 1L;

   @SuppressWarnings({ "unchecked", "unused" })
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      HttpSession session = request.getSession();
      String UserID = null;
      if (session.getAttribute("UserID") != null) {
         UserID = (String)session.getAttribute("UserID");
      }

      if (UserID == null) {
         response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
      }

      PrintWriter out = response.getWriter();
      long[] DispatchID = Utilities.parseLong(request.getParameterValues("DispatchID"));
      long[] SalesID = Utilities.parseLong(request.getParameterValues("SalesID"));
      long[] OutletID = Utilities.parseLong(request.getParameterValues("OutletID"));
      double[] CashReceived = Utilities.parseDouble(request.getParameterValues("CashReceived1"));
      double[] InvoiceAmount1 = Utilities.parseDouble(request.getParameterValues("InvoiceAmount1"));
      int FullyAdjusted = Utilities.parseInt(request.getParameter("FullyAdjusted"));
      String[] Comments = request.getParameterValues("comments");
      
      System.out.println("SalesID" + SalesID);
      Datasource ds = new Datasource();
      JSONObject obj = new JSONObject();
      
      try {
         ds.createConnection();
         ds.startTransaction();
         Statement s = ds.createStatement();
         Statement s1 = ds.createStatement();
         s.executeUpdate("insert into inventory_sales_dispatch_receipts (created_on,created_by) values(now()," + UserID + ")");
         long ReceiptID = 0L;
         ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
         if (rs.first()) {
            ReceiptID = rs.getLong(1);
         }

         if (SalesID != null) {
            for(int i = 0; i < SalesID.length; ++i) {
               s1.executeUpdate("insert into gl_transactions_sales (sales_id,outlet_id,credit,created_on,created_on_date,created_by,comments) values(" + SalesID[i] + "," + OutletID[i] + "," + CashReceived[i] + ",now(),now()," + UserID + " , '" + Comments[i] + "')");
               s1.executeUpdate("insert into inventory_sales_dispatch_receipts_invoices (receipt_id,dispatch_id,sales_id,outlet_id,cash_received) values(" + ReceiptID + "," + DispatchID[i] + "," + SalesID[i] + "," + OutletID[i] + "," + CashReceived[i] + ")");
               if (InvoiceAmount1[i] == CashReceived[i]) {
                  s.executeUpdate("update inventory_sales_adjusted set is_settled=1 where id=" + SalesID[i]);
               }
            }
         }

         ds.commit();
         obj.put("success", "true");
         s.close();
      } catch (Exception var30) {
         try {
            ds.rollback();
         } catch (SQLException var29) {
            var29.printStackTrace();
         }

         obj.put("success", "false");
         obj.put("error", var30.toString());
         var30.printStackTrace();
      } finally {
         try {
            ds.dropConnection();
         } catch (SQLException var28) {
            var28.printStackTrace();
         }

      }

      out.print(obj);
      out.close();
   }
}
