# Proto Builder

Proto Builder es una aplicación web para consultar información de Arknights: Endfield y crear builds de operadores con armas y equipamiento. La interfaz está construida con Jakarta Faces (JSF), PrimeFaces y XHTML; el proyecto se empaqueta como WAR con Maven y está preparado para ejecutarse en Tomcat 11.

## Funcionalidades

- Inicio de sesión y registro de usuarios mock.
- Dashboard con navegación a noticias, creación de builds, builds propias, Community Builds y glosario.
- Creación, edición, consulta y eliminación de builds de Endfield.
- Build de ejemplo disponible para la cuenta `demo`.
- Community Builds de muestra, agrupadas por categorías como «Build de la semana», «Build más creativa» y «Build para Contingency Contract».
- Noticias y glosario de operadores, armas y equipamiento.

La cuenta de demostración es `demo` con contraseña `demo123`. Los usuarios que se registran desde la aplicación solo se guardan en memoria; desaparecen al reiniciar el servidor.

## Tecnologías

- Java 17 y Maven.
- Jakarta Faces 4, CDI (Weld) y PrimeFaces.
- XHTML/Facelets y Bootstrap para las vistas y estilos.
- Tomcat 11 como contenedor web.

## Estructura principal

```text
src/main/java/es/protobuilder/
├── beans/       # Beans CDI/JSF: estado de vistas y lógica de aplicación
├── endfield/    # Modelos de dominio usados por las vistas (Build, Operator, Gear...)
└── model/       # Modelos de usuario usados por la autenticación mock

src/main/webapp/
├── WEB-INF/templates/  # Plantilla común de la aplicación
├── views/              # Vistas incluidas en el dashboard
├── resources/
│   ├── components/     # Componentes Facelets reutilizables
│   ├── css/            # Estilos
│   └── img/            # Imágenes
├── home.xhtml
├── registro.xhtml
└── dashboard.xhtml
```

`home.xhtml` y `registro.xhtml` usan la plantilla `WEB-INF/templates/template.xhtml`. El dashboard conserva la navegación y cambia el contenido de forma parcial con `p:outputPanel` y `ui:include`. Las tarjetas se reutilizan como componentes Facelets: `buildCard` para las builds propias, `communityCard` para las de comunidad, `popularityChart` para las gráficas, `newsPreview` para el resumen de cada noticia y `newsArticle` para su detalle ampliado.

Los títulos usan la clase de estilo propia `text-accent` para el color de marca, evitando usar clases contextuales de Bootstrap como `text-warning` cuando no representan un estado de aviso.

## Cómo se usan los beans

Los beans se exponen a Facelets con `@Named`; por ejemplo, `@Named("loginBean")` se utiliza en una vista como `#{loginBean.login}`. CDI inyecta sus dependencias con `@Inject`. El alcance determina cuánto tiempo conserva el bean su estado:

- `@RequestScoped`: vive durante una petición. Adecuado para procesar acciones puntuales de formulario.
- `@ViewScoped`: conserva el estado mientras el usuario interactúa con la misma vista JSF, incluidas peticiones AJAX.
- `@SessionScoped`: conserva el estado durante la sesión de un usuario.
- `@ApplicationScoped`: comparte una instancia durante la vida de la aplicación.

### Beans de interfaz y autenticación

| Bean | Alcance | Responsabilidad |
|---|---|---|
| `LoginBean` (`loginBean`) | Petición | Valida las credenciales mediante `UsuarioMockBean`, inicia la sesión y navega al dashboard. También ofrece la navegación al registro. |
| `RegistroBean` (`registroBean`) | Petición | Comprueba que las contraseñas coincidan y solicita a `UsuarioMockBean` el registro en memoria. Publica mensajes JSF para errores y éxito. |
| `UsuarioMockBean` (`usuarioMockBean`) | Aplicación | Mantiene usuarios en estructuras en memoria, valida nombres y correos duplicados y autentica. Inicializa la cuenta de demostración. |
| `UsuarioBean` (`usuarioBean`) | Sesión | Proporciona el nombre y estado de autenticación usados por la plantilla, e invalida la sesión al cerrar sesión. |
| `UserSessionBean` (`userSessionBean`) | Sesión | Bean auxiliar para representar un usuario autenticado. En el flujo actual de login, la sesión se conserva en el mapa de sesión de Faces y el nombre visible se administra mediante `UsuarioBean`. |

### Beans del dashboard y de las funcionalidades

