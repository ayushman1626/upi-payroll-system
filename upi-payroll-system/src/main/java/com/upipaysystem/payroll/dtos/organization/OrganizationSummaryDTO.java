package com.upipaysystem.payroll.dtos.organization;

import com.upipaysystem.payroll.model.Organization;
import lombok.Data;

@Data
public class OrganizationSummaryDTO {
    private Long id;
    private String name;

    public OrganizationSummaryDTO(Organization org) {
        this.id = org.getId();
        this.name = org.getName();
    }

    public OrganizationSummaryDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
