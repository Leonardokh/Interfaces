package es.protobuilder.model;

// Enumeración para representar los videojuegos soportados y que sepa que API usar
public enum JuegoEnum {
    ZZZ("Zenless Zone Zero", "https://enka.network/api/zzz/uid/"),
    ENDFIELD("Arknights: Endfield", "https://enka.network/api/ef/uid/");

    private final String nombreMostrar;
    private final String apiEndpoint;

    // Constructor del Enum
    JuegoEnum(String nombreMostrar, String apiEndpoint) {
        this.nombreMostrar = nombreMostrar;
        this.apiEndpoint = apiEndpoint;
    }

    public String getNombreMostrar() {
        return nombreMostrar;
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }
}