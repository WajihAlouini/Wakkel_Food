package entities;

import javafx.scene.control.TextField;


    public class Participation {
        private int idP;
        private int idUser;
        private int idEvent;
        private int nbrPlace;
        private String emailUser; // Add emailUser property

        public Participation(int idUser, int nbrPlace, String emailUser) {
            this.idUser = idUser;
            this.nbrPlace = nbrPlace;
            this.emailUser = emailUser;
        }

        public Participation(int idP, int idUser, int idEvent, int nbrPlace, String emailUser) {
            this.idP = idP;
            this.idUser = idUser;
            this.idEvent = idEvent;
            this.nbrPlace = nbrPlace;
            this.emailUser = emailUser;
        }

        public Participation(int idUser, int idEvent, int nbrPlace, String emailUser) {
            this.idUser = idUser;
            this.idEvent = idEvent;
            this.nbrPlace = nbrPlace;
            this.emailUser = emailUser;
        }

        public Participation() {
        }

        public String getEmailUser() {
            return emailUser;
        }

        public void setEmailUser(String emailUser) {
            this.emailUser = emailUser;
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
                    ", emailUser='" + emailUser + '\'' +
                    '}';
        }
    }