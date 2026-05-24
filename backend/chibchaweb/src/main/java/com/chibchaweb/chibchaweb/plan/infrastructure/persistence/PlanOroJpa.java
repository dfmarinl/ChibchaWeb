package com.chibchaweb.chibchaweb.plan.infrastructure.persistence;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "plan_oro")
@PrimaryKeyJoinColumn(name = "plan_id")
@DiscriminatorValue("ORO")
public class PlanOroJpa extends HostingPlanJpa {

    protected PlanOroJpa() {
    }
}
