package es.protobuilder.beans;

import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;

@Named("vistaBean")
@ViewScoped
public class VistaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // Sección inicial por defecto
    private String seccion = "myBuilds";

    public void mostrarNuevaBuild() {
        this.seccion = "newBuild";
    }

    public void mostrarMisBuilds() {
        this.seccion = "myBuilds";
    }

    public void mostrarGlosario() {
        this.seccion = "glossary";
    }

    // Getters y Setters
    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }
}