package es.protobuilder.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

// Servicio encargado del envío de correos electrónicos transaccionales y de confirmación
public class EmailService {

    // Configuración del servidor SMTP (Ejemplo usando Gmail / Servidor estándar)
    private final String host = "smtp.gmail.com";
    private final String port = "587";
    private final String usuarioEmail = "protobuildapi@gmail.com"; // Cambiar por tu correo emisor
    private final String passwordEmail = "Protobuild098"; // Cambiar por tu contraseña de app

    // Método principal para enviar el correo de confirmación de registro
    public void enviarCorreoConfirmacion(String destinatarioCorreo, String username, String tokenConfirmacion) throws Exception {

        // 1. Configuración de las propiedades de conexión SMTP con cifrado TLS
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // 2. Creación de la sesión autenticada con el servidor SMTP
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuarioEmail, passwordEmail);
            }
        });

        // 3. Construcción del mensaje de correo en formato HTML
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(usuarioEmail, "ProtoBuilder Support"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatarioCorreo));
        message.setSubject("¡Bienvenido a ProtoBuilder! Confirma tu cuenta");

        // Enlace simulado o real de confirmación que incluirá el token del usuario
        String enlaceConfirmacion = "http://localhost:8080/proto-builder/confirmar.xhtml?token=" + tokenConfirmacion;

        // Cuerpo HTML del correo
        String contenidoHTML = "<h2>¡Hola " + username + "!</h2>"
                + "<p>Gracias por registrarte en <strong>ProtoBuilder</strong>.</p>"
                + "<p>Para completar tu registro y activar tu cuenta, por favor haz clic en el siguiente enlace de seguridad:</p>"
                + "<p><a href='" + enlaceConfirmacion + "' style='background-color:#0d6efd; color:white; padding:10px 15px; text-decoration:none; border-radius:5px;'>Confirmar mi Cuenta</a></p>"
                + "<br><p>Si no has solicitado este registro, puedes ignorar este mensaje.</p>";

        message.setContent(contenidoHTML, "text/html; charset=utf-8");

        // 4. Envío del correo
        Transport.send(message);
    }
}