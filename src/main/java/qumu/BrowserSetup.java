package qumu;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class BrowserSetup extends BasePage {

	public static String browser = null;

	/**
	 * Function for multi browser
	 */
	public void selectBrowser() {
		browser = LoadProp.getproperty("Browser");

		if (browser.equalsIgnoreCase("Chrome")) {
			// System.setProperty("webdriver.chrome.driver", CHROME_WIN);
			WebDriverManager.chromedriver().setup();

			ChromeOptions options = new ChromeOptions();

			options.addArguments("--guest");
			options.addArguments("--disable-save-password-bubble"); 
			options.addArguments("--disable-features=PasswordCheck,PasswordManagerOnboarding");

			Map<String, Object> prefs = new HashMap<>();
			prefs.put("credentials_enable_service", false);
			prefs.put("profile.password_manager_enabled", false);

			options.setExperimentalOption("prefs", prefs);

			options.addArguments("--user-data-dir="
			        + System.getProperty("java.io.tmpdir")
			        + "\\ChromeProfile_" + System.currentTimeMillis());

			driver = new ChromeDriver(options);
		} else if (browser.equalsIgnoreCase("edge")) {
			// System.setProperty("webdriver.edge.driver", EDGE);
			WebDriverManager.edgedriver().setup();

			EdgeOptions options = new EdgeOptions();
			options.setCapability("ms:inPrivate", true);

			driver = new EdgeDriver(options);

		} else if (browser.equalsIgnoreCase("Firefox")) {
			WebDriverManager.firefoxdriver().setup();
			// System.setProperty("webdriver.gecko.driver", FIREFOX_WIN);
			driver = new FirefoxDriver();
		} else if (browser.equalsIgnoreCase("chromeMac")) {
			// System.setProperty("webdriver.chrome.driver", CHROME_MAC);
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("chromeHeadless")) {
			// System.setProperty("webdriver.chrome.driver", CHROME_MAC);
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments("--headless");
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver(chromeOptions);
		} else if (browser.equalsIgnoreCase("api")) {

		} else {
			Assert.fail(MessageFormat.format("Wrong Browser: {0}", browser));
		}
	}
}
