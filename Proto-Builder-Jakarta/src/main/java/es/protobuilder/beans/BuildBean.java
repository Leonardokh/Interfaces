package es.protobuilder.beans;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.protobuilder.dto.ArmaDTO;
import es.protobuilder.dto.PersonajeDTO;
import es.protobuilder.model.JuegoEnum;
import es.protobuilder.service.EnkaApiService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("buildBean")
@ViewScoped
public class BuildBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private EnkaApiService enkaApiService = new EnkaApiService();
    private ObjectMapper objectMapper = new ObjectMapper();

    // Propiedades vinculadas a la vista XHTML
    private String nombreCustomBuild;
    private String personajeSeleccionadoId;
    private String armaSeleccionadaId;
    private String notasEstrategia;

    private String urlImagenPersonaje;
    private double statAtaque;
    private double statProbCritica;
    private double statDanoCritico;
    private double statEspecial;

    // Listas consumidas por los componentes <p:selectOneMenu>
    private List<PersonajeDTO> listaPersonajesDisponibles = new ArrayList<>();
    private List<ArmaDTO> listaArmasDisponibles = new ArrayList<>();
    private JuegoEnum juegoSeleccionado;

    @PostConstruct
    public void init() {
        cargarPersonajesDesdeApi();
        cargarArmasPrueba();
    }

    public void cargarPersonajesDesdeApi() {
        try {
            String uidUsuario = "1500069011"; // O recuperado de la sesión

            if (uidUsuario != null && !uidUsuario.isEmpty()) {
                String jsonRespuesta = enkaApiService.obtenerDatosJugador(this.juegoSeleccionado, uidUsuario);

                System.out.println("=== RESPUESTA JSON DE ENKA API ===");
                System.out.println(jsonRespuesta);

                JsonNode root = objectMapper.readTree(jsonRespuesta);
                listaPersonajesDisponibles.clear();

                // 1. Intentar obtener la lista desde la estructura de ZZZ (PlayerInfo.ShowcaseDetail.AvatarList)
                JsonNode avatarList = root.path("PlayerInfo").path("ShowcaseDetail").path("AvatarList");

                // 2. Si no existe, intentar con estructuras alternativas (Genshin / Honkai)
                if (avatarList.isMissingNode() || !avatarList.isArray()) {
                    avatarList = root.path("avatarInfoList");
                }
                if (avatarList.isMissingNode() || !avatarList.isArray()) {
                    avatarList = root.path("characters");
                }

                if (avatarList.isArray() && avatarList.size() > 0) {
                    for (JsonNode avatar : avatarList) {
                        // Lee 'Id' (con mayúscula para ZZZ) o 'avatarId' / 'id'
                        String id = avatar.path("Id").asText(
                                avatar.path("avatarId").asText(
                                        avatar.path("id").asText("0")));

                        // Extrae stats o asigna valores por defecto según el nivel retornado
                        JsonNode stats = avatar.path("fightPropMap");
                        double atk = stats.path("2001").asDouble(1800.0);
                        double critRate = stats.path("2002").asDouble(0.05) * 100;
                        double critDmg = stats.path("2003").asDouble(0.50) * 100;

                        String imgUrl = "https://enka.network/ui/UI_AvatarIcon_" + id + ".png";

                        listaPersonajesDisponibles.add(
                                new PersonajeDTO(id, "Agente ID #" + id, imgUrl, atk, critRate, critDmg, 100.0)
                        );
                    }
                } else {
                    System.out.println("AVISO: No se encontró una lista de personajes válida en el JSON.");
                    cargarPersonajesFicticios();
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR ENKA API: " + e.getMessage());
            e.printStackTrace();
            cargarPersonajesFicticios();
        }
    }

    private void cargarPersonajesFicticios() {
        listaPersonajesDisponibles.clear();
        listaPersonajesDisponibles.add(new PersonajeDTO("1011", "Anby Demara", "https://enka.network/ui/UI_AvatarIcon_1011.png", 1850.0, 45.0, 110.0, 100.0));
        listaPersonajesDisponibles.add(new PersonajeDTO("1021", "Billy Kid", "https://enka.network/ui/UI_AvatarIcon_1021.png", 2100.0, 60.5, 140.0, 80.0));
        listaPersonajesDisponibles.add(new PersonajeDTO("1031", "Nicole Demara", "https://enka.network/ui/UI_AvatarIcon_1031.png", 1600.0, 20.0, 80.0, 150.0));
    }

    private void cargarArmasPrueba() {
        listaArmasDisponibles.clear();
        listaArmasDisponibles.add(new ArmaDTO("101", "W-Engine - Motor de Prueba Alpha"));
        listaArmasDisponibles.add(new ArmaDTO("102", "W-Engine - Restricción Severa"));
        listaArmasDisponibles.add(new ArmaDTO("103", "W-Engine - Sombra de Acero"));
    }

    // Listener AJAX que se ejecuta al seleccionar un operador en el desplegable
    public void onPersonajeChange() {
        if (personajeSeleccionadoId != null && !personajeSeleccionadoId.isEmpty()) {
            for (PersonajeDTO p : listaPersonajesDisponibles) {
                if (p.getId().equals(personajeSeleccionadoId)) {
                    this.urlImagenPersonaje = p.getUrlImagen();
                    this.statAtaque = p.getAtaque();
                    this.statProbCritica = p.getProbCritica();
                    this.statDanoCritico = p.getDanoCritico();
                    this.statEspecial = p.getStatEspecial();
                    return;
                }
            }
        } else {
            limpiarStats();
        }
    }

    private void limpiarStats() {
        this.urlImagenPersonaje = null;
        this.statAtaque = 0;
        this.statProbCritica = 0;
        this.statDanoCritico = 0;
        this.statEspecial = 0;
    }

    public void guardarBuild() {
        // Lógica JPA para guardar la instancia de PersonajeBuild asociada al Usuario
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Build '" + nombreCustomBuild + "' guardada correctamente."));
    }

    // --- Getters y Setters ---
    public String getNombreCustomBuild() { return nombreCustomBuild; }
    public void setNombreCustomBuild(String nombreCustomBuild) { this.nombreCustomBuild = nombreCustomBuild; }

    public String getPersonajeSeleccionadoId() { return personajeSeleccionadoId; }
    public void setPersonajeSeleccionadoId(String personajeSeleccionadoId) { this.personajeSeleccionadoId = personajeSeleccionadoId; }

    public String getArmaSeleccionadaId() { return armaSeleccionadaId; }
    public void setArmaSeleccionadaId(String armaSeleccionadaId) { this.armaSeleccionadaId = armaSeleccionadaId; }

    public String getNotasEstrategia() { return notasEstrategia; }
    public void setNotasEstrategia(String notasEstrategia) { this.notasEstrategia = notasEstrategia; }

    public String getUrlImagenPersonaje() { return urlImagenPersonaje; }
    public double getStatAtaque() { return statAtaque; }
    public double getStatProbCritica() { return statProbCritica; }
    public double getStatDanoCritico() { return statDanoCritico; }
    public double getStatEspecial() { return statEspecial; }

    public List<PersonajeDTO> getListaPersonajesDisponibles() { return listaPersonajesDisponibles; }
    public void setListaPersonajesDisponibles(List<PersonajeDTO> listaPersonajesDisponibles) { this.listaPersonajesDisponibles = listaPersonajesDisponibles; }

    public List<ArmaDTO> getListaArmasDisponibles() { return listaArmasDisponibles; }
    public void setListaArmasDisponibles(List<ArmaDTO> listaArmasDisponibles) { this.listaArmasDisponibles = listaArmasDisponibles; }
}