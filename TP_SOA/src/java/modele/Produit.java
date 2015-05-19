package java.modele;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Produit persistent en base de données.
 * <br/>Le nom est toujours renseigné (ni null, ni vide), et le prix
 * est strictement positif.
 */
@XmlRootElement
public class Produit {

  /** N° du produit. Peut etre null, d'ou le type Integer */
  private Integer id;
  private String nom;
  private double prix;

  /** Constructeur sans paramètre, requis par JAXB */
  public Produit() {
  }
  /** Requiert nom ni null ni vide et prix positif */
  public Produit(Integer id, String nom, double prix) {
    assert nom != null && !nom.matches("^\\s*$");
    assert prix > 0;
    this.id = id;
    this.nom = nom;
    this.prix = prix;
  }

  public Integer getId() {
    return id;
  }

  public String getNom() {
    return nom;
  }

  public double getPrix() {
    return prix;
  }

  /** Requiert id != null */
  public void setId(Integer id) {
    assert id != null;
    this.id = id;
  }

  /** Requiert nom ni null ni vide */
  public void setNom(String nom) {
    assert nom != null && !nom.matches("^\\s*$");
    this.nom = nom;
  }

  /** Requiert prix > 0 */
  public void setPrix(double prix) {
    assert prix > 0;
    this.prix = prix;
  }

  @Override
  public boolean equals(Object autre) {
    boolean result = false;
    if (autre.getClass() == this.getClass()) {
      Produit p = (Produit) autre;
      result = (p.id == id && p.nom.equals(nom) && p.prix == prix);
    }
    return result;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
    hash = 97 * hash + (this.nom != null ? this.nom.hashCode() : 0);
    hash = 97 * hash + (int) (Double.doubleToLongBits(this.prix) ^ (Double.doubleToLongBits(this.prix) >>> 32));
    return hash;
  }

  public static Produit getById(int unNo) throws SQLException {
    Produit result = null;
    Connection connection = Database.getConnection();
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM produit WHERE no_produit=" + unNo);
    if (rs.next()) {
      result = new Produit(rs.getInt("no_produit"), rs.getString("nom"), rs.getDouble("prix"));
    }
    rs.close();
    stmt.close();
    connection.close();
    return result;
  }

  public static Produit getByNom(String unNom) throws SQLException {
    Produit result = null;
    Connection connection = Database.getConnection();
    String sql = "SELECT * FROM produit WHERE nom=?";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setString(1, unNom);
    ResultSet rs = stmt.executeQuery();
    if (rs.next()) {
      result = new Produit(rs.getInt("no_produit"), rs.getString("nom"), rs.getDouble("prix"));
    }
    rs.close();
    stmt.close();
    connection.close();
    return result;
  }

  /**
   * Liste des produits dont le nom commence par debut, triés par
   * ordre alphabétique. Seuls les nbResultats premiers résultats sont
   * retournés.
   *
   * @param debut (pas null)
   * @param nbResultats (positif)
   * @return
   * @throws SQLException
   */
  public static List<Produit> getByDebutNom(String debut,
      int nbResultats) throws SQLException {
    assert debut != null;
    assert nbResultats > 0;
    List<Produit> result = new ArrayList<Produit>();
    Connection connection = Database.getConnection();
    String sql = "SELECT * FROM produit WHERE nom LIKE ? ORDER BY nom ASC LIMIT 0, ?";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setString(1, debut + "%");
    stmt.setInt(2, nbResultats);
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      result.add(new Produit(rs.getInt("no_produit"), rs.getString("nom"), rs.getDouble("prix")));
    }
    rs.close();
    stmt.close();
    connection.close();
    return result;
  }

  /** Met a jour le produit.
   * <br/>Requiert que le nom est renseigné (ni null ni vide)
   * <br/>Requiert que le nom n'existe pas déjà : Produit.getNom(nom)
   * == null || Product.getNom(nom).getId().equals(id)
   * <br/>Requiert que le prix soit positif.
   *
   * @throws java.sql.SQLException
   */
  public void update() throws SQLException {
    assert prix > 0;
    assert nom != null && !nom.matches("/^ \t\n\r$");
    Connection connection = Database.getConnection();
    String sql = "UPDATE produit SET nom=?, prix=? WHERE no_produit=?";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setString(1, nom);
    stmt.setDouble(2, prix);
    stmt.setInt(3, id);
    stmt.executeUpdate();
    stmt.close();
    connection.close();
  }

  /** Inserer le produit, en ignorant son id.
   * <br/>Requiert que le nom est renseigné (ni null ni vide)
   * <br/>Requiert que le nom n'existe pas déjà : Product.getNom(nom)
   * == null || Product.getNom(nom).getId() est le même que id
   * <br/>Requiert que le prix soit positif.
   *
   * @throws java.sql.SQLException s'il existe un produit de ce nom
   */
  public void insert() throws SQLException {
    assert prix > 0;
    assert nom != null && !nom.matches("/^ \t\n\r$");
    Connection connection = Database.getConnection();
    // Commencer une transaction
    connection.setAutoCommit(false);
    try {
      // Inserer le produit
      String sql = "INSERT INTO produit(nom, prix) VALUES(?, ?)";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, nom);
      stmt.setDouble(2, prix);
      stmt.executeUpdate();
      stmt.close();
      // Recuperer le id
      Statement maxStmt = connection.createStatement();
      ResultSet rs = maxStmt.executeQuery("SELECT MAX(no_produit) AS id FROM produit");
      rs.next();
      id = rs.getInt("id");
      rs.close();
      maxStmt.close();
      // Valider
      connection.commit();
    }
    catch (SQLException exc) {
      connection.rollback();
      exc.printStackTrace();
      throw exc;
    } finally {
      connection.close();
    }
  }

  /** Supprime le produit de id unId de la base de données. Renvoie
   * true si un produit est supprimé, false s'il n'existe pas.
   *
   * @throws java.sql.SQLException
   */
  public static boolean delete(int unId) throws SQLException {
    Connection connection = Database.getConnection();
    String sql = "DELETE FROM produit WHERE no_produit=?";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setInt(1, unId);
    int result = stmt.executeUpdate();
    stmt.close();
    connection.close();
    return result == 1;
  }
}
