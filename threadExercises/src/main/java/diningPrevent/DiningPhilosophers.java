package diningPrevent;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This is the classic dining philosophers problem. See the references for a
 * description of the problem. This version will deadlock. Ref:
 * http://everythingcomputerscience.com/projects/java_programs/DiningPhilosophersSolution.txt
 *
 * @author Barbara Lerner
 * @version Oct 5, 2010
 *
 */
public class DiningPhilosophers {

  // The number of philosophers
  private static final int NUM_PHILOSOPHERS = 5;

  /**
   * Test the dining philosophers solution
   *
   * @param args Not used
   */
  public static void main(String[] args) {
    //Start the Deadlock Detector
    new Thread(new DeadLockDetector()).start();

    // Model each chopstick with a lock
    Lock[] chopsticks = new ReentrantLock[NUM_PHILOSOPHERS];

    for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
      chopsticks[i] = new ReentrantLock();
    }

    // Create the philosophers and start each running in its own thread.
    Philosopher[] philosophers = new Philosopher[NUM_PHILOSOPHERS];

    for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
      philosophers[i] = new Philosopher(i, chopsticks[i], chopsticks[(i + 1) % NUM_PHILOSOPHERS]);
      new Thread(philosophers[i]).start();
    }
  }

}

/**
 * A philosopher alternates between thinking and eating. To eat, the philosopher
 * needs to pick up the left chopstick and then the right chopstick
 * sequentially. The phillosopher shares chopsticks with its neighbors, so it
 * cannot eat at the same time as either neighbor.
 *
 * @author Barbara Lerner
 * @version Oct 5, 2010
 *
 */
class Philosopher implements Runnable {

  // Used to vary how long a philosopher thinks before eating and how long the
  // philosopher eats
  private final Random numGenerator = new Random();

  // The philosopher's unique id
  private final int id;

  // The chopsticks this philosopher may use
  private final Lock leftChopstick;
  private final Lock rightChopstick;

  /**
   * Constructs a new philosopher
   *
   * @param id the unique id
   * @param leftChopstick chopstick to the left
   * @param rightChopstick chopstick to the right
   */
  public Philosopher(int id, Lock leftChopstick, Lock rightChopstick) {
    this.id = id;
    this.leftChopstick = leftChopstick;
    this.rightChopstick = rightChopstick;
  }

  /**
   * Repeatedly think, pick up chopsticks, eat and put down chopsticks
   */
  @Override
  public void run() {
    try {
      while (true) {
        think();
        
        /*
        This solves the Deadlock problem using the "Prevent Deadlock" strategy
        We always take either NONE or BOTH chopsticks which removes this deadlock condition:
        "Processes hold on to resources already allocated to them while awaiting the granting of additional ones."
        */
        
        boolean hasBothChopSticks = false;     
        while (!hasBothChopSticks) {
          boolean gotLeftChopstick = pickUpLeftChopstick();
          if(gotLeftChopstick){
            if(!pickUpRightChopstick()){
              leftChopstick.unlock();
            }
            else 
            {
              hasBothChopSticks = true;
            }
          }
          if(!hasBothChopSticks){
            Thread.sleep((int)(Math.random()*50));  //Wait a random period and try again
          }
        }
        
        
        eat();
        putDownChopsticks();
      }
    } catch (InterruptedException e) {
      System.out.println("Philosopher " + id + " was interrupted.\n");
    }
  }

  /**
   * Lets a random amount of time pass to model thinking.
   *
   * @throws InterruptedException
   */
  private void think() throws InterruptedException {
    System.out.println("Philosopher " + id + " is thinking.\n");
    System.out.flush();
    Thread.sleep(numGenerator.nextInt(10));
  }

  /**
   * Locks the left chopstick to signify that this philosopher is holding it
   */
  private boolean pickUpLeftChopstick() {
    if (leftChopstick.tryLock()) {
      System.out.println("Philosopher " + id + " is holding 1 chopstick.\n");
      System.out.flush();
      return true;
    }
    return false;
  }

  /**
   * Locks the right chopstick to signify that this philosopher is holding it
   */
  private boolean pickUpRightChopstick() {
    return rightChopstick.tryLock();
  }

  /**
   * Lets a random amount of time pass to model eating.
   *
   * @throws InterruptedException
   */
  private void eat() throws InterruptedException {
    System.out.println("Philosopher " + id + " is eating.\n");
    System.out.flush();
    Thread.sleep(numGenerator.nextInt(10));
  }

  /**
   * Releases the locks on both chopsticks to model putting them down so the
   * other philosophers can use them.
   */
  private void putDownChopsticks() {
    leftChopstick.unlock();
    rightChopstick.unlock();
  }

}
