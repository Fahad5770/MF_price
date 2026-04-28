package com.pbc.outlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.io.*;

@WebServlet(description = "Uploads a file", urlPatterns = { "/outlet/UploadFileForGeoFancing" })
public class UploadFileForGeoFancing extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UploadFileForGeoFancing() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("Upload FileFor GeoFancing");

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
			Statement sb = null;
			Statement sp = null;
			Statement s2 = null;
			Statement s3 = null;
			long RequestID = 123;
			long Month = 0;
			long Year = 0;
			long created_by = 0;
			String description = "";

			try {
				ds = new Datasource();
				ds.createConnection();
				ds.startTransaction();
				s = ds.createStatement();
				s2 = ds.createStatement();
				s3 = ds.createStatement();
				sb = ds.createStatement();
				sp = ds.createStatement();
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();

				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (item.isFormField()) {
						String name = item.getFieldName();
						String value = item.getString();

						if (name.equals("UserID")) {
							created_by = Long.parseLong(value);

						}
					}
				}

				iterator = items.iterator();
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();

					if (!item.isFormField()) {
						String fileName = item.getName();

						File path = new File(Utilities.getWorkflowAttachmentsPath());
						if (!path.exists()) {
							boolean status = path.mkdirs();
						}

						File uploadedFile = new File(path + "/" + RequestID + "_" + fileName);
						item.write(uploadedFile);
						

						String fName = uploadedFile.getPath();
						String thisLine;
						int count = 0;
						FileInputStream fis = new FileInputStream(fName);
						DataInputStream myInput = new DataInputStream(fis);
						int i = 0;
						int AutoID = 0;
						ArrayList<String> productCoe = new ArrayList<String>();
						int PackID = 0;
						int BrandID = 0;
						while ((thisLine = myInput.readLine()) != null) {

							String strar[] = thisLine.split(",");
							String outletId="";
							String geoFancing = "";
							String radius = "";
							// System.out.println("No. "+i);
							// System.out.println("------------------------------");
							int t = 0;
							for (int j = 0; j < strar.length; j++) {

								if (i > 0) {
									
									if (j == 0) {
										outletId = strar[j];
									//	System.out.print("outletId : "+outletId);
									} else if (j == 1) {
										geoFancing = strar[j];
									//	System.out.print(" geoFancing : "+geoFancing);
									} else {
										radius = strar[j];
										s.executeUpdate("update common_outlets set Is_Geo_Fence="+geoFancing+", Geo_Radius="+radius+" where id="+outletId);
									//	System.out.print(" radius : "+radius);

									}
									//s.executeUpdate("update common_outlets set Is_Geo_Fence="+geoFancing+", Geo_Radius="+radius+" where id="+outletId);
									
								}
							//	System.out.println();
							
								t++;
							}
							
							// System.out.println("Total columns : "+t);
							

							i++;
						}
	

						PrintWriter out = response.getWriter();
						// out.print("{\"ErrorMsg\": \"Successfully Uploaded\"}");
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