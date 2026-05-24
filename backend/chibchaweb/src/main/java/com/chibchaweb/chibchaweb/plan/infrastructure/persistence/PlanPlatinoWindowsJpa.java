package com.chibchaweb.chibchaweb.plan.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "plan_platino_windows")
@PrimaryKeyJoinColumn(name = "plan_id")
public class PlanPlatinoWindowsJpa extends PlanPlatinoJpa {

    @Column(name = "sql_server_incluido")
    private boolean sqlServerIncluido;

    @Column(name = "iis_version")
    private String iisVersion;

    protected PlanPlatinoWindowsJpa() {
    }

    public boolean isSqlServerIncluido() {
        return sqlServerIncluido;
    }

    public void setSqlServerIncluido(boolean sqlServerIncluido) {
        this.sqlServerIncluido = sqlServerIncluido;
    }

    public String getIisVersion() {
        return iisVersion;
    }

    public void setIisVersion(String iisVersion) {
        this.iisVersion = iisVersion;
    }
}
