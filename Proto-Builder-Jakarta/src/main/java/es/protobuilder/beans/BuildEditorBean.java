package es.protobuilder.beans;

import es.protobuilder.endfield.Build;
import es.protobuilder.endfield.Gear;
import es.protobuilder.endfield.Operator;
import es.protobuilder.endfield.Weapon;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("buildEditorBean")
@ViewScoped
public class BuildEditorBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EndfieldServiceBean endfieldService;

    @Inject
    private VistaBean vistaBean;

    private Build buildEnEdicion;
    private String operadorSeleccionado;
    private String armaSeleccionada;
    private List<String> gearSeleccionado = new ArrayList<>();
    private String selectorActivo;
    private int indiceGearActivo = -1;

    public void crearNueva() {
        buildEnEdicion = new Build();
        operadorSeleccionado = "";
        armaSeleccionada = "";
        gearSeleccionado = new ArrayList<>();
        cerrarSelector();

        List<Gear> espaciosGear = new ArrayList<>();
        for (int i = 0; i < endfieldService.getGears().size(); i++) {
            espaciosGear.add(new Gear("", "", "", "", ""));
            gearSeleccionado.add("");
        }
        buildEnEdicion.setGear(espaciosGear);
        vistaBean.mostrarNuevaBuild();
    }

    public void editar(Long id) {
        Build build = endfieldService.buscarBuild(id);
        if (build == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "No se encontró la build seleccionada."));
            return;
        }

        buildEnEdicion = build;
        operadorSeleccionado = build.getOperator() == null ? null : build.getOperator().getId();
        armaSeleccionada = build.getWeapon() == null ? null : build.getWeapon().getId();
        cerrarSelector();
        gearSeleccionado = new ArrayList<>();
        if (build.getGear() != null) {
            for (Gear gear : build.getGear()) {
                gearSeleccionado.add(gear.getId());
            }
        }
        vistaBean.mostrarEditarBuild();
    }

    public void guardarBuild() {
        if (buildEnEdicion == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay una build seleccionada."));
            return;
        }

        Operator operator = endfieldService.buscarOperador(operadorSeleccionado);
        Weapon weapon = endfieldService.buscarArma(armaSeleccionada);
        List<Gear> gear = new ArrayList<>();
        for (String gearId : gearSeleccionado) {
            Gear selectedGear = endfieldService.buscarGear(gearId);
            if (selectedGear == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Selecciona un equipamiento válido para cada pieza."));
                return;
            }
            gear.add(selectedGear);
        }

        if (operator == null || weapon == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Selecciona un operador y un arma válidos."));
            return;
        }

        buildEnEdicion.setOperator(operator);
        buildEnEdicion.setWeapon(weapon);
        buildEnEdicion.setGear(gear);
        endfieldService.guardarBuild(buildEnEdicion);
        limpiarEdicion();
    }

    public void cancelarEdicion() {
        limpiarEdicion();
    }

    public void eliminar(Long id) {
        Build build = endfieldService.buscarBuild(id);
        if (build == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "La build ya no existe o ya fue eliminada."));
            return;
        }

        endfieldService.eliminarBuild(id);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Build eliminada",
                        "Se eliminó la build de " + build.getOperator().getName() + "."));
    }

    private void limpiarEdicion() {
        buildEnEdicion = null;
        operadorSeleccionado = null;
        armaSeleccionada = null;
        gearSeleccionado.clear();
        cerrarSelector();
        vistaBean.mostrarMisBuilds();
    }

    public boolean isNuevaBuild() {
        return buildEnEdicion != null && buildEnEdicion.getId() == null;
    }

    public Build getBuildEnEdicion() {
        return buildEnEdicion;
    }

    public String getOperadorSeleccionado() {
        return operadorSeleccionado;
    }

    public String getNombreOperadorSeleccionado() {
        Operator operator = endfieldService.buscarOperador(operadorSeleccionado);
        return operator == null ? null : operator.getName();
    }

    public void setOperadorSeleccionado(String operadorSeleccionado) {
        this.operadorSeleccionado = operadorSeleccionado;
    }

    public String getArmaSeleccionada() {
        return armaSeleccionada;
    }

    public String getNombreArmaSeleccionada() {
        Weapon weapon = endfieldService.buscarArma(armaSeleccionada);
        return weapon == null ? null : weapon.getName();
    }

    public void setArmaSeleccionada(String armaSeleccionada) {
        this.armaSeleccionada = armaSeleccionada;
    }

    public List<String> getGearSeleccionado() {
        return gearSeleccionado;
    }

    public String getSelectorActivo() {
        return selectorActivo;
    }

    public int getIndiceGearActivo() {
        return indiceGearActivo;
    }

    public void mostrarOperadores() {
        selectorActivo = "operators";
        indiceGearActivo = -1;
    }

    public void mostrarArmas() {
        selectorActivo = "weapons";
        indiceGearActivo = -1;
    }

    public void mostrarGear(int index) {
        if (index >= 0 && index < gearSeleccionado.size()) {
            selectorActivo = "gear";
            indiceGearActivo = index;
        }
    }

    public void cerrarSelector() {
        selectorActivo = null;
        indiceGearActivo = -1;
    }

    public List<Operator> getOperadoresDisponibles() {
        return endfieldService.getOperators();
    }

    public List<Weapon> getArmasDisponibles() {
        return endfieldService.getWeapons();
    }

    public List<Gear> getGearDisponible() {
        return endfieldService.getGears();
    }

    public String getImagenOperadorSeleccionado() {
        Operator operator = endfieldService.buscarOperador(operadorSeleccionado);
        return operator == null ? null : operator.getImage();
    }

    public void seleccionarOperador(String id) {
        operadorSeleccionado = id;
        cerrarSelector();
    }

    public String getImagenArmaSeleccionada() {
        Weapon weapon = endfieldService.buscarArma(armaSeleccionada);
        return weapon == null ? null : weapon.getImage();
    }

    public void seleccionarArma(String id) {
        armaSeleccionada = id;
        cerrarSelector();
    }

    public void seleccionarGear(int index, String id) {
        if (index >= 0 && index < gearSeleccionado.size()) {
            gearSeleccionado.set(index, id);
            cerrarSelector();
        }
    }

    public String getImagenGearSeleccionado(int index) {
        if (index < 0 || index >= gearSeleccionado.size()) {
            return null;
        }

        Gear gear = endfieldService.buscarGear(gearSeleccionado.get(index));
        return gear == null ? null : gear.getImage();
    }

    public String getNombreGearSeleccionado(int index) {
        if (index < 0 || index >= gearSeleccionado.size()) {
            return null;
        }

        Gear gear = endfieldService.buscarGear(gearSeleccionado.get(index));
        return gear == null ? null : gear.getName();
    }
}
