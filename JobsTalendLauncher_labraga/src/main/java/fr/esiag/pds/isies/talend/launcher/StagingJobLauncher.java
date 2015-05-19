package fr.esiag.pds.isies.talend.launcher;

import exemple.jobstagingall_0_1.JobStagingAll;

public class StagingJobLauncher implements IJobLauncher{
	
	JobStagingAll job;
	
	public StagingJobLauncher(){
		job = new JobStagingAll();
	}
	
	public void runJob() {
		String[][] exit = job.runJob(new String[]{});
		
	}

}
