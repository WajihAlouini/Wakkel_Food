package service;

import entities.Evenement;
import utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementService implements IEvenement <Evenement> {
    Connection cnx= DataSource.getInstance().getCnx();


    public void ajouter(Evenement e) {
        String req = "INSERT INTO evenement ( EventName, DateDebut, DateFin) " +
                "VALUES ('" + e.getEventName() + "', '" +
                e.getDateDebut() + "', '" + e.getDateFin() + "')";

        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Event Added successfully!");
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void Modifier(Evenement e) {

    }


    /*@Override
     public void Modifier(Evenement evenement) {
         String sql = "UPDATE Evenement SET idEvent=?, EventName= ?,DateDebut=?,DateFin=? WHERE idEvent = ?";
         try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {

             PreparedStatement ps = cnx.prepareStatement(sql);



             ps.setString(1, evenement.getEventName());
             ps.setDate(2, java.sql.Date.valueOf(evenement.getDateDebut()));
             ps.setDate(3, java.sql.Date.valueOf(evenement.getDateFin()));





             ps.executeUpdate();
             System.out.println("Event updated successfully!");

         } catch (SQLException p) {
             p.printStackTrace();
         }

     }*/
    @Override
    public void supprimer(Evenement evenement) {

    }

    @Override
    public void modifier(Evenement e) {
        String sql = "UPDATE Evenement SET  EventName= ?,DateDebut=?,DateFin=? WHERE idEvent = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, e.getEventName());
            ps.setDate(2, Date.valueOf(e.getDateDebut()));
            ps.setDate(3, Date.valueOf(e.getDateFin()));
            ps.setInt(4, e.getIdEvent());  // Set the ID for the WHERE clause
            int rowsUpdated = ps.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);
            System.out.println("Event updated successfully!");
        } catch (SQLException p) {
            p.printStackTrace();
        }
    }





    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM evenement WHERE idEvent = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Event Deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Evenement> affichage() {

        List<Evenement> Evenements= new ArrayList<>();
        try {

            String req = "SELECT * FROM evenement";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Evenement e = new Evenement();
                e.setIdEvent(rs.getInt(1));

                e.setEventName(rs.getString(2));
                e.setDateDebut(rs.getDate("DateDebut"));
                e.setDateFin(rs.getDate("DateFin"));

                Evenements.add(e);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return Evenements;
    }


    @Override
    public List<Evenement> readAll() {
        String query = "SELECT * FROM evenement";
        List<Evenement> list = new ArrayList<>();

        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Evenement e = new Evenement();
                e.setIdEvent(rs.getInt("idEvent"));

                e.setEventName(rs.getString("EventName"));
                e.setDateDebut(rs.getDate("DateDebut"));
                e.setDateFin(rs.getDate("DateFin"));

                list.add(e);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list;
    }
    public List<Evenement> getAllEvenements() {
        return affichage(); // Assuming 'affichage' method retrieves all events from the database
    }

    @Override
    public Evenement readById(int id) {
        return null;
    }
}