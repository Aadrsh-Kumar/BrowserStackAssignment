package com.browserstack.tests;

import com.browserstack.base.BaseTest;
import com.browserstack.pageobjects.ElPaisHomePage;
import com.browserstack.pageobjects.ElPaisOpinionPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

public class BrowserStackTechAssignment extends BaseTest {

  @Test
  public void technicalAssignment() {
    WebDriver driver = getDriver();
    ElPaisHomePage homePage = new ElPaisHomePage(driver);
    ElPaisOpinionPage opinionPage = new ElPaisOpinionPage(driver);

    /* Task 1 of Assignment */
    homePage.launchApplication();
    homePage.acceptCookieAlert();
    homePage.ensureESPANAisSelectedAsLanguage();

    /* Task 2 of Assignment */
    opinionPage.navigateToOpinionSection();
    List<WebElement> articles = opinionPage.fetchFirstFiveArticles();
    List<String>articleTitles= opinionPage.printArticleTitleContentInSpanishAndDownloadImage(articles);

    /* Task 3 of Assignment */
    List<String>translatedTitles = opinionPage.translateAndPrintArticleTitles(articleTitles);

    /*Task 4: Analyze Translated headers and print repeated words with frequency */
    opinionPage.analyzeTitles(translatedTitles);
  }
}
