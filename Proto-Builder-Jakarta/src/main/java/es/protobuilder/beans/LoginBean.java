package es.protobuilder.beans;

import es.protobuilder.model.Usuario;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import java.io.Serializable;

@Named("loginBean")
@RequestScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // Unidad de persistencia declarada en tu persistence.xml
    private static EntityManagerFactory emf;

    private String username;
    private String password;

    public LoginBean() {
    }

    // Método para obtener el EMF de forma segura (Singleton)
    private synchronized EntityManagerFactory getEmf() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory("ProtoBuilderPU");
        }
        return emf;
    }
    @Inject
    private UsuarioBean usuarioBean;

    public String login() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // 1. Validar campos vacíos en el lado del servidor
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Por favor, complete todos los campos."));
            return null;
        }

        EntityManager em = null;

        try {
            em = getEmf().createEntityManager();

            // 2. Consultar el usuario en la base de datos por su username
            Usuario usuario = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.username = :user", Usuario.class)
                    .setParameter("user", this.username.trim())
                    .getSingleResult();

            // 3. Validar la contraseña enviada contra la BD
            if (usuario.getPassword().equals(this.password)) {

                ExternalContext externalContext = facesContext.getExternalContext();

                // 4. Guardar el usuario en la sesión para mantener la autenticación
                externalContext.getSessionMap().put("usuarioLogueado", usuario);
                usuarioBean.iniciarSesion(usuario.getUsername(), null);

                // PRIMERO añadimos el mensaje
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Bienvenido " + usuario.getUsername()));

                // DESPUÉS mantenemos el mensaje tras la redirección (Flash Scope)
                externalContext.getFlash().setKeepMessages(true);

                return "dashboard?faces-redirect=true";

            } else {
                // Contraseña incorrecta
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario o contraseña incorrectos."));
                return null;
            }

        } catch (NoResultException e) {
            // El usuario no existe en la BD
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario o contraseña incorrectos."));
            return null;

        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error del sistema", "Ocurrió un problema: " + e.getMessage()));
            return null;

        } finally {
            // Garantizar el cierre del EntityManager
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
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
        return "Registro?faces-redirect=true";
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