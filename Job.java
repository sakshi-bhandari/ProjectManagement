package ProjectManagement;

public class Job implements Comparable<Job> {
    
	public String name;
	int runtime;
	public Project project;
	User user;
	public boolean status;
	public int completionTime;
	public int arrivalTime;
	public int creationTime;
	public int priority;
	public Job() {
		project =null;
	}
	
	public Job(String name,Project project,User user,int runtime){
		this.name=name;
		this.project=project;
		this.user=user;
		this.runtime=runtime;
		status=false;
		priority=project.priority;
	}
	
    @Override
    public int compareTo(Job job) {
    	if(priority==job.priority)
    		{
    		if(creationTime>job.creationTime) return -1;
    		else if(creationTime<job.creationTime) return 1;
    		else return 0;
    		}
    	else
    	
    	return priority>job.priority ? 1 : -1;
    	
    }
}

