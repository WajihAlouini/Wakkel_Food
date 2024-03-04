package entities;

import javafx.collections.ObservableList;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

public class reclamation implements Serializable {
    private int id_reclamation;
    private String email_r;


    private Date date;
    private String type;
    private String decription;
    private String statut;
    private Blob imageData;

    public reclamation(int id_reclamation, String email_r, Date date, String type, String decription, String statut) {
        this.id_reclamation = id_reclamation;
        this.email_r = email_r;
        this.date = date;
        this.type = type;
        this.decription = decription;
        this.statut = statut;
    }

    public reclamation(ObservableList<?> items, String text) {
    }

    public int getId_reclamation() {
        return id_reclamation;
    }

    public void setId_reclamation(int id_reclamation) {
        this.id_reclamation = id_reclamation;
    }



    public void setEmail_r(String email_r) {
        this.email_r = email_r;
    }
    public String getEmail_r() {
        return email_r;
    }
    public java.sql.Date getDate() {
        return (java.sql.Date) date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Blob getImageData() {
        return imageData;
    }

    public void setImageData(Blob imageData) {
        this.imageData = imageData;
    }

    @Override
    public String toString() {
        return "reclamation{" +
                "id_reclamation=" + id_reclamation +
                ", email_r=" + email_r +
                ", date=" + date +
                ", type='" + type + '\'' +
                ", decription='" + decription + '\'' +
                ", statut='" + statut + '\'' +
                '}';
    }
}
