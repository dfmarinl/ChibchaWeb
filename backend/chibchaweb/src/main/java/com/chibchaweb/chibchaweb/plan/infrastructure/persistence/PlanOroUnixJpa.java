package com.chibchaweb.chibchaweb.plan.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ORO_UNIX")
public class PlanOroUnixJpa extends PlanOroJpa {

    @Column(name = "python_incluido")
    private boolean pythonIncluido;

    protected PlanOroUnixJpa() {
    }

    public boolean isPythonIncluido() {
        return pythonIncluido;
    }

    public void setPythonIncluido(boolean pythonIncluido) {
        this.pythonIncluido = pythonIncluido;
    }
}
