package com.upipaysystem.payroll.dtos.auth;

import com.upipaysystem.payroll.model.Organization;
import com.upipaysystem.payroll.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Stream;


public class LoginResponse {

    private Map<String, Object> data;
    public LoginResponse(Map<String, Object> data, boolean status, String message){
      this.data = data;
      this.status = status;
      this.message = message;
    }
    private boolean status;
    private String message;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "data=" + data +
                ", status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
