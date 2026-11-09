package es.protobuilder.endfield;

public class Gear {
    
    private String setName;
    private String name;
    private String stat1;
    private String stat2;

    public Gear() {
        
        this.setName = "";
        this.name = "";
        this.stat1 = "";
        this.stat2 = "";
    }
    public Gear(String setName, String name, String stat1, String stat2) {
        this.setName = setName;
        this.name = name;
        this.stat1 = stat1;
        this.stat2 = stat2;
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

    public String getSetName() {
        return setName;
    }
    public void setSetName(String setName) {
        this.setName = setName;
    }

    public String getImage(){
            return "endfield/gears/"+ this.name.toLowerCase().replace(" ","-") + ".webp";
    }
}
