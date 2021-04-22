package oppucmm.models;

import oppucmm.controllers.Controller;

public class FormAux {

    private int id;
    private String fotoBase64;
    private String fullName;
    private String sector;
    private String academicLevel;
    private Double latitude;
    private Double longitude;
    private String user;

    /*Empty constructor*/
    public FormAux() {
    }
    /*Principal Constructor*/
    public FormAux(String fotoBase64,String fullName, String sector, String academicLevel, Double latitude, Double longitude, String user) {
        this.id = id;
        this.fotoBase64 = fotoBase64;
        this.fullName = fullName;
        this.sector = sector;
        this.academicLevel = academicLevel;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
    }
    /*Getters and Setters*/
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getSector() { return sector; }

    public void setSector(String sector) { this.sector = sector; }

    public String getAcademicLevel() { return academicLevel; }

    public Double getLatitude() { return latitude; }


    public Double getLongitude() { return longitude; }

    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getUser() { return user; }

    public void setUser(String user) { this.user = user; }

    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }
}

