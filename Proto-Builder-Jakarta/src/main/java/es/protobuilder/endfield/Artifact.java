package es.protobuilder.endfield;

public class Artifact {
    private String name;
    private String stat1;
    private String stat2;

    public Artifact() {
        this.name = "";
        this.stat1 = "";
        this.stat2 = "";
    }
    public Artifact(String name, String stat1, String stat2) {
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
}
