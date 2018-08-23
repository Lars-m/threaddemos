package demos;

import java.util.ArrayList;
import java.util.List;

public class RandomGenerator extends Thread {
  
  List<Integer> randoms = new ArrayList();
  int numberOfRandoms;

  public RandomGenerator(int numberOfRandoms) {
    this.numberOfRandoms = numberOfRandoms;
  }

  public List<Integer> getRandoms() {
    return randoms;
  }
  
  
  
  @Override
  public void run() {
    for(int i=0; i < this.numberOfRandoms; i++){
      int random = ((int)(Math.random()*6))+1;
      randoms.add(random);
    }
  }
  
}
