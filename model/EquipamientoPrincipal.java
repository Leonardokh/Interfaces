package es.protobuilder.model;

import jakarta.persistence.*;
import java.io.Serializable;

// Entidad que representa el arma principal (W-Engine en ZZZ o Arma en Endfield)
@Entity
@Table(name = "equipamientos_principales")
public class EquipamientoPrincipal implements Serializable {

    private static final long serialVersionUID = 1L;

    // Identificador único (Clave Primaria)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación Uno a Uno con la Build a la que pertenece este equipamiento
    @OneToOne
    @JoinColumn(name = "personaje_build_id", nullable = false)
    private PersonajeBuild personajeBuild;

    // ID del objeto entregado por la API
    @Column(name = "item_id", nullable = false)
    private String itemId;

    // Nivel del arma (ej. 1-60)
    @Column(name = "level")
    private Integer level;

    // Nivel de ascensión o refinamiento del arma
    @Column(name = "promotion_level")
    private Integer promotionLevel;

    // Rareza (Rango B, A, S o Estrellas)
    @Column(name = "rarity")
    private String rarity;

    // Nivel de modificación / refinamiento (Phase 1-5 en ZZZ)
    @Column(name = "refinement_phase")
    private Integer refinementPhase;

    // Valor del atributo base/principal
    @Column(name = "main_stat_value")
    private Double mainStatValue;

    // Nombre de la sub-estadística del arma (ej. "CRIT Rate", "Energy Regen")
    @Column(name = "sub_stat_type")
    private String subStatType;

    // Valor numérico de la sub-estadística del arma
    @Column(name = "sub_stat_value")
    private Double subStatValue;

    // Constructor vacío exigido por JPA
    public EquipamientoPrincipal() {}

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

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public Integer getRefinementPhase() {
        return refinementPhase;
    }

    public void setRefinementPhase(Integer refinementPhase) {
        this.refinementPhase = refinementPhase;
    }

    public Double getMainStatValue() {
        return mainStatValue;
    }

    public void setMainStatValue(Double mainStatValue) {
        this.mainStatValue = mainStatValue;
    }

    public String getSubStatType() {
        return subStatType;
    }

    public void setSubStatType(String subStatType) {
        this.subStatType = subStatType;
    }

    public Double getSubStatValue() {
        return subStatValue;
    }

    public void setSubStatValue(Double subStatValue) {
        this.subStatValue = subStatValue;
    }
}