package es.protobuilder.endfield;

import java.util.List;

import jakarta.inject.Inject;

public class Build {

    private Operator operator;
    private Weapon weapon;
    private List<Gear> gear;

    public Build(Operator operator, Weapon weapon, List<Gear> gears) {
        this.operator = operator;
        this.weapon = weapon;
        this.gear = gears;
    }

    public Build() {
        this.operator = null;
        this.weapon = null;
        this.gear = null;
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

    
}
