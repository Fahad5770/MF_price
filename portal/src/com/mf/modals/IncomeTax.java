package com.mf.modals;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class IncomeTax {

	List<IncomeTaxDetail> income_tax_detail = new ArrayList<IncomeTaxDetail>();

	public List<IncomeTaxDetail> getIncome_tax_detail() {
		return income_tax_detail;
	}

	public void setIncome_tax_detail(List<IncomeTaxDetail> income_tax_detail) {
		this.income_tax_detail = income_tax_detail;
	}

	@SuppressWarnings("unchecked")
	public JSONArray getIntoJson() {

		JSONArray income_tax_array = new JSONArray();

		for (IncomeTaxDetail income_tax : this.income_tax_detail) {
			income_tax_array.add(income_tax.getIntoJson());
		}
		return income_tax_array;
	}

	public class IncomeTaxDetail {
		private int product_id;
		private double filer_register;
		private double filer_unregister;
		private double nonfiler_register;
		private double nonfiler_unregister;

		public IncomeTaxDetail() {
			super();
		}

		public IncomeTaxDetail(int product_id, double filer_register, double filer_unregister, double nonfiler_register,
				double nonfiler_unregister) {
			super();
			this.product_id = product_id;
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

		public int getProduct_id() {
			return product_id;
		}

		public void setProduct_id(int product_id) {
			this.product_id = product_id;
		}

		@SuppressWarnings("unchecked")
		public JSONObject getIntoJson() {
			JSONObject salestax = new JSONObject();

			salestax.put("product_id", this.product_id);
			salestax.put("filer_register", this.filer_register);
			salestax.put("filer_unregister", this.filer_unregister);
			salestax.put("nonfiler_register", this.nonfiler_register);
			salestax.put("nonfiler_unregister", this.nonfiler_unregister);

			return salestax;
		}
	}
}
