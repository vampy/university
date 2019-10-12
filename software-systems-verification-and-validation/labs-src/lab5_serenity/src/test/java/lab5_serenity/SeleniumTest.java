package lab5_serenity;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SeleniumTest
{
    private static ChromeDriver driver;
    private String website = "http://www.emag.ro/homepage";
    WebElement element;

    @BeforeClass
    public static void openBrowser()
    {
        System.setProperty("webdriver.chrome.driver", "./chromedriver");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public void openAndSearch(String searchterm)
    {
        driver.get(website);

        // input search term
        driver.findElementById("emg-input-autosuggest").sendKeys(searchterm);

        // press search button
        driver.findElementById("emg-category-menu-icon").click();
    }

    public WebElement getFirstResult()
    {
        WebElement productHolder = driver.findElementById("products-holder");
        Assert.assertNotNull(productHolder);
        List<WebElement> products = productHolder.findElements(By.className("product-holder-grid"));

        Assert.assertFalse(products.isEmpty());
        Assert.assertTrue(products.size() >= 1);
        WebElement first_product = products.get(0);
        Assert.assertNotNull(first_product);
        return first_product;
    }

    public String getProductString(WebElement product)
    {
        WebElement middle_container = product.findElement(By.className("middle-container"));
        Assert.assertNotNull(middle_container);

        return middle_container.findElement(By.tagName("a")).getText();
    }

    @Test
    public void valid_search()
    {
        openAndSearch("virtual reality");
        Assert.assertEquals(getProductString(getFirstResult()), "Ochelari realitate virtuala Samsung Gear VR, Frost White");
    }

    @Test
    public void valid_filter()
    {
        openAndSearch("virtual reality");
        WebElement checkbox = driver.findElementByCssSelector("ul.filters-list:nth-child(8) > li:nth-child(1) > input:nth-child(1)");
        Assert.assertNotNull(checkbox);
        checkbox.click();
        Assert.assertEquals(getProductString(getFirstResult()), "Ochelari realitate virtuala ZEISS VR ONE");
    }

    @AfterClass
    public static void closeBrowser()
    {
        driver.quit();
    }
}