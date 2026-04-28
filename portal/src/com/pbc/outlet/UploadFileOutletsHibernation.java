package com.pbc.outlet;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

@WebServlet(description = "Uploads a file", urlPatterns = { "/outlet/UploadFileOutletsHibernation" })
public class UploadFileOutletsHibernation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UploadFileOutletsHibernation() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	@SuppressWarnings({ "rawtypes", "resource", "deprecation" })
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("Upload File Outlets Hibernation");

		HttpSession session = request.getSession();

		String UserID = null;

		if (session.getAttribute("UserID") != null) {
			UserID = (String) session.getAttribute("UserID");
		}

		if (UserID == null) {
			response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
		}

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		if (isMultipart == true) {

			Datasource ds = null;
			Statement s = null;

			long RequestID = 123;
			int statusActivation = 0;

			try {
				ds = new Datasource();
				ds.createConnection();
				ds.startTransaction();
				s = ds.createStatement();

				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();

				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (item.isFormField()) {
						String name = item.getFieldName();
						String value = item.getString();

						if (name.equals("RequestID")) {
							RequestID = Long.parseLong(value);

						} else if (name.equals("status")) {
							statusActivation = Utilities.parseInt(value);
						}
					}
				}

				statusActivation = (statusActivation == 2) ? 0 : 1;

				iterator = items.iterator();
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();

					if (!item.isFormField()) {
						String fileName = item.getName();

						File path = new File(Utilities.getWorkflowAttachmentsPath());

						File uploadedFile = new File(path + "/" + RequestID + "_" + fileName);
						item.write(uploadedFile);

						String fName = uploadedFile.getPath();
						String thisLine;
						int count = 0;
						FileInputStream fis = new FileInputStream(fName);
						DataInputStream myInput = new DataInputStream(fis);

						while ((thisLine = myInput.readLine()) != null) {

							if (count != 0) {
								String strar[] = thisLine.split(",");
								String pjpId = strar[0];
								String outletId = strar[1];

								System.out.println(
										"update distributor_beat_plan_schedule set is_active=" + statusActivation
												+ " where outlet_id=" + outletId + " and id = " + pjpId + " ");
								s.executeUpdate(
										"update distributor_beat_plan_schedule set is_active=" + statusActivation
												+ " where outlet_id=" + outletId + " and id = " + pjpId + " ");

							}

							count++;
						}

						PrintWriter out = response.getWriter();
						out.print("{\"Success\": \"true\"}");
						out.close();

					}

				}

				ds.commit();
			} catch (Exception e) {
				try {
					ds.rollback();
					System.out.println("RollBack");
				} catch (SQLException e1) {

					e1.printStackTrace();
				}
				e.printStackTrace();
			} finally {
				try {
					if (s != null) {
						s.close();
						ds.dropConnection();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}

	}

}