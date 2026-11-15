package es.protobuilder.endfield;

import java.io.Serializable;

public class Weapon implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String type;
    private String description;
    private String image;

    public Weapon() {
        this.id = "";
        this.name = "";
        this.type = "";
        this.description = "";
    }

    public Weapon(String id, String name, String type, String description) {
        this(id, name, type, description, null);
    }

    public Weapon(String id, String name, String type, String description, String image) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.image = image;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage(){
        return image != null ? image : "endfield/weapons/"+ this.name.toLowerCase().replace(" ","-") + ".webp";
    }
}
