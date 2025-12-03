package com.pbc.myscripts;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;

import com.pbc.bi.BiProcesses;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;

public class AddDistributorinEidHoliday {
	static Datasource ds = new Datasource();

	public static void main(String[] args) {
		try {

			ds.createConnection();
			// ds.startTransaction();

			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			String sql = "SELECT * FROM pep.common_distributors where is_active=1 and type_id=2 and distributor_id not in (select distinct distributor_id from common_distributor_groups_list where id=2)";

			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				String INSERT = "insert into common_distributor_groups_list (id,distributor_id) values (2,"
						+ rs.getInt("distributor_id") + ")";
				s2.executeUpdate(INSERT);

			}
			
			s2.close();
			s.close();
			ds.dropConnection();

		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
