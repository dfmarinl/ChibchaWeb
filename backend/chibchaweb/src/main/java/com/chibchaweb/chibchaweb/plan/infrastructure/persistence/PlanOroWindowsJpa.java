package com.chibchaweb.chibchaweb.plan.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "plan_oro_windows")
@PrimaryKeyJoinColumn(name = "plan_id")
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
