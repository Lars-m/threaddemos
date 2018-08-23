package demos;


class MyThread extends Thread {
  @Override
  public void run() {
    
  }
}

public class Tester {
 
  
  public static void main(String[] args) throws InterruptedException {
    RandomGenerator r1 = new RandomGenerator(5);
    RandomGenerator r2 = new RandomGenerator(5);
    RandomGenerator r3 = new RandomGenerator(5);
    r1.start();
    r2.start();
    r3.start();
    
    r1.join();
    r2.join();
    r3.join();
    
    System.out.println("From T1");
    for(int i : r1.getRandoms()){
      System.out.println("T1: "+i);
    }
    System.out.println("From T2");
    for(int i : r2.getRandoms()){
      System.out.println("T2: "+i);
    }
    System.out.println("From T3");
    for(int i: r3.getRandoms()){
      System.out.println("T3: "+i);
    };
    
    System.out.println("DONE");
    
    
  }
}
