package es.protobuilder.beans;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;

@Named("usuarioBean")
@SessionScoped
public class UsuarioBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private String fotoUrl;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public boolean isAutenticado() {
        return nombre != null && !nombre.isBlank();
    }

    // 3. Cierra la sesión activa
    public String cerrarSesion() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().invalidateSession();
        return "home?faces-redirect=true";
    }

    public void iniciarSesion(String nombre, String fotoUrl) {
        this.nombre = nombre;
        this.fotoUrl = fotoUrl;
    }

    public void iniciarSesionPrueba() {
    System.out.println("Hola estoy en la función");
    nombre = "Usuario de prueba";
    fotoUrl = null;
}
}
