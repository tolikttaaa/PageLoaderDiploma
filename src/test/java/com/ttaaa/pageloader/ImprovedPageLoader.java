package com.ttaaa.pageloader;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ImprovedPageLoader {
    private final WebDriver driver;

    /**
     * Default constructor for {@link ImprovedPageLoader}.
     *
     * @param driver {@link WebDriver} to implement browser actions
     */
    public ImprovedPageLoader(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Generic method for waiting for web page dynamic elements to load
     * with default thresholdTime of {@value MAX_THRESHOLD_TIME}
     * milliseconds.
     *
     * @param identifier {@link By} identifier of the web element to load
     * @return the found {@link WebElement}
     * @throws TimeoutException if waiting time exceeded thresholdTime
     */
    public WebElement findDynamicElement(By identifier) {
        return findDynamicElement(identifier, MAX_THRESHOLD_TIME);
    }

    /**
     * Generic method for waiting for web page dynamic elements to load.
     *
     * @param identifier    {@link By} identifier of the web element to
     *                      load
     * @param thresholdTime max waiting time in milliseconds
     * @return the found {@link WebElement}
     * @throws TimeoutException if waiting time exceeded thresholdTime
     */
    public WebElement findDynamicElement(
            By identifier,
            long thresholdTime
    ) {
        return (new WebDriverWait(driver, thresholdTime))
                .until((ExpectedCondition<WebElement>) driver ->
                        driver.findElement(identifier));
    }

    /**
     * Method of waiting for a web page to load by analysing load
     * indicators
     * <ul>
     *     <li>The maximum waiting time is equal to the default value
     *     {@value MAX_THRESHOLD_TIME} milliseconds</li>
     *     <li>The time between rechecks is equal to the default value
     *     {@value TIME_BEFORE_NEXT_CHECK} milliseconds</li>
     * </ul>
     *
     * @param loadersIdentifiers list of loading indicator identifiers
     * @throws TimeoutException if waiting time exceeded thresholdTime
     */
    public void waitByLoadersStatus(By... loadersIdentifiers) {
        waitByLoadersStatus(
                Arrays.stream(loadersIdentifiers).toList(),
                MAX_THRESHOLD_TIME,
                TIME_BEFORE_NEXT_CHECK
        );
    }

    /**
     * Method of waiting for a web page to load by analysing load
     * indicators
     * <ul>
     *     <li>The maximum waiting time is equal to the default value
     *     {@value MAX_THRESHOLD_TIME} milliseconds</li>
     *     <li>The time between rechecks is equal to the default value
     *     {@value TIME_BEFORE_NEXT_CHECK} milliseconds</li>
     * </ul>
     *
     * @param loadersIdentifiers list of loading indicator identifiers
     * @throws TimeoutException if waiting time exceeded thresholdTime
     */
    public void waitByLoadersStatus(List<By> loadersIdentifiers) {
        waitByLoadersStatus(
                loadersIdentifiers,
                MAX_THRESHOLD_TIME,
                TIME_BEFORE_NEXT_CHECK
        );
    }

    /**
     * Method of waiting for a web page to load by analysing load
     * indicators
     * <ul>
     *     <li>The time between rechecks is equal to the default value
     *     {@value TIME_BEFORE_NEXT_CHECK} milliseconds</li>
     * </ul>
     *
     * @param loadersIdentifiers list of loading indicator identifiers
     * @param thresholdTime      max waiting time in milliseconds
     * @throws TimeoutException if waiting time exceeded thresholdTime
     */
    public void waitByLoadersStatus(
            List<By> loadersIdentifiers,
            long thresholdTime
    ) {
        waitByLoadersStatus(
                loadersIdentifiers,
                thresholdTime,
                TIME_BEFORE_NEXT_CHECK
        );
    }

    /**
     * Method of waiting for a web page to load by analysing load
     * indicators
     *
     * @param loadersIdentifiers list of loading indicator identifiers
     * @param thresholdTime      max waiting time in milliseconds
     * @param waitingTime        time between rechecks
     * @throws TimeoutException if waiting time exceeded thresholdTime
     */
    public void waitByLoadersStatus(
            List<By> loadersIdentifiers,
            long thresholdTime,
            long waitingTime
    ) {
        if (loadersIdentifiers.isEmpty()) return;

        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < thresholdTime) {
            if (loadersIdentifiers.stream()
                    .map(this::getElement)
                    .allMatch(it -> it == null || !it.isDisplayed()))
                return;

            long currentTime = System.currentTimeMillis();

            waitingTime = Math.min(waitingTime,
                    startTime + thresholdTime - currentTime + 1);

            try {
                Thread.sleep(waitingTime);
            } catch (InterruptedException ignored) {
            }
        }

        if (loadersIdentifiers.stream()
                .map(this::getElement)
                .allMatch(it -> it == null || !it.isDisplayed())) return;

        throw new TimeoutException(
                "Page was not loaded in the specified time "
                        + thresholdTime + "!"
        );
    }

    /**
     * A method for waiting for a web page to load by analysing its DOM
     * <ul>
     *     <li>The maximum waiting time is equal to the default value
     *     {@value MAX_THRESHOLD_TIME} milliseconds</li>
     *     <li>The time during which the size of the analyzed elements
     *     must remain constant is equal to the default value
     *     {@value UNCHANGED_WAITING_TIME} milliseconds</li>
     *     <li>The time between rechecks is equal to the default value
     *     {@value TIME_BEFORE_NEXT_CHECK} milliseconds</li>
     *     <li>The final size of the analysed web page necessarily
     *     differ from the starting one.</li>
     * </ul>
     *
     * @throws TimeoutException if waiting time exceeded thresholdTime
     */
    public void waitByDOMSize() {
        waitByDOMSize(
                MAX_THRESHOLD_TIME,
                UNCHANGED_WAITING_TIME,
                TIME_BEFORE_NEXT_CHECK,
                true
        );
    }

    /**
     * A method for waiting for a web page to load by analysing its DOM
     * <ul>
     *     <li>The time during which the size of the analyzed elements
     *     must remain constant is equal to the default value
     *     {@value UNCHANGED_WAITING_TIME} milliseconds</li>
     *     <li>The time between rechecks is equal to the default value
     *     {@value TIME_BEFORE_NEXT_CHECK} milliseconds</li>
     *     <li>The final size of the analysed web page necessarily
     *     differ from the starting one.</li>
     * </ul>
     *
     * @param thresholdTime max waiting time in milliseconds
     * @throws TimeoutException if waiting time exceeded thresholdTime
     */
    public void waitByDOMSize(long thresholdTime) {
        waitByDOMSize(
                thresholdTime,
                UNCHANGED_WAITING_TIME,
                TIME_BEFORE_NEXT_CHECK,
                true
        );
    }

    /**
     * A method for waiting for a web page to load by analysing its DOM
     * <ul>
     *     <li>The time between rechecks is equal to the default value
     *     {@value TIME_BEFORE_NEXT_CHECK} milliseconds</li>
     *     <li>The final size of the analysed web page necessarily
     *     differ from the starting one.</li>
     * </ul>
     *
     * @param thresholdTime        max waiting time in milliseconds
     * @param unchangedWaitingTime the time during which the analyzed
     *                             page must remain unchanged
     * @throws TimeoutException if waiting time exceeded thresholdTime
     */
    public void waitByDOMSize(
            long thresholdTime,
            long unchangedWaitingTime
    ) {
        waitByDOMSize(
                thresholdTime,
                unchangedWaitingTime,
                TIME_BEFORE_NEXT_CHECK,
                true
        );
    }

    /**
     * A method for waiting for a web page to load by analysing its DOM
     * <ul>
     *     <li>The final size of the analysed web page necessarily
     *     differ from the starting one.</li>
     * </ul>
     *
     * @param thresholdTime        max waiting time in milliseconds
     * @param unchangedWaitingTime the time during which the analyzed
     *                             page must remain unchanged
     * @param repeatedWaitingTime  time between rechecks
     * @throws TimeoutException if waiting time exceeded thresholdTime
     */
    public void waitByDOMSize(
            long thresholdTime,
            long unchangedWaitingTime,
            long repeatedWaitingTime
    ) {
        waitByDOMSize(
                thresholdTime,
                unchangedWaitingTime,
                repeatedWaitingTime,
                true
        );
    }

    /**
     * A method for waiting for a web page to load by analysing its DOM
     *
     * @param thresholdTime        max waiting time in milliseconds
     * @param unchangedWaitingTime the time during which the analyzed
     *                             page must remain unchanged
     * @param repeatedWaitingTime  time between rechecks
     * @param shouldChange         whether the final size should be
     *                             different from the original
     * @throws TimeoutException if waiting time exceeded thresholdTime
     */
    public void waitByDOMSize(
            long thresholdTime,
            long unchangedWaitingTime,
            long repeatedWaitingTime,
            boolean shouldChange
    ) {
        int countChecks = (int) (
                (unchangedWaitingTime + repeatedWaitingTime - 1)
                        / repeatedWaitingTime
        );
        countChecks = Math.max(1, countChecks);
        ArrayList<Integer> initElements = new ArrayList<>();
        for (int i = 0; i < countChecks; i++) {
            initElements.add(-i - 1);
        }
        MinMaxInterval<Integer> sizes = new MinMaxInterval<>(initElements);

        long startTime = System.currentTimeMillis();
        int previousSize = getFullPageSize();
        boolean isChanged = false;

        while (System.currentTimeMillis() - startTime < thresholdTime) {
            int currentSize = getFullPageSize();
            sizes.append(currentSize);

            if (sizes.min().equals(sizes.max())
                    && (!shouldChange || isChanged)) {
                return;
            }

            if (previousSize != currentSize) {
                isChanged = true;
                previousSize = currentSize;
            }

            try {
                Thread.sleep(repeatedWaitingTime);
            } catch (InterruptedException ignored) {
            }
        }

        throw new TimeoutException(
                "Page was not loaded in the specified time "
                        + thresholdTime + "!"
        );
    }

    /**
     * An improved method for waiting for a web page to load by
     * partially analysing its DOM.
     * <ul>
     *     <li>The maximum waiting time is equal to the default value
     *     {@value MAX_THRESHOLD_TIME} milliseconds</li>
     *     <li>The time during which the size of the analyzed elements
     *     must remain constant is equal to the default value
     *     {@value UNCHANGED_WAITING_TIME} milliseconds</li>
     *     <li>The time between rechecks is equal to the default value
     *     {@value TIME_BEFORE_NEXT_CHECK} milliseconds</li>
     *     <li>The final size of the analysed elements must necessarily
     *     differ from the starting one.</li>
     * </ul>
     *
     * @param elementsIdentifiers list of identifiers for analysis
     * @throws TimeoutException if waiting time exceeded thresholdTime
     */
    public void waitByImprovedDOMSize(
            By... elementsIdentifiers
    ) {
        waitByImprovedDOMSize(
                Arrays.stream(elementsIdentifiers).toList(),
                MAX_THRESHOLD_TIME,
                UNCHANGED_WAITING_TIME,
                TIME_BEFORE_NEXT_CHECK,
                true
        );
    }

    /**
     * An improved method for waiting for a web page to load by
     * partially analysing its DOM.
     * <ul>
     *     <li>The maximum waiting time is equal to the default value
     *     {@value MAX_THRESHOLD_TIME} milliseconds</li>
     *     <li>The time during which the size of the analyzed elements
     *     must remain constant is equal to the default value
     *     {@value UNCHANGED_WAITING_TIME} milliseconds</li>
     *     <li>The time between rechecks is equal to the default value
     *     {@value TIME_BEFORE_NEXT_CHECK} milliseconds</li>
     *     <li>The final size of the analysed elements must necessarily
     *     differ from the starting one.</li>
     * </ul>
     *
     * @param elementsIdentifiers list of identifiers for analysis
     * @throws TimeoutException if waiting time exceeded thresholdTime
     */
    public void waitByImprovedDOMSize(
            List<By> elementsIdentifiers
    ) {
        waitByImprovedDOMSize(
                elementsIdentifiers,
                MAX_THRESHOLD_TIME,
                UNCHANGED_WAITING_TIME,
                TIME_BEFORE_NEXT_CHECK,
                true
        );
    }

    /**
     * An improved method for waiting for a web page to load by
     * partially analysing its DOM.
     * <ul>
     *     <li>The time during which the size of the analyzed elements
     *     must remain constant is equal to the default value
     *     {@value UNCHANGED_WAITING_TIME} milliseconds</li>
     *     <li>The time between rechecks is equal to the default value
     *     {@value TIME_BEFORE_NEXT_CHECK} milliseconds</li>
     *     <li>The final size of the analysed elements must necessarily
     *     differ from the starting one.</li>
     * </ul>
     *
     * @param elementsIdentifiers list of identifiers for analysis
     * @param thresholdTime       max waiting time in milliseconds
     * @throws TimeoutException if waiting time exceeded thresholdTime
     */
    public void waitByImprovedDOMSize(
            List<By> elementsIdentifiers,
            long thresholdTime
    ) {
        waitByImprovedDOMSize(
                elementsIdentifiers,
                thresholdTime,
                UNCHANGED_WAITING_TIME,
                TIME_BEFORE_NEXT_CHECK,
                true
        );
    }

    /**
     * An improved method for waiting for a web page to load by
     * partially analysing its DOM.
     * <ul>
     *     <li>The time between rechecks is equal to the default value
     *     {@value TIME_BEFORE_NEXT_CHECK} milliseconds</li>
     *     <li>The final size of the analysed elements must necessarily
     *     differ from the starting one.</li>
     * </ul>
     *
     * @param elementsIdentifiers  list of identifiers for analysis
     * @param thresholdTime        max waiting time in milliseconds
     * @param unchangedWaitingTime the time during which the analyzed
     *                             objects must remain unchanged
     * @throws TimeoutException if waiting time exceeded thresholdTime
     */
    public void waitByImprovedDOMSize(
            List<By> elementsIdentifiers,
            long thresholdTime,
            long unchangedWaitingTime
    ) {
        waitByImprovedDOMSize(
                elementsIdentifiers,
                thresholdTime,
                unchangedWaitingTime,
                TIME_BEFORE_NEXT_CHECK,
                true
        );
    }

    /**
     * An improved method for waiting for a web page to load by
     * partially analysing its DOM.
     * <ul>
     *     <li>The final size of the analysed elements must necessarily
     *     differ from the starting one.</li>
     * </ul>
     *
     * @param elementsIdentifiers  list of identifiers for analysis
     * @param thresholdTime        max waiting time in milliseconds
     * @param unchangedWaitingTime the time during which the analyzed
     *                             objects must remain unchanged
     * @param repeatedWaitingTime  time between rechecks
     * @throws TimeoutException if waiting time exceeded thresholdTime
     */
    public void waitByImprovedDOMSize(
            List<By> elementsIdentifiers,
            long thresholdTime,
            long unchangedWaitingTime,
            long repeatedWaitingTime
    ) {
        waitByImprovedDOMSize(
                elementsIdentifiers,
                thresholdTime,
                unchangedWaitingTime,
                repeatedWaitingTime,
                true
        );
    }

    /**
     * An improved method for waiting for a web page to load by
     * partially analysing its DOM
     *
     * @param elementsIdentifiers  list of identifiers for analysis
     * @param thresholdTime        max waiting time in milliseconds
     * @param unchangedWaitingTime the time during which the analyzed
     *                             objects must remain unchanged
     * @param repeatedWaitingTime  time between rechecks
     * @param shouldChange         whether the final size should be
     *                             different from the original
     * @throws TimeoutException if waiting time exceeded thresholdTime
     */
    public void waitByImprovedDOMSize(
            List<By> elementsIdentifiers,
            long thresholdTime,
            long unchangedWaitingTime,
            long repeatedWaitingTime,
            boolean shouldChange
    ) {
        if (elementsIdentifiers.isEmpty()) return;

        int countChecks = (int) (
                (unchangedWaitingTime + repeatedWaitingTime - 1)
                        / repeatedWaitingTime
        );
        countChecks = Math.max(1, countChecks);
        ArrayList<Integer> initElements = new ArrayList<>();
        for (int i = 0; i < countChecks; i++) {
            initElements.add(-i - 1);
        }
        MinMaxInterval<Integer> sizes = new MinMaxInterval<>(initElements);

        long startTime = System.currentTimeMillis();
        int previousSize = getFullPageSize();
        boolean isChanged = false;

        while (System.currentTimeMillis() - startTime < thresholdTime) {
            int currentSize = elementsIdentifiers.stream()
                    .map(this::getElement)
                    .filter(Objects::nonNull)
                    .mapToInt(this::getElementSize)
                    .sum();

            sizes.append(currentSize);

            if (sizes.min().equals(sizes.max())
                    && (!shouldChange || isChanged)) {
                return;
            }

            if (previousSize != currentSize) {
                isChanged = true;
                previousSize = currentSize;
            }

            try {
                Thread.sleep(repeatedWaitingTime);
            } catch (InterruptedException ignored) {
            }
        }

        throw new
                TimeoutException(
                "Page was not loaded in the specified time "
                        + thresholdTime + "!"
        );
    }

    /**
     * Method to get current size of the web element on the page
     *
     * @param element {@link WebElement} whose size you want to know
     * @return size of the web element
     */
    private int getElementSize(WebElement element) {
        return element.getAttribute("innerHTML").length();
    }

    /**
     * Method to get current size of the web page
     *
     * @return size of the web page
     */
    private int getFullPageSize() {
        return driver.getPageSource().length();
    }

    /**
     * Method that returns a web element by identifier or null
     * if it is not on the page
     *
     * @param identifier {@link By} identifier of the web element to get
     * @return the found {@link WebElement} or null
     */
    private WebElement getElement(By identifier) {
        try {
            return driver.findElement(identifier);
        } catch (NoSuchElementException ex) {
            return null;
        }
    }

    private static final long TIME_BEFORE_NEXT_CHECK = 100;
    private static final long UNCHANGED_WAITING_TIME = 500;
    private static final long MAX_THRESHOLD_TIME = 4000;
}
