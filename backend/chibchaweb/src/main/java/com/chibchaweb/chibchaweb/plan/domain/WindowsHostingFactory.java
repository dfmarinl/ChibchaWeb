package com.chibchaweb.chibchaweb.plan.domain;

public class WindowsHostingFactory implements HostingPlanFactory {

    @Override
    public PlanOro crearPlanOro() {
        return new PlanOroWindows(null, "Oro Windows", 29.99, 10240, 1024, 5, "4.8", 5);
    }

    @Override
    public PlanPlata crearPlanPlata() {
        return new PlanPlataWindows(null, "Plata Windows", 19.99, 5120, 512, 3, 3);
    }

    @Override
    public PlanPlatino crearPlanPlatino() {
        return new PlanPlatinoWindows(null, "Platino Windows", 49.99, 20480, 2048, 10, true, "10.0", 10);
    }
}
