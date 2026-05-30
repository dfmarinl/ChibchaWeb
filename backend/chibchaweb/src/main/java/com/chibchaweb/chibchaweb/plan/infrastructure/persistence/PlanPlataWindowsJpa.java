package com.chibchaweb.chibchaweb.plan.infrastructure.persistence;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PLATA_WINDOWS")
public class PlanPlataWindowsJpa extends PlanPlataJpa {

    protected PlanPlataWindowsJpa() {
    }
}
