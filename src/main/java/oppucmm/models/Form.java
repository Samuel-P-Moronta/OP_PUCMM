package oppucmm.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Form implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 30)
    private String name;
    @Column(length = 30)
    private String sector;
    @Column(length = 30)
    private String academicLevel;
    //New
    @OneToOne
    private User user;
    //New
    @Embedded
    private Location location;
/*
    @OneToOne()
    private Location location;*/

    public Form() {
    }

    public Form(String name,String sector,String academicLevel){
        this.name = name;
        this.sector = sector;
        this.academicLevel = academicLevel;
    }
    /*To create form*/
    public Form(String name, String sector, String academicLevel, User user, Location location) {
        this.name = name;
        this.sector = sector;
        this.academicLevel = academicLevel;
        this.user = user;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getAcademicLevel() {
        return academicLevel;
    }

    public void setAcademicLevel(String academicLevel) {
        this.academicLevel = academicLevel;
    }
    public User getUser() { return user; }

    public void setUser(User user) {
        this.user = user;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
