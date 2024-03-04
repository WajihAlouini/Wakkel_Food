package entities;


import java.sql.Date;
import java.time.LocalDate;

public class Evenement {


    private int idEvent;
    private String EventName;
    private LocalDate DateDebut;
    private LocalDate DateFin ;

   /* public Evenement(int idEvent, int idCodePromo, String EventName, Date DateDebut, Date DateFin) {
        this.idEvent = idEvent;
        this.idCodePromo = idCodePromo;
        this.EventName = EventName;
        this.DateDebut = DateDebut;
        this.DateFin = DateFin;
    }*/
    public Evenement( String EventName,LocalDate DateDebut, LocalDate DateFin) {

        this.EventName = EventName;
        this.DateDebut = DateDebut;
        this.DateFin = DateFin;
    }

    public Evenement() {

    }

    public Evenement(int idEvent, String eventName, LocalDate dateDebut, LocalDate dateFin) {
        this.idEvent = idEvent;
        EventName = eventName;
        DateDebut = dateDebut;
        DateFin = dateFin;
    }

    public Evenement(int idEvent, int idUser, int nbrPlace) {
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }



    public String getEventName() {
        return EventName;
    }

    public void setEventName(String EventName) {
        this.EventName = EventName;
    }

    public LocalDate getDateDebut() {
        return DateDebut;
    }

    public void setDateDebut(Date DateDebut) {
        this.DateDebut = DateDebut.toLocalDate();
    }

    public LocalDate getDateFin() {
        return DateFin;
    }

    public void setDateFin(Date DateFin) {
        this.DateFin = DateFin.toLocalDate();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Evenement{" +
                "idEvent=" + idEvent +

                ", EventName='" + EventName + '\'' +
                ", DateDebut=" + DateDebut +
                ", DateFin=" + DateFin +
                '}';
    }


};
