package es.protobuilder.beans;

import es.protobuilder.model.Usuario;
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
    private UsuarioBean usuarioBean;

    @Inject
    private UsuarioMockBean usuarioMockBean;

    public String login() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // 1. Validar campos vacíos en el lado del servidor
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Por favor, complete todos los campos."));
            return null;
        }

        Usuario usuario = usuarioMockBean.autenticar(this.username.trim(), this.password);
        if (usuario == null) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario o contraseña incorrectos."));
            return null;
        }

        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.getSessionMap().put("usuarioLogueado", usuario);
        usuarioBean.iniciarSesion(usuario.getUsername(), null);

        facesContext.addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Bienvenido " + usuario.getUsername()));
        externalContext.getFlash().setKeepMessages(true);
        return "dashboard?faces-redirect=true";
    }


    public boolean SesionIniciada() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getExternalContext().getSessionMap().get("usuarioLogueado") != null;
    }

    // 2. Redirige al Dashboard si el usuario intenta entrar al Login teniendo la sesión abierta
    public void comprobarSesionAlEntrar() {
        if (SesionIniciada()) {
            try {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.getExternalContext().redirect("dashboard.xhtml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void UsuarioLogueado() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Usuario usuario = (Usuario) facesContext.getExternalContext().getSessionMap().get("usuarioLogueado");
        if (usuario != null) {
            this.username = usuario.getUsername();
        }
    }

    // Método para la redirección a la vista de Registro mediante regla de navegación de JSF
    public String irARegistro() {
        return "registro?faces-redirect=true";
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