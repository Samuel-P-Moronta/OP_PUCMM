package oppucmm.models;


import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Lob
    private String fotoBase64;

    public Photo() {
        fotoBase64 = "";
    }

    public Photo( String fotoBase64){

        this.fotoBase64 = fotoBase64;
    }

    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }
}

