package es.protobuilder.dto;

public class PersonajeDTO {
    private String id;
    private String nombreMostrar;
    private String urlImagen;
    private double ataque;
    private double probCritica;
    private double danoCritico;
    private double statEspecial;

    public PersonajeDTO(String id, String nombreMostrar, String urlImagen, double ataque, double probCritica, double danoCritico, double statEspecial) {
        this.id = id;
        this.nombreMostrar = nombreMostrar;
        this.urlImagen = urlImagen;
        this.ataque = ataque;
        this.probCritica = probCritica;
        this.danoCritico = danoCritico;
        this.statEspecial = statEspecial;
    }

    // --- Getters y Setters ---
    public String getId() { return id; }
    public String getNombreMostrar() { return nombreMostrar; }
    public String getUrlImagen() { return urlImagen; }
    public double getAtaque() { return ataque; }
    public double getProbCritica() { return probCritica; }
    public double getDanoCritico() { return danoCritico; }
    public double getStatEspecial() { return statEspecial; }
}