package diningphilDetectAndRecover;

import java.util.Random;
import java.util.concurrent.locks.Lock;

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

// Refactored to extend thread instead of implementing Runnable
class Philosopher extends Thread {

  // Used to vary how long a philosopher thinks before eating and how long the
  // philosopher eats
  private Random numGenerator = new Random();

  // The philosopher's unique id
  private int id;

  // The chopsticks this philosopher may use
  private Lock leftChopstick;
  private Lock rightChopstick;

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

    /*
    LAM This is refactored so if interrupted, the philoshoper put back his chopsticks and goes back to thinking
    */
    while (true) {
      try {
        think();
        pickUpLeftChopstick();
        pickUpRightChopstick();
        eat();
        putDownChopsticks();
      } catch (Exception e) {
        System.out.println("Philosopher-" + id + " was interrupted.\n");
        try{
        putDownChopsticks();
        } catch(java.lang.IllegalMonitorStateException ex){
          // Will throw if philosoper held none or only one chopstick. Just ignore 
          // He will go back to thinking :-)
        }
      }
    }

  }

  /**
   * Lets a random amount of time pass to model thinking.
   *
   * @throws InterruptedException
   */
  private void think() throws InterruptedException {
    System.out.println("Philosopher-" + id + " is thinking.\n");
    System.out.flush();
    Thread.sleep(numGenerator.nextInt(10));
  }

  /**
   * Locks the left chopstick to signify that this philosopher is holding it
   */
  private void pickUpLeftChopstick() throws InterruptedException {
    //leftChopstick.lock();
    leftChopstick.lockInterruptibly();
    System.out.println("Philosopher-" + id + " is holding 1 chopstick.\n");
    System.out.flush();
  }

  /**
   * Locks the right chopstick to signify that this philosopher is holding it
   */
  private void pickUpRightChopstick() throws InterruptedException {
    rightChopstick.lockInterruptibly();
  }

  /**
   * Lets a random amount of time pass to model eating.
   *
   * @throws InterruptedException
   */
  private void eat() throws InterruptedException {
    System.out.println("Philosopher-" + id + " is eating.\n");
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
    System.out.println("Philosopher-" + id + " holds no chopsticks.\n");
  }

}