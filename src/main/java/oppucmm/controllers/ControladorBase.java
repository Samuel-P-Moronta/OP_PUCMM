package oppucmm.controllers;

import io.javalin.Javalin;

public abstract class ControladorBase {
    protected Javalin app;

    public ControladorBase(Javalin app) {
        this.app = app;
    }
    abstract public void aplicarRutas();

}
