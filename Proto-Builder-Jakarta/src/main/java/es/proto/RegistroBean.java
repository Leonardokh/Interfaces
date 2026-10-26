package es.proto;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("registroBean")
@RequestScoped
public class RegistroBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    public RegistroBean() {
    }

    // --- Getters y Setters ---

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}