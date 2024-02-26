package entities;

import javafx.scene.control.TextField;

public class Participation {
    private  int idP ;
    private  int idUser ;
    private int idEvent;
    private int nbrPlace;


    public Participation(int idUser, int idEvent, int nbrPlace) {
        this.idUser = idUser;
        this.idEvent = idEvent;
        this.nbrPlace = nbrPlace;
    }

    public Participation(int idP, int idUser, int idEvent, int nbrPlace) {
        this.idP = idP;
        this.idUser = idUser;
        this.idEvent = idEvent;
        this.nbrPlace = nbrPlace;
    }


    public Participation() {
    }




    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public int getNbrPlace() {
        return nbrPlace;
    }

    public void setNbrPlace(int nbrPlace) {
        this.nbrPlace = nbrPlace;
    }

    public int getIdP() {
        return idP;
    }

    public void setIdP(int idP) {
        this.idP = idP;
    }



    @Override
    public String toString() {
        return "Participation{" +
                "idP=" + idP +
                ", idUser=" + idUser +
                ", idEvent=" + idEvent +
                ", nbrPlace=" + nbrPlace +
                '}';
    }
}
