package ProjectManagement;

public class JobReport implements JobReport_ {
	
	String user;
	   String project_name;
	   int budget;
	   int arrival_time;
	   int completion_time;
	   
	public JobReport(String a,String b,int c,int d, int e) {
		user=a;
		project_name=b;
		budget=c;
		arrival_time=d;
		completion_time=e;
	}

	@Override
	public String user() {
		// TODO Auto-generated method stub
		return user;
	}

	@Override
	public String project_name() {
		// TODO Auto-generated method stub
		return project_name;
	}

	@Override
	public int budget() {
		// TODO Auto-generated method stub
		return budget;
	}

	@Override
	public int arrival_time() {
		// TODO Auto-generated method stub
		return arrival_time;
	}

	@Override
	public int completion_time() {
		// TODO Auto-generated method stub
		return completion_time;
	}

}
