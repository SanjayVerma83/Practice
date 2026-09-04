package qumu;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class CheckoutOverviewPage extends BasePage {

    private By itemPrices = By.className("inventory_item_price");
    private By itemTotal = By.className("summary_subtotal_label");
    private By tax = By.className("summary_tax_label");
    
    public void verifyItemTotal() {

        List<WebElement> prices = driver.findElements(itemPrices);

        double total = 0;

        for (WebElement price : prices) {
            String amount = price.getText().replace("$", "");
            total += Double.parseDouble(amount);
        }


        // locate the element
        String actualTotal = driver.findElement(itemTotal)
                .getText()
                .replace("Item total: $", "");

        double displayedTotal = Double.parseDouble(actualTotal);

        Assert.assertEquals(displayedTotal, total);
    }
    public void verifyTax(int taxPercentage) {

        String totalText = driver.findElement(itemTotal)
                .getText()
                .replace("Item total: $", "");

        double total = Double.parseDouble(totalText);

        double expectedTax = total * taxPercentage / 100;

        String taxText = driver.findElement(tax)
                .getText()
                .replace("Tax: $", "");

        double actualTax = Double.parseDouble(taxText);

        org.testng.Assert.assertEquals(actualTax, expectedTax, 0.01);

    }
}