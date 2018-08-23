package diningphilDetectAndRecover;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Lars Mortensen
 */
public class DeadlockDetector extends Thread {

  ThreadMXBean tmxb = ManagementFactory.getThreadMXBean();
  boolean doRun = true;

  Philosopher[] philosopers;
  public DeadlockDetector(Philosopher[] philosopers) {
    this.philosopers = philosopers;
  }
  
  public void stopThread() {
    this.doRun = false;
  }
  
  @Override
  public void run() {
    while (doRun) {
      try {
        long[] threadIds = tmxb.findDeadlockedThreads();
        if(threadIds != null){
          System.out.println("###### Deadlock detected #############");
          System.out.print("Involved proceses: ");
          for(long i: threadIds){
            System.out.print(""+i+",");  
          }
          // This will put all chopsticks back for all philoshers, which will make them stop eating, and start thinking instead
          philosopers[0].interrupt();
          philosopers[1].interrupt();
          philosopers[2].interrupt();
          philosopers[3].interrupt();
          philosopers[4].interrupt();
        }
        Thread.sleep(1000);
      } catch (InterruptedException ex) {
        Logger.getLogger(DeadlockDetector.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
}