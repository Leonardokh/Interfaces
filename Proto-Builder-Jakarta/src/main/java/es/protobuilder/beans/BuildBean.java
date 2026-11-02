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

    @PostConstruct
    public void init() {
        cargarPersonajesDesdeApi();
        cargarArmasPrueba();
    }

    public void cargarPersonajesDesdeApi() {
        try {
            // Ejemplo: obtener el UID guardado en sesión para ZZZ
            String uidUsuario = "1300025292";

            if (uidUsuario != null && !uidUsuario.isEmpty()) {
                String jsonRespuesta = enkaApiService.obtenerDatosJugador(JuegoEnum.ZZZ, uidUsuario);

                // Parseo del JSON recibido de Enka
                JsonNode root = objectMapper.readTree(jsonRespuesta);
                JsonNode avatarList = root.path("avatarInfoList");

                listaPersonajesDisponibles.clear();

                if (avatarList.isArray()) {
                    for (JsonNode avatar : avatarList) {
                        String id = avatar.path("avatarId").asText();

                        // Mapeo básico de stats extraídos del nodo 'fightPropMap' de Enka
                        JsonNode stats = avatar.path("fightPropMap");
                        double atk = stats.path("2001").asDouble(1500.0);
                        double critRate = stats.path("2002").asDouble(0.0) * 100;
                        double critDmg = stats.path("2003").asDouble(0.0) * 100;

                        // Construye la URL pública de imágenes/assets según la ID
                        String imgUrl = "https://enka.network/ui/UI_AvatarIcon_" + id + ".png";

                        listaPersonajesDisponibles.add(
                                new PersonajeDTO(id, "Operador #" + id, imgUrl, atk, critRate, critDmg, 100.0)
                        );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de API", e.getMessage()));
        }
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

    // Métodos Getters y Setters añadidos para corregir el error en JSF
    public List<ArmaDTO> getListaArmasDisponibles() { return listaArmasDisponibles; }
    public void setListaArmasDisponibles(List<ArmaDTO> listaArmasDisponibles) { this.listaArmasDisponibles = listaArmasDisponibles; }



}