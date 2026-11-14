
package es.protobuilder.beans;

import es.protobuilder.endfield.Build;
import es.protobuilder.endfield.Gear;
import es.protobuilder.endfield.Operator;
import es.protobuilder.endfield.Weapon;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.util.ArrayList;
import java.util.List;

@Named("endfieldService")
@SessionScoped
public class EndfieldServiceBean {

    private List<Operator> operators;
    private List<Weapon> weapons;
    private List<Gear> gears;
    private List<Build> builds;

    private Long buildId = 1L;

    @PostConstruct
    public void init() {

        // OPERATORS
        operators = new ArrayList<>();

        Operator endministrator = new Operator(
            "Endministrator",
            "Guard",
            "Physical",
            "The protagonist of Endfield..."
        );

        operators.add(endministrator);

        operators.add(new Operator(
            "Perlica", "Caster", "Electric",
            "Supervisor and official spokesperson..."
        ));

        operators.add(new Operator(
            "Xaihi", "Supporter", "Cryo",
            "A researcher dispatched from Laterano..."
        ));

        // WEAPONS
        weapons = new ArrayList<>();

        Weapon complexedIndustry = new Weapon(
            "Complexed Industry",
            "Sword",
            "An all-new product from the Northmarch Support Hub..."
        );

        weapons.add(complexedIndustry);

        weapons.add(new Weapon(
            "Khravengger",
            "Great Sword",
            "A strange weapon released by Witching Hour..."
        ));

        weapons.add(new Weapon(
            "Blessing of Lustrous Carmine",
            "Polearm",
            "One of the polearms produced by the Witching Hour..."
        ));

        // GEARS DISPONIBLES
        gears = new ArrayList<>();

        gears.add(new Gear("Swordmancer", "Heavy Armor",
            "AGILITY", "STRENGTH"));

        gears.add(new Gear("Swordmancer", "TAC Gloves",
            "STRENGTH", "WILL"));

        gears.add(new Gear("Swordmancer", "TAC Gauntlets",
            "AGILITY", "STRENGTH"));

        gears.add(new Gear("Swordmancer", "Flint",
            "AGILITY", "STRENGTH"));

        // BUILD MOCK
        List<Gear> gearsBuild = new ArrayList<>();

        for (Gear gear : gears) {
            gearsBuild.add(new Gear(
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
