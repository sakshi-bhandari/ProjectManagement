package ProjectManagement;


public class Timer {

	   private long startTime = System.nanoTime();

	   private long elapsedTime = 0;


	   public void start() { startTime = System.nanoTime(); }

	   public long since() { elapsedTime = System.nanoTime() - startTime; return elapsedTime;}

	   public long report() { return elapsedTime; }

	   //public void report(String s) { System.out.println(s + elapsedTime); }

	   public static void main(String[] args) {
		   Timer t=new Timer();
		   t.start();
		   System.out.println(t.since());
//		   System.out.println("hey tgeehfiqwmefgiegfigef this is fucking annoying bjudbvdbfv");
//		   System.out.println("hey tgeehfiqwmefgiegfigef this is fucking annoying bjudbvdbfv");
//		   System.out.println("hey tgeehfiqwmefgiegfigef this is fucking annoying bjudbvdbfv");
//		   System.out.println("hey tgeehfiqwmefgiegfigef this is fucking annoying bjudbvdbfv");
//for(int i=0;i<100000;i++) {
//	System.out.println("fuck you");
//}
		   final String message = " Subodh is lub! ";
			final int n = 10;

			for (int i = 0; i < n; i++) {
				for (int j = 0; j <= 4 * n; j++) {
					double d1 = Math.sqrt(Math.pow(i - n, 2)
										+ Math.pow(j - n, 2));

					double d2 = Math.sqrt(Math.pow(i - n, 2) 
										+ Math.pow(j - 3 * n, 2));

					if (d1 < n + 0.5 || d2 < n + 0.5) {
						System.out.print('*');
					} else {
						System.out.print(' ');
					}
				}
				System.out.print(System.lineSeparator());
			}

			for (int i = 1; i < 2 * n; i++) {
				for (int j = 0; j < i; j++) {
					System.out.print(' ');
				}

				for (int j = 0; j < 4 * n + 1 - 2 * i; j++) {
					if (i >= 2 && i <= 4) {
						int idx = j - (4 * n - 2 * i - message.length()) / 2;
						if (idx < message.length() && idx >= 0) {
							if (i == 3) {
								System.out.print(message.charAt(idx));
							} else {
								System.out.print(' ');
							}
						} else {
							System.out.print('*');
						}
					} else {
						System.out.print('*');
					}
				}
				System.out.print(System.lineSeparator());
			}
		   System.out.println(t.since());
		   
	   }
	}



