package com.chibchaweb.chibchaweb.plan.domain;

public class UnixHostingFactory implements HostingPlanFactory {

    @Override
    public PlanOro crearPlanOro() {
        return new PlanOroUnix(null, "Oro Unix", 24.99, 10240, 1024, 5, true, 5);
    }

    @Override
    public PlanPlata crearPlanPlata() {
        return new PlanPlataUnix(null, "Plata Unix", 14.99, 5120, 512, 3, 3);
    }

    @Override
    public PlanPlatino crearPlanPlatino() {
        return new PlanPlatinoUnix(null, "Platino Unix", 39.99, 20480, 2048, 10, true, "8.2", 10);
    }
}
