package com.chibchaweb.chibchaweb.plan.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ORO_WINDOWS")
public class PlanOroWindowsJpa extends PlanOroJpa {

    @Column(name = "asp_net_version")
    private String aspNetVersion;

    protected PlanOroWindowsJpa() {
    }

    public String getAspNetVersion() {
        return aspNetVersion;
    }

    public void setAspNetVersion(String aspNetVersion) {
        this.aspNetVersion = aspNetVersion;
    }
}
