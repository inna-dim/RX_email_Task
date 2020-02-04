package features.steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import features.actors.User;
import net.thucydides.core.annotations.Steps;

public class StepDefinitions {

    @Steps(shared = true)
    User user;



    @When("^I open '(.*)' site$")
    public void iOpenCmsWebsite(String website) {
        user.opensWebsite(website);
    }

    @Given("^I send a message to '(.*)' from my gmail using API$")
    public void iSendAMessageToTestingMailinatorComFromMyGmailUsingAPI(String address) throws Throwable {
        user.sendAMessage(address);
    }

    @And("^I login as '(.*)'$")

    public void iLoginAsTestingMailinatorCom(String email) throws Throwable {
        user.fillsInboxField(email);
    }

    @And("^I open last message$")
    public void iOpenLastMessage() throws Throwable {
        user.opensLatestMessage();
    }

    @Then("^I am able to extract a link from the email body$")
    public void iAmAbleToExtractALinkFromTheEmailBody() throws Throwable {
       user.isAbleToGetLinkFromAnEmail();
    }
}
