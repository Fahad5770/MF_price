package com.pbc.employee;

import com.pbc.util.Utilities;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet({"/employee/PJPFileDownload"})
public class PJPFileDownload extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      HttpSession session = request.getSession();
      long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));
      if (SessionUserID == 0L) {
         response.sendRedirect("AccessDenied.jsp");
      } else {
         String filename = request.getParameter("file");
         String fileuri = Utilities.getOrderImagesPath() + "/" + filename;
         response.setContentType("application/octet-stream");
         response.setHeader("Content-disposition", "attachment; filename=" + filename);
         File my_file = new File(fileuri);
         OutputStream out = response.getOutputStream();
         FileInputStream in = new FileInputStream(my_file);
         byte[] buffer = new byte[4096];

         int length;
         while((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
         }

         in.close();
         out.flush();
      }

   }
}
