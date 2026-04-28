package com.mf.users;

import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import com.pbc.util.Datasource;

public class UserInfoExecute {
	public static void user_logs(Datasource ds, int user_id, int login_typ_id, String device_id,
			HttpServletRequest request) {

		try {

			Statement s = ds.createStatement();

			s.executeUpdate("insert into " + ds.logDatabaseName()
					+ ".log_user_login(user_id,password, ip_address, attempted_on,type_id, mobile_uuid) values("
					+ user_id + ",'','" + request.getRemoteAddr() + "',now()," + login_typ_id + ", '" + device_id
					+ "')");

			s.close();

		} catch (SQLException e) {
			System.out.println("User logs Query : " + e);

		}

	}
}
