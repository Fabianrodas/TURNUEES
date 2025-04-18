package ec.edu.uees.Modelo;

public class Video {
    private String url;
    private String titulo;

    public Video(String url, String titulo) {
        this.url = url;
        this.titulo = titulo;
    }

    public Video() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String toString() {
        return "Video{" + "url=" + url + ", titulo=" + titulo + '}';
    }
}
