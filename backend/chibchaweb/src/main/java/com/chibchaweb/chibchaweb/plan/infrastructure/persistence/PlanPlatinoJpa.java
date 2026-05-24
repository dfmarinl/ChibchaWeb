package com.chibchaweb.chibchaweb.plan.infrastructure.persistence;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "plan_platino")
@PrimaryKeyJoinColumn(name = "plan_id")
@DiscriminatorValue("PLATINO")
public class PlanPlatinoJpa extends HostingPlanJpa {

    protected PlanPlatinoJpa() {
    }
}
