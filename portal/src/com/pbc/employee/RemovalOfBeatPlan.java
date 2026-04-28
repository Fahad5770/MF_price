package com.pbc.employee;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Iterator;

@WebServlet(description = "Uploads a file", urlPatterns = { "/employee/RemovalOfBeatPlan" })
public class RemovalOfBeatPlan extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RemovalOfBeatPlan() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String UserID = null;

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        System.out.println("in PJP file Upload Employee ");

        if (session.getAttribute("UserID") != null) {
            UserID = (String) session.getAttribute("UserID");
        }

        if (UserID == null) {
            response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
            return;
        }

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (isMultipart) {
            Datasource ds = null;
            Statement s = null;
            boolean anyDeleted = false;

            long RequestID = 123; // Could be dynamic

            try {
                ds = new Datasource();
                ds.createConnection();
                ds.startTransaction();
                s = ds.createStatement();

                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);

                List<FileItem> items = upload.parseRequest(request);
                Iterator<FileItem> iterator = items.iterator();

                while (iterator.hasNext()) {
                    FileItem item = iterator.next();

                    if (!item.isFormField()) {
                        String fileName = item.getName();

                        // Ensure directory exists
                        File path = new File(Utilities.getWorkflowAttachmentsPath());
                        if (!path.exists()) {
                            boolean created = path.mkdirs();
                            System.out.println("Upload directory created: " + created);
                        }

                        // Save uploaded file
                        File uploadedFile = new File(path, RequestID + "_" + fileName);
                        System.out.println("Saving file to: " + uploadedFile.getAbsolutePath());
                        item.write(uploadedFile);

                        // Read CSV file
                        try (BufferedReader br = new BufferedReader(
                                new InputStreamReader(new FileInputStream(uploadedFile)))) {
                            String thisLine;
                            boolean isFirstRow = true; // <-- add this
                            while ((thisLine = br.readLine()) != null) {
                                if (thisLine.trim().isEmpty()) continue;

                                // skip header row
                                if (isFirstRow) {
                                    isFirstRow = false;
                                    continue;
                                }

                                String[] data = thisLine.split(",");
                                if (data.length >= 3) {
                                    String beatPlanId = data[0].trim();
                                    String outletId   = data[1].trim();
                                    String dayNumber  = data[2].trim();

                                    String deleteQuery = "DELETE FROM distributor_beat_plan_schedule " +
                                                         "WHERE id = " + beatPlanId +
                                                         " AND outlet_id = " + outletId +
                                                         " AND day_number = " + dayNumber;
                                    System.out.println(deleteQuery);

                                    int rows = s.executeUpdate(deleteQuery);
                                    if (rows > 0) {
                                        anyDeleted = true;
                                    }

                                } else {
                                    System.out.println("Invalid row: " + thisLine);
                                }
                            }
                        }

                    }
                }

                ds.commit();

                if (anyDeleted) {
                    out.print("{\"Success\":\"true\",\"Message\":\"Beat Plan removed successfully\"}");
                } else {
                    out.print("{\"Success\":\"false\",\"Message\":\"No matching records found to delete\"}");
                }

            } catch (Exception e) {
                try {
                    if (ds != null) ds.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
                out.print("{\"Success\":\"false\",\"Message\":\"" + e.getMessage() + "\"}");
            } finally {
                try {
                    if (s != null) s.close();
                    if (ds != null) ds.dropConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                out.close();
            }
        }
    }

}
