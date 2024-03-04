    package service;
    import entities.reclamation;
    import utils.DataSource;

    import java.io.InputStream;
    import java.sql.*;
    import java.time.LocalDate;
    import java.util.ArrayList;
    import java.util.List;

    public class reclamationservice implements service.IServiceReclamation<reclamation> {
        private Connection conn;
        private PreparedStatement pst;

        public reclamationservice() {
            conn = DataSource.getInstance().getCnx();
        }

        @Override
        public void add(reclamation r, InputStream imageInputStream) {
            int latestIdCommande = getLatestIdCommande();
            String query = "INSERT INTO Reclamation (id_reclamation, email_r, date, type, description, statut, image) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try {
                pst = conn.prepareStatement(query);
                pst.setInt(1, r.getId_reclamation());
                pst.setString(2, r.getEmail_r());
                pst.setDate(3, Date.valueOf(LocalDate.now()));
                pst.setString(4, r.getType());
                pst.setString(5, r.getDecription());

                if (r.getStatut() != null) {
                    pst.setString(6, r.getStatut());
                } else {
                    pst.setString(6, "Non resolu");
                }

                if (imageInputStream != null) {
                    System.out.println("Image InputStream available");
                    pst.setBlob(7, imageInputStream);
                } else {
                    System.out.println("Image InputStream is null");
                    pst.setNull(7, java.sql.Types.BLOB);
                }

                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Reclamation added successfully");
                } else {
                    System.out.println("Failed to add reclamation");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }


        private int getLatestIdCommande() {
            String selectLatestIdCommandeQuery = "SELECT id_commande FROM commande ORDER BY id_commande DESC LIMIT 1";

            try (PreparedStatement selectStatement = conn.prepareStatement(selectLatestIdCommandeQuery);
                 ResultSet resultSet = selectStatement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt("id_commande");
                } else {
                    System.out.println("No records found in commande table");
                    return -1;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return -1;
            }
        }

        @Override
        public void add(reclamation reclamation) {

        }

        @Override
        public void delete(int id) {
            String query = "DELETE FROM reclamation WHERE id_reclamation = ?";
            try {
                pst = conn.prepareStatement(query);
                pst.setInt(1, id);
                pst.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void update(reclamation reclamation) {
            String query = "UPDATE reclamation SET email_r = ?, date = ?, type = ?, description = ?, statut = ?, image = ? WHERE id_reclamation = ?";
            try {
                pst = conn.prepareStatement(query);
                pst.setString(1, reclamation.getEmail_r());
                pst.setDate(2, reclamation.getDate());
                pst.setString(3, reclamation.getType());
                pst.setString(4, reclamation.getDecription());
                pst.setString(5, reclamation.getStatut());
                pst.setBlob(6, reclamation.getImageData());
                pst.setInt(7, reclamation.getId_reclamation());
                pst.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public List<reclamation> readAll() {
            List<reclamation> list = new ArrayList<>();
            try {
                pst = conn.prepareStatement("SELECT * FROM reclamation");
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    reclamation rec = new reclamation(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getDate(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(6)
                    );
                    Blob imageData = rs.getBlob(7);
                    rec.setImageData(imageData);
                    list.add(rec);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("Number of reclamations retrieved: " + list.size());
            return list;
        }

        @Override
        public reclamation readById(int id) {
            return null;
        }
    }
