package testNG;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestFirstServlet {
  
	private static WebDriver driver;
	private static String serverBaseURL= System.getProperty("serverBaseURL");
	private static String webpageURI= "/helloworld/FirstServlet";
	
	@BeforeTest
	public static void configureDriver() throws MalformedURLException {
		final ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--headless");
		chromeOptions.addArguments("--no-sandbox");
		chromeOptions.addArguments("--disable-dev-shm-usage");
		chromeOptions.addArguments("--window-size=1200x600");

		chromeOptions.setBinary("/usr/bin/google-chrome");
		DesiredCapabilities capability = DesiredCapabilities.chrome();
		capability.setBrowserName("chrome");
		capability.setPlatform(Platform.LINUX);

		capability.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

		driver = new RemoteWebDriver(new URL("http://selenium__standalone-chrome:4444/wd/hub"), capability);
	}	
	
	
	@Test
	public static void testContent() throws InterruptedException {
		driver.get(serverBaseURL+webpageURI);
		Thread.sleep(1000);
		String expectedString= "Hi There!";
		String actualString= driver.findElement(By.id("hi")).getText();
		Assert.assertEquals(expectedString, actualString);
	}
	
	@Test //This should fail as we are asserting wrong time with that of the one shown in the page
	public static void testTime() throws InterruptedException {
		driver.get(serverBaseURL+webpageURI);
		Thread.sleep(1000);
		String actualDate= driver.findElement(By.id("date")).getText();
		Thread.sleep(1000);
		String expectedDate= "Date=Fri Apr 10 10:30:46 UTC 2020";
		Assert.assertEquals(expectedDate, actualDate);
	}
	
	@AfterSuite
	public static void closeDriver() {
		driver.quit();
	}
	
	
}
