package diningPrevent;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Lars Mortensen
 */
class DeadLockDetector implements Runnable {

  ThreadMXBean tmxb = ManagementFactory.getThreadMXBean();
  boolean doRun = true;
  
  public void stop() {
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
          System.exit(1);
        }
        Thread.sleep(2000);
      } catch (InterruptedException ex) {
        Logger.getLogger(DeadLockDetector.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
}