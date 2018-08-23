package httpendpointstatus;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

class Pinger implements Callable<String> {

    String url;
    public Pinger(String url) {
      this.url = url;
    }    
    @Override
    public String call() throws Exception {
      String result = "Error";
        try {
            URL siteURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) siteURL
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
 
            int code = connection.getResponseCode();
            if (code == 200) 
                result = "Green";
            if(code==301)
                result="Redirect";
        } catch (Exception e) {
            result = "->Red<-";
        }
        return result +"("+url+")";
    }
  }
public class ExecutorPinger {
  public static final int TIMEOUT_VALUE = 3; //seconds
  public static void main(String[] args) throws InterruptedException, ExecutionException {
      String[] hostList = { 
                "http://crunchify.com", 
                "http://yahoo.com",
                "http://www.ebay.com", 
                "http://google.com",
                "http://www.example.co", 
                "https://paypal.com",
                "http://bing.com/", 
                "http://techcrunch.com/",
                "http://mashable.com/", 
                "http://thenextweb.com/",
                "http://wordpress.com/", 
                "http://cphbusiness.dk/",
                "http://example.com/", 
                "http://sjsu.edu/",
                "http://ebay.co.uk/", 
                "http://google.co.uk/",
                "http://www.wikipedia.org/",
                "http://dr.dk",
                "http://pol.dk",
                "https://www.google.dk",
                "http://phoronix.com" ,
                "http://www.webupd8.org/",
                "https://studypoint-plaul.rhcloud.com/", 
                "http://stackoverflow.com",
                "http://docs.oracle.com",
                "https://fronter.com",
                "http://imgur.com/", 
                "http://www.imagemagick.org"
                };
      
      List<Future<String>> futures = new ArrayList<Future<String>>();
          
      ExecutorService executor = Executors.newFixedThreadPool(10);
      for(int i = 0; i < hostList.length; i++){
         Future<String> f = executor.submit(new Pinger(hostList[i]) );
         futures.add(f);
      }
      for(int i = 0 ; i < futures.size(); i++){
        try {
          System.out.println(futures.get(i).get(TIMEOUT_VALUE, TimeUnit.SECONDS));
        } catch (TimeoutException ex) {
          System.out.println("TIME OUT ("+hostList[i]+")");
        }
      }
      executor.shutdown();
      System.out.println("DONE");
      
 
  }
          
          
  
}
