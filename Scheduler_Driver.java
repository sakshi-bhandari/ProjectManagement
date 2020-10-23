package ProjectManagement;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import PriorityQueue.MxHeap;
import RedBlack.RBTree;
import RedBlack.RedBlackNode;
import Trie.Trie;
import Trie.TrieNode;

public class Scheduler_Driver extends Thread implements SchedulerInterface {


    public static void main(String[] args) throws IOException {
//

        Scheduler_Driver scheduler_driver = new Scheduler_Driver();
        File file;
        if (args.length == 0) {
            URL url = Scheduler_Driver.class.getResource("INP");
            file = new File(url.getPath());
        } else {
            file = new File(args[0]);
        }

        scheduler_driver.execute(file);
    }

    public void execute(File commandFile) throws IOException {


        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(commandFile));

            String st;
            while ((st = br.readLine()) != null) {
                String[] cmd = st.split(" ");
                if (cmd.length == 0) {
                    System.err.println("Error parsing: " + st);
                    return;
                }
                String project_name, user_name;
                Integer start_time, end_time;

                long qstart_time, qend_time;

                switch (cmd[0]) {
                    case "PROJECT":
                        handle_project(cmd);
                        break;
                    case "JOB":
                        handle_job(cmd);
                        break;
                    case "USER":
                        handle_user(cmd[1]);
                        break;
                    case "QUERY":
                        handle_query(cmd[1]);
                        break;
                    case "": // HANDLE EMPTY LINE
                        handle_empty_line();
                        break;
                    case "ADD":
                        handle_add(cmd);
                        break;
                    //--------- New Queries
                    case "NEW_PROJECT":
                    case "NEW_USER":
                    case "NEW_PROJECTUSER":
                    case "NEW_PRIORITY":
                        timed_report(cmd);
                        break;
                    case "NEW_TOP":
                        qstart_time = System.nanoTime();
                        timed_top_consumer(Integer.parseInt(cmd[1]));
                        qend_time = System.nanoTime();
                        System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                        break;
                    case "NEW_FLUSH":
                        qstart_time = System.nanoTime();
                        timed_flush( Integer.parseInt(cmd[1]));
                        qend_time = System.nanoTime();
                       // System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                        break;
                    default:
                        System.err.println("Unknown command: " + cmd[0]);
                }

            }


            run_to_completion();
            print_stats();

        } catch (FileNotFoundException e) {
            System.err.println("Input file Not found. " + commandFile.getAbsolutePath());
        } catch (NullPointerException ne) {
            ne.printStackTrace();

        }
    }

    @Override
    public ArrayList<JobReport_> timed_report(String[] cmd) {
        long qstart_time, qend_time;
        ArrayList<JobReport_> res = null;
        switch (cmd[0]) {
            case "NEW_PROJECT":
                qstart_time = System.nanoTime();
                res = handle_new_project(cmd);
                qend_time = System.nanoTime();
                System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                break;
            case "NEW_USER":
                qstart_time = System.nanoTime();
                res = handle_new_user(cmd);
                qend_time = System.nanoTime();
                System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));

                break;
            case "NEW_PROJECTUSER":
                qstart_time = System.nanoTime();
                res = handle_new_projectuser(cmd);
                qend_time = System.nanoTime();
                System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                break;
            case "NEW_PRIORITY":
                qstart_time = System.nanoTime();
                res = handle_new_priority(cmd[1]);
                qend_time = System.nanoTime();
                System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                break;
        }

        return res;
    }
    
    MxHeap jobs=new MxHeap();
    Trie<Project> projects=new Trie();
    RBTree<String, User> users =new RBTree();
    int globalTime=0;
    Vector<String> cmpltd = new Vector<String>();  
    Vector<Job> cmpltdjb = new Vector<Job>();
    Vector<Job> unfnshd = new Vector<Job>();

    @Override
    public ArrayList<UserReport_> timed_top_consumer(int top) {
    	System.out.println("Top query");
    	ArrayList<UserReport_> res = new ArrayList<UserReport_>();
    	Vector<UserReport> v=new Vector<UserReport>();
    	for(int i=0;i<cmpltdjb.size();i++) {
    		Job j=cmpltdjb.elementAt(i);
    		UserReport u=new UserReport(j.user.name,j.user.con,j.user.last);
    		if(!v.contains(u)) v.add(u);
    	}
    	for(int i=0;i<top;i++) res.add(extractmax(v));
    	return res;
    }

