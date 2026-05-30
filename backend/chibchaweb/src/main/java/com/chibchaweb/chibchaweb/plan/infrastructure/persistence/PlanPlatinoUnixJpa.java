package com.chibchaweb.chibchaweb.plan.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PLATINO_UNIX")
public class PlanPlatinoUnixJpa extends PlanPlatinoJpa {

    @Column(name = "mysql_incluido")
    private boolean mysqlIncluido;

    @Column(name = "php_version")
    private String phpVersion;

    protected PlanPlatinoUnixJpa() {
    }

    public boolean isMysqlIncluido() {
        return mysqlIncluido;
    }

    public void setMysqlIncluido(boolean mysqlIncluido) {
        this.mysqlIncluido = mysqlIncluido;
    }

    public String getPhpVersion() {
        return phpVersion;
    }

    public void setPhpVersion(String phpVersion) {
        this.phpVersion = phpVersion;
    }
}
