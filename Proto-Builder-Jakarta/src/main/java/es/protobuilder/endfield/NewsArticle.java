package es.protobuilder.endfield;

import java.io.Serializable;
import java.util.List;

public class NewsArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private String summary;
    private String category;
    private String author;
    private String publishedAt;
    private String image;
    private List<String> content;

    public NewsArticle(String id, String title, String summary, String category,
                       String author, String publishedAt, String image, List<String> content) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.category = category;
        this.author = author;
        this.publishedAt = publishedAt;
        this.image = image;
        this.content = List.copyOf(content);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getCategory() {
        return category;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getImage() {
        return image;
    }

    public List<String> getContent() {
        return content;
    }
}
