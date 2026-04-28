package com.mf.controller.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.pbc.util.Datasource;

public class AuthenticationUtils {

	public static boolean verify_MV_app_version(Datasource ds, String app_version) {
		boolean app_version_active = false;
		try {

			Statement s = ds.createStatement();

			System.out.println("SELECT id FROM pep.mv_app_version where version='" + app_version + "' and is_active=1");

			ResultSet rsAppVersion = s.executeQuery(
					"SELECT id FROM pep.mv_app_version where version='" + app_version + "' and is_active=1");
			app_version_active = (rsAppVersion.first());
			s.close();
		} catch (SQLException e) {
			System.out.println("User logs Query : " + e);

		}
		return app_version_active;

	}
	public static boolean verify_OB_app_version(Datasource ds, String app_version) {
		boolean app_version_active = false;
		try {

			Statement s = ds.createStatement();

			System.out.println("SELECT id FROM pep.ob_app_version where version='" + app_version + "' and is_active=1");

			ResultSet rsAppVersion = s.executeQuery(
					"SELECT id FROM pep.ob_app_version where version='" + app_version + "' and is_active=1");
			app_version_active = (rsAppVersion.first());
			s.close();
		} catch (SQLException e) {
			System.out.println("User logs Query : " + e);

		}
		return app_version_active;

	}
	public static int get_city_id_by_user(Datasource ds, int user_id) {
		int city_id = 0;
		try {

			Statement s = ds.createStatement();

			// System.out.println("select city_id from users where id=" + user_id);

			ResultSet rsCityId = s.executeQuery("select city_id from users where id=" + user_id);
			city_id = (rsCityId.first()) ? rsCityId.getInt("city_id") : 0;

			if (city_id == 0) {
				ResultSet rsCityId2 = s.executeQuery(
						"select city_id from common_distributors where distributor_id in(select distributor_id from distributor_beat_plan_view where assigned_to="
								+ user_id + ")");
				city_id = (rsCityId2.first()) ? rsCityId2.getInt("city_id") : 0;
			}

			s.close();
		} catch (SQLException e) {
			System.out.println("User logs Query : " + e);

		}
		return city_id;

	}

}
