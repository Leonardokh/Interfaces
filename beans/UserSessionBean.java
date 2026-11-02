package es.protobuilder.beans;

import es.protobuilder.model.Usuario;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

// ManagedBean que mantiene la sesión activa del usuario en toda la aplicación
@Named("userSessionBean")
@SessionScoped
public class UserSessionBean implements Serializable {

    // Guarda los datos del usuario actualmente autenticado
    private Usuario usuarioLogueado;

    public UserSessionBean() {}

    // Retorna el usuario en sesión
    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    // Permite establecer el usuario al iniciar sesión
    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    // Comprueba si hay una sesión activa
    public boolean isLogged() {
        return usuarioLogueado != null;
    }
}