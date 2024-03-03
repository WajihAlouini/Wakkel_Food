package service;

import entities.client;
import utils.DataSource;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class clientService implements IService<client> {

    private Connection conn;
    private Statement ste;
    private PreparedStatement pst;
    private static final String AES = "AES";
    private static final String SECRET_KEY = "ThisIsASecretKey";

    public clientService() {
        conn= DataSource.getInstance().getCnx();
    }


    public boolean emailExists(String email) {
        String query = "SELECT * FROM client WHERE email_c = '" + email + "'";
        try {
            ste = conn.createStatement();
            ResultSet rs = ste.executeQuery(query);
            return rs.next(); // Si au moins une ligne est retournée, cela signifie que l'email existe déjà
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean numeroExists(String numero) {
        String query = "SELECT * FROM client WHERE numero_c = '" + numero + "'";
        try {
            ste = conn.createStatement();
            ResultSet rs = ste.executeQuery(query);
            return rs.next(); // Si au moins une ligne est retournée, cela signifie que le numéro existe déjà
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void add(String nom, String prenom, String email, String numero, String adresse, String password, client.GenreEnum genre, String imagePath) {
        String requete = "INSERT INTO client (nom_c, prenom_c, email_c, numero_c, adresse_c, mdp, genre, image) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = conn.prepareStatement(requete);
            pst.setString(1, nom);
            pst.setString(2, prenom);
            pst.setString(3, email);
            pst.setString(4, numero);
            pst.setString(5, adresse);
            pst.setString(6, password);
            pst.setString(7, genre.name()); // Save genre as its enum name

            // Modifier le chemin d'accès à l'image si nécessaire
            String modifiedImagePath = imagePath;
            if (imagePath != null) {
                // Supprimer "file:/" du chemin d'accès
                modifiedImagePath = modifiedImagePath.replace("file:/", "");
                // Remplacer "legion%205" par "legion 5"
                modifiedImagePath = modifiedImagePath.replace("legion%205", "legion 5");
                // Remplacer "/" par "\\"
                modifiedImagePath = modifiedImagePath.replace("/", "\\\\");
            }
            pst.setString(8, modifiedImagePath);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void delete(String email){
        String deleteQuery = "DELETE FROM client WHERE email_c = ?";
        try {
            // Initialize the statement
            pst = conn.prepareStatement(deleteQuery);
            pst.setString(1, email);

            // Execute the delete query
            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Client with email: " + email + " deleted successfully.");
            } else {
                System.out.println("No client found with email: " + email);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(client client) {
        String updateQuery = "UPDATE client SET nom_c = ?, prenom_c = ?, email_c = ?, numero_c = ?, adresse_c = ?, mdp = ?, genre = ?, image = ? WHERE email_c = ?";
        try {
            pst = conn.prepareStatement(updateQuery);
            pst.setString(1, client.getNom());
            pst.setString(2, client.getPrenom());
            pst.setString(3, client.getEmail());
            pst.setString(4, client.getNumero());
            pst.setString(5, client.getAdresse());
            pst.setString(6, client.getNewpassword());
            pst.setString(7, client.getGenre().name()); // Correction ici
            pst.setString(8, client.getImage());
            pst.setString(9, client.getEmail());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Client avec email: " + client.getEmail() + " mis à jour avec succès.");
            } else {
                System.out.println("Aucun client trouvé avec l'email: " + client.getEmail() + ".");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public List<client> readAll() {
        String requete = "SELECT nom_c, prenom_c, email_c, mdp, numero_c, adresse_c, genre, image, role FROM client";
        List<client> list = new ArrayList<>();
        try {
            ste = conn.createStatement();
            ResultSet rs = ste.executeQuery(requete);
            while (rs.next()) {
                String nom = rs.getString("nom_c");
                String prenom = rs.getString("prenom_c");
                String email = rs.getString("email_c");
                String mdp = rs.getString("mdp");
                String numero = rs.getString("numero_c");
                String adresse = rs.getString("adresse_c");
                String genreString = rs.getString("genre").toLowerCase();
                client.GenreEnum genre = client.GenreEnum.valueOf(genreString);
                String image = rs.getString("image");
                client.RoleEnum role = rs.getString("role") != null ? client.RoleEnum.valueOf(rs.getString("role")) : client.RoleEnum.client; // Set a default value if role is null
                list.add(new client(nom, prenom, email, mdp, numero, adresse, genre, image, role));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // Méthode pour valider la connexion
    public boolean validelogin(String email, String password) {
        String query = "SELECT * FROM client WHERE email_c = '" + email + "'";
        try {
            pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                // Utilisateur trouvé dans la base de données, comparer le mot de passe
                if (rs.getString("mdp").equals(encrypt(password))) {
                    // Mot de passe correct, authentification réussie
                    return true;
                } else {
                    // Mot de passe incorrect
                    return false;
                }
            } else {
                // Aucun utilisateur trouvé avec cet email
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public client getClientInfoByMail(String email) {
        String query = "SELECT nom_c, prenom_c, numero_c, email_c, adresse_c, genre, image FROM client WHERE email_c = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, email);
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                String nom = resultSet.getString("nom_c");
                String prenom = resultSet.getString("prenom_c");
                email = resultSet.getString("email_c");
                String numero = resultSet.getString("numero_c");
                String adresse = resultSet.getString("adresse_c");
                String image = resultSet.getString("image");
                client.GenreEnum genre = client.GenreEnum.valueOf(resultSet.getString("genre"));
                return new client(nom, prenom, email, numero, adresse,image, genre);
            }
            resultSet.close();
            pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public String getRole(String email) {
        String query = "SELECT role FROM client WHERE email_c = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            } else {
                // Aucun utilisateur trouvé avec cet e-mail
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public boolean isClientBanned(String email) {
        String query = "SELECT banned FROM client WHERE email_c = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("banned");
            } else {
                // Aucun utilisateur trouvé avec cet email
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void banClient(String email, boolean banned) {
        String query = "UPDATE client SET banned = ? WHERE email_c = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setBoolean(1, banned);
            pst.setString(2, email);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public long countByGenre(client.GenreEnum genre) {
        String query = "SELECT COUNT(*) FROM client WHERE genre = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, genre.name());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                return 0; // Si aucun enregistrement trouvé
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    // Méthode pour crypter le mot de passe
    public static String encrypt(String password) {
        try {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedByteValue = cipher.doFinal(password.getBytes("utf-8"));
            return Base64.getEncoder().encodeToString(encryptedByteValue);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Méthode pour décrypter le mot de passe
    public static String decrypt(String encryptedPassword) {
        try {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");  // Utilisation de PKCS5Padding
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedByteValue = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
            return new String(decryptedByteValue, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getPasswordByEmail(String email) {
        String query = "SELECT mdp FROM client WHERE email_c = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String encryptedPassword = rs.getString("mdp");
                return decrypt(encryptedPassword);
            } else {
                // No user found with this email
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Méthode pour générer une clé secrète pour le chiffrement AES
    private static Key generateKey() throws Exception {
        return new SecretKeySpec(SECRET_KEY.getBytes(), AES);
    }
}