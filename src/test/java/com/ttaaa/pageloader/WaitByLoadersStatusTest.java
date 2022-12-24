package com.ttaaa.pageloader;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class WaitByLoadersStatusTest extends AbstractTestClass {

    @Test
    public void checkWaitByLoadersStatusMethodTrue() {
        driver.get("http://localhost:63342/PageLoader/SimpleMethodTest.html?_ijt=cf7c738fsdg7ubad11l3hmiuae");

        driver.findElement(By.id("start_button")).click();

        ArrayList<By> identifiersList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            identifiersList.add(By.id("loading_text_" + i));
        }

        assertDoesNotThrow(() -> pageLoader.waitByLoadersStatus(identifiersList));
        assertTrue(driver.findElement(By.id("loaded_text")).isDisplayed());
    }

    @Test
    public void checkWaitByLoadersStatusMethod() {
        driver.get("http://localhost:8080/PageLoader/SimpleMethodTest.html");

        driver.findElement(By.id("start_button")).click();

        ArrayList<By> identifiersList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            identifiersList.add(By.id("loading_text_" + i));
        }

        assertDoesNotThrow(() ->
                pageLoader.waitByLoadersStatus(identifiersList));
        assertTrue(driver.findElement(By.id("loaded_text")).isDisplayed());
    }

    @Test
    public void checkNegativeWaitByLoadersStatusMethod() {
        driver.get("http://localhost:8080/PageLoader/SimpleMethodTest.html");

        driver.findElement(By.id("start_button")).click();

        ArrayList<By> identifiersList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            identifiersList.add(By.id("loading_text_" + i));
        }

        assertThrows(
                TimeoutException.class,
                () -> pageLoader.waitByLoadersStatus(
                        identifiersList,
                        500
                )
        );
    }
}
