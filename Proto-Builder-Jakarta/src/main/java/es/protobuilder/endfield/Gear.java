package es.protobuilder.endfield;

import java.io.Serializable;

public class Gear implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String id;
    private String set;
    private String name;
    private String stat1;
    private String stat2;
    private String image;

    public Gear() {
        
        this.id = "";
        this.set = "";
        this.name = "";
        this.stat1 = "";
        this.stat2 = "";
    }
    public Gear(String id, String set, String name, String stat1, String stat2) {
        this(id, set, name, stat1, stat2, null);
    }

    public Gear(String id, String set, String name, String stat1, String stat2, String image) {
        this.id = id;
        this.set = set;
        this.name = name;
        this.stat1 = stat1;
        this.stat2 = stat2;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStat2() {
        return stat2;
    }

    public void setStat2(String stat2) {
        this.stat2 = stat2;
    }

    public String getStat1() {
        return stat1;
    }

    public void setStat1(String stat1) {
        this.stat1 = stat1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSet() {
        return set;
    }
    public void setSet(String set) {
        this.set = set;
    }

    public String getImage(){
            return image != null ? image : "endfield/gears/"+ this.name.toLowerCase().replace(" ","-") + ".webp";
    }
}
