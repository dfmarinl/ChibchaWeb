package com.chibchaweb.chibchaweb.plan.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "plan_plata_windows")
@PrimaryKeyJoinColumn(name = "plan_id")
public class PlanPlataWindowsJpa extends PlanPlataJpa {

    protected PlanPlataWindowsJpa() {
    }
}
