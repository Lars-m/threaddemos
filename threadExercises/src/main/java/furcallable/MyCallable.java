package furcallable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MyCallable implements Callable<String> {

  int taskID; 
  public MyCallable(int taskID) {
    this.taskID = taskID;
  }

  @Override
  public String call() throws Exception {
     Random generator = new Random();
     Integer sleepTime = generator.nextInt(1000);
    Thread.sleep(sleepTime);
    //return the thread name executing this callable task
    String text = "Callable"+taskID+"-----" +Thread.currentThread().getName();
    return text;
  }

  public static void main(String args[]) throws InterruptedException, ExecutionException {
    //Get ExecutorService from Executors utility class, thread pool size is 10
  
    ExecutorService executor = Executors.newFixedThreadPool(10);
    //create a list to hold the Future object associated with Callable
    List<Future<String>> list = new ArrayList<Future<String>>();
    //Create MyCallable instance
    
    for (int i = 0; i < 100; i++) {
      Callable<String> callable = new MyCallable(i);
      //submit Callable tasks to be executed by thread pool
      Future<String> future = executor.submit(callable);
      //add Future to the list, we can get return value using Future
      list.add(future);
    }
    for (Future<String> fut : list) {
      try {
        //print the return value of Future, notice the output delay in console
        // because Future.get() waits for task to get completed
        System.out.println(fut.get());
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }

  
    //shut down the executor service now
    executor.shutdown();
  }

}
