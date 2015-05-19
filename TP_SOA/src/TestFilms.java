import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;



public class TestFilms {

	public static String baseUrl = "http://www.mplasse.com/cinema/ws/";
	Client client;
	
	@Before
	public void init(){
		client = ClientBuilder.newClient();
		WebTarget target = client.target(baseUrl + "reinitialisations");
		Response response = target.request().post(null);
	}
	
	@Test
	public void testGetNotFound() throws ParserConfigurationException{
		client = ClientBuilder.newClient();
		System.out.println("GET film-1000");
		WebTarget target = client.target(baseUrl + "film-1000");
		Response response = target.request().get();
		
		assertEquals(404,response.getStatus());
	}
	
	@Test
	 public void testGetOk() throws ParserConfigurationException {
	 System.out.println("GET film-1");
	 WebTarget target = client.target(baseUrl + "film-1");
	 Response response = target.request().get();
	 assertEquals(200, response.getStatus());
	 // En-tete Content-type
	 assertEquals(new MediaType("text", "xml", "utf8"),
	 response.getMediaType());
	 // Corps de la réponse
	 String result = response.readEntity(String.class);
	 System.out.println(result);
	 String expected = "<?xml version='1.0' encoding='UTF8'?>\n"
	 + "<film id='1' titre='8 femmes' id_genre='P' genre='Policier'>"
	 + "<realisateur id='15' prenom='François' nom='Ozon'/>"
	 + "<acteurs>"
	 + "<acteur id='6' prenom='Catherine' nom='Deneuve'/>"
	 + "<acteur id='1' prenom='Fannie' nom='Ardant'/>"
	 + "<acteur id='9' prenom='Isabelle' nom='Huppert'/>"
	 +"</acteurs>"
	 +"</film>";
	 expected = expected.replace('\'', '\"');
	 assertEquals(expected, result);
	 }
}
