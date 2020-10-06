
package com.crmretail.modelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Expense {

    @SerializedName("exp_for")
    @Expose
    private String expFor;
    @SerializedName("expenses_for")
    @Expose
    private String expensesFor;
    @SerializedName("st_km")
    @Expose
    private String stKm;
    @SerializedName("end_km")
    @Expose
    private String endKm;
    @SerializedName("st_from")
    @Expose
    private Object stFrom;
    @SerializedName("end_to")
    @Expose
    private Object endTo;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("exp_date")
    @Expose
    private String expDate;

    public String getExpFor() {
        return expFor;
    }

    public void setExpFor(String expFor) {
        this.expFor = expFor;
    }

    public String getExpensesFor() {
        return expensesFor;
    }

    public void setExpensesFor(String expensesFor) {
        this.expensesFor = expensesFor;
    }

    public String getStKm() {
        return stKm;
    }

    public void setStKm(String stKm) {
        this.stKm = stKm;
    }

    public String getEndKm() {
        return endKm;
    }

    public void setEndKm(String endKm) {
        this.endKm = endKm;
    }

    public Object getStFrom() {
        return stFrom;
    }

    public void setStFrom(Object stFrom) {
        this.stFrom = stFrom;
    }

    public Object getEndTo() {
        return endTo;
    }

    public void setEndTo(Object endTo) {
        this.endTo = endTo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

}
