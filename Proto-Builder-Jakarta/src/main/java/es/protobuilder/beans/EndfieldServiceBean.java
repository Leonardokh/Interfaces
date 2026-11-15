
package es.protobuilder.beans;

import es.protobuilder.endfield.Build;
import es.protobuilder.endfield.Gear;
import es.protobuilder.endfield.Operator;
import es.protobuilder.endfield.Weapon;
import es.protobuilder.model.EquipamientoPrincipal;
import es.protobuilder.model.PersonajeBuild;
import es.protobuilder.model.PiezaEquipamiento;
import es.protobuilder.model.Usuario;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
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
    private List<Build> demoBuilds;
    private long nextDemoBuildId = -2L;

    private static EntityManagerFactory emf;

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

        demoBuilds = new ArrayList<>();
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
    }

    private synchronized EntityManagerFactory getEmf() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory("ProtoBuilderPU");
        }
        return emf;
    }

    private Long getUsuarioAutenticadoId() {
        Usuario usuario = getUsuarioAutenticado();
        return usuario == null ? null : usuario.getId();
    }

    private Usuario getUsuarioAutenticado() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
            return null;
        }

        Object usuarioSesion = facesContext.getExternalContext()
                .getSessionMap().get("usuarioLogueado");
        return usuarioSesion instanceof Usuario usuario ? usuario : null;
    }

    private boolean esUsuarioDemo() {
        Usuario usuario = getUsuarioAutenticado();
        return usuario != null && usuario.getId() == null && "demo".equalsIgnoreCase(usuario.getUsername());
    }

    private Usuario obtenerUsuario(EntityManager em, Long usuarioId) {
        if (usuarioId == null) {
            throw new IllegalStateException("Debes iniciar sesión para guardar una build.");
        }

        Usuario usuario = em.find(Usuario.class, usuarioId);
        if (usuario == null) {
            throw new IllegalStateException("No se encontró el usuario de la sesión.");
        }
        return usuario;
    }

    // BUSCAR BUILDS DEL USUARIO ACTUAL
    public List<Build> getBuilds() {
        if (esUsuarioDemo()) {
            return new ArrayList<>(demoBuilds);
        }

        Long usuarioId = getUsuarioAutenticadoId();
        if (usuarioId == null) {
            return List.of();
        }

        EntityManager em = getEmf().createEntityManager();
        try {
            List<PersonajeBuild> entidades = em.createQuery(
                            "SELECT DISTINCT b FROM PersonajeBuild b " +
                                    "LEFT JOIN FETCH b.equipamientoPrincipal " +
                                    "LEFT JOIN FETCH b.piezasEquipamiento " +
                                    "WHERE b.usuario.id = :usuarioId AND b.juego = :juego",
                            PersonajeBuild.class)
                    .setParameter("usuarioId", usuarioId)
                    .setParameter("juego", "ENDFIELD")
                    .getResultList();

            List<Build> resultado = new ArrayList<>();
            for (PersonajeBuild entidad : entidades) {
                resultado.add(convertirBuild(entidad));
            }
            return resultado;
        } finally {
            em.close();
        }
    }

    private Build convertirBuild(PersonajeBuild entidad) {
        Operator operator = buscarOperador(entidad.getCharacterId());
        EquipamientoPrincipal equipamiento = entidad.getEquipamientoPrincipal();
        Weapon weapon = equipamiento == null ? null : buscarArma(equipamiento.getItemId());
        if (operator == null || weapon == null) {
            throw new IllegalStateException("La build contiene un personaje o arma que ya no está disponible.");
        }

        List<PiezaEquipamiento> piezas = new ArrayList<>(entidad.getPiezasEquipamiento());
        piezas.sort(Comparator.comparingInt(pieza -> {
            try {
                return Integer.parseInt(pieza.getSlotPosition());
            } catch (NumberFormatException | NullPointerException e) {
                return Integer.MAX_VALUE;
            }
        }));

        List<Gear> buildGears = new ArrayList<>();
        for (PiezaEquipamiento pieza : piezas) {
            Gear gear = buscarGear(pieza.getItemId());
            if (gear == null) {
                throw new IllegalStateException("La build contiene equipamiento que ya no está disponible.");
            }
            buildGears.add(gear);
        }

        Build build = new Build();
        build.setId(entidad.getId());
        build.setOperator(operator);
        build.setWeapon(weapon);
        build.setGear(buildGears);
        return build;
    }

    // BUSCAR BUILD PROPIA
    public Build buscarBuild(Long id) {
        if (id == null) {
            return null;
        }
        if (esUsuarioDemo()) {
            return demoBuilds.stream()
                    .filter(build -> id.equals(build.getId()))
                    .findFirst()
                    .orElse(null);
        }

        Long usuarioId = getUsuarioAutenticadoId();
        if (usuarioId == null) {
            return null;
        }

        EntityManager em = getEmf().createEntityManager();
        try {
            List<PersonajeBuild> entidades = em.createQuery(
                            "SELECT DISTINCT b FROM PersonajeBuild b " +
                                    "LEFT JOIN FETCH b.equipamientoPrincipal " +
                                    "LEFT JOIN FETCH b.piezasEquipamiento " +
                                    "WHERE b.id = :id AND b.usuario.id = :usuarioId AND b.juego = :juego",
                            PersonajeBuild.class)
                    .setParameter("id", id)
                    .setParameter("usuarioId", usuarioId)
                    .setParameter("juego", "ENDFIELD")
                    .getResultList();
            return entidades.isEmpty() ? null : convertirBuild(entidades.get(0));
        } finally {
            em.close();
        }
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

    // GUARDAR BUILD EN LA BASE DE DATOS
    public void guardarBuild(Build build) {
        if (esUsuarioDemo()) {
            guardarBuildDemo(build);
            return;
        }

        Long usuarioId = getUsuarioAutenticadoId();
        EntityManager em = getEmf().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Usuario usuario = obtenerUsuario(em, usuarioId);
            PersonajeBuild entidad;

            if (build.getId() == null) {
                entidad = new PersonajeBuild();
                entidad.setUsuario(usuario);
            } else {
                List<PersonajeBuild> entidades = em.createQuery(
                                "SELECT DISTINCT b FROM PersonajeBuild b " +
                                        "LEFT JOIN FETCH b.equipamientoPrincipal " +
                                        "LEFT JOIN FETCH b.piezasEquipamiento " +
                                        "WHERE b.id = :id AND b.usuario.id = :usuarioId AND b.juego = :juego",
                                PersonajeBuild.class)
                        .setParameter("id", build.getId())
                        .setParameter("usuarioId", usuarioId)
                        .setParameter("juego", "ENDFIELD")
                        .getResultList();
                if (entidades.isEmpty()) {
                    throw new IllegalStateException("No se encontró una build propia para actualizar.");
                }
                entidad = entidades.get(0);
            }

            actualizarEntidad(entidad, build);
            if (build.getId() == null) {
                em.persist(entidad);
            }
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new IllegalStateException("No se pudo guardar la build en la base de datos.", e);
        } finally {
            em.close();
        }
    }

    private void guardarBuildDemo(Build build) {
        if (build == null || build.getOperator() == null || build.getWeapon() == null || build.getGear() == null) {
            throw new IllegalArgumentException("La build debe incluir personaje, arma y equipamiento.");
        }

        if (build.getId() == null) {
            build.setId(nextDemoBuildId--);
            demoBuilds.add(build);
            return;
        }

        for (int i = 0; i < demoBuilds.size(); i++) {
            if (build.getId().equals(demoBuilds.get(i).getId())) {
                demoBuilds.set(i, build);
                return;
            }
        }
        throw new IllegalStateException("No se encontró una build demo para actualizar.");
    }

    private void actualizarEntidad(PersonajeBuild entidad, Build build) {
        if (build.getOperator() == null || build.getWeapon() == null || build.getGear() == null) {
            throw new IllegalArgumentException("La build debe incluir personaje, arma y equipamiento.");
        }

        entidad.setJuego("ENDFIELD");
        entidad.setCharacterId(build.getOperator().getId());

        EquipamientoPrincipal equipamiento = entidad.getEquipamientoPrincipal();
        if (equipamiento == null) {
            equipamiento = new EquipamientoPrincipal();
            equipamiento.setPersonajeBuild(entidad);
            entidad.setEquipamientoPrincipal(equipamiento);
        }
        equipamiento.setPersonajeBuild(entidad);
        equipamiento.setItemId(build.getWeapon().getId());

        entidad.getPiezasEquipamiento().clear();
        for (int i = 0; i < build.getGear().size(); i++) {
            Gear gear = build.getGear().get(i);
            if (gear == null) {
                throw new IllegalArgumentException("Todas las piezas de equipamiento deben ser válidas.");
            }

            PiezaEquipamiento pieza = new PiezaEquipamiento();
            pieza.setPersonajeBuild(entidad);
            pieza.setItemId(gear.getId());
            pieza.setSetName(gear.getSet());
            pieza.setSlotPosition(Integer.toString(i));
            entidad.getPiezasEquipamiento().add(pieza);
        }
    }

    // ELIMINAR BUILD PROPIA DE LA BASE DE DATOS
    public void eliminarBuild(Long id) {
        if (esUsuarioDemo()) {
            boolean eliminada = demoBuilds.removeIf(build -> id != null && id.equals(build.getId()));
            if (!eliminada) {
                throw new IllegalStateException("No se encontró una build demo para eliminar.");
            }
            return;
        }

        Long usuarioId = getUsuarioAutenticadoId();
        EntityManager em = getEmf().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            obtenerUsuario(em, usuarioId);
            List<PersonajeBuild> entidades = em.createQuery(
                            "SELECT b FROM PersonajeBuild b " +
                                    "WHERE b.id = :id AND b.usuario.id = :usuarioId AND b.juego = :juego",
                            PersonajeBuild.class)
                    .setParameter("id", id)
                    .setParameter("usuarioId", usuarioId)
                    .setParameter("juego", "ENDFIELD")
                    .getResultList();
            if (entidades.isEmpty()) {
                throw new IllegalStateException("No se encontró una build propia para eliminar.");
            }
            em.remove(entidades.get(0));
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new IllegalStateException("No se pudo eliminar la build de la base de datos.", e);
        } finally {
            em.close();
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
