package es.protobuilder.dto;

public class ArmaDTO {
    private String id;
    private String nombre;

    public ArmaDTO(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
}