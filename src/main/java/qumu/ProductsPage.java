package qumu;

import org.openqa.selenium.By;

public class ProductsPage extends BasePage {

    private By cartBadge = By.className("shopping_cart_badge");
    private By shoppingCart = By.className("shopping_cart_link");

    public void addProduct(String productName) {

    	driver.findElement(By.xpath(
                "//div[text()='" + productName + "']/../../..//button")).click();

    }

    public int getCartCount() {
    	String count = driver.findElement(cartBadge).getText();

        int cartCount = Integer.parseInt(count);

        return cartCount;
    }

    public void clickShoppingCart() {
        driver.findElement(shoppingCart).click();
    }

}