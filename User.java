package ProjectManagement;

public class User implements Comparable<User> {

	String name;
	public int con,last;
	public User(String n) {
		name=n;
		con=0;
		last=0;
	}

    @Override
    public int compareTo(User user) {
        return name.compareTo(user.name);
    }
}
