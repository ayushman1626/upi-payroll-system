package com.upipaysystem.payroll.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_data")
public class Otp {

    public Otp() {
    }

    public Otp(String otp, String email, LocalDateTime expiration, Topic topic) {
        this.otp = otp;
        this.email = email;
        this.expiration = expiration;
        this.topic = topic;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String otp;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime expiration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Topic topic;

    public enum Topic {
        REGISTRATION, LOGIN, FORGET_PASSWORD
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "Otp{" +
                "id=" + id +
                ", otp='" + otp + '\'' +
                ", email='" + email + '\'' +
                ", expiration=" + expiration +
                ", topic=" + topic +
                '}';
    }
}


