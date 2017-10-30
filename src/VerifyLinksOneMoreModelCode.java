import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.http.HttpClient;
import org.testng.Assert;

public class VerifyLinksOneMoreModelCode
{
 
   public static List<String> errorList = new ArrayList<String>();
   ExecutorService executor;
  // ChromeDriver driver;
  
   @Test
   public void URLCheckTest()
   {
 
       int MYTHREADS = 30;
       executor = Executors.newFixedThreadPool(MYTHREADS);
//       System.setProperty("webdriver.chrome.driver", "chromedrivermac");
//       driver = new ChromeDriver();
//       driver.manage().window().maximize();
//       driver.navigate().to("http://www.yourwebsite.com");
       WebDriver driver=new FirefoxDriver();
   	driver.manage().timeouts().implicitlyWait(16, TimeUnit.SECONDS);
   		driver.get("https://s4testing.nic.in/sarathiservice/sarathiHomePublic.do");
   		driver.findElement(By.xpath("html/body/div/div/div[3]/form/nav/div/div/ul/li[2]/a")).click();
   		driver.findElement(By.id("username")).sendKeys("monika");
   		driver.findElement(By.id("password")).sendKeys("abc@123");
   		driver.findElement(By.xpath(".//*[@id='fm1']/div[4]/div/input[5]")).click();

       List<WebElement> list = driver.findElements(By.tagName("a"));
 
       for (int i = 0; i < list.size(); i++) {
           WebElement element = list.get(i);
           Runnable worker = new MyRunnable(element);
           executor.execute(worker);
       }
       executor.shutdown();
 
       while (!executor.isTerminated()) {
       }
       if(errorList.size()>0) {
           for (String link: errorList
                ) {
               System.out.println("Url = "+link);
           }
           Assert.assertTrue(false);
       }
 
   }
 
   private static void sendGet(WebElement element){
       String href = element.getAttribute("href");
 
           DefaultHttpClient client = new DefaultHttpClient();
           HttpGet request = new HttpGet(href);
 
           HttpResponse response = null;
           try {
               response = client.execute(request);
           } catch (IOException e) {
               e.printStackTrace();
           }
           int code = response.getStatusLine().getStatusCode();
 
           if(code!=200)
               errorList.add(href);
 
   }
 
   public static class MyRunnable implements Runnable {
       private final WebElement hrefEl;
 
       MyRunnable(WebElement el) {
           this.hrefEl = el;
       }
 
       @Override
       public void run() {
 
           sendGet(hrefEl);
       }
   }
 
}