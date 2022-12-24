package com.ttaaa.pageloader;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WaitByDOMSizeTest extends AbstractTestClass {

    @Test
    public void checkWaitByDOMSizeMethodTrue() {
        driver.get("http://localhost:63342/PageLoader/SimpleMethodTest.html?_ijt=cf7c738fsdg7ubad11l3hmiuae");

        driver.findElement(By.id("start_button")).click();

        ArrayList<By> identifiersList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            identifiersList.add(By.id("loading_text_" + i));
        }

        pageLoader.waitByLoadersStatus(identifiersList);
        WebElement element = driver.findElement(By.id("loaded_text"));
        assertTrue(element.isDisplayed());
    }

    @Test
    public void checkWaitByDOMSizeMethod() {
        driver.get("http://localhost:8080/PageLoader/DOMMethodTest.html");

        driver.findElement(By.id("start_button")).click();

        assertDoesNotThrow(() -> pageLoader.waitByDOMSize());
    }

    @Test
    public void checkImprovedWaitByDOMSizeMethod() {
        driver.get("http://localhost:8080/PageLoader/ImrovedDOMMethodTest.html");

        driver.findElement(By.id("start_button")).click();

        assertDoesNotThrow(() ->
                pageLoader.waitByImprovedDOMSize(By.id("loading_text_field")));
    }
}
