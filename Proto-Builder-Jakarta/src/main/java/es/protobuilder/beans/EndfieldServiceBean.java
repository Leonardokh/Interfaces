package es.protobuilder.beans;

import es.protobuilder.endfield.Build;
import es.protobuilder.endfield.Gear;
import es.protobuilder.endfield.Operator;
import es.protobuilder.endfield.Weapon;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("endfieldService")
@RequestScoped
public class EndfieldServiceBean {

    private List<Operator> operators;
    private List<Weapon> weapons;
    private List<Gear> gears;
    private List<Build> builds;

    @PostConstruct
    public void init() {

        // Datos mock simulando respuesta de API
        // ---- Operators ----
        operators = new ArrayList<>();
       
        Operator endministrator = new Operator("Endministrator", "Guard", "Physical",
                "The protagonist of Endfield and the first operator you control. A key guardian who protects civilization on Talos-II.");
        operators.add(endministrator);
        
        operators.add(new Operator("Perlica", "Caster", "Electric",
                "Supervisor and official spokesperson of Endfield Industries, overseeing the development of Protocol-Originium technology."));
        
        operators.add(new Operator("Xaihi", "Supporter", "Cryo",
                "A researcher dispatched from Laterano to Talos-II for technical study, who believes in computation and logical analysis."));

        // ---- Weapons ----
        weapons = new ArrayList<>();

        Weapon complexedIndustry = new Weapon("Complexed Industry",
                "Sword",
                "An all-new product from the Northmarch Support Hub \"Pioneer\" series. Furnished with an excellent power component, it can easily cut down thick vegetation to clear a path. The weapon is highly recommended by many explorers.");
        weapons.add(complexedIndustry);
        weapons.add(new Weapon ("Khravengger",
                "Great Sword",
                "A strange weapon released by Witching Hour. The blade body is chiseled out of an ordinary-looking block of ice, but its surface is infused with a special form of Originium Arts that made it literally indestructible."));
         weapons.add(new Weapon ("Blessing of Lustrous Carmine",
                "Polearm",
                "One of the polearms produced by the Witching Hour. A special commemorative edition was released on Seš'qa Open Day. The body of the lance underwent a special plating technique to provide a finish that is both extremely functional and aesthetically pleasing."));
        
        // ---- Gears ----
        gears = new ArrayList<>();
        gears.add(new Gear("Swordmancer ","Heavy Armor", "AGILITY", "STRENGTH"));
        gears.add(new Gear("Swordmancer ","TAC Gloves", "STRENGTH", "WILL"));
        gears.add(new Gear("Swordmancer ","TAC Gauntlets","AGILITY", "STRENGTH"));
        gears.add(new Gear("Swordmancer ","Flint","AGILITY", "STRENGTH"));     
        
        // ---- Builds ----
        builds = new ArrayList<>();
        Build build1 = new Build (endministrator, complexedIndustry, gears);
        builds.add(build1);
    }
    // Getters
    public List<Operator> getOperators() {
        return operators;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public List<Gear> getGears() {
        return gears;
    }

    public List<Build> getBuilds(){
        return builds;
    }
}