package com.browserstack.pageobjects;

import com.browserstack.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class ElPaisHomePage extends BasePage {
    public ElPaisHomePage(WebDriver driver){
        this.driver = driver;
    }

    /**
     * Element locator present on Home Page
     * The locators are saved in format of <LocatorType>###<LocatorValue>
     */

    private final String cookieAcceptButton = "id###didomi-notice-agree-button";
    private final String languageDropdown = "id###edition_head";
    private final String spanishLanguageOption = "cssSelector###li[data-edition='el-pais']";

    /**
     * Method to accept cookies alert displayed on home page
     */
    public void acceptCookieAlert() {
        waitForElementToAppear(cookieAcceptButton, 20);
        clickElement(cookieAcceptButton);
    }

    /**
     * Method to ensure that Text is in Spanish
     */
    public void ensureESPANAisSelectedAsLanguage(){
        try{
            if(getWebElement(languageDropdown).getText().trim().equals("España".toUpperCase()))
                System.out.println("Website's text is displayed in Spanish");
            else{
                clickElement(languageDropdown);
                clickElement(spanishLanguageOption);
            }
        }catch (Exception e){
            Assert.fail("Error occurred while checking language selection "+e.getLocalizedMessage());
        }
    }
}
