/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java.resource;

import java.modele.Produit;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author plasse
 */
@Path("/produits")
public class ProduitsResource {
  @Context
  private UriInfo context;

  /** Creates a new instance of ProduitsResource */
  public ProduitsResource() {
  }

  /**
   * Retrieves representation of an instance of resource.ProduitsResource
   * @return an instance of java.util.List
   */
  @GET
  @Produces("application/xml")
  public List<Produit> getXml() throws SQLException {
    //TODO return proper representation object
    return Produit.getByDebutNom("", 10);
  }

  /**
   * POST method for creating an instance of ProduitResource
   * @param content representation for the new resource
   * @return an HTTP response with content of the created resource
   */
  @POST
  @Consumes("application/xml")
  @Produces("application/xml")
  public Response postXml(Produit content) throws SQLException {
    //TODO
    content.insert();
    return Response.created(context.getAbsolutePath()).build();
  }

  /**
   * Sub-resource locator method for {id}
   */
  @Path("{id}")
  public ProduitResource getProduitResource(
      @PathParam("id") int id) {
    return ProduitResource.getInstance(id);
  }
}
