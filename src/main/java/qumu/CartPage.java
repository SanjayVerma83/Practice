package qumu;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CartPage extends BasePage {

    private By quantity = By.className("cart_quantity");
    private By checkoutButton = By.id("checkout");

    public void verifyQuantity(int expectedQty) {

        List<WebElement> qtyList = driver.findElements(quantity);

        for (WebElement qty : qtyList) {

            int actualQty = Integer.parseInt(qty.getText());

            if (actualQty != expectedQty) {
                throw new AssertionError("Expected Quantity : " + expectedQty +
                        " but found : " + actualQty);
            }
        }
    }
    public void removeProduct(String productName) {

    	driver.findElement(By.xpath(
                "//div[text()='" + productName + "']/../../..//button")).click();

    }
    public void clickCheckout() {

        driver.findElement(checkoutButton).click();

    }
}