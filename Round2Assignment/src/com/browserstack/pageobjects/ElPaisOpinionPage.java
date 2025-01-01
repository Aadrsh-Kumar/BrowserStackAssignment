package com.browserstack.pageobjects;

import com.browserstack.base.BasePage;
import com.browserstack.utils.UtilityClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElPaisOpinionPage extends BasePage {

  public ElPaisOpinionPage(WebDriver driver) {
    this.driver = driver;
  }

  /**
   * Element locator present on Home Page The locators are saved in format of
   * <LocatorType>###<LocatorValue>
   */
  private final String opinionSectionLink = "LinkText###Opinión";

  private final String opinionSectionHeader = "class###cs_t_e";
  private final String articles = "tagName###article";

  /** Method to navigate to Opinion Section */
  public void navigateToOpinionSection() {
    try {
      clickElement(opinionSectionLink);
      waitForElementToAppear(opinionSectionHeader, 20);
      if (getWebElement(opinionSectionHeader).getText().trim().equals("Opinión"))
        System.out.println("Navigated to Opinion section of application");
      else Assert.fail("Not navigated to Opinion Section of application");
    } catch (Exception e) {
      Assert.fail("Error while navigating to Opinion section");
    }
  }

  /** Method to navigate to Opinion Section */
  public List<WebElement> fetchFirstFiveArticles() {
    List<WebElement> articlesElement = new ArrayList<>();
    try {
      articlesElement = getWebElements(articles);
      int iterator = 5;

      if (articlesElement.size() < iterator) {
        iterator = articlesElement.size();
        System.out.println("Less than 5 articles present on the page. Total Articles: " + iterator);
      }
      return articlesElement.subList(0, iterator);
    } catch (Exception e) {
      Assert.fail("Error while fetching first 5 articles");
    }
    return articlesElement;
  }

  /**
   * Method to print article title and content in spanish and download the image
   *
   * @param articles articles list
   */
  public List<String> printArticleTitleContentInSpanishAndDownloadImage(List<WebElement> articles) {
    List<String> articleTitles = new ArrayList<>();
    try {
      System.out.println("Printing Articles details in Spanish:");
      for (int iterator = 0; iterator < articles.size(); iterator++) {
        System.out.println("Article " + (iterator + 1) + ": ");
        String title = articles.get(iterator).findElement(By.xpath("header/h2")).getText().trim();
        articleTitles.add(title);
        System.out.println("Title: " + title);
        System.out.println(
            "Content: " + articles.get(iterator).findElement(By.tagName("p")).getText().trim());
        downloadImageifAvailable(articles.get(iterator), "Article" + (iterator + 1) + "Image.jpg");
        System.out.println("*********************");
      }
    } catch (Exception e) {
      Assert.fail("Error occurred while printing article in spanish " + e.getLocalizedMessage());
    }
    return articleTitles;
  }

  /**
   * Download image from article if available
   *
   * @param article article
   */
  private void downloadImageifAvailable(WebElement article, String imageName) {
    try {
      if (!article.findElements(By.tagName("img")).isEmpty()) {
        WebElement imageElement = article.findElement(By.tagName("img"));
        String imageUrl = imageElement.getAttribute("src");
        UtilityClass.downloadImage(imageUrl, imageName);
      } else {
        System.out.println("No image found in the article.");
      }
    } catch (Exception e) {
      Assert.fail(e.getLocalizedMessage());
    }
  }

  /**
   * Translates all titles from Spanish to English and returns the translated list.
   *
   * @param articleTitles List of article titles in Spanish.
   * @return List of translated article titles in English.
   */
  public List<String> translateAndPrintArticleTitles(List<String> articleTitles) {
    List<String> translatedTitles = new ArrayList<>();
    System.out.println("Printing Article title in English");
    for (int iterator = 0; iterator < articleTitles.size(); iterator++) {
      String translatedTitle =
          UtilityClass.translateText(
              articleTitles.get(iterator), "es", "en"); // Translate to English
      System.out.println("Article " + (iterator + 1) + ": ");
      System.out.println("Title: " + translatedTitle);
      System.out.println("*****************************");
      translatedTitles.add(translatedTitle);
    }
    return translatedTitles;
  }

  /**
   * Analyze Titles and find repeated words
   *
   * @param translatedTitles titles
   */
  public void analyzeTitles(List<String> translatedTitles) {
    Map<String, Integer> wordFrequency = new HashMap<>();

    /* Process each title to calculate word frequencies*/
    for (String title : translatedTitles) {
      String[] words = title.split("\\s+");
      for (String word : words) {

        word = word.toLowerCase().replaceAll("[^a-zA-Z]", "");
        if (!word.isEmpty()) {
          wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
        }
      }
    }

    /*Print words repeated more than twice*/
    System.out.println("Words repeated more than twice:");
    for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
      if (entry.getValue() > 2) {
        System.out.println(entry.getKey() + ": " + entry.getValue());
      }
    }
  }
}
