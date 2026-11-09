package es.protobuilder.beans;

import es.protobuilder.endfield.Artifact;
import es.protobuilder.endfield.Operator;
import es.protobuilder.endfield.Weapon;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.List;


//Este Bean simula una base de datos de Endfield
@Named("endfieldService")
@RequestScoped
public class EndfieldServiceBean {

    private List<Operator> operators;
    private List<Weapon> weapons;
    private List<Artifact> artifacts;

    @PostConstruct
    public void init() {
        // Datos mock simulando respuesta de API
        operators = new ArrayList<>();
        operators.add(new Operator("Perlica", "Clase: Administradora / Soporte","Eléctrico", "Figura central de Endfield Industries. Coordina las operaciones logísticas y de campo en Talos-II."));
        operators.add(new Operator("Endministrator", "Clase: Vanguardia / DPS", "Físico", "El protagonista y líder de campo de la iniciativa Endfield. Capaz de adaptarse a múltiples roles."));
        operators.add(new Operator("Xaihi", "Clase: Especialista / Sanadora", "Hielo",  "Experta en tecnología médica y de campo, vital para mantener al equipo operativo en zonas de alta peligrosidad."));

        weapons = new ArrayList<>();
        weapons.add(new Weapon("Espada de Aleación Industrial", "Tipo: Espada pesada", "Arma de cuerpo a cuerpo estándar modificada para resistir entornos corrosivos y minerales hostiles."));
        weapons.add(new Weapon("Rifle de Pulso Táctico", "Tipo: Arma a distancia", "Emite descargas de energía concentrada ideales para romper escudos energéticos de los Aggelos."));

        artifacts = new ArrayList<>();
        artifacts.add(new Artifact("Set de Integridad Estructural", "STRENGTH", "WILL"));
        artifacts.add(new Artifact("Núcleo de Sobrecarga Originium", "INTELLIGENCE", "AGILITY."));
    }

    // Getters
    public List<Operator> getOperators() { return operators; }
    public List<Weapon> getWeapons() { return weapons; }
    public List<Artifact> getArtifacts() { return artifacts; }
}