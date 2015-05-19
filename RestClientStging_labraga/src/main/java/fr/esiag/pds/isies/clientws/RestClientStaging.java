package fr.esiag.pds.isies.clientws;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RestClientStaging {

	public static void main(String[] args) {
		
		Client client = Client.create();
		 
		WebResource webResource = client
		   .resource("http://localhost:8080/StagingAreaLoadingWS_labraga/rest/stagingLoad/all");
		
		ClientResponse response = webResource.get(ClientResponse.class);
		
		System.out.println("Reponse : " + response.getStatus());
	}
}
