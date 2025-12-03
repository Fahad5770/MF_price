package com.pbc.workflow;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import org.apache.commons.lang3.text.WordUtils;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class WorkflowEmail {

	public WorkflowEmail() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

	}

	public static String getDiscountRequestHTMLWithActionButtons(long RequestIDVal) {

		Datasource ds = new Datasource();

		String html = "";
		try {

			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();

			long UVID = 0;
			long PromotionID = 0;
			String PromotionName = "";
			ResultSet rs = s
					.executeQuery("select * from inventory_sales_discounts_request where request_id = " + RequestIDVal);
			if (rs.first()) {
				// PromotionName = rs.getString("label");
				UVID = rs.getLong("uvid");
				PromotionID = rs.getLong("id");
			}

			String PackageLabel = "";

			String RawCasesLabel = "";
			String UnitLabel = "";

			String PackageLabel1 = "";
			String BrandLabel1 = "";
			String RawCasesLabel1 = "";
			String UnitLabel1 = "";

			String HTMLMessage = "";
			// HTMLMessage = "<table><tr><td style='background: #123123'>Hello
			// 1</td></tr><tr><td>Hello 2</td></tr></table>";

			int MasterSerialID = 0;
			html = "<html>";
			html += "<body><br>";

			html += "<table style='width: 670px;'>";

			//

			ResultSet rs6 = s1.executeQuery(
					"SELECT isprp.id, isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_labele_id,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id,isprp.quantity,isprp.discount_rate,isprp.selling_price,isprp.variable_cost,isprp.other_cost,isprp.promotion_cost,isprp.marginal_contribution,isprp.serial_no FROM inventory_sales_discounts_request_products isprp join inventory_sales_discounts_request ispr on ispr.id=isprp.id where ispr.request_id="
							+ RequestIDVal + " and isprp.type_id=1");
			while (rs6.next()) {

				PackageLabel = rs6.getString("package_labele_id");
				MasterSerialID = rs6.getInt("serial_no");

				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='6'>"
						+ PackageLabel + "</td>";
				html += "</tr>";

				double Quantity = rs6.getDouble("quantity");
				double DiscountRate = rs6.getDouble("discount_rate");
				double SellingPrice = rs6.getDouble("selling_price");
				double VariableCost = rs6.getDouble("variable_cost");
				double OtherCost = rs6.getDouble("other_cost");
				double PromotionCost = rs6.getDouble("promotion_cost");
				double MarginalContribution = (SellingPrice - VariableCost - PromotionCost - OtherCost);
				double MCPercent = (MarginalContribution / SellingPrice) * 100;

				int PackageID = rs6.getInt("package_id");
				int ProductTableID = rs6.getInt("id");

				int PackageCounter = 1;

				PackageLabel = rs6.getString("package_labele_id");

				// html += "<td colspan='3' valign='top'
				// style='background-color: #EDEFF2;'>"+PackageLabel;
				String BrandLabel = "";
				ResultSet rs2 = s2.executeQuery(
						"SELECT isprp.id,isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_label,isprpb.brand_id,(select label from inventory_brands ib where ib.id=isprpb.brand_id) brand_label,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id,isprpb.serial_no FROM inventory_sales_discounts_request_products isprp join inventory_sales_discounts_request_products_brands isprpb on isprp.id=isprpb.id and isprp.serial_no=isprpb.serial_no and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_sales_discounts_request ispr on ispr.id=isprp.id where ispr.request_id="
								+ RequestIDVal + " and isprp.type_id=1 and isprp.package_id="
								+ rs6.getLong("package_id") + " and isprp.serial_no=" + MasterSerialID
								+ " group by isprp.type_id,isprpb.brand_id");
				while (rs2.next()) {

					if (!rs2.isFirst()) {
						BrandLabel = BrandLabel + ", ";
					}

					BrandLabel += rs2.getString("brand_label");

				}

				html += "<td colspan='6' style='background-color: #EDEFF2;'>" + BrandLabel;

				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Quantity</td>";
				html += "<td style='background-color: #FCFFE6; height: 18px; text-align: center; font-weight: bold;'>Current Price</td>";
				html += "<td style='background-color: #FCFFE6; height: 18px; text-align: center; font-weight: bold;'>Proposed Price</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Variable Cost</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Promotion/Other Cost</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold; min-width: 80px'>MC</td>";
				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Utilities.getDisplayCurrencyFormat(Quantity) + "</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Math.round(DiscountRate) + "</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Math.round(SellingPrice) + "</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Math.round(VariableCost) + "</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Math.round(PromotionCost + OtherCost) + "</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Math.round(MarginalContribution) + " (" + Math.round(MCPercent) + "%)</td>";
				html += "</tr>";

				html += "<tr>";
				html += "<td colspan='6'>&nbsp;";
				html += "</tr>";

			}

			String ValidToDateString = "";
			String ValidToTimeString = "";

			ResultSet rs3 = s.executeQuery(
					"select *,DATE_FORMAT(valid_to,'%b %d %Y') sent_date_valid,DATE_FORMAT(valid_to,'%h:%i %p') sent_time_valid from inventory_sales_discounts_request where request_id="
							+ RequestIDVal);
			while (rs3.next()) {
				ValidToDateString = rs3.getString("sent_date_valid");
				ValidToTimeString = rs3.getString("sent_time_valid");

				html += "<tr><td>&nbsp;</td></tr>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='6'>Reason</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;' colspan='6'>"
						+ rs3.getString("comments") + "</td>";

				html += "</tr>";

			}

			html += "</table>";
			html += "<br>";
			html += "<table style='width: 670px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Messages</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs4 = s.executeQuery(
					"SELECT message,sent_by,(select DISPLAY_NAME from users u where u.id=wrcm.sent_by) sent_by_name,sent_on,DATE_FORMAT(sent_on,'%b %d %Y') sent_date,DATE_FORMAT(sent_on,'%h:%i %p') sent_time FROM workflow_requests_chat wrc join workflow_requests_chat_messages wrcm on wrc.conversation_id=wrcm.conversation_id and  wrc.request_id="
							+ RequestIDVal);
			while (rs4.next()) {

				html += "<b>" + rs4.getString("sent_by_name") + ":</b> " + rs4.getString("message") + " ["
						+ rs4.getString("sent_date") + " | " + rs4.getString("sent_time") + "]<br>";

			}

			ResultSet rs14 = s.executeQuery(
					"SELECT cd.distributor_id,cd.name FROM inventory_sales_discounts_request_distributors isprd join inventory_sales_discounts_request ispr on isprd.product_promotion_id=ispr.id join common_distributors cd on cd.distributor_id=isprd.distributor_id where ispr.id="
							+ PromotionID);
			if (rs14.first()) {
				html += "<br>";
				html += "<table style='width: 670px;'>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Customer</td>";
				html += "</tr>";
				html += "</table>";
				ResultSet rs10 = s.executeQuery(
						"SELECT cd.distributor_id,cd.name FROM inventory_sales_discounts_request_distributors isprd join inventory_sales_discounts_request ispr on isprd.product_promotion_id=ispr.id join common_distributors cd on cd.distributor_id=isprd.distributor_id where ispr.id="
								+ PromotionID);
				while (rs10.next()) {
					html += rs10.getString("distributor_id") + " - " + rs10.getString("name") + "<br/>";
				}
			}

			html += "<br>";
			html += "<table style='width: 670px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Workflow</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs5 = s.executeQuery(
					"SELECT action_label_past, display_name, DATE_FORMAT(completed_on,'%b %d %Y') sent_date, DATE_FORMAT(completed_on,'%h:%i %p') sent_time FROM workflow_requests_steps wrs join workflow_actions wa on wrs.action_id = wa.action_id join users u on wrs.user_id = u.id where request_id="
							+ RequestIDVal + " and wrs.completed_on is not null  order by step_id desc");
			while (rs5.next()) {
				html += "<b>" + rs5.getString("action_label_past") + "</b> by " + rs5.getString("display_name") + " ["
						+ rs5.getString("sent_date") + " | " + rs5.getString("sent_time") + "]<br>";
			}

			html += "<br/><b>Valid until</b> " + ValidToDateString + "<br>";
			html += "<br>";

			html += "<a href='http://203.124.57.25/portal/WM/WMD?token=" + UVID + "&action=1&sessionid=" + Math.random()
					+ "'>Approve</a>    |    <a href='http://203.124.57.25/portal/WM/WMD?token=" + UVID
					+ "&action=2&sessionid=" + Math.random() + "'>Decline</a>";
			// html += "<a
			// href='http://localhost:8080/portal/WM/WMP?token="+UVID+"&action=1&sessionid="+Math.random()+"'>Approve</a>
			// | <a
			// href='http://localhost:8080/portal/WM/WMP?token="+UVID+"&action=2&sessionid="+Math.random()+"'>Decline</a>";

			html += "</body>";

			html += "</html>";

		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return html;

	}

	public static String getDiscountRequestHTMLWithActionButtonsCOO(long RequestIDVal) {

		Datasource ds = new Datasource();

		String html = "";
		try {

			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();

			long UVID = 0;
			long PromotionID = 0;
			String PromotionName = "";
			ResultSet rs = s
					.executeQuery("select * from inventory_sales_discounts_request where request_id = " + RequestIDVal);
			if (rs.first()) {
				// PromotionName = rs.getString("label");
				UVID = rs.getLong("uvid");
				PromotionID = rs.getLong("id");
			}

			String PackageLabel = "";

			String RawCasesLabel = "";
			String UnitLabel = "";

			String PackageLabel1 = "";
			String BrandLabel1 = "";
			String RawCasesLabel1 = "";
			String UnitLabel1 = "";
			int MasterSerialID = 0;
			String HTMLMessage = "";
			// HTMLMessage = "<table><tr><td style='background: #123123'>Hello
			// 1</td></tr><tr><td>Hello 2</td></tr></table>";

			html = "<html>";
			html += "<body><br>";

			html += "<table style='width: 670px;'>";

			ResultSet rs6 = s1.executeQuery(
					"SELECT isprp.id, isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_labele_id,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id,isprp.quantity,isprp.discount_rate,isprp.selling_price,isprp.variable_cost,isprp.other_cost,isprp.promotion_cost,isprp.marginal_contribution,isprp.serial_no FROM inventory_sales_discounts_request_products isprp join inventory_sales_discounts_request ispr on ispr.id=isprp.id where ispr.request_id="
							+ RequestIDVal + " and isprp.type_id=1");
			while (rs6.next()) {

				PackageLabel = rs6.getString("package_labele_id");

				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='6'>"
						+ PackageLabel + "</td>";
				html += "</tr>";

				double Quantity = rs6.getDouble("quantity");
				double DiscountRate = rs6.getDouble("discount_rate");
				double SellingPrice = rs6.getDouble("selling_price");
				double VariableCost = rs6.getDouble("variable_cost");
				double OtherCost = rs6.getDouble("other_cost");
				double PromotionCost = rs6.getDouble("promotion_cost");
				double MarginalContribution = (SellingPrice - VariableCost - PromotionCost - OtherCost);
				double MCPercent = (MarginalContribution / SellingPrice) * 100;

				int PackageID = rs6.getInt("package_id");
				int ProductTableID = rs6.getInt("id");

				int PackageCounter = 1;

				PackageLabel = rs6.getString("package_labele_id");
				MasterSerialID = rs6.getInt("serial_no");

				// html += "<td colspan='3' valign='top'
				// style='background-color: #EDEFF2;'>"+PackageLabel;
				String BrandLabel = "";
				ResultSet rs2 = s2.executeQuery(
						"SELECT isprp.id,isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_label,isprpb.brand_id,(select label from inventory_brands ib where ib.id=isprpb.brand_id) brand_label,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id,isprpb.serial_no FROM inventory_sales_discounts_request_products isprp join inventory_sales_discounts_request_products_brands isprpb on isprp.id=isprpb.id and isprp.serial_no=isprpb.serial_no and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_sales_discounts_request ispr on ispr.id=isprp.id where ispr.request_id="
								+ RequestIDVal + " and isprp.type_id=1 and isprp.package_id="
								+ rs6.getLong("package_id") + " and isprp.serial_no=" + MasterSerialID
								+ " group by isprp.type_id,isprpb.brand_id");
				while (rs2.next()) {

					if (!rs2.isFirst()) {
						BrandLabel = BrandLabel + ", ";
					}

					BrandLabel += rs2.getString("brand_label");

				}

				html += "<td colspan='6' style='background-color: #EDEFF2;'>" + BrandLabel;

				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Quantity</td>";
				html += "<td style='background-color: #FCFFE6; height: 18px; text-align: center; font-weight: bold;'>Current Price</td>";
				html += "<td style='background-color: #FCFFE6; height: 18px; text-align: center; font-weight: bold;'>Proposed Price</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Variable Cost</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Promotion/Other Cost</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold; min-width: 80px'>MC</td>";
				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Utilities.getDisplayCurrencyFormat(Quantity) + "</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Math.round(DiscountRate) + "</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Math.round(SellingPrice) + "</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Math.round(VariableCost) + "</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Math.round(PromotionCost + OtherCost) + "</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Math.round(MarginalContribution) + " (" + Math.round(MCPercent) + "%)</td>";
				html += "</tr>";

				html += "<tr>";
				html += "<td colspan='6'>&nbsp;";
				html += "</tr>";

			}

			String ValidToDateString = "";
			String ValidToTimeString = "";

			ResultSet rs3 = s.executeQuery(
					"select *,DATE_FORMAT(valid_to,'%b %d %Y') sent_date_valid,DATE_FORMAT(valid_to,'%h:%i %p') sent_time_valid from inventory_sales_discounts_request where request_id="
							+ RequestIDVal);
			while (rs3.next()) {
				ValidToDateString = rs3.getString("sent_date_valid");
				ValidToTimeString = rs3.getString("sent_time_valid");

				html += "<tr><td>&nbsp;</td></tr>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='6'>Reason</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;' colspan='6'>"
						+ rs3.getString("comments") + "</td>";

				html += "</tr>";

			}

			html += "</table>";
			html += "<br>";
			html += "<table style='width: 670px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Messages</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs4 = s.executeQuery(
					"SELECT message,sent_by,(select DISPLAY_NAME from users u where u.id=wrcm.sent_by) sent_by_name,sent_on,DATE_FORMAT(sent_on,'%b %d %Y') sent_date,DATE_FORMAT(sent_on,'%h:%i %p') sent_time FROM workflow_requests_chat wrc join workflow_requests_chat_messages wrcm on wrc.conversation_id=wrcm.conversation_id and  wrc.request_id="
							+ RequestIDVal);
			while (rs4.next()) {

				html += "<b>" + rs4.getString("sent_by_name") + ":</b> " + rs4.getString("message") + " ["
						+ rs4.getString("sent_date") + " | " + rs4.getString("sent_time") + "]<br>";

			}

			ResultSet rs14 = s.executeQuery(
					"SELECT cd.distributor_id,cd.name FROM inventory_sales_discounts_request_distributors isprd join inventory_sales_discounts_request ispr on isprd.product_promotion_id=ispr.id join common_distributors cd on cd.distributor_id=isprd.distributor_id where ispr.id="
							+ PromotionID);
			if (rs14.first()) {
				html += "<br>";
				html += "<table style='width: 670px;'>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Customer</td>";
				html += "</tr>";
				html += "</table>";
				ResultSet rs10 = s.executeQuery(
						"SELECT cd.distributor_id,cd.name FROM inventory_sales_discounts_request_distributors isprd join inventory_sales_discounts_request ispr on isprd.product_promotion_id=ispr.id join common_distributors cd on cd.distributor_id=isprd.distributor_id where ispr.id="
								+ PromotionID);
				while (rs10.next()) {
					html += rs10.getString("distributor_id") + " - " + rs10.getString("name") + "<br/>";
				}
			}

			html += "<br>";
			html += "<table style='width: 670px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Workflow</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs5 = s.executeQuery(
					"SELECT action_label_past, display_name, DATE_FORMAT(completed_on,'%b %d %Y') sent_date, DATE_FORMAT(completed_on,'%h:%i %p') sent_time FROM workflow_requests_steps wrs join workflow_actions wa on wrs.action_id = wa.action_id join users u on wrs.user_id = u.id where request_id="
							+ RequestIDVal + " and wrs.completed_on is not null  order by step_id desc");
			while (rs5.next()) {
				html += "<b>" + rs5.getString("action_label_past") + "</b> by " + rs5.getString("display_name") + " ["
						+ rs5.getString("sent_date") + " | " + rs5.getString("sent_time") + "]<br>";
			}

			html += "<br/><b>Valid until</b> " + ValidToDateString + "<br>";
			html += "<br>";

			html += "<a href='http://203.124.57.25/portal/WM/WMD?token=" + UVID + "&action=1&secondary=1&sessionid="
					+ Math.random() + "'>Approve</a>    |    <a href='http://203.124.57.25/portal/WM/WMD?token=" + UVID
					+ "&action=2&secondary=1&sessionid=" + Math.random() + "'>Decline</a>";
			// html += "<a
			// href='http://localhost:8080/portal/WM/WMP?token="+UVID+"&action=1&sessionid="+Math.random()+"'>Approve</a>
			// | <a
			// href='http://localhost:8080/portal/WM/WMP?token="+UVID+"&action=2&sessionid="+Math.random()+"'>Decline</a>";

			html += "</body>";

			html += "</html>";

		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return html;

	}

	public static String getDiscountRequestHTMLWithoutActionButtons(long RequestIDVal) {

		Datasource ds = new Datasource();

		String html = "";
		try {

			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();

			long UVID = 0;
			long PromotionID = 0;
			String PromotionName = "";
			ResultSet rs = s
					.executeQuery("select * from inventory_sales_discounts_request where request_id = " + RequestIDVal);
			if (rs.first()) {
				// PromotionName = rs.getString("label");
				UVID = rs.getLong("uvid");
				PromotionID = rs.getLong("id");
			}

			String PackageLabel = "";

			String RawCasesLabel = "";
			String UnitLabel = "";

			String PackageLabel1 = "";
			String BrandLabel1 = "";
			String RawCasesLabel1 = "";
			String UnitLabel1 = "";
			int MasterSerialID = 0;
			String HTMLMessage = "";
			// HTMLMessage = "<table><tr><td style='background: #123123'>Hello
			// 1</td></tr><tr><td>Hello 2</td></tr></table>";

			html = "<html>";
			html += "<body><br>";

			html += "<table style='width: 670px;'>";

			// aa
			ResultSet rs6 = s1.executeQuery(
					"SELECT isprp.id, isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_labele_id,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id,isprp.quantity,isprp.discount_rate,isprp.selling_price,isprp.variable_cost,isprp.other_cost,isprp.promotion_cost,isprp.marginal_contribution,isprp.serial_no FROM inventory_sales_discounts_request_products isprp join inventory_sales_discounts_request ispr on ispr.id=isprp.id where ispr.request_id="
							+ RequestIDVal + " and isprp.type_id=1");
			while (rs6.next()) {

				PackageLabel = rs6.getString("package_labele_id");

				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='6'>"
						+ PackageLabel + "</td>";
				html += "</tr>";

				double Quantity = rs6.getDouble("quantity");
				double DiscountRate = rs6.getDouble("discount_rate");
				double SellingPrice = rs6.getDouble("selling_price");
				double VariableCost = rs6.getDouble("variable_cost");
				double OtherCost = rs6.getDouble("other_cost");
				double PromotionCost = rs6.getDouble("promotion_cost");
				double MarginalContribution = (SellingPrice - VariableCost - PromotionCost - OtherCost);
				double MCPercent = (MarginalContribution / SellingPrice) * 100;

				int PackageID = rs6.getInt("package_id");
				int ProductTableID = rs6.getInt("id");

				int PackageCounter = 1;

				PackageLabel = rs6.getString("package_labele_id");
				MasterSerialID = rs6.getInt("serial_no");

				// html += "<td colspan='3' valign='top'
				// style='background-color: #EDEFF2;'>"+PackageLabel;
				String BrandLabel = "";
				ResultSet rs2 = s2.executeQuery(
						"SELECT isprp.id,isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_label,isprpb.brand_id,(select label from inventory_brands ib where ib.id=isprpb.brand_id) brand_label,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id,isprpb.serial_no FROM inventory_sales_discounts_request_products isprp join inventory_sales_discounts_request_products_brands isprpb on isprp.id=isprpb.id and isprp.serial_no=isprpb.serial_no and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_sales_discounts_request ispr on ispr.id=isprp.id where ispr.request_id="
								+ RequestIDVal + " and isprp.type_id=1 and isprp.package_id="
								+ rs6.getLong("package_id") + " and isprp.serial_no=" + MasterSerialID
								+ " group by isprp.type_id,isprpb.brand_id");
				while (rs2.next()) {

					if (!rs2.isFirst()) {
						BrandLabel = BrandLabel + ", ";
					}

					BrandLabel += rs2.getString("brand_label");

				}

				html += "<td colspan='6' style='background-color: #EDEFF2;'>" + BrandLabel;

				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Quantity</td>";
				html += "<td style='background-color: #FCFFE6; height: 18px; text-align: center; font-weight: bold;'>Current Price</td>";
				html += "<td style='background-color: #FCFFE6; height: 18px; text-align: center; font-weight: bold;'>Proposed Price</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'></td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'></td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold; min-width: 80px'></td>";
				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Utilities.getDisplayCurrencyFormat(Quantity) + "</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Math.round(DiscountRate) + "</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Math.round(SellingPrice) + "</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'></td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'></td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'></td>";
				html += "</tr>";

				html += "<tr>";
				html += "<td colspan='6'>&nbsp;";
				html += "</tr>";

			}

			String ValidToDateString = "";
			String ValidToTimeString = "";

			ResultSet rs3 = s.executeQuery(
					"select *,DATE_FORMAT(valid_to,'%b %d %Y') sent_date_valid,DATE_FORMAT(valid_to,'%h:%i %p') sent_time_valid from inventory_sales_discounts_request where request_id="
							+ RequestIDVal);
			while (rs3.next()) {
				ValidToDateString = rs3.getString("sent_date_valid");
				ValidToTimeString = rs3.getString("sent_time_valid");

				html += "<tr><td>&nbsp;</td></tr>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='6'>Reason</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;' colspan='6'>"
						+ rs3.getString("comments") + "</td>";

				html += "</tr>";

			}

			html += "</table>";
			html += "<br>";
			html += "<table style='width: 670px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Messages</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs4 = s.executeQuery(
					"SELECT message,sent_by,(select DISPLAY_NAME from users u where u.id=wrcm.sent_by) sent_by_name,sent_on,DATE_FORMAT(sent_on,'%b %d %Y') sent_date,DATE_FORMAT(sent_on,'%h:%i %p') sent_time FROM workflow_requests_chat wrc join workflow_requests_chat_messages wrcm on wrc.conversation_id=wrcm.conversation_id and  wrc.request_id="
							+ RequestIDVal);
			while (rs4.next()) {

				html += "<b>" + rs4.getString("sent_by_name") + ":</b> " + rs4.getString("message") + " ["
						+ rs4.getString("sent_date") + " | " + rs4.getString("sent_time") + "]<br>";

			}

			ResultSet rs14 = s.executeQuery(
					"SELECT cd.distributor_id,cd.name FROM inventory_sales_discounts_request_distributors isprd join inventory_sales_discounts_request ispr on isprd.product_promotion_id=ispr.id join common_distributors cd on cd.distributor_id=isprd.distributor_id where ispr.id="
							+ PromotionID);
			if (rs14.first()) {
				html += "<br>";
				html += "<table style='width: 670px;'>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Customer</td>";
				html += "</tr>";
				html += "</table>";
				ResultSet rs10 = s.executeQuery(
						"SELECT cd.distributor_id,cd.name FROM inventory_sales_discounts_request_distributors isprd join inventory_sales_discounts_request ispr on isprd.product_promotion_id=ispr.id join common_distributors cd on cd.distributor_id=isprd.distributor_id where ispr.id="
								+ PromotionID);
				while (rs10.next()) {
					html += rs10.getString("distributor_id") + " - " + rs10.getString("name") + "<br/>";
				}
			}

			html += "<br>";
			html += "<table style='width: 670px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Workflow</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs5 = s.executeQuery(
					"SELECT action_label_past, display_name, DATE_FORMAT(completed_on,'%b %d %Y') sent_date, DATE_FORMAT(completed_on,'%h:%i %p') sent_time FROM workflow_requests_steps wrs join workflow_actions wa on wrs.action_id = wa.action_id join users u on wrs.user_id = u.id where request_id="
							+ RequestIDVal + " and wrs.completed_on is not null  order by step_id desc");
			while (rs5.next()) {
				html += "<b>" + rs5.getString("action_label_past") + "</b> by " + rs5.getString("display_name") + " ["
						+ rs5.getString("sent_date") + " | " + rs5.getString("sent_time") + "]<br>";
			}

			html += "<br/><b>Valid until</b> " + ValidToDateString + "<br>";
			html += "<br>";

			html += "</body>";

			html += "</html>";

		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return html;

	}

	public static String getPromotionRequestHTMLWithActionButtons(long RequestIDVal) {

		Datasource ds = new Datasource();

		String html = "";
		try {

			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();

			long UVID = 0;
			long PromotionID = 0;
			String PromotionName = "";
			ResultSet rs = s.executeQuery(
					"select * from inventory_sales_promotions_request where request_id = " + RequestIDVal);
			if (rs.first()) {
				PromotionName = rs.getString("label");
				UVID = rs.getLong("uvid");
				PromotionID = rs.getLong("id");
			}

			String PackageLabel = "";
			String BrandLabel = "";
			String RawCasesLabel = "";
			String UnitLabel = "";

			String PackageLabel1 = "";
			String BrandLabel1 = "";
			String RawCasesLabel1 = "";
			String UnitLabel1 = "";

			String HTMLMessage = "";
			// HTMLMessage = "<table><tr><td style='background: #123123'>Hello
			// 1</td></tr><tr><td>Hello 2</td></tr></table>";

			html = "<html>";
			html += "<body><br>";

			html += "<table style='width: 400px;'>";

			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='4'>Promotional Product</td>";
			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Package</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Brand</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Case</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Bottle</td>";
			html += "</tr>";

			html += "<tr>";

			ResultSet rs6 = s1.executeQuery(
					"SELECT distinct isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_labele_id,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id FROM inventory_sales_promotions_request_products isprp join inventory_sales_promotions_request_products_brands isprpb on isprp.id=isprpb.id and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_sales_promotions_request ispr on ispr.id=isprp.id where ispr.request_id="
							+ RequestIDVal + " and isprp.type_id=1");
			while (rs6.next()) {
				int PackageCounter = 1;
				if (PackageCounter == 1) {
					PackageLabel = rs6.getString("package_labele_id");
				}

				html += "<td valign='top' style='background-color: #EDEFF2;'>" + PackageLabel;

				ResultSet rs2 = s2.executeQuery(
						"SELECT isprp.id,isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_label,isprpb.brand_id,(select label from inventory_brands ib where ib.id=isprpb.brand_id) brand_label,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id FROM inventory_sales_promotions_request_products isprp join inventory_sales_promotions_request_products_brands isprpb on isprp.id=isprpb.id and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_sales_promotions_request ispr on ispr.id=isprp.id where ispr.request_id="
								+ RequestIDVal + " and isprp.type_id=1 and isprp.package_id="
								+ rs6.getLong("package_id") + " group by isprp.type_id,isprpb.brand_id");
				while (rs2.next()) {
					BrandLabel += rs2.getString("brand_label") + "<br/>";
				}

				if (rs6.getLong("raw_cases") != 0) {
					RawCasesLabel = rs6.getString("raw_cases");
				}
				if (rs6.getLong("units") != 0) {
					UnitLabel = rs6.getString("units");
				}

			}

			html += "<td style='background-color: #EDEFF2;'>" + BrandLabel;
			html += "<td valign='top' style='background-color: #EDEFF2; text-align: left; '>" + RawCasesLabel;
			html += "<td valign='top' style='background-color: #EDEFF2; text-align: left;'>" + UnitLabel;
			html += "</tr>";

			html += "<tr>";
			html += "<td colspan='4'>&nbsp;";
			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='4'>Free Product</td>";
			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Package</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Brand</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Case</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Bottle</td>";
			html += "</tr>";

			html += "<tr>";

			ResultSet rs7 = s1.executeQuery(
					"SELECT distinct isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_labele_id,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id FROM inventory_sales_promotions_request_products isprp join inventory_sales_promotions_request_products_brands isprpb on isprp.id=isprpb.id and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_sales_promotions_request ispr on ispr.id=isprp.id where ispr.request_id="
							+ RequestIDVal + " and isprp.type_id=2");
			while (rs7.next()) {
				int PackageCounter = 1;
				if (PackageCounter == 1) {
					PackageLabel1 = rs7.getString("package_labele_id");
				}

				html += "<td valign='top' style='background-color: #EDEFF2;'>" + PackageLabel1;

				ResultSet rs2 = s2.executeQuery(
						"SELECT isprp.id,isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_label,isprpb.brand_id,(select label from inventory_brands ib where ib.id=isprpb.brand_id) brand_label,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id FROM inventory_sales_promotions_request_products isprp join inventory_sales_promotions_request_products_brands isprpb on isprp.id=isprpb.id and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_sales_promotions_request ispr on ispr.id=isprp.id where ispr.request_id="
								+ RequestIDVal + " and isprp.type_id=2 and isprp.package_id="
								+ rs7.getLong("package_id") + " group by isprp.type_id,isprpb.brand_id");
				while (rs2.next()) {
					BrandLabel1 += rs2.getString("brand_label") + "<br/>";
				}

				if (rs7.getLong("raw_cases") != 0) {
					RawCasesLabel1 = rs7.getString("raw_cases");
				}
				if (rs7.getLong("units") != 0) {
					UnitLabel1 = rs7.getString("units");
				}

			}

			html += "<td style='background-color: #EDEFF2;'>" + BrandLabel1;
			html += "<td valign='top' style='background-color: #EDEFF2;text-align: left;'>" + RawCasesLabel1;
			html += "<td valign='top' style='background-color: #EDEFF2;text-align: left;'>" + UnitLabel1;
			html += "</tr>";

			html += "<tr>";
			html += "<td colspan='4'>&nbsp;";
			html += "</tr>";

			html += "</table>";

			html += "<table style='width: 400px;'>";

			String ValidToDateString = "";
			String ValidToTimeString = "";

			ResultSet rs3 = s.executeQuery(
					"select *,DATE_FORMAT(valid_to,'%b %d %Y') sent_date_valid,DATE_FORMAT(valid_to,'%h:%i %p') sent_time_valid from inventory_sales_promotions_request where request_id="
							+ RequestIDVal);
			while (rs3.next()) {
				ValidToDateString = rs3.getString("sent_date_valid");
				ValidToTimeString = rs3.getString("sent_time_valid");

				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='2'>Profitability</td>";
				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;'>Estimated Sales Volume (Cases)</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: right;'>"
						+ Utilities.getDisplayCurrencyFormatRounded(rs3.getDouble("estimated_sales_volume")) + "</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;'>Sales SKU Price</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: right;'>"
						+ Utilities.getDisplayCurrencyFormatRounded(rs3.getDouble("sales_sku_price")) + "</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;'>Free SKU Price (Bottle)</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: right;'>"
						+ Utilities.getDisplayCurrencyFormatRounded(rs3.getDouble("free_sku_price")) + "</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;'>Variable Cost + Taxes</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: right;'>"
						+ Utilities.getDisplayCurrencyFormatRounded(rs3.getDouble("variable_cost_and_taxes")) + "</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Marginal Contribution</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: right; font-weight: bold;'>"
						+ Utilities.getDisplayCurrencyFormatRounded(rs3.getDouble("marginal_contribution")) + "</td>";

				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Net Price</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: right; font-weight: bold;'>"
						+ Utilities.getDisplayCurrencyFormatRounded(rs3.getDouble("net_price")) + "</td>";
				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Promotion Cost</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: right; font-weight: bold;'>"
						+ Utilities.getDisplayCurrencyFormatRounded(
								rs3.getDouble("free_sku_price") * rs3.getDouble("estimated_sales_volume"))
						+ "</td>";
				html += "</tr>";

				html += "<tr><td>&nbsp;</td></tr>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='2'>Reason</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;' colspan='2'>"
						+ rs3.getString("comments") + "</td>";

				html += "</tr>";

			}

			html += "</table>";
			html += "<br>";
			html += "<table style='width: 400px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Messages</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs4 = s.executeQuery(
					"SELECT message,sent_by,(select DISPLAY_NAME from users u where u.id=wrcm.sent_by) sent_by_name,sent_on,DATE_FORMAT(sent_on,'%b %d %Y') sent_date,DATE_FORMAT(sent_on,'%h:%i %p') sent_time FROM workflow_requests_chat wrc join workflow_requests_chat_messages wrcm on wrc.conversation_id=wrcm.conversation_id and  wrc.request_id="
							+ RequestIDVal);
			while (rs4.next()) {

				html += "<b>" + rs4.getString("sent_by_name") + ":</b> " + rs4.getString("message") + " ["
						+ rs4.getString("sent_date") + " | " + rs4.getString("sent_time") + "]<br>";

			}

			ResultSet rs15 = s.executeQuery(
					"SELECT cr.region_short_name FROM inventory_sales_promotions_request_regions isprr join inventory_sales_promotions_request ispr on isprr.product_promotion_id=ispr.id join common_regions cr on cr.region_id=isprr.region_id where ispr.id="
							+ PromotionID);
			if (rs15.first()) {
				html += "<br>";
				html += "<table style='width: 400px;'>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Region</td>";
				html += "</tr>";
				html += "</table>";
				ResultSet rs9 = s.executeQuery(
						"SELECT cr.region_short_name,cr.region_name FROM inventory_sales_promotions_request_regions isprr join inventory_sales_promotions_request ispr on isprr.product_promotion_id=ispr.id join common_regions cr on cr.region_id=isprr.region_id where ispr.id="
								+ PromotionID);
				while (rs9.next()) {
					html += rs9.getString("region_short_name") + " - " + rs9.getString("region_name") + "<br/>";
				}
			}

			ResultSet rs14 = s.executeQuery(
					"SELECT cd.distributor_id,cd.name FROM inventory_sales_promotions_request_distributors isprd join inventory_sales_promotions_request ispr on isprd.product_promotion_id=ispr.id join common_distributors cd on cd.distributor_id=isprd.distributor_id where ispr.id="
							+ PromotionID);
			if (rs14.first()) {
				html += "<br>";
				html += "<table style='width: 400px;'>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Distributor</td>";
				html += "</tr>";
				html += "</table>";
				ResultSet rs10 = s.executeQuery(
						"SELECT cd.distributor_id,cd.name FROM inventory_sales_promotions_request_distributors isprd join inventory_sales_promotions_request ispr on isprd.product_promotion_id=ispr.id join common_distributors cd on cd.distributor_id=isprd.distributor_id where ispr.id="
								+ PromotionID);
				while (rs10.next()) {
					html += rs10.getString("distributor_id") + " - " + rs10.getString("name") + "<br/>";
				}
			}

			ResultSet rs13 = s.executeQuery(
					"SELECT dbp.id,dbp.label FROM inventory_sales_promotions_request_pjp isprp join inventory_sales_promotions_request ispr on isprp.product_promotion_id=ispr.id join distributor_beat_plan dbp  on dbp.id=isprp.pjp_id where ispr.id="
							+ PromotionID);
			if (rs13.first()) {
				html += "<br>";
				html += "<table style='width: 400px;'>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>PJP</td>";
				html += "</tr>";
				html += "</table>";
				ResultSet rs11 = s.executeQuery(
						"SELECT dbp.id,dbp.label FROM inventory_sales_promotions_request_pjp isprp join inventory_sales_promotions_request ispr on isprp.product_promotion_id=ispr.id join distributor_beat_plan dbp  on dbp.id=isprp.pjp_id where ispr.id="
								+ PromotionID);
				while (rs11.next()) {
					html += rs11.getString("id") + " - " + rs11.getString("label") + "<br/>";
					;
				}
			}

			html += "<br>";
			html += "<table style='width: 400px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Workflow</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs5 = s.executeQuery(
					"SELECT action_label_past, display_name, DATE_FORMAT(completed_on,'%b %d %Y') sent_date, DATE_FORMAT(completed_on,'%h:%i %p') sent_time FROM workflow_requests_steps wrs join workflow_actions wa on wrs.action_id = wa.action_id join users u on wrs.user_id = u.id where request_id="
							+ RequestIDVal + " and wrs.completed_on is not null  order by step_id desc");
			while (rs5.next()) {
				html += "<b>" + rs5.getString("action_label_past") + "</b> by " + rs5.getString("display_name") + " ["
						+ rs5.getString("sent_date") + " | " + rs5.getString("sent_time") + "]<br>";
			}

			html += "<br/><b>Valid until</b> " + ValidToDateString + "<br>";
			html += "<br>";

			html += "<a href='http://203.124.57.25/portal/WM/WMP?token=" + UVID + "&action=1&sessionid=" + Math.random()
					+ "'>Approve</a>    |    <a href='http://203.124.57.25/portal/WM/WMP?token=" + UVID
					+ "&action=2&sessionid=" + Math.random() + "'>Decline</a>";
			// html += "<a
			// href='http://localhost:8080/portal/WM/WMP?token="+UVID+"&action=1&sessionid="+Math.random()+"'>Approve</a>
			// | <a
			// href='http://localhost:8080/portal/WM/WMP?token="+UVID+"&action=2&sessionid="+Math.random()+"'>Decline</a>";

			html += "</body>";

			html += "</html>";

		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return html;

	}

	public static String getPromotionRequestHTMLWithActionButtonsCOO(long RequestIDVal) {

		Datasource ds = new Datasource();

		String html = "";
		try {

			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();

			long UVID = 0;
			long PromotionID = 0;
			String PromotionName = "";
			ResultSet rs = s.executeQuery(
					"select * from inventory_sales_promotions_request where request_id = " + RequestIDVal);
			if (rs.first()) {
				PromotionName = rs.getString("label");
				UVID = rs.getLong("uvid");
				PromotionID = rs.getLong("id");
			}

			String PackageLabel = "";
			String BrandLabel = "";
			String RawCasesLabel = "";
			String UnitLabel = "";

			String PackageLabel1 = "";
			String BrandLabel1 = "";
			String RawCasesLabel1 = "";
			String UnitLabel1 = "";

			String HTMLMessage = "";
			// HTMLMessage = "<table><tr><td style='background: #123123'>Hello
			// 1</td></tr><tr><td>Hello 2</td></tr></table>";

			html = "<html>";
			html += "<body><br>";

			html += "<table style='width: 400px;'>";

			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='4'>Promotional Product</td>";
			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Package</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Brand</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Case</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Bottle</td>";
			html += "</tr>";

			html += "<tr>";

			ResultSet rs6 = s1.executeQuery(
					"SELECT distinct isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_labele_id,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id FROM inventory_sales_promotions_request_products isprp join inventory_sales_promotions_request_products_brands isprpb on isprp.id=isprpb.id and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_sales_promotions_request ispr on ispr.id=isprp.id where ispr.request_id="
							+ RequestIDVal + " and isprp.type_id=1");
			while (rs6.next()) {
				int PackageCounter = 1;
				if (PackageCounter == 1) {
					PackageLabel = rs6.getString("package_labele_id");
				}

				html += "<td valign='top' style='background-color: #EDEFF2;'>" + PackageLabel;

				ResultSet rs2 = s2.executeQuery(
						"SELECT isprp.id,isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_label,isprpb.brand_id,(select label from inventory_brands ib where ib.id=isprpb.brand_id) brand_label,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id FROM inventory_sales_promotions_request_products isprp join inventory_sales_promotions_request_products_brands isprpb on isprp.id=isprpb.id and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_sales_promotions_request ispr on ispr.id=isprp.id where ispr.request_id="
								+ RequestIDVal + " and isprp.type_id=1 and isprp.package_id="
								+ rs6.getLong("package_id") + " group by isprp.type_id,isprpb.brand_id");
				while (rs2.next()) {
					BrandLabel += rs2.getString("brand_label") + "<br/>";
				}

				if (rs6.getLong("raw_cases") != 0) {
					RawCasesLabel = rs6.getString("raw_cases");
				}
				if (rs6.getLong("units") != 0) {
					UnitLabel = rs6.getString("units");
				}

			}

			html += "<td style='background-color: #EDEFF2;'>" + BrandLabel;
			html += "<td valign='top' style='background-color: #EDEFF2; text-align: left; '>" + RawCasesLabel;
			html += "<td valign='top' style='background-color: #EDEFF2; text-align: left;'>" + UnitLabel;
			html += "</tr>";

			html += "<tr>";
			html += "<td colspan='4'>&nbsp;";
			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='4'>Free Product</td>";
			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Package</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Brand</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Case</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Bottle</td>";
			html += "</tr>";

			html += "<tr>";

			ResultSet rs7 = s1.executeQuery(
					"SELECT distinct isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_labele_id,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id FROM inventory_sales_promotions_request_products isprp join inventory_sales_promotions_request_products_brands isprpb on isprp.id=isprpb.id and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_sales_promotions_request ispr on ispr.id=isprp.id where ispr.request_id="
							+ RequestIDVal + " and isprp.type_id=2");
			while (rs7.next()) {
				int PackageCounter = 1;
				if (PackageCounter == 1) {
					PackageLabel1 = rs7.getString("package_labele_id");
				}

				html += "<td valign='top' style='background-color: #EDEFF2;'>" + PackageLabel1;

				ResultSet rs2 = s2.executeQuery(
						"SELECT isprp.id,isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_label,isprpb.brand_id,(select label from inventory_brands ib where ib.id=isprpb.brand_id) brand_label,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id FROM inventory_sales_promotions_request_products isprp join inventory_sales_promotions_request_products_brands isprpb on isprp.id=isprpb.id and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_sales_promotions_request ispr on ispr.id=isprp.id where ispr.request_id="
								+ RequestIDVal + " and isprp.type_id=2 and isprp.package_id="
								+ rs7.getLong("package_id") + " group by isprp.type_id,isprpb.brand_id");
				while (rs2.next()) {
					BrandLabel1 += rs2.getString("brand_label") + "<br/>";
				}

				if (rs7.getLong("raw_cases") != 0) {
					RawCasesLabel1 = rs7.getString("raw_cases");
				}
				if (rs7.getLong("units") != 0) {
					UnitLabel1 = rs7.getString("units");
				}

			}

			html += "<td style='background-color: #EDEFF2;'>" + BrandLabel1;
			html += "<td valign='top' style='background-color: #EDEFF2;text-align: left;'>" + RawCasesLabel1;
			html += "<td valign='top' style='background-color: #EDEFF2;text-align: left;'>" + UnitLabel1;
			html += "</tr>";

			html += "<tr>";
			html += "<td colspan='4'>&nbsp;";
			html += "</tr>";

			html += "</table>";

			html += "<table style='width: 400px;'>";

			String ValidToDateString = "";
			String ValidToTimeString = "";

			ResultSet rs3 = s.executeQuery(
					"select *,DATE_FORMAT(valid_to,'%b %d %Y') sent_date_valid,DATE_FORMAT(valid_to,'%h:%i %p') sent_time_valid from inventory_sales_promotions_request where request_id="
							+ RequestIDVal);
			while (rs3.next()) {
				ValidToDateString = rs3.getString("sent_date_valid");
				ValidToTimeString = rs3.getString("sent_time_valid");

				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='2'>Profitability</td>";
				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;'>Estimated Sales Volume (Cases)</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: right;'>"
						+ Utilities.getDisplayCurrencyFormatRounded(rs3.getDouble("estimated_sales_volume")) + "</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;'>Sales SKU Price</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: right;'>"
						+ Utilities.getDisplayCurrencyFormatRounded(rs3.getDouble("sales_sku_price")) + "</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;'>Free SKU Price (Bottle)</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: right;'>"
						+ Utilities.getDisplayCurrencyFormatRounded(rs3.getDouble("free_sku_price")) + "</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;'>Variable Cost + Taxes</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: right;'>"
						+ Utilities.getDisplayCurrencyFormatRounded(rs3.getDouble("variable_cost_and_taxes")) + "</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Marginal Contribution</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: right; font-weight: bold;'>"
						+ Utilities.getDisplayCurrencyFormatRounded(rs3.getDouble("marginal_contribution")) + "</td>";

				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Net Price</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: right; font-weight: bold;'>"
						+ Utilities.getDisplayCurrencyFormatRounded(rs3.getDouble("net_price")) + "</td>";
				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Promotion Cost</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: right; font-weight: bold;'>"
						+ Utilities.getDisplayCurrencyFormatRounded(
								rs3.getDouble("free_sku_price") * rs3.getDouble("estimated_sales_volume"))
						+ "</td>";
				html += "</tr>";

				html += "<tr><td>&nbsp;</td></tr>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='2'>Reason</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;' colspan='2'>"
						+ rs3.getString("comments") + "</td>";

				html += "</tr>";

			}

			html += "</table>";
			html += "<br>";
			html += "<table style='width: 400px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Messages</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs4 = s.executeQuery(
					"SELECT message,sent_by,(select DISPLAY_NAME from users u where u.id=wrcm.sent_by) sent_by_name,sent_on,DATE_FORMAT(sent_on,'%b %d %Y') sent_date,DATE_FORMAT(sent_on,'%h:%i %p') sent_time FROM workflow_requests_chat wrc join workflow_requests_chat_messages wrcm on wrc.conversation_id=wrcm.conversation_id and  wrc.request_id="
							+ RequestIDVal);
			while (rs4.next()) {

				html += "<b>" + rs4.getString("sent_by_name") + ":</b> " + rs4.getString("message") + " ["
						+ rs4.getString("sent_date") + " | " + rs4.getString("sent_time") + "]<br>";

			}

			ResultSet rs15 = s.executeQuery(
					"SELECT cr.region_short_name FROM inventory_sales_promotions_request_regions isprr join inventory_sales_promotions_request ispr on isprr.product_promotion_id=ispr.id join common_regions cr on cr.region_id=isprr.region_id where ispr.id="
							+ PromotionID);
			if (rs15.first()) {
				html += "<br>";
				html += "<table style='width: 400px;'>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Region</td>";
				html += "</tr>";
				html += "</table>";
				ResultSet rs9 = s.executeQuery(
						"SELECT cr.region_short_name,cr.region_name FROM inventory_sales_promotions_request_regions isprr join inventory_sales_promotions_request ispr on isprr.product_promotion_id=ispr.id join common_regions cr on cr.region_id=isprr.region_id where ispr.id="
								+ PromotionID);
				while (rs9.next()) {
					html += rs9.getString("region_short_name") + " - " + rs9.getString("region_name") + "<br/>";
				}
			}

			ResultSet rs14 = s.executeQuery(
					"SELECT cd.distributor_id,cd.name FROM inventory_sales_promotions_request_distributors isprd join inventory_sales_promotions_request ispr on isprd.product_promotion_id=ispr.id join common_distributors cd on cd.distributor_id=isprd.distributor_id where ispr.id="
							+ PromotionID);
			if (rs14.first()) {
				html += "<br>";
				html += "<table style='width: 400px;'>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Distributor</td>";
				html += "</tr>";
				html += "</table>";
				ResultSet rs10 = s.executeQuery(
						"SELECT cd.distributor_id,cd.name FROM inventory_sales_promotions_request_distributors isprd join inventory_sales_promotions_request ispr on isprd.product_promotion_id=ispr.id join common_distributors cd on cd.distributor_id=isprd.distributor_id where ispr.id="
								+ PromotionID);
				while (rs10.next()) {
					html += rs10.getString("distributor_id") + " - " + rs10.getString("name") + "<br/>";
				}
			}

			ResultSet rs13 = s.executeQuery(
					"SELECT dbp.id,dbp.label FROM inventory_sales_promotions_request_pjp isprp join inventory_sales_promotions_request ispr on isprp.product_promotion_id=ispr.id join distributor_beat_plan dbp  on dbp.id=isprp.pjp_id where ispr.id="
							+ PromotionID);
			if (rs13.first()) {
				html += "<br>";
				html += "<table style='width: 400px;'>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>PJP</td>";
				html += "</tr>";
				html += "</table>";
				ResultSet rs11 = s.executeQuery(
						"SELECT dbp.id,dbp.label FROM inventory_sales_promotions_request_pjp isprp join inventory_sales_promotions_request ispr on isprp.product_promotion_id=ispr.id join distributor_beat_plan dbp  on dbp.id=isprp.pjp_id where ispr.id="
								+ PromotionID);
				while (rs11.next()) {
					html += rs11.getString("id") + " - " + rs11.getString("label") + "<br/>";
					;
				}
			}

			html += "<br>";
			html += "<table style='width: 400px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Workflow</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs5 = s.executeQuery(
					"SELECT action_label_past, display_name, DATE_FORMAT(completed_on,'%b %d %Y') sent_date, DATE_FORMAT(completed_on,'%h:%i %p') sent_time FROM workflow_requests_steps wrs join workflow_actions wa on wrs.action_id = wa.action_id join users u on wrs.user_id = u.id where request_id="
							+ RequestIDVal + " and wrs.completed_on is not null  order by step_id desc");
			while (rs5.next()) {
				html += "<b>" + rs5.getString("action_label_past") + "</b> by " + rs5.getString("display_name") + " ["
						+ rs5.getString("sent_date") + " | " + rs5.getString("sent_time") + "]<br>";
			}

			html += "<br/><b>Valid until</b> " + ValidToDateString + "<br>";
			html += "<br>";

			html += "<a href='http://203.124.57.25/portal/WM/WMP?token=" + UVID + "&action=1&secondary=1&sessionid="
					+ Math.random() + "'>Approve</a>    |    <a href='http://203.124.57.25/portal/WM/WMP?token=" + UVID
					+ "&action=2&secondary=1&sessionid=" + Math.random() + "'>Decline</a>";
			// html += "<a
			// href='http://localhost:8080/portal/WM/WMP?token="+UVID+"&action=1&sessionid="+Math.random()+"'>Approve</a>
			// | <a
			// href='http://localhost:8080/portal/WM/WMP?token="+UVID+"&action=2&sessionid="+Math.random()+"'>Decline</a>";

			html += "</body>";

			html += "</html>";

		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return html;

	}

	public static String getPromotionRequestHTMLWithoutActionButtons(long RequestIDVal) {

		Datasource ds = new Datasource();

		String html = "";
		try {

			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();

			long UVID = 0;
			long PromotionID = 0;
			String PromotionName = "";
			ResultSet rs = s.executeQuery(
					"select * from inventory_sales_promotions_request where request_id = " + RequestIDVal);
			if (rs.first()) {
				PromotionName = rs.getString("label");
				UVID = rs.getLong("uvid");
				PromotionID = rs.getLong("id");
			}

			String PackageLabel = "";
			String BrandLabel = "";
			String RawCasesLabel = "";
			String UnitLabel = "";

			String PackageLabel1 = "";
			String BrandLabel1 = "";
			String RawCasesLabel1 = "";
			String UnitLabel1 = "";

			String HTMLMessage = "";
			// HTMLMessage = "<table><tr><td style='background: #123123'>Hello
			// 1</td></tr><tr><td>Hello 2</td></tr></table>";

			html = "<html>";
			html += "<body><br>";

			html += "<table style='width: 400px;'>";

			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='4'>Promotional Product</td>";
			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Package</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Brand</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Case</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Bottle</td>";
			html += "</tr>";

			html += "<tr>";

			ResultSet rs6 = s1.executeQuery(
					"SELECT distinct isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_labele_id,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id FROM inventory_sales_promotions_request_products isprp join inventory_sales_promotions_request_products_brands isprpb on isprp.id=isprpb.id and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_sales_promotions_request ispr on ispr.id=isprp.id where ispr.request_id="
							+ RequestIDVal + " and isprp.type_id=1");
			while (rs6.next()) {
				int PackageCounter = 1;
				if (PackageCounter == 1) {
					PackageLabel = rs6.getString("package_labele_id");
				}

				html += "<td valign='top' style='background-color: #EDEFF2;'>" + PackageLabel;

				ResultSet rs2 = s2.executeQuery(
						"SELECT isprp.id,isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_label,isprpb.brand_id,(select label from inventory_brands ib where ib.id=isprpb.brand_id) brand_label,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id FROM inventory_sales_promotions_request_products isprp join inventory_sales_promotions_request_products_brands isprpb on isprp.id=isprpb.id and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_sales_promotions_request ispr on ispr.id=isprp.id where ispr.request_id="
								+ RequestIDVal + " and isprp.type_id=1 and isprp.package_id="
								+ rs6.getLong("package_id") + " group by isprp.type_id,isprpb.brand_id");
				while (rs2.next()) {
					BrandLabel += rs2.getString("brand_label") + "<br/>";
				}

				if (rs6.getLong("raw_cases") != 0) {
					RawCasesLabel = rs6.getString("raw_cases");
				}
				if (rs6.getLong("units") != 0) {
					UnitLabel = rs6.getString("units");
				}

			}

			html += "<td style='background-color: #EDEFF2;'>" + BrandLabel;
			html += "<td valign='top' style='background-color: #EDEFF2; text-align: left; '>" + RawCasesLabel;
			html += "<td valign='top' style='background-color: #EDEFF2; text-align: left;'>" + UnitLabel;
			html += "</tr>";

			html += "<tr>";
			html += "<td colspan='4'>&nbsp;";
			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='4'>Free Product</td>";
			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Package</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Brand</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Case</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Bottle</td>";
			html += "</tr>";

			html += "<tr>";

			ResultSet rs7 = s1.executeQuery(
					"SELECT distinct isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_labele_id,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id FROM inventory_sales_promotions_request_products isprp join inventory_sales_promotions_request_products_brands isprpb on isprp.id=isprpb.id and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_sales_promotions_request ispr on ispr.id=isprp.id where ispr.request_id="
							+ RequestIDVal + " and isprp.type_id=2");
			while (rs7.next()) {
				int PackageCounter = 1;
				if (PackageCounter == 1) {
					PackageLabel1 = rs7.getString("package_labele_id");
				}

				html += "<td valign='top' style='background-color: #EDEFF2;'>" + PackageLabel1;

				ResultSet rs2 = s2.executeQuery(
						"SELECT isprp.id,isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_label,isprpb.brand_id,(select label from inventory_brands ib where ib.id=isprpb.brand_id) brand_label,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id FROM inventory_sales_promotions_request_products isprp join inventory_sales_promotions_request_products_brands isprpb on isprp.id=isprpb.id and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_sales_promotions_request ispr on ispr.id=isprp.id where ispr.request_id="
								+ RequestIDVal + " and isprp.type_id=2 and isprp.package_id="
								+ rs7.getLong("package_id") + " group by isprp.type_id,isprpb.brand_id");
				while (rs2.next()) {
					BrandLabel1 += rs2.getString("brand_label") + "<br/>";
				}

				if (rs7.getLong("raw_cases") != 0) {
					RawCasesLabel1 = rs7.getString("raw_cases");
				}
				if (rs7.getLong("units") != 0) {
					UnitLabel1 = rs7.getString("units");
				}

			}

			html += "<td style='background-color: #EDEFF2;'>" + BrandLabel1;
			html += "<td valign='top' style='background-color: #EDEFF2;text-align: left;'>" + RawCasesLabel1;
			html += "<td valign='top' style='background-color: #EDEFF2;text-align: left;'>" + UnitLabel1;
			html += "</tr>";

			html += "<tr>";
			html += "<td colspan='4'>&nbsp;";
			html += "</tr>";

			html += "</table>";

			html += "<table style='width: 400px;'>";

			String ValidToDateString = "";
			String ValidToTimeString = "";

			ResultSet rs3 = s.executeQuery(
					"select *,DATE_FORMAT(valid_to,'%b %d %Y') sent_date_valid,DATE_FORMAT(valid_to,'%h:%i %p') sent_time_valid from inventory_sales_promotions_request where request_id="
							+ RequestIDVal);
			while (rs3.next()) {
				ValidToDateString = rs3.getString("sent_date_valid");
				ValidToTimeString = rs3.getString("sent_time_valid");

				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='2'>Profitability</td>";
				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;'>Estimated Sales Volume (Cases)</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: right;'>"
						+ Utilities.getDisplayCurrencyFormatRounded(rs3.getDouble("estimated_sales_volume")) + "</td>";
				html += "</tr>";
				/*
				 * 
				 * html += "<tr>"; html +=
				 * "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;'>Sales SKU Price</td>"
				 * ; html +=
				 * "<td style='background-color: #EDEFF2; height: 18px; text-align: right;'>"
				 * +Utilities.getDisplayCurrencyFormatRounded(rs3.getDouble(
				 * "sales_sku_price"))+"</td>"; html += "</tr>"; html += "<tr>";
				 * html +=
				 * "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;'>Free SKU Price (Bottle)</td>"
				 * ; html +=
				 * "<td style='background-color: #EDEFF2; height: 18px; text-align: right;'>"
				 * +Utilities.getDisplayCurrencyFormatRounded(rs3.getDouble(
				 * "free_sku_price"))+"</td>"; html += "</tr>"; html += "<tr>";
				 * html +=
				 * "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;'>Variable Cost + Taxes</td>"
				 * ; html +=
				 * "<td style='background-color: #EDEFF2; height: 18px; text-align: right;'>"
				 * +Utilities.getDisplayCurrencyFormatRounded(rs3.getDouble(
				 * "variable_cost_and_taxes"))+"</td>"; html += "</tr>"; html +=
				 * "<tr>"; html +=
				 * "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Marginal Contribution</td>"
				 * ; html +=
				 * "<td style='background-color: #EDEFF2; height: 18px; text-align: right; font-weight: bold;'>"
				 * +Utilities.getDisplayCurrencyFormatRounded(rs3.getDouble(
				 * "marginal_contribution")) +"</td>";
				 * 
				 * html += "</tr>"; html += "<tr>"; html +=
				 * "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Net Price</td>"
				 * ; html +=
				 * "<td style='background-color: #EDEFF2; height: 18px; text-align: right; font-weight: bold;'>"
				 * +Utilities.getDisplayCurrencyFormatRounded(rs3.getDouble(
				 * "net_price"))+"</td>"; html += "</tr>";
				 * 
				 * html += "<tr><td>&nbsp;</td></tr>"; html += "<tr>"; html +=
				 * "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='2'>Reason</td>"
				 * ; html += "</tr>";
				 * 
				 * html += "<tr>"; html +=
				 * "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;' colspan='2'>"
				 * +rs3.getString("comments")+"</td>"; html += "</tr>";
				 * 
				 */

			}

			html += "</table>";
			html += "<br>";

			/*
			 * html += "<table style='width: 400px;'>"; html += "<tr>"; html +=
			 * "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Messages</td>"
			 * ; html += "</tr>"; html += "</table>"; ResultSet rs4 =
			 * s.executeQuery(
			 * "SELECT message,sent_by,(select DISPLAY_NAME from users u where u.id=wrcm.sent_by) sent_by_name,sent_on,DATE_FORMAT(sent_on,'%b %d %Y') sent_date,DATE_FORMAT(sent_on,'%h:%i %p') sent_time FROM workflow_requests_chat wrc join workflow_requests_chat_messages wrcm on wrc.conversation_id=wrcm.conversation_id and  wrc.request_id="
			 * +RequestIDVal); while(rs4.next()){
			 * 
			 * html += "<b>"+rs4.getString("sent_by_name")+":</b> "
			 * +rs4.getString("message")+" ["+rs4.getString("sent_date")+" | "
			 * +rs4.getString("sent_time")+"]<br>";
			 * 
			 * 
			 * 
			 * 
			 * }
			 */

			ResultSet rs15 = s.executeQuery(
					"SELECT cr.region_short_name FROM inventory_sales_promotions_request_regions isprr join inventory_sales_promotions_request ispr on isprr.product_promotion_id=ispr.id join common_regions cr on cr.region_id=isprr.region_id where ispr.id="
							+ PromotionID);
			if (rs15.first()) {
				html += "<br>";
				html += "<table style='width: 400px;'>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Region</td>";
				html += "</tr>";
				html += "</table>";
				ResultSet rs9 = s.executeQuery(
						"SELECT cr.region_short_name,cr.region_name FROM inventory_sales_promotions_request_regions isprr join inventory_sales_promotions_request ispr on isprr.product_promotion_id=ispr.id join common_regions cr on cr.region_id=isprr.region_id where ispr.id="
								+ PromotionID);
				while (rs9.next()) {
					html += rs9.getString("region_short_name") + " - " + rs9.getString("region_name") + "<br/>";
				}
			}

			ResultSet rs14 = s.executeQuery(
					"SELECT cd.distributor_id,cd.name FROM inventory_sales_promotions_request_distributors isprd join inventory_sales_promotions_request ispr on isprd.product_promotion_id=ispr.id join common_distributors cd on cd.distributor_id=isprd.distributor_id where ispr.id="
							+ PromotionID);
			if (rs14.first()) {
				html += "<br>";
				html += "<table style='width: 400px;'>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Distributor</td>";
				html += "</tr>";
				html += "</table>";
				ResultSet rs10 = s.executeQuery(
						"SELECT cd.distributor_id,cd.name FROM inventory_sales_promotions_request_distributors isprd join inventory_sales_promotions_request ispr on isprd.product_promotion_id=ispr.id join common_distributors cd on cd.distributor_id=isprd.distributor_id where ispr.id="
								+ PromotionID);
				while (rs10.next()) {
					html += rs10.getString("distributor_id") + " - " + rs10.getString("name") + "<br/>";
					;
				}
			}

			ResultSet rs13 = s.executeQuery(
					"SELECT dbp.id,dbp.label FROM inventory_sales_promotions_request_pjp isprp join inventory_sales_promotions_request ispr on isprp.product_promotion_id=ispr.id join distributor_beat_plan dbp  on dbp.id=isprp.pjp_id where ispr.id="
							+ PromotionID);
			if (rs13.first()) {
				html += "<br>";
				html += "<table style='width: 400px;'>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>PJP</td>";
				html += "</tr>";
				html += "</table>";
				ResultSet rs11 = s.executeQuery(
						"SELECT dbp.id,dbp.label FROM inventory_sales_promotions_request_pjp isprp join inventory_sales_promotions_request ispr on isprp.product_promotion_id=ispr.id join distributor_beat_plan dbp  on dbp.id=isprp.pjp_id where ispr.id="
								+ PromotionID);
				while (rs11.next()) {
					html += rs11.getString("id") + " - " + rs11.getString("label") + "<br/>";
					;
				}
			}

			html += "<br>";
			html += "<table style='width: 400px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Workflow</td>";
			html += "</tr>";
			html += "</table>";

			ResultSet rs5 = s.executeQuery(
					"SELECT action_label_past, display_name, DATE_FORMAT(completed_on,'%b %d %Y') sent_date, DATE_FORMAT(completed_on,'%h:%i %p') sent_time FROM workflow_requests_steps wrs join workflow_actions wa on wrs.action_id = wa.action_id join users u on wrs.user_id = u.id where request_id="
							+ RequestIDVal + " and wrs.completed_on is not null order by step_id desc");
			while (rs5.next()) {
				html += "<b>" + rs5.getString("action_label_past") + "</b> by " + rs5.getString("display_name") + " ["
						+ rs5.getString("sent_date") + " | " + rs5.getString("sent_time") + "]<br>";
			}

			html += "<br/><b>Valid until</b> " + ValidToDateString + "<br>";
			html += "<br>";

			html += "</body>";

			html += "</html>";

		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return html;

	}

	public static String getCreditLimitRequestHTMLWithActionButtons(long RequestIDVal) {

		Datasource ds = new Datasource();

		String html = "";
		try {

			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();

			long UVID = 0;
			long PromotionID = 0;
			String PromotionName = "";
			ResultSet rs = s
					.executeQuery("select * from gl_customer_credit_limit_request where request_id = " + RequestIDVal);
			if (rs.first()) {
				// PromotionName = rs.getString("label");
				UVID = rs.getLong("uvid");
				PromotionID = rs.getLong("id");
			}

			String HTMLMessage = "";
			// HTMLMessage = "<table><tr><td style='background: #123123'>Hello
			// 1</td></tr><tr><td>Hello 2</td></tr></table>";

			html = "<html>";
			html += "<body><br>";

			html += "<table style='width: 400px;'>";

			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;'>Credit Summary</td>";
			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Customer</td>";
			// html += "<td style='background-color: #EDEFF2; height: 18px;
			// text-align: left; font-weight: bold;'>Credit Limit</td>";

			html += "</tr>";

			html += "<tr>";

			String CustomerName = "";
			double CreditLimit = 0;
			Date ExpiryDate = null;
			long CustomerID = 0;

			ResultSet rs6 = s1.executeQuery(
					"SELECT *,(select name from common_distributors cd where cd.distributor_id=customer_id) customer_name FROM gl_customer_credit_limit_request where request_id="
							+ RequestIDVal);
			while (rs6.next()) {

				CustomerName = rs6.getLong("customer_id") + " - " + rs6.getString("customer_name");
				CreditLimit = rs6.getDouble("credit_limit");
				CustomerID = rs6.getLong("customer_id");
			}

			html += "<td style='background-color: #EDEFF2;'>" + CustomerName;
			// html += "<td valign='top' style='background-color: #EDEFF2;
			// text-align: left;
			// '>"+Utilities.getDisplayCurrencyFormat(CreditLimit);

			html += "</tr>";
			html += "<tr>";
			// html += "<td style='background-color: #EDEFF2; height: 18px;
			// text-align: left; font-weight: bold;'>Customer</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Credit Limit</td>";
			html += "</tr>";

			html += "<tr>";
			html += "<td valign='top' style='background-color: #EDEFF2; text-align: left; '>"
					+ Utilities.getDisplayCurrencyFormat(CreditLimit);
			html += "</tr>";
			html += "<tr>";
			// html += "<td style='background-color: #EDEFF2; height: 18px;
			// text-align: left; font-weight: bold;'>Customer</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Current Balance</td>";
			html += "</tr>";

			html += "<tr>";
			html += "<td valign='top' style='background-color: #EDEFF2; text-align: left; '>"
					+ Utilities.getDisplayCurrencyFormat(com.pbc.common.Distributor.getLedgerBalance(CustomerID));
			html += "</tr>";

			html += "<tr>";
			html += "<td colspan='4'>&nbsp;";
			html += "</tr>";

			html += "</table>";

			html += "<table style='width: 400px;'>";

			String ValidToDateString = "";
			String ValidToTimeString = "";

			ResultSet rs3 = s.executeQuery(
					"select *,DATE_FORMAT(valid_to,'%b %d %Y') sent_date_valid from gl_customer_credit_limit_request where request_id="
							+ RequestIDVal);
			while (rs3.next()) {
				ValidToDateString = rs3.getString("sent_date_valid");

				html += "<tr><td>&nbsp;</td></tr>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='2'>Reason</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;' colspan='2'>"
						+ rs3.getString("comments") + "</td>";

				html += "</tr>";

			}

			html += "</table>";
			html += "<br>";
			html += "<table style='width: 400px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Messages</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs4 = s.executeQuery(
					"SELECT message,sent_by,(select DISPLAY_NAME from users u where u.id=wrcm.sent_by) sent_by_name,sent_on,DATE_FORMAT(sent_on,'%b %d %Y') sent_date,DATE_FORMAT(sent_on,'%h:%i %p') sent_time FROM workflow_requests_chat wrc join workflow_requests_chat_messages wrcm on wrc.conversation_id=wrcm.conversation_id and  wrc.request_id="
							+ RequestIDVal);
			while (rs4.next()) {

				html += "<b>" + rs4.getString("sent_by_name") + ":</b> " + rs4.getString("message") + " ["
						+ rs4.getString("sent_date") + " | " + rs4.getString("sent_time") + "]<br>";

			}

			html += "<br>";
			html += "<table style='width: 400px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Workflow</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs5 = s.executeQuery(
					"SELECT action_label_past, display_name, DATE_FORMAT(completed_on,'%b %d %Y') sent_date, DATE_FORMAT(completed_on,'%h:%i %p') sent_time FROM workflow_requests_steps wrs join workflow_actions wa on wrs.action_id = wa.action_id join users u on wrs.user_id = u.id where request_id="
							+ RequestIDVal + " and wrs.completed_on is not null  order by step_id desc");
			while (rs5.next()) {
				html += "<b>" + rs5.getString("action_label_past") + "</b> by " + rs5.getString("display_name") + " ["
						+ rs5.getString("sent_date") + " | " + rs5.getString("sent_time") + "]<br>";
			}

			html += "<br/><b>Valid until</b> " + ValidToDateString + "<br>";
			html += "<br>";

			html += "<a href='http://portal.pbc.com.pk/portal/WM/WMCL?token=" + UVID + "&action=1&sessionid="
					+ Math.random() + "'>Approve</a>    |    <a href='http://portal.pbc.com.pk/portal/WM/WMCL?token="
					+ UVID + "&action=2&sessionid=" + Math.random() + "'>Decline</a>";
			// html += "<a
			// href='http://localhost:8080/portal/WM/WMP?token="+UVID+"&action=1&sessionid="+Math.random()+"'>Approve</a>
			// | <a
			// href='http://localhost:8080/portal/WM/WMP?token="+UVID+"&action=2&sessionid="+Math.random()+"'>Decline</a>";

			html += "</body>";

			html += "</html>";

		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return html;

	}

	public static String getCreditLimitRequestHTMLWithoutActionButtons(long RequestIDVal) {

		Datasource ds = new Datasource();

		String html = "";
		try {

			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();

			long UVID = 0;
			long PromotionID = 0;
			String PromotionName = "";
			ResultSet rs = s.executeQuery(
					"select * from inventory_sales_promotions_request where request_id = " + RequestIDVal);
			if (rs.first()) {
				PromotionName = rs.getString("label");
				UVID = rs.getLong("uvid");
				PromotionID = rs.getLong("id");
			}

			String HTMLMessage = "";
			// HTMLMessage = "<table><tr><td style='background: #123123'>Hello
			// 1</td></tr><tr><td>Hello 2</td></tr></table>";

			html = "<html>";
			html += "<body><br>";

			html += "<table style='width: 400px;'>";

			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='4'>Credit Summary</td>";
			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Customer</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Credit Limit</td>";

			html += "</tr>";

			html += "<tr>";

			String CustomerName = "";
			double CreditLimit = 0;
			Date ExpiryDate = null;

			ResultSet rs6 = s1.executeQuery(
					"SELECT *,(select name from common_distributors cd where cd.distributor_id=customer_id) customer_name FROM gl_customer_credit_limit_request where request_id="
							+ RequestIDVal);
			while (rs6.next()) {

				CustomerName = rs6.getLong("customer_id") + " - " + rs6.getString("customer_name");
				CreditLimit = rs6.getDouble("credit_limit");

			}

			html += "<td style='background-color: #EDEFF2;'>" + CustomerName;
			html += "<td valign='top' style='background-color: #EDEFF2; text-align: left; '>"
					+ Utilities.getDisplayCurrencyFormat(CreditLimit);

			html += "</tr>";

			html += "<tr>";
			html += "<td colspan='4'>&nbsp;";
			html += "</tr>";

			html += "</table>";

			html += "<table style='width: 400px;'>";

			String ValidToDateString = "";
			String ValidToTimeString = "";

			ResultSet rs3 = s.executeQuery(
					"select *,DATE_FORMAT(valid_to,'%b %d %Y') sent_date_valid from gl_customer_credit_limit_request where request_id="
							+ RequestIDVal);
			while (rs3.next()) {
				ValidToDateString = rs3.getString("sent_date_valid");

				html += "<tr><td>&nbsp;</td></tr>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='2'>Reason</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;' colspan='2'>"
						+ rs3.getString("comments") + "</td>";

				html += "</tr>";

			}

			html += "</table>";
			html += "<br>";
			html += "<table style='width: 400px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Messages</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs4 = s.executeQuery(
					"SELECT message,sent_by,(select DISPLAY_NAME from users u where u.id=wrcm.sent_by) sent_by_name,sent_on,DATE_FORMAT(sent_on,'%b %d %Y') sent_date,DATE_FORMAT(sent_on,'%h:%i %p') sent_time FROM workflow_requests_chat wrc join workflow_requests_chat_messages wrcm on wrc.conversation_id=wrcm.conversation_id and  wrc.request_id="
							+ RequestIDVal);
			while (rs4.next()) {

				html += "<b>" + rs4.getString("sent_by_name") + ":</b> " + rs4.getString("message") + " ["
						+ rs4.getString("sent_date") + " | " + rs4.getString("sent_time") + "]<br>";

			}

			html += "<br>";
			html += "<table style='width: 400px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Workflow</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs5 = s.executeQuery(
					"SELECT action_label_past, display_name, DATE_FORMAT(completed_on,'%b %d %Y') sent_date, DATE_FORMAT(completed_on,'%h:%i %p') sent_time FROM workflow_requests_steps wrs join workflow_actions wa on wrs.action_id = wa.action_id join users u on wrs.user_id = u.id where request_id="
							+ RequestIDVal + " and wrs.completed_on is not null  order by step_id desc");
			while (rs5.next()) {
				html += "<b>" + rs5.getString("action_label_past") + "</b> by " + rs5.getString("display_name") + " ["
						+ rs5.getString("sent_date") + " | " + rs5.getString("sent_time") + "]<br>";
			}

			html += "<br/><b>Valid until</b> " + ValidToDateString + "<br>";
			html += "<br>";

			html += "</body>";

			html += "</html>";

		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return html;

	}

	public static String getCoolerInjectionRequestHTMLWithActionButtonsCOO(long RequestIDVal) {

		Datasource ds = new Datasource();

		String html = "";
		try {

			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();

			long UVID = 0;
			long MasterParentTableID = 0;
			String PromotionName = "";
			ResultSet rs = s.executeQuery("select * from tot_issue_request where request_id = " + RequestIDVal);
			if (rs.first()) {

				UVID = rs.getLong("uvid");
				MasterParentTableID = rs.getLong("id");
			}

			long OutletID = 0;
			String OutletName = "";
			String OutletAddress = "";
			String OutletContactName = "";
			long RegionID = 0;
			String RegionName = "";
			long SNDID = 0;
			String SNDName = "";
			long RSMID = 0;
			String RSMName = "";
			long DistributorID = 0;
			String DistributorName = "";
			String EmptyStatus = "";
			String TOTCode = "";
			String ExistingTOT = "";

			String PackageLabel1 = "";
			String BrandLabel1 = "";
			String RawCasesLabel1 = "";
			String UnitLabel1 = "";

			String HTMLMessage = "";
			// HTMLMessage = "<table><tr><td style='background: #123123'>Hello
			// 1</td></tr><tr><td>Hello 2</td></tr></table>";

			html = "<html>";
			html += "<body><br>";

			html += "<table style='width: 400px;'>";

			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='4'>Outlet Info</td>";
			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Outlet ID</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Outlet Name</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Address</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Contact Name</td>";
			html += "</tr>";

			html += "<tr>";

			ResultSet rs6 = s1.executeQuery(
					"SELECT *,(select label from common_outlets_channels coc where coc.id=outlet_channel) channel_name,(select concat(region_short_name,' - ',region_name) from common_regions cr where cr.region_id=outlet_region) region_name,IFNULL((select display_name from users u where u.id=outlet_rsm),'') outlet_rsm_name,ifnull((select display_name from users u where u.id=outlet_snd),'') outlet_snd_name,ifnull((select name from common_distributors cd where cd.distributor_id=outlet_distributor_id),'') outlet_distributor_name, (select label from common_assets_tot_sizes cats where cats.id=existing_tot) existing_tot_name FROM tot_issue_request where request_id="
							+ RequestIDVal);
			while (rs6.next()) {
				OutletID = rs6.getLong("outlet_id");
				OutletName = rs6.getString("outlet_name");
				OutletAddress = rs6.getString("outlet_address");
				OutletContactName = rs6.getString("outlet_contact_name");
				RegionName = rs6.getString("region_name");
				RSMName = rs6.getString("outlet_rsm_name");
				SNDName = rs6.getString("outlet_snd_name");
				DistributorName = rs6.getString("outlet_distributor_name");

				EmptyStatus = rs6.getString("tot_empty_status");
				TOTCode = rs6.getString("tot_code");
				ExistingTOT = rs6.getString("existing_tot_name");

			}

			html += "<td style='background-color: #EDEFF2;'>" + OutletID;
			html += "<td valign='top' style='b1ackground-color: #EDEFF2; text-align: left; '>" + OutletName;
			html += "<td valign='top' style='b1ackground-color: #EDEFF2; text-align: left;'>" + OutletAddress;
			html += "<td valign='top' style='b1ackground-color: #EDEFF2; text-align: left;'>" + OutletContactName;
			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Region</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>SND</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>RSM</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Distributor</td>";
			html += "</tr>";

			html += "<tr>";

			html += "<td style='background-color: #EDEFF2;'>" + RegionName;
			html += "<td valign='top' style='ba1ckground-color: #EDEFF2; text-align: left; '>" + RSMName;
			html += "<td valign='top' style='b1ackground-color: #EDEFF2; text-align: left;'>" + SNDName;
			html += "<td valign='top' style='b1ackground-color: #EDEFF2; text-align: left;'>" + DistributorName;
			html += "</tr>";

			html += "<tr>";
			html += "<td colspan='4'>&nbsp;";
			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='4'>Sales Volume-P/C: Per Year</td>";
			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>LRB Type</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;' colspan='2'>Package</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Quantity</td>";

			html += "</tr>";

			html += "<tr>";

			ResultSet rs121 = s.executeQuery(
					"SELECT id,type,(select label from inventory_products_lrb_types iplt where iplt.id=type) type_name,package_id,(select label from inventory_packages ip where ip.id=package_id) package_name,quantity FROM tot_issue_request_sales where id="
							+ MasterParentTableID);
			while (rs121.next()) {

				html += "<tr style='b1ackground-color:#efedee'>";
				html += "<td style='width:25%; padding:5px;' valign='top'>" + rs121.getString("type_name") + "</td>";
				html += "<td style='width:25%; padding:5px;' colspan='2'>" + rs121.getString("package_name") + "</td>";
				html += "<td style='width:25%; padding:5px;' valign='top'>" + rs121.getInt("quantity") + "</td>";

				html += "</tr>";

			}

			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='4'>TOT Info</td>";
			html += "</tr>";

			html += "<tr style='b1ackground-color:#efedee'>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;' valign='top'>Empty Status</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;' colspan='2'>Existing TOT Size</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;' valign='top'>TOT Code</td>";

			html += "</tr>";

			html += "<tr style='b1ackground-color:#efedee'>";
			html += "<td style='width:25%; padding:5px;' valign='top'>" + EmptyStatus + "</td>";
			html += "<td style='width:25%; padding:5px;'>" + ExistingTOT + "</td>";
			html += "<td style='width:25%; padding:5px;' valign='top'>" + TOTCode + "</td>";

			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='4'>Required TOT Size</td>";
			html += "</tr>";

			ResultSet rs20 = s.executeQuery(
					"SELECT id,tot_size_id,(select label from common_assets_tot_sizes cats where cats.id=tot_size_id) tot_size_name,quantity FROM tot_issue_request_sizes where id="
							+ MasterParentTableID);
			while (rs20.next()) {

				html += "<tr>";
				html += "<td style='width:25%; padding:5px;' valign='top' colspan='2'>"
						+ rs20.getString("tot_size_name") + "</td>";
				html += "<td style='width:25%; padding:5px;' colspan='2'>" + rs20.getString("quantity") + "</td>";

				html += "</tr>";

			}

			html += "<tr>";
			html += "<td colspan='4'>&nbsp;";
			html += "</tr>";

			html += "</table>";

			html += "<table style='width: 400px;'>";

			String ValidToDateString = "";
			String ValidToTimeString = "";

			html += "</table>";
			html += "<br>";
			html += "<table style='width: 400px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Messages</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs4 = s.executeQuery(
					"SELECT message,sent_by,(select DISPLAY_NAME from users u where u.id=wrcm.sent_by) sent_by_name,sent_on,DATE_FORMAT(sent_on,'%b %d %Y') sent_date,DATE_FORMAT(sent_on,'%h:%i %p') sent_time FROM workflow_requests_chat wrc join workflow_requests_chat_messages wrcm on wrc.conversation_id=wrcm.conversation_id and  wrc.request_id="
							+ RequestIDVal);
			while (rs4.next()) {

				html += "<b>" + rs4.getString("sent_by_name") + ":</b> " + rs4.getString("message") + " ["
						+ rs4.getString("sent_date") + " | " + rs4.getString("sent_time") + "]<br>";

			}

			html += "<br>";
			html += "<table style='width: 400px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Workflow</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs5 = s.executeQuery(
					"SELECT action_label_past, display_name, DATE_FORMAT(completed_on,'%b %d %Y') sent_date, DATE_FORMAT(completed_on,'%h:%i %p') sent_time FROM workflow_requests_steps wrs join workflow_actions wa on wrs.action_id = wa.action_id join users u on wrs.user_id = u.id where request_id="
							+ RequestIDVal + " and wrs.completed_on is not null  order by step_id desc");
			while (rs5.next()) {
				html += "<b>" + rs5.getString("action_label_past") + "</b> by " + rs5.getString("display_name") + " ["
						+ rs5.getString("sent_date") + " | " + rs5.getString("sent_time") + "]<br>";
			}

			html += "<br/><b>Valid until</b> " + ValidToDateString + "<br>";
			html += "<br>";

			html += "<a href='http://localhost:8080/portal/WM/WMCI?token=" + UVID + "&action=1&sessionid="
					+ Math.random() + "'>Approve</a>    |    <a href='http://localhost:8080/portal/WM/WMCI?token="
					+ UVID + "&action=2&sessionid=" + Math.random() + "'>Decline</a>";
			html += "</body>";

			html += "</html>";

		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return html;

	}

	public static String getCoolerInjectionRequestHTMLWithoutActionButtons(long RequestIDVal) {

		Datasource ds = new Datasource();

		String html = "";
		try {

			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();

			long UVID = 0;
			long MasterParentTableID = 0;
			String PromotionName = "";
			ResultSet rs = s.executeQuery("select * from tot_issue_request where request_id = " + RequestIDVal);
			if (rs.first()) {

				// UVID = rs.getLong("uvid");
				MasterParentTableID = rs.getLong("id");
			}

			long OutletID = 0;
			String OutletName = "";
			String OutletAddress = "";
			String OutletContactName = "";
			long RegionID = 0;
			String RegionName = "";
			long SNDID = 0;
			String SNDName = "";
			long RSMID = 0;
			String RSMName = "";
			long DistributorID = 0;
			String DistributorName = "";
			String EmptyStatus = "";
			String TOTCode = "";
			String ExistingTOT = "";

			String PackageLabel1 = "";
			String BrandLabel1 = "";
			String RawCasesLabel1 = "";
			String UnitLabel1 = "";

			String HTMLMessage = "";
			// HTMLMessage = "<table><tr><td style='background: #123123'>Hello
			// 1</td></tr><tr><td>Hello 2</td></tr></table>";

			html = "<html>";
			html += "<body><br>";

			html += "<table style='width: 400px;'>";

			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='4'>Outlet Info</td>";
			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Outlet ID</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Outlet Name</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Address</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Contact Name</td>";
			html += "</tr>";

			html += "<tr>";

			ResultSet rs6 = s1.executeQuery(
					"SELECT *,(select label from common_outlets_channels coc where coc.id=outlet_channel) channel_name,(select concat(region_short_name,' - ',region_name) from common_regions cr where cr.region_id=outlet_region) region_name,IFNULL((select display_name from users u where u.id=outlet_rsm),'') outlet_rsm_name,ifnull((select display_name from users u where u.id=outlet_snd),'') outlet_snd_name,ifnull((select name from common_distributors cd where cd.distributor_id=outlet_distributor_id),'') outlet_distributor_name, (select label from common_assets_tot_sizes cats where cats.id=existing_tot) existing_tot_name FROM tot_issue_request where request_id="
							+ RequestIDVal);
			while (rs6.next()) {
				OutletID = rs6.getLong("outlet_id");
				OutletName = rs6.getString("outlet_name");
				OutletAddress = rs6.getString("outlet_address");
				OutletContactName = rs6.getString("outlet_contact_name");
				RegionName = rs6.getString("region_name");
				RSMName = rs6.getString("outlet_rsm_name");
				SNDName = rs6.getString("outlet_snd_name");
				DistributorName = rs6.getString("outlet_distributor_name");

				EmptyStatus = rs6.getString("tot_empty_status");
				TOTCode = rs6.getString("tot_code");
				ExistingTOT = rs6.getString("existing_tot_name");

			}

			html += "<td style='background-color: #EDEFF2;'>" + OutletID;
			html += "<td valign='top' style='b1ackground-color: #EDEFF2; text-align: left; '>" + OutletName;
			html += "<td valign='top' style='b1ackground-color: #EDEFF2; text-align: left;'>" + OutletAddress;
			html += "<td valign='top' style='b1ackground-color: #EDEFF2; text-align: left;'>" + OutletContactName;
			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Region</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>SND</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>RSM</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Distributor</td>";
			html += "</tr>";

			html += "<tr>";

			html += "<td style='background-color: #EDEFF2;'>" + RegionName;
			html += "<td valign='top' style='ba1ckground-color: #EDEFF2; text-align: left; '>" + RSMName;
			html += "<td valign='top' style='b1ackground-color: #EDEFF2; text-align: left;'>" + SNDName;
			html += "<td valign='top' style='b1ackground-color: #EDEFF2; text-align: left;'>" + DistributorName;
			html += "</tr>";

			html += "<tr>";
			html += "<td colspan='4'>&nbsp;";
			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='4'>Sales Volume-P/C: Per Year</td>";
			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>LRB Type</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;' colspan='2'>Package</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Quantity</td>";

			html += "</tr>";

			html += "<tr>";

			ResultSet rs121 = s.executeQuery(
					"SELECT id,type,(select label from inventory_products_lrb_types iplt where iplt.id=type) type_name,package_id,(select label from inventory_packages ip where ip.id=package_id) package_name,quantity FROM tot_issue_request_sales where id="
							+ MasterParentTableID);
			while (rs121.next()) {

				html += "<tr style='b1ackground-color:#efedee'>";
				html += "<td style='width:25%; padding:5px;' valign='top'>" + rs121.getString("type_name") + "</td>";
				html += "<td style='width:25%; padding:5px;' colspan='2'>" + rs121.getString("package_name") + "</td>";
				html += "<td style='width:25%; padding:5px;' valign='top'>" + rs121.getInt("quantity") + "</td>";

				html += "</tr>";

			}

			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='4'>TOT Info</td>";
			html += "</tr>";

			html += "<tr style='b1ackground-color:#efedee'>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;' valign='top'>Empty Status</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;' colspan='2'>Existing TOT Size</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;' valign='top'>TOT Code</td>";

			html += "</tr>";

			html += "<tr style='b1ackground-color:#efedee'>";
			html += "<td style='width:25%; padding:5px;' valign='top'>" + EmptyStatus + "</td>";
			html += "<td style='width:25%; padding:5px;'>" + ExistingTOT + "</td>";
			html += "<td style='width:25%; padding:5px;' valign='top'>" + TOTCode + "</td>";

			html += "</tr>";

			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='4'>Required TOT Size</td>";
			html += "</tr>";

			ResultSet rs20 = s.executeQuery(
					"SELECT id,tot_size_id,(select label from common_assets_tot_sizes cats where cats.id=tot_size_id) tot_size_name,quantity FROM tot_issue_request_sizes where id="
							+ MasterParentTableID);
			while (rs20.next()) {

				html += "<tr>";
				html += "<td style='width:25%; padding:5px;' valign='top' colspan='2'>"
						+ rs20.getString("tot_size_name") + "</td>";
				html += "<td style='width:25%; padding:5px;' colspan='2'>" + rs20.getString("quantity") + "</td>";

				html += "</tr>";

			}

			html += "<tr>";
			html += "<td colspan='4'>&nbsp;";
			html += "</tr>";

			html += "</table>";

			html += "<table style='width: 400px;'>";

			String ValidToDateString = "";
			String ValidToTimeString = "";

			html += "</table>";
			html += "<br>";
			html += "<table style='width: 400px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Messages</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs4 = s.executeQuery(
					"SELECT message,sent_by,(select DISPLAY_NAME from users u where u.id=wrcm.sent_by) sent_by_name,sent_on,DATE_FORMAT(sent_on,'%b %d %Y') sent_date,DATE_FORMAT(sent_on,'%h:%i %p') sent_time FROM workflow_requests_chat wrc join workflow_requests_chat_messages wrcm on wrc.conversation_id=wrcm.conversation_id and  wrc.request_id="
							+ RequestIDVal);
			while (rs4.next()) {

				html += "<b>" + rs4.getString("sent_by_name") + ":</b> " + rs4.getString("message") + " ["
						+ rs4.getString("sent_date") + " | " + rs4.getString("sent_time") + "]<br>";

			}

			html += "<br>";
			html += "<table style='width: 400px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Workflow</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs5 = s.executeQuery(
					"SELECT action_label_past, display_name, DATE_FORMAT(completed_on,'%b %d %Y') sent_date, DATE_FORMAT(completed_on,'%h:%i %p') sent_time FROM workflow_requests_steps wrs join workflow_actions wa on wrs.action_id = wa.action_id join users u on wrs.user_id = u.id where request_id="
							+ RequestIDVal + " and wrs.completed_on is not null  order by step_id desc");
			while (rs5.next()) {
				html += "<b>" + rs5.getString("action_label_past") + "</b> by " + rs5.getString("display_name") + " ["
						+ rs5.getString("sent_date") + " | " + rs5.getString("sent_time") + "]<br>";
			}

			html += "<br/><b>Valid until</b> " + ValidToDateString + "<br>";
			html += "<br>";

			html += "</body>";

			html += "</html>";

		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return html;

	}

	public static String getPerCaseDiscountRequestHTMLWithActionButtons(long RequestIDVal) {

		Datasource ds = new Datasource();

		String html = "";
		try {

			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();

			long UVID = 0;
			long PromotionID = 0;
			String PromotionName = "";
			ResultSet rs = s
					.executeQuery("select * from inventory_primary_percase_request where request_id = " + RequestIDVal);
			if (rs.first()) {
				// PromotionName = rs.getString("label");
				UVID = rs.getLong("uvid");
				PromotionID = rs.getLong("id");
			}

			String PackageLabel = "";

			String RawCasesLabel = "";
			String UnitLabel = "";

			String PackageLabel1 = "";
			String BrandLabel1 = "";
			String RawCasesLabel1 = "";
			String UnitLabel1 = "";

			String HTMLMessage = "";
			// HTMLMessage = "<table><tr><td style='background: #123123'>Hello
			// 1</td></tr><tr><td>Hello 2</td></tr></table>";

			int MasterSerialID = 0;
			html = "<html>";
			html += "<body><br>";

			html += "<table style='width: 500px;'>";

			//

			ResultSet rs6 = s1.executeQuery(
					"SELECT isprp.id, isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_labele_id,isprp.type_id,isprp.quantity,isprp.percase_discount_rate,isprp.amount,isprp.serial_no, (select lrb_type_id from inventory_primary_percase_request_products_lrb_types where id = ispr.id and package_id = isprp.package_id limit 1) lrb_type_id, (select distributor_id from inventory_primary_percase_request_distributors where product_promotion_id = ispr.id limit 1) distributor_id FROM inventory_primary_percase_request_products isprp join inventory_primary_percase_request ispr on ispr.id=isprp.id where ispr.request_id="
							+ RequestIDVal + " and isprp.type_id=1");
			while (rs6.next()) {
				
				int PackageID = rs6.getInt("package_id");
				long DistributorID = rs6.getInt("distributor_id");
				int LRBTypeIDFirst = rs6.getInt("lrb_type_id");
				PackageLabel = rs6.getString("package_labele_id");
				MasterSerialID = rs6.getInt("serial_no");

				
				double SKUPrice = 0;
				double UpfrontDiscount = 0;
				ResultSet rs7 = s2.executeQuery("SELECT gross_value/(total_units/cache_units_per_sku) sku_price, (select kbetr from sap_konv where knumv = price_condition_no and kposn = line_no) upfront_discount FROM inventory_delivery_note_source_invoice where distributor_id = "+DistributorID+" and cache_package_id = "+PackageID+" and cache_lrb_type_id = "+LRBTypeIDFirst+" and is_revenue = 1 order by id desc limit 1");
				if (rs7.first()){
					SKUPrice = rs7.getDouble(1);
					//UpfrontDiscount = rs7.getDouble(2);
				}
				if (UpfrontDiscount < 0){
					UpfrontDiscount = UpfrontDiscount * -1;
				}
				
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='3'>"
						+ PackageLabel + "</td>";
				html += "</tr>";

				double Quantity = rs6.getDouble("quantity");
				double PerCaseDiscountRate = rs6.getDouble("percase_discount_rate");
				double Amount = rs6.getDouble("amount");

				//int PackageID = rs6.getInt("package_id");
				int ProductTableID = rs6.getInt("id");

				int PackageCounter = 1;

				PackageLabel = rs6.getString("package_labele_id");

				// html += "<td colspan='3' valign='top'
				// style='background-color: #EDEFF2;'>"+PackageLabel;
				String LRBTypeLabel = "";
				ResultSet rs2 = s2.executeQuery(
						"SELECT isprp.id,isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_label,isprpb.lrb_type_id,(select label from inventory_products_lrb_types ib where ib.id=isprpb.lrb_type_id) lrb_type_label,isprp.type_id,isprpb.serial_no FROM inventory_primary_percase_request_products isprp join inventory_primary_percase_request_products_lrb_types isprpb on isprp.id=isprpb.id and isprp.serial_no=isprpb.serial_no and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_primary_percase_request ispr on ispr.id=isprp.id where ispr.request_id="
								+ RequestIDVal + " and isprp.type_id=1 and isprp.package_id="
								+ rs6.getLong("package_id") + " and isprp.serial_no=" + MasterSerialID
								+ " group by isprp.type_id,isprpb.lrb_type_id");
				while (rs2.next()) {

					if (!rs2.isFirst()) {
						LRBTypeLabel = LRBTypeLabel + ", ";
					}

					LRBTypeLabel += rs2.getString("lrb_type_label");

				}

				html += "<td colspan='3' style='background-color: #EDEFF2;'>" + LRBTypeLabel;

				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #FCFFE6; height: 18px; text-align: center; font-weight: bold;'>Quantity</td>";
				html += "<td style='background-color: #FCFFE6; height: 18px; text-align: center; font-weight: bold;'>Discount</td>";
				html += "<td style='background-color: #FCFFE6; height: 18px; text-align: center; font-weight: bold;'>Amount</td>";

				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Utilities.getDisplayCurrencyFormat(Quantity) + "</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Utilities.getDisplayCurrencyFormat(PerCaseDiscountRate) + "</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>" + Utilities.getDisplayCurrencyFormat(Amount)
						+ "</td>";
				
				html += "</tr>";
				
				
				double NetPrice = SKUPrice - UpfrontDiscount - PerCaseDiscountRate;
				
				if (SKUPrice > 0){
					
				html += "<tr>";
				html += "<td colspan='3' ><b>&nbsp;</b>";
				html += "</tr>";
				html += "<tr>";
				html += "<td colspan='2' style='background-color: #EDEFF2;'>SKU Price";
				html += "<td colspan='1' style='background-color: #EDEFF2; text-align: center;'>"+Utilities.getDisplayCurrencyFormat(SKUPrice);
				html += "</tr>";
//				html += "<tr>";
//				html += "<td colspan='2' style='background-color: #EDEFF2;'>Upfront Discount";
//				html += "<td colspan='1' style='background-color: #EDEFF2; text-align: center;'>"+Utilities.getDisplayCurrencyFormat(UpfrontDiscount);
//				html += "</tr>";
				html += "<tr>";
				html += "<td colspan='2' style='background-color: #EDEFF2;'>Per Case (Wholesale)";
				html += "<td colspan='1' style='background-color: #EDEFF2; text-align: center;'>"+Utilities.getDisplayCurrencyFormat(PerCaseDiscountRate);
				html += "</tr>";
				html += "<tr>";
				html += "<td colspan='2' style='background-color: #EDEFF2;'><b>Estimated Net Price</b>";
				html += "<td colspan='1' style='background-color: #EDEFF2;  text-align: center;'><b>"+Utilities.getDisplayCurrencyFormat(NetPrice)+"</b>";
				html += "</tr>";

				html += "<tr>";
				html += "<td colspan='3' style='font-size: 6px'>&nbsp;";
				html += "</tr>";

				html += "<tr>";
				html += "<td colspan='3' style='font-size: 12px'>* Promotions approved for specific days may impact Net Price as per actual lifting schedule";
				html += "</tr>";

				html += "<tr>";
				html += "<td colspan='3' style='font-size: 6px'>&nbsp;";
				html += "</tr>";

				
				}
			}

			String ValidToDateString = "";
			String ValidToTimeString = "";

			ResultSet rs3 = s.executeQuery(
					"select *,DATE_FORMAT(valid_to,'%b %d %Y') sent_date_valid,DATE_FORMAT(valid_to,'%h:%i %p') sent_time_valid from inventory_primary_percase_request where request_id="
							+ RequestIDVal);
			while (rs3.next()) {
				ValidToDateString = rs3.getString("sent_date_valid");
				ValidToTimeString = rs3.getString("sent_time_valid");

				html += "<tr><td>&nbsp;</td></tr>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='6'>Reason</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;' colspan='6'>"
						+ rs3.getString("comments") + "</td>";

				html += "</tr>";

			}

			html += "</table>";
			html += "<br>";
			html += "<table style='width: 500px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Messages</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs4 = s.executeQuery(
					"SELECT message,sent_by,(select DISPLAY_NAME from users u where u.id=wrcm.sent_by) sent_by_name,sent_on,DATE_FORMAT(sent_on,'%b %d %Y') sent_date,DATE_FORMAT(sent_on,'%h:%i %p') sent_time FROM workflow_requests_chat wrc join workflow_requests_chat_messages wrcm on wrc.conversation_id=wrcm.conversation_id and  wrc.request_id="
							+ RequestIDVal);
			while (rs4.next()) {

				html += "<b>" + rs4.getString("sent_by_name") + ":</b> " + rs4.getString("message") + " ["
						+ rs4.getString("sent_date") + " | " + rs4.getString("sent_time") + "]<br>";

			}

			ResultSet rs14 = s.executeQuery(
					"SELECT cd.distributor_id,cd.name FROM inventory_primary_percase_request_distributors isprd join inventory_primary_percase_request ispr on isprd.product_promotion_id=ispr.id join common_distributors cd on cd.distributor_id=isprd.distributor_id where ispr.id="
							+ PromotionID);
			if (rs14.first()) {
				html += "<br>";
				html += "<table style='width: 500px;'>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Customer</td>";
				html += "</tr>";
				html += "</table>";
				ResultSet rs10 = s.executeQuery(
						"SELECT cd.distributor_id,cd.name FROM inventory_primary_percase_request_distributors isprd join inventory_primary_percase_request ispr on isprd.product_promotion_id=ispr.id join common_distributors cd on cd.distributor_id=isprd.distributor_id where ispr.id="
								+ PromotionID);
				while (rs10.next()) {
					html += rs10.getString("distributor_id") + " - " + rs10.getString("name") + "<br/>";
				}
			}

			html += "<br>";
			html += "<table style='width: 500px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Workflow</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs5 = s.executeQuery(
					"SELECT action_label_past, display_name, DATE_FORMAT(completed_on,'%b %d %Y') sent_date, DATE_FORMAT(completed_on,'%h:%i %p') sent_time FROM workflow_requests_steps wrs join workflow_actions wa on wrs.action_id = wa.action_id join users u on wrs.user_id = u.id where request_id="
							+ RequestIDVal + " and wrs.completed_on is not null  order by step_id desc");
			while (rs5.next()) {
				html += "<b>" + rs5.getString("action_label_past") + "</b> by " + rs5.getString("display_name") + " ["
						+ rs5.getString("sent_date") + " | " + rs5.getString("sent_time") + "]<br>";
			}

			html += "<br/><b>Valid until</b> " + ValidToDateString + "<br>";
			html += "<br>";

			// html += "<a href='#'>Approve</a> | <a href='#'>Decline</a>";

			html += "<a href='http://portal.pbc.com.pk/portal/WM/WMPCD?token=" + UVID + "&action=1&sessionid="
					+ Math.random() + "'>Approve</a>    |    <a href='http://portal.pbc.com.pk/portal/WM/WMPCD?token="
					+ UVID + "&action=2&sessionid=" + Math.random() + "'>Decline</a>";

			html += "</body>";

			html += "</html>";

		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return html;

	}

	public static String getPerCaseDiscountRequestHTMLWithActionButtonsCOO(long RequestIDVal) {

		Datasource ds = new Datasource();

		String html = "";
		try {

			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();

			long UVID = 0;
			long PromotionID = 0;
			String PromotionName = "";
			ResultSet rs = s
					.executeQuery("select * from inventory_primary_percase_request where request_id = " + RequestIDVal);
			if (rs.first()) {
				// PromotionName = rs.getString("label");
				UVID = rs.getLong("uvid");
				PromotionID = rs.getLong("id");
			}

			String PackageLabel = "";

			String RawCasesLabel = "";
			String UnitLabel = "";

			String PackageLabel1 = "";
			String BrandLabel1 = "";
			String RawCasesLabel1 = "";
			String UnitLabel1 = "";

			String HTMLMessage = "";
			// HTMLMessage = "<table><tr><td style='background: #123123'>Hello
			// 1</td></tr><tr><td>Hello 2</td></tr></table>";

			int MasterSerialID = 0;
			html = "<html>";
			html += "<body><br>";

			html += "<table style='width: 500px;'>";

			//

			ResultSet rs6 = s1.executeQuery(
					"SELECT isprp.id, isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_labele_id,isprp.type_id,isprp.quantity,isprp.percase_discount_rate,isprp.amount,isprp.serial_no, (select lrb_type_id from inventory_primary_percase_request_products_lrb_types where id = ispr.id and package_id = isprp.package_id limit 1) lrb_type_id, (select distributor_id from inventory_primary_percase_request_distributors where product_promotion_id = ispr.id limit 1) distributor_id FROM inventory_primary_percase_request_products isprp join inventory_primary_percase_request ispr on ispr.id=isprp.id where ispr.request_id="
							+ RequestIDVal + " and isprp.type_id=1");
			while (rs6.next()) {
				
				int PackageID = rs6.getInt("package_id");
				long DistributorID = rs6.getInt("distributor_id");
				int LRBTypeIDFirst = rs6.getInt("lrb_type_id");
				PackageLabel = rs6.getString("package_labele_id");
				MasterSerialID = rs6.getInt("serial_no");

				
				double SKUPrice = 0;
				double UpfrontDiscount = 0;
				ResultSet rs7 = s2.executeQuery("SELECT gross_value/(total_units/cache_units_per_sku) sku_price, (select kbetr from sap_konv where knumv = price_condition_no and kposn = line_no) upfront_discount FROM inventory_delivery_note_source_invoice where distributor_id = "+DistributorID+" and cache_package_id = "+PackageID+" and cache_lrb_type_id = "+LRBTypeIDFirst+" and is_revenue = 1 order by id desc limit 1");
				if (rs7.first()){
					SKUPrice = rs7.getDouble(1);
					//UpfrontDiscount = rs7.getDouble(2);
				}
				if (UpfrontDiscount < 0){
					UpfrontDiscount = UpfrontDiscount * -1;
				}
				
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='3'>"
						+ PackageLabel + "</td>";
				html += "</tr>";

				double Quantity = rs6.getDouble("quantity");
				double PerCaseDiscountRate = rs6.getDouble("percase_discount_rate");
				double Amount = rs6.getDouble("amount");

				//int PackageID = rs6.getInt("package_id");
				int ProductTableID = rs6.getInt("id");

				int PackageCounter = 1;

				PackageLabel = rs6.getString("package_labele_id");

				// html += "<td colspan='3' valign='top'
				// style='background-color: #EDEFF2;'>"+PackageLabel;
				String LRBTypeLabel = "";
				ResultSet rs2 = s2.executeQuery(
						"SELECT isprp.id,isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_label,isprpb.lrb_type_id,(select label from inventory_products_lrb_types ib where ib.id=isprpb.lrb_type_id) lrb_type_label,isprp.type_id,isprpb.serial_no FROM inventory_primary_percase_request_products isprp join inventory_primary_percase_request_products_lrb_types isprpb on isprp.id=isprpb.id and isprp.serial_no=isprpb.serial_no and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_primary_percase_request ispr on ispr.id=isprp.id where ispr.request_id="
								+ RequestIDVal + " and isprp.type_id=1 and isprp.package_id="
								+ rs6.getLong("package_id") + " and isprp.serial_no=" + MasterSerialID
								+ " group by isprp.type_id,isprpb.lrb_type_id");
				while (rs2.next()) {

					if (!rs2.isFirst()) {
						LRBTypeLabel = LRBTypeLabel + ", ";
					}

					LRBTypeLabel += rs2.getString("lrb_type_label");

				}

				html += "<td colspan='3' style='background-color: #EDEFF2;'>" + LRBTypeLabel;

				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #FCFFE6; height: 18px; text-align: center; font-weight: bold;'>Quantity</td>";
				html += "<td style='background-color: #FCFFE6; height: 18px; text-align: center; font-weight: bold;'>Discount</td>";
				html += "<td style='background-color: #FCFFE6; height: 18px; text-align: center; font-weight: bold;'>Amount</td>";

				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Utilities.getDisplayCurrencyFormat(Quantity) + "</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Utilities.getDisplayCurrencyFormat(PerCaseDiscountRate) + "</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>" + Utilities.getDisplayCurrencyFormat(Amount)
						+ "</td>";
				
				html += "</tr>";
				
				
				double NetPrice = SKUPrice - UpfrontDiscount - PerCaseDiscountRate;
				
				if (SKUPrice > 0){
					
				html += "<tr>";
				html += "<td colspan='3' ><b>&nbsp;</b>";
				html += "</tr>";
				html += "<tr>";
				html += "<td colspan='2' style='background-color: #EDEFF2;'>SKU Price";
				html += "<td colspan='1' style='background-color: #EDEFF2; text-align: center;'>"+Utilities.getDisplayCurrencyFormat(SKUPrice);
				html += "</tr>";
//				html += "<tr>";
//				html += "<td colspan='2' style='background-color: #EDEFF2;'>Upfront Discount";
//				html += "<td colspan='1' style='background-color: #EDEFF2; text-align: center;'>"+Utilities.getDisplayCurrencyFormat(UpfrontDiscount);
//				html += "</tr>";
				html += "<tr>";
				html += "<td colspan='2' style='background-color: #EDEFF2;'>Per Case (Wholesale)";
				html += "<td colspan='1' style='background-color: #EDEFF2; text-align: center;'>"+Utilities.getDisplayCurrencyFormat(PerCaseDiscountRate);
				html += "</tr>";
				html += "<tr>";
				html += "<td colspan='2' style='background-color: #EDEFF2;'><b>Estimated Net Price</b>";
				html += "<td colspan='1' style='background-color: #EDEFF2;  text-align: center;'><b>"+Utilities.getDisplayCurrencyFormat(NetPrice)+"</b>";
				html += "</tr>";

				html += "<tr>";
				html += "<td colspan='3' style='font-size: 6px'>&nbsp;";
				html += "</tr>";

				html += "<tr>";
				html += "<td colspan='3' style='font-size: 12px'>* Promotions approved for specific days may impact Net Price as per actual lifting schedule";
				html += "</tr>";

				html += "<tr>";
				html += "<td colspan='3' style='font-size: 6px'>&nbsp;";
				html += "</tr>";

				
				}
			}

			String ValidToDateString = "";
			String ValidToTimeString = "";

			ResultSet rs3 = s.executeQuery(
					"select *,DATE_FORMAT(valid_to,'%b %d %Y') sent_date_valid,DATE_FORMAT(valid_to,'%h:%i %p') sent_time_valid from inventory_primary_percase_request where request_id="
							+ RequestIDVal);
			while (rs3.next()) {
				ValidToDateString = rs3.getString("sent_date_valid");
				ValidToTimeString = rs3.getString("sent_time_valid");

				html += "<tr><td>&nbsp;</td></tr>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='3'>Reason</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;' colspan='3'>"
						+ rs3.getString("comments") + "</td>";

				html += "</tr>";

			}

			html += "</table>";
			html += "<br>";
			html += "<table style='width: 500px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Messages</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs4 = s.executeQuery(
					"SELECT message,sent_by,(select DISPLAY_NAME from users u where u.id=wrcm.sent_by) sent_by_name,sent_on,DATE_FORMAT(sent_on,'%b %d %Y') sent_date,DATE_FORMAT(sent_on,'%h:%i %p') sent_time FROM workflow_requests_chat wrc join workflow_requests_chat_messages wrcm on wrc.conversation_id=wrcm.conversation_id and  wrc.request_id="
							+ RequestIDVal);
			while (rs4.next()) {

				html += "<b>" + rs4.getString("sent_by_name") + ":</b> " + rs4.getString("message") + " ["
						+ rs4.getString("sent_date") + " | " + rs4.getString("sent_time") + "]<br>";

			}

			ResultSet rs14 = s.executeQuery(
					"SELECT cd.distributor_id,cd.name FROM inventory_primary_percase_request_distributors isprd join inventory_primary_percase_request ispr on isprd.product_promotion_id=ispr.id join common_distributors cd on cd.distributor_id=isprd.distributor_id where ispr.id="
							+ PromotionID);
			if (rs14.first()) {
				html += "<br>";
				html += "<table style='width: 500px;'>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Customer</td>";
				html += "</tr>";
				html += "</table>";
				ResultSet rs10 = s.executeQuery(
						"SELECT cd.distributor_id,cd.name FROM inventory_primary_percase_request_distributors isprd join inventory_primary_percase_request ispr on isprd.product_promotion_id=ispr.id join common_distributors cd on cd.distributor_id=isprd.distributor_id where ispr.id="
								+ PromotionID);
				while (rs10.next()) {
					html += rs10.getString("distributor_id") + " - " + rs10.getString("name") + "<br/>";
				}
			}

			html += "<br>";
			html += "<table style='width: 500px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Workflow</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs5 = s.executeQuery(
					"SELECT action_label_past, display_name, DATE_FORMAT(completed_on,'%b %d %Y') sent_date, DATE_FORMAT(completed_on,'%h:%i %p') sent_time FROM workflow_requests_steps wrs join workflow_actions wa on wrs.action_id = wa.action_id join users u on wrs.user_id = u.id where request_id="
							+ RequestIDVal + " and wrs.completed_on is not null  order by step_id desc");
			while (rs5.next()) {
				html += "<b>" + rs5.getString("action_label_past") + "</b> by " + rs5.getString("display_name") + " ["
						+ rs5.getString("sent_date") + " | " + rs5.getString("sent_time") + "]<br>";
			}

			html += "<br/><b>Valid until</b> " + ValidToDateString + "<br>";
			html += "<br>";

			/////////html += "<a href='http://portal.pbc.com.pk/portal/WM/WMPCD?token=" + UVID + "&action=1&secondary=1&sessionid="
					//////////+ Math.random() + "'>Approve</a>    |    <a href='http://portal.pbc.com.pk/portal/WM/WMPCD?token="
					///////////+ UVID + "&action=2&secondary=1&sessionid=" + Math.random() + "'>Decline</a>";

			 html += "<a "+
			 "href='http://portal.pbc.com.pk/portal/WM/WMPCD?token="+UVID+"&action=1&secondary=1&sessionid="+Math.random()+"'>Approve</a>"
			 +" | <a "+
			 " href='http://portal.pbc.com.pk/portal/WM/WMPCD?token="+UVID+"&action=2&secondary=1&sessionid="+Math.random()+"'>Decline</a>";

			html += "</body>";

			html += "</html>";

		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return html;

	}

	public static String getPerCaseDiscountRequestHTMLWithoutActionButtons(long RequestIDVal) {

		Datasource ds = new Datasource();

		String html = "";
		try {

			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();

			long UVID = 0;
			long PromotionID = 0;
			String PromotionName = "";
			ResultSet rs = s
					.executeQuery("select * from inventory_primary_percase_request where request_id = " + RequestIDVal);
			if (rs.first()) {
				// PromotionName = rs.getString("label");
				UVID = rs.getLong("uvid");
				PromotionID = rs.getLong("id");
			}

			String PackageLabel = "";

			String RawCasesLabel = "";
			String UnitLabel = "";

			String PackageLabel1 = "";
			String BrandLabel1 = "";
			String RawCasesLabel1 = "";
			String UnitLabel1 = "";

			String HTMLMessage = "";
			// HTMLMessage = "<table><tr><td style='background: #123123'>Hello
			// 1</td></tr><tr><td>Hello 2</td></tr></table>";

			int MasterSerialID = 0;
			html = "<html>";
			html += "<body><br>";

			html += "<table style='width: 670px;'>";

			//

			ResultSet rs6 = s1.executeQuery(
					"SELECT isprp.id, isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_labele_id,isprp.type_id,isprp.quantity,isprp.percase_discount_rate,isprp.amount,isprp.serial_no FROM inventory_primary_percase_request_products isprp join inventory_primary_percase_request ispr on ispr.id=isprp.id where ispr.request_id="
							+ RequestIDVal + " and isprp.type_id=1");
			while (rs6.next()) {

				PackageLabel = rs6.getString("package_labele_id");
				MasterSerialID = rs6.getInt("serial_no");

				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='6'>"
						+ PackageLabel + "</td>";
				html += "</tr>";

				double Quantity = rs6.getDouble("quantity");
				double PerCaseDiscountRate = rs6.getDouble("percase_discount_rate");
				double Amount = rs6.getDouble("amount");

				int PackageID = rs6.getInt("package_id");
				int ProductTableID = rs6.getInt("id");

				int PackageCounter = 1;

				PackageLabel = rs6.getString("package_labele_id");

				// html += "<td colspan='3' valign='top'
				// style='background-color: #EDEFF2;'>"+PackageLabel;
				String LRBTypeLabel = "";
				ResultSet rs2 = s2.executeQuery(
						"SELECT isprp.id,isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_label,isprpb.lrb_type_id,(select label from inventory_products_lrb_types ib where ib.id=isprpb.lrb_type_id) lrb_type_label,isprp.type_id,isprpb.serial_no FROM inventory_primary_percase_request_products isprp join inventory_primary_percase_request_products_lrb_types isprpb on isprp.id=isprpb.id and isprp.serial_no=isprpb.serial_no and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_primary_percase_request ispr on ispr.id=isprp.id where ispr.request_id="
								+ RequestIDVal + " and isprp.type_id=1 and isprp.package_id="
								+ rs6.getLong("package_id") + " and isprp.serial_no=" + MasterSerialID
								+ " group by isprp.type_id,isprpb.lrb_type_id");
				while (rs2.next()) {

					if (!rs2.isFirst()) {
						LRBTypeLabel = LRBTypeLabel + ", ";
					}

					LRBTypeLabel += rs2.getString("lrb_type_label");

				}

				html += "<td colspan='6' style='background-color: #EDEFF2;'>" + LRBTypeLabel;

				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center; font-weight: bold;'>Quantity</td>";
				html += "<td style='background-color: #FCFFE6; height: 18px; text-align: center; font-weight: bold;'>Per Case Discount</td>";
				html += "<td style='background-color: #FCFFE6; height: 18px; text-align: center; font-weight: bold;'>Amount</td>";

				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Utilities.getDisplayCurrencyFormat(Quantity) + "</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>"
						+ Utilities.getDisplayCurrencyFormat(PerCaseDiscountRate) + "</td>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: center;'>" + Utilities.getDisplayCurrencyFormat(Amount)
						+ "</td>";

				html += "</tr>";

				html += "<tr>";
				html += "<td colspan='6'>&nbsp;";
				html += "</tr>";

			}

			String ValidToDateString = "";
			String ValidToTimeString = "";

			ResultSet rs3 = s.executeQuery(
					"select *,DATE_FORMAT(valid_to,'%b %d %Y') sent_date_valid,DATE_FORMAT(valid_to,'%h:%i %p') sent_time_valid from inventory_primary_percase_request where request_id="
							+ RequestIDVal);
			while (rs3.next()) {
				ValidToDateString = rs3.getString("sent_date_valid");
				ValidToTimeString = rs3.getString("sent_time_valid");

				html += "<tr><td>&nbsp;</td></tr>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='6'>Reason</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;' colspan='6'>"
						+ rs3.getString("comments") + "</td>";

				html += "</tr>";

			}

			html += "</table>";
			html += "<br>";
			html += "<table style='width: 670px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Messages</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs4 = s.executeQuery(
					"SELECT message,sent_by,(select DISPLAY_NAME from users u where u.id=wrcm.sent_by) sent_by_name,sent_on,DATE_FORMAT(sent_on,'%b %d %Y') sent_date,DATE_FORMAT(sent_on,'%h:%i %p') sent_time FROM workflow_requests_chat wrc join workflow_requests_chat_messages wrcm on wrc.conversation_id=wrcm.conversation_id and  wrc.request_id="
							+ RequestIDVal);
			while (rs4.next()) {

				html += "<b>" + rs4.getString("sent_by_name") + ":</b> " + rs4.getString("message") + " ["
						+ rs4.getString("sent_date") + " | " + rs4.getString("sent_time") + "]<br>";

			}

			ResultSet rs14 = s.executeQuery(
					"SELECT cd.distributor_id,cd.name FROM inventory_primary_percase_request_distributors isprd join inventory_primary_percase_request ispr on isprd.product_promotion_id=ispr.id join common_distributors cd on cd.distributor_id=isprd.distributor_id where ispr.id="
							+ PromotionID);
			if (rs14.first()) {
				html += "<br>";
				html += "<table style='width: 670px;'>";
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Customer</td>";
				html += "</tr>";
				html += "</table>";
				ResultSet rs10 = s.executeQuery(
						"SELECT cd.distributor_id,cd.name FROM inventory_primary_percase_request_distributors isprd join inventory_primary_percase_request ispr on isprd.product_promotion_id=ispr.id join common_distributors cd on cd.distributor_id=isprd.distributor_id where ispr.id="
								+ PromotionID);
				while (rs10.next()) {
					html += rs10.getString("distributor_id") + " - " + rs10.getString("name") + "<br/>";
				}
			}

			html += "<br>";
			html += "<table style='width: 670px;'>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Workflow</td>";
			html += "</tr>";
			html += "</table>";
			ResultSet rs5 = s.executeQuery(
					"SELECT action_label_past, display_name, DATE_FORMAT(completed_on,'%b %d %Y') sent_date, DATE_FORMAT(completed_on,'%h:%i %p') sent_time FROM workflow_requests_steps wrs join workflow_actions wa on wrs.action_id = wa.action_id join users u on wrs.user_id = u.id where request_id="
							+ RequestIDVal + " and wrs.completed_on is not null  order by step_id desc");
			while (rs5.next()) {
				html += "<b>" + rs5.getString("action_label_past") + "</b> by " + rs5.getString("display_name") + " ["
						+ rs5.getString("sent_date") + " | " + rs5.getString("sent_time") + "]<br>";
			}

			html += "<br/><b>Valid until</b> " + ValidToDateString + "<br>";
			html += "<br>";

			html += "</body>";

			html += "</html>";

		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return html;

	}
	
