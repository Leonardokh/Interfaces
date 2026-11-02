package es.protobuilder.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Entidad que representa una pieza de equipamiento (Drive Disc en ZZZ o Armadura en Endfield)
@Entity
@Table(name = "piezas_equipamiento")
public class PiezaEquipamiento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación Muchos a Uno con la build a la que pertenece
    @ManyToOne
    @JoinColumn(name = "personaje_build_id", nullable = false)
    private PersonajeBuild personajeBuild;

    // ID del ítem devuelto por la API
    @Column(name = "item_id")
    private String itemId;

    // Posición o casilla (ej. "Slot 1", "Slot 2", "Casco")
    @Column(name = "slot_position")
    private String slotPosition;

    // Nombre del set al que pertenece la pieza
    @Column(name = "set_name")
    private String setName;

    // Nivel de mejora (+0 a +15)
    @Column(name = "level")
    private Integer level;

    // Rareza (B, A, S)
    @Column(name = "rarity")
    private String rarity;

    // Atributo principal (ej. "ATK%", "CRIT Rate")
    @Column(name = "main_stat_type")
    private String mainStatType;

    // Valor numérico del atributo principal
    @Column(name = "main_stat_value")
    private Double mainStatValue;

    // Relación Uno a Muchos con las sub-estadísticas de esta pieza
    @OneToMany(mappedBy = "piezaEquipamiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubstatPieza> subStats = new ArrayList<>();

    // Constructor vacío exigido por JPA
    public PiezaEquipamiento() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonajeBuild getPersonajeBuild() {
        return personajeBuild;
    }

    public void setPersonajeBuild(PersonajeBuild personajeBuild) {
        this.personajeBuild = personajeBuild;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getSlotPosition() {
        return slotPosition;
    }

    public void setSlotPosition(String slotPosition) {
        this.slotPosition = slotPosition;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getMainStatType() {
        return mainStatType;
    }

    public void setMainStatType(String mainStatType) {
        this.mainStatType = mainStatType;
    }

    public Double getMainStatValue() {
        return mainStatValue;
    }

    public void setMainStatValue(Double mainStatValue) {
        this.mainStatValue = mainStatValue;
    }

    public List<SubstatPieza> getSubStats() {
        return subStats;
    }

    public void setSubStats(List<SubstatPieza> subStats) {
        this.subStats = subStats;
    }
}