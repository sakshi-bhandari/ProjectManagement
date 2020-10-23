# PROJECT MANAGEMENT

A naive project Management software that handles budget, users and jobs. Takes command from an input file, a sample `inp.txt` is added.

### CLASS JOB
Contains all the required variables, i.e. name, runtime, project it is associated to, user it is associated to, completion status, and the time when it is completed.
Constructor initialises all these values.
Compareto prioritises the job according to the priority of the project.

### CLASS USER
Only has one variable, initialised by the constructor and the compareto function compares the students on the basis of their names. 

### CLASS PROJECT
Contains the name, budget and the priority.

### CLASS SCHEDULERDRIVER
We create a maxheap for jobs, a trie of projects and a red black tree of users. We also create a global time initilly set to 0 and a vector to add the completed jobs.

##### handleusers and handleprojects:
Creates a new user and project respectively using the name given in the input and other parameters.

##### handlejob:
Finds the required job and project from the tree and trie and uses them to create a new job.

##### handlequery:
If the vector if it consists the name of the job, if yes, then print completed. If not, then we check the heap. If it contains, then print not finished. If not found anywhere, then print no job exists.

##### handle add:
Converts the string into int, then search the trie for the project and then update its budget .

##### print stats:
Using the vector, we print all the details of the jobs in the required format.

##### schedule:
We first extract the max element from the heap, if its null, then return. If it exists but the budget is less, then we repeat the function. If the budget i sufficient, then complete the required job and add the job to the vector.