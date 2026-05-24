package com.chibchaweb.chibchaweb.plan.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "plan_oro_unix")
@PrimaryKeyJoinColumn(name = "plan_id")
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
