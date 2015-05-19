/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java.resource;

import java.modele.Produit;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author plasse
 */
public class ProduitResource {
  private int id;

  /** Creates a new instance of ProduitResource */
  private ProduitResource(int id) {
    if (id <= 0) {
      Response response = Response
          .status(400)
          .header("Content-Type", "text/plain")
          .entity("idNotPositiveInteger").build();
      throw new WebApplicationException(response);
    }
    else {
      this.id = id;
    }
  }

  /** Get instance of the ProduitResource */
  public static ProduitResource getInstance(int id) {
    // The user may use some kind of persistence mechanism
    // to store and restore instances of ProduitResource class.
    return new ProduitResource(id);
  }

  /**
   * Retrieves representation of an instance of
   * resource.ProduitResource
   *
   * @return an instance of modele.Produit
   */
  @GET
  @Produces("application/xml")
  public Produit getXml() throws SQLException {
    System.out.println("get produits/" + id);
    Produit result = Produit.getById(id);
    if (result == null) {
      throw new WebApplicationException(404);
    }
    return result;
  }

  /**
   * Met à jour le produit
   *
   * @param content representation for the resource
   * @return an HTTP response with content of the updated or created
   * resource.
   */
  @PUT
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void put(@FormParam("nom") String nom,
      @FormParam("prix") double prix) {
    Produit produit = new Produit(id, nom, prix);
    try {
      produit.update();
    }
    catch (SQLException exc) {
      if (exc.getErrorCode() == 1062) {
        Response response = Response
            .status(409)
            .entity("nomDoublon").build();
        throw new WebApplicationException(response);
      }
      else {
       Response response = Response
            .status(500)
            .entity("Problème avec la base de données : " + exc.getMessage())
           .build();
        throw new WebApplicationException(response);
      }
    }
  }

  /**
   * DELETE method for resource ProduitResource
   */
  @DELETE
  public void delete() {
  }
}
