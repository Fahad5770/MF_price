package com.mf.modals;

import java.util.LinkedHashMap;

public class OBPriceList {

    private int id;
    private int product_id;
    private int package_id;
    private int brand_id;

    private String package_label;
    private String brand_label;

    private String unit_per_case;
    private String liquid_in_ml;

    private double raw_case_price;
    private double unit_price;
    
    // Constructor
    public OBPriceList(int id, int product_id, int package_id, int brand_id,
                       String package_label, String brand_label,
                       String unit_per_case, String liquid_in_ml,
                       double raw_case_price, double unit_price
                      ) {
        this.id = id;
        this.product_id = product_id;
        this.package_id = package_id;
        this.brand_id = brand_id;
        this.package_label = package_label;
        this.brand_label = brand_label;
        this.unit_per_case = unit_per_case;
        this.liquid_in_ml = liquid_in_ml;
        this.raw_case_price = raw_case_price;
        this.unit_price = unit_price;

    }

    // Default Constructor
    public OBPriceList() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProduct_id() { return product_id; }
    public void setProduct_id(int product_id) { this.product_id = product_id; }

    public int getPackage_id() { return package_id; }
    public void setPackage_id(int package_id) { this.package_id = package_id; }

    public int getBrand_id() { return brand_id; }
    public void setBrand_id(int brand_id) { this.brand_id = brand_id; }

    public String getPackage_label() { return package_label; }
    public void setPackage_label(String package_label) { this.package_label = package_label; }

    public String getBrand_label() { return brand_label; }
    public void setBrand_label(String brand_label) { this.brand_label = brand_label; }

    public String getUnit_per_case() { return unit_per_case; }
    public void setUnit_per_case(String unit_per_case) { this.unit_per_case = unit_per_case; }

    public String getLiquid_in_ml() { return liquid_in_ml; }
    public void setLiquid_in_ml(String liquid_in_ml) { this.liquid_in_ml = liquid_in_ml; }

    public double getRaw_case_price() { return raw_case_price; }
    public void setRaw_case_price(double raw_case_price) { this.raw_case_price = raw_case_price; }

    public double getUnit_price() { return unit_price; }
    public void setUnit_price(double unit_price) { this.unit_price = unit_price; }

    // Method to return JSON-like structure
    public LinkedHashMap<String, Object> getIntoJson() {
        LinkedHashMap<String, Object> priceDetails = new LinkedHashMap<>();

        priceDetails.put("id", this.id);
        priceDetails.put("ProductID", this.product_id);
        priceDetails.put("PackageID", this.package_id);
        priceDetails.put("BrandID", this.brand_id);
        priceDetails.put("PackageLabel", this.package_label);
        priceDetails.put("BrandLabel", this.brand_label);
        priceDetails.put("UnitPerCase", this.unit_per_case);
        priceDetails.put("LiquidInML", this.liquid_in_ml);
        priceDetails.put("RawCasePrice", this.raw_case_price);
        priceDetails.put("UnitPrice", this.unit_price);
      
        return priceDetails;
    }
}
