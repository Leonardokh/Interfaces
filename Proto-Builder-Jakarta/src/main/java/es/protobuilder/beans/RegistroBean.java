package es.protobuilder.beans;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("registroBean")
@RequestScoped
public class RegistroBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String email;
    private String password;
    private String confirmPassword;

    @Inject
    private UsuarioMockBean usuarioMockBean;

    public String registrarUsuario() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (password == null || !password.equals(confirmPassword)) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Las contraseñas no coinciden."));
            return null;
        }

        UsuarioMockBean.ResultadoRegistro resultado = usuarioMockBean.registrar(username, email, password);
        if (resultado != UsuarioMockBean.ResultadoRegistro.REGISTRADO) {
            String detalle = switch (resultado) {
                case NOMBRE_DUPLICADO -> "El nombre de usuario ya está registrado.";
                case CORREO_DUPLICADO -> "El correo electrónico ya está registrado.";
                case DATOS_INVALIDOS -> "Completa todos los campos del registro.";
                case REGISTRADO -> throw new IllegalStateException("Resultado de registro inesperado.");
            };
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", detalle));
            return null;
        }

        facesContext.addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro Exitoso",
                        "Usuario " + username.trim() + " registrado en memoria."));
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "home?faces-redirect=true";
    }

    // Método para volver a la página de inicio de sesión
    public String Volver() {
        return "home?faces-redirect=true";
    }

    // --- Getters y Setters ---

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}