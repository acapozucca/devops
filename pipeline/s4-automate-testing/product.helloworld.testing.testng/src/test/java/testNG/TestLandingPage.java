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

public class TestLandingPage {
  
	private static WebDriver driver;
	private static String serverBaseURL= System.getProperty("serverBaseURL");
	private static String webpageURI= "/helloworld";
	
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
		String expectedString= "You have reached some content here. Congratulations!";
		String actualString= driver.findElement(By.id("content")).getText();
		Assert.assertEquals(expectedString, actualString);
		Thread.sleep(1000);
	}
	
		
	@AfterSuite
	public static void closeDriver() {
		driver.quit();
	}
	
	
}
