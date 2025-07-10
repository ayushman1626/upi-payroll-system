package com.upipaysystem.payroll.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "razorpay_details",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_rpd_pay_acc", columnNames = "pay_acc_id"),
                @UniqueConstraint(name = "uq_rpd_contact", columnNames = "contact_id"),
                @UniqueConstraint(name = "uq_rpd_fund_acc", columnNames = "fund_account_vap_id")
        })
public class RazorpayDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK to employee_payment_account.id
    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pay_acc_id", nullable = false)
    private EmployeePaymentAccount paymentAccount;

    @NotBlank
    @Column(name = "contact_id", nullable = false)
    private String contactId;

    @NotBlank
    @Column(name = "fund_account_vap_id", nullable = false)
    private String fundAccountVapId;

    // Constructors, getters/setters

    public RazorpayDetails() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EmployeePaymentAccount getPaymentAccount() { return paymentAccount; }
    public void setPaymentAccount(EmployeePaymentAccount paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public String getContactId() { return contactId; }
    public void setContactId(String contactId) { this.contactId = contactId; }

    public String getFundAccountVapId() { return fundAccountVapId; }
    public void setFundAccountVapId(String fundAccountVapId) {
        this.fundAccountVapId = fundAccountVapId;
    }
}
