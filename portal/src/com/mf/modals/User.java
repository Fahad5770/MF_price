package com.mf.modals;

import java.util.LinkedHashMap;

public class User {
	private int id;
	private int is_active;
	private String password;
	private String name;
	private String designation;
	private String department;
	private String city;

	public User() {
	}

	public User(int id, int is_active, String password, String name, String designation, String department,
			String city) {
		super();
		this.id = id;
		this.is_active = is_active;
		this.password = password;
		this.name = name;
		this.designation = designation;
		this.department = department;
		this.city = city;
	}

	public User(int id, String name, String designation, String department, String city) {
		this.id = id;
		this.name = name;
		this.designation = designation;
		this.department = department;
		this.city = city;
	}

	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> user = new LinkedHashMap<String, Object>();
		user.put("user_id", this.id);
		user.put("user_name", this.name);
		user.put("designatiom", this.designation);
		user.put("department", this.department);
		user.put("city", this.city);
		return user;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesignation() {
		return this.designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getIs_active() {
		return is_active;
	}

	public void setIs_active(int is_active) {
		this.is_active = is_active;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
