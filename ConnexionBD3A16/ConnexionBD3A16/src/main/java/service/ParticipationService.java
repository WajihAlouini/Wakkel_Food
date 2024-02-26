package service;
import entities.Evenement;
import entities.Participation;
import utils.DataSource;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class ParticipationService implements IEvenement <Participation> {
    Connection cnx= DataSource.getInstance().getCnx();

    @Override
    public void ajouter(Participation p) {
        String req = "INSERT INTO participation (idEvent,idUser, nbrPlace) " +
                "VALUES (" + p.getIdUser() + ", " + p.getIdEvent() + ", " +
                p.getNbrPlace() + ")";

        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Reservation Added successfully!");
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
    }




    @Override
    public void supprimer(Participation participation) {

    }

    @Override
    public void modifier(Participation participation) {

    }

    @Override

    public void Modifier(Participation p) {
        String sql = "UPDATE participation SET idUser=?,idEvent=?, nbrPlace= ? WHERE idP = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1,p.getIdUser());
            ps.setInt(2, p.getIdEvent());

            ps.setInt(3, p.getNbrPlace());
            ps.setInt(4,p.getIdP());


            int rowsUpdated = ps.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);
            System.out.println("Participation updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Participation> readAll() {
        String query = "SELECT * FROM participation";
        List<Participation> list = new ArrayList<>();

        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Participation p = new Participation();
                p.setIdP(rs.getInt("idP"));
                p.setIdEvent(rs.getInt("idE"));
                p.setIdUser(rs.getInt("idU"));
                p.setNbrPlace(rs.getInt("nbrP"));


                list.add(p);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list;
    }

    @Override
    public Participation readById(int id) {
        return null;
    }


    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM participation WHERE idP = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Participation Deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}












