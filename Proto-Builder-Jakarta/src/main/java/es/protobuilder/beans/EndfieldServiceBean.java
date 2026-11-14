
package es.protobuilder.beans;

import es.protobuilder.endfield.Build;
import es.protobuilder.endfield.Gear;
import es.protobuilder.endfield.Operator;
import es.protobuilder.endfield.Weapon;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named("endfieldService")
@SessionScoped
public class EndfieldServiceBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Operator> operators;
    private Map<String, Operator> operatorsById;
    private List<Weapon> weapons;
    private Map<String, Weapon> weaponsById;
    private List<Gear> gears;
    private Map<String, Gear> gearsById;
    private List<Build> builds;

    private Long buildId = 1L;

    @PostConstruct
    public void init() {

        // OPERATORS
        operators = new ArrayList<>();
        operatorsById = new HashMap<>();

        Operator endministrator = new Operator(
            "endministrator",
            "Endministrator",
            "Guard",
            "Physical",
            "The protagonist of Endfield..."
        );

        agregarOperador(endministrator);

        agregarOperador(new Operator(
            "perlica",
            "Perlica", "Caster", "Electric",
            "Supervisor and official spokesperson..."
        ));

        agregarOperador(new Operator(
            "xaihi",
            "Xaihi", "Supporter", "Cryo",
            "A researcher dispatched from Laterano..."
        ));

        // WEAPONS
        weapons = new ArrayList<>();
        weaponsById = new HashMap<>();

        Weapon complexedIndustry = new Weapon(
            "complexed-industry",
            "Complexed Industry",
            "Sword",
            "An all-new product from the Northmarch Support Hub..."
        );

        agregarArma(complexedIndustry);

        agregarArma(new Weapon(
            "khravengger",
            "Khravengger",
            "Great Sword",
            "A strange weapon released by Witching Hour..."
        ));

        agregarArma(new Weapon(
            "blessing-of-lustrous-carmine",
            "Blessing of Lustrous Carmine",
            "Polearm",
            "One of the polearms produced by the Witching Hour..."
        ));

        // GEARS DISPONIBLES
        gears = new ArrayList<>();
        gearsById = new HashMap<>();

        agregarGear(new Gear("swordmancer-heavy-armor", "Swordmancer", "Heavy Armor",
            "AGILITY", "STRENGTH"));

        agregarGear(new Gear("swordmancer-tac-gloves", "Swordmancer", "TAC Gloves",
            "STRENGTH", "WILL"));

        agregarGear(new Gear("swordmancer-tac-gauntlets", "Swordmancer", "TAC Gauntlets",
            "AGILITY", "STRENGTH"));

        agregarGear(new Gear("swordmancer-flint", "Swordmancer", "Flint",
            "AGILITY", "STRENGTH"));

        // BUILD MOCK
        List<Gear> gearsBuild = new ArrayList<>();

        for (Gear gear : gears) {
            gearsBuild.add(new Gear(
                gear.getId(),
                gear.getSet(),
                gear.getName(),
                gear.getStat1(),
                gear.getStat2()
            ));
        }

        builds = new ArrayList<>();

        Build build1 = new Build(
            buildId++,
            endministrator,
            complexedIndustry,
            gearsBuild
        );

        builds.add(build1);
    }

    // BUSCAR BUILD
    public Build buscarBuild(Long id) {

        return builds.stream()
            .filter(b -> b.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    private void agregarOperador(Operator operator) {
        operators.add(operator);
        operatorsById.put(operator.getId(), operator);
    }

    public Operator buscarOperador(String id) {
        return operatorsById.get(id);
    }

    private void agregarArma(Weapon weapon) {
        weapons.add(weapon);
        weaponsById.put(weapon.getId(), weapon);
    }

    public Weapon buscarArma(String id) {
        return weaponsById.get(id);
    }

    private void agregarGear(Gear gear) {
        gears.add(gear);
        gearsById.put(gear.getId(), gear);
    }

    public Gear buscarGear(String id) {
        return gearsById.get(id);
    }

    // GUARDAR BUILD
    public void guardarBuild(Build build) {

        if (build.getId() == null) {

            build.setId(buildId++);
            builds.add(build);

        } else {

            for (int i = 0; i < builds.size(); i++) {

                if (builds.get(i).getId().equals(build.getId())) {
                    builds.set(i, build);
                    return;
                }
            }

            builds.add(build);
        }
    }

    // ELIMINAR BUILD
    public void eliminarBuild(Long id) {

        builds.removeIf(b -> b.getId().equals(id));
    }

    // GETTERS

    public List<Operator> getOperators() {
        return operators;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public List<Gear> getGears() {
        return gears;
    }

    public List<Build> getBuilds() {
        return builds;
    }
}
