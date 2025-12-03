package com.pbc.distributor;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
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

@WebServlet(
   description = "Dispatch Returns ",
   urlPatterns = {"/distributor/DispatchCashReceiptsExecute"}
)
public class DispatchCashReceiptsExecute extends HttpServlet {
   private static final long serialVersionUID = 1L;

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
      long DispatchID = Utilities.parseLong(request.getParameter("DispatchIDForInsertion"));
      long[] OutletID = Utilities.parseLong(request.getParameterValues("DispatchCashReceiptsOutletID"));
      long[] SalesID = Utilities.parseLong(request.getParameterValues("DispatchCashReceiptsOrderID"));
      long DispatchCashReceiptsCashReceived = Utilities.parseLong(request.getParameter("DispatchCashReceiptsCashReceived"));
      double[] TotalAmount = Utilities.parseDouble(request.getParameterValues("DispatchCashReceiptsTotalAmount"));
      double[] CashReceived = Utilities.parseDouble(request.getParameterValues("DispatchCashReceiptsOrderCashReceipts"));
      System.out.println("DispatchID" + DispatchID);
      Datasource ds = new Datasource();
      JSONObject obj = new JSONObject();

      try {
         ds.createConnection();
         ds.startTransaction();
         Statement s = ds.createStatement();
         Statement s1 = ds.createStatement();
         s.executeUpdate("Delete from inventory_sales_dispatch_cash_receipts where  dispatch_id=" + DispatchID);
         if (OutletID != null) {
            for(int i = 0; i < OutletID.length; ++i) {
               s1.executeUpdate("insert into gl_transactions_sales (sales_id,outlet_id,debit,created_on,created_on_date,created_by) values(" + SalesID[i] + "," + OutletID[i] + "," + TotalAmount[i] + ",now(),now()," + UserID + ")");
               s1.executeUpdate("insert into gl_transactions_sales (sales_id,outlet_id,credit,created_on,created_on_date,created_by) values(" + SalesID[i] + "," + OutletID[i] + "," + CashReceived[i] + ",now(),now()," + UserID + ")");
               s1.executeUpdate("insert into inventory_sales_dispatch_cash_receipts (dispatch_id,outlet_id,cash_received) values(" + DispatchID + "," + OutletID[i] + "," + CashReceived[i] + ")");
               if (TotalAmount[i] == CashReceived[i]) {
                  s1.executeUpdate("update inventory_sales_adjusted set is_settled=1 where id=" + SalesID[i]);
               }
            }
         }

         s.executeUpdate("update inventory_sales_dispatch set is_cash_adjusted=1,cash_received=" + DispatchCashReceiptsCashReceived + " where id=" + DispatchID);
         ds.commit();
         obj.put("success", "true");
         s.close();
      } catch (Exception var29) {
         try {
            ds.rollback();
         } catch (SQLException var28) {
            var28.printStackTrace();
         }

         obj.put("success", "false");
         obj.put("error", var29.toString());
         var29.printStackTrace();
      } finally {
         try {
            ds.dropConnection();
         } catch (SQLException var27) {
            var27.printStackTrace();
         }

      }

      out.print(obj);
      out.close();
   }
}
