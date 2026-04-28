package com.pbc.employee;

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

@WebServlet(description = "Uploads a file", urlPatterns = { "/employee/UploadFilePSRTargets" })
public class UploadFilePSRTargets extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UploadFilePSRTargets() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("PSR Targets Against PJP");

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

						if (name.equals("RequestID")) {
							RequestID = Long.parseLong(value);

						} else if (name.equals("Months")) {
							Month = Long.parseLong(value);
						} else if (name.equals("Years")) {
							Year = Long.parseLong(value);
						} else if (name.equals("UserID")) {
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

						ResultSet rs3 = s.executeQuery("select month,year from pep.employee_targets where month=" + Month
								+ " and year=" + Year + "");
						if (rs3.first()) {

							/*
							 * System.out.println("already Exists"); PrintWriter out = response.getWriter();
							 * out.print("{\"ErrorMsg\": \"Data Already Exist\"}");
							 * out.print("{\"Success\": \"false\"}"); out.close();
							 */
//							/s1.executeUpdate("insert into "+ds.logDatabaseName()+
							// Deleting from table first --- Patch added by Zulqurnan on 31/07/2021
//							System.out.println("INSERT INTO " + ds.logDatabaseName()
//									+ ".`employee_targets_logs`(`target_id`,`pjp_id`,`employee_id`,`month`,`year`,`created_by`,`created_on`,`updated_on`,`updated_by`) (select id,pjp_id,employee_id,month,year,created_by,created_on,now(),"
//									+ created_by + " from employee_targets where month=" + Month + " and year=" + Year
//									+ ")");
							s2.executeUpdate("INSERT INTO " + ds.logDatabaseName()
									+ ".`employee_targets_logs`(`target_id`,`pjp_id`,`employee_id`,`month`,`year`,`created_by`,`created_on`,`updated_on`,`updated_by`) (select id,pjp_id,employee_id,month,year,created_by,created_on,now(),"
									+ created_by + " from pep.employee_targets where month=" + Month + " and year=" + Year
									+ ")");

//							System.out.println("INSERT INTO " + ds.logDatabaseName()
//									+ ".`employee_targets_packages_logs`(`id`,`package_id`,`quantity`,`updated_on`,`updated_by`) (SELECT id,package_id,quantity ,now(),"
//									+ created_by
//									+ " FROM almoiz_pepemployee_targets_packages where id in(SELECT  id FROM almoiz_pepemployee_targets where month="
//									+ Month + " and year=" + Year + "))");
							s2.executeUpdate("INSERT INTO " + ds.logDatabaseName()
									+ ".`employee_targets_packages_logs`(`id`,`package_id`,`quantity`,`updated_on`,`updated_by`) (SELECT id,package_id,quantity ,now(),"
									+ created_by
									+ " FROM pep.employee_targets_packages where id in(SELECT  id FROM pep.employee_targets where month="
									+ Month + " and year=" + Year + "))");

//							System.out.println("INSERT INTO " + ds.logDatabaseName()
//									+ ".`employee_targets_packages_brands_logs`(`id`,`package_id`,`brand_id`,`quantity`,`updated_on`,`updated_by`) (SELECT id,package_id,brand_id,quantity,now(),"
//									+ created_by
//									+ " FROM almoiz_pepemployee_targets_packages_brands where id in(SELECT  id FROM almoiz_pepemployee_targets where month="
//									+ Month + " and year=" + Year + "))");
							s2.executeUpdate("INSERT INTO " + ds.logDatabaseName()
									+ ".`employee_targets_packages_brands_logs`(`id`,`package_id`,`brand_id`,`quantity`,`updated_on`,`updated_by`) (SELECT id,package_id,brand_id,quantity,now(),"
									+ created_by
									+ " FROM pep.employee_targets_packages_brands where id in(SELECT  id FROM pep.employee_targets where month="
									+ Month + " and year=" + Year + "))");
							s.executeUpdate(
									"delete from pep.employee_targets_packages_brands where id in (SELECT id FROM pep.employee_targets where month="
											+ Month + " and year=" + Year + ")");
							s.executeUpdate(
									"delete FROM pep.employee_targets_packages where id in (SELECT id FROM pep.employee_targets where month="
											+ Month + " and year=" + Year + ")");
							s.executeUpdate(
									"delete FROM pep.employee_targets where month=" + Month + " and year=" + Year);

						}

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
							String pjpid = "";
							String psrid = "";
							// System.out.println("No. "+i);
							// System.out.println("------------------------------");
							int t = 0;
							for (int j = 0; j < strar.length; j++) {

								if (i > 1) {
									if (j == 0) {
										psrid = strar[j];
									} else if (j == 1) {
										pjpid = strar[j];
										if (!pjpid.equals("")) {
											psrid = (psrid.equals("")) ? "0" : psrid;
											
											  System.out.println(
											  "insert into pepemployee_targets (employee_id,month,year,created_by,created_on,pjp_id) values("
											  + psrid + "," + Month + "," + Year + "," + created_by + ",now()," + pjpid
											  + ")");
											 
											s2.executeUpdate("insert into pep.employee_targets (employee_id,month,year,created_by,created_on,pjp_id) values("
															+ psrid + "," + Month + "," + Year + "," + created_by
															+ ",now()," + pjpid + ")");
											ResultSet rs = s2.executeQuery("select LAST_INSERT_ID()");
											if (rs.first()) {
												AutoID = rs.getInt(1);
											}
										}
									} else {
										if (!strar[j].equals("0.0000") && !strar[j].equals("0") && !strar[j].equals("")
												&& !pjpid.equals("")) {
										  //System.out.println("SELECT brand_id,package_id,liquid_in_ml FROM pep.inventory_products_view where sap_code="+productCoe.get(j));
											ResultSet rsData = s.executeQuery(
													"SELECT brand_id,package_id,liquid_in_ml FROM pep.inventory_products_view where sap_code="
															+ productCoe.get(j));
											if (rsData.first()) {
												double target = Double.parseDouble(strar[j])
														/ rsData.getDouble("liquid_in_ml");
												DecimalFormat targetFor = new DecimalFormat("##.00");

//											System.out.println(
//														"insert into pep.employee_targets_packages(id,package_id,quantity) values('"
//																+ AutoID + "','" + rsData.getInt("package_id") + "','"
//																+ targetFor.format(target) + "')");
//												s3.executeUpdate(
//														"insert into pep.employee_targets_packages(id,package_id,quantity) values('"
//																+ AutoID + "','" + rsData.getInt("package_id") + "','"
//																+ targetFor.format(target) + "')");
//												System.out.println(
//														"insert into pep.employee_targets_packages_brands(id,package_id,brand_id,quantity) values('"
//																+ AutoID + "','" + rsData.getInt("package_id") + "','"
//																+ rsData.getInt("brand_id") + "','"
//																+ targetFor.format(target) + "')");
												s3.executeUpdate(
														"insert into pep.employee_targets_packages_brands(id,package_id,brand_id,quantity) values('"
																+ AutoID + "','" + rsData.getInt("package_id") + "','"
																+ rsData.getInt("brand_id") + "','"
																+ targetFor.format(target) + "')");

												/*
												 * System.out.print("Product Code : "+j+" : "+productCoe.get(j));
												 * System.out.print(" PSR ID : "+psrid);
												 * System.out.print(" PJP ID : "+pjpid);
												 * System.out.print(" Brand ID : "+rsData.getInt("brand_id"));
												 * System.out.print(" Package ID : "+rsData.getInt("package_id"));
												 * System.out.print(" Liquid In ML : "+
												 * rsData.getDouble("liquid_in_ml"));
												 * System.out.print(" Target : "+strar[j]);
												 * System.out.print("Final Target : "+target);
												 * System.out.print("Final Target 1 : "+targetFor.format(target));
												 * System.out.println("");
												 */
											}
										}

									}
									// System.out.println("Data : "+strar[j]);
								} else if (i == 1) {
									productCoe.add(strar[j]);

								}
								t++;
							}
							// System.out.println("Total columns : "+t);
							// System.out.println("------------------------------");

							/*
							 * for(int j=0;j<productCoe.size();j++) {
							 * 
							 * System.out.println("Product Code : "+j+" : "+productCoe.get(j));
							 * 
							 * }
							 */

							i++;
						}
						// s3.executeUpdate("insert into
						// employee_targets_packages(id,package_id,quantity) select
						// id,package_id,sum(quantity) from employee_targets_packages_brands where id in
						// (select id from employee_targets where created_by="+created_by+" and
						// year="+Year+" and month="+Month+") group by id,package_id;");

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