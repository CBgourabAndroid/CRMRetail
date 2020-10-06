
package com.crmretail.modelClass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpenceListResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("expenses")
    @Expose
    private List<Expense> expenses = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

}
