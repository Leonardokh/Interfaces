package es.protobuilder.beans;

import es.protobuilder.endfield.NewsArticle;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("newsBean")
@ViewScoped
public class NewsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private NewsServiceBean newsService;

    private String selectedArticleId;

    public void openArticle(String id) {
        if (newsService.findById(id) == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "No se encontró la noticia seleccionada."));
            return;
        }
        selectedArticleId = id;
    }

    public void showList() {
        selectedArticleId = null;
    }

    public boolean isShowingArticle() {
        return selectedArticleId != null;
    }

    public NewsArticle getSelectedArticle() {
        return selectedArticleId == null ? null : newsService.findById(selectedArticleId);
    }

    public NewsServiceBean getNewsService() {
        return newsService;
    }
}
