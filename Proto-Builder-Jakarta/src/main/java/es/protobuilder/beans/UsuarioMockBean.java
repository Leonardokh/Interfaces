package es.protobuilder.beans;

import es.protobuilder.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Named("usuarioMockBean")
@ApplicationScoped
public class UsuarioMockBean {

    private final Map<String, Usuario> usuariosPorNombre = new HashMap<>();
    private final Set<String> correosRegistrados = new HashSet<>();

    public UsuarioMockBean() {
        Usuario demo = new Usuario();
        demo.setUsername("demo");
        demo.setEmail("demo@protobuilder.local");
        demo.setPassword("demo123");
        guardar(demo);
    }

    public synchronized Usuario autenticar(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        Usuario usuario = usuariosPorNombre.get(normalizar(username));
        return usuario != null && usuario.getPassword().equals(password) ? usuario : null;
    }

    public synchronized ResultadoRegistro registrar(String username, String email, String password) {
        if (username == null || username.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()) {
            return ResultadoRegistro.DATOS_INVALIDOS;
        }

        String nombreNormalizado = normalizar(username);
        String correoNormalizado = normalizar(email);
        if (usuariosPorNombre.containsKey(nombreNormalizado)) {
            return ResultadoRegistro.NOMBRE_DUPLICADO;
        }
        if (correosRegistrados.contains(correoNormalizado)) {
            return ResultadoRegistro.CORREO_DUPLICADO;
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username.trim());
        usuario.setEmail(email.trim());
        usuario.setPassword(password);
        guardar(usuario);
        return ResultadoRegistro.REGISTRADO;
    }

    private void guardar(Usuario usuario) {
        usuariosPorNombre.put(normalizar(usuario.getUsername()), usuario);
        correosRegistrados.add(normalizar(usuario.getEmail()));
    }

    private String normalizar(String valor) {
        return valor.trim().toLowerCase(Locale.ROOT);
    }

    public enum ResultadoRegistro {
        REGISTRADO,
        NOMBRE_DUPLICADO,
        CORREO_DUPLICADO,
        DATOS_INVALIDOS
    }
}
