package es.protobuilder.beans;

import es.protobuilder.endfield.Gear;
import es.protobuilder.endfield.Operator;
import es.protobuilder.endfield.Weapon;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

@Named("glossaryBean")
@ViewScoped
public class GlossaryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EndfieldServiceBean endfieldService;

    private String operatorSearch;
    private String weaponSearch;
    private String gearSearch;

    public List<Operator> getFilteredOperators() {
        return endfieldService.getOperators().stream()
                .filter(operator -> matches(operator.getName(), operatorSearch))
                .toList();
    }

    public List<Weapon> getFilteredWeapons() {
        return endfieldService.getWeapons().stream()
                .filter(weapon -> matches(weapon.getName(), weaponSearch))
                .toList();
    }

    public List<Gear> getFilteredGear() {
        return endfieldService.getGears().stream()
                .filter(gear -> matches(gear.getName(), gearSearch))
                .toList();
    }

    private boolean matches(String name, String searchTerm) {
        return searchTerm == null || searchTerm.isBlank()
                || normalize(name).contains(normalize(searchTerm));
    }

    private String normalize(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
    }

    public String getOperatorSearch() {
        return operatorSearch;
    }

    public void setOperatorSearch(String operatorSearch) {
        this.operatorSearch = operatorSearch;
    }

    public String getWeaponSearch() {
        return weaponSearch;
    }

    public void setWeaponSearch(String weaponSearch) {
        this.weaponSearch = weaponSearch;
    }

    public String getGearSearch() {
        return gearSearch;
    }

    public void setGearSearch(String gearSearch) {
        this.gearSearch = gearSearch;
    }
}
