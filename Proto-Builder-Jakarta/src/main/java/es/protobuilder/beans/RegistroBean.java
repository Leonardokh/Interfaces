package es.protobuilder.beans;

import es.protobuilder.model.Usuario;
// import es.protobuilder.service.EmailService; // Temporalmente comentado
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.io.Serializable;
// import java.util.UUID; // Temporalmente comentado

@Named("registroBean")
@RequestScoped
public class RegistroBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static EntityManagerFactory emf;

    private String username;
    private String email;
    private String password;
    private String confirmPassword;

    // private EmailService emailService = new EmailService(); // Comentado para
    // pruebas de BD directas

    private synchronized EntityManagerFactory getEmf() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory("ProtoBuilderPU");
        }
        return emf;
    }

    public String registrarUsuario() {
        EntityManager em = null;
        EntityTransaction tx = null;

        try {
            em = getEmf().createEntityManager();
            tx = em.getTransaction();

            // 1. Validar coincidencia de contraseñas
            if (!password.equals(confirmPassword)) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Las contraseñas no coinciden."));
                return null;
            }

            // 2. Comprobar si el username ya existe en MySQL
            Long countUsername = em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.username = :user", Long.class)
                    .setParameter("user", username)
                    .getSingleResult();

            if (countUsername > 0) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                                "El nombre de usuario ya está registrado."));
                return null;
            }

            // 3. Comprobar si el email ya existe en MySQL
            Long countEmail = em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.email = :email", Long.class)
                    .setParameter("email", email)
                    .getSingleResult();

            if (countEmail > 0) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                                "El correo electrónico ya está registrado."));
                return null;
            }

            // 4. Iniciar transacción e insertar el nuevo usuario en MySQL
            tx.begin();

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setUsername(this.username.trim());
            nuevoUsuario.setEmail(this.email.trim());
            nuevoUsuario.setPassword(this.password);

            em.persist(nuevoUsuario);
            tx.commit(); // Aquip persiste físicamente en la base de datos

            /*
             * 5. SECCIÓN DE EMAIL TEMPORALMENTE DESACTIVADA
             * String tokenConfirmacion = UUID.randomUUID().toString();
             * emailService.enviarCorreoConfirmacion(this.email, this.username,
             * tokenConfirmacion);
             */

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro Exitoso",
                            "Usuario " + username + " guardado correctamente en la base de datos."));

            // Redirige directamente al dashboard o home sin esperar verificación por correo
            return "dashboard?faces-redirect=true";

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en el Registro", e.getMessage()));
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
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