package es.protobuilder.service;

import es.protobuilder.model.JuegoEnum;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

// Servicio para realizar las peticiones HTTP a la API de Enka.Network
public class EnkaApiService {

    private final HttpClient httpClient;

    public EnkaApiService() {
        // Inicializa el cliente HTTP de Java 11+ con un timeout de 10 segundos
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    // Método para obtener la respuesta JSON de un jugador según el juego y UID
    public String obtenerDatosJugador(JuegoEnum juego, String uid) throws Exception {
        // Construye la URL del endpoint correspondiente (ej: https://enka.network/api/zzz/uid/1300025292)
        String urlCompleta = juego.getApiEndpoint() + uid;

        // Construye la petición GET configurando el User-Agent obligatorio que exige Enka
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlCompleta))
                .header("User-Agent", "ProtoBuilderApp/1.0 (Contacto: protobuilderapi@gmail.com)")
                .GET()
                .build();

        // Envía la petición HTTP y recibe la respuesta en texto (JSON)
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Si la respuesta es HTTP 200 (OK), retorna el JSON recibido
        if (response.statusCode() == 200) {
            return response.body();
        } else if (response.statusCode() == 404) {
            throw new RuntimeException("El UID " + uid + " no existe o no tiene vitrina pública en " + juego.getNombreMostrar());
        } else if (response.statusCode() == 429) {
            throw new RuntimeException("Límite de peticiones alcanzado. Por favor, espera un momento.");
        } else {
            throw new RuntimeException("Error al consultar. Código de error: " + response.statusCode());
        }
    }
}