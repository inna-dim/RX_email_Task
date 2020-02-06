package ui.mailbox;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class MailinatorPage extends PageObject {
    //-------------------------- Homepage ------------------------------\\
    @FindBy(xpath = "//input[@placeholder=\"Check Any Inbox!\"]")
    public WebElementFacade HOMEPAGE_EMAIL_INPUT;

    @FindBy(xpath = "//button[contains(text(), 'Go')]")
    public WebElementFacade GO_BUTTON;

    //-------------------------- Inbox ------------------------------\\

    @FindBy(xpath = "//table//tr[contains(@id,'row')][1]/td/a")
    public WebElement  latestMessage;

    //-------------------------- Message ------------------------------\\


    @FindBy(xpath = "//iframe[@id=\"msg_body\"]")
    public WebElementFacade MESSAGE_FRAME;

    @FindBy(xpath = "//a[contains(@class,'button') and contains(text(),'Reset your password')]")
    public WebElementFacade resetPasswordButton;

}
