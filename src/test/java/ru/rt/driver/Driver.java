package ru.rt.driver;

import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class Driver {

    public static ChromeDriver getDriver(){
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\MakcoH\\Downloads\\chromedriver_win32\\chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        return driver;
    }

    public static void closeDriver(ChromeDriver driver){
        driver.quit();
    }
}
