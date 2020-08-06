
package com.crmretail.modelClass;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonalInfo {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("reporting")
    @Expose
    private String reporting;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("sp_designation")
    @Expose
    private String spDesignation;
    @SerializedName("doj")
    @Expose
    private String doj;
    @SerializedName("area")
    @Expose
    private String area;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReporting() {
        return reporting;
    }

    public void setReporting(String reporting) {
        this.reporting = reporting;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSpDesignation() {
        return spDesignation;
    }

    public void setSpDesignation(String spDesignation) {
        this.spDesignation = spDesignation;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }


}
