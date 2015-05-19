package java.modele;

import java.sql.*;

public class Database {

  protected static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
  protected static final String URL = "jdbc:mysql://localhost/produits";
  protected static final String USER = "root";
  protected static final String PASSWORD = "";

  public enum SortOrder { ASC, DESC; }

  static {
    // Chargement du pilote
    // Ne doit avoir lieu qu'une seule fois
    try {
      Class.forName(DRIVER_NAME).newInstance();
      System.out.println("*** Driver loaded.");
    }
    catch (ClassNotFoundException e) {
      System.err.println("*** ERROR: Driver " + DRIVER_NAME + " not found");
    }
    catch (InstantiationException e) {
      System.err.println("*** ERROR: Impossible to create an instance of " + DRIVER_NAME);
      System.err.println(e.getMessage());
    }
    catch (IllegalAccessException e) {
      System.err.println("*** ERROR: Impossible to create an instance of " + DRIVER_NAME);
      System.err.println(e.getMessage());
    }
  }

  /** Fournit une connexion à la base de données.
   * Ne fait pas appel à un pool de connexion, mâme si cela est envisageable plus tard
   * (ne changerait rien à l'appel de la méthode)
   * @throws java.sql.SQLException
   */
  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASSWORD);
  }

  /** Ferme une connexion, si elle est non null.
   * Si une exception SQLException est levée, ne la propage pas.
   */
  public static void close(final Connection cx) {
    if (cx != null) {
      try {
        cx.close();
      }
      catch (SQLException exc) {
        System.err.println("Impossible to close connection");
        System.err.println(exc.getMessage());
      }
    }
  }

  /** Ferme une requête SQL, si elle est non null.
   * Si une exception SQLException est levée, ne la propage pas.
   */
  public static void close(final Statement st) {
    if (st != null) {
      try {
        st.close();
      }
      catch (SQLException exc) {
        System.err.println("Impossible to close statement");
        System.err.println(exc.getMessage());
      }
    }
  }

  /** Ferme un ResultSet, s'il est non null.
   * Si une exception SQLException est levée, ne la propage pas.
   */
  public static void close(final ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      }
      catch (SQLException exc) {
        System.err.println("Impossible to close resultSet");
        System.err.println(exc.getMessage());
      }
    }
  }
}
