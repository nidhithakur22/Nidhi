package tests;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BrokenLinkTest {
	
	public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException {
		
	       System.setProperty("webdriver.chrome.driver", "D:\\chromedriver_win32 (1)\\chromedriver_win32\\chromedriver.exe");
	       
	      // driver.manage().deleteAllCookies();
	       
	       //dynamic wait
	       int resCode =200;
	       
			ChromeOptions options =	new ChromeOptions();
			options.addArguments("disable-notifications");
			WebDriver driver = new ChromeDriver(options);
			driver.manage().window().maximize();
			driver.navigate().to("https://hapleaf.com/");
			
	       List<WebElement> linkList = (List<WebElement>) driver.findElements(By.tagName("a"));
	
	       linkList.addAll(driver.findElements(By.tagName("img")));
	       
	       System.out.println("size of all Links and images--"+linkList.size());
	       List<WebElement> activeLinks = new ArrayList<WebElement>();
	       List<WebElement> brokenLinks = new ArrayList<WebElement>();	       //iterate linkList: exclude all link / images - doesnot have href
	       
	       for(int i = 0; i<linkList.size();i++) {
	    	   System.out.println(linkList.get(i).getAttribute("href"));
	    	   if(linkList.get(i).getAttribute("href") != null && (!linkList.get(i).getAttribute("href").contains("tel")))
	    	   {
	    		   activeLinks.add(linkList.get(i));
	    	
	    	   }
	    	       	   
	       }
	
	//get the size of actives links and images
	System.out.println("Size of active links and images--"+ activeLinks.size());
	
	for(int j=0;j<activeLinks.size();j++) {
		
		  HttpURLConnection htc =(HttpURLConnection)new URL(activeLinks.get(j).getAttribute("href")).openConnection();
		  
		  htc.connect();
		  
		  String response =  htc.getResponseMessage();
		  resCode = htc.getResponseCode();
		  htc.disconnect();
		  System.out.println(activeLinks.get(j).getAttribute("href")+"----->"+response+"----"+resCode);
	}
	
	 for(int i = 0; i<linkList.size();i++) {
 	   System.out.println(linkList.get(i).getAttribute("href"));
 	   if(linkList.get(i).getAttribute("href")== null || linkList.get(i).getAttribute("href").isEmpty())
 	   {
 		   brokenLinks.add(linkList.get(i));
 		   System.out.println("Url is either not configured or it is empty");
 		   continue;
 	   }
 	   
 	   if(!linkList.get(i).getAttribute("href").startsWith("https://hapleaf.com")) {
 		   System.out.println("URL belongs to another domain, skipping it");
 		   continue;
 	   }
	 }
 		System.out.println("Size of broken links and images--"+ brokenLinks.size());
 		
 		for(int j=0;j<brokenLinks.size();j++) {
 			
 			  HttpURLConnection htc1 =(HttpURLConnection)new URL(brokenLinks.get(j).getAttribute("href")).openConnection();
 			  try {
 				  htc1.setRequestMethod("HEAD");
 				  htc1.connect();
 				  resCode = htc1.getResponseCode();
 				  if(resCode>=400) {
 					  System.out.println(resCode+ linkList.get(j).getAttribute("href")+ "url is broken");
 				  }
 				  else
 				  {
 					  System.out.println("url is valid link");
 				  }
 			  }catch(MalformedURLException e)
 			  {e.printStackTrace();}
 			  catch(IOException e) {
 				  e.printStackTrace();
 			  }
 			  htc1.connect();
 			  
 			  String response1 =  htc1.getResponseMessage();
 			  resCode = htc1.getResponseCode();
 			  htc1.disconnect();
 			  System.out.println(brokenLinks.get(j).getAttribute("href")+"----->"+response1+"----"+resCode);
 		}
 	       	   
    }
	
	}

