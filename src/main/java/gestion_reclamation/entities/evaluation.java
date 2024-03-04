package gestion_reclamation.entities;
import java.sql.Date;

public class evaluation {
    private int idEvaluation;

    public int getIdEvaluation() {
        return idEvaluation;
    }
    private int id_evaluation;

    private int id_commande;
    private Date date;
    private int note;
    private String commentaire;

    public evaluation(int id_evaluation, int id_commande, Date date, int note, String commentaire) {
        this.id_evaluation = id_evaluation;
        this.id_commande = id_commande;
        this.date = date;
        this.note = note;
        this.commentaire = commentaire;
    }

    public evaluation() {

    }

    public int getId_evaluation() {
        return id_evaluation;
    }

    public void setId_evaluation(int id_evaluation) {
        this.id_evaluation = id_evaluation;
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

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    @Override
    public String toString() {
        return "evaluation{" +
                "id_evaluation=" + id_evaluation +
                ", id_commande=" + id_commande +
                ", date=" + date +
                ", note=" + note +
                ", commentaire='" + commentaire + '\'' +
                '}';
    }

    public String getIdCommande() {
        return null;
    }




}
