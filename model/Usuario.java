package es.protobuilder.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

// Entidad JPA que representa la tabla de usuarios en la Base de Datos
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    // Identificador único (Clave Primaria)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre de usuario en la aplicación ProtoBuilder
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    // Correo electrónico
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // Contraseña encriptada
    @Column(name = "password", nullable = false)
    private String password;

    // UID del jugador en Zenless Zone Zero
    @Column(name = "uid_zzz")
    private String uidZzz;

    // UID del jugador en Arknights: Endfield
    @Column(name = "uid_endfield")
    private String uidEndfield;

    // Relación de Uno a Muchos con las builds guardadas por el usuario
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PersonajeBuild> builds;

    // Fecha en que se registró el usuario
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    // Constructor vacío exigido por JPA
    public Usuario() {
        this.fechaRegistro = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<PersonajeBuild> getBuilds() {
        return builds;
    }

    public void setBuilds(List<PersonajeBuild> builds) {
        this.builds = builds;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}