package selenium;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 * 
 * @author Ravi
 *
 */
public class LoginAutomationTest {

	private String sauseDemoUrl = "https://www.saucedemo.com/index.html";
	private WebDriver webDriver;

	/**
	 * Setup for test suite
	 * reads testng-configuration.properties file for configuration details
	 * @throws IOException
	 */
	@BeforeSuite(alwaysRun = true)
	public void setup() throws IOException {
		Properties properties = readProperties("testng-configuration.properties");
		String browser = properties.getProperty("selenium.browser");
		if(browser.equals("firefox")) {
			System.setProperty("webdriver.gecko.driver", properties.getProperty("firefox.driver"));
			webDriver = new FirefoxDriver();
		} else if(browser.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver", properties.getProperty("chrome.driver"));
			webDriver = new ChromeDriver();
		}
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		webDriver.get(sauseDemoUrl);
	}

	/**
	 * reads properties file from given path
	 * @param testngConfigFile
	 * @return
	 * @throws IOException if configuration file is not found
	 */
	private Properties readProperties(String testngConfigFile) throws IOException {
		FileReader fileReader = new FileReader(testngConfigFile);
		Properties properties = new Properties();
		properties.load(fileReader);
		return properties;
	}

	/**
	 * testcase1
	 */
	@Test(description = "Checks login screen has username, password fields and login button", priority = 0)
	public void checkRequiredFieldsExist() {
		webDriver.navigate().refresh();
		Assert.assertEquals(webDriver.findElement(By.id("user-name")).getAttribute("data-test"), "username");
		Assert.assertEquals(webDriver.findElement(By.id("password")).getAttribute("data-test"), "password");
		Assert.assertEquals(webDriver.findElement(By.className("btn_action")).getAttribute("value"), "LOGIN");
	}

	/**
	 * testcase2
	 */
	@Test(description = "Checks login functionality with an invalid username and an invalid password", priority = 1)
	public void checkLoginWithInvalidUsernamePassword() {
		webDriver.navigate().refresh();
		webDriver.findElement(By.id("user-name")).sendKeys("unknown_user");
		webDriver.findElement(By.id("password")).sendKeys("unknown_password");
		webDriver.findElement(By.className("btn_action")).click();

		Assert.assertEquals(
				webDriver.findElement(By.cssSelector(".login-box > form:nth-child(1) > h3:nth-child(4)")).getText(),
				"Epic sadface: Username and password do not match any user in this service");
	}

	/**
	 * testcase3
	 */
	@Test(description = "Check login functionality with an invalid username but without providing a password", priority = 2)
	public void checkLoginWithInvalidUsernameAndNoPassword() {
		webDriver.navigate().refresh();
		// clear user-name and password fields
		webDriver.findElement(By.id("user-name")).clear();
		webDriver.findElement(By.id("user-name")).sendKeys("unknown_user");
		webDriver.findElement(By.id("password")).clear();
		webDriver.findElement(By.className("btn_action")).click();

		Assert.assertEquals(
				webDriver.findElement(By.cssSelector(".login-box > form:nth-child(1) > h3:nth-child(4)")).getText(),
				"Epic sadface: Password is required");
	}

	/**
	 * testcase4
	 */
	@Test(description = "Check login functionality with an invalid password but without providing a username", priority = 3)
	public void checkLoginWithInvalidPasswordAndNoUsername() {
		webDriver.navigate().refresh();
		// clear user-name and password fields
		webDriver.findElement(By.id("user-name")).clear();
		webDriver.findElement(By.id("password")).clear();

		webDriver.findElement(By.id("password")).sendKeys("unknown_password");
		webDriver.findElement(By.className("btn_action")).click();

		Assert.assertEquals(
				webDriver.findElement(By.cssSelector(".login-box > form:nth-child(1) > h3:nth-child(4)")).getText(),
				"Epic sadface: Username is required");
	}

	/**
	 * testcase5
	 */
	@Test(description = "Check login functionality with a valid username and with an invalid password", priority = 4)
	public void checkLoginWithValidUsernameAndInvalidPassword() {
		webDriver.navigate().refresh();
		// clear user-name and password fields
		webDriver.findElement(By.id("user-name")).clear();
		webDriver.findElement(By.id("password")).clear();

		webDriver.findElement(By.id("user-name")).sendKeys("standard_user");
		webDriver.findElement(By.id("password")).sendKeys("unknown_password");
		webDriver.findElement(By.className("btn_action")).click();

		Assert.assertEquals(
				webDriver.findElement(By.cssSelector(".login-box > form:nth-child(1) > h3:nth-child(4)")).getText(),
				"Epic sadface: Username and password do not match any user in this service");
	}

	/**
	 * testcase6
	 */
	@Test(description = "Check login functionality with a valid username and valid password", priority = 5)
	public void checkLoginWithValidUsernameAndValidPassword() {
		webDriver.navigate().refresh();
		webDriver.findElement(By.id("user-name")).sendKeys("standard_user");
		webDriver.findElement(By.id("password")).sendKeys("secret_sauce");
		webDriver.findElement(By.className("btn_action")).click();

		Assert.assertEquals(webDriver.findElement(By.cssSelector(".product_label")).getText(), "Products");
	}

	/**
	 * This method will be run after each test
	 * @param testResult
	 */
	@AfterMethod
	public void takeScreenshotAfterFailure(ITestResult testResult) {
		if (testResult.getStatus() == ITestResult.FAILURE) {
			File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
			saveScreenshot(screenshot, testResult.getName());
		}
	}

	/**
	 * saves screenshot to screenshots folder
	 * @param screenshot location of the screenshot
	 * @param name name of the test case
	 */
	private void saveScreenshot(File screenshot, String name) {
		try (FileInputStream fileInputStream = new FileInputStream(screenshot);
				FileOutputStream fileOutputStream = new FileOutputStream(
						"screenshots//" + name + ".png");) {
			int byteToRead = 0;
			while ((byteToRead = fileInputStream.read()) != -1) {
				fileOutputStream.write(byteToRead);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * cleans the web driver resource
	 */
	@AfterSuite(alwaysRun = true)
	public void cleanup() {
		if (webDriver != null) {
			webDriver.close();
		}
	}
}