public UserReport extractmax(Vector<UserReport> v) {
	UserReport u=v.get(0);
	for(int i=1;i<v.size();i++) {
		if(v.get(i).con>u.con||(v.get(i).con==u.con&&v.get(i).last>u.last)) {
			u=v.get(i);
			
		}
		v.remove(u);
	}
	return u;
}

    @Override
    public void timed_flush(int waittime) {
        System.out.println("Flush query");
    	int t=globalTime;
    	int[] p=new int[jobs.size];
    	for(int i=0;i<jobs.size;i++) {
    		p[i]=t-jobs.heap[i].creationTime;
    	}
    	//int x=p[0];
    	for(int i=0;i<jobs.size;i++) if(p[i]>waittime) jobs.heap[i].priority=9999;
//    	for(int i=0;i<jobs.size;i++) {
//    		Job j=jobs.heap[i];
//    		System.out.println("Job{user=’"+j.user.name+"’, project=’"+j.project.name+"’, jobstatus=REQUESTED, execution_time="+j.runtime+", end_time=null, priority="+j.priority+", name=’"+j.name+"'}");
//    	}
    	//System.out.println(jobs.size);
    	MxHeap N=new MxHeap();
   	for(int i=0;i<jobs.size;i++) 
   	{
   		N.insert(jobs.heap[i]);
   	}
   	jobs=N;
   	for(int i=0;i<jobs.size;i++) {
		Job j=jobs.heap[i];
		System.out.println("Job{user=’"+j.user.name+"’, project=’"+j.project.name+"’, jobstatus=REQUESTED, execution_time="+j.runtime+", end_time=null, priority="+j.priority+", name=’"+j.name+"'}");
	}
    }
    

    private ArrayList<JobReport_> handle_new_priority(String s) {
    	System.out.println("Priority query");
    	ArrayList<JobReport_> res = new ArrayList<JobReport_>();
    	int a=Integer.parseInt(s);
    	for(int i=0;i<unfnshd.size();i++) {
    		if(unfnshd.get(i).project.priority>a) {
    			res.add(new JobReport(unfnshd.get(i).user.name,unfnshd.get(i).project.name,unfnshd.get(i).runtime,unfnshd.get(i).arrivalTime,unfnshd.get(i).completionTime));
    		}
    	}
        return res;
    }

    private ArrayList<JobReport_> handle_new_projectuser(String[] cmd) {
    	System.out.println("Project User query");
    	ArrayList<JobReport_> res = new ArrayList<JobReport_>();
    	int a=Integer.parseInt(cmd[3]);
    	int b=Integer.parseInt(cmd[4]);
    	for(int i=0;i<cmpltdjb.size();i++) {
    		if(cmpltdjb.get(i).project.name.equals(cmd[0])&&cmpltdjb.get(i).user.name.equals(cmd[0])&&cmpltdjb.get(i).arrivalTime>=a&&cmpltdjb.get(i).completionTime>=b) {
    			res.add(new JobReport(cmpltdjb.get(i).user.name,cmpltdjb.get(i).project.name,cmpltdjb.get(i).runtime,cmpltdjb.get(i).arrivalTime,cmpltdjb.get(i).completionTime));
    			
    		}
    	}
        return res;
    }

    private ArrayList<JobReport_> handle_new_user(String[] cmd) {
    	System.out.println("User query");
    	ArrayList<JobReport_> res = new ArrayList<JobReport_>();
    	int a=Integer.parseInt(cmd[2]);
    	int b=Integer.parseInt(cmd[3]);
    	int c=0;
    	for(int i=0;i<cmpltdjb.size();i++) {
    		if(cmpltdjb.get(i).user.name.equals(cmd[0])&&cmpltdjb.get(i).arrivalTime>=a&&cmpltdjb.get(i).completionTime>=b) {
    			res.add(new JobReport(cmpltdjb.get(i).user.name,cmpltdjb.get(i).project.name,cmpltdjb.get(i).runtime,cmpltdjb.get(i).arrivalTime,cmpltdjb.get(i).completionTime));
    		}
    	}
        return res;
    }

    private ArrayList<JobReport_> handle_new_project(String[] cmd) {
    	System.out.println("Project query");
    	ArrayList<JobReport_> res = new ArrayList<JobReport_>();
    	int a=Integer.parseInt(cmd[2]);
    	int b=Integer.parseInt(cmd[3]);
    	int c=0;
    	for(int i=0;i<cmpltdjb.size();i++) {
    		if(cmpltdjb.get(i).project.name.equals(cmd[0])&&cmpltdjb.get(i).arrivalTime>=a&&cmpltdjb.get(i).completionTime>=b) {
    			res.add(new JobReport(cmpltdjb.get(i).user.name,cmpltdjb.get(i).project.name,cmpltdjb.get(i).runtime,cmpltdjb.get(i).arrivalTime,cmpltdjb.get(i).completionTime));
    		}
    	}
        return res;
    }




    public void schedule() {
            execute_a_job();
    }

    public void run_to_completion() {
    	while(jobs.size!=0) {
    		//Job j=jobs.extractMax();
    		handle_empty_line();
    		System.out.println("System execution completed");
    	}
    }

    public void print_stats() {

    	System.out.println("--------------STATS--------------- \n" + 
    			"Total jobs done: "+cmpltdjb.size());
    	for(int i=0;i<cmpltdjb.size();i++) {
    		Job j=cmpltdjb.get(i);
    		System.out.println("Job{user=’"+j.user.name+"’, project=’"+j.project.name+"’, jobstatus=COMPLETED, execution_time="+j.runtime+", end_time="+j.completionTime+", name=’"+j.name+"'}");
    	}
    	System.out.println("------------------------\n"+"Unfinished jobs:");
    	for(int i=0;i<unfnshd.size();i++) {
    		Job j=unfnshd.get(i);
    		System.out.println("Job{user=’"+j.user.name+"’, project=’"+j.project.name+"’, jobstatus=REQUESTED, execution_time="+j.runtime+", end_time=null, name=’"+j.name+"'}");
    	}
    	System.out.println("Total unfinished jobs: "+unfnshd.size());
    	System.out.println( "--------------STATS DONE---------------");
    }

    public void handle_add(String[] cmd) {
    	int j=Integer.parseInt(cmd[2]);
    	projects.search(cmd[1]).obj.budget+=j;
    	System.out.println("ADDING Budget");
    	
    	for(int i=0;i<unfnshd.size();i++) {
    		 
    			jobs.insert(unfnshd.elementAt(i));
    	}
    	unfnshd.removeAllElements();
    }

    public void handle_empty_line() {
       schedule();
    }


    @Override
    public void handle_project(String[] cmd) {
    	int i=Integer.parseInt(cmd[2]);
    	int j=Integer.parseInt(cmd[3]);
    	Project p=new Project(cmd[1],i,j);
    	projects.insert(cmd[1], p);
    	System.out.println("Creating project");
     
    }

    @Override
    public void handle_job(String[] cmd) {
    	System.out.println("Creating job");
    	TrieNode t=projects.search(cmd[2]);
    	if(t==null) {
    		System.out.println("No such project exists. "+cmd[2]);
    		return;
    	}
    	Project p=(Project) t.obj;
    	//System.out.println(p.name);
    	RedBlackNode<String,User> r=users.search(cmd[3]);
    	//System.out.println(r.Key);
    	if(r==null||r.Key=="random") {
    		System.out.println("No such user exists: "+cmd[3]);
    		return;
    	}
    	String s=(String) r.Key;
    	//System.out.println(s);
    	User u=new User(s);
    	int i=Integer.parseInt(cmd[4]);
    	
    	//System.out.println(i);
    	Job j=new Job(cmd[1],p,u,i);
    	j.creationTime=globalTime;
    	jobs.insert(j);
    	
    	
    }

    @Override
    public void handle_user(String name) {
    	User u=new User(name);
    	users.insert(name,u);
    	System.out.println("Creating user");

    }

    @Override
    public void handle_query(String key) {
    	System.out.println("Querying");
    	if(cmpltd.contains(key)) {
    		System.out.println(key+": COMPLETED");
    		return;
    		}
    	  
    	for(int i=0;i<jobs.size;i++) {
    			if(jobs.heap[i].name.equals(key)) {
    				System.out.println(key+": NOT FINISHED");
    				return;
    			
    		}
    	}
    	System.out.println(key+": NO SUCH JOB");
    	
    }

    public void execute_a_job() {

    	if(jobs.size==0) return; //no jobs left ;)
   	 System.out.println("Running code\n"+"	Remaining jobs: "+(jobs.size+" "+unfnshd.size()));
     Job j=jobs.extractMax();
     //System.out.println(j.name);
     int budget=j.project.budget;
     int runt=j.runtime;
    
	  System.out.println("	Executing: "+j.name+" from: "+j.project.name);
     if(runt<=budget) {
   	  
   	  System.out.println("	Project: "+j.project.name+" budget remaining: "+(budget-runt));
   	  System.out.println("Execution cycle completed");
   	  j.status=true;
   	  cmpltdjb.add(j);
   	  j.user.con+=runt;
   	  j.user.last=runt;
   	  cmpltd.add(j.name);
   	  j.project.budget-=runt;
   	  j.arrivalTime=globalTime;
   	  globalTime+=j.runtime;
 		j.completionTime=globalTime;
   	  //System.out.println(projects.search(j.project.name).obj.budget);
   	  return;
     }
     else {
   	  unfnshd.add(j); 
   	j.arrivalTime=globalTime;
   	  //System.out.println("Executing: "+j.name+" from: "+j.project.name);
   	  System.out.println("	Un-sufficient budget.");
   	  handle_empty();
     }
   }
   
   public void handle_empty() {
   	if(jobs.size==0) return; //no jobs left ;)
   	 
     Job j=jobs.extractMax();
     //System.out.println(j.name);
     int budget=j.project.budget;
     int runt=j.runtime;
    
	  System.out.println("	Executing: "+j.name+" from: "+j.project.name);
     if(runt<=budget) {
   	  
   	  System.out.println("	Project: "+j.project.name+" budget remaining: "+(budget-runt));
   	  System.out.println("Execution cycle completed");
   	  j.status=true;
   	  cmpltdjb.add(j);
   	  cmpltd.add(j.name);
   	  j.project.budget-=runt;
   	  globalTime+=j.runtime;
 		j.completionTime=globalTime;
   	  //System.out.println(projects.search(j.project.name).obj.budget);
   	  return;
     }
     else {
   	  unfnshd.add(j); 
   	  //System.out.println("Executing: "+j.name+" from: "+j.project.name);
   	  System.out.println("	Un-sufficient budget.");
   	  handle_empty();
     }
    }
}