| Bean | Alcance | Responsabilidad |
|---|---|---|
| `VistaBean` (`vistaBean`) | Vista | Selecciona la sección visible del dashboard: noticias, glosario, builds, etc. |
| `EndfieldServiceBean` (`endfieldService`) | Sesión | Carga los catálogos de operadores, armas y equipamiento y mantiene en memoria las builds propias de cada usuario. |
| `BuildEditorBean` (`buildEditorBean`) | Vista | Mantiene el formulario de creación/edición, valida las selecciones y delega guardar o eliminar en `EndfieldServiceBean`. |
| `CommunityBuildBean` (`communityBuildBean`) | Vista | Prepara las builds ficticias que se muestran en la sección Community Builds. |
| `GlossaryBean` (`glossaryBean`) | Vista | Filtra operadores, armas y equipamiento del catálogo, ignorando mayúsculas y acentos. |
| `NewsServiceBean` (`newsService`) | Aplicación | Mantiene el catálogo de noticias de muestra y permite buscarlas por identificador. |
| `NewsBean` (`newsBean`) | Vista | Controla la noticia seleccionada y el paso entre la lista y el detalle. |

### Ciclo de interacción de las vistas

Las páginas Facelets enlazan componentes visuales con propiedades y métodos de los beans mediante expresiones EL (`#{...}`). Al pulsar un `p:commandButton` o al ejecutarse un `p:ajax`, JSF procesa los componentes indicados, actualiza las propiedades enlazadas, invoca el método del bean y renderiza los componentes solicitados. Por ejemplo, `update=":contenido"` actualiza el panel principal del dashboard sin volver a cargar toda la página.

#### Inicio, registro y sesión

1. En `home.xhtml`, los campos de usuario y contraseña se enlazan con `LoginBean.username` y `LoginBean.password`.
2. Al iniciar sesión, `LoginBean` valida que los campos estén completos y llama a `UsuarioMockBean.autenticar`. Si las credenciales son válidas, guarda el objeto `Usuario` en el mapa de sesión de Faces, actualiza `UsuarioBean` para mostrar el nombre y redirige al dashboard. Si no, envía un `FacesMessage` a la vista.
3. `registro.xhtml` enlaza los campos con `RegistroBean`. El bean verifica que ambas contraseñas coincidan y solicita el alta a `UsuarioMockBean`; este valida los datos y evita duplicados. El registro queda únicamente en memoria y no se conserva al reiniciar la aplicación.
4. `UsuarioBean` alimenta el menú de usuario de la plantilla y su acción de cerrar sesión invalida la sesión web.

#### Navegación del dashboard

`dashboard.xhtml` incluye tanto navegación lateral como móvil. Cada botón invoca un método de `VistaBean` que cambia `seccion` (por ejemplo, `news`, `glossary` o `communityBuilds`) y actualiza `:contenido`. Los paneles `rendered` seleccionan el `ui:include` correspondiente. El estado de la sección se mantiene en `VistaBean`, que tiene alcance de vista.

#### Crear, editar y administrar builds

1. **Crear:** el botón «Nueva build» llama a `BuildEditorBean.crearNueva()`. El bean crea un objeto `Build` y prepara los espacios de equipamiento vacíos a partir del catálogo de `EndfieldServiceBean`.
2. **Seleccionar:** las acciones para cambiar personaje, arma o pieza activan un selector en `BuildEditorBean`. La vista obtiene las opciones mediante sus getters (`operadoresDisponibles`, `armasDisponibles`, `gearDisponible`). Al seleccionar una opción, el bean guarda su identificador y la petición AJAX actualiza el formulario.
3. **Guardar:** `BuildEditorBean.guardarBuild()` resuelve los identificadores seleccionados contra el catálogo, valida personaje, arma y cada pieza, y delega la operación en `EndfieldServiceBean`, que crea o actualiza la build en memoria para el usuario actual. Tras guardar, el editor se limpia y `VistaBean` vuelve a «Mis builds».
4. **Consultar y editar:** `myBuilds.xhtml` itera sobre `endfieldService.builds` y pasa cada objeto al componente `buildCard`. Sus botones llaman a `BuildEditorBean.editar(id)` o `eliminar(id)`. La edición obtiene la build mediante `EndfieldServiceBean.buscarBuild`; la eliminación delega en `eliminarBuild`.
5. **Aislamiento:** cada usuario solo consulta y modifica sus propias builds. Tanto las builds como las cuentas de prueba se mantienen en memoria y se pierden al reiniciar la aplicación.

