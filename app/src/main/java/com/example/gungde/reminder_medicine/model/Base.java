package com.example.gungde.reminder_medicine.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Base {

    @Expose
    @SerializedName("data")
    private String data;
    @Expose
    @SerializedName("status")
    private String status;

    @SerializedName("error")
    private Boolean error;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getError() {
        return error;
    }
}
