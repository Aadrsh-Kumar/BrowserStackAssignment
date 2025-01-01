package com.browserstack.utils;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.testng.Assert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class UtilityClass {
    /**
     * Method to get value from AutomationConfig file.
     *
     * @param key Key
     * @return Value
     */
    public static String getConfigValue(String key) throws IOException {
        Properties property = new Properties();
        try (FileInputStream fs = new FileInputStream("automationConfig.properties")) {
            property.load(fs);
            return property.getProperty(key);
        } catch (FileNotFoundException e) {
            System.out.println("Config properties file not found"+e.getLocalizedMessage());
        } catch (IOException e) {
            System.out.println("Error occurred while fetching value form config file"+ e.getLocalizedMessage());
        }
        return property.toString();
    }

    /**
     * Utility method to download an image.
     *
     * @param imageUrl      URL of the image to download
     * @param imageFileName Name of the image file to save
     */
    public static void downloadImage(String imageUrl, String imageFileName) {
        /* Create "ArticleImages" directory in the project root if it doesn't exist*/
        String directoryPath = System.getProperty("user.dir") + File.separator + "ArticleImages";
        File directory = new File(directoryPath);
        if (!directory.exists() && !directory.mkdirs()) {
            System.out.println("Failed to create directory 'ArticleImages'.");
            return;
        }

        String filePath = directoryPath + File.separator + imageFileName;

        try {
            // Connect to the image URL and download the image
            URL url = new URL(imageUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");

            try (BufferedInputStream in = new BufferedInputStream(httpConn.getInputStream());
                 FileOutputStream fileOut = new FileOutputStream(filePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    fileOut.write(buffer, 0, bytesRead);
                }
            }

            System.out.println("Image downloaded successfully: " + filePath);

        } catch (IOException e) {
            System.out.println("Failed to download image from: " + imageUrl);
            e.printStackTrace();
        }
    }

    /**
     * Translates a given text to the specified target language.
     *
     * @param text          Text to translate.
     * @param targetLanguage Target language code (e.g., "en" for English).
     * @return Translated text.
     */
    public static String translateText(String text, String sourceLang, String targetLanguage) {
        try {
            String apiKey = getConfigValue("GoogleCloudAPIKey");
            Translate translate = TranslateOptions.newBuilder()
                    .setApiKey(apiKey)
                    .build()
                    .getService();
            Translation translation = translate.translate(
                    text,
                    Translate.TranslateOption.sourceLanguage(sourceLang),
                    Translate.TranslateOption.targetLanguage(targetLanguage)
            );
            return translation.getTranslatedText();
        } catch (Exception e) {
            Assert.fail("Error during translation: " + e.getMessage());
        }
        return  null;
    }
}