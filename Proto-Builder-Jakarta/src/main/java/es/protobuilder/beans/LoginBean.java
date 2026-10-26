package es.protobuilder.beans;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("loginBean")
@RequestScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    @Inject
    private FacesContext facesContext;

    public LoginBean() {
    }

    public String login() {
        // Simulación de autenticación (Reemplazar por servicio con hashing BCrypt/Argon2)
        if ("admin".equals(username) && "1234".equals(password)) {
            
            // Mantiene el mensaje tras la redirección HTTP (Flash Scope)
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.getFlash().setKeepMessages(true);
            
            facesContext.addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Bienvenido " + username));
            
            return "/dashboard.xhtml?faces-redirect=true"; 

        } else {
            facesContext.addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario o contraseña incorrectos"));
            
            // Retorna null para mantener el estado de la vista y renderizar el error
            return null;
        }
    }

    // --- Getters y Setters ---

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username != null ? username.trim() : null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}