#### Community Builds

`CommunityBuildBean` prepara tres ejemplos usando los catálogos de `EndfieldServiceBean`; no los registra como builds de usuario ni los persiste. `communityBuilds.xhtml` recorre esa lista y entrega cada build, categoría, autor y descripción a `communityCard`. El componente se ocupa únicamente de presentar los datos, sin acciones de edición o borrado.

La sección también muestra gráficas horizontales de popularidad para operadores, armas y gear. Las configuraciones están definidas como los `String` `OPERATOR_CHART`, `WEAPON_CHART` y `GEAR_CHART` en `CommunityBuildBean`; cada uno contiene un objeto JSON de Chart.js escrito en un bloque de texto Java (`"""`). Los getters `getOperatorChart()`, `getWeaponChart()` y `getGearChart()` exponen esas cadenas a `communityBuilds.xhtml`, que las entrega como atributo `value` al componente reutilizable `popularityChart`. Este componente pasa el JSON a `<p:chart>` de PrimeFaces, que dibuja el gráfico.

En cada JSON, `type` define el tipo de gráfico, `data.labels` los nombres de las categorías y `data.datasets` las series con valores y colores. `options.indexAxis: "y"` muestra las barras en horizontal, y `options.scales.x.min`/`max` fijan el rango del eje. Las etiquetas pueden ser arrays de cadenas para repartir su texto en varias líneas. Los porcentajes son datos de ejemplo escritos a mano: no se calculan a partir de las Community Builds ni se actualizan al crear o editar builds. Al modificarlos, hay que mantener un JSON válido.

#### Glosario

`glossary.xhtml` consulta `GlossaryBean` para mostrar operadores, armas y equipamiento. Cada pestaña enlaza su campo de búsqueda a una propiedad distinta del bean. Un `p:ajax` procesa los cambios de teclado con un pequeño retraso y actualiza solo los resultados de esa categoría. Los getters filtrados usan los catálogos de `EndfieldServiceBean` y la comparación ignora mayúsculas y acentos. Las tarjetas `operatorCard`, `weaponCard` y `gearCard` presentan los elementos. Los catálogos incluyen ejemplos adicionales basados en nombres reales de Endfield, como Chen Qianyu, Ardelia, Laevatain, Thermite Cutter y Forgeborn Scathe. Los nuevos operadores ya tienen imágenes propias; las armas y piezas añadidas usan placeholders hasta incorporar sus ilustraciones. Los valores de atributos de los sets Frontiers, Hot Work y Grizzled Edge son ejemplos representativos para el mock, no estadísticas calculadas.

#### Noticias

`NewsServiceBean` ofrece el catálogo de artículos de muestra y permite localizar uno por ID. `NewsBean` conserva el ID seleccionado: `openArticle` valida la selección y activa el detalle, mientras `showList` vuelve al listado. `news.xhtml` muestra una u otra sección según `showingArticle`; las tarjetas reducidas se presentan con `newsPreview` y el detalle ampliado con `newsArticle`. Ambos componentes reciben el mismo objeto `NewsArticle` del catálogo; el botón de `newsPreview` invoca `newsBean.openArticle` y el de `newsArticle` invoca la acción recibida para volver. Las acciones actualizan `:contenido` mediante AJAX.

## Datos en memoria

La aplicación no usa base de datos ni conserva datos de forma permanente. `UsuarioMockBean` mantiene las cuentas de prueba durante la ejecución de la aplicación; `EndfieldServiceBean` mantiene las builds por usuario durante la sesión. La cuenta `demo` incluye una build de ejemplo. Las cuentas, builds y demás datos de muestra se pierden al reiniciar la aplicación.

## Compilar y desplegar

Ejecuta los comandos desde la carpeta del proyecto (donde está `pom.xml`):

```powershell
mvn clean package
```

Para copiar el WAR al directorio configurado en el perfil Maven `deploy-local`:

```powershell
mvn clean install -Pdeploy-local
```

Ese perfil copia el WAR a una ruta local de Tomcat definida en `pom.xml`; actualízala si Tomcat está instalado en otra ubicación.

El perfil `deploy-web` copia archivos de la aplicación web (`.xhtml`, `.css` y recursos) al directorio expandido bajo `CATALINA_HOME`. No copia el WAR completo ni las clases Java, así que para desplegar cambios de backend debe usarse un despliegue del WAR (por ejemplo, `deploy-local` con la ruta configurada, o copiar manualmente `target/proto-builder.war` a Tomcat).
