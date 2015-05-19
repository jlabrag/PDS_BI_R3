package fr.esiag.isies.pds.ws.stagingload;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import fr.esiag.pds.isies.talend.launcher.StagingJobLauncher;

@Path("/stagingLoad")
public class StagingLoadService {
	
	StagingJobLauncher jobLauncher;
	
	@GET
	@Path("/all")
	public Response loadStagingDB() {
		jobLauncher = new StagingJobLauncher();
		jobLauncher.runJob();
 
		return Response.status(200).build();
 
	}
}
