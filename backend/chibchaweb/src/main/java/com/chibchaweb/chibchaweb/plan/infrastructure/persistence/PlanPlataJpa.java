package com.chibchaweb.chibchaweb.plan.infrastructure.persistence;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "plan_plata")
@PrimaryKeyJoinColumn(name = "plan_id")
@DiscriminatorValue("PLATA")
public class PlanPlataJpa extends HostingPlanJpa {

    protected PlanPlataJpa() {
    }
}
