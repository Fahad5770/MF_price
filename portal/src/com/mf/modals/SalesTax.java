package com.mf.modals;

import org.json.simple.JSONObject;

public class SalesTax {
	private double filer_register;
	private double filer_unregister;
	private double nonfiler_register;
	private double nonfiler_unregister;

	public SalesTax() {
		super();
	}

	public SalesTax(double filer_register, double filer_unregister, double nonfiler_register,
			double nonfiler_unregister) {
		super();
		this.filer_register = filer_register;
		this.filer_unregister = filer_unregister;
		this.nonfiler_register = nonfiler_register;
		this.nonfiler_unregister = nonfiler_unregister;
	}

	public double getFiler_register() {
		return filer_register;
	}

	public void setFiler_register(double filer_register) {
		this.filer_register = filer_register;
	}

	public double getFiler_unregister() {
		return filer_unregister;
	}

	public void setFiler_unregister(double filer_unregister) {
		this.filer_unregister = filer_unregister;
	}

	public double getNonfiler_register() {
		return nonfiler_register;
	}

	public void setNonfiler_register(double nonfiler_register) {
		this.nonfiler_register = nonfiler_register;
	}

	public double getNonfiler_unregister() {
		return nonfiler_unregister;
	}

	public void setNonfiler_unregister(double nonfiler_unregister) {
		this.nonfiler_unregister = nonfiler_unregister;
	}

	public JSONObject getIntoJson() {
		JSONObject salestax = new JSONObject();

		salestax.put("filer_register", this.filer_register);
		salestax.put("filer_unregister", this.filer_unregister);
		salestax.put("nonfiler_register", this.nonfiler_register);
		salestax.put("nonfiler_unregister", this.nonfiler_unregister);

		return salestax;
	}

}
