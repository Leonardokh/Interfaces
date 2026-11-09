package es.protobuilder.endfield;

public class Operator {
    private String name;
    private String archetype;
    private String element;
    private String description;

    public Operator() {
        this.name = "";
        this.archetype = "";
        this.element = "";
        this.description = "";
    }

    public Operator(String name, String archetype, String element, String description) {
        this.name = name;
        this.archetype = archetype;
        this.element = element;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArchetype() {
        return archetype;
    }

    public void setArchetype(String archetype) {
        this.archetype = archetype;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage(){
        return "endfield/operators/"+ this.name.toLowerCase().replace(" ","-") + ".webp";
    }
}