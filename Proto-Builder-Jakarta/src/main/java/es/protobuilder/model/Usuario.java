package es.protobuilder.model;

import java.io.Serializable;
import java.time.LocalDateTime;

// Usuario de prueba mantenido en memoria por UsuarioMockBean.
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    // Nombre de usuario en la aplicación ProtoBuilder
    private String username;

    // Correo electrónico
    private String email;

    private String password;

    // UID del jugador en Zenless Zone Zero
    private String uidZzz;

    // UID del jugador en Arknights: Endfield
    private String uidEndfield;

    private LocalDateTime fechaRegistro;

    public Usuario() {
        this.fechaRegistro = LocalDateTime.now();
    }

    // Getters y Setters
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

    public String getUidZzz() {
        return uidZzz;
    }

    public void setUidZzz(String uidZzz) {
        this.uidZzz = uidZzz;
    }

    public String getUidEndfield() {
        return uidEndfield;
    }

    public void setUidEndfield(String uidEndfield) {
        this.uidEndfield = uidEndfield;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}