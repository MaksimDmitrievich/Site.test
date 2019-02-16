package ru.rt.site;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.rt.driver.Driver;
import org.junit.runners.Parameterized.Parameters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(value = Parameterized.class)
public class CreateOrders {
    private static ChromeDriver driver;
    private static final String FILENAME = "Create Orders.csv";

    @Before
    public void setUp() {
        driver = Driver.getDriver();
    }

    @After
    public void tearDown() {
        Driver.closeDriver(driver);

    }

    private String fullName;
    private String street;
    private String building;
    private String flat;
    private String phoneNumber;
    private String email;
    private String comments;

    public CreateOrders(String fullName, String street, String building, String flat, String phoneNumber, String email,
                        String comments) {
        this.fullName = fullName;
        this.street = street;
        this.building = building;
        this.flat = flat;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.comments = comments;
    }

    @Parameters
    public static Collection getData() {
        return readDataFromFile();
    }

    private static Collection<String[]> readDataFromFile() {
        List<String[]> records = new ArrayList<>();
        String record;
        BufferedReader reader;

        try {
            File file = new File(FILENAME);
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            reader = new BufferedReader(isr);
            while ((record = reader.readLine()) != null) {
                String[] fields = record.split(",");
                records.add(fields);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }


    @Test
    public void createOrderFromSite() {
        driver.get("https://ekt.rt.ru/packages/tariffs/all");

        String title = driver.getTitle();
        Assert.assertEquals("Ростелеком", title);

        WebElement connect = driver.findElement(By.cssSelector("#block-rt-ru-super-content > div > div > div > div > article > div > div > div.area.rt-space-bottom > div > div > div > div.rtk-fo__case.rtk-card__offers > div > div.rtk-carousel__overlay > div > div:nth-child(2) > div > div > div.rtk-card__actions > button > div"));
        connect.click();

        WebElement nameField = driver.findElementById("fullName");
        nameField.sendKeys(fullName);

        WebElement streetField = driver.findElementById("street");
        streetField.sendKeys(street);

        WebElement enter = driver.findElement(By.cssSelector("#stepPersonalData > div > div.rtk-collapser__body > div > div > div > div:nth-child(2) > div.rt-col-5.rt-col-td-3.rt-col-md-3 > div > div > ul > li > div.parent__child-wrap--steet"));
        enter.click();

        WebElement house = driver.findElementById("house");
        house.sendKeys(building);

        WebElement apartmentField = driver.findElementById("apartment");
        apartmentField.sendKeys(flat);

        WebElement number = driver.findElement(By.cssSelector("input.form__control:nth-child(1)"));
        for (char num : phoneNumber.toCharArray()) {
            number.sendKeys(String.valueOf(num));
        }

        WebElement mail = driver.findElementByName("email");
        mail.sendKeys(email);

        WebElement comment = driver.findElementById("comment");
        comment.sendKeys(comments);

        WebElement ticket = driver.findElement(By.cssSelector("button.btn"));
        ticket.click();
        ticket.click();

        WebElement orderNumber = driver.findElementById("order_id");
        String order = orderNumber.getText();
        System.out.println("order: " + order);

        try (FileOutputStream fos = new FileOutputStream("Order From CreateOrders.txt")) {
            byte[] buffer = order.getBytes();

            fos.write(buffer, 0, buffer.length);
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }
}
