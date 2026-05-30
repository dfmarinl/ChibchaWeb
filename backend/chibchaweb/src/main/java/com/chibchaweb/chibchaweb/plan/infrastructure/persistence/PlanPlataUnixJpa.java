package com.chibchaweb.chibchaweb.plan.infrastructure.persistence;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PLATA_UNIX")
public class PlanPlataUnixJpa extends PlanPlataJpa {

    protected PlanPlataUnixJpa() {
    }
}