public static String getEmptyCreditLimitRequestHTMLWithActionButtons(long RequestIDVal){
		
		Datasource ds = new Datasource();
		
		String html ="";
		try {
		
		ds.createConnection();
		ds.startTransaction();
		
		
		Statement s = ds.createStatement();
		Statement s1 = ds.createStatement();
		Statement s2 = ds.createStatement();
		
		
		
		long UVID=0;
		long PromotionID=0;
		String PromotionName = "";
		ResultSet rs = s.executeQuery("select * from ec_empty_credit_limit_request where request_id = "+RequestIDVal);
		if(rs.first()){
			//PromotionName = rs.getString("label");
			UVID = rs.getLong("uvid");
			PromotionID = rs.getLong("id");
		}
		
		
		
		String HTMLMessage = "";
		//HTMLMessage = "<table><tr><td style='background: #123123'>Hello 1</td></tr><tr><td>Hello 2</td></tr></table>";
		
		
		 html = "<html>";
		html += "<body><br>";

		html += "<table style='width: 400px;'>";
		
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;'>Credit Summary</td>";
			html += "</tr>";
		
			html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Customer</td>";
				//html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Credit Limit</td>";
			
				
			html += "</tr>";
		
			html += "<tr>";
				
			String CustomerName="";
			String  CreditLimitTypeName="";
			Date ExpiryDate= null;
			long CustomerID = 0;
			
			ResultSet rs6 = s1.executeQuery("select *,(select name from common_distributors cd where cd.distributor_id = ececlr.distributor_id) customer_name,ececlr.credit_type,(select label from ec_empty_credit_types where id=ececlr.credit_type) credit_type_name from ec_empty_credit_limit_request ececlr where ececlr.request_id="+RequestIDVal);
			while(rs6.next()){
				
			CustomerName = rs6.getLong("distributor_id")+" - "+rs6.getString("customer_name");
			CreditLimitTypeName = rs6.getString("credit_type_name");
			CustomerID = rs6.getLong("distributor_id");
			}
			
			
				html += "<td style='background-color: #EDEFF2;'>"+CustomerName;
				//html += "<td valign='top' style='background-color: #EDEFF2; text-align: left; '>"+Utilities.getDisplayCurrencyFormat(CreditLimit);
				
			html += "</tr>";
			html += "<tr>";
			//html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Customer</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Credit Limit Type</td>";
			html += "</tr>";
		
			html += "<tr>";
			html += "<td valign='top' style='background-color: #EDEFF2; text-align: left; '>"+CreditLimitTypeName;
			html += "</tr>";
			
		
			
			
			
			

			html += "</table>";
			
			
			html += "<table style='width: 400px;'>";
			
			html += "<tr><td>&nbsp;</td></tr>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='3'>Products</td>";
			html += "</tr>";
			
			
			
			html += "<tr>";
			html += "<th  style='background-color:#EDEFF2; padding:5px;font-size:12px'>Package</th>";
			
			html += "<th  style='background-color:#EDEFF2; padding:5px;font-size:12px'>Case</th>";
			html += "<th  style='background-color:#EDEFF2; padding:5px;font-size:12px'>Bottle</th>";	
			html += "</tr>";
			
			String PackageLabel="";
			
			String RawCasesLabel="";
			String UnitLabel="";
			
			
			
			ResultSet rs12 = s.executeQuery("SELECT ececlr.package_id,(select label from inventory_packages ip where ip.id=ececlr.package_id) pack_name,ececlr.raw_cases,ececlr.units,ececlr.total_units FROM ec_empty_credit_limit_request ececl join ec_empty_credit_limit_request_products ececlr on ececl.id=ececlr.id where ececl.request_id="+RequestIDVal);
			while(rs12.next()){
				PackageLabel = rs12.getString("pack_name");
				RawCasesLabel = rs12.getString("raw_cases");
				UnitLabel = rs12.getString("units");
			
			
				html += "<tr style='b1ackground-color:#efedee'>";
					html += "<td style='width:25%; padding:5px;' valign='top'>"+PackageLabel+"</td>";
					html += "<td style='width:25%; padding:5px;' valign='top'>"+RawCasesLabel+"</td>";
					html += "<td style='width:25%; padding:5px;' valign='top'>"+UnitLabel+"</td>";
				html += "</tr>";	
			
			}
			
			
			
			
			
			html += "</table>";
			
			

	html += "<table style='width: 400px;'>";









	String ValidToDateString="";
	String ValidToTimeString="";

	ResultSet rs3 = s.executeQuery("select *,DATE_FORMAT(end_date,'%b %d %Y') sent_date_valid from ec_empty_credit_limit_request where request_id="+RequestIDVal);
	while(rs3.next()){
		ValidToDateString = rs3.getString("sent_date_valid");
		
		

		html += "<tr><td>&nbsp;</td></tr>";
		html += "<tr>";
		html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='2'>Reason</td>";
		html += "</tr>";
		html += "<tr>";
		html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;' colspan='2'>"+rs3.getString("reason")+"</td>";
		
		html += "</tr>";




	}





	html += "</table>";
	html += "<br>";
	html += "<table style='width: 400px;'>";
	html += "<tr>";
	html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Messages</td>";
	html += "</tr>";
	html += "</table>";
	ResultSet rs4 = s.executeQuery("SELECT message,sent_by,(select DISPLAY_NAME from users u where u.id=wrcm.sent_by) sent_by_name,sent_on,DATE_FORMAT(sent_on,'%b %d %Y') sent_date,DATE_FORMAT(sent_on,'%h:%i %p') sent_time FROM workflow_requests_chat wrc join workflow_requests_chat_messages wrcm on wrc.conversation_id=wrcm.conversation_id and  wrc.request_id="+RequestIDVal);
	while(rs4.next()){
		
		html += "<b>"+rs4.getString("sent_by_name")+":</b> "+rs4.getString("message")+" ["+rs4.getString("sent_date")+" | "+rs4.getString("sent_time")+"]<br>";
		
		
		
		
	}













	html += "<br>";
	html += "<table style='width: 400px;'>";
	html += "<tr>";
	html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Workflow</td>";
	html += "</tr>";
	html += "</table>";
	ResultSet rs5 = s.executeQuery("SELECT action_label_past, display_name, DATE_FORMAT(completed_on,'%b %d %Y') sent_date, DATE_FORMAT(completed_on,'%h:%i %p') sent_time FROM workflow_requests_steps wrs join workflow_actions wa on wrs.action_id = wa.action_id join users u on wrs.user_id = u.id where request_id="+RequestIDVal+" and wrs.completed_on is not null  order by step_id desc");
	while(rs5.next()){
		html += "<b>" + rs5.getString("action_label_past")+"</b> by "+rs5.getString("display_name")+" ["+rs5.getString("sent_date")+" | "+rs5.getString("sent_time")+"]<br>";
	}




	html += "<br/><b>Valid until</b> "+ValidToDateString+"<br>";
	html += "<br>";


	///////html += "<a href='http://portal.pbc.com.pk/portal/WM/WMCL?token="+UVID+"&action=1&sessionid="+Math.random()+"'>Approve</a>    |    <a href='http://portal.pbc.com.pk/portal/WM/WMCL?token="+UVID+"&action=2&sessionid="+Math.random()+"'>Decline</a>";
	html += "<a href='http://localhost:8080/portal/WM/WMECL?token="+UVID+"&action=1&sessionid="+Math.random()+"'>Approve</a>    |    <a href='http://localhost:8080/portal/WM/WMECL?token="+UVID+"&action=2&sessionid="+Math.random()+"'>Decline</a>";

	//html += "<a href='http://localhost:8080/portal/WM/WMP?token="+UVID+"&action=1&sessionid="+Math.random()+"'>Approve</a>    |    <a href='http://localhost:8080/portal/WM/WMP?token="+UVID+"&action=2&sessionid="+Math.random()+"'>Decline</a>";

	html += "</body>";



	html += "</html>";
		
		
		
		
		
		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return html;
		
	}


	public static String getEmptyCreditLimitRequestHTMLWithoutActionButtons(long RequestIDVal){
		
		Datasource ds = new Datasource();
		
		String html ="";
		try {
		
		ds.createConnection();
		ds.startTransaction();
		
		
		Statement s = ds.createStatement();
		Statement s1 = ds.createStatement();
		Statement s2 = ds.createStatement();
		
		
		
		long UVID=0;
		long PromotionID=0;
		String PromotionName = "";
		ResultSet rs = s.executeQuery("select * from ec_empty_credit_limit_request where request_id = "+RequestIDVal);
		if(rs.first()){
			//PromotionName = rs.getString("label");
			UVID = rs.getLong("uvid");
			PromotionID = rs.getLong("id");
		}
		
		
		
		String HTMLMessage = "";
		//HTMLMessage = "<table><tr><td style='background: #123123'>Hello 1</td></tr><tr><td>Hello 2</td></tr></table>";
		
		
		 html = "<html>";
		html += "<body><br>";

		html += "<table style='width: 400px;'>";
		
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;'>Credit Summary</td>";
			html += "</tr>";
		
			html += "<tr>";
				html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Customer</td>";
				//html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Credit Limit</td>";
			
				
			html += "</tr>";
		
			html += "<tr>";
				
			String CustomerName="";
			String  CreditLimitTypeName="";
			Date ExpiryDate= null;
			long CustomerID = 0;
			
			ResultSet rs6 = s1.executeQuery("select *,(select name from common_distributors cd where cd.distributor_id = ececlr.distributor_id) customer_name,ececlr.credit_type,(select label from ec_empty_credit_types where id=ececlr.credit_type) credit_type_name from ec_empty_credit_limit_request ececlr where ececlr.request_id="+RequestIDVal);
			while(rs6.next()){
				
			CustomerName = rs6.getLong("distributor_id")+" - "+rs6.getString("customer_name");
			CreditLimitTypeName = rs6.getString("credit_type_name");
			CustomerID = rs6.getLong("distributor_id");
			}
			
			
				html += "<td style='background-color: #EDEFF2;'>"+CustomerName;
				//html += "<td valign='top' style='background-color: #EDEFF2; text-align: left; '>"+Utilities.getDisplayCurrencyFormat(CreditLimit);
				
			html += "</tr>";
			html += "<tr>";
			//html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Customer</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Credit Limit Type</td>";
			html += "</tr>";
		
			html += "<tr>";
			html += "<td valign='top' style='background-color: #EDEFF2; text-align: left; '>"+CreditLimitTypeName;
			html += "</tr>";
			
		
			
			
			
			

			html += "</table>";
			
			
			html += "<table style='width: 400px;'>";
			
			html += "<tr><td>&nbsp;</td></tr>";
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='3'>Products</td>";
			html += "</tr>";
			
			
			
			html += "<tr>";
			html += "<th  style='background-color:#EDEFF2; padding:5px;font-size:12px'>Package</th>";
			
			html += "<th  style='background-color:#EDEFF2; padding:5px;font-size:12px'>Case</th>";
			html += "<th  style='background-color:#EDEFF2; padding:5px;font-size:12px'>Bottle</th>";	
			html += "</tr>";
			
			String PackageLabel="";
			
			String RawCasesLabel="";
			String UnitLabel="";
			
			
			
			ResultSet rs12 = s.executeQuery("SELECT ececlr.package_id,(select label from inventory_packages ip where ip.id=ececlr.package_id) pack_name,ececlr.raw_cases,ececlr.units,ececlr.total_units FROM ec_empty_credit_limit_request ececl join ec_empty_credit_limit_request_products ececlr on ececl.id=ececlr.id where ececl.request_id="+RequestIDVal);
			while(rs12.next()){
				PackageLabel = rs12.getString("pack_name");
				RawCasesLabel = rs12.getString("raw_cases");
				UnitLabel = rs12.getString("units");
			
			
				html += "<tr style='b1ackground-color:#efedee'>";
					html += "<td style='width:25%; padding:5px;' valign='top'>"+PackageLabel+"</td>";
					html += "<td style='width:25%; padding:5px;' valign='top'>"+RawCasesLabel+"</td>";
					html += "<td style='width:25%; padding:5px;' valign='top'>"+UnitLabel+"</td>";
				html += "</tr>";	
			
			}
			
			
			
			
			
			html += "</table>";
			
			

	html += "<table style='width: 400px;'>";









	String ValidToDateString="";
	String ValidToTimeString="";

	ResultSet rs3 = s.executeQuery("select *,DATE_FORMAT(end_date,'%b %d %Y') sent_date_valid from ec_empty_credit_limit_request where request_id="+RequestIDVal);
	while(rs3.next()){
		ValidToDateString = rs3.getString("sent_date_valid");
		
		

		html += "<tr><td>&nbsp;</td></tr>";
		html += "<tr>";
		html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='2'>Reason</td>";
		html += "</tr>";
		html += "<tr>";
		html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;' colspan='2'>"+rs3.getString("reason")+"</td>";
		
		html += "</tr>";




	}





	html += "</table>";
	html += "<br>";
	html += "<table style='width: 400px;'>";
	html += "<tr>";
	html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Messages</td>";
	html += "</tr>";
	html += "</table>";
	ResultSet rs4 = s.executeQuery("SELECT message,sent_by,(select DISPLAY_NAME from users u where u.id=wrcm.sent_by) sent_by_name,sent_on,DATE_FORMAT(sent_on,'%b %d %Y') sent_date,DATE_FORMAT(sent_on,'%h:%i %p') sent_time FROM workflow_requests_chat wrc join workflow_requests_chat_messages wrcm on wrc.conversation_id=wrcm.conversation_id and  wrc.request_id="+RequestIDVal);
	while(rs4.next()){
		
		html += "<b>"+rs4.getString("sent_by_name")+":</b> "+rs4.getString("message")+" ["+rs4.getString("sent_date")+" | "+rs4.getString("sent_time")+"]<br>";
		
		
		
		
	}













	html += "<br>";
	html += "<table style='width: 400px;'>";
	html += "<tr>";
	html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Workflow</td>";
	html += "</tr>";
	html += "</table>";
	ResultSet rs5 = s.executeQuery("SELECT action_label_past, display_name, DATE_FORMAT(completed_on,'%b %d %Y') sent_date, DATE_FORMAT(completed_on,'%h:%i %p') sent_time FROM workflow_requests_steps wrs join workflow_actions wa on wrs.action_id = wa.action_id join users u on wrs.user_id = u.id where request_id="+RequestIDVal+" and wrs.completed_on is not null  order by step_id desc");
	while(rs5.next()){
		html += "<b>" + rs5.getString("action_label_past")+"</b> by "+rs5.getString("display_name")+" ["+rs5.getString("sent_date")+" | "+rs5.getString("sent_time")+"]<br>";
	}




	html += "<br/><b>Valid until</b> "+ValidToDateString+"<br>";
	html += "<br>";




	html += "</body>";



	html += "</html>";
		
		
		
		
		
		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return html;
		
	}
}
