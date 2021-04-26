package oppucmm.models;


import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Lob
    private String fotoBase64;
    @ManyToOne(fetch = FetchType.EAGER)
    private Form formAux;

    public Photo() {
        fotoBase64 = "";
    }
    public Photo( String fotoBase64){
        this.fotoBase64 = fotoBase64;
    }
    public Form getFormAux() {
        return formAux;
    }

    public void setFormAux(Form formAux) {
        this.formAux = formAux;
    }

    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }
}

