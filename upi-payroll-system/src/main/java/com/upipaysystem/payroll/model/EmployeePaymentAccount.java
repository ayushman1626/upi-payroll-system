package com.upipaysystem.payroll.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "employee_payment_account",
        uniqueConstraints = @UniqueConstraint(name = "uq_epa_user_channel",
                columnNames = {"user_id", "account_type"}))
public class EmployeePaymentAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    @Column(name = "account_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;   // e.g. "UPI" or "BANK"

    @NotNull
    @Column(name = "is_verified", nullable = false)
    private Boolean verified = false;

    @Column
    private String vpa;           // if accountType == "UPI"

    @OneToOne(mappedBy = "paymentAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RazorpayDetails razorpayDetails;



    // Constructors, getters/setters
    public EmployeePaymentAccount() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public @NotBlank AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(@NotBlank AccountType accountType) {
        this.accountType = accountType;
    }

    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) { this.verified = verified; }

    public String getVpa() { return vpa; }
    public void setVpa(String vpa) { this.vpa = vpa; }

    public enum AccountType{
        UPI,BANK
    }

}
