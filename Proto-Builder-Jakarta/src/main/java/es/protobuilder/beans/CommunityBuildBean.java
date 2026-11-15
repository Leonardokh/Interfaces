package es.protobuilder.beans;

import es.protobuilder.endfield.Build;
import es.protobuilder.endfield.Gear;
import es.protobuilder.endfield.Operator;
import es.protobuilder.endfield.Weapon;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("communityBuildBean")
@ViewScoped
public class CommunityBuildBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String OPERATOR_CHART = """
            {"type":"bar","data":{"labels":["Endministrator (42%)","Perlica (35%)","Xaihi (23%)"],"datasets":[{"label":"Popularidad (%)","data":[42,35,23],"backgroundColor":["#f0ad4e","#4dabf7","#8b72e8"],"borderRadius":6}]}, "options":{"indexAxis":"y","maintainAspectRatio":false,"responsive":true,"plugins":{"legend":{"display":false}},"scales":{"x":{"min":0,"max":100}}}}
            """;
    private static final String WEAPON_CHART = """
            {"type":"bar","data":{"labels":[["Complexed Industry","(46%)"],"Khravengger (31%)",["Blessing of Lustrous","Carmine (23%)"]],"datasets":[{"label":"Popularidad (%)","data":[46,31,23],"backgroundColor":["#f0ad4e","#4dabf7","#8b72e8"],"borderRadius":6}]}, "options":{"indexAxis":"y","maintainAspectRatio":false,"responsive":true,"plugins":{"legend":{"display":false}},"scales":{"x":{"min":0,"max":100}}}}
            """;
    private static final String GEAR_CHART = """
            {"type":"bar","data":{"labels":[["Swordmancer","Heavy Armor (38%)"],["Swordmancer","TAC Gloves (27%)"],["Swordmancer","TAC Gauntlets (21%)"],["Swordmancer Flint (14%)"]],"datasets":[{"label":"Popularidad (%)","data":[38,27,21,14],"backgroundColor":["#f0ad4e","#4dabf7","#8b72e8","#53b987"],"borderRadius":6}]}, "options":{"indexAxis":"y","maintainAspectRatio":false,"responsive":true,"plugins":{"legend":{"display":false}},"scales":{"x":{"min":0,"max":100}}}}
            """;

    @Inject
    private EndfieldServiceBean endfieldService;

    private List<CommunityBuild> builds;

    @PostConstruct
    public void init() {
        builds = new ArrayList<>();
        builds.add(crearBuild(
                "Build de la semana",
                "LunaArc",
                "Una configuración equilibrada para sacar partido al daño físico.",
                "endministrator",
                "complexed-industry",
                "swordmancer-heavy-armor",
                "swordmancer-tac-gloves",
                "swordmancer-tac-gauntlets",
                "swordmancer-flint"));
        builds.add(crearBuild(
                "Build más creativa",
                "NubeNaranja",
                "Una combinación poco habitual centrada en apoyar al equipo.",
                "xaihi",
                "blessing-of-lustrous-carmine",
                "swordmancer-tac-gloves",
                "swordmancer-heavy-armor",
                "swordmancer-flint",
                "swordmancer-tac-gauntlets"));
        builds.add(crearBuild(
                "Build para Contingency Contract",
                "TalosTactician",
                "Una propuesta de supervivencia y consistencia para desafíos exigentes.",
                "perlica",
                "khravengger",
                "swordmancer-tac-gauntlets",
                "swordmancer-heavy-armor",
                "swordmancer-tac-gloves",
                "swordmancer-flint"));
    }

    private CommunityBuild crearBuild(String category, String author, String description,
            String operatorId, String weaponId, String... gearIds) {
        Operator operator = endfieldService.buscarOperador(operatorId);
        Weapon weapon = endfieldService.buscarArma(weaponId);
        List<Gear> gear = new ArrayList<>();
        for (String gearId : gearIds) {
            gear.add(endfieldService.buscarGear(gearId));
        }

        Build build = new Build();
        build.setOperator(operator);
        build.setWeapon(weapon);
        build.setGear(gear);
        return new CommunityBuild(category, author, description, build);
    }

    public List<CommunityBuild> getBuilds() {
        return builds;
    }

    public String getOperatorChart() {
        return OPERATOR_CHART;
    }

    public String getWeaponChart() {
        return WEAPON_CHART;
    }

    public String getGearChart() {
        return GEAR_CHART;
    }

    public static class CommunityBuild implements Serializable {

        private static final long serialVersionUID = 1L;

        private final String category;
        private final String author;
        private final String description;
        private final Build build;

        public CommunityBuild(String category, String author, String description, Build build) {
            this.category = category;
            this.author = author;
            this.description = description;
            this.build = build;
        }

        public String getCategory() {
            return category;
        }

        public String getAuthor() {
            return author;
        }

        public String getDescription() {
            return description;
        }

        public Build getBuild() {
            return build;
        }
    }
}
