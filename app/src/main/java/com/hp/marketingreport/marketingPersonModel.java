package com.hp.marketingreport;

import com.google.firebase.Timestamp;

public class marketingPersonModel {
    String name, emailId, mobileNo, verificationDoc, routeAssign;
    Timestamp dob;

    public marketingPersonModel() {

    }

    public marketingPersonModel(String name, String emailId, String mobileNo, String verificationDoc, String routeAssign, Timestamp dob) {
        this.name = name;
        this.emailId = emailId;
        this.mobileNo = mobileNo;
        this.verificationDoc = verificationDoc;
        this.routeAssign = routeAssign;
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getVerificationDoc() {
        return verificationDoc;
    }

    public void setVerificationDoc(String verificationDoc) {
        this.verificationDoc = verificationDoc;
    }

    public Timestamp getDob() {
        return dob;
    }

    public void setDob(Timestamp dob) {
        this.dob = dob;
    }

    public String getRouteAssign() {
        return routeAssign;
    }

    public void setRouteAssign(String routeAssign) {
        this.routeAssign = routeAssign;
    }
}
