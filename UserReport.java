package ProjectManagement;

public class UserReport implements UserReport_ {

	String user;
	int con;
	int last;
	
	UserReport(String a,int b,int c){
		user=a;
		con=b;
		last=c;
	}
	
	@Override
	public String user() {
		// TODO Auto-generated method stub
		return user;
	}

	@Override
	public int consumed() {
		// TODO Auto-generated method stub
		return con;
	}

}
