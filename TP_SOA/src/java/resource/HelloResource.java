/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java.resource;

import javax.validation.constraints.NotNull;
import javax.validation.executable.ValidateOnExecution;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author plasse
 */
@Path("/hello")
public class HelloResource {
  @Context
  private UriInfo context;

  private static String name = "tout le monde";

  /** Renvoie "Bonjour à xxx de yyy" où
   * xxx est le paramètre "to" (valeur par défaut : "vous")
   * et yyy est le nom stocké dans la classe   */
  @GET
  @Produces("text/html")
  public String get(@DefaultValue("vous") @QueryParam("to") String to) {
    return "Bonjour à " + to + " de " + name;
  }

  /** Change le nom après "Bonjour ".
   * Paramètre "name", obligatoire (présent et non vide). 400 sinon.
   * Si tout ok, 204.
   */
  @PUT
  @Consumes("application/x-www-form-urlencoded")
  @ValidateOnExecution
//  public Response put(MultivaluedMap<String, String> formParams) {
//    Response response;
//    name = formParams.getFirst("name");
//    if (name == null || name.matches("^(\\s)*$")) {
//      response = Response
//          .status(Response.Status.BAD_REQUEST)
//          .entity("nameRequired").build();
//    }
//    else {
//      response = Response
//          .status(Response.Status.NO_CONTENT).build();
//    }
//    return response;
//  }
  public Response put(@NotNull @FormParam("name") String name) {
    this.name = name;
    return Response.status(Response.Status.NO_CONTENT).build();
  }
}
