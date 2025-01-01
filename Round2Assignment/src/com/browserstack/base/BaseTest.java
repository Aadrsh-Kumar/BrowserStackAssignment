package com.browserstack.base;

import com.browserstack.utils.UtilityClass;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class BaseTest {
  private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

  /** Executes before each @Test method */
  @BeforeMethod
  @Parameters({"browser"})
  public void beforeMethodSetup(String browser) {
    try {
      Boolean isLocal = Boolean.valueOf(UtilityClass.getConfigValue("isLocal"));
      WebDriver driver = initializeDriver(browser, isLocal);
      driverThreadLocal.set(driver); // Set the driver in ThreadLocal
      if (browser.equalsIgnoreCase("chrome") || browser.equalsIgnoreCase("firefox") || browser.equalsIgnoreCase("edge"))
        driver.manage().window().maximize();
    } catch (Exception e) {
      System.out.println("Error occurred in BeforeMethod setup: " + e.getLocalizedMessage());
    }
  }

  /** Executes after each @Test method */
  @AfterMethod
  public void afterMethod(ITestResult result) {
    try {
      WebDriver driver = driverThreadLocal.get();
      if (driver != null) {
        tearDownBrowserStack(result, driver);
      }
    } catch (Exception e) {
      System.out.println("Error occurred in AfterMethod teardown: " + e.getLocalizedMessage());
    } finally {
      driverThreadLocal.remove(); // Clean up the ThreadLocal to avoid memory leaks
    }
  }

  /** Tear down the BrowserStack session and mark the status */
  public void tearDownBrowserStack(ITestResult result, WebDriver driver) {
    try {
      if (driver instanceof RemoteWebDriver) {
        RemoteWebDriver webDriver = (RemoteWebDriver) driver;

        // Determine the test status
        String status = result.isSuccess() ? "passed" : "failed";
        String reason = result.isSuccess() ? "" : result.getThrowable().getMessage();

        // Mark the test status on BrowserStack
        markTestStatus(status, reason, webDriver);

        // Quit the driver
        webDriver.quit();
      } else {
        driver.quit();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** Marks the test status on BrowserStack */
  private void markTestStatus(String status, String reason, RemoteWebDriver webDriver) {
    try {
      String sessionId = webDriver.getSessionId().toString();
      URL url = new URL("https://api.browserstack.com/automate/sessions/" + sessionId + ".json");

      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setRequestMethod("PUT");
      connection.setRequestProperty("Content-Type", "application/json");

      String auth =
          UtilityClass.getConfigValue("BROWSERSTACK_USERNAME").trim()
              + ":"
              + UtilityClass.getConfigValue("BROWSERSTACK_ACCESS_KEY").trim();
      String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());
      connection.setRequestProperty("Authorization", "Basic " + encodedAuth);

      // JSON payload for status update
      String payload = "{ \"status\": \"" + status + "\", \"reason\": \"" + reason + "\" }";

      // Send request
      try (OutputStream os = connection.getOutputStream()) {
        os.write(payload.getBytes());
        os.flush();
      }

      if (connection.getResponseCode() != 200) {
        System.out.println(
            "Failed to mark test status on BrowserStack: " + connection.getResponseMessage());
      } else {
        System.out.println("Test status marked successfully on BrowserStack.");
      }
      connection.disconnect();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** Method to initialize driver for both local and BrowserStack execution. */
  public WebDriver initializeDriver(String browser, Boolean isLocal) throws IOException {
    if (isLocal) {
      // Local execution setup
      if (browser.equalsIgnoreCase("chrome")) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        return new ChromeDriver(options);
      } else if (browser.equalsIgnoreCase("firefox")) {
        return new FirefoxDriver();
      } else if (browser.equalsIgnoreCase("safari")) {
        return new SafariDriver();
      } else {
        throw new IllegalArgumentException("Unsupported browser for local execution: " + browser);
      }
    } else {
      // BrowserStack setup
      DesiredCapabilities caps = new DesiredCapabilities();
      HashMap<String, Object> bStackCaps = new HashMap<>();
      bStackCaps.put("projectName", "BrowserStackTechnicalAssignment");
      bStackCaps.put("buildName", "ParallelExecution");
      caps.setCapability("bstack:options", bStackCaps);

      String userName = UtilityClass.getConfigValue("BROWSERSTACK_USERNAME").trim();
      String accessKey = UtilityClass.getConfigValue("BROWSERSTACK_ACCESS_KEY").trim();
      URL browserStackUrl =
          new URL("http://" + userName + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub");

      if (browser.equalsIgnoreCase("chrome")) {
        caps.setCapability("browserName", "Chrome");
        caps.setCapability("browserVersion", "latest");
      } else if (browser.equalsIgnoreCase("firefox")) {
        caps.setCapability("browserName", "Firefox");
        caps.setCapability("browserVersion", "latest");
      } else if (browser.equalsIgnoreCase("edge")) {
        caps.setCapability("browserName", "Edge");
        caps.setCapability("browserVersion", "latest");
      } else if (browser.equalsIgnoreCase("android_mobile")) {
        // Android Mobile setup
        MutableCapabilities androidCap = new MutableCapabilities();
        HashMap<String, Object> androidOptions = new HashMap<>();
        androidOptions.put("deviceName", "Google Pixel 6");
        androidOptions.put("realMobile", "true");
        androidOptions.put("osVersion", "12.0");
        androidOptions.put("local", "false");
        androidOptions.put("projectName", "BrowserStackTechnicalAssignment");
        androidOptions.put("buildName", "ParallelExecution");
        androidCap.setCapability("bstack:options", androidOptions);
        return new RemoteWebDriver(browserStackUrl, androidCap);
      } else if (browser.equalsIgnoreCase("android_tab")) {
        // Android Tablet setup
        MutableCapabilities androidCap = new MutableCapabilities();
        HashMap<String, Object> androidOptions = new HashMap<>();
        androidOptions.put("deviceName", "Samsung Galaxy Tab A9 Plus");
        androidOptions.put("realMobile", "true");
        androidOptions.put("osVersion", "14.0");
        androidOptions.put("local", "false");
        androidOptions.put("projectName", "BrowserStackTechnicalAssignment");
        androidOptions.put("buildName", "ParallelExecution");
        androidCap.setCapability("bstack:options", androidOptions);
        return new RemoteWebDriver(browserStackUrl, androidCap);
      } else {
        throw new IllegalArgumentException(
            "Unsupported browser for BrowserStack execution: " + browser);
      }

      return new RemoteWebDriver(browserStackUrl, caps);
    }
  }

  /** Get the current driver instance */
  public WebDriver getDriver() {
    return driverThreadLocal.get();
  }
}
