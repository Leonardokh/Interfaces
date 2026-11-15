
package es.protobuilder.beans;

import es.protobuilder.endfield.Build;
import es.protobuilder.endfield.Gear;
import es.protobuilder.endfield.Operator;
import es.protobuilder.endfield.Weapon;
import es.protobuilder.model.Usuario;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    private Map<String, List<Build>> buildsByUsername;
    private long nextBuildId = -2L;

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

        buildsByUsername = new HashMap<>();
        List<Build> demoBuilds = new ArrayList<>();
        Build buildDemo = new Build();
        buildDemo.setId(-1L);
        buildDemo.setOperator(buscarOperador("endministrator"));
        buildDemo.setWeapon(buscarArma("complexed-industry"));
        buildDemo.setGear(List.of(
            buscarGear("swordmancer-heavy-armor"),
            buscarGear("swordmancer-tac-gloves"),
            buscarGear("swordmancer-tac-gauntlets"),
            buscarGear("swordmancer-flint")
        ));
        demoBuilds.add(buildDemo);
        buildsByUsername.put(normalizarUsuario("demo"), demoBuilds);
    }

    private String getUsuarioAutenticadoNombre() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
            return null;
        }

        Object usuarioSesion = facesContext.getExternalContext()
                .getSessionMap().get("usuarioLogueado");
        return usuarioSesion instanceof Usuario usuario ? usuario.getUsername() : null;
    }

    private String normalizarUsuario(String username) {
        return username.trim().toLowerCase(Locale.ROOT);
    }

    private List<Build> buildsDelUsuario(String username) {
        return buildsByUsername.computeIfAbsent(normalizarUsuario(username), ignored -> new ArrayList<>());
    }

    public synchronized List<Build> getBuilds() {
        String username = getUsuarioAutenticadoNombre();
        if (username == null || username.isBlank()) {
            return List.of();
        }
        return new ArrayList<>(buildsByUsername.getOrDefault(normalizarUsuario(username), List.of()));
    }

    // BUSCAR BUILD PROPIA
    public synchronized Build buscarBuild(Long id) {
        String username = getUsuarioAutenticadoNombre();
        if (id == null || username == null || username.isBlank()) {
            return null;
        }
        return buildsByUsername.getOrDefault(normalizarUsuario(username), List.of()).stream()
                .filter(build -> id.equals(build.getId()))
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

    public synchronized void guardarBuild(Build build) {
        String username = getUsuarioAutenticadoNombre();
        if (username == null || username.isBlank()) {
            throw new IllegalStateException("Debes iniciar sesión para guardar una build.");
        }
        if (build == null || build.getOperator() == null || build.getWeapon() == null || build.getGear() == null) {
            throw new IllegalArgumentException("La build debe incluir personaje, arma y equipamiento.");
        }

        List<Build> builds = buildsDelUsuario(username);
        if (build.getId() == null) {
            build.setId(nextBuildId--);
            builds.add(build);
            return;
        }

        for (int i = 0; i < builds.size(); i++) {
            if (build.getId().equals(builds.get(i).getId())) {
                builds.set(i, build);
                return;
            }
        }
        throw new IllegalStateException("No se encontró una build propia para actualizar.");
    }

    public synchronized void eliminarBuild(Long id) {
        String username = getUsuarioAutenticadoNombre();
        if (username == null || username.isBlank()) {
            throw new IllegalStateException("Debes iniciar sesión para eliminar una build.");
        }
        List<Build> builds = buildsByUsername.get(normalizarUsuario(username));
        if (builds == null || !builds.removeIf(build -> id != null && id.equals(build.getId()))) {
            throw new IllegalStateException("No se encontró una build propia para eliminar.");
        }
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

}
