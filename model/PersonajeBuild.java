package es.protobuilder.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Entidad principal que representa la build de un personaje (ZZZ o Endfield)
@Entity
@Table(name = "personajes_builds")
public class PersonajeBuild implements Serializable {

    private static final long serialVersionUID = 1L;

    // Identificador único de la build (Clave Primaria)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación Muchos a Uno con el Usuario propietario de la build
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Identificador del juego al que pertenece la build ("ZZZ" o "ENDFIELD")
    @Column(name = "juego", nullable = false)
    private String juego;

    // ID o nombre único del personaje en la API (ej. "Ellen Joe", "Zhu Yuan")
    @Column(name = "character_id", nullable = false)
    private String characterId;

    // Nombre personalizado dado por el usuario en ProtoBuilder
    @Column(name = "nombre_custom_build")
    private String nombreCustomBuild;

    // Nivel del personaje (1-60 en ZZZ, etc.)
    @Column(name = "level")
    private Integer level;

    // Nivel de ascensión/promoción del personaje
    @Column(name = "promotion_level")
    private Integer promotionLevel;

    // Duplicados del personaje (Mindscape Cinema en ZZZ / Potencial en Endfield)
    @Column(name = "duplicate_level")
    private Integer duplicateLevel;

    // Cadena JSON con los niveles de talentos o habilidades del personaje
    @Column(name = "skill_levels_json", columnDefinition = "TEXT")
    private String skillLevelsJson;

    // Relación Uno a Uno con el arma equipada (W-Engine o Arma principal)
    @OneToOne(mappedBy = "personajeBuild", cascade = CascadeType.ALL, orphanRemoval = true)
    private EquipamientoPrincipal equipamientoPrincipal;

    // Relación Uno a Muchos con las piezas de equipamiento (Drive Discs o Armadura)
    @OneToMany(mappedBy = "personajeBuild", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PiezaEquipamiento> piezasEquipamiento = new ArrayList<>();

    // Relación Uno a Muchos con las estadísticas calculadas del personaje
    @OneToMany(mappedBy = "personajeBuild", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EstadisticaPersonaje> estadisticas = new ArrayList<>();

    // Constructor vacío exigido por JPA
    public PersonajeBuild() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getJuego() {
        return juego;
    }

    public void setJuego(String juego) {
        this.juego = juego;
    }

    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

    public String getNombreCustomBuild() {
        return nombreCustomBuild;
    }

    public void setNombreCustomBuild(String nombreCustomBuild) {
        this.nombreCustomBuild = nombreCustomBuild;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getPromotionLevel() {
        return promotionLevel;
    }

    public void setPromotionLevel(Integer promotionLevel) {
        this.promotionLevel = promotionLevel;
    }

    public Integer getDuplicateLevel() {
        return duplicateLevel;
    }

    public void setDuplicateLevel(Integer duplicateLevel) {
        this.duplicateLevel = duplicateLevel;
    }

    public String getSkillLevelsJson() {
        return skillLevelsJson;
    }

    public void setSkillLevelsJson(String skillLevelsJson) {
        this.skillLevelsJson = skillLevelsJson;
    }

    public EquipamientoPrincipal getEquipamientoPrincipal() {
        return equipamientoPrincipal;
    }

    public void setEquipamientoPrincipal(EquipamientoPrincipal equipamientoPrincipal) {
        this.equipamientoPrincipal = equipamientoPrincipal;
    }

    public List<PiezaEquipamiento> getPiezasEquipamiento() {
        return piezasEquipamiento;
    }

    public void setPiezasEquipamiento(List<PiezaEquipamiento> piezasEquipamiento) {
        this.piezasEquipamiento = piezasEquipamiento;
    }

    public List<EstadisticaPersonaje> getEstadisticas() {
        return estadisticas;
    }

    public void setEstadisticas(List<EstadisticaPersonaje> estadisticas) {
        this.estadisticas = estadisticas;
    }
}