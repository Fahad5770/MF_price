package com.mf.dao;

import java.util.LinkedHashMap;

public class NoOrderReportResponse {

    private String outetName;
    private String createdOn;
    private String user;
    private String mobile_order_no;
    private String noOrderReasonType;

    public NoOrderReportResponse() {
    }

    public NoOrderReportResponse(String outetName, String createdOn, String user, String mobile_order_no, String noOrderReasonType) {
        this.outetName = outetName;
        this.createdOn = createdOn;
        this.user = user;
        this.mobile_order_no = mobile_order_no;
        this.noOrderReasonType = noOrderReasonType;
    }

    // Getters and setters
    public String getOutetName() {
        return outetName;
    }

    public void setOutetName(String outetName) {
        this.outetName = outetName;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMobile_order_no() {
        return mobile_order_no;
    }

    public void setMobile_order_no(String mobile_order_no) {
        this.mobile_order_no = mobile_order_no;
    }

    public String getNoOrderReasonType() {
        return noOrderReasonType;
    }

    public void setNoOrderReasonType(String noOrderReasonType) {
        this.noOrderReasonType = noOrderReasonType;
    }

    @SuppressWarnings("unchecked")
    public LinkedHashMap<String, Object> getIntoJson() {
        LinkedHashMap<String, Object> dataArray = new LinkedHashMap<String, Object>();
        dataArray.put("outetName", this.outetName);
        dataArray.put("createdOn", this.createdOn);
        dataArray.put("user", this.user);
        dataArray.put("mobile_order_no", this.mobile_order_no);
        dataArray.put("noOrderReasonType", this.noOrderReasonType);
        return dataArray;
    }
}
