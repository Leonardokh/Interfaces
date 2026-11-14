package es.protobuilder.endfield;

import java.util.ArrayList;
import java.util.List;

import jakarta.inject.Inject;

public class Build {

    private Long id;

    private Operator operator;
    private Weapon weapon;
    private List<Gear> gear;

    public Build(Long id, Operator operator, Weapon weapon, List<Gear> gears) {
        this.id = id;
        this.operator = operator;
        this.weapon = weapon;
        this.gear = new ArrayList<Gear>(gears);
    }

    public Build() {
        this.gear = new ArrayList<Gear>();
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public List<Gear> getGear() {
        return gear;
    }

    public void setGear(List<Gear> gears) {
        this.gear = gears;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
}
