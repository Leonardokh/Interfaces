package es.protobuilder.endfield;

import java.io.Serializable;

public class Operator implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String archetype;
    private String element;
    private String description;

    public Operator() {
        this.id = "";
        this.name = "";
        this.archetype = "";
        this.element = "";
        this.description = "";
    }

    public Operator(String id, String name, String archetype, String element, String description) {
        this.id = id;
        this.name = name;
        this.archetype = archetype;
        this.element = element;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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