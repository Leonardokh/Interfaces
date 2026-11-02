package es.protobuilder.model;

import jakarta.persistence.*;
import java.io.Serializable;

// Entidad para almacenar las estadísticas numéricas finales del personaje (HP, ATK, Prob. CRIT, etc.)
@Entity
@Table(name = "estadisticas_personaje")
public class EstadisticaPersonaje implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación Muchos a Uno con el personaje al que pertenece este valor
    @ManyToOne
    @JoinColumn(name = "personaje_build_id", nullable = false)
    private PersonajeBuild personajeBuild;

    // Nombre de la estadística (ej. "ATK", "CRIT_RATE", "ANOMALY_MASTERY")
    @Column(name = "stat_name", nullable = false)
    private String statName;

    // Valor numérico de la estadística
    @Column(name = "stat_value", nullable = false)
    private Double statValue;

    // Constructor vacío exigido por JPA
    public EstadisticaPersonaje() {}

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

    public String getStatName() {
        return statName;
    }

    public void setStatName(String statName) {
        this.statName = statName;
    }

    public Double getStatValue() {
        return statValue;
    }

    public void setStatValue(Double statValue) {
        this.statValue = statValue;
    }
}