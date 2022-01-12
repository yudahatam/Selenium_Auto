package Selenium_Auto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class App {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Add chromedriver to path to use to open the browser
        System.setProperty("webdriver.chrome.driver", "Z:/School_Programs/Selenium Drivers/chromedriver.exe");
        // Get input data from user
        System.out.println("Please enter your username");
        String username = input.nextLine();
        System.out.println("Thank you! \nNow please enter your password");
        String password = input.nextLine();
        

        // Enter chrome driver
        WebDriver driver = new ChromeDriver();
        // Use this url for page
        String URL = "https://www.aac.ac.il/";
        // Go to url
        driver.get(URL);
        // Take control of login button, take the url and change the page to login page
        WebElement loginPage = driver.findElement(By
                .cssSelector("body > div.wrapper > header > div.main.row > div.wrap > div.info-btn > a:nth-child(1)"));
        driver.get(loginPage.getAttribute("href"));
        // Take control of username,password fields and login button
        WebElement usernameField = driver.findElement(By.cssSelector("#Ecom_User_ID"));
        WebElement passwordField = driver.findElement(By.cssSelector("#Ecom_Password"));
        WebElement loginButton = driver.findElement(By.cssSelector("#wp-submit"));
        // Enter the data given by the user and press login button
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();
        /**
         * Until the login detail are correct ask the user for them and try again
         */
        while (driver.getCurrentUrl().equals("https://is.aac.ac.il/nidp/idff/sso?sid=0&sid=0")) {
            System.out.println("Wrong username or password please enter again");
            System.out.println("Please enter your username");
            username = input.nextLine();
            System.out.println("Thank you! \nNow please enter your password");
            password = input.nextLine();
            usernameField = driver.findElement(By.cssSelector("#Ecom_User_ID"));
            passwordField = driver.findElement(By.cssSelector("#Ecom_Password"));
            loginButton = driver.findElement(By.cssSelector("#wp-submit"));
            usernameField.sendKeys(username);
            passwordField.sendKeys(password);
            loginButton.click();
        }
        // Take control of moodle system button and click it
        WebElement moodleLogin = driver.findElement(By.cssSelector(
                "body > div.pageContentWrapper > div > div.row > div.col-md-7.nopadding > div > div > div:nth-child(4) > "
                        + "div.col-sm-6.dot-right.dot-bottom > a"));
        moodleLogin.click();
        // Wait 2 second for page to load completely
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            System.out.println("Couldn't wait 2 seconds");
            e.printStackTrace();
        }
        // Create list to take control of all the elements of <a></a> tag
        List<WebElement> sites = driver.findElements(By.tagName("a"));
        // Create map for course number and its url
        Map<Integer, String> courseMap = new HashMap<>();
        // Initiate counter for courses
        int cnt = 0;
        /**
         * Go over all the elements and take their link, clean them and put them in map
         */
        for (WebElement site : sites) {
            // Take the attribute of href which is the url of the course page
            String siteUrl = site.getAttribute("href");
            // Check if the link that was taken is of a course in the main page and not a
            // course in the side menu
            if (siteUrl.contains("https://moodle.aac.ac.il/course")
                    && (site.getAttribute("class").contains("aalink"))) {
                cnt++;
                // Take the class name and clean it from the user preferences
                String courseName = site.getText().replace("הקורס סומן כמועדף", "").replace("שם הקורס", "").strip();
                // Put the url in the map with its corresponding number
                courseMap.put(cnt, siteUrl);
                // Print the list of courses and their number to user
                System.out.println(cnt + "  " + courseName);
            }
        }
        // Ask the user for number of course
        System.out.println("Please choose the number of the course you want to enter");
        int userChoice = input.nextInt();
        // Until the number is in the valid range ask the user again
        while (userChoice < 1 || userChoice > courseMap.size()) {
            System.out.println("Invalid number entered please enter another");
            userChoice = input.nextInt();
        }
        // Change the page to the page of the course that the user chose
        driver.get(courseMap.get(userChoice));

        /**
         * Take control of the buttons and click on them in order to properly exit the
         * system
         */
        WebElement profileButton = driver
                .findElement(By.cssSelector("#action-menu-toggle-1 > span > span.avatars > span > img"));
        profileButton.click();
        WebElement logOutMoodleButton = driver.findElement(By.cssSelector("#actionmenuaction-6"));
        logOutMoodleButton.click();
        WebElement logOutSystemButton = driver.findElement(By.cssSelector("#menu-top-header > li:nth-child(2) > a"));
        logOutSystemButton.click();
        // Close the scanner
        input.close();
        // Change the page to original page of system
        driver.get(URL);
    }
}
