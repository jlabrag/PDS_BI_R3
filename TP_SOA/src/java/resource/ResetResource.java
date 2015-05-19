/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java.resource;

import java.modele.Database;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author plasse
 */
@Path("/reset")
public class ResetResource {
  @Context
  private UriInfo context;

  /**
   * POST method for updating or creating an instance of ResetResource
   *
   * @param content representation for the resource
   * @return an HTTP response with content of the updated or created
   * resource.
   */
  @POST
  public void post() {
    try {
      Connection connexion = Database.getConnection();
      CallableStatement cs = connexion.prepareCall("CALL produits.refresh()");
      cs.execute();
      connexion.close();
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
}
