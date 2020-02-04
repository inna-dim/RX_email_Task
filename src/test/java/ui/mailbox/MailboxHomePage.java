package ui.mailbox;

import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MailboxHomePage extends PageObject {
    @FindBy(xpath = "//input[@placeholder='Enter Inbox']")
    public WebElement emailField;

    @FindBy(xpath = "//button[contains(text(),'GO!')]")
    public WebElement goButton;
}
