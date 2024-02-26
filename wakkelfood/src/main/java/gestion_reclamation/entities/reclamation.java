package gestion_reclamation.entities;
import javafx.collections.ObservableList;

import java.sql.Date;

public class reclamation {
    private int id_reclamation;
    private int id_commande;
    private Date date;
    private String type;
    private String decription;
    private String statut;

    public reclamation(int id_reclamation, int id_commande, Date date, String type, String decription, String statut) {
        this.id_reclamation = id_reclamation;
        this.id_commande = id_commande;
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

    public int getId_commande() {
        return id_commande;
    }

    public void setId_commande(int id_commande) {
        this.id_commande = id_commande;
    }

    public Date getDate() {
        return date;
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

    @Override
    public String toString() {
        return "reclamation{" +
                "id_reclamation=" + id_reclamation +
                ", id_commande=" + id_commande +
                ", date=" + date +
                ", type='" + type + '\'' +
                ", decription='" + decription + '\'' +
                ", statut='" + statut + '\'' +
                '}';
    }
}




