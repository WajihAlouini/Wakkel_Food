package entities;
import java.sql.Date;

public class evaluation {
    private String email_e;

    public String getEmail_e() {
        return email_e;
    }
    private int id_evaluation;

    private Date date;
    private int note;
    private String commentaire;

    public evaluation(int id_evaluation, String email_e, Date date, int note, String commentaire) {
        this.id_evaluation = id_evaluation;
        this.email_e = email_e;
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
                ", email_e=" + email_e +
                ", date=" + date +
                ", note=" + note +
                ", commentaire='" + commentaire + '\'' +
                '}';
    }

    public String getIdCommande() {
        return null;
    }




}
