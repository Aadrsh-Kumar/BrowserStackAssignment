package com.browserstack.base;

import com.browserstack.utils.UtilityClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BasePage {
  protected WebDriver driver;

  /** Method to launch Application */
  public void launchApplication() {
    try {
      driver.get(UtilityClass.getConfigValue("appURL"));
    } catch (Exception e) {
      Assert.fail(
          "Error occurred while launching the applicatioin" + e.getLocalizedMessage());
    }
  }

  /**
   * Method to wait for element to appear
   *
   * @param element element locator and locator value (format <locatorType###locatorvalue)
   * @param timeOut timeout
   */
  public void waitForElementToAppear(String element, int timeOut) {
    try {
      String locatorType = element.split("###")[0];
      String locatorValue = element.split("###")[1];
      if (locatorType != null) {
        if (element.contains("id")) {
          (new WebDriverWait(driver, Duration.ofSeconds(timeOut)))
              .until(ExpectedConditions.visibilityOfElementLocated(By.id(locatorValue)));
        } else if (element.contains("class")) {
          (new WebDriverWait(driver, Duration.ofSeconds(timeOut)))
              .until(ExpectedConditions.visibilityOfElementLocated(By.className(locatorValue)));
        } else if (element.contains("xpath")) {
          (new WebDriverWait(driver, Duration.ofSeconds(timeOut)))
              .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorValue)));
        } else if (element.contains("cssSelector")) {
          (new WebDriverWait(driver, Duration.ofSeconds(timeOut)))
              .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locatorValue)));
        } else if (element.contains("LinkText")) {
          (new WebDriverWait(driver, Duration.ofSeconds(timeOut)))
              .until(ExpectedConditions.visibilityOfElementLocated(By.linkText(locatorValue)));
            }
      }
    } catch (Exception e) {
      System.out.println("Error occurred while waiting for element: " + element);
    }
  }

  /**
   * Method to get WebElement for the given element details
   *
   * @param element element details
   * @return element
   */
  public WebElement getWebElement(String element) {
    WebElement webElement = null;
    try {
      String locatorType = element.split("###")[0];
      String locatorValue = element.split("###")[1];
      if (locatorType != null) {
        if (element.contains("id")) {
          webElement = driver.findElement(By.id(locatorValue));
        } else if (element.contains("class")) {
          webElement = driver.findElement(By.className(locatorValue));
        } else if (element.contains("xpath")) {
          webElement = driver.findElement(By.xpath(locatorValue));
        } else if (element.contains("cssSelector")) {
          webElement = driver.findElement(By.cssSelector(locatorValue));
        } else if (element.contains("LinkText")) {
          webElement = driver.findElement(By.linkText(locatorValue));
        }
        else if (element.contains("tagName")) {
          webElement = driver.findElement(By.tagName(locatorValue));
        }
      }
    } catch (Exception e) {
      System.out.println("Error occurred while waiting for element: " + element);
    }
    return webElement;
  }

  /**
   * Method to get list of WebElement for the given element details
   *
   * @param element element details
   * @return elements
   */
  public List<WebElement> getWebElements(String element) {
    List<WebElement> webElement = new ArrayList<>();
    try {
      String locatorType = element.split("###")[0];
      String locatorValue = element.split("###")[1];
      if (locatorType != null) {
        if (element.contains("id")) {
          webElement = driver.findElements(By.id(locatorValue));
        } else if (element.contains("class")) {
          webElement = driver.findElements(By.className(locatorValue));
        } else if (element.contains("xpath")) {
          webElement = driver.findElements(By.xpath(locatorValue));
        } else if (element.contains("cssSelector")) {
          webElement = driver.findElements(By.cssSelector(locatorValue));
        }
        else if (element.contains("LinkText")) {
            webElement = driver.findElements(By.linkText(locatorValue));
        }
        else if (element.contains("tagName")) {
          webElement = driver.findElements(By.tagName(locatorValue));
        }
      }
    } catch (Exception e) {
      System.out.println("Error occurred while waiting for element: " + element);
    }
    return webElement;
  }

  /**
   * Method to click webElement
   * @param elementName element locator details
   */
  public void clickElement(String elementName){
      try{
          waitForElementToAppear(elementName, 10);
          WebElement element = getWebElement(elementName);
          element.click();
      }catch (Exception e){
          System.out.println("Error occurred while clicking element: "+elementName + e.getLocalizedMessage());
      }
  }
}
