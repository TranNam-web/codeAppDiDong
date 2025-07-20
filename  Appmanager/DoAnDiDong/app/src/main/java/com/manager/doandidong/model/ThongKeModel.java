package com.manager.doandidong.model;

import java.util.List;

public class ThongKeModel {
    boolean  success;
    String message;
    List<ThongKe> result ;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ThongKe> getResult() {
        return result;
    }

    public void setResult(List<ThongKe> result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
