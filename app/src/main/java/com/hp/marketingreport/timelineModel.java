package com.hp.marketingreport;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public class timelineModel {
    String storeName, storeMobNo, empName, salesmanMobNo, salesmanName;
    Timestamp date;
    GeoPoint location;

    public timelineModel() {

    }

    public timelineModel(String storeName, String storeMobNo, String empName, GeoPoint location, String salesmanMobNo, String salesmanName, Timestamp date) {
        this.storeName = storeName;
        this.storeMobNo = storeMobNo;
        this.empName = empName;
        this.location = location;
        this.salesmanMobNo = salesmanMobNo;
        this.salesmanName = salesmanName;
        this.date = date;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreMobNo() {
        return storeMobNo;
    }

    public void setStoreMobNo(String storeMobNo) {
        this.storeMobNo = storeMobNo;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getSalesmanMobNo() {
        return salesmanMobNo;
    }

    public void setSalesmanMobNo(String salesmanMobNo) {
        this.salesmanMobNo = salesmanMobNo;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}
