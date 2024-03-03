package entities;

public class client {
    private int id_client;
    private String nom;
    private String prenom;
    private String email;
    private String numero;
    private String adresse;
    private String newpassword;
    private RoleEnum role;
    private GenreEnum genre;
    private String image;
    private boolean banned;

    public enum RoleEnum {client, admin}
    public enum GenreEnum {homme, femme}

    public client(String nom, String prenom, String email,String newpassword, String numero, String adresse, GenreEnum genre, String image, RoleEnum role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.newpassword = newpassword;
        this.numero = numero;
        this.adresse = adresse;
        this.genre = genre;
        this.image = image;
        this.role = role;
    }
    public client(String nom, String prenom, String email, String numero, String adresse,String newpassword, GenreEnum genre, String image) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numero = numero;
        this.adresse = adresse;
        this.newpassword = newpassword;
        this.genre = genre;
        this.image = image;
    }
    public client(String nom, String prenom, String email, String numero, String adresse, String newpassword) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numero = numero;
        this.adresse = adresse;
        this.newpassword=newpassword;
    }

    public client(String nom, String prenom, String email, String numero, String adresse, String image,GenreEnum genre) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numero = numero;
        this.adresse = adresse;
        this.image=image;
        this.genre=genre;
    }


    public int getId() {
        return id_client;
    }
    public void setId(int id) {
        this.id_client = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public String getAdresse() {
        return adresse;
    }
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public String getNewpassword() {
        return newpassword;
    }
    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }
    public RoleEnum getRole() {return role;}
    public void setRole(RoleEnum role) {this.role = role;}
    public GenreEnum getGenre() {return genre;}
    public void setGenre(GenreEnum genre) {this.genre = genre;}
    public String getImage() {return image;}
    public void setImage(String image) {this.image = image;}
    public boolean isBanned() {return banned;}
    public void setBanned(boolean banned) {this.banned = banned;}

    @Override
    public String toString() {
        return "client{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", newpassword='" + newpassword + '\'' +
                ", numero='" + numero + '\'' +
                ", adresse='" + adresse + '\'' +
                ", genre='" + genre + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
