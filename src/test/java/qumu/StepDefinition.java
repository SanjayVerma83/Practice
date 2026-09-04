package qumu;

import java.util.List;
import java.util.Map;

import org.testng.Assert;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class StepDefinition {

	Response response;
	List<Integer> allUserIds = new ArrayList<>();

	@Given("^I am on the home page$")
	public void iAmOnTheHomePage() {
		HomePage.homePage();
	}

	@And("^I login in with the following details$")
	public void iLoginWithTheFollowingDetails(DataTable table) {

		List<Map<String, String>> data = table.asMaps(String.class, String.class);

		String username = data.get(0).get("userName");
		String password = data.get(0).get("Password");

		LoginPage loginPage = new LoginPage();
		loginPage.login(username, password);

	}

	@And("^I add the following items to the basket$")
	public void iAddTheFollowingItemsToTheBasket(DataTable table) {

		ProductsPage productsPage = new ProductsPage();

		List<String> productList = table.asList(String.class);

		for (String product : productList) {
			productsPage.addProduct(product);
		}
	}

	@And("^I  should see (\\d+) items added to the shopping cart$")
	public void iShouldSeeItemsAddedToTheShoppingCart(int expectedCount) {

		ProductsPage productsPage = new ProductsPage();

		Assert.assertEquals(productsPage.getCartCount(), expectedCount);

	}

	@And("^I click on the shopping cart$")
	public void iClickOnTheShoppingCart() {

		ProductsPage productsPage = new ProductsPage();
		productsPage.clickShoppingCart();

	}

	@And("^I verify that the QTY count for each item should be (\\d+)$")
	public void iVerifyThatTheQTYCountForEachItemShouldBe(int expectedQty) {

		CartPage cartPage = new CartPage();
		cartPage.verifyQuantity(expectedQty);

	}

	@And("^I remove the following item:$")
	public void iRemoveTheFollowingItem(DataTable table) {

		CartPage cartPage = new CartPage();

		List<String> products = table.asList(String.class);

		for (String product : products) {
			cartPage.removeProduct(product);
		}
	}

	@And("^I click on the CHECKOUT button$")
	public void iClickOnTheCHECKOUTButton() {

		CartPage cartPage = new CartPage();
		cartPage.clickCheckout();

	}

	@And("^I type \"([^\"]*)\" for First Name$")
	public void iTypeForFirstName(String firstName) {

		CheckoutPage checkoutPage = new CheckoutPage();
		checkoutPage.enterFirstName(firstName);

	}

	@And("^I type \"([^\"]*)\" for Last Name$")
	public void iTypeForLastName(String lastName) {

		CheckoutPage checkoutPage = new CheckoutPage();
		checkoutPage.enterLastName(lastName);

	}

	@And("^I type \"([^\"]*)\" for ZIP/Postal Code$")
	public void iTypeForZIPPostalCode(String zip) {

		CheckoutPage checkoutPage = new CheckoutPage();
		checkoutPage.enterPostalCode(zip);

	}

	@When("^I click on the CONTINUE button$")
	public void iClickOnTheCONTINUEButton() {

		CheckoutPage checkoutPage = new CheckoutPage();
		checkoutPage.clickContinue();

	}

	@Then("^Item total will be equal to the total of items on the list$")
	public void itemTotalWillBeEqualToTheTotalOfItemsOnTheList() {

		CheckoutOverviewPage overviewPage = new CheckoutOverviewPage();
		overviewPage.verifyItemTotal();

	}

	@Then("^a Tax rate of (\\d+) % is applied to the total$")
	public void aTaxRateOfIsAppliedToTheTotal(int taxRate) {

		CheckoutOverviewPage overviewPage = new CheckoutOverviewPage();
		overviewPage.verifyTax(taxRate);

	}

	@Given("^I get the default list of users for on 1st page$")
	public void iGetTheDefaultListofusers() {
		response = APIBase.request().when().get("/users?page=1");

	}

	@When("I get the list of all users within every page")
	public void iGetTheListOfAllUsers() {

		int totalPages = response.jsonPath().getInt("total_pages");

		for (int page = 1; page <= totalPages; page++) {

			Response pageResponse = APIBase.request().when().get("/users?page=" + page);

			List<Integer> ids = pageResponse.jsonPath().getList("data.id");

			allUserIds.addAll(ids);
		}

	}

	@Then("I should see total users count equals the number of user ids")
	public void iShouldMatchTotalCount() {

		int expectedTotal = response.jsonPath().getInt("total");

		Assert.assertEquals(allUserIds.size(), expectedTotal);

	}

	@Given("I make a search for user (.*)")
	public void iMakeASearchForUser(String sUserID) {

		response = APIBase.request().when().get("/users/" + sUserID);

	}

	@Then("I should see the following user data")
	public void IShouldSeeFollowingUserData(DataTable dt) {

		List<Map<String, String>> data = dt.asMaps(String.class, String.class);

		String expectedFirstName = data.get(0).get("first_name");
		String expectedEmail = data.get(0).get("email");

		String actualFirstName = response.jsonPath().getString("data.first_name");
		String actualEmail = response.jsonPath().getString("data.email");

		Assert.assertEquals(actualFirstName, expectedFirstName);
		Assert.assertEquals(actualEmail, expectedEmail);
	}

	@Then("I receive error code (.*) in response")
	public void iReceiveErrorCodeInResponse(int responseCode) {

		Assert.assertEquals(response.getStatusCode(), responseCode);
	}

	@Given("I create a user with following (.*) (.*)")
	public void iCreateAUserWithFollowing(String name, String job) {

		Map<String, String> body = new HashMap<>();

		body.put("name", name);
		body.put("job", job);

		response = APIBase.request().body(body).when().post("/users");

	}

	@Then("response should contain the following data")
	public void responseShouldContainTheFollowingData(DataTable dt) {

		Assert.assertEquals(response.getStatusCode(), 201);
		
		String actualId = response.jsonPath().getString("id");
		String actualCreatedAt = response.jsonPath().getString("createdAt");

		Assert.assertNotNull(actualId);
		Assert.assertNotNull(actualCreatedAt);

		Assert.assertFalse(actualId.isEmpty());
		Assert.assertFalse(actualCreatedAt.isEmpty());
	}

	@Given("I login with the following data")
	public void iLoginWithTheFollowingData(DataTable dt) {

	    List<Map<String, String>> data = dt.asMaps(String.class, String.class);

	    Map<String, String> body = new HashMap<>();

	    body.put("email", data.get(0).get("Email"));
	    body.put("password", data.get(0).get("Password"));

	    response = APIBase.request()
	                      .body(body)
	                      .when()
	                      .post("/login");
	}

	@Given("^I wait for the user list to load$")
	public void iWaitForUserListToLoad() {

		response = APIBase.request().when().get("/users?delay=3");

	}

	@Then("I should see that every user has a unique id")
	public void iShouldSeeThatEveryUserHasAUniqueID() {

		List<Integer> ids = response.jsonPath().getList("data.id");


		HashSet<Integer> uniqueIds = new HashSet<>(ids);

		Assert.assertEquals(uniqueIds.size(), ids.size());

	}

	@Then("^I should get a response code of (\\d+)$")
	public void iShouldGetAResponseCodeOf(int responseCode) {

		Assert.assertEquals(response.getStatusCode(), responseCode);

		if (responseCode == 200) {

			String token = response.jsonPath().getString("token");

			Assert.assertNotNull(token);
			Assert.assertFalse(token.isEmpty());
		}
	}

	@And("^I should see the following response message:$")
	public void iShouldSeeTheFollowingResponseMessage(DataTable dataTable) {

		List<String> expectedMessage = dataTable.asList(String.class);

		String actualResponse = response.asPrettyString();


		Assert.assertTrue(actualResponse.contains(expectedMessage.get(0)));
	}
}
