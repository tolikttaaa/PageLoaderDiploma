package com.ttaaa.pageloader;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

public class FindDynamicElementTest extends AbstractTestClass {

    @Test
    public void checkFindDynamicElementMethodTrue() {
        driver.get("http://localhost:63342/PageLoader/SimpleMethodTest.html?_ijt=bt6n99fj9pre33ent0dece3nj0");

        driver.findElement(By.id("start_button")).click();

        assertTrue(pageLoader.findDynamicElement(By.id("loaded_text")).isDisplayed());
    }

    @Test
    public void checkFindDynamicElementMethod() {
        driver.get("http://localhost:8080/PageLoader/SimpleMethodTest.html");

        driver.findElement(By.id("start_button")).click();

        assertTrue(pageLoader.findDynamicElement(By.id("loaded_text"))
                        .isDisplayed());
    }

    @Test
    public void checkNegativeFindDynamicElementMethod() {
        driver.get("http://localhost:8080/PageLoader/SimpleMethodTest.html");

        driver.findElement(By.id("start_button")).click();

        assertThrows(
                TimeoutException.class,
                () -> pageLoader.findDynamicElement(
                        By.id("loaded_text"),
                        500
                )
        );
    }
}
