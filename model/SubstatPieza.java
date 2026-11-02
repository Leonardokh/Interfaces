package es.protobuilder.model;

import jakarta.persistence.*;
import java.io.Serializable;

// Entidad que mapea cada una de las sub-estadísticas secundarias de un disco o pieza de armadura
@Entity
@Table(name = "substats_piezas")
public class SubstatPieza implements Serializable {

    private static final long serialVersionUID = 1L;

    // Identificador único de la sub-estadística (Clave Primaria)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación Muchos a Uno con la pieza de equipamiento a la que pertenece
    @ManyToOne
    @JoinColumn(name = "pieza_equipamiento_id", nullable = false)
    private PiezaEquipamiento piezaEquipamiento;

    // Nombre de la sub-estadística (ej. "PEN Ratio", "Impact", "ATK", "CRIT Rate")
    @Column(name = "stat_type", nullable = false)
    private String statType;

    // Valor numérico de la sub-estadística
    @Column(name = "stat_value", nullable = false)
    private Double statValue;

    // Cantidad de aumentos o mejoras ("rolls") que recibió esta sub-estadística al subir de nivel
    @Column(name = "rolls")
    private Integer rolls;

    // Constructor vacío exigido por JPA
    public SubstatPieza() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PiezaEquipamiento getPiezaEquipamiento() {
        return piezaEquipamiento;
    }

    public void setPiezaEquipamiento(PiezaEquipamiento piezaEquipamiento) {
        this.piezaEquipamiento = piezaEquipamiento;
    }

    public String getStatType() {
        return statType;
    }

    public void setStatType(String statType) {
        this.statType = statType;
    }

    public Double getStatValue() {
        return statValue;
    }

    public void setStatValue(Double statValue) {
        this.statValue = statValue;
    }

    public Integer getRolls() {
        return rolls;
    }

    public void setRolls(Integer rolls) {
        this.rolls = rolls;
    }
}