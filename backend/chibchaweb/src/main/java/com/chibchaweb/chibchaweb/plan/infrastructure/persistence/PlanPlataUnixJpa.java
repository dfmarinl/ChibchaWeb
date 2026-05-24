package com.chibchaweb.chibchaweb.plan.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "plan_plata_unix")
@PrimaryKeyJoinColumn(name = "plan_id")
public class PlanPlataUnixJpa extends PlanPlataJpa {

    protected PlanPlataUnixJpa() {
    }
}
