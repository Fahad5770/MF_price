package com.pbc.employee;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
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

import com.mf.utils.MFParseUtils;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

@WebServlet(description = "Uploads Distributor Targets", urlPatterns = { "/employee/UploadFileDistributorTargets" })
public class UploadFileDistributorTargets extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UploadFileDistributorTargets() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	@SuppressWarnings({ "rawtypes", "unused", "resource", "deprecation" })
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("Distributor Targets Against PJP");

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
			Statement s2 = null;
			Statement s3 = null;
			long RequestID = 123;

			int Month = 0;
			int Year = 0;
			String created_by = UserID;

			try {
				ds = new Datasource();
				ds.createConnection();
				ds.startTransaction();
				s = ds.createStatement();
				s2 = ds.createStatement();
				s3 = ds.createStatement();

				ArrayList<String> productCode = new ArrayList<String>();

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
							Month = MFParseUtils.parseInt(value);
						} else if (name.equals("Years")) {
							Year = MFParseUtils.parseInt(value);
						} else if (name.equals("UserID")) {
							created_by = value;
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

						System.out.println("select month,year from pep.distributor_targets where month=" + Month
								+ " and year=" + Year + "");
						ResultSet rs3 = s.executeQuery("select month,year from pep.distributor_targets where month="
								+ Month + " and year=" + Year + "");
						if (rs3.first()) {
							// System.out.println("INSERT INTO " + ds.logDatabaseName()
							// +
							// ".`distributor_targets_logs`(`target_id`,`distributor_id`,`month`,`year`,`created_by`,`created_on`,`updated_on`,`updated_by`)
							// (select id,distributor_id,month,year,created_by,created_on,now(),"
							// + created_by + " from pep.distributor_targets where month=" + Month + " and
							// year="
							// + Year + ")");
							s2.executeUpdate("INSERT INTO " + ds.logDatabaseName()
									+ ".`distributor_targets_logs`(`target_id`,`distributor_id`,`month`,`year`,`created_by`,`created_on`,`updated_on`,`updated_by`) (select id,distributor_id,month,year,created_by,created_on,now(),"
									+ created_by + " from pep.distributor_targets where month=" + Month + " and year="
									+ Year + ")");

							// System.out.println("INSERT INTO " + ds.logDatabaseName()
							// +
							// ".`distributor_targets_packages_logs`(`id`,`package_id`,`quantity`,`updated_on`,`updated_by`)
							// (SELECT id,package_id,quantity ,now(),"
							// + created_by
							// + " FROM pep.distributor_targets_packages where id in(SELECT id FROM
							// pep.distributor_targets where month="
							// + Month + " and year=" + Year + "))");
							s2.executeUpdate("INSERT INTO " + ds.logDatabaseName()
									+ ".`distributor_targets_packages_logs`(`id`,`package_id`,`quantity`,`updated_on`,`updated_by`) (SELECT id,package_id,quantity ,now(),"
									+ created_by
									+ " FROM pep.distributor_targets_packages where id in(SELECT  id FROM pep.distributor_targets where month="
									+ Month + " and year=" + Year + "))");

							// System.out.println("INSERT INTO " + ds.logDatabaseName()
							// +
							// ".`distributor_targets_packages_brands_logs`(`id`,`package_id`,`brand_id`,`quantity`,`updated_on`,`updated_by`)
							// (SELECT id,package_id,brand_id,quantity,now(),"
							// + created_by
							// + " FROM pep.distributor_targets_packages_brands where id in(SELECT id FROM
							// pep.distributor_targets where month="
							// + Month + " and year=" + Year + "))");
							s2.executeUpdate("INSERT INTO " + ds.logDatabaseName()
									+ ".`distributor_targets_packages_brands_logs`(`id`,`package_id`,`brand_id`,`quantity`,`updated_on`,`updated_by`) (SELECT id,package_id,brand_id,quantity,now(),"
									+ created_by
									+ " FROM pep.distributor_targets_packages_brands where id in(SELECT  id FROM pep.distributor_targets where month="
									+ Month + " and year=" + Year + "))");
							// System.out.println(
							// "delete from pep.distributor_targets_packages_brands where id in (SELECT id
							// FROM pep.distributor_targets where month="
							// + Month + " and year=" + Year + ")");
							s.executeUpdate(
									"delete from pep.distributor_targets_packages_brands where id in (SELECT id FROM pep.distributor_targets where month="
											+ Month + " and year=" + Year + ")");
							// System.out.println(
							// "delete FROM pep.distributor_targets_packages where id in (SELECT id FROM
							// pep.distributor_targets where month="
							// + Month + " and year=" + Year + ")");
							s.executeUpdate(
									"delete FROM pep.distributor_targets_packages where id in (SELECT id FROM pep.distributor_targets where month="
											+ Month + " and year=" + Year + ")");
							// System.out.println(
							// "delete FROM pep.distributor_targets where month=" + Month + " and year=" +
							// Year);
							s.executeUpdate(
									"delete FROM pep.distributor_targets where month=" + Month + " and year=" + Year);

						}

						String fName = uploadedFile.getPath();
						String thisLine;
						int count = 0;
						FileInputStream fis = new FileInputStream(fName);
						DataInputStream myInput = new DataInputStream(fis);
						int i = 0;
						int AutoID = 0;
						// ArrayList<String> productCoe = new ArrayList<String>();
						int PackID = 0;
						int BrandID = 0;
						while ((thisLine = myInput.readLine()) != null) {

							String strar[] = thisLine.split(",");

							String distributor_id = "";

							// int t = 0;
							for (int j = 0; j < strar.length; j++) {

								if (i > 1) {
									if (j == 0) {
										// System.out.println("i "+i);
										// System.out.println("j "+j);
										distributor_id = strar[j];
										if (!distributor_id.equals("")) {
											// s.executeQuery("select * from distributor_targets where month="+Month+"
											// and year="+Year+" and distributor_id="+distributor_id);

											System.out.println(
													"insert into pep.distributor_targets (distributor_id,month,year,created_by,created_on,type_id) values("
															+ distributor_id + "," + Month + "," + Year + ","
															+ created_by
															+ ",now(), (select type_id from common_distributors where distributor_id="
															+ distributor_id + "))");

											s2.executeUpdate(
													"insert into pep.distributor_targets (distributor_id,month,year,created_by,created_on,type_id) values("
															+ distributor_id + "," + Month + "," + Year + ","
															+ created_by
															+ ",now(), (select type_id from common_distributors where distributor_id="
															+ distributor_id + "))");
											ResultSet rs = s2.executeQuery("select LAST_INSERT_ID()");
											if (rs.first()) {
												AutoID = rs.getInt(1);
											}
										}
									} else {
										// System.out.println("i "+i);
										// System.out.println("j "+j);
										// System.out.println("strar[j] "+strar[j]);
										if (!strar[j].equals("0.0000") && !strar[j].equals("0")
												&& !strar[j].equals("")) {
											System.out.println(
													"SELECT brand_id,package_id,liquid_in_ml FROM pep.inventory_products_view where sap_code="
															+ productCode.get(j));
											ResultSet rsData = s.executeQuery(
													"SELECT brand_id,package_id,liquid_in_ml FROM pep.inventory_products_view where sap_code="
															+ productCode.get(j));
											if (rsData.first()) {
												double target = Double.parseDouble(strar[j])
														/ rsData.getDouble("liquid_in_ml");
												DecimalFormat targetFor = new DecimalFormat("##.00");

												System.out.println(
														"insert into pep.distributor_targets_packages(id,package_id,quantity) values('"
																+ AutoID + "','" + rsData.getInt("package_id") + "','"
																+ targetFor.format(target) + "')");
												s3.executeUpdate(
														"insert into pep.distributor_targets_packages(id,package_id,quantity) values('"
																+ AutoID + "','" + rsData.getInt("package_id") + "','"
																+ targetFor.format(target) + "')");

												System.out.println(
														"insert into pep.distributor_targets_packages_brands(id,package_id,brand_id,quantity) values('"
																+ AutoID + "','" + rsData.getInt("package_id") + "','"
																+ rsData.getInt("brand_id") + "','"
																+ targetFor.format(target) + "')");
												s3.executeUpdate(
														"insert into pep.distributor_targets_packages_brands(id,package_id,brand_id,quantity) values('"
																+ AutoID + "','" + rsData.getInt("package_id") + "','"
																+ rsData.getInt("brand_id") + "','"
																+ targetFor.format(target) + "')");

											}
										}

									}
								} else if (i == 1) {
									productCode.add(strar[j]);

								}
								// t++;
							}

							i++;
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