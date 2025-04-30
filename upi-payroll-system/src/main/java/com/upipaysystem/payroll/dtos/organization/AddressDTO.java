package com.upipaysystem.payroll.dtos.organization;

import com.upipaysystem.payroll.model.Address;
import jakarta.validation.constraints.NotBlank;

public class AddressDTO {
    @NotBlank(message = "Address line one is required")
    private String addressLineOne;

    @NotBlank(message = "Address line two is required")
    private String addressLineTwo;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Pin code is required")
    private String pinCode;

    public AddressDTO() {
    }

    public AddressDTO(Address address) {
        this.addressLineOne = address.getAddressLineOne();
        this.addressLineTwo = address.getAddressLineTwo();
        this.city = address.getCity();
        this.pinCode = address.getPinCode();
        this.state = address.getState();
    }


    public @NotBlank(message = "Address line one is required") String getAddressLineOne() {
        return addressLineOne;
    }

    public void setAddressLineOne(@NotBlank(message = "Address line one is required") String addressLineOne) {
        this.addressLineOne = addressLineOne;
    }

    public @NotBlank(message = "Address line two is required") String getAddressLineTwo() {
        return addressLineTwo;
    }

    public void setAddressLineTwo(@NotBlank(message = "Address line two is required") String addressLineTwo) {
        this.addressLineTwo = addressLineTwo;
    }

    public @NotBlank(message = "City is required") String getCity() {
        return city;
    }

    public void setCity(@NotBlank(message = "City is required") String city) {
        this.city = city;
    }

    public @NotBlank(message = "State is required") String getState() {
        return state;
    }

    public void setState(@NotBlank(message = "State is required") String state) {
        this.state = state;
    }

    public @NotBlank(message = "Pin code is required") String getPinCode() {
        return pinCode;
    }

    public void setPinCode(@NotBlank(message = "Pin code is required") String pinCode) {
        this.pinCode = pinCode;
    }
}
