package es.protobuilder.beans;

import es.protobuilder.endfield.NewsArticle;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named("newsService")
@ApplicationScoped
public class NewsServiceBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<NewsArticle> articles;

    @PostConstruct
    public void init() {
        articles = List.of(
                new NewsArticle(
                        "talos-expedition",
                        "Se amplía la exploración de Talos-II",
                        "Endfield Industries prepara nuevas rutas y puntos de apoyo para las próximas expediciones.",
                        "Operaciones",
                        "Equipo de operaciones",
                        "14 nov 2026 · 10:30",
                        "news/talos-ii.webp",
                        List.of(
                                "Tras completar el reconocimiento del corredor oriental, Endfield Industries ha autorizado una nueva fase de exploración en Talos-II. Los equipos trabajarán en rutas que hasta ahora solo se habían podido observar desde puntos elevados.",
                                "La primera prioridad será establecer estaciones de apoyo con suministros, comunicaciones y refugio. Cada emplazamiento se elegirá después de comprobar la estabilidad del terreno y la disponibilidad de rutas de evacuación.",
                                "Las expediciones avanzarán por etapas. Los operadores registrarán cambios ambientales, obstáculos y recursos útiles, y enviarán sus informes antes de que se autorice el siguiente tramo.",
                                "La dirección recuerda que la planificación puede cambiar según las condiciones encontradas. La seguridad de los equipos tendrá prioridad sobre el calendario previsto."
                        )),
                new NewsArticle(
                        "perlica-field-report",
                        "Perlica coordina un nuevo informe de campo",
                        "El próximo informe reunirá observaciones sobre anomalías, recursos y condiciones de las misiones recientes.",
                        "Investigación",
                        "Oficina de investigación",
                        "13 nov 2026 · 16:00",
                        "news/perlica.webp",
                        List.of(
                                "Perlica ha puesto en marcha una revisión conjunta de los informes recibidos tras las últimas operaciones. El objetivo es que los datos recogidos por los distintos equipos puedan compararse con un mismo criterio.",
                                "Cada registro incluirá la ubicación aproximada, la duración de la observación y las condiciones presentes. Cuando una medición no pueda confirmarse, se marcará como provisional en lugar de tratarse como un resultado definitivo.",
                                "El equipo de investigación también preparará una guía breve para que los operadores documenten incidentes de forma consistente. La guía cubrirá observaciones ambientales, hallazgos de recursos y cambios que puedan afectar a futuras rutas.",
                                "Una vez revisados, los resultados se compartirán con las unidades de operaciones para ayudar a preparar las siguientes expediciones."
                        )),
                new NewsArticle(
                        "support-hub-equipment",
                        "El centro de apoyo presenta equipo de prueba",
                        "Una nueva tanda de armamento experimental pasa a evaluación antes de su posible incorporación al equipo de campo.",
                        "Equipamiento",
                        "Centro de apoyo de Northmarch",
                        "11 nov 2026 · 09:15",
                        "news/weapons.webp",
                        List.of(
                                "El centro de apoyo de Northmarch ha terminado una primera serie de prototipos destinados a pruebas controladas. La evaluación medirá su respuesta en distintos escenarios y su compatibilidad con el equipo que ya utilizan los operadores.",
                                "Las pruebas se realizarán en un entorno supervisado y con objetivos definidos. Los participantes documentarán el manejo, el rendimiento y cualquier incidencia, evitando conclusiones basadas en una única sesión.",
                                "Los comentarios se recopilarán junto con las mediciones del equipo técnico. Si los resultados muestran problemas de fiabilidad o mantenimiento, los prototipos volverán al taller para ser revisados antes de otra prueba.",
                                "El centro publicará una actualización cuando termine la evaluación. Hasta entonces, el material seguirá siendo experimental y no formará parte del inventario operativo."
                        ))
        );
    }

    public List<NewsArticle> getArticles() {
        return articles;
    }

    public NewsArticle findById(String id) {
        return articles.stream()
                .filter(article -> article.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
